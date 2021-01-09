package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.entity.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    List<String> getCustomerName(String name);

    boolean save(Customer customer);

    PaginationVO<Customer> pageList(Map<String, Object> map);

    Customer detail(String id);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean updateCustomer(Customer customer);
}
