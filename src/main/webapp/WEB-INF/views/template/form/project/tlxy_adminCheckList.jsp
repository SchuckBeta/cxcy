<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript" src="${ctxJs}/backTable/sort.js"></script><!--排序js-->
<body>

<style>
    .form-search-block .search-btn-box{
        margin-top:0;
        margin-bottom:20px;
    }
    .data-count{
        line-height:50px;
        font-size: 12px;
    }
    .modal-export .control-group{
        padding-top:30px;
    }
    .team-sepatator{
        border-top:1px solid #eee;
        position: relative;
        margin-top:60px;
    }
    .team-sepatator strong{
        position: absolute;
        top:-11px;
    }
    .team-sepatator select{
        width:50px;
    }
    .team-sepatator > div{
        margin-bottom:10px;
    }
    .team-sepatator > div:last-child{
        color:red;
    }
</style>

<div class="container-fluid">
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form id="searchForm" modelAttribute="proModel" action="${actionUrl}" method="post"
               class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input type="hidden" id="orderBy" name="orderBy" value="${page.orderBy}"/>
        <input type="hidden" id="orderByType" name="orderByType" value="${page.orderByType}"/>
        <input id="actywId" name="actywId" type="hidden" value="${actywId}"/>
        <input id="gnodeId" name="gnodeId" type="hidden" value="${gnodeId}"/>

        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">所属学院</label>
                <div class="controls">
                    <form:select id="officeId" path="deuser.office.id" class="input-medium form-control">
                        <form:option value="" label="所有学院"/>
                        <form:options items="${fns:findColleges()}" itemLabel="name" itemValue="id"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">项目类别</label>
                <div class="controls">
                    <form:select path="proCategory" class="input-medium">
                        <form:option value="" label="所有类别"/>
                        <form:options items="${fns:getProCategoryByActywId(actywId)}" itemValue="value"
                                      itemLabel="label"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">项目级别</label>
                <div class="controls">
                    <form:select path="proCategory" class="input-medium">
                        <form:option value="" label="所有级别"/>
                        <form:option value="" label="校级"/>
                        <form:option value="" label="省级"/>
                        <form:option value="" label="国家级"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">申报日期</label>
                <div class="controls">
                    <input id="qstartQDate" name="startQDate" type="text" readonly="readonly" maxlength="20"
                           class="input-medium Wdate"
                           value="<fmt:formatDate value="${pwEnter.startQDate}" pattern="yyyy-MM-dd"/>"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,maxDate:$('#qstartDate').val()});"/> -
                    <input id="qstartDate" name="startDate" type="text" readonly="readonly" maxlength="20"
                           class="input-medium Wdate"
                           value="<fmt:formatDate value="${pwEnter.startDate}" pattern="yyyy-MM-dd"/>"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true, minDate:$('#qstartQDate').val()});"/>
                </div>
            </div>
        </div>
        <span class="data-count">选择<span>0</span>条数据</span>
        <div class="search-btn-box">
            <form:input class="input-xlarge" path="queryStr" htmlEscape="false" maxlength="100" autocomplete="off"
                        placeholder="项目名称/编号/负责人/组成员/指导教师"/>
            <button id="searchBtn" type="submit" class="btn btn-primary">查询</button>
            <button type="button" class="btn btn-primary recommend-level">推荐级别</button>
            <button type="button" class="btn btn-primary batch-pass">批量通过</button>
            <button type="button" class="btn btn-primary batch-check">批量审核</button>
            <shiro:hasPermission name="sys:user:import">
            	<c:if test="${fns:isFirstMenu(actywId,gnodeId)}">
	            <button id="impBtn" type="button" class="btn btn-primary" style="display: none;"
	                    onclick="toImpPage(false, '${actywId}', '${gnodeId}')">导入项目
	            </button>
	            </c:if>
            </shiro:hasPermission>
            <button type="button" class="btn btn-primary" onclick="toExpPage()">项目导出</button>
            <%--<button class="btn btn-primary expbtn" disabled type="button">导出附件</button>--%>
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
                    ${menuName}.zip
                </td>
                <td>
                    [项目数]
                </td>
                <!-- <td>
                    [文件]
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
            <th><input type="checkbox" id="selectAllbtn" onclick="selectAll(this)"></th>
            <th width="140" data-name="a.competition_number"><a class="btn-sort" href="javascript:void(0);">项目编号<i
                    class="icon-sort"></i></a></th>
            <th width="25%" data-name="a.p_name"><a class="btn-sort" href="javascript:void(0);">项目名称<i
                                class="icon-sort"></i></a></th>
            <th>项目类别</th>
            <th>负责人</th>
            <th>学院</th>
            <th>组成员</th>
            <th>指导教师</th>
            <th data-name="a.date"><a class="btn-sort" href="javascript:void(0);">申报日期<i
                                class="icon-sort"></i></a></th>
            <th data-name="a.level"><a class="btn-sort" href="javascript:void(0);">项目级别<i
                                            class="icon-sort"></i></a></th>
            <th data-name="a.final_result"><a class="btn-sort" href="javascript:void(0);">${menuName}结果<i
                                                        class="icon-sort"></i></a></th>
            <%--<th>年份</th>--%>
            <th data-name="a.state"><a class="btn-sort" href="javascript:void(0);">审核状态<i
                                                                    class="icon-sort"></i></a></th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="item">
            <c:set var="map" value="${fns:getActByPromodelId(item.id)}"/>
            <tr>
                <td><input type="checkbox" name="subck" onclick="subckchange(this)" value="${item.id}"></td>
                <td>${item.competitionNumber}</td>
                <td><a href="${ctx}/promodel/proModel/viewForm?id=${item.id}">${item.pName}</a></td>
                <td>${fns:getDictLabel(item.proCategory, "project_type", "")}</td>
                <td>${fns:getUserById(item.declareId).name}</td>
                <td>${item.deuser.office.name}</td>
                <td>${item.team.memberNum}</td>
                <td>
                        ${item.team.uName}
                </td>
                <td></td>
                <td></td>
                <td>
                        ${item.finalResult}
                </td>
                <%--<td>年份${item.year}</td>--%>
                <td>
                    <a href="${ctx}/actyw/actYwGnode/designView?groupId=${item.actYw.groupId}&proInsId=${item.procInsId}"
                       target="_blank">
                        <c:choose>
                            <c:when test="${item.state =='1'}">
                                项目已结项
                            </c:when>
                            <c:otherwise>待${map.taskName}</c:otherwise>
                        </c:choose>
                    </a>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${item.state =='1'}">
                            <button type="button" class="btn btn-primary btn-small" disabled>审核</button>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${map.status =='todo'}">
                                    <a class="btn btn-small btn-primary"
                                       href="${ctx}/act/task/auditform?gnodeId=${map.gnodeId}&proModelId=${item.id}
                              &pathUrl=${actionUrl}&taskName=${map.taskName}">审核</a>
                                </c:when>
                                <c:otherwise>
                                    <button type="button" class="btn btn-primary btn-small" disabled>审核</button>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    ${page.footer}
