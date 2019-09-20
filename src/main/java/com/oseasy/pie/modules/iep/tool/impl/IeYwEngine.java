/**
 * .
 */

package com.oseasy.pie.modules.iep.tool.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.POIXMLException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.common.utils.thread.ThreadPoolUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pie.modules.iep.entity.IepTpl;
import com.oseasy.pie.modules.iep.tool.IeAbsEngine;
import com.oseasy.pie.modules.iep.tool.IeAbsEser;
import com.oseasy.pie.modules.iep.tool.IeAbsYw;
import com.oseasy.pie.modules.iep.vo.TplFType;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.exception.ImpDataException;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.service.IitAbsService;
import com.oseasy.pro.modules.promodel.vo.TplType;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.SpSteel;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import com.oseasy.util.common.utils.rsa.MD5Util;

/**
 * 通用导入模板引擎.
 * @author chenhao
 *
 */
public class IeYwEngine extends IeAbsEngine<IeAbsEser>{
    public final static Logger logger = Logger.getLogger(IeYwEngine.class);
    private static IeYwEservice ieYwEservice = SpringContextHolder.getBean(IeYwEservice.class);
    public IeYwEngine() {
        super(ieYwEservice);
    }

    @Override
    public void checkTpl(IepTpl iepTpl, IeAbsYw yw) {
        service().checkTpl(iepTpl, yw);
    }

