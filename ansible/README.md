# ansible

# GCE Auth

Environment variables:

```
GCE_EMAIL=it-terraform@informationtechnology-205813.iam.gserviceaccount.com
GCE_PROJECT=neo-narrative
GCE_CREDENTIALS_FILE_PATH=~/.config/gcloud/it-terraform.json
```

# Gathering SSH Keys

If you attempt to run an Ansible playbook, you may get messages like this:

```
➜  ansible git:(master) ✗ ansible-playbook -i inventory/neo narrative-init.yml

PLAY [all] *********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************

TASK [Gathering Facts] *********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
Thursday 19 July 2018  09:28:16 -0400 (0:00:00.054)       0:00:00.054 *********
Thursday 19 July 2018  09:28:16 -0400 (0:00:00.052)       0:00:00.052 *********
The authenticity of host '35.237.104.45 (35.237.104.45)' can't be established.
ECDSA key fingerprint is SHA256:jPnxjs+z3TwIqm8vD0DC/CSLltD16t89ieK/HiCZiLM.
Are you sure you want to continue connecting (yes/no)? The authenticity of host '35.233.242.249 (35.233.242.249)' can't be established.
ECDSA key fingerprint is SHA256:wMuqcrvmjeyhG05CnBlAKZ8xv7C6u7mlrZTT6jjKGXU.
Are you sure you want to continue connecting (yes/no)? The authenticity of host '35.204.180.99 (35.204.180.99)' can't be established.
ECDSA key fingerprint is SHA256:unDdr+vSQTMnm5V8zf4qP6D2zf4z+wgwPj4vh1Q4lyE.
Are you sure you want to continue connecting (yes/no)? The authenticity of host '35.234.142.8 (35.234.142.8)' can't be established.
ECDSA key fingerprint is SHA256:q164DZiW/jvCAg0QwG2joU8S246GEECRFxzoGHG3bYI.
...
```

How can you verify the validity of this SSH fingerprint? There's a playbook to gather that from the Google Cloud API:

```
ansible-playbook localhost_gce_serial_scan.yml -e project=neo-narrative
```

This will collect SSH keys and put them in your `~/.ssh/known_hosts` file. Note that this requires the systemd timer from the Ansible role `service.ssh-print-hostkeys`. This is applied using the `narrative-init` playbook.

# narrative-init

All instances/VMs should be configured to emit their public SSH host key to the Google Cloud Serial Log:

```
ansible-playbook -i inventory/neo narrative-init.yml
```

# Solr staging

Init:
```
ansible-playbook -i inventory/staging narrative-init.yml --limit tag_solr
```

Deploy:
```
ansible-playbook -i inventory/staging solr-deploy.yml 
```
