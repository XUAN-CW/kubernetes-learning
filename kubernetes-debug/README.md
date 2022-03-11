---
title: kubernetes-debug
tags: 
date: 2022-03-08 23:08:09
id: 1646752089900710500
---
# 摘要

# 常用命令

## pod

```sh
# 查看命名空间为 ingress-nginx 的 pod
kubectl get pod -n ingress-nginx 
```

```sh
# 进入pod
kubectl exec -it <your-pod-name>  -n <your-namespace>  -- /bin/sh
```

```sh
# 查看指定pod的日志
kubectl logs <pod_name>
kubectl logs -f <pod_name> #类似tail -f的方式查看(tail -f 实时查看日志文件 tail -f 日志文件log)

# 查看指定pod中指定容器的日志
kubectl logs <pod_name> -c <container_name>
#  (一次性查看)
kubectl logs <pod_name> -c <container_name> -n <your-namespace>
# (tail -f方式实时查看)
kubectl logs -f <pod_name> -n <your-namespace>
```

## deployment

```sh
# 查看 deployment
kubectl get deployment 
```

## service

```sh
# 查看 service
kubectl get svc
```

```sh
# 查看名为 ingress-nginx 的 service 暴露端口
kubectl get svc -n ingress-nginx | awk '{printf "%-40s%-15s\n",$1,$5}'
```

## ingress

```sh
# 查看名为 ingress-wildcard-host 的 ingress
kubectl describe ingress ingress-wildcard-host
```

## namespace

```sh
kubectl create namespace test-env #Create a namespace
kubectl get namespace #Get a list of namespaces
```



# debug 手段

[查看所有镜像](https://kubernetes.io/docs/tasks/access-application-cluster/list-all-running-container-images/#list-all-container-images-in-all-namespaces) 

```
kubectl get pods --all-namespaces -o jsonpath="{.items[*].spec.containers[*].image}" |\
tr -s '[[:space:]]' '\n'
```

