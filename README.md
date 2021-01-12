# qboot
#### 简介
qboot是一个提供了基础系统功能的单体项目，基于sprintboot的建立便捷java单体项目
该项目的功能集成了基本的系统模块(用户/菜单/权限/登陆/任务)和简单的代码生成。
由开发者扩充业务功能

####项目说明
qboot-common: 常用工具和常量定义
qboot-init: 项目初始化所需配置
qboot-web: web主项目

####环境要求
mysql 5.7+ 
redis 4.0+

####初始化
1，将项目拉取下来后，新建数据库，并执行create_table.sql 和 insert_table.sql来初始化数据表
2，qboot-init/conf目录下是多环境下的配置文件(dev,test,prod)，将开发文件application-dev.yaml
的数据库与redis配置上，放在resources目录下

#### 运行说明
有两种方式来开启qboot开发
1，直接在qboot项目里开发
    1),将application-dev.yaml拷贝到工程的resources目录下
    2),运行QBootApplication类启动项目
    
2，maven依赖qboot项目
    1),将qboot项目执行maven指令 clean install后，将qboot打包到本地maven仓库
    2),新建一个jar工程，将qboot-common,qboot-web加入依赖
    3),将application-dev.yaml拷贝到工程的resources目录下
    4),运行QBootApplication类启动项目