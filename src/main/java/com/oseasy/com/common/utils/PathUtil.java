/**
 * .
 */

package com.oseasy.com.common.utils;

import com.oseasy.com.common.config.Sval;
import com.oseasy.util.common.utils.StringUtil;


/**
 * 系统基础路径定义.
 * @author chenhao
 */
public class PathUtil {
    /**
     * 项目基础包.
     * @return String
     */
    public static String packge(){
        return Sval.BASE_PACKAGE;
    }

    /**
     * 页面根路径.
     * @return String
     */
    public static String view(){
        return Sval.WEB_INF_VIEWS + StringUtil.LINE;
    }

    /**
     * 页面mappings路径.
     * @return String
     */
    public static String mappings(){
        return Sval.MAPPINGS + StringUtil.LINE;
    }

    /**
     * 页面modules路径.
     * @return String
     */
    public static String vmodules(){
        return Sval.VIEWS_MODULES + StringUtil.LINE;
    }

    /**
     * 页面site路径.
     * @return String
     */
    public static String vsites(){
        return Sval.VIEWS_SITES + StringUtil.LINE;
    }

    /**
     * 页面template路径.
     * @return String
     */
    public static String vtemplate(){
        return Sval.VIEWS_TEMPLATE + StringUtil.LINE;
    }

    /**
     * 模块惟一标识（模块包名、子项目包名）.
     * com.oseasy.getMkey()...
     */
    public static String packge(IPath path){
        if(StringUtil.isEmpty(path.subkey())){
            return packge() + StringUtil.DOT + path.mkey();
        }
        return packge() + StringUtil.DOT + path.mkey() + StringUtil.DOT + path.subkey();
    }

    /**
     * 模块惟一标识（模块包名、子项目包名）.
     * com.oseasy.getMkey().getMskey()..
     */
    public static String packge(IPath path, String mskey){
        return packge(path) + StringUtil.DOT + mskey;
    }

    /**
     * mappings/getMkey()...
     */
    public static String mappings(IPath path){
        if(StringUtil.isEmpty(path.subkey())){
            return mappings() + path.mkey() + StringUtil.LINE;
        }
        return mappings() + path.mkey() + StringUtil.LINE + path.subkey() + StringUtil.LINE;
    }

    /**
     * mappings/getMkey()/getMskey()...
     */
    public static String mappings(IPath path, String mskey){
        return mappings(path) + mskey + StringUtil.LINE ;
    }

    /**
     * webapp\WEB-INF\views\modules\getMkey()
     */
    public static String vmodules(IPath path){
        if(StringUtil.isEmpty(path.subkey())){
            return vmodules() + path.mkey() + StringUtil.LINE;
        }
        return vmodules() + path.mkey() + StringUtil.LINE + path.subkey() + StringUtil.LINE;
    }

    /**
     * webapp\WEB-INF\views\modules\getMkey()\getMskey().
     */
    public static String vmodules(IPath path, String mskey){
        return vmodules(path) + mskey + StringUtil.LINE;
    }


    /**
     * webapp\WEB-INF\views\sites\***\getMkey()
     */
    public static String vsites(IPath path, String app){
        if(StringUtil.isEmpty(path.subkey())){
            if(StringUtil.isEmpty(app)){
                return vsites();
            }
            return vsites() + app + StringUtil.LINE;
        }

        if(StringUtil.isEmpty(app)){
            return vsites() + path.subkey() + StringUtil.LINE;
        }
        return vsites() + path.subkey() + StringUtil.LINE + app + StringUtil.LINE;
    }

    /**
     * webapp\WEB-INF\views\sites\***\getMkey()\getMskey().
     */
    public static String vsites(IPath path, String mskey, String app){
        if(StringUtil.isEmpty(app)){
            return vsites(path, null) + mskey + StringUtil.LINE;
        }
        return vsites() + mskey + StringUtil.LINE + app + StringUtil.LINE;
    }
}
