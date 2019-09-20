/**
 * .
 */

package com.oseasy.com.common.utils;

import java.util.List;

import com.oseasy.com.common.config.Sval;

/**
 * 项目模块基础缓存标识.
 * @author chenhao
 */
public interface ICkey {
    /**
     * 模块惟一标识（项目包名、子项目包名、模块包名）.
     * @return String
     */
    public String mkey();
    public String subkey();
    public Sval.Emkey emkey();
    public List<CkeyMvo> mkeys();

    /**
     * 获取所有子模块.
     */
    public List<CkeyMsvo> mskeys();

    /**
     * 获取当前Key模块.
     */
    public CkeyMvo mkey(String key);
    /**
     * 获取当前模块.
     */
    public CkeyMvo curmkey();
}
