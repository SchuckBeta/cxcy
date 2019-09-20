package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by Administrator on 2018/2/22 0022.
 */
public class RtPxCroles {
	private String value;
    private String $$hashKey;

	public RtPxCroles() {
        this.$$hashKey = IdGen.uuid();
	}

	public RtPxCroles(String value) {
        this.$$hashKey = IdGen.uuid();
		this.value = value;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

    public String get$$hashKey() {
        return $$hashKey;
    }

    public void set$$hashKey(String $$hashKey) {
        this.$$hashKey = $$hashKey;
    }

    /**
     * 角色转换为RtPxCroles.
     * @param roles 角色列表
     * @return List
     */
     public static List<RtPxCroles> convert(List<Role> roles){
         List<RtPxCroles> croles = Lists.newArrayList();
         if(StringUtil.checkNotEmpty(roles)){
             for (Role role : roles) {
                 croles.add(new RtPxCroles(StringUtil.JSP_VAL_PREFIX + ActYwTool.FLOW_ROLE_ID_PREFIX + role.getId() + StringUtil.JSP_VAL_POSTFIX));
             }
         }
         return croles;
     }
}
