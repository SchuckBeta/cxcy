package com.oseasy.auy.modules.act.tool.project.impl;

import com.oseasy.auy.common.config.AuyIds;
import com.oseasy.com.pcore.common.config.CoreIds;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.tool.project.ActProParamVo;
import com.oseasy.act.modules.actyw.tool.project.IActProDeal;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.sys.common.config.SysIds;

/**
 * Created by Administrator on 2017/7/29 0029.
 */
//入驻栏目和菜单创建
public class ActProEnter implements IActProDeal {
	//@Autowired
	// CategoryService categoryService;

	ActProUtil actProUtil = (ActProUtil) SpringContextHolder.getBean(ActProUtil.class);
	@Override
	@Transactional(readOnly = false)
	public Boolean dealMenu(ActProParamVo actProParamVo) 	{
		//根据入驻 固定菜单生成流程子菜单
		return actProUtil.dealMenu1(actProParamVo, AuyIds.SITE_PW_ENTER_ROOT.getId());

	}

    @Override
    public Boolean dealDelMenu(ActProParamVo actProParamVo) {
        return true;
    }

	@Override
	@Transactional(readOnly = false)
	public Boolean dealCategory(ActProParamVo actProParamVo) {
		//return actProUtil.dealCategory(actProParamVo,CmsIds.SITE_CATEGORYS_PROJECT_ROOT.getId(),"");
		return true;
	}

    @Override
    public Boolean dealDelCategory(ActProParamVo actProParamVo) {
        return true;
    }

  @Override
  public Boolean dealTime(ActProParamVo actProParamVo) {
    return true;
  }

  @Override
  public Boolean dealIcon(ActProParamVo actProParamVo) {
    return true;
  }

  @Override
  public Boolean dealActYw(ActProParamVo actProParamVo) {
    return true;
  }

  @Override
  public Boolean dealDeploy(ActProParamVo actProParamVo) {
    return true;
  }

  @Override
  public Boolean requireMenu() {
    return true;
  }

  @Override
  public Boolean requireCategory() {
    return true;
  }

  @Override
  public Boolean requireTime() {
    return true;
  }

  @Override
  public Boolean requireIcon() {
    return true;
  }

  @Override
  public Boolean requireActYw() {
    return true;
  }

  @Override
  public Boolean requireDeploy() {
    return true;
  }
}
