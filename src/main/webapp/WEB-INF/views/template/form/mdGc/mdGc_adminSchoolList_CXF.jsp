<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript" src="${ctxJs}/backTable/sort.js"></script><!--排序js-->
    <style>
        .modal-common-exp{
          position: absolute;
          left: 50%;
          transform: translateX(-50%);
          margin-left:0
        }
    </style>
<body>
<div class="container-fluid">
    <%--<div class="edit-bar clearfix">--%>
    <%--<div class="edit-bar-left">--%>
    <%--<span>${menuName}</span>--%>
    <%--<i class="line weight-line"></i>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>

    <form:form id="searchForm" modelAttribute="proModelMdGc" action="${actionUrl}" method="post"
               class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input type="hidden" id="orderBy" name="orderBy" value="${page.orderBy}"/>
        <input type="hidden" id="orderByType" name="orderByType" value="${page.orderByType}"/>
        <input id="actywId" name="actywId" type="hidden" value="${actywId}"/>
        <input id="gnodeId" name="gnodeId" type="hidden" value="${gnodeId}"/>

        <div class="col-control-group">
            <div class="control-group" style="margin-right: 410px;">
                <label class="control-label">所属学院</label>
                <div class="controls">
                    <form:select id="officeId" path="proModel.deuser.office.id" class="input-medium form-control">
                        <form:option value="" label="所有学院"/>
                        <form:options items="${fns:findColleges()}" itemLabel="name" itemValue="id"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
        </div>
        <div class="search-btn-box">
            <form:input id="queryStr" class="input-xlarge" path="proModel.queryStr" htmlEscape="false" maxlength="100" autocomplete="off"
                        placeholder="编号/作品名称/负责人模糊搜索"/>
            <button id="searchBtn" type="submit" class="btn btn-primary">查询</button>
            <shiro:hasPermission name="sys:user:import">
            	<c:if test="${fns:isFirstMenu(actywId,gnodeId)}">
		            <button id="impBtn" type="button" class="btn btn-primary" style="display: none;"
		                    onclick="toImpPage(false, '${actywId}', '${gnodeId}')">导入
		            </button>
		            <button id="popBtnGJ" type="button" class="btn btn-primary" style="display: none;"
		                    onclick="toPopPage('10','10', '${actywId}', '${gnodeId}')">互联网+国赛导入
		            </button>
		            <%--<button id="popBtn" type="button" class="btn btn-primary" style="display: none;"--%>
		                    <%--onclick="toPopPage('20','20', '${actywId}', '${gnodeId}')">互联网+--%>
		            <%--</button>--%>
	            </c:if>
            </shiro:hasPermission>
            <button type="button" class="btn btn-primary" onclick="toExpPage()">导出</button>
            <button class="btn btn-primary expbtn" disabled type="button">导出附件</button>
            <button id="btnSubmit1" type="button" class="btn btn-primary" data-toggle="modal"
                    data-target="#myModal">批量审核
            </button>
            <%--<button type="button" class="btn btn-primary" onclick="projectExport()">导出</button>--%>
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
                <td>
                    [文件]
                </td>
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
            <th width="32"><input type="checkbox" id="check_all" data-flag="false"></th>
            <th width="140" data-name="pm.competition_number"><a class="btn-sort" href="javascript:void(0);">编号<i class="icon-sort"></i></a></th>
            <th width="25%" data-name="pm.p_name"><a class="btn-sort" href="javascript:void(0);">作品名称<i class="icon-sort"></i></a></th>
            <th data-name="u.name"><a class="btn-sort" href="javascript:void(0);">负责人<i class="icon-sort"></i></a></th>
            <th data-name="o6.name"><a class="btn-sort" href="javascript:void(0);">学院<i class="icon-sort"></i></a></th>
            <th>组人数</th>
            <th>指导老师</th>
            <th>报名日期</th>
            <th>${menuName}结果</th>
            <th>获奖情况</th>
            <th>年份</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="item">
            <c:set var="map" value="${fns:getActByPromodelId(item.proModel.id)}"/>
            <tr>
                <td class="checkone">
                    <c:if test="${map.status =='todo'}">
                        <input type="checkbox" value="${item.proModel.id}:${gnodeId}" name="boxTd">
                    </c:if>
                </td>
                <td>${item.proModel.competitionNumber}</td>
                <td>
                    <a href="${ctx}/promodel/proModel/viewForm?id=${item.proModel.id}&taskName=${map.taskName}">${item.proModel.pName}</a>
                </td>

                <td>${fns:getUserById(item.proModel.declareId).name}</td>
                <td>${item.proModel.deuser.office.name}</td>
                <td>${item.proModel.team.memberNum}</td>
                <td>
                        ${item.proModel.team.uName}
                </td>
                <td><fmt:formatDate value="${item.createDate}" pattern="yyyy-MM-dd"/></td>
                <td>
                        ${item.proModel.finalResult}
                </td>
                <td>
                 ${fns:getDictLabel(item.proModelMdGcHistory.result, item.proModelMdGcHistory.type, "")}
                </td>
                <td>${item.proModel.year}</td>
                <td>
                    <a href="${ctx}/actyw/actYwGnode/designView?groupId=${item.proModel.actYw.groupId}&proInsId=${item.proModel.procInsId}"
                       target="_blank">
                        <c:choose>
                            <c:when test="${item.proModel.state eq '1'}">
                                项目已结项
                            </c:when>
                            <c:otherwise>待${map.taskName}</c:otherwise>
                        </c:choose>
                    </a>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${item.proModel.state =='1'}">
                            <button type="button" class="btn btn-primary btn-small" disabled>审核</button>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${map.status =='todo'}">
                                    <a class="btn btn-small btn-primary"
                                       href="${ctx}/act/task/auditform?gnodeId=${map.gnodeId}&proModelId=${item.proModel.id}
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
<!-- Modal -->
<div id="myModal" data-backdrop="static"  class="modal hide" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">批量审核</h3>
    </div>
    <div class="modal-body" id="selectArea">
        <form class="form-horizontal" id="addForm" name="addForm" method="post" novalidate>
            <div class="control-group">
                <label class="control-label" style="width: 175px;">需要将所选项目批量审核为</label>
                <div class="controls" style="margin-left: 190px;">
                    <select class="form-control" id="leveldialog" name="leveldialog" required>
                        <option value="">--请选择--</option>
                        <%--<option value="0000000155">推荐省赛</option>--%>
                        <%--<option value="0000000156">不通过</option>--%>
                        <c:forEach items="${actYwStatusList}" var="item">
                            <option value="${item.status}">${item.state}</option>
                        </c:forEach>

                    </select>
                </div>
            </div>
        </form>
        <div class="buffer_gif" style="text-align:center;padding:20px 0px;display:none;" id="bufferImg">
            <img src="${ctxImg}/jbox-loading1.gif" alt="缓冲图片">
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn btn-primary" aria-hidden="true" id="confirmBtn"
                onclick="doBatch('/a/projectBatch/mdGcShcoolBatch');">确定
        </button>
        <button class="btn btn-default" data-dismiss="modal">取消</button>
    </div>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>


