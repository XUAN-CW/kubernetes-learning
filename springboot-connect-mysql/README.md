---
title: springboot-connect-mysql
tags: 
date: 2022-04-14 08:52:02
id: 1649897522504865900
---
# 摘要

基于通过实践有用的方案：[Kubernetes Tutorial | Run & Deploy Spring Boot CRUD Application With MySQL on K8S | JavaTechie](https://www.youtube.com/watch?v=pIPji3_rYPY ) （资料 [springboot-crud-k8.7z](assets\references\springboot-crud-k8.7z) ） 实现 Kubernetes 中 spring boot 连接 MySQL



# 常用

```
curl springboot-connect-mysql.default.svc.cluster.local
```

```
curl 10.96.252.17
```



```
kubectl get pod 
kubectl exec -it -- bin/sh
```



```
kubectl run -it --rm --image=busybox:1.28 --restart=Never busybox -- /bin/sh
```

