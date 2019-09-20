package com.oseasy.pro.modules.analysis.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.analysis.service.GlobalAnalysisService;
import com.oseasy.pro.modules.analysis.vo.BarVo;
import com.oseasy.pro.modules.analysis.vo.EchartVo;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "${adminPath}/analysis/globalAnalysis")
public class GlobalAnalysisController extends BaseController {
	@Autowired
	private GlobalAnalysisService globalAnalysisService;
	@RequestMapping(value = "toPage")
	public String toPage(HttpServletRequest request, HttpServletResponse response,Model model) {
		List<String> years =globalAnalysisService.getYears();
		model.addAttribute("years",years);
		return ProSval.path.vms(ProEmskey.ANALYSIS.k()) + "globalAnalysis";
//		return ProSval.path.vms(ProEmskey.ANALYSIS.k()) + "globalAnalysis2";
	}
	@RequestMapping(value = "getData")
	@ResponseBody
	public JSONObject getData(HttpServletRequest request, HttpServletResponse response) {
		return globalAnalysisService.getData();
	}

	/**
	 * 获取 双创大赛参赛分布 数据
	 * @param year
	 * @return
     */
	@RequestMapping(value = "getContestTypeData")
	@ResponseBody
	public List<EchartVo> getContestTypeData(String year) {
		List<EchartVo> list=new ArrayList<>();
		//设置 "创青春","挑战杯","蓝桥杯","i创杯","其他" 的数据
		/*int number;
		if (StringUtil.equals("2014",year)) {
			list= FakeBase.getContestList2014();
			number=5;
		}else if (StringUtil.equals("2015",year)) {
			list= FakeBase.getContestList2015();
			number=6;
		}else if (StringUtil.equals("2016",year)) {
			list= FakeBase.getContestList2016();
			number=8;
		}else if (StringUtil.equals("2017",year)) {
			list= FakeBase.getContestList2017();
			number=10;
		}else{
			list= FakeBase.getContestListAll();
			number=29;
		}*/
		list=globalAnalysisService.findAllGcontestType(year);
        //查找互联网+大赛数据
//		int number=globalAnalysisService.getContestNumber(year);
		//EchartVo vo=new EchartVo("互联网+大赛",number);
		//list.add(vo);

		return list;
	}

	/**
	 * 获得 双创项目类别
	 * @param year
	 * @return
     */
	@RequestMapping(value = "getProjectTypeData")
	@ResponseBody
	public List<EchartVo> getProjectTypeData(String year) {
		List<EchartVo> list=new ArrayList<>();
		//设置 "创业项目","创新项目" 的数据
		/*if (StringUtil.equals("2014",year)) {
			list= FakeBase.getProjectList2014();
		}else if (StringUtil.equals("2015",year)) {
			list= FakeBase.getProjectList2015();
		}else if (StringUtil.equals("2016",year)) {
			list= FakeBase.getProjectList2016();
		}else if (StringUtil.equals("2017",year)) {
			list= FakeBase.getProjectList2017();
		}else{
			list= FakeBase.getProjectListAll();
		}*/
		list=globalAnalysisService.findAllProjectType(year);
		//int gcNumber=globalAnalysisService.getProjectNumber(year);
		return list;
	}

    /**
	 *  获得 双创学生比例 数据
	 * @param year
	 * @return
     */
	@RequestMapping(value = "getProjectStudentData")
	@ResponseBody
	public List<EchartVo> getProjectStudentData(String year) {
		List<EchartVo> list=new ArrayList<>();
		list=globalAnalysisService.findAllGcontestStuCurrState(year);

		/*int num1;  //在校学生数
		int num2; //毕业学生数
		int num3; //休学学生数
		if (StringUtil.equals("2014",year)) {
			num1=500;
			num2=20;
			num3=10;
		}else if (StringUtil.equals("2015",year)) {
			num1=600;
			num2=30;
			num3=20;
		}else if (StringUtil.equals("2016",year)) {
			num1=700;
			num2=60;
			num3=40;
		}else if (StringUtil.equals("2017",year)) {
			num1=800;
			num2=50;
			num3=50;
		}else{
			num1=2700;
			num2=160;
			num3=110;
		}

		EchartVo vo1=new EchartVo("在校学生",num1);
		EchartVo vo2=new EchartVo("毕业学生",num2);
		EchartVo vo3=new EchartVo("休学学生",num3);
		list.add(vo1);
		list.add(vo2);
		list.add(vo3);*/

		return list;
	}

