package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.entity.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.entity.Activity;
import com.bjpowernode.crm.workbench.entity.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public boolean save(Activity activity) {

        boolean flag = true;
        int count = activityDao.save(activity);
        if (count != 1){
            //添加失败
            flag = false;
        }
        return flag;

    }

    @Override
    public PaginationVO<Activity> pageList(Map<String,Object> map) {

        System.out.println("map集合中的数据"+map.get("name"));
        //取得total
        int total = activityDao.getTotalByCondition(map);

        //取得dataList
        List<Activity> dataList = activityDao.getActivityByCondition(map);

        //创建一个vo对象，将vo和dataList封装到vo中
        PaginationVO<Activity> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回
        return vo;

    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;

        //查询出需要删除的备注的数量
        int count = activityRemarkDao.getCountByAids(ids);

        //删除关联该市场活动的备注信息，返回受影响的条数（实际删除的条数）
        int effectCount = activityRemarkDao.delete(ids);
        if (count != effectCount){
            flag = false;
        }

        //删除该条市场活动的信息
        int count2 = activityDao.delete(ids);
        if (count2 != ids.length){
            flag = false;
        }

        return flag;

    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        //取uList
        List<User> userList = userDao.getUserList();

        //取a
        Activity activity = activityDao.getActivityById(id);

        //将uList和a封装到map中
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uList",userList);
        map.put("a",activity);

        //返回map
        return map;

    }

    @Override
    public boolean updateActivity(Activity activity) {

        boolean flag = true;
        int count = activityDao.updateActivity(activity);
        if (count != 1){
            //添加失败
            flag = false;
        }
        return flag;

    }

    @Override
    public Activity detail(String id) {

        //拿到activity记录
        Activity activity = activityDao.getDetailById(id);

        return activity;

    }

    @Override
    public List<ActivityRemark> getRemarkListByActivityId(String activityId) {

        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByActivityId(activityId);

        System.out.println(arList);

        return arList;

    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = false;

        int count = activityRemarkDao.deleteById(id);

        if (count == 1){
            flag = true;
        }

        return flag;

    }

    @Override
    public boolean saveReamrk(ActivityRemark activityRemark) {

        boolean flag = false;
        int count = activityRemarkDao.saveRemark(activityRemark);
        if (count == 1){
            flag = true;
        }

        return flag;

    }

    @Override
    public boolean updateReamrk(ActivityRemark ar) {

        boolean flag = false;
        int count = activityRemarkDao.updateRemark(ar);
        if (count == 1){
            flag = true;
        }

        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {

        List<Activity> activityList = activityDao.getActivityListByClueId(clueId);

        return activityList;

    }

    @Override
    public List<Activity> getActivityListByName(Map<String, Object> map) {

        List<Activity> activityList = activityDao.getActivityListByName(map);

        return activityList;

    }

    @Override
    public List<Activity> getActivityListByNameJust(String activityName) {

        List<Activity> activityList = activityDao.getActivityListByNameJust(activityName);

        return activityList;

    }


}
