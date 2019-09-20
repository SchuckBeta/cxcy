package com.oseasy.act.modules.actyw.entity;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.tool.apply.IForm;
import com.oseasy.act.modules.actyw.tool.apply.IFrorm;
import com.oseasy.act.modules.actyw.tool.apply.IGnode;
import com.oseasy.act.modules.actyw.tool.process.vo.FormClientType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormStyleType;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 节点表单Entity.
 * @author chenh
 * @version 2018-01-15
 */
public class ActYwGform extends DataEntity<ActYwGform> implements IFrorm{

    private static final long serialVersionUID = 1L;
    private ActYwGroup group;       // 流程编号
    private ActYwGnode gnode;       // 流程节点编号
    private ActYwForm form;      // 表单编号
    private Boolean isDelegate;     //委派:0、默认（否）；1、是

    public ActYwGform() {
        super();
        this.isDelegate = false;
    }

    public ActYwGform(ActYwGnode gnode) {
        super();
        this.gnode = gnode;
        this.isDelegate = false;
    }
    public ActYwGform(ActYwGroup group, ActYwGnode gnode) {
        super();
        this.group = group;
        this.gnode = gnode;
        this.isDelegate = false;
    }

    public ActYwGform(String groupId, String gnodeId, String formId) {
        super();
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.form = new ActYwForm(formId);
        this.isDelegate = false;
    }

    public ActYwGform(String id, String groupId, String gnodeId, String formId) {
        super();
        this.id = id;
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.form = new ActYwForm(formId);
        this.isDelegate = false;
    }

    public ActYwGform(String id, ActYwGroup group, String gnodeId, String formId) {
        super();
        this.id = id;
        this.group = group;
        this.gnode = new ActYwGnode(gnodeId);
        this.form = new ActYwForm(formId);
        this.isDelegate = false;
    }
    public ActYwGform(String id, ActYwGroup group, String gnodeId, String formId, Boolean isDelegate) {
        super();
        this.id = id;
        this.group = group;
        this.gnode = new ActYwGnode(gnodeId);
        this.form = new ActYwForm(formId);
        this.isDelegate = isDelegate;
    }

    public ActYwGform(String groupId, String gnodeId, ActYwForm form) {
        super();
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.form = form;
        this.isDelegate = false;
    }

    public ActYwGform(String id, String groupId, String gnodeId, ActYwForm form) {
        super();
        this.id = id;
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.form = form;
        this.isDelegate = false;
    }

    public ActYwGform(String id, ActYwGroup group, String gnodeId, ActYwForm form) {
        super();
        this.id = id;
        this.group = group;
        this.gnode = new ActYwGnode(gnodeId);
        this.form = form;
        this.isDelegate = false;
    }

    public ActYwGform(ActYwGroup group, ActYwGnode gnode, ActYwForm form) {
        super();
        this.group = group;
        this.gnode = gnode;
        this.form = form;
        this.isDelegate = false;
    }

    public Boolean getIsDelegate() {
        return isDelegate;
    }

    public void setIsDelegate(Boolean isDelegate) {
        this.isDelegate = isDelegate;
    }

    public ActYwGroup getGroup() {
        return group;
    }

    public void setGroup(ActYwGroup group) {
        this.group = group;
    }

    public ActYwGnode getGnode() {
        return gnode;
    }

    public void setGnode(ActYwGnode gnode) {
        this.gnode = gnode;
    }

    public ActYwForm getForm() {
        return form;
    }

    public void setForm(ActYwForm form) {
        this.form = form;
    }

    /**
     * IDS转换为节点表单对象.
     * @param groupId 流程ID
     * @param gnodeId 节点ID
     * @param ids ID串
     * @return List
     */
    public static List<ActYwGform> converts(ActYwGroup group, String gnodeId, List<ActYwForm> pids) {
        List<ActYwGform> statuss = Lists.newArrayList();
        for (ActYwForm pid : pids) {
            statuss.add(new ActYwGform(IdGen.uuid(), group, gnodeId, pid.getId()));
        }
        return statuss;
    }
    public static List<ActYwGform> convertDfs(ActYwGroup group, String gnodeId, List<String> pids) {
        List<ActYwGform> statuss = Lists.newArrayList();
        for (String pid : pids) {
            statuss.add(new ActYwGform(IdGen.uuid(), group, gnodeId, pid, true));
        }
        return statuss;
    }

