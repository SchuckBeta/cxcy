package com.oseasy.act.modules.actyw.vo;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwApply;
import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * 流程申请Entity.
 * @author zy
 * @version 2017-12-05
 */
public class ActYwApplyVo extends ActYwApply{
    private static final long serialVersionUID = 1L;
    public static final String GRADE_PASS = "1";//通过
    public static final String GRADE_END = "0";//不通过

    String grade; // 审核结果

	public ActYwApplyVo() {
    super();
  }

    public ActYwApplyVo(String id) {
    super(id);
  }

    public ActYwApplyVo(String id, ActYw actYw, User applyUser) {
    super(id, actYw, applyUser);
  }

    public ActYwApplyVo(String id, ActYw actYw, User applyUser, String procInsId) {
        super(id, actYw, applyUser, procInsId);
    }

    public ActYwApplyVo(String id, ActYw actYw, User applyUser, String procInsId, String grade) {
        super(id, actYw, applyUser, procInsId, null);
        this.grade = grade;
    }

    public ActYwApplyVo(String id, ActYw actYw, User applyUser, String procInsId, String no, String type, String relId) {
        super(id, actYw, applyUser, procInsId, no, type, relId);
    }

	public String getGrade() {
    return grade;
  }

    public void setGrade(String grade) {
		this.grade = grade;
	}
}