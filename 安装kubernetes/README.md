---
title: 安装kubernetes
tags: 
date: 2022-01-03 22:02:46
id: 1641218567019044600
---
# 概述

安装 kubernetes:v1.20.9



# 服务器准备

准备三台 **centOS 7.9** 服务器

## 服务器要求

对于一个初学者来说，一定不要自作聪明，下面的配置一个都不能错！（高手当我放屁）

|          | k8s-master | k8s-node1  | k8s-node2  |
| -------- | ---------- | ---------- | ---------- |
| 系统     | CentOS 7.9 | CentOS 7.9 | CentOS 7.9 |
| IP       | 172.31.0.2 | 172.31.0.3 | 172.31.0.4 |
| 内存     | 4G 以上    | 4G 以上    | 4G 以上    |
| 登录用户 | root       | root       | root       |
| CPU      | 至少两个   | 至少一个   | 至少一个   |
| hostname | k8s-master | k8s-node1  | k8s-node2  |

## 提示

### hostname 设置方法

```sh
# hostname set-hostname [想要设置名称]
#设置 hostname 为 k8s-master
hostnamectl set-hostname k8s-master
#设置 hostname 为 k8s-node1
hostnamectl set-hostname k8s-node1
#设置 hostname 为 k8s-node2
hostnamectl set-hostname k8s-node2
```

# kubeadm 式安装

## 添加 cluster-endpoint 映射

节点都使用 `ping cluster-endpoint` 命令 PING 通 master 节点为配置成功

```sh
# 主节点、从节点都添加 master 域名映射，我这里的 master 为 172.31.0.2
echo "172.31.0.2  cluster-endpoint" >> /etc/hosts

ping cluster-endpoint
```

## 添加 hostname 映射

这里要求各主机能 ping 通自己的 hostname 

```sh
# k8s-master 节点运行
echo "172.31.0.2  k8s-master" >> /etc/hosts

```

```sh
# k8s-node1 节点运行
echo "172.31.0.3  k8s-node1" >> /etc/hosts

```

```sh
# k8s-node2 节点运行
echo "172.31.0.4  k8s-node2" >> /etc/hosts

```

## docker-ce-20.10.7 安装

```sh
# 主节点、从节点运行
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

# 安装指定版本 docker 
sudo yum install -y docker-ce-20.10.7 docker-ce-cli-20.10.7  containerd.io-1.4.6
# 启动 docker
sudo systemctl start docker
sudo systemctl enable docker

```

## 环境准备

1. 安装 kubernetes 需要进行以下设置，我也不知道为什么，官网就是这么说的，跟着做吧

```sh
# 主节点、从节点运行

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

```sh
# 主节点、从节点运行

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

# 安装 kubelet、kubeadm、kubectl
sudo yum install -y kubelet-1.20.9 kubeadm-1.20.9 kubectl-1.20.9 --disableexcludes=kubernetes

# 启动
sudo systemctl enable --now kubelet

```

## 安装相关镜像

### 主节点

1. 这一步在国外不是必须的，因为后面会自动下载这些镜像
2. 这里我们使用阿里云的镜像来代替原来的镜像

```sh
docker pull registry.aliyuncs.com/google_containers/kube-apiserver:v1.20.9
docker pull registry.aliyuncs.com/google_containers/kube-controller-manager:v1.20.9
docker pull registry.aliyuncs.com/google_containers/kube-scheduler:v1.20.9
docker pull registry.aliyuncs.com/google_containers/kube-proxy:v1.20.9
docker pull registry.aliyuncs.com/google_containers/pause:3.2
docker pull registry.aliyuncs.com/google_containers/etcd:3.4.13-0
docker pull registry.aliyuncs.com/google_containers/coredns:1.7.0

```

如果你有离线 docker 镜像，你也可以使用下面的命令把镜像批量加载进来：

```sh
ls *docker_image* |sed -r 's#(.*)#docker load -i \1#' | bash

