---
title: k8s部署mysql
tags: 
date: 2022-04-02 15:07:18
id: 1648883238552994800
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

```sh
# 每台 NFS 相关的机器都执行
mkdir -p /nfs/data/mysql

```



```yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: mysql-pv 
spec:
  capacity:
    storage: 5Gi 
  accessModes:
  - ReadWriteOnce 
  nfs: 
    path: /nfs/data/mysql
    server: 172.31.0.2
  persistentVolumeReclaimPolicy: Retain 
```

# PVC

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-data-pvc
spec:
  accessModes:
  - ReadWriteOnce
  resources:
    requests:
      storage: 500Mi
```

# MySQL

```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: mysql-set
  labels:
    app: mysql-app
spec:
  selector:
    matchLabels:
      octopusexport: OctopusExport
  replicas: 1
  updateStrategy:
    type: RollingUpdate
  serviceName: mysql-service
  podManagementPolicy: OrderedReady
  template:
    metadata:
      labels:
        app: mysql-app
        octopusexport: OctopusExport
    spec:
      dnsPolicy: Default
      hostNetwork: true
      volumes:
        - name: mysql-data-volume
          persistentVolumeClaim:
            claimName: mysql-data-pvc
      containers:
        - name: mysql
          image: 'mysql:5.7.30'
          ports:
            - name: mysql-port
              containerPort: 3306
          env:
            - name: MYSQL_RANDOM_ROOT_PASSWORD
              value: root
            - name: MYSQL_ROOT_PASSWORD
              value: root
          volumeMounts:
            - name: mysql-data-volume
              mountPath: /var/lib/mysql
              subPath: mysql


```











