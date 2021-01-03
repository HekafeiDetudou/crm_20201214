package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.entity.Tran;

public interface TranService {

    boolean save(Tran tran, String customerName);

}
