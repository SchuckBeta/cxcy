package com.oseasy.act.modules.actyw.entity;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.DataEntity;

import java.util.List;

/**
 * 学校确认步骤Entity.
 * @author zy
 * @version 2018-01-15
 */
public class ActYwStep extends DataEntity<ActYwStep> {
    public enum StepEnmu {
        STEP1("1", "创建流程"),
        STEP2("2", "设计流程"),
        STEP3("3", "发布流程"),
        STEP4("4", "关联流程"),
        STEP5("5", "创建项目"),
        STEP6("6", "设置项目编号"),
        STEP7("7", "选择发布学校")
        ;
        private String value;
        private String name;
        StepEnmu(String value, String name) {
            this.value = value;
            this.name = name;
        }

        /**
         * 根据valuey获取枚举 .
         *
         * @param value 枚举标识
         * @return StepEnmu
         */
        public static StepEnmu getByValue(String value) {
            if ((value != null)) {
                StepEnmu[] entitys = StepEnmu.values();
                for (StepEnmu entity : entitys) {
                    if ((entity.getValue() != null) && (value).equals(entity.getValue())) {
                        return entity;
                    }
                }
            }
            return null;
        }
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public String toString() {
            return "{\"value\":\"" + this.value + "\",\"name\":\"" + this.name + "\"}";
        }
    }

	private static final long serialVersionUID = 1L;
    private String provGroupId;		// 省流程id
	private String provActywId;		// 省项目id
	private String modelGroupId;		// 模板流程id
    private String modelActywId;		// 模板项目id
    private String step;		// 步骤

	public ActYwStep() {
		super();
	}

    public String getProvGroupId() {
        return provGroupId;
    }

    public void setProvGroupId(String provGroupId) {
        this.provGroupId = provGroupId;
    }

    public String getProvActywId() {
        return provActywId;
    }

    public void setProvActywId(String provActywId) {
        this.provActywId = provActywId;
    }

    public String getModelGroupId() {
        return modelGroupId;
    }

    public void setModelGroupId(String modelGroupId) {
        this.modelGroupId = modelGroupId;
    }

    public String getModelActywId() {
        return modelActywId;
    }

    public void setModelActywId(String modelActywId) {
        this.modelActywId = modelActywId;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}