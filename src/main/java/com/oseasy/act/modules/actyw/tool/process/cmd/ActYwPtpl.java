/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.cmd
 * @Description [[_ActYwNtpl_]]文件
 * @date 2017年6月18日 上午11:24:24
 *
 */

package com.oseasy.act.modules.actyw.tool.process.cmd;

import org.apache.log4j.Logger;

import com.oseasy.act.modules.actyw.tool.process.cmd.vo.ActYwPgnode;

/**
 * 流程节点参数模板.
 * @author chenhao
 * @date 2017年6月18日 上午11:24:24
 *
 */
public interface ActYwPtpl<T> {
    public ActYwPgnode check(T engine, String key, ActYwPgnode tpl, Logger logger);
}
