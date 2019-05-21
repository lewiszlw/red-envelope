# 简介
红包秒杀系统：模拟红包秒杀时的高并发场景，学习并实现业界成熟的秒杀架构。

[调研设计笔记](https://github.com/lewiszlw/notebooks/tree/master/wheels/red-envelope)
# 启动
1.安装并启动rocketmq

http://rocketmq.apache.org/docs/quick-start/
   
```shell
nohup sh bin/mqnamesrv &
nohup sh bin/mqbroker -n localhost:9876 &
```

2.安装并启动reids

https://redis.io/download

```shell
./src/redis-server
```

3.启动本项目

运行RedEnvelopeApplication main方法

4.创建红包

POST http://localhost:8080/red-envelope/create

请求body:
```json
{
	"user": "lewiszlw",
	"type": "ORDINARY",
	"amount": 10000,
	"size": 5
}
```

5.抢红包

GET http://localhost:8080/red-envelope/grab?envelopeId=1&grabber=lewis

注：envelopeId 为红包id，grabber 为抢红包者

6.验证结果

GET http://localhost:8080/red-envelope/verify?envelopeIds=3,4,5,6

# 压测
