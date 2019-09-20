package com.oseasy.pie.modules.impdata.service;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.rsa.MD5Util;

/**
 * 附件信息表Service
 * @author zy
 * @version 2017-03-23
 */
@Service
@Transactional(readOnly = true)
public class PieSysAttachmentService {
    public final static Logger logger = Logger.getLogger(PieSysAttachmentService.class);
    @Autowired
    private SysAttachmentService sysAttachmentService;

    @Transactional(readOnly = false)
    public void saveFileForImp(ProModel pd, ActYw ay,FileTypeEnum attachMentType, FileStepEnum attachMentFileStep) {
        try {
            ProProject pp=ay.getProProject();
            if((pd == null) || StringUtil.isEmpty(pd.getPName())){
                throw new RuntimeException("名称为空,导致空指针异常，请检查文件标题是否为第(" + ImpDataService.descHeadRow + 1 +")行开始！");
            }
            System.out.println("saveFileForImp--->name="+pd.getPName());
            String pnamemd5=MD5Util.string2MD5(pd.getPName());
            String path=ImpDataService.tempProModelFilePath+pp.getProType()+pp.getType()+StringUtil.LINE+pnamemd5;
            FTPFile[] ffs=VsftpUtils.getFiles(path);
            if(ffs==null){
                logger.warn("没有找到对应的附件信息！路径：" + path);
                return;
            }

            for (FTPFile ff:ffs) {
                if(ff.isFile()){
                    String sname = ff.getName().substring(0,ff.getName().lastIndexOf("."));
                    String suffix = ff.getName().substring(ff.getName().lastIndexOf(".") + 1).toLowerCase();
                    String fname=(String)CacheUtils.get(CacheUtils.GcontestImpFile_CACHE+pnamemd5+sname);
                    SysAttachment sa = new SysAttachment();
                    sa.setUid(pd.getId());
                    sa.setName(fname);
                    sa.setSuffix(suffix);
                    sa.setSize(ff.getSize()+"");
                    sa.setType(attachMentType);
                    sa.setFileStep(attachMentFileStep);
                    String newPath=ImpDataService.proModelFilePath+DateUtil.getDate("yyyy-MM-dd")+StringUtil.LINE+IdGen.uuid()+"."+suffix;
                    sa.setUrl(VsftpUtils.moveFile(path+StringUtil.LINE+ff.getName(),newPath));
                    sysAttachmentService.save(sa);
                    CacheUtils.remove(CacheUtils.GcontestImpFile_CACHE+pnamemd5+sname);
                    VsftpUtils.word2PDF(sa.getUrl());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}