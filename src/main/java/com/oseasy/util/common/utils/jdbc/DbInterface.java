/**
 * .
 */

package com.oseasy.util.common.utils.jdbc;

/**
 * DB数据库连接实例接口.
 * @author chenhao
 *
 */
public interface DbInterface {
    /**
     * 获取DB数据库标识.
     */
    public String getKey();
    /**
     * 获取DB驱动.
     */
    public String getDbDriver();
    /**
     * 获取DB URL.
     */
    public String getDbUrl();
    /**
     * 获取DB 用户名.
     */
    public String getDbUsername();
    /**
     * 获取DB 密码.
     */
    public String getDbPassword();
    /**
     * 获取ID.
     */
    public String getId();
}
