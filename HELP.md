# 开发人员请注意

### 本项目须在顶部(与src, pom.xml等平级)自行添加.env文本文件，填入邮箱信息、数据库密码等
### 前端开发人员还需完成邮件界面和标准错误界面
* 邮件界面： src/main/resources/templates/mail.html
* 错误界面： src/main/resources/templates/error.html
### 静态页面可在 cn.edu.usst.cs.campusAid.config.WebConfig 类的 addViewControllers 方法中添加

## 后端接口
### 认证板块（注册和登录） 均只返还一个字符串，仅在状态码为200时标志返回的时成功信息
### 其他板块的返还如为json 结构均可在顶部jsonInterfaces文件夹中查到
* 仅在状态码为200时标志返回的时成功信息
* 其余时候仅返回一个字符串，标志返回的时失败信息

### 项目依赖包版本信息如下：


# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.13/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.13/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.13/reference/htmlsingle/#web)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/2.6.13/reference/htmlsingle/#web.reactive)
* [Spring Session](https://docs.spring.io/spring-session/reference/)
* [Rest Repositories](https://docs.spring.io/spring-boot/docs/2.6.13/reference/htmlsingle/#howto.data-access.exposing-spring-data-repositories-as-rest)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/2.6.13/reference/htmlsingle/#web.servlet.spring-mvc.template-engines)
* [JDBC API](https://docs.spring.io/spring-boot/docs/2.6.13/reference/htmlsingle/#data.sql)
* [Spring Data JDBC](https://docs.spring.io/spring-boot/docs/2.6.13/reference/htmlsingle/#data.sql.jdbc)
* [MyBatis Framework](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.6.13/reference/htmlsingle/#actuator)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Neo4j Data with REST](https://spring.io/guides/gs/accessing-neo4j-data-rest/)
* [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Accessing Relational Data using JDBC with Spring](https://spring.io/guides/gs/relational-data-access/)
* [Managing Transactions](https://spring.io/guides/gs/managing-transactions/)
* [Using Spring Data JDBC](https://github.com/spring-projects/spring-data-examples/tree/master/jdbc/basics)
* [MyBatis Quick Start](https://github.com/mybatis/spring-boot-starter/wiki/Quick-Start)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

