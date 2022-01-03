---
title: Kubernetes-learning
tags: 
date: 2022-01-03 16:50:49
id: 1641199849546655200
---
# 概述

# 环境

## 内存

8G 

## CentOS 

```
[root@10 ~]# cat /etc/redhat-release
CentOS Linux release 7.9.2009 (Core)
```

VirtualBox 6.1.26 + Vagrant 2.2.18 

## Docker 

```sh
#################### 安装docker ####################
# 移除之前安装好的 docker
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
# 配置 yum 源
sudo yum install -y yum-utils
sudo yum-config-manager \
--add-repo \
http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

# s安装指定版本 docker 
sudo yum install -y docker-ce-20.10.7 docker-ce-cli-20.10.7  containerd.io-1.4.6
# 启动 docker
sudo systemctl start docker
sudo systemctl enable docker
#################### 设置k8s 基础环境 ####################
# 将 SELinux 设置为 permissive 模式（相当于将其禁用）
sudo setenforce 0
sudo sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config

# 关闭swap
swapoff -a
sed -ri 's/.*swap.*/#&/' /etc/fstab

# 允许 iptables 检查桥接流量
cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
br_netfilter
EOF

cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
# 配置生效
sudo sysctl --system

#################### 安装kubelet、kubeadm、kubectl ####################
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


sudo yum install -y kubelet-1.20.9 kubeadm-1.20.9 kubectl-1.20.9 --disableexcludes=kubernetes

sudo systemctl enable --now kubelet




# 下载镜像
sudo tee ./images.sh <<-'EOF'
#!/bin/bash
images=(
kube-apiserver:v1.20.9
kube-proxy:v1.20.9
kube-controller-manager:v1.20.9
kube-scheduler:v1.20.9
coredns:1.7.0
etcd:3.4.13-0
pause:3.2
)
for imageName in ${images[@]} ; do
docker pull registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/$imageName
done
EOF

chmod +x ./images.sh && ./images.sh

#所有机器添加master域名映射，以下需要修改为自己的
echo "172.31.0.2  cluster-endpoint" >> /etc/hosts


```

---

## IP

172.31.0.2(k8s-master)、172.31.0.3(k8s-node1)、172.31.0.4(k8s-node1) 

## hostname

```
hostnamectl set-histname k8s-master
hostnamectl set-histname k8s-node1
hostnamectl set-histname k8s-node2
```

# 主节点

```
kubeadm init \
--apiserver-advertise-address=172.31.0.2 \
--control-plane-endpoint=cluster-endpoint \
--image-repository registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images \
--kubernetes-version v1.20.9 \
--service-cidr=10.96.0.0/16 \
--pod-network-cidr=192.168.0.0/16
```





# 参考

 [云原生Java架构师的第一课K8s+Docker+KubeSphere+DevOps](https://www.bilibili.com/video/BV13Q4y1C7hS) 