```

如果你想知道原来的镜像是什么，你可以运行 `kubeadm config images list [--kubernetes-version <version>]` 命令查看：

```
[root@k8s-master ~]# kubeadm config images list --kubernetes-version v1.20.9
k8s.gcr.io/kube-apiserver:v1.20.9
k8s.gcr.io/kube-controller-manager:v1.20.9
k8s.gcr.io/kube-scheduler:v1.20.9
k8s.gcr.io/kube-proxy:v1.20.9
k8s.gcr.io/pause:3.2
k8s.gcr.io/etcd:3.4.13-0
k8s.gcr.io/coredns:1.7.0
```

### 从节点

1. 这一步在国外不是必须的，因为后面会自动下载这些镜像
2. 这里我们使用阿里云的镜像来代替原来的镜像

```sh
# 在从节点安装镜像
docker pull calico/node:v3.21.2
docker pull registry.aliyuncs.com/google_containers/kube-proxy:v1.20.9
docker pull registry.aliyuncs.com/google_containers/pause:3.2
docker pull calico/pod2daemon-flexvol:v3.21.2

```

## 主节点初始化

只有主节点需要初始化

```sh
#主节点初始化,我的 master 节点为 172.31.0.2 
kubeadm init \
--apiserver-advertise-address=172.31.0.2 \
--control-plane-endpoint=cluster-endpoint \
--image-repository registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images \
--kubernetes-version v1.20.9 \
--service-cidr=10.96.0.0/16 \
--pod-network-cidr=192.168.0.0/16 
```

如果初始化成功，你应该能够看到类似于下面这样的东西：

```
... ...
Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config
... ...
kubeadm join cluster-endpoint:6443 --token ob55n2.owwcbxxjdix40zgu \
    --discovery-token-ca-cert-hash sha256:212a1f282b6ecbc35656702bc6c75c8638c66f6f5823b7bff49448f43b64ea30
```

## 设置.kube/config

1. 根据安装成功的提示，我们需要运行下面的命令
2. 只有主节点需要运行下面的命令

```sh
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

```

## 安装网络组件

1. 这里选择 [calico](https://docs.projectcalico.org/getting-started/kubernetes/self-managed-onprem/onpremises#install-calico-with-kubernetes-api-datastore-more-than-50-nodes) 
2. 只有主节点需要安装网络组件
2. calico.yaml 实在下载不下来，我这里有一份  [calico.yaml](assets/data/calico.yaml) 

```bash
curl https://docs.projectcalico.org/manifests/calico.yaml -O

kubectl apply -f calico.yaml

```

## 从节点加入集群

### 创建令牌

主节点中创建令牌：

```
[root@k8s-master ~]# kubeadm token create --print-join-command
kubeadm join cluster-endpoint:6443 --token 9kto10.1u0e74ypj3ag4rix     --discovery-token-ca-cert-hash sha256:dd4160970cd55d5687d16ee417adad3e697dc189911d3f06a7e4dbf1066934c9 
[root@k8s-master ~]# 
```

### 从节点加入集群

复制主节点中的令牌，然后在所有从节点中执行，从节点执行成功后，你可以看到：

```
[root@k8s-node1 ~]# kubeadm join cluster-endpoint:6443 --token 9kto10.1u0e74ypj3ag4rix     --discovery-token-ca-cert-hash sha256:dd4160970cd55d5687d16ee417adad3e697dc189911d3f06a7e4dbf1066934c9 
[preflight] Running pre-flight checks
	[WARNING IsDockerSystemdCheck]: detected "cgroupfs" as the Docker cgroup driver. The recommended driver is "systemd". Please follow the guide at https://kubernetes.io/docs/setup/cri/
	[WARNING SystemVerification]: this Docker version is not on the list of validated versions: 20.10.7. Latest validated version: 19.03
[preflight] Reading configuration from the cluster...
[preflight] FYI: You can look at this config file with 'kubectl -n kube-system get cm kubeadm-config -o yaml'
[kubelet-start] Writing kubelet configuration to file "/var/lib/kubelet/config.yaml"
[kubelet-start] Writing kubelet environment file with flags to file "/var/lib/kubelet/kubeadm-flags.env"
[kubelet-start] Starting the kubelet
[kubelet-start] Waiting for the kubelet to perform the TLS Bootstrap...

