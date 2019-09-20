package com.oseasy.pie.modules.impdata.enums;

public enum GcontestColEnum {
		S0("0","name")
		,S1("1","type")
		,S2("2","groups")
		,S3("3","leader")
		,S4("4","office")
		,S5("5","profes")
		,S6("6","mobile")
		,S7("7","steachers")
		,S8("8","eteachers")
		,S9("9","remarks")
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

		private GcontestColEnum(String value, String name) {
			this.value=value;
			this.name=name;
		}
		public static Integer getValueByName(String name) {
			if (name!=null) {
				for(GcontestColEnum e:GcontestColEnum.values()) {
					if (e.name.equals(name)) {
						return Integer.parseInt(e.value);
					}
				}
			}
			return null;
		}
		
	}
