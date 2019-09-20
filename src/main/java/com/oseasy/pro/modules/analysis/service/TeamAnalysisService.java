package com.oseasy.pro.modules.analysis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.pro.modules.analysis.dao.TeamAnalysisDao;
import com.oseasy.pro.modules.analysis.vo.AnnerVo;
import com.oseasy.pro.modules.analysis.vo.AxisVo;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional(readOnly = true)
public class TeamAnalysisService {
	@Autowired
	private TeamAnalysisDao teamAnalysisDao;
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
	public JSONObject getTeamYearMemsNum() {
		int maxYear=0;
		int minYear=0;
		Set<String> protype=new HashSet<String>();
		Map<String,String> data=new HashMap<String,String>();
		List<Map<String, Object>> plist=teamAnalysisDao.getTeamYearMemsFromProject();
		if (plist!=null&&plist.size()>0) {
			for(Map<String, Object> map:plist) {
				if (map.get("label")!=null&&map.get("name")!=null) {
					int cy=Integer.valueOf(map.get("name").toString());
					if (minYear==0||cy<minYear) {
						minYear=cy;
					}
					if (maxYear==0||cy>maxYear) {
						maxYear=cy;
					}
					data.put(map.get("label").toString()+map.get("name").toString()+"年", map.get("cc").toString());
					protype.add(map.get("label").toString());
				}
			}
		}
		List<Map<String, Object>> glist=teamAnalysisDao.getTeamYearMemsFromGcontest();
		if (glist!=null&&glist.size()>0) {
			for(Map<String, Object> map:glist) {
				if (map.get("label")!=null&&map.get("name")!=null) {
					int cy=Integer.valueOf(map.get("name").toString());
					if (minYear==0||cy<minYear) {
						minYear=cy;
					}
					if (maxYear==0||cy>maxYear) {
						maxYear=cy;
					}
					data.put(map.get("label").toString()+map.get("name").toString()+"年", map.get("cc").toString());
					protype.add(map.get("label").toString());
				}
			}
		}
		List<Map<String, Object>> nlist=teamAnalysisDao.getTeamYearMemsFromModel();
		if (nlist!=null&&nlist.size()>0) {
			for(Map<String, Object> map:nlist) {
				if (map.get("label")!=null&&map.get("name")!=null) {
					int cy=Integer.valueOf(map.get("name").toString());
					if (minYear==0||cy<minYear) {
						minYear=cy;
					}
					if (maxYear==0||cy>maxYear) {
						maxYear=cy;
					}
					data.put(map.get("label").toString()+map.get("name").toString()+"年", map.get("cc").toString());
					protype.add(map.get("label").toString());
				}
			}
		}
		if (protype.size()>0) {
			JSONObject js=new JSONObject();
			JSONArray legend=new JSONArray();
			JSONArray sdata=new JSONArray();
			JSONArray years=getYearList(minYear, maxYear);
			for(String k:protype) {
				legend.add(k+"人数");
				JSONObject stem=new JSONObject();
				stem.put("name", k+"人数");
				stem.put("type", "line");
				JSONArray barSarr=new JSONArray();
				for(int i=0;i<years.size();i++) {
					String s=data.get(k+years.getString(i));
					if (s==null) {
						barSarr.add(0);
					}else{
						barSarr.add(Integer.valueOf(s));
					}
				}
				stem.put("data", barSarr);
				sdata.add(stem);
			}
			js.put("legend", legend);
			js.put("xData", years);
			js.put("sData", sdata);
			return js;
		}
		return null;
	}
	public JSONObject getTeamNum(String year) {
		if (StringUtil.isNotEmpty(year)) {
			year=year+"-01-01 00:00:00";
		}
		Set<String> offices=new HashSet<String>();
		Map<String,Integer> protype=new LinkedHashMap<String,Integer>();
		Map<String,String> data=new HashMap<String,String>();
		List<Map<String, Object>> plist=teamAnalysisDao.getTeamNumFromProject(year);
		if (plist!=null&&plist.size()>0) {
			for(Map<String, Object> map:plist) {
				if (map.get("label")!=null&&map.get("name")!=null) {
					Integer cc=Integer.valueOf(map.get("cc").toString());
					if (protype.get(map.get("label").toString())==null) {
						protype.put(map.get("label").toString(), cc);
					}else{
						protype.put(map.get("label").toString(), protype.get(map.get("label").toString())+cc);
					}
					data.put(map.get("label").toString()+map.get("name").toString(), map.get("cc").toString());
					offices.add(map.get("name").toString());
				}
			}
		}
		List<Map<String, Object>> glist=teamAnalysisDao.getTeamNumFromGcontest(year);
		if (glist!=null&&glist.size()>0) {
			for(Map<String, Object> map:glist) {
				if (map.get("label")!=null&&map.get("name")!=null) {
					Integer cc=Integer.valueOf(map.get("cc").toString());
					if (protype.get(map.get("label").toString())==null) {
						protype.put(map.get("label").toString(), cc);
					}else{
						protype.put(map.get("label").toString(), protype.get(map.get("label").toString())+cc);
					}
					data.put(map.get("label").toString()+map.get("name").toString(), map.get("cc").toString());
					offices.add(map.get("name").toString());
				}
			}
		}
		List<Map<String, Object>> nlist=teamAnalysisDao.getTeamNumFromModel(year);
		if (nlist!=null&&nlist.size()>0) {
			for(Map<String, Object> map:nlist) {
				if (map.get("label")!=null&&map.get("name")!=null) {
					Integer cc=Integer.valueOf(map.get("cc").toString());
					if (protype.get(map.get("label").toString())==null) {
						protype.put(map.get("label").toString(), cc);
					}else{
						protype.put(map.get("label").toString(), protype.get(map.get("label").toString())+cc);
					}
					data.put(map.get("label").toString()+map.get("name").toString(), map.get("cc").toString());
					offices.add(map.get("name").toString());
				}
			}
		}
		if (protype.size()>0) {
			JSONObject js=new JSONObject();
			JSONArray pieLegend=new JSONArray();
			JSONArray pieData=new JSONArray();
			JSONArray barXdata=getOfficeList(offices);
			JSONArray barSdata=new JSONArray();
			for(String k:protype.keySet()) {
				pieLegend.add(k+"团队数("+protype.get(k)+")");
				JSONObject tem=new JSONObject();
				tem.put("name", k+"团队数("+protype.get(k)+")");
				tem.put("value", protype.get(k));
				pieData.add(tem);
				JSONObject stem=new JSONObject();
				stem.put("name", k);
				stem.put("type", "bar");
				stem.put("barWidth", 15);
				stem.put("stack", "团队人数占比");
				JSONArray barSarr=new JSONArray();
				for(int i=0;i<barXdata.size();i++) {
					String s=data.get(k+barXdata.getString(i));
					if (s==null) {
						barSarr.add(0);
					}else{
						barSarr.add(Integer.valueOf(s));
					}
				}
				stem.put("data", barSarr);
				barSdata.add(stem);
			}
			js.put("pieLegend", pieLegend);
			js.put("pieData", pieData);
			js.put("barXdata", barXdata);
			js.put("barSdata", barSdata);
			return js;
		}
		return null;
	}
	public JSONObject getTeamMemsNum(String year) {
		if (StringUtil.isNotEmpty(year)) {
			year=year+"-01-01 00:00:00";
		}
		Set<String> offices=new HashSet<String>();
		Map<String,Integer> protype=new LinkedHashMap<String,Integer>();
		Map<String,String> data=new HashMap<String,String>();
		List<Map<String, Object>> plist=teamAnalysisDao.getTeamMemsFromProject(year);
		if (plist!=null&&plist.size()>0) {
			for(Map<String, Object> map:plist) {
				if (map.get("label")!=null&&map.get("name")!=null) {
					Integer cc=Integer.valueOf(map.get("cc").toString());
					if (protype.get(map.get("label").toString())==null) {
						protype.put(map.get("label").toString(), cc);
					}else{
						protype.put(map.get("label").toString(), protype.get(map.get("label").toString())+cc);
					}
					data.put(map.get("label").toString()+map.get("name").toString(), map.get("cc").toString());
					offices.add(map.get("name").toString());
				}
			}
		}
		List<Map<String, Object>> glist=teamAnalysisDao.getTeamMemsFromGcontest(year);
		if (glist!=null&&glist.size()>0) {
			for(Map<String, Object> map:glist) {
				if (map.get("label")!=null&&map.get("name")!=null) {
					Integer cc=Integer.valueOf(map.get("cc").toString());
					if (protype.get(map.get("label").toString())==null) {
						protype.put(map.get("label").toString(), cc);
					}else{
						protype.put(map.get("label").toString(), protype.get(map.get("label").toString())+cc);
					}
					data.put(map.get("label").toString()+map.get("name").toString(), map.get("cc").toString());
					offices.add(map.get("name").toString());
				}
			}
		}
		List<Map<String, Object>> nlist=teamAnalysisDao.getTeamMemsFromModel(year);
		if (nlist!=null&&nlist.size()>0) {
			for(Map<String, Object> map:nlist) {
				if (map.get("label")!=null&&map.get("name")!=null) {
					Integer cc=Integer.valueOf(map.get("cc").toString());
					if (protype.get(map.get("label").toString())==null) {
						protype.put(map.get("label").toString(), cc);
					}else{
						protype.put(map.get("label").toString(), protype.get(map.get("label").toString())+cc);
					}
					data.put(map.get("label").toString()+map.get("name").toString(), map.get("cc").toString());
					offices.add(map.get("name").toString());
				}
			}
		}
		if (protype.size()>0) {
			JSONObject js=new JSONObject();
			JSONArray pieLegend=new JSONArray();
			JSONArray pieData=new JSONArray();
			JSONArray barXdata=getOfficeList(offices);
			JSONArray barSdata=new JSONArray();
			for(String k:protype.keySet()) {
				pieLegend.add(k+"人数("+protype.get(k)+")");
				JSONObject tem=new JSONObject();
				tem.put("name", k+"人数("+protype.get(k)+")");
				tem.put("value", protype.get(k));
				pieData.add(tem);
				JSONObject stem=new JSONObject();
				stem.put("name", k);
				stem.put("type", "bar");
				stem.put("barWidth", 15);
				stem.put("stack", "人数占比");
				JSONArray barSarr=new JSONArray();
				for(int i=0;i<barXdata.size();i++) {
					String s=data.get(k+barXdata.getString(i));
					if (s==null) {
						barSarr.add(0);
					}else{
						barSarr.add(Integer.valueOf(s));
					}
				}
				stem.put("data", barSarr);
				barSdata.add(stem);
			}
			js.put("pieLegend", pieLegend);
			js.put("pieData", pieData);
			js.put("barXdata", barXdata);
			js.put("barSdata", barSdata);
			return js;
		}
		return null;
	}
	private JSONArray getOfficeList(Set<String> offices) {
		if (offices==null||offices.size()==0) {
			return null;
		}else{
			JSONArray ja=new JSONArray();
			for(String s:offices) {
				ja.add(s);
			}
			return ja;
		}
	}
	public List<AnnerVo> findTeamTeacherByYear(String param) {
		List<AnnerVo> listnew=new ArrayList<AnnerVo>();
		List<String> yearList= getYears();
		if (yearList.size()==0) {
			return listnew;
		}
		//年份
		List<String> categeries=new ArrayList<>();
		for(int i=0;i<yearList.size();i++) {
			categeries.add(yearList.get(i));
		}
		List<Map<String,Object>> findlist=teamAnalysisDao.findTeamTeacherByYear(param);
		//得到类别
		List<String> typeList=getTypes();
		for(int i=0;i<typeList.size();i++) {
			AnnerVo bar=new AnnerVo(typeList.get(i));
			bar.setCategories(categeries);
			bar.setType("line");
			for (int j = 0; j < categeries.size(); j++) {
				int yearNum = 0;
				for(Map<String,Object> echartVo:findlist) {
					String year=echartVo.get("year").toString();
					String label=echartVo.get("label").toString();
					int sum=Integer.valueOf(echartVo.get("num").toString());
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
		List<String> years=teamAnalysisDao.getYears();
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
	public List<String> getTypes() {
		return teamAnalysisDao.getTypes();
	}

	public List<String> getColleges() {
			return teamAnalysisDao.getColleges();
		}

	public JSONObject findTeamTeacherByCollege(String year) {
		JSONObject listjs = new JSONObject();
		List<AxisVo> listnew=new ArrayList<AxisVo>();

		Map<String,String> map=new HashMap<>();
		map.put("year",year);
		List<Map<String,Object>> findlist=teamAnalysisDao.findTeamTeacherByCollege(map);

		List<String> colleges= getColleges();
		List<String> categeries=new ArrayList<>();
		for(int i=0;i<colleges.size();i++) {
			categeries.add(colleges.get(i));
		}

		//得到类别
		List<String> typeList=getTypes();
		for(int i=0;i<typeList.size();i++) {
			AxisVo bar = new AxisVo(typeList.get(i)+"导师人数（总人数）");
			bar.setBarWidth(8);
			bar.setStack("导师人数");
			bar.setType("bar");
			for (int j = 0; j < categeries.size(); j++) {
				int yearNum = 0;
				for(Map<String,Object> echartVo:findlist) {
					String name=echartVo.get("name").toString();
					String label=echartVo.get("label").toString();
					int num=Integer.valueOf(echartVo.get("num").toString());
					//符合类别的
					if (typeList.get(i).equals(label)) {
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
		listjs.put("seriesData",listnew);
		listjs.put("xAxisData",categeries);
		return listjs;
	}

	public List<AnnerVo> findTeamNumByYear(String param) {
			List<AnnerVo> listnew=new ArrayList<AnnerVo>();
			List<String> yearList= getYears();
			if (yearList.size()==0) {
				return listnew;
			}
			//年份
			List<String> categeries=new ArrayList<>();
			for(int i=0;i<yearList.size();i++) {
				categeries.add(yearList.get(i));
			}
			List<Map<String,Object>> findlist=teamAnalysisDao.findTeamNumByYear(param);
			//得到类别
			List<String> typeList=getTypes();
			for(int i=0;i<typeList.size();i++) {
				AnnerVo bar=new AnnerVo(typeList.get(i));
				bar.setCategories(categeries);
				bar.setType("line");
				for (int j = 0; j < categeries.size(); j++) {
					int yearNum = 0;
					for(Map<String,Object> echartVo:findlist) {
						String year=echartVo.get("year").toString();
						String label=echartVo.get("label").toString();
						int sum=Integer.valueOf(echartVo.get("num").toString());
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
}
