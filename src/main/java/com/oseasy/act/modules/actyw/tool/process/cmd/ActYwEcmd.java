/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.cmd
 * @Description [[_ActYwEcmd_]]文件
 * @date 2017年6月18日 上午11:39:24
 *
 */

package com.oseasy.act.modules.actyw.tool.process.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.act.modules.actyw.tool.process.cmd.impl.Gpend;
import com.oseasy.act.modules.actyw.tool.process.cmd.impl.Gpflow;
import com.oseasy.act.modules.actyw.tool.process.cmd.impl.Gpgate;
import com.oseasy.act.modules.actyw.tool.process.cmd.impl.Gprocess;
import com.oseasy.act.modules.actyw.tool.process.cmd.impl.Gpstart;
import com.oseasy.act.modules.actyw.tool.process.cmd.impl.Gptask;
import com.oseasy.act.modules.actyw.tool.process.cmd.impl.Grend;
import com.oseasy.act.modules.actyw.tool.process.cmd.impl.Grflow;
import com.oseasy.act.modules.actyw.tool.process.cmd.impl.Grgate;
import com.oseasy.act.modules.actyw.tool.process.cmd.impl.Groot;
import com.oseasy.act.modules.actyw.tool.process.cmd.impl.Grstart;
import com.oseasy.act.modules.actyw.tool.process.cmd.impl.Grtask;
import com.oseasy.act.modules.actyw.tool.process.cmd.vo.ActYwPgnode;
import com.oseasy.act.modules.actyw.tool.process.cmd.vo.ActYwRgnode;
import com.oseasy.act.modules.actyw.tool.process.impl.ActYwEngineImpl;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.util.common.utils.json.JsonNetUtils;

/**
 * 流程生成命令枚举.
 * @author chenhao
 * @date 2017年6月18日 上午11:39:24
 *
 */
public enum ActYwEcmd {
    ECMD_R("Groot", ActYwEcoper.ECR_ADD, GnodeType.GT_ROOT,"新增根节点"),
    ECMD_R_START("Grstart", ActYwEcoper.ECR_ADD, GnodeType.GT_ROOT_START,"新增根开始节点"),
    ECMD_R_FLOW("Grflow", ActYwEcoper.ECR_ADD, GnodeType.GT_ROOT_FLOW, "新增根连接线节点"),
    ECMD_R_END("Grend", ActYwEcoper.ECR_ADD, GnodeType.GT_ROOT_END, "新增根结束节点"),
    ECMD_R_TASK("Grtask", ActYwEcoper.ECR_ADD, GnodeType.GT_ROOT_TASK, "新增根任务节点"),
    ECMD_R_GATE("Grgate", ActYwEcoper.ECR_ADD, GnodeType.GT_ROOT_GATEWAY, "新增根网关节点"),
    ECMD_PROCESS("Gprocess", ActYwEcoper.ECR_ADD, GnodeType.GT_PROCESS, "新增子流程"),
    ECMD_P_START("Gpstart", ActYwEcoper.ECR_ADD, GnodeType.GT_PROCESS_START, "新增子流程开始节点"),
    ECMD_P_FLOW("Gpflow", ActYwEcoper.ECR_ADD, GnodeType.GT_PROCESS_FLOW, "新增子流程连接线节点"),
    ECMD_P_END("Gpend", ActYwEcoper.ECR_ADD, GnodeType.GT_PROCESS_END, "新增子流程结束节点"),
    ECMD_P_TASK("Gptask", ActYwEcoper.ECR_ADD, GnodeType.GT_PROCESS_TASK, "新增子流程任务节点"),
    ECMD_P_GATE("Gpgate", ActYwEcoper.ECR_ADD, GnodeType.GT_PROCESS_GATEWAY, "新增子流程网关节点")
  ;
    private static Logger logger = LoggerFactory.getLogger(ActYwEcmd.class);

  private String key;
  private ActYwEcoper oper;
  private GnodeType gtype;
  private String remark;

  private ActYwEcmd(String key, ActYwEcoper oper, GnodeType gtype, String remark) {
    this.key = key;
    this.oper = oper;
    this.gtype = gtype;
    this.remark = remark;
  }

