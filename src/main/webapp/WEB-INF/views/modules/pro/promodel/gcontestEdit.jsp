<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>--%>
<!DOCTYPE html>
<html>
<head>
    <title>大赛变更</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
    <style type="text/css">
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

<div class="container-fluid container-fluid-audit" style="margin-bottom: 60px;">
    <%--<div class="edit-bar clearfix">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<span>大赛变更</span> <i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form id="inputForm" modelAttribute="proModel" action="${ctx}/promodel/proModel/saveGcontestEdit"
               method="post" class="form-horizontal form-container">
        <form:hidden path="id"/>
        <form:hidden path="year"/>
        <form:hidden path="act.taskId"/>
        <form:hidden path="act.taskName"/>
        <form:hidden path="act.taskDefKey"/>
        <form:hidden path="act.procInsId"/>
        <form:hidden path="act.procDefId"/>

        <input type="hidden" name="actionPath" value="${actionPath}"/>
        <input type="hidden" name="gnodeId" value="${gnodeId}"/>
        <input type="hidden" id="secondName" value="${secondName}"/>

        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>基本信息</span> <i class="line"></i> <a
                    data-toggle="collapse" href="#projectPeopleDetail"><i
                    class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>
        <div id="projectPeopleDetail" class="panel-body collapse in">
            <div class="row-fluid row-info-fluid">
                <div class="span4">
                    <span class="item-label">项目负责人：</span>
                    <div class="items-box">
                            ${proModel.deuser.name}
                    </div>
                </div>
                <div class="span4">
                    <span class="item-label">填报日期：</span>
                    <div class="items-box"><fmt:formatDate
                            value="${proModel.createDate}"></fmt:formatDate></div>
                </div>

            </div>
            <div class="row-fluid row-info-fluid">
                <div class="span4">
                    <span class="item-label">学号：</span>
                    <div class="items-box">${proModel.deuser.no}</div>
                </div>
                <div class="span4">
                    <span class="item-label">性别：</span>
                    <div class="items-box">
                        <c:choose>
                            <c:when test="${proModel.deuser.sex == '1'}">男</c:when>
                            <c:when test="${proModel.deuser.sex == '0'}">女</c:when>
                        </c:choose>
                    </div>
                </div>
                <div class="span4">
                    <span class="item-label">所属学院：</span>
                    <div class="items-box">
                            ${proModel.deuser.office.name}
                    </div>
                </div>

            </div>
            <div class="row-fluid row-info-fluid">


                <div class="span4">
                    <span class="item-label">专业：</span>
                    <div class="items-box">${proModel.deuser.subject.name}</div>
                </div>
                <div class="span4">
                    <span class="item-label">联系电话：</span>
                    <div class="items-box">
                            ${proModel.deuser.mobile}
                    </div>
                </div>
                <div class="span4">
                    <span class="item-label">Email：</span>
                    <div class="items-box">
                            ${proModel.deuser.email}
                    </div>
                </div>
            </div>
        </div>

        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>大赛信息</span> <i class="line"></i> <a data-toggle="collapse"
                                                          href="#teamDetail"><i
                    class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>
        <div id="teamDetail" class="panel-body collapse in">
            <div class="panel-inner">
                <div class="control-group">
                    <label class="control-label"><i>*</i>大赛编号：</label>
                    <div class="controls">
                        <input type="text" name="competitionNumber"
                               id="competitionNumber" maxlength="64" value="${proModel.competitionNumber}"
                               class="required"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><i>*</i>参赛项目名称：</label>
                    <div class="controls">
                        <input type="text" class="required"
                               maxlength="128" name="pName" value="${proModel.pName}"
                               placeholder="最多128个字符">
                    </div>
                </div>
                <div class="control-group">
                 <label class="control-label"><i>*</i>参赛组别：</label>
                    <div class="controls">
                    <form:select path="proCategory" class="required form-control">
                                    <form:option value="" label="请选择"/>
                                    <form:options items="${proCategoryMap}" itemLabel="label"
                                                  itemValue="value" htmlEscape="false"/>
                                </form:select>
                    </div>
                </div>
				<div class="control-group">
                    <label class="control-label"><i>*</i>参赛组别：</label>
                    <div class="controls">
                    <form:select path="level" class="required form-control">
                                    <form:option value="" label="请选择"/>
                                    <form:options items="${fns:getDictList('gcontest_level')}" itemLabel="label"
                                                  itemValue="value" htmlEscape="false"/>
                                </form:select>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><i>*</i>项目介绍：</label>
                    <div class="controls">
                        <textarea class="required input-xxlarge"
                                  name="introduction"
                                  rows="3"
                                  maxlength="1024" placeholder="最多1024个字符">${proModel.introduction }</textarea>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">附件：</label>
                    <div class="controls">
                            <%--<sys:frontFileUpload fileitems="${sysAttachments}" filepath="project"></sys:frontFileUpload>--%>
                        <sys:frontFileUpload className="accessories-h30"
                                             fileitems="${applyFiles}"></sys:frontFileUpload>
                    </div>
                </div>
            </div>
        </div>

        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>团队信息</span> <i class="line"></i> <a data-toggle="collapse"
                                                          href="#teamInfo"><i
                    class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>
        <div id="teamInfo" class="panel-body collapse in">
            <div class="panel-inner">
                <div class="row-fluid row-info-fluid">
                    <div class="span6">
                        <span class="item-label">团队名称：</span>${team.name}
                    </div>
                </div>


                <div class="table-caption">学生团队</div>
                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover studenttb">
                    <thead>
                    <tr>
                        <th>姓名</th>
                        <th>学号</th>
                        <th>联系电话</th>
                        <th>学院</th>
                        <th>当前在研</th>
                        <th>职责</th>
                        <th><input type="button" class="btn btn-primary btn-small" userType="1" value="新增"
                                   onclick="selectUser(this)"/></th>
                    </tr>
                    </thead>
                    <tbody id="teamTableStudent">
                    <c:forEach items="${teamStu}" var="item" varStatus="status">
                        <tr userid="${item.userId}">
                            <input class="custindex" type="hidden" name="studentList[${status.index}].userId"
                                   value="${item.userId}">
                            <input class="custindex" type="hidden" name="studentList[${status.index}].userType"
                                   value="1">
                            <td>${item.name}</td>
                            <td>${item.no}</td>
                            <td>${item.mobile}</td>
                            <td>${item.orgName}</td>
                            <td>${item.curJoin}</td>
                            <td>
                                <select class="zzsel custindex" name="studentList[${status.index}].userzz">
                                    <option value="0"
                                            <c:if test="${item.userId==proModel.declareId}">selected</c:if> >
                                        负责人
                                    </option>
                                    <option value="1"
                                            <c:if test="${item.userId!=proModel.declareId}">selected</c:if> >
                                        组成员
                                    </option>
                                </select>
                            </td>
                            <td><a class="minus"></a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="table-caption">指导教师</div>
                <table
                        class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap teachertb">
                    <thead>
                    <tr>
                        <th>姓名</th>
                        <th>工号</th>
                        <th>导师来源</th>
                        <th>职称（职务）</th>
                        <th>学历</th>
                        <th>联系电话</th>
                        <th>当前指导</th>
                        <th><input type="button" class="btn btn-primary btn-small" userType="2" value="新增"
                                   onclick="selectUser(this)"/></th>
                    </tr>
                    </thead>
                    <tbody id="teamTableTeacher">
                    <c:forEach items="${teamTea}" var="item" varStatus="status">
                        <tr userid="${item.userId}">
                            <input class="custindex" type="hidden" name="teacherList[${status.index}].userId"
                                   value="${item.userId}">
                            <input class="custindex" type="hidden" name="teacherList[${status.index}].userType" value="2">
                            <td>${item.name}</td>
                            <td>${item.no}</td>
                            <td>${item.teacherType}</td>
                            <td>${item.technicalTitle}</td>
                            <td>${fns:getDictLabel(item.education,'enducation_level','')}</td>
                            <td>${item.mobile}</td>
                            <td>${item.curJoin}</td>
                            <td><a class="minus"></a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

            </div>
        </div>


        <!-- 学生提交的各种报告-->
        <c:if test="${not empty reports}">
            <c:forEach items="${reports}" var="item" varStatus="status">
                <c:set var="actywGnode" value="${fns:getActYwGnode(item.gnodeId)}"/>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>${actywGnode.name}</span> <i class="line"></i><a data-toggle="collapse"
                                                                               href="#files${status.index}"><i
                            class="icon-collaspe icon-double-angle-up"></i></a>
                    </div>
                </div>

                <div id="files${status.index}" class="panel-body collapse in">
                    <div class="panel-inner">
                        <div class="row-fluid row-info-fluid">
                            <div class="span10">
                                <span class="item-label">附件：</span>
                                <div class="items-box">
                                    <sys:frontFileUpload className="accessories-h30" fileitems="${item.files}"
                                                         readonly="true"></sys:frontFileUpload>
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid row-info-fluid">
                            <div class="span11">
                                <span class="item-label" style="width:150px;">已取得阶段性成果：</span>
                                <div class="items-box" style="margin-left: 165px;">
                                        ${item.stageResult}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:if>


        <!-- 审核记录 -->
        <c:if test="${not empty actYwAuditInfos}">

            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>审核记录</span> <i class="line"></i><a data-toggle="collapse" href="#actYwAuditInfos"><i
                        class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>

            <div id="actYwAuditInfos" class="panel-body collapse in">
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
                                    <td><fmt:formatDate value="${item.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td>${item.user.name}</td>
                                    <td>${item.result}</td>
                                    <td>${item.suggest}</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="5"
                                        style="color: red; text-align: right;font-weight: bold">${item.auditName}：${item.result}</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

        </c:if>
        <c:if test="${not empty changeGnodes}">
        <div class="control-group">
            <span class="control-label">变更流程到节点：</span>
            <div class="controls">
                <form:select path="toGnodeId">
                    <form:option value="" label="--请选择--"/>
                    <form:options items="${changeGnodes}" itemValue="gnodeId"
                                  itemLabel="auditName"
                                  htmlEscape="false"/></form:select>
            </div>
        </div>
        </c:if>
        <div class="form-actions-cyjd text-center" style="border-top: none">
            <a class="btn  btn-primary" id="submitBtn" href="javascript:void(0)" onclick="submitData()">保存</a>
            <a class="btn btn-default" href="javascript:history.go(-1)">返回</a>
        </div>
    </form:form>


