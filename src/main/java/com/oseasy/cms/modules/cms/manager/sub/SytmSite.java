package com.oseasy.cms.modules.cms.manager.sub;

import com.oseasy.cms.modules.cms.dao.*;
import com.oseasy.cms.modules.cms.entity.*;
import com.oseasy.com.fileserver.common.vsftp.config.Global;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.manager.sub.SupSytm;
import com.oseasy.com.pcore.modules.syt.manager.sub.SytmvTenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmSite extends SupSytm<SytmvSite> {
    private final static Logger logger = LoggerFactory.getLogger(SytmvSite.class);
    private static SiteDao sietdao = SpringContextHolder.getBean(SiteDao.class);
    private static CmsSiteconfigDao cmsSiteconfigDao = SpringContextHolder.getBean(CmsSiteconfigDao.class);
    private static CmsLinkDao cmsLinkDao = SpringContextHolder.getBean(CmsLinkDao.class);
    private static CmsIndexDao cmsIndexDao = SpringContextHolder.getBean(CmsIndexDao.class);
    private static CategoryDao categoryDao = SpringContextHolder.getBean(CategoryDao.class);

    public SytmSite(SytmvSite sytmvo) {
        super(sytmvo);
    }

    @Override
    public String name() {
        return "站点";
    }

    @Override
    public boolean before() {
        if(!super.before()){
            this.status.setMsg(name() + "不能为Null！");
        }

        if((this.sytmvo.getSytmvTenant() == null)){
            this.status.setMsg(name() + this.sytmvo.getSytmvTenant().getName() + "不能为空！");
        }

        this.sytmvo.setName(name());
        return true;
    }

    @Override
    public boolean run() {
        if(!before()){
            return false;
        }

        System.out.println(name() + "处理中...");
        SytmvTenant sytmvTenant = this.sytmvo.getSytmvTenant();
        SysTenant sysTenant = sytmvTenant.getSysTenant();
        //新增站点数据
        Site site = new Site(sytmvTenant.getTenantId(),sysTenant.getSchoolName(),sysTenant.getDomainName(), CoreSval.Const.YES, CoreSval.Const.YES);
        sietdao.insert(site);
        //新增站点配置数据
        CmsSiteconfig siteConfig_logoLeft = new CmsSiteconfig(sytmvTenant.getTenantId(),site.getId(),CoreSval.Const.YES,"logoLeft","",new Date(),new Date(),"");
        CmsSiteconfig siteConfig_logoRight = new CmsSiteconfig(sytmvTenant.getTenantId(),site.getId(),CoreSval.Const.YES,"logoRight","",new Date(),new Date(),"");
        CmsSiteconfig siteConfig_bannerimage = new CmsSiteconfig(sytmvTenant.getTenantId(),site.getId(),CoreSval.Const.YES,"bannerimage",
                Global.REMOTEPATH  +"/ueditor/2018-10-12/81adf6ab78ee41548a316b90af24e8c0.jpg",new Date(),new Date(),"");
        CmsSiteconfig siteConfig_dynamicImg = new CmsSiteconfig(sytmvTenant.getTenantId(),site.getId(),CoreSval.Const.YES,"dynamicImg",
                Global.REMOTEPATH  +"/ueditor/2018-10-26/a2ff6e5c7f8349798a67e9fb74f8d745.png",new Date(),new Date(),"");
        CmsSiteconfig siteConfig_msgImg = new CmsSiteconfig(sytmvTenant.getTenantId(),site.getId(),CoreSval.Const.YES,"msgImg",
                Global.REMOTEPATH  + "/ueditor/2018-10-26/23df5c35e6eb46bf97646e3f3b25562a.png",new Date(),new Date(),"");
        CmsSiteconfig siteConfig_linkType = new CmsSiteconfig(sytmvTenant.getTenantId(),site.getId(),CoreSval.Const.YES,"linkType",
                "",new Date(),new Date(),"2");
        cmsSiteconfigDao.insert(siteConfig_logoLeft);
        cmsSiteconfigDao.insert(siteConfig_logoRight);
        cmsSiteconfigDao.insert(siteConfig_bannerimage);
        cmsSiteconfigDao.insert(siteConfig_dynamicImg);
        cmsSiteconfigDao.insert(siteConfig_msgImg);
        cmsSiteconfigDao.insert(siteConfig_linkType);
        //新增友情链接数据
        CmsLink cmsLink1 = new CmsLink(sytmvTenant.getTenantId(),"中国创新创业教育联盟","www.ieeac2015.org.cn/p/index.html","2",
                Global.REMOTEPATH  +"/ueditor/2018-10-26/caca79ecfe074e288b41e97ae76b3ebc.png","1","1",new Date(),new Date());
        CmsLink cmsLink2 = new CmsLink(sytmvTenant.getTenantId(),"全国大学生创业服务网","cy.ncss.org.cn/","2",
                Global.REMOTEPATH  +"/ueditor/2018-10-26/c5ea83862c2a4a918bbf5f221df6e352.png","2","1",new Date(),new Date());
        CmsLink cmsLink3 = new CmsLink(sytmvTenant.getTenantId(),"教育部","www.moe.gov.cn/","2",
                Global.REMOTEPATH  +"/ueditor/2018-10-26/fab5dd5c3f8d46dfae35a609d83a4e68.png","3","1",new Date(),new Date());
        cmsLinkDao.insert(cmsLink1);
        cmsLinkDao.insert(cmsLink2);
        cmsLinkDao.insert(cmsLink3);
        //新增首页管理数据
        CmsIndex cmsIndex1 = new CmsIndex(sytmvTenant.getTenantId(),"banner","homeBanner","","1","1");
        CmsIndex cmsIndex2 = new CmsIndex(sytmvTenant.getTenantId(),"通知公告","homeAnnouncement","","2","1");
        CmsIndex cmsIndex3 = new CmsIndex(sytmvTenant.getTenantId(),"优秀项目展示","homeProject","OUTSTANDING PROJECT PRESENTATION","3","1");
        CmsIndex cmsIndex4 = new CmsIndex(sytmvTenant.getTenantId(),"名师讲堂","homeCourse","TOP TEACHERS","7","1");
        CmsIndex cmsIndex5 = new CmsIndex(sytmvTenant.getTenantId(),"导师风采","homeTeacher","OUTSTANDING TEACHERS","5","1");
        CmsIndex cmsIndex6 = new CmsIndex(sytmvTenant.getTenantId(),"大赛热点","homeGcontest","HOT TOPICS","6","1");
        CmsIndex cmsIndex7 = new CmsIndex(sytmvTenant.getTenantId(),"通知/动态","homeNotice","NEWS","4","1");
        CmsIndex cmsIndex8 = new CmsIndex(sytmvTenant.getTenantId(),"双创动态","homeSCDT","",null,"1");
        CmsIndex cmsIndex9 = new CmsIndex(sytmvTenant.getTenantId(),"双创通知","homeSCTZ","",null,"1");
        CmsIndex cmsIndex10 = new CmsIndex(sytmvTenant.getTenantId(),"省市动态","homeSSDT","",null,"1");
        cmsIndexDao.insert(cmsIndex1);
        cmsIndexDao.insert(cmsIndex2);
        cmsIndexDao.insert(cmsIndex3);
        cmsIndexDao.insert(cmsIndex4);
        cmsIndexDao.insert(cmsIndex5);
        cmsIndexDao.insert(cmsIndex6);
        cmsIndexDao.insert(cmsIndex7);
        cmsIndexDao.insert(cmsIndex8);
        cmsIndexDao.insert(cmsIndex9);
        cmsIndexDao.insert(cmsIndex10);
        //新增栏目数据

        Category category = new Category();
        SytmvTenant.setTenant(sytmvTenant, category);
        List<Category> categoryList = categoryDao.findListByTenant(category);
        categoryDao.delByTenantId(sytmvTenant.getTenantId());
        for(Category category1 : categoryList){
            Category category2 = new Category();
            BeanUtils.copyProperties(category1, category2);
            category2.setTenantId(sytmvTenant.getTenantId());
            category2.setParentIds("0,");
            category2.setSite(site);
            categoryDao.insert(category2);
        }
        //更新栏目父级id
        Category category1 = new Category();
        category1.setTenantId(sytmvTenant.getTenantId());
        List<Category> categoryList1 = categoryDao.findListByTenant(category1);
        updateParent(categoryList,categoryList1,site);

        /*Course course = new Course();
        SytmvTenant.setTenant(sytmvTenant, course);
        List<Course> courseList = courseDao.findList(course);
        for(Course source : courseList){
            Course target = new Course();
            BeanUtils.copyProperties(source, target);
            target.setTenantId(sytmvTenant.getTenantId());
            courseDao.insert(target);
        }*/
        after();
        return true;
    }

    @Override
    public boolean after() {
        return true;
    }

    public void updateParent(List<Category> sourceList,List<Category> targetList,Site site){
        for(Category source : sourceList){
            for(Category target : targetList){
                if(source.getName().equals(target.getName())){
                    if(!source.getParentId().equals(CoreSval.Const.YES)){
                        Category temp = categoryDao.findCategoryByTenantId(source);
                        Category dbtemp = new Category();
                        dbtemp.setName(temp.getName());
                        dbtemp.setTenantId(target.getTenantId());
                        dbtemp.setSite(site);
                        Category get = categoryDao.findCategoryByTenantAndName(dbtemp);
                        target.setParentId(get.getId());
                        categoryDao.update(target);
                    }
                }
            }
        }
    }


}
