package com.oseasy.pro.modules.tpl.vo.impl;

import java.io.Serializable;
import java.util.List;

import com.oseasy.act.modules.actyw.tool.process.ActYwSten;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.proprojectmd.entity.ProModelMd;
import com.oseasy.pro.modules.tpl.vo.IWparam;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.json.JsonAliUtils;

public class WparamReport implements IWparam, Serializable{
  private static final long serialVersionUID = 1L;
  private String fileName;
  private String tplFileName;
  private Wpro pro;
  private Wteam team;

  public Wpro getPro() {
    return pro;
  }

  public void setPro(Wpro pro) {
    this.pro = pro;
  }

  public Wteam getTeam() {
    return team;
  }

  public void setTeam(Wteam team) {
    this.team = team;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getTplFileName() {
    return tplFileName;
  }

  public void setTplFileName(String tplFileName) {
    this.tplFileName = tplFileName;
  }

  /**
   * 根据json生成对象.
   * @author chenhao
   * @param jsons json文件
   * @return WordParam
   */
  public static WparamReport genWparam(String json) {
    try {
      return genWparams(ActYwSten.readJson(IWparam.WORD_JSON + FileUtil.LINE + json)).get(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 根据json生成对象.
   * @author chenhao
   * @param json json
   * @return List
   */
  public static List<WparamReport> genWparams(String json) {
    return (List<WparamReport>) JsonAliUtils.toBean("[" + json + "]", WparamReport.class);
  }

  public static WparamReport init(ProModel proModel, ProModelMd proModelMd, Team team, List<BackTeacherExpansion> xyteachers, List<BackTeacherExpansion> qyteachers, List<StudentExpansion> students) {
    return init(null, proModel, proModelMd, team, xyteachers, qyteachers, students);
  }

  public static WparamReport init(WparamReport param, ProModel proModel, ProModelMd proModelMd, Team team, List<BackTeacherExpansion> xyteachers, List<BackTeacherExpansion> qyteachers, List<StudentExpansion> students) {
    if (param == null) {
      param = new WparamReport();
    }
    param.setPro(Wpro.init(proModel, proModelMd));
    param.setTeam(Wteam.init(proModel.getDeuser(), team, xyteachers, qyteachers, students));
    return param;
  }
}
