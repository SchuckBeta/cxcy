/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process.cmd.impl;

import org.apache.log4j.Logger;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.process.cmd.ActYwAbsEngine;
import com.oseasy.act.modules.actyw.tool.process.cmd.ActYwCommand;
import com.oseasy.act.modules.actyw.tool.process.cmd.ActYwCommandCheck;
import com.oseasy.act.modules.actyw.tool.process.cmd.ActYwEcmd;
import com.oseasy.act.modules.actyw.tool.process.cmd.vo.ActYwPgnode;
import com.oseasy.act.modules.actyw.tool.process.cmd.vo.ActYwRgnode;
import com.oseasy.act.modules.actyw.tool.process.impl.ActYwEngineImpl;
import com.oseasy.act.modules.actyw.tool.process.rest.GnodeMargeVo;
import com.oseasy.com.common.config.ApiTstatus;

/**
 * 子流程开始节点添加.
 * @author chenhao
 *
 */
public class Gpstart extends ActYwAbsEngine<ActYwEngineImpl> implements ActYwCommand<ActYwPgnode>, ActYwCommandCheck<ActYwPgnode, ActYwRgnode, ActYwGnode>{
    protected static final Logger logger = Logger.getLogger(Gpstart.class);

    @Override
    public ActYwEcmd cmd() {
        return ActYwEcmd.ECMD_P_START;
    }

    public Gpstart() {
      super();
    }

    public Gpstart(ActYwEngineImpl engine) {
      super(engine);
    }

    @Override
    public ActYwRgnode validate(ActYwPgnode tpl) {
        ActYwRgnode actYwrg = new ActYwRgnode();
        ApiTstatus<ActYwGnode> rstsusParams = checkParams(cmd(), tpl);
        ApiTstatus<ActYwGnode> rstsusBeforeEx = checkBeforeExecute(tpl);
        if (!rstsusParams.getStatus()) {
            actYwrg.setMsg(rstsusParams.getMsg());
        }
        if (!rstsusBeforeEx.getStatus()) {
            actYwrg.setMsg(rstsusBeforeEx.getMsg());
        }
        actYwrg.setStatus(rstsusParams.getStatus() && rstsusBeforeEx.getStatus());
        return actYwrg;
    }

    /**
     * 执行必要参数 .
     * @author chenhao
     * @param tpl 节点参数
     * @return ActYwRgnode
     * @throws Exception
     */
    @Override
    public ActYwRgnode execute(ActYwPgnode tpl) throws Exception {
        ActYwRgnode actYwrg = validate(tpl);
        if (actYwrg.getStatus()) {
            GnodeMargeVo gnodeMargeVo = engine.gnode().margeList(engine, tpl.getGnode());
            actYwrg.setStatus(gnodeMargeVo.getStatus());
            actYwrg.setMsg(actYwrg.getMsg());
        }
        return actYwrg;
    }

    @Override
    public ApiTstatus<ActYwGnode> checkBeforeExecute(ActYwPgnode tpl) {
        return new ApiTstatus<ActYwGnode>();
    }

    @Override
    public ApiTstatus<ActYwGnode> initParams(ActYwPgnode tpl) {
        return new ApiTstatus<ActYwGnode>();
    }

    @Override
    public ApiTstatus<ActYwGnode> checkParams(ActYwEcmd cmd, ActYwPgnode tpl) {
        return tpl.check(engine, cmd.getKey(), tpl, logger);
    }

    @Override
    public ApiTstatus<ActYwGnode> checkPerfect(ActYwPgnode tpl) {
        return new ApiTstatus<ActYwGnode>();
    }
}
