<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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


    <script type="text/javascript">

        $(function () {

            //引入日期控件
            $(".time").datetimepicker({
                minView: "month",
                language:  'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });

            //为创建按钮绑定事件，目的是打开添加操作的模态窗口
            $("#addBtn").click(function (){

                //引入日期控件
                $(".time").datetimepicker({
                    minView: "month",
                    language:  'zh-CN',
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "bottom-left"
                });


                /*
                * 操作模态窗口的方式：
                *       需要操作的模态窗口的jquery对象，调用modal方法，并为该方法传递参数
                *           --show:打开模态窗口
                *           --hide:关闭模态窗口
                * */
                //$("#createActivityModal").modal("show");

                //去后台获取数据库中user表的用户信息，将其作为模态窗口中 所有者 框的下拉列表的值
                $.ajax({
                    url:"workbench/activity/getUserList.do",
                    data : {

                    },
                    type:"get",
                    dataType:"json",
                    success : function (data){

                        /*
                        * data
                        *   [{用户1},{用户2},{用户3}...]
                        * */
                        var html = "<option></option>";
                        //遍历出来的每一个n就是一个User对象
                        $.each(data,function (i,n){

                            html += "<option value='"+n.id+"'>"+n.name+"</option>"

                        })
                        //将拼接好的下拉列表选项插入到下拉列表标签中
                        $("#create-marketActivityOwner").html(html);

                        //将当前登录的用户，设置为下拉框的默认选项
                        var id = "${sessionScope.user.id}";
                        $("#create-marketActivityOwner").val(id);

                        //所有下拉框处理完毕后，展现模态窗口
                        $("#createActivityModal").modal("show");

                    }
                })

            })

            //为保存按钮绑定事件，执行添加操作
            $("#saveBtn").click(function (){

                $.ajax({
                    url:"workbench/activity/save.do",
                    data : {

                        "owner" : $.trim($("#create-marketActivityOwner").val()),
                        "name" : $.trim($("#create-marketActivityName").val()),
                        "startDate" : $.trim($("#create-startDate").val()),
                        "endDate" : $.trim($("#create-endDate").val()),
                        "cost" : $.trim($("#create-cost").val()),
                        "description" : $.trim($("#create-description").val())

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
                                    ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


                            //清空模态窗口中的数据
                            $("#activityAddForm")[0].reset();

                            //关闭添加操作的模态窗口
                            $("#createActivityModal").modal("hide");

                        }else{

                            alert("添加市场活动失败");

                        }

                    }
                })

            })

            //页面加载完毕后触发一个方法
            pageList(1,2);

            //为查询按钮绑定事件，触发pageList方法
            $("#searchBtn").click(function (){

                /*
                *   点击查询按钮的时候，我们应该将搜索框中的内容保存起来，保存到隐藏域中
                * */
                $("#hidden-name").val($.trim($("#search-name").val()));
                $("#hidden-owner").val($.trim($("#search-owner").val()));
                $("#hidden-startDate").val($.trim($("#search-startDate").val()));
                $("#hidden-endDate").val($.trim($("#search-endDate").val()));


                pageList(1,2);

            })

            //为全选的复选框绑定事件，触发全选操作
            $("#checkedAll").click(function (){

                $("input[name=xz]").prop("checked",this.checked)

            })

            //子选择框，如果全部选中，那么最上面的复选框，也自动勾选
            $("#activityBody").on("click",$("input[name=xz]"),function (){

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
                            url:"workbench/activity/delete.do",
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
                                    pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


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
                        url:"workbench/activity/getUserListAndActivity.do",
                        data : {

                            "id" : id

                        },
                        type:"get",
                        dataType:"json",
                        success : function (data){

                            /*
                            *   data
                            *       用户列表
                            *       市场活动对象
                            *
                            *       {“uList”:[{"user1":user1},{"user2":user2},{"user3":user3}....],"a":Activity}
                            * */
                            //处理所有者的下拉框
                            var html = "<option></option>";

                            $.each(data.uList,function (i,n){

                                html += "<option value='"+n.id+"'>"+n.name+"</option>";

                            })

                            $("#edit-marketActivityOwner").html(html);

                            //处理单条activity记录
                            $("#edit-id").val(data.a.id);
                            $("#edit-marketActivityOwner").val(data.a.owner);
                            $("#edit-marketActivityName").val(data.a.name);
                            $("#edit-startDate").val(data.a.startDate);
                            $("#edit-endDate").val(data.a.endDate);
                            $("#edit-cost").val(data.a.cost);
                            $("#edit-description").val(data.a.description);

                            //所有下拉框处理完毕后，展现模态窗口
                            $("#editActivityModal").modal("show");

                        }
                    })


                }

            })

            //为修改操做模态窗口中的更新按钮绑定事件，点击执行更新市操作
            $("#edit-updateBtn").click(function (){

                //alert("123");

                $.ajax({
                    url:"workbench/activity/updateActivity.do",
                    data : {
                        "id" : $.trim($("#edit-id").val()),
                        "owner" : $.trim($("#edit-marketActivityOwner").val()),
                        "name" : $.trim($("#edit-marketActivityName").val()),
                        "startDate" : $.trim($("#edit-startDate").val()),
                        "endDate" : $.trim($("#edit-endDate").val()),
                        "cost" : $.trim($("#edit-cost").val()),
                        "description" : $.trim($("#edit-description").val())

                    },
                    type:"get",
                    dataType:"json",
                    success : function (data){

                        if (data.success){

                            //修改成功
                            //刷新市场活动信息列表（局部刷新）
                            //pageList(1,2);
                            //修改操作后应该维持在当前页，维持每页显示的记录数
                            pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                                    ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                            //清空模态窗口中的数据
                            //$("#editActivityForm")[0].reset();

                            //关闭添加操作的模态窗口
                            $("#editActivityModal").modal("hide");

                        }else{

                            alert("修改市场活动失败");

                        }

                    }
                })

            })


        });

        /*
            对于所有的关系行数据库，做前端分页相关操作的基础组件
            就是pageNo,pageSize
            pageNo  页码
            pageSize  每页显示的记录数
         */
        function pageList(pageNo,pageSize){

            //清空全选的复选框(将全选的复选框上面的√清除)
            $("#checkedAll").prop("checked",false);

            //查询前，将隐藏域中的信息放到搜索框中
            $("#search-name").val($.trim($("#hidden-name").val()));
            $("#search-owner").val($.trim($("#hidden-owner").val()));
            $("#search-startDate").val($.trim($("#hidden-startDate").val()));
            $("#search-endDate").val($.trim($("#hidden-endDate").val()));

            //alert("展现市场活动分页");

            //使用Ajax请求完成分页操作
            $.ajax({
                url:"workbench/activity/pageList.do",
                data : {

                    //需要为后台提供的参数
                    "pageNo" : pageNo,
                    "pageSize" : pageSize,
                    //条件查询
                    "name" : $.trim($("#search-name").val()),
                    "owner" : $.trim($("#search-owner").val()),
                    "startDate" : $.trim($("#search-startDate").val()),
                    "endDate" : $.trim($("#search-endDate").val())

                },
                type:"get",
                dataType:"json",
                success : function (data){

                    /*
                        data：
                            我们需要的市场活动信息列表
                                [{市场活动1},{市场活动2},{市场活动3},{市场活动4}...]
                            一会分页插件需要的：查询出来的总记录条数
                                {"total":100}
                            拼接起来：
                                {"total":100,"dataList":[{市场活动1},{市场活动2},{市场活动3},{市场活动4}...]}
                     */
                    var html = "";

                    //每一个n就是一个市场活动对象
                    $.each(data.dataList,function (i,n){

                        html += '<tr class="active">';
                        html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
                        html += '<td><a style="text-decoration: none; cursor: pointer;"';
                        html += 'onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
                        html += '<td>'+n.owner+'</td>';
                        html += '<td>'+n.startDate+'</td>';
                        html += '<td>'+n.endDate+'</td>';
                        html += '</tr>';

                    })

                    $("#activityBody").html(html);

                    //计算总页数
                    var totalPages = data.total%pageSize==0 ? data.total/pageSize : parseInt(data.total/pageSize)+1;

                    //数据处理完毕后，结合分页查询，对前端展示信息
                    $("#activityPage").bs_pagination({
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
    <input type="hidden" id="hidden-name" />
    <input type="hidden" id="hidden-owner" />
    <input type="hidden" id="hidden-startDate" />
    <input type="hidden" id="hidden-endDate" />

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form id="activityAddForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">

                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-startDate">
                        </div>
                        <label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-endDate">
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
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

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form id="editActivityForm" class="form-horizontal" role="form">

                    <%--隐藏域保存该条记录的id--%>
                    <input type="hidden" id="edit-id" />

                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-marketActivityOwner">
                                <%--<option>zhangsan</option>
                                <option>lisi</option>
                                <option>wangwu</option>--%>
                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-marketActivityName" >
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-startDate" >
                        </div>
                        <label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-endDate" >
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" >
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <%--
                                关于文本域标签textarea
                                    （1）一定要前后紧挨着
                                    （2）textarea虽然是以标签对的形式来呈现的，但是他是也是属于表单元素的范畴
                                         我们所有的额对于textarea标签的取值和赋值操作，应该统一使用val()方法
                            --%>
                            <textarea class="form-control" rows="3" id="edit-description"></textarea>
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
            <h3>市场活动列表</h3>
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
                        <input class="form-control" type="text" id="search-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="search-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control time" type="text" id="search-startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control time" type="text" id="search-endDate">
                    </div>
                </div>

                <button type="button" id="searchBtn" class="btn btn-default">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">

                <%--
                    data-toggle="modal"
                        表示触发该按钮，将要打开一个模态窗口
                    data-target="#createActivityModal
                        表示打开哪个模态窗口，通过#id的方式找到该窗口

                    以属性和属性值的方式卸载button中，用来打开模态窗口，
                        缺点：
                            无法对按钮进行扩充
                        更改方案：
                            自己写js代码来操作
                    data-toggle="modal" data-target="#createActivityModal"
                --%>
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
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                    <tr style="color: #B3B3B3;">
                        <td><input type="checkbox" id="checkedAll" /></td>
                        <td>名称</td>
                        <td>所有者</td>
                        <td>开始日期</td>
                        <td>结束日期</td>
                    </tr>
                </thead>
                <tbody id="activityBody">
                    <%--<tr class="active">
                        <td><input type="checkbox"/></td>
                        <td><a style="text-decoration: none; cursor: pointer;"
                               onclick="window.location.href='workbench/activity/detail.html';">发传单</a></td>
                        <td>zhangsan</td>
                        <td>2020-10-10</td>
                        <td>2020-10-20</td>
                    </tr>
                    <tr class="active">
                        <td><input type="checkbox"/></td>
                        <td><a style="text-decoration: none; cursor: pointer;"
                               onclick="window.location.href='workbench/activity/detail.html';">发传单</a></td>
                        <td>zhangsan</td>
                        <td>2020-10-10</td>
                        <td>2020-10-20</td>
                    </tr>--%>
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">
            <%--注释掉之前页面中的分页--%>
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
            <div id="activityPage"></div>

        </div>

    </div>

</div>
</body>
</html>