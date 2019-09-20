package com.oseasy.pw.modules.pw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 耗材Entity.
 * @author chenh
 * @version 2017-11-26
 */
public class PwCosMaterial extends DataEntity<PwCosMaterial> {

	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private PwCategory pwCategory;		// 耗材类别
	private String brand;		// 品牌
	private String specification;		// 规格
	private Integer stocks;		// 库存

	public PwCosMaterial() {
		super();
	}

  public PwCosMaterial(PwCategory pwCategory) {
    super();
    this.pwCategory = pwCategory;
  }

	public PwCosMaterial(String id){
		super(id);
	}

	@Length(min=0, max=255, message="名称长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PwCategory getPwCategory() {
    return pwCategory;
  }

  public void setPwCategory(PwCategory pwCategory) {
    this.pwCategory = pwCategory;
  }

  @Length(min=0, max=255, message="品牌长度必须介于 0 和 255 之间")
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Length(min=0, max=255, message="规格长度必须介于 0 和 255 之间")
	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Integer getStocks() {
		return stocks;
	}

	public void setStocks(Integer stocks) {
		this.stocks = stocks;
	}

}