package com.oseasy.pro.modules.gcontest.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.modules.gcontest.dao.GcontestHotsDao;
import com.oseasy.pro.modules.gcontest.dao.GcontestHotsKeywordDao;
import com.oseasy.pro.modules.gcontest.entity.GcontestHots;
import com.oseasy.pro.modules.gcontest.entity.GcontestHotsKeyword;
import com.oseasy.pro.modules.interactive.entity.SysViews;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 大赛热点Service.
 * @author 9527
 * @version 2017-07-12
 */
@Service
@Transactional(readOnly = true)
public class GcontestHotsService extends CrudService<GcontestHotsDao, GcontestHots> {
	@Autowired
	private GcontestHotsKeywordDao gcontestHotsKeywordDao;
	@Autowired
	private SysAttachmentService sysAttachmentService;
	
	/**
	 *大赛热点浏览量队列的处理
	 * @return 处理的数据条数
	 */
	@Transactional(readOnly = false)
	public int handleViews() {
		Map<String,Integer> map=new HashMap<String,Integer>();//需要更新浏览量数量的map
		int tatol=10000;
		int count=0;
		Integer up=null;
		SysViews sv=(SysViews)CacheUtils.rpop(CacheUtils.GCONTESTHOTS_VIEWS_QUEUE);
		while(count<tatol&&sv!=null) {
			count++;//增加了一条数据
			up=map.get(sv.getForeignId());
			if (up==null) {
				map.put(sv.getForeignId(), 1);	
			}else{
				map.put(sv.getForeignId(), up+1);
			}
			if (count<tatol) {
				sv=(SysViews)CacheUtils.rpop(CacheUtils.GCONTESTHOTS_VIEWS_QUEUE);
			}
		}
		if (count>0) {//有数据需要处理
			dao.updateViews(map);
		}
		return count;
	}
	public GcontestHots get(String id) {
		return super.get(id);
	}
	public List<Map<String,Object>> getMore(String id,List<String> keys) {
		return dao.getMore(id,keys);
	}
	public GcontestHots getTop() {
		return dao.getTop();
	}
	public List<GcontestHots> findList(GcontestHots gcontestHots) {
		return super.findList(gcontestHots);
	}

	public Page<GcontestHots> findPage(Page<GcontestHots> page, GcontestHots gcontestHots) {
		return super.findPage(page, gcontestHots);
	}
	private boolean savegcontestHotsContent(GcontestHots es) {
		Map<String,String> map=sysAttachmentService.moveAndSaveTempFile(es.getContent(), es.getId(), FileTypeEnum.S7, FileStepEnum.S701);
		if ("1".equals(map.get("ret"))) {
			es.setContent(map.get("content"));
			return true;
		}
		return false;
	}
	@Transactional(readOnly = false)
	public void save(GcontestHots gcontestHots) {
		if (StringUtil.isNotEmpty(gcontestHots.getContent())) {
			gcontestHots.setContent(gcontestHots.getContent().replaceAll(FtpUtil.FTP_HTTPURL, FtpUtil.FTP_MARKER));
		}
		if (StringUtil.isNotEmpty(gcontestHots.getId())) {
			GcontestHots old=get(gcontestHots.getId());
			if (old!=null) {
				if ("0".equals(old.getIsRelease())&&"1".equals(gcontestHots.getIsRelease())) {//未发布变为已发布时
					gcontestHots.setReleaseDate(new Date());
				}else{
					gcontestHots.setReleaseDate(old.getReleaseDate());
				}
			}
		}else{
			if ("1".equals(gcontestHots.getIsRelease())) {//发布
				gcontestHots.setReleaseDate(new Date());
			}
		}
		if (gcontestHots.getViews()==null) {
			gcontestHots.setViews("0");
		}
		super.save(gcontestHots);
		//处理内容里的临时url---start(需要在entity保存之后，需要id)
		//反转义
		gcontestHots.setContent(StringEscapeUtils.unescapeHtml4(gcontestHots.getContent()));
		//处理之前替换回占位字符串
		gcontestHots.setContent(gcontestHots.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
		boolean saveContent=savegcontestHotsContent(gcontestHots);
		if (saveContent) {
			gcontestHots.setContent(gcontestHots.getContent().replaceAll(FtpUtil.FTP_HTTPURL, FtpUtil.FTP_MARKER));
			//转义
			gcontestHots.setContent(StringEscapeUtils.escapeHtml4(gcontestHots.getContent()));
			super.save(gcontestHots);//更新
		}
		//处理内容里的临时url---end
		if (StringUtil.isNotEmpty(gcontestHots.getId())) {
			gcontestHotsKeywordDao.delByEsid(gcontestHots.getId());
		}
		if (gcontestHots.getKeywords()!=null) {
			for(String ek:gcontestHots.getKeywords()) {
				GcontestHotsKeyword ekk=new GcontestHotsKeyword();
				ekk.setKeyword(ek);
				ekk.setGcontestHotsId(gcontestHots.getId());
				ekk.preInsert();
				gcontestHotsKeywordDao.insert(ekk);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(GcontestHots gcontestHots) {
		super.delete(gcontestHots);
	}

}