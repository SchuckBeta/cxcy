package com.oseasy.pie.modules.impdata.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.pie.modules.iep.tool.check.ItCparamGgj;
import com.oseasy.pie.modules.impdata.tool.IitCheckEety;

/**
 * 导入数据错误信息表Entity
 * @author 9527
 * @version 2017-05-16
 */
public class ImpInfoErrmsg extends DataEntity<ImpInfoErrmsg> implements IitCheckEety{

	private static final long serialVersionUID = 1L;
	public static final String ERROR_MSG = "errmsg";
	public static final String COL_NAME = "colname";
	public static final String DATA_ID = "data_id";
	public static final String DATA_SUB_ID = "data_sub_id";
	public static final String IDX_SHEET = "sheet_indx";
	public static final String IDX_ROW = "row_index";
	public static final String IDX_COL = "cell_index";
	private String impId;		// 导入信息表主键
	private String dataId;		// 导入错误数据表主键
	private String dataSubId;		// 导入错误数据表子表主键
	private String colname;		// 错误字段名称
	private String errmsg;		// 错误信息
	private String sheetIndx;		// 错误字段所在sheet
	private String rowIndex;		// 错误字段所在sheet
	private String cellIndex;		// 错误字段所在sheet
	public ImpInfoErrmsg() {
		super();
	}

	public ImpInfoErrmsg(String id) {
		super(id);
	}

   public ImpInfoErrmsg(String id, String impId){
        super(id);
        this.impId = impId;
    }

	public ImpInfoErrmsg(ItCparamGgj param, String impId, String dataId) {
	    super();
        this.impId = impId;
        this.dataId = dataId;
        this.colname = param.getColName();
        this.sheetIndx = param.getIdxSheet();
        this.rowIndex = param.getIdxRow() + "";
        this.cellIndex = param.getIdx() + "";
}

    public String getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(String rowIndex) {
		this.rowIndex = rowIndex;
	}

	public String getCellIndex() {
		return cellIndex;
	}

	public void setCellIndex(String cellIndex) {
		this.cellIndex = cellIndex;
	}

	public String getDataSubId() {
		return dataSubId;
	}

	public void setDataSubId(String dataSubId) {
		this.dataSubId = dataSubId;
	}

	public String getSheetIndx() {
		return sheetIndx;
	}

	public void setSheetIndx(String sheetIndx) {
		this.sheetIndx = sheetIndx;
	}

	@Length(min=0, max=64, message="导入信息表主键长度必须介于 0 和 64 之间")
	public String getImpId() {
		return impId;
	}

	public void setImpId(String impId) {
		this.impId = impId;
	}

	@Length(min=0, max=64, message="导入错误数据表主键长度必须介于 0 和 64 之间")
	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	@Length(min=0, max=128, message="错误字段名称长度必须介于 0 和 128 之间")
	public String getColname() {
		return colname;
	}

	public void setColname(String colname) {
		this.colname = colname;
	}

	@Length(min=0, max=512, message="错误信息长度必须介于 0 和 512 之间")
	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

}