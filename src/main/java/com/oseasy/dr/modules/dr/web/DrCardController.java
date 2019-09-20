package com.oseasy.dr.modules.dr.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.dr.common.config.DrSval;
import com.oseasy.dr.common.config.DrSval.DrEmskey;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrCardErspace;
import com.oseasy.dr.modules.dr.entity.DrEmentNo;
import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.manager.DrConfig;
import com.oseasy.dr.modules.dr.service.DrCardErspaceService;
import com.oseasy.dr.modules.dr.service.DrCardService;
import com.oseasy.dr.modules.dr.service.DrPwEnterService;
import com.oseasy.dr.modules.dr.vo.DrAuth;
import com.oseasy.dr.modules.dr.vo.DrCardEparam;
import com.oseasy.dr.modules.dr.vo.DrCardEquipment;
import com.oseasy.dr.modules.dr.vo.DrCardType;
import com.oseasy.dr.modules.dr.vo.DrCdealStatus;
import com.oseasy.dr.modules.dr.vo.DrCstatus;
import com.oseasy.dr.modules.dr.vo.PwSpace;
import com.oseasy.dr.modules.dr.vo.PwSpaceDoor;
import com.oseasy.dr.modules.dr.vo.UserQuery;
import com.oseasy.pw.modules.pw.service.PwEnterService;
import com.oseasy.pw.modules.pw.vo.PwEnterType;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 门禁卡Controller.
 * @author chenh
 * @version 2018-03-30
 */
@Controller
@RequestMapping(value = "${adminPath}/dr/drCard")
public class DrCardController extends BaseController {
	@Autowired
	private PwEnterService pwEnterService;
	@Autowired
	private DrPwEnterService drPwEnterService;

	@Autowired
	private SystemService systemService;
	@Autowired
	private DrCardService drCardService;
	@Autowired
	private DrCardErspaceService drCardErspaceService;

