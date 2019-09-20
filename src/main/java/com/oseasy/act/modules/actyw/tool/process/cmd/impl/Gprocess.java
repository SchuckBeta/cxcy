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
 * 根节点业务节点添加.
 * @author chenhao
 *
 */
public class Gprocess extends ActYwAbsEngine<ActYwEngineImpl> implements ActYwCommand<ActYwPgnode>, ActYwCommandCheck<ActYwPgnode, ActYwRgnode, ActYwGnode>{
    protected static final Logger logger = Logger.getLogger(Gprocess.class);

    @Override
    public ActYwEcmd cmd() {
        return ActYwEcmd.ECMD_PROCESS;
    }

    public Gprocess() {
      super();
    }

    public Gprocess(ActYwEngineImpl engine) {
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
        tpl =  tpl.check(engine, cmd.getKey(), tpl, logger);
//        ActYwGnode curGnode = tpl.getGnode();
//        ActYwGnode curPreGnode = engine.gnode().get(curGnode.getPreGnode().getId());
//        if(curPreGnode == null){
//            tpl = new ActYwPgnode();
//            tpl.setMsg(cmd.getKey() + " 命令[gnode.preGnode]-["+curGnode.getId()+"]查询为空！");
//            logger.warn(tpl.getMsg());
//            tpl.setStatus(false);
//            return tpl;
//        }
//
//        ActYwGnode curPreFunGnode = engine.gnode().get(curGnode.getPreFunGnode().getId());
//        if(curPreFunGnode == null){
//            tpl = new ActYwPgnode();
//            tpl.setMsg(cmd.getKey() + " 命令[gnode.preFunGnode]-["+curGnode.getId()+"]查询为空！");
//            logger.warn(tpl.getMsg());
//            tpl.setStatus(false);
//            return tpl;
//        }
//
//        if(!((GnodeType.GT_ROOT_FLOW.getId()).equals(curPreGnode.getType()))){
//            tpl = new ActYwPgnode();
//            tpl.setMsg(cmd.getKey() + " 命令[gnode.preGnode]-["+curGnode.getId()+"]节点类型错误！");
//            logger.warn(tpl.getMsg());
//            tpl.setStatus(false);
//            return tpl;
//        }
//
//        if(!((GnodeType.GT_ROOT_START.getId()).equals(curPreFunGnode.getType()) ||
//                (GnodeType.GT_ROOT_GATEWAY.getId()).equals(curPreFunGnode.getType()) ||
//                (GnodeType.GT_ROOT_TASK.getId()).equals(curPreFunGnode.getType()) ||
//                (GnodeType.GT_PROCESS.getId()).equals(curPreFunGnode.getType()))){
//            tpl = new ActYwPgnode();
//            tpl.setMsg(cmd.getKey() + " 命令[gnode.preFunGnode]-["+curGnode.getId()+"]节点类型错误！");
//            logger.warn(tpl.getMsg());
//            tpl.setStatus(false);
//            return tpl;
//        }
        return tpl;
    }

    @Override
    public ApiTstatus<ActYwGnode> checkPerfect(ActYwPgnode tpl) {
        return new ApiTstatus<ActYwGnode>();
    }
}