  /**
   * 根据关键字获取枚举 .
   *
   * @author chenhao
   * @param key
   *          关键字
   * @return ActYwEcmd
   */
  public static ActYwEcmd getByKey(String key) {
    ActYwEcmd[] entitys = ActYwEcmd.values();
    for (ActYwEcmd entity : entitys) {
      if ((key).equals(entity.getKey())) {
        return entity;
      }
    }
    return null;
  }

  /**
   * 根据命令获取实例.
   * @param gtid 节点类型标识
   * @param oper 操作
   * @return ActYwEcmd
   */
  public static ActYwEcmd getCmdBygtid(String gtid, ActYwEcoper oper) {
      ActYwEcmd cmd = null;
      GnodeType  gty = GnodeType.getById(gtid);
      if(gty == null){
          return null;
      }

      ActYwEcmd[] entitys = ActYwEcmd.values();
      for (ActYwEcmd entity : entitys) {
        if ((entity.getGtype()).equals(gty) && (oper).equals(entity.getOper())) {
            cmd = entity;
            break;
        }
      }
      return cmd;
  }

  /**
   * 根据命令获取实例.
   * @param cmd
   * @return ActYwCommand
   */
  public static ActYwRgnode exeEngine(ActYwEcmd cmd, ActYwEngineImpl engine, ActYwPgnode param) {
      if(cmd != null){
          logger.info(cmd.getOper().name() + "-" + cmd.getGtype().name() + "-" + (cmd.getRemark()));
      }

      ActYwRgnode rgnode = null;
      try {
          if((ECMD_R).equals(cmd)){
              Groot curEng = new Groot();
              curEng.setEngine(engine);
              rgnode = curEng.execute(param);
          }else if((ECMD_R_START).equals(cmd)){
              Grstart curEng = new Grstart();
              curEng.setEngine(engine);
              rgnode = curEng.execute(param);
          }else if((ECMD_R_FLOW).equals(cmd)){
              Grflow curEng = new Grflow();
              curEng.setEngine(engine);
              rgnode = curEng.execute(param);
          }else if((ECMD_R_END).equals(cmd)){
              Grend curEng = new Grend();
              curEng.setEngine(engine);
              rgnode = curEng.execute(param);
          }else if((ECMD_R_GATE).equals(cmd)){
              Grgate curEng = new Grgate();
              curEng.setEngine(engine);
              rgnode = curEng.execute(param);
          }else if((ECMD_R_TASK).equals(cmd)){
              Grtask curEng = new Grtask();
              curEng.setEngine(engine);
              rgnode = curEng.execute(param);
          }else if((ECMD_PROCESS).equals(cmd)){
              Gprocess curEng = new Gprocess();
              curEng.setEngine(engine);
              rgnode = curEng.execute(param);
          }else if((ECMD_P_START).equals(cmd)){
              Gpstart curEng = new Gpstart();
              curEng.setEngine(engine);
              rgnode = curEng.execute(param);
          }else if((ECMD_P_FLOW).equals(cmd)){
              Gpflow curEng = new Gpflow();
              curEng.setEngine(engine);
              rgnode = curEng.execute(param);
          }else if((ECMD_P_END).equals(cmd)){
              Gpend curEng = new Gpend();
              curEng.setEngine(engine);
              rgnode = curEng.execute(param);
          }else if((ECMD_P_GATE).equals(cmd)){
              Gpgate curEng = new Gpgate();
              curEng.setEngine(engine);
              rgnode = curEng.execute(param);
          }else if((ECMD_P_TASK).equals(cmd)){
              Gptask curEng = new Gptask();
              curEng.setEngine(engine);
              rgnode = curEng.execute(param);
          }else{
              rgnode = new ActYwRgnode(false, "命令未定义或不存在！["+cmd+"]->(" + param.getGnode().getId()+")");
          }
      } catch (Exception e) {
          logger.error("命令执行出错：", e.getMessage());
      }
      return rgnode;
  }

  public ActYwEcoper getOper() {
    return oper;
}

public void setOper(ActYwEcoper oper) {
    this.oper = oper;
}

public GnodeType getGtype() {
    return gtype;
}

public void setGtype(GnodeType gtype) {
    this.gtype = gtype;
}

public void setKey(String key) {
    this.key = key;
}

public void setRemark(String remark) {
    this.remark = remark;
}

public final String getKey() {
    return key;
  }

  public String getRemark() {
    return remark;
  }
}
