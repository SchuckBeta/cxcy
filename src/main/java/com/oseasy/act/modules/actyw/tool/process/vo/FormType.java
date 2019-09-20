package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.tool.apply.IActYw;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 表单类型.
 * 对应字典表 act_form_type .
 * @author chenhao
 *
 */
public enum FormType {
	FT_ALL_APPLY("10", FormFtype.GNODE, new FlowType[]{FlowType.FWT_ALL, FlowType.FWT_XM, FlowType.FWT_DASAI, FlowType.FWT_SCORE, FlowType.FWT_TECHNOLOGY, FlowType.FWT_APPOINTMENT, FlowType.FWT_ENTER}, FormStyleType.FST_FORM, FormClientType.FST_FRONT, "applyForm","通用申报")
	,FT_ALL_VIEW("15", FormFtype.VIEW, new FlowType[]{FlowType.FWT_ALL, FlowType.FWT_XM, FlowType.FWT_DASAI, FlowType.FWT_SCORE, FlowType.FWT_TECHNOLOGY, FlowType.FWT_APPOINTMENT, FlowType.FWT_ENTER}, FormStyleType.FST_VIEW, FormClientType.FST_ADMIN, "viewForm","通用查看")
	,FT_ALL_AUDIT("20", FormFtype.GNODE, new FlowType[]{FlowType.FWT_ALL, FlowType.FWT_XM, FlowType.FWT_DASAI, FlowType.FWT_SCORE, FlowType.FWT_TECHNOLOGY, FlowType.FWT_APPOINTMENT, FlowType.FWT_ENTER}, FormStyleType.FST_FORM, FormClientType.FST_ADMIN, "auditForm","通用审核")
	,FT_ALL_LISTFORNT("30", FormFtype.GNODE, new FlowType[]{FlowType.FWT_ALL, FlowType.FWT_XM, FlowType.FWT_DASAI, FlowType.FWT_SCORE, FlowType.FWT_TECHNOLOGY, FlowType.FWT_APPOINTMENT, FlowType.FWT_ENTER}, FormStyleType.FST_LIST, FormClientType.FST_FRONT, "listFrontForm","通用前台列表")
	,FT_ALL_LISTADMIN("40", FormFtype.GNODE, new FlowType[]{FlowType.FWT_ALL, FlowType.FWT_XM, FlowType.FWT_DASAI, FlowType.FWT_SCORE, FlowType.FWT_TECHNOLOGY, FlowType.FWT_APPOINTMENT, FlowType.FWT_ENTER}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "listAdminForm","通用后台列表")

	,FT_XM_APPLY("100", FormFtype.GNODE, new FlowType[]{FlowType.FWT_XM}, FormStyleType.FST_FORM, FormClientType.FST_FRONT, "applyForm","项目申报")
	,FT_XM_RECOMENT_PRO("102", FormFtype.GNODE, new FlowType[]{FlowType.FWT_XM}, FormStyleType.FST_FORM, FormClientType.FST_FRONT, "recomendProForm","省项目推荐")
	,FT_XM_RECOMENT_COUNTRY("102", FormFtype.GNODE, new FlowType[]{FlowType.FWT_XM}, FormStyleType.FST_FORM, FormClientType.FST_FRONT, "recomendCountryForm","国家项目推荐")
	,FT_XM_REPORT("105", FormFtype.GNODE, new FlowType[]{FlowType.FWT_XM}, FormStyleType.FST_FORM, FormClientType.FST_FRONT, "reportForm","项目提交报告")
	,FT_XM_AUDIT("110", FormFtype.GNODE, new FlowType[]{FlowType.FWT_XM}, FormStyleType.FST_FORM, FormClientType.FST_ADMIN, "auditForm","项目审核")
	,FT_XM_LISTFORNT("120", FormFtype.GNODE, new FlowType[]{FlowType.FWT_XM}, FormStyleType.FST_LIST, FormClientType.FST_FRONT, "listFrontForm","项目前台列表")
	,FT_XM_LISTADMIN("130", FormFtype.GNODE, new FlowType[]{FlowType.FWT_XM}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "listAdminForm","项目后台列表")
	,FT_XM_VIEW("135", FormFtype.VIEW, new FlowType[]{FlowType.FWT_XM}, FormStyleType.FST_VIEW, FormClientType.FST_ADMIN, "viewForm","项目查看")
	,FT_XM_SCORE("140", FormFtype.GNODE, new FlowType[]{FlowType.FWT_XM}, FormStyleType.FST_FORM, FormClientType.FST_ADMIN, "scoreForm","项目打分")
	,FT_XM_GRATE("145", FormFtype.GNODE, new FlowType[]{FlowType.FWT_XM}, FormStyleType.FST_FORM, FormClientType.FST_ADMIN, "grateForm","项目评级")
	,FT_XM_CHANGE("150", FormFtype.GNODE, new FlowType[]{FlowType.FWT_XM}, FormStyleType.FST_FORM, FormClientType.FST_ADMIN, "changeForm","项目变更")
	,FT_XM_AUDITWPLIST("160", FormFtype.GNODE, new FlowType[]{FlowType.FWT_XM}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "auditWpListForm","项目网评列表")
	,FT_XM_AUDITLYLIST("170", FormFtype.GNODE, new FlowType[]{FlowType.FWT_XM}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "auditLyListForm","项目路演列表")
	,FT_XM_AUDITENDLIST("180", FormFtype.GNODE, new FlowType[]{FlowType.FWT_XM}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "auditEndListForm","项目评级列表")

