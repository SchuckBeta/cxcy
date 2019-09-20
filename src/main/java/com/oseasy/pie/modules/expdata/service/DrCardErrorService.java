package com.oseasy.pie.modules.expdata.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.OfficeUtils;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.service.DrCardService;
import com.oseasy.pie.modules.expdata.dao.DrCardErrorDao;
import com.oseasy.pie.modules.impdata.vo.DrCardError;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 门禁卡Service.
 * @author chenh
 * @version 2018-03-30
 */
@Service
@Transactional(readOnly = true)
public class DrCardErrorService extends CrudService<DrCardErrorDao, DrCardError> {
    @Autowired
    private CoreService coreService;
    @Autowired
    UserService userService;
    @Autowired
    DrCardService drCardService;
    @Autowired
    OfficeService officeService;

	public DrCardError get(String id) {
		return super.get(id);
	}
	public DrCardError getByg(String id) {
	    return dao.get(id);
	}

	public List<Map<String, String>> findListByImpId(String impId) {
		return dao.findListByImpId(impId);
	}

	public List<DrCardError> findList(DrCardError DrCardError) {
	    return super.findList(DrCardError);
	}
	public Page<DrCardError> findPage(Page<DrCardError> page, DrCardError entity) {
		return super.findPage(page, entity);
	}
	public Page<DrCardError> findPageByg(Page<DrCardError> page, DrCardError entity) {
	    entity.setPage(page);
        page.setList(dao.findList(entity));
        return page;
	}

	@Transactional(readOnly = false)
	public void save(DrCardError DrCardError) {
		super.save(DrCardError);
	}

	@Transactional(readOnly = false)
	public void delete(DrCardError DrCardError) {
		super.delete(DrCardError);
	}

	@Transactional(readOnly = false)
	public void deleteWLByImpId(DrCardError DrCardError) {
	    dao.deleteWLByImpId(DrCardError);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(DrCardError DrCardError) {
  	  dao.deleteWL(DrCardError);
  	}

    /**
     * 门禁卡导入保存卡和用户机构信息.
     * @param validinfo
     * @param impVo
     */
    @Transactional(readOnly = false)
    public void saveCardAndUser(DrCardError pe, ItOper impVo) {
        //学院
        Office officeXY = null;
        if((pe.getCurOffice() != null) && StringUtil.isNotEmpty(pe.getCurOffice().getId())){
            officeXY = officeService.get(pe.getCurOffice());
        }
        if(officeXY == null){
            officeXY = OfficeUtils.getOfficeByName(pe.getOffice());
        }

        if (officeXY == null) {
            officeXY = DrCardError.convertToOfficeXY(pe, officeXY);
            officeXY.setId(IdGen.uuid());
            officeXY.setIsNewRecord(true);
            officeService.save(officeXY);
            pe.setCurOffice(officeXY);
        }else{
            pe.setCurOffice(officeXY);
        }

        //专业
        Office officeZY = null;
        if((pe.getCurProfessional() != null) && StringUtil.isNotEmpty(pe.getCurProfessional().getId())){
            officeZY = officeService.get(pe.getCurProfessional());
        }
        if(officeZY == null){
            officeZY = OfficeUtils.getProfessionalByName(pe.getOffice(), pe.getProfessional());
        }

        if (officeZY == null) {
            officeZY = DrCardError.convertToOfficeZY(pe, officeXY, officeZY);
            officeZY.setId(IdGen.uuid());
            officeZY.setIsNewRecord(true);
            officeService.save(officeZY);
            pe.setCurProfessional(officeZY);
        }

        //用户
        User user = null;
        if((pe.getCurUser() != null) && StringUtil.isNotEmpty(pe.getCurUser().getId())){
            user = userService.findUserById(pe.getCurUser().getId());
        }
        if(user == null){
            user = userService.getByNo(pe.getTmpNo());
        }

        if (user == null) {
            user = DrCardError.convertToUser(pe, user);
            user.setId(IdGen.uuid());
            user.setUserType(RoleBizTypeEnum.XS.getValue());
            user.setOffice(officeZY);
            String password = CoreUtils.USER_PSW_DEFAULT;
            password = CoreUtils.entryptPassword(password);
            user.setPassword(password);
            userService.saveUser(user);

            //给用户添加角色信息
            if (user != null) {
                List<Role> roleList=new ArrayList<Role>();
                roleList.add(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()));
                user.setRoleList(roleList);
                userService.insertUserRole(user);
            }
            pe.setCurUser(user);
        }else{
            user = DrCardError.convertToUser(pe, user);
            user.setUserType(RoleBizTypeEnum.XS.getValue());
            user.setOffice(officeZY);
            userService.updateUser(user);
            pe.setCurUser(user);
        }

        //门禁卡
        DrCard card = drCardService.getByNo(pe.getNo());
        if (card == null) {
            card = DrCardError.convertToDrCard(pe, null);
            card.setId(IdGen.uuid());
            card.setIsNewRecord(true);
        }else{
            //card = DrCardError.convertToDrCard(pe, card);
            card.setIsNewRecord(false);
        }
        drCardService.save(card);
    }

