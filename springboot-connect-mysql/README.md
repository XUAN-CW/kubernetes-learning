---
title: springboot-connect-mysql
tags: 
date: 2022-04-14 08:52:02
id: 1649897522504865900
---
# 摘要



```
kubectl create ns springboot-connect-mysql
```





然后创建镜像：

```sh
docker image rm springboot-connect-mysql:1.0
docker image build -t springboot-connect-mysql:1.0 -f Dockerfile .
```

最后运行：

```sh
docker container rm -f springboot-connect-mysql:1.0
docker run -it --rm -p 9000:8080 springboot-connect-mysql:1.0
```





 http://10.98.12.24:9000/connect-mysql 
