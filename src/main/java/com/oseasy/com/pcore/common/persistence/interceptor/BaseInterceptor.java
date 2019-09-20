/**
 *
 */

package com.oseasy.com.pcore.common.persistence.interceptor;

import java.io.Serializable;
import java.util.Properties;

import com.oseasy.com.pcore.common.persistence.BaseEntity;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.sys.vo.TenantCvtype;
import com.oseasy.util.common.utils.StringUtil;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.persistence.dialect.Dialect;
import com.oseasy.com.pcore.common.persistence.dialect.db.DB2Dialect;
import com.oseasy.com.pcore.common.persistence.dialect.db.DerbyDialect;
import com.oseasy.com.pcore.common.persistence.dialect.db.H2Dialect;
import com.oseasy.com.pcore.common.persistence.dialect.db.HSQLDialect;
import com.oseasy.com.pcore.common.persistence.dialect.db.MySQLDialect;
import com.oseasy.com.pcore.common.persistence.dialect.db.OracleDialect;
import com.oseasy.com.pcore.common.persistence.dialect.db.PostgreSQLDialect;
import com.oseasy.com.pcore.common.persistence.dialect.db.SQLServer2005Dialect;
import com.oseasy.com.pcore.common.persistence.dialect.db.SybaseDialect;
import com.oseasy.util.common.utils.Reflections;



/**
 * Mybatis分页拦截器基类
 *
 * @author poplar.yfyang / oseasy
 * @version 2013-8-28
 */
public abstract class BaseInterceptor implements Interceptor, Serializable {

  private static final long serialVersionUID = 1L;

  protected static final String PAGE = "page";
  protected static final String TENANT_ID = "tenantId";

  protected static final String DELEGATE = "delegate";

  protected static final String MAPPED_STATEMENT = "mappedStatement";

  protected transient Log log = LogFactory.getLog(this.getClass());

  private static Logger logger = LoggerFactory.getLogger(BaseInterceptor.class);

  protected transient Dialect curDialect;

  // /**
  // * 拦截的ID，在mapper中的id，可以匹配正则
  // */
  // protected String _SQL_PATTERN = "";

  /**
   * 对参数进行转换和检查.
   *
   * @param parameterObject
   *          参数对象
   * @param page
   *          分页对象
   * @return 分页对象
   * @throws NoSuchFieldException
   *           无法找到参数
   */
  @SuppressWarnings("unchecked")
  protected static Page<Object> convertParameter(Object parameterObject, Page<Object> page) {
    try {
      if (parameterObject instanceof Page) {
        return (Page<Object>) parameterObject;
      } else {
        if (Reflections.hasField(parameterObject, PAGE)) {
          return (Page<Object>) Reflections.getFieldValue(parameterObject, PAGE);
        }
        return null;
      }
    } catch (Exception e) {
      logger.warn("convert Parameter NoSuchFieldException:" + e.getMessage());
      return null;
    }
  }

  protected static String convertParameter(Object parameterObject) {
    try {
      if (parameterObject instanceof BaseEntity) {
        return (String) Reflections.getFieldValue(parameterObject, TENANT_ID);
      } else {
        if (Reflections.hasField(parameterObject, TENANT_ID)) {
          return (String) Reflections.getFieldValue(parameterObject, TENANT_ID);
        }
        return null;
      }
    } catch (Exception e) {
      logger.warn("convert Parameter NoSuchFieldException:" + e.getMessage());
      return null;
    }
  }

  /**
   * 获取当前租户.
   * 1、若为空，默认取参数实体中的租户ID,
   * 2、若为空，然后取User缓存Key中的租户ID,
   * 3、若为空，最后取域名端口缓存Key中的租户ID.
   * @param parameterObject
   * @return
     */
  protected static String curTenant(Object parameterObject) {
    String curTenant = null;
    /**
     * 获取属性租户ID.
     */
    if (parameterObject != null) {
      curTenant = convertParameter(parameterObject);
    }

    if(StringUtil.isEmpty(curTenant)){
      curTenant = TenantConfig.getCacheTenant();
    }
    return curTenant;
  }

  /**
   * 设置属性，支持自定义方言类和制定数据库的方式 <code>dialectClass</code>,自定义方言类。可以不配置这项 <ode>dbms</ode> 数据库类型，插件支持的数据库.
   * <code>sqlPattern</code> 需要拦截的SQL ID
   *
   * @param p
   *          属性
   */
  protected void initProperties(Properties p) {
    Dialect dialect = null;
    String dbType = null;
    if (p == null) {
      dbType = CoreSval.getDbName();
    }else{
      dbType = "mysql";
    }
    if ("db2".equals(dbType)) {
      dialect = new DB2Dialect();
    } else if ("derby".equals(dbType)) {
      dialect = new DerbyDialect();
    } else if ("h2".equals(dbType)) {
      dialect = new H2Dialect();
    } else if ("hsql".equals(dbType)) {
      dialect = new HSQLDialect();
    } else if ("mysql".equals(dbType)) {
      dialect = new MySQLDialect();
    } else if ("oracle".equals(dbType)) {
      dialect = new OracleDialect();
    } else if ("postgre".equals(dbType)) {
      dialect = new PostgreSQLDialect();
    } else if ("mssql".equals(dbType) || "sqlserver".equals(dbType)) {
      dialect = new SQLServer2005Dialect();
    } else if ("sybase".equals(dbType)) {
      dialect = new SybaseDialect();
    }
    if (dialect == null) {
      throw new RuntimeException("mybatis dialect error.");
    }
    curDialect = dialect;
    // _SQL_PATTERN = p.getProperty("sqlPattern");
    // _SQL_PATTERN = CoreSval.getConfig("mybatis.pagePattern");
    // if (StringUtils.isEmpty(_SQL_PATTERN)) {
    // throw new RuntimeException("sqlPattern property is not found!");
    // }
  }
}