This node has joined the cluster:
* Certificate signing request was sent to apiserver and a response was received.
* The Kubelet was informed of the new secure connection details.

Run 'kubectl get nodes' on the control-plane to see this node join the cluster.

[root@k8s-node1 ~]# 
```

1. 在主节点执行 `kubectl get nodes` 可以查看集群节点状态
2. 运行中的应用在 docker 里面叫容器，在k8s里面叫Pod，在主节点中使用 `kubectl get pods -A` 可查看运行中的应用。若所有应用 STATUS 为 Running ，则表示这一步成功。在这一步，我遇到过 [ImagePullBackOff](#ImagePullBackOff) 



# error

## ImagePullBackOff

### error 原因

`kubectl get pods -A`  出现 `Init:ImagePullBackOff` 或`Init:ErrImagePull` 就是说你镜像拉取失败了

### 解决方案

1. 主节点执行 `kubectl get pods -A -o wide` ，查看是哪台机器哪个 pod 拉取失败
2. 主节点 [describe](https://kubernetes.io/docs/reference/generated/kubectl/kubectl-commands#describe) 拉取失败的 pod ，查看是哪个 image 缺失
3. 自行 docker pull 缺失的 image 

当时我是有一台服务器成功拉取下来了，然后我 docker save 了镜像，再上传到拉取失败的服务器上

### 参考

 [ImagePullBackOff 错误处理.html](assets\references\ImagePullBackOff 错误处理.html) 



## connect: no route to host

### 表现

如果出现从节点链接不上子节点，那可能就是网络出问题了，会有下面的表现

```
[root@k8s-node1 ~]# kubeadm join cluster-endpoint:6443 --token as0p1b.gngpce4upd7hwy3t     --discover  y-token-ca-cert-hash sha256:6a026748b0a16966abafc22bdbbc44e34052fb49e3addaed94ca81ae7ee4502722
[preflight] Running pre-flight checks
        [WARNING IsDockerSystemdCheck]: detected "cgroupfs" as the Docker cgroup driver. The recomm  ended driver is "systemd". Please follow the guide at https://kubernetes.io/docs/setup/cri/
        [WARNING SystemVerification]: this Docker version is not on the list of validated versions:   20.10.7. Latest validated version: 19.03
error execution phase preflight: couldn't validate the identity of the API Server: expected a 32 by  te SHA-256 hash, found 33 bytes
To see the stack trace of this error execute with --v=5 or higher
[root@centos7 ~]# kubeadm join cluster-endpoint:6443 --token as0p1b.gngpce4upd7hwy3t     --discover  y-token-ca-cert-hash sha256:6a026748b0a16966abafc22bdbbc44e34052fb49e3addaed94ca81ae7ee45027
[preflight] Running pre-flight checks
        [WARNING IsDockerSystemdCheck]: detected "cgroupfs" as the Docker cgroup driver. The recomm  ended driver is "systemd". Please follow the guide at https://kubernetes.io/docs/setup/cri/
        [WARNING SystemVerification]: this Docker version is not on the list of validated versions:   20.10.7. Latest validated version: 19.03
error execution phase preflight: couldn't validate the identity of the API Server: Get "https://clu  ster-endpoint:6443/api/v1/namespaces/kube-public/configmaps/cluster-info?timeout=10s": dial tcp 172  .31.0.2:6443: connect: no route to host
To see the stack trace of this error execute with --v=5 or higher

```

### 原因

使用下面的命令测试，发现连接不上

```
[root@k8s-node1 ~]# nc -zv 172.31.0.4 6443
Ncat: Version 7.50 ( https://nmap.org/ncat )
Ncat: Connection refused.

```

### 解决方法

```sh
# To clear up the firewall rules for an external firewall, you can use this command

sudo iptables -F 
```

### 参考

 [‘No Route to Host’ Error in Linux.html](assets\references\‘No Route to Host’ Error in Linux.html) 

# 参考

 [How To Setup Kubernetes Cluster Using Kubeadm.html](assets\references\How To Setup Kubernetes Cluster Using Kubeadm.html) 









