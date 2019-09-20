/**
 *
 */
package com.oseasy.com.pcore.modules.sys.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.oseasy.com.pcore.common.persistence.TreeEntity;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 菜单Entity


 */
public class Menu extends TreeEntity<Menu> {

	private static final long serialVersionUID = 1L;
	private Menu parent;	// 父级菜单
	private String parentIds; // 所有父级编号
	private String rid; 	// 模板菜单ID
	private Integer ltype; 	// 授权认证类型 TenantMenu
	private String name; 	// 名称
	private String href; 	// 链接
	private String target; 	// 目标（ mainFrame、_blank、_self、_parent、_top）
	private String icon; 	// 图标
	private Integer sort; 	// 排序
	private String isShow; 	// 是否在菜单中显示（1：显示；0：不显示）
	private String permission; // 权限标识
	private String imgUrl; // 菜单图片
	private String userId;
	private long todoCount;  //待办事项数

	private List<Menu> menuList;

	private List<Menu> children;
	/**
	 * 控制显示能否关闭
	 */
	public Integer open;

	public Integer getOpen() {
		return open;
	}

	public void setOpen(Integer open) {
		this.open = open;
	}

	public Integer getLtype() {
		return ltype;
	}

	public void setLtype(Integer ltype) {
		this.ltype = ltype;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public Menu() {
		super();
		this.sort = 30;
		this.isShow = "1";
	}

	public Menu(List<String> ids) {
		super();
		super.ids = ids;
	}

	public Menu(String id) {
		super(id);
	}


	@JsonBackReference
	@NotNull
	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	@Length(min=1, max=2000)
	public String getParentIds() {
		if(StringUtil.isEmpty(this.parentIds) && (this.parent != null) && StringUtil.isNotEmpty(this.parent.getParentIds()) && StringUtil.isNotEmpty(this.parent.getId())){
			this.parentIds = this.parent.getParentIds() + this.parent.getId() + StringUtil.DOTH;
		}
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=2000)
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Length(min=0, max=20)
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Length(min=0, max=100)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Length(min=1, max=1)
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	@Length(min=0, max=200)
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : CoreIds.NCE_SYS_TREE_PROOT.getId();
	}

	@JsonIgnore
	public static void sortList(List<Menu> list, List<Menu> sourcelist, String parentId, boolean cascade) {
		for (int i=0; i<sourcelist.size(); i++) {
			Menu e = sourcelist.get(i);
			if ("基础数据管理系统".equals(e.getName())){
				e.setOpen(1);
			}
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)) {
				list.add(e);
				if (cascade) {
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++) {
						Menu child = sourcelist.get(j);
						if (child.getParent()!=null && child.getParent().getId()!=null
								&& child.getParent().getId().equals(e.getId())) {
							sortList(list, sourcelist, e.getId(), true);
							break;
						}
					}
				}
			}
		}
	}

	@JsonIgnore
	public static String getRootId() {
    return CoreIds.NCE_SYS_TREE_ROOT.getId();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getTodoCount() {
		return this.todoCount;
	}

	public void setTodoCount(long todoCount) {
		this.todoCount = todoCount;
	}


	@Override
	public String toString() {
		return name;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public static Menu genRoot() {
		Menu m = new Menu(CoreIds.NCE_SYS_TREE_PROOT.getId());
		m.setParentIds("0,");
		return m;

	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {return true;}
		if (!(obj instanceof Menu)) { return false;}
		Menu re = (Menu) obj;
		if(StringUtil.isNotEmpty(re.getId())){
			return id.equals(re.getId());
		}else{
			return false;
		}
	}
}