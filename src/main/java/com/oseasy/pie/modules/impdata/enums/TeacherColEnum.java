package com.oseasy.pie.modules.impdata.enums;

public enum TeacherColEnum {
		 S0("0","login_name")
		,S1("1","teacherType")
		,S2("2","no")
		,S3("3","name")
		,S4("4","sex")
		,S5("5","mobile")
		,S6("6","email")
		,S7("7","remarks")
		,S8("8","birthday")
		,S9("9","id_type")
		,S10("10","id_no")
		,S11("11","domain")
		,S12("12","education_type")
		,S13("13","degree")
		,S14("14","education")
		,S15("15","office_id")
		,S16("16","professional")
		,S17("17","national")
		,S18("18","political")
		,S19("19","area")
		,S20("20","discipline")
		,S21("21","industry")
		,S22("22","technical_title")
		,S23("23","service_intention")
		,S24("24","work_unit")
		,S25("25","address")
		,S26("26","first_bank")
		,S27("27","bank_account")
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

		private TeacherColEnum(String value, String name) {
			this.value=value;
			this.name=name;
		}
		public static Integer getValueByName(String name) {
			if (name!=null) {
				for(TeacherColEnum e:TeacherColEnum.values()) {
					if (e.name.equals(name)) {
						return Integer.parseInt(e.value);
					}
				}
			}
			return null;
		}
		
	}
