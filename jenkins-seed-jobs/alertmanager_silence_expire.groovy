pipelineJob("alertmanager_silence_expire") {
  description("Expires an Alertmanager silence based on a label/value pair. Will remove a silence that was previously created, allowing alerts with that label/value pair to send notifications to Slack and PagerDuty.")
  keepDependencies(false)
  parameters {
    stringParam("GIT_BRANCH_ORIGIN", "origin/master", "The Git branch to use. Defaults to master.")
    choiceParam('ALERTMANAGER', ['primary', 'it', 'sandbox', 'staging', 'production'], 'Prometheus/Alertmanager environment to expire the silence.')
    stringParam("SILENCE_LABEL", "", "The silence label to specify, such as gce_instance_name.")
    stringParam("SILENCE_VALUE", "", "The silence value to specify, such as neo-database-01.")
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
      scriptPath("jenkins/alertmanager_silence_expire.groovy")
    }
  }
  disabled(false)
}
