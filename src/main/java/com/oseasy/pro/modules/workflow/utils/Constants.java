package com.oseasy.pro.modules.workflow.utils;

public class Constants {

    public enum WorkFlowType {

        FLOW_TYPE_COMMON("common", "proModelService", "常规流程"),
        FLOW_TYPE_MD("md", "proModelMdService", "明大流程"),
        FLOW_TYPE_GZSMXX("gzsmxx", "proModelGzxmxxService", "桂子山梦想秀");


        private String key;
        private String value;
        private String description;

        WorkFlowType(String key, String value, String description) {
            this.key = key;
            this.value = value;
            this.description = description;
        }

        public static WorkFlowType getWorkFlowTypeByKey(String key) {
            WorkFlowType workFlowType = null;
            for (WorkFlowType flowType : WorkFlowType.values()) {
                if (key.equals(flowType.getKey())) {
                    workFlowType = flowType;
                    break;
                }
            }
            return workFlowType;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