    /**
     * 门禁卡保存用户机构信息.
     * @param validinfo
     * @param impVo
     */
    @Transactional(readOnly = false)
    public User saveUser(DrCardError pe) {
        //学院
        Office officeXY = null;
        if((pe.getCurOffice() != null) && StringUtil.isNotEmpty(pe.getCurOffice().getId())){
            officeXY = officeService.get(pe.getCurOffice());
        }
        if(officeXY == null){
            officeXY = OfficeUtils.getOfficeByName(pe.getOffice());
        }

        if (officeXY == null) {
            officeXY = DrCardError.convertToOfficeXY(pe, officeXY);
            officeXY.setId(IdGen.uuid());
            officeXY.setIsNewRecord(true);
            officeService.save(officeXY);
            pe.setCurOffice(officeXY);
        }else{
            pe.setCurOffice(officeXY);
        }

        //专业
        Office officeZY = null;
        if((pe.getCurProfessional() != null) && StringUtil.isNotEmpty(pe.getCurProfessional().getId())){
            officeZY = officeService.get(pe.getCurProfessional());
        }
        if(officeZY == null){
            officeZY = OfficeUtils.getProfessionalByName(pe.getOffice(), pe.getProfessional());
        }

        if (officeZY == null) {
            officeZY = DrCardError.convertToOfficeZY(pe, officeXY, officeZY);
            officeZY.setId(IdGen.uuid());
            officeZY.setIsNewRecord(true);
            officeService.save(officeZY);
            pe.setCurProfessional(officeZY);
        }

        //用户
        User user = null;
        if((pe.getCurUser() != null) && StringUtil.isNotEmpty(pe.getCurUser().getId())){
            user = userService.findUserById(pe.getCurUser().getId());
        }
        if(user == null){
            user = userService.getByNo(pe.getTmpNo());
        }

        if((user == null) && StringUtil.isNotEmpty(pe.getMobile())){
            User cuser = new User();
            cuser.setMobile(pe.getMobile());
            user = userService.getByMobile(cuser);
        }

        if (user == null) {
            user = DrCardError.convertToUser(pe, user);

            user.setId(IdGen.uuid());
            user.setUserType(RoleBizTypeEnum.XS.getValue());
            user.setOffice(officeXY);
            String password = CoreUtils.USER_PSW_DEFAULT;
            password = CoreUtils.entryptPassword(password);
            user.setPassword(password);
            userService.saveUser(user);

            //给用户添加角色信息
            if (user != null) {
                List<Role> roleList=new ArrayList<Role>();
                roleList.add(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()));
                user.setRoleList(roleList);
                userService.insertUserRole(user);
            }
            pe.setCurUser(user);
        }else{
            user = DrCardError.convertToUser(pe, user);
            user.setUserType(RoleBizTypeEnum.XS.getValue());
            user.setOffice(officeZY);
            userService.updateUser(user);
            pe.setCurUser(user);
        }
        return user;
    }
}