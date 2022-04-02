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
# nfs-server 的地址是 172.31.0.2
showmount -e 172.31.0.2 

mkdir -p /nfs/data

mount -t nfs 172.31.0.2:/nfs/data /nfs/data

```

# PV

```yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv0001 
spec:
  capacity:
    storage: 5Gi 
  accessModes:
  - ReadWriteOnce 
  nfs: 
    path: /nfs/data
    server: 172.31.0.2
  persistentVolumeReclaimPolicy: Retain 
```

# PVC

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: pvc0001
spec:
  accessModes:  # PVC也需要定义访问模式，不过它的模式一定是和现有PV相同或者是它的子集，否则匹配不到PV
  - ReadWriteOnce
  resources: # 定义资源要求PV满足这个PVC的要求才会被匹配到
    requests:
      storage: 500Mi  # 定义要求有多大空间
```







# 参考

 [Persistent storage using NFS.html](assets\references\Persistent storage using NFS.html) 

 [PV、PVC、StorageClass讲解.html](assets\references\PV、PVC、StorageClass讲解.html) 

 [NFS pv部署.html](assets\references\NFS pv部署.html) 



