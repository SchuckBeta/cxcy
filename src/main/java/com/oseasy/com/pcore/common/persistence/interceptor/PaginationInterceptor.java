/**
 *
 */
package com.oseasy.com.pcore.common.persistence.interceptor;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.persistence.annotation.FindDictByTenantUtil;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenantUtil;
import com.oseasy.com.pcore.common.persistence.annotation.PageNutil;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.Reflections;
import com.oseasy.util.common.utils.StringUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;


/**
 * 数据库分页插件，只拦截查询语句.
 * @author poplar.yfyang / oseasy
 * @version 2013-8-28
 */
@Intercepts({@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PaginationInterceptor extends BaseInterceptor {

    private static final long serialVersionUID = 1L;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        boolean isPass = true;
        String id = mappedStatement.getId();
        Object parameter = null;
        BoundSql boundSql = null;
        Object parameterObject = null;
        String originalSql ="";
        if(StringUtil.isNotEmpty(id)){
            Integer idx = id.lastIndexOf(".");
            String dao = id.substring(0, idx);
            String method = id.substring(idx + 1, id.length());
            if(PageNutil.check(dao, method)){
                isPass = false;
            }
            parameter = invocation.getArgs()[1];
            boundSql = mappedStatement.getBoundSql(parameter);
            parameterObject = boundSql.getParameterObject();
            originalSql = boundSql.getSql().trim();
            //判断多租户开关是否打开
            if(CoreSval.getTenantIsopen()){
                //判断方法中是否有@FindListByTenant标签
                if(FindListByTenantUtil.check(dao, method)){
                    String curTenant = curTenant(parameterObject);
                    if(StringUtil.isNotEmpty(curTenant)){
                        if(originalSql.contains("where") ||originalSql.contains("WHERE")){
                            originalSql = originalSql.replaceAll("WHERE","where");
                            originalSql = originalSql.replaceAll("where"," where a.tenant_id = '" + curTenant + "' and ");
                        }

                    }

                }
                if(FindDictByTenantUtil.check(dao, method)) {
                    String curTenant = curTenant(parameterObject);
                    if(originalSql.contains("sys_dict") && !isTplTenant(curTenant) && StringUtil.isNotEmpty(curTenant)){
                        originalSql = originalSql.replace("sys_dict","sys_dict"+"_"+curTenant);
                    }
                }

            }else{
                //当租户开关为关闭状态时，为单节点校平台部署模式，租户id为默认的0000
                String curTenant = CoreSval.Const.DEFAULT_SCHOOL_TENANTID;
                if(FindListByTenantUtil.check(dao, method)){
                    if(StringUtil.isNotEmpty(curTenant)){
                        originalSql = originalSql.toLowerCase().replaceAll("where"," where a.tenant_id = '" + curTenant + "' and ");
                    }
                }
                if(FindDictByTenantUtil.check(dao, method)) {
                    if(originalSql.contains("sys_dict") && !isTplTenant(curTenant) && StringUtil.isNotEmpty(curTenant)){
                        originalSql = originalSql.replace("sys_dict","sys_dict"+"_"+curTenant);
                    }
                }
            }
            BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), originalSql, boundSql.getParameterMappings(), parameterObject);
            if (Reflections.getFieldValue(boundSql, "metaParameters") != null) {
                MetaObject mo = (MetaObject) Reflections.getFieldValue(boundSql, "metaParameters");
                Reflections.setFieldValue(newBoundSql, "metaParameters", mo);
            }
            //解决MyBatis 分页foreach 参数失效 end
            MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));
            invocation.getArgs()[0] = newMs;



        }
        if (isPass) {
            //获取分页参数对象
            Page<Object> page = null;
            if (parameterObject != null) {
                page = convertParameter(parameterObject, page);
            }
            if (StringUtil.isBlank(boundSql.getSql())) {
                return null;
            }
            //如果设置了分页对象，则进行分页
            if (page != null && page.getPageSize() != -1) {

                //得到总记录数
                page.setCount(SQLHelper.getCount(originalSql, null, mappedStatement, parameterObject, boundSql, log));

                //分页查询 本地化对象 修改数据库注意修改实现
                String pageSql = SQLHelper.generatePageSql(originalSql, page, curDialect);
//                if (log.isDebugEnabled()) {
//                    log.debug("PAGE SQL:" + StringUtils.replace(pageSql, "\n", ""));
//                }
                invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
                BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), pageSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
                //解决MyBatis 分页foreach 参数失效 start
                if (Reflections.getFieldValue(boundSql, "metaParameters") != null) {
                    MetaObject mo = (MetaObject) Reflections.getFieldValue(boundSql, "metaParameters");
                    Reflections.setFieldValue(newBoundSql, "metaParameters", mo);
                }
                //解决MyBatis 分页foreach 参数失效 end
                MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));

                invocation.getArgs()[0] = newMs;
            }

        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        super.initProperties(properties);
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms,
                                                    SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(),
                ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null) {
            for (String keyProperty : ms.getKeyProperties()) {
                builder.keyProperty(keyProperty);
            }
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
    private Boolean isTplTenant(String tenantId){
        if(StringUtil.isNotEmpty(tenantId)){
            for(String tenant : CoreIds.filterTenantNoProvinceByNsAdmin()){
                if(tenant.equals(tenantId)){
                    return true;
                }
            }
        }
        return false;
    }
}
