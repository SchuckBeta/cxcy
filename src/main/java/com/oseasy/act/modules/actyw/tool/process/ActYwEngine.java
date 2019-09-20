/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process
 * @Description [[_ActYwEngine_]]文件
 * @date 2017年6月19日 上午11:49:15
 *
 */

package com.oseasy.act.modules.actyw.tool.process;

/**
 * 流程生成器数据库操作引擎.
 * @author chenhao
 * @date 2017年6月19日 上午11:49:15
 *
 */
public interface ActYwEngine<T, N, F, GF, GS, GR, GU, GT> {
  public T gnode();
  public N node();
  public F form();
  public GS gstatus();
  public GF gform();
  public GR grole();
  public GU guser();
//  public GC gclazz();
  public GT gtime();
}
