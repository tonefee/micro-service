# 核心思想
1、对musql的修改，添加，删除会产生binlog日志。  
2、对mysql binlog(row) parser 这一步交给canal。  
3、MQ对解析后binlog增量数据进行推送。  
4、redis等相关的nosql对MQ数据进行消费（接收+数据解析，考虑消费速度，MQ队列的阻塞），数据写入/修改到nosql 。 

binlog为记录触发事件，canal的作用是将事件实时通知出来， 并将binlog解析成所有语言都可读，
在事件传输的各个环节 提高 可用性 和 扩展性 （加入MQ等方法）最终提高系统的稳定。  

![数据同步](pictures/p1.png)  

问题1：为什么要使用消息队列（MQ）进行binlog传输？
回答： 增加缓冲，binlog生产端（canal client）只负责生产而不需要考虑消费端的消费能力, 不等待阻塞。  
binlog 消费端: 可实时根据MQ消息的堆积情况，动态 增加/减少 消费端的数量，达到合理的资源利用和消费。  

# 日志增量订阅和消费的业务场景
数据库镜像  
数据库实时备份  
索引构建和实时维护(拆分异构索引、倒排索引等)  
业务 cache 刷新  
带业务逻辑的增量数据处理  

# canal [kə'næl]介绍
译意： 水道/管道/沟渠  
产品定位： 基于数据库增量日志解析，提供增量数据订阅和消费    

## 工作原理
MySQL主备复制原理  

![数据同步](pictures/p2.png)    

MySQL master 将数据变更写入二进制日志( binary log, 其中记录叫做二进制日志事件binary log events，可以通过 show binlog events 进行查看)。  
MySQL slave 将 master 的 binary log events 拷贝到它的中继日志(relay log)。  
MySQL slave 重放 relay log 中事件，将数据变更反映它自己的数据。  

canal 工作原理  

![数据同步](pictures/p3.png)   

canal 模拟 MySQL slave 的交互协议，伪装自己为 MySQL slave ，向 MySQL master 发送dump 协议。  
MySQL master 收到 dump 请求，开始推送 binary log 给 slave (即 canal )。  
canal 解析 binary log 对象(原始为 byte 流) 。  

# 准备
对于自建 MySQL , 需要先开启 Binlog 写入功能，配置 binlog-format 为 ROW 模式，my.cnf 中配置如下：  
```
[mysqld]
log-bin=mysql-bin # 开启 binlog
binlog-format=ROW # 选择 ROW 模式
server_id=1 # 配置 MySQL replaction 需要定义，不要和 canal 的 slaveId 重复
```
注意：针对阿里云 RDS for MySQL , 默认打开了 binlog , 并且账号默认具有 binlog dump 权限 , 不需要任何权限或者 binlog 设置,可以直接跳过这一步。  
授权 canal 链接 MySQL 账号具有作为 MySQL slave 的权限, 如果已有账户可直接 grant。  
```
CREATE USER canal IDENTIFIED BY 'canal';  
FLUSH PRIVILEGES;
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
-- GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' ;
FLUSH PRIVILEGES;
```
# canal server 安装并启动
下载 canal, [访问 release 页面](https://github.com/alibaba/canal/releases) , 选择需要的包下载, 如以 1.0.17 版本为例。  
```
wget https://github.com/alibaba/canal/releases/download/canal-1.0.17/canal.deployer-1.0.17.tar.gz  

```
解压缩  
mkdir /tmp/canal  
tar -zxvf canal.deployer-1.1.0.tar.gz -C /tmp/canal  
解压完成后，进入 /tmp/canal 目录，可以看到如下结构  

![数据同步](pictures/p4.png)  

配置修改  
公共配置  
sudo vim conf/canal.properties    
canal.port= 11111 # canal server 运行端口，保证该端口为占用状态，或者使用其他未占用端口。   
保存退出。   

实例配置  
sudo vi conf/example/instance.properties   
 
```
## mysql serverId
canal.instance.mysql.slaveId = 1234
#position info，需要改成自己的数据库信息
canal.instance.master.address = 127.0.0.1:3306  # mysql连接
canal.instance.master.journal.name = 
canal.instance.master.position = 
canal.instance.master.timestamp = 
#canal.instance.standby.address = 
#canal.instance.standby.journal.name =
#canal.instance.standby.position = 
#canal.instance.standby.timestamp = 
#username/password，需要改成自己的数据库信息
canal.instance.dbUsername = canal  # mysql账号
canal.instance.dbPassword = canal  # 密码
canal.instance.defaultDatabaseName = # 需要同步的库名
canal.instance.connectionCharset = UTF-8 # mysql编码
#table regex
canal.instance.filter.regex = .\*\\\\..\*
```
保存退出。  
canal.instance.connectionCharset 代表数据库的编码方式对应到 java 中的编码类型，比如 UTF-8，GBK , ISO-8859-1。  
如果系统是1个 cpu，需要将 canal.instance.parser.parallel 设置为 false。  

启动  
sh bin/startup.sh  
![数据同步](pictures/p5.png)  

查看 server 日志，canal server端运行日志
vi logs/canal/canal.log  
![数据同步](pictures/p6.png)  

查看 instance 的日志，canal client端连接日志
vi logs/example/example.log  

logs/example/meta.log # 实例binlog 读取记录文件（记录变更位置，默认为新增变更(tail)）  

发现报错，如下：  
![数据同步](pictures/p7.png)   
报错原因：mysql版本的问题。    
解决办法      
以管理员的账号与密码登录，然后执行如下的操作：  
mysql> use mysql;    
mysql> delete from user where user='';     
mysql> flush privileges;    
   
执行 sh bin/stop.sh 关闭canal，重启canal，成功启动日志如下：  
![数据同步](pictures/p8.png)    

# canal client 配置启动
canal client将从canal server获取的binlog数据最终以json行格式保存到指定文件(也可省略这步，
直接发送到MQ)。   
binlog生产端和消费端的之间，增加MQ作为缓冲，增加容错度和动态扩展性。  

# 从头创建工程
## 依赖配置
``` 
<dependency>
    <groupId>com.alibaba.otter</groupId>
    <artifactId>canal.client</artifactId>
    <version>1.1.0</version>
</dependency>
```

## 增加canal服务端的连接信息
修改 application.yml，添加连接canal服务端的连接信息配置，如下：  
```
canal:
  server:
    ip: 192.168.0.43
    port: 11111
    dest: example
    userName:
    userPass:
```    
## 增加从canal服务端读取binlog的canal客户端相关的类  
相关的类是 CanalClient，进入该类自己查看相应的业务逻辑。  

## 启动类中获取 CanalClient，程序启动的时候进行监听服务端的 binlog 日志的获取  

```
 public static void main(String[] args) {
        SpringApplication.run(DataSynApplication.class, args);
        CanalClient canalClient = SpringContext.getBean(CanalClient.class);
        canalClient.createConnect();
    }
```

启动项目，修改数据库相关的表的记录，控制台打印日志如下：    
![数据同步](pictures/p9.png)    

下面我们优化以上的逻辑，就是把监控到的binlog以json格式发送到rabbitmq中，然后redis异步从
rabbitmq中获取数据进行数据库中的数据与缓存中的数据的数据同步，实际的生产环境要保证redis与
rabbitmq的高可用。  








  




  





