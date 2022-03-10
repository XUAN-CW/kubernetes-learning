---
title: ImagePullBackOff
tags: 
date: 2022-03-10 09:57:34
id: 1646877454119019600
---
# 摘要



# error 原因

`kubectl get pods -A`  出现 `Init:ImagePullBackOff` 或`Init:ErrImagePull` 就是说你镜像拉取失败了

# 解决方案

1. 主节点执行 `kubectl get pods -A -o wide` ，查看是哪台机器哪个 pod 拉取失败
2. 主节点 [describe](https://kubernetes.io/docs/reference/generated/kubectl/kubectl-commands#describe) 拉取失败的 pod ，查看是哪个 image 缺失
3. 自行 docker pull 缺失的 image 

当时我是有一台服务器成功拉取下来了，然后我 docker save 了镜像，再上传到拉取失败的服务器上

# 参考

 [ImagePullBackOff 错误处理.html](assets\references\ImagePullBackOff 错误处理.html) 
