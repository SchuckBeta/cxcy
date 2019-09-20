package com.oseasy.com.fileserver.modules.attachment.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 附件信息表DAO接口
 * @author zy
 * @version 2017-03-23
 */
@MyBatisDao
public interface SysAttachmentDao extends CrudDao<SysAttachment> {
	@Override
	@InsertByTenant
	public int insert(SysAttachment entity);
	public void deleteByCdnNotInSet(@Param("sa")SysAttachment s,@Param("urls")Set<String> set);
	public void deleteByCdn(SysAttachment s);
	public List<SysAttachment> getFilesNotInSet(@Param("sa")SysAttachment s,@Param("urls")Set<String> set);
	public List<SysAttachment> getFiles(SysAttachment s);
	public List<Map<String,String>> getFileInfo(Map<String,String> map);
	public SysAttachment getByUrl(String url);
	public void deleteByUid(String uid);
	public void updateAtt(@Param("gid")String gid,@Param("oldGidId")String oldGidId);
	public List<SysAttachment> findListInIds(SysAttachment entity);

	public List<SysAttachment> findAttListByUid(@Param("uid")String uid);
	/**
	 * 根据项目、自定义项目大赛查找.
	 * @param entity
	 * @return
	 */
	public List<SysAttachment> findListInIdsByPpg(SysAttachment entity);

	String checkHasFile(@Param("id")String id);

	@FindListByTenant
	String getLogoByProModelId(@Param("uid")String uid);

	/**
	 * 查询省模板文件名
	 * @param actYwId
	 * @return
	 */
	List<SysAttachment> getProvinceDoc(@Param("actYwId")String actYwId, @Param("provinceTenantId")String provinceTenantId,
									   @Param("fileStep")String fileStep);
}