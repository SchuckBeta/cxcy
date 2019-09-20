package com.oseasy.pw.modules.pw.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.entity.PwEnterRoom;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.pw.modules.pw.service.PwEnterRoomService;
import com.oseasy.pw.modules.pw.service.PwEnterService;
import com.oseasy.pw.modules.pw.service.PwRoomService;
import com.oseasy.pw.modules.pw.vo.PwEroom;
import com.oseasy.pw.modules.pw.vo.PwEroomParam;
import com.oseasy.pw.modules.pw.vo.PwEroomStatus;
import com.oseasy.pw.modules.pw.vo.PwSparam;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻场地分配Controller.
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwEnterRoom")
public class PwEnterRoomController extends BaseController {

	@Autowired
	private PwRoomService pwRoomService;
	@Autowired
	private PwEnterRoomService pwEnterRoomService;

	@Autowired
	private PwEnterService pwEnterService;

	@ModelAttribute
	public PwEnterRoom get(@RequestParam(required=false) String id) {
		PwEnterRoom entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = pwEnterRoomService.get(id);
		}
		if (entity == null){
			entity = new PwEnterRoom();
		}
		return entity;
	}

	@RequiresPermissions("pw:pwEnterRoom:view")
	@RequestMapping(value = {"list", ""})
	public String list(PwEnterRoom pwEnterRoom, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PwEnterRoom> page = pwEnterRoomService.findPage(new Page<PwEnterRoom>(request, response), pwEnterRoom);
		model.addAttribute("page", page);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterRoomList";
	}

	@RequiresPermissions("pw:pwEnterRoom:view")
	@RequestMapping(value = "form")
	public String form(PwEnterRoom pwEnterRoom, Model model) {
		model.addAttribute("pwEnterRoom", pwEnterRoom);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterRoomForm";
	}

	@RequiresPermissions("pw:pwEnterRoom:edit")
	@RequestMapping(value = "save")
	public String save(PwEnterRoom pwEnterRoom, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, pwEnterRoom)){
			return form(pwEnterRoom, model);
		}
		pwEnterRoomService.save(pwEnterRoom);
		addMessage(redirectAttributes, "保存入驻场地分配成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnterRoom/?repage";
	}

	@RequiresPermissions("pw:pwEnterRoom:edit")
	@RequestMapping(value = "delete")
	public String delete(PwEnterRoom pwEnterRoom, RedirectAttributes redirectAttributes) {
		pwEnterRoomService.delete(pwEnterRoom);
		addMessage(redirectAttributes, "删除入驻场地分配成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnterRoom/?repage";
	}

	@RequiresPermissions("pw:pwEnter:view")
	@RequestMapping(value = "assignRoomForm")
	public String assignRoomForm(PwEnterRoom pwEnterRoom, Model model, HttpServletRequest request) {
		PwEnter pwEnter = pwEnterService.get(pwEnterRoom.getPwEnter().getId());
		pwEnterRoom.setPwEnter(pwEnter);
		String secondName=request.getParameter("secondName");
		if(StringUtil.isNotEmpty(secondName)){
			model.addAttribute("secondName",secondName);
		}
		model.addAttribute("pwEnterRoom", pwEnterRoom);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterRoomAssignForm";
	}

	/**
	 * 分配场地
	 * @param pwEnterRoom
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("pw:pwEnter:view")
	@RequestMapping(value = "assignRoom")
	public String assignRoom(PwEnterRoom pwEnterRoom, Model model, RedirectAttributes redirectAttributes) {
		try {
		    if((pwEnterRoom.getPwEnter() == null) || StringUtil.isEmpty(pwEnterRoom.getPwEnter().getId())){
	            addMessage(redirectAttributes, "分配状态不能为空！");
	            return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnter/listFPCD?repage";
	        }

            if((pwEnterRoom.getPwRoom() == null) || StringUtil.isEmpty(pwEnterRoom.getPwRoom().getId())){
                addMessage(redirectAttributes, "申请记录不能为空！");
                return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnter/listFPCD?repage";
	        }

	        if((pwEnterRoom.getNum() == null)){
                addMessage(redirectAttributes, "工位数不能为空！");
                return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnter/listFPCD?repage";
	        }

		    PwEroomParam param = new PwEroomParam();
            param.setId(pwEnterRoom.getPwEnter().getId());
            param.setErooms(Arrays.asList(new PwEroom[]{new PwEroom(pwEnterRoom.getPwRoom().getId(), pwEnterRoom.getNum())}));
			pwEnterRoomService.saveEnter(param);
			addMessage(redirectAttributes, "分配场地成功");
		} catch (Exception e){
			addMessage(redirectAttributes, e.getMessage());
		}
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnter/listFPCD?repage";
	}


	  /**
	   * 房间分配入驻团队.
	   * @param pwRoom 实体
	   * @param model 模型
	   * @param request 请求
	   * @param redirectAttributes 重定向
	   * @return String
	   */
	  @ResponseBody
	  @RequestMapping(value = "ajaxPwEnterRoom/{rid}")
	  public ApiTstatus<PwEnterRoom> ajaxPwEnterRoom(@PathVariable(value = "rid") String rid, @RequestParam(required = true) Boolean isEnter, @RequestParam(required = true) String eid, @RequestParam("num") Integer num, HttpServletRequest request, RedirectAttributes redirectAttributes) {
	    if(isEnter == null){
	      return new ApiTstatus<PwEnterRoom>(false, "分配状态不能为空！");
	    }

	    if(StringUtil.isEmpty(eid)){
	      return new ApiTstatus<PwEnterRoom>(false, "申请记录不能为空！");
	    }

	    if((num == null)){
	        return new ApiTstatus<PwEnterRoom>(false, "工位数不能为空！");
	    }

	    PwEnterRoom pwEnterRoom = new PwEnterRoom(rid, eid);
	    if(isEnter){
	        PwEroomParam param = new PwEroomParam();
	        param.setId(eid);
	        param.setErooms(Arrays.asList(new PwEroom[]{new PwEroom(rid, num)}));
	        pwEnterRoomService.saveEnter(param);
	        return new ApiTstatus<PwEnterRoom>(true, "分配成功！", pwEnterRoom);
	    }else{
	      Boolean isTrue = pwEnterRoomService.deletePLWLByErid(pwEnterRoom);
	      if(isTrue){
	        return new ApiTstatus<PwEnterRoom>(true, "取消分配成功！", pwEnterRoom);
	      }else{
	        return new ApiTstatus<PwEnterRoom>(false, "取消分配失败！", pwEnterRoom);
	      }
	    }
	  }

	  /**
	   * 房间分配入驻团队.
	   * @param pwRoom 实体
	   * @param model 模型
	   * @param request 请求
	   * @param redirectAttributes 重定向
	   * @return String
	   */
	  @ResponseBody
	  @RequestMapping(value = "ajaxReassigned/{id}")
	  public ApiTstatus<PwEnterRoom> ajaxReassigned(@PathVariable(value = "id") String id, @RequestParam("num") Integer num, HttpServletRequest request, RedirectAttributes redirectAttributes) {
	      if(StringUtil.isEmpty(id)){
	          return new ApiTstatus<PwEnterRoom>(false, "分配记录ID不能为空！");
	      }

	      if((num == null) || (num <= 0)){
	          return new ApiTstatus<PwEnterRoom>(false, "工位数不能为空！");
	      }

	      PwEnterRoom peroom = pwEnterRoomService.get(id);

          if((peroom.getPwRoom() == null) || StringUtil.isEmpty((peroom.getPwRoom().getId()))){
              return new ApiTstatus<PwEnterRoom>(false, "房间不存在！");
          }
          PwRoom proom = pwRoomService.get(peroom.getPwRoom().getId());

          if(!((PwRoom.Type_Enum.TYPE2.getValue()).equals(peroom.getPwRoom().getNumtype()))){
              return new ApiTstatus<PwEnterRoom>(false, "房间类型不为["+PwRoom.Type_Enum.TYPE2.getName()+"]！");
          }

	      if((num == null) || (num <= 0)){
              return new ApiTstatus<PwEnterRoom>(false, "工位数不能为空！");
          }

	      //分配数<=总工位数，分配数<=剩余工位数+已分配工位数
	      if(((num > peroom.getPwRoom().getNum()) || (num > (peroom.getPwRoom().getRemaindernum() + peroom.getNum())))){
              return new ApiTstatus<PwEnterRoom>(false, "工位数不能为空！");
          }

	      PwEnterRoom peRoom = new PwEnterRoom();
	      peRoom.setPwRoom(new PwRoom(proom.getId()));
          List<PwEnterRoom> perooms =  pwEnterRoomService.findList(peRoom);
          Integer curMax = 0;
          for (PwEnterRoom curPeroom : perooms) {
              if((curPeroom.getId()).equals(peroom.getId())){
                  continue;
              }
              curMax += curPeroom.getNum();
          }

	      peroom.setNum(num);
          pwEnterRoomService.save(peroom);
          proom.setRemaindernum(proom.getNum() - curMax - peroom.getNum());
          pwRoomService.save(proom);
          if ((peroom.getPwEnter() != null) && StringUtil.isNotEmpty(peroom.getPwEnter().getId())) {
              PwEnter pwEnter = pwEnterService.get(peroom.getPwEnter().getId());
              if ((pwEnter != null)) {
                  pwEnter.setRestatus(PwEroomStatus.PER_YFP.getKey());
                  pwEnterService.update(pwEnter);
              }
          }
          return new ApiTstatus<PwEnterRoom>(true, "分配成功！", peroom);
	  }

    /**
     * 未分配房间列表.
     * @param pwSparam
     * @return
     */
    @ResponseBody
    @RequestMapping(value="ajaxPwRooms", method = RequestMethod.POST, produces = "application/json")
    public ApiResult ajaxPwRooms(@RequestBody PwSparam pwSparam){
        PwRoom room = new PwRoom();
        if(pwSparam.getNum() != null){
            room.setRemaindernum(pwSparam.getNum());
        }
		room.setIsAssign(Const.YES);
		room.setNumtype(PwRoom.Type_Enum.TYPE2.getValue());
        List<PwRoom> rooms = pwRoomService.findList(room);
        /*判断房间是否符合条件，不符合条件移除.*/
        Boolean isRoom;
        Boolean needAdd;
        List<PwRoom> curRooms = Lists.newArrayList();
        for (PwRoom curRoom : rooms) {
            isRoom = true;
            needAdd = true;

            //判断是否独立使用房间
            if(pwSparam.getIsAlone()){
                //判断房间容纳人数=剩余容纳人数（未使用）
                if((curRoom.getRemaindernum() < curRoom.getNum())){
                    isRoom = false;
                    continue;
                }
            }

            //判断房间期望容纳人数<剩余容纳人数
            if(((pwSparam.getNum() != null)) && (curRoom.getRemaindernum() < pwSparam.getNum())){
                isRoom = false;
                continue;
            }

            if(needAdd && isRoom){
                curRooms.add(curRoom);
            }
        }
        return ApiResult.success(curRooms);
    }

    /**
     * 未分配房间列表.
     * @param pwSparam
     * @return
     */
    @ResponseBody
    @RequestMapping(value="ajaxPwRoomYfps", method = RequestMethod.POST, produces = "application/json")
    public ApiResult ajaxPwRoomYfps(@RequestBody PwSparam pwSparam){
        if(StringUtil.isEmpty(pwSparam.getEid())){
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+": 入驻标识不能为空！");
        }

        List<PwRoom> rrooms = Lists.newArrayList();
		PwEnterRoom peroom = PwSparam.convertPeroom(pwSparam);
		peroom.getPwRoom().setIsAssign(Const.YES);
		peroom.getPwRoom().setNumtype(PwRoom.Type_Enum.TYPE2.getValue());
        List<PwEnterRoom> erooms = pwEnterRoomService.findList(peroom);
        for (PwEnterRoom curEroom : erooms) {
            if(curEroom.getPwRoom() != null){
                rrooms.add(curEroom.getPwRoom());
            }
        }
        return ApiResult.success(erooms);
    }

	/**
     * 取消分配房间接口.
     * @param pwSparam
     * @return
     */
    @ResponseBody
    @RequestMapping(value="ajaxCancelRooms", method = RequestMethod.POST, produces = "application/json")
    public ApiResult ajaxCancelRooms(@RequestBody PwEroomParam param){
        if(StringUtil.isEmpty(param.getId())){
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+": 入驻标识不能为空！");
        }
        if(StringUtil.checkEmpty(param.getErooms())){
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+": 房间标识不能为空！");
        }
        Boolean istrue = pwEnterRoomService.cancelEnter(param);
        if(istrue){
            return ApiResult.success(param);
        }
        return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+": 分配失败！");
    }

    /**
     * 分配房间接口.
     * @param pwSparam
     * @return
     */
    @ResponseBody
    @RequestMapping(value="ajaxAssignRooms", method = RequestMethod.POST, produces = "application/json")
    public ApiResult ajaxAssignRooms(@RequestBody PwEroomParam param){
        if(StringUtil.isEmpty(param.getId())){
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+": 入驻标识不能为空！");
        }
        if(StringUtil.checkEmpty(param.getErooms())){
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+": 房间标识不能为空！");
        }
        Boolean istrue = pwEnterRoomService.saveEnter(param);
        if(istrue){
            return ApiResult.success(param);
        }
        return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+": 分配失败！");
    }
}