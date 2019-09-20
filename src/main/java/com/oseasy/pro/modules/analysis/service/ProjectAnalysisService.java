package com.oseasy.pro.modules.analysis.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oseasy.pro.modules.analysis.dao.ProjectAnalysisDao;
import com.oseasy.pro.modules.analysis.vo.AnnerVo;
import com.oseasy.pro.modules.analysis.vo.AxisVo;
import com.oseasy.pro.modules.analysis.vo.BarVo;
import com.oseasy.pro.modules.analysis.vo.EchartVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional(readOnly = true)
public class ProjectAnalysisService {
	@Autowired
	private ProjectAnalysisDao projectAnalysisDao;
	public JSONObject getData() {
		JSONObject data=new JSONObject();
		data.put("data1", getData1());
		data.put("data2", getData2());
		data.put("data3", getData3());
		return data;
	}
	private JSONObject getData1() {
		JSONObject data=new JSONObject();
		List<Map<String, Object>> list1=projectAnalysisDao.getData1();
		if (list1!=null&&list1.size()>0) {
			JSONArray ja1=new JSONArray();
			JSONArray ja2=new JSONArray();
			for(Map<String, Object> map:list1) {
				String label=map.get("label").toString();
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
		List<Map<String, Object>> list1=projectAnalysisDao.getData2();
		if (list1!=null&&list1.size()>0) {
			JSONArray ja1=new JSONArray();
			JSONArray ja2=new JSONArray();
			for(Map<String, Object> map:list1) {
				String label=map.get("label").toString();
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
	private JSONObject getData3() {
		JSONObject data=new JSONObject();
		List<Map<String, Object>> list=projectAnalysisDao.getData3();
		if (list!=null&&list.size()>0) {
			Map<String, Integer> map1=new HashMap<String,Integer>();//项目级别:索引值
			Map<String, Integer> map2=new HashMap<String,Integer>();//学院:索引值
			Map<String, Long> map3=new HashMap<String,Long>();//项目级别-学院:数据值
			List<String> list1=new ArrayList<String>();//项目级别集合，需有序
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
	public List<AnnerVo> findAllPojectByYear(String years) {
		List<AnnerVo> listnew=new ArrayList<AnnerVo>();
		List<Map<String,Object>> findlist=projectAnalysisDao.findAllPojectByYear(years);
		List<String> yearList= getYears();
		//得到类别
		List<String> typeList=getProjecTypes();

		if (yearList.size()==0) {
			return listnew;
		}
		//年份
		List<String> categeries=new ArrayList<>();
		for(int i=0;i<yearList.size();i++) {
			categeries.add(yearList.get(i));
		}
		for(int i=0;i<typeList.size();i++) {
			AnnerVo bar=new AnnerVo(typeList.get(i));
			bar.setCategories(categeries);

			for (int j = 0; j < categeries.size(); j++) {
				int yearNum = 0;
				for(Map<String,Object> echartVo:findlist) {
					String year=echartVo.get("year").toString();
					String label=echartVo.get("label").toString();
					int sum=Integer.valueOf(echartVo.get("sum").toString());
					//符合类别的
					if (bar.getName().equals(label)) {
						//符合年份的
						if (categeries.get(j).equals(year)) {
							yearNum= sum;
						}
					}
				}
				bar.addData(yearNum);
			}

			listnew.add(bar);
		}
		return listnew;
	}

	public List<String> getYears() {
		List<String> years=projectAnalysisDao.getYears();
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

	public List<String> getColleges() {
		return projectAnalysisDao.getColleges();
	}

	public List<String> getProjecTypes() {
		return projectAnalysisDao.getProjecTypes();
	}

	public List<EchartVo> findAllPojectByType(String year) {
		List<EchartVo> listnew=new ArrayList<EchartVo>();
		Map<String,String> map=new HashMap<>();
		map.put("year",year);
		List<Map<String,Object>> findlist=projectAnalysisDao.findAllPojectByType(map);

		for(Map<String,Object> echartVo:findlist) {
			int num=Integer.valueOf(echartVo.get("num").toString());
			EchartVo vo1=new EchartVo(echartVo.get("label").toString(),num);
			listnew.add(vo1);
		}
		return listnew;
	}

	public List<EchartVo> findPojectByTypeAndCategory(String label, String year) {
		List<EchartVo> listnew=new ArrayList<EchartVo>();
		Map<String,String> map=new HashMap<>();
		map.put("label",label);
		map.put("year",year);
		List<Map<String,Object>> findlist=projectAnalysisDao.findPojectByTypeAndCategory(map);
		for(Map<String,Object> echartVo:findlist) {
			int num=Integer.valueOf(echartVo.get("num").toString());
			EchartVo vo1=new EchartVo(echartVo.get("label").toString(),num);
			listnew.add(vo1);
		}
		return listnew;
	}

	public JSONObject findProjectApplyByType(String label, String year) {
		JSONObject js=new JSONObject();

		List<String> colleges= getColleges();
		//得到类别
		List<String> typeList=getProjecTypes();
		//学院
		List<String> categeries=new ArrayList<>();
		for(int i=0;i<colleges.size();i++) {
			categeries.add(colleges.get(i));
		}
		List<String> newcategeries=new ArrayList<>();
		Map<String,String> map=new HashMap<>();
		map.put("label",label);
		map.put("year",year);
		List<Map<String,Object>> findlist=projectAnalysisDao.findProjectApplyByType(map);
		List<Map<String,Object>> findLilist=projectAnalysisDao.findProjectLiByType(map);
		List<AxisVo> listnew=new ArrayList<AxisVo>();
		for(int i=0;i<typeList.size();i++) {
			AxisVo bar = new AxisVo(typeList.get(i)+"申报数");
			bar.setBarWidth(8);
			bar.setStack(typeList.get(i));
			bar.setType("bar");
			newcategeries.add(bar.getName());
			for (int j = 0; j < categeries.size(); j++) {
				int yearNum = 0;
				for(Map<String,Object> echartVo:findlist) {
					String name=echartVo.get("name").toString();
					String labelL=echartVo.get("label").toString();
					int num=Integer.valueOf(echartVo.get("num").toString());
					//符合类别的
					if (bar.getStack().equals(labelL)) {
						//符合学院的
						if (categeries.get(j).equals(name)) {
							yearNum= num;
						}
					}
				}
				bar.addData(yearNum);
			}
			listnew.add(bar);
		}

		for(int i=0;i<typeList.size();i++) {
			AxisVo bar = new AxisVo(typeList.get(i)+"立项数");
			bar.setBarWidth(8);
			bar.setStack(typeList.get(i));
			bar.setType("bar");
			newcategeries.add(bar.getName());
			for (int j = 0; j < categeries.size(); j++) {
				int yearNum = 0;
				for(Map<String,Object> echartVo:findLilist) {
					String name=echartVo.get("name").toString();
					String labelL=echartVo.get("label").toString();
					int num=Integer.valueOf(echartVo.get("num").toString());
					//符合类别的
					if (bar.getStack().equals(labelL)) {
						//符合学院的
						if (categeries.get(j).equals(name)) {
							yearNum= num;
						}
					}
				}
				bar.addData(yearNum);
			}
			listnew.add(bar);
		}

		js.put("seriesData",listnew);
		js.put("xAxisData",categeries);
		return js;
	}

	public JSONObject findProjectApplyLiByType(String label, String year) {
		JSONObject js=new JSONObject();

		List<String> colleges= getColleges();
		//得到类别
		List<String> typeList=getProjecTypes();
		//学院
		List<String> categeries=new ArrayList<>();
		for(int i=0;i<colleges.size();i++) {
			categeries.add(colleges.get(i));
		}
		List<String> newcategeries=new ArrayList<>();
		Map<String,String> map=new HashMap<>();
		map.put("label",label);
		map.put("year",year);
		List<Map<String,Object>> findlist=projectAnalysisDao.findProjectApplyByTypeNum(map);
		List<Map<String,Object>> findLilist=projectAnalysisDao.findProjectLiByTypeNum(map);
		List<AxisVo> listnew=new ArrayList<AxisVo>();

			AxisVo bar = new AxisVo("申报总数");
			bar.setBarWidth(8);
			bar.setStack("总数");
			bar.setType("bar");
			newcategeries.add(bar.getName());
			for (int j = 0; j < categeries.size(); j++) {
				int yearNum = 0;
				for(Map<String,Object> echartVo:findlist) {
					String name=echartVo.get("name").toString();
					int num=Integer.valueOf(echartVo.get("num").toString());
					//符合学院的
					if (categeries.get(j).equals(name)) {
						yearNum= num;
					}
				}
				bar.addData(yearNum);
			}
			listnew.add(bar);
			AxisVo barli = new AxisVo("立项总数");
			barli.setBarWidth(8);
			barli.setStack("总数");
			barli.setType("bar");
			newcategeries.add(bar.getName());
			for (int j = 0; j < categeries.size(); j++) {
				int yearNum = 0;
				for(Map<String,Object> echartVo:findLilist) {
					String name=echartVo.get("name").toString();
					int num=Integer.valueOf(echartVo.get("num").toString());
					//符合学院的
					if (categeries.get(j).equals(name)) {
						yearNum= num;
					}
				}
				barli.addData(yearNum);
			}
			listnew.add(barli);


		js.put("seriesData",listnew);
		js.put("xAxisData",categeries);
		return js;
	}
}
