package com.oseasy.pie.modules.impdata.enums;

import com.oseasy.act.modules.actyw.tool.process.ActYwTool;

public enum ProjectHsColEnum {
		S0("0","office")
		,S1("1","name")
		,S2("2","number")
		,S3("3","type")
		,S4("4","leader")
		,S5("5","no")
		,S6("6","mobile")
		,S7("7","email")
		,S8("8","profes")
		,S9("9",ActYwTool.FLOW_PROP_GATEWAY_STATE)
		,S10("10","members")
		,S11("11","teachers")
		,S12("12","tea_no")
		,S13("13","tea_title")
		,S14("14","level")
		,S15("15","remarks")
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

		private ProjectHsColEnum(String value, String name) {
			this.value=value;
			this.name=name;
		}
		public static Integer getValueByName(String name) {
			if (name!=null) {
				for(ProjectHsColEnum e:ProjectHsColEnum.values()) {
					if (e.name.equals(name)) {
						return Integer.parseInt(e.value);
					}
				}
			}
			return null;
		}

	}
