/**
 * .
 */

package com.oseasy.com.common.utils;

import com.oseasy.util.common.utils.StringUtil;


/**
 * 系统基础缓存标识定义.
 * @author chenhao
 */
public class CkeyUtil {
    /**
     * 页面modules路径.
     * @return String
     */
    public static String ckeys(String tid){
        return tid + StringUtil.MAOH;
    }

    /**
     * webapp\WEB-INF\views\modules\getMkey()
     */
    public static String ckeys(ICkey path){
        if(StringUtil.isEmpty(path.subkey())){
            return path.mkey();
        }
        return path.mkey() + StringUtil.LINE_D + path.subkey();
    }

    /**
     * webapp\WEB-INF\views\modules\getMkey()\getMskey().
     */
    public static String ckeys(ICkey path, String mskey){
        return ckeys(path) + StringUtil.MAOH + mskey + StringUtil.MAOH;
    }

    /**
     * webapp\WEB-INF\views\modules\getMkey()
     */
    public static String ckeys(String tid, ICkey path){
        if(StringUtil.isEmpty(path.subkey())){
            return ckeys(tid) + path.mkey();
        }
        return path.mkey() + StringUtil.LINE_D + path.subkey() + StringUtil.MAOH + ckeys(tid);
    }

    /**
     * webapp\WEB-INF\views\modules\getMkey()\getMskey().
     */
    public static String ckeys(String tid, ICkey path, String mskey){
        return ckeys(path, mskey) + tid;
    }
}
