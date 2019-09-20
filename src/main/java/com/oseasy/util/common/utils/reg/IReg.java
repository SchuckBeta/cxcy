/**
 * .
 */

package com.oseasy.util.common.utils.reg;

/**
 * .
 * @author chenhao
 *
 */
public interface IReg {
    /**
     * 规则标识.
     */
    public RegType key();

    /**
     * 校验参数是否合法.
     */
    public Boolean check(RegVo regVo);

    /**
     * 生成并初始化RegeVo.
     */
    public RegVo gen(RegVo regVo);

    /**
     * 校验参数是否符合规则.
     */
    public boolean validate(RegVo regVo, float val);
}
