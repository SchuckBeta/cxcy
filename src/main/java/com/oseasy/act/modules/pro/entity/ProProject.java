package com.oseasy.act.modules.pro.entity;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.tool.apply.IConfig;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 创建项目Entity.
 *
 * @author zhangyao
 * @version 2017-06-15
 */
public class ProProject extends DataEntity<ProProject> implements IConfig{

    private static final long serialVersionUID = 1L;
    public static final String PRO_PROJECT = "proProject";
    protected String categoryRid;   //项目对应前台栏目根结点
    private String menuRid;   //项目对应后台菜单根结点
    @JsonIgnore
    private Menu menu;        //后台菜单
//    private Category category;        //前台栏目
    private String content;        // 内容
    private String projectName;        // 项目名称
    private String projectMark;        // 项目标识（英文）gcontest
    private String imgUrl;        // 项目图片
    private String state;   // 发布状态 1：是发布 0：是未发布
    private boolean restCategory;        //是否重置子栏目
    private boolean restMenu;        //是否重置子菜单

    private String proType;        //大项目 类型（双创，科研等）
    private String type;        //项目 类型（大创，互联网+大赛等）//项目类型：project_style；大赛类型：competition_type
    private String proCategory;        //项目类别：创新,创业//项目类别：project_type；大赛类别：competition_net_type
    private List<String> proCategorys;//类别
    private List<Dict> proCategoryds;//类别
    private String level;        //项目级别：project_degree；大赛级别：gcontest_level
    private List<String> levels;    //级别
    private List<Dict> levelds;    //级别
    private String finalStatus;        //项目状态：project_result；大赛状态：competition_college_prise
    private List<String> finalStatuss;        //结果状态
    private Date startDate;  // 开始日期
    private Date endDate;  // 结束日期
    private String year;  //项目年份

    private boolean nodeState;  // 开关日期（申报）1：生效 0：不生效
    private Date nodeStartDate;  // 节点开始日期（申报）
    private Date nodeEndDate;  // 节点结束日期（申报）

    public ProProject() {
        super();
    }

    public ProProject(String id) {
        super(id);
    }

    public String getProjectMark() {
        return projectMark;
    }

    public void setProjectMark(String projectMark) {
        this.projectMark = projectMark;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public List<Dict> getLevelds() {
        return levelds;
    }

    public void setLevelds(List<Dict> levelds) {
        this.levelds = levelds;
    }

    public List<Dict> getProCategoryds() {
        return proCategoryds;
    }

    public void setProCategoryds(List<Dict> proCategoryds) {
        this.proCategoryds = proCategoryds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProCategory() {
        return proCategory;
    }

    public void setProCategory(String proCategory) {
        this.proCategory = proCategory;
    }

    public void setCategoryRid(String categoryRid) {
        this.categoryRid = categoryRid;
    }

    public String getCategoryRid() {
        return this.categoryRid;
    }
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public boolean getNodeState() {
        return nodeState;
    }

    public void setNodeState(boolean nodeState) {
        this.nodeState = nodeState;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getNodeStartDate() {
        return nodeStartDate;
    }

    public void setNodeStartDate(Date nodeStartDate) {
        this.nodeStartDate = nodeStartDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getNodeEndDate() {
        return nodeEndDate;
    }

    public void setNodeEndDate(Date nodeEndDate) {
        this.nodeEndDate = nodeEndDate;
    }

    public List<String> getFinalStatuss() {
        if (StringUtils.isNotBlank(finalStatus)) {
            String[] finalStatusArray = StringUtils.split(finalStatus, StringUtil.DOTH);
            finalStatuss = Lists.newArrayList();
            for (String fStatus : finalStatusArray) {
                finalStatuss.add(fStatus);
            }
        }
        return finalStatuss;
    }

    public void setFinalStatuss(List<String> finalStatuss) {
        if ((finalStatuss != null) && (finalStatuss.size() > 0)) {
            StringBuffer strbuff = new StringBuffer();
            for (String fStatus : finalStatuss) {
                strbuff.append(fStatus);
                strbuff.append(StringUtil.DOTH);
            }
            String curfStatus = strbuff.substring(0, strbuff.lastIndexOf(StringUtil.DOTH));
            setFinalStatus(curfStatus);
        }
        this.finalStatuss = finalStatuss;
    }

    public List<String> getProCategorys() {
        if (StringUtils.isNotBlank(proCategory)) {
            String[] proCategorysArray = StringUtils.split(proCategory, StringUtil.DOTH);
            proCategorys = Lists.newArrayList();
            for (String pCategory : proCategorysArray) {
                proCategorys.add(pCategory);
            }
        }
        return proCategorys;
    }

    public void setProCategorys(List<String> proCategorys) {
        if ((proCategorys != null) && (proCategorys.size() > 0)) {
            StringBuffer strbuff = new StringBuffer();
            for (String pCategory : proCategorys) {
                strbuff.append(pCategory);
                strbuff.append(StringUtil.DOTH);
            }
            String curpCategory = strbuff.substring(0, strbuff.lastIndexOf(StringUtil.DOTH));
            setProCategory(curpCategory);
        }
        this.proCategorys = proCategorys;
    }

    public List<String> getLevels() {
        if (StringUtils.isNotBlank(level)) {
            String[] levelArray = StringUtils.split(level, StringUtil.DOTH);
            levels = Lists.newArrayList();
            for (String lv : levelArray) {
                levels.add(lv);
            }
        }
        return levels;
    }

    public void setLevels(List<String> levels) {
        if ((levels != null) && (levels.size() > 0)) {
            StringBuffer strbuff = new StringBuffer();
            for (String lv : levels) {
                strbuff.append(lv);
                strbuff.append(StringUtil.DOTH);
            }
            String curlv = strbuff.substring(0, strbuff.lastIndexOf(StringUtil.DOTH));
            setLevel(curlv);
        }
        this.levels = levels;
    }

    @Length(min = 0, max = 64, message = "项目名称长度必须介于 0 和 64 之间")
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Length(min = 0, max = 2, message = "发布状态 1：是发布 0：是未发布长度必须介于 0 和 2 之间")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonIgnore
    public String getMenuRid() {
        if (StringUtil.isEmpty(this.menuRid) && (this.menu != null)) {
            this.menuRid = this.menu.getId();
        }
        return menuRid;
    }

    public void setMenuRid(String menuRid) {
        this.menuRid = menuRid;
    }

    @Transient
    @JsonIgnore
    public Menu getMenu() {
        return menu;
    }

    @Transient
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Transient
    public String getImgUrl() {
        if (StringUtil.isEmpty(this.imgUrl) && (this.menu != null)) {
            this.imgUrl = this.menu.getImgUrl();
        }
        return imgUrl;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Transient
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Transient
    public boolean isRestCategory() {
        return restCategory;
    }

    @Transient
    public void setRestCategory(boolean restCategory) {
        this.restCategory = restCategory;
    }

    @Transient
    public boolean isRestMenu() {
        return restMenu;
    }

    @Transient
    public void setRestMenu(boolean restMenu) {
        this.restMenu = restMenu;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public String keyss() {
        return this.projectMark;
    }

    @Override
    public String ptype() {
        return this.type;
    }

    @Override
    public String ptype(String ptype) {
        this.type = ptype;
        return this.type;
    }
}