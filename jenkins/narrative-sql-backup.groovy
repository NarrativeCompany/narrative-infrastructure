pipeline {

  agent { label 'narrative-k8s' }

  options {
    skipStagesAfterUnstable()
  }
  post {
    success {
      slackSend channel: "#jenkins",
                color: "good",
                message: "SUCCESS Google Cloud SQL ${env.BACKUP_DESCRIPTION} backup for ${env.PROJECT}. :white_check_mark:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nRepository: <${env.HTML_URL}/tree/${env.GIT_BRANCH}>\nBranch: ${env.GIT_BRANCH}"
    }

    failure {
      slackSend channel: "#jenkins",
                color: "danger",
                message: "@here FAILED - Google Cloud SQL ${env.BACKUP_DESCRIPTION} backup for ${env.PROJECT}. :red_circle:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nRepository: <${env.HTML_URL}/tree/${env.GIT_BRANCH}>\nBranch: ${env.GIT_BRANCH}."
    }

    unstable {
      slackSend channel: "#jenkins",
                color: "danger",
                message: "@here FAILED - Google Cloud SQL ${env.BACKUP_DESCRIPTION} backup for ${env.PROJECT}. :red_circle:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nRepository: <${env.HTML_URL}/tree/${env.GIT_BRANCH}>\nBranch: ${env.GIT_BRANCH}."
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
    stage('code-checkout') { // for display purposes
      steps {
         // notifyStarted()
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
    stage('google-sql-backup') {
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
              function backup () {
                local PROJECT=$1
                if [ "${PROJECT}" == "sandbox-narrative" ]; then
                  INSTANCE="dev-master"
                elif [ "${PROJECT}" == "staging-narrative" ]; then
                  INSTANCE="staging-master"
                elif [ "${PROJECT}" == "production-narrative" ]; then
                  INSTANCE="production-master"
                fi
                ansible-playbook google-sql-backup-create.yml -e project=${PROJECT} -e instance=${INSTANCE} -e description=${BACKUP_DESCRIPTION}
                ansible-playbook google-sql-backup-delete.yml -e project=${PROJECT} -e instance=${INSTANCE} -e description=${BACKUP_DESCRIPTION} -e count=${BACKUP_COUNT}
              }
              cd ansible/
              export ANSIBLE_VAULT_PASSWORD_FILE=${ANSIBLE_VAULT_OPS}
              export GCE_EMAIL=jenkins@informationtechnology-205813.iam.gserviceaccount.com
              export GCE_CREDENTIALS_FILE_PATH=${GCLOUD_JENKINS_FILE}
              export GCE_PROJECT=${PROJECT}
              gcloud auth activate-service-account jenkins@informationtechnology-205813.iam.gserviceaccount.com --key-file=${GCLOUD_JENKINS_FILE}
              mkdir -p ${HOME}/.ssh
              export BOTO_CONFIG=/dev/null
              gsutil cp gs://narrative-devops/known_hosts ${HOME}/.ssh/known_hosts

              if [ "${PROJECT}" == "all" ]; then
                for list in sandbox-narrative staging-narrative production-narrative; do
                  backup ${list}
                done
              else
                backup ${PROJECT}
              fi
            ''',
            )
          }
        }
      }
    }
  }
}

