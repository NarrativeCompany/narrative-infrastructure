pipelineJob("sandbox-narrative-power-button") {
  description("Enables or disables the sandbox-narrative cluster (Dev).")
  keepDependencies(false)
  parameters {
    stringParam("GIT_BRANCH_ORIGIN", "origin/master", "The Git branch to use. Defaults to master.")
    choiceParam('STATUS', ['on', 'off'], 'Do you want to turn the sandbox-narrative cluster on or off?.')
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
      scriptPath("jenkins/sandbox-narrative-power-button.groovy")
    }
  }
//  triggers {
//    parameterizedTimerTrigger {
//      parameterizedSpecification('''
//        # Turn the sandbox cluster on at 7 AM Eastern (Midnight UTC) Monday through Friday.
//        0 12 * * 1-5 %STATUS=on
//        # Turn the sandbox cluster off at 11:15 PM Eastern (4:15 AM UTC) every day.
//        15 4 * * * %STATUS=off
//      ''')
//    }
//  }
  disabled(false)
}
