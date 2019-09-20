<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/gContestForm.css?v=1">
    <link rel="stylesheet" href="${ctxStatic}/webuploader/webuploader.css">
    <script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/frontCyjd/uploaderFile.js"></script>

</head>
<body>
<div class="container container-ct">
    <ol class="breadcrumb" style="margin-top: 0;">
        <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
        <li><a href="${ctxFront}/html-sctzsj">双创大赛</a></li>
        <li class="active">${projectName}</li>
    </ol>
    <input type="hidden" id="pageType" value="edit">
    <h2 class="text-center mgb20">${projectName}申请表</h2>
    <form:form id="competitionfm" class="form-horizontal" modelAttribute="proModel" autocomplete="off"
               action="${ctxFront}/promodel/proModel/submit"
               method="post" enctype="multipart/form-data">
        <input type="hidden" name="proMark" value="${proModel.proMark}"/>
        <div class="contest-content">
            <div class="tool-bar">
                <a class="btn-print" onClick="window.print()" href="javascript:void(0);">打印申报表</a>
                <div class="inner">
                    <c:if test="${id!=null}">
                        <span>大赛编号：</span>
                        <i>${competitionNumber}</i>
                    </c:if>
                    <input type="hidden" name="competitionNumber" value="${competitionNumber}"/>
                    <span>填表日期:</span>
                    <i>${sysdate}</i>
                </div>
            </div>
            <h4 class="contest-title">大赛报名</h4>
            <div class="contest-wrap">
                <div class="row">
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">申报人：</label>
                            <div class="input-box">
                                <p class="form-control-static">${cuser.name}</p>
                                <input type="hidden" name="declareId" id="declareId" value="${cuser.id}"/>
                                <input type="hidden" name="actYwId" id="actYwId" value="${actYw.id}"/>
                                <input type="hidden" name="id" id="id" value="${proModel.id}"/>
                                <input type="hidden" name="type" id="type" value="${proModel.type}"/>
                                <form:hidden path="year"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">学院：</label>
                            <div class="input-box">
                                <p class="form-control-static">${cuser.office.name}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">专业年级：</label>
                            <div class="input-box">
                                <p class="form-control-static">${fns:getProfessional(cuser.professional)}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">联系电话：</label>
                            <div class="input-box">
                                <p class="form-control-static">${cuser.mobile}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">E-mail：</label>
                            <div class="input-box">
                                <p class="form-control-static">${cuser.email}</p>
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
                    <div class="col-xs-12">
                        <div class="form-group">
                            <label for="projectName" class="control-label"><i>*</i>参赛项目名称：</label>
                            <div class="input-box">
                                <input type="text" name="pName" id="projectName" class="form-control" maxlength='30'
                                       value="${proModel.pName}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <c:if test="${not empty proCategoryMap}">
                        <div class="col-xs-6">
                            <div class="form-group">
                                <flow:flowTypeSelect name="大赛类别" divClass="input-box" type="proCategory"
                                                     typeList="${proCategoryMap}"></flow:flowTypeSelect>
                            </div>
                        </div>
                    </c:if>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label for="level" class="control-label"><i class="icon-require">*</i>参赛组别：</label>
                            <div class="input-box">
                                <form:select path="level" required="required" class="form-control">
                                    <form:option value="" label="请选择"/>
                                    <form:options items="${fns:getDictList('gcontest_level')}" itemLabel="label"
                                                  itemValue="value" htmlEscape="false"/>
                                </form:select>
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
                        <label class="control-label"><i class="icon-require">*</i>团队信息：</label>
                        <div class="input-box" style="max-width: 394px;">
                                <%--<form:select required="required" onchange="findTeamPerson()" path="teamId"--%>
                                <%--class="form-control"><form:option value="" label="--请选择--"/>--%>
                                <%--<form:options items="${teams}" itemValue="id" itemLabel="name" htmlEscape="false"/>--%>
                                <%--</form:select>--%>

                            <form:select onchange="findTeamPerson()"
                                         path="teamId"
                                         class="form-control has-disabled required proModelTeamId">
                                <form:option value="" label="--请选择--"/>
                                <form:options items="${teams}" itemValue="id" itemLabel="name" htmlEscape="false"/>
                            </form:select>
                        </div>
                    </div>
                </div>
                <div class="table-title">
                    <span>学生团队</span>
                    <span class="ratio"></span>
                </div>
                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover table-team studenttb">
                    <thead>
                    <tr id="studentTr">
                        <th>序号</th>
                        <th>姓名</th>
                        <th>学号</th>
                        <th>手机号</th>
                        <th>所在学院</th>
                            <%--  <th>项目分工</th>--%>
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
                                <input type="hidden" name="proModel.teamUserHistoryList[${status.index}].userId"
                                       value="${item.userId}">
                            </tr>
                        </c:forEach>
                    </c:if>

                    </tbody>
                </table>
                <div class="table-title">
                    <span>指导教师</span>
                </div>
                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover table-team teachertb">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <th>工号</th>
                        <th>导师来源</th>
                        <th>职称（职务）</th>
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
                        <span>项目介绍</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="form-group">
                    <label for="introduction" class="control-label"><i>*</i>项目介绍：</label>
                    <div class="input-box">
                        <textarea id="introduction" class="introarea form-control" rows="5" name="introduction"
                                  maxlength='1024'>${proModel.introduction}</textarea>
                    </div>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>附     件</span>
                        <i class="line"></i>
                    </div>
                </div>
                <c:if test="${gnodeId!=null && gnodeId!=''}">
                    <input type="hidden" name="gnodeId" value="${gnodeId}"/>
                </c:if>
                <sys:frontFileUpload fileitems="${sysAttachments}" filepath="promodel"
                                     tip1="支持上传word、pdf、excel、txt、rar、ppt图片类型等文件"></sys:frontFileUpload>

                <div class="text-center mgb20">
                    <button data-control="uploader" type="submit" class="btn btn-primary">提交并保存</button>
                    <a href="javascript:void(0)" class="btn btn-default" onClick="history.back(-1)">返回</a>
                </div>
            </div>
        </div>


    </form:form>

