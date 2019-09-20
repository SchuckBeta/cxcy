/**
 * .
 */

package com.oseasy.util.common.utils.jdbc.impl;

import com.oseasy.util.common.utils.jdbc.DbSval;

/**
 * JDBC数据库默认连接类.
 * @author chenhao
 */
public class DbDefault extends DbAbs{
    private static final String JDBC_KEY = "deft";
    private static final String JDBC_ID_DEFAULT = "1";
    private static final String JDBC_USERNAME_DEFAULT = "root";
    private static final String JDBC_PASSWORD_DEFAULT = "123456";
    private static final String JDBC_DRIVER_DEFAULT = "com.mysql.jdbc.Driver";
    private static final String JDBC_URL_DEFAULT = "jdbc:mysql://127.0.0.1:3306/"+JDBC_KEY+"?useUnicode=true&characterEncoding=utf-8";

    @Override
    public String getKey() {
        return JDBC_KEY;
    }

    @Override
    public String getDbDriver() {
        return DbSval.getDbDriver(getKey(), JDBC_DRIVER_DEFAULT);
    }

    @Override
    public String getDbUrl() {
        return DbSval.getDbUrl(getKey(), JDBC_URL_DEFAULT);
    }

    @Override
    public String getDbUsername() {
        return DbSval.getDbUrl(getKey(), JDBC_USERNAME_DEFAULT);
    }

    @Override
    public String getDbPassword() {
        return DbSval.getDbUrl(getKey(), JDBC_PASSWORD_DEFAULT);
    }

    @Override
    public String getId() {
        return DbSval.getDbUrl(getKey(), JDBC_ID_DEFAULT);
    }
}
