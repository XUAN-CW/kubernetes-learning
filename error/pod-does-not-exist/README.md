---
title: pod-does-not-exist
tags: 
date: 2022-03-10 09:59:02
id: 1646877542042312600
---
# 摘要



```
kubectl create deployment service1 --image=tomcat:8.5.73-jre8-openjdk-slim-bullseye

```



## 问题描述

```
[root@k8s-master ~]# kubectl exec service1-8fc6694c9-8pm25 -it -n default -- /bin/sh
error: unable to upgrade connection: pod does not exist
[root@k8s-master ~]#
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

```
# 从节点中查找 10-kubeadm.conf 
[root@k8s-node1 ~]# find /  -name "10-kubeadm.conf"
/usr/lib/systemd/system/kubelet.service.d/10-kubeadm.conf
[root@k8s-node1 ~]#
```

编辑 `10-kubeadm.conf` ，添加下面的配置：

```
Environment="KUBELET_EXTRA_ARGS=--node-ip=<worker IP address>"
```

这里我的从节点 IP 地址是 `172.31.0.3` ，因此我添加下面的配置：

```
Environment="KUBELET_EXTRA_ARGS=--node-ip=172.31.0.3"
```

如果有多个从节点，那就需要像这个节点一样编辑 `10-kubeadm.conf` ，方法都是一样的

### 添加域名解析

```
[root@k8s-master ~]# echo "172.31.0.3  k8s-node1" >> /etc/hosts
[root@k8s-master ~]# echo "172.31.0.4  k8s-node2" >> /etc/hosts
```

### 重启

```
systemctl stop kubelet.service && \
systemctl daemon-reload && \
systemctl start kubelet.service
```

## 参考

 [kubectl logs、exec、port-forward 执行失败问题解决.html](assets\references\kubectl logs、exec、port-forward 执行失败问题解决.html) 

 [Playing with kubeadm in Vagrant Machines, Part 2.html](assets\references\Playing with kubeadm in Vagrant Machines, Part 2.html) 
