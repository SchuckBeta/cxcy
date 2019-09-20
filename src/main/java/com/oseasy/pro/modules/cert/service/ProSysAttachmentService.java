package com.oseasy.pro.modules.cert.service;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.fileserver.common.vsftp.vo.VsFile;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.pro.modules.cert.entity.CertElement;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.entity.ProReport;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 附件信息表Service
 * @author zy
 * @version 2017-03-23
 */
@Service
@Transactional(readOnly = true)
public class ProSysAttachmentService extends SysAttachmentService {
    public final static Logger logger = Logger.getLogger(ProSysAttachmentService.class);

    /**
     * 根据项目负责人所属的机构生成导出附件的目录.
     * @param proType
     * @param uids
     * @param gnodeName
     * @param distDir
     * @return
     */
    public List<VsFile> getVsGnodeFiles(FileTypeEnum proType, List<String> uids, String gnodeName,String distDir) {
        return getVsGnodeFilesByImpProModel(proType, uids, Lists.newArrayList(), gnodeName, distDir);
    }
    public List<VsFile> getVsGnodeFilesByImpProModel(FileTypeEnum proType, List<String> uids, List<ProModel> pds, String gnodeName,String distDir) {
        List<VsFile> vsFiles = Lists.newArrayList();
        SysAttachment psysAtt = new SysAttachment();
        if ((uids == null) || (proType == null)) {
            return vsFiles;
        }
        psysAtt.setType(proType);
        psysAtt.setUids(uids);
        List<SysAttachment> sysAtts = dao.findListInIdsByPpg(psysAtt);
        for (SysAttachment sysAtt : sysAtts) {
            ProModel curPd = null;
            if(StringUtil.isEmpty(sysAtt.getUid())){
                logger.warn("导出附件存在垃圾数据,没有uid,数据ID = "+sysAtt.getId());
                continue;
            }

            for (ProModel pd : pds) {
                if((sysAtt.getUid()).equals(pd.getId())){
                    curPd = pd;
                    break;
                }
            }

            User cuser = sysAtt.getCreateBy();
            if ((cuser != null) && (cuser.getOffice() != null)) {
                VsFile vsFile = new VsFile();
                vsFile.setRemotePath(sysAtt.getRemotePath());
                vsFile.setRfileName(sysAtt.getFileName());
                vsFile.setLocalPath(distDir + FileUtil.LINE + gnodeName + FileUtil.LINE + cuser.getOffice().getName());
                String prefix = null;
                if(curPd == null){
                    prefix = cuser.getName() + cuser.getNo();
                    vsFile.setLfileName(sysAtt.getName(prefix));
                }else{
                    prefix = cuser.getName() + FileUtil.LINE_D + curPd.getPName() + FileUtil.LINE_D;
                    vsFile.setLfileName(prefix + sysAtt.getName());
                }
                vsFiles.add(vsFile);
            }
        }
        return vsFiles;
    }

    /**
     * 根据导入规则生成导出附件的目录.
     * @param proType
     * @param uids
     * @param gnodeName
     * @param distDir
     * @return
     */
    public List<VsFile> getVsGnodeFilesByImpProjectDeclare(FileTypeEnum proType, List<String> uids, List<ProjectDeclare> pds, String gnodeName,String distDir) {
        List<VsFile> vsFiles = Lists.newArrayList();
        SysAttachment psysAtt = new SysAttachment();
        if ((uids == null) || (proType == null)) {
            return vsFiles;
        }
        psysAtt.setType(proType);
        psysAtt.setUids(uids);
        List<SysAttachment> sysAtts = dao.findListInIdsByPpg(psysAtt);
        for (SysAttachment sysAtt : sysAtts) {
            ProjectDeclare curPd = null;
            if(StringUtil.isEmpty(sysAtt.getUid())){
                logger.warn("导出附件存在垃圾数据,没有uid,数据ID = "+sysAtt.getId());
                continue;
            }

            for (ProjectDeclare pd : pds) {
                if((sysAtt.getUid()).equals(pd.getId())){
                    curPd = pd;
                    break;
                }
            }

            if(curPd == null){
                continue;
            }

            User cuser = sysAtt.getCreateBy();
            if ((cuser != null) && (cuser.getOffice() != null)) {
                VsFile vsFile = new VsFile();
                vsFile.setRemotePath(sysAtt.getRemotePath());
                vsFile.setRfileName(sysAtt.getFileName());
                vsFile.setLfileName(curPd.getLeaderString() + FileUtil.LINE_D + curPd.getName() + FileUtil.LINE_D + sysAtt.getName());
                vsFile.setLocalPath(distDir + FileUtil.LINE + gnodeName);
                vsFiles.add(vsFile);
            }
        }
        return vsFiles;
    }

