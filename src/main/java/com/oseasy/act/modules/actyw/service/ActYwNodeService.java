package com.oseasy.act.modules.actyw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.dao.ActYwNodeDao;
import com.oseasy.act.modules.actyw.entity.ActYwNode;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 项目流程节点Service.
 *
 * @author chenhao
 * @version 2017-05-23
 */
@Service
@Transactional(readOnly = true)
public class ActYwNodeService extends CrudService<ActYwNodeDao, ActYwNode> {

    public ActYwNode get(String id) {
        return super.get(id);
    }

    public List<ActYwNode> findList(ActYwNode actYwNode) {
        return super.findList(actYwNode);
    }

    public Page<ActYwNode> findPage(Page<ActYwNode> page, ActYwNode actYwNode) {
        return super.findPage(page, actYwNode);
    }

    /**
     * 需要传递节点类型参数.
     */
    @Transactional(readOnly = false)
    public void save(ActYwNode actYwNode) {
        if (actYwNode != null) {
//            if (actYwNode.getType() != RtTypeVal.RT_T0) {
//                actYwNode.setNodeType(StenType.ST_JG_SUB_PROCESS.getType().getKey());
//                actYwNode.setNodeKey(StenType.ST_JG_SUB_PROCESS.getKey());
//            }else {
//                actYwNode.setNodeType(StenType.ST_TASK_USER.getType().getKey());
//                actYwNode.setNodeKey(StenType.ST_TASK_USER.getKey());
//            }

            actYwNode.setIsForm(((actYwNode.getIsForm() != null) && actYwNode.getIsForm()) ? true : false);

            String tmpPath = actYwNode.getIconUrl();
            // 图标处理先把 图片从临时目录移到正式目录，改变url
            if (StringUtil.contains(tmpPath, "/temp")) {
                String realPath = tmpPath.replace("/temp", "");
                VsftpUtils.moveFile(tmpPath);
                actYwNode.setIconUrl(realPath);
            }
            super.save(actYwNode);
        }
    }

    @Transactional(readOnly = false)
    public void delete(ActYwNode actYwNode) {
        super.delete(actYwNode);
    }

}