	,FT_DASAI_APPLY("200", FormFtype.GNODE, new FlowType[]{FlowType.FWT_DASAI}, FormStyleType.FST_FORM, FormClientType.FST_FRONT, "applyForm","大赛申报")
  ,FT_DASAI_REPORT("205", FormFtype.GNODE, new FlowType[]{FlowType.FWT_DASAI}, FormStyleType.FST_FORM, FormClientType.FST_FRONT, "reportForm","大赛提交报告")
	,FT_DASAI_AUDIT("210", FormFtype.GNODE, new FlowType[]{FlowType.FWT_DASAI}, FormStyleType.FST_FORM, FormClientType.FST_ADMIN, "auditForm","大赛审核")
	,FT_DASAI_LISTFORNT("220", FormFtype.GNODE, new FlowType[]{FlowType.FWT_DASAI}, FormStyleType.FST_LIST, FormClientType.FST_FRONT, "listFrontForm","大赛前台列表")
	,FT_DASAI_LISTADMIN("230", FormFtype.GNODE, new FlowType[]{FlowType.FWT_DASAI}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "listAdminForm","大赛后台列表")
	,FT_DASAI_VIEW("235", FormFtype.VIEW, new FlowType[]{FlowType.FWT_DASAI}, FormStyleType.FST_VIEW, FormClientType.FST_ADMIN, "viewForm","大赛查看")
	,FT_DASAI_SCORE("240", FormFtype.GNODE, new FlowType[]{FlowType.FWT_DASAI}, FormStyleType.FST_FORM, FormClientType.FST_ADMIN, "scoreForm","大赛打分")
  ,FT_DASAI_GRATE("245", FormFtype.GNODE, new FlowType[]{FlowType.FWT_DASAI}, FormStyleType.FST_FORM, FormClientType.FST_ADMIN, "grateForm","大赛评级")
	,FT_DASAI_CHANGE("250", FormFtype.GNODE, new FlowType[]{FlowType.FWT_DASAI}, FormStyleType.FST_FORM, FormClientType.FST_ADMIN, "changeForm","大赛变更")
	,FT_DASAI_AUDITWPLIST("260", FormFtype.GNODE, new FlowType[]{FlowType.FWT_DASAI}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "auditWpListForm","大赛网评列表")
	,FT_DASAI_AUDITLYLIST("270", FormFtype.GNODE, new FlowType[]{FlowType.FWT_DASAI}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "auditLyListForm","大赛路演列表")
	,FT_DASAI_AUDITENDLIST("280", FormFtype.GNODE, new FlowType[]{FlowType.FWT_DASAI}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "auditEndListForm","大赛评级列表")

	,FT_SCORE_APPLY("300", FormFtype.GNODE, new FlowType[]{FlowType.FWT_SCORE}, FormStyleType.FST_FORM, FormClientType.FST_FRONT, "applyForm","学分申报")
	,FT_SCORE_AUDIT("310", FormFtype.GNODE, new FlowType[]{FlowType.FWT_SCORE}, FormStyleType.FST_FORM, FormClientType.FST_ADMIN, "auditForm","学分审核")
	,FT_SCORE_LISTFORNT("320", FormFtype.GNODE, new FlowType[]{FlowType.FWT_SCORE}, FormStyleType.FST_LIST, FormClientType.FST_FRONT, "listFrontForm","学分前台列表")
	,FT_SCORE_LISTADMIN("330", FormFtype.GNODE, new FlowType[]{FlowType.FWT_SCORE}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "listAdminForm","学分后台列表")
	,FT_SCORE_QUERYADMIN("340", FormFtype.QUERY, new FlowType[]{FlowType.FWT_SCORE}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "queryList","学分查询列表")

