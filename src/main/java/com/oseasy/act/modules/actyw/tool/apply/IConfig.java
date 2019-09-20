/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

/**
 * 流程配置实体接口.
 * @author chenhao
 *
 */
public interface IConfig {
    public final static String I_PTYPE = "ptype";
    /**
     * 流程配置ID.
     * @return String
     */
    String id();

    /**
     * 流程配置标识字符.
     * @return String
     */
    String keyss();

    /**
     * 流程项目类型.
     * @return String
     */
    String ptype();
    /**
     * 设置流程项目类型.
     * @return String
     */
    String ptype(String ptype);
}
