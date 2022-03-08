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

# debug 手段

[查看所有镜像](https://kubernetes.io/docs/tasks/access-application-cluster/list-all-running-container-images/#list-all-container-images-in-all-namespaces) 

```
kubectl get pods --all-namespaces -o jsonpath="{.items[*].spec.containers[*].image}" |\
tr -s '[[:space:]]' '\n'
```



# error

## ImagePullBackOff

### error 原因

`kubectl get pods -A`  出现 `Init:ImagePullBackOff` 或`Init:ErrImagePull` 就是说你镜像拉取失败了

### 解决方案

1. 主节点执行 `kubectl get pods -A -o wide` ，查看是哪台机器哪个 pod 拉取失败
2. 主节点 [describe](https://kubernetes.io/docs/reference/generated/kubectl/kubectl-commands#describe) 拉取失败的 pod ，查看是哪个 image 缺失
3. 自行 docker pull 缺失的 image 

当时我是有一台服务器成功拉取下来了，然后我 docker save 了镜像，再上传到拉取失败的服务器上

### 参考

 [ImagePullBackOff 错误处理.html](assets\references\ImagePullBackOff 错误处理.html) 





