---
title: yaml文件的编写
tags: 
- k8s
- yaml
date: 2022-01-09 14:21:20
id: 1641709280025393800
---
# 摘要

介绍两种 yaml 文件模板的获取方法

1. kubectl create 
2. kubectl get

# 环境

-  kubernetes:v1.20.9 

# 实践

## kubectl create 

原来的命令：

```sh
kubectl create deployment tomcat --image=tomcat 
```

生成 yaml：

```sh
kubectl create deployment tomcat --image=tomcat -o yaml --dry-run=client
```

## kubectl get

这条命令需要根据已部署的服务创建 yaml ，这里我们先创建一个 deployment :

```
[root@k8s-master ~]# kubectl create deployment tomcat --image=tomcat
deployment.apps/tomcat created
[root@k8s-master ~]# kubectl get deploy
NAME     READY   UP-TO-DATE   AVAILABLE   AGE
tomcat   0/1     1            0           8s
[root@k8s-master ~]#
```

然后获取 yaml :

```sh
kubectl get deploy tomcat -o yaml
```











