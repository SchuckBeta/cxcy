<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/gContestForm.css">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/competitionRegistration.css"/>
    <meta name="decorator" content="site-decorator"/>
    <title>${frontTitle}</title>
    <style>
        .competition-title {
            width: 100%;
            height: 40px;
            line-height: 40px;
            color: #656565;
            background-color: #f4e6d4;
            font-size: 16px;
            font-weight: bold;
            box-sizing: border-box;
            -moz-box-sizing: border-box;
            -webkit-box-sizing: border-box;
            padding-left: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <input type="hidden" id="pageType" value="edit">
    <h4>${projectName}</h4>
    <div class="form-horizontal">
        <div class="contest-content">
            <div class="tool-bar">
                <a class="btn-print" onClick="window.print()" href="javascript:void(0);">打印申报表</a>
                <div class="inner">
                    <c:if test="${id!=null}">
                        <span>大赛编号：</span>
                        <i>${proModel.competitionNumber}</i>
                    </c:if>
                    <span>填表日期:</span>
                    <i><fmt:formatDate value="${proModel.subTime}" pattern="yyyy-MM-dd"/></i>
                </div>
            </div>
            <h4 class="contest-title">大赛报名</h4>
            <div class="contest-wrap">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">申报人：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.name}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">学院：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.office.name}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">专业年级：</label>
                            <div class="input-box">
                                <p class="form-control-static">${fns:getProfessional(sse.professional)}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">联系电话：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.mobile}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">E-mail：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.email}</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>大赛基本信息</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">参赛项目名称：</label>
                            <div class="input-box">
                                <p class="form-control-static">${proModel.pName}</p>
                            </div>
                        </div>
                    </div>
                    <c:if test="${not empty proCategoryMap}">
                        <div class="col-md-6">
                            <div class="form-group">
                                    <%--<flow:flowTypeSelect name="项目类型" divClass="input-box" type="type"--%>
                                    <%--typeList="${proCategoryMap}"></flow:flowTypeSelect>--%>
                                <label class="control-label">大赛类别：</label>
                                <div class="input-box">
                                    <p class="form-control-static">
                                            ${fns:getDictLabel(proModel.proCategory, 'competition_net_type',"" )}</p>
                                </div>
                            </div>
                        </div>

                    </c:if>
                    <c:if test="${not empty prolevelMap}">
                        <div class="col-md-6">
                            <div class="form-group">
                                    <%--<flow:flowTypeSelect name="参赛组别" divClass="input-box" type="level"--%>
                                    <%--typeList="${prolevelMap}"></flow:flowTypeSelect>--%>
                                <label class="control-label">参赛级别：</label>
                                <div class="input-box">
                                    <p class="form-control-static">
                                            ${fns:getDictLabel(proModel.level, 'competition_format',"" )}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">融资情况：</label>
                            <div class="input-box">
                                <%--<form:select id="financingStat" path="financingStat" required="required"--%>
                                <%--class="form-control">--%>
                                <%--<form:option value="" label="请选择"/>--%>
                                <%--<form:options items="${fns:getDictList('financing_stat')}" itemLabel="label"--%>
                                <%--itemValue="value" htmlEscape="false"/>--%>
                                <%--</form:select>--%>
                                <p class="form-control-static">
                                    ${fns:getDictLabel(proModel.financingStat, 'financing_stat',"" )}
                                </p>
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
                        <div class="input-box">
                            <%--<form:select required="required" onchange="findTeamPerson()" path="teamId"
                                         class="form-control"><form:option value="" label="--请选择--"/>
                                <form:options items="${teams}" itemValue="id" itemLabel="name" htmlEscape="false"/>
                            </form:select>--%>
                            <p class="form-control-static">${team.name}</p>
                        </div>
                    </div>
                </div>
                <div class="table-title">
                    <span>学生团队</span>
                    <span id="ratio" style="background-color: #fff;color: #df4526;"></span>
                </div>
                <table class="table table-bordered table-team studenttb">
                    <thead>
                    <tr id="studentTr">
                        <th>序号</th>
                        <th>姓名</th>
                        <th>学号</th>
                        <th>学院</th>
                        <th>专业</th>
                        <th>技术领域</th>
                        <th>联系电话</th>
                        <th>在读学位</th>
                        <th class='credit-ratio'>学分配比</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${teamStu!=null && teamStu.size() > 0}">
                        <c:forEach items="${teamStu}" var="item" varStatus="status">
                            <tr>
                                <td>${status.index+1}</td>
                                <td><c:out value="${item.name}"/></td>
                                <td><c:out value="${item.no}"/></td>
                                <td><c:out value="${item.org_name}"/></td>
                                <td><c:out value="${item.professional}"/></td>
                                <td><c:out value="${item.domain}"/></td>
                                <td><c:out value="${item.mobile}"/></td>
                                <td><c:out value="${item.instudy}"/></td>
                                <td class="credit-ratio">
                                        ${item.weightVal}
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
                <div class="table-title">
                    <span>指导教师</span>
                </div>
                <table class="table table-bordered table-team teachertb">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <th>单位（学院或企业、机构）</th>
                        <th>职称（职务）</th>
                        <th>技术领域</th>
                        <th>联系电话</th>
                        <th>E-mail</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${teamTea!=null&& teamTea.size() > 0}">
                        <c:forEach items="${teamTea}" var="item" varStatus="status">
                            <tr>
                                <td>${status.index+1}</td>
                                <td><c:out value="${item.name}"/></td>
                                <td><c:out value="${item.org_name}"/></td>
                                <td><c:out value="${item.technical_title}"/></td>
                                <td><c:out value="${item.domain}"/></td>
                                <td><c:out value="${item.mobile}"/></td>
                                <td><c:out value="${item.email}"/></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>

                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>项目介绍</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">项目介绍：</label>
                    <div class="input-box">
                        <p class="form-control-static">${proModel.introduction}</p>
                    </div>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>附     件</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div id="fujian" class="accessory-box">
                    <sys:frontFileUploadCommon fileitems="${sysAttachments}" filepath="promodel"
                                               btnid="btnUpload" readonly="true"></sys:frontFileUploadCommon>
                </div>
                <div class="btn-tool-bar">
                    <button type="button" class="btn btn-default" onclick="history.back(-1)">返回</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>