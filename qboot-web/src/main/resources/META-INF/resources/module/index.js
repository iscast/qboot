layui.define(['_config', 'admin', 'layer', 'laytpl', 'element', 'form','_i18n'], function (exports) {
    var $ = layui.$;
    var _config = layui._config;
    var admin = layui.admin;
    var layer = layui.layer;
    var laytpl = layui.laytpl;
    var element = layui.element;
    var form = layui.form;
    var _i18n = layui._i18n;

    function getMenu(menus) {
        var _menus = $.extend( [], menus );
        for (var i = menus.length - 1; i >= 0; i--) {
            var tempMenu = _menus[i];
            if(_i18n.getGlobalVal(tempMenu.name)){
                tempMenu.name = _i18n.getGlobalVal(tempMenu.name);
            }
            if (tempMenu.auth && !admin.hasPerm(tempMenu.auth)) {
                _menus.splice(i, 1);
                continue;
            }

            if (tempMenu.subMenus && tempMenu.subMenus.length <= 0) {
                _menus.splice(i, 1);
                continue;
            }

            if (!tempMenu.subMenus) {
                continue;
            }

            getMenu(tempMenu.subMenus);
        }
        return menus;
    }

    var index = {
        // 渲染左侧导航栏
        initLeftNav: function (menus) {
            // 判断权限
            var _menus = getMenu(menus);
            // 渲染
            $('.layui-layout-admin .layui-side').load('pages/side.html', function () {
                laytpl(sideNav.innerHTML).render(_menus, function (html) {
                    $('#sideNav').after(html);
                });
                element.render('nav');
                admin.activeNav(Q.lash);
            });
        },
        // 路由注册
        initRouter: function (menus) {
            index.regRouter(menus);
            Q.init({
                index: 'index_page'
            });
        },
        // 使用递归循环注册
        regRouter: function (menus) {
            $.each(menus, function (i, data) {
                if (data.url && data.url.indexOf('#!') == 0) {
                    Q.reg(data.url.substring(2), function () {
                        var menuId = data.url.substring(2);
                        var menuPath = 'pages/' + data.path;
                        index.loadView(menuId, menuPath, data.name);
                    });
                }
                if (data.subMenus) {
                    index.regRouter(data.subMenus);
                }
            });
        },
        // 路由加载组件
        loadView: function (menuId, menuPath, menuName) {
            var contentDom = '.layui-layout-admin .layui-body';
            admin.showLoading('.layui-layout-admin .layui-body');
            var flag;  // 选项卡是否添加
            // 判断是否开启了选项卡功能
            if (_config.pageTabs) {
                $('.layui-layout-admin .layui-body .layui-tab .layui-tab-title>li').each(function (index) {
                    if ($(this).attr('lay-id') === menuId) {
                        flag = true;
                    }
                });
                if (!flag) {
                    element.tabAdd('admin-pagetabs', {
                        title: menuName,
                        content: '<div id="' + menuId + '"></div>',
                        id: menuId
                    });
                }
                contentDom = '#' + menuId;
                element.tabChange('admin-pagetabs', menuId);
                admin.rollPage('auto');
                // 切换tab关闭表格内浮窗
                $('.layui-table-tips-c').trigger('click');
                // 解决切换tab滚动条时而消失的问题
                var $iframe = $('.layui-layout-admin .layui-body .layui-tab-content .layui-tab-item.layui-show .admin-iframe')[0];
                if ($iframe) {
                    $iframe.style.height = "99%";
                    $iframe.scrollWidth;
                    $iframe.style.height = "100%";
                }
            }
            if (!flag || admin.isRefresh) {
                $(contentDom).load(menuPath, function () {
                    admin.isRefresh = false;
                    element.render('breadcrumb');
                    form.render('select');
                    admin.removeLoading('.layui-layout-admin .layui-body');
                });
            } else {
                admin.removeLoading('.layui-layout-admin .layui-body');
            }
            admin.activeNav(Q.lash);
            // 移动设备切换页面隐藏侧导航
            if (document.body.clientWidth <= 750) {
                admin.flexible(true);
            }
        },
        //获取用户权限
        getAuth: function(callback) {
            try{
            	$.ajax({
            		url: _config.base_server + '/user/getUserMenus',
            		cache: false,
            		method: 'post',
            		success: function(res) {
                        var data = res.data;
                        var menuArray = [
                        	{
                        		name: _i18n.getGlobalVal("layui.menu.home"),
                        		url: '#!index_page',
                                path: 'index_page.html',
                                icon: 'layui-icon-home',
                            }
                        ];
                        var subArray = [];
                        var menuObejct = {};
                        var authObject = {};
                        
                        if(data == null || data.length == 0){
                            layer.msg('get user fail', {icon: 2});
                            _config.removeToken();
                            location.replace('login.html');
                        }
                        
                        for( var i = 0; i < data.length; i ++ ) {
                            let originalName = data[i].name;
                            if('zh_CN' != _i18n.getLang()) {
                                let ornModule = data[i].permission;
                                if(ornModule.indexOf(':') != -1) {
                                    ornModule = data[i].permission.split(':')[0];
                                }
                                let i18nModuleName = _i18n.getModuleVal(ornModule, data[i].permission);
                                if(!!i18nModuleName) {
                                    originalName = i18nModuleName;
                                }
                            }
                            var menu = {
                                name: originalName,
                                url: 'javascript:;',
                                icon: data[i].icon
                            };
                            menuObejct[data[i].id] = menu;
                            menu.subMenus = [];

                            if( data[i].parentId == 0 || !data[i].parentId ) {
                            	menu.icon = data[i].icon;
                                menuArray.push( menu );
                            } else {
                                if( data[i].type == 999 ) {
                                    var authArr = data[i].permission.split(":");
                                    var len = authArr.length - 1;
                                    authArr.splice(len, 1);
                                    if( !authObject[ authArr.join("_") ] ) {
                                        authObject[ authArr.join("_") ] = {};
                                    }
                                    authObject[ authArr.join("_") ][data[i].permission] = 1;
                                } else {
                                	subArray.push( data[i] );
                                }
                                
                            }
                        }
                        
                        for( var i = 0; i < subArray.length; i ++ ) {
                        	var subMenu;
                        	if(subArray[i].hasSub == 0 || !subArray[i].hasSub) {
                        		var pageName = subArray[i].permission.replace(/\:/g, '_');
                                let originalName = subArray[i].name;
                                if('zh_CN' != _i18n.getLang()) {
                                    let ornModule = subArray[i].permission;
                                    if(ornModule.indexOf(':') != -1) {
                                        ornModule = subArray[i].permission.split(':')[0];
                                    }
                                    let i18nModuleName = _i18n.getModuleVal(ornModule, subArray[i].permission);
                                    if(!!i18nModuleName) {
                                        originalName = i18nModuleName;
                                    }
                                }
                                subMenu = {
                                    name: originalName,
                                    url: '#!' + pageName,
                                    path: subArray[i].href.substring(1),
                                    icon: subArray[i].icon
                                };
                                subMenu.icon = subArray[i].icon;
                                if(menuObejct[ subArray[i].parentId ]){
                                	menuObejct[ subArray[i].parentId ].subMenus.push(subMenu);
                                }
                        	} else {
                        		if(menuObejct[ subArray[i].parentId ]){
                        			menuObejct[ subArray[i].parentId ].subMenus.push(menuObejct[subArray[i].id]);
                        		}
                        	}
                        }
                        sessionStorage.setItem("auth", JSON.stringify(authObject));
                        
                        for(var attr in menuObejct) {
                        	if(menuObejct[attr].subMenus.length === 0) {
                        		delete menuObejct[attr];
                        	}
                        }
                        
                        var error = {
                                name: "错误页面",
                                url: "#!error",
                                path: 'error/error.html',
                                hidden: true
                            };
                        menuArray.push(error)
                        
                        callback(menuArray);
                    },
            		error:function(err){
            			layer.msg('获取权限失败', {icon: 2});
                        _config.removeToken();
                        location.replace('login.html');
            		}
            	});
            }catch(err){
            	layer.msg('获取权限失败', {icon: 2});
                _config.removeToken();
                location.replace('login.html');
            }
        },
        // 从服务器获取登录用户的信息
        getUser: function (success) {
            var load = layer.load(2);
            admin.req('/user/getUserInfo', {}, function (data) {
                layer.close(load);
                if (0 == data.code ) {
                    _config.putUser(data.data);
                    success(data.data);
                } else {
                    layer.msg('获取用户失败', {icon: 2});
                    _config.removeToken();
                    location.replace('login.html');
                }
            }, 'GET');
        },
        setLang:function(){
            var lang = sessionStorage.getItem("lang");
            if(lang){
                $('#setLang').val(lang);
            }else{
                var user = _config.getUser();
                if(user&&user.lang){
                    $('#setLang').val(user.lang);
                    sessionStorage.setItem("lang",user.lang);
                }else {
                    $.get('/i18n/getLocale', {}, function (data) {
                        $('#setLang').val(data.data);
                    })
                }
            }
        },
        // 页面元素绑定事件监听
        bindEvent: function () {
            _i18n.initPage();
            // 退出登录
            $('#btnLogout').click(function () {
                layer.confirm(_i18n.getGlobalVal('layer.confirm.logout.msg'),
                    _i18n.getBtns(),function () {
                    $.ajax({
                		url: _config.base_server + '/logout',
                		cache: false,
                		method: 'post',
                		success: function(res) {
                			_config.removeToken();
                            location.replace('login.html');
                        },
                		error:function(err){
                            _config.removeToken();
                            location.replace('login.html');
                		}
                	});
                });
            });
            // 修改密码
            $('#setPsw').click(function () {
                admin.popupRight('pages/sys/password.html');
            });
            $('#setLang').change(function () {
                $.ajax({
                    url: _config.base_server+'/user/switchLanguage',
                    data:{access_token:_config.getToken().access_token,lang:$(this).val()},
                    cache: false,
                    method: 'get',
                    success: function(res) {

                    }
                });
                sessionStorage.setItem("lang",$(this).val());
                location.reload();
            });
            // 个人信息
            $('#setInfo').click(function () {

            });
        },
        // 检查多标签功能是否开启
        checkPageTabs: function () {
            // 加载主页
            if (_config.pageTabs) {
                $('.layui-layout-admin').addClass('open-tab');
                element.tabAdd('admin-pagetabs', {
                    title: '<i class="layui-icon layui-icon-home"></i>',
                    content: '<div id="index_page"></div>',
                    id: 'index_page'
                });
                $('#index_page').load('pages/home.html', function () {
                });
            } else {
                $('.layui-layout-admin').removeClass('open-tab');
            }
        },
        // 打开新页面
        openNewTab: function (param) {
            var url = param.url;
            var title = param.title;
            var menuId = param.menuId;
            if (!menuId) {
                menuId = url.replace(/[?:=&/]/g, '_');
            }

            Q.reg(menuId, function () {
                index.loadView(menuId, url, title);
            });

            Q.go(menuId);
        },
        // 关闭选项卡
        closeTab: function (menuId) {
            element.tabDelete('admin-pagetabs', menuId);
        }
    };

    // tab选项卡切换监听
    element.on('tab(admin-pagetabs)', function (data) {
        var layId = $(this).attr('lay-id');

        Q.go(layId);
    });
    
    admin.req('/user/checkFirstLogin',{}, function (data) {
        if (0 != data.code ) {
        	layer.msg(data.msg, {icon: 1, time: 5000});
        	admin.popupRight('pages/sys/password.html');
        }
    }, 'get');

    exports('index', index);
});
