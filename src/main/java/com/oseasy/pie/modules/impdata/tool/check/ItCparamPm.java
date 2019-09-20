/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.service.ImpInfoErrmsgService;

/**
 * 导入参数校验的参数对象.
 * @author chenhao
 */
public class ItCparamPm extends AbsItCparam<XSSFSheet, ImpInfoErrmsgService, ImpInfo, ImpInfoErrmsg> {
    public final static Logger logger = Logger.getLogger(ItCparamPm.class);
    protected ActYw actyw;//流程项目对象

    public ItCparamPm() {
        super();
    }

    public ItCparamPm(XSSFSheet xs, ImpInfoErrmsgService ies, ImpInfo info, ImpInfoErrmsg ie) {
        super(xs, ies, info, ie);
    }

    public ItCparamPm(XSSFSheet xs, ImpInfoErrmsgService ies, ImpInfo info, ImpInfoErrmsg ie, ActYw actyw) {
        super(xs, ies, info, ie);
        this.actyw = actyw;
    }

    public ItCparamPm(XSSFSheet xs, ImpInfoErrmsgService ies, ImpInfo info, ImpInfoErrmsg ie, int rows, int tag, int idx, String val, ActYw actyw) {
        super(xs, ies, info, ie, rows, tag, idx, val);
        this.actyw = actyw;
    }

    public ActYw getActyw() {
        return actyw;
    }

    public void setActyw(ActYw actyw) {
        this.actyw = actyw;
    }

    @Override
    public boolean check() {
        if((this.xs == null) || (this.ies == null) || (this.info == null) || (this.ie == null)){
            logger.warn("当前校验初始化参数存在空对象！");
            return false;
        }

        if((this.xs instanceof XSSFSheet) && (this.ies instanceof ImpInfoErrmsgService) && (this.info instanceof ImpInfo) && (this.ie instanceof ImpInfoErrmsg)){
            return true;
        }
        logger.warn("当前校验初始化参数存在类型不匹配对象！");
        return false;
    }
}