</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>

<script type="text/javascript">
    var validate;
    $(document).ready(function () {
        validate = $("#inputForm").validate({
            rules: {
                "competitionNumber": {
                    remote: {
                        url: "/a/promodel/proModel/checkNumber",     //后台处理程序
                        type: "post",               //数据发送方式
                        dataType: "json",           //接受数据格式
                        data: {                     //要传递的数据
                            id: "${proModel.id}",
                            num: function () {
                                return $("input[name='competitionNumber']").val();
                            }
                        }
                    }
                }
            },
            messages: {
                "competitionNumber": {
                    remote: "项目编号已存在"
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });


    /* 团队变更 */
    $(document).on('click', '.minus', function (e) {
        var tbd = $(this).parents("tbody");
        var tb = $(this).parents("table");
        $(this).parents("tr").remove();
        resetNameIndex(tbd);
        if (tb.hasClass("studenttb")) {
            $(".xfpbval").val("");
        }
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
    function submitData() {
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
        if (validate.form()) {
            $("#submitBtn").removeAttr("onclick");
            $("#submitBtn").addClass("disabled");

            $("#inputForm").ajaxSubmit(function (data) {
                if (data.ret == "0") {
                    $("#submitBtn").attr("onclick", "submitData();");
                    $("#submitBtn").removeClass("disabled");
                    alertx(data.msg);
                } else {
                    alertx(data.msg, function () {
                        window.location.href = "/a/cms/form/queryMenuList/?actywId=${proModel.actYwId}";
                    });
                }
            });
        }
    }
</script>
<script type="text/template" id="tpl_st">
    <tr userid="{{userId}}">
        <input type="hidden" class="custindex" name="studentList[custindex].userId" value="{{userId}}">
        <input type="hidden" class="custindex" name="studentList[custindex].userType" value="1">
        <td>{{name}}</td>
        <td>{{no}}</td>
        <td>{{mobile}}</td>
        <td>{{office}}</td>
        <td>{{curJoin}}</td>
        <td>
            <select class="zzsel custindex" name="studentList[custindex].userzz">
                <option value="0">负责人
                </option>
                <option value="1" selected>组成员
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
        <input type="hidden" class="custindex" name="teacherList[custindex].userId" value="{{userId}}">
        <input type="hidden" class="custindex" name="teacherList[custindex].userType" value="2">
        <td>{{name}}</td>
        <td>{{no}}</td>
        <td>{{teacherType}}</td>
        <td>{{postTitle}}</td>
        <td>{{education}}</td>
        <td>{{mobile}}</td>
        <td>{{curJoin}}</td>
        <td>
            <a class="minus"></a>
        </td>
    </tr>
</script>
</body>
</html>
