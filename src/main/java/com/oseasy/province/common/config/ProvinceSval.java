package com.oseasy.province.common.config;

import com.google.common.collect.Lists;
import com.oseasy.act.common.config.ActCkey;
import com.oseasy.act.common.config.ActPath;
import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.IEu;
import com.oseasy.com.common.utils.PathMsvo;

import java.util.List;

/**
 * @author: QM
 * @date: 2019/4/20 11:15
 * @description:  省后台常量
 */
public class ProvinceSval extends Sval {

	public static ProvincePath path = new ProvincePath();
	public static ProvinceCkey ck = new ProvinceCkey();

	public static final String ASD_HOME = "/auditstandard/index/home";//YWID标识
	public static final String ASD_INDX = "/auditstandard/index";//YWID标识
	public static final String ASD_INDEX = ASD_INDX + "?" + ActYwGroup.JK_ACTYW_ID + "=";//YWID标识

	public enum ProvinceEmskey implements IEu {
		AIM("aim", "aimtron");
		private String key;//url
		private String remark;
		ProvinceEmskey(String key, String remark) {
			this.key = key;
			this.remark = remark;
		}

		public static List<PathMsvo> toPmsvos() {
			List<PathMsvo> entitys = Lists.newArrayList();
			for (ActSval.ActEmskey entity : ActSval.ActEmskey.values()) {
				entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
			}
			return entitys;
		}

		public static List<CkeyMsvo> toCmsvos() {
			List<CkeyMsvo> entitys = Lists.newArrayList();
			for (ActSval.ActEmskey entity : ActSval.ActEmskey.values()) {
				entitys.add(new CkeyMsvo(entity.k(), entity.getRemark()));
			}
			return entitys;
		}

		@Override
		public String k() {
			return key;
		}

		public String getRemark() {
			return remark;
		}

	}
}