</div>

<%--<div id="modalExport" class="modal hide" tabindex="-1" role="dialog">--%>
    <%--<div class="modal-dialog" role="document">--%>
        <%--<div class="modal-content">--%>
            <%--<div class="modal-header">--%>
                <%--<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span--%>
                        <%--aria-hidden="true">&times;</span></button>--%>
                <%--<span class="modal-title">导出项目</span>--%>
            <%--</div>--%>
            <%--<div class="modal-body">--%>
                <%--<form id="formExport" class="form-inline">--%>
                    <%--<div class="control-group text-center">--%>
                        <%--<label class="radio inline">--%>
                            <%--<input type="radio" name="isAll" value="0">导出当前查询条件项目--%>
                        <%--</label>--%>
                        <%--<label class="radio inline">--%>
                            <%--<input type="radio" name="isAll" checked value="1">导出全部项目--%>
                        <%--</label>--%>
                    <%--</div>--%>
                <%--</form>--%>
            <%--</div>--%>
            <%--<div class="modal-footer">--%>
                <%--<button type="button" class="btn btn-default btn-small btn-sm" data-dismiss="modal">取消</button>--%>
                <%--<button type="button" class="btn btn-primary btn-small btn-sm">确定</button>--%>
            <%--</div>--%>
        <%--</div><!-- /.modal-content -->--%>
    <%--</div><!-- /.modal-dialog -->--%>
