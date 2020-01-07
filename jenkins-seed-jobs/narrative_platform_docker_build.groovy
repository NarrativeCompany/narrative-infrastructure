pipelineJob("narrative-platform-docker-build") {
  properties {
    disableConcurrentBuilds()
  }
  description("Builds the Docker images narrative-core and narrative-web-front-end for the narrative-platform project.")
  keepDependencies(false)
  parameters {
    stringParam("GIT_BRANCH_ORIGIN", "origin/dev", "The Git branch to use.")
		stringParam("COMMIT_SHA", "auto", "If a specific Git commit SHA should be used, enter it here. Otherwise it will default to the HEAD of the Git branch.")
		stringParam("PARENT_JOB", "self", "Used to pass what parent job started this job.")
    stringParam("SLACK_USER", "Manual_Jenkins", "The Slack user (in the format '<@UID>').")
  }
  triggers {
    GenericTrigger {
      genericVariables {
        genericVariable {
          expressionType("JSONPath")
          key("GIT_BRANCH_ORIGIN")
          value("\$.branch_origin")
        }
        genericVariable {
          expressionType("JSONPath")
          key("HTML_URL")
          value("\$.repository.html_url")
        }
        genericVariable {
          expressionType("JSONPath")
          key("COMMIT_SHA")
          value("\$.head_commit.id")
        }
        genericVariable {
          expressionType("JSONPath")
          key("SLACK_USER")
          value("\$.slack_user")
        }
      }
      regexpFilterText("\$GIT_BRANCH_ORIGIN \$HTML_URL")
      regexpFilterExpression("^(origin/release.*|origin/hotfix.*|origin/master).*(narrative-platform).*\$")
      token("ebdkeiadl23839apdosi45")
      printPostContent(false)
      printContributedVariables(false)
      causeString("\$SLACK_USER pushed to \$GIT_BRANCH_ORIGIN")
    }
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
      scriptPath("jenkins/docker-build.groovy")
    }
  }
  disabled(false)
}
