#!/bin/bash
## 设置 已经包含应用的Node为不可调度
namespace=$1
deployment=$2
for i in $(kubectl get node |grep Ready|awk '{print $1}') 
do
    echo $i
    check_results=$(kubectl describe node $i | grep ${deployment} | awk "{print $2}"|wc -l)
    if (( $check_results > 0 )); then
       echo $check_results
       kubectl cordon $i 
    fi
done
## 删除多个 POD 部署的
for i in $(kubectl get nodes |grep Ready|awk '{print $1}')
do
    echo $i
    check_results=$(kubectl describe node $i | grep ${deployment} | awk "{print $2}"|wc -l)
    if (( $check_results > 1 )); then
       echo $check_results
       check_results=$(kubectl describe node $i | grep ${deployment} | awk '{print $2}'|head -1)
       echo $check_results
       kubectl delete pods  $check_results -n ${namespace} --grace-period=0 --force
    fi
done

## 设置 恢复Node 为可调度
for i in $(kubectl get nodes|grep Ready|awk '{print $1}') 
do
    echo $i
    kubectl uncordon $i 
done
