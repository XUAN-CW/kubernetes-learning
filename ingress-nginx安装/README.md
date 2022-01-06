---
title: ingress-nginx安装
tags: 
- ingress-nginx
- 安装
date: 2022-01-04 16:11:07
id: 1641283867823595200
---
# 摘要

在 **kubernetes:v1.20.9** 下安装 **ingress-nginx:0.47** 

# 安装

## 下载  [deploy.yaml](assets\data\deploy.yaml) 

1. deploy.yaml 实在下载不下来，我这里有一份 [deploy.yaml](assets\data\deploy.yaml) 

```sh
# 在 master 节点运行

wget https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v0.47.0/deploy/static/provider/baremetal/deploy.yaml

```

## 修改 [deploy.yaml](assets\data\deploy.yaml) 

使用 `cat deploy.yaml | grep image:` 命令可知，需要两个镜像：

```
k8s.gcr.io/ingress-nginx/controller:v0.46.0@sha256:52f0058bed0a17ab0fb35628ba97e8d52b5d32299fbc03cc0f6c7b9ff036b61a
docker.io/jettech/kube-webhook-certgen:v1.5.1
```

由于 **k8s.gcr.io** 无法访问，所以我们需要更换镜像源：

```sh
# 在 master 节点运行

# 1. 编辑 deploy.yaml
vi deploy.yaml
# 2. 把 k8s.gcr.io/ingress-nginx/controller:v0.46.0@sha256:52f0058bed0a17ab0fb35628ba97e8d52b5d32299fbc03cc0f6c7b9ff036b61a 修改为 registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/ingress-nginx-controller:v0.46.0
```

如果你要搞到内网上，那你可以参考  [离线部署kubernetes ingress nginx controller.html](assets\references\离线部署kubernetes ingress nginx controller.html) 

## 安装

安装前，建议你先拉取需要的镜像：

```sh
# 从节点运行

docker pull docker.io/jettech/kube-webhook-certgen:v1.5.1
docker pull registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/ingress-nginx-controller:v0.46.0
```

拉取完毕后再运行：

```sh
# 在 master 节点运行

kubectl apply -f deploy.yaml

```

# 查看

```
[root@k8s-master ~]# kubectl get pod -n ingress-nginx 
NAME                                        READY   STATUS      RESTARTS   AGE
ingress-nginx-admission-create-wbrpm        0/1     Completed   0          2m16s
ingress-nginx-admission-patch-qlqr8         0/1     Completed   0          2m16s
ingress-nginx-controller-65bf56f7fc-jv7jx   1/1     Running     0          2m16s
[root@k8s-master ~]# kubectl get svc -n ingress-nginx 
NAME                                 TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                      AGE
ingress-nginx-controller             NodePort    10.96.192.37    <none>        80:30886/TCP,443:31693/TCP   2m25s
ingress-nginx-controller-admission   ClusterIP   10.96.195.108   <none>        443/TCP                      2m26s
[root@k8s-master ~]#
```











