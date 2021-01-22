package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    int save(Activity activity);

    List<Activity> getActivityByCondition(Map<String,Object> map);

    int getTotalByCondition(Map<String,Object> map);

    int delete(String[] ids);

    Activity getActivityById(String id);

    int updateActivity(Activity activity);

    Activity getDetailById(String id);

    List<Activity> getActivityListByClueId(String clueId);


    List<Activity> getActivityListByName(Map<String, Object> map);

    List<Activity> getActivityListByNameJust(String activityName);

    List<String> getXList();

    List<String> getYList();

    List<Map<String, Object>> getCharts();

}
