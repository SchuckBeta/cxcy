/**
 * .
 */

package com.oseasy.com.common.utils;

import java.util.List;

import com.oseasy.com.common.config.Sval;

/**
 * 项目模块基础路径.
 * @author chenhao
 */
public interface IPath {
    /**
     * 模块惟一标识（项目包名、子项目包名、模块包名）.
     * com.oseasy.getMkey()...
     * mappings.getMkey()...
     * webapp\WEB-INF\views\modules\getMkey()
     * @return String
     */
    public String mkey();
    public String subkey();
    public Sval.Emkey emkey();
    public List<PathMvo> mkeys();

    /**
     * 获取所有子模块.
     */
    public List<PathMsvo> mskeys();

    /**
     * 获取当前Key模块.
     */
    public PathMvo mkey(String key);
    /**
     * 获取当前模块.
     */
    public PathMvo curmkey();
}
