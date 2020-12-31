package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.entity.Contacts;

import java.util.List;

public interface ContactsService {

    List<Contacts> getContactListByName(String fullname);
}
