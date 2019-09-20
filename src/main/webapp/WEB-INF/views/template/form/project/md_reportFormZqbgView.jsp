<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta name="decorator" content="cyjd-site-default"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/sco/frontCreditModule.css">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/project/form.css?v=1"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
    <script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
</head>
<body>


<div class="container container-ct">
    <h4 class="main-title">大学生创新创业训练计划中期报告</h4>
    <div class="form-horizontal">
        <div class="contest-content">
            <div class="tool-bar">
                <div class="inner">
                    <span>项目编号：${proModel.competitionNumber}</span>

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
                    <div class="col-xs-12">
                        <div class="form-group">
                            <label class="control-label">项目名称：</label>
                            <div class="input-box">
                                <p class="form-control-static">${proModel.pName}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">所属学科：</label>
                            <div class="input-box">
                                <p class="form-control-static">${fns:getDictLabel(proModelMd.subject, "0000000111","" )}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">项目类别：</label>
                            <div class="input-box">
                                <p class="form-control-static"> ${fns:getDictLabel(proModel.proCategory, "project_type","" )}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">申报级别：</label>
                            <div class="input-box">
                                <p class="form-control-static">${fns:getDictLabel(proModelMd.appLevel, "0000000196","" )}</p>
                            </div>
                        </div>
                    </div>

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
                <div class="result-textarea-box" style="padding: 15px;">
                    ${proModelMd.stageResult}
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>中期报告附件</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="col-xs-12">
                   <div class="form-group">
                       <label class="control-label"><i class="icon-require">*</i>上传申报资料：</label>
                       <div class="input-box">
                           <sys:frontFileUpload  fileitems="${sysAttachments}" fileInfoPrefix="proModel." className="accessories-h34" filepath="scoapply" readonly="true"></sys:frontFileUpload>
                       </div>
                   </div>
                </div>
                <div class="btngroup">
                    <button type="button"onclick="history.go(-1)"  class="btn btn-default-oe">返回</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
