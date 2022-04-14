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

# 外部MySQL映射到内部

## Endpoints

```yaml
# mysql-endpoints.yaml
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
  name: mysql-external
  namespace: default
spec:
  clusterIP: None
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

## telnet

```sh
kubectl run -it --rm --image=busybox:1.28 --restart=Never busybox -- /bin/sh
```

```sh
ping mysql-external.default.svc.cluster.local
```

```sh
telnet mysql-external.default.svc.cluster.local 3306
```

## MySQL 

此测试未完成，以后再说

```sh
# 
kubectl run -it --rm --env MYSQL_ROOT_PASSWORD=root --image=mysql:5.7.30 --restart=Never mysql-client -- mysql -h mysql-external.default.svc.cluster.local -u root -p root
```



# 参考

 [K8S集群内部pod访问外部mysql.html](assets\references\K8S集群内部pod访问外部mysql.html) 
