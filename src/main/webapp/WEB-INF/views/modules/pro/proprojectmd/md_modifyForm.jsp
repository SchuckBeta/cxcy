<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>变更</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/state/titlebar.css">
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
    <style>
        .panel-inner-controls .control-label{
            width: 120px;
        }
        .panel-inner-controls .controls{
            margin-left: 140px;
        }
        .row-info-fluid .item-label{
            margin-right: 20px;
        }
        .minus {
            display: inline-block;
            width: 19px;
            height: 19px;
            background: url(/img/minuse.png) no-repeat;
        }

        .minus:hover {
            background: url(/img/minus2.png) no-repeat;
            cursor: pointer;
        }
    </style>
</head>

<body>
<div class="container-fluid container-fluid-audit">
    <div class="edit-bar edit-bar-tag clearfix">
        <div class="edit-bar-left">
            <span>变更</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <div class="form-wrap">
        <form:form id="inputForm" modelAttribute="proModelMd" action="${ctx}/proprojectmd/proModelMd/saveModify"
                   cssClass="form-horizontal form-container"
                   method="post">
            <form:hidden path="id"/>
            <form:hidden path="proModel.year"/>
            <div class="panel-body collapse in">
                <div class="row-fluid row-info-fluid">
                    <div class="span6">
                        <span class="item-label">项目编号：</span>${proModel.competitionNumber}
                    </div>
                    <div class="span6">
                        <span class="item-label">申报日期：</span><fmt:formatDate value="${proModel.subTime}"
                                                                             pattern="yyyy-MM-dd"/>
                    </div>
                </div>
                <div class="row-fluid row-info-fluid">
                    <div class="span6">
                        <span class="item-label">项目负责人：</span>${sse.name}
                    </div>
                    <div class="span6">
                        <span class="item-label">学号：</span>${sse.no}
                    </div>


                </div>
                <div class="row-fluid row-info-fluid">
                    <div class="span6">
                        <span class="item-label">性别：</span>${fns:getDictLabel(sse.sex, 'sex', '')}
                    </div>
                    <div class="span6">
                        <span class="item-label">民族：</span>${sse.national}
                    </div>


                </div>
                <div class="row-fluid row-info-fluid">
                    <div class="span6">
                        <span class="item-label">学院：</span>${sse.office.name}
                    </div>
                    <div class="span6">
                        <span class="item-label">专业：</span>${fns:getProfessional(sse.professional)}
                    </div>

                </div>
                <div class="row-fluid row-info-fluid">
                    <div class="span6">
                        <span class="item-label">联系电话：</span>${sse.mobile}
                    </div>
                    <div class="span6">
                        <span class="item-label">E-mail：</span>${sse.email}
                    </div>
                </div>
                <div class="row-fluid row-info-fluid">
                    <div class="span6">
                        <span class="item-label">身份证号：</span>${sse.idNumber}
                    </div>
                    <div class="span6">
                        <span class="item-label">QQ：</span>${sse.qq}
                    </div>
                </div>
            </div>

            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>项目基本信息</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="panel-body collapse in">
                <div class="panel-inner panel-inner-controls">
                    <div class="row-fluid">
                        <div class="span6">
                            <div class="control-group">
                                <label class="control-label"><i>*</i>参赛项目名称：</label>
                                <div class="controls">
                                    <form:input path="proModel.pName" maxlength="128" class="required"></form:input>
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label"><i>*</i>项目类别：</label>
                                <div class="controls">
                                    <form:select path="proModel.proCategory" class="required">
                                        <form:option value="" label="请选择"/>
                                        <form:options items="${proCategoryMap}" itemLabel="label"
                                                      itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label"><i>*</i>申请金额：</label>
                                <div class="controls">
                                    <form:input path="appAmount" maxlength="20" class="required"></form:input>
                                </div>
                            </div>
                            <div class="control-group proSourceChangeShow">
                                <label class="control-label"><i>*</i>来源项目名称：</label>
                                <div class="controls">
                                    <form:input path="sourceProjectName" maxlength="256"
                                                class="required "></form:input>
                                </div>
                            </div>
                        </div>
                        <div class="span6">
                            <div class="control-group">
                                <label class="control-label"><i>*</i>所属学科：</label>
                                <div class="controls">
                                    <form:select path="course" class="required">
                                        <form:option value="" label="请选择"/>
                                        <form:options items="${fns:getDictList('000111')}" itemLabel="label"
                                                      itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label"><i>*</i>申报级别：</label>
                                <div class="controls">
                                    <form:select path="appLevel" class="required">
                                        <form:option value="" label="请选择"/>
                                        <form:options items="${fns:getDictList('0000000196')}" itemLabel="label"
                                                      itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label"><i>*</i>项目来源：</label>
                                <div class="controls">
                                    <form:select path="proSource" class="required" onchange="souceChange();">
                                        <form:option value="" label="请选择"/>
                                        <form:options items="${fns:getDictList('project_soure_type')}" itemLabel="label"
                                                      itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                </div>
                            </div>
                            <div class="control-group proSourceChangeShow">
                                <label class="control-label"><i>*</i>来源项目类别：</label>
                                <div class="controls">
                                    <form:select path="sourceProjectType" class="required ">
                                        <form:option value="" label="请选择"/>
                                        <form:options items="${fns:getDictList('0000000138')}" itemLabel="label"
                                                      itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>阶段成果</span> <i class="line"></i>
                </div>
            </div>
            <div class="panel-body collapse in">
                <div class="panel-inner panel-inner-controls">
                    <div class="control-group">
                        <label class="control-label">中期成果：</label>
                        <div class="controls">
                            <form:textarea path="stageResult" cssClass="input-xxlarge" rows="3"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">结项成果：</label>
                        <div class="controls">
                            <form:textarea path="result" cssClass="input-xxlarge" rows="3"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>团队信息</span> <i class="line"></i>
                </div>
            </div>
            <div class="panel-body collapse in">
                <div class="panel-inner">
                    <p>项目团队：${team.name}</p>
                    <div class="table-caption">学生团队</div>
                    <table class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap studenttb">
                        <thead>
                        <tr>
                            <th>姓名</th>
                            <th>学号</th>
                            <th>手机号</th>
                            <th>所在学院</th>
                            <%--<th>当前在研</th>--%>
                            <th>职责</th>
                            <th><input type="button" class="btn btn-small btn-primary" userType="1" value="新增"
                                       onclick="selectUser(this)"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${teamStu!=null&&teamStu.size() > 0}">
                            <c:forEach items="${teamStu}" var="item" varStatus="status">
                                <tr userid="${item.userId}">
                                    <input type="hidden" class="custindex"
                                           name="proModel.studentList[${status.index}].userId" value="${item.userId}">
                                    <input type="hidden" class="custindex"
                                           name="proModel.studentList[${status.index}].userType" value="1">
                                    <td>${item.name}</td>
                                    <td>${item.no}</td>
                                    <td>${item.mobile}</td>
                                    <td>${item.org_name}</td>
                                    <%--<td>${item.curJoin}</td>--%>
                                    <td>
                                        <select class="zzsel custindex"
                                                name="proModel.studentList[${status.index}].userzz">
                                            <option value="0"
                                                    <c:if test="${item.userId==proModelMd.proModel.declareId }">selected</c:if> >
                                                负责人
                                            </option>
                                            <option value="1"
                                                    <c:if test="${item.userId!=proModelMd.proModel.declareId }">selected</c:if> >
                                                组成员
                                            </option>
                                        </select>
                                    </td>
                                    <td><a class="minus"></a></td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        </tbody>
                    </table>
                    <div class="table-caption">指导老师</div>
                    <table class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap teachertb">
                        <thead>
                        <tr>
                            <th>姓名</th>
                            <th>工号</th>
                            <th>导师来源</th>
                            <th>职称（职务）</th>
                            <th>学历</th>
                            <th>联系电话</th>
                            <%--<th>当前指导</th>--%>
                            <th><input type="button" class="btn btn-small btn-primary" userType="2" value="新增"
                                       onclick="selectUser(this)"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${teamTea!=null&&teamTea.size() > 0}">
                            <c:forEach items="${teamTea}" var="item" varStatus="status">
                                <tr userid="${item.userId}">
                                    <input type="hidden" name="proModel.teacherList[${status.index}].userId"
                                           value="${item.userId}">
                                    <input type="hidden" name="proModel.teacherList[${status.index}].userType" value="2">
                                    <td>${item.name}</td>
                                    <td>${item.no}</td>
                                    <td>${item.teacherType}</td>
                                    <td>${item.technical_title}</td>
                                    <td>${fns:getDictLabel(item.education,'enducation_level','')}</td>
                                    <td>${item.mobile}</td>
                                    <%--<td>${item.curJoin}</td>--%>
                                    <td><a class="minus"></a></td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>申报附件</span> <i class="line"></i>
                    <div class="btn-upload-file" style="margin-left: 0" id="appFileBtn">上传附件</div>
                </div>
            </div>
            <div class="accessory-box">
                <sys:adminFileUpload fileInfoPrefix="proModel." btnid="appFileBtn" fileitems="${appSysAttachments}"
                                     fileTypeEnum="10" fileStepEnum="2000" filepath="project"></sys:adminFileUpload>
            </div>

            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>中期检查附件</span> <i class="line"></i>
                    <div class="btn-upload-file" style="margin-left: 0" id="midFileBtn">上传附件</div>
                </div>
            </div>
            <div class="accessory-box">
                <sys:adminFileUpload fileInfoPrefix="proModel." btnid="midFileBtn" fileitems="${midSysAttachments}"
                                     fileTypeEnum="10" fileStepEnum="2200" filepath="project"></sys:adminFileUpload>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>结项审核附件</span>
                    <i class="line"></i>
                    <div class="btn-upload-file" style="margin-left: 0" id="closeFileBtn">上传附件</div>
                </div>
            </div>
            <div class="accessory-box">
                <sys:adminFileUpload fileInfoPrefix="proModel." btnid="closeFileBtn" fileitems="${closeSysAttachments}"
                                     fileTypeEnum="10" fileStepEnum="2300" filepath="project"></sys:adminFileUpload>
            </div>
            <div class="text-center mar_bottom">
                <input class="btn btn-primary" id="saveBtn" type="button" onclick="saveModify();" value="保存"/>
                <input class="btn btn-default" type="button" onclick="history.go(-1)" value="返回"/>
            </div>

        </form:form>
    </div>
