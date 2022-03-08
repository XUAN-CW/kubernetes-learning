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
| 内存     | 16G        | 16G        | 16G        |
| 登录用户 | root       | root       | root       |
| CPU      | 4          | 4          | 4          |
| hostname | k8s-master | k8s-node1  | k8s-node2  |

# 安装步骤

## nfs文件系统

### 安装nfs-server

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

### 配置nfs-client

```sh
# 在从节点执行
# 我的主节点是 172.31.0.2
showmount -e 172.31.0.2 

mkdir -p /nfs/data

mount -t nfs 172.31.0.2:/nfs/data /nfs/data
```

### 配置默认存储

主节点中 apply [sc.yaml](assets\data\sc.yaml) ，需要修改两个地方，其他不用修改：

```yaml
# 1. 指定自己nfs服务器地址
spec.template.spec.containers[0].env[1].value: 172.31.0.2

# 2. 指定自己nfs服务器地址
spec.template.spec.volumes[0].nfs.server: 172.31.0.2
```

然后 apply ： 

```sh
kubectl apply -f sc.yaml
```

### 测试(选做)

1. 新创建 pvc.yaml 文件：

```yaml
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: nginx-pvc
spec:
  accessModes:
    - ReadWriteMany
  resources:
    requests:
      storage: 200Mi
```

2. 然后 apply：

```sh
kubectl apply -f pvc.yaml
```

3. 最后查看 pvc ：

```sh
kubectl get pvc
```

## metrics-server

安装集群指标监控组件 [metrics-server.yaml](assets\data\metrics-server.yaml) ：

```sh
kubectl apply -f metrics-server.yaml
```

## 部署 KubeSphere

### 下载

参考 [在 Kubernetes 上最小化安装 KubeSphere](https://kubesphere.io/zh/docs/quick-start/minimal-kubesphere-on-k8s/) ，可以知道安装 KubeSphere 需要 [kubesphere-installer.yaml](assets\data\kubesphere-installer.yaml) 、 [cluster-configuration.yaml](assets\data\cluster-configuration.yaml) 两个文件，我们先把它们下载下来：

```sh
wget https://github.com/kubesphere/ks-installer/releases/download/v3.1.1/kubesphere-installer.yaml

wget https://github.com/kubesphere/ks-installer/releases/download/v3.1.1/cluster-configuration.yaml
```

### 修改 cluster-configuration.yaml

```yaml
spec.etcd.monitoring: true
# 我的 master 节点为 172.31.0.2 
spec.etcd.endpointIps: 172.31.0.2
spec.common.redis.enabled: true
spec.common.openldap.enabled: true
spec.alerting.enabled: true
spec.auditing.enabled: true
spec.devops.enabled: true
spec.events.enabled: true
spec.logging.enabled: true
spec.network.networkpolicy.enabled: true
spec.network.ippool.type: calico
spec.openpitrix.store.enabled: true
spec.servicemesh.enabled: true
```

修改完毕后： [cluster-configuration-modified.yaml](assets\data\cluster-configuration-modified.yaml) 

### apply

```sh
kubectl apply -f kubesphere-installer.yaml
kubectl apply -f cluster-configuration.yaml
```

### 解决etcd监控证书找不到问题

```sh
# 主节点运行
kubectl -n kubesphere-monitoring-system create secret generic kube-etcd-client-certs  --from-file=etcd-client-ca.crt=/etc/kubernetes/pki/etcd/ca.crt  --from-file=etcd-client.crt=/etc/kubernetes/pki/apiserver-etcd-client.crt  --from-file=etcd-client.key=/etc/kubernetes/pki/apiserver-etcd-client.key
```

### 访问

 **http://集群任意IP:30880** ，比如 http://172.31.0.2:30880 

### 重新设置密码(选做)

如果你忘记密码，可以在主节点执行以下命令重新设置密码：

```sh
kubectl patch users <username> -p '{"spec":{"password":"<password>"}}' --type='merge' && kubectl annotate users <username> iam.kubesphere.io/password-encrypted-
```