<div id="modalExport" class="modal hide" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <span class="modal-title">导出大赛</span>
            </div>
            <div class="modal-body">
                <form id="formExport" class="form-inline">
                    <div class="control-group text-center">
                        <label class="radio inline">
                            <input type="radio" name="isAll" value="0">导出当前查询条件大赛
                        </label>
                        <label class="radio inline">
                            <input type="radio" name="isAll" checked value="1">导出全部大赛
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

<div id="modalExportFile" class="modal hide" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <span class="modal-title">导出大赛附件</span>
            </div>
            <div class="modal-body">
                <form id="formExportFile" class="form-inline">
                    <div class="control-group text-center">
                        <label class="radio inline">
                            <input type="radio" name="isAll" value="0">导出当前查询条件大赛附件
                        </label>
                        <label class="radio inline">
                            <input type="radio" name="isAll" checked value="1">导出全部大赛附件
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






<div id="modalCommonExp" class="modal-common-exp modal hide" tabindex="-1" role="dialog" aria-labelledby="myModalTitle" aria-hidden="true"
     data-backdrop="static" data-keyboard="false" style="width: 900px;top:4%">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <span class="modal-title" id="myModalTitle">导入</span>
            </div>
            <div id="modalCommonExpBody" class="modal-body" style="height: 350px;overflow-y: hidden;max-height: none;">
                <iframe id="iframe" src="" name="iframeName"
                        frameborder="0" style="width: 100%; height: 100%;">
                </iframe>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default btn-small btn-sm" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>


<script src="${ctxJs}/gcProject/auditList.js?v=1"></script>

<script>

        var $modalCommonExp = $('#modalCommonExp');
        var $iframe = $('#modalCommonExp #iframe');
        var iframeSrc = '';
        var $modalCommonExpBody = $('#modalCommonExp .modal-body');
        var iframe = document.getElementById("iframe");

        function toPopPage(ieId, operType, actywId, gnodeId) {
            iframeSrc = "${ctx}/iep/iepTpl/stepList?id=" + ieId + "&operType=" + operType + "&actywId=" + actywId + "&gnodeId=" + gnodeId;
            $modalCommonExp.modal('show');

        }
        $modalCommonExp.on('shown.bs.modal', function () {
            $iframe.attr('src',iframeSrc);
        });

        $modalCommonExp.on('hidden.bs.modal', function () {
//            $iframe.attr('src','');
//            $modalCommonExpBody.height(350);
            location.reload();
        });









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
                        $("#impBtnGJ").show();
                        $("#popBtnGJ").show();
                        $("#popBtn").show();
                        return;
                    }
                }
                $("#impBtn").hide();
                $("#impBtnGJ").hide();
                $("#popBtnGJ").hide();
                $("#popBtn").hide();
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
	    toImpPage(isQuery, actywId, gnodeId, null);
    }
    function toImpPage(isQuery, actywId, gnodeId, tplType) {
        var href = "${ctx}/impdata/promodelgcontestlist?actywId=" + actywId;
        if (isQuery == true) {
            location.href = href;
        } else {
            if (gnodeId == null) {
                alert("流程节点不能为空");
                return;
            }
            if((tplType != undefined) && (tplType != null) && (tplType != '')){
                location.href = href + "&gnodeId=" + gnodeId + "&tplType=" + tplType;
            }else{
                location.href = href + "&gnodeId=" + gnodeId;
            }
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
        if(isAll == '1'){
	   	    hrefparam = "&isAll=" + isAll;
        }else{
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