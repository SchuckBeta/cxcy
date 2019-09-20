package com.oseasy.pie.modules.impdata.enums;

public enum ProjectMdMidColEnum {
		S1("1","p_number")
		,S2("2","p_name")
		,S3("3","leader_name")
		,S4("4","no")
		,S5("5","mobile")
		,S6("6","teachers")
		,S7("7","pro_category")
		,S8("8","level")
		,S9("9","result")
		,S10("10","stage_result")
		,S11("11","reimbursement_amount")
		,S12("12","pro_model_md_id")
		,S13("13","gnodeid")
		;

		private String value;
		private String name;
		
		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private ProjectMdMidColEnum(String value, String name) {
			this.value=value;
			this.name=name;
		}
		public static Integer getValueByName(String name) {
			if (name!=null) {
				for(ProjectMdMidColEnum e:ProjectMdMidColEnum.values()) {
					if (e.name.equals(name)) {
						return Integer.parseInt(e.value);
					}
				}
			}
			return null;
		}
		
	}
