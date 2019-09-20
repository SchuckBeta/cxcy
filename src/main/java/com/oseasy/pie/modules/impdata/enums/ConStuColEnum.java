package com.oseasy.pie.modules.impdata.enums;

public enum ConStuColEnum {
		 S0("0","name")
		,S1("1","office_id")
		,S2("2","professional")
		,S3("3","mobile")
		,S4("4","area")
		,S5("5","company_id")
		,S6("6","email")
		,S7("7","enterDate")
		,S8("8","graduation")
		,S9("9","no")
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

		private ConStuColEnum(String value, String name) {
			this.value=value;
			this.name=name;
		}
		public static Integer getValueByName(String name) {
			if (name!=null) {
				for(ConStuColEnum e: ConStuColEnum.values()) {
					if (e.name.equals(name)) {
						return Integer.parseInt(e.value);
					}
				}
			}
			return null;
		}
		
	}
