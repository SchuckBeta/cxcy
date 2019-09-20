/**
 * .
 */

package com.oseasy.com.pcore.common.persistence.annotation;

import java.lang.reflect.Method;

import com.oseasy.util.common.utils.Reflections;

/**
 * 分页忽略统计查询条数注解检查工具类.
 * @author chenhao
 *
 */
public class PageNutil {
    public static void main(String[] args) {
        System.out.println(check("com.oseasy.com.pcore.common.persistence.PaginationA", "testPage"));
    }

    /**
     * 检查类指定方法是否存在@PageNcount注解.
     * @param cstr 类路径
     * @param mstr 方法签名
     */
    public static boolean check(String className, String mstr) {
        try {
            Method[] methods =Reflections.getMethods(className);
            if((methods == null) || (methods.length <= 0)){
                return false;
            }

            for (Method method : methods) {
                if((method == null) || (!(method.toGenericString().contains(mstr)))){
                    continue;
                }

                // 如果此方法有注解，就把注解里面的数据赋值到user对象
                if (method.isAnnotationPresent(PageNcount.class) && (method.getAnnotation(PageNcount.class) != null)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
