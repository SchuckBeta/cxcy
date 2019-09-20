/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.cmd
 * @Description [[_ActYwCommandBeforeCheck_]]文件
 * @date 2017年6月20日 下午4:11:27
 *
 */

package com.oseasy.act.modules.actyw.tool.process.cmd;

import com.oseasy.act.modules.actyw.tool.process.impl.ActYwEngineImpl;
import com.oseasy.com.common.config.ApiTstatus;

/**
 * 命令执行前后检查接口.
 * @author chenhao
 * @date 2017年6月20日 下午4:11:27
 *
 */
public interface ActYwCommandCheck<T extends ActYwPtpl<ActYwEngineImpl>, R extends ActYwRtpl, E> {
  /**
   * 流程命令执行前检查.
   * @author chenhao
   * @param tpl 模板类
   * @return Boolean
   */
  public abstract ApiTstatus<E> checkBeforeExecute(T tpl);

  /**
   * 流程节点初始化参数.
   * @author chenhao
   * @param tpl 模板类
   * @return Boolean
   */
  public abstract ApiTstatus<E> initParams(T tpl);

  /**
   * 流程节点初始化参数完整性.
   * @author chenhao
   * @param tpl 模板类
   * @return Boolean
   */
  public abstract ApiTstatus<E> checkParams(ActYwEcmd cmd, T tpl);

  /**
   * 流程节点是否完整.
   * @author chenhao
   * @param tpl 模板类
   * @return Boolean
   */
  public abstract ApiTstatus<E> checkPerfect(T tpl);

  /**
   * 验证最终结果.
   * @param tpl
   * @return
   */
  public R validate(T tpl);
}
