package com.oseasy.pie.modules.impdata.enums;

public enum ConGcontestColEnum {
		S0("0","name")
		,S1("1","leader")
		,S2("2","no")
		,S3("3","mobile")
		,S4("4","email")
		,S5("5","degree") //学历
		,S6("6","enterDate")
		,S7("7","graduation ")
		,S8("8","professional")
		,S9("9","level")
		,S10("10","type")
		,S11("11","members") //项目阶段
		,S12("12","introduction")
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

		private ConGcontestColEnum(String value, String name) {
			this.value=value;
			this.name=name;
		}
		public static Integer getValueByName(String name) {
			if (name!=null) {
				for(ConGcontestColEnum e: ConGcontestColEnum.values()) {
					if (e.name.equals(name)) {
						return Integer.parseInt(e.value);
					}
				}
			}
			return null;
		}
		
	}
