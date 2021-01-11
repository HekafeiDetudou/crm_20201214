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
import com.bjpowernode.crm.workbench.entity.ActivityRemark;
import com.bjpowernode.crm.workbench.entity.Contacts;
import com.bjpowernode.crm.workbench.entity.ContactsRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ContactsService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ContactsServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsControllerServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到联系人控制器");

        String path = req.getServletPath();
        if ("/workbench/contacts/getUserList.do".equals(path)){

            getUserList(req,resp);

        }else if ("/workbench/contacts/getCustomerName.do".equals(path)){

            getCustomerName(req,resp);

        }else if ("/workbench/contacts/save.do".equals(path)){

            save(req,resp);

        }else if ("/workbench/contacts/pageList.do".equals(path)){

            pageList(req,resp);

        }else if ("/workbench/contacts/delete.do".equals(path)){

            delete(req,resp);

        }else if ("/workbench/contacts/getUserListAndContacts.do".equals(path)){

            getUserListAndContacts(req,resp);

        }else if ("/workbench/contacts/updateContacts.do".equals(path)){

            updateContacts(req,resp);

        }else if ("/workbench/contacts/detail.do".equals(path)){

            detail(req,resp);

        }else if ("/workbench/contacts/saveRemark.do".equals(path)){

            saveRemark(req,resp);

        }else if ("/workbench/contacts/getRemarkListByContactsId.do".equals(path)){

            getRemarkListByContactsId(req,resp);

        }else if ("/workbench/contacts/deleteRemark.do".equals(path)){

            deleteRemark(req,resp);

        }else if ("/workbench/contacts/updateRemark.do".equals(path)){

            updateRemark(req,resp);

        }

    }

    //进入到备注的修改操作
    private void updateRemark(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到备注的修改操作");

        String noteContent = req.getParameter("noteContent");
        String id = req.getParameter("id");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ContactsRemark cr = new ContactsRemark();
        cr.setNoteContent(noteContent);
        cr.setId(id);
        cr.setEditFlag(editFlag);
        cr.setEditBy(editBy);
        cr.setEditTime(editTime);

        ContactsService contactsService = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = contactsService.updateReamrk(cr);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("contactsRemark",cr);

        PrintJson.printJsonObj(resp,map);

    }

    //执行备注的删除操作
    private void deleteRemark(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行备注的删除操作");

        String id = req.getParameter("id");

        ContactsService contactsService = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = contactsService.deleteRemark(id);

        PrintJson.printJsonFlag(resp,flag);

    }

    //根据联系人的id取得备注信息列表
    private void getRemarkListByContactsId(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("根据联系人的id取得备注信息列表");

        String contactsId = req.getParameter("contactsId");

        ContactsService contactsService = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        List<ContactsRemark> crList = contactsService.getRemarkListByContactsId(contactsId);

        PrintJson.printJsonObj(resp,crList);

    }

    //进入到保存备注信息操作
    private void saveRemark(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到保存备注信息操作");

        String noteContent = req.getParameter("noteContent");
        String contactsId = req.getParameter("contactsId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ContactsRemark contactsRemark = new ContactsRemark();
        contactsRemark.setContactsId(contactsId);
        contactsRemark.setCreateBy(createBy);
        contactsRemark.setCreateTime(createTime);
        contactsRemark.setEditFlag(editFlag);
        contactsRemark.setId(id);
        contactsRemark.setNoteContent(noteContent);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());
        boolean flag = cs.saveReamrk(contactsRemark);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("contactsRemark",contactsRemark);

        PrintJson.printJsonObj(resp,map);

    }

    //进入到跳转到详细信息页操作
    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到跳转到详细信息页操作");

        String id = req.getParameter("id");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        Contacts contacts = cs.detail(id);

        req.setAttribute("c",contacts);

        req.getRequestDispatcher("/workbench/contacts/detail.jsp").forward(req,resp);

    }

    //执行联系人的修改操作
    private void updateContacts(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行联系人的修改操作");

        //获取到前端传过来的数据
        String id = req.getParameter("id");
        String owner = req.getParameter("owner");
        String source = req.getParameter("source");
        String customerName = req.getParameter("customerName");
        String fullname = req.getParameter("fullname");
        String appellation = req.getParameter("appellation");
        String email = req.getParameter("email");
        String job = req.getParameter("job");
        String mphone = req.getParameter("mphone");
        String birth = req.getParameter("birth");
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String address = req.getParameter("address");
        //修改时间:当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人：当前登录用户
        String editBy = ((User)req.getSession().getAttribute("user")).getName();

        Contacts contacts = new Contacts();
        contacts.setId(id);
        contacts.setOwner(owner);
        contacts.setSource(source);
        //contacts.setCustomerId(customerName);找到他的id放到里面去
        contacts.setFullname(fullname);
        contacts.setAppellation(appellation);
        contacts.setEmail(email);
        contacts.setMphone(mphone);
        contacts.setJob(job);
        contacts.setBirth(birth);
        contacts.setEditBy(editBy);
        contacts.setEditTime(editTime);
        contacts.setDescription(description);
        contacts.setContactSummary(contactSummary);
        contacts.setNextContactTime(nextContactTime);
        contacts.setAddress(address);


        //调用service执行更新操作，返回值为boolean类型
        ContactsService contactsService = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = contactsService.updateContacts(contacts,customerName);

        //将数据转换成json格式
        PrintJson.printJsonFlag(resp,flag);

    }

    //获取用户信息列表和根据市场活动id查询1条市场活动记录
    private void getUserListAndContacts(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("获取用户信息列表和根据市场活动id查询1条市场活动记录");

        String id = req.getParameter("id");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        /*
                总结：
                    controller调用service方法，返回值应该是什么，
                    要看一下前端需要什么，就从service层拿什么

                前端需要的向业务层去要
                    uList-->用户信息列表
                    a-->activity对象

                以上两项信息，复用率不高，所以使用map进行打包，然后进行传值
        * */
        Map<String,Object> map = cs.getUserListAndContacts(id);

        PrintJson.printJsonObj(resp,map);

    }

    //执行联系人的删除操作
    private void delete(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行联系人的删除操作");

        //接收id数组（param）  http://8080....id=dsdshdishdisdhs&id=diushdishdissdsd23
        String[] ids = req.getParameterValues("id");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.delete(ids);

        PrintJson.printJsonFlag(resp,flag);

    }

    //进入到查询市场活动信息列表的操作（结合分页查询+条件查询）
    private void pageList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到查询市场活动信息列表的操作（结合分页查询+条件查询）");

        String pageNoStr = req.getParameter("pageNo");
        String pageSizeStr = req.getParameter("pageSize");
        String owner = req.getParameter("owner");
        String fullname = req.getParameter("fullname");
        String customerName = req.getParameter("customerName");
        String source = req.getParameter("source");
        String birth = req.getParameter("birth");

        int pageNo = Integer.parseInt(pageNoStr);
        //每页显示的记录数
        int pageSize = Integer.parseInt(pageSizeStr);
        //计算略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("fullname",fullname);
        map.put("owner",owner);
        map.put("customerName",customerName);
        map.put("source",source);
        map.put("birth",birth);

        ContactsService contactsService = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        PaginationVO<Contacts> vo = contactsService.pageList(map);

        //System.out.println(vo);
        //vo-->{"total":100,"dataList":[{市场活动1},{市场活动2},{市场活动3},{市场活动4}...]}
        PrintJson.printJsonObj(resp,vo);

    }

    //进入到保存联系人操作
    private void save(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到保存联系人操作");

        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String source = req.getParameter("source");
        String customerName = req.getParameter("customerName");
        String fullname = req.getParameter("fullname");
        String appellation = req.getParameter("appellation");
        String email = req.getParameter("email");
        String mphone = req.getParameter("mphone");
        String job = req.getParameter("job");
        String birth = req.getParameter("birth");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String address = req.getParameter("address");

        Contacts contacts = new Contacts();

        contacts.setId(id);
        contacts.setOwner(owner);
        contacts.setSource(source);
        contacts.setFullname(fullname);
        contacts.setAppellation(appellation);
        contacts.setEmail(email);
        contacts.setMphone(mphone);
        contacts.setJob(job);
        contacts.setBirth(birth);
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setDescription(description);
        contacts.setContactSummary(contactSummary);
        contacts.setNextContactTime(nextContactTime);
        contacts.setAddress(address);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.save(contacts,customerName);

        PrintJson.printJsonFlag(resp,flag);

    }

    //取得客户名称列表（按照客户名称进行模糊查询）
    private void getCustomerName(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("取得客户名称列表（按照客户名称进行模糊查询）");

        String name = req.getParameter("name");

        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = customerService.getCustomerName(name);

        PrintJson.printJsonObj(resp,sList);

    }

    //取得用户信息列表
    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("取得用户信息列表");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();

        //将集合转换成json数组的格式
        PrintJson.printJsonObj(resp,userList);

    }
}