<%--</div><!-- /.modal -->--%>

<div id="modalExport" class="modal hide modal-export" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <span class="modal-title">导出项目</span>
            </div>
            <div class="modal-body">
                <form id="formExport" class="form-inline">
                    <div class="control-group text-center">
                        <label class="radio inline">
                            <input type="radio" name="isAll" value="0">导出所有项目
                        </label>
                        <label class="radio inline">
                            <input type="radio" name="isAll" checked value="1">导出当前页项目
                        </label>
                        <label class="radio inline">
                            <input type="radio" name="isAll" checked value="2">导出查询项目
                        </label>
                    </div>
                    <div class="control-group team-sepatator">
                        <strong>自定义导出表单中的分隔符</strong>
                        <div>
                            <span>团队成员姓名和学号之间的分隔符</span>
                            <select name="prefix" class="input-xlarge ">
								<c:forEach var="pref" items="${fns:spiltPrefs()}">
									<c:if test="${pref.isDef }">
										<option value="${pref.key }" selected="selected">${pref.remark }</option>
									</c:if>
									<c:if test="${!pref.isDef }">
										<option value="${pref.key }">${pref.remark }</option>
									</c:if>
								</c:forEach>
							</select>
                            <span style="margin-left:20px;">多个成员之间的分隔符</span>
                            <select name="postfix" class="input-xlarge ">
								<c:forEach var="post" items="${fns:spiltPosts()}">
									<c:if test="${post.isDef }">
										<option value="${post.key }" selected="selected">${post.remark }</option>
									</c:if>
									<c:if test="${!pref.post }">
										<option value="${post.key }">${post.remark }</option>
									</c:if>
								</c:forEach>
							</select>
                        </div>
                        <div>示例：张三/123456，李四/789123（导师同理）</div>
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
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <span class="modal-title">导出项目附件</span>
            </div>
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



<div id="modalRecommendLevel" class="modal hide" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <span class="modal-title">推荐级别</span>
            </div>
            <div class="modal-body">
                <form id="formLevel" class="form-inline">
                    <div class="control-group text-center">
                        <label class="radio inline">
                            <input type="radio" name="isAll" value="0">校级
                        </label>
                        <label class="radio inline">
                            <input type="radio" name="isAll" checked value="1">省级
                        </label>
                        <label class="radio inline">
                            <input type="radio" name="isAll" checked value="1">国家级
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

