<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

    <%--自动补全--%>
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

    <script type="text/javascript">

        $(function(){

            //引入自动补全控件
            $("#create-customerName").typeahead({
                source: function (query, process) {
                    $.get(
                        "workbench/contacts/getCustomerName.do",
                        { "name" : query },
                        function (data) {
                            //alert(data);
                            process(data);
                        },
                        "json"
                    );
                },
                delay: 1500
            });

            //定制字段
            $("#definedColumns > li").click(function(e) {
                //防止下拉菜单消失
                e.stopPropagation();
            });

            //为创建按钮绑定事件，目的是打开添加操作的模态窗口
            $("#addBtn").click(function (){

                //引入自动补全控件
                $("#create-customerName").typeahead({
                    source: function (query, process) {
                        $.get(
                            "workbench/contacts/getCustomerName.do",
                            { "name" : query },
                            function (data) {
                                //alert(data);
                                process(data);
                            },
                            "json"
                        );
                    },
                    delay: 1500
                });

                //引入日期控件
                $(".time").datetimepicker({
                    minView: "month",
                    language:  'zh-CN',
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "bottom-left"
                });
                //引入日期控件
                $(".time2").datetimepicker({
                    minView: "month",
                    language:  'zh-CN',
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "top-left"
                });

                //去后台获取数据库中user表的用户信息，将其作为模态窗口中 所有者 框的下拉列表的值
                $.ajax({
                    url:"workbench/contacts/getUserList.do",
                    type:"get",
                    dataType:"json",
                    success : function (data){

                        var html = "<option></option>";
                        //遍历出来的每一个n就是一个User对象
                        $.each(data,function (i,n){

                            html += "<option value='"+n.id+"'>"+n.name+"</option>"

                        })
                        //将拼接好的下拉列表选项插入到下拉列表标签中
                        $("#create-contactsOwner").html(html);

                        //将当前登录的用户，设置为下拉框的默认选项
                        var id = "${sessionScope.user.id}";
                        $("#create-contactsOwner").val(id);

                        //所有下拉框处理完毕后，展现模态窗口
                        $("#createContactsModal").modal("show");

                    }
                })

            })

            //为保存按钮绑定事件，执行添加操作
            $("#saveBtn").click(function (){

                $.ajax({
                    url:"workbench/contacts/save.do",
                    data : {

                        "owner" : $.trim($("#create-contactsOwner").val()),
                        "source" : $.trim($("#create-contactsSource").val()),
                        "customerName" : $.trim($("#create-customerName").val()),
                        "fullname" : $.trim($("#create-fullname").val()),
                        "appellation" : $.trim($("#create-appellation").val()),
                        "email" : $.trim($("#create-email").val()),
                        "mphone" : $.trim($("#create-mphone").val()),
                        "job" : $.trim($("#create-job").val()),
                        "birth" : $.trim($("#create-birth").val()),
                        "description" : $.trim($("#create-description").val()),
                        "contactSummary" : $.trim($("#create-contactSummary").val()),
                        "nextContactTime" : $.trim($("#create-nextContactTime").val()),
                        "address" : $.trim($("#create-address").val())

                    },
                    type:"post",
                    dataType:"json",
                    success : function (data){

                        if (data.success){

                            //添加成功
                            //刷新市场活动信息列表（局部刷新）
                            //pageList(1,2);

                            /*
                            * $("#activityPage").bs_pagination('getOption', 'currentPage')
                            *       操作后停留在当前页
                            * $("#activityPage").bs_pagination('getOption', 'rowsPerPage')
                            *       操作后维持已经设置好的每页显示的记录数
                            * */

                            pageList(1
                                ,$("#contactsPage").bs_pagination('getOption', 'rowsPerPage'));

                            //清空模态窗口中的数据
                            //$("#contactsAddForm")[0].reset();

                            //关闭添加操作的模态窗口
                            $("#createContactsModal").modal("hide");

                        }else{

                            alert("添加市场活动失败");

                        }

                    }
                })

            })

            pageList(1,2);

            //为查询按钮绑定事件，触发pageList方法
            $("#searchBtn").click(function (){

                /*
                *   点击查询按钮的时候，我们应该将搜索框中的内容保存起来，保存到隐藏域中
                * */
                $("#hidden-fullname").val($.trim($("#search-fullname").val()));
                $("#hidden-owner").val($.trim($("#search-owner").val()));
                $("#hidden-customerName").val($.trim($("#search-customerName").val()));
                $("#hidden-source").val($.trim($("#search-customerSource").val()));
                $("#hidden-birth").val($.trim($("#search-birth").val()));


                pageList(1,2);

            })

            //为全选的复选框绑定事件，触发全选操作
            $("#checkedAll").click(function (){

                $("input[name=xz]").prop("checked",this.checked)

            })

            //子选择框，如果全部选中，那么最上面的复选框，也自动勾选
            $("#contactsBody").on("click",$("input[name=xz]"),function (){

                $("#checkedAll").prop("checked",$("input[name=xz]").length == $("input[name=xz]:checked").length);

            })

            //为删除按钮绑定事件，执行市场活动的删除操作
            $("#deleteBtn").click(function (){

                //找到复选框中所有选中的jQuery对象
                var $xz = $("input[name=xz]:checked");

                if ($xz.length == 0){

                    //用户没有选择需要删除的记录
                    alert("请选择需要删除的记录");

                }else {

                    //程序执行到此处，说明用户选择了记录，但是有可能是1条，也可能是多条
                    //alert("123");

                    if (confirm("确定要删除所选中的数据吗？删除后不可恢复！")){

                        //拼接参数
                        var param = "";

                        //将$xz中的每一个dom对象遍历出来，取其value值，就相当于取得了需要删除的记录id
                        for (var i=0; i<$xz.length; i++){

                            param += "id=" + $($xz[i]).val();
                            //如果不是最后一个元素，则需要追加&
                            if (i<$xz.length - 1){
                                param += "&";
                            }
                        }

                        //alert(param);
                        //使用ajax发送请求，使后台执行删除操作
                        $.ajax({
                            url:"workbench/contacts/delete.do",
                            data : param,
                            type:"post",
                            dataType:"json",
                            success : function (data){
                                /*
                                *
                                * data
                                *       {"success":true/false}
                                * */
                                if (data.success){
                                    //删除成功
                                    //对页面数据进行局部刷新
                                    //pageList(1,2);
                                    //删除成功后，回到第一页，维持每页展现的记录数
                                    pageList(1,$("#contactsPage").bs_pagination('getOption', 'rowsPerPage'));


                                }else {

                                    //删除失败
                                    alert("删除市场活动失败");

                                }
                            }
                        })
                    }



                }

            })

            //为修改按钮绑定事件，打开修改操作的模态窗口
            $("#editBtn").click(function (){

                //引入自动补全控件
                $("#edit-customerName").typeahead({
                    source: function (query, process) {
                        $.get(
                            "workbench/contacts/getCustomerName.do",
                            { "name" : query },
                            function (data) {
                                //alert(data);
                                process(data);
                            },
                            "json"
                        );
                    },
                    delay: 1500
                });

                //引入日期控件
                $(".time").datetimepicker({
                    minView: "month",
                    language:  'zh-CN',
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "bottom-left"
                });
                //引入日期控件
                $(".time2").datetimepicker({
                    minView: "month",
                    language:  'zh-CN',
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "top-left"
                });

                var $xz = $("input[name=xz]:checked");

                if ($xz.length == 0){

                    //程序执行到此处，说明用户没有选择需要修改的记录
                    alert("请先选择需要修改的市场活动");

                }else if ($xz.length > 1){

                    alert("只能选择一个市场活动进行修改，请重新选择");

                }else{

                    //程序执行到此处，说明用户只选择了一条需要修改的数据
                    var id = $xz.val();

                    //发出Ajax请求
                    $.ajax({
                        url:"workbench/contacts/getUserListAndContacts.do",
                        data : {

                            "id" : id

                        },
                        type:"get",
                        dataType:"json",
                        success : function (data){

                            /*
                            *   data
                            *       用户列表
                            *       联系人对象
                            *
                            *       {“uList”:[{"user1":user1},{"user2":user2},{"user3":user3}....],"c":Contacts}
                            * */
                            //处理所有者的下拉框
                            var html = "<option></option>";

                            $.each(data.uList,function (i,n){

                                html += "<option value='"+n.id+"'>"+n.name+"</option>";

                            })

                            $("#edit-owner").html(html);

                            //处理单条activity记录
                            $("#edit-id").val(data.c.id);
                            $("#edit-owner").val(data.c.owner);
                            $("#edit-source").val(data.c.source);
                            $("#edit-fullname").val(data.c.fullname);
                            $("#edit-appellation").val(data.c.appellation);
                            $("#edit-job").val(data.c.job);
                            $("#edit-mphone").val(data.c.mphone);
                            $("#edit-email").val(data.c.email);
                            $("#edit-birth").val(data.c.birth);
                            $("#edit-customerName").val(data.c.customerId);
                            $("#edit-description").val(data.c.description);
                            $("#edit-contactSummary").val(data.c.contactSummary);
                            $("#edit-nextContactTime").val(data.c.nextContactTime);
                            $("#edit-address").val(data.c.address);

                            //所有下拉框处理完毕后，展现模态窗口
                            $("#editContactsModal").modal("show");

                        }
                    })


                }

            })

            //为修改操做模态窗口中的更新按钮绑定事件，点击执行更新市操作
            $("#edit-updateBtn").click(function (){

                //alert("123");

                $.ajax({
                    url:"workbench/contacts/updateContacts.do",
                    data : {

                        "id" : $.trim($("#edit-id").val()),
                        "owner" : $.trim($("#edit-owner").val()),
                        "source" : $.trim($("#edit-source").val()),
                        "customerName" : $.trim($("#edit-customerName").val()),
                        "fullname" : $.trim($("#edit-fullname").val()),
                        "appellation" : $.trim($("#edit-appellation").val()),
                        "email" : $.trim($("#edit-email").val()),
                        "mphone" : $.trim($("#edit-mphone").val()),
                        "job" : $.trim($("#edit-job").val()),
                        "birth" : $.trim($("#edit-birth").val()),
                        "description" : $.trim($("#edit-description").val()),
                        "contactSummary" : $.trim($("#edit-contactSummary").val()),
                        "nextContactTime" : $.trim($("#edit-nextContactTime").val()),
                        "address" : $.trim($("#edit-address").val())

                    },
                    type:"post",
                    dataType:"json",
                    success : function (data){

                        if (data.success){

                            //修改成功
                            //刷新市场活动信息列表（局部刷新）
                            //pageList(1,2);
                            //修改操作后应该维持在当前页，维持每页显示的记录数
                            pageList($("#contactsPage").bs_pagination('getOption', 'currentPage')
                                ,$("#contactsPage").bs_pagination('getOption', 'rowsPerPage'));

                            //关闭添加操作的模态窗口
                            $("#editContactsModal").modal("hide");

                        }else{

                            alert("修改市场活动失败");

                        }

                    }
                })

            })

        });

        function pageList(pageNo,pageSize){

            //清空全选的复选框(将全选的复选框上面的√清除)
            $("#checkedAll").prop("checked",false);

            //查询前，将隐藏域中的信息放到搜索框中
            $("#search-fullname").val($.trim($("#hidden-fullname").val()));
            $("#search-owner").val($.trim($("#hidden-owner").val()));
            $("#search-customerName").val($.trim($("#hidden-customerName").val()));
            $("#search-customerSource").val($.trim($("#hidden-source").val()));
            $("#search-birth").val($.trim($("#hidden-birth").val()));

            //使用Ajax请求完成分页操作
            $.ajax({
                url:"workbench/contacts/pageList.do",
                data : {

                    //需要为后台提供的参数
                    "pageNo" : pageNo,
                    "pageSize" : pageSize,
                    //条件查询
                    "owner" : $.trim($("#search-owner").val()),
                    "fullname" : $.trim($("#search-fullname").val()),
                    "customerName" : $.trim($("#search-customerName").val()),
                    "source" : $.trim($("#search-customerSource").val()),
                    "birth" : $.trim($("#search-birth").val())

                },
                type:"get",
                dataType:"json",
                success : function (data){

                    /*
                        data：
                            我们需要的联系人信息列表
                                [{联系人1},{联系人2},{联系人3}...]
                            一会分页插件需要的：查询出来的总记录条数
                                {"total":100}
                            拼接起来：
                                {"total":100,"dataList":[{联系人1},{联系人2},{联系人3}...]}
                     */
                    var html = "";

                    //每一个n就是一个市场活动对象
                    $.each(data.dataList,function (i,n){

                        html += '<tr class="active">';
                        html += '<td><input type="checkbox" name="xz" value="'+n.id+'" /></td>';
                        html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/contacts/detail.do?id='+n.id+'\';">'+n.fullname+'</a></td>';
                        html += '<td>'+n.customerId+'</td>';
                        html += '<td>'+n.owner+'</td>';
                        html += '<td>'+n.source+'</td>';
                        html += '<td>'+n.birth+'</td>';
                        html += '</tr>';

                    })

                    $("#contactsBody").html(html);

                    //计算总页数
                    var totalPages = data.total%pageSize==0 ? data.total/pageSize : parseInt(data.total/pageSize)+1;

                    //数据处理完毕后，结合分页查询，对前端展示信息
                    $("#contactsPage").bs_pagination({
                        currentPage: pageNo, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数
                        totalRows: data.total, // 总记录条数

                        visiblePageLinks: 3, // 显示几个卡片

                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,

                        onChangePage : function(event, data){
                            pageList(data.currentPage , data.rowsPerPage);
                        }
                    });

                }
            })

        }

    </script>
