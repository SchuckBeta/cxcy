package com.oseasy.act.modules.actyw.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.com.pcore.common.persistence.DataExtEntity;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.DateUtil;

/**
 * 表单组Entity.
 * @author chenh
 * @version 2018-09-06
 */
public class ActYwGtheme extends DataExtEntity<ActYwGtheme> {

	private static final long serialVersionUID = 1L;
	private Integer idx;		// 主题标识
	private String ftype;		// 流程类型
	private String keyss;		// 主题标识
	private String name;		// 名称
	private String sname;		// 英文名
	private String serviceName;		// 服务名
	private Boolean enable;		// 可见
	private Integer sort;		// 排序

	public ActYwGtheme() {
		super();
	}

	public ActYwGtheme(String id){
		super(id);
	}

    public ActYwGtheme(ActYwGtheme entity) {
        super();
        this.id = entity.id;
        this.idx = entity.idx;
        this.ftype = entity.ftype;
        this.keyss = entity.keyss;
        this.name = entity.name;
        this.sname = entity.sname;
        this.serviceName = entity.serviceName;
        this.enable = entity.enable;
        this.sort = entity.sort;
        this.remarks = entity.remarks;
        this.updateBy = entity.updateBy;
        this.createBy = entity.createBy;
        this.updateDate = entity.updateDate;
        this.createDate = entity.createDate;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    @Length(min=0, max=10, message="主题标识长度必须介于 0 和 10 之间")
	public String getKeyss() {
		return keyss;
	}

	public void setKeyss(String keyss) {
		this.keyss = keyss;
	}

	@Length(min=0, max=255, message="名称长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=255, message="英文名长度必须介于 0 和 255 之间")
	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	@Length(min=0, max=255, message="服务名长度必须介于 0 和 255 之间")
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public static List<ActYwGtheme> converts(List<FormTheme> formThemes){
	    List<ActYwGtheme> gthemes = Lists.newArrayList();
	    int sort = 0;
	    for (FormTheme formTheme : formThemes) {
	        gthemes.add(new Builder()
	                .id(IdGen.uuid())
	                .idx(formTheme.getId())
	                .ftype(formTheme.getFtype())
	                .keyss(formTheme.getKey())
	                .name(formTheme.getName())
	                .sname(formTheme.getSname())
	                .serviceName(formTheme.getServiceName())
                    .enable(formTheme.getEnable())
                    .remarks(formTheme.getRemark())
                    .createBy(UserUtils.getUser())
                    .updateBy(UserUtils.getUser())
                    .createDate(DateUtil.newDate())
                    .updateDate(DateUtil.newDate())
                    .sort(sort += 10)
	                .build()
	                );
        }
	    return gthemes;
	}

    //新增Builder静态类并赋默认值
    public static class Builder {
        private ActYwGtheme entity;

        public Builder() {
            super();
            entity = new ActYwGtheme();
        }

        //为每一个属性创建返回自身Builder对象的方法
        public Builder id(String id) {
            entity.id = id;
            return this;
        }
        //为每一个属性创建返回自身Builder对象的方法
        public Builder idx(Integer idx) {
            entity.idx = idx;
            return this;
        }
        //为每一个属性创建返回自身Builder对象的方法
        public Builder ftype(String ftype) {
            entity.ftype = ftype;
            return this;
        }

        public Builder keyss(String keyss) {
            entity.keyss = keyss;
            return this;
        }

        public Builder name(String name) {
            entity.name = name;
            return this;
        }

        public Builder sname(String sname) {
            entity.sname = sname;
            return this;
        }

        public Builder remarks(String remarks) {
            entity.remarks = remarks;
            return this;
        }

        public Builder enable(Boolean enable) {
            entity.enable = enable;
            return this;
        }

        public Builder sort(Integer sort) {
            entity.sort = sort;
            return this;
        }
        public Builder delFlag(String delFlag) {
            entity.delFlag = delFlag;
            return this;
        }
        public Builder serviceName(String serviceName) {
            entity.serviceName = serviceName;
            return this;
        }
        public Builder createBy(User createBy) {
            entity.createBy = UserUtils.getUser();
            return this;
        }
        public Builder createDate(Date createDate) {
            entity.createDate = createDate;
            return this;
        }
        public Builder updateBy(User updateBy) {
            entity.updateBy = updateBy;
            return this;
        }
        public Builder updateDate(Date updateDate) {
            entity.updateDate = updateDate;
            return this;
        }

        //新建一个build方法，创建一个父类对象，传递给apply方法为这个空对象赋构建出来的参数值，返回这个构建对象即可。
        public ActYwGtheme build(){
            return new ActYwGtheme(this.entity);
        }
    }
}