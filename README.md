# 启动
1.安装rocketmq

http://rocketmq.apache.org/docs/quick-start/
   
2.启动rocketmq
```bash
nohup sh bin/mqnamesrv &
nohup sh bin/mqbroker -n localhost:9876 &
```