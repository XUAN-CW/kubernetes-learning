---
title: ingress-nginx域名访问
tags: 
date: 2022-01-05 22:31:52
id: 1641393112526808400
---
# 摘要



# 步骤

## create deployment

```sh
# 建议先把需要的 nginx、tomcat 镜像拉取下来
kubectl create deployment service1 --image=nginx --port=80 
kubectl create deployment service2 --image=tomcat --port=8080 


kubectl get deployment 
```

## expose deployment

```

kubectl expose deployment service1 --port=80 --target-port=80 --type=NodePort 
kubectl expose deployment service2 --port=8080 --target-port=80 --type=NodePort 



kubectl get svc

```

### Resource backends

 [ingress-resource-backend.yaml](assets/data/ingress-resource-backend.yaml) 

```sh
kubectl apply -f ingress-resource-backend.yaml
```



```sh
describe ingress ingress-resource-backend
```



# 测试

## hosts



## 查看端口

```
kubectl get svc -A | grep ingress-nginx-controller
```



## 访问



http://k8s.example.com:32485