</div>

<script type="text/javascript">
    var validate1;
    $(function () {
        souceChange();
        validate1 = $('#inputForm').validate({
            errorPlacement: function (error, element) {
                if ($(element).attr("type") == "checkbox" || $(element).attr("type") == "radio") {
                    error.appendTo($(element).parent().parent().parent());
                } else {
                    error.insertAfter(element);
                }
            }
        });
    });
    function souceChange() {
        var v = $("#proSource").val();
        if (v == 'B' || v == 'C') {
            $(".proSourceChangeShow").attr("style", "display:");
        } else {
            $(".proSourceChangeShow").attr("style", "display:none");
            $(".proSourceChangeVal").val("");
        }
    }
    $(document).on('click', '.minus', function (e) {
        var tb = $(this).parents("tbody");
        $(this).parents("tr").remove();
        resetNameIndex(tb);
    });
    $(document).on('change', '.zzsel', function (e) {
        if ($(this).val() == "0") {
            $(".zzsel").val("1");
            $(this).val("0");
        }
    });
    function saveModify() {
        var haveFzr = false;
        $(".zzsel").each(function (i, v) {
            if ($(v).val() == "0") {
                haveFzr = true;
                return;
            }
        });
        if (!haveFzr) {
            alertx("请选择负责人");
            return;
        }
        if (validate1.form()) {
            $("#saveBtn").removeAttr("onclick");
            $("#saveBtn").addClass("disabled");
            $("#inputForm").ajaxSubmit(function (data) {
                if (data.ret == "0") {
                    $("#saveBtn").attr("onclick", "saveModify();");
                    $("#saveBtn").removeClass("disabled");
                    alertx(data.msg);
                } else {
                    alertx(data.msg, function () {
                        history.go(-1);
                    });
                }
            });
        }
    }
    function selectUser(ob) {
        var c_btn = $(ob);
        var idsarr = [];
        c_btn.parents("table").find("tbody>tr").each(function (i, v) {
            idsarr.push($(v).attr("userid"));
        });
        var ids = idsarr.join(",");
        var userType = c_btn.attr("userType");
        var ititle = "选择学生";
        if (userType == '2') {
            ititle = "选择导师";
        }
        top.$.jBox.open('iframe:' + '/a/backUserSelect?ids=' + ids + '&userType=' + userType, ititle, 1100, 540, {
            buttons: {"确定": "ok", "关闭": true}, submit: function (v, h, f) {
                var temarr = [];
                $("input[name='boxTd']:checked", $(h.find("iframe")[0].contentDocument).find("iframe")[0].contentDocument)
                        .each(function (i, v) {
                            temarr.push($(v).val());
                        });
                if (v == "ok") {
                    addSelectUser(userType, temarr.join(","));
                    return true;
                }
            },
            loaded: function (h) {
                $(".jbox-content", top.document).css("overflow-y", "hidden");
            }
        });
    }
    function addSelectUser(usertype, ids) {
        if (ids != "") {
            if ("1" == usertype) {
                $.ajax({
                    type: "POST",
                    url: "/a/selectUser/getStudentInfo",
                    data: {ids: ids},
                    success: function (data) {
                        if (data) {
                            var datahtml = "";
                            var tpl = $("#tpl_st").html();
                            $.each(data, function (i, v) {
                                datahtml = datahtml + Mustache.render(tpl, {
                                            userId: v.userId,
                                            name: v.name,
                                            no: v.no,
                                            mobile: v.mobile,
                                            office: v.office,
                                            curJoin: v.curJoin
                                        });
                            });
                            $(".studenttb>tbody").append(datahtml);
                            resetNameIndex($(".studenttb>tbody"));
                        }
                    }
                });
            } else {
                $.ajax({
                    type: "POST",
                    url: "/a/selectUser/getTeaInfo",
                    data: {ids: ids},
                    success: function (data) {
                        if (data) {
                            var datahtml = "";
                            var tpl = $("#tpl_tea").html();
                            $.each(data, function (i, v) {
                                datahtml = datahtml + Mustache.render(tpl, {
                                            userId: v.userId,
                                            name: v.name,
                                            no: v.no,
                                            teacherType: v.teacherType,
                                            postTitle: v.postTitle,
                                            education: v.education,
                                            mobile: v.mobile,
                                            mobile: v.mobile,
                                            curJoin: v.curJoin
                                        });
                            });
                            $(".teachertb>tbody").append(datahtml);
                            resetNameIndex($(".teachertb>tbody"));
                        }
                    }
                });
            }
        }
    }
    function resetNameIndex(tbodyOb) {
        var indexNum = 0;
        var rex = "\\[(.+?)\\]";
        $(tbodyOb).find("tr").each(function (i, v) {
            $(v).find(".custindex").each(function (ti, tv) {
                var name = $(tv).attr("name");
                var indx = name.match(rex)[1];
                $(tv).attr("name", name.replace(indx, indexNum));
            });
            indexNum++;
        });
    }
