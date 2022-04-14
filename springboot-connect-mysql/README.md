---
title: springboot-connect-mysql
tags: 
date: 2022-04-14 08:52:02
id: 1649897522504865900
---
# 摘要

待看的解决方案：

https://stackoverflow.com/questions/60964196/how-does-spring-boot-connect-localhost-mysql-in-k8s

# 环境

```sql
#简单启动
sudo docker run -d \
  -p 3306:3306 \
  --restart=always \
  --privileged=true \
  -e MYSQL_ROOT_PASSWORD=root \
  --name mysql-dev \
  mysql:5.7.30
```

```sh
#简单启动
sudo docker run -d \
  -p 3307:3306 \
  --restart=always \
  --privileged=true \
  -e MYSQL_ROOT_PASSWORD=root \
  --name mysql-test \
  mysql:5.7.30
```



# 编写代码

# 创建 docker 镜像

```sh
# 创建镜像
docker image rm springboot-connect-mysql:1.0
docker image build -t springboot-connect-mysql:1.0 -f Dockerfile .

```





## 测试

### test

```sh
# 运行
docker container rm -f scm-test
docker run -it --rm \
  -p 9001:8080 \
  --name scm-test \
  springboot-connect-mysql:1.0

```

 http://192.168.18.10:9000/connect-mysql 

### dev

```sh
# 运行
docker container rm -f scm-dev
docker run -it --rm \
  -p 9000:8080  \
  --name scm-dev \
  -e PARAMS="--spring.profiles.active=dev" \
  springboot-connect-mysql:1.0

```

 http://192.168.18.10:9000/connect-mysql 

# k8s

## Deployment

```yaml
# springboot-connect-mysql-Deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: springboot-connect-mysql
spec:
  selector:
    matchLabels:
      octopusexport: OctopusExport
  replicas: 1
  strategy:
    type: RollingUpdate
  template:
    metadata:
      labels:
        octopusexport: OctopusExport
    spec:
      dnsPolicy: Default
      hostNetwork: true
      containers:
        - name: scm
          image: 'springboot-connect-mysql:1.0'
          ports:
            - containerPort: 8080
          env:
            - name: PARAMS
              value: '--spring.profiles.active=prod'

```

## service

```
kubectl expose deployment springboot-connect-mysql --port=8080 --target-port=8080
```

```
kubectl delete svc springboot-connect-mysql
```



## 访问

```sh
kubectl describe svc springboot-connect-mysql
```

 http://172.31.0.4:8080/connect-mysql 

# 











