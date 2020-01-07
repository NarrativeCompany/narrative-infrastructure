pipelineJob("production-narrative-platform-start-pods-with-deploy") {
  properties {
    disableConcurrentBuilds()
  }
  description("Starts up the narrative pods using the deployment process (so it will deploy the latest code). Brings the system back up from the stop-pods job.")
  keepDependencies(false)
  parameters {
    stringParam("GIT_BRANCH_ORIGIN", "origin/master", "The Git branch to use.")
    stringParam("COMMIT_SHA", "auto", "If a specific Git commit SHA should be used, enter it here. Otherwise it will default to the HEAD of the Git branch.")
    choiceParam('ENVIRONMENT', ['production'], 'Only deploys to production.')
    stringParam("DOCKER_TAG_DEPLOY", "", "Optional. The Docker tag to deploy for narrative-core and narrative-web-front-end. Leave blank if unsure. dev-latest and master-latest are also fairly safe.")
    stringParam("REP_DOCKER_TAG_DEPLOY", "", "Optional. The Docker tag to deploy for narrative-reputation. Leave blank if unsure. dev-latest and master-latest are also fairly safe.")
    stringParam("SLACK_USER", "Manual_Jenkins", "The Slack user (in the format '<@UID>').")
  }
  definition {
    cpsScm {
      scm {
        git {
          remote {
            github("NarrativeCompany/narrative", "ssh")
            credentials("jenkins-narrative-platform")
          }
          branch("\$GIT_BRANCH_ORIGIN")
        }
      }
      scriptPath("jenkins/start-pods-with-deploy.groovy")
    }
  }
  disabled(false)
}
