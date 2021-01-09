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
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClueControllerServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到线索控制器");

        String path = req.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)) {

            getUserList(req, resp);

        } else if ("/workbench/clue/save.do".equals(path)) {

            save(req, resp);

        } else if ("/workbench/clue/pageList.do".equals(path)) {

            pageList(req, resp);

        } else if ("/workbench/clue/detail.do".equals(path)) {

            detail(req, resp);

        } else if ("/workbench/clue/getActivityListByClueId.do".equals(path)) {

            getActivityListByClueId(req, resp);

        } else if ("/workbench/clue/unbound.do".equals(path)) {

            unbound(req, resp);

        } else if ("/workbench/clue/getActivityListByName.do".equals(path)) {

            getActivityListByName(req, resp);

        } else if ("/workbench/clue/bound.do".equals(path)) {

            bound(req, resp);

        } else if ("/workbench/clue/getActivityListByNameJust.do".equals(path)) {

            getActivityListByNameJust(req, resp);

        }else if ("/workbench/clue/convert.do".equals(path)) {

            convert(req, resp);

        }else if ("/workbench/clue/getUserListAndClue.do".equals(path)) {

            getUserListAndClue(req, resp);

        }else if ("/workbench/clue/updateClue.do".equals(path)) {

            updateClue(req, resp);

        }else if ("/workbench/clue/delete.do".equals(path)) {

            delete(req, resp);

        }else if ("/workbench/clue/saveRemark.do".equals(path)) {

            saveRemark(req, resp);

        }else if ("/workbench/clue/getRemarkListByClueId.do".equals(path)) {

            getRemarkListByClueId(req, resp);

        }else if ("/workbench/clue/deleteRemark.do".equals(path)) {

            deleteRemark(req, resp);

        }else if ("/workbench/clue/updateRemark.do".equals(path)) {

            updateRemark(req, resp);

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

        ClueRemark cr = new ClueRemark();
        cr.setNoteContent(noteContent);
        cr.setId(id);
        cr.setEditFlag(editFlag);
        cr.setEditBy(editBy);
        cr.setEditTime(editTime);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.updateReamrk(cr);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("cr1",cr);

        PrintJson.printJsonObj(resp,map);

    }

    //执行备注的删除操作
    private void deleteRemark(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行备注的删除操作");

        String id = req.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.deleteRemark(id);

        PrintJson.printJsonFlag(resp,flag);

    }

    //根据线索的id取得备注信息列表
    private void getRemarkListByClueId(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("根据线索的id取得备注信息列表");

        String clueId = req.getParameter("clueId");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        List<ClueRemark> crList = cs.getRemarkListByClueId(clueId);

        PrintJson.printJsonObj(resp,crList);

    }


    //进入到保存备注信息操作
    private void saveRemark(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到保存备注信息操作");

        String noteContent = req.getParameter("noteContent");
        String clueId = req.getParameter("clueId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ClueRemark clueRemark = new ClueRemark();
        clueRemark.setClueId(clueId);
        clueRemark.setCreateBy(createBy);
        clueRemark.setCreateTime(createTime);
        clueRemark.setEditFlag(editFlag);
        clueRemark.setId(id);
        clueRemark.setNoteContent(noteContent);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.saveReamrk(clueRemark);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("clueRemark",clueRemark);

        PrintJson.printJsonObj(resp,map);

    }

    //执行线索的删除操作
    private void delete(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行线索的删除操作");

        //接收id数组（param）  http://8080....id=dsdshdishdisdhs&id=diushdishdissdsd23
        String[] ids = req.getParameterValues("id");

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());


        boolean flag = cs.delete(ids);

        PrintJson.printJsonFlag(resp,flag);

    }

    //执行线索的修改操作
    private void updateClue(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行线索的修改操作");

        //获取到前端传过来的数据
        String id = req.getParameter("id");
        String fullname = req.getParameter("fullname");
        String appellation = req.getParameter("appellation");
        String owner = req.getParameter("owner");
        String company = req.getParameter("company");
        String job = req.getParameter("job");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String website = req.getParameter("website");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");
        String source = req.getParameter("source");
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String address = req.getParameter("address");

        //修改时间:当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人：当前登录用户
        String editBy = ((User)req.getSession().getAttribute("user")).getName();

        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setEditBy(editBy);
        clue.setEditTime(editTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);



        //调用service执行更新操作，返回值为boolean类型
        ClueService clueService = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.updateClue(clue);

        //将数据转换成json格式
        PrintJson.printJsonFlag(resp,flag);

    }

    //获取用户信息列表和根据线索id查询1条线索记录
    private void getUserListAndClue(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("获取用户信息列表和根据线索id查询1条线索记录");

        String id = req.getParameter("id");

        /*ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Map<String,Object> map = as.getUserListAndActivity(id);*/

        ClueService clueService = (ClueService)ServiceFactory.getService(new ClueServiceImpl());

        Map<String,Object> map = clueService.getUserListAndClue(id);

        PrintJson.printJsonObj(resp,map);

    }


    //进入到线索转换操作
    private void convert(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        System.out.println("进入到线索转换操作");

        String clueId = req.getParameter("clueId");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();

        //接收是否需要创建交易的标记
        String flag = req.getParameter("flag");
        Tran t = null;

        if ("a".equals(flag)){

            //程序执行到到此处说明需要创建交易
            //接收表单中的参数
            t = new Tran();

            String money = req.getParameter("money");
            String name = req.getParameter("name");
            String expectedDate = req.getParameter("expectedDate");
            String stage = req.getParameter("stage");
            String activityId = req.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();

            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);

        }

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        /*
        * 为业务层传递的参数
        *   1、必须传递的参数clueId，有了这个参数才知道需要转换的是那条记录
        *   2、必须传递的参数t，因为在线索转化过程中，有可能需要 新建交易（业务层接收的交易有可能是null）
        * */
        boolean flag1 = clueService.convert(clueId,t,createBy);

        if (flag1){

            resp.sendRedirect(req.getContextPath()+"/workbench/clue/index.jsp");

        }

    }

    //查询市场活动列表（根据名称模糊查询）
    private void getActivityListByNameJust(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("查询市场活动列表（根据名称模糊查询）");

        String activityName = req.getParameter("activityName");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activityList = as.getActivityListByNameJust(activityName);

        PrintJson.printJsonObj(resp,activityList);

    }

    //进入到关联线索的市场活动操作
    private void bound(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到关联线索的市场活动操作");

        String cid = req.getParameter("cid");
        String[] aids = req.getParameterValues("aid");
        Map<String,Object> map = new HashMap<>();
        map.put("cid",cid);
        map.put("aids",aids);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = clueService.bound(map);

        PrintJson.printJsonFlag(resp,flag);

    }

    //查询市场活动信息列表（根据名称模糊查询+排除掉已经关联当前线索的市场活动）
    private void getActivityListByName(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("查询市场活动信息列表（根据名称模糊查询+排除掉已经关联当前线索的市场活动）");

        String activityName = req.getParameter("activityName");
        String clueId = req.getParameter("clueId");

        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activityList = as.getActivityListByName(map);

        PrintJson.printJsonObj(resp,activityList);

    }


    //进入到解除线索和市场活动关联操作
    private void unbound(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到解除线索和市场活动关联操作");

        String id = req.getParameter("id");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = clueService.unbound(id);

        PrintJson.printJsonFlag(resp,flag);

    }

    //根据线索id查询关联的市场活动列表
    private void getActivityListByClueId(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("根据线索id查询关联的市场活动列表");

        String clueId = req.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activityList = as.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(resp,activityList);

    }

    //进入到跳转到详细信息页操作
    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到跳转到详细信息页操作");

        String id = req.getParameter("id");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue clue = clueService.detail(id);

        req.setAttribute("c",clue);

        req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req,resp);

    }

    //分页查询
    private void pageList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到线索分页查询操作");

        String fullname = req.getParameter("fullname");
        String company = req.getParameter("company");
        String phone = req.getParameter("phone");
        String source = req.getParameter("source");
        String owner = req.getParameter("owner");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");
        String pageNoStr = req.getParameter("pageNo");
        String pageSizeStr = req.getParameter("pageSize");

        int pageNo = Integer.parseInt(pageNoStr);
        int pageSize = Integer.parseInt(pageSizeStr);

        //略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        PaginationVO<Clue> vo = clueService.pageList(map);

        PrintJson.printJsonObj(resp,vo);

    }

    //进入到保存线索操作
    private void save(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到保存线索操作");

        String id = UUIDUtil.getUUID();
        String fullname = req.getParameter("fullname");
        String appellation = req.getParameter("appellation");
        String owner = req.getParameter("owner");
        String company = req.getParameter("company");
        String job = req.getParameter("job");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String website = req.getParameter("website");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");
        String source = req.getParameter("source");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String address = req.getParameter("address");

        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.save(clue);

        PrintJson.printJsonFlag(resp,flag);


    }

    //获取用户信息列表
    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("获取用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = us.getUserList();

        PrintJson.printJsonObj(resp,userList);

    }


}
