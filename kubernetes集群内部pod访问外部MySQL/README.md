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

