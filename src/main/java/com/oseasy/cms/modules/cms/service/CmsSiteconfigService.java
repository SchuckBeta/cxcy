package com.oseasy.cms.modules.cms.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.cms.modules.cms.dao.CmsSiteconfigDao;
import com.oseasy.cms.modules.cms.entity.CmsSiteconfig;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 网站配置Service.
 * @author zy
 * @version 2018-08-27
 */
@Service
@Transactional(readOnly = true)
public class CmsSiteconfigService extends CrudService<CmsSiteconfigDao, CmsSiteconfig> {

	public CmsSiteconfig get(String id) {
		return super.get(id);
	}

	public List<CmsSiteconfig> findList(CmsSiteconfig cmsSiteconfig) {
		return super.findList(cmsSiteconfig);
	}

	public Page<CmsSiteconfig> findPage(Page<CmsSiteconfig> page, CmsSiteconfig cmsSiteconfig) {
		return super.findPage(page, cmsSiteconfig);
	}

	@Transactional(readOnly = false)
	public void save(CmsSiteconfig cmsSiteconfig) {
		super.save(cmsSiteconfig);
	}

	@Transactional(readOnly = false)
	public void delete(CmsSiteconfig cmsSiteconfig) {
		super.delete(cmsSiteconfig);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(CmsSiteconfig cmsSiteconfig) {
  	  dao.deleteWL(cmsSiteconfig);
  	}

	@Transactional(readOnly = false)
	public void saveNewId(CmsSiteconfig cmsSiteconfig) {
//		String id=getLastId();
//		cmsSiteconfig.setId(String.valueOf(Integer.parseInt(id)+1));
		if(cmsSiteconfig.getPicList().size()>0){
			List<CmsSiteconfig> cmsSiteconfigList=new ArrayList<CmsSiteconfig>();
			for(Map<String ,String> map:cmsSiteconfig.getPicList()){
				CmsSiteconfig cmsSiteconfigIndex=new CmsSiteconfig();
				String type=map.get("type");
				String url=map.get("url");
				cmsSiteconfigIndex.preInsert();
				cmsSiteconfigIndex.setType(type);
				String newUrl= null;
				if(StringUtil.isNotEmpty(url)){
					List<String> picUrlList= Arrays.asList(url.split(","));
					List<String> newPicUrlList=new ArrayList<String>();
					for(int i=0;i<picUrlList.size();i++){
						newUrl = VsftpUtils.moveFile(picUrlList.get(i));
						newPicUrlList.add(newUrl);
					}

					String endString="";
					for(int i=0;i<newPicUrlList.size();i++){
						if(i== (newPicUrlList.size()-1)){
							endString=endString+newPicUrlList.get(i);
						}else {
							endString = endString + newPicUrlList.get(i) + ",";
						}
					}
					cmsSiteconfigIndex.setPicUrl(endString);
				}
				cmsSiteconfigIndex.setSiteId(cmsSiteconfig.getSiteId());
				cmsSiteconfigIndex.setTheme(cmsSiteconfig.getTheme());
				cmsSiteconfigIndex.setHeadText(cmsSiteconfig.getHeadText());
				cmsSiteconfigList.add(cmsSiteconfigIndex);
			}
			dao.savePl(cmsSiteconfigList);
		}else {
			save(cmsSiteconfig);
		}
	}

	private String getLastId() {
		String id=dao.getLastId();
		if(StringUtil.isEmpty(id)){
			id="10000";
		}
		return id;
	}

	public List<CmsSiteconfig> getBySiteId(String siteId) {
		return dao.getBySiteId(siteId);
	}

	@Transactional(readOnly = false)
	public void update(CmsSiteconfig cmsSiteconfig) {
		dao.delBySiteId(cmsSiteconfig.getSiteId());
		if(cmsSiteconfig.getPicList().size()>0){
			List<CmsSiteconfig> cmsSiteconfigList=new ArrayList<CmsSiteconfig>();
			for(Map<String ,String> map:cmsSiteconfig.getPicList()){
				CmsSiteconfig cmsSiteconfigIndex=new CmsSiteconfig();
				String type=map.get("type");
				String url=map.get("url");
				cmsSiteconfigIndex.preInsert();
				cmsSiteconfigIndex.setType(type);
				if(StringUtil.isNotEmpty(url)){
					String newUrl= null;
					List<String> picUrlList= Arrays.asList(url.split(","));
					List<String> newPicUrlList=new ArrayList<String>();
					for(int i=0;i<picUrlList.size();i++){
						newUrl = VsftpUtils.moveFile(picUrlList.get(i));
						newPicUrlList.add(newUrl);
					}
					String endString="";
					for(int i=0;i<newPicUrlList.size();i++){
						if(i== (newPicUrlList.size()-1)){
							endString=endString+newPicUrlList.get(i);
						}else {
							endString = endString + newPicUrlList.get(i) + ",";
						}
					}
					cmsSiteconfigIndex.setPicUrl(endString);
				}


//				try {
//					newUrl = VsftpUtils.moveFile(url);
//				} catch (IOException e) {
//					logger.error(e.toString());
//				}
//				if(StringUtil.isNotEmpty(newUrl)){
//					cmsSiteconfigIndex.setPicUrl(newUrl);
//				}
				cmsSiteconfigIndex.setSiteId(cmsSiteconfig.getSiteId());
				cmsSiteconfigIndex.setTheme(cmsSiteconfig.getTheme());
				cmsSiteconfigIndex.setHeadText(cmsSiteconfig.getHeadText());
				cmsSiteconfigList.add(cmsSiteconfigIndex);
			}
			dao.savePl(cmsSiteconfigList);
		}

	}

	public CmsSiteconfig getBySiteIdAndBanner(String id) {
		return dao.getBySiteIdAndBanner(id);
	}

	public CmsSiteconfig getBySiteIdAndType(String id,String type) {
		return dao.getBySiteIdAndType(id,type);
	}

	@Transactional(readOnly = false)
	public void updateLinkType(CmsSiteconfig cmsSiteconfig) {
		dao.update(cmsSiteconfig);
	}
}