  ,FT_TECHNOLOGY_APPLY("410", FormFtype.GNODE, new FlowType[]{FlowType.FWT_TECHNOLOGY}, FormStyleType.FST_FORM, FormClientType.FST_FRONT, "applyForm","科研成果申报")
  ,FT_TECHNOLOGY_AUDIT("420", FormFtype.GNODE, new FlowType[]{FlowType.FWT_TECHNOLOGY}, FormStyleType.FST_FORM, FormClientType.FST_ADMIN, "auditForm","科研成果审核")
  ,FT_TECHNOLOGY_LISTFORNT("430", FormFtype.GNODE, new FlowType[]{FlowType.FWT_TECHNOLOGY}, FormStyleType.FST_LIST, FormClientType.FST_FRONT, "listFrontForm","科研成果前台列表")
  ,FT_TECHNOLOGY_LISTADMIN("440", FormFtype.GNODE, new FlowType[]{FlowType.FWT_TECHNOLOGY}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "listAdminForm","科研成果后台列表")

  ,FT_APPOINTMENT_APPLY("510", FormFtype.GNODE, new FlowType[]{FlowType.FWT_APPOINTMENT}, FormStyleType.FST_FORM, FormClientType.FST_FRONT, "applyForm","预约申报")
  ,FT_APPOINTMENT_AUDIT("520", FormFtype.GNODE, new FlowType[]{FlowType.FWT_APPOINTMENT}, FormStyleType.FST_FORM, FormClientType.FST_ADMIN, "auditForm","预约审核")
  ,FT_APPOINTMENT_LISTFORNT("530", FormFtype.GNODE, new FlowType[]{FlowType.FWT_APPOINTMENT}, FormStyleType.FST_LIST, FormClientType.FST_FRONT, "listFrontForm","预约前台列表")
  ,FT_APPOINTMENT_LISTADMIN("540", FormFtype.GNODE, new FlowType[]{FlowType.FWT_APPOINTMENT}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "listAdminForm","预约后台列表")

  ,FT_ENTER_APPLY("610", FormFtype.GNODE, new FlowType[]{FlowType.FWT_ENTER}, FormStyleType.FST_FORM, FormClientType.FST_FRONT, "applyForm", "入驻申报")
  ,FT_ENTER_AUDIT("620", FormFtype.GNODE, new FlowType[]{FlowType.FWT_ENTER}, FormStyleType.FST_FORM, FormClientType.FST_ADMIN, "auditForm", "入驻审核")
  ,FT_ENTER_LISTFORNT("630", FormFtype.GNODE, new FlowType[]{FlowType.FWT_ENTER}, FormStyleType.FST_LIST, FormClientType.FST_FRONT, "listFrontForm", "入驻前台列表")
  ,FT_ENTER_LISTADMIN("640", FormFtype.GNODE, new FlowType[]{FlowType.FWT_ENTER}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "listAdminForm", "入驻后台列表")

//  ,FT_QINGJIA_APPLY("910", new FlowType[]{FlowType.FWT_QINGJIA}, FormStyleType.FST_FORM, FormClientType.FST_FRONT, "applyForm", "请假申请")
//  ,FT_QINGJIA_AUDIT("920", new FlowType[]{FlowType.FWT_QINGJIA}, FormStyleType.FST_FORM, FormClientType.FST_ADMIN, "auditForm", "请假审核")
//  ,FT_QINGJIA_LISTFORNT("930", new FlowType[]{FlowType.FWT_QINGJIA}, FormStyleType.FST_LIST, FormClientType.FST_FRONT, "listFrontForm", "请假前台列表")
//  ,FT_QINGJIA_LISTADMIN("940", new FlowType[]{FlowType.FWT_QINGJIA}, FormStyleType.FST_LIST, FormClientType.FST_ADMIN, "listAdminForm", "请假后台列表")
	;

    public static final String TEMPLATE_FORM_ROOT = "/template/form/";
	private String key;
	private FormFtype ftype;
	private FlowType[] type;
	private FormStyleType style;
	private FormClientType client;
	private String value;
	private String name;

  private FormType(String key, FormFtype ftype, FlowType[] type, FormStyleType style, FormClientType client, String value, String name) {
    this.key = key;
    this.ftype = ftype;
    this.type = type;
    this.style = style;
    this.client = client;
    this.value=value;
    this.name=name;
  }

	public String getKey() {
    return key;
  }

