package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer);

}
