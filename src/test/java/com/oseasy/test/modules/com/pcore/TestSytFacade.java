package com.oseasy.test.modules.com.pcore;

import com.oseasy.test.common.BaseTest;

/**
 * Created by Administrator on 2019/4/20 0020.
 */
public class TestSytFacade  extends BaseTest {
//    @Autowired
//    private SysTenantDao sysTenantDao;
//
//    @Autowired
//    private OfficeDao officeDao;
//
//    @Autowired
//    private UserDao userDao;
//
//    private SytFacade facade;
//    @Before
//    public void init(){
//        facade = new SytFacade();
//    }
//
////    @Test
////    public void sytFacade(){
////        System.out.println("租户开始启动....");
////
////
////        SysTenant curSysTenant = sysTenantDao.get(CoreIds.NSC_SYS_TENANT_TPL.getId());
////        Office sysOffice = officeDao.get(CoreIds.NSC_SYS_OFFICE_TOP.getId());
////        Office cur = new Office();
////        System.out.println(sysOffice.getId());
////        System.out.println(sysOffice.toString());
////        BeanUtils.copyProperties(sysOffice, cur);
////        cur.setId(IdGen.uuid());
////        cur.setName("华中师范大学");
////        cur.setTenantId(curSysTenant.getTenantId());
////        officeDao.insert(cur);
////
//////        SytFacade facade = new SytFacade();
//////        facade.init();
////        System.out.println("租户执行完成....");
////    }
//
//
//    @Test
//    public void testSytmMenu(){
//        SytmvTenant sytmvTenant0 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 0);
//        sytmvTenant0.setTname(Sval.EmPn.NCENTER.getRemark());
//        sytmvTenant0.setType(Sval.EmPn.NCENTER.getKey());
//        SytmTenant sytmTenant0 = this.facade.initSytmTenant(sytmvTenant0);
//        SytmOffice sytmOffice0 = this.facade.initSytmOffice(sytmTenant0.sytmvo, CoreIds.NCE_SYS_OFFICE_TOP.getId());
//        SytmOfficeSub sytmOfficeSub0 = this.facade.initSytmOfficeSub(sytmOffice0.sytmvo);
//        SytmRole sytmRole0 = this.facade.initSytmRole(sytmOffice0.sytmvo);
//        SytmUser sytmUser0 = this.facade.initSytmUser(sytmOffice0.sytmvo);
//        SytmUserOsub sytmUserOsub0 = this.facade.initSytmUserOsub(sytmOfficeSub0.sytmvo);
//
//        //SytmMenu sytmMenu0 = this.facade.initSytmMenu(sytmTenant0.sytmvo);
//
//
////        SytmvTenant sytmvTenant1 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 1);
////        sytmvTenant1.setTname(Sval.EmPn.NPROVINCE.getRemark());
////        sytmvTenant1.setType(Sval.EmPn.NPROVINCE.getKey());
////        SytmTenant sytmTenant1 = this.facade.initSytmTenant(sytmvTenant1);
////        SytmOffice sytmOffice1 = this.facade.initSytmOffice(sytmTenant1.sytmvo, CoreIds.NPR_SYS_OFFICE_TOP.getId(), sytmOffice0.sytmvo.getOffice());
////        SytmOfficeSub sytmOfficeSub1 = this.facade.initSytmOfficeSub(sytmOffice1.sytmvo);
////        SytmRole sytmRole1 = this.facade.initSytmRole(sytmOffice1.sytmvo);
////        SytmUser sytmUser1 = this.facade.initSytmUser(sytmOffice1.sytmvo);
////        SytmUserOsub sytmUserOsub1 = this.facade.initSytmUserOsub(sytmOfficeSub1.sytmvo);
////
////
////        SytmvTenant sytmvTenant2 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 2);
////        sytmvTenant2.setTname(Sval.EmPn.NSCHOOL.getRemark());
////        sytmvTenant2.setType(Sval.EmPn.NSCHOOL.getKey());
////        SytmTenant sytmTenant2 = this.facade.initSytmTenant(sytmvTenant2);
////        SytmOffice sytmOffice2 = this.facade.initSytmOffice(sytmTenant2.sytmvo, CoreIds.NSC_SYS_OFFICE_TOP.getId(), sytmOffice0.sytmvo.getOffice());
////        SytmOfficeSub sytmOfficeSub2 = this.facade.initSytmOfficeSub(sytmOffice2.sytmvo);
////        SytmRole sytmRole2 = this.facade.initSytmRole(sytmOffice2.sytmvo);
////        SytmUser sytmUser2 = this.facade.initSytmUser(sytmOffice2.sytmvo);
////        SytmUserOsub sytmUserOsub2 = this.facade.initSytmUserOsub(sytmOfficeSub2.sytmvo, sytmRole2.sytmvo);
//    }
//
//
//    @Test
//    public void testSytmUser(){
//        SytmvTenant sytmvTenant0 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 0);
//        sytmvTenant0.setTname(Sval.EmPn.NCENTER.getRemark());
//        sytmvTenant0.setType(Sval.EmPn.NCENTER.getKey());
//        SytmTenant sytmTenant0 = this.facade.initSytmTenant(sytmvTenant0);
//        SytmOffice sytmOffice0 = this.facade.initSytmOffice(sytmTenant0.sytmvo, CoreIds.NCE_SYS_OFFICE_TOP.getId());
//        SytmOfficeSub sytmOfficeSub0 = this.facade.initSytmOfficeSub(sytmOffice0.sytmvo);
//        SytmRole sytmRole0 = this.facade.initSytmRole(sytmOffice0.sytmvo);
//        SytmUser sytmUser0 = this.facade.initSytmUser(sytmOffice0.sytmvo);
//        SytmUserOsub sytmUserOsub0 = this.facade.initSytmUserOsub(sytmOfficeSub0.sytmvo);
//
//
//        SytmvTenant sytmvTenant1 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 1);
//        sytmvTenant1.setTname(Sval.EmPn.NPROVINCE.getRemark());
//        sytmvTenant1.setType(Sval.EmPn.NPROVINCE.getKey());
//        SytmTenant sytmTenant1 = this.facade.initSytmTenant(sytmvTenant1);
//        SytmOffice sytmOffice1 = this.facade.initSytmOffice(sytmTenant1.sytmvo, CoreIds.NPR_SYS_OFFICE_TOP.getId(), sytmOffice0.sytmvo.getOffice());
//        SytmOfficeSub sytmOfficeSub1 = this.facade.initSytmOfficeSub(sytmOffice1.sytmvo);
//        SytmRole sytmRole1 = this.facade.initSytmRole(sytmOffice1.sytmvo);
//        SytmUser sytmUser1 = this.facade.initSytmUser(sytmOffice1.sytmvo);
//        SytmUserOsub sytmUserOsub1 = this.facade.initSytmUserOsub(sytmOfficeSub1.sytmvo);
//
//
//        SytmvTenant sytmvTenant2 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 2);
//        sytmvTenant2.setTname(Sval.EmPn.NSCHOOL.getRemark());
//        sytmvTenant2.setType(Sval.EmPn.NSCHOOL.getKey());
//        SytmTenant sytmTenant2 = this.facade.initSytmTenant(sytmvTenant2);
//        SytmOffice sytmOffice2 = this.facade.initSytmOffice(sytmTenant2.sytmvo, CoreIds.NSC_SYS_OFFICE_TOP.getId(), sytmOffice0.sytmvo.getOffice());
//        SytmOfficeSub sytmOfficeSub2 = this.facade.initSytmOfficeSub(sytmOffice2.sytmvo);
//        SytmRole sytmRole2 = this.facade.initSytmRole(sytmOffice2.sytmvo);
//        SytmUser sytmUser2 = this.facade.initSytmUser(sytmOffice2.sytmvo);
//        SytmUserOsub sytmUserOsub2 = this.facade.initSytmUserOsub(sytmOfficeSub2.sytmvo, sytmRole2.sytmvo);
//    }
//
//    @Test
//    public void testSytmRole(){
//        SytmvTenant sytmvTenant0 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 0);
//        sytmvTenant0.setTname(Sval.EmPn.NCENTER.getRemark());
//        sytmvTenant0.setType(Sval.EmPn.NCENTER.getKey());
//        SytmTenant sytmTenant0 = this.facade.initSytmTenant(sytmvTenant0);
//        SytmOffice sytmOffice0 = this.facade.initSytmOffice(sytmTenant0.sytmvo, CoreIds.NCE_SYS_OFFICE_TOP.getId());
//        SytmOfficeSub sytmOfficeSub0 = this.facade.initSytmOfficeSub(sytmOffice0.sytmvo);
//        this.facade.initSytmRole(sytmOffice0.sytmvo);
//
//        SytmvTenant sytmvTenant1 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 1);
//        sytmvTenant1.setTname(Sval.EmPn.NPROVINCE.getRemark());
//        sytmvTenant1.setType(Sval.EmPn.NPROVINCE.getKey());
//        SytmTenant sytmTenant1 = this.facade.initSytmTenant(sytmvTenant1);
//        SytmOffice sytmOffice1 = this.facade.initSytmOffice(sytmTenant1.sytmvo, CoreIds.NPR_SYS_OFFICE_TOP.getId(), sytmOffice0.sytmvo.getOffice());
//        SytmOfficeSub sytmOfficeSub1 = this.facade.initSytmOfficeSub(sytmOffice1.sytmvo);
//        this.facade.initSytmRole(sytmOffice1.sytmvo);
//
//        SytmvTenant sytmvTenant2 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 2);
//        sytmvTenant2.setTname(Sval.EmPn.NSCHOOL.getRemark());
//        sytmvTenant2.setType(Sval.EmPn.NSCHOOL.getKey());
//        SytmTenant sytmTenant2 = this.facade.initSytmTenant(sytmvTenant2);
//        SytmOffice sytmOffice2 = this.facade.initSytmOffice(sytmTenant2.sytmvo, CoreIds.NSC_SYS_OFFICE_TOP.getId(), sytmOffice0.sytmvo.getOffice());
//        SytmOfficeSub sytmOfficeSub2 = this.facade.initSytmOfficeSub(sytmOffice2.sytmvo);
//        this.facade.initSytmRole(sytmOffice2.sytmvo);
//    }
//
//    @Test
//    public void testSytmOfficeSub(){
//        SytmvTenant sytmvTenant0 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 0);
//        sytmvTenant0.setTname(Sval.EmPn.NCENTER.getRemark());
//        sytmvTenant0.setType(Sval.EmPn.NCENTER.getKey());
//        SytmTenant sytmTenant0 = this.facade.initSytmTenant(sytmvTenant0);
//        SytmOffice sytmOffice0 = this.facade.initSytmOffice(sytmTenant0.sytmvo, CoreIds.NCE_SYS_OFFICE_TOP.getId());
//        this.facade.initSytmOfficeSub(sytmOffice0.sytmvo);
//
//        SytmvTenant sytmvTenant1 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 1);
//        sytmvTenant1.setTname(Sval.EmPn.NPROVINCE.getRemark());
//        sytmvTenant1.setType(Sval.EmPn.NPROVINCE.getKey());
//        SytmTenant sytmTenant1 = this.facade.initSytmTenant(sytmvTenant1);
//        SytmOffice sytmOffice1 = this.facade.initSytmOffice(sytmTenant1.sytmvo, CoreIds.NPR_SYS_OFFICE_TOP.getId(), sytmOffice0.sytmvo.getOffice());
//        this.facade.initSytmOfficeSub(sytmOffice1.sytmvo);
//
//        SytmvTenant sytmvTenant2 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 2);
//        sytmvTenant2.setTname(Sval.EmPn.NSCHOOL.getRemark());
//        sytmvTenant2.setType(Sval.EmPn.NSCHOOL.getKey());
//        SytmTenant sytmTenant2 = this.facade.initSytmTenant(sytmvTenant2);
//        SytmOffice sytmOffice2 = this.facade.initSytmOffice(sytmTenant2.sytmvo, CoreIds.NSC_SYS_OFFICE_TOP.getId(), sytmOffice0.sytmvo.getOffice());
//        this.facade.initSytmOfficeSub(sytmOffice2.sytmvo);
//    }
//
//    @Test
//    public void testSytmOffice(){
//        SytmvTenant sytmvTenant0 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 0);
//        sytmvTenant0.setTname(Sval.EmPn.NCENTER.getRemark());
//        sytmvTenant0.setType(Sval.EmPn.NCENTER.getKey());
//        SytmTenant sytmTenant0 = this.facade.initSytmTenant(sytmvTenant0);
//        SytmOffice sytmvOffice0 = this.facade.initSytmOffice(sytmTenant0.sytmvo, CoreIds.NCE_SYS_OFFICE_TOP.getId());
//
//
//        SytmvTenant sytmvTenant1 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 1);
//        sytmvTenant1.setTname(Sval.EmPn.NPROVINCE.getRemark());
//        sytmvTenant1.setType(Sval.EmPn.NPROVINCE.getKey());
//        SytmTenant sytmTenant1 = this.facade.initSytmTenant(sytmvTenant1);
//        this.facade.initSytmOffice(sytmTenant1.sytmvo, CoreIds.NPR_SYS_OFFICE_TOP.getId(), sytmvOffice0.sytmvo.getOffice());
//
//
//        SytmvTenant sytmvTenant2 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 2);
//        sytmvTenant2.setTname(Sval.EmPn.NSCHOOL.getRemark());
//        sytmvTenant2.setType(Sval.EmPn.NSCHOOL.getKey());
//        SytmTenant sytmTenant2 = this.facade.initSytmTenant(sytmvTenant2);
//        this.facade.initSytmOffice(sytmTenant2.sytmvo, CoreIds.NSC_SYS_OFFICE_TOP.getId(), sytmvOffice0.sytmvo.getOffice());
//    }
//
//    @Test
//    public void testSysTenant(){
//        SytmvTenant sytmvTenant0 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 0);
//        sytmvTenant0.setTname(Sval.EmPn.NCENTER.getRemark());
//        sytmvTenant0.setType(Sval.EmPn.NCENTER.getKey());
//        this.facade.initSytmTenant(sytmvTenant0);
//
//        SytmvTenant sytmvTenant1 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 1);
//        sytmvTenant1.setTname(Sval.EmPn.NPROVINCE.getRemark());
//        sytmvTenant1.setType(Sval.EmPn.NPROVINCE.getKey());
//        this.facade.initSytmTenant(sytmvTenant1);
//
//        SytmvTenant sytmvTenant2 = new SytmvTenant(SytFacade.IN_INTERCEPTOR + 2);
//        sytmvTenant2.setTname(Sval.EmPn.NSCHOOL.getRemark());
//        sytmvTenant2.setType(Sval.EmPn.NSCHOOL.getKey());
//        this.facade.initSytmTenant(sytmvTenant2);
//    }
}
