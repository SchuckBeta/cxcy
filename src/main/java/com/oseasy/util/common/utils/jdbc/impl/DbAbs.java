/**
 * .
 */

package com.oseasy.util.common.utils.jdbc.impl;

import com.oseasy.util.common.utils.jdbc.DbInterface;
import com.oseasy.util.common.utils.jdbc.DbSval;

/**
 * JDBC数据库默认连接类.
 * @author chenhao
 */
public abstract class DbAbs implements DbInterface{

    @Override
    public abstract String getKey();

    @Override
    public String getDbDriver() {
        return DbSval.getDbDriver(getKey(), null);
    }

    @Override
    public String getDbUrl() {
        return DbSval.getDbUrl(getKey(), null);
    }

    @Override
    public String getDbUsername() {
        return DbSval.getDbUsername(getKey(), null);
    }

    @Override
    public String getDbPassword() {
        return DbSval.getDbPassword(getKey(), null);
    }

    @Override
    public String getId() {
        return DbSval.getId(getKey(), null);
    }
}
