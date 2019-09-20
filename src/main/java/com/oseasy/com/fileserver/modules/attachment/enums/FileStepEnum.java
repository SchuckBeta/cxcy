package com.oseasy.com.fileserver.modules.attachment.enums;

import java.util.List;

import com.google.common.collect.Lists;

/*文件类别对应sys_attachment表file_step字段*/
public enum FileStepEnum implements StringEnum<FileStepEnum>{
	/** 民大项目. */
	S2000("2000", FileTypeEnum.S10, "民大项目申报"),
	S2100("2100", FileTypeEnum.S10, "民大项目立项"),
	S2200("2200", FileTypeEnum.S10, "民大项目中期"),
	S2300("2300", FileTypeEnum.S10, "民大项目结项"),
	S2010("2010", FileTypeEnum.S10, "民大项目申报导出文件"),
	S2210("2210", FileTypeEnum.S10, "民大项目中期导出文件"),
	S2310("2310", FileTypeEnum.S10, "民大项目结项导出文件"),

	/** 双创项目. */
	S100("100", FileTypeEnum.S0, "双创项目申报"),
	S101("101", FileTypeEnum.S0, "双创项目周报"),
	S102("102", FileTypeEnum.S0, "双创项目中期检查"),
	S103("103", FileTypeEnum.S0, "双创项目结项"),

	 /** 双创项目通告. */
	S200("200", FileTypeEnum.S1, "双创项目通告"),

	/** 大赛申报. */
	S300("300", FileTypeEnum.S2, "大赛申报"),

	/** 课程学分. */
	S5("500", FileTypeEnum.S5, "课程学分认定成果物附件"),
	/** 优秀展示. */
	S601("601", FileTypeEnum.S6, "优秀展示封面"),
	S602("602", FileTypeEnum.S6, "优秀展示内容"),

	/** 大赛热点内容. */
	S701("701", FileTypeEnum.S7, "大赛热点内容"),

	/** 通告发布内容. */
	S801("801", FileTypeEnum.S8,"通告发布内容"),

	/** 课程. */
	S900("900", FileTypeEnum.S9, "课程课件"),

	/** 入驻. */
  S_ENTER_TEAM("31000", FileTypeEnum.S_PW_ENTER, "入驻团队附件"),
  S_ENTER_PROJECT("32000", FileTypeEnum.S_PW_ENTER, "入驻项目附件"),
  S_ENTER_COMPANY("33000", FileTypeEnum.S_PW_ENTER, "入驻企业附件"),

  /** 流程图. */
  S_FLOW_NODELV1("20000", FileTypeEnum.S_FLOW_ICON, "节点一级图标"),
  S_FLOW_NODELV2("20001", FileTypeEnum.S_FLOW_ICON, "节点二级图标"),

  /** 留言. */
  S_GUEST_BOOK("40000", FileTypeEnum.S_GUEST_BOOK, "留言附件"),

  /** 自定义. */
	S1100("1100", FileTypeEnum.S11, "自定义"),
	/**
	 * 项目logo
	 */
	S1101("1101", FileTypeEnum.S11, "项目logo"),
	/**
	 * 项目申报
	 */
	S1102("1102", FileTypeEnum.S11, "项目申报"),
	/**
	 * 项目中期
	 */
	S1103("1103", FileTypeEnum.S11, "项目中期"),
	/**
	 * 项目结项
	 */
	S1104("1104", FileTypeEnum.S11, "项目结项"),
	/** 申报通知内容. */
	S1201("701", FileTypeEnum.S12, "申报通知内容"),
	/**证书模板元素*/
	S10001("10001", FileTypeEnum.S_SYS_CERT, "证书模板元素"),
	/**证书模板页面生成的图片*/
	S10002("10002", FileTypeEnum.S_SYS_CERT, "证书模板页面生成的图片(预览用)"),
	/**下发证书页面生成的图片*/
	S10003("10003", FileTypeEnum.S_SYS_CERT, "下发证书页面生成的图片"),
	/**证书模板页面生成的图片*/
	S10004("10004", FileTypeEnum.S_SYS_CERT, "证书模板页面生成的图片(下发证书用)"),
	S1301("1301", FileTypeEnum.S13, "基地项目申报附件"),
	S1401("1401", FileTypeEnum.S14, "基地企业申报附件"),
	S1501("1501", FileTypeEnum.S15, "基地申报成果物附件"),
	S1601("1601", FileTypeEnum.S16, "学分认定申请成果物附件"),
	S1701("1701", FileTypeEnum.S17, "学分认定申请证书成果物附件"),
	S1801("1801", FileTypeEnum.S18, "模板流程申报附件"),
	S1901("1901", FileTypeEnum.S19, "模板流程时间轴附件")
	;

	//(100双创项目申报，101双创项目周报，102双创项目中期检查，103双创项目结项，200双创项目通告,300双创大赛。。。,301,400双创大赛通告。。。,401）自己加
	private String value;
	private String name;
	private FileTypeEnum type;

	private FileStepEnum(String value, FileTypeEnum type, String name) {
    this.value = value;
    this.name = name;
    this.type = type;
  }

  public FileTypeEnum getType() {
    return type;
  }

  public void setType(FileTypeEnum type) {
    this.type = type;
  }

  public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private FileStepEnum(String value, String name) {
		this.value=value;
		this.name=name;
	}

	 /**
	  * 根据类型获取 .
	  * @author chenhao
	  * @param key 枚举标识
	  * @return List
	  */
	 public static List<FileStepEnum> getByType(FileTypeEnum type) {
	   if (type != null) {
	     List<FileStepEnum> fileSteps = Lists.newArrayList();
	     FileStepEnum[] entitys = FileStepEnum.values();
	     for (FileStepEnum entity : entitys) {
	       if ((type).equals(entity.getType())) {
	         fileSteps.add(entity);
	       }
	     }
	     return fileSteps;
	   }
	   return null;
	 }

	 /**
	  * 根据key获取枚举 .
	  *
	  * @author chenhao
	  * @param key 枚举标识
	  * @return FileStepEnum
	  */
	 public static FileStepEnum getByValue(String value) {
	   if ((value != null)) {
	     FileStepEnum[] entitys = FileStepEnum.values();
	     for (FileStepEnum entity : entitys) {
	       if ((value).equals(entity.getValue())) {
	         return entity;
	       }
	     }
	   }
	   return null;
	 }

	public static String getNameByValue(String value) {
		if (value!=null) {
			for(FileStepEnum e:FileStepEnum.values()) {
				if (e.value.equals(value)) {
					return e.name;
				}
			}
		}
		return "";
	}

	@Override
	public String getStringValue() {
		return this.value;
	}
}
