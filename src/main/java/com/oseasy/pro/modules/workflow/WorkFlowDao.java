package com.oseasy.pro.modules.workflow;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.pro.modules.workflow.impl.WorkFetyPm;

public interface WorkFlowDao<T extends WorkFetyPm<?>, V extends IWorkDaoety> extends CrudDao<T> {

    int checkMdProName(@Param("pname") String pname, @Param("pid") String pid, @Param("type") String type);

    T getByProModelId(String proModelId);

    List<T> findListByIdsUnAudit(T  t);

    List<T> findListByIds(T t);

    List<V> export(T t);
}