</script>


<script type="text/template" id="tpl_st">
    <tr userid="{{userId}}">
        <input type="hidden" class="custindex" name="proModel.studentList[custindex].userId" value="{{userId}}">
        <input type="hidden" class="custindex" name="proModel.studentList[custindex].userType" value="1">
        <td>{{name}}</td>
        <td>{{no}}</td>
        <td>{{mobile}}</td>
        <td>{{office}}</td>
        <%--<td>{{curJoin}}</td>--%>
        <td>
            <select class="zzsel custindex" name="proModel.studentList[custindex].userzz">
                <option value="0" >负责人
                </option>
                <option value="1" selected >组成员
                </option>
            </select>
        </td>
        <td>
            <a class="minus"></a>
        </td>
    </tr>
</script>
<script type="text/template" id="tpl_tea">
    <tr userid="{{userId}}">
        <input type="hidden" class="custindex" name="proModel.teacherList[custindex].userId" value="{{userId}}">
        <input type="hidden" class="custindex" name="proModel.teacherList[custindex].userType" value="2">
        <td>{{name}}</td>
        <td>{{no}}</td>
        <td>{{teacherType}}</td>
        <td>{{postTitle}}</td>
        <td>{{education}}</td>
        <td>{{mobile}}</td>
        <%--<td>{{curJoin}}</td>--%>
        <td>
            <a class="minus"></a>
        </td>
    </tr>
</script>
</body>

</html>
