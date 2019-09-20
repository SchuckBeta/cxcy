package com.oseasy.cms.modules.cms.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.cms.common.config.CmsIds;
import com.oseasy.cms.modules.cms.dao.CmsDeclareNotifyDao;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.entity.CmsDeclareNotify;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.BaseEntity;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * declareService.
 * @author 奔波儿灞
 * @version 2018-01-24
 */
@Service
@Transactional(readOnly = true)
public class CmsDeclareNotifyService extends CrudService<CmsDeclareNotifyDao, CmsDeclareNotify> {
	//前台栏目-双创项目
	public static final String CATEGORYS_PROJECT="0000000260";
	//前台栏目-双创大赛
	public static final String CATEGORYS_GCONTEST="0000000261";
	@Autowired
	private SysAttachmentService sysAttachmentService;
	@Autowired
	private CategoryService categoryService;

	public CmsDeclareNotify get(String id) {
		return super.get(id);
	}

	public List<CmsDeclareNotify> findList(CmsDeclareNotify cmsDeclareNotify) {
		return super.findList(cmsDeclareNotify);
	}

	public Page<CmsDeclareNotify> findPage(Page<CmsDeclareNotify> page, CmsDeclareNotify cmsDeclareNotify) {
		return super.findPage(page, cmsDeclareNotify);
	}

