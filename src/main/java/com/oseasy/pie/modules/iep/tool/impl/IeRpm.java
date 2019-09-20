/**
 * .
 */

package com.oseasy.pie.modules.iep.tool.impl;

import com.oseasy.pie.modules.iep.tool.IeAbsPparam;

/**
 * .
 * @author chenhao
 *
 */
public class IeRpm extends IeAbsPparam{
    @Override
    public String getkey() {
        return "id";
    }

    /**
     * 生成参数.
     */
    public static String genParam(IeRpm param) {
       return "&referrer="+ param.getReferrer();
    }
}
