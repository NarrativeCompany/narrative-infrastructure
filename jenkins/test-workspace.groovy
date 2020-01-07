pipeline {

  agent { label 'k8s' }

  options {
    skipStagesAfterUnstable()
  }
  post {
    success {
      slackSend channel: '#devops',
                color: "good",
                message: "<${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}> for repository ${env.GIT_URL} branch ${env.GIT_BRANCH} successful."
    }

    failure {
      slackSend channel: '#devops',
                color: "danger",
                message: "<${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}> for repository ${env.GIT_URL} branch ${env.GIT_BRANCH} FAILED."
    }

    unstable {
        slackSend channel: '#devops',
                color: "danger",
                message: "<${env.BUILD_URL}|${env.JOB_BASE_NAME} ${env.BUILD_DISPLAY_NAME}> for repository ${env.GIT_URL} branch ${env.GIT_BRANCH} FAILED."
    }
  }

  environment {
      GIT_BRANCH = sh(
          script: '''
            echo $REF | awk -F'/' '{ print $NF }'
          ''',
          returnStdout: true
          ).trim()
  }

  stages {
    stage('Preparation') { // for display purposes
      steps {
         // Get some code from a GitHub repository
         // git 'git@github.com:NarrativeCompany/narrative-infrastructure.git'
         sh "mkdir -p ${HOME}.ssh && echo 'github.com ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEAq2A7hRGmdnm9tUDbO9IDSwBK6TbQa+                                                                                              PXYPCPy6rbTrTtw7PHkccKrpp0yVhp5HdEIcKr6pLlVDBfOLX9QUsyCOV0wzfjIJNlGEYsdlLJizHhbn2mUjvSAHQqZETYP81eFzLQNnPHt4EVVUh7VfDESU84KezmD5QlWpXLmvU31/yMf+Se8xhHTvKSCZIFImWwoG6mbUoWf9nzpIoaSjB+weqqUUmpaaasXVal72J+UX2B+       2RPW3RcT0eOzQgqlJL3RKrTJvdsjE3JEAvGq3lGHSZXy28G3skua2SmVi/w4yCE6gbODqnTWlg7+wC604ydGXA8VJiS5ap43JXiUFFAaQ==' > $HOME/.ssh/known_hosts"
         checkout scm: [
             $class: 'GitSCM',
             branches: [[name: "origin/${env.BRANCH}"]],
             doGenerateSubmoduleConfigurations: false,
             submoduleCfg: [],
             userRemoteConfigs: [[credentialsId: '24489ad6-f93a-4682-8ac3-cdeb6de18c71',
                                  url: 'git@github.com:NarrativeCompany/narrative-infrastructure.git']]
         ]
         // Get the Maven tool.
         // ** NOTE: This 'M3' Maven tool must be configured
         // **       in the global configuration.
         // sh "apt-get update && apt-get install -y python-pip"
      }
    }
    stage('Build') {
      steps {
        sh "ls -alh"
      }
    }
    stage('Results') {
      steps {
         sh "echo ${REF}"
         sh "echo 'printing environment...'"
         sh "env"
      }
    }
  }
}