	/**
	 * 获得 双创项目学生比例柱状图 数据
	 * @param year
	 * @return
     */
	@RequestMapping(value = "getStuDtn")
	@ResponseBody
	public List<BarVo> getStuDtn (String year) {
		List<BarVo> list=new ArrayList<>();
		list=globalAnalysisService.findAllProjectStuCurrState(year);
		/*List<String> categeries=new ArrayList<>();
		categeries.add("国创项目");
		categeries.add("创业项目");
		categeries.add("创新项目");
		BarVo bar1=new BarVo("在校学生");  //统计在校学生的数据  依次为 国创项目数、创业项目数、创新项目数
		BarVo bar2=new BarVo("毕业学生");
		BarVo bar3=new BarVo("休学学生");
		int[] gcNums=new int[3];  // 国创项目  在校学生、毕业学生、休学学生数
		int[] cyNums=new int[3];  // 创业项目  在校学生、毕业学生、休学学生数
		int[] cxNums=new int[3]; // 创新项目  在校学生、毕业学生、休学学生数
		if (StringUtil.equals("2014",year)) {
			gcNums[0]=300; gcNums[1]=10; gcNums[2]=8;
			cyNums[0]=100; cyNums[1]=5; cyNums[2]=2;
			cxNums[0]=100; cxNums[1]=5; cxNums[2]=2;
		}else if (StringUtil.equals("2015",year)) {
			gcNums[0]=400; gcNums[1]=15; gcNums[2]=10;
			cyNums[0]=250; cyNums[1]=10; cyNums[2]=5;
			cxNums[0]=150; cxNums[1]=5; cxNums[2]=5;
		}else if (StringUtil.equals("2016",year)) {
			gcNums[0]=350; gcNums[1]=30; gcNums[2]=20;
			cyNums[0]=250; cyNums[1]=20; cyNums[2]=10;
			cxNums[0]=100; cxNums[1]=10; cxNums[2]=10;
		}else if (StringUtil.equals("2017",year)) {
			gcNums[0]=400; gcNums[1]=30; gcNums[2]=20;
			cyNums[0]=200; cyNums[1]=10; cyNums[2]=20;
			cxNums[0]=200; cxNums[1]=10; cxNums[2]=10;
		}else{
			gcNums[0]=1500; gcNums[1]=100; gcNums[2]=50;
			cyNums[0]=500; cyNums[1]=20; cyNums[2]=50;
			cxNums[0]=200; cxNums[1]=40; cxNums[2]=10;
		}
		bar1.addValue(gcNums[0]);
		bar1.addValue(cyNums[0]);
		bar1.addValue(cxNums[0]);
		bar1.setCategories(categeries);
		bar2.addValue(gcNums[1]);
		bar2.addValue(cyNums[1]);
		bar2.addValue(cxNums[1]);
		bar2.setCategories(categeries);
		bar3.addValue(gcNums[2]);
		bar3.addValue(cyNums[2]);
		bar3.addValue(cxNums[2]);
		bar3.setCategories(categeries);
		list.add(bar1);
		list.add(bar2);
		list.add(bar3);*/

		return list;
	}

