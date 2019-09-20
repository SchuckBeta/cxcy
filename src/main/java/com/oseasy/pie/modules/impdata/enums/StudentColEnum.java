package com.oseasy.pie.modules.impdata.enums;

public enum StudentColEnum {
		 S0("0","login_name")
		,S1("1","name")
		,S2("2","no")
		,S3("3","mobile")
		,S4("4","email")
		,S5("5","remarks")
		,S6("6","birthday")
		,S7("7","id_type")
		,S8("8","id_no")
		,S9("9","sex")
		,S10("10","domain")
		,S11("11","degree")
		,S12("12","education")
		,S13("13","office_id")
		,S14("14","professional")
		,S15("15","t_class")
		,S16("16","country")
		,S17("17","national")
		,S18("18","political")
		,S19("19","project_experience")
		,S20("20","contest_experience")
		,S21("21","award")
		,S22("22","enterDate")
		,S23("23","temporary_date")
		,S24("24","graduation")
		,S25("25","address")
		,S26("26","instudy")
		,S27("27","curr_state")
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

		private StudentColEnum(String value, String name) {
			this.value=value;
			this.name=name;
		}
		public static Integer getValueByName(String name) {
			if (name!=null) {
				for(StudentColEnum e:StudentColEnum.values()) {
					if (e.name.equals(name)) {
						return Integer.parseInt(e.value);
					}
				}
			}
			return null;
		}
		
	}
