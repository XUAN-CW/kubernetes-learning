---
title: pod-does-not-exist
tags: 
date: 2022-03-10 09:59:02
id: 1646877542042312600
---
# 摘要







# 问题描述

```
[root@k8s-master ~]# kubectl create deployment service1 --image=tomcat:8.5.73-jre8-openjdk-slim-bullseye
deployment.apps/service1 created
[root@k8s-master ~]# kubectl get pod -n default
NAME                       READY   STATUS              RESTARTS   AGE
service1-8fc6694c9-8pm25   0/1     ContainerCreating   0          27s
[root@k8s-master ~]# kubectl exec service1-8fc6694c9-8pm25 -it -n default -- /bin/sh
error: unable to upgrade connection: pod does not exist
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



# 修复方案

## 添加域名解析

这里要求各主机能 ping 通自己的 hostname 

```
[root@k8s-node1 ~]# echo "172.31.0.3  k8s-node1" >> /etc/hosts
```

```
[root@k8s-node2 ~]# echo "172.31.0.4  k8s-node2" >> /etc/hosts
```

## 重启

```
systemctl stop kubelet.service && \
systemctl daemon-reload && \
systemctl start kubelet.service
```

# 参考

 [kubectl logs、exec、port-forward 执行失败问题解决.html](assets\references\kubectl logs、exec、port-forward 执行失败问题解决.html) 

 [Playing with kubeadm in Vagrant Machines, Part 2.html](assets\references\Playing with kubeadm in Vagrant Machines, Part 2.html) 
