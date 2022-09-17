## 码匠社区

##资料
[Spring 文档](https://spring.io/guides)  
[Spring Web](https://spring.io/guides/gs/serving-web-content/)  
[es](https://elasticsearch.cn/explore)  
[BootStrap](https://v3.bootcss.com/)  
[Github OAuth](https://docs.github.com/cn/developers/apps/building-oauth-apps/creating-an-oauth-app)
[Mybatis](http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)  
[Spring Boot文档](https://docs.spring.io/spring-boot/docs/2.0.0.RC2/reference/htmlsingle/)  
[flyway](https://flywaydb.org/documentation/getstarted/firststeps/maven)  


## 工具
[Git](https://git-scm.com/downloads)  

##脚本
```sql
    CREATE TABLE USER(
        "ID" INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
        "ACCOUNT_ID" VARCHAR(100),
        "NAME" VARCHAR(50),
        "TOKEN" CHAR(36),
        "GMT_CREATE" BIGINT,
        "GMT_MODIFIED" BIGINT
    )
    

```
```bash
mvn flyway:migrate
```