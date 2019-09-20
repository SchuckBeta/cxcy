/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_RtProperties_]]文件
 * @date 2017年6月2日 下午2:12:13
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.json.JsonAliUtils;

/**
 * 流程BPMNDi属性.
 *
 * @author chenhao
 * @date 2017年6月2日 下午2:12:13
 *
 */
public class RtProperties {
    private static Logger logger = LoggerFactory.getLogger(RtProperties.class);
  /**
   * process_id : state_project_audit name : 国创项目审核流程 documentation : process_author : zhangzheng
   * process_version : process_namespace : http://www.activiti.org/processdef executionlisteners :
   * eventlisteners : signaldefinitions : messagedefinitions : conditionsequenceflow : ${pass==1}
   */

  private String process_id;
  private String name;
  private String documentation;
  private String process_author;
  private String process_version;
  private String process_namespace;
  private String executionlisteners;
  private String eventlisteners;
  private String signaldefinitions;
  private String messagedefinitions;
  private String messages;

  public RtProperties() {
    super();
}

public RtProperties(String process_id, String name, String documentation, String process_author, String process_version,
        String process_namespace, String executionlisteners, String eventlisteners, String signaldefinitions,
        String messagedefinitions,
        String messages) {
    super();
    this.process_id = process_id;
    this.name = name;
    this.documentation = documentation;
    this.process_author = process_author;
    this.process_version = process_version;
    this.process_namespace = process_namespace;
    this.executionlisteners = executionlisteners;
    this.eventlisteners = eventlisteners;
    this.signaldefinitions = signaldefinitions;
    this.messagedefinitions = messagedefinitions;
    this.messages = messages;
}

public String getProcess_id() {
    return process_id;
  }

