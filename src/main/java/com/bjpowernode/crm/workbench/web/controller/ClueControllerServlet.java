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
import com.bjpowernode.crm.workbench.entity.Clue;
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

        }

    }

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
