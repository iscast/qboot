<div class="layui-card">
    <div class="layui-card-body">

        <!-- 数据表格 -->
        <div id="${functionName}LayTable">
        	<table class="layui-table ${functionName}_auth" id="${functionName}-table" lay-filter="${functionName}-table" data-auth="${moduleName}:${functionName}:qry"></table>
        </div>
    </div>
</div>

<style>
	#${functionName}LayTable tbody .layui-table-click{background-color: #ccffeb !important;}
</style>
<script>
    layui.use(['form', 'table', 'util', '_config', 'admin', 'formSelects'], function () {
        var form = layui.form;
        var table = layui.table;
        var _config = layui._config;
        var layer = layui.layer;
        var util = layui.util;
        var admin = layui.admin;
        var formSelects = layui.formSelects;

        //渲染表格
        table.render({
            elem: '#${functionName}-table',
            url: _config.base_server + '/${moduleName}/${functionName}/qryPage',
            where: {
                access_token: _config.getToken().access_token
            },
            method: 'post',
            page: true,
            even: true, //开启隔行背景
            cols: [[
                {type: 'numbers'},
                <#list columnInfos as columnInfo>
			          {field: '${columnInfo.javaFieldName}', title: '${columnInfo.columnComment}'},
			    </#list>
                {align: 'center', templet: function(d){
                	var auth = JSON.parse(sessionStorage.getItem("auth"));
                	var page = location.hash.substring(2);
                	var upBtn = '';
                	var delBtn = '';
                	if(auth[page] && auth[page]['${moduleName}:${functionName}:update']){
                		upBtn = '<a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="edit" data-auth="${moduleName}:${functionName}:update">修改</a>';
            	    }
                	
                	if(auth[page] && auth[page]['${moduleName}:${functionName}:delete']){
                		delBtn = '<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del" data-auth="${moduleName}:${functionName}:delete">删除</a>';
            	    }
                	if((upBtn+delBtn) == ''){
                		return '无权限';
                	}else{
                		return upBtn+delBtn;
                	}
                }, title: '操作'}
            ]],
            done: function(res, curr, count){
            	var data = res.data;
            	$('.layui-table-body tr').each(function(i){
	            	var index = $(this).attr('data-index');
	            	$(this).click(function(){
	            		var fals = $(this).hasClass('layui-table-click');
	                	if(!fals){
	                		$(this).addClass('layui-table-click');
	                	}else{
	                		$(this).removeClass('layui-table-click');
	                	}
	            	});

            	});
            }
        });

        // 添加按钮点击事件
        $('#${functionName}-btn-add').click(function () {
            showEditModel();
        });

        // 工具条点击事件
        table.on('tool(${functionName}-table)', function (obj) {
            var data = obj.data;
            if (obj.event === 'edit') {
                showEditModel(data);
            } else if (obj.event == 'del') { //删除
                doDelete(obj);
            }
        });
        
     // 修改
        form.on('switch(${functionName}-tpl-state)', function (obj) {
            layer.load(2);
            admin.req('/${moduleName}/${functionName}/setStatus', {
                id: obj.elem.value,
                status: obj.elem.checked ? 0 : 1
            }, function (data) {
                layer.closeAll('loading');
                if (data.code == 0) {
                    layer.msg(data.msg, {icon: 1});
                } else {
                    layer.msg(data.msg, {icon: 2});
                    $(obj.elem).prop('checked', !obj.elem.checked);
                    form.render('checkbox');
                }
            }, 'post');
        });
        
        
        // 搜索按钮点击事件
        $('#${functionName}-btn-search').click(function () {
            var ${functionName}Name = $('#${functionName}Name').val();
            var ${functionName}Type = $('#${functionName}Type').val();
            var status = $('#status option:selected').val();
            table.reload('${functionName}-table', {where: {name: ${functionName}Name, type: ${functionName}Type, status:status}});
        });

        // 显示编辑弹窗
        var showEditModel = function (data) {
            var title = data ? '编辑' : '新增';
            if(data){
            	admin.req('/${moduleName}/${functionName}/get', {
                    id: data.id
                }, function (data) {
                    layer.closeAll('loading');
                    if (data.code == 0) {
                        admin.putTempData('t_${functionName}', data.data);
                        admin.popupCenter({
                            title: title,
                            path: 'pages/${moduleName}/${functionName}_form.html',
                            finish: function () {
                                table.reload('${functionName}-table', {});
                            }
                        });
                    } else {
                        layer.msg(data.msg, {icon: 2});
                    }
                }, 'get');
            }else{
            	admin.putTempData('t_${functionName}', {});
            	admin.popupCenter({
                    title: title,
                    path: 'pages/${moduleName}/${functionName}_form.html',
                    finish: function () {
                        table.reload('${functionName}-table', {});
                    }
                });
            }
            
            
            
        };

        // 删除
        var doDelete = function (obj) {
            layer.confirm('确定要删除吗？', function (i) {
                layer.close(i);
                layer.load(2);
                admin.req('/${moduleName}/${functionName}/delete?id=' + obj.data.id, {}, function (data) {
                    layer.closeAll('loading');
                    if (data.code == 0) {
                        layer.msg(data.msg, {icon: 1});
                        obj.del();
                    } else {
                        layer.msg(data.msg, {icon: 2});
                    }
                }, 'POST');
            });
        };

    });

	var auth = JSON.parse(sessionStorage.getItem("auth"));
	var page = location.hash.substring(2);
	$(".${functionName}_auth").each(function(index, item) {
	    var buttonAuth = $(item).attr("data-auth");
	    if( !auth[page] || auth[page][buttonAuth] != 1 ){
	        $(item).remove();
	    }
	});

</script>