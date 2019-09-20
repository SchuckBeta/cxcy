package com.oseasy.act.modules.actyw.tool.apply;

import com.oseasy.act.modules.actyw.tool.apply.vo.SuStatusAparam;

/**
 * 订阅对象-执行审核处理.
 * @author chenhao
 */
public interface ISuObserverAudit<T extends IRstatus> extends ISuObserver{

  /**
   * 审核业务执行结果.
   * 返回true，表示业务处理成功，流程状态变更到下一步,
   * 返回false，表示业务处理失败，流程状态不做变更.
   * 当主题状态改变时,会将一个String类型字符传入该方法的参数,每个观察者都需要实现该方法.
   * @param status 状态值
   */
  public T audit(SuStatusAparam aparam);
}
