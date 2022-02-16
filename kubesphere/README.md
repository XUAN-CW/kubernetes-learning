---
title: kubesphere
tags: 
date: 2022-02-16 23:05:41
id: 1645023941462309200
---
# 摘要

# 环境

## docker

docker-ce-20.10.7

## kubernetes

kubernetes:v1.20.9

## 服务器要求

对于一个初学者来说，一定不要自作聪明，下面的配置一个都不能错！（高手当我放屁）

|          | k8s-master | k8s-node1  | k8s-node2  |
| -------- | ---------- | ---------- | ---------- |
| 系统     | CentOS 7.9 | CentOS 7.9 | CentOS 7.9 |
| IP       | 172.31.0.2 | 172.31.0.3 | 172.31.0.4 |
| 内存     | 16G        | 4GG        | 46G        |
| 登录用户 | root       | root       | root       |
| CPU      | 4          | 4          | 4          |
| hostname | k8s-master | k8s-node1  | k8s-node2  |

# 安装步骤

## nfs文件系统

```sh
# 在每个机器
yum install -y nfs-utils
```

```sh
# 在主节点
# 在master 执行以下命令 
echo "/nfs/data/ *(insecure,rw,sync,no_root_squash)" > /etc/exports


# 执行以下命令，启动 nfs 服务;创建共享目录
mkdir -p /nfs/data


# 在master执行
systemctl enable rpcbind
systemctl enable nfs-server
systemctl start rpcbind
systemctl start nfs-server

# 使配置生效
exportfs -r

#检查配置是否生效
exportfs
```

## 配置nfs-client

```sh
# 在从节点执行
# 我的主节点是 172.31.0.4 
showmount -e 172.31.0.4

mkdir -p /nfs/data

mount -t nfs 172.31.0.4:/nfs/data /nfs/data
```










