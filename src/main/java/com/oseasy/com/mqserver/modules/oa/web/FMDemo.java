package com.oseasy.com.mqserver.modules.oa.web;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/21.
 */
public class FMDemo {

    //Freemarker
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        //模板+数据模型 = 输出
        //ftl: freemarker template
        //第一步: 读取html模板
        String dir = "D:\\IDEA_WORKSPACE_SC\\v2.0.0.100\\ftl";
        conf.setDirectoryForTemplateLoading(new File(dir));
        Template template = conf.getTemplate("freemarker.html");
        //第二步: 加载数据模型
        Map root = new HashMap();
        root.put("world", "世界你好");
        //List集合
        List<String> persons = new ArrayList<String>();
        persons.add("范冰冰");
        persons.add("李冰冰");
        persons.add("何炅");
        root.put("persons", persons);
        //Map集合
        Map map = new HashMap();
        map.put("fbb", "范冰冰");
        map.put("lbb", "李冰冰");
        root.put("map", map);
        //list和map混合
        List<Map> maps = new ArrayList<Map>();
        Map pms1 = new HashMap();
        pms1.put("id1", "范冰冰");
        pms1.put("id2", "李冰冰");
        Map pms2 = new HashMap();
        pms2.put("id1", "曾志伟");
        pms2.put("id2", "何炅");
        maps.add(pms1);
        maps.add(pms2);
        root.put("maps", maps);
        Writer out = new FileWriter(new File(dir + "hello.html"));
        template.process(root, out);
        System.out.println("生成完成");
    }

}
