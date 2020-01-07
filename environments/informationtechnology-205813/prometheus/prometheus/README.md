# prometheus

This deploys a custom Prometheus deployment. Note that this is NOT the helm installed Prometheus. It lacks the customization that may be needed.

# Requirements

Requires cluster admin:

```
# cluster-admin setup
---
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: cluster-admins
subjects:
- kind: User
  name: <YOURUSER>
roleRef:
  kind: ClusterRole
  name: cluster-admin
  apiGroup: ""
```

Requires that the prometheus-operator and kube-prometheus helm charts be installed. See the file `../values.yml`.

# Setup

Be sure that the ingress is properly named and defined. This is the only unique setting for each environment. After that, update the deploy.sh script as needed.

# Deploy

To deploy, run the `./deploy.sh` script.
