pipelineJob("production-narrative-platform-stop-pods") {
  properties {
    disableConcurrentBuilds()
  }
  description("Stops all narrative pods and puts the system update page in place.. This will create the system update ingresses. It will stop the narrative-core, narrative-web-front-end, and narrative-reputation pods. Finally it will flush redis.")
  keepDependencies(false)
  parameters {
    stringParam("GIT_BRANCH_ORIGIN", "origin/master", "The Git branch to use.")
    stringParam("COMMIT_SHA", "auto", "If a specific Git commit SHA should be used, enter it here. Otherwise it will default to the HEAD of the Git branch.")
    choiceParam('ENVIRONMENT', ['production'], 'The environment to deploy to. This job ONLY runs against production.')
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
      scriptPath("jenkins/stop-pods.groovy")
    }
  }
  disabled(false)
}
