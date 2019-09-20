/**
 * 
 */
package com.oseasy.cms.modules.cms.dao;

import com.oseasy.cms.modules.cms.entity.Comment;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 评论DAO接口

 * @version 2013-8-23
 */
@MyBatisDao
public interface CommentDao extends CrudDao<Comment> {

}
