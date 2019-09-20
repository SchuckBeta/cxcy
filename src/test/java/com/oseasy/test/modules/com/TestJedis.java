package com.oseasy.test.modules.com;


import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.OfficeDao;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.test.common.BaseTest;

import com.oseasy.util.common.utils.StringUtil;

import java.util.HashMap;
import java.util.List;

import org.activiti.engine.TaskService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.Map;

import static com.oseasy.com.pcore.modules.sys.utils.CoreUtils.CACHE_OFFICE_LIST;

/**
 * Created by Administrator on 2019/4/17 0017.
 */
public class TestJedis extends BaseTest {
    @Autowired
    OfficeDao officeDao;
    String tenant= "tenant";
    @Test
    public void testJedisSave(){
        Office office =new Office();
        office.setId("3");
//        officeDao.insert(office);
        JedisUtils.setObject(CoreSval.ck.cks(CoreSval.CoreEmskey.OFFICE, tenant)+office.getId(),office);
        JedisUtils.delObject(CACHE_OFFICE_LIST+ StringUtil.LINE_D+tenant);
    }
    @Test
    public void testJedisList(){
        List<Office> list=new ArrayList<Office>();
        Office office =new Office();
        office.setId("1");
        Office office1 =new Office();
        office.setId("2");
        list.add(office);
        list.add(office1);
        JedisUtils.setObject(CACHE_OFFICE_LIST+ StringUtil.LINE_D+tenant,list);
    }
//    @Test
//    public void testJedisGetList(){
//
//        TenantConfig config = (TenantConfig)CacheUtils.get(TenantConfig.genConfigKey());
//        if(config != null){
//            System.out.println(config.toString());
//        }else{
//            System.out.println("config == null");
//        }
//
//    }
    @Autowired
    public static TaskService taskService ;

    @Test
    public void testAct(){
        List<Office> list=new ArrayList<Office>();
        Office office =new Office();
        office.setId("1");
        office.setName("1111111");
        Office office1 =new Office();
        office1.setId("2");
        office1.setName("2222222");
        list.add(office);
        list.add(office1);
        JedisUtils.hset("office:10","officeList",list);
        JedisUtils.hset("office:10","officeId1",office);
        Map<String,Object> map=JedisUtils.hashGetKey("office:10");
        System.out.println("officeList===========>"+ map.get("officeList"));
        Office redisOffice=(Office)map.get("officeId1");
        System.out.println("officeId1===========>"+ redisOffice.getName());


        Map<String,Object> mapEnd=JedisUtils.hashGetKey("office");
//        JedisUtils.hashDelByKey("office");
//        Map<String,Object> mapEnd=JedisUtils.hashGetKey("office");
//        if(mapEnd!=null && mapEnd.get("officeList")!=null){
//            System.out.println("officeList===========>"+ mapEnd.get("officeList"));
//        }else{
//            System.out.println("hashDelByKey===========>success");
//        }

    }
}
