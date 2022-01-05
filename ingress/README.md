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

因此，使用 docker pull 拉取镜像：

```sh
# 在 master 节点运行

# k8s.gcr.io 由于墙的原因访问不了，因此使用代替方案
# 下载镜像
docker pull registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/ingress-nginx-controller:v0.46.0
# 打标签
docker tag registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/ingress-nginx-controller:v0.46.0 k8s.gcr.io/ingress-nginx/controller:v0.46.0@sha256:52f0058bed0a17ab0fb35628ba97e8d52b5d32299fbc03cc0f6c7b9ff036b61a


docker pull jettech/kube-webhook-certgen:v1.5.1
```

## 安装

```sh
# 在 master 节点运行

kubectl apply -f deploy.yaml

```















