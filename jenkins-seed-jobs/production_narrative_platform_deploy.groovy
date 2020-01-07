pipelineJob("production-narrative-platform-deploy") {
  description("Deploys a specified narrative-core and narrative-web-front-end Docker image to production. Deployment will also update the Solr configset (such as schema and stopwords).")
  keepDependencies(false)
  parameters {
    stringParam("GIT_BRANCH_ORIGIN", "origin/master", "The Git branch to use.")
    stringParam("COMMIT_SHA", "auto", "If a specific Git commit SHA should be used, enter it here. Otherwise it will default to the HEAD of the Git branch.")
    choiceParam("ENVIRONMENT", ["production"], "The production environment to deploy to. Defaults to production.")
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
      scriptPath("jenkins/manual-deploy.groovy")
    }
  }
  disabled(false)
}
