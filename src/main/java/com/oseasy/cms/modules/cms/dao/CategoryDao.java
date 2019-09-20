package com.oseasy.cms.modules.cms.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.com.pcore.common.persistence.TreeDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.Dict;

/**
 * 栏目管理DAO接口.
 * @author liangjie
 * @version 2018-08-30
 */
@MyBatisDao
public interface CategoryDao extends TreeDao<Category> {
    @Override
    @FindListByTenant
    public List<Category> findList(Category entity);
    @Override
    @InsertByTenant
    public int insert(Category entity);
    public void updateHref(@Param("id")String id,@Param("href")String href);

    public Category findCategoryByTenantId(Category category);
    public List<Category> findModule(Category category);

    @Override
    @FindListByTenant
    public List<Category> findByParentIdsLike(Category entity);

    @FindListByTenant
    public Category getByPublishCategory(Category entity);



//  public List<Category> findByParentIdsLike(Category category);
//  {
//      return find("from Category where parentIds like :p1", new Parameter(parentIds));
//  }

    public List<Category> findByModule(String module);
//  {
//      return find("from Category where delFlag=:p1 and (module='' or module=:p2) order by site.id, sort",
//              new Parameter(Category.DEL_FLAG_NORMAL, module));
//  }

    public List<Category> findByParentId(String parentId, String isMenu);
//  {
//      return find("from Category where delFlag=:p1 and parent.id=:p2 and inMenu=:p3 order by site.id, sort",
//              new Parameter(Category.DEL_FLAG_NORMAL, parentId, isMenu));
//  }

    public List<Category> findByParentIdAndSiteId(Category entity);

    public List<Category> findListByTenant(Category entity);

    public List<Map<String, Object>> findStats(String sql);
//  {
//      return find("from Category where delFlag=:p1 and parent.id=:p2 and site.id=:p3 order by site.id, sort",
//              new Parameter(Category.DEL_FLAG_NORMAL, parentId, siteId));
//  }

    /**
     * 根据parentId获取栏目
     * @param entity
     * @return
     */
    public List<Category> getByParentId(Category entity);

    /**
     * 根据parentId获取栏目
     * @param entity
     * @return
     */
    public List<Category> findListIdsLike(Category entity);

    /**
     * 根据name获取栏目
     * @param entity
     * @return
     */
    public Category getCategoryByName(String name);

    //public List<Category> findByIdIn(String[] ids);
//  {
//      return find("from Category where id in (:p1)", new Parameter(new Object[]{ids}));
//  }
    //public List<Category> find(Category category);

//  @Query("select distinct c from Category c, Role r, User u where c in elements (r.categoryList) and r in elements (u.roleList)" +
//          " and c.delFlag='" + Category.DEL_FLAG_NORMAL + "' and r.delFlag='" + Role.DEL_FLAG_NORMAL +
//          "' and u.delFlag='" + User.DEL_FLAG_NORMAL + "' and u.id=?1 or (c.user.id=?1 and c.delFlag='" + Category.DEL_FLAG_NORMAL +
//          "') order by c.site.id, c.sort")
//  public List<Category> findByUserId(Long userId);

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(Category cmsCategory);

  public void updateOrder(@Param("cmsCategoryList") List<Category> cmsCategory);

  public List<Category> getListBySiteId(String siteId);

  public void saveList(@Param("cmsCategoryList")List<Category> cmsCategoryList);
  public void updateShow(Category cmsCategory);

  public void delBySiteId(String siteId);

    public void updateList(@Param("cmsCategoryList")List<Category> cmsCategoryList);

    @FindListByTenant
    public Category getByIdAndType(@Param("siteId")String siteId, @Param("type")String type);

    public List<Dict> getProCategoryByActywId(String actywId);

    public Category findCategoryByTenantAndName(Category category);

    public void delByTenantId(String tenantId);
}