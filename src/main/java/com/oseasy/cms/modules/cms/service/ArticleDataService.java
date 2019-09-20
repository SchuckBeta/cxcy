/**
 * 
 */
package com.oseasy.cms.modules.cms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.cms.modules.cms.dao.ArticleDataDao;
import com.oseasy.cms.modules.cms.entity.ArticleData;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 站点Service


 */
@Service
@Transactional(readOnly = true)
public class ArticleDataService extends CrudService<ArticleDataDao, ArticleData> {

}
