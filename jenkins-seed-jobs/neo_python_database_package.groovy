pipelineJob("neo_python_database_package") {
	description("Uploads the neo-python database to https://neo-python-chain.narrative.org/index.html.")
	keepDependencies(false)
	parameters {
		stringParam("GIT_BRANCH_ORIGIN", "origin/master", "The Git branch to use. Defaults to master.")
		stringParam("HTML_URL", "https://github.com/NarrativeCompany/narrative-infrastructure", "The Github URL.")
	}
	definition {
		cpsScm {
			scm {
				git {
					remote {
						github("NarrativeCompany/narrative-infrastructure", "ssh")
						credentials("24489ad6-f93a-4682-8ac3-cdeb6de18c71")
					}
					branch("\$GIT_BRANCH_ORIGIN")
				}
			}
			scriptPath("jenkins/neo_python_database_package.groovy")
		}
	}
  triggers {
    cron('@daily')
  }
	disabled(false)
}
