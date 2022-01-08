---
title: 安装kubernetes-dashboard
tags: 
date: 2022-01-07 20:25:14
id: 1641558314499927500
---
# 摘要



# 部署 dashboard 

## 安装

1. kubernetes 官方提供的可视化界面，https://github.com/kubernetes/dashboard 
2. recommended.yaml 实在下载不下来，我这里有一份  [recommended.yaml](assets/data/recommended.yaml) 
3. 在主节点部署 dashboard 

```sh
# 如有必要,科学上网
wget https://raw.githubusercontent.com/kubernetes/dashboard/v2.3.1/aio/deploy/recommended.yaml

kubectl apply -f recommended.yaml
```

## 设置访问端口

在主节点中运行：

```sh
kubectl edit svc kubernetes-dashboard -n kubernetes-dashboard
```

然后在出现的文本中做出如下修改

```
type: ClusterIP 改为 type: NodePort
```

## 查看访问端口

```
[root@k8s-master ~]# kubectl get svc -A |grep kubernetes-dashboard
kubernetes-dashboard   dashboard-metrics-scraper   ClusterIP   10.96.193.91    <none>        8000/TCP                 19m
kubernetes-dashboard   kubernetes-dashboard        NodePort    10.96.186.152   <none>        443:31634/TCP            19m
```

由此可知，访问端口为 `31634` ，注意。端口每次都不一样，我这里是 `31634` 

# 访问

1.  https://集群任意IP:端口 。我这里用主节点 IP `172.31.0.2` ，端口用上面的 `31634` ，则有  https://172.31.0.2:31634 
2.  面临潜在的安全风险。不理他，直接继续就好
3.  看到下图，表示到目前为止是成功的

![image-20220104014153882](assets/images/image-20220104014153882.png)

## 创建访问账号

### dash.yaml

主节点中创建 dash.yaml ：

```yaml
# 创建访问账号，准备一个yaml文件； vi dash.yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: admin-user
  namespace: kubernetes-dashboard
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-user
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: admin-user
  namespace: kubernetes-dashboard
```

### kubectl apply

主节点中 kubectl apply 

```
kubectl apply -f dash.yaml
```

## 登录

### 获取访问令牌

主节点中获取访问令牌

```sh
kubectl -n kubernetes-dashboard get secret $(kubectl -n kubernetes-dashboard get sa/admin-user -o jsonpath="{.secrets[0].name}") -o go-template="{{.data.token | base64decode}}"
```

### token 登录

输入 token 后登录即可
