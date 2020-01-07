pipelineJob("narrative-sql-backup-monthly") {
  properties {
    disableConcurrentBuilds()
  }
	description("narrative-sql-backup-monthly Creates google cloud SQL backups with the description Automated-Monthly and then prunes them down to the ammount set by the BACKUP_COUNTER param.")
	keepDependencies(false)
	parameters {
		stringParam('GIT_BRANCH_ORIGIN', 'origin/master', 'The Git branch to use.')
		stringParam('SLACK_USER', 'Manual_Jenkins', "The Slack user (in the format '<@UID>').")
		stringParam('HTML_URL', 'https://github.com/NarrativeCompany/narrative-infrastructure', 'The Github URL.')
		choiceParam('PROJECT', ['all', 'sandbox-narrative', 'staging-narrative', 'production-narrative'], 'Project in which to create backups, defaults to all.')
		stringParam('BACKUP_DESCRIPTION', 'Automated-Monthly', 'Monthly backups description.')
		stringParam('BACKUP_COUNT', '12', 'Number of monthly backups to keep.')
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
			scriptPath("jenkins/narrative-sql-backup.groovy")
		}
	}
	triggers {
		cron('0 18 1 * *')
 	}
	disabled(false)
}
