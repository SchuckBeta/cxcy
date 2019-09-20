/**
 * .
 */

package com.oseasy.com.pcore.common.persistence.annotation;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.Reflections;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 分页忽略统计查询条数注解检查工具类.
 * @author chenhao
 *
 */
public class InsertByTenantUtil {

    /**
     * 检查类指定方法是否存在@InsertByTenant注解.
     * @param mstr 方法签名
     */
    public static boolean check(String className, String mstr) {
        try {
            List<String> classNameList = Lists.newArrayList();
            classNameList.add(className);
            classNameList.add("com.oseasy.com.pcore.common.persistence.CrudDao");
            for(String classNames : classNameList){
                Method[] methods =Reflections.getMethods(classNames);
                if((methods == null) || (methods.length <= 0)){
                    return false;
                }
                for (Method method : methods) {
                    if((method == null) || (!(method.toGenericString().contains(mstr)))){
                        continue;
                    }
                    // 如果此方法有注解，就把注解里面的数据赋值到user对象
                    if (method.isAnnotationPresent(InsertByTenant.class) && (method.getAnnotation(InsertByTenant.class) != null)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
