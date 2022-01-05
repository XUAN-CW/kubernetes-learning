---
title: ingress-nginx域名访问
tags: 
date: 2022-01-05 22:31:52
id: 1641393112526808400
---
# 摘要



# 步骤

## create deployment

```
kubectl create deployment test --image=nginx --port=80 

kubectl get deployment 
```

## expose deployment

```

kubectl expose deployment test --port=80 --target-port=80 --type=NodePort 

kubectl get svc

```

### Resource backends

 [ingress-resource-backend.yaml](assets/data/ingress-resource-backend.yaml) 

```sh
kubectl apply -f ingress-resource-backend.yaml
```







