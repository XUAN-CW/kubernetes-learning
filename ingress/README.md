---
title: ingress
tags: 
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

## 准备镜像

下面的 [安装](#安装) 需要使用镜像，为了防止网络原因，先准备好镜像。使用 `cat deploy.yaml | grep image:` 命令可知，需要两个镜像：

```
k8s.gcr.io/ingress-nginx/controller:v0.46.0@sha256:52f0058bed0a17ab0fb35628ba97e8d52b5d32299fbc03cc0f6c7b9ff036b61a
docker.io/jettech/kube-webhook-certgen:v1.5.1
```

如果你要搞到内网上，那你可以参考  [离线部署kubernetes ingress nginx controller.html](assets\references\离线部署kubernetes ingress nginx controller.html) ，如果你有网络那你更换镜像源即可：

```sh
# 在 master 节点运行

# 1. 编辑 deploy.yaml
vi deploy.yaml
# 2. 把 k8s.gcr.io/ingress-nginx/controller:v0.46.0@sha256:52f0058bed0a17ab0fb35628ba97e8d52b5d32299fbc03cc0f6c7b9ff036b61a 修改为 registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/ingress-nginx-controller:v0.46.0
```

## 安装

```sh
# 在 master 节点运行

kubectl apply -f deploy.yaml

```

# 查看

```
[root@k8s-master ~]# kubectl get pod,svc -n ingress-nginx
NAME                                            READY   STATUS      RESTARTS   AGE
pod/ingress-nginx-admission-create-zlrs7        0/1     Completed   0          95s
pod/ingress-nginx-admission-patch-ffg2z         0/1     Completed   0          95s
pod/ingress-nginx-controller-65bf56f7fc-lsc7t   1/1     Running     0          95s

NAME                                         TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                      AGE
service/ingress-nginx-controller             NodePort    10.96.213.191   <none>        80:32485/TCP,443:31259/TCP   95s
service/ingress-nginx-controller-admission   ClusterIP   10.96.103.198   <none>        443/TCP                      95s
[root@k8s-master ~]# 

```