    @Transactional(readOnly = false)
    public Map<String, SysAttachment> saveByVo(ProReport proReport, FileTypeEnum attachMentType) {
        Map<String, SysAttachment> map = new HashMap<>();
        AttachMentEntity attachment = proReport.getAttachMentEntity();
        try {
            if (attachment != null && attachment.getFielFtpUrl() != null && attachment.getFielFtpUrl().size() > 0) {

                for (int i = 0; i < attachment.getFielFtpUrl().size(); i++) {
                    SysAttachment sa = new SysAttachment();
                    sa.setUid(proReport.getProModelId());
                    sa.setName(URLDecoder.decode(URLDecoder.decode(attachment.getFielTitle().get(i), FileUtil.UTF_8),
                            FileUtil.UTF_8));
                    sa.setGnodeId(proReport.getGnodeId());
                    sa.setSuffix(attachment.getFielType().get(i));
                    sa.setSize(attachment.getFielSize().get(i));
                    sa.setType(attachMentType);
                    sa.setUrl(VsftpUtils.moveFile(attachment.getFielFtpUrl().get(i)));
                    save(sa);
                    VsftpUtils.word2PDF(sa.getUrl());
                    map.put(attachment.getFielFtpUrl().get(i), sa);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    //处理证书模板
    public  Map<String,String> moveAndSaveTempFile(List<CertElement> els,String attachMentUid,FileTypeEnum attachMentType,FileStepEnum attachMentFileStep) throws Exception{
        Map<String,String> map=new HashMap<String,String>();//存放key-temp url和移动之后的value-url
        Set<String> set=getFtpFileUrl(els);//获取所有ftp链接
        SysAttachment sa=new SysAttachment();
        sa.setUid(attachMentUid);
        sa.setType(attachMentType);
        sa.setFileStep(attachMentFileStep);
        deleteByCdnNotInSet(sa, getOldFileUrl(set));//删除所有不在content内容里的文件（ftp和数据库）
        if(set!=null&&set.size()>0){
            for(String tempstr:set){
                if(tempstr.contains("/temp/")){//只需要处理新增的temp链接
                    String tempurl=tempstr.split("\\?")[0];
                    String tempparam=tempstr.split("\\?")[1];
                    String url=VsftpUtils.moveFile("/tool"+tempurl.replace(FtpUtil.FTP_HTTPURL, ""));
                    SysAttachment temsa=new SysAttachment();
                    temsa.setUid(attachMentUid);
                    temsa.setType(attachMentType);
                    temsa.setFileStep(attachMentFileStep);
                    temsa.setSize(tempparam.split("&")[0].split("=")[1]);
                    temsa.setName(tempparam.split("&")[1].split("=")[1]);
                    temsa.setSuffix(tempparam.split("&")[2].split("=")[1]);
                    temsa.setUrl(url);
                    save(temsa);
                    map.put(tempstr, url);
                }
            }
        }
        return map;
    }

    private Set<String> getFtpFileUrl(List<CertElement> list) {
        Set<String> set = new HashSet<String>();
        if(list!=null&&list.size()>0){
            for(CertElement c:list){
                if(StringUtil.isNotEmpty(c.getUrl())){
                    set.add(c.getUrl());
                }
            }
        }
        return set;
    }
}