package com.oseasy.pie.modules.impdata.enums;

public enum BackUserColEnum {
		 S0("0","login_name")
		,S1("1","user_type")
		,S2("2","roles")
		,S3("3","name")
		,S4("4","no")
		,S5("5","mobile")
		,S6("6","email")
		,S7("7","remarks")
		,S8("8","domain")
		,S9("9","office_id")
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

		private BackUserColEnum(String value, String name) {
			this.value=value;
			this.name=name;
		}
		public static Integer getValueByName(String name) {
			if (name!=null) {
				for(BackUserColEnum e:BackUserColEnum.values()) {
					if (e.name.equals(name)) {
						return Integer.parseInt(e.value);
					}
				}
			}
			return null;
		}
		
	}
