package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.entity.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.workbench.entity.Contacts;
import com.bjpowernode.crm.workbench.service.ContactsService;
import com.bjpowernode.crm.workbench.service.impl.ContactsServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TranControllerServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到交易控制器");

        String path = request.getServletPath();
        if ("/workbench/transaction/add.do".equals(path)){

            add(request,response);

        }else if ("/workbench/transaction/getContactListByName.do".equals(path)){

            getContactListByName(request,response);

        }

    }

    private void getContactListByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询联系人列表操作（根据fullname模糊查询）");

        String fullname = request.getParameter("fullname");

        ContactsService contactsService = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        List<Contacts> contactsList = contactsService.getContactListByName(fullname);

        PrintJson.printJsonObj(response,contactsList);

    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到交易添加页的操作");

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = userService.getUserList();

        request.setAttribute("userList",userList);

        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);


    }
}












