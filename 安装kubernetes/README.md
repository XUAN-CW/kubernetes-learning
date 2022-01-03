---
title: 安装kubernetes
tags: 
date: 2022-01-03 22:02:46
id: 1641218567019044600
---
# 概述



# 安装步骤

## 环境准备

1. 安装 kubernetes 需要进行以下设置，我也不知道为什么，官网就是这么说的，跟着做吧
2. 主从节点都要进行下面的设置

```sh
# 将 SELinux 设置为 permissive 模式（相当于将其禁用）
sudo setenforce 0
sudo sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config

#关闭swap
swapoff -a  
sed -ri 's/.*swap.*/#&/' /etc/fstab

#允许 iptables 检查桥接流量
cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
br_netfilter
EOF
cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF

# 生效
sudo sysctl --system

```

## 安装 kubelet、kubeadm、kubectl 

主从节点都要安装 kubelet、kubeadm、kubectl 

```sh
# 设置 yum 源
cat <<EOF | sudo tee /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=http://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg
   http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
exclude=kubelet kubeadm kubectl
EOF

# 安装
sudo yum install -y kubelet-1.20.9 kubeadm-1.20.9 kubectl-1.20.9 --disableexcludes=kubernetes

# 启动
sudo systemctl enable --now kubelet

```

## 安装相关镜像

1. 其实这一步不是必须的，因为后面会自动下载这些镜像，但由于可能出现网络问题，不知道卡在哪里了，所以我们先准备好镜像
2. 主从节点都要安装下面的镜像

```sh
docker pull registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/kube-apiserver:v1.20.9
docker pull registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/kube-proxy:v1.20.9
docker pull registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/kube-controller-manager:v1.20.9
docker pull registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/kube-scheduler:v1.20.9
docker pull registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/coredns:1.7.0
docker pull registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/etcd:3.4.13-0
docker pull registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/pause:3.2
```























