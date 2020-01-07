pipeline {

  agent { label 'narrative-k8s' }

  options {
    skipStagesAfterUnstable()
  }
  post {
    success {
      slackSend channel: "#jenkins",
                color: "good",
                message: "SUCCESS alert silence expired:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nLabel: ${env.SILENCE_LABEL}\nValue: ${env.SILENCE_VALUE}"
    }

    failure {
      slackSend channel: "#jenkins",
                color: "danger",
                message: "@here FAILED alert silence NOT expired:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nLabel: ${env.SILENCE_LABEL}\nValue: ${env.SILENCE_VALUE}"
    }

    unstable {
        slackSend channel: "#jenkins",
                color: "danger",
                message: "@here FAILED alert silence NOT expired:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nLabel: ${env.SILENCE_LABEL}\nValue: ${env.SILENCE_VALUE}"
    }
  }
  environment {
      GIT_BRANCH = sh(
          script: '''
            echo $GIT_BRANCH_ORIGIN | awk -F'/' '{ print $NF }'
          ''',
          returnStdout: true
          ).trim()
  }

  stages {
    stage('code-checkout') {
      steps {
         checkout scm: [
             $class: 'GitSCM',
             branches: [[name: "${env.GIT_BRANCH_ORIGIN}"]],
             doGenerateSubmoduleConfigurations: false,
             submoduleCfg: [],
             userRemoteConfigs: [[credentialsId: '24489ad6-f93a-4682-8ac3-cdeb6de18c71',
                                  url: 'git@github.com:NarrativeCompany/narrative-infrastructure.git']]
         ]
      }
    }
    stage('alertmanager-silence-expire') {
      steps {
        withCredentials([
          file(credentialsId: 'gcloud-jenkins-file', variable: 'GCLOUD_JENKINS_FILE'),
          file(credentialsId: 'ansible_vault_ops', variable: 'ANSIBLE_VAULT_OPS'),
          file(credentialsId: 'bot_ops_ssh_key', variable: 'BOT_OPS_SSH_KEY')
        ])
        {
          script {
            sh(
              script: '''#!/bin/bash
              set -e
              cd ansible
              export ANSIBLE_VAULT_PASSWORD_FILE=${ANSIBLE_VAULT_OPS}
              export ANSIBLE_PRIVATE_KEY_FILE=${BOT_OPS_SSH_KEY}
              export GCE_EMAIL=jenkins@informationtechnology-205813.iam.gserviceaccount.com
              export GCE_CREDENTIALS_FILE_PATH=${GCLOUD_JENKINS_FILE}
              export GCE_PROJECT=${PROJECT}
              echo "Activating the service account..."
              gcloud auth activate-service-account jenkins@informationtechnology-205813.iam.gserviceaccount.com --key-file=${GCLOUD_JENKINS_FILE}
              NAMESPACE="monitoring"
              # Configure Kubernetes based on the Alertmanager to silence
              if [ "${ALERTMANAGER}" == "primary" ]; then
                gcloud beta container clusters get-credentials it-cluster --region us-east1 --project informationtechnology-205813
                TOKEN=$(gcloud auth print-access-token --account=jenkins@informationtechnology-205813.iam.gserviceaccount.com)
                # prometheus primary is in a different namespace
                NAMESPACE="it"
              elif [ "${ALERTMANAGER}" == "it" ]; then
                gcloud beta container clusters get-credentials it-cluster --region us-east1 --project informationtechnology-205813
                TOKEN=$(gcloud auth print-access-token --account=jenkins@informationtechnology-205813.iam.gserviceaccount.com)
              elif [ "${ALERTMANAGER}" == "sandbox" ]; then
                # sandbox environment
                gcloud beta container clusters get-credentials sandbox-cluster --region us-east1 --project sandbox-narrative
                TOKEN=$(gcloud auth print-access-token --account=jenkins@informationtechnology-205813.iam.gserviceaccount.com)
              elif [ "${ALERTMANAGER}" == "staging" ]; then
                # staging environment
                gcloud beta container clusters get-credentials staging-cluster --region us-east1 --project staging-narrative
                TOKEN=$(gcloud auth print-access-token --account=jenkins@informationtechnology-205813.iam.gserviceaccount.com)
              elif [ "${ALERTMANAGER}" == "production" ]; then
                # production is in central...
                gcloud beta container clusters get-credentials ${PROJECT}-cluster --region us-central1 --project ${PROJECT}-narrative
                TOKEN=$(gcloud auth print-access-token --account=jenkins@informationtechnology-205813.iam.gserviceaccount.com)
              else
                # something went wrong...
                echo "Something went wrong... Exiting..."
                exit 1
              fi

              # Configure port forwarding to alertmanager....
              echo "Setting up port forward..."
              APOD=$(kubectl -n ${NAMESPACE} get pods | grep alertmanager | awk '{ print $1 }')
              echo "Alertmanager pod: ${APOD}"
              PORT=$(( ( RANDOM % 100 )  + 9093 ))
              while true &>/dev/null </dev/tcp/127.0.0.1/${PORT}; do
                echo "Port ${PORT} is in use. Incrementing and testing again..."
                PORT=$((PORT+1))
                sleep 1
              done 
              echo "Using port ${PORT}"
              echo "Running port forward in the background..."
              kubectl -n ${NAMESPACE} port-forward ${APOD} ${PORT}:9093 &
              FORWARDER=$!
              ansible-playbook alertmanager-silence-expire.yml -e alertmanager_silence_label_name=${SILENCE_LABEL} -e alertmanager_silence_label_value=${SILENCE_VALUE} -e alertmanager_url=localhost:${PORT}
              kill ${FORWARDER}
            ''',
            )
          }
        }
      }
    }
  }
}

