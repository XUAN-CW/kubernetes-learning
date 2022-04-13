---
title: kubernetes集群内部pod访问外部MySQL
tags: 
date: 2022-04-13 15:31:13
id: 1649835073316869800
---
# 摘要

# 外部MySQL

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

# endpoint

## Endpoints

```yaml
# mysql-endpoints.yaml
apiVersion: v1
kind: Endpoints
metadata:
  name: mysql-dev
  namespace: default
subsets:
  - addresses:
    - ip: 101.34.161.147 #外部数据库地址
    ports:
    - port: 3306
```

```sh
kubectl apply -f mysql-endpoints.yaml
```

```sh
kubectl get endpoints
```



## service

```yaml
# mysql-service.yaml 
apiVersion: v1
kind: Service
metadata:
  name: mysql-dev
  namespace: default
spec:
  clusterIP: 10.96.2.128
  ports:
  - port: 3306
    targetPort: 3306
    protocol: TCP
```

```sh
kubectl apply -f mysql-service.yaml
```

```sh
kubectl get svc
```

# 测试

```sh
kubectl run test-pod1 -it --image=busybox:1.28
```

```sh
ping mysql-dev.default.svc.cluster.local
```

```sh
telnet mysql-dev.default.svc.cluster.local 3306
```

# 参考

 [K8S集群内部pod访问外部mysql.html](assets\references\K8S集群内部pod访问外部mysql.html) 
