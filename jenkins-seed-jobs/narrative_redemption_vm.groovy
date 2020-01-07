pipelineJob("narrative-redemption-vm") {
  description("Create or destroy the narrative-redemption VM in the neo-narrative project.")
  keepDependencies(false)
  parameters {
    stringParam("GIT_BRANCH_ORIGIN", "origin/master", "The Git branch to use. Defaults to master.")
    choiceParam('STATUS', ['create', 'destroy'], 'Do you want to create or destroy the narrative-redemption VM?.')
    choiceParam('BLOCKCHAIN', ['MainNet', 'TestNet'], 'Do you want the Docker container to use MainNet or TestNet.')
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
      scriptPath("jenkins/narrative-redemption-vm.groovy")
    }
  }
  disabled(false)
}
