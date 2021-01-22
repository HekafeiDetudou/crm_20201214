package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.entity.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        Map<String,String> map = new HashMap<String,String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user = userDao.login(map);
        if (user == null){

            throw new LoginException("账号密码错误！");
        }

        //程序执行到此处，说明账号密码是正确的，需要继续验证另外3项信息

        //验证失效信息
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)<0){

            //程序执行到此处，说明此账号已经过了的失效时间，不可再用
            throw new LoginException("此账号已经过了的失效时间,账号已失效！");

        }

        //判断锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)){

            //程序执行到此处，说明账号为锁定状态
            throw new LoginException("此账号已经锁定，无法使用！");
        }

        //判断ip地址
        String allowIps = user.getAllowIps();
        if (allowIps == null || "".equals(allowIps)){
            return user;
        }
        if (!allowIps.contains(ip)){

            //程序执行到此处，说明账号登录设备ip越界
            throw new LoginException("账号登录设备ip越界!");

        }

        return user;
    }

    @Override
    public List<User> getUserList() {

        List<User> userList = userDao.getUserList();

        return userList;
    }

    @Override
    public boolean updatePwd(User user) {

        boolean flag = true;
        int num = userDao.updatePwd(user);
        if (num != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean checkLoginAct(String loginAct) {

        boolean flag = false;
        int num = userDao.checkLoginAct(loginAct);
        if (num == 1){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean addUser(User user) {

        boolean flag = true;
        int num = userDao.userAdd(user);
        if (num != 1){
            flag = false;
        }
        return flag;
    }
}
