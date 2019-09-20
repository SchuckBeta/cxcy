package com.oseasy.pro.modules.excellent.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.cms.modules.cms.utils.CmsUtils;
import com.oseasy.cms.modules.cms.vo.ExcellentGcontestVo;
import com.oseasy.cms.modules.cms.vo.ExcellentProjectVo;
import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.excellent.dao.ExcellentKeywordDao;
import com.oseasy.pro.modules.excellent.dao.ExcellentShowDao;
import com.oseasy.pro.modules.excellent.entity.ExcellentKeyword;
import com.oseasy.pro.modules.excellent.entity.ExcellentShow;
import com.oseasy.sys.modules.team.dao.TeamDao;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 优秀展示Service.
 * @author 9527
 * @version 2017-06-23
 */
@Service
@Transactional(readOnly = true)
public class ExcellentShowService extends CrudService<ExcellentShowDao, ExcellentShow> {
	@Autowired
	private ActYwService actYwService;
	@Autowired
	private TeamDao teamDao;
	@Autowired
	private ExcellentKeywordDao excellentKeywordDao;
	@Autowired
	private SysAttachmentService sysAttachmentService;
	public Page<ExcellentGcontestVo> findPage(Page<ExcellentGcontestVo> page, ExcellentGcontestVo vo) {
		vo.setPage(page);
		page.setList(dao.findGcontestList(vo));
		return page;
	}
	public Page<ExcellentProjectVo> findPage(Page<ExcellentProjectVo> page, ExcellentProjectVo vo) {
		vo.setPage(page);
		page.setList(dao.findProjectList(vo));
		return page;
	}

	@Transactional(readOnly = false)
	public JSONObject delete(String ids) {
		JSONObject js=new JSONObject();
		js.put("ret", "1");
		js.put("msg", "删除成功");
		if (StringUtil.isEmpty(ids)) {
			js.put("ret", "0");
			js.put("msg", "删除失败，请选择数据");
		}
		dao.deleteAll(ids.split(","), UserUtils.getUser().getId());
		return js;
	}
	@Transactional(readOnly = false)
	public JSONObject unrelease(String ids) {
		JSONObject js=new JSONObject();
		js.put("ret", "1");
		js.put("msg", "取消发布成功");
		if (StringUtil.isEmpty(ids)) {
			js.put("ret", "0");
			js.put("msg", "取消发布失败，请选择数据");
		}
		dao.unrelease(ids.split(","), UserUtils.getUser().getId());
		return js;
	}
	@Transactional(readOnly = false)
	public JSONObject resall(String fids) {
		JSONObject js=new JSONObject();
		js.put("ret", "1");
		js.put("msg", "发布成功");
		if (StringUtil.isEmpty(fids)) {
			js.put("ret", "0");
			js.put("msg", "发布失败，请选择数据");
		}
		dao.resall(fids.split(","), UserUtils.getUser().getId());
		return js;
	}
	@Transactional(readOnly = false)
	public JSONObject saveExcellentShow(String introduction,String teamid,String type,String fid,String actyw) {
		ActYw  ay=actYwService.get(actyw);
		if (ay!=null) {
			Map<String ,Object> model=new HashMap<String ,Object>();
			model.put("introduction", (introduction==null?"":introduction));
			String s=teamDao.get(teamid).getSummary();
			model.put("team_summary", (s==null?"":s));
			ExcellentShow es=new ExcellentShow();
			es.setType(type);
			es.setForeignId(fid);
			es.setSubType(ay.getProProject().getType());
			es.setContent(CmsUtils.getHtmlByTemplatePath("/templates/modules/excellent/default.xml", model));
			return frontSaveExcellentShow(es);
		}else{
			return null;
		}
	}
	public List<Map<String,String>> getGcontestTeacherInfo(String gcontestId) {
		List<Map<String, String>> list=dao.getGcontestTeacherInfo(gcontestId);
		if (list==null||list.isEmpty()) {
			list=dao.getGcontestTeacherInfoFromProModel(gcontestId);
		}
		return list;
	}
	public Map<String,String> getGcontestInfo(String gcontestId) {
		Map<String, String> map=dao.getGcontestInfo(gcontestId);
		if (map==null||map.isEmpty()) {
			map=dao.getGcontestInfoFromProModel(gcontestId);
		}
		return map;
	}

