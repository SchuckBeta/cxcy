package com.oseasy.act.modules.actyw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 监听Entity.
 * @author chenh
 * @version 2018-03-01
 */
public class ActYwClazz extends DataEntity<ActYwClazz> {

	private static final long serialVersionUID = 1L;
	private String theme;		// 主题
	private String type;		// 类型
	private String packag;		// 类包
    private String clazz;       // 类名
	private String alias;		// 别名

	public ActYwClazz() {
		super();
	}

	public ActYwClazz(String id){
		super(id);
	}

	public ActYwClazz(String id, String type){
	    super(id);
	    this.type = type;
	}

	@Length(min=0, max=200, message="类型长度必须介于 0 和 200 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=64, message="类包长度必须介于 0 和 64 之间")
	public String getPackag() {
		return packag;
	}

	public void setPackag(String packag) {
		this.packag = packag;
	}

	public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @Length(min=0, max=64, message="类名长度必须介于 0 和 64 之间")
	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	@Length(min=0, max=200, message="别名长度必须介于 0 和 200 之间")
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}