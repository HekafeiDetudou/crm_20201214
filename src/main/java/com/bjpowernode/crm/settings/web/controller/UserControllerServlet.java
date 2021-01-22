package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.entity.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserControllerServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到用户控制器");

        String path = req.getServletPath();
        if ("/settings/user/login.do".equals(path)){

            login(req,resp);

        }else if ("/settings/visit/updatePwd.do".equals(path)){

            updatePwd(req,resp);

        }else if ("/settings/visit/checkLoginAct.do".equals(path)){

            checkLoginAct(req,resp);

        }else if ("/settings/visit/userAdd.do".equals(path)){

            userAdd(req,resp);

        }
    }

    //进入到保存用户操作
    private void userAdd(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到保存用户操作");

        String id = UUIDUtil.getUUID();
        String loginAct = request.getParameter("loginAct");
        String name = request.getParameter("name");
        String loginPwd = request.getParameter("loginPwd");
        loginPwd = MD5Util.getMD5(loginPwd);
        String email = request.getParameter("email");
        String expireTime = "2080-11-30 23:50:55";          //后台生成
        String lockState = "1";                             //后台生成
        String deptno = "A001";                             //后台生成
        String allowIps = "";                               //后台生成
        //创建时间:当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy = id;

        User user = new User();
        user.setId(id);
        user.setLoginAct(loginAct);
        user.setName(name);
        user.setLoginPwd(loginPwd);
        user.setEmail(email);
        user.setExpireTime(expireTime);
        user.setLockState(lockState);
        user.setDeptno(deptno);
        user.setAllowIps(allowIps);
        user.setCreateBy(createBy);
        user.setCreateTime(createTime);

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = userService.addUser(user);

        PrintJson.printJsonFlag(response,flag);

    }

    //进入到检查登录账号是否重复的操作
    private void checkLoginAct(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到检查登录账号是否重复的操作");

        String loginAct = request.getParameter("loginAct");

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        boolean flag = userService.checkLoginAct(loginAct);

        PrintJson.printJsonFlag(response,flag);

    }

    //进入到修改用户密码操作
    private void updatePwd(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到修改用户密码操作");

        /*
        先验证原密码是否正确
            如果源密码正确，
                则判断pwd2和pwd3是否相同，
                    如果相同，则修改密码，返回true
                    如果不同，则返回错误信息
            如果原密码不正确，
                则直接返回msg，源密码不正确，false
        * */

        //接收参数
        String pwd1 = request.getParameter("pwd1");
        String pwd2 = request.getParameter("pwd2");
        String pwd3 = request.getParameter("pwd3");
        //将明文密码通过MD5加密方式转换成密文
        pwd1 = MD5Util.getMD5(pwd1);
        String loginPwd = ((User)request.getSession().getAttribute("user")).getLoginPwd();
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        if (loginPwd.equals(pwd1)){

            //如果原密码正确
            if (pwd2.equals(pwd3)){

                //修改密码
                User user = (User) request.getSession().getAttribute("user");
                user.setLoginPwd(MD5Util.getMD5(pwd2));
                boolean flag = userService.updatePwd(user);
                PrintJson.printJsonFlag(response,flag);

            }else {

                String msg = "新密码和确认密码不一致，请重新输入";
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("seccess",false);
                map.put("msg",msg);
                PrintJson.printJsonObj(response,map);

            }

        }else {

            //如果原密码不正确
            String msg = "原密码不正确，请重新输入";
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("seccess",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);

        }


    }

    //进入到登录验证操作
    private void login(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到登录验证操作");
        String loginAct = req.getParameter("loginAct");
        String loginPwd = req.getParameter("loginPwd");
        //将明文密码通过MD5加密方式转换成密文
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收浏览器端的ip地址
        String ip = req.getRemoteAddr();
        System.out.println("ip地址："+ip);

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try {
            User user = userService.login(loginAct,loginPwd,ip);
            req.getSession().setAttribute("user",user);
            //程序执行到此处，表示登录成功
            PrintJson.printJsonFlag(resp,true);
        }catch (Exception e){
            e.printStackTrace();
            //程序执行到此处，表示登录失败
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("seccess",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(resp,map);
        }
    }
}
