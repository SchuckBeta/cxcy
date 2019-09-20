package com.oseasy.pro.modules.analysis.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangzheng on 2017/5/10.
 * 数据库无法查到的假数据
 */
public class FakeBase {

    public static List<EchartVo> getContestListAll() {
        List<EchartVo> list=new ArrayList<>();
        EchartVo vo1=new EchartVo("创青春",10);
        EchartVo vo2=new EchartVo("挑战杯",10);
        EchartVo vo3=new EchartVo("蓝桥杯",10);
        EchartVo vo4=new EchartVo("i创杯",10);
        EchartVo vo5=new EchartVo("其他",10);
        list.add(vo1);
        list.add(vo2);
        list.add(vo3);
        list.add(vo4);
        list.add(vo5);
        return list;
    }

    public static List<EchartVo> getContestList2014() {
        List<EchartVo> list=new ArrayList<>();
        EchartVo vo1=new EchartVo("创青春",1);
        EchartVo vo2=new EchartVo("挑战杯",1);
        EchartVo vo3=new EchartVo("蓝桥杯",1);
        EchartVo vo4=new EchartVo("i创杯",1);
        EchartVo vo5=new EchartVo("其他",1);
        list.add(vo1);
        list.add(vo2);
        list.add(vo3);
        list.add(vo4);
        list.add(vo5);
        return list;
    }

    public static List<EchartVo> getContestList2015() {
        List<EchartVo> list=new ArrayList<>();
        EchartVo vo1=new EchartVo("创青春",2);
        EchartVo vo2=new EchartVo("挑战杯",2);
        EchartVo vo3=new EchartVo("蓝桥杯",2);
        EchartVo vo4=new EchartVo("i创杯",2);
        EchartVo vo5=new EchartVo("其他",2);
        list.add(vo1);
        list.add(vo2);
        list.add(vo3);
        list.add(vo4);
        list.add(vo5);
        return list;
    }

    public static List<EchartVo> getContestList2016() {
        List<EchartVo> list=new ArrayList<>();
        EchartVo vo1=new EchartVo("创青春",3);
        EchartVo vo2=new EchartVo("挑战杯",3);
        EchartVo vo3=new EchartVo("蓝桥杯",3);
        EchartVo vo4=new EchartVo("i创杯",3);
        EchartVo vo5=new EchartVo("其他",3);
        list.add(vo1);
        list.add(vo2);
        list.add(vo3);
        list.add(vo4);
        list.add(vo5);
        return list;
    }

    public static List<EchartVo> getContestList2017() {
        List<EchartVo> list=new ArrayList<>();
        EchartVo vo1=new EchartVo("创青春",4);
        EchartVo vo2=new EchartVo("挑战杯",4);
        EchartVo vo3=new EchartVo("蓝桥杯",4);
        EchartVo vo4=new EchartVo("i创杯",4);
        EchartVo vo5=new EchartVo("其他",4);
        list.add(vo1);
        list.add(vo2);
        list.add(vo3);
        list.add(vo4);
        list.add(vo5);
        return list;
    }



    public static List<EchartVo> getProjectListAll() {
        List<EchartVo> list=new ArrayList<>();
        EchartVo vo1=new EchartVo("创业项目",10);
        EchartVo vo2=new EchartVo("创新项目",10);
        list.add(vo1);
        list.add(vo2);
        return list;
    }
    public static List<EchartVo> getProjectList2014() {
        List<EchartVo> list=new ArrayList<>();
        EchartVo vo1=new EchartVo("创业项目",1);
        EchartVo vo2=new EchartVo("创新项目",1);
        list.add(vo1);
        list.add(vo2);
        return list;
    }
    public static List<EchartVo> getProjectList2015() {
        List<EchartVo> list=new ArrayList<>();
        EchartVo vo1=new EchartVo("创业项目",2);
        EchartVo vo2=new EchartVo("创新项目",2);
        list.add(vo1);
        list.add(vo2);
        return list;
    }
    public static List<EchartVo> getProjectList2016() {
        List<EchartVo> list=new ArrayList<>();
        EchartVo vo1=new EchartVo("创业项目",3);
        EchartVo vo2=new EchartVo("创新项目",3);
        list.add(vo1);
        list.add(vo2);
        return list;
    }
    public static List<EchartVo> getProjectList2017() {
        List<EchartVo> list=new ArrayList<>();
        EchartVo vo1=new EchartVo("创业项目",4);
        EchartVo vo2=new EchartVo("创新项目",4);
        list.add(vo1);
        list.add(vo2);
        return list;
    }

    //虚构2014年创业项目 学生数
    public static Map<String,Integer> getCYStudent2014() {
        Map<String,Integer> map=new HashMap<>();
        map.put("在校学生",2);
        map.put("毕业学生",1);
        return map;
    }
    //虚构2015年创业项目 学生数
    public static Map<String,Integer> getCYStudent2015() {
        Map<String,Integer> map=new HashMap<>();
        map.put("在校学生",3);
        map.put("毕业学生",2);
        return map;
    }

    //虚构2016年创业项目 学生数
    public static Map<String,Integer> getCYStudent2016() {
        Map<String,Integer> map=new HashMap<>();
        map.put("在校学生",4);
        map.put("毕业学生",3);
        return map;
    }

    //虚构2017年创业项目 学生数
    public static  Map<String,Integer> getCYStudent2017() {
        Map<String,Integer> map=new HashMap<>();
        map.put("在校学生",5);
        map.put("毕业学生",4);
        return map;
    }

    //虚构全部创业项目 学生数
    public static Map<String,Integer> getCYStudentAll() {
        Map<String,Integer> map=new HashMap<>();
        map.put("在校学生",11);
        map.put("毕业学生",10);
        return map;
    }


    //虚构2014年创新项目 学生数
    public static Map<String,Integer> getCXStudent2014() {
        Map<String,Integer> map=new HashMap<>();
        map.put("在校学生",4);
        map.put("毕业学生",2);
        return map;
    }
    //虚构2015年创新项目 学生数
    public static Map<String,Integer> getCXStudent2015() {
        Map<String,Integer> map=new HashMap<>();
        map.put("在校学生",6);
        map.put("毕业学生",4);
        return map;
    }

    //虚构2016年创新项目 学生数
    public static Map<String,Integer> getCXStudent2016() {
        Map<String,Integer> map=new HashMap<>();
        map.put("在校学生",8);
        map.put("毕业学生",6);
        return map;
    }

    //虚构2017年创新项目 学生数
    public static Map<String,Integer> getCXStudent2017() {
        Map<String,Integer> map=new HashMap<>();
        map.put("在校学生",10);
        map.put("毕业学生",8);
        return map;
    }

    //虚构全部创新项目 学生数
    public static Map<String,Integer> getCXStudentAll() {
        Map<String,Integer> map=new HashMap<>();
        map.put("在校学生",22);
        map.put("毕业学生",20);
        return map;
    }


}
