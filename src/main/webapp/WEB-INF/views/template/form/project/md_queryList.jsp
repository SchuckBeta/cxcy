<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title></title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript" src="${ctxJs}/cyjd/checkall.js"></script>
    <script type="text/javascript" src="${ctxJs}/cyjd/publishExPro.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#ps").val($("#pageSize").val());
        });
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
        function expData() {
            $.get('/a/proprojectmd/checkExpAll?actywId=' + $("#actywId").val(), {}, function (data) {
                if (data.ret == "0") {
                    confirmx("没有需要导出的数据，继续导出？", function () {
                        location.href = "/a/proprojectmd/expAll?actywId=" + $("#actywId").val();
                    });
                } else {
                    location.href = "/a/proprojectmd/expAll?actywId=" + $("#actywId").val();
                }
            });
        }
        function selectAll(ob) {
            if ($(ob).attr("checked")) {
                $("input[name='subck']:checkbox").attr("checked", true);
            } else {
                $("input[name='subck']:checkbox").attr("checked", false);
            }
        }
        function resall() {
            var temarr = [];
            $("input[name='subck']:checked").each(function (i, v) {
                temarr.push($(v).val());
            });
            if (temarr.length == 0) {
                alertx("请选择要发布的项目");
                return;
            }
            confirmx("确定发布所选项目到门户网站？", function () {
                $.ajax({
                    type: 'post',
                    url: '/a/excellent/resall',
                    dataType: "json",
                    data: {
                        fids: temarr.join(",")
                    },
                    success: function (data) {
                        if (data) {
                            alertx(data.msg);
                        }
                    }
                });
            });
        }
        function subckchange(ob) {
            if (!$(ob).attr("checked")) {
                $("#selectAllbtn").attr("checked", false);
            }
            /* if($("input[name='subck']:checked").length==0){
             $("#resallbtn").removeAttr("onclick");
             }else{
             $("#resallbtn").attr("onclick","resall()");
             } */
        }
        function toCertMakePage() {
            location.href = "/a/certMake/list?actywId=" + $("#actywId").val();
        }
    </script>
</head>
<body>

<style>

    .ui-dialog .ui-dialog-titlebar {
        margin: -2.5px -2px 0 -2.5px;
        border: none;
        border-radius: 0;
    }

    .ui-dialog .ui-dialog-titlebar-close {
        border: none;
        width: 16px;
        height: 16px;
        right: 8px;
    }
    .ui-widget-header .ui-icon{
        display: none;
    }

</style>

