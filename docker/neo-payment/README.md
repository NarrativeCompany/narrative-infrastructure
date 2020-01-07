# Overview

Documentation on the neo-payment Docker image.

# Important Files

Note that you will want to volume mount the following files:

```
/opt/tokensale-neo-smartcontract/util/config/nrve-niche-config.json
/opt/tokensale-neo-smartcontract/util/config/db-config.json
/opt/tokensale-neo-smartcontract/util/config/smtp-config.json
```

nrve-niche-config.json

```
{
  "network" : "PRIVNET",
  "smart_contract" : "",
  "niche_payment_address" : ""
}
```

db-config.json

```
{
  "host": "localhost",
  "user" : "root",
  "password" : "",
  "db" : "global"
}
```

smtp-config.json

```
{
  "host" : "localhost",
  "port" : 25,
  "use_tls" : false,
  "from_address" : "",
  "to_address" : ""
}
```

# Requirements For Use

- Need a properly configured nrve-niche-config.json.
- Need a properly configured db-config.json, with credentials.
- Need a properly configured SMTP configuration.
- Need to run the cloud-sql-proxy Docker image.
- Need to run a postfix Docker image.
- Need to have all the VMs running the Docker images using host networking.

# Build

To build, run the build_docker script:

```
./build_docker.sh
```

Then retag and push

```
docker tag narrativecompany/neo-payment:latest narrativecompany/neo-payment:<YOURTAG> 
docker push narrativecompany/neo-payment:<YOURTAG>
```
