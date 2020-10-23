DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`
(
    `ID`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '编号',
    `PARENT_ID`   varchar(32)  NOT NULL COMMENT '父部门',
    `PARENT_IDS`  varchar(500) NOT NULL COMMENT '父IDS，按层级逗号分隔',
    `NAME`        varchar(100) NOT NULL COMMENT '部门名称',
    `SORT`        int(10)      NOT NULL COMMENT '排序',
    `CREATE_BY`   varchar(32)  NOT NULL COMMENT '创建者',
    `CREATE_DATE` datetime     NOT NULL COMMENT '创建时间',
    `UPDATE_BY`   varchar(32)  NOT NULL COMMENT '更新者',
    `UPDATE_DATE` datetime     NOT NULL COMMENT '更新时间',
    `REMARKS`     varchar(255) DEFAULT NULL COMMENT '备注信息',
    `PHYSICS_FLAG` tinyint(4) DEFAULT '1' COMMENT '删除标识：1-正常；0-删除',
    `version` bigint DEFAULT '1' COMMENT '版本',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `IDX_SD_PARENT_ID` (`PARENT_ID`) USING BTREE,
    KEY `IDX_SD_CREATE_DATE` (`CREATE_DATE`) USING BTREE,
    KEY `IDX_SD_CREATE_BY` (`CREATE_BY`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='组织机构';

DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`
(
    `ID`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '编号',
    `TYPE`        varchar(50) NOT NULL COMMENT '字典类型',
    `CODE`        varchar(50) NOT NULL COMMENT '编码',
    `NAME`        varchar(50) NOT NULL COMMENT '名称',
    `SORT`        int(11)     NOT NULL COMMENT '排序',
    `STATUS`      char(1)     NOT NULL COMMENT '是否启用1：是，0：否',
    `CREATE_BY`   varchar(32) NOT NULL COMMENT '创建者',
    `CREATE_DATE` datetime    NOT NULL COMMENT '创建时间',
    `UPDATE_BY`   varchar(32)  DEFAULT NULL COMMENT '更新者',
    `UPDATE_DATE` datetime     DEFAULT NULL COMMENT '更新时间',
    `REMARKS`     varchar(255) DEFAULT NULL COMMENT '备注信息',
    `PHYSICS_FLAG` tinyint(4) DEFAULT '1' COMMENT '删除标识：1-正常；0-删除',
    `version` bigint DEFAULT '1' COMMENT '版本',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `TYPE` (`TYPE`) USING BTREE,
    KEY `CREATE_DATE` (`CREATE_DATE`) USING BTREE,
    KEY `CREATE_BY` (`CREATE_BY`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='字典';

DROP TABLE IF EXISTS `sys_gen`;
CREATE TABLE `sys_gen`
(
    `ID`            varchar(64) NOT NULL COMMENT '编号',
    `TABLE_NAME`    varchar(64) NOT NULL COMMENT '表名',
    `PK_TYPE`       varchar(10) NOT NULL COMMENT '主键类型',
    `MENU_NAME`     varchar(50) NOT NULL COMMENT '菜单名',
    `MODULE_NAME`   varchar(10) NOT NULL COMMENT '系统包名',
    `FUNCTION_NAME` varchar(20) NOT NULL COMMENT '功能模块',
    `TEMPLATE`      varchar(1)  NOT NULL COMMENT '生成模板',
    `CLASS_NAME`    varchar(50) NOT NULL COMMENT '类名',
    `COLUMNS`       text        NOT NULL COMMENT '字段信息',
    `CREATE_BY`     varchar(32) NOT NULL COMMENT '创建者',
    `CREATE_DATE`   datetime    NOT NULL COMMENT '创建时间',
    `UPDATE_BY`     varchar(32) NOT NULL COMMENT '更新者',
    `UPDATE_DATE`   datetime    NOT NULL COMMENT '更新时间',
    `REMARKS`       varchar(255) DEFAULT NULL COMMENT '备注信息',
    `PHYSICS_FLAG`  tinyint(4) DEFAULT '1' COMMENT '删除标识：1-正常；0-删除',
    `version` bigint DEFAULT '1' COMMENT '版本',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `CREATE_DATE` (`CREATE_DATE`) USING BTREE,
    KEY `CREATE_BY` (`CREATE_BY`) USING BTREE,
    KEY `TABLE_NAME` (`TABLE_NAME`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='代码生成';

DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`
(
    `ID`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '编号',
    `USER_ID`      varchar(32) NOT NULL COMMENT '用户ID',
    `STATUS`       varchar(1)  NOT NULL COMMENT '登录状态0:成功;1.密码错误；2.禁用;3.锁定24小时',
    `LOGIN_TIME`   datetime    NOT NULL COMMENT '登录时间',
    `IP`           varchar(16)   DEFAULT NULL COMMENT 'IP地址',
    `AREA`         varchar(20)   DEFAULT NULL COMMENT '登录地区',
    `USER_AGENT`   varchar(1000) DEFAULT NULL COMMENT '用户代理',
    `DEVICE_NAME`  varchar(100)  DEFAULT NULL COMMENT '设备名称',
    `BROWSER_NAME` varchar(100)  DEFAULT NULL COMMENT '浏览器名称',
    `CREATE_BY`    varchar(32)   DEFAULT NULL COMMENT '创建者',
    `CREATE_DATE`  datetime      DEFAULT NULL COMMENT '创建时间',
    `UPDATE_BY`    varchar(32)   DEFAULT NULL COMMENT '更新者',
    `UPDATE_DATE`  datetime      DEFAULT NULL COMMENT '更新时间',
    `REMARKS`      varchar(255)  DEFAULT NULL COMMENT '备注信息',
    `FIRST_LOGIN`  int(4)        DEFAULT '0' COMMENT '是否需要修改密码：0-正常；1-初始化密码；2-已修改密码',
    `PHYSICS_FLAG` tinyint(4) DEFAULT '1' COMMENT '删除标识：1-正常；0-删除',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `USER_ID` (`USER_ID`) USING BTREE,
    KEY `IP` (`IP`) USING BTREE,
    KEY `LOGIN_TIME` (`LOGIN_TIME`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='登录日志';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `ID`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '编号',
    `PARENT_ID`   varchar(32)  NOT NULL COMMENT '父级编号',
    `PARENT_IDS`  varchar(160) NOT NULL COMMENT '所有父级编号',
    `NAME`        varchar(50)  NOT NULL COMMENT '名称',
    `SORT`        int(10)      NOT NULL COMMENT '排序',
    `HREF`        varchar(200) DEFAULT NULL COMMENT '链接',
    `TARGET`      varchar(10)  DEFAULT NULL COMMENT '目标',
    `TYPE`        varchar(4)   NOT NULL COMMENT '0：目录   1：菜单   2：按钮',
    `ICON`        varchar(50)  DEFAULT NULL COMMENT '图标',
    `IS_SHOW`     char(1)      NOT NULL COMMENT '是否在菜单中显示',
    `PERMISSION`  varchar(200) DEFAULT NULL COMMENT '权限标识',
    `CREATE_BY`   varchar(32)  NOT NULL COMMENT '创建者',
    `CREATE_DATE` datetime     NOT NULL COMMENT '创建时间',
    `UPDATE_BY`   varchar(32)  DEFAULT NULL COMMENT '更新者',
    `UPDATE_DATE` datetime     DEFAULT NULL COMMENT '更新时间',
    `REMARKS`     varchar(255) DEFAULT NULL COMMENT '备注信息',
    `PHYSICS_FLAG` tinyint(4) DEFAULT '1' COMMENT '删除标识：1-正常；0-删除',
    `version` bigint DEFAULT '1' COMMENT '版本',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `PARENT_ID` (`PARENT_ID`) USING BTREE,
    KEY `NAME` (`NAME`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='菜单';

DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log`
(
    `ID`             bigint(20) NOT NULL AUTO_INCREMENT,
    `LOG_ID`         bigint(32) NOT NULL COMMENT '日志ID.',
    `REQUEST_URI`    varchar(128)    DEFAULT NULL COMMENT '用户IP.',
    `REQUEST_IP`     varchar(64)     DEFAULT NULL COMMENT '终端',
    `REQUEST_USERID` varchar(128)    DEFAULT NULL COMMENT '请求来源.',
    `REQUEST_DATE`   timestamp  NULL DEFAULT NULL COMMENT '请求开始时间.',
    `RESPONSE_TIME`  int(8)          DEFAULT NULL COMMENT '请求耗时.',
    `REQUEST_PARAMS` varchar(4096)    DEFAULT NULL,
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `SYS_OPERATION_LOG_USERID` (`REQUEST_USERID`) USING BTREE,
    KEY `SYS_OPERATION_LOG_REQ_DATE` (`REQUEST_DATE`) USING BTREE,
    KEY `SYS_OPERATION_LOG_LOGID` (`LOG_ID`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='操作日志表.';

DROP TABLE IF EXISTS `sys_operate_log_info`;
CREATE TABLE `sys_operate_log_info`
(
    `LOG_ID`       bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID.',
    `REQ_URI`      varchar(128)    DEFAULT NULL COMMENT '请求URI',
    `URI_NAME`     varchar(128)    DEFAULT NULL COMMENT 'URI名称.',
    `URI_TYPE`     tinyint(4) DEFAULT '0' COMMENT 'URI类型.1:菜单地址，2：操作地址，0：其它;默认为0.',
    `PHYSICS_FLAG` tinyint(4) DEFAULT '1' COMMENT '删除标识：1-正常；0-删除',
    `CREATE_DATE`  timestamp  NULL DEFAULT NULL COMMENT '创建时间.',
    `UPDATE_DATE`  timestamp  NULL DEFAULT NULL COMMENT '更新时间',
    `UPDATE_BY`    varchar(8)      DEFAULT NULL COMMENT '更新人',
    `CREATE_BY`    varchar(8)      DEFAULT NULL COMMENT '创建人',
    `REMARKS`      varchar(255)    DEFAULT NULL COMMENT '备注',
    `version`      bigint DEFAULT '1' COMMENT '版本',
    PRIMARY KEY (`LOG_ID`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='操作日志配置表.';

DROP TABLE IF EXISTS `sys_param_class`;
CREATE TABLE `sys_param_class`
(
    `ID`               bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '编号',
    `PARAM_TYPE_CLASS` varchar(128) NOT NULL COMMENT '类型分类',
    `PARAM_TYPE_NAME`  varchar(128) NOT NULL COMMENT '类型名称',
    `VISIBLE`          tinyint(4)       NOT NULL DEFAULT '1' COMMENT '是否可用：1-可用；0-停用',
    `CREATE_BY`        varchar(32)  NOT NULL COMMENT '创建者',
    `CREATE_DATE`      datetime     NOT NULL COMMENT '创建时间',
    `UPDATE_BY`        varchar(32)           DEFAULT NULL COMMENT '更新者',
    `UPDATE_DATE`      datetime              DEFAULT NULL COMMENT '更新时间',
    `REMARKS`          varchar(255)          DEFAULT NULL COMMENT '备注信息',
    `PHYSICS_FLAG`     tinyint(4) DEFAULT '1' COMMENT '删除标识：1-正常；0-删除',
    `version`      bigint DEFAULT '1' COMMENT '版本',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `PARAM_KEY` (`PARAM_TYPE_NAME`) USING BTREE,
    KEY `CREATE_BY` (`CREATE_BY`) USING BTREE,
    KEY `CREATE_DATE` (`CREATE_DATE`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统参数';

DROP TABLE IF EXISTS `sys_param_type`;
CREATE TABLE `sys_param_type`
(
    `ID`               bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    `PARAM_TYPE_CLASS` varchar(128) DEFAULT NULL COMMENT '类型分类',
    `PARAM_TYPE_ID`    int(8)       DEFAULT NULL COMMENT '类型ID.不允许修改',
    `PARAM_TYPE_CODE`  varchar(128) DEFAULT NULL COMMENT '分类CODE',
    `PARAM_TYPE_NAME`  varchar(128) DEFAULT '' COMMENT '分类名称',
    `CREATE_BY`        varchar(32)  DEFAULT NULL COMMENT '创建者',
    `CREATE_DATE`      datetime     DEFAULT NULL COMMENT '创建时间',
    `UPDATE_BY`        varchar(32)  DEFAULT NULL COMMENT '更新者',
    `UPDATE_DATE`      datetime     DEFAULT NULL COMMENT '更新时间',
    `REMARKS`          varchar(255) DEFAULT NULL COMMENT '备注信息',
    `PHYSICS_FLAG`     tinyint(4)      DEFAULT '1' COMMENT '删除标识：1-正常；0-删除',
    `i18n_field`       text COMMENT 'json格式的国际化信息',
    `version`      bigint DEFAULT '1' COMMENT '版本',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `CREATE_BY` (`CREATE_BY`) USING BTREE,
    KEY `CREATE_DATE` (`CREATE_DATE`) USING BTREE,
    KEY `param_key` (`PARAM_TYPE_ID`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统参数';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `ID`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '编号',
    `NAME`        varchar(50) NOT NULL COMMENT '角色名称',
    `CREATE_BY`   varchar(32) NOT NULL COMMENT '创建者',
    `CREATE_DATE` datetime    NOT NULL COMMENT '创建时间',
    `UPDATE_BY`   varchar(32)  DEFAULT NULL COMMENT '更新者',
    `UPDATE_DATE` datetime     DEFAULT NULL COMMENT '更新时间',
    `REMARKS`     varchar(255) DEFAULT NULL COMMENT '备注信息',
    `PHYSICS_FLAG`     tinyint(4)      DEFAULT '1' COMMENT '删除标识：1-正常；0-删除',
    `version`      bigint DEFAULT '1' COMMENT '版本',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NAME` (`NAME`) USING BTREE,
    KEY `CREATE_BY` (`CREATE_BY`) USING BTREE,
    KEY `CREATE_DATE` (`CREATE_DATE`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';

DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`
(
    `ROLE_ID` varchar(32) NOT NULL COMMENT '角色ID',
    `DEPT_ID` varchar(32) NOT NULL COMMENT '部门ID',
    PRIMARY KEY (`ROLE_ID`, `DEPT_ID`) USING BTREE,
    KEY `ROLE_ID` (`ROLE_ID`) USING BTREE,
    KEY `DEPT_ID` (`DEPT_ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色部门';

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `ROLE_ID` varchar(32) NOT NULL COMMENT '角色编号',
    `MENU_ID` varchar(32) NOT NULL COMMENT '菜单编号',
    PRIMARY KEY (`ROLE_ID`, `MENU_ID`) USING BTREE,
    KEY `ROLE_ID` (`ROLE_ID`) USING BTREE,
    KEY `MENU_ID` (`MENU_ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色-菜单';

DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task`
(
    `ID`            bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '任务id.',
    `TASK_NAME`     varchar(64)  NOT NULL COMMENT '任务名称.',
    `GROUP_NAME`    varchar(32)           DEFAULT NULL COMMENT '任务分组.',
    `CRON_EXP`      varchar(32)  NOT NULL COMMENT '时间表达式.',
    `TASK_TARGET`   varchar(128) NOT NULL COMMENT '执行目标.',
    `STATUS`        tinyint(1)       NOT NULL COMMENT '任务状态.0未启用;1启用',
    `LAST_RUN_TIME` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后执行时间.',
    `LAST_RESULT`   text COMMENT '最后执行结果.',
    `REMARKS`       varchar(512)          DEFAULT NULL COMMENT '任务备注.',
    `PARAMS`        varchar(512)          DEFAULT NULL COMMENT '附加参数.json格式',
    `CREATE_DATE`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间.',
    `UPDATE_DATE`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间.',
    `CREATE_BY`     varchar(64)           DEFAULT NULL COMMENT '创建者',
    `UPDATE_BY`     varchar(64)           DEFAULT NULL COMMENT '更新者',
    `EXEC_TYPE`     tinyint(1)       NOT NULL DEFAULT '0' COMMENT '执行方式:0阻塞 1非阻塞.',
    `PHYSICS_FLAG`     tinyint(4)      DEFAULT '1' COMMENT '删除标识：1-正常；0-删除',
    `version`      bigint DEFAULT '1' COMMENT '版本',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='任务表.';

DROP TABLE IF EXISTS `sys_task_log`;
CREATE TABLE `sys_task_log`
(
    `ID`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志id.',
    `TASK_ID`     bigint(20) NOT NULL COMMENT '任务id.',
    `BEGIN_TIME`  timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行开始时间.',
    `END_TIME`    timestamp  NULL     DEFAULT NULL COMMENT '执行结束时间.',
    `COST_TIME`   bigint(20)          DEFAULT NULL COMMENT '执行耗时.',
    `EXEC_IP`     varchar(16)         DEFAULT NULL COMMENT '执行节点ip.',
    `EXEC_STATUS` tinyint(1)              DEFAULT NULL COMMENT '执行结果.1成功;2失败',
    `EXEC_RESULT` text COMMENT '结果描述.',
    `CREATE_DATE` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间.',
    `UPDATE_DATE` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间.',
    `CREATE_BY`   varchar(64)         DEFAULT NULL COMMENT '创建者',
    `UPDATE_BY`   varchar(64)         DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `bi_task_log_idx1` (`TASK_ID`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='任务日志表.';

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `ID`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '编号',
    `DEPT_ID`     varchar(32)  DEFAULT NULL COMMENT '部门',
    `LOGIN_NAME`  varchar(50)  NOT NULL COMMENT '登录名',
    `PASSWORD`    varchar(100) NOT NULL COMMENT '密码',
    `SALT`        varchar(20)  NOT NULL,
    `NAME`        varchar(50)  NOT NULL COMMENT '姓名',
    `MOBILE`      varchar(50)  DEFAULT NULL COMMENT '手机',
    `PHOTO`       varchar(100) DEFAULT NULL COMMENT '用户头像',
    `EMAIL`       varchar(100) DEFAULT NULL COMMENT '邮件',
    `USER_TYPE`   varchar(2)   DEFAULT NULL COMMENT '用户类型，业务扩展用',
    `STATUS`      varchar(1)   DEFAULT NULL COMMENT '状态1：正常，0：禁用',
    `CREATE_BY`   varchar(32)  NOT NULL COMMENT '创建者',
    `CREATE_DATE` datetime     NOT NULL COMMENT '创建时间',
    `UPDATE_BY`   varchar(32)  DEFAULT NULL COMMENT '更新者',
    `UPDATE_DATE` datetime     DEFAULT NULL COMMENT '更新时间',
    `REMARKS`     varchar(255) DEFAULT NULL,
    `FLD_N1`      int(9)       DEFAULT NULL COMMENT '扩展数值字段1',
    `FLD_N2`      int(9)       DEFAULT NULL COMMENT '扩展数值字段2',
    `FLD_S1`      varchar(64)  DEFAULT NULL COMMENT '扩展字符字段1',
    `FLD_S2`      varchar(64)  DEFAULT NULL COMMENT '扩展字符字段1',
    `lang`        varchar(5)   DEFAULT NULL COMMENT '语言',
    `PHYSICS_FLAG`     tinyint(4)      DEFAULT '1' COMMENT '删除标识：1-正常；0-删除',
    `version`      bigint DEFAULT '1' COMMENT '版本',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `LOGIN_NAME` (`LOGIN_NAME`) USING BTREE,
    KEY `DEPT_ID` (`DEPT_ID`) USING BTREE,
    KEY `CREATE_BY` (`CREATE_BY`) USING BTREE,
    KEY `CREATE_DATE` (`CREATE_DATE`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户信息';

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `USER_ID` varchar(32) NOT NULL COMMENT '用户编号',
    `ROLE_ID` varchar(32) NOT NULL COMMENT '角色编号',
    KEY `USER_ID` (`USER_ID`) USING BTREE,
    KEY `ROLE_ID` (`ROLE_ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户-角色';
