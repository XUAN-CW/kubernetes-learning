---
title: 服务器准备
tags: 
date: 2022-01-03 21:06:02
id: 1641215162829355800
---
# 概述

准备三台 **centOS 7.9** 服务器

# 服务器要求

对于一个初学者来说，一定不要自作聪明，下面的配置一个都不能错！（高手当我放屁）

|          | k8s-master | k8s-node1  | k8s-node2  |
| -------- | ---------- | ---------- | ---------- |
| 系统     | CentOS 7.9 | CentOS 7.9 | CentOS 7.9 |
| IP       | 172.31.0.2 | 172.31.0.3 | 172.31.0.4 |
| hostname | k8s-master | k8s-node1  | k8s-node2  |
| 内存     | 4G 以上    | 4G 以上    | 4G 以上    |

# 提示

## hostname 设置方法

```sh
# hostname set-hostname [想要设置名称]
#设置 hostname 为 k8s-master
hostnamectl set-hostname k8s-master
#设置 hostname 为 k8s-node1
hostnamectl set-hostname k8s-node1
#设置 hostname 为 k8s-node2
hostnamectl set-hostname k8s-node2
```



















