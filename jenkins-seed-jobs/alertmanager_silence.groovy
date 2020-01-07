pipelineJob("alertmanager_silence") {
  description("Creates an Alertmanager silence for a duration of time, based on an alert label/value pair. This will suppress alerts matching that label/value from sending notifications to Slack and PagerDuty.  Useful for when known alerts will fire or when planned maintenance will occur.")
  keepDependencies(false)
  parameters {
    stringParam("GIT_BRANCH_ORIGIN", "origin/master", "The Git branch to use. Defaults to master.")
    choiceParam('ALERTMANAGER', ['primary', 'it', 'sandbox', 'staging', 'production'], 'Prometheus/Alertmanager environment to silence.')
    stringParam("SILENCE_LABEL", "", "The silence label to specify, such as gce_instance_name.")
    stringParam("SILENCE_VALUE", "", "The silence value to specify, such as neo-database-01.")
    stringParam("SILENCE_DURATION", "60", "The silence duration (in minutes) to specify, such as 60 (1 hour).")
    choiceParam('SLACK_NOTIFY', ['yes', 'no'], 'Whether or not to notify the #jenkins channel in Slack.')
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
      scriptPath("jenkins/alertmanager_silence.groovy")
    }
  }
  disabled(false)
}
