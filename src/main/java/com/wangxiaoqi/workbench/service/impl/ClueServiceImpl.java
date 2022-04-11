package com.wangxiaoqi.workbench.service.impl;

import com.wangxiaoqi.utils.DateTimeUtil;
import com.wangxiaoqi.utils.SqlSessionUtil;
import com.wangxiaoqi.utils.UUIDUtil;
import com.wangxiaoqi.vo.PaginationVo;
import com.wangxiaoqi.workbench.dao.*;
import com.wangxiaoqi.workbench.domain.*;
import com.wangxiaoqi.workbench.service.ClueService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClueServiceImpl implements ClueService {
    //线索相关的表
    ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    //客户相关的表
    CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    //联系人相关的表
    ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    //交易相关的表
    TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


    @Override
    public boolean save(Clue clue) {
        boolean flag = true;

        int count = clueDao.save(clue);

        if (count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVo<Clue> pageList(Map<String, Integer> map) {

        //获取总条数
        int total = clueDao.getTotal(map);

        //获取线索对象List
        List<Clue> clueList = clueDao.getClueList(map);

        PaginationVo<Clue> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(clueList);

        return vo;
    }

    @Override
    public Clue detail(String id) {

        Clue clue = clueDao.detail(id);

        return clue;
    }

    @Override
    public boolean unbund(String id) {

        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);

        if (count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {

        boolean flag = true;

        for (String aid : aids){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);

            int count = clueActivityRelationDao.bund(car);

            if (count != 1){
                flag = false;
            }
        }

        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran tran, String creatBy) {

        boolean flag = true;

        String creatTime = DateTimeUtil.getSysTime();

        //(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.getClueById(clueId);

        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);

        //如果customer为空，即客户不存在，需新建客户
        if (customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setDescription(clue.getDescription());
            customer.setCreateTime(creatTime);
            customer.setCreateBy(creatBy);
            customer.setContactSummary(clue.getContactSummary());

            //添加客户
            int count2 = customerDao.save(customer);
            if (count2!=1){
                flag = false;
            }
        }

        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateTime(creatTime);
        contacts.setCreateBy(creatBy);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setId(UUIDUtil.getUUID());

        //添加联系人
        int count3 = contactsDao.save(contacts);
        if (count3!=1){
            flag = false;
        }

        //(4) 线索备注转换到客户备注以及联系人备注
        //获取到线索备注
        List<ClueRemark> clueRemarkList = clueRemarkDao.getRemarkByClueId(clueId);
        //遍历线索备注
        for (ClueRemark clueRemark : clueRemarkList){
            //将线索里的备注转换到客户备注中
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(clueRemark.getNoteContent());
            customerRemark.setEditFlag("0");
            customerRemark.setCreateBy(creatTime);
            customerRemark.setCreateTime(clueRemark.getCreateTime());
            customerRemark.setCustomerId(customer.getId());

            int count4 = customerRemarkDao.save(customerRemark);
            if (count4!=1){
                flag = false;
            }


            //将线索里的备注转换到联系人备注中
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(clueRemark.getNoteContent());
            contactsRemark.setEditFlag("0");
            contactsRemark.setCreateTime(creatTime);
            contactsRemark.setCreateBy(creatBy);
            contactsRemark.setContactsId(contacts.getId());

            int count5 = contactsRemarkDao.save(contactsRemark);
            if (count5!=1){
                flag = false;
            }
        }

        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        //获取与当前线索关联的市场活动
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getActivityByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList){

            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
            contactsActivityRelation.setContactsId(contacts.getId());

            //添加联系人和市场活动关联表
            int count6 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count6!=1){
                flag = false;
            }
        }

        //(6) 如果有创建交易需求，创建一条交易
        if (tran!=null){
            tran.setOwner(clue.getOwner());
            tran.setSource(clue.getSource());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setCustomerId(customer.getId());
            tran.setContactSummary(clue.getContactSummary());
            tran.setContactsId(contacts.getId());

            int count7 = tranDao.save(tran);
            if (count7!=1){
                flag = false;
            }

            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(creatBy);
            tranHistory.setCreateTime(creatTime);
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setStage(tran.getStage());
            tranHistory.setTranId(tran.getId());

            int count8 = tranHistoryDao.save(tranHistory);
            if (count8!=1){
                flag = false;
            }
        }

        //(8) 删除线索备注
        for (ClueRemark clueRemark : clueRemarkList){
            int count9 = clueRemarkDao.delete(clueRemark);
            if (count9!=clueRemarkList.size()){
                flag = false;
            }
        }

        //(9) 删除线索和市场活动的关系
       int count10 = clueActivityRelationDao.delete(clueId);
        if (count10!=clueActivityRelationList.size()){
            flag = false;
        }

        //(10) 删除线索
        int count11 = clueDao.delete(clue);
        if (count11!=1){
            flag = false;
        }

        return flag;
    }


}
