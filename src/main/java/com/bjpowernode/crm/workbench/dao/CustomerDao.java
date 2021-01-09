package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer);

    List<String> getCustomerName(String name);

    int getTotalByCondition(Map<String, Object> map);

    List<Customer> getCustomerByCondition(Map<String, Object> map);

    Customer getDetailById(String id);

    int delete(String[] ids);

    Customer getCustomerById(String id);

    int updateCustomer(Customer customer);

}
