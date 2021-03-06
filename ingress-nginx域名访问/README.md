---
title: ingress-nginx域名访问
tags: 
date: 2022-01-05 22:31:52
id: 1641393112526808400
---
# 摘要



# 环境

安装好  **ingress-nginx:0.47** 。其实先不安装也行，照样运行，只是 [测试](#测试) 时访问不到。我试过执行完下面这些，再安装 ingress ，一点问题都没有

# 步骤

## create deployment

### 准备镜像

为了避免出现由于网络原因导致的镜像拉取错误，建议先把需要的 tomcat 镜像拉取下来

```sh
# 在从节点执行

docker pull tomcat:8.5.73-jre8-openjdk-slim-bullseye
docker pull tomcat:9.0.56-jre8-openjdk-slim-bullseye
```

### deployment

```sh
# 在主节点执行

kubectl create deployment service1 --image=tomcat:8.5.73-jre8-openjdk-slim-bullseye

kubectl create deployment service2 --image=tomcat:9.0.56-jre8-openjdk-slim-bullseye

```

查看是否部署成功：

```
[root@k8s-master ~]# kubectl get deployment 
NAME       READY   UP-TO-DATE   AVAILABLE   AGE
service1   1/1     1            1           6m21s
service2   1/1     1            1           13m
```

## expose deployment

```sh
# 在主节点执行

kubectl expose deployment service1 --port=80 --target-port=8080
kubectl expose deployment service2 --port=80 --target-port=8080
```

查看是否暴露端口：

```
[root@k8s-master ~]# kubectl get svc
NAME         TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)        AGE
kubernetes   ClusterIP   10.96.0.1       <none>        443/TCP        47h
service1     NodePort    10.96.96.245    <none>        80:32626/TCP   14m
service2     NodePort    10.96.116.154   <none>        80:31920/TCP   14m
[root@k8s-master ~]# 
```

## Hostname wildcards

###  [ingress-wildcard-host.yaml](assets/data/ingress-wildcard-host.yaml) 

上传  [ingress-wildcard-host.yaml](assets/data/ingress-wildcard-host.yaml) 到主节点

### apply

```sh
# 在主节点执行

kubectl apply -f ingress-wildcard-host.yaml
```

查看是否完成 ingress-wildcard-host 设置：

```
[root@k8s-master ~]# kubectl describe ingress ingress-wildcard-host
Name:             ingress-wildcard-host
Namespace:        default
Address:          10.0.2.15
Default backend:  default-http-backend:80 (<error: endpoints "default-http-backend" not found>)
Rules:
  Host         Path  Backends
  ----         ----  --------
  foo.bar.com  
               /bar   service1:80 (192.168.169.138:8080)
  *.foo.com    
               /foo   service2:80 (192.168.36.73:8080)
Annotations:   <none>
Events:
  Type    Reason  Age                From                      Message
  ----    ------  ----               ----                      -------
  Normal  Sync    15m (x2 over 15m)  nginx-ingress-controller  Scheduled for sync
[root@k8s-master ~]#
```

# 测试

##  [hosts](C:\Windows\System32\drivers\etc\hosts) 

```
172.31.0.2 foo.bar.com
172.31.0.2 bar.foo.com
172.31.0.2 baz.bar.foo.com
172.31.0.2 foo.com
```

## 查看端口

```
[root@k8s-master ~]# kubectl get svc -n ingress-nginx | awk '{printf "%-40s%-15s\n",$1,$5}'
NAME                                    PORT(S)        
ingress-nginx-controller                80:32485/TCP,443:31259/TCP
ingress-nginx-controller-admission      443/TCP
```

记住 **ingress-nginx-controller** 对外暴露的端口 **32485** 

## 访问

### server1

1. http://foo.bar.com:32485/bar (访问成功)

### server2

1. http://bar.foo.com:32485/foo (访问成功)
2. http://baz.bar.foo.com:32485/foo (nginx 404 Not Found) 
3. http://foo.com:32485/foo (nginx 404 Not Found) 

# 直接暴露80端口

```
kubectl patch svc ingress-nginx-controller -n ingress-nginx  -p '{"spec": {"type": "LoadBalancer", "externalIPs":["172.31.0.2"]}}'
```

 [Load Balancer Service type for Kubernetes.html](assets\references\Load Balancer Service type for Kubernetes.html) 

# 参考

 [Ingress _ Kubernetes.html](assets/references/Ingress _ Kubernetes.html) 

 [Kubernetes进阶之Ingress.html](assets/references/Kubernetes进阶之Ingress.html) 









