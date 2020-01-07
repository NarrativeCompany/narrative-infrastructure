pipelineJob("narrative-platform-build-and-deploy") {
  properties {
    disableConcurrentBuilds()
  }
	description("A narrative-platform job that first, builds a new narrative-core and narrative-web-front-end Docker image and then deploys that Docker image to an environment (which is determined by the branch). This job will automatically run when a push to the dev branch occurs. ")
	keepDependencies(false)
	parameters {
		stringParam("GIT_BRANCH_ORIGIN", "origin/dev", "The Git branch to use.")
		stringParam("COMMIT_SHA", "auto", "If a specific Git commit SHA should be used, enter it here. Otherwise it will default to the HEAD of the Git branch.")
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
      regexpFilterExpression("^(origin/dev ).*(narrative-platform).*\$")
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
			scriptPath("jenkins/build-and-deploy.groovy")
		}
	}
	disabled(false)
}