    @SuppressWarnings("resource")
    @Override
    public void impData(IepTpl iepTpl, IeAbsYw yw, MultipartFile mpFile) {
        ImpInfo ii = null;
        XSSFWorkbook wb = null;
        InputStream is = null;
        try {
            is = mpFile.getInputStream();
            wb = new XSSFWorkbook(is);
            if((TplType.MR.getKey()).equals(iepTpl.getType())){
                if (!(yw.getRpparam() instanceof IeRpmFlow)) {
                    logger.warn("参数类型未定义！");
                    return;
                }
                ii = ImpInfo.genImpInfo((ActYw) yw.getIeYw(), mpFile.getOriginalFilename(), iepTpl.getType());
                IitAbsService.impservice.save(ii);// 插入导入信息

                IeAbsYw nyw = IeAbsYw.initTpl(iepTpl, yw, ii, wb);
                checkTpl(iepTpl, nyw);// 检查模板版本
                IitAbsService.impservice.save(ii);// 插入导入信息
                IeRpmFlow param = (IeRpmFlow) nyw.getRpparam();
                param.setIi(ii);
                yw.setRpparam(param);

                ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                    @Override
                    public void run() {
                        try {
                            service().impData(iepTpl, nyw, mpFile);
                        } catch (Exception e) {
                            param.getIi().setIsComplete(Const.YES);
                            IitAbsService.impservice.save(param.getIi());
                            CacheUtils.remove(CacheUtils.IMPDATA_CACHE, param.getIi().getId());
                            logger.error(iepTpl.getParent().getName() + iepTpl.getName() + "出错,模板检查异常", e);
                        }
                    }
                });
            }else if((TplType.GJ.getKey()).equals(iepTpl.getType())){
                if (!(yw.getRpparam() instanceof IeRpmFlow)) {
                    logger.warn("参数类型未定义！");
                    return;
                }
                ii = ImpInfo.genImpInfo((ActYw) yw.getIeYw(), mpFile.getOriginalFilename(), iepTpl.getType());
                IitAbsService.impservice.save(ii);// 插入导入信息

                IeAbsYw nyw = IeAbsYw.initTpl(iepTpl, yw, ii, wb);
                checkTpl(iepTpl, nyw);// 检查模板版本
                IitAbsService.impservice.save(ii);// 插入导入信息
                IeRpmFlow param = (IeRpmFlow) nyw.getRpparam();
                param.setIi(ii);
                yw.setRpparam(param);

                ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                    @Override
                    public void run() {
                        try {
                            service().impData(iepTpl, nyw, mpFile);
                        } catch (Exception e) {
                            param.getIi().setIsComplete(Const.YES);
                            IitAbsService.impservice.save(param.getIi());
                            CacheUtils.remove(CacheUtils.IMPDATA_CACHE, param.getIi().getId());
                            logger.error(iepTpl.getParent().getName() + iepTpl.getName() + "出错,模板检查异常", e);
                        }
                    }
                });
            }else{

            }
        } catch (POIXMLException e) {
            ii.setIsComplete(Const.YES);
            if(e.getMessage() != null){
                ii.setErrmsg(e.getMessage());
            }else{
                ii.setErrmsg("请选择正确的文件");
            }
            IitAbsService.impservice.save(ii);
            logger.error("导入出错,模板格式校验出错", e);
        } catch (ImpDataException e) {
            ii.setIsComplete(Const.YES);
            ii.setErrmsg(e.getMessage());
            IitAbsService.impservice.save(ii);
            logger.error("导入出错", e);
        } catch (Exception e) {
            ii.setIsComplete(Const.YES);
            ii.setErrmsg("导入出错");
            IitAbsService.impservice.save(ii);
            logger.error("导入出错", e);
        } finally {
            yw.getRpparam().setIi(ii);
            try {
                is.close();
                //wb.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

    }

    @Override
    public void run(IepTpl iepTpl, IeAbsYw yw, List<MultipartFile> mpFiles) {
        for (MultipartFile mpFile : mpFiles) {
            if(iepTpl.getHasFile()){
                uploadFtp((ActYw) yw.getIeYw(), mpFile);
            }

            // 检查模板和导入信息初始化
            String fileExt = StringUtil.DOT + FileUtil.getFileExtension(mpFile.getOriginalFilename());
            if(((TplFType.EXCEL_XLS.getPostfix()).equals(fileExt) || (TplFType.EXCEL_XLSX.getPostfix()).equals(fileExt))){
                impData(iepTpl, yw, mpFile);
            }
        }
    }

    public void uploadFtp(ActYw yw, MultipartFile mpFile) {
        //上传附件
        try {
            uploadZip(yw, mpFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入附件时，上传附件到Ftp服务器.
     * @param ay
     * @param imgFile
     * @throws Exception
     */
    public static void uploadZip(ActYw ay, MultipartFile imgFile) throws Exception {
        //保存zip文件到本地
        if (!FileUtil.checkFileType(imgFile, FileUtil.DOT + FileUtil.SUFFIX_ZIP)) {
            return;
        }
        String filedir = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
        try {
            File tempPathDir = new File(filedir + File.separator);
            if (!tempPathDir.exists()) {
                tempPathDir.mkdirs();
            }
            String fname = imgFile.getOriginalFilename().toLowerCase();
            File newFile = new File(filedir + File.separator + fname);
            InputStream is = null;
            OutputStream os = null;
            try {
                is = imgFile.getInputStream();
                os = new FileOutputStream(newFile);
                byte buffer[] = new byte[1024];
                int cnt = 0;
                while ((cnt = is.read(buffer)) > 0) {
                    os.write(buffer, 0, cnt);
                }
            } finally {
                //关闭输入输出流
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            }
            //解压缩zip文件
            String unzipdir = filedir + File.separator + IdGen.uuid();// 生成的文件所在目录
            FileUtil.unZipFiles(filedir + File.separator + fname, unzipdir);
            //将解压缩的文件按项目名称放入文件夹
            moveFileToDir(unzipdir);
            //上传到ftp
            uploadZipToFtp(ay, unzipdir);
        } finally {
            //FileUtil.deleteDirectory(filedir);
        }
    }

  //将解压缩的文件按项目名称放入文件夹
    private static void moveFileToDir(String filespath) throws Exception {
        List<String> filters = DictUtils.getDictVsByType(SpSteel.SP_STEEL_DKEY);
        File dir = new File(filespath);
        File[] fs = dir.listFiles();
        if (fs == null || fs.length == 0) {
            return;
        }
        for (File f : fs) {
            if (f.isDirectory()) {
                File[] sfs = f.listFiles();
                if (sfs == null || sfs.length == 0) {
                    continue;
                }

                for (File sff : sfs) {
                    if (sff.isDirectory()) {
                        continue;
                    }

                    String fname = sff.getName();
                    String proname = null;
                    try {
                        /**
                         * 判断文件名是否含有两个下划线分隔，如果有则为项目名。
                         */
                        int first = (fname).indexOf(StringUtil.LINE_D);
                        int last = (fname).indexOf(StringUtil.LINE_D, (first + 1));
                        if((first != -1) && (last != -1) && (first != last)){
                            proname = (fname).substring(first + 1, last);
                        }else{
                            proname = null;
                        }
//                        proname = fname.substring(fname.indexOf("_") + 1, fname.lastIndexOf("_"));
                    } catch (Exception e) {
                        logger.error(ExceptionUtil.getStackTrace(e)+" 文件名不能为空且文件名必须包含两个_！当前文件["+fname+"]");
                        proname = null;
                    }
                    if (proname == null) {
                        continue;
                    }
                    proname = SpSteel.replaceAll(proname, filters);
                    File tempPathDir = new File(filespath + StringUtil.LINE + proname + StringUtil.LINE);
                    if (!tempPathDir.exists()) {
                        tempPathDir.mkdirs();
                    }
                    sff.renameTo(new File(filespath + StringUtil.LINE + proname + StringUtil.LINE + fname));
                }
                continue;
            }
            String fname = f.getName();
            String proname = null;
            try {
                  /**
                  * 判断文件名是否含有两个下划线分隔，如果有则为项目名。
                  */
                 int first = (fname).indexOf(StringUtil.LINE_D);
                 int last = (fname).indexOf(StringUtil.LINE_D, (first + 1));
                 if((first != -1) && (last != -1) && (first != last)){
                     proname = (fname).substring(first + 1, last);
                 }else{
                     proname = null;
                 }
            } catch (Exception e) {
                logger.error(ExceptionUtil.getStackTrace(e) +" 文件名不能为空且文件名必须包含两个_！当前文件["+fname+"]");
                proname = null;
            }
            if (proname == null) {
                continue;
            }
            proname = SpSteel.replaceAll(proname, filters);
            File tempPathDir = new File(filespath + StringUtil.LINE + proname + StringUtil.LINE);
            if (!tempPathDir.exists()) {
                tempPathDir.mkdirs();
            }
            f.renameTo(new File(filespath + StringUtil.LINE + proname + StringUtil.LINE + fname));
        }
    }

  //上传到ftp
    private static void uploadZipToFtp(ActYw ay, String filespath) throws Exception {
        List<String> filters = DictUtils.getDictVsByType(SpSteel.SP_STEEL_DKEY);
        ProProject pp = ay.getProProject();
        if (pp == null || StringUtil.isEmpty(pp.getProType()) || StringUtil.isEmpty(pp.getType())) {
            throw new ImpDataException("大赛配置信息错误");
        }
        File dir = new File(filespath);
        File[] fs = dir.listFiles();
        if (fs == null || fs.length == 0) {
            return;
        }
        String ftpfilepath = ImpDataService.tempProModelFilePath + pp.getProType() + pp.getType();
        File tempPathDir = new File(ftpfilepath + StringUtil.LINE);
        if (!tempPathDir.exists()) {
            tempPathDir.mkdirs();
        }

        for (File f : fs) {
            if (!f.isDirectory()) {
                continue;
            }
            File[] sfs = f.listFiles();
            if (sfs == null || sfs.length == 0) {
                continue;
            }
            String fmd5 = MD5Util.string2MD5(SpSteel.replaceAll(f.getName(), filters));
            for (File sf : sfs) {
                if (!sf.isDirectory()) {
                    String sname = sf.getName().substring(0, sf.getName().lastIndexOf("."));
                    String suffix = sf.getName().substring(sf.getName().lastIndexOf(".") + 1).toLowerCase();
                    String tempname = MD5Util.string2MD5(sname);
                    String sfmd5 = tempname + "." + suffix;
                    File stempPathDir = new File(ftpfilepath + StringUtil.LINE + fmd5 + StringUtil.LINE);
                    if (!stempPathDir.exists()) {
                        stempPathDir.mkdirs();
                    }
                    if (VsftpUtils.isFileExist(ftpfilepath + StringUtil.LINE + fmd5 + StringUtil.LINE + sfmd5)) {
                        VsftpUtils.removeFile(ftpfilepath + StringUtil.LINE + fmd5, sfmd5);
                    }
                    System.out.println("上传文件路径："+ftpfilepath + StringUtil.LINE + fmd5+StringUtil.LINE+sfmd5);
                    VsftpUtils.uploadFile(ftpfilepath + StringUtil.LINE + fmd5, sfmd5, sf);
                    CacheUtils.put(CacheUtils.GcontestImpFile_CACHE + fmd5 + tempname, sf.getName());
                }
            }
        }
    }
}
