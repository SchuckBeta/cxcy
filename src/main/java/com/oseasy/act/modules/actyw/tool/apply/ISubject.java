package com.oseasy.act.modules.actyw.tool.apply;

import java.util.List;

/**
 * 主题.
 * @author chenhao
 *
 */
public interface ISubject<T extends ISuObserver, R extends IRstatus> {
  //添加观察者
  void add(T sobj);

  //移除观察者
  void delete(T sobj);

  //当主题方法改变时,这个方法被调用,通知所有的观察者
  List<R> notifys();

//  public static void main(String[] args) {
//    SuAstatus status = new SuAstatus();
//    SuAudit audit = new SuAudit();
//    String type = "3";
//    String audit0 = "0";
//    String audit1 = "1";
//    String audit2 = "2";
//    String audit3 = "3";
//    if ((type).equals(audit1)) {
//      new SuoEnter("SuoEnter1000", audit, status);
//      SuStatusParam param = status.dealAstatus("SuoEnter1000");
//      System.out.println("【入驻】获取到的状态为：" + SuStatusGrade.getGradeByGnode(param.getStatus(), "节点A").getGrades().toString());
//
//      List<SuRstatusParam> isAuditTrue = audit.dealAuditss(new SuStatusAparam("1", audit0));
//      System.out.println("【入驻】审核结果为1，业务处理结果为：" + isAuditTrue);
//    }else if ((type).equals(audit2)) {
//      new SuoAppointment("SuoAppointment1000", audit, status);
//      SuStatusParam param = status.dealAstatus("SuoAppointment1000");
//      System.out.println("【预约】获取到的状态为：" + SuStatusGrade.getGradeByGnode(param.getStatus(), "节点B").getGrades().toString());
//      SuRstatus isAuditTrue = audit.dealAudit(new SuStatusAparam("2", audit0), "", FlowType.FWT_DASAI);
//      System.out.println("【预约】审核结果为2，业务处理结果为：" + isAuditTrue);
//      if (isAuditTrue.getStatus()) {
//        //处理成功.流程执行下一步
//        System.out.println("【预约】处理成功.流程执行下一步");
//      }else{
//        //处理失败，流程执行结束
//        System.out.println("【预约】处理失败，流程执行结束");
//      }
//    }else if ((type).equals(audit3)) {
//      new SuoEnter("SuoEnter1000", audit, status);
//      new SuoAppointment("SuoAppointment1000", audit, status);
//      SuStatusParam param = status.dealAstatus("SuoEnter1000");
//      System.out.println("【入驻】获取到的状态为：" + SuStatusGrade.getGradeByGnode(param.getStatus(), "节点A").getGrades().toString());
//
//      SuStatusParam param2 = status.dealAstatus("SuoAppointment1000");
//      System.out.println("【预约】获取到的状态为：" + SuStatusGrade.getGradeByGnode(param2.getStatus(), "节点B").getGrades().toString());
//      SuRstatusParam isAuditTrues = audit.dealAudits(new SuStatusAparam(audit2, audit0), "", FlowType.FWT_DASAI);
//      SuRstatus isAuditTrue = isAuditTrues.getStatus().get(0);
//      if(isAuditTrue != null){
//          System.out.println("【预约】审核结果为2，业务处理结果为：" + isAuditTrue);
//          if (isAuditTrue.getStatus()) {
//            if (audit0.equals(isAuditTrue.getRtVal())) {
//              //处理成功.流程执行下一步
//              System.out.println("【预约】处理成功.流程执行下一步");
//            }else{
//              //处理成功.流程执行非预期结果
//              System.out.println("【预约】处理成功.流程执行下一步");
//            }
//          }else{
//            if ((audit1).equals(isAuditTrue.getRtVal())) {
//              //处理成功.流程执行下一步
//              System.out.println("【预约】处理成功.流程执行下一步");
//            }else if ((audit2).equals(isAuditTrue.getRtVal())) {
//              //处理成功.流程执行非预期结果
//              System.out.println("【预约】处理失败，流程执行结果2");
//            }else{
//              //处理失败，流程执行结束
//              System.out.println("【预约】处理失败，流程执行结束");
//            }
//          }
//      }
//    }
//  }
}
