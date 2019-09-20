package com.oseasy.act.modules.actyw.tool.apply.impl;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.tool.apply.ISuObserverAudit;
import com.oseasy.act.modules.actyw.tool.apply.ISuObserverStatus;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuRstatus;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuRstatusParam;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuStatus;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuStatusAparam;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuStatusGrade;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuStatusParam;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;

/**
 * 审核处理主题-入驻.
 * @author chenhao
 *
 */
public class SuoEnter extends ISuObserverStatus implements ISuObserverAudit<SuRstatusParam> {
  /**
   * 订阅审核事务.
   **/
  private SuAudit audit;
  private SuAstatus astatus;

  public SuoEnter(String id, SuAudit audit, SuAstatus astatus) {
    super(FlowType.FWT_ENTER, id);
    this.audit = audit;
    this.astatus = astatus;
    // 每新建一个学生对象,默认添加到观察者的行列
    this.audit.add(this);
    this.astatus.add(this);
  }

  @Override
  public SuRstatusParam audit(SuStatusAparam aparam) {
    // TODO Auto-generated method stub
    System.out.println("执行【入驻】审核成功！");

    /**
     * 获取状态.
     */
    SuStatusParam suStatusParam = astatus.dealAstatus(aparam.getKey());
    
    for (SuStatusGrade statu : suStatusParam.getStatus()) {
      if ((statu.getGnodeId()).equals(aparam.getGnodeId())) {
        for (SuStatus suss : statu.getGrades()) {
          if ((suss.getKey()).equals(aparam.getKey())) {
            return new SuRstatusParam(aparam.getKey(), flowType, Arrays.asList(new SuRstatus[]{new SuRstatus(aparam.getGnodeId(), true, suss)}));
          }
        }
      }
    }
    return null;
  }

  @Override
  public List<SuStatusGrade> getStatus() {
    // TODO Auto-generated method stub
    System.out.println("获取【入驻】参数成功！");
    List<SuStatusGrade> allGrade = Lists.newArrayList();
    allGrade.add(new SuStatusGrade("A", Arrays.asList(new SuStatus[]{new SuStatus("0", "不通过"), new SuStatus("1", "通过")})));
    allGrade.add(new SuStatusGrade("B", Arrays.asList(new SuStatus[]{new SuStatus("0", "A级别"), new SuStatus("1", "C级别"), new SuStatus("3", "C级别")})));
    allGrade.add(new SuStatusGrade("C", Arrays.asList(new SuStatus[]{new SuStatus("0", "不通过"), new SuStatus("1", "不通过")})));
    return allGrade;
  }
}
