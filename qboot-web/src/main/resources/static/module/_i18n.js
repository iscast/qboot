layui.define(['_config', 'admin'], function (exports) {
    var locale = "";
    var $ = layui.jquery;
    var lang = sessionStorage.getItem("lang");
    var localeData = !lang ? {} : {lang: lang};
    $.post('/user/getLocale', localeData, function (d) {
        locale = d.data;
        var msgs;
        var moduleMsg = {};
        var i18nModuleCahe = {};
        $.ajax({
            url: 'module/i18n/global_' + locale + '.json',
            type: "get",
            dataType: "json",
            async: false,
            success: function (obj) {
                msgs = obj;
            }
        });
        var _i18n = {
            locale: locale,
            mod: new Object(),
            sysType: new Object(),
            options: new Array(),
            modules:[],
            getMsg: function (module) {
                if (!module) {
                    return '';
                }
                let cacheKey = module + '_' + _i18n.locale;

                let result = i18nModuleCahe[cacheKey];
                if(!!result) {
                    return result;
                }

                $.ajax({
                    url: 'module/i18n/' + module + '/' + _i18n.locale + '.json',
                    type: "get",
                    dataType: "json",
                    async: false,
                    success: function (obj) {
                        result = obj;
                        if(_i18n.modules.indexOf(module)<0){
                            _i18n.mod = $.extend(_i18n.mod,obj);
                            _i18n.modules.push(module);
                            i18nModuleCahe[cacheKey] = _i18n.mod;
                        }
                    },
                    error:function (e) {
                        var res = $.parseJSON(e.responseText);
                        console.log('read i18n error msg:' + res.msg);
                        i18nModuleCahe[cacheKey] = {};
                    }

                });
                return _i18n.mod;
            },
            initPage: function () {
                $(".layui-laypage-count").each(function (i, n) {
                    $(this).text($(this).text().replace("共", msgs['layui-laypage-count-start']).replace("条", msgs['layui-laypage-count-end']));
                });
                $(".layui-laypage-limits select option").each(function (i, n) {
                    $(this).text($(this).text().replace(/条\/页/g, msgs['layui-laypage-limits']));
                });
                $(".layui-btn-search").each(function (i, n) {
                    $(this).html('<i class="layui-icon">&#xe615;</i>' + msgs['layui.btn.search']);
                });
                $(".layui-btn-add").each(function (i, n) {
                    $(this).html('<i class="layui-icon">&#xe608;</i>' + msgs['layui.btn.add']);
                });
                $(".layui-btn-save").each(function (i, n) {
                    $(this).html(msgs['layui.btn.save']);
                });
                $(".layui-btn-cancel").each(function (i, n) {
                    $(this).html(msgs['layui.btn.cancel']);
                });
                $(".i18n-pic-btn").each(function (i, n) {
                    $(this).html('<i class="layui-icon">&#xe67c;</i>' + msgs['i18n-pic-btn']);
                });
                $(".layui-none").each(function (i,n) {
                    $(this).text(msgs['layui-laypage-text-none']);
                });
                $(".i18n-input-hint").each(function(i,n){
                    var modKey = $(this).attr("i18n-key");
                    if(modKey){
                        $(this).attr("placeholder",_i18n.mod[modKey]?_i18n.mod[modKey]:'');
                    }
                });
                var modMsg = _i18n.mod;
                $(".layui-i18n").each(function (i,n) {
                    var msgKey = $(this).attr("i18n-key");
                    if(modMsg&&msgKey) {
                        var msgVal = modMsg[msgKey];
                        msgVal = msgVal?msgVal:msgs[msgKey];
                        $(this).text(msgVal?msgVal:msgKey);
                    }
                });
                $(".layui-laypage-skip").each(function (i, n) {
                    if (locale != 'zh_CN') {
                        $(this).text('');
                    }
                });
                $('title').text(msgs['layui.menu.sysName']);
                $('#platformTitle').text(msgs['layui.menu.sysName']);
            },
            getDataNoneTips: function () {
                return msgs['layui-laypage-text-none'];
            },
            getBtns: function () {
                return {
                    btn: [_i18n.getGlobalVal('layer.confirm.btn.yes'),
                        _i18n.getGlobalVal('layer.confirm.btn.no')],
                    title: _i18n.getGlobalVal('layer.confirm.title')
                };
            },
            getGlobalVal: function (code) {
                if (!code) {
                    return '';
                }
                return msgs[code] ? msgs[code] : code;
            },
            getModuleVal(module, code) {
                if(!moduleMsg[module]) {
                    moduleMsg[module] = this.getMsg(module);
                }

                if(!!moduleMsg[module]) {
                    return moduleMsg[module][code];
                } else {
                    return '';
                }
            },
            getLang: function () {
                return locale;
            }
        };
        exports('_i18n', _i18n);
    });
});
