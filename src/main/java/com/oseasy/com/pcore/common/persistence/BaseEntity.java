/**
 *
 */

package com.oseasy.com.pcore.common.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.supcan.treelist.SupTreeList;
import com.oseasy.com.pcore.common.supcan.treelist.cols.SupCol;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Entity支持类.
 *
 * @version 2014-05-16
 *
 */
@SupTreeList
public abstract class BaseEntity<T> implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TENANT_ID = "tenantId";

  /**
   * 实体编号（唯一标识）.
   */
  protected String id;

  /**
   * 当前用户.
   */
  protected User currentUser;

  /**
   * 当前实体分页对象.
   */
  protected Page<T> page;

  /**
   * 自定义SQL（SQL标识，SQL内容）.
   */
  private Map<String, String> sqlMap;

  /**
   * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入.
   */
  protected boolean isNewRecord = false;
  protected Boolean useCorpModel; //多租户属性配置开关

  public List<String> qfilters;       // 查询Ids过滤属性
  public String tenantId;  //多租户id
  private String domianURL;
  public BaseEntity() {

  }

  public BaseEntity(String id) {
    this();
    this.id = id;
  }

  public List<String> getQfilters() {
    return qfilters;
  }

  public void setQfilters(List<String> qfilters) {
    this.qfilters = qfilters;
  }

  public String getDomianURL() {
    return domianURL;
  }

  public void setDomianURL(String domianURL) {
    this.domianURL = domianURL;
  }

  @SupCol(isUnique = "true", isHide = "true")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  public Boolean getUseCorpModel() {
    //return CoreSval.getTenantIsopen();
    return true;
  }

  public void setUseCorpModel(Boolean useCorpModel) {
    this.useCorpModel = useCorpModel;
  }
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }
  /**
   * 获取当前用户.
   *
   * @author chenhao
   * @return User 用户
   */
  @JsonIgnore
  @XmlTransient
  public User getCurrentUser() {
    if (currentUser == null||StringUtil.isEmpty(currentUser.getId())) {
      currentUser = CoreUtils.getUser();
    }
    return currentUser;
  }

  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
  }

  /**
   * 获取分页对象.
   *
   * @author chenhao
   * @return page 分页对象
   */
  @JsonIgnore
  @XmlTransient
  public Page<T> getPage() {
    if (page == null) {
      page = new Page<T>();
    }
    return page;
  }

  public Page<T> setPage(Page<T> page) {
    this.page = page;
    return page;
  }

  /**
   * 获取SqlMap参数.
   *
   * @author chenhao
   * @return sqlMap 参数
   */
  @JsonIgnore
  @XmlTransient
  public Map<String, String> getSqlMap() {
    if (sqlMap == null) {
      sqlMap = Maps.newHashMap();
    }
    return sqlMap;
  }

  public void setSqlMap(Map<String, String> sqlMap) {
    this.sqlMap = sqlMap;
  }

  /**
   * 插入之前执行方法，子类实现.
   */
  public abstract void preInsert();

  /**
   * 更新之前执行方法，子类实现.
   */
  public abstract void preUpdate();

  /**
   * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入.
   *
   * @return boolean 是否新纪录
   */
  public boolean getIsNewRecord() {
    return isNewRecord || StringUtil.isBlank(getId());
  }

  /**
   * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入.
   */
  public void setIsNewRecord(boolean isNewRecord) {
    this.isNewRecord = isNewRecord;
  }

  /**
   * 全局变量对象.
   */
  @JsonIgnore
  public CoreSval getGlobal() {
    return CoreSval.getInstance();
  }

  /**
   * 获取数据库名称.
   */
  @JsonIgnore
  public String getDbName() {
    return CoreSval.getDbName();
  }

  @Override
  public boolean equals(Object obj) {
    if (null == obj) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (!getClass().equals(obj.getClass())) {
      return false;
    }
    BaseEntity<?> that = (BaseEntity<?>) obj;
    return null == this.getId() ? false : this.getId().equals(that.getId());
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

  /**
   * 删除标记（0：正常；1：删除；2：审核；）.
   */
  public static final String DEL_FLAG_NORMAL = "0";
  public static final String DEL_FLAG_DELETE = "1";
  public static final String DEL_FLAG_AUDIT = "2";

}
