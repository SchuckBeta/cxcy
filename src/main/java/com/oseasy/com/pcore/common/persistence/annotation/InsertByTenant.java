/**
 * .
 */

package com.oseasy.com.pcore.common.persistence.annotation;

import java.lang.annotation.*;

/**
 * .
 * @author chenhao
 *
 */
@Documented
@Inherited
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface InsertByTenant
{
    public String value() default "";
}
