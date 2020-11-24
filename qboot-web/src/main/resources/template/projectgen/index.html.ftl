<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>${projectName}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="assets/libs/layui/css/layui.css"/>
    <link rel="stylesheet" href="assets/css/admin.css"/>
    <link rel="stylesheet" href="module/formSelects/formSelects-v4.css"/>
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin" id="root">
    <!-- 头部 -->
    <div class="layui-header" p-com="header"></div>

    <!-- 侧边栏 -->
    <div class="layui-side" p-com="side"></div>

    <!-- 主体部分 -->
    <div class="layui-body">
        <div class="layui-tab" lay-allowClose="true" lay-filter="admin-pagetabs">
            <ul class="layui-tab-title">
            </ul>
            <div class="layui-tab-content">
            </div>
        </div>
        <div class="layui-icon admin-tabs-control layui-icon-prev" ew-event="leftPage"></div>
        <div class="layui-icon admin-tabs-control layui-icon-next" ew-event="rightPage"></div>
        <div class="layui-icon admin-tabs-control layui-icon-down">
            <ul class="layui-nav admin-tabs-select" lay-filter="admin-pagetabs-nav">
                <li class="layui-nav-item" lay-unselect>
                    <a href="javascript:;"></a>
                    <dl class="layui-nav-child layui-anim-fadein">
                        <dd ew-event="closeThisTabs" lay-unselect><a href="javascript:;">关闭当前标签页</a></dd>
                        <dd ew-event="closeOtherTabs" lay-unselect><a href="javascript:;">关闭其它标签页</a></dd>
                        <dd ew-event="closeAllTabs" lay-unselect><a href="javascript:;">关闭全部标签页</a></dd>
                    </dl>
                </li>
            </ul>
        </div>
    </div>

    <!-- 底部 -->
    <!--如果加底部说明，需要改动样式-->
    <!--<div class="layui-footer">Copyright © 2018 EasyWeb All rights reserved. <span class="pull-right">Version 2.0</span></div>-->

    <!-- 手机屏幕遮罩层 -->
    <div class="site-mobile-shade"></div>
</div>

<script type="text/javascript" src="assets/libs/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="assets/libs/q.js"></script>
<script type="text/javascript" src="assets/libs/pandyle.min.js"></script>
<script type="text/javascript" src="assets/libs/layui/layui.js"></script>

<script>
    layui.config({
        base: 'module/'
    }).extend({
        formSelects: 'formSelects/formSelects-v4',
        treetable: 'treetable-lay/treetable',
        treeGrid: 'treeGrid/treeGrid',
    }).use(['_config', 'index', 'element', 'layer'], function () {
        var $ = layui.$;
        var _config = layui._config;
        var index = layui.index;
        var element = layui.element;
        var layer = layui.layer;

        // 检查是否登录
        if (!_config.getToken() || _config.getToken() == '') {
            location.replace(_config.getLoginPage());
            return;
        }

        // 检查多标签功能是否开启
        index.checkPageTabs();

        //获取用户权限
        index.getAuth(function(menus) {
            // 获取当前用户信息
            index.getUser(function (user) {
                $('.layui-layout-admin .layui-header').vm(user);
                index.initLeftNav(menus);
                element.render('nav');
                index.initRouter(menus);
                index.bindEvent();
            });
        });

        var hash = location.hash.substring(2);
        var pageName = hash.split("_")[0];

        //生成menu Array的时候也要生成一个menu的Object
        var menuObj = {
            userInfo: {
                name: '用户详情',
                path: 'user/info.html'
            }
        };
        

        if( !Q[hash] ) {
            Q.reg(hash, function () {
                index.loadView(hash, 'pages/' + menuObj[pageName].path, menuObj[pageName].name);
            });
        }

    });
</script>
</body>

</html>