<div class="container-fluid">
    <div class="edit-bar edit-bar-tag clearfix">
        <div class="edit-bar-left">
            <span>${menuName}</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="searchForm" modelAttribute="proModelMd" action="${ctx}/cms/form/queryMenuList" method="post"
               class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="actywId" name="actywId" type="hidden" value="${actywId}"/>

        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">项目类别</label>
                <div class="controls">
                    <form:select path="proModel.proCategory" class="input-medium">
                        <form:option value="" label="所有类别"/>
                        <form:options items="${fns:getDictList('project_type')}" itemLabel="label" itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
        </div>
        <div class="search-btn-box">
            <form:input class="input-xlarge" path="proModel.queryStr" htmlEscape="false" maxlength="100" autocomplete="off"
                        placeholder="项目编号/项目名称/负责人模糊搜索"/>
            <button id="searchBtn" type="submit" class="btn btn-primary">查询</button>
            <shiro:hasPermission name="excellent:projectShow:edit">
            	<button type="button" class="btn btn-primary" disabled data-toggle="publishExPro">发布优秀项目</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="cert:sysCert:make">
	            <button type="button" class="btn btn-primary btn-mid" onclick="toCertMakePage()">证书下发</button>
            </shiro:hasPermission>
        </div>
    </form:form>
    <table id="contentTable"
           class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
        <thead>
        <tr>
            <th><input type="checkbox" data-toggle="checkall"  name="allCheck" id="selectAllbtn" onclick="selectAll(this)"></th>
            <th>项目编号</th>
            <th>申报时间</th>
            <th width="25%">参赛项目名称</th>
            <th>申报人</th>
            <th>项目类型</th>
            <th>证书</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="proModelMd">
            <tr>
               	<td><input type="checkbox" onclick="subckchange(this)" data-toggle="checkitem" name="projectId" value="${proModelMd.proModel.id}"></td>
                <%--<td><input type="checkbox" name="subck" onclick="subckchange(this)" value="${proModelMd.proModel.id}"></td>--%>

                <td>${proModelMd.proModel.competitionNumber}</td>
                <td>
                    <fmt:formatDate value="${proModelMd.proModel.subTime}" pattern="yyyy-MM-dd"/>
                </td>
                <td>
                    <a href="${ctx}/promodel/proModel/viewForm?id=${proModelMd.proModel.id}">${proModelMd.proModel.pName}</a>
                </td>
                <td>${fns:getUserById(proModelMd.proModel.declareId).name}</td>
                <td>
                        ${fns:getDictLabel(proModelMd.proModel.proCategory, "project_type", "")}
                </td>
                <td>
                    <c:forEach items="${proModelMd.proModel.scis}" var="sci">
                        <a href="javascript:void(0)" class="look-type" sciid="${sci.id}"
                           style="color:#53BEFF;">${sci.name}</a>
                        <br>
                    </c:forEach>
                </td>
                <td>

                    <c:choose>
                        <c:when test="${proModelMd.closeState!=null }">
                            <c:if test="${proModelMd.closeState=='0' }">
                                <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${ proModelMd.proModel.procInsId}&grade=1"
                                   class="check_btn btn-pray btn-lx-primary" target="_blank">
                                    结项审核不通过
                                </a>
                            </c:if>
                            <c:if test="${proModelMd.closeState=='1' }">
                                <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${ proModelMd.proModel.procInsId}"
                                   class="check_btn btn-pray btn-lx-primary" target="_blank">
                                    结项审核通过
                                </a>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${proModelMd.midState!=null }">
                                    <c:if test="${proModelMd.midState=='0' }">
                                        <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${ proModelMd.proModel.procInsId}&grade=1"
                                           class="check_btn btn-pray btn-lx-primary" target="_blank">
                                            中期检查不通过
                                        </a>
                                    </c:if>
                                    <c:if test="${proModelMd.midState=='1' }">
                                        <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${ proModelMd.proModel.procInsId}"
                                           class="check_btn btn-pray btn-lx-primary" target="_blank">
                                            中期检查通过
                                        </a>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${proModelMd.setState!=null }">
                                            <c:if test="${proModelMd.setState=='0' }">
                                                <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${ proModelMd.proModel.procInsId}&grade=1"
                                                   class="check_btn btn-pray btn-lx-primary" target="_blank">
                                                    立项审核不通过
                                                </a>
                                            </c:if>
                                            <c:if test="${proModelMd.setState=='1' }">
                                                <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${ proModelMd.proModel.procInsId}"
                                                   class="check_btn btn-pray btn-lx-primary" target="_blank">
                                                    立项审核通过
                                                </a>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${ proModelMd.proModel.procInsId}"
                                               class="check_btn btn-pray btn-lx-primary" target="_blank">
                                                    ${pj:getProModelAuditNameById(proModelMd.proModel.procInsId)}
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                        <%--<a href="${ctx}/promodel/proModel/viewForm?id=${proModelMd.proModel.id}">查看</a>--%>
                    <a class="btn btn-primary btn-small"
                       href="${ctx}/proprojectmd/proModelMd/toModifyPage?proModelId=${proModelMd.proModel.id}">变更</a>
                    <%--<shiro:hasPermission name="excellent:projectShow:edit">--%>
	                    <%--<a class="btn btn-small btn-primary" href="${ctx}/excellent/projectShowForm?projectId=${item.id}">展示</a>--%>
                    <%--</shiro:hasPermission>--%>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
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
                    console.log(data);
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