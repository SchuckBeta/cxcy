package com.oseasy.province.common.config;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;

import java.util.List;

/**
 * @author: QM
 * @date: 2019/4/20 11:21
 * @description: 省后台模块路径常量
 */
public class ProvincePath extends SupPath implements IPath {

	public ProvincePath() {
		super();
	}

	@Override
	public Sval.Emkey emkey() {
		return Sval.Emkey.CMS;
	}

	@Override
	public List<PathMsvo> mskeys() {
		return ProvinceSval.ProvinceEmskey.toPmsvos();
	}

	@Override
	public IPath path() {
		return ProvinceSval.path;
	}

	@Override
	public PathMvo curmkey() {
		return mkey(mkey());
	}
}
