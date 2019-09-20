/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.cmd
 * @Description [[_ActYwAbsEngine_]]文件
 * @date 2017年6月19日 下午1:40:09
 *
 */

package com.oseasy.act.modules.actyw.tool.process.cmd;

/**
 * 引擎初始化抽象类.
 * @author chenhao
 * @date 2017年6月19日 下午1:40:09
 *
 */
public class ActYwAbsEngine<T> {
  public T engine;

  public ActYwAbsEngine() {
    super();
  }

  public ActYwAbsEngine(T engine) {
    super();
    this.engine = engine;
  }

  public T getEngine() {
    return engine;
  }

  public void setEngine(T engine) {
    this.engine = engine;
  }
}
