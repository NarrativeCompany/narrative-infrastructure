pipeline {

  agent { label 'narrative-k8s' }

  options {
    skipStagesAfterUnstable()
  }
  post {
    success {
      slackSend channel: "#jenkins",
                color: "good",
                message: "SUCCESS\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nNarrative redemption VM: ${env.STATUS} complete."
    }

    failure {
      slackSend channel: "#jenkins",
                color: "danger",
                message: "@here FAILED\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>"
    }

    unstable {
        slackSend channel: "#jenkins",
                color: "danger",
                message: "@here FAILED\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>"
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
    stage('package-install') {
      steps {
        withCredentials([
          file(credentialsId: 'bot_ops_ssh_key', variable: 'BOT_OPS_SSH_KEY')
        ])
        {
          script {
            sh(
              script: '''#!/bin/bash
              set -ex
              apt-get update && apt-get install -y ed
              wget https://releases.hashicorp.com/terraform/0.11.11/terraform_0.11.11_linux_amd64.zip
              unzip terraform_0.11.11_linux_amd64.zip
              install terraform /usr/local/bin/
            ''',
            )
          }
        }
      }
    }
    stage('narrative-redemption-vm') {
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
              set -ex 
              export ANSIBLE_VAULT_PASSWORD_FILE=${ANSIBLE_VAULT_OPS}
              export ANSIBLE_PRIVATE_KEY_FILE=${BOT_OPS_SSH_KEY}
              export ANSIBLE_INVALID_TASK_ATTRIBUTE_FAILED=False
              export GCE_EMAIL=jenkins@informationtechnology-205813.iam.gserviceaccount.com
              export GCE_CREDENTIALS_FILE_PATH=${GCLOUD_JENKINS_FILE}
              gcloud auth activate-service-account jenkins@informationtechnology-205813.iam.gserviceaccount.com --key-file=${GCLOUD_JENKINS_FILE}
              export GOOGLE_APPLICATION_CREDENTIALS=${GCLOUD_JENKINS_FILE}
              export GOOGLE_PROJECT=neo-narrative

              # Set gcloud project to neo-narrative
              gcloud config set project neo-narrative

              if [ ${STATUS} = 'create' ]; then

                # Create narrative-redemption-01 VM in neo-narrative
                cd environments/neo-narrative/terraform/
                terraform init
                terraform apply -auto-approve -target=google_compute_instance.narrative-redemption-01

                # Set option variable
                if [ ${BLOCKCHAIN} = 'TestNet']; then
                  OPTION='-t'
                elif [ ${BLOCKCHAIN} = 'MainNet']; then
                  OPTION='-m'
                fi

                # Deploy neo-python to narrative-redemption-01 VM
                cd ../../../ansible/
                ansible-playbook localhost_gce_serial_scan.yml -e project=neo-narrative
                ansible-playbook -i inventory/neo narrative-redemption-deploy.yml -e option="${OPTION}"

              elif [ ${STATUS} = 'destroy' ]; then

                # Destroy narrative-redemption-01 VM in neo-narrative
                cd environments/neo-narrative/terraform/
                terraform init
                terraform destroy -auto-approve -target=google_compute_instance.narrative-redemption-01

              fi
            ''',
            )
          }
        }
      }
    }
  }
}
