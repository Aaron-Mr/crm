package com.wangxiaoqi.workbench.dao;


import com.wangxiaoqi.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer);
}
