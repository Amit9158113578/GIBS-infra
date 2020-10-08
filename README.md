# NCHR CRM
# Deploy NCHR CRM

# general structure

Risa stack intends to consolidate the deployment of a whole stack of applications which are core for RISA in one docker deployment. It provides an nginx as entrypoint which links to the appropriate subdomains.

Currently the whole stack will be deployed with docker-compose. 

# file structure

The stack consists of stack services (like elastic-docker) each of them is split into single services (like heartbeat, logstash, elastic, etc...).
All stack services are structured into subdirectories. The root of the directory contains a docker-compose file which will be used to bring up the service.

# configuration steps

1. install docker
2. install sudo apt install pass (on ubuntu needed otherwise registry login will fail)
3. install gitlab-runner and link it to repository.risa.rw
4. tag it to a specific host-name (like staging123)
5. create on host subdirectory structure /usr/share/projects/risa-stack with sticky bit
6. assign gitlab-runner to docker group and assign the uid's 999 to gitlab-runner and gitlab-runner group
7. create deployment and cleanup jobs for the target host in gitlab-ci.yml
9. run pipeline to deploy the empty stack (all volumes will be created fresh - this is a new deployment)
10. migrate data if possible from volume to volume


