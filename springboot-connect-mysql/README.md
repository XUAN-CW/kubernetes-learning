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
docker image build -t springboot-initializr:1.0 -f Dockerfile .
```

最后运行：

```sh
docker run -itd -p 9000:8080 springboot-initializr:1.0
```