    /**
     * IDS转换为节点表单对象.
     * @param groupId 流程ID
     * @param gnodeId 节点ID
     * @param ids ID串
     * @return List
     */
    public static List<ActYwGform> convert(String groupId, String gnodeId, List<String> ids) {
        List<ActYwGform> statuss = Lists.newArrayList();
        for (String id : ids) {
            statuss.add(new ActYwGform(IdGen.uuid(), groupId, gnodeId, id));
        }
        return statuss;
    }

    /**
     * 检查当前结点是否为前台节点.
     * @param curGnode 节点
     * @return Boolean
     */
    public static Boolean checkFront(Boolean isFront, ActYwGnode curGnode) {
        if((curGnode == null) || StringUtil.checkEmpty(curGnode.getGforms())){
            return isFront;
        }

        for (ActYwGform curGform : curGnode.getGforms()) {
            if(curGform.getForm() == null){
                continue;
            }

            if((FormClientType.FST_FRONT.getKey()).equals(curGform.getForm().getClientType())){
                isFront=true;
            }
        }
        return isFront;
    }

    public static String checkFront(String formId,Boolean isFront, ActYwGnode curGnode) {
           if((curGnode == null) || StringUtil.checkEmpty(curGnode.getGforms())){
               return formId;
           }

           for (ActYwGform curGform : curGnode.getGforms()) {
               if(curGform.getForm() == null){
                   continue;
               }

               if((FormClientType.FST_FRONT.getKey()).equals(curGform.getForm().getClientType())){
                   formId=curGform.getForm().getId();
               }
           }
           return formId;
       }
    /**
     * 检查当前结点是否为前台节点.
     * @param gnodes 节点
     * @return Boolean
     */
    public static Boolean checkFront(Boolean isFront, List<ActYwGnode> gnodes) {
        for (ActYwGnode curGnode : gnodes) {
            if(ActYwGform.checkFront(isFront, curGnode)){
                isFront = true;
                break;
            }
        }
        return isFront;
    }

    public static String checkFront(String formId,Boolean isFront, List<ActYwGnode> gnodes) {
           for (ActYwGnode curGnode : gnodes) {
               if(ActYwGform.checkFront(formId,isFront, curGnode)!=null){
                   break;
               }
           }
           return formId;
       }
    /**
     * 检查当前结点是否为后台列表表单ID.
     * @param formId 表单Id
     * @param curGnode 节点
     * @return Boolean
     */
    public static String checkAdminList(ActYwGnode curGnode) {
        String formId = null;
        if((curGnode == null) || StringUtil.checkEmpty(curGnode.getGforms())){
            return formId;
        }

        for (ActYwGform curGform : curGnode.getGforms()) {
            if(curGform.getForm() == null){
                continue;
            }

            if((FormClientType.FST_ADMIN.getKey()).equals(curGform.getForm().getClientType()) && (FormStyleType.FST_LIST.getKey()).equals(curGform.getForm().getStyleType())){
                formId = curGform.getForm().getId();
            }
        }
        return formId;
    }

    /**
     * 检查当前结点是否为后台列表表单ID.
     * @param formId 表单Id
     * @param gnodes 节点
     * @return Boolean
     */
    public static String checkAdminList(String formId, List<ActYwGnode> gnodes) {
        for (ActYwGnode curGnode : gnodes) {
            formId = ActYwGform.checkAdminList(curGnode);
            if(StringUtil.isNotEmpty(formId)){
                break;
            }
        }
        return formId;
    }

    @Override
    public IGnode ignode() {
        return this.gnode;
    }

    @Override
    public IForm iform() {
        return this.form;
    }
}