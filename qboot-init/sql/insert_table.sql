-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept`(ID, PARENT_ID, PARENT_IDS, NAME, SORT, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS) VALUES (1, '1', '0,1', '超级管理', 1, 'system', now(), 'system', now(), '');
COMMIT;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict`(ID, TYPE, CODE, NAME, SORT, STATUS, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS) VALUES (1, 'YES_NO', '1', '是', 1, '1', 'system', now(), 'system', now(), '');
INSERT INTO `sys_dict`(ID, TYPE, CODE, NAME, SORT, STATUS, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS) VALUES (2, 'YES_NO', '0', '否', 2, '1', 'system', now(), 'system', now(), '');
INSERT INTO `sys_dict`(ID, TYPE, CODE, NAME, SORT, STATUS, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS) VALUES (3, 'SYS_USER_TYPE', '1', '系统管理员', 1, '1', 'system', now(), 'system', now(), '');
INSERT INTO `sys_dict`(ID, TYPE, CODE, NAME, SORT, STATUS, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS) VALUES (4, 'SYS_USER_TYPE', '2', '普通用户', 2, '1', 'system', now(), 'system', now(), '');
INSERT INTO `sys_dict`(ID, TYPE, CODE, NAME, SORT, STATUS, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS) VALUES (5, 'SEX', '1', '男', 1, '1', 'system', now(), 'system', now(), '');
INSERT INTO `sys_dict`(ID, TYPE, CODE, NAME, SORT, STATUS, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS) VALUES (6, 'SEX', '2', '女', 2, '1', 'system', now(), 'system', now(), '');
COMMIT;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (1, '0', '0,', '系统管理', 1, '', '', '', 'layui-icon-set', '1', 'sys', 'system', now(), 'system', now(), '系统管理');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (2, '1', '0,,1', '用户管理', 1, '/sys/user.html', 'main', '', '', '1', 'sys:user', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (3, '1', '0,,1', '角色管理', 2, '/sys/role.html', 'main', '1', '', '1', 'sys:role', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (4, '1', '0,,1', '菜单管理', 3, '/sys/menu.html', 'main', '1', '', '1', 'sys:menu', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (5, '1', '0,1', '部门管理', 4, '/sys/dept.html', 'main', '1', NULL, '0', 'sys:dept', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (6, '1', '0,,1', '系统参数', 5, '/sys/dict.html', 'main', '1', '', '1', 'sys:dict', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (7, '1', '0,,1', '系统类型', 6, '/sys/param.html', 'main', '1', '', '1', 'sys:param', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (8, '1', '0,,1', '任务管理', 10, '/sys/task.html', 'main', '1', '', '1', 'sys:task', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (10, '79', '0,79', '登录日志', 7, '/sys/loginlog.html', 'main', '1', '', '1', 'sys:loginlog', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (11, '2', '0,1,2,', '修改', 2, NULL, NULL, '999', NULL, '1', 'sys:user:update', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (12, '2', '0,12', '删除', 10, '', '', '999', NULL, '1', 'sys:user:delete', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (13, '2', '0,,1,2', '添加', 1, '', NULL, '999', '', '1', 'sys:user:save', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (14, '3', '0,1,3,', '添加', 10, NULL, NULL, '999', NULL, '1', 'sys:role:save', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (15, '3', '0,1,3,', '删除', 10, NULL, NULL, '999', NULL, '1', 'sys:role:delete', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (16, '3', '0,1,3,', '修改', 10, NULL, NULL, '999', NULL, '1', 'sys:role:update', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (17, '4', '0,1,4,', '删除', 10, NULL, NULL, '999', NULL, '1', 'sys:menu:delete', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (18, '4', '0,1,4,', '修改', 10, NULL, NULL, '999', NULL, '1', 'sys:menu:update', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (19, '4', '0,1,4,', '添加', 10, NULL, NULL, '999', NULL, '1', 'sys:menu:save', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (20, '5', '0,1,5', '添加', 10, NULL, NULL, '999', NULL, '0', 'sys:dept:save', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (21, '5', '0,1,5,', '删除', 10, NULL, NULL, '999', NULL, '0', 'sys:dept:delete', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (22, '5', '0,1,5,', '修改', 10, NULL, NULL, '999', NULL, '0', 'sys:dept:update', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (23, '6', '0,1,6', '删除', 10, NULL, NULL, '999', NULL, '1', 'sys:dict:delete', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (24, '6', '0,1,6,', '修改', 10, NULL, NULL, '999', NULL, '1', 'sys:dict:update', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (25, '6', '0,1,6,', '添加', 10, NULL, NULL, '999', NULL, '1', 'sys:dict:save', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (26, '7', '0,1,7,', '修改', 10, NULL, NULL, '999', NULL, '1', 'sys:param:update', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (27, '7', '0,1,7,', '添加', 10, NULL, NULL, '999', NULL, '1', 'sys:param:save', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (28, '7', '0,1,7,', '删除', 10, NULL, NULL, '999', NULL, '1', 'sys:param:delete', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (29, '8', '0,,1,8', '新增', 10, '', NULL, '999', '', '1', 'sys:task:save', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (30, '8', '0,,1,8', '删除', 10, '', NULL, '999', '', '1', 'sys:task:delete', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (31, '35', '0,9,', '修改', 2, NULL, NULL, '999', NULL, '1', 'sys:gen:update', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (32, '35', '0,9,', '删除', 10, NULL, NULL, '999', NULL, '1', 'sys:gen:delete', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (33, '35', '0,9,', '添加', 1, NULL, NULL, '999', NULL, '1', 'sys:gen:save', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (34, '10', '0,10,', '删除', 10, NULL, NULL, '999', NULL, '0', 'sys:loginlog:delete', 'system', now(), 'system', now(), NULL);
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (35, '1', '0,1', '代码生成', 999, '/sys/gen.html', 'main', '1', '', '1', 'sys:gen', 'system', now(), 'system', now(), '代码生成');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (37, '3', '0,1,3', '查询角色', 1, '/rdp/sys/role/qryPage', '', '999', NULL, '1', 'sys:role:qry', 'system', now(), 'system', now(), '111');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (38, '2', '0,1,2', '查询用户', 1, '/rdp/sys/user/qryPage', '1', '999', NULL, '1', 'sys:user:qry', 'system' ,now(), 'system', now(), '1');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (39, '4', '0,1,4', '查询菜单', 1, '/rdp/sys/menu/qryPage', '', '999', NULL, '1', 'sys:menu:qry', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (40, '5', '0,1,5', '查询部门', 1, '/rdp/sys/dept/qryPage', '', '999', NULL, '0', 'sys:dept:qry', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (41, '6', '0,,1,6', '查询系统参数', 1, '/rdp/sys/dict/qryPage', '', '999', '', '1', 'sys:dict:qry', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (42, '7', '0,,1,7', '查询系统类型', 1, '/rdp/sys/param/qryPage', '', '999', '', '1', 'sys:param:qry', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (43, '9', '0,1,9', '查询生成案例', 1, '/rdp/sys/gen/qryPage', '', '999', NULL, '1', 'sys:gen:qry', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (44, '8', '0,,1,8', '查询', 1, '', '', '999', '', '1', 'sys:task:qry', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (46, '10', '0,1,10', '查询日志', 1, '/rdp/sys/log/qryPage', '', '999', NULL, '0', 'sys:loginlog:qry', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (58, '35', '0,1,35', '查询', 1, '/rdp/sys/gen/qry', '查询', '999', NULL, '1', 'sys:gen:qry', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (65, '79', '0,,1', '访问日志', 2, '/sys/operatlog.html', '2', '0', '', '1', 'sys:operatelog', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (66, '65', '0,79,65', '查询', 1, '/rdp/sys/operatelog/qry', '', '999', 'layui-icon-search', '1', 'sys:operatelog:qry', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (67, '79', '0,,1', '日志配置', 8, '/sys/operateinfo.html', '', '0', '', '1', 'sys:operateinfo', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (68, '67', '0,79,67', '查询', 1, '/rdp/sys/operateinfo/qry', '', '999', 'layui-icon-search', '1', 'sys:operateinfo:qry', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (69, '67', '0,1,67', '添加', 2, '/rdp/sys/operateinfo/save', '', '999', NULL, '1', 'sys:operateinfo:save', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (70, '67', '0,1,67', '修改', 3, '/rdp/sys/operateinfo/update', '', '999', NULL, '1', 'sys:operateinfo:update', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (71, '67', '0,1,67', '删除', 4, '/rdp/sys/operateinfo/delete', '', '999', NULL, '1', 'sys:operateinfo:delete', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (79, '0', '0,', '系统监控', 10, '', '', '0', 'layui-icon-console', '1', 'monitor', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (84, '79', '0,,79', '在线用户', 1, '/sys/online.html', '1', '1', '', '0', 'sys:online', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (85, '79', '0,,79', 'DB请求监控', 3, '/sys/dbmonitor.html', '3', '1', '', '0', 'sys:dbmonitor', 'system', now(), 'system', now(), '');
INSERT INTO `sys_menu` (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, TARGET, TYPE, ICON, IS_SHOW, PERMISSION, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS)VALUES (86, '79', '0,,79', '服务器监控', 4, '/sys/sysmonitor.html', '4', '1', '', '0', 'sys:sysmonitor', 'system', now(), 'system', now(), '');
COMMIT;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role`(ID, NAME, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS) VALUES (1, '普通管理员', 'system', now(), 'system', now(), '普通管理员');
COMMIT;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES ('2', '');
INSERT INTO `sys_role_menu` VALUES ('2', '273');
INSERT INTO `sys_role_menu` VALUES ('2', '274');
INSERT INTO `sys_role_menu` VALUES ('2', '275');
INSERT INTO `sys_role_menu` VALUES ('2', '276');
INSERT INTO `sys_role_menu` VALUES ('2', '277');
INSERT INTO `sys_role_menu` VALUES ('2', '278');
INSERT INTO `sys_role_menu` VALUES ('2', '279');
INSERT INTO `sys_role_menu` VALUES ('2', '280');
INSERT INTO `sys_role_menu` VALUES ('2', '281');
INSERT INTO `sys_role_menu` VALUES ('2', '282');
INSERT INTO `sys_role_menu` VALUES ('2', '283');
INSERT INTO `sys_role_menu` VALUES ('2', '284');
INSERT INTO `sys_role_menu` VALUES ('2', '287');
INSERT INTO `sys_role_menu` VALUES ('2', '288');
INSERT INTO `sys_role_menu` VALUES ('2', '289');
INSERT INTO `sys_role_menu` VALUES ('2', '290');
INSERT INTO `sys_role_menu` VALUES ('2', '291');
INSERT INTO `sys_role_menu` VALUES ('2', '292');
INSERT INTO `sys_role_menu` VALUES ('2', '293');
INSERT INTO `sys_role_menu` VALUES ('2', '294');
INSERT INTO `sys_role_menu` VALUES ('2', '295');
INSERT INTO `sys_role_menu` VALUES ('2', '296');
INSERT INTO `sys_role_menu` VALUES ('2', '297');
INSERT INTO `sys_role_menu` VALUES ('2', '298');
INSERT INTO `sys_role_menu` VALUES ('2', '299');
INSERT INTO `sys_role_menu` VALUES ('2', '300');
INSERT INTO `sys_role_menu` VALUES ('2', '301');
INSERT INTO `sys_role_menu` VALUES ('2', '307');
INSERT INTO `sys_role_menu` VALUES ('2', '308');
INSERT INTO `sys_role_menu` VALUES ('2', '309');
INSERT INTO `sys_role_menu` VALUES ('2', '311');
INSERT INTO `sys_role_menu` VALUES ('2', '312');
INSERT INTO `sys_role_menu` VALUES ('2', '313');
INSERT INTO `sys_role_menu` VALUES ('2', '314');
INSERT INTO `sys_role_menu` VALUES ('2', '316');
INSERT INTO `sys_role_menu` VALUES ('2', '318');
INSERT INTO `sys_role_menu` VALUES ('2', '319');
INSERT INTO `sys_role_menu` VALUES ('2', '320');
INSERT INTO `sys_role_menu` VALUES ('2', '321');
INSERT INTO `sys_role_menu` VALUES ('2', '322');
INSERT INTO `sys_role_menu` VALUES ('2', '323');
INSERT INTO `sys_role_menu` VALUES ('2', '324');
INSERT INTO `sys_role_menu` VALUES ('2', '325');
INSERT INTO `sys_role_menu` VALUES ('2', '326');
INSERT INTO `sys_role_menu` VALUES ('2', '327');
INSERT INTO `sys_role_menu` VALUES ('2', '328');
INSERT INTO `sys_role_menu` VALUES ('2', '329');
INSERT INTO `sys_role_menu` VALUES ('2', '330');
INSERT INTO `sys_role_menu` VALUES ('2', '331');
COMMIT;

-- ----------------------------
-- Records of sys_task
-- demo
-- ----------------------------
# BEGIN;
# INSERT INTO `sys_task` VALUES (1, '心跳检测', 'heartBeatJob', '0 */5 * * * ?', 'heartBeatJob', 0, now(), '执行成功,发送心跳数据到设备台数:26', '', '', now(), now(), '1', '1', 0);
# COMMIT;


-- ----------------------------
-- Records of sys_param_class
-- Records of sys_param_type
-- ----------------------------
BEGIN;
INSERT INTO sys_param_class (PARAM_TYPE_CLASS, PARAM_TYPE_NAME, VISIBLE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, PHYSICS_FLAG, version) VALUES ('SYS_USER_TYPE', 'SYS_USER_TYPE', 1, 'system', now(), 'system', now(), 'SYS_USER_TYPE', 1, 1);
INSERT INTO sys_param_type (PARAM_TYPE_CLASS, PARAM_TYPE_ID, PARAM_TYPE_CODE, PARAM_TYPE_NAME, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, PHYSICS_FLAG, i18n_field, version) VALUES ('SYS_USER_TYPE', 1, 'MANAGER', '管理员', 'system', now(), 'system', now(), '', 1, '[{"key":"zh_CN","value":"管理员","name":"中文简体"}]', 1);
INSERT INTO sys_param_type (PARAM_TYPE_CLASS, PARAM_TYPE_ID, PARAM_TYPE_CODE, PARAM_TYPE_NAME, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, PHYSICS_FLAG, i18n_field, version) VALUES ('SYS_USER_TYPE', 2, 'USER', '普通用户', 'system', now(), 'system', now(), '', 1, '[{"key":"zh_CN","value":"普通用户","name":"中文简体"}]', 1);
COMMIT;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user`(ID, DEPT_ID, LOGIN_NAME, PASSWORD, SALT, NAME, MOBILE, PHOTO, EMAIL, USER_TYPE, STATUS, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, lang) VALUES
(1, NULL, 'superadmin', '573cb6da1a1c15ebe3040eb9f7b46c483355b8e7edbd4d531bf8a0c0201ab064', 'rNBdNtjuefmwLGzXjHoN', '超级管理员', '18999999999', '/2018/06/02/60d2e10d1c714076ac972c492ddeb822.png', '21221@qq.com', '1', '1', 'system', now(), 'system', now(), '超级管理员', 'zh_CN');
COMMIT;

