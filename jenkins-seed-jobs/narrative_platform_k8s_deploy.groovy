pipelineJob("narrative-platform-k8s-deploy") {
  properties {
    disableConcurrentBuilds()
  }
  description("Deploys the narrative-platform Docker images for narrative-core and narrative-web-front-end to Kubernetes. This job should rarely be run manually, use the narrative-platform-deploy job instead.")
  keepDependencies(false)
  parameters {
    stringParam("GIT_BRANCH_ORIGIN", "origin/dev", "The Git branch to use. Note that if DOCKER_TAG_DEPLOY is not defined, it will use the Git branch to determine what Docker tag to use.")
    stringParam("COMMIT_SHA", "auto", "If a specific Git commit SHA should be used, enter it here. Otherwise it will default to the HEAD of the Git branch.")
    choiceParam("ENVIRONMENT", ["auto", "dev", "staging"], "The Narrative environment to deploy to. Defaults to auto.")
    stringParam("DOCKER_TAG_DEPLOY", "", "Optional. The Docker tag to deploy for narrative-core and narrative-web-front-end. Leave blank if unsure. dev-latest and master-latest are also fairly safe.")
    stringParam("REP_DOCKER_TAG_DEPLOY", "", "Optional. The Docker tag to deploy for narrative-reputation. Leave blank if unsure. dev-latest and master-latest are also fairly safe.")
    stringParam("PARENT_JOB", "self", "Used to pass what parent job started this job.")
    stringParam("SLACK_USER", "Manual_Jenkins", "The Slack user (in the format '<@UID>', if auto-triggered by Github). Enter an identifiable name.")
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
      scriptPath("jenkins/k8s-deploy.groovy")
    }
  }
  disabled(false)
}
