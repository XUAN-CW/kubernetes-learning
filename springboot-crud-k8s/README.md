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

# 外部MySQL

## IP 地址要求

外部 MySQL 的 IP 地址不能在 kubernetes 的 **service-cluster-ip-range** 和 **cluster-cidr** 范围内， **service-cluster-ip-range** 和 **cluster-cidr** 查看方法如下：

```
[root@k8s-master ~]# kubectl cluster-info dump | grep -m 1 service-cluster-ip-range
                            "--service-cluster-ip-range=10.96.0.0/16",
[root@k8s-master ~]# kubectl cluster-info dump | grep -m 1 cluster-cidr
                            "--cluster-cidr=192.168.0.0/16",

```

这里我外部 MySQL 的地址为 `10.98.12.24` 

## 部署

```sh
#简单启动
sudo docker run -d \
  -p 3306:3306 \
  --restart=always \
  --privileged=true \
  -e MYSQL_ROOT_PASSWORD=root \
  --name mysql \
  mysql:5.7.30
```

## 外部MySQL映射到内部

参考 [Integrating External Services.html](assets\references\Integrating External Services.html) 得到  [external-mysql.yaml](external-mysql\external-mysql.yaml) 

## 测试

### ping

```sh
kubectl run -it --rm --image=busybox:1.28 --restart=Never busybox -- /bin/sh
```

```sh
ping mysql-external.default.svc.cluster.local
```

### telnet

```sh
telnet mysql-external.default.svc.cluster.local 3306
```







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

