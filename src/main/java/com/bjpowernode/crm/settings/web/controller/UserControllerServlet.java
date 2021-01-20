package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.entity.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;

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
        }else if ("/workbench/visit/updatePswd.do".equals(path)){

            updatePswd(req,resp);

        }
    }

    private void updatePswd(HttpServletRequest req, HttpServletResponse resp) {

        

    }

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
