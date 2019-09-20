<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <script type="text/javascript">
        $frontOrAdmin = "${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}";
    </script>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript" src="${ctxJs}/backTable/sort.js"></script><!--排序js-->

    <script type="text/javascript" src="${ctxJs}/cyjd/checkall.js"></script>
    <script type="text/javascript" src="${ctxJs}/cyjd/publishExPro.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            $("#ps").val($("#pageSize").val());
//            $('input[data-toggle="checkall"], input[data-toggle="checkitem"]').on('change', function () {
//                $('.expbtn').prop('disabled', !window['checkAllInstance'].someChecked())
//            })
        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

    </script>

    <style>
        .sepatator-line strong{
            position: relative;
            z-index: 10;
            background-color: #fff;
        }
        .sepatator-line{
            position: relative;
        }
        .sepatator-line:after{
            content: '';
            position: absolute;
            top:50%;
            left: 0;
            width: 100%;
            height: 1px;
            background-color: #ddd;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <%--<div class="edit-bar edit-bar-tag clearfix">--%>
    <%--<div class="edit-bar-left">--%>
    <%--<span>项目查询</span>--%>
    <%--<i class="line weight-line"></i>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form id="searchForm" modelAttribute="projectDeclare" action="${ctx}/state/allList"
               class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input type="hidden" id="orderBy" name="orderBy" value="${page.orderBy}"/>
        <input type="hidden" id="orderByType" name="orderByType" value="${page.orderByType}"/><!--desc向下 asc向上-->
        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">项目类别</label>
                <div class="controls">
                    <form:select path="type" cssClass="form-control input-medium">
                        <form:option value="" label="所有项目类别"/>
                        <form:options items="${fns:getDictList('project_type')}" itemLabel="label"
                                      itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">项目级别</label>
                <div class="controls">
                    <form:select cssClass="form-control input-medium" path="level">
                        <form:option value="" label="请选择"/>
                        <c:if test="${fns:getUser().userType=='4'}"> <!--学院专家-->
                            <form:option value="3" label="B级"/>
                            <form:option value="4" label="C级"/>
                        </c:if>
                        <c:if test="${fns:getUser().userType=='5'}"> <!--学院专家-->
                            <form:option value="1" label="A+级"/>
                            <form:option value="2" label="A级"/>
                        </c:if>
                        <c:if test="${fns:getUser().userType  eq '3'|| fns:getUser().userType  eq '6' || fns:getUser().userType  eq '7' || fns:getUser().id  eq '1'}">
                            <form:options items="${fns:getDictList('project_degree')}" itemLabel="label"
                                          itemValue="value"
                                          htmlEscape="false"/>
                        </c:if>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">状态</label>
                <div class="controls">
                    <form:select path="status" cssClass="form-control input-medium">
                        <form:option value="" label="请选择"/>
                        <form:options items="${projectStatusEnumList}" itemLabel="name"
                                      itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group" style="margin-right: 370px;">
                <label class="control-label">申报年份</label>
                <div class="controls">
                    <form:input path="startDateStr" class="Wdate form-control input-medium" readonly="true"
                                onclick="WdatePicker({dateFmt:'yyyy', isShowClear:true});"></form:input>
                    <span>年</span>
                </div>
            </div>


        </div>
        <div class="search-btn-box">
            <form:input path="keyword" cssClass="input-medium" type="text" placeholder="关键字"/>
            <button id="btnSubmit" class="btn btn-primary" type="submit">查询</button>
            <button type="button" class="btn btn-primary" onclick="toExpPage()">导出</button>
            <button class="btn btn-primary expbtn" disabled type="button">导出附件</button>
            <shiro:hasPermission name="excellent:projectShow:edit">
                <button type="button" class="btn btn-primary" disabled data-toggle="publishExPro">发布优秀项目</button>
            </shiro:hasPermission>
        </div>
    </form:form>
    <sys:message content="${message}"/>
    <div class="search-form-wrap">
        <table id="expTable"
               class="table table-bordered table-condensed table-hover  table-sort table-theme-default table-center"
               style="display:none">
            <tbody>
            <tr id="expTr">
                <td>
                 	大创项目.zip
                </td>
                <td>
                    [总记录数]
                </td>
                <!-- <td>
                    [处理记录数]
                </td> -->
                <td>
                    [开始时间]
                </td>
                <td>
                    [状态]
                </td>
                <td>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <table id="contentTable"
           class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-sort">
        <thead>
        <tr>
            <th><input type="checkbox" data-toggle="checkall" name="allCheck"></th>
            <th width="151" data-name="a.number">
                <a class="btn-sort" href="javascript:void(0)"><span>项目编号</span><i class="icon-sort"></i></a>
            </th>
            <th width="25%" data-name="a.name">
                <a class="btn-sort" href="javascript:void(0)"><span>项目名称</span><i class="icon-sort"></i></a>
            </th>
            <th data-name="a.type">
                <a class="btn-sort" href="javascript:void(0)"><span>项目类别</span><i class="icon-sort"></i></a>
            </th>
            <th data-name="leaderString">
                <a class="btn-sort" href="javascript:void(0)"><span>负责人</span><i class="icon-sort"></i></a>
            </th>
            <th>组人数</th>
            <th data-name="tnames">
                <a class="btn-sort" href="javascript:void(0)"><span>指导老师</span><i class="icon-sort"></i></a>
            </th>
            <th data-name="a.level">
                <a class="btn-sort" href="javascript:void(0)"><span>项目级别</span><i class="icon-sort"></i></a>
            </th>

            <th data-name="finalResultString">
                <a class="btn-sort" href="javascript:void(0)"><span>项目结果</span><i class="icon-sort"></i></a>
            </th>
            <th>
                <span>年份</span>
            </th>
            <th>状态</th>
            <th width="160">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="item">
            <c:set var="projectDeclare" value="${pj:getProjectDeclareListVoById(item.id)}"/>
            <tr>
                <td><input type="checkbox" data-toggle="checkitem" name="projectId" value="${item.id}"></td>
                <td>${projectDeclare.number}</td>
                <td><a href="${ctx}/state/projectDetail?id=${item.id}">${projectDeclare.project_name}</a></td>
                <td>${item.typeString}</td>
                <td>${item.leaderString}</td>
                <td>
                        ${pj:getTeamNum(item.snames)}
                </td>
                    <%-- 组人数--%>
                <td>${item.tnames}</td>
                <td>${item.levelString}</td>
                <td>${item.finalResultString}</td>
                <td>${projectDeclare.year}</td>
                <td>
                    <c:if test="${empty item.finalResultString}">
                        <%-- <a href="${ctx}/act/task/processMap2?proInsId=${item.procInsId}" target="_blank">待${item.act.taskName}</a> --%>
                        <a href="${ctx}/actyw/actYwGnode/designView/${projectDeclare.status_code}?groupId=${projectDeclare.groupId}"
                           target="_blank">
                                <%--${item.act.taskName}--%>
                                ${pj:getAuditStatus(projectDeclare.status_code)}
                        </a>
                    </c:if>
                    <c:if test="${not empty item.finalResultString}">
                        <c:if test="${not empty item.procInsId}">
                            <%-- <a href="${ctx}/act/task/processMapByType?proInstId=${item.procInsId}&type=gc&status=${item.status}" target="_blank">${pj:getStatus(item.id)}</a> --%>
                            <a href="${ctx}/actyw/actYwGnode/designView/${projectDeclare.status_code}?groupId=${projectDeclare.groupId}"
                               target="_blank">${pj:getStatus(item.id)}</a>
                        </c:if>
                        <c:if test="${ empty item.procInsId}">
                            ${pj:getStatus(item.id)}
                        </c:if>
                    </c:if>
                </td>
                <td>
                    <shiro:hasPermission name="project:dcproject:modify">
                        <c:if test="${not empty item.procInsId}">
                            <a class="btn btn-primary btn-small"
                               href="${ctx}/state/projectEdit?id=${item.id}">
                                变更
                            </a>
                        </c:if>

                        <c:if test="${isAdmin == '1'}">
                            <a class="btn btn-small btn-default" href="${ctx}/state/projectDelete?id=${item.id}"
                               onclick="return confirmx('会删除项目相关信息,确认要删除吗？', this.href)">删除</a>
                        </c:if>
                    </shiro:hasPermission>
                    <%--<shiro:hasPermission name="excellent:projectShow:edit">--%>
                        <%--<a class="btn btn-primary btn-small"--%>
                           <%--href="${ctx}/excellent/projectShowForm?projectId=${item.id}">--%>
                            <%--展示--%>
                        <%--</a>--%>
                    <%--</shiro:hasPermission>--%>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    ${page.footer}
</div>


<div id="modalExport" class="modal hide" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <!-- <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <span class="modal-title">导出项目</span>
            </div> -->
            <div class="modal-body">
                <form id="formExport" class="form-inline">
                    <div class="control-group text-center">
                        <label class="radio inline">
                            <input type="radio" name="isAll" value="0">导出当前查询条件项目
                        </label>
                        <label class="radio inline">
                            <input type="radio" name="isAll" checked value="1">导出全部项目
                        </label>
                    </div>
                    <div class="control-group team-sepatator">
                        <div class="mgb-20 sepatator-line">
                            <strong>自定义导出表单中的分隔符</strong>
                        </div>
                        <div class="mgb-20">
                            <span>团队成员姓名和学号之间的分隔符</span>
                            <select name="separator" id="separator" class="input-mini">
                                <c:forEach items="${fns:spiltPrefs()}" var="spiltPref" varStatus="idx">
                                    <c:if test="${!fns: contains(spiltPref.remark, '(')}">
                                        <option value="${spiltPref.key}"
                                                data-remark="${spiltPref.remark}">${spiltPref.remark}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                            <span style="margin-left:20px;">多个成员之间的分隔符</span>
                            <select name="memberSepatator" id="memberSepatator" class="input-mini">
                                <c:forEach items="${fns:spiltPosts()}" var="spiltPost" varStatus="idx">
                                    <option value="${spiltPost.key}"
                                            data-remark="${spiltPost.remark}">${spiltPost.remark}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="text-center" style="color: red">示例：张三<span
                                class="separator-text">/</span>123456<span
                                class="memberSepatator-text">/</span>李四<span
                                class="separator-text">/</span>789123（导师同理）
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default btn-small btn-sm" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary btn-small btn-sm">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div id="modalExportFile" class="modal hide" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <!-- <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <span class="modal-title">导出项目附件</span>
            </div> -->
            <div class="modal-body">
                <form id="formExportFile" class="form-inline">
                    <div class="control-group text-center">
                        <label class="radio inline">
                            <input type="radio" name="isAll" value="0">导出当前查询条件项目附件
                        </label>
                        <label class="radio inline">
                            <input type="radio" name="isAll" checked value="1">导出全部项目附件
                        </label>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default btn-small btn-sm" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary btn-small btn-sm">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script>
    $(document).ready(function () {
        getExpInfo();

        $('select[name="separator"]').change(function () {
            $('.separator-text').text($('select[name="separator"] option:selected').attr('data-remark'))
        }).change()

        $('select[name="memberSepatator"]').change(function () {
            $('.memberSepatator-text').text($('select[name="memberSepatator"] option:selected').attr('data-remark'))
        }).change()
    });

    function expData() {
        $modalExportFile.modal('show');
    }

    var isFirst = true;
    var exportTimer = null;
    function getExpInfo() {
        $.ajax({
            type: "GET",
            url: "/a/exp/getExpPdGnode",
            data: "gnodeId=${gnodeId}",
            dataType: "json",
            success: function (data) {
                if (data) {
                    $("#expTable").attr("style", "display:");
                    if (data.total) {
                        $("#expTr").children('td').eq(1).html("[总记录数]" + data.total);
                    }
                    /* if (data.success) {
                        $("#expTr").children('td').eq(2).html("[处理记录数]" + data.success);
                    } */
                    if (data.startDate) {
                        $("#expTr").children('td').eq(2).html("[开始时间]" + data.startDate);
                    }
                    if (data.isComplete == '1' && data.sa) {
                        $("#expTr").children('td').eq(3).html("[状态]可下载");
                    }
                    if (data.isComplete == '1' && !data.sa) {
                        $("#expTr").children('td').eq(3).html("[状态]导出失败");
                    }
                    if (data.isComplete == '0') {
                        $("#expTr").children('td').eq(3).html("[状态]文件准备中...");
                    }
                    if (data.isComplete == '1' && data.sa) {
                        $("#expTr").children('td').eq(4).html("<a href='/a/ftp/ueditorUpload/downFile?url=" + data.sa.url + "'>下载文件</a>");
                    }
                    if (data.isComplete == '1') {
                        $(".expbtn").attr("onclick", "expData()");
                        $(".expbtn").removeAttr("disabled");
                    } else {
                        //eval("it" + data.id + "=setInterval(function(){getExpInfoSub(data.id);},2000);");
                        if ((data.id != undefined)) {
                            exportTimer = setInterval(function () {
                                getExpInfoSub(data.id);
                            }, 5000);
                        }
                    }
                } else {
                    if (!isFirst) {
                        location.reload();
                    }
                    $(".expbtn").attr("onclick", "expData()");
                    $(".expbtn").removeAttr("disabled");
                }
                isFirst = false;
            }
        });
    }
    function getExpInfoSub(id) {
        $.ajax({
            type: "GET",
            url: "/a/exp/getGnodeExpInfo",
            data: "eid=" + id,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                if (data) {
                    if (data.total) {
                        $("#expTr").children('td').eq(1).html("[总记录数]" + data.total);
                    }
                    /* if (data.success) {
                        $("#expTr").children('td').eq(2).html("[处理记录数]" + data.success);
                    } */
                    if (data.startDate) {
                        $("#expTr").children('td').eq(2).html("[开始时间]" + data.startDate);
                    }
                    if (data.isComplete == '1' && data.sa) {
                        $("#expTr").children('td').eq(3).html("[状态]可下载");
                    }
                    if (data.isComplete == '1' && !data.sa) {
                        $("#expTr").children('td').eq(3).html("[状态]导出失败");
                    }
                    if (data.isComplete == '0') {
                        $("#expTr").children('td').eq(3).html("[状态]文件准备中...");
                    }
                    if (data.isComplete == '1' && data.sa) {
                        $("#expTr").children('td').eq(4).html("<a href='/a/ftp/ueditorUpload/downFile?url=" + data.sa.url + "'>下载文件</a>");
                    }
                    if (data.isComplete == '1') {
                        $(".expbtn").attr("onclick", "expData()");
                        $(".expbtn").removeAttr("disabled");
                        clearInterval(exportTimer);
                    }
                } else {
                    $(".expbtn").attr("onclick", "expData()");
                    $(".expbtn").removeAttr("disabled");
                    clearInterval(exportTimer);
                }
            },
            error: function (msg) {
                $(".expbtn").attr("onclick", "expData()");
                $(".expbtn").removeAttr("disabled");
                clearInterval(exportTimer);
            }
        });
    }

    var $modalExport = $('#modalExport');
    var $modalExportFile = $('#modalExportFile');
    var $formExport = $('#formExport');
    var $formExportFile = $('#formExportFile');
    function toExpPage() {
        $modalExport.modal('show');
    }
    function getHparam($form) {
        var isAll = $form.find('input[name="isAll"]:checked').val()
        var proCategory = $("#proCategory").val();
        var officeId = $("#officeId").val();
        var queryStr = $("#queryStr").val();
        var hrefparam;
        if (isAll == '1') {
            hrefparam = "&isAll=" + isAll;
        } else {
            hrefparam = "&isAll=" + isAll;
            if (proCategory) {
                hrefparam += "&proCategory=" + proCategory;
            }
            if (officeId) {
                hrefparam += "&officeId=" + officeId;
            }
            if (queryStr) {
                hrefparam += "&queryStr=" + queryStr;
            }
        }
        return hrefparam;
    }
    $modalExport.find('.btn-primary').on('click', function () {
        location.href = "${ctx}/exp/expPdData/${actywId}?gnodeId=" + '${gnodeId}' + getHparam($formExport)+"&prefix="+$('select[name="separator"] option:selected').val()+"&postfix="+$('select[name="memberSepatator"] option:selected').val();
        $formExport[0].reset();
        $modalExport.modal('hide');
    })
    $modalExportFile.find('.btn-primary').on('click', function () {
        $(".expbtn").removeAttr("onclick", "expData()");
        $(".expbtn").attr("disabled", true);
        $.get("/a/exp/expPdByGnode?gnodeId=${gnodeId}&actywId=${actywId}" + getHparam($formExportFile), {}, function (data) {
            if (data.ret == "1") {
                $("#expTr").children('td').eq(1).html("[总记录数]");
                /* $("#expTr").children('td').eq(2).html("[处理记录数]"); */
                $("#expTr").children('td').eq(2).html("[开始时间]");
                $("#expTr").children('td').eq(3).html("[状态]");
                $("#expTr").children('td').eq(4).html("");
                getExpInfo();
            } else {
                alertx(data.msg);
                $(".expbtn").attr("onclick", "expData()");
                $(".expbtn").removeAttr("disabled");
            }
            $formExportFile[0].reset();
            $modalExportFile.modal('hide');
        });
    })
</script>
</body>
</html>