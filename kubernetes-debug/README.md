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

# pod does not exist

```
[root@k8s-master system]# kubectl exec his-mysql-0 -it -n his -- /bin/sh
error: unable to upgrade connection: pod does not exist
```

```
[root@k8s-node1 ~]# find /  -name "10-kubeadm.conf"
/usr/lib/systemd/system/kubelet.service.d/10-kubeadm.conf
```

```
kubectl logs his-mysql-0 -n his
```



```
[root@k8s-master ~]# kubectl get nodes k8s-node1 -o yaml | grep address
        f:addresses:
            f:address: {}
            f:address: {}
  addresses:
  - address: 10.0.2.15
  - address: k8s-node1

```



## 修复方案

### 编辑 10-kubeadm.conf



### 添加域名解析

```
[root@k8s-master ~]# echo "172.31.0.3  k8s-node1" >> /etc/hosts
[root@k8s-master ~]# echo "172.31.0.4  k8s-node2" >> /etc/hosts
```

## 参考

 [kubectl logs、exec、port-forward 执行失败问题解决.html](assets\references\kubectl logs、exec、port-forward 执行失败问题解决.html) 

 [Playing with kubeadm in Vagrant Machines, Part 2.html](assets\references\Playing with kubeadm in Vagrant Machines, Part 2.html) 
