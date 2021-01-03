package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.entity.Customer;
import com.bjpowernode.crm.workbench.entity.Tran;
import com.bjpowernode.crm.workbench.entity.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    @Override
    public boolean save(Tran tran, String customerName) {

        System.out.println("进入到保存交易的操作");

        /*
        * tran中保存的数据，缺少一个字段：customerId
        *   1、根据customerName去customer表中查询，
        *       如果有，则返回id，保存到tran中
        *       如果没有，则新建一个客户，返回该客户的id，然后保存到tran中
        *
        *   2、添加交易完毕后，需要创建一条交易历史
        * */

        boolean flag = true;

        Customer customer = customerDao.getCustomerByName(customerName);

        if (customer == null){

            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(tran.getOwner());
            customer.setName(tran.getName());
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(tran.getCreateTime());
            customer.setContactSummary(tran.getContactSummary());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setDescription(tran.getDescription());

            //添加客户
            int count1 = customerDao.save(customer);
            if (count1 != 1){
                flag = false;
            }

        }

        //将客户的id封装进tran对象中，使得tran对象完整
        tran.setCustomerId(customer.getId());

        //添加交易
        int count2 = tranDao.save(tran);
        if (count2 != 1){
            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(tran.getId());
        th.setStage(tran.getStage());
        th.setMoney(tran.getMoney());
        th.setExpectedDate(tran.getExpectedDate());
        th.setCreateBy(tran.getCreateBy());
        th.setCreateTime(tran.getCreateTime());

        int count3 = tranHistoryDao.save(th);
        if (count3 != 1){
            flag = false;
        }

        return flag;

    }
}
