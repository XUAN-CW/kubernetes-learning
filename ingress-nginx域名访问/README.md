---
title: ingress-nginx域名访问
tags: 
date: 2022-01-05 22:31:52
id: 1641393112526808400
---
# 摘要



# 步骤

## create deployment

```sh
# 建议先把需要的 tomcat 镜像拉取下来
kubectl create deployment service1 --image=tomcat8.5.46-jdk8-corretto --port=8080 
kubectl create deployment service2 --image=tomcat:9.0.45-jdk8-corretto --port=8080 


kubectl get deployment 
```

## expose deployment

```

kubectl expose deployment service1 --port=80 --target-port=8080 --type=NodePort 
kubectl expose deployment service2 --port=80 --target-port=8080 --type=NodePort 



kubectl get svc
```

## Hostname wildcards

 [ingress-wildcard-host.yaml](assets/data/ingress-wildcard-host.yaml) 

```sh
kubectl apply -f ingress-wildcard-host.yaml
```



```sh
kubectl describe ingress ingress-wildcard-host
```



# 测试

##  [hosts](C:\Windows\System32\drivers\etc\hosts) 

## 查看端口

```
kubectl get svc -n ingress-nginx
```



## 访问



http://foo.bar.com:32485 

http://bar.foo.com:32485





# 参考

 [Ingress _ Kubernetes.html](assets/references/Ingress _ Kubernetes.html) 

 [Kubernetes进阶之Ingress.html](assets/references/Kubernetes进阶之Ingress.html) 









