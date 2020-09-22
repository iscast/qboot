
-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES (1, '1', '0,1', '超级管理', 1, '1', '2018-05-26 16:52:35', '1', '2019-04-10 17:35:05', '');
COMMIT;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` VALUES (1, 'YES_NO', '1', '是', 1, '1', '1', '2018-04-05 18:30:36', '1', '2018-04-07 17:38:00', '');
INSERT INTO `sys_dict` VALUES (2, 'YES_NO', '0', '否', 2, '1', '1', '2018-04-06 08:43:50', '1', '2018-04-06 08:43:50', '');
INSERT INTO `sys_dict` VALUES (3, 'SYS_USER_TYPE', '0', '系统管理员', 1, '0', '1', '2018-04-07 16:48:51', '1', '2018-04-15 00:13:37', '');
INSERT INTO `sys_dict` VALUES (4, 'SYS_USER_TYPE', '1', '普通用户', 2, '1', '1', '2018-04-07 16:49:04', '1', '2018-04-15 00:13:41', '');
INSERT INTO `sys_dict` VALUES (5, 'SEX', '1', '男', 1, '1', '1', '2018-06-02 11:02:55', '1', '2018-06-02 11:02:55', '');
INSERT INTO `sys_dict` VALUES (6, 'SEX', '2', '女', 2, '1', '1', '2018-06-02 11:03:04', '1', '2018-06-02 11:03:04', '');
COMMIT;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1, '0', '0,', '系统管理', 1, '', '', '', 'layui-icon-set', '1', 'sys', '1', '2018-04-15 00:05:08', 'swiftpass', '2019-04-26 10:56:40', '系统管理');
INSERT INTO `sys_menu` VALUES (2, '1', '0,,1', '用户管理', 1, '/sys/user.html', 'main', '', '', '1', 'sys:user', '1', '2018-04-15 00:05:46', 'swiftpass', '2019-04-26 10:56:54', '12122212');
INSERT INTO `sys_menu` VALUES (3, '1', '0,,1', '角色管理', 2, '/sys/role.html', 'main', '1', '', '1', 'sys:role', '1', '2018-04-15 00:07:38', 'swiftpass', '2019-04-26 10:59:39', '');
INSERT INTO `sys_menu` VALUES (4, '1', '0,,1', '菜单管理', 3, '/sys/menu.html', 'main', '1', '', '1', 'sys:menu', '1', '2018-04-15 00:08:16', 'swiftpass', '2019-04-26 11:02:25', '');
INSERT INTO `sys_menu` VALUES (5, '1', '0,1', '部门管理', 4, '/sys/dept.html', 'main', '1', NULL, '0', 'sys:dept', '1', '2018-04-15 00:06:37', '0', '2018-09-17 18:04:25', '');
INSERT INTO `sys_menu` VALUES (6, '1', '0,,1', '系统参数', 5, '/sys/dict.html', 'main', '1', '', '1', 'sys:dict', '1', '2018-04-15 00:09:12', 'swiftpass', '2019-04-26 11:02:54', '');
INSERT INTO `sys_menu` VALUES (7, '1', '0,,1', '系统类型', 6, '/sys/param.html', 'main', '1', '', '1', 'sys:param', '1', '2018-04-15 00:10:05', 'swiftpass', '2019-04-26 11:03:13', '');
INSERT INTO `sys_menu` VALUES (8, '1', '0,,1', '任务管理', 10, '/sys/task.html', 'main', '1', '', '1', 'sys:task', '1', '2018-04-15 21:44:01', 'swiftpass', '2019-04-26 11:02:03', '');
INSERT INTO `sys_menu` VALUES (10, '79', '0,79', '登录日志', 7, '/sys/loginlog.html', 'main', '1', '', '0', 'sys:loginlog', '1', '2018-05-31 23:04:50', '1', '2018-09-28 14:25:57', '');
INSERT INTO `sys_menu` VALUES (11, '2', '0,1,2,', '修改', 2, NULL, NULL, '999', NULL, '1', 'sys:user:update', '1', '2018-04-16 21:48:05', '1', '2018-09-14 15:10:12', NULL);
INSERT INTO `sys_menu` VALUES (12, '2', '0,12', '删除', 10, '', '', '999', NULL, '1', 'sys:user:delete', '1', '2018-04-16 21:51:02', '1', '2018-09-18 15:50:28', '');
INSERT INTO `sys_menu` VALUES (13, '2', '0,,1,2', '添加', 1, '', NULL, '999', '', '1', 'sys:user:save', '1', '2018-04-16 21:47:42', 'swiftpass', '2019-04-28 15:13:32', NULL);
INSERT INTO `sys_menu` VALUES (14, '3', '0,1,3,', '添加', 10, NULL, NULL, '999', NULL, '1', 'sys:role:save', '1', '2018-04-19 14:36:23', '1', '2018-04-19 14:36:23', NULL);
INSERT INTO `sys_menu` VALUES (15, '3', '0,1,3,', '删除', 10, NULL, NULL, '999', NULL, '1', 'sys:role:delete', '1', '2018-04-19 14:36:48', '1', '2018-09-17 16:33:20', NULL);
INSERT INTO `sys_menu` VALUES (16, '3', '0,1,3,', '修改', 10, NULL, NULL, '999', NULL, '1', 'sys:role:update', '1', '2018-04-19 14:36:37', '1', '2018-04-19 14:36:37', NULL);
INSERT INTO `sys_menu` VALUES (17, '4', '0,1,4,', '删除', 10, NULL, NULL, '999', NULL, '1', 'sys:menu:delete', '1', '2018-04-19 14:47:47', '1', '2018-09-13 10:55:24', NULL);
INSERT INTO `sys_menu` VALUES (18, '4', '0,1,4,', '修改', 10, NULL, NULL, '999', NULL, '1', 'sys:menu:update', '1', '2018-04-19 14:47:34', '1', '2018-04-19 14:47:34', NULL);
INSERT INTO `sys_menu` VALUES (19, '4', '0,1,4,', '添加', 10, NULL, NULL, '999', NULL, '1', 'sys:menu:save', '1', '2018-04-19 14:47:12', '1', '2018-04-19 14:47:12', NULL);
INSERT INTO `sys_menu` VALUES (20, '5', '0,1,5', '添加', 10, NULL, NULL, '999', NULL, '0', 'sys:dept:save', '1', '2018-04-19 14:40:21', '1', '2018-04-19 14:40:21', NULL);
INSERT INTO `sys_menu` VALUES (21, '5', '0,1,5,', '删除', 10, NULL, NULL, '999', NULL, '0', 'sys:dept:delete', '1', '2018-04-19 14:40:54', '1', '2018-09-13 10:55:22', NULL);
INSERT INTO `sys_menu` VALUES (22, '5', '0,1,5,', '修改', 10, NULL, NULL, '999', NULL, '0', 'sys:dept:update', '1', '2018-04-19 14:40:36', '1', '2018-04-19 14:40:36', NULL);
INSERT INTO `sys_menu` VALUES (23, '6', '0,1,6', '删除', 10, NULL, NULL, '999', NULL, '1', 'sys:dict:delete', '1', '2018-04-19 15:00:12', '1', '2018-09-13 10:55:29', NULL);
INSERT INTO `sys_menu` VALUES (24, '6', '0,1,6,', '修改', 10, NULL, NULL, '999', NULL, '1', 'sys:dict:update', '1', '2018-04-19 15:00:01', '1', '2018-04-19 15:00:01', NULL);
INSERT INTO `sys_menu` VALUES (25, '6', '0,1,6,', '添加', 10, NULL, NULL, '999', NULL, '1', 'sys:dict:save', '1', '2018-04-19 14:59:49', '1', '2018-04-19 14:59:49', NULL);
INSERT INTO `sys_menu` VALUES (26, '7', '0,1,7,', '修改', 10, NULL, NULL, '999', NULL, '1', 'sys:param:update', '1', '2018-04-19 15:06:27', '1', '2018-04-19 15:06:27', NULL);
INSERT INTO `sys_menu` VALUES (27, '7', '0,1,7,', '添加', 10, NULL, NULL, '999', NULL, '1', 'sys:param:save', '1', '2018-04-19 15:05:58', '1', '2018-04-19 15:05:58', NULL);
INSERT INTO `sys_menu` VALUES (28, '7', '0,1,7,', '删除', 10, NULL, NULL, '999', NULL, '1', 'sys:param:delete', '1', '2018-04-19 15:06:39', '1', '2018-09-13 10:55:35', NULL);
INSERT INTO `sys_menu` VALUES (29, '8', '0,,1,8', '新增', 10, '', NULL, '999', '', '1', 'sys:task:save', '1', '2018-04-19 15:09:07', 'swiftpass', '2019-05-16 18:43:43', NULL);
INSERT INTO `sys_menu` VALUES (30, '8', '0,,1,8', '删除', 10, '', NULL, '999', '', '1', 'sys:task:delete', '1', '2018-04-19 15:07:51', 'swiftpass', '2019-05-16 18:44:21', NULL);
INSERT INTO `sys_menu` VALUES (31, '35', '0,9,', '修改', 2, NULL, NULL, '999', NULL, '1', 'sys:gen:update', '1', '2018-05-13 21:07:04', '1', '2018-05-13 21:07:04', NULL);
INSERT INTO `sys_menu` VALUES (32, '35', '0,9,', '删除', 10, NULL, NULL, '999', NULL, '1', 'sys:gen:delete', '1', '2018-05-13 21:07:04', '1', '2018-09-13 10:55:43', NULL);
INSERT INTO `sys_menu` VALUES (33, '35', '0,9,', '添加', 1, NULL, NULL, '999', NULL, '1', 'sys:gen:save', '1', '2018-05-13 21:08:06', '1', '2018-05-13 21:08:06', NULL);
INSERT INTO `sys_menu` VALUES (34, '10', '0,10,', '删除', 10, NULL, NULL, '999', NULL, '0', 'sys:loginlog:delete', '1', '2018-05-31 23:04:50', '1', '2018-09-14 14:44:19', NULL);
INSERT INTO `sys_menu` VALUES (35, '1', '0,1', '代码生成', 999, '/sys/gen.html', 'main', '1', '', '1', 'sys:gen', '1', '2018-04-26 22:19:45', '1', '2018-09-27 10:55:22', '代码生成');
INSERT INTO `sys_menu` VALUES (37, '3', '0,1,3', '查询角色', 1, '/rdp/sys/role/qryPage', '', '999', NULL, '1', 'sys:role:qry', '0', '2018-09-13 14:52:59', '0', '2018-09-13 15:29:39', '111');
INSERT INTO `sys_menu` VALUES (38, '2', '0,1,2', '查询用户', 1, '/rdp/sys/user/qryPage', '1', '999', NULL, '1', 'sys:user:qry', '0', '2018-09-13 15:26:52', '0', '2018-09-14 15:10:11', '1');
INSERT INTO `sys_menu` VALUES (39, '4', '0,1,4', '查询菜单', 1, '/rdp/sys/menu/qryPage', '', '999', NULL, '1', 'sys:menu:qry', '0', '2018-09-13 15:30:51', NULL, '2018-10-20 18:09:19', '');
INSERT INTO `sys_menu` VALUES (40, '5', '0,1,5', '查询部门', 1, '/rdp/sys/dept/qryPage', '', '999', NULL, '0', 'sys:dept:qry', '0', '2018-09-13 15:31:54', NULL, '2018-09-13 15:31:54', '');
INSERT INTO `sys_menu` VALUES (41, '6', '0,,1,6', '查询系统参数', 1, '/rdp/sys/dict/qryPage', '', '999', '', '1', 'sys:dict:qry', '0', '2018-09-13 15:32:53', '1', '2018-10-29 15:05:07', '');
INSERT INTO `sys_menu` VALUES (42, '7', '0,,1,7', '查询系统类型', 1, '/rdp/sys/param/qryPage', '', '999', '', '1', 'sys:param:qry', '0', '2018-09-13 15:33:47', '1', '2018-10-29 15:04:47', '');
INSERT INTO `sys_menu` VALUES (43, '9', '0,1,9', '查询生成案例', 1, '/rdp/sys/gen/qryPage', '', '999', NULL, '1', 'sys:gen:qry', '0', '2018-09-13 15:35:35', NULL, '2018-09-14 23:07:09', '');
INSERT INTO `sys_menu` VALUES (44, '8', '0,,1,8', '查询', 1, '', '', '999', '', '1', 'sys:task:qry', '0', '2018-09-13 15:36:39', 'swiftpass', '2019-05-16 18:43:06', '');
INSERT INTO `sys_menu` VALUES (46, '10', '0,1,10', '查询日志', 1, '/rdp/sys/log/qryPage', '', '999', NULL, '0', 'sys:loginlog:qry', '0', '2018-09-13 17:00:59', NULL, '2018-09-13 17:00:59', '');
INSERT INTO `sys_menu` VALUES (58, '35', '0,1,35', '查询', 1, '/rdp/sys/gen/qry', '查询', '999', NULL, '1', 'sys:gen:qry', '0', '2018-09-15 11:49:01', NULL, '2018-09-15 11:49:01', '');
INSERT INTO `sys_menu` VALUES (65, '1', '0,,1', '访问日志', 2, '/sys/operatlog.html', '2', '0', '', '1', 'sys:operatelog', '1', '2018-09-21 15:05:45', 'swiftpass', '2019-04-26 10:59:59', '');
INSERT INTO `sys_menu` VALUES (66, '65', '0,79,65', '查询', 1, '/rdp/sys/operatelog/qry', '', '999', 'layui-icon-search', '1', 'sys:operatelog:qry', '1', '2018-09-21 15:06:50', '1', '2018-10-17 11:02:27', '');
INSERT INTO `sys_menu` VALUES (67, '1', '0,,1', '日志配置', 8, '/sys/operateinfo.html', '', '0', '', '1', 'sys:operateinfo', '1', '2018-09-21 15:07:46', 'swiftpass', '2019-04-26 11:04:19', '');
INSERT INTO `sys_menu` VALUES (68, '67', '0,79,67', '查询', 1, '/rdp/sys/operateinfo/qry', '', '999', 'layui-icon-search', '1', 'sys:operateinfo:qry', '1', '2018-09-21 15:08:35', '4', '2018-10-17 11:03:12', '');
INSERT INTO `sys_menu` VALUES (69, '67', '0,1,67', '添加', 2, '/rdp/sys/operateinfo/save', '', '999', NULL, '1', 'sys:operateinfo:save', '1', '2018-09-21 15:09:17', NULL, '2018-09-21 15:09:17', '');
INSERT INTO `sys_menu` VALUES (70, '67', '0,1,67', '修改', 3, '/rdp/sys/operateinfo/update', '', '999', NULL, '1', 'sys:operateinfo:update', '1', '2018-09-21 15:10:12', '1', '2018-09-26 11:09:43', '');
INSERT INTO `sys_menu` VALUES (71, '67', '0,1,67', '删除', 4, '/rdp/sys/operateinfo/delete', '', '999', NULL, '1', 'sys:operateinfo:delete', '1', '2018-09-21 15:10:45', NULL, '2018-09-21 15:10:45', '');
INSERT INTO `sys_menu` VALUES (79, '0', '0,', '系统监控', 10, '', '', '0', 'layui-icon-console', '0', 'monitor', '1', '2018-09-28 14:24:11', '1', '2018-11-03 15:29:44', '');
INSERT INTO `sys_menu` VALUES (84, '79', '0,,79', '在线用户', 1, '/sys/online.html', '1', '1', '', '0', 'sys:online', '1', '2018-10-22 14:14:04', '1', '2018-10-22 15:00:51', '');
INSERT INTO `sys_menu` VALUES (85, '79', '0,,79', 'DB请求监控', 3, '/sys/dbmonitor.html', '3', '1', '', '0', 'sys:dbmonitor', '1', '2018-10-22 14:15:41', NULL, '2018-10-22 14:15:41', '');
INSERT INTO `sys_menu` VALUES (86, '79', '0,,79', '服务器监控', 4, '/sys/sysmonitor.html', '4', '1', '', '0', 'sys:sysmonitor', '1', '2018-10-22 14:16:56', '1', '2018-10-22 15:01:39', '');
COMMIT;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (2, '普通管理员', '1', '2019-07-02 15:01:59', '1', '2019-07-24 15:43:23', '停车场');
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
-- ----------------------------
BEGIN;
INSERT INTO `sys_task` VALUES (1, '心跳检测', 'heartBeatJob', '0 */5 * * * ?', 'heartBeatJob', 0, '2019-08-16 19:10:00', '执行成功,发送心跳数据到设备台数:26', '', '', '2019-07-24 11:09:50', '2019-08-16 11:11:21', '1', '1', 0);
COMMIT;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, NULL, 'swiftpass', '573cb6da1a1c15ebe3040eb9f7b46c483355b8e7edbd4d531bf8a0c0201ab064', 'rNBdNtjuefmwLGzXjHoN', '超级管理员', '18999999999', '/2018/06/02/60d2e10d1c714076ac972c492ddeb822.png', '21221@qq.com', '2', '1', '1', '2017-12-29 14:22:04', '1', '2019-07-22 17:46:31', '超级管理员', NULL, NULL, '', NULL, 'zh_CN');
COMMIT;