  public FormFtype getFtype() {
        return ftype;
    }

  public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public FormStyleType getStyle() {
    return style;
  }

  public FormClientType getClient() {
    return client;
  }

  public static String getFormLabel(String value) {
      return FormType.getNameByValue(value);
  }

  public static String getNameByValue(String value) {
		if (value!=null) {
			for(FormType e: FormType.values()) {
				if (e.value.equals(value)) {
					return e.name;
				}
			}
		}
		return null;
	}

	/**
	 */
	public static FormType getByKey(String key) {
	  if (StringUtil.isNotEmpty(key)) {
	    for(FormType e: FormType.values()) {
	      if ((e.getKey()).equals(key)) {
	        return e;
	      }
	    }
	  }
	  return null;
	}

	/**
	 * 根据流程标识获取表单类型.
	 * @param type 流程唯一标识
	 * @return List
	 */
	public static List<FormType> getByType(String type) {
	  return getByType(FlowType.getByKey(type));
	}

	 /**
   * 根据流程标识获取表单类型.
   * @param type FlowType流程
   * @return List
   */
	public static List<FormType> getByFtype(FlowType type, FormFtype ftype, FormClientType client) {
	  List<FormType> formTypes = Lists.newArrayList();
	  for(FormType formType: FormType.values()) {
	      if((ftype).equals(formType.getFtype()) && (client).equals(formType.getClient())){
	          for (FlowType fwType : formType.getType()) {
	              if ((fwType).equals(type)) {
	                formTypes.add(formType);
	              }
	          }
	      }
	  }
	  return formTypes;
	}

	/**
	 * 根据流程标识获取表单类型.
	 * @param type FlowType流程
	 * @return List
	 */
	public static List<FormType> getByType(FlowType type, FormStyleType style, FormClientType client) {
	    List<FormType> formTypes = Lists.newArrayList();
	    for(FormType formType: FormType.values()) {
	        if((style).equals(formType.getStyle()) && (client).equals(formType.getClient())){
	            for (FlowType fwType : formType.getType()) {
	                if ((fwType).equals(type)) {
	                    formTypes.add(formType);
	                }
	            }
	        }
	    }
	    return formTypes;
	}

	/**
	 * 根据流程标识获取表单类型.
	 * @param type FlowType流程
	 * @return List
	 */
	public static List<FormType> getByType(FlowType type) {
	    List<FormType> formTypes = Lists.newArrayList();
	    for(FormType formType: FormType.values()) {
	        for (FlowType fwType : formType.getType()) {
	            if ((fwType).equals(type)) {
	                formTypes.add(formType);
	            }
	        }
	    }
	    return formTypes;
	}

	/**
	 * 根据排版形式获取表单类型.
	 * @param style 流程唯一标识
	 * @return List
	 */
	public static List<FormType> getByStyle(String style) {
	  return getByStyle(FormStyleType.getByKey(style));
	}

	/**
	 * 根据排版形式获取表单类型.
	 * @param type FormStyleType流程
	 * @return List
	 */
	public static List<FormType> getByStyle(FormStyleType style) {
	  List<FormType> formTypes = Lists.newArrayList();
	  for(FormType formType: FormType.values()) {
	    if ((formType.getStyle()).equals(style)) {
	      formTypes.add(formType);
	    }
	  }
	  return formTypes;
	}

	/**
	 * 根据排版形式获取表单类型.
	 * @param Client 流程唯一标识
	 * @return List
	 */
	public static List<FormType> getByClient(String client) {
	  return getByClient(FormClientType.getByKey(client));
	}

	/**
	 * 根据排版形式获取表单类型.
	 * @param type FormClientType流程
	 * @return List
	 */
	public static List<FormType> getByClient(FormClientType client) {
	  List<FormType> formTypes = Lists.newArrayList();
	  for(FormType formType: FormType.values()) {
	    if ((formType.getClient()).equals(client)) {
	      formTypes.add(formType);
	    }
	  }
	  return formTypes;
	}

    /**
     * 根据表单类型获取流程类型字符串.
     * @param key 表单唯一标识
     * @param split 分隔符
     * @param hasEnd 结尾分隔符
     * @return List
     */
    public static String getFlowStrByKey(String key, String split, Boolean hasEnd) {
        List<FlowType> flowTypes = Lists.newArrayList();
        if (StringUtil.isNotEmpty(key)) {
            for(FormType e: FormType.values()) {
              if ((e.getKey()).equals(key)) {
                  flowTypes = Arrays.asList(e.getType());
                  break;
              }
            }
        }

        StringBuffer sb = new StringBuffer();
        if(hasEnd == null){
            hasEnd = false;
        }
        if(StringUtil.isEmpty(split)){
            return null;
        }

        boolean isFirst = true;
        for (FlowType entity : flowTypes) {
            if (isFirst) {
                sb.append(entity.getKey());
                isFirst = false;
            }else{
                sb.append(split);
                sb.append(entity.getKey());
            }
        }

        if(hasEnd){
            sb.append(split);
        }
        return sb.toString();
    }

