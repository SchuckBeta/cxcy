/**
 * .
 */

package com.oseasy.com.pcore.common.persistence.annotation;

import java.lang.annotation.*;

import com.oseasy.com.pcore.common.config.CoreSval.Const;


@Documented
@Inherited
@Target({ ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckToken{
    public String value() default Const.YES;//1为需要检验 0为不需要
}
