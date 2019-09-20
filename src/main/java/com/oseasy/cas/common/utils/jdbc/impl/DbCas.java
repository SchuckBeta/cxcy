/**
 * .
 */

package com.oseasy.cas.common.utils.jdbc.impl;

import com.oseasy.util.common.utils.jdbc.impl.DbAbs;

/**
 * JDBC数据库默认连接类.
 * @author chenhao
 */
public class DbCas extends DbAbs{
    private static final String JDBC_KEY = "cas";
    private static final DbCas dbCas = new DbCas();

    private DbCas() {
        super();
    }
    @Override
    public String getKey() {
        return JDBC_KEY;
    }

    public static DbCas init() {
        return dbCas;
    }
}
