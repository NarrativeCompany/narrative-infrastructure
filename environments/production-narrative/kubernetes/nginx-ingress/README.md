# Overview

To deploy the nginx-ingress-controller (only deploys a public ingress controller at this time).

```
# nginx-ingress-0.30.0
helm install --name nginx-ingress stable/nginx-ingress --values values.yml
```

The current endpoint IP address for the NGINX ingress load balancer is `35.232.27.49`.

Notes:

```
NAME:   nginx-ingress
LAST DEPLOYED: Wed Sep  5 10:42:05 2018
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1/Service
NAME                              TYPE          CLUSTER-IP  EXTERNAL-IP  PORT(S)                     AGE
nginx-ingress-controller-metrics  ClusterIP     10.0.4.112  <none>       9913/TCP                    0s
nginx-ingress-controller          LoadBalancer  10.0.5.206  <pending>    80:30461/TCP,443:30964/TCP  0s
nginx-ingress-controller-stats    ClusterIP     10.0.1.35   <none>       18080/TCP                   0s
nginx-ingress-default-backend     ClusterIP     10.0.8.98   <none>       80/TCP                      0s

==> v1beta1/Deployment
NAME                           DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
nginx-ingress-controller       3        3        3           0          0s
nginx-ingress-default-backend  1        1        1           0          0s

==> v1/Pod(related)
NAME                                           READY  STATUS             RESTARTS  AGE
nginx-ingress-controller-7746f7cfbc-8rfqh      0/1    ContainerCreating  0         0s
nginx-ingress-controller-7746f7cfbc-vvjdb      0/1    ContainerCreating  0         0s
nginx-ingress-controller-7746f7cfbc-wmmcl      0/1    ContainerCreating  0         0s
nginx-ingress-default-backend-d676cbb5f-x6j9l  0/1    ContainerCreating  0         0s

==> v1/ConfigMap
NAME                      DATA  AGE
nginx-ingress-controller  1     1s

==> v1beta1/Role
NAME           AGE
nginx-ingress  0s

==> v1beta1/ClusterRoleBinding
NAME           AGE
nginx-ingress  0s

==> v1beta1/RoleBinding
NAME           AGE
nginx-ingress  0s

==> v1beta1/PodDisruptionBudget
NAME                           MIN AVAILABLE  MAX UNAVAILABLE  ALLOWED DISRUPTIONS  AGE
nginx-ingress-controller       1              N/A              0                    0s
nginx-ingress-default-backend  1              N/A              0                    0s

==> v1/ServiceAccount
NAME           SECRETS  AGE
nginx-ingress  1        1s

==> v1beta1/ClusterRole
NAME           AGE
nginx-ingress  0s


NOTES:
The nginx-ingress controller has been installed.
It may take a few minutes for the LoadBalancer IP to be available.
You can watch the status by running 'kubectl --namespace default get services -o wide -w nginx-ingress-controller'

An example Ingress that makes use of the controller:

  apiVersion: extensions/v1beta1
  kind: Ingress
  metadata:
    annotations:
      kubernetes.io/ingress.class: nginx
    name: example
    namespace: foo
  spec:
    rules:
      - host: www.example.com
        http:
          paths:
            - backend:
                serviceName: exampleService
                servicePort: 80
              path: /
    # This section is only required if TLS is to be enabled for the Ingress
    tls:
        - hosts:
            - www.example.com
          secretName: example-tls

If TLS is enabled for the Ingress, a Secret containing the certificate and key must also be provided:

  apiVersion: v1
  kind: Secret
  metadata:
    name: example-tls
    namespace: foo
  data:
    tls.crt: <base64 encoded cert>
    tls.key: <base64 encoded key>
  type: kubernetes.io/tls

```

# Monitoring ingress controller


Current version: nginx-ingress-0.31.0
```
helm repo update
helm install --namespace monitoring --name nginx-ingress-monitoring stable/nginx-ingress --values values-monitoring.yml
```

The current endpoint IP address is `35.239.145.148`.

Notes:
```
NAME:   nginx-ingress-monitoring
LAST DEPLOYED: Wed Nov 14 11:52:32 2018
NAMESPACE: monitoring
STATUS: DEPLOYED

RESOURCES:
==> v1beta1/PodDisruptionBudget
NAME                                 MIN AVAILABLE  MAX UNAVAILABLE  ALLOWED DISRUPTIONS  AGE
nginx-ingress-monitoring-controller  1              N/A              0                    1s

==> v1/ConfigMap
NAME                                 DATA  AGE
nginx-ingress-monitoring-controller  1     2s

==> v1beta1/ClusterRole
NAME                      AGE
nginx-ingress-monitoring  1s

==> v1beta1/Role
nginx-ingress-monitoring  1s

==> v1/Service
NAME                                         TYPE          CLUSTER-IP   EXTERNAL-IP  PORT(S)                     AGE
nginx-ingress-monitoring-controller-metrics  ClusterIP     10.0.6.223   <none>       9913/TCP                    1s
nginx-ingress-monitoring-controller          LoadBalancer  10.0.10.159  <pending>    80:32668/TCP,443:30840/TCP  1s
nginx-ingress-monitoring-controller-stats    ClusterIP     10.0.4.192   <none>       18080/TCP                   1s
nginx-ingress-monitoring-default-backend     ClusterIP     10.0.0.51    <none>       80/TCP                      1s

==> v1beta1/Deployment
NAME                                      DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
nginx-ingress-monitoring-controller       3        3        3           0          1s
nginx-ingress-monitoring-default-backend  1        1        1           0          1s

==> v1/Pod(related)
NAME                                                       READY  STATUS             RESTARTS  AGE
nginx-ingress-monitoring-controller-65c8796cfd-dd6hh       0/1    ContainerCreating  0         1s
nginx-ingress-monitoring-controller-65c8796cfd-mhd64       0/1    ContainerCreating  0         1s
nginx-ingress-monitoring-controller-65c8796cfd-w4g9p       0/1    ContainerCreating  0         1s
nginx-ingress-monitoring-default-backend-5f5887cd85-d845s  0/1    ContainerCreating  0         1s

==> v1/ServiceAccount
NAME                      SECRETS  AGE
nginx-ingress-monitoring  1        1s

==> v1beta1/ClusterRoleBinding
NAME                      AGE
nginx-ingress-monitoring  1s

==> v1beta1/RoleBinding
NAME                      AGE
nginx-ingress-monitoring  1s


NOTES:
The nginx-ingress controller has been installed.
It may take a few minutes for the LoadBalancer IP to be available.
You can watch the status by running 'kubectl --namespace monitoring get services -o wide -w nginx-ingress-monitoring-controller'

An example Ingress that makes use of the controller:

  apiVersion: extensions/v1beta1
  kind: Ingress
  metadata:
    annotations:
      kubernetes.io/ingress.class: monitoring
    name: example
    namespace: foo
  spec:
    rules:
      - host: www.example.com
        http:
          paths:
            - backend:
                serviceName: exampleService
                servicePort: 80
              path: /
    # This section is only required if TLS is to be enabled for the Ingress
    tls:
        - hosts:
            - www.example.com
          secretName: example-tls

If TLS is enabled for the Ingress, a Secret containing the certificate and key must also be provided:

  apiVersion: v1
  kind: Secret
  metadata:
    name: example-tls
    namespace: foo
  data:
    tls.crt: <base64 encoded cert>
    tls.key: <base64 encoded key>
  type: kubernetes.io/tls
```
