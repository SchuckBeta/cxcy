package com.oseasy.act.modules.actyw.tool.project.impl;

import com.oseasy.act.modules.act.vo.ActRstatus;
import com.oseasy.act.modules.actyw.tool.project.IStrategy;

/**
 * 菜单生成策略.
 */
public class StyMenu implements IStrategy{

  @Override
  public ActRstatus deal() {
    return new ActRstatus(true, "StyMenu1 方案执行处理完成了！");
  }
}
