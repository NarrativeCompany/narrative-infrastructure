pipelineJob("ssh-known-hosts-update") {
	description("Gathers the SSH public keys from the serial console and uploads them to the narrative-devops Google Bucket.")
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
			scriptPath("jenkins/ssh-known-hosts-update.groovy")
		}
	}
	disabled(false)
}
