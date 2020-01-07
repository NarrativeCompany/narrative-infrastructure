# narrative-infrastructure

This repository houses all code, scripts, and configuration responsible for the Narrative infrastructure.

# Git Layout

The top level folder structure is as follows:

- **docker** - any infrastructure Docker images created to support the Narrative infrastructure.
- **environments** - the Narrative project environments, such as "informationtechnology" (tools such as [Jenkins](https://jenkins.narrative.cloud)) and "dev".
- **jenkins** - Jenkins groovy scripts for the infrastructure project.
- **jenkins-seed-jobs** - Jenkins seed jobs. These are jobs that will be created in Jenkins, for other projects.
