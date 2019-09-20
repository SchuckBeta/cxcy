package com.oseasy.act.modules.actyw.tool.project.impl;

import com.oseasy.act.modules.act.vo.ActRstatus;
import com.oseasy.act.modules.actyw.tool.project.IStrategy;
import com.oseasy.act.modules.actyw.tool.project.IStrategyRunner;

public class StyCategoryRunner<T extends IStrategy> implements IStrategyRunner{
  //持有一个具体策略的对象
  private T strategy;

  public StyCategoryRunner(T strategy) {
    super();
    this.strategy = strategy;
  }

  @Override
  public ActRstatus gen() {
    return strategy.deal();
  }
}
