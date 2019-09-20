<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxStatic}/vue/vue.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/backTable/sort.js"></script><!--排序js-->
    <style type="text/css">
        .modal-large {
            width: 800px;
        }

        .modal {
            transform: translateX(-16%);
        }

        .ui-dialog .ui-dialog-titlebar {
            margin: -2.5px -2.5px 0 -2.5px;
            border: none;
            border-radius: 0;
        }

        .dialog-look .ui-button .ui-icon, .dialog-together .ui-button .ui-icon {
            display: none;
        }

        .table .actions{
            width: 100px;
            margin: 0 auto;
            text-align: center;
        }
        .table .actions .btn{
            box-sizing: border-box;
        }

        .table .actions .btn:nth-child(3),.table .actions .btn:nth-child(4){
            width: 100%;
            margin-top: 4px;
        }

    </style>
</head>
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

        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">项目年份</label>
                <div class="controls">
                    <form:input path="proModel.year" readonly="true" maxlength="20" class="input-medium Wdate"
                                value="${proModel.year}"
                                onclick="WdatePicker({isShowToday: false, dateFmt:'yyyy',isShowClear:true});"/>

                </div>
            </div>

            <div class="control-group" style="margin-right: 470px;">
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
                        placeholder="项目编号/项目名称/负责人模糊搜索"/>
            <button id="searchBtn" type="submit" class="btn btn-primary">查询</button>
        </div>
        <div class="text-right mgb-20">
        	<!-- 民大查询禁止大赛导入 -->
        	<%-- <shiro:hasPermission name="sys:user:import">
	            <button id="impBtn" type="button" class="btn btn-primary"
	                    onclick="toImpPage(true, '${actywId}', '')">导入
	            </button>
            </shiro:hasPermission> --%>
            <button type="button" class="btn btn-primary" onclick="toExpPage()">导出</button>
            <shiro:hasPermission name="cert:sysCert:make">
                <button type="button" class="btn btn-primary btn-mid" onclick="toCertMakePage()">证书下发</button>
            </shiro:hasPermission>
        </div>


    </form:form>
    <div id="auditInfo">
        <table id="contentTable"
               class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-sort">
            <thead>
            <tr>
                <th width="140" data-name="pm.competition_number"><a class="btn-sort" href="javascript:void(0);">编号<i
                        class="icon-sort"></i></a></th>
                <th width="25%" data-name="pm.p_name"><a class="btn-sort" href="javascript:void(0);">作品名称<i
                        class="icon-sort"></i></a></th>

                <th data-name="u.name"><a class="btn-sort" href="javascript:void(0);">负责人<i
                        class="icon-sort"></i></a></th>
                <th data-name="o6.name"><a class="btn-sort" href="javascript:void(0);">学院<i
                        class="icon-sort"></i></a></th>
                <th>组人数</th>
                <th>指导老师</th>
                <th>报名日期</th>
                <th>项目结果</th>
                <th>获奖情况</th>
                <th>年份</th>
                <th>状态</th>
                <th width="110">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.list}" var="item">
                <c:set var="map" value="${fns:getActByPromodelId(item.proModel.id)}"/>
                <tr>
                    <td>${item.proModel.competitionNumber}</td>
                    <td><a href="${ctx}/promodel/proModel/viewForm?id=${item.proModel.id}">${item.proModel.pName}</a>
                    </td>

                    <td>${fns:getUserById(item.proModel.declareId).name}</td>
                    <td>${item.proModel.deuser.office.name}</td>
                    <td>${item.proModel.team.memberNum}</td>
                    <td>
                            ${item.proModel.team.uName}
                    </td>
                    <td>
                        <fmt:formatDate value="${item.createDate}" pattern="yyyy-MM-dd"/>
                    </td>
                    <td>
                            ${item.proModel.finalResult}
                    </td>
                    <td>
                        <ul style="list-style: none;padding: 0;margin: 0">
                        <c:forEach items="${item.proModelMdGcHistoryList}" var="proHis">
                            <li>
                                ${fns:getDictLabel(proHis.type, "0000000169", "")} : ${fns:getDictLabel(proHis.result, proHis.type, "")}
                            </li>
                        </c:forEach>

                        </ul>
                    </td>
                    <td>
                            ${item.proModel.year}
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${item.proModel.state == '1'}">
                                项目已结项
                            </c:when>
                            <c:otherwise>
                                <a href="${ctx}/actyw/actYwGnode/designView?groupId=${item.proModel.actYw.groupId}&proInsId=${item.proModel.procInsId}"
                                   target="_blank">
                                    <c:choose>
                                        <c:when test="${item.proModel.state =='1'}">
                                            项目已结项
                                        </c:when>
                                        <c:otherwise>待${map.taskName}</c:otherwise>
                                    </c:choose>
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <div class="actions">
                        <shiro:hasPermission name="promodel:promodel:modify">
                            <a class="btn btn-primary btn-small" href="${ctx}/promodel/proModel/gcontestEdit?id=${item.proModel.id}&secondName=变更">
                                        变更
                            </a>
                            <a class="btn btn-small btn-default" href="${ctx}/promodel/proModel/promodelDelete?id=${item.proModel.id}"
                                                           onclick="return confirmx('会删除项目相关信息,确认要删除吗？', this.href)">删除</a>
                        </shiro:hasPermission>
                        <button class="btn btn-small btn-primary" type="button" onclick="getAuditInfo('${item.proModel.id}')">审核记录
                                                </button>
                        <%--<c:if test="${isDel == '1'}">--%>
                            <%----%>
                        <%--</c:if>--%>
                        <a class="btn btn-small btn-primary"  href="${ctx}/promodel/proModel/process?id=${item.proModel.id}">进度跟踪</a>
                            </div>
                    </td>
                </tr>
            </c:forEach>
            </tbody>

        </table>
        <div v-show="auditInfoShow"
                    style="position: fixed;width: 100%;height: 100%;top: 0;left: 0;display: none;z-index: 1000">
                   <div v-drag style="z-index: 1050" class="modal modal-calendar modal-large">
                       <div class="modal-header">
                           <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                                   @click="auditInfoShow=false">&times;
                           </button>
                           <span class="modal-title">审核记录</span>
                       </div>
                       <div class="modal-body">
                           <table class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
                               <thead>
                               <tr>
                                   <th style="width:15%">审核动作</th>
                                   <th style="width:20%">审核时间</th>
                                   <th style="width:10%">审核人</th>
                                   <th style="width:10%">审核结果</th>
                                   <th style="width:45%">建议及意见</th>
                               </tr>
                               </thead>
                               <tbody>
                               <tr v-for="item in auditInfoList">
                                   <template v-if="item.id">
                                       <td>
                                           {{item.auditName}}
                                       </td>
                                       <td>
                                           {{item.updateDate}}
                                       </td>
                                       <td>
                                           {{item.user ? item.user.name : ''}}
                                       </td>
                                       <td>
                                           {{item.result}}
                                       </td>
                                       <td style="word-break: break-all">
                                           {{item.suggest}}
                                       </td>
                                   </template>
                                   <template v-else>
                                       <td colspan="5" style="color: red; text-align: right;font-weight: bold">
                                           {{item.auditName}}平均分：{{item.result}}
                                       </td>
                                   </template>
                               </tr>
                               <tr v-show="auditInfoList.length < 1">
                                   <td colspan="5">没有数据</td>
                               </tr>
                               </tbody>
                           </table>
                       </div>
                       <div class="modal-footer">
                           <button class="btn btn-default" @click="auditInfoShow=false">关闭</button>
                       </div>

                   </div>
                   <div class="modal-backdrop in"></div>
               </div>
    </div>
    ${page.footer}
