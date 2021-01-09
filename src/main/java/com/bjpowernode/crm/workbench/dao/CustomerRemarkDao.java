package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.CustomerRemark;

public interface CustomerRemarkDao {

    int save(CustomerRemark customerRemark);

    int getCountByAids(String[] ids);

    int delete(String[] ids);

}
