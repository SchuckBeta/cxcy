package com.oseasy.cms.modules.cms.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.cms.modules.cms.dao.CmsArticleDao;
import com.oseasy.cms.modules.cms.dao.CmsArticleDataDao;
import com.oseasy.cms.modules.cms.entity.CmsArticle;
import com.oseasy.cms.modules.cms.entity.CmsArticleData;
import com.oseasy.cms.modules.cms.enums.CategoryModel;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.mqserver.modules.oa.utils.OaUtils;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 一般内容管理Service.
 * @author liangjie
 * @version 2018-09-04
 */
@Service
@Transactional(readOnly = true)
public class CmsArticleService extends CrudService<CmsArticleDao, CmsArticle> {
	@Autowired
	private CmsArticleDataDao cmsArticleDataDao;
	@Autowired
	private CategoryService categoryService;

	public CmsArticle get(String id) {
		return super.get(id);
	}

	public List<CmsArticle> findList(CmsArticle cmsArticle) {
		return super.findList(cmsArticle);
	}

	public List<CmsArticle> validateArticleName(CmsArticle cmsArticle) {
		return dao.validateArticleName(cmsArticle);
	}

	public Page<CmsArticle> findPage(Page<CmsArticle> page, CmsArticle cmsArticle) {
		return super.findPage(page, cmsArticle);
	}
	public Page<CmsArticle> findNormalContentPage(Page<CmsArticle> page, CmsArticle cmsArticle) {
		cmsArticle.setPage(page);
		page.setList(dao.findNormalContentList(cmsArticle));
		return page;
	}
	public Page<CmsArticle> frontArticleListPage(Page<CmsArticle> page, CmsArticle cmsArticle){
		cmsArticle.setPage(page);
		page.setList(dao.frontArticleList(cmsArticle));
		return page;
	}
	@Transactional(readOnly = false)
	public void save(CmsArticle cmsArticle) {

		if (cmsArticle.getCmsArticleData()!= null && cmsArticle.getCmsArticleData().getContent()!=null) {
			cmsArticle.getCmsArticleData().setContent(OaUtils.convertFront(cmsArticle.getCmsArticleData().getContent()));
			cmsArticle.getCmsArticleData().setContent(StringEscapeUtils.unescapeHtml4(
					cmsArticle.getCmsArticleData().getContent()));
		}
		cmsArticle.setUpdateBy(UserUtils.getUser());
		cmsArticle.setUpdateDate(new Date());

		CmsArticleData cmsArticleData = cmsArticle.getCmsArticleData();
		if(cmsArticleData==null){
			cmsArticleData=new CmsArticleData();
		}
		String newUrl = "";
		cmsArticleData.setContentId(cmsArticle.getId());
		if(null != cmsArticle.getId() && StringUtil.isNotBlank(cmsArticle.getId())){
			//修改保存
			cmsArticle.preUpdate();
			if(StringUtil.isNotEmpty(cmsArticle.getImage())) {
				newUrl = VsftpUtils.moveFile(cmsArticle.getImage());
				if (StringUtil.isNotEmpty(newUrl)) {
					cmsArticle.setImage(newUrl);
				}
			}
			if(StringUtil.isNotEmpty(cmsArticle.getThumbnail())) {
				newUrl = VsftpUtils.moveFile(cmsArticle.getThumbnail());
				if (StringUtil.isNotEmpty(newUrl)) {
					cmsArticle.setThumbnail(newUrl);
				}
			}
			dao.update(cmsArticle);
			if(StringUtil.isNotEmpty(cmsArticleData.getId())){
				cmsArticleDataDao.update(cmsArticleData);
			}else{
				//保存文章详情数据
				cmsArticleDataDao.insert(cmsArticleData);
			}
		}else {
			//新增保存
			cmsArticle.preInsert();
			if(StringUtil.isNotEmpty(cmsArticle.getImage())){
				 newUrl = VsftpUtils.moveFile(cmsArticle.getImage());
				if(StringUtil.isNotEmpty(newUrl)){
					cmsArticle.setImage(newUrl);
				}
			}
			if(StringUtil.isNotEmpty(cmsArticle.getThumbnail())){
				newUrl = VsftpUtils.moveFile(cmsArticle.getThumbnail());
				if(StringUtil.isNotEmpty(newUrl)){
					cmsArticle.setThumbnail(newUrl);
				}
			}
			cmsArticle.setViews(0);
			dao.insert(cmsArticle);
			cmsArticleData.setContentId(cmsArticle.getId());
			//保存文章详情数据
			cmsArticleDataDao.insert(cmsArticleData);
		}
	}

	@Transactional(readOnly = false)
	public void updateArticleLikes(CmsArticleData cmsArticleData){
		cmsArticleDataDao.updateArticleLikes(cmsArticleData);
	}


