package com.oseasy.pro.modules.analysis.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.pro.modules.analysis.dao.GcontestAnalysisDao;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional(readOnly = true)
public class GcontestAnalysisService {
	@Autowired
	private GcontestAnalysisDao gcontestAnalysisDao;
	public JSONArray getGcontestMemNum(String year) {
		if (StringUtil.isNotEmpty(year)) {
			year=year+"-01-01 00:00:00";
		}
		JSONArray ja=new JSONArray();
		Set<String> names=new HashSet<String>();
		Map<String,String> data=new HashMap<String,String>();
		List<Map<String, Object>> olist=gcontestAnalysisDao.getGcontestMemNum(year);
		if (olist!=null&&olist.size()>0) {
			for(Map<String, Object> map:olist) {
				if (map.get("label")!=null&&map.get("year")!=null) {
					names.add(map.get("year").toString());
					data.put(map.get("label").toString()+map.get("year").toString(), map.get("cc").toString());
				}
			}
		}
		List<Map<String, Object>> nlist=gcontestAnalysisDao.getGcontestMemNumFromModel(year);
		if (nlist!=null&&nlist.size()>0) {
			for(Map<String, Object> map:nlist) {
				if (map.get("label")!=null&&map.get("year")!=null) {
					names.add(map.get("year").toString());
					data.put(map.get("label").toString()+map.get("year").toString(), map.get("cc").toString());
				}
			}
		}
		if (names.size()>0) {
			JSONArray years=getGcontestTypeList(names);
			JSONArray curstateList=getCurstateList();
			if (years!=null&&curstateList!=null) {
				for(int m=0;m<curstateList.size();m++) {
					String name=curstateList.getString(m);
					JSONObject js=new JSONObject();
					JSONArray yeardata=new JSONArray();
					for(String s:names) {
						String num=data.get(name+s);
						if (num==null) {
							yeardata.add(0);
						}else{
							yeardata.add(Integer.parseInt(num));
						}
					}
					js.put("name", name+"参赛人数");
					js.put("categories", years);
					js.put("data", yeardata);
					ja.add(js);
				}
			}
		}
		return ja;
	}
	private JSONArray getCurstateList() {
		List<Dict> list=DictUtils.getDictList("current_sate");
		if (list==null||list.size()==0) {
			return null;
		}else{
			JSONArray ja=new JSONArray();
			for(Dict s:list) {
				ja.add(s.getLabel());
			}
			return ja;
		}
	}
	private JSONArray getGcontestTypeList(Set<String> names) {
		if (names==null||names.size()==0) {
			return null;
		}else{
			JSONArray ja=new JSONArray();
			for(String s:names) {
				ja.add(s);
			}
			return ja;
		}
	}
	public JSONArray getGcontestOfficeNum(String type,String year) {
		if (StringUtil.isNotEmpty(year)) {
			year=year+"-01-01 00:00:00";
		}
		JSONArray ja=new JSONArray();
		JSONObject js=new JSONObject();
		JSONArray data=new JSONArray();
		JSONArray categories=new JSONArray();
		List<Map<String, Object>> olist=null;
		List<Map<String, Object>> nlist=null;
		Map<String,Integer> mdata=new HashMap<String,Integer>();
		if (StringUtil.isEmpty(type)) {
			olist=gcontestAnalysisDao.getGcontestOfficeNum(type, year);
			nlist=gcontestAnalysisDao.getGcontestOfficeNumFromModel(type, year);
		}else{
			if ("1".equals(type)) {
				olist=gcontestAnalysisDao.getGcontestOfficeNum(type, year);
			}else{
				nlist=gcontestAnalysisDao.getGcontestOfficeNumFromModel(type, year);
			}
		}
		if (olist!=null&&olist.size()>0) {
			for(Map<String, Object> map:olist) {
				Integer tem=mdata.get(map.get("name").toString());
				if (tem==null) {
					mdata.put(map.get("name").toString(),Integer.parseInt(map.get("cc").toString()));
				}else{
					mdata.put(map.get("name").toString(),Integer.parseInt(map.get("cc").toString())+tem);
				}
			}
		}
		if (nlist!=null&&nlist.size()>0) {
			for(Map<String, Object> map:nlist) {
				Integer tem=mdata.get(map.get("name").toString());
				if (tem==null) {
					mdata.put(map.get("name").toString(),Integer.parseInt(map.get("cc").toString()));
				}else{
					mdata.put(map.get("name").toString(),Integer.parseInt(map.get("cc").toString())+tem);
				}
			}
		}
		js.put("name", "校级初赛");
		for(String k:mdata.keySet()) {
			categories.add(k);
			data.add(mdata.get(k));
		}
		js.put("data", data);
		js.put("categories", categories);
		ja.add(js);
		return ja;
	}
	public JSONArray getGcontestNum() {
		JSONArray ja=new JSONArray();
		int maxYear=0;
		int minYear=0;
		Set<String> names=new HashSet<String>();
		Map<String,String> data=new HashMap<String,String>();
		List<Map<String, Object>> olist=gcontestAnalysisDao.getGcontestNum();
		if (olist!=null&&olist.size()>0) {
			for(Map<String, Object> map:olist) {
				names.add(map.get("label").toString());
				if (map.get("year")!=null) {
					int cy=Integer.valueOf(map.get("year").toString());
					if (minYear==0||cy<minYear) {
						minYear=cy;
					}
					if (maxYear==0||cy>maxYear) {
						maxYear=cy;
					}
					data.put(map.get("label").toString()+cy+"年", map.get("cc").toString());
				}
			}
		}
		List<Map<String, Object>> nlist=gcontestAnalysisDao.getGcontestNumFromModel();
		if (nlist!=null&&nlist.size()>0) {
			for(Map<String, Object> map:nlist) {
				names.add(map.get("label").toString());
				if (map.get("year")!=null) {
					int cy=Integer.valueOf(map.get("year").toString());
					if (minYear==0||cy<minYear) {
						minYear=cy;
					}
					if (maxYear==0||cy>maxYear) {
						maxYear=cy;
					}
					data.put(map.get("label").toString()+cy+"年", map.get("cc").toString());
				}
			}
		}
		if (names.size()>0) {
			JSONArray years=getYearList(minYear, maxYear);
			if (years!=null) {
				for(String name:names) {
					JSONObject js=new JSONObject();
					JSONArray yeardata=new JSONArray();
					for(int i=0;i<years.size();i++) {
						String num=data.get(name+years.getString(i));
						if (num==null) {
							yeardata.add(0);
						}else{
							yeardata.add(Integer.parseInt(num));
						}
					}
					js.put("name", name);
					js.put("data", yeardata);
					js.put("categories", years);
					ja.add(js);
				}
			}else{
				for(String name:names) {
					JSONObject js=new JSONObject();
					js.put("name", name);
					js.put("data", new JSONArray());
					js.put("categories", new JSONArray());
					ja.add(js);
				}
			}
		}
		return ja;
	}
	private JSONArray getYearList(int minYear,int maxYear) {
		if (maxYear==0||minYear==0) {
			return null;
		}else{
			JSONArray list=new JSONArray();
			for(int i=minYear;i<=maxYear;i++) {
				list.add(i+"年");
			}
			return list;
		}
	}
	public JSONObject getData() {
		JSONObject data=new JSONObject();
		data.put("data1", getData1());
		data.put("data2", getData2());
		data.put("data3", getData3());
		return data;
	}
	private JSONObject getData1() {
		JSONObject data=new JSONObject();
		List<Map<String, Object>> list=gcontestAnalysisDao.getData1();
		if (list!=null&&list.size()>0) {
			Map<String, Integer> map1=new HashMap<String,Integer>();//大赛类型:索引值
			Map<String, Integer> map2=new HashMap<String,Integer>();//学院:索引值
			Map<String, Long> map3=new HashMap<String,Long>();//大赛类型-学院:数据值
			List<String> list1=new ArrayList<String>();//大赛类型集合，需有序
			List<String> list2=new ArrayList<String>();//学院集合，需有序
			
			for(Map<String, Object> map:list) {
				String label=map.get("label").toString();
				String name=map.get("name").toString();
				long cc=((Long)map.get("cc")).longValue();
				if (map1.get(label)==null) {
					list1.add(label);
					map1.put(label, list1.size()-1);
				}
				if (map2.get(name)==null) {
					list2.add(name);
					map2.put(name, list2.size()-1);
				}
				if (map3.get(label+"-"+name)==null) {
					map3.put(label+"-"+name, cc);
				}
			}
			
			JSONArray ja1=JSONArray.fromObject(list1);
			JSONArray ja2=JSONArray.fromObject(list2);
			JSONArray ja3=new JSONArray();
			for(String label:list1) {
				JSONObject series=getData1Series();
				series.put("name", label);
				long[] ls=new long[list2.size()];
				for(String name:list2) {
					if (map3.get(label+"-"+name)!=null) {
						ls[map2.get(name)]=map3.get(label+"-"+name);
					}
				}
				series.put("data", JSONArray.fromObject(ls));
				ja3.add(series);
			}
			data.put("ja1", ja1);
			data.put("ja2", ja2);
			data.put("ja3", ja3);
		}
		return data;
	}
	private JSONObject getData1Series() {
		JSONObject js=new JSONObject();
		js.put("type", "bar");
		js.put("barMaxWidth", 30);
		js.put("stack", "搜索引擎");
		JSONObject itemStyle=new JSONObject();
		JSONObject normal=new JSONObject();
		normal.put("color", "#5c6d78");
		itemStyle.put("normal",normal);
		js.put("itemStyle", itemStyle);
		JSONObject label=new JSONObject();
		JSONObject normal1=new JSONObject();
		normal1.put("show", true);
		normal1.put("position", "inside");
		label.put("normal",normal1);
		js.put("label", label);
		return js;
	}
	private JSONObject getData2() {
		JSONObject data=new JSONObject();
		List<Map<String, Object>> list=gcontestAnalysisDao.getData2();
		if (list!=null&&list.size()>0) {
			Map<String, Integer> map1=new HashMap<String,Integer>();//在校学生参赛:索引值
			Map<String, Integer> map2=new HashMap<String,Integer>();//组别:索引值
			Map<String, Long> map3=new HashMap<String,Long>();//在校学生参赛-组别:数据值
			List<String> list1=new ArrayList<String>();//在校学生参赛集合，需有序
			List<String> list2=new ArrayList<String>();//组别集合，需有序
			
			for(Map<String, Object> map:list) {
				String label=map.get("label").toString();
				String name=map.get("name").toString();
				long cc=((Long)map.get("cc")).longValue();
				if (map1.get(label)==null) {
					list1.add(label);
					map1.put(label, list1.size()-1);
				}
				if (map2.get(name)==null) {
					list2.add(name);
					map2.put(name, list2.size()-1);
				}
				if (map3.get(label+"-"+name)==null) {
					map3.put(label+"-"+name, cc);
				}
			}
			
			JSONArray ja1=JSONArray.fromObject(list1);
			JSONArray ja2=JSONArray.fromObject(list2);
			JSONArray ja3=new JSONArray();
			for(String label:list1) {
				JSONObject series=new JSONObject();
				series.put("name", label);
				series.put("type", "bar");
				series.put("barMaxWidth", 30);
				long[] ls=new long[list2.size()];
				for(String name:list2) {
					if (map3.get(label+"-"+name)!=null) {
						ls[map2.get(name)]=map3.get(label+"-"+name);
					}
				}
				series.put("data", JSONArray.fromObject(ls));
				ja3.add(series);
			}
			data.put("ja1", ja1);
			data.put("ja2", ja2);
			data.put("ja3", ja3);
		}
		return data;
	}
	private JSONObject getData3() {
		JSONObject data=new JSONObject();
		List<Map<String, Object>> list=gcontestAnalysisDao.getData3();
		if (list!=null&&list.size()>0) {
			Map<String, Integer> map1=new HashMap<String,Integer>();//'参赛项目数':索引值
			Map<String, Integer> map2=new HashMap<String,Integer>();//互联网+参赛类别:索引值
			Map<String, Long> map3=new HashMap<String,Long>();//'参赛项目数'-互联网+参赛类别:数据值
			List<String> list1=new ArrayList<String>();//'参赛项目数'集合，需有序
			List<String> list2=new ArrayList<String>();//互联网+参赛类别集合，需有序
			
			for(Map<String, Object> map:list) {
				String label=map.get("label").toString();
				String name=map.get("name").toString();
				long cc=((Long)map.get("cc")).longValue();
				if (map1.get(label)==null) {
					list1.add(label);
					map1.put(label, list1.size()-1);
				}
				if (map2.get(name)==null) {
					list2.add(name);
					map2.put(name, list2.size()-1);
				}
				if (map3.get(label+"-"+name)==null) {
					map3.put(label+"-"+name, cc);
				}
			}
			
			JSONArray ja1=JSONArray.fromObject(list1);
			JSONArray ja2=JSONArray.fromObject(list2);
			JSONArray ja3=new JSONArray();
			for(String label:list1) {
				JSONObject series=getData3Series();
				series.put("name", label);
				long[] ls=new long[list2.size()];
				for(String name:list2) {
					if (map3.get(label+"-"+name)!=null) {
						ls[map2.get(name)]=map3.get(label+"-"+name);
					}
				}
				series.put("data", JSONArray.fromObject(ls));
				ja3.add(series);
			}
			data.put("ja1", ja1);
			data.put("ja2", ja2);
			data.put("ja3", ja3);
		}
		return data;
	}
	private JSONObject getData3Series() {
		JSONObject js=new JSONObject();
		js.put("type", "bar");
		js.put("barMaxWidth",30);
		JSONObject label=new JSONObject();
		JSONObject normal1=new JSONObject();
		normal1.put("show", true);
		normal1.put("position", "inside");
		JSONObject textStyle=new JSONObject();
		textStyle.put("color", "#000");
		normal1.put("textStyle", textStyle);
		label.put("normal",normal1);
		js.put("label", label);
		return js;
	}
}