  public void setProcess_id(String process_id) {
    this.process_id = process_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDocumentation() {
    return documentation;
  }

  public void setDocumentation(String documentation) {
    this.documentation = documentation;
  }

  public String getProcess_author() {
    return process_author;
  }

  public void setProcess_author(String process_author) {
    this.process_author = process_author;
  }

  public String getProcess_version() {
    return process_version;
  }

  public void setProcess_version(String process_version) {
    this.process_version = process_version;
  }

  public String getProcess_namespace() {
    return process_namespace;
  }

  public void setProcess_namespace(String process_namespace) {
    this.process_namespace = process_namespace;
  }

  public String getExecutionlisteners() {
    return executionlisteners;
  }

  public void setExecutionlisteners(String executionlisteners) {
    this.executionlisteners = executionlisteners;
  }

  public String getEventlisteners() {
    return eventlisteners;
  }

  public void setEventlisteners(String eventlisteners) {
    this.eventlisteners = eventlisteners;
  }

  public String getSignaldefinitions() {
    return signaldefinitions;
  }

  public void setSignaldefinitions(String signaldefinitions) {
    this.signaldefinitions = signaldefinitions;
  }

  public String getMessagedefinitions() {
    return messagedefinitions;
  }

  public void setMessagedefinitions(String messagedefinitions) {
    this.messagedefinitions = messagedefinitions;
  }

  public String getMessages() {
    return messages;
  }

    public void setMessages(String messages) {
        this.messages = messages;
    }

/**
   * 初始化结果集属性数据 .
   *
   * @author chenhao
   * @param rt
   *          需初始化对象
   * @param pcsId
   *          流程ID
   * @param name
   *          流程名称
   * @param documentation
   *          相关文档
   * @param pcsAuthor
   *          流程作者
   * @param pcsVersion
   *          流程版本
   * @param pcsNamespace
   *          流程命名空间
   * @param executionlisteners
   *          执行监听
   * @param eventlisteners
   *          事件监听
   * @param signaldefinitions
   *          xx定义
   * @param messagedefinitions
   *          消息定义
   * @param messages
   *          消息
   * @return ActYwResult
   */
  public static RtProperties init(RtProperties rtp, String pcsId, String name,
      String pcsAuthor, String pcsVersion, String pcsNamespace, String documentation,
      String executionlisteners, String eventlisteners, String signaldefinitions,
      String messagedefinitions,
      String messages) {
      if(rtp == null){
          rtp = new RtProperties();
      }
    rtp.setProcess_id(pcsId);
    rtp.setName(name);
    rtp.setProcess_author(pcsAuthor);
    rtp.setProcess_version(pcsVersion);
    rtp.setProcess_namespace(pcsNamespace);

    if (pcsVersion == null) {
      rtp.setProcess_version(RtSvl.RtPropertiesVal.RT_PROCESS_VERSION);
    } else {
      rtp.setProcess_version(pcsVersion);
    }

    if (pcsNamespace == null) {
      rtp.setProcess_namespace(RtSvl.RtPropertiesVal.RT_PROCESS_NAMESPACE);
    } else {
      rtp.setProcess_namespace(pcsNamespace);
    }

    if (documentation == null) {
      rtp.setDocumentation(ActYwTool.FLOW_PROP_NULL);
    } else {
      rtp.setDocumentation(documentation);
    }

    if (executionlisteners == null) {
      rtp.setExecutionlisteners("{\"executionListeners\":\"[]\"}");
    } else {
      rtp.setExecutionlisteners(executionlisteners);
    }

    if (eventlisteners == null) {
      rtp.setEventlisteners("{\"eventlisteners\":\"[]\"}");
    } else {
      rtp.setEventlisteners(eventlisteners);
    }

    if (signaldefinitions == null) {
      rtp.setSignaldefinitions(ActYwTool.FLOW_PROP_LIST);
    } else {
      rtp.setSignaldefinitions(signaldefinitions);
    }

    if (messagedefinitions == null) {
      rtp.setMessagedefinitions(ActYwTool.FLOW_PROP_LIST);
    } else {
      rtp.setMessagedefinitions(messagedefinitions);
    }

    if (messages == null) {
        rtp.setMessages(ActYwTool.FLOW_PROP_LIST);
    } else {
        rtp.setMessages(messages);
    }
    return rtp;
  }

  public static RtProperties init(RtProperties rtp, ActYwGroup group, RtModel rtModel) {
      if(rtp == null){
          rtp = new RtProperties();
      }

      if(rtModel != null){
          if(StringUtil.isNotEmpty(rtModel.getKey())){
              rtp.setProcess_id(rtModel.getKey());
          }
          if(StringUtil.isNotEmpty(rtModel.getName())){
              rtp.setName(rtModel.getName());
          }
      }

      if(group != null){
          if(StringUtil.isNotEmpty(group.getAuthor())){
              rtp.setProcess_author(group.getAuthor());
          }

          if(StringUtil.isNotEmpty(group.getVersion())){
              rtp.setProcess_version(group.getVersion());
          }
          if(StringUtil.isNotEmpty(group.getRemarks())){
              rtp.setDocumentation(group.getRemarks());
          }
      }else{
          rtp.setProcess_version(RtSvl.RtPropertiesVal.RT_PROCESS_VERSION);
          rtp.setDocumentation(ActYwTool.FLOW_PROP_NULL);
      }

      rtp.setProcess_namespace(RtSvl.RtPropertiesVal.RT_PROCESS_NAMESPACE);
      rtp.setExecutionlisteners("{\"executionListeners\":\"[]\"}");
      rtp.setEventlisteners("{\"eventlisteners\":\"[]\"}");
      rtp.setSignaldefinitions(ActYwTool.FLOW_PROP_LIST);
      rtp.setMessagedefinitions(ActYwTool.FLOW_PROP_LIST);
      rtp.setMessages(ActYwTool.FLOW_PROP_LIST);
      return rtp;
  }

  /**
   * 初始化属性.
   * @param group 流程
   * @param rtModel 模型
   * @return RtProperties
   */
  public static RtProperties init(ActYwGroup group, RtModel rtModel) {
      RtProperties rtp = null;
      String file = FileUtil.getClassPath(ActYwTool.FLOW_JSON_PATH) + RtProperties.class.getSimpleName() + StringUtil.JSON;
      try {
          rtp = init(JsonAliUtils.readBeano(file, RtProperties.class), group, rtModel);
      } catch (Exception e) {
          logger.warn("RtProperties:文件处理失败，使用默认数据生成，路径：" + file);
          rtp = init(null, group, rtModel);
      }
      return rtp;
  }
}