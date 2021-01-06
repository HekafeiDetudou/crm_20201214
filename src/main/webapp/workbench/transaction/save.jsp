<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <%--自动补全--%>
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

    <script type="text/javascript">

        $(function () {

            //引入自动补全控件
            $("#create-customerName").typeahead({
                source: function (query, process) {
                    $.get(
                        "workbench/transaction/getCustomerName.do",
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
            $(".time1").datetimepicker({
                minView: "month",
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });
            $(".time2").datetimepicker({
                minView: "month",
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "top-left"
            });


            //为 放大镜图标，绑定事件，打开搜索市场活动的模态窗口
            $("#openSearchModalBtn").click(function (){
                //alert("123");
                $("#findMarketActivity").modal("show");
            })
            //为搜索操作模态窗口中的搜索框绑定事件，执行搜索操作并展现市场活动列表
            $("#activityName").keydown(function (event){

                if (event.keyCode==13){

                    $.ajax({
                        url:"workbench/clue/getActivityListByNameJust.do",
                        data : {
                            "activityName" : $.trim($("#activityName").val())
                        },
                        type:"get",
                        dataType:"json",
                        success : function (data){

                            /*
                            * data
                            *       [{市场活动1},{市场活动2},{市场活动3}...]
                            * */
                            var html = "";

                            $.each(data,function (i,n){

                                html += '<tr>';
                                html += '<td><input type="radio" name="xz" value="'+n.id+'" /></td>';
                                html += '<td id="'+n.id+'" >'+n.name+'</td>';
                                html += '<td>'+n.startDate+'</td>';
                                html += '<td>'+n.endDate+'</td>';
                                html += '<td>'+n.owner+'</td>';
                                html += '</tr>';

                            })

                            $("#activitySearchBody").html(html);
                        }
                    })

                    return false;

                }

            })
            //为提交按钮绑定事件，填充市场活动源的内容，市场活动的name 和 id
            $("#submitActivityBtn").click(function (){

                //取得选中的id
                var $xz = $("input[name=xz]:checked");
                var id = $xz.val();

                //取得id的name
                var name = $("#"+id).html();
                //alert(id);
                //alert(name);

                $("#activityId1").val(id);
                $("#activityName1").val(name);
                //alert($("#activityId1").val());
                $("#activitySearchBody").html("");
                $("#findMarketActivity").modal("hide");

            })


            //为 放大镜图标，绑定事件，打开搜索联系人的模态窗口
            $("#openSearchConModalBtn").click(function (){
                //alert("123");
                $("#findContacts").modal("show");
            })
            //为搜索操作模态窗口中的搜索框绑定事件，执行搜索操作并展现联系人列表
            $("#search-contactName").keydown(function (event){

                if (event.keyCode==13){

                    $.ajax({
                        url:"workbench/transaction/getContactListByName.do",
                        data : {
                            "fullname" : $.trim($("#search-contactName").val())
                        },
                        type:"get",
                        dataType:"json",
                        success : function (data){

                            /*
                            * data
                            *       [{联系人1},{联系人2},{联系人3}...]
                            * */
                            var html = "";

                            $.each(data,function (i,n){

                                html += '<tr>';
                                html += '<td><input type="radio" name="xz1" value="'+n.id+'" /></td>';
                                html += '<td id="'+n.id+'">'+n.fullname+'</td>';
                                html += '<td>'+n.email+'</td>';
                                html += '<td>'+n.mphone+'</td>';
                                html += '</tr>';

                            })

                            $("#findContactBody").html(html);
                        }
                    })

                    return false;

                }

            })
            //为提交按钮绑定事件，填充联系人名称的内容，联系人名称的fullname 和 id
            $("#submitContactsBtn").click(function (){

                //取得选中的id
                var $xz = $("input[name=xz1]:checked");
                var id = $xz.val();

                //取得id的name
                var name = $("#"+id).html();
                //alert(id);
                //alert(name);

                $("#contactsId").val(id);
                $("#contactsName").val(name);
                //alert($("#contactsId").val());
                $("#findContactBody").html("");
                $("#findContacts").modal("hide");

            })


            //为保存按钮绑定事件，执行交易的添加操作
            $("#saveBtn").click(function (){

                //alert("123");
                //提交表单
                $("#tranForm").submit();

            })

        });

    </script>

</head>
<body>

<!-- 查找市场活动 -->
<div class="modal fade" id="findMarketActivity" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">查找市场活动</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" class="form-control" style="width: 300px;"
                                   id="activityName"
                                   placeholder="请输入市场活动名称，支持模糊查询">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable3" class="table table-hover"
                       style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>开始日期</td>
                        <td>结束日期</td>
                        <td>所有者</td>
                    </tr>
                    </thead>
                    <tbody id="activitySearchBody">
                    <%--<tr>
                        <td><input type="radio" name="activity"/></td>
                        <td>发传单</td>
                        <td>2020-10-10</td>
                        <td>2020-10-20</td>
                        <td>zhangsan</td>
                    </tr>
                    <tr>
                        <td><input type="radio" name="activity"/></td>
                        <td>发传单</td>
                        <td>2020-10-10</td>
                        <td>2020-10-20</td>
                        <td>zhangsan</td>
                    </tr>--%>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="submitActivityBtn" >提交</button>
            </div>
        </div>
    </div>
</div>

<!-- 查找联系人 -->
<div class="modal fade" id="findContacts" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">查找联系人</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text"
                                   id="search-contactName"
                                   class="form-control" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>邮箱</td>
                        <td>手机</td>
                    </tr>
                    </thead>
                    <tbody id="findContactBody" >
                    <%--<tr>
                        <td><input type="radio" name="activity"/></td>
                        <td>李四</td>
                        <td>lisi@bjpowernode.com</td>
                        <td>12345678901</td>
                    </tr>
                    <tr>
                        <td><input type="radio" name="activity"/></td>
                        <td>李四</td>
                        <td>lisi@bjpowernode.com</td>
                        <td>12345678901</td>
                    </tr>--%>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="submitContactsBtn" >提交</button>
            </div>
        </div>
    </div>
</div>


<div style="position:  relative; left: 30px;">
    <h3>创建交易</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" class="btn btn-primary" id="saveBtn">保存</button>
        <button type="button" class="btn btn-default">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form action="workbench/transaction/save.do" id="tranForm" method="post" class="form-horizontal" role="form" style="position: relative; top: -30px;">
    <div class="form-group">
        <label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-transactionOwner" name="owner" >
                <option></option>
                <c:forEach items="${userList}" var="u">
                    <option value="${u.id}" ${user.id eq u.id ? "selected" : ""} >${u.name}</option>
                </c:forEach>
                <%--<option>zhangsan</option>
                <option>lisi</option>
                <option>wangwu</option>--%>
            </select>
        </div>
        <label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-amountOfMoney" name="money" >
        </div>
    </div>

    <div class="form-group">
        <label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-transactionName" name="name" >
        </div>
        <label for="create-expectedClosingDate" class="col-sm-2 control-label ">预计成交日期<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control time1" id="create-expectedClosingDate" name="expectedDate" >
        </div>
    </div>

    <div class="form-group">
        <label for="create-customerName" class="col-sm-2 control-label">客户名称<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-customerName" name="customerName" placeholder="支持自动补全，输入客户不存在则新建">
        </div>
        <label for="create-transactionStage" class="col-sm-2 control-label">阶段<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-transactionStage" name="stage" >
                <option></option>
                <c:forEach items="${stage}" var="s">
                    <option value="${s.value}">${s.text}</option>
                </c:forEach>
                <%--<option>资质审查</option>
                <option>需求分析</option>
                <option>价值建议</option>
                <option>确定决策者</option>
                <option>提案/报价</option>
                <option>谈判/复审</option>
                <option>成交</option>
                <option>丢失的线索</option>
                <option>因竞争丢失关闭</option>--%>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label for="create-transactionType" class="col-sm-2 control-label">类型</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-transactionType" name="type">
                <option></option>
                <c:forEach items="${transactionType}" var="t">
                    <option value="${t.value}">${t.text}</option>
                </c:forEach>
                <%--<option>已有业务</option>
                <option>新业务</option>--%>
            </select>
        </div>

        <%--<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-possibility">
        </div>--%>
    </div>

    <div class="form-group">
        <label for="create-clueSource" class="col-sm-2 control-label">来源</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-clueSource" name="source">
                <option></option>
                <c:forEach items="${source}" var="sc">
                    <option value="${sc.value}">${sc.text}</option>
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
        <label for="activityName1" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;
            <a href="javascript:void(0);" id="openSearchModalBtn">
                <span class="glyphicon glyphicon-search"></span>
            </a>
        </label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="activityName1" placeholder="点击左侧放大镜搜索">
            <input type="hidden" id="activityId1" name="activityId1">
        </div>

    </div>

    <div class="form-group">
        <label for="contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;
            <a href="javascript:void(0);" id="openSearchConModalBtn">
                <span class="glyphicon glyphicon-search"></span>
            </a>
        </label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="contactsName" placeholder="点击左侧放大镜搜索">
            <input type="hidden" id="contactsId" name="contactsId">
        </div>
    </div>

    <div class="form-group">
        <label for="create-describe" class="col-sm-2 control-label">描述</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="create-describe" name="describe"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="create-contactSummary" name="contactSummary"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="create-nextContactTime" class="col-sm-2 control-label ">下次联系时间</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control time2" id="create-nextContactTime" name="nextContactTime">
        </div>
    </div>

</form>
</body>
</html>