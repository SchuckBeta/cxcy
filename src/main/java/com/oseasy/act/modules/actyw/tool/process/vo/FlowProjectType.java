package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程项目类型.
 * 字典：act_project_type.
 * @author chenhao
 *
 */
public enum FlowProjectType {
	PMT_XM("1", "project", "双创项目", FlowPtype.PTT_XM, FlowPcategoryType.PCT_XM, FlowPlevelType.PLT_XM, FlowPstatusType.PST_XM)
  	,PMT_DASAI("7", "gcontest", "双创大赛", FlowPtype.PTT_DASAI, FlowPcategoryType.PCT_DASAI, FlowPlevelType.PLT_DASAI, FlowPstatusType.PST_DASAI)
  	,PMT_TECHNOLOGY("8", "technology", "科研成果", FlowPtype.PTT_TECHNOLOGY, FlowPcategoryType.PCT_TECHNOLOGY, FlowPlevelType.PLT_TECHNOLOGY, FlowPstatusType.PST_TECHNOLOGY)
	,PMT_SCORE("9", "score","课程学分", FlowPtype.PTT_SCORE, FlowPcategoryType.PCT_SCORE, FlowPlevelType.PLT_SCORE, FlowPstatusType.PST_SCORE)
	,PMT_APPOINTMENT("10", "pwAppointment","预约", FlowPtype.PTT_APPOINTMENT, FlowPcategoryType.PCT_APPOINTMENT, FlowPlevelType.PLT_APPOINTMENT, FlowPstatusType.PST_APPOINTMENT)
	,PMT_ENTER("20", "pwenter","入驻", FlowPtype.PTT_ENTER, FlowPcategoryType.PCT_ENTER, FlowPlevelType.PLT_ENTER, FlowPstatusType.PST_ENTER)
	,PMT_ALL("100", "all","通用", FlowPtype.PTT_ALL, FlowPcategoryType.PCT_ALL, FlowPlevelType.PLT_ALL, FlowPstatusType.PST_ALL)
	;

    public final static String KEY_ACT_PROJECT_TYPE = "act_project_type";

	private String key;
	private String value;
	private String name;
	private FlowPtype type;
	private FlowPcategoryType category;
  private FlowPlevelType level;
	private FlowPstatusType status;

	private FlowProjectType(String key, String value, String name, FlowPtype type, FlowPcategoryType category, FlowPlevelType level, FlowPstatusType status) {
    this.key = key;
    this.value = value;
    this.name = name;
    this.type = type;
    this.category = category;
    this.level = level;
    this.status = status;
  }

  /**
   * 获取枚举 .
   * @author chenhao
   * @return List<FlowProjectType>
   */
  public static List<FlowProjectType> getList() {
    return Arrays.asList(FlowProjectType.values());
  }

  public FlowPtype getType() {
    return type;
  }

  public FlowPcategoryType getCategory() {
    return category;
  }

  public FlowPlevelType getLevel() {
    return level;
  }

  public FlowPstatusType getStatus() {
    return status;
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

    public String getKey() {
        return key;
    }
    public String key() {
        return key+StringUtil.DOTH;
    }

  public void setKey(String key) {
    this.key = key;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return FlowProjectType
   */
  public static FlowProjectType getByKey(String key) {
    if ((key != null)) {
      FlowProjectType[] entitys = FlowProjectType.values();
      for (FlowProjectType entity : entitys) {
        if ((key).equals(entity.getKey())) {
          return entity;
        }
      }
    }
    return null;
  }

  public static String getNameByValue(String value) {
		if (value!=null) {
			for(FlowProjectType e: FlowProjectType.values()) {
				if (e.value.equals(value)) {
					return e.name;
				}
			}
		}
		return "";
	}

  /**
   * 枚举转字符串.
   */
  public static String listToStr(List<FlowProjectType> pestatus) {
      StringBuffer buffer = new StringBuffer();
      for (int i = 0; i < pestatus.size(); i++) {
          FlowProjectType estatus = pestatus.get(i);
          if(i == 0){
              buffer.append(estatus.getKey());
          }else{
              buffer.append(StringUtil.DOTH);
              buffer.append(estatus.getKey());
          }
      }
      return buffer.toString();
  }

  /**
   * 显示所有流程设计的功能.
   * @return List
   */
  public static List<FlowProjectType> showZps() {
      List<FlowProjectType> es = Lists.newArrayList();
      es.add(FlowProjectType.PMT_XM);
      es.add(FlowProjectType.PMT_DASAI);
      return es;
  }
  public static String showZp() {
      return FlowProjectType.listToStr(FlowProjectType.showZps()) + StringUtil.DOTH;
  }

  @Override
  public String toString() {
      return "{\"value\":\"" + this.value + "\",\"key\":\"" + this.key + "\",\"type\":\"" + this.type + "\",\"name\":" + this.name+ "\",\"category\":" + this.category+ "\",\"level\":" + this.level + "\",\"status\":" + this.status + "\"}";
  }
}
