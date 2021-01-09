package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.entity.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.entity.Activity;
import com.bjpowernode.crm.workbench.entity.Customer;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerControllerServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到客户控制器");

        String path = request.getServletPath();
        if ("/workbench/customer/getUserList.do".equals(path)){

            getUserList(request,response);

        }else if ("/workbench/customer/save.do".equals(path)){

            save(request,response);

        }else if ("/workbench/customer/pageList.do".equals(path)){

            pageList(request,response);

        }else if ("/workbench/customer/detail.do".equals(path)){

            detail(request,response);

        }else if ("/workbench/customer/delete.do".equals(path)){

            delete(request,response);

        }else if ("/workbench/customer/getUserListAndCustomer.do".equals(path)){

            getUserListAndCustomer(request,response);

        }else if ("/workbench/customer/updateCustomer.do".equals(path)){

            updateCustomer(request,response);

        }

    }


    //执行客户的修改操作
    private void updateCustomer(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行客户的修改操作");

        //获取到前端传过来的数据
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String website = request.getParameter("website");
        String phone = request.getParameter("phone");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");
        //修改时间:当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人：当前登录用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Customer customer = new Customer();
        customer.setId(id);
        customer.setOwner(owner);
        customer.setName(name);
        customer.setWebsite(website);
        customer.setPhone(phone);
        customer.setEditBy(editBy);
        customer.setEditTime(editTime);
        customer.setContactSummary(contactSummary);
        customer.setNextContactTime(nextContactTime);
        customer.setDescription(description);
        customer.setAddress(address);

        //调用service执行更新操作，返回值为boolean类型
        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        boolean flag = customerService.updateCustomer(customer);

        //将数据转换成json格式
        PrintJson.printJsonFlag(response,flag);

    }


    //获取用户信息列表和根据市场活动id查询1条市场活动记录
    private void getUserListAndCustomer(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("获取用户信息列表和根据市场活动id查询1条市场活动记录");

        String id = request.getParameter("id");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        Map<String,Object> map = cs.getUserListAndActivity(id);

        PrintJson.printJsonObj(response,map);

    }

    //执行客户的删除操作
    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行客户的删除操作");

        //接收id数组（param）  http://8080....id=dsdshdishdisdhs&id=diushdishdissdsd23
        String[] ids = request.getParameterValues("id");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.delete(ids);

        PrintJson.printJsonFlag(response,flag);

    }

    //进入到跳转到详细信息页操作
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到跳转到详细信息页操作");

        String id = request.getParameter("id");

        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        Customer c = customerService.detail(id);

        request.setAttribute("c",c);

        request.getRequestDispatcher("/workbench/customer/detail.jsp").forward(request,response);

    }

    //进入到查询客户信息列表的操作（结合分页查询+条件查询）
    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询客户信息列表的操作（结合分页查询+条件查询）");

        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");

        int pageNo = Integer.parseInt(pageNoStr);
        //每页显示的记录数
        int pageSize = Integer.parseInt(pageSizeStr);
        //计算略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("name",name);
        map.put("owner",owner);
        map.put("phone",phone);
        map.put("website",website);

        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        //vo-->{"total":100,"dataList":[{客户1},{客户2},{客户3}...]}
        PaginationVO<Customer> vo = customerService.pageList(map);

        PrintJson.printJsonObj(response,vo);

    }

    //执行客户的添加操作
    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行客户的添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String website = request.getParameter("website");
        String phone = request.getParameter("phone");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String description = request.getParameter("description");
        String address = request.getParameter("address");

        //创建时间:当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Customer customer = new Customer();
        customer.setId(id);
        customer.setOwner(owner);
        customer.setName(name);
        customer.setWebsite(website);
        customer.setPhone(phone);
        customer.setCreateBy(createBy);
        customer.setCreateTime(createTime);
        customer.setContactSummary(contactSummary);
        customer.setNextContactTime(nextContactTime);
        customer.setDescription(description);
        customer.setAddress(address);

        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        boolean flag = customerService.save(customer);

        PrintJson.printJsonFlag(response,flag);

    }

    //取得用户信息列表
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户信息列表");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();

        //将集合转换成json数组的格式
        PrintJson.printJsonObj(response,userList);

    }
}