<div id="modalCheck" class="modal hide" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <span class="modal-title">批量审核</span>
            </div>
            <div class="modal-body">
                <form id="formCheck" class="form-inline">
                    <div class="control-group text-center" style="padding-top:15px;">
                        <select name="setScore" id="setScore" style="width:100px;margin-right:8px;">
                            <option value="0">大于</option>
                            <option value="1">大于等于</option>
                            <option value="2">小于</option>
                            <option value="3">小于等于</option>
                            <option value="4">等于</option>
                        </select>
                        <input type="number" id="scoreText" value="60" name="scoreText" style="width:35px;margin-right: 5px;"><span>分</span>
                        <select name="isPass" id="isPass" style="width:100px;margin-left:8px;">
                            <option value="0">通过</option>
                            <option value="1">不通过</option>
                        </select>

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
    $(function () {
        window.parent.sideNavModule.changeUnreadTag('${actywId}');
        getExpInfo();
        checkFirstTask();
    })

    $(document).ready(function () {
        $("#ps").val($("#pageSize").val());
    });

    function page(n, s) {
        $("#pageNo").val(n);
        $("#pageSize").val(s);
        $("#searchForm").submit();
        return false;
    }
    function selectAll(ob) {
        if ($(ob).attr("checked")) {
            $("input[name='subck']:checkbox").attr("checked", true);
        } else {
            $("input[name='subck']:checkbox").attr("checked", false);
        }
    }

    function subckchange(ob) {
        if (!$(ob).attr("checked")) {
            $("#selectAllbtn").attr("checked", false);
        }
    }


    $(function(){
        var $contentTableInput = $('#contentTable input');
        var $recommendLevel = $('.recommend-level');
        var $batchPass = $('.batch-pass');
        var $batchCheck = $('.batch-check');
        var $modalRecommendLevel = $('#modalRecommendLevel');
        var $modalCheck = $('#modalCheck');
        var $formLevel = $('#formLevel');
        var $formCheck = $('#formCheck');
        var $dataCount = $('.data-count');
        $contentTableInput.change(function(){
            var checkedLen = $("input[name='subck']:checkbox:checked").length;
            $dataCount.find('span').text(checkedLen);
            if(checkedLen > 0){
                $recommendLevel.removeAttr('disabled');
                $batchPass.removeAttr('disabled');
                $batchCheck.removeAttr('disabled');
            }else{
                $recommendLevel.attr('disabled','disabled');
                $batchPass.attr('disabled','disabled');
                $batchCheck.attr('disabled','disabled');
            }
        })
        $batchPass.click(function(){
            confirmx("确定批量审核通过？",function(){

            })
        })
        $recommendLevel.click(function(){
            $modalRecommendLevel.modal('show');
        })
        $batchCheck.click(function(){
            $modalCheck.modal('show');
        })

        $modalRecommendLevel.find('.btn-primary').on('click', function () {
            $formLevel[0].reset();
            $modalRecommendLevel.modal('hide');
        })
        $modalCheck.find('.btn-primary').on('click', function () {
            $formCheck[0].reset();
            $modalCheck.modal('hide');
        })
    })




    //检查是否为第一个任务节点.
    function checkFirstTask() {
        $.ajax({
            type: "GET",
            url: "${ctx}/actyw/gnode/ajaxGetFirstTaskByYwid/${actywId}",
            dataType: "json",
            success: function (data) {
                if (data.status && (data.datas != null)) {
                    if ((data.datas.id == '${gnodeId}') || (data.datas.parentId == '${gnodeId}')) {
                        $("#impBtn").show();
                        return;
                    }
                }
                $("#impBtn").hide();
            }
        });
    }

    function expData() {
        $modalExportFile.modal('show');
    }

    var isFirst = true;
    var exportTimer = null;
    function getExpInfo() {
        $.ajax({
            type: "GET",
            url: "/a/exp/getExpGnode",
            data: "gnodeId=${gnodeId}",
            dataType: "json",
            success: function (data) {
                if (data) {
                	$("#expTable").attr("style", "display:");
                    if(data.total){
                        $("#expTr").children('td').eq(1).html("[项目数]" + data.total);
                	}
                	/* if(data.success){
                    	$("#expTr").children('td').eq(2).html("[文件]" + data.success);
                	} */
                	if(data.startDate){
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
                  			exportTimer = setInterval(function(){getExpInfoSub(data.id);},5000);
                    	}
                    }
                } else {
                	if(!isFirst){location.reload();}
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
                	$("#expTable").attr("style", "display:");
                    if(data.total){
                        $("#expTr").children('td').eq(1).html("[项目数]" + data.total);
                	}
                	/* if(data.success){
                    	$("#expTr").children('td').eq(2).html("[文件]" + data.success);
                	} */
                	if(data.startDate){
                    	$("#expTr").children('td').eq(3).html("[开始时间]" + data.startDate);
                	}
                    if (data.isComplete == '1' && data.sa) {
                        $("#expTr").children('td').eq(4).html("[状态]可下载");
                    }
                    if (data.isComplete == '1' && !data.sa) {
                        $("#expTr").children('td').eq(4).html("[状态]导出失败");
                    }
                    if (data.isComplete == '0') {
                        $("#expTr").children('td').eq(4).html("[状态]文件准备中...");
                    }
                    if (data.isComplete == '1' && data.sa) {
                        $("#expTr").children('td').eq(5).html("<a href='/a/ftp/ueditorUpload/downFile?url=" + data.sa.url + "'>下载文件</a>");
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

    function toImpPage(isQuery, actywId, gnodeId) {
        var href = "${ctx}/impdata/promodellist?actywId=" + actywId;
        if (isQuery == true) {
            location.href = href;
        } else {
            if (gnodeId == null) {
                alert("流程节点不能为空");
                return;
            }
            location.href = href + "&gnodeId=" + gnodeId;
        }
    }

    var $modalExport = $('#modalExport');
    var $modalExportFile = $('#modalExportFile');
    var $formExport = $('#formExport');
    var $formExportFile = $('#formExportFile');
    function toExpPage() {
        $modalExport.modal('show');
    }
    function getHparam($form){
		var isAll = $form.find('input[name="isAll"]:checked').val()
        var proCategory = $("#proCategory").val();
        var officeId = $("#officeId").val();
        var queryStr = $("#queryStr").val();
   	    var hrefparam;
        if(isAll == '0'){
	   	    hrefparam = "&isAll=" + isAll;
        }else if(isAll == '2'){
	   	    hrefparam = "&isAll=" + isAll;
	   	    if(proCategory){
	   	    	hrefparam += "&proCategory=" + proCategory;
	   	    }
	   	    if(officeId){
	   	    	hrefparam += "&officeId=" + officeId;
	   	    }
	   	    if(queryStr){
	   	    	hrefparam += "&queryStr=" + queryStr;
	   	    }
        }else{

        }
        return hrefparam;
	}
    $modalExport.find('.btn-primary').on('click', function () {
        location.href="${ctx}/exp/expData/${actywId}?gnodeId=" + '${gnodeId}'+getHparam($formExport);
        $formExport[0].reset();
        $modalExport.modal('hide');
    })
    $modalExportFile.find('.btn-primary').on('click', function () {
    	$(".expbtn").removeAttr("onclick", "expData()");
        $(".expbtn").attr("disabled", true);
        $.get("/a/exp/expByGnode?gnodeId=${gnodeId}&actywId=${actywId}" + getHparam($formExportFile), {}, function (data) {
            if (data.ret == "1") {
                $("#expTr").children('td').eq(1).html("[项目数]");
                /* $("#expTr").children('td').eq(2).html("[文件]"); */
                $("#expTr").children('td').eq(3).html("[开始时间]");
                $("#expTr").children('td').eq(4).html("[状态]");
                $("#expTr").children('td').eq(5).html("");
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

    <%--function projectExportData() {--%>
    <%--var actywId = '${actywId}';--%>
    <%--var proCategory = $("#proCategory").val();--%>
    <%--var officeId = $("#officeId").val();--%>
    <%--var queryStr = $("#queryStr").val();--%>
    <%--location.href="${ctx}/exp/expByGnode?actywId=" + actywId--%>
    <%--+"&officeId=" + officeId + "&queryStr=" + queryStr + "&gnodeId=" + '${gnodeId}';--%>
    <%--}--%>

</script>
</body>
</html>