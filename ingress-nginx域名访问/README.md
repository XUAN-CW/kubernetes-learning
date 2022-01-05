---
title: ingress-nginx域名访问
tags: 
date: 2022-01-05 22:31:52
id: 1641393112526808400
---
# 摘要



# 步骤

## create deployment

### 准备镜像

为了避免出现由于网络原因导致的镜像拉取错误，建议先把需要的 tomcat 镜像拉取下来

```sh
# 在从节点执行

docker pull tomcat:8.5.46-jdk8-corretto
docker pull tomcat:9.0.45-jdk8-corretto
```

### deployment

```sh
# 在主节点执行

kubectl create deployment service1 --image=tomcat:8.5.46-jdk8-corretto --port=8080 
kubectl create deployment service2 --image=tomcat:9.0.45-jdk8-corretto --port=8080 

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

kubectl expose deployment service1 --port=80 --target-port=8080 --type=NodePort 
kubectl expose deployment service2 --port=80 --target-port=8080 --type=NodePort 
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

## 查看端口

```
[root@k8s-master ~]# kubectl get svc -n ingress-nginx
NAME                                 TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                      AGE
ingress-nginx-controller             NodePort    10.96.213.191   <none>        80:32485/TCP,443:31259/TCP   13h
ingress-nginx-controller-admission   ClusterIP   10.96.103.198   <none>        443/TCP                      13h
[root@k8s-master ~]# 
```



## 访问



http://foo.bar.com:32485/bar

http://bar.foo.com:32485/foo





# 参考

 [Ingress _ Kubernetes.html](assets/references/Ingress _ Kubernetes.html) 

 [Kubernetes进阶之Ingress.html](assets/references/Kubernetes进阶之Ingress.html) 