</div>
<%--<div id="dialog-look" title="预览">--%>
    <%--<div class="dialog-look-banner">--%>
        <%--<a href="javascript:;"><span class="pre"><</span></a>--%>
        <%--<a href="javascript:;"><span class="next">></span></a>--%>

        <%--<div class="cert-content">--%>
        <%--</div>--%>

    <%--</div>--%>
<%--</div>--%>


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

<script>
    $(document).ready(function () {
        window.parent.sideNavModule.changeUnreadTag('${actywId}');
        //getExpInfo();
        //checkFirstTask();
        $("#ps").val($("#pageSize").val());
    });

    function page(n, s) {
        $("#pageNo").val(n);
        $("#pageSize").val(s);
        $("#searchForm").submit();
        return false;
    }

    var auditInfo = new Vue({
        el: '#auditInfo',
        data: function () {
            return {
                auditInfoShow: false,
                auditInfoList: []
            }
        },
        watch: {
            'auditInfoShow': function (val) {
                this.auditInfoShow = val;
                if (!val) {
                    this.auditInfoList = [];
                }
            }
        },
        directives: {
            drag: function (element, binding, vnode) {
                $(element).draggable({
                    handle: ".modal-header",
                })
            }
        },
        mounted: function () {
        }
    });

    function getAuditInfo(proModelId) {
        var buildListXhr = $.get('${ctx}/promodel/proModel/auditInfo/' + proModelId);
        buildListXhr.success(function (data) {
            $.each(data, function (index, item) {
                auditInfo.$data.auditInfoList.push(item);
            });
            auditInfo.$data.auditInfoList = data;
            auditInfo.$data.auditInfoShow = true;
        });
    }

    function toImpPage(isQuery, actywId, gnodeId) {
        var href = "${ctx}/impdata/promodelgcontestlist?actywId=" + actywId;
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
    var $formExport = $('#formExport');
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
    /* function toExpPage() {
        var actywId = '${actywId}';
        var year = typeof($("#year").val()) == "undefined" ? "" : $("#year").val();
        var proCategory = $("#proCategory").val();
        var officeId = typeof($("#officeId").val()) == "undefined" ? "" : $("#officeId").val();
        var queryStr = typeof($("#queryStr").val()) == "undefined" ? "" : $("#queryStr").val();

        url = "${ctx}/workflow.mdgc/proModelMdGc/export?proModel.year=" + year
            + "&proModel.deuser.office.officeId=" + officeId + "&proModel.queryStr=" + queryStr;
        console.log("url", url);
        location.href = url;

    } */
    $modalExport.find('.btn-primary').on('click', function () {
        //location.href="${ctx}/workflow.mdgc/proModelMdGc/export?actywId=${actywId}"+getHparam($formExport);
        location.href="${ctx}/exp/expData/${actywId}?gnodeId=" + '${gnodeId}'+getHparam($formExport);
        $formExport[0].reset();
        $modalExport.modal('hide');
    })

    function toCertMakePage() {
        location.href = "/a/certMake/list?actywId=" + $("#actywId").val();
    }

    $(function () {


        var wWidth = $(window).width();
        var lookWidth = wWidth * 0.65;
        var lookHeight = lookWidth * 0.63;
        var bannerWidth = lookWidth * 0.91;
        var bannerHeight = lookHeight * 0.86;
        var dialogLook = $("#dialog-look");

        dialogLook.dialog({
            autoOpen: false,
            width: lookWidth,
            dialogClass: 'dialog-look',
            modal: true,
            containment: 'document'
        });

        var startIndex = 0;
        var $index;
        var $exdex;
        var len;
        var lookImg;


        $(".dialog-look-banner .cert-content .cert-content-img").css({
            width: '',
            height: ''
        });

        dialogLook.on('dialogclose', function () {
            $(".dialog-look-banner .cert-content .cert-content-img").css({
                left: '',
                width: '',
                height: ''
            });
            startIndex = 0;
        })

        $('.dialog-look-banner').css({
            width: bannerWidth,
            height: bannerHeight
        });


        $(".look-type").on("click", function () {

            getCertPage($(this).attr("sciid"));

            dialogLook.dialog("open");

            lookImg = $('.dialog-look-banner .cert-content-img');
            len = lookImg.length;

            lookImg.css('visibility', 'hidden');
            lookImg.on('load', function () {
                var lm = $(this);
                var lw = parseInt(lm.css('width').replace("px", ""));
                var lh = parseInt(lm.css('height').replace("px", ""));
                var scaleW = (lw / lh) * bannerHeight;
                var scaleH = (lh / lw) * bannerWidth;
                if (lh < bannerHeight) {
                    lm.css({
                        width: bannerWidth,
                        height: 'auto',
                        marginTop: (bannerHeight - scaleH) / 2
                    });
                } else {
                    lm.css({
                        width: 'auto',
                        height: bannerHeight,
                        marginLeft: (bannerWidth - scaleW) / 2
                    });
                }
                lm.css('visibility', 'visible')
            })

        });


        $(".next").click(function () {
            //手动轮播
            var nextLen = lookImg.length;
            $index = startIndex;
            $exdex = startIndex;
            $index++;
            if (nextLen > 1) {
                if ($index > len - 1) {
                    $index = 0;
                }
                startIndex = $index;
                next();
            }
        });

        //上一页的点击事件
        $(".pre").click(function () {
            var preLen = lookImg.length;
            $index = startIndex;
            $exdex = startIndex;
            $index--;
            if (preLen > 1) {
                if ($index < 0) {
                    $index = len - 1;
                }
                ;
                startIndex = $index;
                pre();
            }
        });
        //这里为右移和左移的事件函数。
        //右移基本原理就是先让exdex定位的left左移百分百，而选中的当前页从屏幕右边移入,left变为0
        function next() {
            lookImg.eq($index).stop(true, true).css("left", "100%").animate({"left": "0"});
            lookImg.eq($exdex).stop(true, true).css("left", "0").animate({"left": "-100%"});
        }

        function pre() {
            lookImg.eq($index).stop(true, true).css("left", "-100%").animate({"left": "0"});
            lookImg.eq($exdex).stop(true, true).css("left", "0").animate({"left": "100%"});
        }

        $('.dialog-look-banner .pre, .next').css('top', bannerHeight / 2 - 30);

    });

    function getCertPage(id) {
        $(".cert-content").html("");
        return $.ajax({
            type: 'post',
            url: '/a/cert/getCertIns',
            async: false,
            dataType: "json",
            data: {
                certinsid: id
            },
            success: function (data) {
                if (data) {
                    var indx = 0;
                    var html = "";
                    $.each(data, function (i, v) {
                        if (indx == 0) {
                            html = html + '<div><img class="cert-content-img first" src="' + v.imgUrl + '"></div>';
                        } else {
                            html = html + '<div><img class="cert-content-img" src="' + v.imgUrl + '"></div>';
                        }
                        indx++;
                    });
                    $(".cert-content").html(html);
                }
            }
        });
    }
</script>
</body>
</html>