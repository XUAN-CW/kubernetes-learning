---
title: kubernetes-debug
tags: 
date: 2022-03-08 23:08:09
id: 1646752089900710500
---
# 摘要

# pod

```sh
# 查看命名空间为 ingress-nginx 的 pod
kubectl get pod -n ingress-nginx 
```

# deployment

```sh
# 查看 deployment
kubectl get deployment 
```

# service

```sh
# 查看 service
kubectl get svc
```

```sh
# 查看名为 ingress-nginx 的 service 暴露端口
kubectl get svc -n ingress-nginx | awk '{printf "%-40s%-15s\n",$1,$5}'
```

# ingress

```sh
# 查看名为 ingress-wildcard-host 的 ingress
kubectl describe ingress ingress-wildcard-host
```





