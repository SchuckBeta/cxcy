package com.oseasy.pie.modules.impdata.enums;

public enum ProjectColEnum {
		S0("0","approving_year")
		,S1("1","province")
		,S2("2","university_code")
		,S3("3","university_name")
		,S4("4","number")
		,S5("5","name")
		,S6("6","type")
		,S7("7","leader")
		,S8("8","leader_no")
		,S9("9","team_stu_info")
		,S10("10","teacher")
		,S11("11","finance_grant")
		,S12("12","university_grant")
		,S13("13","total_grant")
		,S14("14","introduction")
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

		private ProjectColEnum(String value, String name) {
			this.value=value;
			this.name=name;
		}
		public static Integer getValueByName(String name) {
			if (name!=null) {
				for(ProjectColEnum e:ProjectColEnum.values()) {
					if (e.name.equals(name)) {
						return Integer.parseInt(e.value);
					}
				}
			}
			return null;
		}
		
	}
