---
title: yaml文件的编写
tags: 
- k8s
- yaml
date: 2022-01-09 14:21:20
id: 1641709280025393800
---
# 摘要

介绍两种 yaml 文件的编写方法

1. kubectl create
2. kubectl get

# 环境

-  kubernetes:v1.20.9 

# 实践

原来的命令：

```sh
kubectl create deployment tomcat --image=tomcat 
```

生成 yaml：

```sh
kubectl create deployment tomcat --image=tomcat -o yaml --dry-run=client
```













