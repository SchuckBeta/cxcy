package com.oseasy.cms.modules.cms.entity;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.oseasy.cms.common.config.CmsIds;
import com.oseasy.cms.modules.cms.utils.CmsUtils;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.TreeEntity;

/**
 * 栏目管理Entity.
 * @author liangjie
 * @version 2018-08-30
 */
public class Category extends TreeEntity<Category> {
    public static final String DEFAULT_TEMPLATE = "frontList";
	private static final long serialVersionUID = 1L;
	private String parentId;		// 上级栏目
	private String parentIds;		// 所有父级编号
	private String name;		// 栏目名称
	private String module;		//栏目模块
	private Site site;		// 站点编号
    private String href;    // 访问路径
	private Integer isNewtab;		// 是否打开新窗口（0否1是）
	private Integer isShow;		// 显示（0否1是）
	private Integer sort;		// 排列顺序
	private String showModes;		// 展现方式（默认展现方式，首栏目内容列表，栏目第一条内容）
	private String contenttype;		// 内容类型(0图文1普通)
	private String allowComment;		// 是否允许评论（0否1是）
	private String isAudit;		// 是否需要审核（0否1是）
	private Integer isContentstatic;		// 内容静态化
	private String likes;		// 点赞量
	private String description;		// 描述
    private List<Category> childList = Lists.newArrayList();    // 拥有子分类列表
    private List<CmsIndexRegion> childRegionList = Lists.newArrayList();    // 拥有资源列表
    private String publishCategory; //定义栏目类型，以便定位栏目
    private Integer isSys;//系统栏目标识
    private Integer ltype;//栏目枚举


    public Category() {
        super();
        this.module = "";
        this.sort = 30;
        this.showModes = "0";
        this.allowComment = Const.NO;
        this.delFlag = DEL_FLAG_NORMAL;
        this.isAudit = Const.NO;
    }

    public Category(String id){
        super(id);
    }

    public Category(String id, Site site) {
        this();
        this.id = id;
        this.setSite(site);
    }

    public Integer getLtype() {
        return ltype;
    }

    public void setLtype(Integer ltype) {
        this.ltype = ltype;
    }

    public Integer getIsSys() {
        return isSys;
    }

    public void setIsSys(Integer isSys) {
        this.isSys = isSys;
    }

    public String getPublishCategory() {
        return publishCategory;
    }

    public void setPublishCategory(String publishCategory) {
        this.publishCategory = publishCategory;
    }

    public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Length(min=1, max=2000, message="所有父级编号长度必须介于 1 和 2000 之间")
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@Length(min=0, max=20, message="栏目名称长度必须介于 0 和 20 之间")
	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsNewtab() {
		return isNewtab;
	}

	public void setIsNewtab(Integer isNewtab) {
		this.isNewtab = isNewtab;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getContenttype() {
		return contenttype;
	}

	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}

	@Length(min=0, max=11, message="内容静态化长度必须介于 0 和 11 之间")
	public Integer getIsContentstatic() {
		return isContentstatic;
	}

	public void setIsContentstatic(Integer isContentstatic) {
		this.isContentstatic = isContentstatic;
	}

	@Length(min=1, max=11, message="点赞量长度必须介于 1 和 11 之间")
	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

//
//    private static final long serialVersionUID = 1L;
//    private Site site;      // 归属站点
//    private Office office;  // 归属部门
////  private Category parent;// 父级菜单
////  private String parentIds;// 所有父级编号
//    private String module;  // 栏目模型（article：文章；picture：图片；download：下载；link：链接；special：专题）
////  private String name;    // 栏目名称
//    private String image;   // 栏目图片
//    private String target;  // 目标（ _blank、_self、_parent、_top）
//    private String description;     // 描述，填写有助于搜索引擎优化
//    private String keywords;    // 关键字，填写有助于搜索引擎优化
////  private Integer sort;       // 排序（升序）
//    private String inMenu;      // 是否在导航中显示（1：显示；0：不显示）
//    private String inList;      // 是否在分类页中显示列表（1：显示；0：不显示）
//    private String showModes;   // 展现方式（0:有子栏目显示栏目列表，无子栏目显示内容列表;1：首栏目内容列表；2：栏目第一条内容）
//    private String allowComment;// 是否允许评论
//    private String isAudit; // 是否需要审核
//    private String customListView;      // 自定义列表视图
//    private String customContentView;   // 自定义内容视图
//    private String viewConfig;  // 视图参数
//
//    private Date beginDate; // 开始时间
//    private Date endDate;   // 结束时间
//    private String cnt;//信息量
//    private String hits;//点击量

    public Site getSite() {
        return site;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setSite(Site site) {
        this.site = site;
    }

//  @JsonBackReference
//  @NotNull
    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

//  @Length(min=1, max=255)
//  public String getParentIds() {
//      return parentIds;
//  }
//
//  public void setParentIds(String parentIds) {
//      this.parentIds = parentIds;
//  }

    @Length(min=0, max=20)
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

//  @Length(min=0, max=100)
//  public String getName() {
//      return name;
//  }
//
//  public void setName(String name) {
//      this.name = name;
//  }


    @Length(min=0, max=255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//  @NotNull
//  public Integer getSort() {
//      return sort;
//  }
//
//  public void setSort(Integer sort) {
//      this.sort = sort;
//  }

    @Length(min=1, max=1)
    public String getShowModes() {
        return showModes;
    }

    public void setShowModes(String showModes) {
        this.showModes = showModes;
    }

    @Length(min=1, max=1)
    public String getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(String allowComment) {
        this.allowComment = allowComment;
    }

    @Length(min=1, max=1)
    public String getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(String isAudit) {
        this.isAudit = isAudit;
    }

    public List<Category> getChildList() {
        return childList;
    }

    public void setChildList(List<Category> childList) {
        this.childList = childList;
    }

    public List<CmsIndexRegion> getChildRegionList() {
        return childRegionList;
    }

    public void setChildRegionList(List<CmsIndexRegion> childRegionList) {
        this.childRegionList = childRegionList;
    }

    public static void sortList(List<Category> list, List<Category> sourcelist, String parentId) {
        for (int i=0; i<sourcelist.size(); i++) {
            Category e = sourcelist.get(i);
            if (e.getParentId()!=null && e.getParentId().equals(parentId)) {
                list.add(e);
                // 判断是否还有子节点, 有则继续获取子节点
                for (int j=0; j<sourcelist.size(); j++) {
                    Category child = sourcelist.get(j);
                    if ( child.getParentId()!=null
                            && child.getParentId().equals(e.getId())) {
                        sortList(list, sourcelist, e.getId());
                        break;
                    }
                }
            }
        }
    }

//  public String getIds() {
//      return (this.getParentIds() !=null ? this.getParentIds().replaceAll(",", " ") : "")
//              + (this.getId() != null ? this.getId() : "");
//  }

    public boolean isRoot() {
        return isRoot(this.id);
    }

    /**
     * 是否栏目根节点
     * @param id
     * @return
     */
    public static boolean isRoot(String id) {
        return id != null && id.equals(CmsIds.SITE_CATEGORYS_SYS_ROOT.getId());
    }

    /**
     * 是否网站首页
     * @param id
     * @return
     */
    public static boolean isHome(String id) {
        return id != null && id.equals(CmsIds.SITE_CATEGORYS_TOP_ROOT.getId());
    }

    public String getUrl() {
        return CmsUtils.getUrlDynamic(this);
    }

    public static Category initByParentId(String parentId) {
        Category c = new Category();
        Category pc = new Category();
        pc.setId(parentId);
        c.setParent(pc);
        return c;
    }

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}