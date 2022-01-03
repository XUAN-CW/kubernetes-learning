---
title: Kubernetes-learning
tags: 
date: 2022-01-03 16:50:49
id: 1641199849546655200
---
# 概述



# 环境

## VirtualBox 6.1.26 

## Vagrant 2.2.18 

## CentOS 

```
[root@10 ~]# cat /etc/redhat-release
CentOS Linux release 7.9.2009 (Core)
```

## Docker 

```
[root@10 ~]# docker -v
Docker version 20.10.7
```

```sh
# 移除之前安装好的 docker
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
# 配置 yum 源
sudo yum install -y yum-utils
sudo yum-config-manager \
--add-repo \
http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

#安装指定版本 docker 
yum install -y docker-ce-20.10.7 docker-ce-cli-20.10.7  containerd.io-1.4.6
```



## IP

172.31.0.2(k8s-master)、172.31.0.3(k8s-node1)、172.31.0.4(k8s-node1) 

## 内存

8G 

# 参考

 [云原生Java架构师的第一课K8s+Docker+KubeSphere+DevOps](https://www.bilibili.com/video/BV13Q4y1C7hS) 