	@Transactional(readOnly = false)
	public void delete(CmsArticle cmsArticle) {
		super.delete(cmsArticle);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(CmsArticle cmsArticle) {
  	  dao.deleteWL(cmsArticle);
  	}

	@Transactional(readOnly = false)
  	public void udpateTop(String ids,String tops){
		String[] idsList = ids.split(",");
		String[] topsList = tops.split(",");
		List<CmsArticle> cmsArticleList = new ArrayList<>();
		for(int i=0;i<idsList.length;i++){
			CmsArticle cmsArticle = new CmsArticle();
			cmsArticle.setId(idsList[i]);
			cmsArticle.setTop(topsList[i]);
			cmsArticleList.add(cmsArticle);
		}
		dao.udpateTop(cmsArticleList);
	}

	@Transactional(readOnly = false)
	public void udpateSort(String ids,String sorts){
		String[] idsList = ids.split(",");
		String[] sortsList = sorts.split(",");
		List<CmsArticle> cmsArticleList = new ArrayList<>();
		for(int i=0;i<idsList.length;i++){
			CmsArticle cmsArticle = new CmsArticle();
			cmsArticle.setId(idsList[i]);
			cmsArticle.setSort(sortsList[i]);
			cmsArticleList.add(cmsArticle);
		}
		dao.udpateSort(cmsArticleList);
	}


	@Transactional(readOnly = false)
	public void udpatePublishStatus(String ids,String publishstatus){
		String[] idsList = ids.split(",");
		String[] publishStatusList = publishstatus.split(",");
		List<CmsArticle> cmsArticleList = new ArrayList<>();
		for(int i=0;i<idsList.length;i++){
			CmsArticle cmsArticle = new CmsArticle();
			cmsArticle.setId(idsList[i]);
			cmsArticle.setPublishStatus(publishStatusList[i]);
			cmsArticleList.add(cmsArticle);
		}
		dao.udpatePublishStatus(cmsArticleList);
	}

	@Transactional(readOnly = false)
	public void deleteCmsArticle(String ids){
		String[] idsList = ids.split(",");

		List<CmsArticle> cmsArticleList = new ArrayList<>();
		for(int i=0;i<idsList.length;i++){
			CmsArticle cmsArticle = new CmsArticle();
			cmsArticle.setId(idsList[i]);
			cmsArticle.setDelFlag("1");
			cmsArticleList.add(cmsArticle);
		}
		dao.udpateDelFlag(cmsArticleList);
	}

    public List<CmsArticle> findIndexList(CmsArticle cmsArticle) {
        return dao.findIndexList(cmsArticle);
    }

    public List<CmsArticle> findIndexFrontList(CmsArticle cmsArticle) {
        return dao.findIndexFrontList(cmsArticle);
    }

    public List<CmsArticle> findListByLimit(CmsArticle cmsArticle) {
        return dao.findListByLimit(cmsArticle);
    }

	@Transactional(readOnly = false)
	public void editTopArticle(CmsArticle cmsArticle) {
		dao.updateTopArticle(cmsArticle);
	}

	public Page<CmsArticle> articleInCommentList(Page<CmsArticle> page, CmsArticle cmsArticle){
		cmsArticle.setPage(page);
		page.setList(dao.articleInCommentList(cmsArticle));
		return page;
	}
	@Transactional(readOnly = false)
	public void publishStatus(String ids, String publishStatus) {
		String[] idsList = ids.split(",");
		List<CmsArticle> cmsArticleList = new ArrayList<>();
		for(int i=0;i<idsList.length;i++){
			CmsArticle cmsArticle = new CmsArticle();
			cmsArticle.setId(idsList[i]);
			cmsArticle.setPublishStatus(publishStatus);
			cmsArticleList.add(cmsArticle);
		}
		dao.udpatePublishStatus(cmsArticleList);
	}
	@Transactional(readOnly = false)
	public void deletePl(String ids) {
		String[] idStr=ids.split(",");
		List<String> idList= Arrays.asList(idStr);
		dao.delPl(idList);
	}

	public void getGcontestShow(Map<String, Object> map) {
		CmsArticle cmsArticle=new CmsArticle();
		cmsArticle.setModule(CategoryModel.HOTMODELPARAM);
		cmsArticle.setPublishStatus("1");
		List<CmsArticle> cmsArticleList =findList(cmsArticle);
		if(cmsArticleList.size()>0){
			CmsArticle cmsArticleIndex=cmsArticleList.get(0);
			map.put("id",cmsArticleIndex.getId());
			map.put("video",cmsArticleIndex.getVideo());
			map.put("Images",cmsArticleIndex.getThumbnail());
		}
	}
	@Transactional(readOnly = false)
	public List<CmsArticle> getCmsArticleByIds(String relation) {
		String[] ids=relation.split(",");
		List<String> relationIdList= Arrays.asList(ids);
		return dao.getCmsArticleByIds(relationIdList);
	}
	@Transactional(readOnly = false)
	public void updateSortArticle(CmsArticle cmsArticle) {
		dao.updateSortArticle(cmsArticle);
	}

	public boolean checkIsHavePublish(String ids) {
		String[] idStrings=ids.split(",");
		List<String> idsList= Arrays.asList(idStrings);
		List<CmsArticle> cmsArticleList= dao.getCmsArticleByPrIds(idsList);
		if(cmsArticleList.size()>0){
			return true;
		}else{
			return false;
		}
	}

	@Transactional(readOnly = false)
	public void updateHitsAddOne(String id) {
		dao.updateHitsAddOne(id);
	}

	//判断是否过期和是否置顶
	@Transactional(readOnly = false)
	public void changeStatusByDate() {
		dao.updateTopPassDate();
		dao.updatePubLishPassDate();
	}

    @Transactional(readOnly = false)
    public void savePublishProject(List<CmsArticle> cmsArticleList) {
        dao.savePublishProject(cmsArticleList);
    }

	public  List<CmsArticle> getCmsArticleAboutList(CmsArticle cmsArticle){
		List<CmsArticle> cmsArticleAbout = new ArrayList();
		if(null == cmsArticle.getCmsArticleData()){

		}else if(null != cmsArticle.getCmsArticleData().getRelation() && StringUtil.isNotBlank(cmsArticle.getCmsArticleData().getRelation())){
			String[] relations = cmsArticle.getCmsArticleData().getRelation().split(",");
			for(int i=0;i<relations.length;i++){
				cmsArticleAbout.add(get(relations[i]));
			}
		}
		return cmsArticleAbout;
	}
}