	/**
	 *  获得 双创导师分布比例 数据
	 * @param year
	 * @return
	 */
	@RequestMapping(value = "getProjectTerData")
	@ResponseBody
	public List<EchartVo> getProjectTerData(String year) {
		List<EchartVo> list=new ArrayList<>();
		/*int inNumber; //在校园导师数
		int outNumber; //企业导师数
		if (StringUtil.equals("2014",year)) {
			inNumber=10;
			outNumber=1;
		}else if (StringUtil.equals("2015",year)) {
			inNumber=20;
			outNumber=1;
		}else if (StringUtil.equals("2016",year)) {
			inNumber=30;
			outNumber=3;
		}else if (StringUtil.equals("2017",year)) {
			inNumber=40;
			outNumber=4;
		}else{
			inNumber=100;
			outNumber=10;
		}
		EchartVo vo1=new EchartVo("校园导师",inNumber);
		EchartVo vo2=new EchartVo("企业导师",outNumber);
		list.add(vo1);
		list.add(vo2);*/
		list=globalAnalysisService.findAllTeacherByType(year);
		return list;
	}



	/**
	 * 获得 人气导师前五名柱状图 数据
	 * @param year
	 * @return
	 */
	@RequestMapping(value = "getTeacherDtn")
	@ResponseBody
	public List<BarVo> getTeacherDtn (String year) {
		List<BarVo> list=new ArrayList<>();

		list=globalAnalysisService.findTeacherDtn(year);
	/*
		List<String> categeries;
		BarVo bar1=new BarVo("指导频率");
		BarVo bar2=new BarVo("学生点赞数");
		if (StringUtil.equals("2014",year)) {
			categeries=new ArrayList<>();
			categeries.add("曾帛员");
			categeries.add("韩 松");
			categeries.add("孙蝶妃");
			categeries.add("江浩华");
			categeries.add("田宇旺");
			bar1.addValue(100);
			bar1.addValue(80);
			bar1.addValue(60);
			bar1.addValue(50);
			bar1.addValue(40);
			bar1.setCategories(categeries);
			bar2.addValue(100);
			bar2.addValue(80);
			bar2.addValue(60);
			bar2.addValue(50);
			bar2.addValue(40);
			bar2.setCategories(categeries);

		}else if (StringUtil.equals("2015",year)) {
			categeries=new ArrayList<>();
			categeries.add("江浩华");
			categeries.add("田宇旺");
			categeries.add("冉迪振");
			categeries.add("许娇翔");
			categeries.add("庞 妍");
			bar1.addValue(120);
			bar1.addValue(80);
			bar1.addValue(60);
			bar1.addValue(50);
			bar1.addValue(40);
			bar1.setCategories(categeries);
			bar2.addValue(120);
			bar2.addValue(80);
			bar2.addValue(60);
			bar2.addValue(50);
			bar2.addValue(40);
			bar2.setCategories(categeries);

		}else if (StringUtil.equals("2016",year)) {
			categeries=new ArrayList<>();
			categeries.add("曾帛员");
			categeries.add("陈莲眉");
			categeries.add("孙蝶妃");
			categeries.add("江浩华");
			categeries.add("田宇旺");
			bar1.addValue(140);
			bar1.addValue(80);
			bar1.addValue(60);
			bar1.addValue(50);
			bar1.addValue(40);
			bar1.setCategories(categeries);
			bar2.addValue(140);
			bar2.addValue(80);
			bar2.addValue(60);
			bar2.addValue(50);
			bar2.addValue(40);
			bar2.setCategories(categeries);

		}else if (StringUtil.equals("2017",year)) {
			categeries=new ArrayList<>();
			categeries.add("曾帛员");
			categeries.add("韩 松");
			categeries.add("孙蝶妃");
			categeries.add("冉迪振");
			categeries.add("田宇旺");
			bar1.addValue(150);
			bar1.addValue(80);
			bar1.addValue(60);
			bar1.addValue(55);
			bar1.addValue(50);
			bar1.setCategories(categeries);
			bar2.addValue(150);
			bar2.addValue(80);
			bar2.addValue(60);
			bar2.addValue(56);
			bar2.addValue(50);
			bar2.setCategories(categeries);

		}else{
			categeries=new ArrayList<>();
			categeries.add("唐亚升");
			categeries.add("韩 松");
			categeries.add("陈寿渊");
			categeries.add("江浩华");
			categeries.add("田宇旺");
			bar1.addValue(200);
			bar1.addValue(160);
			bar1.addValue(90);
			bar1.addValue(80);
			bar1.addValue(50);
			bar1.setCategories(categeries);
			bar2.addValue(200);
			bar2.addValue(160);
			bar2.addValue(60);
			bar2.addValue(40);
			bar2.addValue(50);
			bar2.setCategories(categeries);
		}

		list.add(bar1);
		list.add(bar2);
*/
		return list;
	}


