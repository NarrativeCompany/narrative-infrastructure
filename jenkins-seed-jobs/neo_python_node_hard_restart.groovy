pipelineJob("neo_python_node_hard_restart") {
	description("Performs a hard restart (delete data and re-download) of a neo-node")
	keepDependencies(false)
	parameters {
		stringParam("GIT_BRANCH_ORIGIN", "origin/master", "The Git branch to use. Defaults to master.")
		stringParam("HTML_URL", "https://github.com/NarrativeCompany/narrative-infrastructure", "The Github URL.")
		stringParam("HOST", "ENTER HOST HERE", "The NEO node to force a hard restart (like neo-01, for instance)")
		stringParam("NEO_BLOCK_NUMBER", "latest", "The NEO block number to use, defaults to latest (currently in the format of '2595979_2018-08-08'")
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
			scriptPath("jenkins/neo_python_node_hard_restart.groovy")
		}
	}
	disabled(false)
}
