/**
 * 
 */
package com.oseasy.com.pcore.common.supcan.treelist;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.oseasy.com.pcore.common.supcan.common.fonts.SupFont;
import com.oseasy.com.pcore.common.supcan.common.properties.SupProperties;
import com.oseasy.com.pcore.common.supcan.treelist.cols.SupGroup;

/**
 * 硕正TreeList注解
 * @author WangZhen

 * @see 在类上添加注解，应用实例：

@SupTreeList(
	properties=@SupProperties(headerFontIndex="2", curSelBgColor="#ccddcc",
		displayMask="backColor=if (name='管理员', '#ff0000', transparent)",
		expresses={
			@SupExpress(text="total=round(price*num, 2)"),
			@SupExpress(text="price=round(total/num, 4)")
	}),
	fonts={
		@SupFont(faceName="宋体", weight="400"),
		@SupFont(faceName="楷体", weight="700", height="-12"),
		@SupFont(faceName="楷体", weight="400", height="-12")}, 
	groups={
		@SupGroup(id="date", name="日期", headerFontIndex="1", sort=50),
		@SupGroup(id="date2", name="日期2", headerFontIndex="2", sort=60, parentId="date"),
		@SupGroup(id="date3", name="日期3", headerFontIndex="2", sort=70, parentId="date")
})

 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SupTreeList {
	
	/**
	 * 属性对象
	 */
	SupProperties properties() default @SupProperties;

	/**
	 * 字体对象
	 */
	SupFont[] fonts() default {};
	
	/**
	 * 列表头组
	 */
	SupGroup[] groups() default {};
	
}
