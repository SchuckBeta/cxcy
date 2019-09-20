/**
 * .
 */

package com.oseasy.pro.modules.promodel.vo;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.pro.modules.workflow.impl.SpiltPost;
import com.oseasy.pro.modules.workflow.impl.SpiltPref;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 导入导出功能参数传递Vo,.
 * @author chenhao
 *
 */
public class ItReqParam implements ExpRparam{
    /**
     * .
     */
    private static final String IS_MD_OLD = "isMdOld";
    public static final String HAS_FILE = "hasFile";//判断是否需要导入附件
    public static final String PRE_FIX = "prefix";//前缀分隔
    public static final String POST_FIX = "postfix";//后缀分隔
    private String actywId;//导入的actywId
    private Boolean hasFile;//是否有附件
    private String gnodeId;//导入的节点ID
    private String type;//类型参数（固定流程的节点标识，兼容旧的流程）
    private String tplType;//模板类型参数
    private String prefix;//导出分隔符
    private SpiltPref spre;//导出分隔符
    private String postfix;//导出分隔符
    private SpiltPost spost;//导出分隔符
    private Boolean isNew;//是否新版导入
    private Boolean isMdOld;//是否旧版本民大导入

    public ItReqParam() {
        super();
    }

    public ItReqParam(HttpServletRequest request) {
        String tplType = request.getParameter("tplType");
        this.tplType = StringUtil.isNotEmpty(tplType)?tplType:"";
        String type = request.getParameter(CoreJkey.JK_TYPE);
        this.type = StringUtil.isNotEmpty(type)?type:"";
        this.actywId = request.getParameter(ActYwGroup.JK_ACTYW_ID);
        this.gnodeId = request.getParameter(ActYwGroup.JK_GNODE_ID);
        SpiltPref spiltPref = SpiltPref.getByKey(request.getParameter(PRE_FIX));
        spiltPref = (spiltPref != null)?spiltPref:SpiltPref.getDef();
        this.spre = spiltPref;
        this.prefix = spiltPref.getRemark();

        SpiltPost spiltPost = SpiltPost.getByKey(request.getParameter(POST_FIX));
        spiltPost = (spiltPost != null)?spiltPost:SpiltPost.getDef();
        this.spost = spiltPost;
        this.postfix = spiltPost.getRemark();
        this.hasFile = (Const.YES).equals(request.getParameter(ItReqParam.HAS_FILE))?true:false;
        this.isNew = (Const.YES.equals((request.getParameter(CoreJkey.JK_IS_TRUE)))?true:false);
        this.isMdOld = (Const.YES.equals((request.getParameter(IS_MD_OLD)))?true:false);
    }

    public ItReqParam(HttpServletRequest request, String actywId) {
        this.actywId = actywId;
        String tplType = request.getParameter("tplType");
        this.tplType = StringUtil.isNotEmpty(tplType)?tplType:"";
        String type = request.getParameter(CoreJkey.JK_TYPE);
        this.type = StringUtil.isNotEmpty(type)?type:"";
        this.gnodeId = request.getParameter(ActYwGroup.JK_GNODE_ID);
        SpiltPref spiltPref = SpiltPref.getByKey(request.getParameter(PRE_FIX));
        spiltPref = (spiltPref != null)?spiltPref:SpiltPref.getDef();
        this.spre = spiltPref;
        this.prefix = spiltPref.getRemark();

        SpiltPost spiltPost = SpiltPost.getByKey(request.getParameter(POST_FIX));
        spiltPost = (spiltPost != null)?spiltPost:SpiltPost.getDef();
        this.spost = spiltPost;
        this.postfix = spiltPost.getRemark();
        this.hasFile = (Const.YES).equals(request.getParameter(ItReqParam.HAS_FILE))?true:false;
        this.isNew = (Const.YES.equals((request.getParameter(CoreJkey.JK_IS_TRUE)))?true:false);
        this.isMdOld = (Const.YES.equals((request.getParameter(IS_MD_OLD)))?true:false);
    }

    public ItReqParam(String gnodeId) {
        super();
        this.gnodeId = gnodeId;
        this.isNew = false;
    }
    public ItReqParam(String gnodeId, Boolean isImpFileData) {
        super();
        this.gnodeId = gnodeId;
        this.isNew = false;
    }
    public ItReqParam(String gnodeId, Boolean isImpFileData, boolean isNew) {
        super();
        this.gnodeId = gnodeId;
        this.isNew = isNew;
    }

    public String getActywId() {
        return actywId;
    }

    public void setActywId(String actywId) {
        this.actywId = actywId;
    }

    public SpiltPref getSpre() {
        return spre;
    }

    public void setSpre(SpiltPref spre) {
        this.spre = spre;
    }

    public SpiltPost getSpost() {
        return spost;
    }

    public void setSpost(SpiltPost spost) {
        this.spost = spost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTplType() {
        return tplType;
    }

    public void setTplType(String tplType) {
        this.tplType = tplType;
    }

    public Boolean getHasFile() {
        return hasFile;
    }

    public void setHasFile(Boolean hasFile) {
        this.hasFile = hasFile;
    }

    public String getGnodeId() {
        return gnodeId;
    }

    public void setGnodeId(String gnodeId) {
        this.gnodeId = gnodeId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Boolean getIsMdOld() {
        return isMdOld;
    }

    public void setIsMdOld(Boolean isMdOld) {
        this.isMdOld = isMdOld;
    }
}
