<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page import="com.oseasy.cms.modules.cms.entity.Site" %>
<%@ page import="com.oseasy.com.pcore.modules.sys.utils.UserUtils" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<% request.setAttribute("user", UserUtils.getUser()); %>
<!--顶部header公用部分-->

<div class="header">
    <div class="mid">
        <!--logo图-->
        <%--<c:if test="${empty fns:ftpImgUrl(fns:curSite().logo)}">--%>
        <%--<img src="${ctxImg}/logo.png" />--%>
        <%--</c:if>--%>
        <%--<c:if test="${not empty fns:ftpImgUrl(fns:curSite().logo)}">--%>
        <%--<img src="${fns:ftpImgUrl(fns:curSite().logo)}" />--%>
        <%--</c:if>--%>
        <div class="logo-box">
            <div class="header-brand">
                <c:if test="${empty fns:ftpImgUrl(fns:curSite().logo)}">
                    <img class="img-responsive" src="${ctxImg}/logo.png"/>
                </c:if>
                <c:if test="${not empty fns:ftpImgUrl(fns:curSite().logo)}">
                    <img class="img-responsive" src="${fns:ftpImgUrl(fns:curSite().logo)}"/>
                </c:if>
            </div>
            <div id="brandTextSlide" class="header-brand-text">
                <%--<c:if test="${empty fns:ftpImgUrl(fns:curSite().logoSite)}">--%>
                <div class="brand-text-item <c:if test="${empty fns:ftpImgUrl(fns:curSite().logoSite)}">active</c:if>">
                    <img class="img-responsive" src="${ctxImg}/s-brandx161.png"/>
                </div>
                <%--</c:if>--%>
                <c:if test="${not empty fns:ftpImgUrl(fns:curSite().logoSite)}">
                    <div class="brand-text-item active">
                        <img class="img-responsive" src="${fns:ftpImgUrl(fns:curSite().logoSite)}"/>
                    </div>
                </c:if>
            </div>
        </div>
        <!--导航部分-->
        <%--<cms:frontCategorysIndex></cms:frontCategorysIndex>--%>
        <ul id="header_info" style="margin-bottom: 0px;">
            <c:forEach var="pitem" items="${fns:getCategorysIndex() }" varStatus="idx">
                    <c:if test="${idx.index < 7 }">
                        <li>
                            <c:if test="${empty pitem.href}">
                                <a href="javascript:void(0);" <%--target="${pitem.target}"--%>><span>${pitem.name }</span></a>
                            </c:if>
                            <c:if test="${not empty pitem.href}">
                                <a href="${ctxFront}/${pitem.href}" <%--target="${pitem.target}"--%>><span>${pitem.name }</span></a>
                            </c:if>
                            <c:if test="${fn:length(pitem.childList) > 0}">
                                <div class="secondlever">
                                    <p>
                                        <c:forEach var="item" items="${pitem.childList }">
                                            <c:if test="${empty item.href}">
                                                <a href="javascript:void(0);" <%--target="${item.target}"--%>>●${item.name }</a>
                                            </c:if>
                                            <c:if test="${not empty item.href}">
                                                <c:if test="${fn:contains(item.href,'javascript:')}">
                                                    <a href="${item.href}" <%--target="${item.target}"--%>>●${item.name }</a>
                                                </c:if>
                                                <c:if test="${not fn:contains(item.href,'javascript:')}">
                                                    <a href="${ctxFront}/${item.href}" <%--target="${item.target}"--%>>●${item.name }</a>
                                                </c:if>
                                            </c:if>
                                        </c:forEach>
                                    </p>
                                </div>
                            </c:if>
                        </li>
                    </c:if>
            </c:forEach>
        </ul>
        <!--登录块-->
    </div>
    <%--<div class="user-wrap">--%>
    <cms:frontLogin user="${user}"></cms:frontLogin>
</div>
<div class="header-help"><a href="javascript:void(0);" target="http://${fns:getSysFrontIp()}/sys/help"><span>下载<br>帮助文档</span></a></div>
</div>
<%--<div class="affix-sidebar">--%>
<%--<a class="affix-item affix-item-help" href="http://${fns:getSysFrontIp()}/f/help" target="_blank"> <span--%>
<%--class="icon-help"></span> <span class="text">帮助文档</span></a>--%>
<%--</div>--%>
<div class="scroll-top">
    <a href="javascript:void(0)">返回顶部</a>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>

