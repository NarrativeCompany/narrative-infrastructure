pipeline {

  agent { label 'narrative-k8s' }

  options {
    skipStagesAfterUnstable()
  }
  post {
    success {
      slackSend channel: "#jenkins",
                color: "good",
                message: ":neo: SUCCESS NEO database tar file created :white_check_mark:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nRepository: <${env.HTML_URL}/tree/${env.GIT_BRANCH}>\nBranch: ${env.GIT_BRANCH}\nSite: https://neo-python-chain.narrative.org/index.html"
    }

    failure {
      slackSend channel: "#jenkins",
                color: "danger",
                message: "@here FAILED - NEO database tar file not created :red_circle:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nRepository: <${env.HTML_URL}/tree/${env.GIT_BRANCH}>\nBranch: ${env.GIT_BRANCH}\nSite: https://neo-python-chain.narrative.org/index.html NOT built."
    }

    unstable {
        slackSend channel: "#jenkins",
                color: "danger",
                message: "@here FAILED - NEO database tar file not created :red_circle:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nRepository: <${env.HTML_URL}/tree/${env.GIT_BRANCH}>\nBranch: ${env.GIT_BRANCH}\nSite: https://neo-python-chain.narrative.org/index.html NOT built."
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
    stage('alertmanager-silence') {
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
              cd ansible/
              export ANSIBLE_VAULT_PASSWORD_FILE=${ANSIBLE_VAULT_OPS}
              export ANSIBLE_PRIVATE_KEY_FILE=${BOT_OPS_SSH_KEY}
              export GCE_EMAIL=jenkins@informationtechnology-205813.iam.gserviceaccount.com
              export GCE_CREDENTIALS_FILE_PATH=${GCLOUD_JENKINS_FILE}
              export GCE_PROJECT=neo-narrative
              ansible-playbook alertmanager-silence.yml -e alertmanager_silence_label_name=gce_instance_name -e alertmanager_silence_label_value=neo-database-01
            ''',
            )
          }
        }
      }
    }
    stage('neo-database-upload') {
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
              cd ansible/
              export ANSIBLE_VAULT_PASSWORD_FILE=${ANSIBLE_VAULT_OPS}
              export ANSIBLE_PRIVATE_KEY_FILE=${BOT_OPS_SSH_KEY}
              export GCE_EMAIL=jenkins@informationtechnology-205813.iam.gserviceaccount.com
              export GCE_CREDENTIALS_FILE_PATH=${GCLOUD_JENKINS_FILE}
              export GCE_PROJECT=neo-narrative
              ansible-playbook -i inventory/neo neo-database-upload.yml
              ansible-playbook -i inventory/neo neo-database-testnet-upload.yml
            ''',
            )
          }
        }
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
              set -ex 
              cd ansible/
              export ANSIBLE_VAULT_PASSWORD_FILE=${ANSIBLE_VAULT_OPS}
              export ANSIBLE_PRIVATE_KEY_FILE=${BOT_OPS_SSH_KEY}
              export GCE_EMAIL=jenkins@informationtechnology-205813.iam.gserviceaccount.com
              export GCE_CREDENTIALS_FILE_PATH=${GCLOUD_JENKINS_FILE}
              export GCE_PROJECT=neo-narrative
              ansible-playbook alertmanager-silence-expire.yml -e alertmanager_silence_label_name=gce_instance_name -e alertmanager_silence_label_value=neo-database-01
            ''',
            )
          }
        }
      }
    }
  }
}

