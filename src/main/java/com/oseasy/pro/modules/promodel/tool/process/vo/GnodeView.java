package com.oseasy.pro.modules.promodel.tool.process.vo;

import java.util.List;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.MenuDao;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYwGassign;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.act.modules.actyw.vo.EarAtype;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程执行状态实体.
 *
 * @author chenhao
 */
public class GnodeView extends ActYwGnode {
    private static CoreService coreService = SpringContextHolder.getBean(CoreService.class);
    private static final long serialVersionUID = 1L;
    protected static Logger logger = LoggerFactory.getLogger(GnodeView.class);
    public static final int GV_NO_START = 0;// 0未开始/1执行中/2已执行结束
    public static final int GV_RUNNING = 1;// 0未开始/1执行中/2已执行结束
    public static final int GV_END = 2;// 0未开始/1执行中/2已执行结束
    private Integer rstatus; // 0未开始/1执行中/2已执行结束
    private Boolean isFront; // 是否前台节点
    private Boolean isShow; // 是否前台节点
    private List<User> users; // 审核用户

    private List<User> assignUsers; // 指派、委派操作用户，对应ActYwGassign.getAssignUser()
    private Boolean isHasDelegate; // 是否处理了委派
    private List<User> delegateUsers; // 被委派用户，对应ActYwGassign.getRevUser()
    private Boolean isHasRev; // 是否处理了指派
    private List<User> revUsers; //  被指派用户，对应ActYwGassign.getRevUser()
    private Boolean isFteacher; // 是否为第一导师
    private String fteacher; // 第一导师ID

    public GnodeView() {
        super();
    }

    public GnodeView(ActYwGnode gnode) {
        super(gnode);
        this.rstatus = GV_NO_START;
        this.isFront = false;
        this.isShow = true;
        this.isHasDelegate = false;
        this.isHasRev = false;
        this.isFteacher = false;
    }

    public GnodeView(ActYwGnode gnode, Boolean isAll) {
        super(gnode, isAll);
        this.rstatus = GV_NO_START;
        this.isFront = false;
        this.isShow = true;
        this.isHasDelegate = false;
        this.isHasRev = false;
        this.isFteacher = false;
    }
    public GnodeView(ActYwGnode gnode, Boolean isAll, String fteacher) {
        super(gnode, isAll);
        this.rstatus = GV_NO_START;
        this.isFront = false;
        this.isHasDelegate = false;
        this.isHasRev = false;
        this.isShow = true;
        if(StringUtil.isNotEmpty(fteacher)){
            this.isFteacher = true;
            this.fteacher = fteacher;
        }else{
            this.isFteacher = false;
        }
    }

    public GnodeView(ActYwGnode gnode, Integer rstatus, Boolean isFront) {
        super(gnode);
        this.rstatus = rstatus;
        this.isFront = isFront;
        this.isFteacher = false;
        this.isHasDelegate = false;
        this.isHasRev = false;
    }

    public GnodeView(ActYwGnode gnode, Integer rstatus, Boolean isFront, Boolean isShow) {
        super(gnode);
        this.rstatus = rstatus;
        this.isFront = isFront;
        this.isShow = isShow;
        this.isFteacher = false;
        this.isHasDelegate = false;
        this.isHasRev = false;
    }

    public Integer getRstatus() {
        if (rstatus == null) {
            rstatus = GV_NO_START;
        }
        return rstatus;
    }

    public Boolean getIsFront() {
        return isFront;
    }

    public Boolean getIsHasRev() {
        return isHasRev;
    }

    public void setIsHasRev(Boolean isHasRev) {
        this.isHasRev = isHasRev;
    }

    public Boolean getIsHasDelegate() {
        return isHasDelegate;
    }

    public void setIsHasDelegate(Boolean isHasDelegate) {
        this.isHasDelegate = isHasDelegate;
    }

    public List<User> getDelegateUsers() {
        if(StringUtil.checkEmpty(this.delegateUsers)){
            this.delegateUsers = Lists.newArrayList();
        }
        return delegateUsers;
    }

    public void setDelegateUsers(List<User> delegateUsers) {
        this.delegateUsers = delegateUsers;
    }

    public List<User> getAssignUsers() {
        if(StringUtil.checkEmpty(this.assignUsers)){
            this.assignUsers = Lists.newArrayList();
        }
        return assignUsers;
    }

    public void setAssignUsers(List<User> assignUsers) {
        this.assignUsers = assignUsers;
    }

    public List<User> getRevUsers() {
        if(StringUtil.checkEmpty(this.revUsers)){
            this.revUsers = Lists.newArrayList();
        }
        return this.revUsers;
    }

    public void setRevUsers(List<User> revUsers) {
        this.revUsers = revUsers;
    }

