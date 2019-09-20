package com.oseasy.pie.modules.impdata.enums;

public enum ProModelColEnum {
		S0("0","office")
		,S1("1","name")
		,S2("2","number")
		,S3("3","type")
		,S4("4","leader")
		,S5("5","no")
        ,S6("6","team_id")
        ,S7("7","team_name")
        ,S8("8","mobile")
		,S9("9","email")
		,S10("10","profes")
		,S11("11","members")
		,S12("12","teachers")
		,S13("13","tea_no")
		,S14("14","tea_title")
		,S15("15","year")
		,S16("16","result")
		,S17("17","hasfile")
        ,S18("18","introduction")
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

		private ProModelColEnum(String value, String name) {
			this.value=value;
			this.name=name;
		}
		public static Integer getValueByName(String name) {
			if (name!=null) {
				for(ProModelColEnum e:ProModelColEnum.values()) {
					if (e.name.equals(name)) {
						return Integer.parseInt(e.value);
					}
				}
			}
			return null;
		}

	}
