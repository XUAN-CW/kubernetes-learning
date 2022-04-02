---
title: k8s部署mysql集群
tags: 
date: 2022-04-01 22:55:37
id: 1648824938004205700
---
# 摘要

# NFS

## 主节点

```sh
# 在每个机器
yum install -y nfs-utils


# 在master 执行以下命令 
echo "/data/nfs/ *(insecure,rw,sync,no_root_squash)" > /etc/exports

# 执行以下命令，启动 nfs 服务;创建共享目录
mkdir -p /data/nfs/


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

## 从节点

- 主节点地址为 **172.31.0.2** 

```sh
yum install -y nfs-utils

showmount -e 172.31.0.2

mkdir -p /data/nfs/

mount -t nfs 172.31.0.2:/data/nfs/ /data/nfs/

```

