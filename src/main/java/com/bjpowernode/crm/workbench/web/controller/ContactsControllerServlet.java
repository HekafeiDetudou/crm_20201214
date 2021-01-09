package com.bjpowernode.crm.workbench.web.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContactsControllerServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到联系人控制器");

        String path = req.getServletPath();
        if ("/workbench/contacts/xxx.do".equals(path)){

            //xxx(req,resp);

        }else if ("/workbench/contacts/xxx.do".equals(path)){

            //xxx(req,resp);

        }

    }
}
