package com.oseasy.act.modules.actyw.tool.project;

/**
 * TODO 需要考虑拓展性
 * Created by Administrator on 2017/7/29 0029.
 */
public interface IActProDeal {
  /*
   * 要求菜单.
   * 返回True表示该类流程需要处理菜单
   */
  public Boolean requireMenu();

  /*
   * 处理菜单.
   * 返回True表示该类流程生成菜单处理成功
   */
	public Boolean dealMenu(ActProParamVo actProParamVo);

	/*
	 * 删除菜单.
	 * 返回True表示该类流程删除菜单处理成功
	 */
	public Boolean dealDelMenu(ActProParamVo actProParamVo);

  /*
   * 要求栏目.
   * 返回True表示该类流程需要处理栏目
   */
  public Boolean requireCategory();

	/*
   * 处理栏目.
   * 返回True表示该类流程栏目处理成功
   */
	public Boolean dealCategory(ActProParamVo actProParamVo);

	/*
	 * 删除栏目.
	 * 返回True表示该类流程删除栏目处理成功
	 */
	public Boolean dealDelCategory(ActProParamVo actProParamVo);

	/*
	 * 要求日期.
	 * 返回True表示该类流程需要处理时间
	 */
	public Boolean requireTime();

	/*
	 * 处理日期.
	 * 返回True表示该类流程时间处理成功
	 */
	public Boolean dealTime(ActProParamVo actProParamVo);

	/*
	 * 要求图标.
   * 返回True表示该类流程需要处理图标
	 */
	public Boolean requireIcon();

	/*
	 * 处理图标.
	 * 返回True表示该类流程图标处理成功
	 */
	public Boolean dealIcon(ActProParamVo actProParamVo);

  /*
   * 要求业务流程.
   * 返回True表示该类流程需要处理业务流程
   */
  public Boolean requireActYw();

  /*
   * 处理业务流程数据新增修改.
   * 返回True表示ActYw处理成功
   */
  public Boolean dealActYw(ActProParamVo actProParamVo);

  /*
   * 要求业务发布.
   * 返回True表示该类流程需要处理业务发布
   */
  public Boolean requireDeploy();

  /*
   * 处理业务流程发布.
   * 返回True表示业务发布处理成功
   */
  public Boolean dealDeploy(ActProParamVo actProParamVo);
}
