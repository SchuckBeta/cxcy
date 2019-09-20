package com.oseasy.pie.modules.expdata.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.shiro.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.fileserver.modules.attachment.dao.SysAttachmentDao;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pie.modules.expdata.dao.ExpInfoDao;
import com.oseasy.pie.modules.expdata.dao.PieSysAttachmentDao;
import com.oseasy.pie.modules.expdata.entity.ExpInfo;

/**
 * 导出数据信息Service.
 * @author 奔波儿灞
 * @version 2017-12-27
 */
@Service
@Transactional(readOnly = true)
public class ExpInfoService extends CrudService<ExpInfoDao, ExpInfo> {
    public final static Logger logger = Logger.getLogger(ExpInfoService.class);
	@Autowired
	private SysAttachmentDao sysAttachmentDao;
	@Autowired
	private PieSysAttachmentDao pieSysAttachmentDao;
	public ExpInfo getExpInfoByType(String type) {
		List<ExpInfo> list = dao.findListByType(type);
		if (list != null && list.size()>0) {
		    logger.info("查询到导出信息，size="+list.size());
			ExpInfo ii=list.get(0);
			if (ii != null) {
				SysAttachment sa=new SysAttachment();
				sa.setUid(ii.getId());
				List<SysAttachment> li=sysAttachmentDao.getFiles(sa);
				if (li!=null&&li.size()>0) {
					ii.setSa(li.get(0));
				}
			}
			return ii;
		}else{
            logger.info("没有查到导出信息，请重试！");
		}
//		ExpInfo expInfo = new ExpInfo();
//		expInfo.setErrmsg("没有查到导出信息，请重试！");
		return null;
	}
	public ExpInfo getExpInfo(String id) {
		ExpInfo ii=(ExpInfo)CacheUtils.get(CacheUtils.EXP_INFO_CACHE+id);
		if (ii==null) {
			ii=super.get(id);
		}else{
			ii.setSuccess(getSucNum(id)+"");
		}
		if (ii!=null) {
			SysAttachment sa=new SysAttachment();
			sa.setUid(ii.getId());
			List<SysAttachment> li=sysAttachmentDao.getFiles(sa);
			if (li!=null&&li.size()>0) {
				ii.setSa(li.get(0));
			}
		}
		return ii;
	}
	public static int getSucNum(String expInfoId) {
		Cache<String, Object> cache=(Cache<String, Object>)CacheUtils.getCache(CacheUtils.EXP_NUM_CACHE+expInfoId);
		int tag=0;
		if (cache!=null) {
			for(String s:cache.keys()) {
				tag=tag+(Integer)cache.get(s);
			}
		}
		return tag;
	}
	public ExpInfo get(String id) {
		return super.get(id);
	}

	public List<ExpInfo> findList(ExpInfo expInfo) {
		return super.findList(expInfo);
	}

	public Page<ExpInfo> findPage(Page<ExpInfo> page, ExpInfo expInfo) {
		return super.findPage(page, expInfo);
	}

	@Transactional(readOnly = false)
	public void save(ExpInfo expInfo) {
		super.save(expInfo);
	}

	@Transactional(readOnly = false)
	public void delete(ExpInfo expInfo) {
		super.delete(expInfo);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ExpInfo expInfo) {
  		dao.deleteWL(expInfo);
  	}
  	@Transactional(readOnly = false)
  	public void deleteByType(String type) {
  		List<ExpInfo> list=dao.findListByType(type);
  		if (list!=null&&list.size()>0) {
  			List<SysAttachment> sas= pieSysAttachmentDao.findByExpInfo(list);
  			if (sas!=null&&sas.size()>0) {
  				for(SysAttachment sa:sas) {
  					String url=sa.getUrl();
  					String remotePath = url.substring(0, url.lastIndexOf("/") + 1);
  					String fileName = url.substring(url.lastIndexOf("/") + 1);
						VsftpUtils.removeFile(remotePath, fileName);
  					sysAttachmentDao.delete(new SysAttachment(sa.getId()));
  				}
  			}
  			dao.deleteByList(list);
  		}
  	}
}