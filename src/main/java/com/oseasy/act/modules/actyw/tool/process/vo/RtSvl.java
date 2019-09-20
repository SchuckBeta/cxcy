/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_RtSvl_]]文件
 * @date 2017年6月2日 下午4:23:59
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

/**
 * 流结果集常量类.
 *
 * @author chenhao
 * @date 2017年6月2日 下午4:23:59
 *
 */
public class RtSvl {
  /**
   * model.
   */
  public static class RtModelVal {
    public static final String STENCILSET_JSON = "stencilset.json";
    public static final String M = "model";
    public static final String M_PREFIX = "sid-";
    public static final String M_ID = "modelId";
    public static final String M_NAME = "name";
    public static final String M_REVISION = "revision";
    public static final String M_DESCRIPTION = "description";

    public static final String JSON_XML = "json_xml";
    public static final String SVG_XML = "svg_xml";
    public static final String UTF_8 = "utf-8";

    public static final String ON_ID = "id";
    public static final String ON_RESOURCE_ID = "resourceId";
    public static final String ON_PROPERTIES = "properties";
    public static final String ON_STENCILSET = "stencilset";
    public static final String ON_NAMESPACE = "namespace";
    public static final String ON_PROCESS_AUTHOR = "process_author";
  }

  /**
   * ActYwResult.properties 默认值.
   */
  public static class RtPropertiesVal {
    /**
     * ActYwResult.properties.process_namespace 默认值.
     */
    public static final String RT_PROCESS_NAMESPACE = "http://www.activiti.org/processdef";
    public static final String RT_PROCESS_VERSION = "1.0";

  }

  /**
   * ActYwResult.bounds 默认值.
   */
  public static class RtBoundsVal {
    /**
     * ActYwResult.bounds.lowerRight.x 默认值.
     */
    public static final Double RT_BOUNDS_LOWERRIGHT_X = 1200.0;
    /**
     * ActYwResult.bounds.lowerRight.y 默认值.
     */
    public static final Double RT_BOUNDS_LOWERRIGHT_Y = 10500.0;
    /**
     * ActYwResult.bounds.upperLeft.x 默认值.
     */
    public static final Double RT_BOUNDS_UPPERLEFT_X = 0.0;
    /**
     * ActYwResult.bounds.upperLeft.y 默认值.
     */
    public static final Double RT_BOUNDS_UPPERLEFT_Y = 0.0;

    /**
     * 起始节点距离边缘位置间隔
     */
    public static final Double RT_ZERO = 50.0;

    /**
     * 任务节点之间间隔倍率
     */
    public static final Double RT_RATE = 1.0;

    /**
     * ActYwResult.最大列数.
     */
    public static final Integer RT_COLS_MAX = 5;
    /**
     * ActYwResult.每列宽度.
     */
    public static final Integer RT_COLS_WIDTH = 300;
    /**
     * ActYwResult.每列高.
     */
    public static final Integer RT_COLS_HEIGHT = 150;
  }

  /**
   * ActYwResult.stencilset 默认值.
   */
  public static class RtStencilsetVal {
    /**
     * ActYwResult.stencil.id 默认值.
     */
    public static final String RT_URL = "stencilsets/bpmn2.0/bpmn2.0.json";

    /**
     * ActYwResult.stencil.id 默认值.
     */
    public static final String RT_NAMESPACE = "http://b3mn.org/stencilset/bpmn2.0#";

  }

  /**
   * 等级常量类.
   * @author chenhao
   * @date 2017年6月28日 下午2:06:55
   *
   */
  public static class RtLevelVal {
    public static final String RT_LV0 = "0";
    public static final String RT_LV1 = "1";
    public static final String RT_LV2 = "2";
    public static final String RT_LV3 = "3";
    public static final String RT_LV4 = "4";
    public static final String RT_LV5 = "5";
    public static final String RT_LV6 = "6";
    public static final String RT_LV7 = "7";
    public static final String RT_LV8 = "8";
    public static final String RT_LV9 = "9";
  }
  /**
   * 节点类型常量类.
   * 字典：act_node_type.
   * @author chenhao
   * @date 2017年6月28日 下午2:06:55
   *
   */
  public static class RtTypeVal {
    public static final String RT_T0 = "0";//流程图形节点

    public static final String RT_T1 = "1";//立项审核
    public static final String RT_T2 = "2";//中期审核
    public static final String RT_T3 = "3";//结项审核
    public static final String RT_T4 = "4";//结果评定
    public static final String RT_T5 = "5";//学分认定
    public static final String RT_T10 = "10";//学分

    public static final String RT_T20 = "20";//网评
    public static final String RT_T30 = "30";//路演
    public static final String RT_T40 = "40";//评级

    public static final String RT_TBLACK = "100";//空白节点

  }

  /**
   * 节点类型必填项常量类.
   * 字典：act_node_require_type
   * 可选(任意个数)/必要(只有一个)/必要(至少一个)/没有或一个
   * @author chenhao
   * @date 2017年6月28日 下午2:06:55
   *
   */
  public static class RtRequireVal {
    public static final String RT_T1 = "1";//可选(任意个数)
    public static final String RT_T2 = "2";//必要(只有一个)
    public static final String RT_T3 = "3";//必要(至少一个)
    public static final String RT_T4 = "4";//没有或一个
  }
}
