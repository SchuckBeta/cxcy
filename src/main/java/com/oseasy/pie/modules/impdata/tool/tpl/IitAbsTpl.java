/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.tpl;

import org.apache.log4j.Logger;

import com.oseasy.pie.modules.iep.tool.IeAbsTpl;
import com.oseasy.util.common.utils.FileUtil;

/**
 * 抽象模板类.
 *
 * @author chenhao
 */
public class IitAbsTpl extends IeAbsTpl{
    public final static Logger logger = Logger.getLogger(IitAbsTpl.class);

    public String getClassPath(String path) {
        String clzzPath = this.getClass().getResource(FileUtil.LINE).getPath();
        return clzzPath + path;
    }

    public String getWebPath(String path) {
        String clzzPath = this.getClass().getResource(FileUtil.LINE).getPath();
        return clzzPath.replace(FileUtil.WEB_INF_CLASSES, "") + path;
    }

    public IitAbsTpl() {
        super();
    }
    public IitAbsTpl(String rootPath) {
        super();
        this.rootPath = rootPath;
    }
    public IitAbsTpl(String rootPath, String tplName) {
        super();
        this.rootPath = rootPath;
        this.tplName = tplName;
    }
    public IitAbsTpl(String rootPath, String fname, String tplName) {
        super();
        this.rootPath = rootPath;
        this.fname = fname;
        this.tplName = tplName;
    }
}
