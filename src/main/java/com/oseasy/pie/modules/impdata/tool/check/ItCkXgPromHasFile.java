/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import org.apache.log4j.Logger;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.util.common.utils.SpSteel;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.rsa.MD5Util;

/**
 * 检验项目名称列.
 * @author chenhao
 *
 */
public class ItCkXgPromHasFile implements IitCheck<ItCparamPm>{
    public final static Logger logger = Logger.getLogger(ItCkXgPromHasFile.class);

    @Override
    public String key() {
        return "是否有附件";
    }

    @Override
    public boolean validateKey(ItCparamPm param) {
        if ((key()).equals(ExcelUtils.getStringByCell(param.getXs().getRow(param.getRows()).getCell(param.getIdx()), param.getXs()))){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamPm validate(ItCparamPm param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param) && (param.getActyw() != null))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();

        if((pe instanceof ProModelError) && (pev instanceof ProModelError)){
            ProModelError phe = (ProModelError)pe;
            ProModelError validinfo = (ProModelError)pev;

            phe.setHasfile(param.getVal());
            validinfo.setHasfile(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isEmpty(param.getVal())) {
                param.setTag(param.getTag() + 1);
                param.setTags(param.getTags() + 1);
                iie.setErrmsg("必填信息");
            } else if ((param.getVal()).length() > 1) {
                param.setTag(param.getTag() + 1);
                param.setTags(param.getTags() + 1);
                iie.setErrmsg("最多1个字符");
                phe.setHasfile(null);
            } else if (!Const.YES_ZH.equals(param.getVal()) && !Const.NO_ZH.equals(param.getVal())) {
                param.setTag(param.getTag() + 1);
                param.setTags(param.getTags() + 1);
                iie.setErrmsg("填写错误");
            } else if (!checkValidFile(validinfo, param.getActyw())) {
                param.setTag(param.getTag() + 1);
                param.setTags(param.getTags() + 1);
                iie.setErrmsg("未找到上传的附件");
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }else if((pe instanceof ProModelGcontestError) && (pev instanceof ProModelGcontestError)){
            ProModelGcontestError phe = (ProModelGcontestError)pe;
            ProModelGcontestError validinfo = (ProModelGcontestError)pev;

            phe.setHasfile(param.getVal());
            validinfo.setHasfile(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isEmpty(param.getVal())) {
                param.setTag(param.getTag() + 1);
                param.setTags(param.getTags() + 1);
                iie.setErrmsg("必填信息");
            } else if ((param.getVal()).length() > 1) {
                param.setTag(param.getTag() + 1);
                param.setTags(param.getTags() + 1);
                iie.setErrmsg("最多1个字符");
                phe.setHasfile(null);
            } else if (!Const.YES_ZH.equals(param.getVal()) && !Const.NO_ZH.equals(param.getVal())) {
                param.setTag(param.getTag() + 1);
                param.setTags(param.getTags() + 1);
                iie.setErrmsg("填写错误");
            } else if (!checkValidFile(validinfo, param.getActyw())) {
                param.setTag(param.getTag() + 1);
                param.setTags(param.getTags() + 1);
                iie.setErrmsg("未找到上传的附件");
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }else{
            logger.warn("PE 或 PEV 未定义！");
        }
        return param;
    }

    /**
     * 校验附件是否存在.
     * @param phe
     * @param ay
     * @return boolean
     * @throws Exception
     */
    private boolean checkValidFile(ProModelError phe, ActYw ay) {
        if (!phe.isValidName()) {
            return true;
        }
        if (Const.NO_ZH.equals(phe.getHasfile())) {
            return true;
        }
        String filepath = ImpDataService.tempProModelFilePath + ay.getProProject().getProType() + ay.getProProject().getType() + "/" + MD5Util.string2MD5(phe.getName());
        int c;
        try {
            c = VsftpUtils.getFileCount(filepath);
        } catch (Exception e) {
            c = 0;
            logger.warn("校验附件是否存在时，调用FtpUtil处理失败！\n" + e.getMessage());
        }

        if (c > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验附件是否存在.
     * @param phe
     * @param ay
     * @return boolean
     * @throws Exception
     */
    private boolean checkValidFile(ProModelGcontestError phe, ActYw ay){
        if (!phe.isValidName()) {
            return true;
        }
        if (Const.NO_ZH.equals(phe.getHasfile())) {
            return true;
        }

        phe.setName(SpSteel.replaceAll(phe.getName(), DictUtils.getDictVsByType(SpSteel.SP_STEEL_DKEY)));
        String filepath = ImpDataService.tempProModelFilePath + ay.getProProject().getProType() + ay.getProProject().getType() + "/" + MD5Util.string2MD5(phe.getName());
        int c;
        try {
            System.out.println("校验文件路径："+filepath);
            c = VsftpUtils.getFileCount(filepath);
        } catch (Exception e) {
            c = 0;
            logger.warn("校验附件是否存在时，调用FtpUtil处理失败！\n" + e.getMessage());
        }
        if (c > 0) {
            return true;
        } else {
            return false;
        }
    }
}
