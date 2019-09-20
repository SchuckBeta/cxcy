/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程业务实体接口.
 * @author chenhao
 *
 */
public interface IActYw {
    /**
     * 自定义流程申请对象KEY.
     */
    public final static String IACTY = "iactyw";
    public final static String IACTYW_ID = "actywId";
    public static final String KEY_SEPTOR = "_";
    public static final String KEY_SEPTOR_PREFIX = "F_";

    public static String getPkey(IActYw actYw) {
        if (actYw == null) {
            return StringUtil.EMPTY;
        }

        if ((actYw.group() == null) || (StringUtil.isEmpty(actYw.group().keyss()))) {
            return StringUtil.EMPTY;
        }

        if ((actYw.config() == null) || (StringUtil.isEmpty(actYw.config().keyss()))) {
            return StringUtil.EMPTY;
        }
        return KEY_SEPTOR_PREFIX + actYw.config().keyss() + KEY_SEPTOR + actYw.group().keyss();
    }



    String id();
    String id(String id);
    IGroup group();
    IGroup group(IGroup group);
    String ptype();
    FlowType flowType();
    FormTheme theme();
    IConfig config();
    IConfig config(IConfig config);
}