    /**
     * 根据表单类型获取流程类型字符串.
     * @param key 表单唯一标识
     * @return List
     */
    public static String getFlowStrByKey(String key) {
        return getFlowStrByKey(key, StringUtil.DOTH, true);
    }

    /**
     * 根据表单类型获取项目类型字符串.
     * @param key 表单唯一标识
     * @param split 分隔符
     * @param hasEnd 结尾分隔符
     * @return List
     */
    public static String getProStrByKey(String key, String split, Boolean hasEnd) {
        List<FlowProjectType> flowptypes = Lists.newArrayList();
        List<FlowType> flowTypes = Lists.newArrayList();
        if (StringUtil.isNotEmpty(key)) {
            for(FormType e: FormType.values()) {
                if ((e.getKey()).equals(key)) {
                    flowTypes = Arrays.asList(e.getType());
                    break;
                }
            }
        }

        for (FlowType ftype : flowTypes) {
            flowptypes.addAll(Arrays.asList(ftype.getType().getTypes()));
        }

        StringBuffer sb = new StringBuffer();
        if(hasEnd == null){
            hasEnd = false;
        }
        if(StringUtil.isEmpty(split)){
            return null;
        }

        boolean isFirst = true;
        for (FlowProjectType entity : flowptypes) {
            if (isFirst) {
                sb.append(entity.getKey());
                isFirst = false;
            }else{
                sb.append(split);
                sb.append(entity.getKey());
            }
        }
        if(hasEnd){
            sb.append(split);
        }
        return sb.toString();
    }

    /**
     * 根据表单类型获取项目类型字符串.
     * @param key 表单唯一标识
     * @return List
     */
    public static String getProStrByKey(String key) {
        return getProStrByKey(key, StringUtil.DOTH, true);
    }

    /**
     * 根据表单类型获取项目类型.
     * @param key 表单唯一标识
     * @return List
     */
    public static List<FlowProjectType> getProByKey(String key) {
        List<FlowProjectType> flowptypes = Lists.newArrayList();
        List<FlowType> flowTypes = Lists.newArrayList();
        if (StringUtil.isNotEmpty(key)) {
            for(FormType e: FormType.values()) {
                if ((e.getKey()).equals(key)) {
                    flowTypes = Arrays.asList(e.getType());
                    break;
                }
            }
        }

        for (FlowType ftype : flowTypes) {
            flowptypes.addAll(Arrays.asList(ftype.getType().getTypes()));
        }
        return flowptypes;
    }


    /**
     * 生成表单模板路径,默认去第一个.
     */
    public static String genTpath(IActYw iactYw, FormFtype ftype, FormClientType fclient) {
        FlowType flowType = iactYw.flowType();
        FormTheme formTheme = iactYw.theme();
        if((flowType == null) || (formTheme == null) || (ftype == null) || (fclient == null)){
            return null;
        }

        List<FormType> formTypes = FormType.getByFtype(flowType, ftype, fclient);
        if(StringUtil.checkEmpty(formTypes)){
            return null;
        }
        return TEMPLATE_FORM_ROOT + flowType.getType().getKey() + StringUtil.LINE + formTheme.getKey() + formTypes.get(0).getValue();
    }

  public FlowType[] getType() {
    return type;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setType(FlowType[] type) {
    this.type = type;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
      return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\",\"value\":\"" + this.value
             /* + "\",\"type\":" + this.type.toString()
              + "\",\"style\":" + this.style.toString()
              + "\",\"client\":" + this.client.toString()*/
              + "\"}";
  }


	public static void main(String[] args) {
		ActYw actyw = new ActYw();
		ActYwGroup group = new ActYwGroup();
		group.setFlowType(FlowType.FWT_DASAI.getKey());
		group.setTheme(FormTheme.F_NP_GC.getId());
//		group.setFlowType(FormType.FT_DASAI_SCORE.getKey());
		actyw.setGroup(group);
		String tpath = FormType.genTpath(actyw, FormFtype.GNODE, FormClientType.FST_ADMIN);
		System.out.println(tpath);
	}
}
