package com.oseasy.com.pcore.modules.sys.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.entity.SysNo;
import com.oseasy.com.pcore.modules.sys.entity.SysNoOffice;
import com.oseasy.com.pcore.modules.sys.service.SysNoService;
import com.oseasy.util.common.utils.StringUtil;

public class SysNodeTool {
  private static final String SNO_MAX = "max";
  protected static Logger logger = LoggerFactory.getLogger(SysNodeTool.class);

  private static SysNoService sysNoService = SpringContextHolder.getBean(SysNoService.class);
  /******************************************************************************
   * 根据唯一标识类型获取系统最大的Key.
   * @param sysNoType Key类型
   * @return String
   */
  public static String genByKeyss(SysNoType sysNoType) {
    return genBySysMaxNo(sysNoService.genByKeyss(sysNoType));
  }

  /*
   * 生成系统最大编号.
   * @param sysNo 编号
   * @return String
   */
  public static String genBySysMaxNo(SysNo sysNo) {
    return genByMaxNo(sysNo, null);
  }

  /**
   * 生成系统最大编号.
   * @param sysNo 编号
   * @return String
   */
  public static String genByMaxNo(SysNo sysNo, Long maxNo) {
    return gen(sysNo, null, maxNo);
  }

  /**
   * 生成机构最大编号.
   * @param sysNo 编号
   * @return String
   */
  public static String genByOfficeNoSysMaxNo(SysNo sysNo, Long officeNo) {
    return genByOfficeNoMaxNo(sysNo, officeNo, null);
  }

  /**
   * 生成机构最大编号.
   * @param sysNo 编号
   * @return String
   */
  public static String genByOfficeNoMaxNo(SysNo sysNo, Long officeNo, Long maxNo) {
    return gen(sysNo, officeNo, maxNo);
  }

  /**
   * 渲染编号模板,生成最大编号.
   *  1、若机构标识为空、机构最大值为空，使用[前缀+格式化+全局最大值+后缀]
   *  2、若机构标识为空、机构最大值不为空，使用[前缀+格式化+机构最大值+后缀]
   *  3、若机构标识不为空、机构最大值为空，使用[前缀+机构标识+格式化+全局最大值+后缀]
   *  3、若机构标识不为空、机构最大值不为空，使用[前缀+机构标识+格式化+机构最大值+后缀]
   * format: prefix-{{0}}-postfix
   * @param sysNoOffice 系统机构编号
   * @param officeNo 机构标识
   * @param maxNo 最大编号
   * @return
   */
  public static String gen(SysNoOffice sysNoOffice, Long officeNo, Long maxNo) {
    if ((sysNoOffice == null) || (sysNoOffice.getSysNo() == null)) {
      return null;
    }
    SysNo sysNo = sysNoOffice.getSysNo();
    Map<String, Object> model = new HashMap<String, Object>();
    if (StringUtil.isEmpty(sysNo.getPrefix())) {
      sysNo.setPrefix("");
    }

    if (StringUtil.isEmpty(sysNo.getPostfix())) {
      sysNo.setPostfix("");
    }

    if (maxNo == null) {
      maxNo = sysNo.getSysmaxVal();
    }

    StringBuffer buffer = new StringBuffer();
    buffer.append(sysNo.getPrefix());
    if (StringUtil.isNotEmpty(sysNo.getFormat())) {
      if (officeNo != null) {
        buffer.append(officeNo);
      }
      model.put(SNO_MAX, StringUtil.autoGenZero(sysNo.getMaxByte(), maxNo));
      buffer.append(SysNodeFresultTool.renderFormat(new Date(), new SysNodeFresult(sysNo.getFormat()), model));
    }else{
      if (officeNo != null) {
        buffer.append(officeNo);
      }
    }
    buffer.append(sysNo.getPostfix());

    return buffer.toString();
  }

  /**
   * 渲染编号模板,生成最大编号.
   *  1、若机构标识为空、机构最大值为空，使用[前缀+格式化+全局最大值+后缀]
   *  2、若机构标识为空、机构最大值不为空，使用[前缀+格式化+机构最大值+后缀]
   *  3、若机构标识不为空、机构最大值为空，使用[前缀+机构标识+格式化+全局最大值+后缀]
   *  3、若机构标识不为空、机构最大值不为空，使用[前缀+机构标识+格式化+机构最大值+后缀]
   * format: prefix-{{0}}-postfix
   * @param sysNo 系统编号
   * @param officeNo 机构标识
   * @param maxNo 最大编号
   * @return
   */
  public static String gen(SysNo sysNo, Long officeNo, Long maxNo) {
    if (sysNo == null) {
      return null;
    }

    Map<String, Object> model = new HashMap<String, Object>();
    if (StringUtil.isEmpty(sysNo.getPrefix())) {
      sysNo.setPrefix("");
    }

    if (StringUtil.isEmpty(sysNo.getPostfix())) {
      sysNo.setPostfix("");
    }

    if (maxNo == null) {
      maxNo = sysNo.getSysmaxVal();
    }

    StringBuffer buffer = new StringBuffer();
    buffer.append(sysNo.getPrefix());
    if (StringUtil.isNotEmpty(sysNo.getFormat())) {
      if (officeNo != null) {
        buffer.append(officeNo);
      }
      model.put(SNO_MAX, StringUtil.autoGenZero(sysNo.getMaxByte(), maxNo));
      buffer.append(SysNodeFresultTool.renderFormat(new Date(), new SysNodeFresult(sysNo.getFormat()), model));
    }else{
      if (officeNo != null) {
        buffer.append(officeNo);
      }
    }
    buffer.append(sysNo.getPostfix());

    return buffer.toString();
  }
}
