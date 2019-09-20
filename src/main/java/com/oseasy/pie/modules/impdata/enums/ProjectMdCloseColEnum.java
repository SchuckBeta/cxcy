package com.oseasy.pie.modules.impdata.enums;

public enum ProjectMdCloseColEnum {
		S1("1","p_number")
		,S2("2","p_name")
		,S3("3","leader_name")
		,S4("4","no")
		,S5("5","mobile")
		,S6("6","members")
		,S7("7","tea_name")
		,S8("8","tea_no")
		,S9("9","level")
		,S10("10","pro_category")
		,S11("11","result")
		,S12("12","audit_result")
		,S13("13","excellent")
		,S14("14","reimbursement_amount")
		,S15("15","pro_model_md_id")
		,S16("16","gnodeid")
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

		private ProjectMdCloseColEnum(String value, String name) {
			this.value=value;
			this.name=name;
		}
		public static Integer getValueByName(String name) {
			if (name!=null) {
				for(ProjectMdCloseColEnum e:ProjectMdCloseColEnum.values()) {
					if (e.name.equals(name)) {
						return Integer.parseInt(e.value);
					}
				}
			}
			return null;
		}
		
	}
