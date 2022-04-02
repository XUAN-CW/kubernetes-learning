---
title: pv_pvc
tags: 
date: 2022-04-02 13:25:42
id: 1648877142320744800
---
# 摘要

# nfs文件系统

## nfs-server

```sh
# 在 nfs-server 执行以下命令,这里我的 nfs-server 是 172.31.0.2
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

## nfs-client

```sh

yum install -y nfs-utils
# 在从节点执行
# nfs-client 的地址是 172.31.0.2
showmount -e 172.31.0.2 

mkdir -p /nfs/data

mount -t nfs 172.31.0.2:/nfs/data /nfs/data
```

