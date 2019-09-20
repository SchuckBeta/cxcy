package com.oseasy.com.mqserver.common.utils.sms.impl;

import com.oseasy.com.mqserver.common.utils.sms.ISendParam;

/**
 * Created by victor on 2017/9/28.
 */
public class SendParam extends ISendParam{
    private String name;
    private String team;
    private String toMobile;

    public SendParam(String name, String team) {
        this.name = name;
        this.team = team;
    }

    public SendParam(String name, String team, String toMobile) {
		this.name = name;
		this.team = team;
		this.toMobile = toMobile;
	}

	public String getToMobile() {
		return toMobile;
	}

	public void setToMobile(String toMobile) {
		this.toMobile = toMobile;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
