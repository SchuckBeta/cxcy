package com.oseasy.com.pcore.modules.sys.entity;

import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.vo.CorePropType;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 系统功能Entity.
 * @author chenh
 * @version 2018-03-30
 */
public class SysProp extends DataEntity<SysProp> {

	private static final long serialVersionUID = 1L;
	private String type;		// 配置类型
	private String title;		// 标题
	private String micaPanic;		// 面包屑
    private String onOff;       // 开关
    private List<SysPropItem> items;       //配置项

	public SysProp() {
		super();
	}

	public SysProp(String id){
		super(id);
	}

    public String getType() {
        return type;
    }

    public String getTypeName() {
        if(StringUtil.isEmpty(type)){
            return "";
        }

        CorePropType sysPropType = CorePropType.getByKey(type);
        if(sysPropType == null){
            return "";
        }

        return sysPropType.getName();
    }

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=255, message="标题长度必须介于 0 和 255 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Length(min=0, max=255, message="面包屑长度必须介于 0 和 255 之间")
	public String getMicaPanic() {
		return micaPanic;
	}

	public void setMicaPanic(String micaPanic) {
		this.micaPanic = micaPanic;
	}

	@Length(min=0, max=1, message="开关长度必须介于 0 和 1 之间")
	public String getOnOff() {
		return onOff;
	}

	public void setOnOff(String onOff) {
		this.onOff = onOff;
	}

    public List<SysPropItem> getItems() {
        return items;
    }

    public void setItems(List<SysPropItem> items) {
        this.items = items;
    }

    /**
     * 渲染变量及属性.
     */
    public static SysProp render(Map<String, Object> maps, SysProp prop) {
        if((prop == null) || StringUtil.checkEmpty(prop.getItems())){
            return prop;
        }

        Object obj = maps.get(prop.getType());
        if(obj == null){
            return prop;
        }

        List<SysPropItem> items = Lists.newArrayList();
        for (SysPropItem item : prop.getItems()) {
            item.setVarMaps(SysPropItem.render(obj, item));
            items.add(item);
        }
        prop.setItems(items);
        return prop;
    }

    /**
     * 批量渲染变量及属性.
     */
    public static List<SysProp> render(Map<String, Object> maps, List<SysProp> props) {
        if((maps == null) || StringUtil.checkEmpty(props)){
            return props;
        }

        List<SysProp> sysProps = Lists.newArrayList();
        for (SysProp prop : props) {
            sysProps.add(SysProp.render(maps, prop));
        }
        return sysProps;
    }
}