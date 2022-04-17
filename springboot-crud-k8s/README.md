---
title: springboot-crud-k8s
tags: 
date: 2022-04-17 14:10:52
id: 1650175852295202800
---
# 摘要

Run & Deploy Spring Boot CRUD Application With MySQL on K8S。

改编自 [Kubernetes Tutorial | Run & Deploy Spring Boot CRUD Application With MySQL on K8S | JavaTechie](https://www.youtube.com/watch?v=pIPji3_rYPY) ，资料地址：https://github.com/Java-Techie-jt/springboot-crud-k8s 

# 制作镜像

```
mvn clean package -Dmaven.test.skip=true 
```

```
docker image build -t springboot-crud-k8s:1.0 -f Dockerfile .
```

# 内部 MySQL

## 准备 PV

自行准备 NFS ，我这里使用 [pv.yaml](internal-mysql\pv.yaml) 

## 部署 MySQL

如果部署失败，检查 PV 。不知道为什么，我部署失败后，删除 PV 并清空 PV 的挂载点，重新部署由成功了

# 常用命令

```
kubectl delete -f pv.yaml
kubectl apply -f pv.yaml

kubectl get pv

kubectl delete -f mysql-configMap.yaml
kubectl apply -f mysql-configMap.yaml

kubectl delete -f mysql-secrets.yaml
kubectl apply -f mysql-secrets.yaml

kubectl delete -f mysql-deployment.yaml
kubectl apply -f mysql-deployment.yaml

kubectl get pod
kubectl describe deployment mysql

kubectl delete -f crud-internal-mysql.yaml
kubectl apply -f crud-internal-mysql.yaml


kubectl get svc





```





```
kubectl delete -f .
rm -rf  /nfs/data/*
```

