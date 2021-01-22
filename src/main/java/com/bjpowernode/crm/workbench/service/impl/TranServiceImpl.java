package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.dao.TranRemarkDao;
import com.bjpowernode.crm.workbench.entity.*;
import com.bjpowernode.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranRemarkDao tranRemarkDao = SqlSessionUtil.getSqlSession().getMapper(TranRemarkDao.class);
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
            customer.setName(customerName);
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

    @Override
    public PaginationVO<Tran> pageList(Map<String, Object> map) {
        //System.out.println("map集合中的数据"+map.get("name"));
        //取得total
        int total = tranDao.getTotalByCondition(map);
        //System.out.println(total);
        //取得dataList
        List<Tran> dataList = tranDao.getTranByCondition(map);

        //创建一个vo对象，将vo和dataList封装到vo中
        PaginationVO<Tran> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回
        return vo;
    }

    @Override
    public Tran detail(String id) {

        //取得id对应的Tran对象
        Tran tran = tranDao.getTranById(id);

        //返回tran
        return tran;

    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String id) {

        //取得tranid对应的交易历史列表
        List<TranHistory> tranHistoryList = tranHistoryDao.getHistoryListByTranId(id);

        //返回列表
        return tranHistoryList;

    }

    @Override
    public Map<String, Object> getCharts() {

        //取得total
        int total = tranDao.getTotal();

        //取得dataList
        List<Map<String,Object>> dataList = tranDao.getCharts();

        //将total和dataList保存到map中
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);

        //返回map
        return map;

    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;

        //查询出需要删除的备注的数量
        int count = tranRemarkDao.getCountByAids(ids);

        //删除关联该交易的备注信息，返回受影响的条数（实际删除的条数）
        int effectCount = tranRemarkDao.delete(ids);
        if (count != effectCount){
            flag = false;
        }

        //查询出需要删除的交易历史
        int count2 = tranHistoryDao.getCountByTids(ids);

        //删除相关的交易历史
        int effectCount2 = tranHistoryDao.delete(ids);
        if (count2 != effectCount2){
            flag = false;
        }

        //删除该条市场活动的信息
        int count3 = tranDao.delete(ids);
        if (count3 != ids.length){
            flag = false;
        }

        return flag;

    }

    @Override
    public List<TranRemark> getRemarkListByTranId(String tranId) {

        List<TranRemark> trList = tranRemarkDao.getRemarkListByTranId(tranId);

        return trList;

    }

    @Override
    public boolean saveReamrk(TranRemark tranRemark) {

        boolean flag = false;
        int count = tranRemarkDao.saveRemark(tranRemark);
        if (count == 1){
            flag = true;
        }

        return flag;

    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = false;

        int count = tranRemarkDao.deleteById(id);

        if (count == 1){
            flag = true;
        }

        return flag;

    }

    @Override
    public boolean updateReamrk(TranRemark tranRemark) {

        boolean flag = false;
        int count = tranRemarkDao.updateRemark(tranRemark);
        if (count == 1){
            flag = true;
        }

        return flag;

    }

    @Override
    public List<Tran> getTranListByCustomerId(String customerId) {

        List<Tran> tranList = tranDao.getTranListByCustomerId(customerId);

        return tranList;

    }

    @Override
    public List<Tran> getTranListByContactsId(String contactsId) {

        List<Tran> tranList = tranDao.getTranListByContactsId(contactsId);

        return tranList;

    }

    @Override
    public boolean edit(Tran tran, String customerName) {

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
            customer.setName(customerName);
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

        //修改交易
        int count2 = tranDao.update(tran);
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
        th.setCreateBy(tran.getEditBy());
        th.setCreateTime(tran.getEditTime());

        int count3 = tranHistoryDao.save(th);
        if (count3 != 1){
            flag = false;
        }

        return flag;

    }

    @Override
    public Tran detail2(String id) {

        //取得id对应的Tran对象
        Tran tran = tranDao.getACIdById(id);

        //返回tran
        return tran;

    }

    @Override
    public Map<String, Object> getCharts2() {

        //取得xLit
        List<String> xList = tranDao.getXList();

        //取得yList
        List<String> yList = tranDao.getYList();

        //将total和dataList保存到map中
        Map<String,Object> map = new HashMap<>();
        map.put("xList",xList);
        map.put("yList",yList);

        //返回map
        return map;

    }
}
