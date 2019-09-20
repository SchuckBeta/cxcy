/**
 * .
 */

package com.oseasy.util.common.utils.jdbc;

import java.util.Map;

import com.google.common.collect.Maps;
import com.oseasy.util.common.utils.PropertiesLoader;
import com.oseasy.util.common.utils.StringUtil;

/**
 * oe-license常量类.
 * @author chenhao
 *
 */
public class DbSval {
    private static final String DB_PROPERTIES = "db.properties";
    private static final String JDBC_ID = "jdbc.id";
    private static final String JDBC_DRIVER = "jdbc.driver";
    private static final String JDBC_URL = "jdbc.url";
    private static final String JDBC_PASSWORD = "jdbc.password";
    private static final String JDBC_USERNAME = "jdbc.username";

    /**
     * 当前对象实例.
     */
    private static DbSval globalLicense = new DbSval();

    /**
     * 保存全局属性值.
     */
    private static Map<String, String> mapLicense = Maps.newHashMap();

    /**
     * 属性文件加载对象.
     */
    private static PropertiesLoader license = new PropertiesLoader(DB_PROPERTIES);

    /**
     * 获取当前对象实例.
     */
    public static DbSval getInstance() {
        return globalLicense;
    }

    /**
     * 获取配置.
     *
     */
    public static String getConfig(String key) {
        String value = mapLicense.get(key);
        if (value == null) {
            value = license.getProperty(key);
            mapLicense.put(key, value != null ? value : StringUtil.EMPTY);
        }
        return value;
    }

    /**
     * 获取DB驱动.
     */
    public static String getDbDriver(String key, String deft) {
        String value = getConfig(StringUtil.isEmpty(key) ? (JDBC_DRIVER) : (key + StringUtil.DOT + JDBC_DRIVER));
        return StringUtil.isEmpty(value) ? deft : value;
    }
    /**
     * 获取DB URL.
     */
    public static String getDbUrl(String key, String deft) {
        String value = getConfig(StringUtil.isEmpty(key) ? (JDBC_URL) : (key + StringUtil.DOT + JDBC_URL));
        return StringUtil.isEmpty(value) ? deft : value;
    }
    /**
     * 获取DB 用户名.
     */
    public static String getDbUsername(String key, String deft) {
        String value = getConfig(StringUtil.isEmpty(key) ? (JDBC_USERNAME) : (key + StringUtil.DOT + JDBC_USERNAME));
        return StringUtil.isEmpty(value) ? deft : value;
    }
    /**
     * 获取DB 密码.
     */
    public static String getDbPassword(String key, String deft) {
        String value = getConfig(StringUtil.isEmpty(key) ? (JDBC_PASSWORD) : (key + StringUtil.DOT + JDBC_PASSWORD));
        return StringUtil.isEmpty(value) ? deft : value;
    }

    /**
     * 获取ID.
     */
    public static String getId(String key, String deft) {
        String value = getConfig(StringUtil.isEmpty(key) ? (JDBC_ID) : (key + StringUtil.DOT + JDBC_ID));
        return StringUtil.isEmpty(value) ? deft : value;
    }
}
