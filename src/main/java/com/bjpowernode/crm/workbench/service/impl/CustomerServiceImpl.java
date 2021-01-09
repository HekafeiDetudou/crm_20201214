package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.entity.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.CustomerRemarkDao;
import com.bjpowernode.crm.workbench.entity.Activity;
import com.bjpowernode.crm.workbench.entity.Customer;
import com.bjpowernode.crm.workbench.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public List<String> getCustomerName(String name) {

        List<String>  stringList = customerDao.getCustomerName(name);

        return stringList;
    }

    @Override
    public boolean save(Customer customer) {

        boolean flag = true;
        int count = customerDao.save(customer);
        if (count != 1){
            //添加失败
            flag = false;
        }
        return flag;

    }

    @Override
    public PaginationVO<Customer> pageList(Map<String, Object> map) {

        //取得total
        int total = customerDao.getTotalByCondition(map);

        //取得dataList
        List<Customer> dataList = customerDao.getCustomerByCondition(map);

        //创建一个vo对象，将vo和dataList封装到vo中
        PaginationVO<Customer> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回
        return vo;

    }

    @Override
    public Customer detail(String id) {

        //拿到customer记录
        Customer customer = customerDao.getDetailById(id);

        return customer;

    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;

        //查询出需要删除的备注的数量
        int count = customerRemarkDao.getCountByAids(ids);

        //删除关联该市场活动的备注信息，返回受影响的条数（实际删除的条数）
        int effectCount = customerRemarkDao.delete(ids);
        if (count != effectCount){
            flag = false;
        }

        //删除该条市场活动的信息
        int count2 = customerDao.delete(ids);
        if (count2 != ids.length){
            flag = false;
        }

        return flag;

    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        //取uList
        List<User> userList = userDao.getUserList();

        //取c
        Customer customer = customerDao.getCustomerById(id);

        //将uList和c封装到map中
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uList",userList);
        map.put("c",customer);

        //返回map
        return map;

    }

    @Override
    public boolean updateCustomer(Customer customer) {

        boolean flag = true;
        int count = customerDao.updateCustomer(customer);
        if (count != 1){
            //添加失败
            flag = false;
        }
        return flag;

    }


}
