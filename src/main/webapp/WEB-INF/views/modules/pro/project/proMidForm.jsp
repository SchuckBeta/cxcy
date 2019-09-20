<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>中期检查报告</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
    <script type="text/javascript" src="${ctxCommon}/common-js/datepicker/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/projectForm.css"/>
    <script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
    <%--	<script src="${ctxCommon}/common-js/ajaxfileupload.js"></script>--%>
    <link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>


    <link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
    <script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
    <link rel="stylesheet" href="${ctxCss}/frontCyjd/frontBime.css">
    <script type="text/javascript" src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
    <style>
        .accessory-info a {
            color: #e9432d;
        }
    </style>
    <style>
        .error-tips {
            padding-left: 130px;
        }

        .error-tips span {
            display: inline-block;
            width: 100px;
        }

        .error-tips .end-errorTips {
            margin-left: 20px;
        }

        label.error {
            background-position: 0 3px;
        }

        .prj-division {
            background-position: 0 3px;
            position: relative;
            left: -90px;
        }

        .str-tips, .end-tips {
            position: absolute;
            top: 58px;
        }

        .str-tips label.error, .end-tips label.error {
            margin-left: 0px;
        }

        .str-tips {
            left: 43px;
        }

        .end-tips {
            left: 174px;
        }

        .prj-division-ch {
            left: -65px;
        }

        .programme {
            margin-left: -10px;
        }

        .pro-textarea {
            width: 100%;
            margin: 0;
        }

        .footer-btn-wrap {
            text-align: center;
        }

        .must-write {
            color: red;
            font-weight: normal;
            font-style: normal;
            margin: 0 4px 0 -10px;
        }

    </style>

    <script type="text/javascript">

        var validate;
        var ratio = "";
        $(document).ready(function () {
            ratio = "${ratio}";
            if (ratio == "") {
                $("#ratioSpan").remove();
                $('th.credit-ratio,td.credit-ratio').remove();
            }
            validate = $("#inputForm").validate({
                rules: {
                    taskBeginDate: {
                        required: true
                    },
                    taskEndDate: {
                        required: true
                    }
                },
                errorPlacement: function (error, element) {
                    if (element.attr("id") == "taskBeginDate") {
                        $(".start-errorTips").html(error);
                    } else if (element.attr("id") == "taskEndDate") {
                        $(".end-errorTips").html(error);
                    } else if (element.attr("name").indexOf("proSituationList") >= 0) {
                        element.siblings('.prj-division').html(error)
                    } else if (element.attr("name").indexOf("startDate") >= 0) {
                        element.siblings('.str-tips').html(error);
                    } else if (element.attr("name").indexOf("endDate") >= 0) {
                        element.siblings('.end-tips').html(error);
                    } else if (element.attr("name").indexOf("proProgresseList") >= 0) {
                        element.siblings('.prj-division-ch').html(error);
                    } else if (element.attr("name") == "programme") {
                        element.siblings('.programme').html(error);
                    }
                    else {
                        error.insertAfter(element);
                    }
                }
            });
        });


        function submit() {
            //学分配比校验
            var ischeck = true;
            ischeck = checkMenuByNum(ischeck);
            if (!ischeck) {
                return false;
            }

            if (validate.form()) {
                var me = $("#submitBtn");
                if (me.data("data-clicked") === 1) {
                    return;
                }
                $("#inputForm").submit();
                me.data("data-clicked", 1);
            }
        }

        //学分配比校验
        function checkRatio() {
            var result = true;
            if (ratio != "") {
                var creditArr = [];
                $('.credit-ratio input.form-control').each(function (i, item) {
                    creditArr.push($(item).val());
                });

                creditArr.sort(function (a, b) {
                    return b - a;
                });
                var ratioArr = ratio.split(':').sort(function (a, b) {
                    return b - a;
                });

                if (creditArr.join(':') !== ratioArr.join(':')) {
                    result = false;
                }
            }
            return result;
        }

        function checkMenuByNum(ischeck) {
            $.ajax({
                type: "GET",
                url: "/f/authorize/checkMenuByNum",
                data: "num=5",
                async: false,
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    if (data) {
                        if (!checkRatio()) {
                            ischeck = false;
                            showModalMessage(0, "学分配比不符合规则！", {
                                "确定": function () {
                                    $(this).dialog("close");
                                    $("input[name='teamUserRelationList[0].weightVal']").focus();
                                }
                            });

                        }
                    }
                },
                error: function (msg) {
                    showModalMessage(0, msg);
                }
            });
            return ischeck;
        }
    </script>
