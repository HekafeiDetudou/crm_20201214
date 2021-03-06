package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.entity.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    User login(Map<String,String> map);

    List<User> getUserList();

    int updatePwd(User user);

    int checkLoginAct(String loginAct);

    int userAdd(User user);

}
