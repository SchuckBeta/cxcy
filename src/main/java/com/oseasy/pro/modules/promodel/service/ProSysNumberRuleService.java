package com.oseasy.pro.modules.promodel.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.pro.modules.project.dao.AppTypeDao;
import com.oseasy.sys.common.utils.Tree;
import com.oseasy.sys.modules.sys.service.SysNumberRuleService;
import com.oseasy.util.common.utils.exception.RunException;

/**
 * 编号规则管理Service.
 *
 * @author 李志超
 * @version 2018-05-17
 */
@Service
@Transactional(readOnly = false)
public class ProSysNumberRuleService{
    private static String APP_TYPE = "app_type"; //字典表中type为app_type数据
    @Autowired
    private AppTypeDao appTypeDao;

    @Transactional(readOnly = true)
    public List<Tree> getAppTypeTreeList() {

        try {
            //获取字典表中的APP_TYPE类别
            List<Dict> dictList = DictUtils.getDictListByType(APP_TYPE);
            List<Tree> trees = new ArrayList<>();

            //添加大类下的小类
            dictList.forEach(dict -> {
                Tree tree = new Tree();
                tree.setId(dict.getId());
                tree.setText(dict.getLabel());
                tree.setParentId("0");

                //获取字典表中value值：{"method":"getAppTypeList", "params":{"flowType":1}}
                JSONObject jsonObject = JSONObject.parseObject(dict.getValue());
                jsonObject.getJSONObject("params").put("parentId", dict.getId());

                //获取appTypeDao中的methods
                Method[] methods = appTypeDao.getClass().getDeclaredMethods();
                //遍历appTypeDao中的methods
                for (Method method : methods) {
                    //匹配value中method方法是否相同，相同则执行此方法
                    if (method.getName().equals((String) jsonObject.get("method"))) {
                        try {
                            List<Tree> childList = (List<Tree>) method.invoke(appTypeDao, jsonObject.getJSONObject("params"));
                            tree.setChildList(childList);
                            trees.add(tree);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            return trees;
        } catch (Exception e) {
            throw new RunException("查询应用列表数据失败，请联系管理员");
        }
    }
}