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
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

    <script type="text/javascript">

        $(function () {

            //引入日期控件
            $(".time").datetimepicker({
                minView: "month",
                language:  'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "top-left"
            });


            //为全选的复选框绑定事件，触发全选操作
            $("#checkedAll").click(function (){

                $("input[name=xz]").prop("checked",this.checked)

            })
            //子选择框，如果全部选中，那么最上面的复选框，也自动勾选
            $("#clueBody").on("click",$("input[name=xz]"),function (){

                $("#checkedAll").prop("checked",$("input[name=xz]").length == $("input[name=xz]:checked").length);

            })

            //为创建按钮绑定事件，打开添加操作的模态窗口
            $("#addBtn").click(function (){

                //alert("123");

                $.ajax({
                    url:"workbench/clue/getUserList.do",
                    data : {

                    },
                    type:"get",
                    dataType:"json",
                    success : function (data){

                        //返回用户信息列表[{用户1},{用户2},{用户3}...]

                        var html = "<option></option>";
                        $.each(data,function (i,n){

                            html += "<option value='"+n.id+"'>"+n.name+"</option>";

                        })

                        $("#create-clueOwner").html(html);

                        //将系统当前用户设置为所有者的默认选项
                        var id = "${user.id}";

                        $("#create-clueOwner").val(id);

                        //处理完所有者下拉框的数据后，打开模态窗口
                        $("#createClueModal").modal("show");

                    }
                })

            })


            //为保存按钮绑定事件，执行线索的添加操作
            $("#saveBtn").click(function (){

                $.ajax({
                    url:"workbench/clue/save.do",
                    data : {

                        "fullname" : $.trim($("#create-fullname").val()),
                        "appellation" : $.trim($("#create-appellation").val()),
                        "owner" : $.trim($("#create-clueOwner").val()),
                        "company" : $.trim($("#create-company").val()),
                        "job" : $.trim($("#create-job").val()),
                        "email" : $.trim($("#create-email").val()),
                        "phone" : $.trim($("#create-phone").val()),
                        "website" : $.trim($("#create-website").val()),
                        "mphone" : $.trim($("#create-mphone").val()),
                        "state" : $.trim($("#create-state").val()),
                        "source" : $.trim($("#create-source").val()),
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
                            //刷新列表
                            pageList(1,2);

                            //清空模态窗口中的数据
                            //$("#clueAddForm")[0].reset();

                            //关闭模态窗口
                            $("#createClueModal").modal("hide");

                        }else{

                            //添加失败
                            alert("线索添加失败");

                        }

                    }
                })

            })


            //页面加载完毕后，执行分页操作
            pageList(1,2);

            //为查询按钮绑定事件，触发pageList方法
            $("#searchBtn").click(function (){

                //点击查询按钮的时候，将搜索框中的信息保存起来，放到隐藏域中
                $("#hidden-fullname").val($.trim($("#search-fullname").val()));
                $("#hidden-owner").val($.trim($("#search-owner").val()));
                $("#hidden-company").val($.trim($("#search-company").val()));
                $("#hidden-phone").val($.trim($("#search-phone").val()));
                $("#hidden-mphone").val($.trim($("#search-mphone").val()));
                $("#hidden-state").val($.trim($("#search-cluState").val()));
                $("#hidden-source").val($.trim($("#search-source").val()));

                pageList(1,2);

            })


            //为修改按钮绑定事件，打开修改操作的模态窗口
            $("#editBtn").click(function (){

                //引入日期控件
                $(".time").datetimepicker({
                    minView: "month",
                    language:  'zh-CN',
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "bottom-left"
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
                        url:"workbench/clue/getUserListAndClue.do",
                        data : {
                            "id" : id
                        },
                        type:"get",
                        dataType:"json",
                        success : function (data){

                            /*
                            *   data
                            *       用户列表
                            *       线索对象
                            *
                            *       {“uList”:[{"user1":user1},{"user2":user2},{"user3":user3}....],"c":Clue}
                            * */
                            //处理所有者的下拉框
                            var html = "<option></option>";
                            $.each(data.uList,function (i,n){
                                html += "<option value='"+n.id+"'>"+n.name+"</option>";
                            })
                            $("#edit-clueOwner").html(html);

                            //处理单条clue记录
                            $("#edit-id").val(data.c.id);
                            $("#edit-clueOwner").val(data.c.owner);
                            $("#edit-company").val(data.c.company);
                            $("#edit-appellation").val(data.c.appellation);
                            $("#edit-fullname").val(data.c.fullname);
                            $("#edit-job").val(data.c.job);
                            $("#edit-email").val(data.c.email);
                            $("#edit-phone").val(data.c.phone);
                            $("#edit-website").val(data.c.website);
                            $("#edit-mphone").val(data.c.mphone);
                            $("#edit-state").val(data.c.state);
                            $("#edit-source").val(data.c.source);
                            $("#edit-description").val(data.c.description);
                            $("#edit-contactSummary").val(data.c.contactSummary);
                            $("#edit-nextContactTime").val(data.c.nextContactTime);
                            $("#edit-address").val(data.c.address);

                            //所有下拉框处理完毕后，展现模态窗口
                            $("#editClueModal").modal("show");

                        }
                    })

                }

            })


            //为修改操做模态窗口中的更新按钮绑定事件，点击执行更新市操作
            $("#edit-updateBtn").click(function (){

                //alert("123");

                $.ajax({
                    url:"workbench/clue/updateClue.do",
                    data : {
                        "id" : $.trim($("#edit-id").val()),
                        "fullname" : $.trim($("#edit-fullname").val()),
                        "appellation" : $.trim($("#edit-appellation").val()),
                        "owner" : $.trim($("#edit-clueOwner").val()),
                        "company" : $.trim($("#edit-company").val()),
                        "job" : $.trim($("#edit-job").val()),
                        "email" : $.trim($("#edit-email").val()),
                        "phone" : $.trim($("#edit-phone").val()),
                        "website" : $.trim($("#edit-website").val()),
                        "mphone" : $.trim($("#edit-mphone").val()),
                        "state" : $.trim($("#edit-state").val()),
                        "source" : $.trim($("#edit-source").val()),
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
                            pageList($("#cluePage").bs_pagination('getOption', 'currentPage')
                                ,$("#cluePage").bs_pagination('getOption', 'rowsPerPage'));

                            //清空模态窗口中的数据
                            //$("#editActivityForm")[0].reset();

                            //关闭添加操作的模态窗口
                            $("#editClueModal").modal("hide");

                        }else{

                            alert("修改市场活动失败");

                        }

                    }
                })

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
                            url:"workbench/clue/delete.do",
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
                                    pageList(1,$("#cluePage").bs_pagination('getOption', 'rowsPerPage'));


                                }else {

                                    //删除失败
                                    alert("删除市场活动失败");

                                }
                            }
                        })
                    }



                }

            })

        });

        //分页查询
        function pageList(pageNo,pageSize){

            //alert("展现市场活动分页");

            $("#search-fullname").val($.trim($("#hidden-fullname").val()));
            $("#search-owner").val($.trim($("#hidden-owner").val()));
            $("#search-company").val($.trim($("#hidden-company").val()));
            $("#search-phone").val($.trim($("#hidden-phone").val()));
            $("#search-mphone").val($.trim($("#hidden-mphone").val()));
            $("#search-state").val($.trim($("#hidden-cluState").val()));
            $("#search-source").val($.trim($("#hidden-source").val()));

            //使用Ajax请求完成分页操作
            $.ajax({
                url:"workbench/clue/pageList.do",
                data : {

                    //需要为后台提供的参数
                    "pageNo" : pageNo,
                    "pageSize" : pageSize,
                    //条件查询
                    "fullname" : $.trim($("#search-fullname").val()),
                    "company" : $.trim($("#search-company").val()),
                    "phone" : $.trim($("#search-phone").val()),
                    "source" : $.trim($("#search-source").val()),
                    "owner" : $.trim($("#search-owner").val()),
                    "mphone" : $.trim($("#search-mphone").val()),
                    "state" : $.trim($("#search-cluState").val())

                },
                type:"get",
                dataType:"json",
                success : function (data){

                    /*
                        data：
                            我们需要的市场活动信息列表
                                [{线索1},{线索2},{线索3}...]
                            一会分页插件需要的：查询出来的总记录条数
                                {"total":100}
                            拼接起来：
                                {"total":100,"dataList":[{线索1},{线索2},{线索3}...]}
                     */
                    var html = "";

                    //每一个n就是一个线索对象
                    $.each(data.dataList,function (i,n){

                        html += '<tr>';
                        html += '<td><input type="checkbox" name="xz" value="'+n.id+'" /></td>';
                        html += '<td><a style="text-decoration: none; cursor: pointer;"';
                        html += 'onclick="window.location.href=\'workbench/clue/detail.do?id='+n.id+'\';">'+n.fullname+n.appellation+'</a></td>';
                        html += '<td>'+n.company+'</td>';
                        html += '<td>'+n.phone+'</td>';
                        html += '<td>'+n.mphone+'</td>';
                        html += '<td>'+n.source+'</td>';
                        html += '<td>'+n.owner+'</td>';/*这里注意数据库中存储的是用户id*/
                        html += '<td>'+n.state+'</td>';
                        html += '</tr>';

                    })

                    $("#clueBody").html(html);

                    //计算总页数
                    var totalPages = data.total%pageSize==0 ? data.total/pageSize : parseInt(data.total/pageSize)+1;

                    //数据处理完毕后，结合分页查询，对前端展示信息
                    $("#cluePage").bs_pagination({
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


    <%--使用隐藏域--%>
    <input type="hidden" id="hidden-fullname" />
    <input type="hidden" id="hidden-owner" />
    <input type="hidden" id="hidden-company" />
    <input type="hidden" id="hidden-phone" />
    <input type="hidden" id="hidden-mphone" />
    <input type="hidden" id="hidden-state" />
    <input type="hidden" id="hidden-source" />

<!-- 创建线索的模态窗口 -->
<div class="modal fade" id="createClueModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">创建线索</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="clueAddForm" >

                    <div class="form-group">
                        <label for="create-clueOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-clueOwner">
                                <%--<option>zhangsan</option>
                                <option>lisi</option>
                                <option>wangwu</option>--%>
                            </select>
                        </div>
                        <label for="create-company" class="col-sm-2 control-label">公司<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-company">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-appellation" class="col-sm-2 control-label">称呼</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-appellation">
                                <option></option>
                                <c:forEach items="${appellation}" var="a">
                                    <option value="${a.value}" >${a.text}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-fullname" class="col-sm-2 control-label">姓名<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-fullname">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-job" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-job">
                        </div>
                        <label for="create-email" class="col-sm-2 control-label">邮箱</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-email">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-phone" class="col-sm-2 control-label">公司座机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-phone">
                        </div>
                        <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-website">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-mphone" class="col-sm-2 control-label">手机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-mphone">
                        </div>
                        <label for="create-state" class="col-sm-2 control-label">线索状态</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-state">
                                <option></option>
                                <c:forEach items="${clueState}" var="clueState">
                                    <option value="${clueState.value}" >${clueState.text}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-source" class="col-sm-2 control-label">线索来源</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-source">
                                <option></option>
                                <c:forEach items="${source}" var="source">
                                    <option value="${source.value}" >${source.text}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">线索描述</label>
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
                                <input type="text" class="form-control time " id="create-nextContactTime">
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
                <button type="button" class="btn btn-primary" id="saveBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改线索的模态窗口 -->
<div class="modal fade" id="editClueModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">修改线索</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">

                    <%--隐藏域保存该条记录的id--%>
                    <input type="hidden" id="edit-id" />

                    <div class="form-group">
                        <label for="edit-clueOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-clueOwner">
                                <%--<option>zhangsan</option>
                                <option>lisi</option>
                                <option>wangwu</option>--%>
                            </select>
                        </div>
                        <label for="edit-company" class="col-sm-2 control-label">公司<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-company">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-appellation" class="col-sm-2 control-label">称呼</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-appellation">
                                <option></option>
                                <c:forEach items="${appellation}" var="a">
                                    <option value="${a.value}" >${a.text}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-fullname" class="col-sm-2 control-label">姓名<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-fullname">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-job" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-job">
                        </div>
                        <label for="edit-email" class="col-sm-2 control-label">邮箱</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-email">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-phone">
                        </div>
                        <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-website">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-mphone" class="col-sm-2 control-label">手机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-mphone">
                        </div>
                        <label for="edit-state" class="col-sm-2 control-label">线索状态</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-state">
                                <option></option>
                                <c:forEach items="${clueState}" var="clueState">
                                    <option value="${clueState.value}" >${clueState.text}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-source" class="col-sm-2 control-label">线索来源</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-source">
                                <option></option>
                                <c:forEach items="${source}" var="source">
                                    <option value="${source.value}" >${source.text}</option>
                                </c:forEach>
                            </select>
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
                                <input type="text" class="form-control time" id="edit-nextContactTime">
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
            <h3>线索列表</h3>
        </div>
    </div>
</div>

<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="search-fullname" >
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">公司</div>
                        <input class="form-control" type="text" id="search-company" >
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">公司座机</div>
                        <input class="form-control" type="text" id="search-phone" >
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">线索来源</div>
                        <select class="form-control" id="search-source">
                            <option></option>
                            <c:forEach items="${source}" var="source">
                                <option value="${source.value}" >${source.text}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <br>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="search-owner" >
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">手机</div>
                        <input class="form-control" type="text" id="search-mphone" >
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">线索状态</div>
                        <select class="form-control" id="search-cluState" >
                            <option></option>
                            <c:forEach items="${clueState}" var="clueState">
                                <option value="${clueState.value}" >${clueState.text}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="searchBtn" >查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
            <div class="btn-group" style="position: relative; top: 18%;">

                <button type="button" class="btn btn-primary" id="addBtn" >
                    <span class="glyphicon glyphicon-plus"></span> 创建
                </button>

                <button type="button" class="btn btn-default" id="editBtn" >
                    <span class="glyphicon glyphicon-pencil"></span> 修改
                </button>

                <button type="button" class="btn btn-danger" id="deleteBtn" >
                    <span class="glyphicon glyphicon-minus"></span> 删除
                </button>

            </div>


        </div>
        <div style="position: relative;top: 50px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="checkedAll" /></td>
                    <td>名称</td>
                    <td>公司</td>
                    <td>公司座机</td>
                    <td>手机</td>
                    <td>线索来源</td>
                    <td>所有者</td>
                    <td>线索状态</td>
                </tr>
                </thead>
                <tbody id="clueBody" >
                <%--<tr>
                    <td><input type="checkbox"/></td>
                    <td><a style="text-decoration: none; cursor: pointer;"
                           onclick="window.location.href='workbench/clue/detail.jsp';">李四先生</a></td>
                    <td>动力节点</td>
                    <td>010-84846003</td>
                    <td>12345678901</td>
                    <td>广告</td>
                    <td>zhangsan</td>
                    <td>已联系</td>
                </tr>
                <tr class="active">
                    <td><input type="checkbox"/></td>
                    <td><a style="text-decoration: none; cursor: pointer;"
                           onclick="window.location.href='workbench/clue/detail.jsp';">李四先生</a></td>
                    <td>动力节点</td>
                    <td>010-84846003</td>
                    <td>12345678901</td>
                    <td>广告</td>
                    <td>zhangsan</td>
                    <td>已联系</td>
                </tr>--%>
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 60px;">
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
                <div id="cluePage"></div>
        </div>

    </div>

</div>
</body>
</html>