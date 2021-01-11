package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.CustomerRemark;

import java.util.List;

public interface CustomerRemarkDao {

    int save(CustomerRemark customerRemark);

    int getCountByAids(String[] ids);

    int delete(String[] ids);

    int saveRemark(CustomerRemark customerRemark);

    List<CustomerRemark> getRemarkListByCustomerId(String customerId);

    int deleteById(String id);

    int updateRemark(CustomerRemark customerRemark);

}
