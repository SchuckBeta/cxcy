package com.oseasy.province.common.config;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.CkeyMvo;
import com.oseasy.com.common.utils.ICkey;
import com.oseasy.com.common.utils.SupCkey;

import java.util.List;

/**
 * @author: QM
 * @date: 2019/4/20 11:24
 * @description: 国创缓存标识
 */
public class ProvinceCkey extends SupCkey implements ICkey{

	public ProvinceCkey() {
		super();
	}

	@Override
	public Sval.Emkey emkey() {
		return Sval.Emkey.ACT;
	}

	@Override
	public List<CkeyMsvo> mskeys() {
		return ProvinceSval.ProvinceEmskey.toCmsvos();
	}

	@Override
	public ICkey path() {
		return ActSval.ck;
	}

	@Override
	public CkeyMvo curmkey() {
		return mkey(mkey());
	}
}
