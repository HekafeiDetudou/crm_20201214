package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {


    int getCountByAids(String[] ids);

    int delete(String[] ids);

    List<ActivityRemark> getRemarkListByActivityId(String activityId);

    int deleteById(String id);

    int saveRemark(ActivityRemark activityRemark);

    int updateRemark(ActivityRemark ar);

}
