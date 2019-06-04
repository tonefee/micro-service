在上个月我们知道 Eureka 2.X 遇到困难停止开发了，但其实对国内的用户影响甚小，一方面国内大都使用的是 Eureka 1.X 系列，
另一方面 Spring Cloud 支持很多服务发现的软件，Eureka 只是其中之一，下面是 Spring Cloud 支持的服务发现软件以及特性对比：  

![服务发现](pictures/consul1.png)   

在以上服务发现的软件中，Euerka和Consul使用最为广泛。  

# Consul 介绍

Consul 是 HashiCorp 公司推出的开源工具，用于实现分布式系统的服务发现与配置。
与其它分布式服务注册与发现的方案，Consul 的方案更“一站式”，内置了服务注册与发现框 架、分布一致性协议实现、
健康检查、Key/Value 存储、多数据中心方案，不再需要依赖其它工具（比如 ZooKeeper 等）。
使用起来也较 为简单。`Consul 使用 Go 语言编写`，因此具有天然可移植性(支持Linux、windows和Mac OS X)；
安装包仅包含一个可执行文件，方便部署，与 Docker 等轻量级容器可无缝配合。

## Consul 的优势：

使用 Raft 算法来保证一致性, 比复杂的 Paxos 算法更直接. 相比较而言, 
zookeeper 采用的是 Paxos, 而 etcd 使用的则是 Raft。

支持多数据中心，内外网的服务采用不同的端口进行监听。 
多数据中心集群可以避免单数据中心的单点故障,而其部署则需要考虑网络延迟, 分片等情况等。
 zookeeper 和 etcd 均不提供多数据中心功能的支持。  
 
支持健康检查。 etcd 不提供此功能。  

支持 http 和 dns 协议接口。 zookeeper 的集成较为复杂, etcd 只支持 http 协议。  

官方提供 web 管理界面, etcd 无此功能。  

综合比较, Consul 作为服务注册和配置管理的新星, 比较值得关注和研究。  

## 特性：

服务发现  
健康检查  
Key/Value 存储  
多数据中心  

## Consul 角色

client: 客户端, 无状态, 将 HTTP 和 DNS 接口请求转发给局域网内的服务端集群。  

server: 服务端, 保存配置信息, 高可用集群, 在局域网内与本地客户端通讯, 通过广域网与其它数据中心通讯。 
每个数据中心的 server 数量推荐为 3 个或是 5 个。  

Consul 客户端、服务端还支持夸中心的使用，更加提高了它的高可用性。  

![服务发现](pictures/consul2.png)   

## Consul 工作原理：

![服务发现](pictures/consul3.png)   

1、当 Producer 启动的时候，会向 Consul 发送一个 post 请求，告诉 Consul 自己的 IP 和 Port。  

2、Consul 接收到 Producer 的注册后，每隔10s（默认）会向 Producer 发送一个健康检查的请求，检验Producer是否健康。  

3、当 Consumer 发送 GET 方式请求 /api/address 到 Producer 时，会先从 Consul 中拿到一个存储服务 IP 和 Port 的临时表，
从表中拿到 Producer 的 IP 和 Port 后再发送 GET 方式请求 /api/address。    

4、该临时表每隔10s会更新，只包含有通过了健康检查的 Producer。  

Spring Cloud Consul 项目是针对 Consul 的服务治理实现。Consul是一个分布式高可用的系统，它包含多个组件，
但是作为一个整体，在微服务架构中为我们的基础设施提供服务发现和服务配置的工具。    

# Consul VS Eureka

Eureka 是一个服务发现工具。该体系结构主要是客户端/服务器，每个数据中心有一组 Eureka 服务器，
通常每个可用区域一个。通常 Eureka 的客户使用嵌入式 SDK 来注册和发现服务。对于非本地集成的客户，
官方提供的 Eureka 一些 REST 操作 API，其它语言可以使用这些 API 来实现对 Eureka Server 的操作
从而实现一个非 jvm 语言的 Eureka Client。  

Eureka 提供了一个弱一致的服务视图，尽可能的提供服务可用性。当客户端向服务器注册时，
该服务器将尝试复制到其它服务器，但不提供保证复制完成。服务注册的生存时间（TTL）较短，
要求客户端对服务器心跳检测。不健康的服务或节点停止心跳，导致它们超时并从注册表中删除。
服务发现可以路由到注册的任何服务，由于心跳检测机制有时间间隔，可能会导致部分服务不可用。
这个简化的模型允许简单的群集管理和高可扩展性。  

Consul 提供了一系列特性，包括更丰富的健康检查，键值对存储以及多数据中心。Consul 需要每个数据中心都有一套服务，
以及每个客户端的 agent，类似于使用像 Ribbon 这样的服务。Consul agent 允许大多数应用程序成为 Consul 不知情者，
通过配置文件执行服务注册并通过 DNS 或负载平衡器 sidecars 发现。  

Consul 提供强大的一致性保证，因为服务器使用 Raft 协议复制状态 。Consul 支持丰富的健康检查，
包括 TCP，HTTP，Nagios / Sensu 兼容脚本或基于 Eureka 的 TTL。  

Consul 强烈的一致性意味着它可以作为领导选举和集群协调的锁定服务。Eureka 不提供类似的保证，
并且通常需要为需要执行协调或具有更强一致性需求的服务运行 ZooKeeper。

`服务注册相比 Eureka 会稍慢一些。因为 Consul 的 raft 协议要求必须过半数的节点都写入成功才认为注册成功 Leader 挂掉时，重新选举期间整个 Consul 不可用。保证了强一致性但牺牲了可用性。`  

Eureka 保证高可用(A)和最终一致性：  
`服务注册相对要快，因为不需要等注册信息 replicate 到其它节点，也不保证注册信息是否 replicate 成功 当数据出现不一致时，虽然 A, B 上的注册信息不完全相同，但每个 Eureka 节点依然能够正常对外提供服务，这会出现查询服务信息时如果请求 A 查不到，但请求 B 就能查到。如此保证了可用性但牺牲了一致性。`  

`其它方面，eureka 就是个 servlet 程序，跑在 servlet 容器中; Consul 则是 go 编写而成。`  















 
 

