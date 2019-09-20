<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/sco/frontCreditModule.css">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/project/form.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
    <script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/frontCyjd/uploaderFile.js"></script>

</head>
<body>


<div class="container container-ct">

    <h4 class="main-title">大学生创新创业训练计划中期报告</h4>
    <form:form id="competitionfm" modelAttribute="proModelMd" action="${ctxFront}/proprojectmd/proModelMd/midSubmit" method="post" class="form-horizontal"
                   enctype="multipart/form-data">

        <div class="contest-content">
            <div class="tool-bar">
                <div class="inner">
                    <span>项目编号：${proModel.competitionNumber}</span>
                    <span style="margin-left: 15px;">填表日期:</span>
                    <i>${sysdate}</i>
                    <span style="margin-left: 15px">申请人:</span>
                    <i>${sse.name}</i>
                </div>
            </div>
            <h4 class="contest-title">项目中期报告</h4>
            <div class="contest-wrap">
                <div class="row">
                    <div class="col-xs-4">
                        <div class="form-group">
                            <label class="control-label">项目负责人：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.name}</p>
                                <!-- 下载模板必须属性 -->
                                <input type="hidden" id="proNtype" value="2"/>
                                <input type="hidden" id="proId" value="${proModel.id}"/>
                                <input type="hidden" id="proCate" value="${proModel.proCategory}"/>

                                <input type="hidden" name="id" id="proModelMdId" value="${proModelMd.id}"/>
                                <input type="hidden" name="proModel.declareId" id="declareId" value="${sse.id}"/>
                               <%--<input type="hidden" name="modelId" id="modelId" value="${proModelMd.modelId}"/>--%>
                               <input type="hidden" name="proModel.year" id="proModel.year" value="${proModel.year}"/>
                                <input type="hidden" name="proModel.id" id="proModelId" value="${proModelMd.modelId}"/>
                                <input type="hidden" name="proModel.actYwId" id="proModel.actYwId" value="${actYw.id}"/>
                                <input type="hidden" name="proModel.type" id="proModel.type" value="${proProject.type}"/>
                                <input type="hidden" name="proModel.proType" id="proModel.proType" value="${proProject.proType}"/>
                                <input type="hidden" name="college" id="college" value="${sse.office.name}"/>
                                <input type="hidden" name="gnodeId" id="gnodeId" value="${gnodeId}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-4">
                        <div class="form-group">
                            <label class="control-label">学院：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.office.name}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-4">
                        <div class="form-group">
                            <label class="control-label">联系电话：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.mobile}</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>项目基本信息</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">项目名称：</label>
                            <div class="input-box">
                                <p class="form-control-static">${proModel.pName}</p>
                            </div>
                        </div>
                    </div>
                   <%-- <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">项目层次：</label>
                            <div class="input-box">
                                    &lt;%&ndash;是否需要用treeselect&ndash;%&gt;
                                <p class="form-control-static">1层</p>
                            </div>
                        </div>
                    </div>--%>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>团队信息</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="table-condition">
                    <div class="form-group">
                        <label class="control-label">团队信息：</label>
                        <div class="input-box" style="max-width: 394px;">
                            <p class="form-control-static">${team.name}</p>
                        </div>
                    </div>
                </div>
                <div class="table-title">
                    <span>学生团队</span>
                    <span class="ratio"></span>
                </div>
                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                    <thead>
                    <tr id="studentTr">
                        <th>序号</th>
                        <th>姓名</th>
                        <th>学号</th>
                        <th>手机号</th>
                        <th>所在学院</th>

                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${teamStu!=null&&teamStu.size() > 0}">
                        <c:forEach items="${teamStu}" var="item" varStatus="status">
                            <tr>
                                <td>${status.index+1}</td>
                                <td><c:out value="${item.name}"/></td>
                                <td><c:out value="${item.no}"/></td>
                                <td><c:out value="${item.mobile}"/></td>
                                <td><c:out value="${item.org_name}"/></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
                <div class="table-title">
                    <span>指导教师</span>
                </div>
                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <th>工号</th>
                        <th>导师来源</th>
                        <th>职称（职务）</th>
                        <th>学历</th>
                        <th>联系电话</th>
                        <th width="175">E-mail</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${teamTea!=null&&teamTea.size() > 0}">
                        <c:forEach items="${teamTea}" var="item" varStatus="status">
                            <tr>
                                <td>${status.index+1}</td>
                                <td><c:out value="${item.name}"/></td>
                                <td><c:out value="${item.no}"/></td>
                                <td><c:out value="${item.teacherType}"/></td>
                                <td><c:out value="${item.technical_title}"/></td>
                                <td><c:out value="${fns:getDictLabel(item.education,'enducation_level','')}"/></td>
                                <td><c:out value="${item.mobile}"/></td>
                                <td><c:out value="${item.email}"/></td>
                            </tr>
                        </c:forEach>
                      </c:if>
                    </tbody>
                </table>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>已取得阶段性成果</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="result-textarea-box">
                    <textarea placeholder="不少于300字" name="stageResult" minLength="300" class="form-control" rows="5"></textarea>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>项目资料</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <div class="form-group">
                            <label class="control-label">下载项目资料：</label>
                            <div class="input-box">
                                <div id="downFileUl" class="accessories-container">
                                    <div class="accessories accessories-h34">
                                        <div class="accessory">
                                            <div class="accessory-info">
                                                <a class="accessory-file" href="javascript: void(0);">
                                                    <img src="${ctxImg}/filetype/word.png">
                                                    <span class="accessory-name"></span>
                                                    <i title="" class="btn-downfile-newaccessory"></i></a>

                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>上传项目资料：</label>
                            <div id="accessories" class="input-box">
                                <%--<sys:frontFileUpload  fileInfoPrefix="proModel." className="accessories-h34" filepath="scoapply"></sys:frontFileUpload>--%>
                                <sys:frontFileUpload  fileInfoPrefix="proModel." className="accessories-h34" filepath="scoapply" tip1="支持上传word、pdf、excel、txt、rar、ppt图片类型等文件"></sys:frontFileUpload>

                            </div>
                        </div>
                    </div>
                </div>

                <div class="btngroup">
                    <%--<div id="btnUpload" class="btn btn-primary">上传中期检查资料</div>--%>
                    <%--<button type="button" class="btn btn-primary-oe">保存</button>--%>
                    <button type="button" data-control="uploader" class="btn btn-primary" onclick="midSubmit(this)">提交</button>
                    <button type="button" data-control="uploader" onclick="history.go(-1)" class="btn btn-default">返回</button>
                </div>
            </div>
        </div>
        <div id="fujianpp" style="display:none">
           <%-- <c:forEach items="${sysAttachments}" var="sysAttachment">
                <input type='hidden' name='proModel.attachMentEntity.fielSize' value='${sysAttachment.size}'/>
                <input type='hidden' name='proModel.attachMentEntity.fielTitle' value='${sysAttachment.name}'/>
                <input type='hidden' name='proModel.attachMentEntity.fielType' value='${sysAttachment.suffix}'/>
                <input type='hidden' name='proModel.attachMentEntity.fielFtpUrl' value='${sysAttachment.url}'/>
            </c:forEach>--%>
        </div>
    </form:form>
</div>
<div id="dialog-message" title="信息">
    <p id="dialog-content"></p>
</div>
<script>
    window.wtypes = '${wtypes}';
    window.wprefix = '${wprefix}';
</script>
<script src="${ctxJs}/md/downloadTplMd.js?v=1111" type="text/javascript" charset="utf-8"></script>
<script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
</body>
</html>
