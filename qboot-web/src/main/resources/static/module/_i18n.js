layui.define(['_config', 'admin'], function (exports) {
    var locale = "";
    var $ = layui.jquery;
    var lang = sessionStorage.getItem("lang");
    var localeData = lang ? {lang: lang} : {};
    $.get('/i18n/getLocale', localeData, function (d) {
        locale = d.data;
        //get all global
        var msgs;
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
                var result;
                $.ajax({
                    url: 'module/i18n/' + module + '/' + _i18n.locale + '.json',
                    type: "get",
                    dataType: "json",
                    async: false,
                    success: function (obj) {
                        result = obj;
                        if(_i18n.modules.indexOf(module)<0){
                            _i18n.mod = $.extend(_i18n.mod,obj);
                            _i18n.modules.push(module)
                        }

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
            getLang: function () {
                return (locale.substr(0, 2) == "zh" ? "cn" : "en");
            },
            getValByI18nField:function(i18nField){
                var paramTypeName = '';
                if (!i18nField) {
                    return '';
                }
                var arr = JSON.parse(i18nField);
                if (arr && arr.length > 0) {
                    for (var j in arr) {
                        if (_i18n.locale == arr[j].key) {
                            paramTypeName = arr[j].value;
                        }
                    }
                }
                return paramTypeName;
            },
            getType:function(data,type){
                var paramTypeName = '';
                if(!data){
                    return type;
                }
                for (var i = 0; i < data.length; i++) {
                    if (data[i].paramTypeId==type&&data[i].i18nField) {
                        paramTypeName = _i18n.getValByI18nField(data[i].i18nField);
                    }
                }
                return paramTypeName;
            },
            getTypeByClass:function(paramKey,type){
                if(!paramKey||!type){
                    return "";
                }
                if(_i18n.sysType[paramKey+"_"+type]){
                    return _i18n.sysType[paramKey+"_"+type];
                }
                var paramData = _i18n.getParamTypeData(paramKey);
                var i18nType = _i18n.getType(paramData,type);
                if(i18nType) {
                    _i18n.sysType[paramKey+"_"+type] = i18nType;
                }
                return i18nType;
            },
            getOptions: function (data,noDefault) {
                if (!data || data.length == 0) {
                    return '';
                }
                var typeHtml = noDefault?'':'<option value="">' + msgs['layui.select.first.option'] + '</option>';

                $.each(data,function(index,value){
                    if(value){
                        var paramTypeName = _i18n.getValByI18nField(value.i18nField);
                        if (value.paramTypeId) {
                            typeHtml += "<option value=\"" + value.paramTypeId + "\">"
                                + (paramTypeName ? paramTypeName : value.paramTypeName)
                                + "</option>";
                        }
                    }
                });
                return typeHtml;
            },
            getParamTypeData:function(paramKey){
                var optData = _i18n.options[paramKey];
                if(!optData||optData.length==0){
                    $.ajax({
                        url:"/i18n/getType",
                        type: 'Get',
                        async: false,
                        data:{paramKey:paramKey},
                        success:function (data) {
                            if(0 == data.code){
                                _i18n.options[paramKey] = data.data;
                                optData = _i18n.options[paramKey];
                            }
                        }
                    });
                }
                return optData;
            },
            getOptionsByClass:function (paramKey,noDefault) {
                return _i18n.getOptions(_i18n.getParamTypeData(paramKey),noDefault);
            },
            getCheckBoxByClass:function (paramKey,name,layFilter) {
                var checkBoxData = _i18n.getParamTypeData(paramKey);
                if(!checkBoxData||checkBoxData.length==0){
                    return '';
                }
                var inputs = '';
                $.each(checkBoxData,function(index,value){
                    var paramTypeName = _i18n.getValByI18nField(value.i18nField);
                    inputs += ('<input type="checkbox" '
                        +'name="'+name +
                        '" lay-skin="primary" value="'+value.paramTypeId+'" ' +
                        'title="'+(paramTypeName?paramTypeName:value.paramTypeName)+'"' +
                        (layFilter?'lay-filter="'+layFilter:'')+'"/>');

                });
                return inputs;
            }
        };
        exports('_i18n', _i18n);
    });
});
