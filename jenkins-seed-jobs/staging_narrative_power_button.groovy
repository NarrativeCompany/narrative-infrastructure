pipelineJob("staging-narrative-power-button") {
  description("Enables or disables the staging-narrative cluster.")
  keepDependencies(false)
  parameters {
    stringParam("GIT_BRANCH_ORIGIN", "origin/master", "The Git branch to use. Defaults to master.")
    choiceParam('STATUS', ['on', 'off'], 'Do you want to turn the staging-narrative cluster on or off?.')
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
      scriptPath("jenkins/staging-narrative-power-button.groovy")
    }
  }
//  triggers {
//    parameterizedTimerTrigger {
//      parameterizedSpecification('''
//        # Turn the staging cluster off at 11 PM Eastern (4 AM UTC) every day.
//        0 4 * * * %STATUS=off
//      ''')
//    }
//  }
  disabled(false)
}
