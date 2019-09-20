package com.oseasy.pro.jobs.cert;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.pro.modules.cert.entity.CertPage;
import com.oseasy.pro.modules.cert.entity.SysCert;
import com.oseasy.pro.modules.cert.service.SysCertFlowService;
import com.oseasy.pro.modules.cert.service.SysCertService;
import com.oseasy.pro.modules.cert.vo.SysCertFlowVo;
import com.oseasy.util.common.exception.UtilRunException;
import com.oseasy.util.common.utils.FileUtil;

@Service("mdCertJob")
public class MdCertJob extends AbstractJobDetail {
    public final static Logger logger = Logger.getLogger(MdCertJob.class);
    @Autowired
    private SysCertService sysCertService;
    @Autowired
    private SysCertFlowService sysCertFlowService;
    @Autowired
    private UserService userService;
    private final static String roleid = "3";//学校管理员角色

    @Override
    public void doWork() {
        try {
            User send = null;
            List<User> list = userService.findByRoleId(roleid);
            if (list != null && list.size() > 0) {
                send = list.get(0);
            }
            Map<String, List<String>> map = sysCertService.getMdCertFlowsForJob(null, null);
            if (map != null && !map.isEmpty()) {
                for (String cid : map.keySet()) {
                    SysCertFlowVo scfv = sysCertFlowService.getCertFlowVo(cid);
                    if (scfv == null) {
                        logger.error("证书下发任务出错,证书模板关联已被删除");
                        continue;
                    }
                    SysCert sc = sysCertService.get(scfv.getCertId());
                    if (sc == null) {
                        logger.error("证书下发任务出错,证书模板已被删除");
                        continue;
                    }
                    List<CertPage> cps = sysCertService.getCertPages(scfv.getCertId());
                    if (cps == null || cps.size() == 0) {
                        logger.error("证书下发任务出错,证书模板页已被删除");
                        continue;
                    }
                    String tempRootPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
                    for (CertPage cp : cps) {
                        String templatePath = cp.getImgPath();
                        String realName = templatePath.substring(templatePath.lastIndexOf("/") + 1);
                        String path = templatePath.substring(0, templatePath.lastIndexOf("/") + 1);
                        VsftpUtils.downFile(path, realName, tempRootPath);
                    }
                    List<String> pids = map.get(cid);
                    for (String pid : pids) {
                        try {
                            sysCertService.createMdCert(send, cps, sc, scfv, pid, tempRootPath);
                        } catch (UtilRunException e) {
                            logger.error(scfv.getFlowName() + " " + scfv.getNodeName() + " " + sc.getName() + "生成失败:" + e.getMessage());
                        } catch (Exception e) {
                            logger.error(scfv.getFlowName() + " " + scfv.getNodeName() + " " + sc.getName() + "生成失败", e);
                        }
                    }
                    deleteFileOrDir(new File(tempRootPath));
                }
            }
        } catch (Exception e) {
            logger.error("证书下发任务出错", e);
        }
    }

    private void deleteFileOrDir(File dir) {
        if (dir != null) {
            if (dir.isDirectory() && dir.list().length > 0) {
                for (String fps : dir.list()) {
                    deleteFileOrDir(new File(dir, fps));
                }
            }
            dir.delete();
        }
    }
}
