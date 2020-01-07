def notifyStarted() {
  slackSend channel: "#jenkins",
  color: '#8cc252',
  message: ":neo: Started narrativecompany/neo-python Docker image build...\n*Job:* <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\n*Repository:* ${env.HTML_URL}\n*Branch:* ${env.GIT_BRANCH}\nJob has started... :narrative:"
}

def notifyImageUpToDate() {
  slackSend channel: "#jenkins",
  color: '#8cc252',
  message: ":neo: :sadpanda: No new commits in https://github.com/CityOfZion/neo-python.git. \n*Job:* <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\n*Repository:* ${env.HTML_URL}\n*Branch:* ${env.GIT_BRANCH}\n*neo-python Version:* ${env.NEO_PYTHON_VERSION}\nNo Docker image built."
}

def notifyImageNew() {
  slackSend (channel: "#jenkins",
  color: '#8cc252',
  message: ":neo: :white_check_mark: New commits found in https://github.com/CityOfZion/neo-python.git. \nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nRepository: ${env.HTML_URL}\nBranch: ${env.GIT_BRANCH}\n*Docker image:* <https://hub.docker.com/r/narrativecompany/neo-python/|narrativecompany/neo-python:${env.NEO_PYTHON_VERSION}> :docker:")
}

pipeline {

  agent { label 'gce' }

  options {
    skipStagesAfterUnstable()
  }
  post {
    // success {
    //   slackSend (channel: "#jenkins",
    //             color: "good",
    //             message: "SUCCESS narrativecompany/neo-python Docker image build complete. :white_check_mark:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nRepository: <${env.HTML_URL}/tree/${env.GIT_BRANCH}>\nBranch: ${env.GIT_BRANCH}\nDocker image: ${DOCKER_IMAGE}\nSuccessfully built."
    // }

    failure {
      slackSend channel: "#jenkins",
                color: "danger",
                message: "@here FAILED - narrativecompany/neo-python Docker image build FAILED. :red_circle:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nRepository: <${env.HTML_URL}/tree/${env.GIT_BRANCH}>\nBranch: ${env.GIT_BRANCH}\nDocker image: ${DOCKER_IMAGE}\nFailed to build."
    }

    unstable {
        slackSend channel: "#jenkins",
                color: "danger",
                message: "@here FAILED - narrativecompany/neo-python Docker image build FAILED. :red_circle:\nJob: <${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}>\nRepository: <${env.HTML_URL}/tree/${env.GIT_BRANCH}>\nBranch: ${env.GIT_BRANCH}\nDocker image: ${DOCKER_IMAGE}\nFailed to build."
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
    stage('pull-and-check') {
      steps {
        withCredentials([
          [$class: 'UsernamePasswordMultiBinding', credentialsId: 'jenkins-docker-hub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD'],
          file(credentialsId: 'gcloud-jenkins-file', variable: 'GCLOUD_JENKINS_FILE')
        ])
          {
          script {
            NEO_PYTHON_IMAGE = sh(
              script: '''#!/bin/bash
              set -ex 
              cd docker/neo-python
              rm -rf ./neo-python
              git clone https://github.com/CityOfZion/neo-python.git > /dev/null 2>&1
              cd neo-python
              GIT_SHA=$(git show-ref -s refs/heads/master)
              GIT_TAG=$(git tag | tail -n 1)
              cd ../
              docker pull narrativecompany/neo-python:latest > /dev/null 2>&1
              DOCKER_GIT_TAG_LABEL=$(docker inspect narrativecompany/neo-python:latest | jq -r '.[0].Config.Labels.GIT_TAG')
              if [ "${GIT_TAG}" == "${DOCKER_GIT_TAG_LABEL}" ]; then
                echo "notifyImageUpToDate"
              else
                echo "notifyImageNew"
              fi
            ''',
            returnStdout: true
            ).trim()
          }
          script {
            NEO_PYTHON_VERSION = sh(
              script: '''#!/bin/bash
              cd docker/neo-python/neo-python
              git tag | tail -n 1
            ''',
            returnStdout: true
            ).trim()
          }
        }
      }
    }
    stage('notify-about-image') {
      steps {
        script {
          if (NEO_PYTHON_IMAGE == 'notifyImageUpToDate') {
            notifyImageUpToDate()
          }
          else if (NEO_PYTHON_IMAGE == 'notifyImageNew') {
            withCredentials([
              [$class: 'UsernamePasswordMultiBinding', credentialsId: 'jenkins-docker-hub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD'],
              file(credentialsId: 'gcloud-jenkins-file', variable: 'GCLOUD_JENKINS_FILE')
            ])
            {
              script {
                sh(
                  script: '''#!/bin/bash
                  set -ex 
                  cd docker/neo-python/neo-python
                  echo "Get latest tag..."
                  GIT_TAG=$(git tag | tail -n 1)
                  git checkout tags/${GIT_TAG} -b ${GIT_TAG} || git checkout ${GIT_TAG}
                  GIT_SHA_TAG=$(git show-ref -s refs/tags/${GIT_TAG})
                  echo "${GIT_SHA_TAG}"
                  cd ..
                  echo "Building neo-python Docker image..."
                  docker build -t narrativecompany/neo-python:${GIT_TAG} --build-arg GIT_SHA_TAG=${GIT_SHA_TAG} --build-arg GIT_TAG=${GIT_TAG} .
                  docker tag narrativecompany/neo-python:${GIT_TAG} narrativecompany/neo-python:latest
                  docker login -u "${USERNAME}" -p "${PASSWORD}"
                  docker push narrativecompany/neo-python:${GIT_TAG}
                  docker push narrativecompany/neo-python:latest
                  docker logout
                  rm -rf neo-python
                ''',
                )
              }
            }
            notifyImageNew()
          }
          else {
            withCredentials([
              [$class: 'UsernamePasswordMultiBinding', credentialsId: 'jenkins-docker-hub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD'],
              file(credentialsId: 'gcloud-jenkins-file', variable: 'GCLOUD_JENKINS_FILE')
            ])
            {
              script {
                sh(
                  script: '''#!/bin/bash
                  set -ex 
                  echo "Something went wrong: ${NEO_PYTHON_IMAGE}"
                ''',
                )
              }
            }
          }
        }
      }
    }
  }
}

