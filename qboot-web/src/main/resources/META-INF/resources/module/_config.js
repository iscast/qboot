layui.define(function (exports) {

    var _config = {
        url: {
          host: "/"
        },
        // base_server: '/', // 接口地址，实际项目请换成http形式的地址
        base_server: '', // 接口地址，实际项目请换成http形式的地址
        tableName: 'qboot',  // 存储表名
        autoRender: false,  // 窗口大小改变后是否自动重新渲染表格，解决layui数据表格非响应式的问题，目前实现的还不是很好，暂时关闭该功能
        pageTabs: true,   // 是否开启多标签
        loginPage: '',
        // 获取缓存的token
        getToken: function () {
            var t = layui.sessionData(_config.tableName).token;
            if (t) {
                return JSON.parse(t);
            }
        },
        removeToken: function () {
            layui.sessionData(_config.tableName, {
                key: 'token',
                remove: true
            });
        },
        // 缓存token
        putToken: function (token) {
            layui.sessionData(_config.tableName, {
                key: 'token',
                value: JSON.stringify(token)
            });
        },
        getLoginPage: function() {
            let loginPage = _config.loginPage;
            if(loginPage &&  typeof(loginPage)!="undefined") {
                return loginPage;
            }
            loginPage = '/login_pc.html';
            $.ajaxSettings.async = false;
            $.get(_config.base_server + '/user/getLoginPage', function(res) {
                console.log("request result:" + res.data.loginPage)
                if(res.data && res.data.loginPage) {
                    _config.loginPage = res.data.loginPage;
                    loginPage = res.data.loginPage
                }
            });
            $.ajaxSettings.async = true;
            return loginPage;
        },
        // 导航菜单，最多支持三级，因为还有判断权限，所以渲染左侧菜单很复杂，无法做到递归，你需要更多极请联系我添加，添加可以无限添加，只是无法做到递归
        menus: [
            {
                name: '主页',
                url: 'javascript:;',
                icon: 'layui-icon-home',
                subMenus: [{
                    name: '主页一',
                    url: '#!console',
                    path: 'console.html'
                }]
            }, {
                name: '系统管理',
                icon: 'layui-icon-set',
                url: 'javascript:;',
                subMenus: [{
                    name: '用户管理',
                    url: '#!user',  // 这里url不能带斜杠，因为是用递归循环进行关键字注册，带斜杠会被q.js理解为其他注册模式
                    path: 'system/user.html',
                    auth: 'post:/user/query'
                }]
            },{
                name: "错误页面",
                url: "#!error",
                path: 'error/error.html',
                hidden: true
            }
        ],
        // current login user
        getUser: function () {
            var u = layui.sessionData(_config.tableName).login_user;
            if (u) {
                return JSON.parse(u);
            }
        },
        // cache user
        putUser: function (user) {
            layui.sessionData(_config.tableName, {
                key: 'login_user',
                value: JSON.stringify(user)
            });
        },
    };
    exports('_config', _config);
});