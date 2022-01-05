---
title: 修改kubernetes-dashboard的token过期时间
tags: 
date: 2022-01-05 08:18:55
id: 1641341935329697200
---
# 摘要



# 步骤

## 编辑kubernetes-dashboard

登录dashboard web端页面，切换到 **全部命名空间** ，找到 **kubenetes-dashboard**，编辑 ，如下图：

![image-20220105081955654](assets/images/image-20220105081955654.png)



## 添加参数

在如图位置中加上 `- '--token-ttl=43200'`  ，然后点击更新就修改成功了

![image-20220105081906321](assets/images/image-20220105081906321.png)
