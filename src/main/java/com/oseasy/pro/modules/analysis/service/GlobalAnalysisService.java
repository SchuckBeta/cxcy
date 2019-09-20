package com.oseasy.pro.modules.analysis.service;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.tool.process.vo.FlowPtype;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.pro.modules.analysis.dao.GlobalAnalysisDao;
import com.oseasy.pro.modules.analysis.vo.BarVo;
import com.oseasy.pro.modules.analysis.vo.EchartVo;
import com.oseasy.sys.common.config.SysSval;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional(readOnly = true)
public class GlobalAnalysisService {
	@Autowired
	private GlobalAnalysisDao globalAnalysisDao;

	public JSONObject getData() {
		JSONObject data=new JSONObject();
		data.put("data1", getData1());
		data.put("data2", getData2());
		data.put("data3", getData3());
		return data;
	}
	private JSONObject getData1() {
		JSONObject data=new JSONObject();
		List<Map<String, Object>> list1=globalAnalysisDao.getData1();
		if (list1!=null&&list1.size()>0) {
			JSONArray ja1=new JSONArray();
			JSONArray ja2=new JSONArray();
			for(Map<String, Object> map:list1) {
				String label=map.get("name").toString();
				long cc=((Long)map.get("cc")).longValue();

				ja1.add(label+"("+cc+")");

				JSONObject jo=new JSONObject();
				jo.put("value", cc);
				jo.put("name", label+"("+cc+")");
				ja2.add(jo);
			}
			data.put("ja1", ja1);
			data.put("ja2", ja2);
		}
		return data;
	}
	private JSONObject getData2() {
		JSONObject data=new JSONObject();
		List<Map<String, Object>> list1=globalAnalysisDao.getData2();
		if (list1!=null&&list1.size()>0) {
			JSONArray ja1=new JSONArray();
			JSONArray ja2=new JSONArray();
			JSONArray ja2_ret=new JSONArray();
			long s=0l;
			for(Map<String, Object> map:list1) {
				String label=map.get("name").toString();
				if ("双导".equals(label)) {
					s=((Long)map.get("cc")).longValue();
				}else{
					long cc=((Long)map.get("cc")).longValue();

					ja1.add(label+"("+cc+")");

					JSONObject jo=new JSONObject();
					jo.put("value", cc);
					jo.put("name", label);
					ja2.add(jo);
				}
			}

			for(int i=0;i<ja2.size();i++) {
				JSONObject jo=ja2.getJSONObject(i);
				jo.put("value", jo.getLong("value")+s);
				jo.put("name", jo.getString("name")+"("+jo.getLong("value")+")");
				ja2_ret.add(jo);
			}

			data.put("ja1", ja1);
			data.put("ja2", ja2_ret);
		}
		return data;
	}
	private JSONObject getData3() {
		JSONObject data=new JSONObject();
		List<Map<String, Object>> list=globalAnalysisDao.getData3();
		if (list!=null&&list.size()>0) {
			list=getTempData(list);
			Map<String, Integer> map1=new HashMap<String,Integer>();//学生导师:索引值
			Map<String, Integer> map2=new HashMap<String,Integer>();//年份:索引值
			Map<String, Long> map3=new HashMap<String,Long>();//学生导师-年份:数据值
			List<String> list1=new ArrayList<String>();//学生导师集合，需有序
			List<String> list2=new ArrayList<String>();//年份集合，需有序

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
			JSONArray ja2=new JSONArray();
			for(String name:list2) {
				ja2.add(name+"("+getBili(map3.get("导师-"+name),map3.get("学生-"+name))+")");
			}
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
	private String getBili(Long l1,Long l2) {
		float f1=(float)l1;
		float f2=(float)l2;
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(f2/f1);
	}
	private List<Map<String, Object>> getTempData(List<Map<String, Object>> list) {
		List<Map<String, Object>> l=new ArrayList<Map<String, Object>>();
		Map<String, Object> map1=new HashMap<String, Object> ();
		map1.put("label", "学生");
		map1.put("name", "2013");
		map1.put("cc", 85l);
		Map<String, Object> map11=new HashMap<String, Object> ();
		map11.put("label", "导师");
		map11.put("name", "2013");
		map11.put("cc", 55l);
		Map<String, Object> map2=new HashMap<String, Object> ();
		map2.put("label", "学生");
		map2.put("name", "2014");
		map2.put("cc", 45l);
		Map<String, Object> map21=new HashMap<String, Object> ();
		map21.put("label", "导师");
		map21.put("name", "2014");
		map21.put("cc", 23l);
		Map<String, Object> map3=new HashMap<String, Object> ();
		map3.put("label", "学生");
		map3.put("name", "2015");
		map3.put("cc", 66l);
		Map<String, Object> map31=new HashMap<String, Object> ();
		map31.put("label", "导师");
		map31.put("name", "2015");
		map31.put("cc", 53l);
		Map<String, Object> map4=new HashMap<String, Object> ();
		map4.put("label", "学生");
		map4.put("name", "2016");
		map4.put("cc", 99l);
		Map<String, Object> map41=new HashMap<String, Object> ();
		map41.put("label", "导师");
		map41.put("name", "2016");
		map41.put("cc", 65l);
		l.add(map1);
		l.add(map2);
		l.add(map3);
		l.add(map4);
		l.add(map11);
		l.add(map21);
		l.add(map31);
		l.add(map41);
		l.addAll(list);
		return l;
	}

	/**
	 * 根据年份获得国创项目数
	 * @param year
	 * @return
     */
	public int getProjectNumber(String year) {
		Map<String, Object> map=new HashMap<>();
		if (StringUtils.isNotBlank(year)) {
			int yearNumber=Integer.parseInt(year);
			Date createDateStart=new Date(yearNumber-1900,1,1);
			Date createDateEnd=new Date(yearNumber-1900,12,31);
			map.put("createDateStart",createDateStart);
			map.put("createDateEnd",createDateEnd);
		}
		int count=globalAnalysisDao.getProjectNumber(map);
		return count;
	}

	/**
	 * 根据年份获得 互联网+大赛参赛人数
	 * @param year
	 * @return
     */
	public int getContestNumber(String year) {
		Map<String, Object> map=new HashMap<>();
		if (StringUtils.isNotBlank(year)) {
			int yearNumber=Integer.parseInt(year);
			Date createDateStart=new Date(yearNumber-1900,1,1);
			Date createDateEnd=new Date(yearNumber-1900,12,31);
			map.put("createDateStart",createDateStart);
			map.put("createDateEnd",createDateEnd);
		}
		int count=globalAnalysisDao.getContestNumber(map);
		return count;
	}
	//获得所有大赛类型参数数量
	public List<EchartVo> findAllGcontestType(String year) {
		List<EchartVo> listnew=new ArrayList<EchartVo>();
		List<Dict> distList=DictUtils.getDictList(FlowPtype.PTT_DASAI.getKey());
		List<Map<String,Object>> list=globalAnalysisDao.findAllGcontestType(year);
		/*for(Dict dist:distList) {
			Boolean isTrue = true;*/
			EchartVo echartV = null;
			for(Map<String,Object> echartVo:list) {
				/*if (echartVo.get("label").toString().equals(dist.getLabel())) {
					isTrue = false;*/
					int num=Integer.valueOf(echartVo.get("num").toString());
					echartV = new EchartVo(echartVo.get("label").toString(),num);
					listnew.add(echartV);
				/*}*/
			}
		/*	if (isTrue) {
				echartV = new EchartVo(dist.getLabel(),0);
			}*/
			//listnew.add(echartV);
		/*}*/
		return listnew;
	}
	//获得所有项目类型参数数量
	public List<EchartVo> findAllProjectType(String year) {
		List<EchartVo> listnew=new ArrayList<EchartVo>();
		List<Map<String,Object>> list=globalAnalysisDao.findAllProjectType(year);
		for(Map<String,Object> echartVo:list) {
			int num=Integer.valueOf(echartVo.get("num").toString());
			EchartVo vo1=new EchartVo(echartVo.get("label").toString(),num);
			listnew.add(vo1);
		}
		return listnew;
	}
	//获得所有大赛参赛学生分布数量
	public List<EchartVo> findAllGcontestStuCurrState(String year) {
		List<EchartVo> listnew=new ArrayList<EchartVo>();
		Map<String,String> map=new HashMap<>();
		map.put("year",year);
		List<Map<String,Object>> list=globalAnalysisDao.findAllGcontestStuCurrState(map);
		for(Map<String,Object> echartVo:list) {
			int num=Integer.valueOf(echartVo.get("num").toString());
			EchartVo vo1=new EchartVo(echartVo.get("label").toString(),num);
			listnew.add(vo1);
		}
		return listnew;
	}

	//获得所有大赛导师分布数量
	public List<EchartVo> findAllTeacherByType(String year) {
		List<EchartVo> listnew=new ArrayList<EchartVo>();
		List<Map<String,Object>> list=globalAnalysisDao.findAllTeacherByType(year);
		for(Map<String,Object> echartVo:list) {
			int num=Integer.valueOf(echartVo.get("num").toString());
			EchartVo vo1=new EchartVo(echartVo.get("label").toString(),num);
			listnew.add(vo1);
		}
		return listnew;
	}

	public List<String> getProjecTypes() {
		return globalAnalysisDao.getProjecTypes();
	}

	public List<BarVo> findAllProjectStuCurrState(String year) {
		List<BarVo> listnew=new ArrayList<BarVo>();

		Map<String,String> map=new HashMap<>();
		map.put("year",year);
		List<Map<String,Object>> list=globalAnalysisDao.findAllProjectStuCurrState(map);

		List<String> types=getProjecTypes();

		List<String> categeries=new ArrayList<>();
		for(int i=0;i<types.size();i++) {
			categeries.add(types.get(i));
		}
		List<Dict> distList=DictUtils.getDictList("current_sate");
		for(int i=0;i<distList.size();i++) {
			BarVo bar=new BarVo(distList.get(i).getLabel());
			for(int j=0;j<types.size();j++) {
				String label=types.get(j);
				int stateNum=0;
				for(Map<String,Object> echartVo:list) {
					String proTypeValue=echartVo.get("proType").toString();
					String curStateValue=echartVo.get("curStateValue").toString();
					int num=Integer.valueOf(echartVo.get("num").toString());
					//符合创新项目
					if (proTypeValue.equals(label)) {
						//符合在校学生
						if (curStateValue.equals(distList.get(i).getValue())) {
							stateNum=num;
						}
					}
				}
				bar.addValue(stateNum);
			}
			bar.setCategories(categeries);
			listnew.add(bar);
		}
		return listnew;
	}

	public List<BarVo> findHotTechnology(String year) {
		List<BarVo> listnew=new ArrayList<BarVo>();
		Map<String,String> map=new HashMap<>();
		map.put("year",year);
		List<Map<String,Object>> list=globalAnalysisDao.findHotTechnology(map);

		List<Dict> distList=DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
		List<String> bars=new ArrayList<>();
		/*bars.add("国创项目");
		bars.add("创业项目");
		bars.add("创新项目");*/

		List<String> types=getProjecTypes();

		for(int i=0;i<types.size();i++) {
			bars.add(types.get(i));
		}

		List<String> categeries=new ArrayList<>();
		for(int j=0;j<distList.size();j++) {
			categeries.add(distList.get(j).getLabel());
		}
		//List<Dict> distList=DictUtils.getDictList("current_sate");

		for(int i=0;i<bars.size();i++) {
			BarVo bar=new BarVo(bars.get(i));
			//领域
			for(int j=0;j<distList.size();j++) {
				int techNum=0;
				for(Map<String,Object> echartVo:list) {
					String proTypeValue = echartVo.get("label").toString();
					String domain = echartVo.get("domain").toString();
					//符合创新项目
					if (proTypeValue.equals(bar.getName())) {
						//符合领域
						if (domain.contains(distList.get(j).getValue())) {
							techNum = techNum + 1;
						}
					}
				}
				bar.addValue(techNum);
			}
			bar.setCategories(categeries);
			listnew.add(bar);
		}
		return listnew;
	}

	public List<BarVo> findTeacherDtn(String year) {
		List<BarVo> listnew=new ArrayList<BarVo>();
		List<Map<String,Object>> list=globalAnalysisDao.findTeacherDtn(year);

		BarVo bar1=new BarVo("学生点赞数");
		List<String> categeries=new ArrayList<>();
		for(int j=0;j<list.size();j++) {
			if (j>4) {
				break;
			}
			int num=Integer.valueOf(list.get(j).get("likes").toString());
			categeries.add(list.get(j).get("name").toString());
			bar1.addValue(num);
		}
		bar1.setCategories(categeries);
		listnew.add(bar1);

		return listnew;
	}

	public List<String> getYears() {
		List<String> years=globalAnalysisDao.getYears();
		if (years.size()>0) {
			int minYear=Integer.valueOf(years.get(0));
			int maxYear=Integer.valueOf(years.get((years.size()-1)));
			years.clear();
			for(int j=minYear;j<=maxYear;j++) {
				years.add(String.valueOf(j));
			}
		}
		return years;
	}

}
