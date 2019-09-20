/**
 *
 */
package com.oseasy.com.pcore.common.persistence.interceptor;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenantUtil;
import com.oseasy.com.pcore.modules.sys.entity.User;
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

import java.util.Properties;

/**
 * 多租户
 * @author poplar.yfyang / oseasy
 * @version 2013-8-28
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
       })
public class InsertinationInterceptor extends BaseInterceptor {

    private static final long serialVersionUID = 1L;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String id = mappedStatement.getId();
        Object parameter = null;
        BoundSql boundSql = null;
        String originalSql ="";
        if(StringUtil.isNotEmpty(id)){
            Integer idx = id.lastIndexOf(".");
            String dao = id.substring(0, idx);
            String method = id.substring(idx + 1, id.length());
            parameter = invocation.getArgs()[1];
            boundSql = mappedStatement.getBoundSql(parameter);
            originalSql = boundSql.getSql().trim();
            Object parameterObject = boundSql.getParameterObject();
            //判断多租户开关是否打开
            if(CoreSval.getTenantIsopen()){
                //判断方法中是否有@InsertByTenant标签

//                String curTenant = curTenant(parameterObject);
                originalSql = getOriginalSqlString(originalSql, dao, method,parameterObject);


            }else{
                //当租户开关为关闭状态时，为单节点校平台部署模式，租户id为默认的0000
                String curTenant = CoreSval.Const.DEFAULT_SCHOOL_TENANTID;
                originalSql = getOriginalSqlString(originalSql, dao, method, curTenant);
            }
            BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), originalSql, boundSql.getParameterMappings(), parameterObject);
            if (Reflections.getFieldValue(boundSql, "metaParameters") != null) {
                MetaObject mo = (MetaObject) Reflections.getFieldValue(boundSql, "metaParameters");
                Reflections.setFieldValue(newBoundSql, "metaParameters", mo);
            }
            MappedStatement newMs = copyFromMappedStatement(mappedStatement, new PaginationInterceptor.BoundSqlSqlSource(newBoundSql));
            invocation.getArgs()[0] = newMs;
        }
        return invocation.proceed();
    }

    private String getOriginalSqlString(String originalSql, String dao, String method, Object parameterObject) {
        String ten = null;
        if(InsertByTenantUtil.check(dao, method)){
            String curTenant = curTenant(parameterObject);
            //重新拼装插入sql,在第一个左括号后面加参数tenant_id，最后一个左括号后面加值
            if(StringUtil.isNotEmpty(curTenant)){
                StringBuilder stringBuilder2=new StringBuilder(originalSql.toLowerCase());
                stringBuilder2.insert(stringBuilder2.indexOf("(")+1,"tenant_id,");
                String tenantId = "'"+ curTenant+"',";
                String originalSqlOld = stringBuilder2.toString();
                String originalSqlBegin = originalSqlOld.substring(0,originalSqlOld.indexOf("values"));
                String originalSqlNext = originalSqlOld.substring(originalSqlOld.indexOf("values")).replace("(","("+tenantId);
                originalSql = originalSqlBegin + originalSqlNext;
                ten = curTenant;
            }
        }

        if(originalSql.contains("sys_dict") && !isTplTenant(ten) && StringUtil.isNotEmpty(ten)){
            originalSql = originalSql.replace("sys_dict","sys_dict"+"_"+ten);
        }
        return originalSql;
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