	@Transactional(readOnly = false)
	public void save(CmsDeclareNotify cmsDeclareNotify) {
		super.save(cmsDeclareNotify);
	}
	private boolean saveContent(CmsDeclareNotify es) {
		Map<String,String> map=sysAttachmentService.moveAndSaveTempFile(es.getContent(), es.getId(), FileTypeEnum.S12, FileStepEnum.S1201);
		if ("1".equals(map.get("ret"))) {
			es.setContent(map.get("content"));
			return true;
		}
		return false;
	}
	@Transactional(readOnly = false)
	public JSONObject release(CmsDeclareNotify cmsDeclareNotify){
		JSONObject js=new JSONObject();
		js.put("ret", 0);
		if(cmsDeclareNotify==null||StringUtil.isEmpty(cmsDeclareNotify.getId())||BaseEntity.DEL_FLAG_DELETE.equals(cmsDeclareNotify.getDelFlag())){
			js.put("msg", "发布失败，通知已被删除");
			return js;
		}
		Category parent = null;
		if(CATEGORYS_PROJECT.equals(cmsDeclareNotify.getType())){
			parent = categoryService.get(CmsIds.SITE_CATEGORYS_PROJECT_ROOT.getId());
		}else if(CATEGORYS_GCONTEST.equals(cmsDeclareNotify.getType())){
			parent = categoryService.get(CmsIds.SITE_CATEGORYS_GCONTEST_ROOT.getId());
		}else{
		    js.put("msg", "发布失败，parent 为空");
            return js;
		}
		Category categoryapp=new Category();
		categoryapp.setParent(parent);
		categoryapp.setSite(parent.getSite());
		categoryapp.setName(cmsDeclareNotify.getTitle());
		categoryapp.setIsAudit(Const.NO);
		categoryapp.setSort(10);
		categoryapp.setHref( "/cms/cmsDeclareNotify/view?id="+cmsDeclareNotify.getId());
		categoryService.save(categoryapp);
		cmsDeclareNotify.setCategoryId(categoryapp.getId());
		cmsDeclareNotify.setIsRelease("1");
		save(cmsDeclareNotify);
		js.put("ret", 1);
		js.put("msg", "发布成功");
		return js;
	}
	@Transactional(readOnly = false)
	public JSONObject unrelease(CmsDeclareNotify cmsDeclareNotify){
		JSONObject js=new JSONObject();
		js.put("ret", 0);
		if(cmsDeclareNotify==null||StringUtil.isEmpty(cmsDeclareNotify.getId())||BaseEntity.DEL_FLAG_DELETE.equals(cmsDeclareNotify.getDelFlag())){
			js.put("msg", "取消发布失败，通知已被删除");
			return js;
		}
		if(StringUtil.isNotEmpty(cmsDeclareNotify.getCategoryId())){
			categoryService.delete(new Category(cmsDeclareNotify.getCategoryId()));
		}
		cmsDeclareNotify.setCategoryId(null);
		cmsDeclareNotify.setIsRelease("0");
		save(cmsDeclareNotify);
		js.put("ret", 1);
		js.put("msg", "取消发布成功");
		return js;
	}
	@Transactional(readOnly = false)
	public JSONObject saveCdnotify(CmsDeclareNotify cmsDeclareNotify){
		JSONObject js=new JSONObject();
		//处理申报通知菜单
		boolean saveNewCategory=false;//是否要新建菜单
		if("1".equals(cmsDeclareNotify.getIsRelease())){//已发布
			if (StringUtil.isNotEmpty(cmsDeclareNotify.getId())) {//修改时
				CmsDeclareNotify old=get(cmsDeclareNotify.getId());
				if (!"1".equals(old.getIsRelease())) {//未发布变为已发布时
					saveNewCategory=true;
				}
				if(!old.getType().equals(cmsDeclareNotify.getType())){
					categoryService.delete(new Category(old.getCategoryId()));
					saveNewCategory=true;
				}
			}else{//新增时
				saveNewCategory=true;
			}
			if(saveNewCategory){
				Category parent = null;
				if(CATEGORYS_PROJECT.equals(cmsDeclareNotify.getType())){
					parent = categoryService.get(CmsIds.SITE_CATEGORYS_PROJECT_ROOT.getId());
				}else if(CATEGORYS_GCONTEST.equals(cmsDeclareNotify.getType())){
					parent = categoryService.get(CmsIds.SITE_CATEGORYS_GCONTEST_ROOT.getId());
    			}else{
    			    parent = new Category();
    	        }
				Category categoryapp=new Category();
				categoryapp.setParent(parent);
				categoryapp.setSite(parent.getSite());
				categoryapp.setName(cmsDeclareNotify.getTitle());
				categoryapp.setIsAudit(Const.NO);
				categoryapp.setSort(10);
				categoryService.save(categoryapp);
				cmsDeclareNotify.setCategoryId(categoryapp.getId());
			}
		}else{//未发布
			if(StringUtil.isNotEmpty(cmsDeclareNotify.getCategoryId())){
				categoryService.delete(new Category(cmsDeclareNotify.getCategoryId()));
			}
		}
		if (StringUtil.isNotEmpty(cmsDeclareNotify.getId())) {
			CmsDeclareNotify old=get(cmsDeclareNotify.getId());
			if (old!=null) {
				if ("0".equals(old.getIsRelease())&&"1".equals(cmsDeclareNotify.getIsRelease())) {//未发布变为已发布时
					cmsDeclareNotify.setReleaseDate(new Date());
				}else{
					cmsDeclareNotify.setReleaseDate(old.getReleaseDate());
				}
			}
		}else{
			if ("1".equals(cmsDeclareNotify.getIsRelease())) {//发布
				cmsDeclareNotify.setReleaseDate(new Date());
			}
		}
		if (cmsDeclareNotify.getViews()==null) {
			cmsDeclareNotify.setViews("0");
		}
		super.save(cmsDeclareNotify);
		if("1".equals(cmsDeclareNotify.getIsRelease())&&saveNewCategory){
			categoryService.updateHref(cmsDeclareNotify.getCategoryId(), "/cms/cmsDeclareNotify/view?id="+cmsDeclareNotify.getId());
		}
		//处理内容里的临时url---start(需要在entity保存之后，需要id)
		//反转义
		cmsDeclareNotify.setContent(StringEscapeUtils.unescapeHtml4(cmsDeclareNotify.getContent()));
		boolean saveContent=saveContent(cmsDeclareNotify);
		if (saveContent) {
			//转义
			cmsDeclareNotify.setContent(StringEscapeUtils.escapeHtml4(cmsDeclareNotify.getContent()));
			super.save(cmsDeclareNotify);//更新
		}
		//处理内容里的临时url---end


		js.put("ret", 1);
		js.put("msg", "保存成功");
		return js;
	}
	@Transactional(readOnly = false)
	public void delete(CmsDeclareNotify cmsDeclareNotify) {
		super.delete(cmsDeclareNotify);
		if(StringUtil.isNotEmpty(cmsDeclareNotify.getCategoryId())){
			categoryService.delete(new Category(cmsDeclareNotify.getCategoryId()));
		}
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(CmsDeclareNotify cmsDeclareNotify) {
  	  dao.deleteWL(cmsDeclareNotify);
  	}
}