</head>
<body>

        <%--使用隐藏域，搜索条件，防止跳转的小bug--%>
        <input type="hidden" id="hidden-owner" />
        <input type="hidden" id="hidden-fullname" />
        <input type="hidden" id="hidden-customerName" />
        <input type="hidden" id="hidden-source" />
        <input type="hidden" id="hidden-birth" />

<!-- 创建联系人的模态窗口 -->
<div class="modal fade" id="createContactsModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" onclick="$('#createContactsModal').modal('hide');">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabelx">创建联系人</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="contactsAddForm">

                    <div class="form-group">
                        <label for="create-contactsOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-contactsOwner">
                                <%--<option>zhangsan</option>
                                <option>lisi</option>
                                <option>wangwu</option>--%>
                            </select>
                        </div>
                        <label for="create-contactsSource" class="col-sm-2 control-label">来源</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-contactsSource">
                                <option></option>
                                <c:forEach items="${source}" var="source">
                                    <option value="${source.value}" >${source.text}</option>
                                </c:forEach>
                                <%--<option>广告</option>
                                <option>推销电话</option>
                                <option>员工介绍</option>
                                <option>外部介绍</option>
                                <option>在线商场</option>
                                <option>合作伙伴</option>
                                <option>公开媒介</option>
                                <option>销售邮件</option>
                                <option>合作伙伴研讨会</option>
                                <option>内部研讨会</option>
                                <option>交易会</option>
                                <option>web下载</option>
                                <option>web调研</option>
                                <option>聊天</option>--%>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-fullname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-fullname">
                        </div>
                        <label for="create-appellation" class="col-sm-2 control-label">称呼</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-appellation">
                                <option></option>
                                <c:forEach items="${appellation}" var="appellation">
                                    <option value="${appellation.value}" >${appellation.text}</option>
                                </c:forEach>
                                <%--<option>先生</option>
                                <option>夫人</option>
                                <option>女士</option>
                                <option>博士</option>
                                <option>教授</option>--%>
                            </select>
                        </div>

                    </div>

                    <div class="form-group">
                        <label for="create-job" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-job">
                        </div>
                        <label for="create-mphone" class="col-sm-2 control-label">手机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-mphone">
                        </div>
                    </div>

                    <div class="form-group" style="position: relative;">
                        <label for="create-email" class="col-sm-2 control-label">邮箱</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-email">
                        </div>
                        <label for="create-birth" class="col-sm-2 control-label">生日</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-birth">
                        </div>
                    </div>

                    <div class="form-group" style="position: relative;">
                        <label for="create-customerName" class="col-sm-2 control-label">客户名称</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-customerName" placeholder="支持自动补全，输入客户不存在则新建">
                        </div>
                    </div>

                    <div class="form-group" style="position: relative;">
                        <label for="create-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                    <div style="position: relative;top: 15px;">
                        <div class="form-group">
                            <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control time2" id="create-nextContactTime">
                            </div>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                    <div style="position: relative;top: 20px;">
                        <div class="form-group">
                            <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="1" id="create-address"></textarea>
                            </div>
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveBtn" >保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改联系人的模态窗口 -->
<div class="modal fade" id="editContactsModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">修改联系人</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editContactsForm">

                    <%--隐藏域--%>
                    <input type="hidden" id="edit-id">

                    <div class="form-group">
                        <label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-owner">
                                <option selected>zhangsan</option>
                                <option>lisi</option>
                                <option>wangwu</option>
                            </select>
                        </div>
                        <label for="edit-source" class="col-sm-2 control-label">来源</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-source">
                                <option></option>
                                <c:forEach items="${source}" var="source">
                                    <option value="${source.value}" >${source.text}</option>
                                </c:forEach>
                                <%--<option selected>广告</option>
                                <option>推销电话</option>
                                <option>员工介绍</option>
                                <option>外部介绍</option>
                                <option>在线商场</option>
                                <option>合作伙伴</option>
                                <option>公开媒介</option>
                                <option>销售邮件</option>
                                <option>合作伙伴研讨会</option>
                                <option>内部研讨会</option>
                                <option>交易会</option>
                                <option>web下载</option>
                                <option>web调研</option>
                                <option>聊天</option>--%>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-fullname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-fullname">
                        </div>
                        <label for="edit-appellation" class="col-sm-2 control-label">称呼</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-appellation">
                                <option></option>
                                <c:forEach items="${appellation}" var="appellation">
                                    <option value="${appellation.value}" >${appellation.text}</option>
                                </c:forEach>
                                <%--<option selected>先生</option>
                                <option>夫人</option>
                                <option>女士</option>
                                <option>博士</option>
                                <option>教授</option>--%>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-job" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-job">
                        </div>
                        <label for="edit-mphone" class="col-sm-2 control-label">手机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-mphone">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-email" class="col-sm-2 control-label">邮箱</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-email">
                        </div>
                        <label for="edit-birth" class="col-sm-2 control-label">生日</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-birth">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-customerName" class="col-sm-2 control-label">客户名称</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-customerName" placeholder="支持自动补全，输入客户不存在则新建">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-description"></textarea>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                    <div style="position: relative;top: 15px;">
                        <div class="form-group">
                            <label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="edit-contactSummary"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control time2" id="edit-nextContactTime">
                            </div>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                    <div style="position: relative;top: 20px;">
                        <div class="form-group">
                            <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="1" id="edit-address"></textarea>
                            </div>
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="edit-updateBtn">更新</button>
            </div>
        </div>
    </div>
