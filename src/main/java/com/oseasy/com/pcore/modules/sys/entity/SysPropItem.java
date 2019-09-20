package com.oseasy.com.pcore.modules.sys.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.Reflections;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.VarMap;

/**
 * 系统功能配置项Entity.
 * @author chenh
 * @version 2018-03-30
 */
public class SysPropItem extends DataEntity<SysPropItem> {

	private static final long serialVersionUID = 1L;
	private SysProp prop;		// 系统配置ID
	private String name;		// 标题
	private String isOpen;		// 开关
	private String params;		// 参数JSON数组
	private String pfomat;		// 参数JSON默认值
	private String instruction;		// 介绍信息
	private List<VarMap> varMaps;		// 介绍信息

	public SysPropItem() {
		super();
	}

	public SysPropItem(String id){
		super(id);
	}

	@NotNull(message="系统配置ID不能为空")
	public SysProp getProp() {
		return prop;
	}

	public void setProp(SysProp prop) {
		this.prop = prop;
	}

	@Length(min=0, max=255, message="标题长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=1, message="开关长度必须介于 0 和 1 之间")
	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	@Length(min=0, max=255, message="参数JSON数组长度必须介于 0 和 255 之间")
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	@Length(min=0, max=255, message="参数JSON默认值长度必须介于 0 和 255 之间")
	public String getPfomat() {
		return pfomat;
	}

	public void setPfomat(String pfomat) {
		this.pfomat = pfomat;
	}

	@Length(min=0, max=255, message="介绍信息长度必须介于 0 和 255 之间")
	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

   @Length(min=0, max=1, message="开关长度必须介于 0 和 1 之间")
    public String getOpenval() {
       if((this.pfomat == null) && (this.pfomat == null)){

       }
        return isOpen;
    }

    public List<VarMap> getVarMaps() {
        return varMaps;
    }

    public void setVarMaps(List<VarMap> varMaps) {
        this.varMaps = varMaps;
    }

    /**
     * 渲染变量及属性.
     */
    public static List<VarMap> render(Object obj, SysPropItem pitem) {
        List<VarMap> varMaps = Lists.newArrayList();
        if(StringUtil.isEmpty(pitem.getInstruction())){
            return varMaps;
        }else{
            varMaps = Reflections.reflectVarMaps(obj, VarMap.splitVar(pitem.getInstruction(), StringUtil.JSP_VAL_PREFIX, StringUtil.JSP_VAL_POSTFIX));
        }
        return varMaps;
    }

    public static void main(String[] args) {
        SysPropItem pitem = new SysPropItem();
        pitem.setInstruction("ssss${name}ssdds${no}sad${email}sadlkas${password}kasdas${loginName}kaksad${loginName}kaksd${loginName}kaskd${loginName}kakasd");
        User user = new User();
        user.setLoginName("张三");
        user.setPassword("爬山");
        user.setNo("NO999");
        user.setName("哈哈");
        user.setEmail("316937855@qq.com");
        List<VarMap> ss = SysPropItem.render(user, pitem);
        System.out.println(VarMap.renderVar(VarMap.splitVar(pitem.getInstruction(), StringUtil.JSP_VAL_PREFIX, StringUtil.JSP_VAL_POSTFIX)));

//        List<VarMap> vmaps = Reflections.reflectVarMaps(user, VarMap.splitVar(pitem.getInstruction(), StringUtil.JSP_VAL_PREFIX, ActYwTool.FLOW_ROLE_POSTFIX));
//        System.out.println(VarMap.renderVar(vmaps));
//
//        String sss = Reflections.reflectVarMapStr(user, VarMap.splitVar(pitem.getInstruction(), StringUtil.JSP_VAL_PREFIX, ActYwTool.FLOW_ROLE_POSTFIX));
//        System.out.println(sss);
    }
}