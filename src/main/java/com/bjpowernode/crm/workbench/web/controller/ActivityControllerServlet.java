package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.entity.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.*;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.entity.Activity;
import com.bjpowernode.crm.workbench.entity.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivityControllerServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到市场活动控制器");

        String path = req.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)){

            getUserlist(req,resp);

        }else if ("/workbench/activity/save.do".equals(path)){

            save(req,resp);

        }else if ("/workbench/activity/pageList.do".equals(path)){

            pageList(req,resp);

        }else if ("/workbench/activity/delete.do".equals(path)){

            delete(req,resp);

        }else if ("/workbench/activity/getUserListAndActivity.do".equals(path)){

            getUserListAndActivity(req,resp);

        }else if ("/workbench/activity/updateActivity.do".equals(path)){

            updateActivity(req,resp);

        }else if ("/workbench/activity/detail.do".equals(path)){

            detail(req,resp);

        }else if ("/workbench/activity/getRemarkListByActivityId.do".equals(path)){

            getRemarkListByActivityId(req,resp);

        }else if ("/workbench/activity/deleteRemark.do".equals(path)){

            deleteRemark(req,resp);

        }else if ("/workbench/activity/saveRemark.do".equals(path)){

            saveRemark(req,resp);

        }else if ("/workbench/activity/updateRemark.do".equals(path)){

            updateRemark(req,resp);

        }else if ("/workbench/activity/getCharts2.do".equals(path)){

            getCharts2(req,resp);

        }else if ("/workbench/activity/getCharts.do".equals(path)){

            getCharts(req,resp);

        }

    }

    //获取饼图的图表数据
    private void getCharts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("获取饼图的图表数据");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Map<String,Object>> mapList = activityService.getCharts();

        PrintJson.printJsonObj(response,mapList);

    }

    //获取折线图的图表数据
    private void getCharts2(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("获取折线图的图表数据");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*
         * 业务层为我们返回
         *       xList
         *       yList
         * */
        Map<String,Object> map = activityService.getCharts2();

        PrintJson.printJsonObj(response,map);

    }

    private void updateRemark(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到备注的修改操作");

        String noteContent = req.getParameter("noteContent");
        String id = req.getParameter("id");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark ar = new ActivityRemark();
        ar.setNoteContent(noteContent);
        ar.setId(id);
        ar.setEditFlag(editFlag);
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.updateReamrk(ar);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("activityRemark",ar);

        PrintJson.printJsonObj(resp,map);

    }

    private void saveRemark(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到保存备注信息操作");

        String noteContent = req.getParameter("noteContent");
        String activityId = req.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setActivityId(activityId);
        activityRemark.setCreateBy(createBy);
        activityRemark.setCreateTime(createTime);
        activityRemark.setEditFlag(editFlag);
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.saveReamrk(activityRemark);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("activityRemark",activityRemark);

        PrintJson.printJsonObj(resp,map);


    }

    //执行备注的删除操作
    private void deleteRemark(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行备注的删除操作");

        String id = req.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.deleteRemark(id);

        PrintJson.printJsonFlag(resp,flag);

    }

    //根据市场活动的id取得备注信息列表
    private void getRemarkListByActivityId(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("根据市场活动的id取得备注信息列表");

        String activityId = req.getParameter("activityId");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> arList = activityService.getRemarkListByActivityId(activityId);

        PrintJson.printJsonObj(resp,arList);

    }

    //进入到跳转到详细信息页操作
    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到跳转到详细信息页操作");

        String id = req.getParameter("id");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity a = activityService.detail(id);

        req.setAttribute("a",a);

        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req,resp);

    }

    //执行市场活动的修改操作
    private void updateActivity(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行市场活动的修改操作");

        //获取到前端传过来的数据
        String id = req.getParameter("id");
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        //修改时间:当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人：当前登录用户
        String editBy = ((User)req.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);

        //调用service执行更新操作，返回值为boolean类型
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.updateActivity(activity);

        //将数据转换成json格式
        PrintJson.printJsonFlag(resp,flag);

    }

    //获取用户信息列表和根据市场活动id查询1条市场活动记录
    private void getUserListAndActivity(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("获取用户信息列表和根据市场活动id查询1条市场活动记录");

        String id = req.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*
                总结：
                    controller调用service方法，返回值应该是什么，
                    要看一下前端需要什么，就从service层拿什么

                前端需要的向业务层去要
                    uList-->用户信息列表
                    a-->activity对象

                以上两项信息，复用率不高，所以使用map进行打包，然后进行传值
        * */
        Map<String,Object> map = as.getUserListAndActivity(id);

        PrintJson.printJsonObj(resp,map);

    }

    //执行市场活动的删除操作
    private void delete(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行市场活动的删除操作");

        //接收id数组（param）  http://8080....id=dsdshdishdisdhs&id=diushdishdissdsd23
        String[] ids = req.getParameterValues("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.delete(ids);
        System.out.println(flag);
        PrintJson.printJsonFlag(resp,flag);

    }

    //分页查询市场互动信息
    private void pageList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到查询市场活动信息列表的操作（结合分页查询+条件查询）");

        String pageNoStr = req.getParameter("pageNo");
        String pageSizeStr = req.getParameter("pageSize");
        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");

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
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PaginationVO<Activity> vo = activityService.pageList(map);
        //System.out.println(vo);
        //vo-->{"total":100,"dataList":[{市场活动1},{市场活动2},{市场活动3},{市场活动4}...]}
        PrintJson.printJsonObj(resp,vo);

    }

    //保存市场活动信息
    private void save(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行市场活动的添加操作");

        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        //创建时间:当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy = ((User)req.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.save(activity);

        PrintJson.printJsonFlag(resp,flag);

    }

    //获取用户列表
    private void getUserlist(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("取得用户信息列表");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();

        //将集合转换成json数组的格式
        PrintJson.printJsonObj(resp,userList);

    }

}
