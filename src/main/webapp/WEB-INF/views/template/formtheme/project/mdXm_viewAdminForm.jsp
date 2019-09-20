<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backCommon.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/fileList.css">
    <script src="${ctxJs}/md/download.js" type="text/javascript" charset="utf-8"></script>
    <title>${frontTitle}</title>
    <style type="text/css">
        .controls-static {
            margin-bottom: 0;
            line-height: 30px;
            color: #333;
        }

        .form-horizontal-apply .control-label {
            width: 100px;
        }

        .form-horizontal-apply .controls {
            margin-left: 100px;
        }

        .apply-form-container{
            padding: 0 15px;
            border: 1px solid #efefef;
            border-radius: 5px;
        }
        .apply-form-container .tool-bar{
            margin: 6px 0;
            white-space: nowrap;
        }
        .apply-form-container .tool-bar span{
            margin-left: 15px;
        }
        .apply-form-container .tool-bar span:first-child{
            margin-left: 0;
        }
        .apply-form-container .apply-form-title{
            line-height: 30px;
            padding: 5px 0 5px 15px;
            color: #333;
            font-size: 16px;
            font-weight: 700;
            background-color: #f4e6d4;
            text-align: left;
            margin: 0 -15px 15px;
        }
        .row-table-title{
            display: inline-block;
            border-radius: 3px 3px 0 0;
            padding: 6px 12px;
            font-size: 14px;
            color: #656565;
            background-color: #ebebeb;
        }
        .btn-default-oe {
            color: #333;
            background-color: #fff;
            border-color: #ccc;
            background-image: none;
        }

        .btn-default-oe.focus, .btn-default-oe:focus {
            color: #333;
            background-color: #e6e6e6;
            border-color: #8c8c8c
        }

        .btn-default-oe:hover {
            color: #333;
            background-color: #e6e6e6;
            border-color: #adadad
        }

        .btn-default-oe.active, .btn-default-oe:active, .open>.dropdown-toggle.btn-default-oe
        {
            color: #333;
            background-color: #e6e6e6;
            border-color: #adadad
        }
        .row-btns{
            margin: 20px 0;
        }
    </style>