	@ModelAttribute
	public DrCard get(@RequestParam(required=false) String id) {
		DrCard entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = drCardService.get(id);
		}
		if (entity == null){
			entity = new DrCard();
		}
		return entity;
	}

	@RequiresPermissions("dr:drCard:view")
	@RequestMapping(value = {"list", ""})
	public String list(DrCard drCard, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DrCard> page = drCardService.findPage(new Page<DrCard>(request, response), drCard);
		model.addAttribute("page", page);
		model.addAttribute(DrConfig.DET_CARD_EXPIRE_MIN_KEY, DrConfig.DET_CARD_EXPIRE_MIN);
		model.addAttribute(DrConfig.DET_CARD_EXPIRE_MAX_KEY, DrConfig.DET_CARD_EXPIRE_MAX);
		model.addAttribute(DrAuth.DR_AUTHORS, Arrays.asList(DrAuth.values()));
		model.addAttribute(DrCstatus.DR_CSTATUSS, DrCstatus.getAll());
		model.addAttribute(DrCdealStatus.DR_CDSTATUSS, DrCdealStatus.getAll());
		return DrSval.path.vms(DrEmskey.DR.k()) + "drCardList";
	}

	@RequiresPermissions("dr:drCard:view")
	@RequestMapping(value = {"listByUser"})
	public String listByUser(DrCard drCard, HttpServletRequest request, HttpServletResponse response, Model model) {
        drCard.setCardType(DrCardType.NO_TEMP.getKey());
	    Page<DrCard> page = drCardService.findUserPageBy(new Page<DrCard>(request, response), drCard);
	    if(StringUtil.checkNotEmpty(page.getList())){
	        List<DrCard> drCards = page.getList();
	        for (DrCard card : drCards) {
	            if(card.getUser() == null){
	                continue;
	            }
	            card.setPeInfo(pwEnterService.getPwEnterInfo(card.getUser().getId()));
            }
	    }
	    model.addAttribute("page", page);
        model.addAttribute(DrConfig.DET_CARD_EXPIRE_MIN_KEY, DrConfig.DET_CARD_EXPIRE_MIN);
        model.addAttribute(DrConfig.DET_CARD_EXPIRE_MAX_KEY, DrConfig.DET_CARD_EXPIRE_MAX);
        model.addAttribute(DrAuth.DR_AUTHORS, Arrays.asList(DrAuth.values()));
        model.addAttribute(DrCstatus.DR_CSTATUSS, DrCstatus.getAll());
        model.addAttribute(DrCdealStatus.DR_CDSTATUSS, DrCdealStatus.getAll());
        model.addAttribute(PwEnterType.PW_ETYPES, Arrays.asList(PwEnterType.values()));
        return DrSval.path.vms(DrEmskey.DR.k()) + "drCardListByUser";
	}

	@RequiresPermissions("dr:drCard:view")
	@RequestMapping(value = {"listByTemp"})
	public String listByTemp(DrCard drCard, HttpServletRequest request, HttpServletResponse response, Model model) {
	    drCard.setCardType(DrCardType.TEMP.getKey());
	    Page<DrCard> page = drCardService.findPage(new Page<DrCard>(request, response), drCard);
	    if(StringUtil.checkNotEmpty(page.getList())){
	        List<DrCard> drCards = page.getList();

            List<User> users = drCardService.findUserListNoCard(new UserQuery());
	        for (DrCard card : drCards) {
	            if(card.getUser() == null){
	                continue;
	            }
	            card.setPeInfo(pwEnterService.getPwEnterInfo(card.getUser().getId()));

                /**
                 * 判断当前是否为临时卡，且用户Id是否为空！
                 */
                if((card.getUser() == null) || StringUtil.isEmpty(card.getUser().getId()) || (DrCardType.NO_TEMP.getKey()).equals(card.getCardType())){
                    continue;
                }

                /**
                 * 判断当前临时卡能否转为正是卡：条件，当前用户入驻且没有卡！
                 */
                for (User user : users) {
                    if((card.getUser().getId()).equals(user.getId())){
                        card.setCanTozs(true);
                        break;
                    }
                }
	        }
	    }
	    model.addAttribute("page", page);
	    model.addAttribute(DrConfig.DET_CARD_EXPIRE_MIN_KEY, DrConfig.DET_CARD_EXPIRE_MIN);
	    model.addAttribute(DrConfig.DET_CARD_EXPIRE_MAX_KEY, DrConfig.DET_CARD_EXPIRE_MAX);
	    model.addAttribute(DrAuth.DR_AUTHORS, Arrays.asList(DrAuth.values()));
	    model.addAttribute(DrCstatus.DR_CSTATUSS, DrCstatus.getAll());
	    model.addAttribute(DrCdealStatus.DR_CDSTATUSS, DrCdealStatus.getAll());
	    return DrSval.path.vms(DrEmskey.DR.k()) + "drCardListByTemp";
	}

	@RequiresPermissions("dr:drCard:view")
	@RequestMapping(value = "form")
	public String form(DrCard drCard, Model model, HttpServletRequest request) {
        model.addAttribute(DrConfig.DET_CARD_EXPIRE_KEY, DrConfig.getExpiry().getTime());
        model.addAttribute(DrAuth.DR_AUTHORS, Arrays.asList(DrAuth.values()));
        model.addAttribute(DrCstatus.DR_CSTATUSS, Arrays.asList(DrCstatus.values()));
        if(drCard.getExpiry() == null){
            drCard.setExpiry(DrConfig.getExpiry().getTime());
        }
        if((drCard.getUser() != null) && StringUtil.isNotEmpty(drCard.getUser().getId())){
            User curUser = systemService.getUser(drCard.getUser().getId());
            if(curUser != null){
                drCard.setUser(curUser);
            }
        }
		model.addAttribute("drCard", drCard);
		dealPwSoace(drCard, model, request);
		return DrSval.path.vms(DrEmskey.DR.k()) + "drCardForm";
	}

	@RequiresPermissions("dr:drCard:view")
	@RequestMapping(value = "formPl")
	public String formPl(DrCard drCard, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if((drCard == null) || StringUtil.checkEmpty(drCard.getIds())){
            addMessage(redirectAttributes, "待授权的卡不能为空!");
            return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCard/listByTemp/?repage";
        }

	    List<DrCard> drCards = drCardService.findList(drCard);
	    if(StringUtil.checkEmpty(drCards)){
	        drCards = Lists.newArrayList();
	    }
        model.addAttribute(DrConfig.DET_CARD_EXPIRE_KEY, DrConfig.getExpiry().getTime());
        model.addAttribute(DrAuth.DR_AUTHORS, Arrays.asList(DrAuth.values()));
        model.addAttribute(DrCstatus.DR_CSTATUSS, Arrays.asList(DrCstatus.values()));
	    model.addAttribute("drCards", drCards);

	    dealPwSoace(drCard, model, request);
	    return DrSval.path.vms(DrEmskey.DR.k()) + "drCardPlForm";
	}

	/**
	 * 处理授权页面显示的场地信息.
	 * @param drCard
	 * @param model
	 * @param request
	 */
	private void dealPwSoace(DrCard drCard, Model model, HttpServletRequest request) {
        List<PwSpace> l= drPwEnterService.getPwSpaceInfo(drCard.getId());
        JSONArray j=JSONArray.fromObject(l);
        model.addAttribute("pwSpaceInfo",j);
        String secondName=request.getParameter("secondName");
        if(StringUtil.isNotEmpty(secondName)){
            model.addAttribute("secondName",secondName);
        }
        if(!Const.NO.equals(drCard.getCardType())){//正式卡
            model.addAttribute("pwEnterInfo", pwEnterService.getPwEnterInfo(drCard.getUser().getId()));
            model.addAttribute("user",UserUtils.get(drCard.getUser().getId()));
        }
    }


	@RequiresPermissions("dr:drCard:view")
	@RequestMapping(value = "getCardData")
	@ResponseBody
	public JSONObject getCardData(DrCard drCard, Model model) {
		JSONObject data=new JSONObject();
		List<PwSpace> l = drPwEnterService.getPwSpaceInfo(drCard.getId());
		if(l!=null&&l.size()>0){
			for(PwSpace ps:l){
				if(ps.getDoors()!=null&&ps.getDoors().size()>0){
					for(PwSpaceDoor d:ps.getDoors()){
						String etpid= d.getEptId();
						String etpno= d.getEptNo();
						String drno= d.getDrNo();
						if(etpid!=null&&etpno!=null&&drno!=null){
							data.put(etpid+","+etpno+","+drno, d.getSelStatus());
						}
					}
				}
			}
		}
		return data;
	}
	/**
	 * 查卡号唯一
	 *
	 * @param cardno
	 * @param cardid
	 * @return
	 */
	@RequestMapping(value = "checkCardNo")
	@ResponseBody
	public Boolean checkNo(String cardno, String cardid) {
        return drCardService.getByNoWithoutId(cardno, cardid) == null;
	}
	@RequiresPermissions("dr:drCard:edit")
	@RequestMapping(value = "save")
	public String save(DrCard drCard, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, drCard)){
			return form(drCard, model,request);
		}
		drCardService.save(drCard);
		addMessage(redirectAttributes, "保存门禁卡成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCard/listByUser/?repage";
	}

	@RequiresPermissions("dr:drCard:edit")
	@RequestMapping(value = "delete")
	public String delete(DrCard drCard, RedirectAttributes redirectAttributes) {
	    if(StringUtil.isEmpty(drCard.getCardType())){
	        addMessage(redirectAttributes, "门禁卡类型不能为空");
            return CorePages.ERROR_404.getIdxUrl();
	    }
		drCardService.delete(drCard);
		DrCardErspace pentity = new DrCardErspace();
        pentity.setCard(new DrCard(drCard.getId()));
        List<DrCardErspace> drCardErspaces = drCardErspaceService.findList(pentity);
        for (DrCardErspace drcerspace : drCardErspaces) {
            drCardErspaceService.deleteWL(drcerspace);
        }
		addMessage(redirectAttributes, "删除门禁卡成功");
		if((DrCardType.TEMP.getKey()).equals(drCard.getCardType())){
	        return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCard/listByUser/?repage";
        }else if((DrCardType.NO_TEMP.getKey()).equals(drCard.getCardType())){
            return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCard/listByUser/?repage";
        }else{
            return CorePages.ERROR_404.getIdxUrl();
        }
	}

	/**
     * 多卡多设备开卡.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxCardPublishPl", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<String>> ajaxCardPublishPl(@RequestBody JSONObject gps) {
        try{
            Map<String, Class> classMap = new HashMap<String, Class>();
            classMap.put(CoreJkey.JK_LIST, DrCardEquipment.class);
            classMap.put(DrEmentNo.DR_EMENTNOS, DrEmentNo.class);
            DrCardEparam gparam = (DrCardEparam) JSONObject.toBean(gps, DrCardEparam.class, classMap);
            if((gparam != null) && StringUtil.checkNotEmpty(gparam.getList())){
                gparam = DrCardEparam.dealDate(gps, gparam);
                ApiTstatus<List<DrCard>> rstatus = drCardService.ajaxCardPublishPl(gparam.getList());
                if(StringUtil.checkNotEmpty(rstatus.getDatas())){
                    return new ApiTstatus<List<String>>(rstatus.getStatus(), rstatus.getMsg(), StringUtil.listIdToList(rstatus.getDatas()));
                }else{
                    return new ApiTstatus<List<String>>(rstatus.getStatus(), rstatus.getMsg());
                }
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            return new ApiTstatus<List<String>>(false, "开卡失败");
        }
        return new ApiTstatus<List<String>>(false, "开卡参数不能为空！");
    }

    /**
     * 单卡多设备激活.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxCardActivateIds", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<DrCard> ajaxCardActivateIds(@RequestParam(required = true) String ids) {
        if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<DrCard>(false, "查询失败,卡号为空！");
        }
        DrCardErspace entity = new DrCardErspace();
        entity.setCard(new DrCard(Arrays.asList((ids).split(StringUtil.DOTH))));
        List<DrCardErspace> drCardErspaces = drCardErspaceService.findListByg(entity);
        if(StringUtil.checkEmpty(drCardErspaces)){
            return new ApiTstatus<DrCard>(false, "参数异常，当前卡没有任何门禁授权信息！");
        }
        return drCardService.ajaxCardActivatePl(DrCardEquipment.converts(drCardErspaces));
    }

    /**
     * 多卡多设备激活.
     * @return ActYwRstatus
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/ajaxCardActivatePl", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<DrCard> ajaxCardActivatePl(@RequestBody JSONObject gps) {
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put(CoreJkey.JK_LIST, DrCardEquipment.class);
        List<DrCardEquipment> gcards = (List<DrCardEquipment>) JSONObject.toBean(gps, DrCardEquipment.class, classMap);
        return drCardService.ajaxCardActivatePl(gcards);
    }

    /**
     * 单卡多设备挂失.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxCardLossIds", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<DrCard> ajaxCardLossIds(@RequestParam(required = true) String ids) {
        if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<DrCard>(false, "查询失败,卡号为空！");
        }
        DrCardErspace entity = new DrCardErspace();
        entity.setCard(new DrCard(Arrays.asList((ids).split(StringUtil.DOTH))));
        List<DrCardErspace> drCardErspaces = drCardErspaceService.findListByg(entity);
        if(StringUtil.checkEmpty(drCardErspaces)){
            return new ApiTstatus<DrCard>(false, "参数异常，当前卡没有任何门禁授权信息！");
        }
        return drCardService.ajaxCardLossPl(DrCardEquipment.converts(drCardErspaces));
    }

    /**
     * 多卡多设备挂失.
     * @return ActYwRstatus
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/ajaxCardLossPl", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<DrCard> ajaxCardLossPl(@RequestBody JSONObject gps) {
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put(CoreJkey.JK_LIST, DrCardEquipment.class);
        List<DrCardEquipment> gcards = (List<DrCardEquipment>) JSONObject.toBean(gps, DrCardEquipment.class, classMap);
        return drCardService.ajaxCardLossPl(gcards);
    }

    /**
     * 单卡多设备黑名单.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxCardBackListIds", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<DrCard> ajaxCardBackListIds(@RequestParam(required = true) String ids) {
        if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<DrCard>(false, "查询失败,卡号为空！");
        }
        DrCardErspace entity = new DrCardErspace();
        entity.setCard(new DrCard(Arrays.asList((ids).split(StringUtil.DOTH))));
        List<DrCardErspace> drCardErspaces = drCardErspaceService.findListByg(entity);
        if(StringUtil.checkEmpty(drCardErspaces)){
            return new ApiTstatus<DrCard>(false, "参数异常，当前卡没有任何门禁授权信息！");
        }
        return drCardService.ajaxCardBackListPl(DrCardEquipment.converts(drCardErspaces));
    }

    /**
     * 多卡多设备黑名单.
     * @return ActYwRstatus
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/ajaxCardBackListPl", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<DrCard> ajaxCardBackListPl(@RequestBody JSONObject gps) {
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put(CoreJkey.JK_LIST, DrCardEquipment.class);
        List<DrCardEquipment> gcards = (List<DrCardEquipment>) JSONObject.toBean(gps, DrCardEquipment.class, classMap);
        return drCardService.ajaxCardBackListPl(gcards);
    }

    /**
     * 单卡多设备退卡.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxCardBackIds", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<DrCard> ajaxCardBackIds(@RequestParam(required = true) String ids) {
        if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<DrCard>(false, "查询失败,卡号为空！");
        }
        DrCardErspace entity = new DrCardErspace();
        entity.setCard(new DrCard(Arrays.asList((ids).split(StringUtil.DOTH))));
        List<DrCardErspace> drCardErspaces = drCardErspaceService.findListByg(entity);
        if(StringUtil.checkEmpty(drCardErspaces)){
            return new ApiTstatus<DrCard>(false, "参数异常，当前卡没有任何门禁授权信息！");
        }
        return drCardService.ajaxCardBackPl(DrCardEquipment.converts(drCardErspaces));
    }

    /**
     * 多卡多设备退卡.
     * @return ActYwRstatus
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/ajaxCardBackPl", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<DrCard> ajaxCardBackPl(@RequestBody JSONObject gps) {
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put(CoreJkey.JK_LIST, DrCardEquipment.class);
        List<DrCardEquipment> gcards = (List<DrCardEquipment>) JSONObject.toBean(gps, DrCardEquipment.class, classMap);
        return drCardService.ajaxCardBackPl(gcards);
    }

    /**
     * 单卡多设备重新发卡.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxCardRepublishIds", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<DrCard>> ajaxCardRepublishIds(@RequestParam(required = true) String ids) {
        if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<List<DrCard>>(false, "查询失败,卡号为空！");
        }
        DrCardErspace entity = new DrCardErspace();
        entity.setCard(new DrCard(Arrays.asList((ids).split(StringUtil.DOTH))));
        List<DrCardErspace> drCardErspaces = drCardErspaceService.findListByg(entity);
        if(StringUtil.checkEmpty(drCardErspaces)){
            return new ApiTstatus<List<DrCard>>(false, "参数异常，当前卡没有任何门禁授权信息！");
        }
        return drCardService.ajaxCardRepublishPl(DrCardEquipment.converts(drCardErspaces));
    }

    /**
     * 多卡多设备重新发卡.
     * @return ActYwRstatus
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/ajaxCardRepublishPl", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<DrCard>> ajaxCardRepublishPl(@RequestBody JSONObject gps) {
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put(CoreJkey.JK_LIST, DrCardEquipment.class);
        List<DrCardEquipment> gcards = (List<DrCardEquipment>) JSONObject.toBean(gps, DrCardEquipment.class, classMap);
        return drCardService.ajaxCardRepublishPl(gcards);
    }

    /**
     * 获取处理中状态
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxDealing", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<DrCard>> ajaxDealing(String ids) {
        if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<List<DrCard>>(true, "没有卡需要处理");
        }
        DrCard drErspace = new DrCard();
        drErspace.setIds(Arrays.asList(StringUtil.split(ids, StringUtil.DOTH)));
        List<DrCard> drCards = drCardService.findList(drErspace);
        return new ApiTstatus<List<DrCard>>(true, "卡处理成功！", drCards);
    }

    /**
     * 获取所有入驻未开卡用户.
     * @param user user
     * @return  ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "ajaxNoCardUsers")
    public ApiTstatus<List<User>> ajaxNoCardUsers(UserQuery user) {
        List<User> users = drCardService.findUserListNoCard(user);
        if (StringUtil.checkNotEmpty(users)) {
            return new ApiTstatus<List<User>>(true, "修复失败,角色ID为空!", users);
        }
        return new ApiTstatus<List<User>>(false, "没有数据！");
    }

    /**
     * 获取所有未开卡用户.
     * @param user user
     * @return  ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "ajaxAllNoCardUsers")
    public ApiTstatus<List<User>> ajaxAllNoCardUsers(UserQuery user) {
        List<User> users = drCardService.findAllUserListNoCard(user);
        if (StringUtil.checkNotEmpty(users)) {
            return new ApiTstatus<List<User>>(true, "修复失败,角色ID为空!", users);
        }
        return new ApiTstatus<List<User>>(false, "没有数据！");
    }

    /**
     * 转正式卡.
     */
    @RequestMapping(value = "ajaxStatusZs")
    public String ajaxStatusZs(DrCard card, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (card.getStatus() != null) {
            drCardService.save(card);
            addMessage(redirectAttributes, "转正式卡成功");
            return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCard/listByUser/?repage";
        }
        addMessage(redirectAttributes, "转正式卡失败");
        return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCard/listByTemp/?repage";
    }

    /**
     * 校验是否存在重复开卡.
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxCheckRepeat", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxCheckRepeat(String param, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ApiTstatus<String> rstatus = new ApiTstatus<String>(true, "校验通过", param);
        if(StringUtil.isEmpty(param)){
            rstatus.setMsg("校验参数不能为空！");
            rstatus.setStatus(false);
            return rstatus;
        }
        if(StringUtil.checkNotEmpty(drCardService.findListCardByNameOrNOorMobile(param))){
            rstatus.setMsg("(姓名|学号|手机号|卡号)对应存在卡，不能重复开卡!");
            rstatus.setStatus(false);
            return rstatus;
        }
        return rstatus;
    }

    /**
     * 重置处理中状态
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxDealReset", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<DrEquipment>> ajaxDealReset(String id) {
        if(StringUtil.isEmpty(id)){
            return new ApiTstatus<List<DrEquipment>>(true, "没有需要重置");
        }

        drCardService.dealReset(id);
        return new ApiTstatus<List<DrEquipment>>(true, "重置成功！");
    }
}