/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.pie.modules.impdata.tool.tpl.IitAbsTpl;
import com.oseasy.pro.modules.workflow.enums.ExpType;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
public interface IitTpl<T> {
    public static Logger logger = Logger.getLogger(IitTpl.class);

    /**
     * 获取模板文件绝对路径.
     */
    public static String getFpath(HttpServletRequest request, String fname) {
        return getFpath(request, ExpType.TPL_ROOT_STATICEXCELTEMPLATE, fname);
    }
    public static String getFpath(HttpServletRequest request, String rootpath, String fname) {
        return SpringContextHolder.getWebPath(request, rootpath + fname);
    }

    /**
     * 根据Yw获取模板文件名称-旧版使用.
     * @param absTpl 模板参数，避免参数丢失时可以传递该对象
     * @param actyw 业务流程
     * @param rootPath 根路径
     * @param tplName 模板名
     * @return IitAbsTpl
     */
    public static IitAbsTpl getTplByYwName(HttpServletRequest request, IitAbsTpl absTpl, ActYw actyw) {
        if(absTpl == null){
            absTpl = new IitAbsTpl();
        }

        FormTheme ftheme =  actyw.getFtheme();
        FlowProjectType fpType =  actyw.getFptype();
        if((ftheme == null) || (fpType == null)){
            logger.warn("模板类型未定义，请检查参数！");
            return null;
        }

        String tplName = null;
        if((FlowProjectType.PMT_XM).equals(fpType)){
            if((FormTheme.F_MD).equals(ftheme)){
                tplName = "promodel_data_template.xlsx";
            }else if((FormTheme.F_MD).equals(ftheme)){
                tplName = "promodel_data_template.xlsx";
            }else if((FormTheme.F_TLXY).equals(ftheme)){
                tplName = "promodel_data_template.xlsx";
            }else{
                tplName = "promodel_data_template.xlsx";
            }
        }else if((FlowProjectType.PMT_DASAI).equals(fpType)){
            if((FormTheme.F_MD).equals(ftheme)){
                tplName = "promodel_gcontest_data_template.xlsx";
            }else if((FormTheme.F_MD).equals(ftheme)){
                tplName = "promodel_gcontest_data_template.xlsx";
            }else{
                tplName = "promodel_gcontest_data_template.xlsx";
            }
        }else{
            logger.info("当前下载的模板类型未定义！");
        }

        absTpl.setFname(actyw.projectTname() + "导入模板" + StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX);
        absTpl.setTplName(tplName);
        absTpl.setRootPath(SpringContextHolder.getWebPath(request, ExpType.TPL_ROOT_STATICEXCELTEMPLATE));
        logger.info("当前下载的模板路径为：" + absTpl.getRootPath() +"/"+ absTpl.getTplName());
        return absTpl;
    }

    /**
     * 根据Yw获取模板文件名称.
     * @param absTpl 模板参数，避免参数丢失时可以传递该对象
     * @param actyw 业务流程
     * @param rootPath 根路径
     * @param key 模板特性标识
     * @return IitAbsTpl
     */
    public static IitAbsTpl getTplByActYw(ActYw actyw, String rootPath, String key) {
        return getTplByActYw(null, actyw, rootPath, key);
    }
    public static IitAbsTpl getTplByActYw(IitAbsTpl absTpl, ActYw actyw, String rootPath, String key) {
        if(absTpl == null){
            absTpl = new IitAbsTpl();
        }

        if(StringUtil.isEmpty(rootPath)){
            return null;
        }

        FormTheme ftheme =  actyw.getFtheme();
        FlowProjectType fpType =  actyw.getFptype();
        if((ftheme == null) || (fpType == null)){
            logger.warn("模板类型未定义，请检查参数！");
            return null;
        }

        String tplName = StringUtil.remove(ftheme.getKey(), StringUtil.LINE_D) + StringUtil.LINE + ftheme.getKey() + fpType.getValue();
        if(StringUtil.isNotEmpty(key)){
            tplName += StringUtil.LINE_D + key;
        }
        tplName += StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX;

        absTpl.setFname(actyw.projectTname() + "导入模板" + StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX);
        absTpl.setTplName(tplName);
        absTpl.setRootPath(rootPath);
        logger.info("当前下载的模板路径为：" + absTpl.getRootPath() +"/"+ absTpl.getTplName());
        return absTpl;
    }


    /**
     * 获取模板类型.
     */
    String getType();

    String getRootPath();

    /**
     * 获取模板对象.
     */
    T getFile();

    /**
     * 获取模板文件类型(不含逗号).
     */
    String getFtype();

    /**
     * 获取模板文件类型(含逗号).
     */
    String getFileType();
}
