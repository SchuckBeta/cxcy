/**
 *
 */
package com.oseasy.act.modules.act.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

//import com.oseasy.annotation.Domain;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.vo.ActRstatus;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.ActYwResult;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.BaseService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程模型相关Controller


 */
@Service
@Transactional(readOnly = true)
public class ActModelService extends BaseService {

	@Autowired
	private RepositoryService repositoryService;

  @Autowired
  private ActYwService actYwService;

//	@Autowired
//	private ObjectMapper objectMapper;
	protected ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 流程模型列表
	 */
	public Page<org.activiti.engine.repository.Model> modelList(Page<org.activiti.engine.repository.Model> page, String category) {
		ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByLastUpdateTime().desc();

		if (StringUtils.isNotEmpty(category)) {
			modelQuery.modelCategory(category);
		}

    page.setCount(modelQuery.count());
    int firstResult = (page.getFirstResult() >= 0) ? page.getFirstResult() : 0;
    int maxResult = (int) ((page.getMaxResults() >= page.getCount()) ? page.getCount() : page.getMaxResults());
    page.setList(modelQuery.listPage(firstResult, maxResult));
    return page;
	}

	/**
	 * 创建模型
	 * @throws UnsupportedEncodingException
	 */
	@Transactional(readOnly = false)
	public Model create(String name, String key, String description, String category) throws UnsupportedEncodingException {

		ObjectNode editorNode = objectMapper.createObjectNode();
		editorNode.put("id", "canvas");
		editorNode.put("resourceId", "canvas");
		ObjectNode properties = objectMapper.createObjectNode();
		properties.put("process_author", "initiate");
		editorNode.put("properties", properties);
		ObjectNode stencilset = objectMapper.createObjectNode();
		stencilset.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		editorNode.put("stencilset", stencilset);

		Model modelData = repositoryService.newModel();
		description = StringUtils.defaultString(description);
		modelData.setKey(StringUtils.defaultString(key));
		modelData.setName(name);
		modelData.setCategory(category);
		modelData.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count()+1)));

		ObjectNode modelObjectNode = objectMapper.createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
		modelData.setMetaInfo(modelObjectNode.toString());

		repositoryService.saveModel(modelData);
		repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

		return modelData;
	}

	/**
	 * 根据Model部署流程.
	 * @param id 模型ID
	 * @param ywId 业务ID
	 */
	@Transactional(readOnly = false)
	public ActRstatus deploy(String id, Boolean isUpdateYw) {
    ActRstatus result = deploy(id);
    /**
     * 流程发布，流程ID回填到业务表.
     */
    if (!result.getStatus()) {
      return result;
    }

    if (isUpdateYw && (StringUtil.isNotEmpty(result.getKey()))) {
      List<ActYw> actYws = actYwService.getByKeyss(ActYw.pkeySplitKeyss(result.getKey()));
      if ((actYws != null) && (actYws.size() == 1)) {
        ActYw actYw = actYws.get(0);
        actYw.setFlowId(result.getId());
        actYw.setDeploymentId(result.getDeploymentId());
        actYwService.save(actYw);
      }else{
        result.setStatus(true);
        result.setMsg("业务不存在，流程部署执行回滚！");
      }
    }
    return result;
	}

  @Transactional(readOnly = false)
  public ActRstatus deploy(String id) {
	  ActRstatus result = new ActRstatus(false, "流程部署失败!");
		try {
			org.activiti.engine.repository.Model modelData = repositoryService.getModel(id);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

      result.setKey(modelData.getKey());

			String processName = modelData.getName();
			if (!StringUtils.endsWith(processName, ".bpmn20.xml")) {
				processName += ".bpmn20.xml";
			}
//			System.out.println("========="+processName+"============"+modelData.getName());
			ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
			Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
					.tenantId(TenantConfig.getCacheTenant())
					.addInputStream(processName, in).deploy();

			// 设置流程分类
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
      if ((list != null) && (list.size() > 0)) {
  			for (ProcessDefinition processDefinition : list) {
  				repositoryService.setProcessDefinitionCategory(processDefinition.getId(), modelData.getCategory());
  				result.setStatus(true);
  				result.setId(processDefinition.getId());
          result.setDeploymentId(deployment.getId());
  				result.setMsg("部署成功，流程ID=" + processDefinition.getId());
  			}
			}
		} catch (Exception e) {
      result.setMsg("设计模型图不正确，检查模型正确性，模型ID=" + id);
			throw new ActivitiException("设计模型图不正确，检查模型正确性，模型ID=" + id, e);
		}

		return result;
	}

	/**
	 * 导出model的xml文件
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public void export(String id, HttpServletResponse response) {
		try {
			org.activiti.engine.repository.Model modelData = repositoryService.getModel(id);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

			ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
			IOUtils.copy(in, response.getOutputStream());
			String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.flushBuffer();
		} catch (Exception e) {
			throw new ActivitiException("导出model的xml文件失败，模型ID="+id, e);
		}

	}

	/**
	 * 更新Model分类
	 */
	@Transactional(readOnly = false)
	public void updateCategory(String id, String category) {
		org.activiti.engine.repository.Model modelData = repositoryService.getModel(id);
		modelData.setCategory(category);
		repositoryService.saveModel(modelData);
	}

	/**
	 * 删除模型
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public void delete(String id) {
		repositoryService.deleteModel(id);
	}
}
