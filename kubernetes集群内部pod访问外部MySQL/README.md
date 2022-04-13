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
    - ip: 192.168.18.10 #外部数据库地址
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



```
mysql-dev.default.svc.cluster.local
```

```
telnet mysql-dev.default.svc.cluster.local 3306
```



## nodePort

```yaml
# mysql-nodePort.yaml
apiVersion: v1
kind: Service
metadata:
  name: mysql-nodeport
  namespace: default
spec:
 type: NodePort
 ports:
 - port: 30080          
   targetPort: 3306
   nodePort: 30006
 selector:
  name: mysql-dev
```

```
kubectl apply -f mysql-nodePort.yaml
```

```sh
kubectl get svc
```









