package com.oseasy.pie.modules.impdata.enums;

public enum ProjectMdApprovalColEnum {
		S1("1","pro_category")
		,S2("2","level")
		,S3("3","p_number")
		,S4("4","p_name")
		,S5("5","leader_name")
		,S6("6","no")
		,S7("7","mobile")
		,S8("8","pro_source")
		,S9("8","introduction")
		,S10("8","introduction")
		,S11("11","source_project_name")
		,S12("12","source_project_type")
		,S13("13","teachers1")
		,S14("14","teachers2")
		,S15("15","teachers3")
		,S16("16","teachers4")
		,S17("17","rufu")
		,S18("18","members1")
		,S19("19","members2")
		,S20("20","members3")
		,S21("21","result")
		,S22("22","pro_model_md_id")
		,S23("23","gnodeid")
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

		private ProjectMdApprovalColEnum(String value, String name) {
			this.value=value;
			this.name=name;
		}
		public static Integer getValueByName(String name) {
			if (name!=null) {
				for(ProjectMdApprovalColEnum e:ProjectMdApprovalColEnum.values()) {
					if (e.name.equals(name)) {
						return Integer.parseInt(e.value);
					}
				}
			}
			return null;
		}
		
	}
