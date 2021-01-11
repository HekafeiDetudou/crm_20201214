package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.ContactsActivityRelation;

public interface ContactsActivityRelationDao {

    int save(ContactsActivityRelation contactsActivityRelation);

    int getCountByAids(String[] ids);

    int delete(String[] ids);

}