</div>



<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>联系人列表</h3>
        </div>
    </div>
</div>

<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;" id="searchContactsForm">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="search-owner">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">姓名</div>
                        <input class="form-control" type="text" id="search-fullname">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">客户名称</div>
                        <input class="form-control" type="text" id="search-customerName">
                    </div>
                </div>

                <br>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">来源</div>
                        <select class="form-control" id="search-customerSource">
                            <option></option>
                            <c:forEach items="${source}" var="source">
                                <option value="${source.value}" >${source.text}</option>
                            </c:forEach>
                            <%--<option>广告</option>
                            <option>推销电话</option>
                            <option>员工介绍</option>
                            <option>外部介绍</option>
                            <option>在线商场</option>
                            <option>合作伙伴</option>
                            <option>公开媒介</option>
                            <option>销售邮件</option>
                            <option>合作伙伴研讨会</option>
                            <option>内部研讨会</option>
                            <option>交易会</option>
                            <option>web下载</option>
                            <option>web调研</option>
                            <option>聊天</option>--%>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">生日</div>
                        <input class="form-control" type="text" id="search-birth">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="searchBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
            <div class="btn-group" style="position: relative; top: 18%;">

                <button type="button" class="btn btn-primary" id="addBtn" >
                    <span class="glyphicon glyphicon-plus"></span> 创建
                </button>

                <button type="button" class="btn btn-default" id="editBtn" >
                    <span class="glyphicon glyphicon-pencil"></span> 修改
                </button>

                <button type="button" class="btn btn-danger" id="deleteBtn">
                    <span class="glyphicon glyphicon-minus"></span> 删除
                </button>

            </div>


        </div>
        <div style="position: relative;top: 20px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="checkedAll" /></td>
                    <td>姓名</td>
                    <td>客户名称</td>
                    <td>所有者</td>
                    <td>来源</td>
                    <td>生日</td>
                </tr>
                </thead>
                <tbody id="contactsBody">
                <%--<tr>
                    <td><input type="checkbox" /></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/contacts/detail.html';">李四</a></td>
                    <td>动力节点</td>
                    <td>zhangsan</td>
                    <td>广告</td>
                    <td>2000-10-10</td>
                </tr>
                <tr class="active">
                    <td><input type="checkbox" /></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">李四</a></td>
                    <td>动力节点</td>
                    <td>zhangsan</td>
                    <td>广告</td>
                    <td>2000-10-10</td>
                </tr>--%>
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 10px;">
            <%--<div>
                <button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
            </div>
            <div class="btn-group" style="position: relative;top: -34px; left: 110px;">
                <button type="button" class="btn btn-default" style="cursor: default;">显示</button>
                <div class="btn-group">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                        10
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="#">20</a></li>
                        <li><a href="#">30</a></li>
                    </ul>
                </div>
                <button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
            </div>
            <div style="position: relative;top: -88px; left: 285px;">
                <nav>
                    <ul class="pagination">
                        <li class="disabled"><a href="#">首页</a></li>
                        <li class="disabled"><a href="#">上一页</a></li>
                        <li class="active"><a href="#">1</a></li>
                        <li><a href="#">2</a></li>
                        <li><a href="#">3</a></li>
                        <li><a href="#">4</a></li>
                        <li><a href="#">5</a></li>
                        <li><a href="#">下一页</a></li>
                        <li class="disabled"><a href="#">末页</a></li>
                    </ul>
                </nav>
            </div>--%>

            <div id="contactsPage"></div>

        </div>

    </div>

</div>
</body>
</html>