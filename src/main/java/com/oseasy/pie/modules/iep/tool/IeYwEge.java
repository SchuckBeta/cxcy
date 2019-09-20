/**
 * .
 */

package com.oseasy.pie.modules.iep.tool;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.oseasy.pie.modules.iep.entity.IepTpl;

/**
 * 导入导出模板引擎.
 * @author chenhao
 *
 */
public interface IeYwEge{
    /**
     * 检查模板.
     */
    public void checkTpl(IepTpl iepTpl, IeAbsYw yw);

    /**
     * 导入单个附件数据.
     */
    public void impData(IepTpl iepTpl, IeAbsYw yw, MultipartFile mpFiles);

    /**
     * 执行导入.
     */
    public void run(IepTpl iepTpl, IeAbsYw yw, List<MultipartFile> mpFiles);
}
