pipelineJob("narrative-platform-deploy") {
  description("Deploys a specified narrative-core and narrative-web-front-end Docker image to either dev or staging. Deployment will also update the Solr configset (such as schema and stopwords).")
  keepDependencies(false)
  parameters {
    stringParam("GIT_BRANCH_ORIGIN", "origin/dev", "The Git branch to use. Note that if DOCKER_TAG_DEPLOY is not defined, it will use the Git branch to determine what Docker tag to use.")
    stringParam("COMMIT_SHA", "auto", "If a specific Git commit SHA should be used, enter it here. Otherwise it will default to the HEAD of the Git branch.")
    choiceParam("ENVIRONMENT", ["dev", "staging"], "The Narrative environment to deploy to. Defaults to dev.")
    stringParam("DOCKER_TAG_DEPLOY", "", "Optional. The Docker tag to deploy for narrative-core and narrative-web-front-end. Leave blank if unsure. dev-latest and master-latest are also fairly safe.")
    stringParam("REP_DOCKER_TAG_DEPLOY", "", "Optional. The Docker tag to deploy for narrative-reputation. Leave blank if unsure. dev-latest and master-latest are also fairly safe.")
    stringParam("SLACK_USER", "Manual_Jenkins", "The Slack user (in the format '<@UID>', if possible, otherwise just type your name).")
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
