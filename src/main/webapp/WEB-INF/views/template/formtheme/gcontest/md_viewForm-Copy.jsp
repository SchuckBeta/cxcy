<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>--%>
<!DOCTYPE html>
<html>
<head>
    <title>${fns:getFrontTitle()}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <script src="${ctxJs}/frontCyjd/uploaderFile.js"></script>


</head>

<body>


<div class="container container-ct">
    <div class="row-apply">
        <div class="row row-top-bar">
            <c:set var="sse" value="${fns:getUserById(proModel.declareId)}"/>
            <div class="col-xs-4">
                <p class="text-center topbar-item">项目编号：${proModel.competitionNumber}</p>
            </div>
            <div class="col-xs-4">
                <p class="text-center topbar-item">填报日期：<fmt:formatDate value='${proModel.createDate}'
                                                                        pattern='yyyy-MM-dd'/></p>
            </div>
            <div class="col-xs-4">
                <p class="text-center topbar-item"> 申报人：${sse.name}</p>
            </div>
        </div>
        <h4 class="titlebar">项目申报表</h4>
        <div class="contest-wrap form-horizontal form-horizontal-apply">
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">项目负责人：</label>
                    <p class="form-control-static">${sse.name}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">学号：</label>
                    <p class="form-control-static">${sse.no}</p>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">性别：</label>
                    <p class="form-control-static">${fns:getDictLabel(sse.sex, 'sex', '')}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">学院：</label>
                    <p class="form-control-static">${sse.office.name}</p>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">专业年级：</label>
                    <p class="form-control-static">${fns:getProfessional(sse.professional)}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">联系电话：</label>
                    <p class="form-control-static">${sse.mobile}</p>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">E-mail：</label>
                    <p class="form-control-static">${sse.email}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">身份证号：</label>
                    <p class="form-control-static">${sse.idNumber}</p>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>项目基本信息</span>
                    <i class="line"></i>
                    <a data-toggle="collapse" href="#applicatioinDetail"><i
                            class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>
            <div class="form-horizontal form-horizontal-apply mgb40">
                <div id="applicatioinDetail" class="panel-body collapse in">
                    <div class="row row-user-info">
                        <div class="col-xs-6">
                            <label class="label-static">参赛项目名称：</label>
                            <p class="form-control-static" style="word-break: break-all">${proModel.pName}</p>
                        </div>
                        <div class="col-xs-6">
                            <label class="label-static">大赛类别：</label>
                            <p class="form-control-static">${fns:getDictLabel(proModel.proCategory, "competition_net_type","" )}</p>
                        </div>
                        <div class="col-xs-6">
                            <label class="label-static">大赛组别：</label>
                            <p class="form-control-static">${fns:getDictLabel(proModel.level, "gcontest_level","" )}</p>
                        </div>
                        <div class="col-xs-12">
                            <label class="label-static">项目简介：</label>
                            <p class="form-control-static">${proModel.introduction}</p>
                        </div>

                        <div class="col-xs-12">
                            <label class="label-static">附件：</label>
                            <div class="form-control-static">
                                <sys:frontFileUpload className="accessories-h30" fileitems="${sysAttachments}"
                                                     readonly="true"></sys:frontFileUpload>

                                <%--<ul id="accessoryListPdf" class="file-list">--%>
                                <%--<c:forEach items="${sysAttachments}" var="sysAttachment">--%>
                                    <%--<li class="file-item">--%>
                                        <%--<div class="file-info">--%>
                                            <%--<img src="${ctxImg}/filetype/word.png">--%>
                                            <%--<a href="javascript:void(0);" data-url="${sysAttachment.url}"--%>
                                             <%--data-original="" data-size="" data-id="${sysAttachment.id}"--%>
                                             <%--data-title="${sysAttachment.name}"--%>
                                             <%--data-type=""--%>
                                             <%--data-ftp-url="${sysAttachment.url}">${sysAttachment.name}</a>--%>

                                        <%--</div>--%>
                                    <%--</li>--%>
                                <%--</c:forEach>--%>
                                <%--</ul>--%>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>团队信息</span>
                        <i class="line"></i>
                        <a data-toggle="collapse" href="#projectDetail"><i
                                class="icon-collaspe icon-double-angle-up"></i></a>
                    </div>
                </div>
                <div class="table-condition form-horizontal form-horizontal-apply">
                    <div id="projectDetail" class="panel-body collapse in">
                        <div class="row row-user-info">
                            <div class="col-xs-8">
                                <label class="label-static">团队名称：</label>
                                <p class="form-control-static">${team.name}</p>
                            </div>
                        </div>
                        <div class="table-title">
                            <span>学生团队</span>
                            <span class="ratio" id="ratio"></span>
                        </div>
                        <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                            <thead>
                            <tr id="studentTr">
                                <th>序号</th>
                                <th>姓名</th>
                                <th>工号</th>
                                <th>学院</th>
                                <th>专业</th>
                                <th>手机号</th>
                                <th>现状</th>
                                <th>当前在研</th>
                                <th>技术领域</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${teamStu!=null&&teamStu.size() > 0}">
                                <c:forEach items="${teamStu}" var="item" varStatus="status">
                                    <tr>
                                        <td>${status.index+1 }</td>
                                        <td>${item.name }</td>
                                        <td>${item.no }</td>
                                        <td>${item.org_name }</td>
                                        <td>${item.professional}</td>
                                        <td>
                                                ${item.mobile }
                                        </td>
                                        <td>${fns:getDictLabel(item.currState, 'current_sate', '')}</td>
                                        <td>${item.curJoin }</td>
                                        <td>${item.domain }</td>
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
                                <th>单位(学院或企业、机构)</th>
                                <th>导师来源</th>
                                <th>当前指导</th>
                                <th>技术领域</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${teamTea!=null&&teamTea.size() > 0}">
                                <c:forEach items="${teamTea}" var="item" varStatus="status">
                                    <tr>
                                        <td>${status.index+1 }</td>
                                        <td>${item.name }</td>
                                        <td>${item.no }</td>
                                        <td>${item.org_name }</td>
                                        <td>${item.teacherType}</td>
                                        <td>${item.curJoin }</td>
                                        <td>${item.domain }</td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!--   审核记录  -->
                <c:if test="${not empty actYwAuditInfos}">

                    <div class="edit-bar edit-bar-sm clearfix">
                        <div class="edit-bar-left">
                            <span>审核记录</span> <i class="line"></i><a data-toggle="collapse" href="#actYwAuditInfos"><i
                                class="icon-collaspe icon-double-angle-up"></i></a>
                        </div>
                    </div>

                    <div id="actYwAuditInfos" class="panel-body collapse in">
                        <div class="panel-inner">
                            <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                <thead>
                                <tr>
                                    <th>审核动作</th>
                                    <th>审核时间</th>
                                    <th>审核人</th>
                                    <th>审核结果</th>
                                    <th style="width:45%">建议及意见</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${actYwAuditInfos}" var="item">
                                    <c:choose>
                                        <c:when test="${not empty item.id}">
                                            <tr>
                                                <td>${item.auditName}</td>
                                                <td><fmt:formatDate value="${item.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                <td>${item.user.name}</td>
                                                <td>${item.result}</td>
                                                <td>${item.suggest}</td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="5" style="color: red; text-align: right;font-weight: bold">${item.auditName}：${item.result}</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>

                    </div>

                </c:if>
            </div>
        </div>
        <div class="text-center mgb20">
            <a class="btn btn-default" href="javascript:history.go(-1)">返回</a>
        </div>
    </div>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>


</body>


</html>
