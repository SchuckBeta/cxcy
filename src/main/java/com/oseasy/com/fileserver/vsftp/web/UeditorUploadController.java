package com.oseasy.com.fileserver.vsftp.web;

import com.oseasy.com.fileserver.common.vsftp.config.Global;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.oseasy.com.fileserver.common.config.FilesrSval;
import com.oseasy.com.fileserver.common.config.FilesrSval.FilesrEmskey;
import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.fileserver.modules.vsftp.service.FtpService;
import com.oseasy.com.fileserver.modules.vsftp.service.UeditorUploadService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONObject;

/**
 * Created by zhangzheng on 2017/6/26.
 */
@Controller
public class UeditorUploadController extends BaseController {

    @Autowired
    private UeditorUploadService ueditorUploadService;
    @Autowired
    private SysAttachmentService sysAttachmentService;

    // 富文本文件上传
    @RequestMapping(value = "${adminPath}/ftp/ueditorUpload/upload")
    @ResponseBody
    public JSONObject uploadA(HttpServletRequest request, HttpServletResponse response) {
        return upload(request, response);
    }

    // 富文本文件上传
    @RequestMapping(value = "${frontPath}/ftp/ueditorUpload/upload")
    @ResponseBody
    public JSONObject uploadF(HttpServletRequest request, HttpServletResponse response) {
        return upload(request, response);
    }