</div>
<script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxJs}/md/findteamGc.js" type="text/javascript" charset="utf-8"></script>

<script>
    $(function () {
        findTeamPerson();
        onGcontestApplyInit($("[id='actYwId']").val(), $("[id='id']").val(), null);

        var customizeContest = {
            $customizeContestForm: $('#competitionfm'),
            init: function () {
                this.customizeContestFormValidate();
            },
            getBtnSubmit: function () {
                return this.$customizeContestForm.find('button[type="submit"]')
            },
            customizeContestFormValidate: function () {
                var self = this;
                var $btnSubmit = this.getBtnSubmit();
                return this.$customizeContestForm.validate({
                    rules: {
                        pName: {
                            required: true,
                            remote: {
                                url: "/f/promodel/proModel/checkProName",     //后台处理程序
                                type: "post",               //数据发送方式
                                dataType: "json",           //接受数据格式
                                data: {                     //要传递的数据
                                    actYwId: function() {
                                        return $("[id='actYwId']").val();
                                    },
                                    id: function () {
                                        return $("[id='id']").val();
                                    }
                                }
                            }
                        },
                        introduction: {
                            required: true
                        }
                    },
                    messages: {
                        pName: {
                            required: '请填写参赛项目名称',
                            remote: '项目名称已经存在'
                        },
                        introduction: {
                            required: '请填写项目介绍'
                        }
                    },
                    submitHandler: function (form) {
                        $btnSubmit.prop('disabled', true).text('提交中...');
                        self.$customizeContestForm.ajaxSubmit(function (data) {
                            if (checkIsToLogin(data)) {
                                loginOuted();
                                $btnSubmit.prop('disabled', false).text('提交并保存');
                                return false;
                            }
                            dialogCyjd.createDialog(data.ret, data.msg, {
                                buttons: [{
                                    'text': '确定',
                                    'class': 'btn btn-sm btn-primary',
                                    'click': function () {
                                        $(this).dialog('close');
                                        if (data.ret == 1) {
                                            $btnSubmit.text('提交成功')
                                            location.href = '/f/gcontest/gContest/list';
                                        } else {
                                            $btnSubmit.prop('disabled', false).text('提交并保存');
                                        }
                                    }
                                }]
                            })
                        })
                        return false;
                    }
                })
            }
        }
        customizeContest.init();


    });
</script>
</body>
</html>