---
title: 部署busybox
tags: 
date: 2022-03-24 21:16:31
id: 1648127791830393300
---
# 摘要



# 最终解决方案



```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: busybox
spec:
  selector:
    matchLabels:
      octopusexport: OctopusExport
  replicas: 1
  strategy:
    type: RollingUpdate
  template:
    metadata:
      labels:
        octopusexport: OctopusExport
    spec:
      containers:
        - name: busybox
          image: 'busybox:1.35-glibc'
          command: ["/bin/sh", "-ce", "tail -f /dev/null"]
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
            - weight: 100
              podAffinityTerm:
                labelSelector:
                  matchExpressions:
                    - key: app
                      operator: In
                      values:
                        - web
                topologyKey: kubernetes.io/hostname


```



# 一键部署

```
cat << EOF > busybox.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: busybox
spec:
  selector:
    matchLabels:
      octopusexport: OctopusExport
  replicas: 1
  strategy:
    type: RollingUpdate
  template:
    metadata:
      labels:
        octopusexport: OctopusExport
    spec:
      containers:
        - name: busybox
          image: 'busybox:1.35-glibc'
          command: ["/bin/sh", "-ce", "tail -f /dev/null"]
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
            - weight: 100
              podAffinityTerm:
                labelSelector:
                  matchExpressions:
                    - key: app
                      operator: In
                      values:
                        - web
                topologyKey: kubernetes.io/hostname
EOF

kubectl apply -f busybox.yaml
kubectl get pod
```



# debug 过程

## busybox 直接部署

```
kubectl create deployment busybox --image=busybox:1.34-glibc
```

## 查看发现 CrashLoopBackOff

```
[root@k8s-master ~]# kubectl get pod
NAME                       READY   STATUS             RESTARTS   AGE
busybox-588dcd5dd8-8l4lf   0/1     CrashLoopBackOff   1          6s
```



## describe

describe 后发现 `Back-off restarting failed container` ，这是说 Pod 运行了，但已经运行结束了

```
[root@k8s-master ~]# kubectl describe pod  busybox-588dcd5dd8-8l4lf

... ...

  Normal   Created    16s (x4 over 57s)  kubelet            Created container busybox
  Normal   Started    16s (x4 over 57s)  kubelet            Started container busybox
  Warning  BackOff    3s (x6 over 56s)   kubelet            Back-off restarting failed container

```

## 解决方案

因此在yaml文件中指定一个启动命令，内容如下：

```
      command: ["/bin/sh", "-ce", "tail -f /dev/null"]
```

在 yaml 中的位置如下：

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: cmds
spec:
  containers:
    - name: cmds
      image: cmd:v1
      command: ["/bin/sh", "-ce", "tail -f /dev/null"]
      securityContext:
        privileged: true
  nodeSelector:
      kubernetes.io/hostname: cn-beijing.10.0.0.19
```

# 参考

 [k8s启动Pod遇到CrashLoopBackOff的解决方法.html](assets\references\k8s启动Pod遇到CrashLoopBackOff的解决方法.html) 

 [Pod报错——Back-off restarting failed container 的解决办法.html](assets\references\Pod报错——Back-off restarting failed container 的解决办法.html) 







