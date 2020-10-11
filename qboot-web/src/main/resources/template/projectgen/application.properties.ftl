# Spring boot application
admin.path=/${projectName}
spring.application.name=${projectName}
server.port=8080
spring.profiles.active=dev

spring.redis.timeout=3000
spring.redis.redisson.config=classpath:redisson.yaml

spring.datasource.druid.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.druid.url=
spring.datasource.druid.username=
spring.datasource.druid.password=
spring.datasource.druid.initial-size=5
spring.datasource.druid.max-active=100
spring.datasource.druid.min-idle=3
spring.datasource.druid.time-between-eviction-runs-millis=60000
spring.datasource.druid.min-evictable-idle-time-millis=300000
spring.datasource.druid.validation-query=select 1
spring.datasource.druid.test-while-idle=true
spring.datasource.druid.test-on-borrow=true
spring.datasource.druid.test-on-return=false
spring.datasource.druid.max-wait=60000
spring.datasource.druid.filters=stat,config
spring.datasource.druid.filter.stat.enabled=true
spring.datasource.druid.filter.stat.slow-sql-millis=10
spring.datasource.druid.filter.stat.log-slow-sql=true
spring.datasource.druid.connection-properties=config.decrypt=true;config.decrypt.key=MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGLsGgsqFFzkRjj2MkeLzs/WIdQYc97R6LGffAKXkfuBRhMFqzWZSPksQbeaulUutt32dy3l9M/DEw8/iLlgepCwqoMc9uRDOoWqeMLffESFu/TT2JBz1IgGe6qdtimyRxkToCjRccxtiuxa3nOKXDcf+cHgUEC9H2qN3RPRd/4wIDAQAB

mybatis.mapper-locations=classpath*:mappers/**/*.xml
mybatis.type-aliases-package=org.qboot.${projectName}.*.dto
mybatis.config-location=classpath:mybatis-config.xml

#日志拦截
spring.aop.auto=true
spring.aop.proxy-target-class=false

## json时间格式设置
spring.jackson.default-property-inclusion=non-null
spring.jackson.time-zone=GMT+8
spring.jackson.date-format=yyyy-MM-dd