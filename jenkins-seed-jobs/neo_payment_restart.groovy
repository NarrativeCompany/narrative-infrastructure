pipelineJob("neo_payment_restart") {
	description("Restarts the neo-payment service on th a Neo node that gets stuck.")
	keepDependencies(false)
	parameters {
		stringParam("GIT_BRANCH_ORIGIN", "origin/master", "The Git branch to use. Defaults to master.")
		stringParam("GCE_INSTANCE_NAME", "insert_gce_instance_name_here", "The GCE instance name to restart (example: neo-pay-01)")
    choiceParam('CLUSTER', ['narrative-sandbox-cluster', 'narrative-staging-cluster', 'narrative-production-cluster', 'neo'], 'The cluster to use. The Ansible inventory is used from this variable.')
	}
  triggers {
    GenericTrigger {
      genericVariables {
        genericVariable {
          expressionType("JSONPath")
          key("GIT_BRANCH_ORIGIN")
          value("neo-payment_restart_402")
        }
        genericVariable {
          expressionType("JSONPath")
          key("ALERTNAME")
          value("\$.alertname")
        }
        genericVariable {
          expressionType("JSONPath")
          key("CLUSTER")
          value("\$.cluster")
        }
        genericVariable {
          expressionType("JSONPath")
          key("GCE_INSTANCE_NAME")
          value("\$.gce_instance_name")
        }
      }
      regexpFilterText("\$ALERTNAME")
      regexpFilterExpression("^NeoBlockRateLowRestart")
      token("ebdkeiadl23839apdosi45")
      printPostContent(false)
      printContributedVariables(false)
      causeString("\$CLUSTER \$GCE_INSTANCE_NAME Neo blockchain is stuck.")
    }
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
			scriptPath("jenkins/neo-payment-restart.groovy")
		}
	}
	disabled(false)
}