    public Boolean getIsShow() {
        if (isShow == null) {
            isShow = true;
        }
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public void setIsFront(Boolean isFront) {
        this.isFront = isFront;
    }

    public Boolean getIsFteacher() {
        return isFteacher;
    }

    public void setIsFteacher(Boolean isFteacher) {
        this.isFteacher = isFteacher;
    }

    public String getFteacher() {
        if (StringUtil.isNotEmpty(this.fteacher)) {
            this.isFteacher = true;
        }
        return fteacher;
    }

    public void setFteacher(String fteacher) {
        this.fteacher = fteacher;
        if (StringUtil.isNotEmpty(this.fteacher)) {
            this.isFteacher = true;
        }
    }

    public void setRstatus(Integer rstatus) {
        this.rstatus = rstatus;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * gnodes转换为GnodeView对象. 处理前后台属性 isFront 处理前显示属性 isShow 处理状态属性 rstatus
     * 注意：gnodes必须有序，没有当前结点定位到开始节点。
     * @param gnodes
     *            节点列表
     * @param curGnode
     *            当前执行节点（id和prentId,level必填）
     * @return List
     */
    public static List<GnodeView> convertsGview(List<ActYwGnode> gnodes, ActYwGnode curGnode, List<ActYwGassign> gassigns, String fteacher) {
        List<GnodeView> gvs = Lists.newArrayList();
        Boolean hasCurGnode = true;

        if ((curGnode == null) || StringUtil.isEmpty(curGnode.getId()) || StringUtil.isEmpty(curGnode.getType())
                || (curGnode.getLevel() == null) || (curGnode.getParent() == null)
                || ((!(curGnode.getParent().getId()).equals(CoreIds.NCE_SYS_TREE_ROOT.getId())) && (curGnode.getParent().getLevel() == null))) {
            hasCurGnode = false;
        }
        /**
         * 在列表中找出开始节点的位置.
         */
        for (ActYwGnode gnode : gnodes) {
            GnodeView gview = new GnodeView(gnode, true, fteacher);
            if(StringUtil.checkNotEmpty(gassigns) && (gview.getIsDelegate() || gview.getIsAssign())){
                for (ActYwGassign gassign : gassigns) {
                    if(!(gview.getId()).equals(gassign.getGnodeId())){
                        continue;
                    }
                    //注意，指派用户和被指派用户互换
                    if((EarAtype.ZP.getKey()).equals(gassign.getType())){
                        gview.setIsHasRev(true);
                        gview.getRevUsers().add(gassign.getRevUser());
                        gview.getAssignUsers().add(gassign.getAssignUser());
                    }else if((EarAtype.WP.getKey()).equals(gassign.getType())){
                        gview.setIsHasDelegate(true);
                        gview.getDelegateUsers().add(gassign.getRevUser());
                        gview.getAssignUsers().add(gassign.getAssignUser());
                    }else{
                        gview.setIsHasRev(false);
                        gview.setIsHasDelegate(false);
                    }
                }
            }

            if (StringUtil.checkNotEmpty(gnode.getGroles())) {
                String roleIds = StringUtil.listIdToStr(gnode.getGroles());
                if (roleIds!= null &&(!roleIds.equals("null")) && StringUtil.isNotEmpty(roleIds)){
                    if ((roleIds).contains(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()).getId())) {
                        gview.setIsFront(true);
                    }
                }

            }

            /**
             * 没有当前结点，定位为开始节点.
             */
            if (!hasCurGnode) {
                if ((GnodeType.GT_ROOT_START.getId()).equals(gview.getType())) {
                    gview.setRstatus(GV_RUNNING);
                } else {
                    gview.setRstatus(GV_NO_START);
                }
                gvs.add(gview);
                continue;
            }

            if ((GnodeType.getIdByRoot()).contains(gview.getType()) && (GnodeType.getIdByRoot()).contains(curGnode.getType())) {
                if (curGnode.getLevel() > gview.getLevel()) {
                    gview.setRstatus(GV_END);
                } else if ((curGnode.getLevel() == gview.getLevel())) {
                    if ((curGnode.getId()).equals(gview.getId())) {
                        gview.setRstatus(GV_RUNNING);
                    } else {
                        gview.setRstatus(GV_NO_START);
                        gview.setIsShow(false);
                    }
                } else {
                    gview.setRstatus(GV_NO_START);
                }
            } else if ((GnodeType.getIdByRoot()).contains(gview.getType()) && (GnodeType.getIdByProcess()).contains(curGnode.getType())) {
                if (curGnode.getParent().getLevel() > gview.getLevel()) {
                    gview.setRstatus(GV_END);
                } else if (curGnode.getParent().getLevel() == gview.getLevel()) {
                    if ((curGnode.getParent().getId()).equals(gview.getId())) {
                        gview.setRstatus(GV_RUNNING);//子流程定位去掉
                    } else {
                        gview.setRstatus(GV_NO_START);
                        gview.setIsShow(false);
                    }
                } else {
                    gview.setRstatus(GV_NO_START);
                }
            } else if ((GnodeType.getIdByProcess()).contains(gview.getType()) && (GnodeType.getIdByRoot()).contains(curGnode.getType())) {
                if (curGnode.getLevel() > gview.getParent().getLevel()) {
                    gview.setRstatus(GV_END);
                } else if (curGnode.getLevel() == gview.getParent().getLevel()) {
//                    if ((curGnode.getId()).equals(gview.getId())) {
//                        gview.setRstatus(GV_RUNNING);//子流程定位去掉
//                    } else {
//                        gview.setRstatus(GV_NO_START);
//                        gview.setIsShow(false);
//                    }
                } else {
                    gview.setRstatus(GV_NO_START);
                }
            } else if ((GnodeType.getIdByProcess()).contains(gview.getType()) && (GnodeType.getIdByProcess()).contains(curGnode.getType())) {
                if (curGnode.getLevel() > gview.getLevel()) {
                    gview.setRstatus(GV_END);
                } else if ((curGnode.getLevel() == gview.getLevel())) {
                    if ((curGnode.getId()).equals(gview.getId())) {
                        gview.setRstatus(GV_RUNNING);
                    } else {
                        gview.setRstatus(GV_NO_START);
                        gview.setIsShow(false);
                    }
                } else {
                    gview.setRstatus(GV_NO_START);
                }
            } else {
                logger.warn("节点类型错误或为空！");
            }
            gvs.add(gview);
        }
        return gvs;
    }
}