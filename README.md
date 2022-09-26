## 码匠社区

##部署
###依赖
-Git    
-JDK  
-Maven  
-MySQL  

##步骤
-yum update  
-[Failed to download metadata for repo ‘appstream‘: Cannot prepare internal mirrorlist的解决方法](https://blog.csdn.net/qq_575775600/article/details/125274121)  
-yum install git  
-在根目录/root下 mkdir App  
-cd App  
-git clone https://github.com/Theresa-Zhu/community.git  
- 在commmunity文件下 yum install maven  
-mvn -v  
-cp src/main/resources/application.properties src/main/resources/application-production.properties  
-vim src/main/resources/application-production.properties  




##资料
[Spring 文档](https://spring.io/guides)  
[Spring Web](https://spring.io/guides/gs/serving-web-content/)  
[拦截器 + Spring手册](https://docs.spring.io/spring-framework/docs/5.0.3.RELEASE/spring-framework-reference/web.html#mvc-config-interceptors)  
[es](https://elasticsearch.cn/explore)  
[BootStrap](https://v3.bootcss.com/)  
[Github OAuth](https://docs.github.com/cn/developers/apps/building-oauth-apps/creating-an-oauth-app)
[Mybatis](http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)  
[Spring Boot文档](https://docs.spring.io/spring-boot/docs/2.0.0.RC2/reference/htmlsingle/)  
[thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#setting-attribute-values)  
[MyBatis Generator](http://mybatis.org/generator/index.html)  
[editor.md](https://pandao.github.io/editor.md/examples/index.html)  



## 工具
[Git](https://git-scm.com/downloads)   
[lombok](https://projectlombok.org/setup/maven)  
[flyway](https://flywaydb.org/documentation/getstarted/firststeps/maven)  
[富文本编辑器](https://github.com/pandao/editor.md)  

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
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```