package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.Contacts;

import java.util.List;

public interface ContactsDao {

    int save(Contacts contacts);

    List<Contacts> getActivityListByNameJust(String fullname);

}
