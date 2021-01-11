package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.entity.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.entity.*;
import com.bjpowernode.crm.workbench.service.*;
import com.bjpowernode.crm.workbench.service.impl.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        }else if ("/workbench/transaction/pageList.do".equals(path)){

            pageList(request,response);

        }else if ("/workbench/transaction/detail.do".equals(path)){

            detail(request,response);

        }else if ("/workbench/transaction/getHistoryListByTranId.do".equals(path)){

            getHistoryListByTranId(request,response);

        }else if ("/workbench/transaction/getCharts.do".equals(path)){

            getCharts(request,response);

        }else if ("/workbench/transaction/delete.do".equals(path)){

            delete(request,response);

        }else if ("/workbench/transaction/getRemarkListByTranId.do".equals(path)){

            getRemarkListByTranId(request,response);

        }else if ("/workbench/transaction/saveRemark.do".equals(path)){

            saveRemark(request,response);

        }else if ("/workbench/transaction/deleteRemark.do".equals(path)){

            deleteRemark(request,response);

        }else if ("/workbench/transaction/updateRemark.do".equals(path)){

            updateRemark(request,response);

        }

    }

    //进入到备注的修改操作
    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到备注的修改操作");

        String noteContent = request.getParameter("noteContent");
        String id = request.getParameter("id");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        TranRemark tranRemark = new TranRemark();
        tranRemark.setNoteContent(noteContent);
        tranRemark.setId(id);
        tranRemark.setEditFlag(editFlag);
        tranRemark.setEditBy(editBy);
        tranRemark.setEditTime(editTime);

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = tranService.updateReamrk(tranRemark);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("tranRemark",tranRemark);

        PrintJson.printJsonObj(response,map);

    }

    //执行备注的删除操作
    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行备注的删除操作");

        String id = request.getParameter("id");

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = tranService.deleteRemark(id);

        PrintJson.printJsonFlag(response,flag);

    }

    //进入到保存备注信息操作
    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到保存备注信息操作");

        String noteContent = request.getParameter("noteContent");
        String tranId = request.getParameter("tranId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        TranRemark tranRemark = new TranRemark();
        tranRemark.setTranId(tranId);
        tranRemark.setCreateBy(createBy);
        tranRemark.setCreateTime(createTime);
        tranRemark.setEditFlag(editFlag);
        tranRemark.setId(id);
        tranRemark.setNoteContent(noteContent);

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = tranService.saveReamrk(tranRemark);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("tranRemark",tranRemark);

        PrintJson.printJsonObj(response,map);

    }

    //根据交易的id取得备注信息列表
    private void getRemarkListByTranId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据交易的id取得备注信息列表");

        String tranId = request.getParameter("tranId");

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranRemark> trList = tranService.getRemarkListByTranId(tranId);

        PrintJson.printJsonObj(response,trList);

    }

    //执行交易的删除操作
    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行交易的删除操作");

        String[] ids = request.getParameterValues("id");

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = tranService.delete(ids);

        System.out.println(flag);

        PrintJson.printJsonFlag(response,flag);

    }

    //进入到获取ECharts数据操作
    private void getCharts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到获取ECharts数据操作");

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        /*
        * 业务层为我们返回
        *       total
        *       dataList
        * */
        Map<String,Object> map = tranService.getCharts();

        PrintJson.printJsonObj(response,map);

    }

    //获取交易历史记录
    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("获取交易历史记录");

        String id = request.getParameter("id");

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranHistory> tranHistoryList = tranService.getHistoryListByTranId(id);

        PrintJson.printJsonObj(response,tranHistoryList);

    }

    //跳转到展示交易细节页面
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到展示交易细节页面");

        String id = request.getParameter("id");

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran tran = tranService.detail(id);

        request.setAttribute("t",tran);

        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);

    }

    //分页查询交易信息
    private void pageList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到查询交易信息列表的操作（结合分页查询+条件查询）");

        String pageNoStr = req.getParameter("pageNo");
        String pageSizeStr = req.getParameter("pageSize");
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String customerName = req.getParameter("customerName");
        String stage = req.getParameter("stage");
        String type = req.getParameter("type");
        String source = req.getParameter("source");
        String contactsName = req.getParameter("contactsName");


        //System.out.println("name:"+name);
        int pageNo = Integer.parseInt(pageNoStr);
        //每页显示的记录数
        int pageSize = Integer.parseInt(pageSizeStr);
        //计算略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        //System.out.println(pageSize);
        //System.out.println(skipCount);

        Map<String,Object> map = new HashMap<>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        map.put("owner",owner);
        map.put("name",name);
        map.put("customerName",customerName);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        map.put("contactsName",contactsName);

       /* ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PaginationVO<Activity> vo = activityService.pageList(map);*/
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        PaginationVO<Tran> vo = tranService.pageList(map);
        //System.out.println(vo);
        //vo-->{"total":100,"dataList":[{市场活动1},{市场活动2},{市场活动3},{市场活动4}...]}
        PrintJson.printJsonObj(resp,vo);

    }

    //进入到保存交易操作
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
        String description = request.getParameter("describe");
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

            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");

        }

    }

    //取得客户名称列表（按照客户名称进行模糊查询）
    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得客户名称列表（按照客户名称进行模糊查询）");

        String name = request.getParameter("name");

        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = customerService.getCustomerName(name);

        PrintJson.printJsonObj(response,sList);

    }

    //进入到查询联系人列表操作（根据fullname模糊查询）
    private void getContactListByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询联系人列表操作（根据fullname模糊查询）");

        String fullname = request.getParameter("fullname");

        ContactsService contactsService = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        List<Contacts> contactsList = contactsService.getContactListByName(fullname);

        PrintJson.printJsonObj(response,contactsList);

    }

    //跳转到交易添加页的操作
    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到交易添加页的操作");

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = userService.getUserList();

        request.setAttribute("userList",userList);

        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);


    }

}