	/**
	 * 获得 技术领域柱状图 数据
	 * @param year
	 * @return
	 */
	@RequestMapping(value = "getDomainDtn")
	@ResponseBody
	public List<BarVo> getDomainDtn (String year) {
		List<BarVo> list=new ArrayList<>();

		list=globalAnalysisService.findHotTechnology(year);

		/*List<String> categeries;
		BarVo bar1=new BarVo("国创项目数");
		BarVo bar2=new BarVo("创新项目数");
		BarVo bar3=new BarVo("创业项目数");
		if (StringUtil.equals("2014",year)) {
			categeries=new ArrayList<>();
			categeries.add("电子信息");
			categeries.add("生物");
			categeries.add("光机电");
			categeries.add("新能源");
			categeries.add("新材料");
			categeries.add("环境保护");
			categeries.add("航空航天");
			categeries.add("地球");
			categeries.add("核应用技术");
			categeries.add("其它");
			bar1.addValue(400);
			bar1.addValue(350);
			bar1.addValue(300);
			bar1.addValue(280);
			bar1.addValue(250);
			bar1.addValue(200);
			bar1.addValue(180);
			bar1.addValue(120);
			bar1.addValue(100);
			bar1.addValue(40);
			bar1.setCategories(categeries);
			bar2.addValue(200);
			bar2.addValue(175);
			bar2.addValue(150);
			bar2.addValue(140);
			bar2.addValue(120);
			bar2.addValue(100);
			bar2.addValue(90);
			bar2.addValue(60);
			bar2.addValue(50);
			bar2.addValue(20);
			bar2.setCategories(categeries);
			bar3.addValue(200);
			bar3.addValue(175);
			bar3.addValue(150);
			bar3.addValue(140);
			bar3.addValue(120);
			bar3.addValue(100);
			bar3.addValue(90);
			bar3.addValue(60);
			bar3.addValue(50);
			bar3.addValue(20);
			bar3.setCategories(categeries);

		}else if (StringUtil.equals("2015",year)) {
			categeries=new ArrayList<>();
			categeries.add("电子与信息");
			categeries.add("生物");
			categeries.add("光机电");
			categeries.add("新能源");
			categeries.add("新材料");
			categeries.add("环境保护");
			categeries.add("航空航天");
			categeries.add("地球");
			categeries.add("核应用技术");
			categeries.add("其它");
			bar1.addValue(440);
			bar1.addValue(385);
			bar1.addValue(330);
			bar1.addValue(308);
			bar1.addValue(275);
			bar1.addValue(220);
			bar1.addValue(198);
			bar1.addValue(132);
			bar1.addValue(110);
			bar1.addValue(44);
			bar1.setCategories(categeries);
			bar2.addValue(220);
			bar2.addValue(195);
			bar2.addValue(170);
			bar2.addValue(154);
			bar2.addValue(132);
			bar2.addValue(110);
			bar2.addValue(99);
			bar2.addValue(66);
			bar2.addValue(55);
			bar2.addValue(22);
			bar2.setCategories(categeries);
			bar3.addValue(220);
			bar3.addValue(185);
			bar3.addValue(165);
			bar3.addValue(154);
			bar3.addValue(130);
			bar3.addValue(110);
			bar3.addValue(100);
			bar3.addValue(70);
			bar3.addValue(60);
			bar3.addValue(30);
			bar3.setCategories(categeries);
		}else if (StringUtil.equals("2016",year)) {
			categeries=new ArrayList<>();
			categeries.add("电子与信息");
			categeries.add("生物");
			categeries.add("光机电一体化");
			categeries.add("新能源");
			categeries.add("新材料");
			categeries.add("环境保护");
			categeries.add("航空航天");
			categeries.add("地球");
			categeries.add("核应用技术");
			categeries.add("其它");
			bar1.addValue(500);
			bar1.addValue(450);
			bar1.addValue(350);
			bar1.addValue(300);
			bar1.addValue(280);
			bar1.addValue(250);
			bar1.addValue(210);
			bar1.addValue(150);
			bar1.addValue(140);
			bar1.addValue(60);
			bar1.setCategories(categeries);
			bar2.addValue(250);
			bar2.addValue(200);
			bar2.addValue(180);
			bar2.addValue(160);
			bar2.addValue(140);
			bar2.addValue(120);
			bar2.addValue(110);
			bar2.addValue(100);
			bar2.addValue(70);
			bar2.addValue(30);
			bar2.setCategories(categeries);
			bar3.addValue(250);
			bar3.addValue(205);
			bar3.addValue(180);
			bar3.addValue(170);
			bar3.addValue(130);
			bar3.addValue(120);
			bar3.addValue(110);
			bar3.addValue(70);
			bar3.addValue(50);
			bar3.addValue(30);
			bar3.setCategories(categeries);
		}else if (StringUtil.equals("2017",year)) {
			categeries=new ArrayList<>();
			categeries.add("电子与信息");
			categeries.add("生物");
			categeries.add("光机电一体化");
			categeries.add("新能源");
			categeries.add("新材料");
			categeries.add("环境保护");
			categeries.add("航空航天");
			categeries.add("地球");
			categeries.add("核应用技术");
			categeries.add("其它");
			bar1.addValue(600);
			bar1.addValue(400);
			bar1.addValue(350);
			bar1.addValue(300);
			bar1.addValue(250);
			bar1.addValue(220);
			bar1.addValue(200);
			bar1.addValue(150);
			bar1.addValue(140);
			bar1.addValue(60);
			bar1.setCategories(categeries);
			bar2.addValue(500);
			bar2.addValue(350);
			bar2.addValue(250);
			bar2.addValue(180);
			bar2.addValue(150);
			bar2.addValue(120);
			bar2.addValue(100);
			bar2.addValue(70);
			bar2.addValue(60);
			bar2.addValue(30);
			bar2.setCategories(categeries);
			bar3.addValue(400);
			bar3.addValue(300);
			bar3.addValue(250);
			bar3.addValue(200);
			bar3.addValue(180);
			bar3.addValue(150);
			bar3.addValue(120);
			bar3.addValue(100);
			bar3.addValue(70);
			bar3.addValue(40);
			bar3.setCategories(categeries);
		}else{
			categeries=new ArrayList<>();
			categeries.add("电子与信息");
			categeries.add("生物");
			categeries.add("光机电一体化");
			categeries.add("新能源");
			categeries.add("新材料");
			categeries.add("环境保护");
			categeries.add("航空航天");
			categeries.add("地球");
			categeries.add("核应用技术");
			categeries.add("其它");
			bar1.addValue(1940);
			bar1.addValue(1600);
			bar1.addValue(1300);
			bar1.addValue(1000);
			bar1.addValue(800);
			bar1.addValue(700);
			bar1.addValue(600);
			bar1.addValue(500);
			bar1.addValue(400);
			bar1.addValue(300);
			bar1.setCategories(categeries);
			bar2.addValue(1170);
			bar2.addValue(1100);
			bar2.addValue(1000);
			bar2.addValue(900);
			bar2.addValue(800);
			bar2.addValue(700);
			bar2.addValue(600);
			bar2.addValue(400);
			bar2.addValue(300);
			bar2.addValue(200);
			bar2.setCategories(categeries);
			bar3.addValue(900);
			bar3.addValue(700);
			bar3.addValue(500);
			bar3.addValue(400);
			bar3.addValue(300);
			bar3.addValue(200);
			bar3.addValue(150);
			bar3.addValue(120);
			bar3.addValue(100);
			bar3.addValue(80);
			bar3.setCategories(categeries);
		}
		list.add(bar1);
		list.add(bar2);
		list.add(bar3);*/
		return list;
	}
}