<script>
    function checkIsToLogin(data){
        try{
            if(data.indexOf("id=\"imFrontLoginPage\"") != -1){
                return true;
            }
        }catch(e){
            return false;
        }
    }
    function loginTimeout() {
        dialogCyjd.createDialog(0, "未登录或登录超时。请重新登录，谢谢！", {
            dialogClass: 'dialog-cyjd-container none-close',
            buttons: [{
                text: '确定',
                'class': 'btn btn-sm btn-primary',
                click: function () {
                    $(this).dialog('close');
                    top.location = top.location;
                }
            }]
        });
    }
    function onProjectApplyInit(actywId,pid,callFun){
        $.ajax({
            type:'post',
            url:'/f/project/projectDeclare/onProjectApply',
            data: {actywId:actywId,pid:pid},
            success:function(data){
                if(checkIsToLogin(data)){
                    loginTimeout();
                }else{
                    if(data.ret=='0'){
                        $('.btn-save').prop('disabled', true);
                        $('.btn-submit').prop('disabled', true);
                        $('input,select,textarea').prop('disabled', true);
                        dialogCyjd.createDialog(0, data.msg, {
                            dialogClass: 'dialog-cyjd-container none-close',
                            buttons: [{
                                text: '确定',
                                'class': 'btn btn-sm btn-primary',
                                click: function () {
                                    $(this).dialog('close')
                                }
                            }]
                        });
                    }else if(data.ret=='1'){
                        if(callFun)callFun();
                    }else if(data.ret=='2'){
                        dialogCyjd.createDialog(0, data.msg, {
                            dialogClass: 'dialog-cyjd-container none-close',
                            buttons: data.btns && getBtns(data.btns)
                        });
                    }
                }
            }
        });
    }
    function onGcontestApplyInit(actywId,pid,callFun){
        console.log("headerNew.jsp", arguments);
        $.ajax({
            type:'post',
            url:'/f/gcontest/gContest/onGcontestApply',
            data: {actywId:actywId,pid:pid},
            success:function(data){
                if(checkIsToLogin(data)){
                    loginTimeout();
                }else{
                    if(data.ret=='0'){
                        $('.btn-save').prop('disabled', true);
                        $('.btn-submit').prop('disabled', true);
                        $('button[type="submit"]').prop('disabled', true);
                        $('.btn-primary').prop('disabled', true);
                        $('input,select,textarea').prop('disabled', true);
                        dialogCyjd.createDialog(0, data.msg, {
                            dialogClass: 'dialog-cyjd-container none-close',
                            buttons: [{
                                text: '确定',
                                'class': 'btn btn-sm btn-primary',
                                click: function () {
                                    $(this).dialog('close')
                                }
                            }]
                        });
                    }else if(data.ret=='1'){
                        if(callFun)callFun();
                    }else if(data.ret=='2'){
                        dialogCyjd.createDialog(0, data.msg, {
                            dialogClass: 'dialog-cyjd-container none-close',
                            buttons: data.btns && getBtns(data.btns)
                        });
                    }
                }
            }
        });
    }
    function getBtns(btns){
        var btns_arr=[];
        $.each(btns,function(i,v){
            var btns_ob={
                text: v.name,
                'class': 'btn btn-sm btn-primary',
                click: function () {
                    $(this).dialog('close')
                    location.href=v.url;
                }
            };
            btns_arr.push(btns_ob);
        });
        return btns_arr;
    }
    $(function () {
        var headerBrandTextSlide = brandTextSlide('brandTextSlide', 2000, 300);
        headerBrandTextSlide && headerBrandTextSlide();

        var $win = $(window);
        var $scrollTopEle = $('.scroll-top');
        var $scrollTopELeA = $scrollTopEle.find('a');
        var winScrollTop = $(document).scrollTop();
        var scrollTimerId = null;

        $win.on('scroll', function () {
            scrollTimerId && clearTimeout(scrollTimerId);
            winScrollTop = $(document).scrollTop();
            scrollTimerId = setTimeout(function () {
                if (winScrollTop > 30) {
                    $scrollTopEle.show()
                } else {
                    $scrollTopEle.hide()
                }
            }, 30);
        });

        $scrollTopELeA.on('click', function () {
            scrollTimerId && clearTimeout(scrollTimerId);
            $("html,body").animate({"scrollTop": 0}, 400);
        })

    })
</script>