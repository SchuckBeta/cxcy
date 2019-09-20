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
    <script type="text/javascript" src="${ctxJs}/cyjd/checkall.js"></script>
    <script type="text/javascript" src="${ctxJs}/cyjd/publishExPro.js"></script>
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
    <script>
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
        $(function () {
            $("#ps").val($("#pageSize").val());
        })
    </script>
</head>
<body>

<div class="container-fluid">
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <input id="actywId" name="actywId" type="hidden" value="${actywId}"/>
    <form:form id="searchForm" modelAttribute="proModel" action="${actionUrl}" method="post"
        class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input type="hidden" id="orderBy" name="orderBy" value="${page.orderBy}"/>
        <input type="hidden" id="orderByType" name="orderByType" value="${page.orderByType}"/>
        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">项目年份</label>
                <div class="controls">
                    <form:input path="year" readonly="true" maxlength="20" class="input-medium Wdate"
                                value="${proModel.year}"
                                onclick="WdatePicker({isShowToday: false, dateFmt:'yyyy',isShowClear:true});"/>

                </div>
            </div>
            <div class="control-group">
                <label class="control-label">项目类别</label>
                <div class="controls">
                    <form:select path="proCategory" class="input-medium">
                        <form:option value="" label="所有类别"/>
                        <form:options items="${fns:getDictList('project_type')}" itemLabel="label" itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group" style="margin-right: 470px;">
                <label class="control-label">所属学院</label>
                <div class="controls">
                    <form:select id="officeId" path="deuser.office.id" class="input-medium form-control">
                        <form:option value="" label="所有学院"/>
                        <form:options items="${fns:findColleges()}" itemLabel="name" itemValue="id"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
        </div>
        <div class="search-btn-box">
            <form:input class="input-xlarge" path="queryStr" htmlEscape="false" maxlength="100" autocomplete="off"
                        placeholder="项目编号/项目名称/负责人模糊搜索"/>
            <button id="searchBtn" type="submit" class="btn btn-primary">查询</button>
        </div>
        <div class="text-right mgb-20">
            <shiro:hasPermission name="sys:user:import">
               	<button id="impBtn" type="button" class="btn btn-primary" onclick="toImpPage(true, '${actywId}', '');">导入</button>
            </shiro:hasPermission>
            <button type="button" class="btn btn-primary" onclick="projectExport()">导出</button>
            <shiro:hasPermission name="excellent:projectShow:edit">
	            <button type="button" class="btn btn-primary" disabled data-toggle="publishExPro">发布优秀项目</button>
            </shiro:hasPermission>
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
                <th><input type="checkbox" data-toggle="checkall"  name="allCheck"></th>
                <th width="140" data-name="a.competition_number"><a class="btn-sort" href="javascript:void(0);">项目编号<i
                        class="icon-sort"></i></a></th>
                <th width="25%" data-name="a.p_name"><a class="btn-sort" href="javascript:void(0);">项目名称<i
                        class="icon-sort"></i></a></th>
                <th>项目类别</th>
                <th data-name="u.name"><a class="btn-sort" href="javascript:void(0);">负责人<i
                        class="icon-sort"></i></a></th>
                <th data-name="o6.name"><a class="btn-sort" href="javascript:void(0);">学院<i
                        class="icon-sort"></i></a></th>
                <th>组人数</th>
                <th>指导老师</th>
                <th>证书</th>
                <th>项目结果</th>
                <th>年份</th>
                <th>状态</th>
                <th width="110">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.list}" var="item">
                <c:set var="map" value="${fns:getActByPromodelId(item.id)}"/>
                <tr>
                    <td><input type="checkbox" data-toggle="checkitem" name="projectId" value="${item.id}"></td>
                    <td>${item.competitionNumber}</td>
                    <td><a href="${ctx}/promodel/proModel/viewForm?id=${item.id}">${item.pName}</a></td>
                    <td>${fns:getDictLabel(item.proCategory, "project_type", "")}</td>
                    <td>${fns:getUserById(item.declareId).name}</td>
                    <td>${item.deuser.office.name}</td>
                    <td>${item.team.memberNum}</td>
                    <td>
                            ${item.team.uName}
                    </td>
                    <td>
                        <c:forEach items="${item.scis}" var="sci">
                            <a href="javascript:void(0)" class="look-type" sciid="${sci.id}"
                               style="color:#53BEFF;">${sci.name}</a>
                            <br>
                        </c:forEach>
                    </td>
                    <td>
                            ${item.finalResult}
                    </td>
                    <td>
                            ${item.year}
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${item.state == '1' and empty item.procInsId}">
                                项目已结项
                            </c:when>
                            <c:otherwise>
                                <a href="${ctx}/actyw/actYwGnode/designView?groupId=${item.actYw.groupId}&proInsId=${item.procInsId}"
                                   target="_blank">
                                    <c:choose>
                                        <c:when test="${item.state =='1'}">
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
                                <a class="btn btn-primary btn-small"
                                   href="${ctx}/promodel/proModel/projectEdit?id=${item.id}&secondName=变更">
                                    变更
                                </a>
                                <a class="btn btn-small btn-default" href="${ctx}/promodel/proModel/promodelDelete?id=${item.id}"
                                   onclick="return confirmx('会删除项目相关信息,确认要删除吗？', this.href)">删除</a>
                            </shiro:hasPermission>
                            <button class="btn btn-small btn-primary" onclick="getAuditInfo('${item.id}')">审核记录</button>
                                <%--<c:if test="${isDel == '1'}">--%>
                                <%--</c:if>--%>
                            <a class="btn btn-small btn-primary" href="${ctx}/promodel/proModel/process?id=${item.id}">进度跟踪</a>
		                    <%--<shiro:hasPermission name="excellent:projectShow:edit">--%>
	                            <%--<a class="btn btn-small btn-primary" href="${ctx}/excellent/projectShowForm?projectId=${item.id}">展示</a>--%>
                            <%--</shiro:hasPermission>--%>
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
                    <span>审核记录</span>
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
                                    {{item.auditName}}：{{item.result}}
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
<div id="dialog-look" title="预览">
    <div class="dialog-look-banner">
        <a href="javascript:;"><span class="pre"><</span></a>
        <a href="javascript:;"><span class="next">></span></a>

        <div class="cert-content">
        </div>

    </div>
</div>
<script>
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


    function projectExport() {
        var actywId = '${actywId}';
        var year = $("#year").val();
        var proCategory = $("#proCategory").val();
        var officeId = $("#officeId").val();
        var queryStr = $("#queryStr").val();
        location.href = "${ctx}/exp/expData/" + actywId + "?year=" + year
                + "&proCategory=" + proCategory + "&officeId=" + officeId + "&queryStr=" + queryStr;

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