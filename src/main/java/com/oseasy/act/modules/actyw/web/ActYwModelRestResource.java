/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.rest
 * @Description [[_ActYwModelRestResource_]]文件
 * @date 2017年6月5日 下午3:46:50
 *
 */

package com.oseasy.act.modules.actyw.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.tool.process.vo.RtModel;
import com.oseasy.act.modules.actyw.tool.process.vo.RtSvl;
import com.oseasy.com.pcore.common.web.BaseController;



/**
 * 模型资源控制器.
 *
 * @author chenhao
 * @date 2017年6月5日 下午3:46:50
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/model/")
public class ActYwModelRestResource extends BaseController {
  protected static final Logger LOGGER = Logger.getLogger(ActYwModelRestResource.class);

  @Autowired
  private RepositoryService repositoryService;
  protected ObjectMapper objectMapper = new ObjectMapper();

  @RequestMapping(value = "form")
  public String create(Model model) {
    return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actywModelForm";
  }

  /**
   * 创建流程模型 .
   *
   * @author chenhao
   * @param category
   *          类目
   * @param name
   *          流程名称
   * @param key
   *          流程标识
   * @param description
   *          流程描述
   * @param jsonXml
   *          流程json
   * @param request
   *          HttpServletRequest
   * @param response
   *          HttpServletResponse
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public void create(String category, String name, String key, String description, String jsonXml, HttpServletRequest request, HttpServletResponse response) {
    try {
      RtModel rtModel = new RtModel(name, key, description, category, jsonXml, null);
      org.activiti.engine.repository.Model modelData = ActYwTool.genModelData(rtModel, repositoryService);
      repositoryService.saveModel(modelData);

      org.activiti.engine.repository.Model repModel = repositoryService.getModel(modelData.getId());
      jsonXml = "{\"resourceId\":\"4d32348b796e494299c37ddf342c5d5b\",\"properties\":{\"process_id\":\"process110\",\"name\":\"大赛流程110\",\"documentation\":\"\",\"process_author\":\"\",\"process_version\":\"\",\"process_namespace\":\"http://www.activiti.org/processdef\",\"executionlisteners\":\"{\\\"executionListeners\\\":\\\"[]\\\"}\",\"eventlisteners\":\"{\\\"eventListeners\\\":\\\"[]\\\"}\",\"signaldefinitions\":\"\\\"[]\\\"\",\"messagedefinitions\":\"\\\"[]\\\"\",\"messages\":[]},\"stencil\":{\"id\":\"BPMNDiagram\"},\"childShapes\":[{\"resourceId\":\"sid-E76260EF-151A-49A4-B235-825E54BAFF5B\",\"properties\":{\"overrideid\":\"sid-E76260EF-151A-49A4-B235-825E54BAFF5B\",\"name\":\"学生（项目负责人）\",\"documentation\":\"\",\"executionlisteners\":\"\",\"initiator\":\"\",\"formkeydefinition\":\"\",\"formproperties\":\"\"},\"stencil\":{\"id\":\"StartNoneEvent\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-FDE6ECD4-B683-434B-BC05-3BAF38B80B36\"}],\"bounds\":{\"lowerRight\":{\"x\":174.3333282470703,\"y\":115},\"upperLeft\":{\"x\":144.3333282470703,\"y\":85}},\"dockers\":[]},{\"resourceId\":\"sid-35EB7BDF-E1A5-44F9-B00B-5F975A497D46\",\"properties\":{\"overrideid\":\"sid-35EB7BDF-E1A5-44F9-B00B-5F975A497D46\",\"name\":\"学院审核（教学秘书）\",\"documentation\":\"\",\"asynchronousdefinition\":false,\"exclusivedefinition\":true,\"executionlisteners\":{\"executionListeners\":[]},\"multiinstance_type\":\"None\",\"multiinstance_cardinality\":\"\",\"multiinstance_collection\":\"\",\"multiinstance_variable\":\"\",\"multiinstance_condition\":\"\",\"isforcompensation\":\"false\",\"usertaskassignment\":\"\",\"formkeydefinition\":\"\",\"duedatedefinition\":\"\",\"prioritydefinition\":\"\",\"formproperties\":\"\",\"tasklisteners\":{\"taskListeners\":[]}},\"stencil\":{\"id\":\"UserTask\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-85C8FBB6-4298-4974-B2C6-F233D7543396\"}],\"bounds\":{\"lowerRight\":{\"x\":400,\"y\":140},\"upperLeft\":{\"x\":300,\"y\":60}},\"dockers\":[]},{\"resourceId\":\"sid-6D43F4CC-A9EE-46FB-B40D-5765FF4E1D17\",\"properties\":{\"overrideid\":\"sid-6D43F4CC-A9EE-46FB-B40D-5765FF4E1D17\",\"name\":\"审核结束（项目评级）\",\"documentation\":\"\",\"executionlisteners\":\"\"},\"stencil\":{\"id\":\"EndNoneEvent\"},\"childShapes\":[],\"outgoing\":[],\"bounds\":{\"lowerRight\":{\"x\":645.3333282470703,\"y\":328},\"upperLeft\":{\"x\":617.3333282470703,\"y\":300}},\"dockers\":[]},{\"resourceId\":\"sid-FDE6ECD4-B683-434B-BC05-3BAF38B80B36\",\"properties\":{\"overrideid\":\"sid-FDE6ECD4-B683-434B-BC05-3BAF38B80B36\",\"name\":\"提交审核\",\"documentation\":\"\",\"conditionsequenceflow\":\"\",\"executionlisteners\":\"\",\"defaultflow\":\"false\"},\"stencil\":{\"id\":\"SequenceFlow\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-35EB7BDF-E1A5-44F9-B00B-5F975A497D46\"}],\"bounds\":{\"lowerRight\":{\"x\":299.8437486886978,\"y\":100},\"upperLeft\":{\"x\":175.22916197776794,\"y\":100}},\"dockers\":[{\"x\":15,\"y\":15},{\"x\":50,\"y\":40}],\"target\":{\"resourceId\":\"sid-35EB7BDF-E1A5-44F9-B00B-5F975A497D46\"}},{\"resourceId\":\"sid-85C8FBB6-4298-4974-B2C6-F233D7543396\",\"properties\":{\"overrideid\":\"sid-85C8FBB6-4298-4974-B2C6-F233D7543396\",\"name\":\"提交给学校\",\"documentation\":\"\",\"conditionsequenceflow\":\"\",\"executionlisteners\":\"\",\"defaultflow\":\"false\",\"showdiamondmarker\":false},\"stencil\":{\"id\":\"SequenceFlow\"},\"childShapes\":[],\"outgoing\":[{\"resourceId\":\"sid-6D43F4CC-A9EE-46FB-B40D-5765FF4E1D17\"}],\"bounds\":{\"lowerRight\":{\"x\":618.9164207579244,\"y\":306.21570089208706},\"upperLeft\":{\"x\":400.5513526795757,\"y\":138.76476785791297}},\"dockers\":[{\"x\":50,\"y\":40},{\"x\":22.166671752929688,\"y\":22}],\"target\":{\"resourceId\":\"sid-6D43F4CC-A9EE-46FB-B40D-5765FF4E1D17\"}}],\"bounds\":{\"lowerRight\":{\"x\":1200,\"y\":1050},\"upperLeft\":{\"x\":0,\"y\":0}},\"stencilset\":{\"url\":\"stencilsets/bpmn2.0/bpmn2.0.json\",\"namespace\":\"http://b3mn.org/stencilset/bpmn2.0#\"},\"ssextensions\":[]}";
      rtModel.setJsonXml(jsonXml);
      repositoryService.addModelEditorSource(repModel.getId(), rtModel.getJsonXml().getBytes(RtSvl.RtModelVal.UTF_8));

      //dealSvgXml(repModel, rtModel);

      logger.error("build [" + rtModel.getName() + "] is success!!");
      PrintWriter pw = response.getWriter();

      pw.write("build [" + rtModel.getName() + "] is success!!");
      pw.flush();
      pw.close();
    } catch (Exception e) {
      logger.error("创建模型失败：", e);
    }
  }

  /**
   * 处理svgXml .
   *
   * @author chenhao
   * @param model
   *          模型参数
   * @throws TranscoderException
   *           转码异常
   * @throws IOException
   *           IO异常
   */
  private void dealSvgXml(org.activiti.engine.repository.Model model, RtModel rtModel)
      throws TranscoderException, IOException {
    // svgXml = "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:oryx=\"http://oryx-editor.org\"
    // id=\"sid-81686A21-A20D-48F2-A323-B2FA7EDE6D28\" width=\"695.3333282470703\" height=\"378\"
    // xmlns:xlink=\"http://www.w3.org/1999/xlink\"
    // xmlns:svg=\"http://www.w3.org/2000/svg\"><defs><marker
    // id=\"sid-68EBB65E-E7DE-4B7B-913F-B99134A4E1E2start\" refX=\"1\" refY=\"5\"
    // markerUnits=\"userSpaceOnUse\" markerWidth=\"17\" markerHeight=\"11\"
    // orient=\"auto\">undefined<path id=\"sid-68EBB65E-E7DE-4B7B-913F-B99134A4E1E2default\" d=\"M 5
    // 0 L 11 10\" fill=\"white\" stroke=\"#585858\" stroke-width=\"1\"
    // display=\"none\"/></marker><marker id=\"sid-68EBB65E-E7DE-4B7B-913F-B99134A4E1E2end\"
    // refX=\"15\" refY=\"6\" markerUnits=\"userSpaceOnUse\" markerWidth=\"15\" markerHeight=\"12\"
    // orient=\"auto\"><path id=\"sid-68EBB65E-E7DE-4B7B-913F-B99134A4E1E2arrowhead\" d=\"M 0 1 L 15
    // 6 L 0 11z\" fill=\"#585858\" stroke=\"#585858\" stroke-linejoin=\"round\"
    // stroke-width=\"2\"/></marker><marker id=\"sid-57A1B833-C40D-4A52-95A0-E6C6DBA607D8start\"
    // refX=\"1\" refY=\"5\" markerUnits=\"userSpaceOnUse\" markerWidth=\"17\" markerHeight=\"11\"
    // orient=\"auto\">undefined<path id=\"sid-57A1B833-C40D-4A52-95A0-E6C6DBA607D8default\" d=\"M 5
    // 0 L 11 10\" fill=\"white\" stroke=\"#585858\" stroke-width=\"1\"
    // display=\"none\"/></marker><marker id=\"sid-57A1B833-C40D-4A52-95A0-E6C6DBA607D8end\"
    // refX=\"15\" refY=\"6\" markerUnits=\"userSpaceOnUse\" markerWidth=\"15\" markerHeight=\"12\"
    // orient=\"auto\"><path id=\"sid-57A1B833-C40D-4A52-95A0-E6C6DBA607D8arrowhead\" d=\"M 0 1 L 15
    // 6 L 0 11z\" fill=\"#585858\" stroke=\"#585858\" stroke-linejoin=\"round\"
    // stroke-width=\"2\"/></marker></defs><svg id=\"underlay-container\"/><g stroke=\"none\"
    // font-family=\"Verdana, sans-serif\" font-size-adjust=\"none\" font-style=\"normal\"
    // font-variant=\"normal\" font-weight=\"normal\" line-heigth=\"normal\" font-size=\"12\"><g
    // class=\"stencils\"><g class=\"me\"/><g class=\"children\"><g
    // id=\"svg-sid-E76260EF-151A-49A4-B235-825E54BAFF5B\"><g class=\"stencils\"
    // transform=\"translate(144.3333282470703, 85)\"><g class=\"me\"><g pointer-events=\"fill\"
    // id=\"sid-C0C7F141-1024-4B85-91E3-862EE950DD23\" title=\"开始事件\"><circle
    // id=\"sid-C0C7F141-1024-4B85-91E3-862EE950DD23bg_frame\" cx=\"15\" cy=\"15\" r=\"15\"
    // stroke=\"#585858\" fill=\"#ffffff\" stroke-width=\"1\"/><text font-size=\"11\"
    // id=\"sid-C0C7F141-1024-4B85-91E3-862EE950DD23text_name\" x=\"15\" y=\"32\" oryx:align=\"top
    // center\" stroke=\"#373e48\" stroke-width=\"0pt\" letter-spacing=\"-0.01px\"
    // transform=\"rotate(0 15 32)\" oryx:fontSize=\"11\" text-anchor=\"middle\"><tspan dy=\"11\"
    // x=\"15\" y=\"32\">学生（项目负责人）</tspan></text></g></g><g class=\"children\"
    // style=\"overflow:hidden\"/><g class=\"edge\"/></g><g class=\"controls\"><g
    // class=\"dockers\"/><g class=\"magnets\" transform=\"translate(144.3333282470703, 85)\"><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(7, 7)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g></g></g></g><g
    // id=\"svg-sid-35EB7BDF-E1A5-44F9-B00B-5F975A497D46\"><g class=\"stencils\"
    // transform=\"translate(300, 60)\"><g class=\"me\"><g pointer-events=\"fill\"
    // oryx:minimumSize=\"50 40\" id=\"sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8C\"
    // title=\"用户任务\"><rect id=\"sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8Ctext_frame\"
    // oryx:anchors=\"bottom top right left\" x=\"1\" y=\"1\" width=\"94\" height=\"79\" rx=\"10\"
    // ry=\"10\" stroke=\"none\" stroke-width=\"0\" fill=\"none\"/><rect
    // id=\"sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8Cbg_frame\" oryx:resize=\"vertical horizontal\"
    // x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" stroke=\"#bbbbbb\"
    // stroke-width=\"1\" fill=\"#f9f9f9\"/><text font-size=\"12\"
    // id=\"sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8Ctext_name\" x=\"50\" y=\"40\"
    // oryx:align=\"middle center\" oryx:fittoelem=\"text_frame\" stroke=\"#373e48\"
    // stroke-width=\"0pt\" letter-spacing=\"-0.01px\" transform=\"rotate(0 50 40)\"
    // oryx:fontSize=\"12\" text-anchor=\"middle\"><tspan x=\"50\" y=\"40\"
    // dy=\"-1\">学院审核（教学</tspan><tspan x=\"50\" y=\"40\" dy=\"11\">秘书）</tspan></text><g
    // id=\"sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8CuserTask\" transform=\"translate(3,3)\"><path
    // oryx:anchors=\"top left\" style=\"fill:#d1b575;stroke:none;\" d=\"m 1,17 16,0 0,-1.7778
    // -5.333332,-3.5555 0,-1.7778 c 1.244444,0 1.244444,-2.3111 1.244444,-2.3111 l 0,-3.0222 C
    // 12.555557,0.8221 9.0000001,1.0001 9.0000001,1.0001 c 0,0 -3.5555556,-0.178 -3.9111111,3.5555
    // l 0,3.0222 c 0,0 0,2.3111 1.2444443,2.3111 l 0,1.7778 L 1,15.2222 1,17 17,17\"
    // id=\"sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8C_sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8C_17\"/></g><g
    // id=\"sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8Cparallel\" display=\"none\"><path
    // oryx:anchors=\"bottom\" fill=\"none\" stroke=\"#bbbbbb\" d=\"M46 70 v8 M50 70 v8 M54 70 v8\"
    // stroke-width=\"2\"
    // id=\"sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8C_sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8C_18\"/></g><g
    // id=\"sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8Csequential\" display=\"none\"><path
    // oryx:anchors=\"bottom\" fill=\"none\" stroke=\"#bbbbbb\" stroke-width=\"2\"
    // d=\"M46,76h10M46,72h10 M46,68h10\"
    // id=\"sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8C_sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8C_19\"/></g><g
    // id=\"sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8Ccompensation\" display=\"none\"><path
    // oryx:anchors=\"bottom\" fill=\"none\" stroke=\"#bbbbbb\" d=\"M 62 74 L 66 70 L 66 78 L 62 74
    // L 62 70 L 58 74 L 62 78 L 62 74\" stroke-width=\"1\"
    // id=\"sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8C_sid-33899822-5D9A-4171-AF9A-D10E0F1D2F8C_20\"/></g></g></g><g
    // class=\"children\" style=\"overflow:hidden\"/><g class=\"edge\"/></g><g class=\"controls\"><g
    // class=\"dockers\"/><g class=\"magnets\" transform=\"translate(300, 60)\"><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(-7, 12)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(-7, 32)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(-7, 52)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(17, 71)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(42, 71)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(67, 71)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(91, 12)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(91, 32)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(91, 52)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(17, -7)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(42, -7)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(67, -7)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(42, 32)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g></g></g></g><g
    // id=\"svg-sid-6D43F4CC-A9EE-46FB-B40D-5765FF4E1D17\"><g class=\"stencils\"
    // transform=\"translate(617.3333282470703, 300)\"><g class=\"me\"><g pointer-events=\"fill\"
    // id=\"sid-7E18C7F0-F1AF-433D-BC58-F7C7912FF150\" title=\"结束事件\"><circle
    // id=\"sid-7E18C7F0-F1AF-433D-BC58-F7C7912FF150bg_frame\" cx=\"14\" cy=\"14\" r=\"14\"
    // stroke=\"#585858\" fill=\"#ffffff\" stroke-width=\"3\"/><text font-size=\"11\"
    // id=\"sid-7E18C7F0-F1AF-433D-BC58-F7C7912FF150text_name\" x=\"14\" y=\"30\" oryx:align=\"top
    // center\" stroke=\"#373e48\" stroke-width=\"0pt\" letter-spacing=\"-0.01px\"
    // transform=\"rotate(0 14 30)\" oryx:fontSize=\"11\" text-anchor=\"middle\"><tspan dy=\"11\"
    // x=\"14\" y=\"30\">审核结束（项目评级）</tspan></text></g></g><g class=\"children\"
    // style=\"overflow:hidden\"/><g class=\"edge\"/></g><g class=\"controls\"><g
    // class=\"dockers\"/><g class=\"magnets\" transform=\"translate(617.3333282470703, 300)\"><g
    // pointer-events=\"all\" display=\"none\" transform=\"translate(6, 6)\"><circle cx=\"8\"
    // cy=\"8\" r=\"4\" stroke=\"none\" fill=\"red\" fill-opacity=\"0.3\"/></g></g></g></g></g><g
    // class=\"edge\"><g id=\"svg-sid-FDE6ECD4-B683-434B-BC05-3BAF38B80B36\"><g
    // class=\"stencils\"><g class=\"me\"><g pointer-events=\"painted\"><path
    // id=\"sid-68EBB65E-E7DE-4B7B-913F-B99134A4E1E2_1\" d=\"M175.22916197776794
    // 100L299.8437486886978 100 \" stroke=\"#585858\" fill=\"none\" stroke-width=\"2\"
    // stroke-linecap=\"round\" stroke-linejoin=\"round\"
    // marker-start=\"url(#sid-68EBB65E-E7DE-4B7B-913F-B99134A4E1E2start)\"
    // marker-end=\"url(#sid-68EBB65E-E7DE-4B7B-913F-B99134A4E1E2end)\"/></g><text
    // id=\"sid-68EBB65E-E7DE-4B7B-913F-B99134A4E1E2text_name\" x=\"183\" y=\"92\"
    // oryx:edgePosition=\"startTop\" stroke-width=\"0pt\" letter-spacing=\"-0.01px\"
    // transform=\"rotate(360 175 100)\" oryx:fontSize=\"12\" text-anchor=\"start\"><tspan dy=\"0\"
    // x=\"183\" y=\"92\">提交审核</tspan></text></g><g class=\"children\" style=\"overflow:hidden\"/><g
    // class=\"edge\"/></g><g class=\"controls\"><g class=\"dockers\"/><g
    // class=\"magnets\"/></g></g><g id=\"svg-sid-85C8FBB6-4298-4974-B2C6-F233D7543396\"><g
    // class=\"stencils\"><g class=\"me\"><g pointer-events=\"painted\"><path
    // id=\"sid-57A1B833-C40D-4A52-95A0-E6C6DBA607D8_1\" d=\"M400.5513526795757
    // 138.76476785791297L618.9164207579244 306.21570089208706 \" stroke=\"#585858\" fill=\"none\"
    // stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"
    // marker-start=\"url(#sid-57A1B833-C40D-4A52-95A0-E6C6DBA607D8start)\"
    // marker-end=\"url(#sid-57A1B833-C40D-4A52-95A0-E6C6DBA607D8end)\"/></g><text
    // id=\"sid-57A1B833-C40D-4A52-95A0-E6C6DBA607D8text_name\" x=\"408\" y=\"130\"
    // oryx:edgePosition=\"startTop\" stroke-width=\"0pt\" letter-spacing=\"-0.01px\"
    // transform=\"rotate(37.48241142771849 400 138)\" oryx:fontSize=\"12\"
    // text-anchor=\"start\"><tspan dy=\"0\" x=\"408\" y=\"130\">提交给学校</tspan></text></g><g
    // class=\"children\" style=\"overflow:hidden\"/><g class=\"edge\"/></g><g class=\"controls\"><g
    // class=\"dockers\"/><g class=\"magnets\"/></g></g></g></g><g class=\"svgcontainer\"><g
    // display=\"none\"><rect x=\"0\" y=\"0\" stroke-width=\"1\" stroke=\"#777777\" fill=\"none\"
    // stroke-dasharray=\"2,2\" pointer-events=\"none\"/></g><g display=\"none\"><path
    // stroke-width=\"1\" stroke=\"silver\" fill=\"none\" stroke-dasharray=\"5,5\"
    // pointer-events=\"none\"/></g><g display=\"none\"><path stroke-width=\"1\" stroke=\"silver\"
    // fill=\"none\" stroke-dasharray=\"5,5\" pointer-events=\"none\"/></g><g/></g></g></svg>";

    InputStream svgStream = new ByteArrayInputStream(
        rtModel.getSvgXml().getBytes(RtSvl.RtModelVal.UTF_8));
    TranscoderInput input = new TranscoderInput(svgStream);

    PNGTranscoder transcoder = new PNGTranscoder();
    // Setup output
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    TranscoderOutput output = new TranscoderOutput(outStream);

    // Do the transformation
    transcoder.transcode(input, output);
    final byte[] result = outStream.toByteArray();
    repositoryService.addModelEditorSourceExtra(model.getId(), result);
    outStream.close();
  }


  /**
   * 获取流程模型 .
   *
   * @author chenhao
   * @param modelId 模型ID
   */
  @RequestMapping(value = "/{modelId}/json", method = RequestMethod.GET, produces = "application/json")
  public ObjectNode getEditorJson(@PathVariable String modelId) {
    ObjectNode modelNode = null;

    org.activiti.engine.repository.Model model = repositoryService.getModel(modelId);

    if (model != null) {
      try {
        if (StringUtils.isNotEmpty(model.getMetaInfo())) {
          modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
        } else {
          modelNode = objectMapper.createObjectNode();
          modelNode.put(RtSvl.RtModelVal.M_NAME, model.getName());
        }
        modelNode.put(RtSvl.RtModelVal.M_ID, model.getId());
        ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(new String(
            repositoryService.getModelEditorSource(model.getId()), RtSvl.RtModelVal.UTF_8));
        modelNode.put(RtSvl.RtModelVal.M, editorJsonNode);

      } catch (Exception e) {
        LOGGER.error("Error creating model JSON", e);
        throw new ActivitiException("Error creating model JSON", e);
      }
    }
    return modelNode;
  }

  /**
   * 更新流程模型 .
   *
   * @author chenhao
   * @param modelId 模型ID
   * @param values 模型参数
   */
  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  @RequestMapping(value = "/{modelId}/save", method = RequestMethod.PUT)
  public void save(@PathVariable String modelId,
      @RequestBody MultiValueMap<String, String> values) {
    try {

      org.activiti.engine.repository.Model model = repositoryService.getModel(modelId);

      ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());

      modelJson.put(RtSvl.RtModelVal.M_NAME, values.getFirst(RtSvl.RtModelVal.M_NAME));
      modelJson.put(RtSvl.RtModelVal.M_DESCRIPTION,
          values.getFirst(RtSvl.RtModelVal.M_DESCRIPTION));
      model.setMetaInfo(modelJson.toString());
      model.setName(values.getFirst(RtSvl.RtModelVal.M_NAME));

      repositoryService.saveModel(model);

      repositoryService.addModelEditorSource(model.getId(),
          values.getFirst(RtSvl.RtModelVal.JSON_XML).getBytes(RtSvl.RtModelVal.UTF_8));

      InputStream svgStream = new ByteArrayInputStream(
          values.getFirst(RtSvl.RtModelVal.SVG_XML).getBytes(RtSvl.RtModelVal.UTF_8));
      TranscoderInput input = new TranscoderInput(svgStream);

      PNGTranscoder transcoder = new PNGTranscoder();
      // Setup output
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      TranscoderOutput output = new TranscoderOutput(outStream);

      // Do the transformation
      transcoder.transcode(input, output);
      final byte[] result = outStream.toByteArray();
      repositoryService.addModelEditorSourceExtra(model.getId(), result);
      outStream.close();

    } catch (Exception e) {
      LOGGER.error("Error saving model", e);
      throw new ActivitiException("Error saving model", e);
    }
  }
}
