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

## 提供动态供应能力

根据 [官网](https://github.com/kubernetes-sigs/nfs-subdir-external-provisioner) 资料修改后得到 [sc.yaml](assets\data\sc.yaml) ， 主节点中 apply [sc.yaml](assets\data\sc.yaml) ，需要修改两个地方，其他不用修改：

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

# MySQL

```yaml

apiVersion: v1
kind: Service
metadata:
  name: nginx
  labels:
    app: nginx
spec:
  ports:
  - port: 80
    name: web
  clusterIP: None
  selector:
    app: nginx
---

apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: web
spec:
  selector:
    matchLabels:
      app: nginx # has to match .spec.template.metadata.labels
  serviceName: "nginx"  #声明它属于哪个Headless Service.
  replicas: 3 # by default is 1
  template:
    metadata:
      labels:
        app: nginx # has to match .spec.selector.matchLabels
    spec:
      terminationGracePeriodSeconds: 10
      containers:
      - name: nginx
        image: nginx:1.16.1
        ports:
        - containerPort: 80
          name: web
        volumeMounts:
        - name: www
          mountPath: /usr/share/nginx/html
  volumeClaimTemplates:   #可看作pvc的模板
  - metadata:
      name: www
    spec:
      accessModes: [ "ReadWriteOnce" ]
      resources:
        requests:
          storage: 1Gi
```











