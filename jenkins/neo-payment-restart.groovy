pipeline {

  agent { label 'narrative-k8s||gce' }

  options {
    skipStagesAfterUnstable()
  }
  post {
    success {
      slackSend channel: "#jenkins",
                color: "good",
                message: ":recycle: SUCCESS neo-payment service restarted:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nCluster: ${env.CLUSTER}\nGCE instance: ${env.GCE_INSTANCE_NAME}"
    }

    failure {
      slackSend channel: "#jenkins",
                color: "danger",
                message: "@here FAILED neo-payment NOT restarted:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nCluster: ${env.CLUSTER}\nGCE instance: ${env.GCE_INSTANCE_NAME}"
    }

    unstable {
        slackSend channel: "#jenkins",
                color: "danger",
                message: "@here FAILED neo-payment NOT restarted:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nCluster: ${env.CLUSTER}\nGCE instance: ${env.GCE_INSTANCE_NAME}"
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
    stage('get-ssh-fingerprint') {
      steps {
        withCredentials([
          file(credentialsId: 'gcloud-jenkins-file', variable: 'GCLOUD_JENKINS_FILE'),
          file(credentialsId: 'ansible_vault_ops', variable: 'ANSIBLE_VAULT_OPS')
        ])
        {
          script {
            sh(
              script: '''#!/bin/bash
              set -ex 
              cd ansible/
              export ANSIBLE_VAULT_PASSWORD_FILE=${ANSIBLE_VAULT_OPS}
              export GCE_EMAIL=jenkins@informationtechnology-205813.iam.gserviceaccount.com
              export GCE_CREDENTIALS_FILE_PATH=${GCLOUD_JENKINS_FILE}
              export GCE_PROJECT=neo-narrative
              gcloud auth activate-service-account jenkins@informationtechnology-205813.iam.gserviceaccount.com --key-file=${GCLOUD_JENKINS_FILE}
              mkdir -p ${HOME}/.ssh
              export BOTO_CONFIG=/dev/null
              gsutil cp gs://narrative-devops/known_hosts ${HOME}/.ssh/known_hosts
            ''',
            )
          }
        }
      }
    }
    stage('neo-payment-restart') {
      steps {
        withCredentials([
          file(credentialsId: 'gcloud-jenkins-file', variable: 'GCLOUD_JENKINS_FILE'),
          file(credentialsId: 'ansible_vault_ops', variable: 'ANSIBLE_VAULT_OPS'),
          file(credentialsId: 'prod_bot_ops_ssh_key', variable: 'PROD_BOT_OPS_SSH_KEY'),
          file(credentialsId: 'bot_ops_ssh_key', variable: 'BOT_OPS_SSH_KEY')
        ])
        {
          script {
            sh(
              script: '''#!/bin/bash
              set -ex 
              cd ansible/
              export ANSIBLE_VAULT_PASSWORD_FILE=${ANSIBLE_VAULT_OPS}
              export ANSIBLE_PRIVATE_KEY_FILE=${BOT_OPS_SSH_KEY}
              export GCE_EMAIL=jenkins@informationtechnology-205813.iam.gserviceaccount.com
              export GCE_CREDENTIALS_FILE_PATH=${GCLOUD_JENKINS_FILE}
              INVENTORY=$(echo "${CLUSTER}" | awk -F'-' '{ print $2 }')
              if [ "${CLUSTER}" == "neo" ] ;then
                ansible-playbook -i inventory/neo neo-python-restart.yml -e host=${GCE_INSTANCE_NAME} -e service=neo-python
              elif [ "${CLUSTER}" == "narrative-production-cluster" ]; then
                export ANSIBLE_PRIVATE_KEY_FILE=${PROD_BOT_OPS_SSH_KEY}
                ansible-playbook -i inventory/${INVENTORY} neo-python-restart.yml -e host=tag_neo-pay -e service=neo-payment --limit ${GCE_INSTANCE_NAME}
              else
                ansible-playbook -i inventory/${INVENTORY} neo-python-restart.yml -e host=tag_neo-pay -e service=neo-payment --limit ${GCE_INSTANCE_NAME}
              fi
            ''',
            )
          }
        }
      }
    }
  }
}