	public List<Map<String,String>> getProjectTeacherInfo(String projectId) {
		List<Map<String, String>> list=dao.getProjectTeacherInfo(projectId);
		if (list==null||list.isEmpty()) {
			list=dao.getProjectTeacherInfoFromProModel(projectId);
		}
		return list;
	}
	public Map<String,String> getProjectInfo(String projectId) {
		Map<String, String> map=dao.getProjectInfo(projectId);
		if (map==null||map.isEmpty()) {
			map=dao.getProjectInfoFromProModel(projectId);
		}
		return map;
	}

	public Map<String,Object> findIndexProjectList() {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("projectList", dao.findProjectForIndex());
		map.put("gcontestList", dao.findGcontestForIndex());
		return map;
	}

	public Map<String,Object> findForIndex() {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("project", dao.findProjectForIndex());
		map.put("gcontest", dao.findGcontestForIndex());
		map.put("scientific", null);
		return map;
	}
	public ExcellentShow getByForid(String id) {
		return dao.getByForid(id);
	}
	public ExcellentShow get(String id) {
		return super.get(id);
	}

	public List<ExcellentShow> findList(ExcellentShow excellentShow) {
		return super.findList(excellentShow);
	}

	public Page<ExcellentShow> findPage(Page<ExcellentShow> page, ExcellentShow excellentShow) {
		return super.findPage(page, excellentShow);
	}
	@Transactional(readOnly = false)
	public JSONObject frontSaveExcellentShow(ExcellentShow excellentShow) {
		JSONObject js=new JSONObject();
		js.put("ret", "1");
		js.put("msg", "保存成功");
		if (StringUtil.isEmpty(excellentShow.getContent())) {
			js.put("ret", "0");
			js.put("msg", "请编辑页面内容");
			return js;
		}
		try {
			if (StringUtil.isNotEmpty(excellentShow.getId())) {
				ExcellentShow old=get(excellentShow.getId());
				if (old!=null) {
					excellentShow.setIsComment(old.getIsComment());
					excellentShow.setIsTop(old.getIsTop());
					excellentShow.setIsRelease("0");
					excellentShow.setReleaseDate(null);
				}
			}else{
				excellentShow.setIsComment("1");
				excellentShow.setIsTop("0");
				excellentShow.setIsRelease("0");
				if ("1".equals(excellentShow.getIsRelease())) {//发布
					excellentShow.setReleaseDate(new Date());
				}
			}
			//ftp_httpurl会变化，用固定的占位符代替，即使地址变化也不影响文件获取
			excellentShow.setContent(excellentShow.getContent().replaceAll(FtpUtil.FTP_HTTPURL, FtpUtil.FTP_MARKER));
			if (StringUtil.isNotEmpty(excellentShow.getId())) {
				excellentKeywordDao.delByEsid(excellentShow.getId());
			}
			super.save(excellentShow);
			//处理封面、内容里的临时url---start
			//反转义
			excellentShow.setContent(StringEscapeUtils.unescapeHtml4(excellentShow.getContent()));
			//处理之前替换回占位字符串
			excellentShow.setContent(excellentShow.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
			boolean saveImg=saveExcellentShowCoverImg(excellentShow);
			boolean saveContent=saveExcellentShowContent(excellentShow);
			if (saveImg||saveContent) {
				excellentShow.setContent(excellentShow.getContent().replaceAll(FtpUtil.FTP_HTTPURL, FtpUtil.FTP_MARKER));
				//转义
				excellentShow.setContent(StringEscapeUtils.escapeHtml4(excellentShow.getContent()));
				super.save(excellentShow);//更新
			}
			//处理封面、内容里的临时url---end
			js.put("id", excellentShow.getId());
			if (excellentShow.getKeywords()!=null) {
				for(String ek:excellentShow.getKeywords()) {
					ExcellentKeyword ekk=new ExcellentKeyword();
					ekk.setKeyword(ek);
					ekk.setExcellentId(excellentShow.getId());
					ekk.preInsert();
					excellentKeywordDao.insert(ekk);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			js.put("ret", "0");
			js.put("msg", "保存失败，系统错误");
		}
		return js;
	}
	private boolean saveExcellentShowContent(ExcellentShow es) {
		Map<String,String> map=sysAttachmentService.moveAndSaveTempFile(es.getContent(), es.getId(), FileTypeEnum.S6, FileStepEnum.S602);
		if ("1".equals(map.get("ret"))) {
			es.setContent(map.get("content"));
			return true;
		}
		return false;
	}
	private boolean saveExcellentShowCoverImg(ExcellentShow es) {
		if (es.getAttachMentEntity()!=null&&es.getAttachMentEntity().getFielFtpUrl()!=null&&es.getAttachMentEntity().getFielFtpUrl().size()>0) {
			SysAttachment s =new SysAttachment();
			s.setUid(es.getId());
			s.setType(FileTypeEnum.S6);
			s.setFileStep(FileStepEnum.S601);
			sysAttachmentService.deleteByCdn(s);

			Map<String,SysAttachment> map=sysAttachmentService.saveByVo(es.getAttachMentEntity(), es.getId(), FileTypeEnum.S6, FileStepEnum.S601);
			es.setCoverImg(map.get(es.getAttachMentEntity().getFielFtpUrl().get(0)).getUrl());
			return true;
		}
		return false;
	}
	@Transactional(readOnly = false)
	public JSONObject saveExcellentShow(ExcellentShow excellentShow) {
		JSONObject js=new JSONObject();
		js.put("ret", "1");
		js.put("msg", "发布成功");
		if (StringUtil.isEmpty(excellentShow.getContent())) {
			js.put("ret", "0");
			js.put("msg", "请编辑页面内容");
			return js;
		}
		try {
			if (StringUtil.isNotEmpty(excellentShow.getId())) {
				ExcellentShow old=get(excellentShow.getId());
				if (old!=null) {
					if ("0".equals(old.getIsRelease())&&"1".equals(excellentShow.getIsRelease())) {//未发布变为已发布时
						excellentShow.setManaged("1");
						excellentShow.setReleaseDate(new Date());
					}else{
						excellentShow.setReleaseDate(old.getReleaseDate());
					}
				}
			}else{
				if ("1".equals(excellentShow.getIsRelease())) {//发布
					excellentShow.setManaged("1");
					excellentShow.setReleaseDate(new Date());
				}
			}
			excellentShow.setContent(excellentShow.getContent().replaceAll(FtpUtil.FTP_HTTPURL, FtpUtil.FTP_MARKER));//ftp_httpurl会变化，用固定的占位符代替，即使地址变化也不影响文件获取
			if (StringUtil.isNotEmpty(excellentShow.getId())) {
				excellentKeywordDao.delByEsid(excellentShow.getId());
			}
			super.save(excellentShow);
			//处理封面、内容里的临时url---start(需要在entity保存之后，需要id)
			//反转义
			excellentShow.setContent(StringEscapeUtils.unescapeHtml4(excellentShow.getContent()));
			//处理之前替换回占位字符串
			excellentShow.setContent(excellentShow.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
			boolean saveImg=saveExcellentShowCoverImg(excellentShow);
			boolean saveContent=saveExcellentShowContent(excellentShow);
			if (saveImg||saveContent) {//如果有需要处理的url
				excellentShow.setContent(excellentShow.getContent().replaceAll(FtpUtil.FTP_HTTPURL, FtpUtil.FTP_MARKER));
				//转义
				excellentShow.setContent(StringEscapeUtils.escapeHtml4(excellentShow.getContent()));
				super.save(excellentShow);//更新
			}
			//处理封面、内容里的临时url---end
			js.put("id", excellentShow.getId());
			if (excellentShow.getKeywords()!=null) {
				for(String ek:excellentShow.getKeywords()) {
					ExcellentKeyword ekk=new ExcellentKeyword();
					ekk.setKeyword(ek);
					ekk.setExcellentId(excellentShow.getId());
					ekk.preInsert();
					excellentKeywordDao.insert(ekk);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			js.put("ret", "0");
			js.put("msg", "发布失败，系统错误");
		}
		return js;
	}
	@Transactional(readOnly = false)
	public void save(ExcellentShow excellentShow) {
		super.save(excellentShow);
	}

	@Transactional(readOnly = false)
	public void delete(ExcellentShow excellentShow) {
		super.delete(excellentShow);
	}

	public Page<Map<String,String>> findAllProjectShow(Page<Map<String, String>> page, Map<String, Object> param) {
		if (page.getPageNo()<=0) {
			page.setPageNo(1);
		}
		page.setPageSize(8);
		int count=dao.findAllProjectShowCount(param);
		param.put("offset", (page.getPageNo()-1)*page.getPageSize());
		param.put("pageSize", page.getPageSize());
		List<Map<String,String>> list=null;
		if (count>0) {
			list = dao.findAllProjectShow(param);
		}
		page.setCount(count);
		page.setList(list);
		page.initialize();
		return page;
	}

}