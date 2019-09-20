package com.oseasy.pie.modules.iep.entity;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.persistence.TreeEntity;
import com.oseasy.util.common.utils.IidEntity;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 模板导入导出Entity.
 * @author chenhao
 * @version 2019-02-14
 */
public class IepTpl extends TreeEntity<IepTpl> implements IidEntity{

	private static final long serialVersionUID = 1L;
	public static final String IEPTPL = "iepTpl";
    public static final String IEPTPL_ID = "iepId";
	private IepTpl parent;		// 父
	private String parentIds;		// 所有父级编号
	private String name;		// 名称
	private String type;		// 模板类型
    private Integer level;      // 级别
    private Integer dstartRow;      // 数据起始行
    private String path;      // 模板路径
    private String epath;      // 错误模板路径
	private String operType;		// 操作类型
	private String ftype;		// 文件类型
	private String filters;		// 文件后缀
	private Integer step;		// 关联模板步骤
	private Boolean hasFile;		// 是否需要处理附件:0、默认（否）；1、是
	private Boolean hasFgf;		// 是否需要处理分隔符:0、默认（否）；1、是
    private IepTflow tdetail;     //模板明细
    private Integer curr;        //是否当前步骤:0、默认（否）；1、是
	@Transient
	private List<IepTpl> childrens;     //子元素
	@Transient
    private boolean isError;      // 数据起始行
	public IepTpl() {
		super();
		this.tdetail = new IepTflow();
		tdetail.setIsFirst(true);
	}

	public IepTpl(String id){
		super(id);
	}

	@JsonBackReference
	@NotNull(message="子流程编号不能为空")
	public IepTpl getParent() {
		return parent;
	}

	public void setParent(IepTpl parent) {
		this.parent = parent;
	}

	@Length(min=1, max=2000, message="所有父级编号长度必须介于 1 和 2000 之间")
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getFilters() {
        return filters;
    }

	public List<String> filters() {
	    if(StringUtil.isNotEmpty(this.filters)){
	        return Arrays.asList(StringUtil.split(this.filters, StringUtil.DOTH));
	    }
	    return null;
	}

    public void setFilters(String filters) {
        this.filters = filters;
    }

    @Length(min=0, max=255, message="名称长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=3, message="模板类型长度必须介于 0 和 3 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getDstartRow() {
        return dstartRow;
    }

    public void setDstartRow(Integer dstartRow) {
        this.dstartRow = dstartRow;
    }

    @Length(min=0, max=3, message="操作类型长度必须介于 0 和 3 之间")
	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	@Length(min=0, max=3, message="文件类型长度必须介于 0 和 3 之间")
	public String getFtype() {
		return ftype;
	}

	public void setFtype(String ftype) {
		this.ftype = ftype;
	}

	public Integer getCurr() {
	    if(this.curr == null){
	        this.curr = 0;
	    }
        return curr;
    }

    public void setCurr(Integer curr) {
        this.curr = curr;
    }

    @Length(min=0, max=11, message="关联模板步骤长度必须介于 0 和 11 之间")
	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public Boolean getHasFile() {
		return hasFile;
	}

	public void setHasFile(Boolean hasFile) {
		this.hasFile = hasFile;
	}

	public boolean isError() {
        return isError;
    }

    public void setError(boolean isError) {
        this.isError = isError;
    }

	public Boolean getHasFgf() {
		return hasFgf;
	}

	public void setHasFgf(Boolean hasFgf) {
		this.hasFgf = hasFgf;
	}

	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}

	public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEpath() {
        return epath;
    }

    public void setEpath(String epath) {
        this.epath = epath;
    }

    public List<IepTpl> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<IepTpl> childrens) {
        this.childrens = childrens;
    }

    /**
     * 节点排序.
     * @return String
     */
    @JsonIgnore
    public static void sortList(List<IepTpl> list, List<IepTpl> sourcelist, String parentId, boolean cascade) {
        if(sourcelist != null){
            for (int i = 0; i < sourcelist.size(); i++) {
                IepTpl e = sourcelist.get(i);
                if (e.getParent() != null && e.getParent().getId() != null
                        && e.getParent().getId().equals(parentId)) {
                    list.add(e);
                    if (cascade) {
                        // 判断是否还有子节点, 有则继续获取子节点
                        for (int j = 0; j < sourcelist.size(); j++) {
                            IepTpl child = sourcelist.get(j);
                            if (child.getParent() != null && child.getParent().getId() != null && child.getParent().getId().equals(e.getId())) {
                                sortList(list, sourcelist, e.getId(), true);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }


    public static void main(String[] args) {
        List<IepTpl> list = Lists.newArrayList();
        IepTpl root = new IepTpl();
        root.setId("10");
        root.setParent(new IepTpl("1"));
        root.setName("国家互联网+大赛");
        list.add(root);

        IepTpl tm = new IepTpl();
        tm.setId("100");
        tm.setParent(root);
        tm.setName("团队信息");
        list.add(tm);

        IepTpl hz = new IepTpl();
        hz.setId("103");
        hz.setParent(root);
        hz.setName("汇总信息");
        list.add(hz);

        IepTpl fj = new IepTpl();
        fj.setId("106");
        fj.setParent(root);
        fj.setName("附件信息");
        list.add(fj);

        IepTpl dr = new IepTpl();
        dr.setId("1060");
        dr.setParent(fj);
        dr.setName("附件信息-导入");
        list.add(dr);

        IepTpl dc = new IepTpl();
        dc.setId("1061");
        dc.setParent(fj);
        dc.setName("附件信息-导出");
        list.add(dc);

        IepTpl.treeList(root, list, true);
        System.out.println(root);
    }

    /**
     * 构建树列表.
     * @param iepTpl
     * @param entity
     * @return
     */
    @JsonIgnore
    public static IepTpl treeList(IepTpl entity, List<IepTpl> sourcelist, boolean cascade) {
        if((entity != null) && sourcelist != null){
            for (int i = 0; i < sourcelist.size(); i++) {
                IepTpl e = sourcelist.get(i);
                if (e.getParent() != null && e.getParent().getId() != null && e.getParent().getId().equals(entity.getId())) {
                    if (cascade) {
                        treeList(e, sourcelist, true);
                    }

                    if(entity.getChildrens() == null){
                        entity.setChildrens(Lists.newArrayList());
                    }
                    entity.getChildrens().add(e);
                }
            }
        }
        return entity;
    }

    /**
     * 流程根节点.
     *
     * @return String
     */
    @JsonIgnore
    public static String getRootId() {
        return CoreIds.NCE_SYS_TREE_ROOT.getId();
    }

    /**
     * 流程根节点的父节点.
     * @return String
     */
    @JsonIgnore
    public static String getRootPid() {
        return CoreIds.NCE_SYS_TREE_PROOT.getId();
    }

    @Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}