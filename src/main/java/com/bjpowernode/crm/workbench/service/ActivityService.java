package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.entity.Activity;
import com.bjpowernode.crm.workbench.entity.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity activity);

    PaginationVO<Activity> pageList(Map<String,Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean updateActivity(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByActivityId(String activityId);

    boolean deleteRemark(String id);


    boolean saveReamrk(ActivityRemark activityRemark);

    boolean updateReamrk(ActivityRemark ar);


    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByName(Map<String, Object> map);


}
