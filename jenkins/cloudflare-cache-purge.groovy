def sendSuccess() {
  if (env.SLACK_NOTIFY == 'yes') {
    slackSend channel: "#jenkins",
      color: "good",
      message: ":mostly_sunny: SUCCESS Cloudflare cache purged\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nZone: ${env.ZONE}"
  }
}

pipeline {

  agent { label 'narrative-k8s||gce' }

  options {
    skipStagesAfterUnstable()
  }
  post {
    failure {
      slackSend channel: "#jenkins",
        color: "danger",
        message: "@here FAILED Cloudflare cache purge FAILED :red_circle:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nZone: ${env.ZONE}"
    }

    unstable {
      slackSend channel: "#jenkins",
        color: "danger",
        message: "@here FAILED Cloudflare cache purge FAILED :red_circle:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nZone: ${env.ZONE}"
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
    stage('cloudflare-cache-reset') {
      steps {
        withCredentials([
          string(credentialsId: 'cloudflare_api_key', variable: 'CLOUDFLARE_API_KEY')
        ]) 
        {
          script {
            sh(
              script: '''#!/bin/bash
              # exit if anything fails
              set -e
              if [ "${ZONE}" == "narrative.org" ]; then
                ZONEID="e6fdfeab4fae1e494098f1f6f9c66e21"
              else
                echo "ZONE not equal to narrative.org. This is the only supported zone right now. Exiting..."
                exit 1
              fi
              echo "Purging Cloudflare Cache..."
              CRESULT=`curl -X POST "https://api.cloudflare.com/client/v4/zones/${ZONEID}/purge_cache" \
                -H "X-Auth-Email: bill.cawthra@narrative.network" \
                -H "X-Auth-Key: ${CLOUDFLARE_API_KEY}" \
                -H "Content-Type: application/json" \
                --data '{"purge_everything":true}'`
              echo "Result: ${CRESULT}"
            ''',
            )
          }
          sendSuccess()
        }
      }
    }
  }
}

