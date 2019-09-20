<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="cyjd-site-default"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
    <script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
    <title>${frontTitle}</title>
</head>
<body>


<div class="container container-ct">

    <h4 class="main-title">${gnode.name}</h4>
    <div class="row-apply">
        <div class="row row-top-bar">
            <%--<span>项目编号：${proModel.competitionNumber}</span>--%>
            <%--<span style="margin-left: 15px;">填报日期:</span>--%>
            <%--<i>${sysdate}</i>--%>
            <%--<span style="margin-left: 15px">申请人:</span>--%>
            <%--<i>${sse.name}</i>--%>
            <div class="col-xs-4">
                <p class="text-center topbar-item">项目编号：${proModel.competitionNumber}</p>
            </div>
            <div class="col-xs-4">
                <p class="text-center topbar-item">填报日期：${sysdate}</p>
            </div>
            <div class="col-xs-4">
                <p class="text-center topbar-item"> 申请人：${sse.name}</p>
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
                            <label class="label-static">项目名称：</label>
                            <p class="form-control-static" style="word-break: break-all">${proModel.pName}</p>
                        </div>
                        <div class="col-xs-6">
                            <label class="label-static">项目类别：</label>
                            <p class="form-control-static">${fns:getDictLabel(proModel.proCategory, "project_type","" )}</p>
                        </div>
                        <div class="col-xs-12">
                            <label class="label-static">项目简介：</label>
                            <p class="form-control-static">${proModel.introduction}</p>
                        </div>
                        <div class="col-xs-12">
                            <label class="label-static">项目logo：</label>
                            <div class="form-control-static">
                                <div class="project-logo-box">
                                    <img class="img-responsive"
                                         src="${empty proModel.logo.url ? '/images/upload-default-image1.png':fns:ftpImgUrl(proModel.logo.url)}">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>项目申报资料</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="row row-user-info">
                    <div class="col-xs-12">
                        <label class="label-static">申报资料：</label>
                        <div class="form-control-static" style="padding: 0">
                            <sys:frontFileUpload className="accessories-h34" fileitems="${applyFiles}"
                                                 readonly="true"></sys:frontFileUpload>
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
                                        <td>${item.mobile }</td>
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
                <form:form id="competitionfm" modelAttribute="proReport" action="${ctxFront}/proproject/reportSubmit"
                           method="post"
                           enctype="multipart/form-data">
                    <input type="hidden" name="gnodeId" value="${gnodeId}"/>
                    <input type="hidden" name="proModelId" value="${proModel.id}"/>
                    <input type="hidden" name="id" value="${proReport.id}"/>

                    <div class="edit-bar edit-bar-sm clearfix">
                        <div class="edit-bar-left">
                            <span><i style="color: red;margin-right: 4px;">*</i>已取得阶段性成果</span>
                            <i class="line"></i>
                        </div>
                    </div>
                    <textarea placeholder="最多2000字" name="stageResult" maxLength="2000"
                              class="form-control required" rows="5">${proReport.stageResult }</textarea>
                    <div class="edit-bar edit-bar-sm clearfix">
                        <div class="edit-bar-left">
                            <span>报告</span>
                            <i class="line"></i>
                        </div>
                    </div>
                    <div class="row row-user-info">
                        <div id="filesDiv" class="col-xs-12">
                            <label class="label-static"><i style="color: red;margin-right: 4px;">*</i>上传报告：</label>
                            <div class="form-control-static" style="padding: 0">
                                <sys:frontFileUpload className="accessories-h34"
                                                     fileitems="${proReport.files}"
                                                     filepath="project"></sys:frontFileUpload>
                            </div>
                        </div>
                    </div>
                </form:form>
                <div class="text-center">
                    <button type="button" class="btn btn-primary" onclick="reportSubmit(this)">提交</button>
                    <button type="button" onclick="history.go(-1)" class="btn btn-default">返回</button>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>
<script type="text/javascript">
    var competitionfm = $('#competitionfm').validate();
    function reportSubmit(obj) {
        if (!competitionfm.form()) {
            return false;
        }
        if (ckeckFiles()) {
            dialogCyjd.createDialog(0, '提交后不可修改，是否继续？', {
                dialogClass: 'dialog-cyjd-container none-close',
                buttons: [{
                    text: '继续',
                    'class': 'btn btn-sm btn-primary',
                    click: function () {
                        $(this).dialog('close');
                        $(obj).prop('disabled', true);
                        $(obj).prev().addClass('disabled').find('input[type="file"]').prop('disabled', true)
                        $("#competitionfm").ajaxSubmit(function (data) {
                            isLogin = checkIsToLogin(data);
                            if (isLogin) {
                                $(obj).prop('disabled', false);
                                $(obj).prev().removeClass('disabled').find('input[type="file"]').prop('disabled', false)
                                dialogTimeout();
                            } else {
                                if (data.ret == '1') {
                                    $('.icon-remove-sign').detach();
                                    dialogCyjd.createDialog(1, data.msg, {
                                        dialogClass: 'dialog-cyjd-container none-close',
                                        buttons: [{
                                            text: '确定',
                                            'class': 'btn btn-sm btn-primary',
                                            click: function () {
                                                $(this).dialog('close');
                                                top.location = "/f/project/projectDeclare/list";
                                            }
                                        }]
                                    });
                                } else {
                                    $(obj).prop('disabled', false);
                                    $(obj).prev().removeClass('disabled').find('input[type="file"]').prop('disabled', false);
                                    dialogCyjd.createDialog(0, data.msg, {
                                        dialogClass: 'dialog-cyjd-container none-close',
                                        buttons: [{
                                            text: '确定',
                                            'class': 'btn btn-sm btn-primary',
                                            click: function () {
                                                $(this).dialog('close');
                                            }
                                        }]
                                    });
                                }
                            }
                        });
                    }
                }, {
                    text: '取消',
                    'class': 'btn btn-sm btn-primary',
                    click: function () {
                        $(this).dialog('close');
                    }
                }]
            });
        } else {
            dialogCyjd.createDialog(0, "请上传附件", {
                dialogClass: 'dialog-cyjd-container none-close',
                buttons: [{
                    text: '确定',
                    'class': 'btn btn-sm btn-primary',
                    click: function () {
                        $(this).dialog('close');
                    }
                }]
            });
        }
    }
    function ckeckFiles() {
        var newfiles = $("#filesDiv").find("input[name='attachMentEntity.fielFtpUrl']");
        if (newfiles.length > 0) {
            return true;
        } else {
            var oldfiles = $("#filesDiv").find("a[class='accessory-file']");
            if (oldfiles.length > 0) {
                return true;
            } else {
                return false;
            }
        }
    }
    function checkIsToLogin(data) {
        return typeof data == 'string' && data.indexOf("id=\"imFrontLoginPage\"") > -1;
    }
    function dialogTimeout() {
        dialogCyjd.createDialog(0, "未登录或登录超时。请重新登录，谢谢！", {
            dialogClass: 'dialog-cyjd-container none-close',
            buttons: [{
                text: '确定',
                'class': 'btn btn-sm btn-primary',
                click: function () {
                    $(this).dialog('close');
                    top.location = top.location;
                }
            }]
        });
    }


    $(function () {
        $('.file-info .btn-downfile-newaccessory').click(function () {
            location.href = "/f/proproject/downTemplate?type=close";
        });
    })


</script>
</body>
</html>
