#### 待学习

XXL-JOB（学习）

##### 分布式存储

1.caffeine（java内存缓存，高性能）

2.etcd（云原生架构的一个分布式储存，存储配置，扩容能力）



#### Redis

1) NoSQL

2) Key-Value存储系统（区别于MySQL，redis存储的是键值对）

   ###### java里的实现方式

   1.spring Data Redis(推荐)

   通用的数据访问框架，定义了一组增删改查的接口

   2.jedis

   3.Redisson

   4.Lettuce(高阶)

   ## 连接池最大的好处是复用

   

   ## Redis 数据结构（高频面试点）

   string 字符串类型：name：”yijiawen“

   List列表：names["yupi","yijiawen"]

   Set集合： names["yupi","yijiawen"]不能重复

   Hash哈希：nameAge：{”yijiawen“：1}

   Zset集合：names{yijiawen-1}

   #### 设计缓存key

   不同用户看到的数据不同

   systemId_entityId_userId<options>

   userSystem_user_userId

   ### Redis一定要设置过期时间

   #### 缓存预热

   ####  定时任务的实现

   1) Spring Scheduler(springboot默认整合)

      - 开启主类@EnableSchedulling

      - 再任务中加上注解@Scheduled

        

   2) XXL-job

   

   ### 伙伴匹配系统的亮点

   1.redisson

   

   

   #### 锁

   在有限资源的情况下，控制同一时间段只有某些线程能访问资源

   ###  

   ###  分布式锁实现关键

   #### 抢锁机制
   
   怎么保证同时只有一个服务器能抢到锁？
   
   *核心思想就是先来的人把数据改成自己的标识，后来的人等方法执行完毕清空标识，
   
   再抢锁
   
   用redis可以使用setnx作为锁，用完锁要释放
   
   redis设置过期时间
   
   set lock yjw ex 10 nx
   
   *如果方法执行时间过长，锁提前过期，？
   
   ##### 问题
   
   1.连锁效应，释放掉别人的锁
   
   2.这样还是会存在多个方法同时执行
   
   解决方案
   
   1.续期
   
   #### Redisson
   
   是java客户端，数据网格
   
   实现了很多java里支持的接口和数据结构
   
   **像操作集合那样操作redis**
   
   两种引入方式
   
   1.redisson springboot starter
   
   2.直接引入redisson
   
   
   
   
   
   
   
   
   
   
   
   



