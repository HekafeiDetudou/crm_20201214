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
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <script>
        $(function (){

            if(window.top!=window){
                window.top.location=window.location;
            }


            //页面加载完毕后，清空用户文本框中的内容
            $("#loginAct").val("");
            $("#loginPwd").val("");

            //页面加载完成后，让用户的文本框自动获得焦点
            $("#loginAct").focus();

            //为登录按钮绑定事件执行登录操作
            $("#submitBtn").click(function (){
                login();
            })

            //为当前窗口绑定键盘敲击事件，执行登录操作
            $(window).keydown(function (event){
                //如果取得的码值是13，则表示敲的是回车键
                if (event.keyCode==13){
                    login();
                }
            })

            //为保存按钮绑定事件，执行添加用户操作
            $("#saveBtn").click(function (){
                //alert(123);
                doSave();
            })

            //清空模态窗口中的数据
            cleanModul();


        })




        //清空模态窗口中的数据
        function cleanModul(){

            $("#create-loginAct").val("");
            $("#create-name").val("");
            $("#create-loginPwd").val("");
            $("#create-email").val("");
            $("#checkLA").val("");

        }

        //验证用户登录
        function login(){

            //alert("执行登录验证操作");
            //验证账号密码不能为空
            //取得账号,密码
            //将文本中的空格去除，使用$.trim();
            var loginAct = $.trim($("#loginAct").val());
            var loginPwd = $.trim($("#loginPwd").val());
            if (loginAct == "" || loginPwd == ""){
                $("#msg").html("账号密码不能为空");
                return false;//此行代码执行之后login方法中，此行下面的代码全部不执行
            }

            //去后台验证账号密码
            $.ajax({
                url:"settings/user/login.do",
                data : {
                    "loginAct":loginAct,
                    "loginPwd":loginPwd
                },
                type:"post",
                dataType:"json",
                success : function (data){
                    if (data.success){
                        //如果登录成功
                        //跳转到工作台的欢迎页
                        window.location.href = "workbench/index.jsp";
                    }else {
                        //登录失败
                        $("#msg").html(data.msg);
                    }
                }
            })


        }

        //打开用户注册模态窗口
        function a(){
            //alert("123");
            cleanModul();
            $("#createUserModal").modal("show");
        }

        //提交用户表单，保存数据
        function doSave() {
            //验证表单数据是否合法
            var ok = validateform();
            //如果合法，提交表单
            if (ok) {
                //document.forms[0].submit();

                $.ajax({
                    url:"settings/visit/userAdd.do",
                    data : {

                        "loginAct" : $.trim($("#create-loginAct").val()),
                        "name" : $.trim($("#create-name").val()),
                        "loginPwd" : $.trim($("#create-loginPwd").val()),
                        "email" : $.trim($("#create-email").val())

                    },
                    type:"post",
                    dataType:"json",
                    success : function (data){

                        if (data.success){

                            //清空模态窗口中的数据
                            $("#userAddForm")[0].reset();

                            //清空登录框中的数据
                            $("#loginAct").val("");
                            $("#loginPwd").val("");

                            //关闭添加操作的模态窗口
                            $("#createUserModal").modal("hide");

                        }else {

                            alert("添加用户失败！");

                        }

                    }
                })

            }
        }

        //校验表单是否合法，非空校验和重复校验
        function validateform() {
            //用户代码不能为空
            var loginAct = document.getElementById("create-loginAct");
            if (loginAct.value == ""){
                alert("登录账号不能为空，请填写！");
                loginAct.focus();
                return false;
            }

            //用户姓名不能为空
            var name = document.getElementById("create-name");
            if (name.value == ""){
                alert("用户姓名不能为空，请填写!");
                name.focus();
                return false;
            }
            //用户密码不能为空
            var loginPwd = document.getElementById("create-loginPwd");
            if (loginPwd.value == ""){
                alert("用户密码不能为空，请填写!");
                loginPwd.focus();
                return false;
            }
            //用户邮箱不能为空
            var email = document.getElementById("create-email");
            if (email.value == ""){
                alert("用户邮箱不能为空，请填写！");
                email.focus();
                return false;
            }

            //如果span标签中有报错信息，说明该登录账号已经存在
            if ($("#checkLA").val() != ""){
                return false;
            }

            return true;
        }

        //使用Ajax请求，验证账号是否已经存在
        function checkLoginAct(loginAct){

            $.ajax({
                url:"settings/visit/checkLoginAct.do",
                data : {
                    "loginAct" : loginAct
                },
                type:"get",
                dataType:"json",
                success : function (data){

                    if (data.success){

                        //如果返回为true，说明已经存在该账户
                        $("#checkLA").html("该登录账号已经存在，请重新选择！");

                    }else{

                        $("#checkLA").html("");

                    }

                }
            })

        }

    </script>

</head>
<body>

<!-- 创建用户的模态窗口 -->
<div class="modal fade" id="createUserModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 45%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建用户</h4>
            </div>
            <div class="modal-body">

                <form id="userAddForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-loginAct" class="col-sm-2 control-label">登录账号<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-loginAct" onblur="checkLoginAct(this.value)" name="loginAct">
                            <span id="checkLA" style="color: red"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-name" class="col-sm-2 control-label">用户姓名<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-name" name="name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-loginPwd" class="col-sm-2 control-label">登录密码<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control " id="create-loginPwd" name="loginPwd">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-email" class="col-sm-2 control-label">邮箱</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control " id="create-email" name="email">
                        </div>
                    </div>
                    <%--<div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">登录账号</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>--%>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveBtn">创建</button>
            </div>
        </div>
    </div>
</div>

<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">
        CRM &nbsp;<span style="font-size: 12px;">&copy;2021&nbsp;</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <%--<h1>登录</h1>--%>
            <table cellspacing="20px"; border="0px"; width="300px">
                <tr>
                    <th style="text-align: left"><span style="font-size: 30px">登录</span></th>

                    <th style="text-align: right">
                        <a style="text-decoration: none; cursor: pointer;"; onclick="a()"; >
                            <span style="font-size: 30px">注册</span>
                        </a>
                    </th>
                </tr>
            </table>

        </div>
        <form action="workbench/index.html" class="form-horizontal" role="form">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input class="form-control" type="text" placeholder="用户名" id="loginAct">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input class="form-control" type="password" placeholder="密码" id="loginPwd">
                </div>
                <div class="checkbox" style="position: relative;top: 30px; left: 10px;">

                    <span id="msg" style="color: red"></span>

                </div>

                <%--
                    按钮写在form表单中，默认的行为就是提交表单
                    一定要将按钮设置为button
                    按钮所触发的行为应该是由我们自己手动写js代码来决定
                --%>
                <button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"
                        style="width: 350px; position: relative;top: 45px;">登录
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>