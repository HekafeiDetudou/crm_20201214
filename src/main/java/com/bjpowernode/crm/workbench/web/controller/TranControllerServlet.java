package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.entity.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.entity.Contacts;
import com.bjpowernode.crm.workbench.entity.Tran;
import com.bjpowernode.crm.workbench.service.ContactsService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.ContactsServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;

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

        }else if ("/workbench/transaction/getCustomerName.do".equals(path)){

            getCustomerName(request,response);

        }else if ("/workbench/transaction/save.do".equals(path)){

            save(request,response);

        }

    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("进入到保存交易操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId1");
        String contactsId = request.getParameter("contactsId");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran tran = new Tran();

        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        //tran.setCustomerId(customerId);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.save(tran,customerName);
        if (flag){

            response.sendRedirect(request.getContextPath() + "/workbench/contacts/index.jsp");

        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得客户名称列表（按照客户名称进行模糊查询）");

        String name = request.getParameter("name");

        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = customerService.getCustomerName(name);

        PrintJson.printJsonObj(response,sList);

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