</head>
<body>
<form action="${ctxFront}/project/proMid/save" id="inputForm" method="post">
    <div class="top-title">
        <h3>${projectDeclare.name}</h3>
        <h4>中期检查报告</h4>
        <div class="top-bread">
            <div class="top-prj-num"><span>项目编号:</span>${projectDeclare.number}</div>
            <div class="top-prj-num"><span>创建时间:</span><fmt:formatDate value="${projectDeclare.createDate}"/></div>
            <a href="javascript:;" class="btn btn-sm btn-primary" onclick="history.go(-1);">返回</a>
        </div>
    </div>

    <input type="hidden" name="id" value="${proMid.id}"/>

    <div class="outer-wrap">
        <div class="container-fluid">
            <input type="hidden" id="projectId" name="projectId" value="${projectDeclare.id}">
            <div class="row content-wrap">
                <section class="row">
                    <div class="form-horizontal" novalidate>
                        <c:set var="leader" value="${fns:getUserById(projectDeclare.leader)}"/>
                        <div class="form-group col-sm-6 col-md-6 col-lg-6">
                            <label class="col-xs-3 ">项目负责人：</label>
                            <p class="col-xs-9">
                                ${leader.name}
                            </p>
                        </div>
                        <div class="form-group col-sm-2 col-md-2 col-lg-2"></div>
                        <div class="form-group col-sm-4 col-md-4 col-lg-4">
                            <label class="col-xs-5">专业年级：</label>
                            <p class="col-xs-7">${fns:getOffice(leader.professional).name}<fmt:formatDate
                                    value="${student.enterDate}" pattern="yyyy"/></p>
                        </div>
                    </div>

                    <div class="form-horizontal" novalidate>
                        <div class="form-group col-sm-6 col-md-6 col-lg-6">
                            <label class="col-xs-3 ">项目组成员：</label>
                            <p class="col-xs-9">
                                ${teamList}
                            </p>
                        </div>
                        <div class="form-group col-sm-2 col-md-2 col-lg-2"></div>
                        <div class="form-group col-sm-4 col-md-4 col-lg-4">
                            <label class="col-xs-5">项目导师：</label>
                            <p class="col-xs-7">${teacher}</p>
                        </div>
                    </div>

                    <div class="form-horizontal" novalidate>
                        <div class="form-group col-sm-12 col-md-12 col-lg-12">
                            <label class="col-xs-2 "><i class="must-write">*</i>任务时间段：</label>
                            <p class="col-xs-10" style="margin-left: -45px;">

                                <input class="Wdate" type="text" name="taskBeginDate" id="taskBeginDate"
                                       onClick="WdatePicker({maxDate:'#F{$dp.$D(\'taskEndDate\')}'})"
                                       value='<fmt:formatDate value="${proMid.taskBeginDate}" pattern="yyyy-MM-dd"/>'
                                />
                                -
                                <input class="Wdate" type="text" name="taskEndDate" id="taskEndDate"
                                       onClick="WdatePicker({minDate:'#F{$dp.$D(\'taskBeginDate\')}'})"
                                       value='<fmt:formatDate value="${proMid.taskEndDate}" pattern="yyyy-MM-dd"/>'
                                />
                            </p>
                        </div>
                    </div>

                    <div class="row error-tips">
                        <span class="start-errorTips"></span>
                        <span class="end-errorTips"></span>
                    </div>
                </section>

                <section class="row">
                    <div class="prj_common_info"><h4>计划工作任务</h4><span class="yw-line"></span></div>
                    <table class="table table-hover  table-condensed table-bordered">
                        <thead>
                        <th>序号</th>
                        <th>计划时间段</th>
                        <th>计划工作任务</th>
                        </thead>
                        <tbody>
                        <c:forEach items="${plans}" var="plan" varStatus="status">
                            <tr>
                                <td>${status.count}</td>
                                <td><fmt:formatDate value="${plan.startDate}" pattern="yyyy-MM-dd"/> 至 <fmt:formatDate
                                        value="${plan.endDate}" pattern="yyyy-MM-dd"/></td>
                                <td>${plan.content}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </section>

                <section class="row">
                    <div class="prj_common_info">
                        <h4><i class="must-write">*</i>任务完成情况</h4>

                        <span class="yw-line"></span>

                    </div>
                    <div class="table-wrap">
                        <table class="table  table-hover table-bordered">
                            <caption
                                    style="width:auto;text-align: left;background-color: #fff;border-radius: 0;padding: 0;">
                                <span style="background-color: #ebebeb;padding: 0 10px;line-height: 28px;display: inline-block;vertical-align: top; border-radius: 3px 3px 0 0">组成员完成情况</span>
                                <c:if test="${fns:checkMenuByNum(5)}">
                                    <c:if test="${proMid.id ==null}">
                                        <span style="color: #df4526;vertical-align: top;line-height: 28px;margin-left: 8px;"
                                              id="ratioSpan">学分配比规则：${ratio}</span>
                                    </c:if>
                                </c:if>
                            </caption>
                            <thead>
                            <th>项目组成员</th>
                            <th>项目分工</th>
                            <th>完成情况</th>
                            <c:if test="${fns:checkMenuByNum(5)}">
                                <c:if test="${proMid.id ==null}">
                                    <th class='credit-ratio'>学分配比</th>
                                </c:if>
                            </c:if>
                            </thead>
                            <tbody>
                            <c:if test="${proMid.id!=null}">
                                <c:forEach items="${proSituationList}" var="proSituation" varStatus="status">
                                    <tr>
                                        <input type="hidden" name="proSituationList[${status.index}].user.id"
                                               value="${proSituation.user.id}"/>
                                        <td>${proSituation.user.name}</td>
                                        <td class="td_input">
                                            <textarea name="proSituationList[${status.index}].division" required
                                                      maxlength="2000">${proSituation.division}</textarea>
                                            <br/><span class="prj-division"></span>
                                        </td>
                                        <td class="td_input">
                                            <textarea name="proSituationList[${status.index}].situation" required
                                                      maxlength="2000">${proSituation.situation}</textarea>
                                            <br/><span class="prj-division"></span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            <c:if test="${proMid.id ==null}">
                                <c:forEach items="${turStudents}" var="tur" varStatus="status">
                                    <tr>
                                        <input type="hidden" name="proSituationList[${status.index}].user.id"
                                               value="${tur.userId}"/>
                                        <td>${tur.name}</td>
                                        <td class="td_input">
                                            <textarea name="proSituationList[${status.index}].division" required
                                                      maxlength="2000"></textarea>
                                            <br/><span class="prj-division"></span>
                                        </td>
                                        <td class="td_input">
                                            <textarea name="proSituationList[${status.index}].situation" required
                                                      maxlength="2000"></textarea>
                                            <br/><span class="prj-division"></span>
                                        </td>
                                        <input type="hidden" name="teamUserHistoryList[${status.index}].id"
                                               value="${tur.id}">
                                        <c:if test="${fns:checkMenuByNum(5)}">
                                            <td class="credit-ratio">
                                                <input class="form-control input-sm "
                                                       name="teamUserHistoryList[${status.index}].weightVal"
                                                       value="${tur.weightVal}">
                                            </td>
                                        </c:if>
                                    </tr>
                                </c:forEach>
                            </c:if>

                            </tbody>
                        </table>

                        <table class="table  table-hover table-bordered">
                            <caption style="width: 116px;">当前项目进度</caption>
                            <thead>
                            <th>序号</th>
                            <th>实际完成时间</th>
                            <th>实际完成情况</th>
                            <th>阶段性成果</th>
                            <th>操作</th>
                            </thead>
                            <tbody id="prj_process_body">

                            <script type="text/template" id="tpl">
                                <tr>
                                    <td>
                                        {{idx}}
                                    </td>
                                    <td style="position:relative;">
                                        <input id="startDate[{{idIdx}}]" name="proProgresseList[{{nameIdx}}].startDate"
                                               class="Wdate" type="text"
                                               onClick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate[{{idIdx}}]\')}'} )"
                                               value="{{row.startDate}}" required/>
                                        <span class="str-tips"></span>
                                        <span>至</span>
                                        <input id="endDate[{{idIdx}}]" name="proProgresseList[{{nameIdx}}].endDate"
                                               class="Wdate" type="text"
                                               onClick="WdatePicker({minDate:'#F{$dp.$D(\'startDate[{{idIdx}}]\')}'}  )"
                                               value="{{row.endDate}}" required/>
                                        <span class="end-tips"></span>
                                    </td>
                                    <td class="td_input">
                                        <textarea name="proProgresseList[{{nameIdx}}].situation" required
                                                  maxlength="2000">{{row.situation}}</textarea>
                                        <br/><span class="prj-division prj-division-ch"></span>
                                    </td>
                                    <td class="td_input">
                                        <textarea name="proProgresseList[{{nameIdx}}].result" required maxlength="2000">{{row.result}}</textarea>
                                        <br/><span class="prj-division prj-division-ch"></span>
                                    </td>

                                    <td class="opt_btn" style="width:150px">
                                        <a href="javascript:;" onclick="addRow('#prj_process_body',tpl,'');"
                                           class="btn btn-sm btn-primary btn-sm-reset btn-add"><span
                                                class="icon-plus"></span>添加</a>
                                        {{#delBtn}}<a href="javascript:;" onclick="delRow(this);"
                                                      class="btn btn-sm btn-primary btn-sm-reset btn-delete"><span
                                            class="icon-minus "></span>删除</a>{{/delBtn}}
                                    </td>
                                </tr>
                            </script>
                            <script type="text/javascript">
                                var rowIdx = 1;
                                var idIdx = 1;
                                var tpl = $("#tpl").html();
                                var delBtn = true;
                                function addRow(tbodyId, tpl, row) {
                                    if (rowIdx == 1) {
                                        delBtn = false;
                                    } else {
                                        delBtn = true;
                                    }
                                    $(tbodyId).append(Mustache.render(tpl, {
                                        idx: rowIdx, nameIdx: rowIdx - 1, delBtn: delBtn, row: row, idIdx: idIdx
                                    }));
                                    rowIdx++;
                                    idIdx++;
                                }
                                function delRow(obj) {
                                    $(obj).parent().parent().remove();
                                    rowIdx--;
                                    reOrder();
                                }
                                function reOrder() {
                                    //重置序号
                                    $("#prj_process_body  tr").find("td:first").each(function (i, v) {
                                        $(this).html(i + 1);
                                    });
                                    var index = 0;
                                    var rex = "\\[(.+?)\\]";
                                    $("#prj_process_body tr").each(function (i, v) {
                                        $(this).find("[name]").each(function (i2, v2) {
                                            var name = $(this).attr("name");
                                            var indx = name.match(rex)[1];
                                            ;
                                            $(this).attr("name", name.replace(indx, index));
                                        });
                                        index++;
                                    });
                                }

                                var data = ${fns:toJson(proProgressList)};
                                if (data.length > 0) {
                                    for (var i = 0; i < data.length; i++) {
                                        addRow('#prj_process_body', tpl, data[i]);
                                    }
                                } else {
                                    addRow('#prj_process_body', tpl, '');
                                }

                            </script>

                            </tbody>
                        </table>
                    </div>
                </section>

                <section class="row">
                    <div class="prj_common_info"><h4><i class="must-write">*</i>目前存在的问题及解决方案</h4><span
                            class="yw-line"></span></div>
                    <div class="textarea-wrap">
                        <textarea name="programme" maxlength="2000"
                                  class="col-xs-12 col-md-12 col-sm-12 col-lg-12 my-textarea pro-textarea"
                                  required>${proMid.programme}</textarea>
                        <br/><span class="programme"></span>
                    </div>
                </section>

                <section class="row">
                    <div class="prj_common_info" style="height: 40px;line-height: 40px;">
                        <h4 class="sub-file" style="margin-top: 25px;">附件 </h4><span class="yw-line yw-line-fj"></span>
                    </div>
                    <div class="textarea-wrap">
                        <%--<sys:frontFileUploadOld fileitems="${fileListMap}" filepath="proMidForm" btnid="upload"></sys:frontFileUploadOld>--%>
                        <sys:frontFileUpload fileitems="${fileListMap}" filepath="proMidForm"
                                             className="accessories-h34"></sys:frontFileUpload>
                        <%--<sys:frontFileUploadCommon fileitems="${fileListMap}" filepath="proMidForm"--%>
                        <%--btnid="upload"></sys:frontFileUploadCommon>--%>
                    </div>
                </section>


                <c:if test="${not empty proMid.tutorSuggest}">
                    <section class="row">
                        <div class="prj_common_info" style="height: 40px;line-height: 40px;">
                            <h4 class="sub-file" style="margin-top: 25px;">导师意见及建议</h4><span
                                class="yw-line yw-line-fj"></span>
                            <span href="javascript:;" class="upload-file"
                                  style="background: none;color:#656565 !important;">
											<strong>
												时间：
												<c:choose>
                                                    <c:when test="${not empty proMid.tutorSuggestDate}">
                                                        <fmt:formatDate value="${proMid.tutorSuggestDate}"
                                                                        pattern="yyyy-MM-dd"/>
                                                    </c:when>
                                                    <c:otherwise>${fns:getDate('yyyy-MM-dd')}</c:otherwise>
                                                </c:choose>
											</strong>
										</span>
                        </div>
                            <%--<textarea maxlength="2000"  name="tutorSuggest" class="col-xs-12 col-md-12 col-sm-12 col-lg-12 my-textarea" readonly  >${proMid.tutorSuggest}</textarea>--%>
                        <p>${proMid.tutorSuggest}</p>
                    </section>
                </c:if>


                <div class="footer-btn-wrap">
                    <%--<a href="javascript:;" class="btn btn-sm btn-primary btn-save" onclick="submit()">保存</a>--%>
                    <button type="button" data-control="uploader" class="btn btn-sm btn-primary" onclick="submit()"
                            id="submitBtn">提交</a>
                </div>
            </div>
        </div>
    </div>
</form>
<%--	<script src="${ctxJs}/gcProject/fileUpLoad.js"></script>--%>
</body>
</html>