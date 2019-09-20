/**
 * .
 */

package com.oseasy.pie.modules.iep.tool.impl;

import org.apache.log4j.Logger;

import com.oseasy.pie.common.config.PieSval;
import com.oseasy.pie.modules.iep.entity.IepTpl;
import com.oseasy.pie.modules.iep.tool.IeAbsTpl;
import com.oseasy.pie.modules.iep.vo.TplFType;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;


/**
 * 模板类.
 * @author chenhao
 */
public class IeTpl extends IeAbsTpl{
    public final static Logger logger = Logger.getLogger(IeTpl.class);

    public IeTpl() {
        super();
    }

    public IeTpl(IepTpl iepTpl, String root) {
        super();
        if(iepTpl.getParent() != null){
            this.fname = iepTpl.getParent().getName();
        }

        if(iepTpl.isError()){
            if(StringUtil.isNotEmpty(iepTpl.getEpath())){
                this.path = PieSval.VIEWS_TEMPLATE_IMP + iepTpl.getEpath();
                this.tplName = FileUtil.getFileName(iepTpl.getEpath());
                this.rootPath = root + (this.path).replace(this.tplName, StringUtil.EMPTY);
            }
            this.fname += "错误模板";
        }else{
            if(StringUtil.isNotEmpty(iepTpl.getPath())){
                this.path = PieSval.VIEWS_TEMPLATE_IMP + iepTpl.getPath();
                this.tplName = FileUtil.getFileName(iepTpl.getPath());
                this.rootPath = root + (this.path).replace(this.tplName, StringUtil.EMPTY);
            }
            this.fname += "模板";
        }

        if((TplFType.EXCEL_XLS.getKey()).equals(iepTpl.getFtype())){
            this.fname += TplFType.EXCEL_XLS.getPostfix();
        }else if((TplFType.EXCEL_XLSX.getKey()).equals(iepTpl.getFtype())){
            this.fname += TplFType.EXCEL_XLSX.getPostfix();
        }else if((TplFType.WORD.getKey()).equals(iepTpl.getFtype())){
            this.fname += TplFType.WORD.getPostfix();
        }
    }
}
