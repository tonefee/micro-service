# micro-service
Use of Simple Micro Services  
结合网上开源的分布式事务框架LCN去实现分布式事务，相关实现代码请您查看fukun-distribute-transaction这个模块。  
此项目使用spring-boot的2.0.5.RELEASE版本，单表crud通用mapper，实现了全局异常处理，登录验证、统一响应返回码、  
动态为数据库中的某张表添加扩展字段等功能，详情请查看相关模块的源代码。

# project items build order
fukun-parent <br/>
fukun-commons 注意构建fukun-commons时，注释掉pom.xml中的modules标签中的配置再次进行构建<br/>
fukun-commons-util<br/>
fukun-commons-public<br/>
fukun-commons-lock<br/>
fukun-commons-attributes<br/>
fukun-commons-dao<br/>
fukun-commons-service<br/>
fukun-user 注意构建fukun-user时，注释掉pom.xml中的modules标签中的配置再次进行构建<br/>
fukun-user-model<br/>
fukun-user-api<br/>
fukun-user-token<br/>
fukun-commons-web<br/>
fukun-user-client<br/>
fukun-user-service<br/>

