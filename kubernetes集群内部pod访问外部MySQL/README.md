---
title: kubernetes集群内部pod访问外部MySQL
tags: 
date: 2022-04-13 15:31:13
id: 1649835073316869800
---
# 摘要

# 外部MySQL

## IP 地址要求

```
[root@k8s-master in]# kubectl cluster-info dump | grep -m 1 service-cluster-ip-range
                            "--service-cluster-ip-range=10.96.0.0/16",
[root@k8s-master in]# kubectl cluster-info dump | grep -m 1 cluster-cidr
                            "--cluster-cidr=192.168.0.0/16",
```



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













# 外部MySQL映射到内部

参考 [Integrating External Services.html](assets\references\Integrating External Services.html) 

```yaml
apiVersion: v1
kind: Endpoints
metadata:
  name: mysql-external
  namespace: default
subsets:
  - addresses:
    - ip: 10.98.12.24 #外部数据库地址
    ports:
    - port: 3306
---
apiVersion: v1
kind: Service
metadata:
  name: mysql-external
  namespace: default
spec:
  type: ClusterIP
  ports:
  - port: 3306
    targetPort: 3306
    protocol: TCP


```

这里说一下我踩过的坑：

1. 注意外部数据库地址是否在 `service-cidr` 与 `pod-network-cidr` 范围内。









# 测试

## ping

```sh
kubectl run -it --rm --image=busybox:1.28 --restart=Never busybox -- /bin/sh
```

```sh
ping mysql-external.default.svc.cluster.local
```

## telnet

```sh
telnet mysql-external.default.svc.cluster.local 3306
```

