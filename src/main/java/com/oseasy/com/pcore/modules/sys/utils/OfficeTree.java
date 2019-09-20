package com.oseasy.com.pcore.modules.sys.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.LogDao;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by Administrator on 2018/12/28.
 */
public class OfficeTree {
    private static OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);

    public static List<Office> buildTree(List<Office> officeList){
        List<Office> list = Lists.newArrayList();
        Office officeRoot = officeService.getRoot();
        String fRoot = "";
        if(officeRoot != null){
            fRoot = officeRoot.getId();
        }
        for(Office office : officeList){

            if(fRoot.equals(office.getId())){
                list.add(office);
            }
            for(Office childArea : officeList){
                if(childArea.getParent().getId().equals(office.getId())){
                    if(office.getChildren() == null){
                        office.setChildren(new ArrayList<Office>());
                    }
                    office.getChildren().add(childArea);
                }
            }
        }
        return list;
    }

    public static List<Office> buildTree(List<Office> officeList, String root){
        if(StringUtil.isEmpty(root)){
            root = "0";
        }
        List<Office> list = Lists.newArrayList();
        for(Office office : officeList){

            if((root).equals(office.getParent().getId())){
                list.add(office);
            }
            for(Office childArea : officeList){
                if(childArea.getParent().getId().equals(office.getId())){
                    if(office.getChildren() == null){
                        office.setChildren(new ArrayList<Office>());
                    }
                    office.getChildren().add(childArea);
                }
            }
        }
        return list;
    }
}
