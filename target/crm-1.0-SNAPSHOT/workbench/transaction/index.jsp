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

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <%--分页查询组件--%>
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


    <script type="text/javascript">

        $(function(){

            //页面加载完毕后触发一个方法
            pageList(1,2);

            //为查询按钮绑定事件，触发pageList方法
            $("#searchBtn").click(function (){

                //alert("123");
                /*
                *   点击查询按钮的时候，我们应该将搜索框中的内容保存起来，保存到隐藏域中
                * */
                $("#hidden-owner").val($.trim($("#search-owner").val()));
                $("#hidden-name").val($.trim($("#search-name").val()));
                $("#hidden-customerName").val($.trim($("#search-customerName").val()));
                $("#hidden-stage").val($.trim($("#search-stage").val()));
                $("#hidden-type").val($.trim($("#search-type").val()));
                $("#hidden-source").val($.trim($("#search-source").val()));
                $("#hidden-contactsName").val($.trim($("#search-contactsName").val()));

                pageList(1,2);

            })

        });

        function pageList(pageNo,pageSize){

            //alert("123");
            //清空全选的复选框(将全选的复选框上面的√清除)
            //$("#checkedAll").prop("checked",false);

            //查询前，将隐藏域中的信息放到搜索框中
            $("#search-owner").val($.trim($("#hidden-owner").val()));
            $("#search-name").val($.trim($("#hidden-name").val()));
            $("#search-customerName").val($.trim($("#hidden-customerName").val()));
            $("#search-stage").val($.trim($("#hidden-stage").val()));
            $("#search-type").val($.trim($("#hidden-type").val()));
            $("#search-source").val($.trim($("#hidden-source").val()));
            $("#search-contactsName").val($.trim($("#hidden-contactsName").val()));

            //alert("展现市场活动分页");

            //使用Ajax请求完成分页操作
            $.ajax({
                url:"workbench/transaction/pageList.do",
                data : {

                    //需要为后台提供的参数
                    "pageNo" : pageNo,
                    "pageSize" : pageSize,
                    //条件查询
                    "owner" : $.trim($("#search-owner").val()),
                    "name" : $.trim($("#search-name").val()),
                    "customerName" : $.trim($("#search-customerName").val()),
                    "stage" : $.trim($("#search-stage").val()),
                    "type" : $.trim($("#search-type").val()),
                    "source" : $.trim($("#search-source").val()),
                    "contactsName" : $.trim($("#search-contactsName").val())

                },
                type:"get",
                dataType:"json",
                success : function (data){

                    /*
                        data：
                            我们需要的交易信息列表
                                [{交易1},{交易2},{交易3}...]
                            一会分页插件需要的：查询出来的总记录条数
                                {"total":100}
                            拼接起来：
                                {"total":100,"dataList":[{交易1},{交易2},{交易3}...]}
                     */
                    var html = "";

                    //每一个n就是一个交易对象
                    $.each(data.dataList,function (i,n){

                        html += '<tr>';
                        html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
                        html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/transaction/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
                        html += '<td>'+n.customerId+'</td>';
                        html += '<td>'+n.stage+'</td>';
                        html += '<td>'+n.type+'</td>';
                        html += '<td>'+n.owner+'</td>';
                        html += '<td>'+n.source+'</td>';
                        html += '<td>'+n.contactsId+'</td>';
                        html += '</tr>';

                    })

                    $("#tranBody").html(html);

                    //计算总页数
                    var totalPages = data.total%pageSize==0 ? data.total/pageSize : parseInt(data.total/pageSize)+1;

                    //数据处理完毕后，结合分页查询，对前端展示信息
                    $("#tranPage").bs_pagination({
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
        <input type="hidden" id="hidden-name" />
        <input type="hidden" id="hidden-customerName" />
        <input type="hidden" id="hidden-stage" />
        <input type="hidden" id="hidden-type" />
        <input type="hidden" id="hidden-source" />
        <input type="hidden" id="hidden-contactsName" />

<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>交易列表</h3>
        </div>
    </div>
</div>

<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="search-owner">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="search-name">
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
                        <div class="input-group-addon">阶段</div>
                        <select class="form-control"  id="search-stage">
                            <option></option>
                            <c:forEach items="${stage}" var="stage">
                                <option value="${stage.value}" >${stage.text}</option>
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
                    <div class="input-group">
                        <div class="input-group-addon">类型</div>
                        <select class="form-control"  id="search-type">
                            <option></option>
                            <c:forEach items="${transactionType}" var="transactionType">
                                <option value="${transactionType.value}" >${transactionType.text}</option>
                            </c:forEach>
                            <%--<option>已有业务</option>
                            <option>新业务</option>--%>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">来源</div>
                        <select class="form-control" id="search-source">
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
                        <div class="input-group-addon">联系人名称</div>
                        <input class="form-control" type="text" id="search-contactsName">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="searchBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" onclick="window.location.href='workbench/transaction/add.do';"><span class="glyphicon glyphicon-plus"></span> 创建</button>
                <button type="button" class="btn btn-default" onclick="window.location.href='workbench/transaction/edit.jsp';"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
                <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" /></td>
                    <td>名称</td>
                    <td>客户名称</td>
                    <td>阶段</td>
                    <td>类型</td>
                    <td>所有者</td>
                    <td>来源</td>
                    <td>联系人名称</td>
                </tr>
                </thead>
                <tbody id="tranBody">
                <%--<tr>
                    <td><input type="checkbox" /></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/transaction/detail.html';">动力节点-交易01</a></td>
                    <td>动力节点</td>
                    <td>谈判/复审</td>
                    <td>新业务</td>
                    <td>zhangsan</td>
                    <td>广告</td>
                    <td>李四</td>
                </tr>
                <tr class="active">
                    <td><input type="checkbox" /></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/transaction/detail.html';">动力节点-交易01</a></td>
                    <td>动力节点</td>
                    <td>谈判/复审</td>
                    <td>新业务</td>
                    <td>zhangsan</td>
                    <td>广告</td>
                    <td>李四</td>
                </tr>--%>
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 20px;">
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
                <div id="tranPage"></div>
        </div>

    </div>

</div>
</body>
</html>