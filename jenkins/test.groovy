node {
  post {
    success {
      slackSend channel: '#devops',
                color: "good",
                message: ":narrative: :white_checkmark: ${env.branch} built."
    }

    failure {
      slackSend channel: '#devops',
                color: "danger",
                message: ":narrative: :red_circle: ${env.branch} failed."
    }

    unstable {
        slackSend channel: '#devops',
                color: "danger",
                message: ":narrative: :red_circle: ${env.branch} failed."
    }
  }

   stage('Preparation') { // for display purposes
        // Get some code from a GitHub repository
        // git 'git@github.com:NarrativeCompany/narrative-infrastructure.git'
        sh "mkdir ${HOME}.ssh && echo 'github.com ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEAq2A7hRGmdnm9tUDbO9IDSwBK6TbQa+PXYPCPy6rbTrTtw7PHkccKrpp0yVhp5HdEIcKr6pLlVDBfOLX9QUsyCOV0wzfjIJNlGEYsdlLJizHhbn2mUjvSAHQqZETYP81eFzLQNnPHt4EVVUh7VfDESU84KezmD5QlWpXLmvU31/yMf+Se8xhHTvKSCZIFImWwoG6mbUoWf9nzpIoaSjB+weqqUUmpaaasXVal72J+UX2B+2RPW3RcT0eOzQgqlJL3RKrTJvdsjE3JEAvGq3lGHSZXy28G3skua2SmVi/w4yCE6gbODqnTWlg7+wC604ydGXA8VJiS5ap43JXiUFFAaQ==' > $HOME/.ssh/known_hosts"
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
        sh "apt-get update && apt-get install -y python-pip"
   }
   stage('Build') {
      // Run the maven build
      sh "ls -alh"
   }
   stage('Results') {
        sh "echo $env"
        // echo sh(returnStdout: true, script: 'env')
   }
}
