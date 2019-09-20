package com.oseasy.act.modules.actyw.tool.project.impl;

import com.oseasy.act.modules.act.vo.ActRstatus;
import com.oseasy.act.modules.actyw.tool.project.IStrategy;

/**
 * 栏目生成策略.
 */
public class StyCategory implements IStrategy{

  @Override
  public ActRstatus deal() {
    return new ActRstatus(true, "StyCategory1 方案执行处理完成了！");
  }
}
