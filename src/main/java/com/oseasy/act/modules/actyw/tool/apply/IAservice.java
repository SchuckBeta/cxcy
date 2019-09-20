/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.com.pcore.common.persistence.Page;

/**
 * 服务.
 * @author chenhao
 */
public interface IAservice<T extends IAengine> {
    /**
     * 查询申请数据.
     * @param id String
     * @return IApply
     */
    public IApply get(String id);

    /**
     * 初始化流程参数.
     * @param engine IAengine
     * @param subGnode 当前结点的子节点
     * @param apply 申请数据
     * @return IApply
     */
    public IApply initIamap(T engine, IGnode subGnode, IApply apply);

    /**
     * 根据ID查询待审核数据.
     * @param engine IAengine
     * @return List
     */
    List<?> ifindTodoByIds(List<String> ids);

    /**
     * 查询审核节点列表数据.
     * @param engine IAengine
     * @return Page
     */
    Page<?> ifindPage(T engine);

    /**
     * 查询申请的查询列表数据.
     * @param engine IAengine
     * @return Page
     */
    Page<?> ifindQPage(T engine);

    /**
     * 批量删除.
     */
    public void idelPl(List<String> ids);

    /**
     * 生成查询条件.
     * @param request 请求
     * @param apply 申请数据
     * @return
     */
    IApply gen(HttpServletRequest request);
}
