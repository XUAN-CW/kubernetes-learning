---
title: springboot-connect-mysql
tags: 
date: 2022-04-14 08:52:02
id: 1649897522504865900
---
# 摘要



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

# 创建镜像

```sh
# 创建镜像
docker image rm springboot-connect-mysql:1.0
docker image build -t springboot-connect-mysql:1.0 -f Dockerfile .
# 运行
docker container rm -f springboot-connect-mysql:1.0
docker run -it --rm -p 9000:8080 springboot-connect-mysql:1.0

```





## 测试

 http://192.168.18.10:9000/connect-mysql 