    // 富文本文件上传
    private JSONObject upload(HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile imgFile1 = multipartRequest.getFile("upfile"); // 文件
        String urlFileName = imgFile1.getOriginalFilename();
        // 得到文件名后缀，用id到名称保存。.
        String filename = urlFileName.substring(0, urlFileName.lastIndexOf(".")); // 文件名
        String suffix = urlFileName.substring(urlFileName.lastIndexOf(".") + 1);
        String ftpId = IdGen.uuid();
        String saveFileName = ftpId + "." + suffix;
        String folder = request.getParameter("folder");
        if (StringUtil.isBlank(folder)) {
            folder = "ueditor";
        }
        String ftpPath = folder + "/" + DateUtil.getDate("yyyy-MM-dd");
        long size = imgFile1.getSize();
        String remotePath = Global.REMOTEPATH  + ftpPath;
        try {
            VsftpUtils.uploadFile(remotePath, saveFileName, imgFile1.getInputStream());
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return obj;
        }
        obj.put("state", "SUCCESS");// 上传成功
        obj.put("original", filename);
        obj.put("size", size);
        obj.put("title", urlFileName);
        obj.put("type", suffix);
        obj.put("url", FtpUtil.ftpImgUrl("/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + ftpPath + "/" + saveFileName));
        obj.put("ftpUrl", "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + ftpPath + "/" + saveFileName);
        return obj;
    }

    // 文本上传到临时目录 返回文件链接由应用服务器提供
    @RequestMapping(value = "${adminPath}/ftp/ueditorUpload/uploadTemp")
    @ResponseBody
    public JSONObject uploadTempA(HttpServletRequest request, HttpServletResponse response) {
        return uploadTemp(request, response);
    }


    // 文本上传到临时目录 返回文件链接由应用服务器提供
    @RequestMapping(value = "${adminPath}/ftp/ueditorUpload/uploadPwTemp")
    @ResponseBody
    public JSONObject uploadPwTemp(HttpServletRequest request, HttpServletResponse response) {
        return ueditorUploadService.uploadPwTemp(request, response);
    }

    // 文本上传到临时目录 返回文件链接由应用服务器提供
    @RequestMapping(value = "${frontPath}/ftp/ueditorUpload/uploadTemp")
    @ResponseBody
    public JSONObject uploadTempF(HttpServletRequest request, HttpServletResponse response) {
        return uploadTemp(request, response);
    }

    // 文本上传到临时目录 返回文件链接由应用服务器提供
     @RequestMapping(value = "${frontPath}/ftp/ueditorUpload/uploadPwTemp")
     @ResponseBody
     public JSONObject uploadPwTempF(HttpServletRequest request, HttpServletResponse response) {
         return uploadPwTemp(request, response);
     }

    // 文本上传到正式目录 返回文件链接由应用服务器提供
    @RequestMapping(value = "${frontPath}/ftp/ueditorUpload/uploadTempFormal")
    @ResponseBody
    public JSONObject uploadTempFormalF(HttpServletRequest request, HttpServletResponse response) {
        return uploadTempIndex(request, response);
    }

    // 文本上传到正式目录 返回文件链接由应用服务器提供
    @RequestMapping(value = "${adminPath}/ftp/ueditorUpload/uploadTempFormal")
    @ResponseBody
    public JSONObject uploadTempFormalA(HttpServletRequest request, HttpServletResponse response) {
        return uploadTempIndex(request, response);
    }

    // 文本上传到正式目录 返回文件链接由应用服务器提供
    private JSONObject uploadTempIndex(HttpServletRequest request, HttpServletResponse response) {
        return ueditorUploadService.uploadTempIndex(request);
    }

    // 文本上传到临时目录 返回文件链接由应用服务器提供
    private JSONObject uploadTemp(HttpServletRequest request, HttpServletResponse response) {
        return ueditorUploadService.uploadTempBiz(request);
    }

    // 文本上传到临时目录 返回文件链接由FTP服务器提供
    @RequestMapping(value = "${adminPath}/ftp/ueditorUpload/uploadTempFtp")
    @ResponseBody
    public JSONObject uploadTempFtpA(HttpServletRequest request, HttpServletResponse response) {
        return uploadTempFtp(request, response);
    }

    // 文本上传到临时目录 返回文件链接由FTP服务器提供
    @RequestMapping(value = "${frontPath}/ftp/ueditorUpload/uploadTempFtp")
    @ResponseBody
    public JSONObject uploadTempFtpF(HttpServletRequest request, HttpServletResponse response) {
        return uploadTempFtp(request, response);
    }

    // 文本上传到临时目录 返回文件链接由FTP服务器提供
    private JSONObject uploadTempFtp(HttpServletRequest request, HttpServletResponse response) {
        return ueditorUploadService.uploadTempFtp(request);
    }

    // 上传图片到临时目录
    @RequestMapping(value = "${adminPath}/ftp/ueditorUpload/uploadImg")
    @ResponseBody
    public JSONObject uploadImgA(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return uploadImg(request, response);
    }

    // 上传图片到临时目录
    @RequestMapping(value = "${frontPath}/ftp/ueditorUpload/uploadImg")
    @ResponseBody
    public JSONObject uploadImgF(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return uploadImg(request, response);
    }

    // 上传图片到临时目录
    private JSONObject uploadImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject obj = new JSONObject();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile imgFile1 = multipartRequest.getFile("upfile"); // 文件
        String urlFileName = imgFile1.getOriginalFilename();

        // 判断图片大小
        String imgWidth = (String) request.getParameter("imgWidth"); // 宽度标准
        String imgHeight = (String) request.getParameter("imgHeight"); // 高度标准

        BufferedImage bi = ImageIO.read(imgFile1.getInputStream());
        String realWidth = bi.getWidth() + "";
        String realHeight = bi.getHeight() + "";
        if (!StringUtil.equals(imgWidth, realWidth) && !StringUtil.equals(imgHeight, realHeight)) {
            obj.put("state", "false");
            obj.put("msg", "图片长 X 宽应为(" + imgHeight + "px X " + imgWidth + "px)");
        } else {
            // 得到文件名后缀，用id到名称保存。.
            String filename = urlFileName.substring(0, urlFileName.lastIndexOf(".")); // 文件名
            String suffix = urlFileName.substring(urlFileName.lastIndexOf(".") + 1);
            String ftpId = IdGen.uuid();
            String saveFileName = ftpId + "." + suffix;
            String folder = request.getParameter("folder");
            if (StringUtil.isBlank(folder)) {
                folder = "ueditor";
            }
            String ftpPath = folder + "/" + DateUtil.getDate("yyyy-MM-dd");
            long size = imgFile1.getSize();
            String remotePath = Global.REMOTEPATH  + "/temp/" + ftpPath;
            try {
                VsftpUtils.uploadFile(remotePath, saveFileName, imgFile1.getInputStream());
            } catch (Exception e) {
                logger.error(ExceptionUtil.getStackTrace(e));
                return obj;
            }
            obj.put("state", "SUCCESS");// 上传成功
            obj.put("original", filename);
            obj.put("size", size);
            obj.put("title", urlFileName);
            obj.put("type", suffix);
            obj.put("url", FtpUtil.ftpImgUrl("/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + "/temp/" + ftpPath + "/" + saveFileName));
            obj.put("ftpUrl", "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + "/temp/" + ftpPath + "/" + saveFileName);
            obj.put("width", bi.getWidth());
            obj.put("height", bi.getHeight());
        }
        return obj;
    }

    // 文件删除
    @RequestMapping(value = "${adminPath}/ftp/ueditorUpload/delFile")
    @ResponseBody
    public boolean delFileA(HttpServletRequest request, HttpServletResponse response) {
        return delFile(request, response);
    }

    // 文件删除
    @RequestMapping(value = "${frontPath}/ftp/ueditorUpload/delFile")
    @ResponseBody
    public boolean delFileF(HttpServletRequest request, HttpServletResponse response) {
        return delFile(request, response);
    }

    private boolean delFile(HttpServletRequest request, HttpServletResponse response) {
        return ueditorUploadService.delFile(request, response);
    }

    // 文件下载
    @RequestMapping(value = "${adminPath}/ftp/ueditorUpload/downFile")
    public void downFileA(HttpServletRequest request, HttpServletResponse response) throws Exception {
        downFile(request, response);
    }

    // 文件下载
    @RequestMapping(value = "${frontPath}/ftp/ueditorUpload/downFile")
    public void downFileF(HttpServletRequest request, HttpServletResponse response) throws Exception {
        downFile(request, response);
    }

    // 文件下载
    private void downFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url = request.getParameter("url");
        if (StringUtil.isNotEmpty(url)) {
            String fileName = request.getParameter(FileUtil.FILE_NAME);
            if (StringUtil.isEmpty(fileName)) {
                SysAttachment sa = sysAttachmentService.getByUrl(url);
                if (sa != null) {
                    fileName = URLDecoder.decode(URLDecoder.decode(sa.getName(), FileUtil.UTF_8), FileUtil.UTF_8);
                }
            } else {
                fileName = URLDecoder.decode(URLDecoder.decode(fileName, FileUtil.UTF_8), FileUtil.UTF_8);
            }
            if (StringUtil.isEmpty(fileName)) {
                fileName = "未知的文件";
            }
            String realName = url.substring(url.lastIndexOf("/") + 1);
            String path = url.substring(0, url.lastIndexOf("/") + 1);
            VsftpUtils.downFileWithName(request, response, fileName, realName, path);
        }
    }

    @RequestMapping(value = "${adminPath}/ftp/ueditorUpload/cutImgToTempDir")
    @ResponseBody
    public JSONObject cutImgToTempDirA(HttpServletRequest request, int x, int y, int width, int height) throws Exception {
        return cutImgToTempDir(request, x, y, width, height);
    }

    @RequestMapping(value = "${frontPath}/ftp/ueditorUpload/cutImgToTempDir")
    @ResponseBody
    public JSONObject cutImgToTempDirF(HttpServletRequest request, int x, int y, int width, int height) throws Exception {
        return cutImgToTempDir(request, x, y, width, height);
    }

    private JSONObject cutImgToTempDir(HttpServletRequest request, int x, int y, int width, int height) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("state", true);
        if ((x < 0) || (y < 0) || (width < 0) || (height < 0)) {
            obj.put("state", false);// 上传失败
            obj.put("msg", "参数值不能小于0");//
            return obj;
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile imgFile1 = multipartRequest.getFile("upfile"); // 文件
        String urlFileName = imgFile1.getOriginalFilename();
        // 得到文件名后缀，用id到名称保存。.
        String filename = urlFileName.substring(0, urlFileName.lastIndexOf(".")); // 文件名
        String suffix = urlFileName.substring(urlFileName.lastIndexOf(".") + 1);
        String ftpId = IdGen.uuid();
        String saveFileName = ftpId + "." + suffix;
        String folder = request.getParameter("folder");
        if (StringUtil.isBlank(folder)) {
            folder = "ueditor";
        }
        String ftpPath = folder + "/" + DateUtil.getDate("yyyy-MM-dd");
        long size = imgFile1.getSize();
        String remotePath = Global.REMOTEPATH  + "/temp/" + ftpPath;

        /** 读取图片 */
        Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(suffix);
        ImageReader reader = it.next();

        /** 获取图片流 */
        InputStream is = imgFile1.getInputStream();
        ImageInputStream iis = ImageIO.createImageInputStream(is);

        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(x, y, width, height);
        param.setSourceRegion(rect);
        BufferedImage bi;
        try {
            bi = reader.read(0, param);
        } catch (Exception e1) {
            obj.put("state", false);// 上传失败
            obj.put("msg", "图片格式不支持，建议上传jpg、png格式图片");//
            return obj;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bi, suffix, os);
        InputStream is2 = new ByteArrayInputStream(os.toByteArray());

        try {
            VsftpUtils.uploadFile(remotePath, saveFileName, is2);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            obj.put("state", false);// 上传失败
            obj.put("msg", "文件服务器异常");//
            return obj;
        }
        obj.put("state", "SUCCESS");// 上传成功
        obj.put("original", filename);
        obj.put("size", size);
        obj.put("title", urlFileName);
        obj.put("type", suffix);
        obj.put("url", FtpUtil.ftpImgUrl("/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + "/temp/" + ftpPath + "/" + saveFileName));
        obj.put("ftpUrl", "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + "/temp/" + ftpPath + "/" + saveFileName);
        return obj;
    }

    // 图片裁剪
    @RequestMapping(value = "${adminPath}/ftp/ueditorUpload/cutImg")
    @ResponseBody
    public JSONObject cutImgA(HttpServletRequest request, int x, int y, int width, int height) throws Exception {
        return cutImg(request, x, y, width, height);
    }

    // 图片裁剪
    @RequestMapping(value = "${frontPath}/ftp/ueditorUpload/cutImg")
    @ResponseBody
    public JSONObject cutImgF(HttpServletRequest request, int x, int y, int width, int height) throws Exception {
        return cutImg(request, x, y, width, height);
    }

    // 图片裁剪
    private JSONObject cutImg(HttpServletRequest request, int x, int y, int width, int height) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("state", true);
        if ((x < 0) || (y < 0) || (width < 0) || (height < 0)) {
            obj.put("state", false);// 上传失败
            obj.put("msg", "参数值不能小于0");//
            return obj;
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile imgFile1 = multipartRequest.getFile("upfile"); // 文件
        String urlFileName = imgFile1.getOriginalFilename();
        // 得到文件名后缀，用id到名称保存。.
        String filename = urlFileName.substring(0, urlFileName.lastIndexOf(".")); // 文件名
        String suffix = urlFileName.substring(urlFileName.lastIndexOf(".") + 1);
        String ftpId = IdGen.uuid();
        String saveFileName = ftpId + "." + suffix;
        String folder = request.getParameter("folder");
        if (StringUtil.isBlank(folder)) {
            folder = "ueditor";
        }
        String ftpPath = folder + "/" + DateUtil.getDate("yyyy-MM-dd");
        long size = imgFile1.getSize();
        String remotePath = Global.REMOTEPATH  + ftpPath;

        /** 读取图片 */
        Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(suffix);
        ImageReader reader = it.next();

        /** 获取图片流 */
        InputStream is = imgFile1.getInputStream();
        ImageInputStream iis = ImageIO.createImageInputStream(is);

        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(x, y, width, height);
        param.setSourceRegion(rect);
        BufferedImage bi;
        try {
            bi = reader.read(0, param);
        } catch (Exception e1) {
            obj.put("state", false);// 上传失败
            obj.put("msg", "图片格式不支持，建议上传jpg、png格式图片");//
            return obj;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bi, suffix, os);
        InputStream is2 = new ByteArrayInputStream(os.toByteArray());

        try {
            VsftpUtils.uploadFile(remotePath, saveFileName, is2);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            obj.put("state", false);// 上传失败
            obj.put("msg", "文件服务器异常");//
            return obj;
        }
        obj.put("state", "SUCCESS");// 上传成功
        obj.put("original", filename);
        obj.put("size", size);
        obj.put("title", urlFileName);
        obj.put("type", suffix);
        obj.put("url", FtpUtil.ftpImgUrl("/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + ftpPath + "/" + saveFileName));
        obj.put("ftpUrl", "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + ftpPath + "/" + saveFileName);
        return obj;
    }

    @RequestMapping(value = "${adminPath}/ftp/pagePdfView")
    public String pagePdfViewA(Model model, String url, String fileName, String ftpUrl) {
        return pagePdfView(model, url, fileName, ftpUrl);
    }

    @RequestMapping(value = "${frontPath}/ftp/pagePdfView")
    public String pagePdfViewF(Model model, String url, String fileName, String ftpUrl) {
        return pagePdfView(model, url, fileName, ftpUrl);
    }

    private String pagePdfView(Model model, String url, String fileName, String ftpUrl) {
        model.addAttribute("url", url);
        model.addAttribute("fileName", fileName);
        model.addAttribute("ftpUrl", ftpUrl);
        return FilesrSval.path.vms(FilesrEmskey.ATTACHMENT.k()) + "pagePdfView";
    }

    @RequestMapping(value = "${adminPath}/ftp/ueditorUpload/normal")
    @ResponseBody
    public JSONObject normalA(HttpServletRequest request) throws Exception {
        return normal(request);
    }

    @RequestMapping(value = "${frontPath}/ftp/ueditorUpload/normal")
    @ResponseBody
    public JSONObject normalF(HttpServletRequest request) throws Exception {
        return normal(request);
    }

    /**
     * 直接上传文件，不处理文件
     *
     * @param request
     * @return
     * @throws Exception
     */
    private JSONObject normal(HttpServletRequest request) throws Exception {
        JSONObject obj = new JSONObject();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("upfile"); // 文件
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = FilenameUtils.getName(originalFilename);
        String suffix = FilenameUtils.getExtension(fileName);
        String ftpId = IdGen.uuid();
        String saveFileName = ftpId + "." + suffix;
        String nFileName = request.getParameter("filename");
        String folder = request.getParameter("folder");
        int fileDotIndex;
        if (StringUtil.isBlank(folder)) {
            folder = "ueditor";
        }
        if (StringUtil.isEmpty(suffix)) {
            fileName = nFileName;
            fileDotIndex = nFileName.indexOf(".");
            suffix = nFileName.substring(fileDotIndex+1);
            saveFileName = ftpId + nFileName.replace(nFileName.substring(0, fileDotIndex), "");
        }
        String ftpPath = folder + "/" + DateUtil.getDate("yyyy-MM-dd");
        String remotePath = Global.REMOTEPATH  + ftpPath;
        try {
            VsftpUtils.uploadFile(remotePath, saveFileName, multipartFile.getInputStream());
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return obj;
        }
        obj.put("state", "SUCCESS");// 上传成功
        obj.put("original", fileName);
        obj.put("size", multipartFile.getSize());
        obj.put("title", originalFilename);
        obj.put("type", suffix);
        obj.put("url", FtpUtil.ftpImgUrl("/"+ TenantConfig.getCacheTenant()+remotePath + "/" + saveFileName));
        obj.put("ftpUrl", "/"+ TenantConfig.getCacheTenant()+remotePath + "/" + saveFileName);
        return obj;
    }

}
