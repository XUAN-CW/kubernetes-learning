---
title: StatefulSet
tags: 
date: 2022-04-02 14:53:41
id: 1648882421382045100
---
# 摘要



```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: mysql-set
  labels:
    app: mysql-app
spec:
  selector:
    matchLabels:
      octopusexport: OctopusExport
  replicas: 3
  updateStrategy:
    type: OnDelete
  serviceName: mysql-service
  podManagementPolicy: OrderedReady
  volumeClaimTemplates: []
  template:
    metadata:
      labels:
        app: mysql-app
        octopusexport: OctopusExport
    spec:
      dnsPolicy: Default
      hostNetwork: true
      containers:
        - name: mysql
          image: 'mysql:5.7.30'
          ports:
            - name: mysql-port
              containerPort: 3306
          env:
            - name: MYSQL_RANDOM_ROOT_PASSWORD
              value: root
            - name: MYSQL_ROOT_PASSWORD
              value: root

```

