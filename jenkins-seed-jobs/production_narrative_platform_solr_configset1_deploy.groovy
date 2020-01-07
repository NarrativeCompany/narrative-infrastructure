pipelineJob("production-narrative-platform-solr-configset1-deploy") {
  properties {
    disableConcurrentBuilds()
  }
	description("Deploys the latest configset1 to Solr/Zookeeper (stopwords, schema, solrconfg). This job should rarely be run manually, and is usually run by production-narrative-platform-deploy.")
	keepDependencies(false)
	parameters {
		stringParam("GIT_BRANCH_ORIGIN", "origin/master", "The Git branch to use.")
		stringParam("COMMIT_SHA", "auto", "If a specific Git commit SHA should be used, enter it here. Otherwise it will default to the HEAD of the Git branch.")
		choiceParam("ENVIRONMENT", ["production"], "The environment to deploy to. Defaults to production.")
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
			scriptPath("jenkins/solr-configset1-deploy.groovy")
		}
	}
	disabled(false)
}