</head>
<body>
<div class="container-fluid" style="margin-bottom: 60px;">
    <div class="edit-bar edit-bar-tag clearfix">
        <div class="edit-bar-left">
            <span>项目查看</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <div class="form-horizontal form-horizontal-apply">
        <div class="apply-form-container">
            <div class="tool-bar">
                <c:set var="sse" value="${fns:getUserById(proModel.declareId)}" />
                <span>项目编号：${proModel.competitionNumber}</span>
                <span>填表日期：<fmt:formatDate value="${proModel.createDate}" pattern="yyyy-MM-dd"/></span>
                <span>申请人：${sse.name}</span>
            </div>
            <h3 class="apply-form-title">项目申报表</h3>
            <div class="row-fluid">
                <div class="span3">
                    <div class="control-group">
                        <label class="control-label">项目负责人：</label>
                        <div class="controls">
                            <p class="controls-static">${sse.name}</p>
                        </div>
                    </div>
                </div>
                <div class="span3">
                    <div class="control-group">
                        <label class="control-label">学号：</label>
                        <div class="controls">
                            <p class="controls-static">${sse.no}</p>
                        </div>
                    </div>
                </div>
                <div class="span3">
                    <div class="control-group">
                        <label class="control-label">性别：</label>
                        <div class="controls">
                            <p class="controls-static">${fns:getDictLabel(sse.sex, 'sex', '')}</p>
                        </div>
                    </div>
                </div>
                <div class="span3">
                    <div class="control-group">
                        <label class="control-label">民族：</label>
                        <div class="controls">
                            <p class="controls-static">${sse.national}</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span3">
                    <div class="control-group">
                        <label class="control-label">学院：</label>
                        <div class="controls">
                            <p class="controls-static">${sse.office.name}</p>
                        </div>
                    </div>
                </div>
                <div class="span3">
                    <div class="control-group">
                        <label class="control-label">专业年级：</label>
                        <div class="controls">
                            <p class="controls-static">${fns:getProfessional(sse.professional)}</p>
                        </div>
                    </div>
                </div>
                <div class="span3">
                    <div class="control-group">
                        <label class="control-label">联系电话：</label>
                        <div class="controls">
                            <p class="controls-static">${sse.mobile}</p>
                        </div>
                    </div>
                </div>
                <div class="span3">
                    <div class="control-group">
                        <label class="control-label">E-mail：</label>
                        <div class="controls">
                            <p class="controls-static">${sse.email}</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span3">
                    <div class="control-group">
                        <label class="control-label">身份证号：</label>
                        <div class="controls">
                            <p class="controls-static">${sse.idNumber}</p>
                        </div>
                    </div>
                </div>
                <div class="span3">
                    <div class="control-group">
                        <label class="control-label">QQ：</label>
                        <div class="controls">
                            <p class="controls-static">${sse.qq}</p>
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
            <div class="row-fluid">
                <div class="span4">
                    <div class="control-group">
                        <label class="control-label">项目名称：</label>
                        <div class="controls">
                            <p class="controls-static">${proModel.pName}</p>
                        </div>
                    </div>
                </div>
                <div class="span4">
                    <div class="control-group">
                        <label class="control-label">项目简称：</label>
                        <div class="controls">
                            <p class="controls-static">${proModel.shortName}</p>
                        </div>
                    </div>
                </div>
                <div class="span4">
                    <div class="control-group">
                        <label class="control-label">所属学科：</label>
                        <div class="controls">
                            <p class="controls-static">${fns:getDictLabel(proModelMd.course, "000111","" )}</p>
                        </div>
                    </div>
                </div>

            </div>
            <div class="row-fluid">
                <div class="span4">
                    <div class="control-group">
                        <label class="control-label">项目类别：</label>
                        <div class="controls">
                            <p class="controls-static">
                            ${fns:getDictLabel(proModel.proCategory, "project_type","" )}
                            </p>
                        </div>
                    </div>
                </div>

                <div class="span4">
                    <div class="control-group">
                        <label class="control-label">申报级别：</label>
                        <div class="controls">
                            <p class="controls-static">
                            ${fns:getDictLabel(proModelMd.appLevel, "0000000196","" )}
                            </p>
                        </div>
                    </div>
                </div>
                <div class="span4">
                    <div class="control-group">
                        <label class="control-label">申请金额：</label>
                        <div class="controls">
                            <p class="controls-static">${proModelMd.appAmount}</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span4">
                    <div class="control-group">
                        <label class="control-label">项目来源：</label>
                        <div class="controls">
                            <p class="controls-static">
                            ${fns:getDictLabel(proModelMd.proSource, "project_soure_type","" )}
                            </p>
                        </div>
                    </div>
                </div>
                <div class="span4">
                    <div class="control-group">
                        <label class="control-label">来源项目名称：</label>
                        <div class="input-box">
                            <p class="controls-static">${proModelMd.sourceProjectName}</p>
                        </div>
                    </div>
                </div>
                <div class="span4">
                    <div class="control-group">
                        <label class="control-label">来源项目类别：</label>
                        <div class="input-box">
                            <p class="controls-static">${fns:getDictLabel(proModelMd.sourceProjectType,'0000000138','')}</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span12">
                    <div class="control-group">
                        <label class="control-label">项目简介：</label>
                        <div class="input-box">
                            <p class="form-control-static" style="margin:5px 30px 0 100px">${proModel.introduction}</p>
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
            <div class="control-group">
                <label class="control-label">团队信息：</label>
                <div class="controls">
                    <p class="controls-static">${team.name}</p>
                </div>
            </div>
            <div class="row-table">
                <span class="row-table-title">学生团队</span>
                <table class="table table-bordered table-theme-default table-hover table-condensed">
                    <thead>
                    <tr>
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
	                            <td><c:out value="${item.orgName}"/></td>
	                        </tr>
	                    </c:forEach>
	                </c:if>
	                </tbody>
                </table>
            </div>
            <div class="row-table">
                <span class="row-table-title">导师团队</span>
                <table class="table table-bordered table-theme-default table-condensed">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <th>工号</th>
                        <th>导师来源</th>
                        <th>职称</th>
                        <th>学历</th>
                        <th>联系电话</th>
                        <th>E-mail</th>
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
                                <td><c:out value="${item.technicalTitle}"/></td>
                                <td><c:out value="${fns:getDictLabel(item.education,'enducation_level','')}"/></td>
                                <td><c:out value="${item.mobile}"/></td>
                                <td><c:out value="${item.email}"/></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>

                </table>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>项目申报资料</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="row-fj">
                <ul id="accessoryListPdf" class="file-list">
                    <c:forEach items="${sysAttachments}" var="sysAttachment">
                    <li class="file-item">
                        <div class="file-info">
                            <img src="${ctxImg}/filetype/word.png">
                            <a href="javascript:void(0);" data-url="${sysAttachment.url}"
                               data-original="" data-size="" data-id="${sysAttachment.id}"
                               data-title="${sysAttachment.name}"
                               data-type=""
                               data-ftp-url="${sysAttachment.url}">${sysAttachment.name}</a>

                        </div>
                    </li>
                    </c:forEach>
                </ul>
            </div>
            <div class="text-center row-btns">
                <button type="button" class="btn btn-default-oe" onclick="history.go(-1)">返回</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
