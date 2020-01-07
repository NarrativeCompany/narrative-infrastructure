pipeline {

  agent { label 'narrative-k8s||gce' }

  options {
    skipStagesAfterUnstable()
  }
  // post {
  //   success {
  //     slackSend channel: "#jenkins",
  //               color: "good",
  //               message: "SUCCESS\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nknown_hosts file updated at <https://console.cloud.google.com/storage/browser/narrative-devops?project=informationtechnology-205813&organizationId=149049775531>."
  //   }

  //   failure {
  //     slackSend channel: "#jenkins",
  //               color: "danger",
  //               message: "@here FAILED\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>"
  //   }

  //   unstable {
  //       slackSend channel: "#jenkins",
  //               color: "danger",
  //               message: "@here FAILED\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>"
  //   }
  // }
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
    stage('get-ssh-public-keys-from-gce') {
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
              cd ansible
              export ANSIBLE_VAULT_PASSWORD_FILE=${ANSIBLE_VAULT_OPS}
              export ANSIBLE_PRIVATE_KEY_FILE=${BOT_OPS_SSH_KEY}
              export GCE_EMAIL=jenkins@informationtechnology-205813.iam.gserviceaccount.com
              export GCE_CREDENTIALS_FILE_PATH=${GCLOUD_JENKINS_FILE}
              gcloud auth activate-service-account jenkins@informationtechnology-205813.iam.gserviceaccount.com --key-file=${GCLOUD_JENKINS_FILE}
              ansible-playbook localhost_gce_serial_scan.yml -e project=neo-narrative -e skip_removed=enabled
              ansible-playbook localhost_gce_serial_scan.yml -e project=sandbox-narrative -e skip_remove=enabled
              ansible-playbook localhost_gce_serial_scan.yml -e project=staging-narrative -e skip_removed=enabled
              ansible-playbook localhost_gce_serial_scan.yml -e project=production-narrative -e skip_removed=enabled
            ''',
            )
          }
        }
      }
    }
    stage('upload-known-hosts-file') {
      steps {
        withCredentials([
          file(credentialsId: 'gcloud-jenkins-file', variable: 'GCLOUD_JENKINS_FILE')
        ])
        {
          script {
            sh(
              script: '''#!/bin/bash
              set -ex 
              export BOTO_CONFIG=/dev/null
              gsutil cp ${HOME}/.ssh/known_hosts gs://narrative-devops/known_hosts
            ''',
            )
          }
        }
      }
    }
  }
}

