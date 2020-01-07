# grafana

Create the oauth secret in the IT namespace:

```
# in 1password narrative.cloud oauth
kubectl --namespace it create secret generic oauth --from-literal=OAUTH2_PROXY_CLIENT_ID=<ID> --from-literal=OAUTH2_PROXY_CLIENT_SECRET=<SECRET> --from-literal=OAUTH2_PROXY_COOKIE_SECRET=<cookie>
```

Create the admin password:

```
# in 1password grafana admin
kubectl -n it create secret generic grafana-credentials --from-literal=GF_SECURITY_ADMIN_PASSWORD=yourpassword
```
