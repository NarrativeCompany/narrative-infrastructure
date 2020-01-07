pipelineJob("production-narrative-platform-downtime-deploy") {
  properties {
    disableConcurrentBuilds()
  }
  description("Runs a downtime deployment against the production environment. This will put up a system-update page placeholder, shutdown the Narrative site, run a narrative-core container (to update the database if needed), and once updated, will put the Narrative platform back into place.")
  keepDependencies(false)
  parameters {
    stringParam("GIT_BRANCH_ORIGIN", "origin/master", "The Git branch to use.")
    stringParam("COMMIT_SHA", "auto", "If a specific Git commit SHA should be used, enter it here. Otherwise it will default to the HEAD of the Git branch.")
    choiceParam('ENVIRONMENT', ['production'], 'The environment to deploy to.')
    stringParam("DOCKER_TAG_DEPLOY", "", "Optional. The Docker tag to deploy for narrative-core and narrative-web-front-end. Leave blank if unsure. master-latest is fairly safe.")
    stringParam("REP_DOCKER_TAG_DEPLOY", "", "Optional. The Docker tag to deploy for narrative-reputation. Leave blank if unsure. master-latest is fairly safe.")
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
      scriptPath("jenkins/downtime-deploy.groovy")
    }
  }
  disabled(false)
}
