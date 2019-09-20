<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page import="com.oseasy.cms.modules.cms.entity.Site" %>
<%@ page import="com.oseasy.com.pcore.modules.sys.utils.UserUtils" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<% request.setAttribute("user", UserUtils.getUser()); %>
<!--顶部header公用部分-->
<div class="header">
    <div class="container">
        <div class="brand">
            <a href="${ctxFront}"><img style="height: 52px; margin-top: 4px; width: auto" src="${ctxImages}/hlw_logo.png"></a>
            <%--<div class="brand-left">--%>
                <%--<a href="${ctxFront}"><img src="${ctxImg}/logo.png"></a>--%>
            <%--</div>--%>
            <%--<div class="brand-right">--%>
                <%--<div class="brand-logo-item active">--%>
                    <%--<img src="${ctxImg}/s-brandx161.png">--%>
                <%--</div>--%>
            <%--</div>--%>

        </div>
        <cms:frontCategorysIndex></cms:frontCategorysIndex>
        <cms:frontLogin user="${user}"></cms:frontLogin>
    </div>
    <input type="hidden" name="userId" value="${user.id}">
</div>
<div class="header-placeholder"></div>

<div id="dialogCyjd" class="dialog-cyjd"></div>

<script>
    function checkIsToLogin(data) {
        try {
            if (data.indexOf("id=\"imFrontLoginPage\"") != -1) {
                return true;
            }
        } catch (e) {
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
    function onProjectApplyInit(actywId, pid, callFun) {
        $.ajax({
            type: 'post',
            url: '/f/project/projectDeclare/onProjectApply',
            data: {actywId: actywId, pid: pid},
            success: function (data) {
                if (checkIsToLogin(data)) {
                    loginTimeout();
                } else {
                    if (data.ret == '0') {
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
                    } else if (data.ret == '1') {
                        if (callFun)callFun();
                    } else if (data.ret == '2') {
                        dialogCyjd.createDialog(0, data.msg, {
                            dialogClass: 'dialog-cyjd-container none-close',
                            buttons: data.btns && getBtns(data.btns)
                        });
                    }
                }
            }
        });
    }
    function onGcontestApplyInit(actywId, pid, callFun) {
        console.log("headerNew.jsp", arguments);
        $.ajax({
            type: 'post',
            url: '/f/gcontest/gContest/onGcontestApply',
            data: {actywId: actywId, pid: pid},
            success: function (data) {
                if (checkIsToLogin(data)) {
                    loginTimeout();
                } else {
                    if (data.ret == '0') {
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
                    } else if (data.ret == '1') {
                        if (callFun)callFun();
                    } else if (data.ret == '2') {
                        dialogCyjd.createDialog(0, data.msg, {
                            dialogClass: 'dialog-cyjd-container none-close',
                            buttons: data.btns && getBtns(data.btns)
                        });
                    }
                }
            }
        });
    }
    function getBtns(btns) {
        var btns_arr = [];
        $.each(btns, function (i, v) {
            var btns_ob = {
                text: v.name,
                'class': 'btn btn-sm btn-primary',
                click: function () {
                    $(this).dialog('close')
                    location.href = v.url;
                }
            };
            btns_arr.push(btns_ob);
        });
        return btns_arr;
    }
    $(function () {

        //获取logo

        function getBrandLogo() {
            var $brandLogo = $('#brandLogo');
            var brandLogoRight = '<div class="brand-logo-item"> <img src="logoRight"> </div>';
            $.get('${ctxFront}/cms/index/indexLogo').success(function (response) {
                var data = response.data;
                if (data) {
                    if(data.logoLeft){
                        $brandLogo.find('.brand-left img').attr('src', '${fns:ftpHttpUrl()}' + data.logoLeft);
                    }
                    if (data.logoRight) {
                        brandLogoRight = brandLogoRight.replace('logoRight', '${fns:ftpHttpUrl()}' + data.logoRight);
                        $brandLogo.find('.brand-right').append($(brandLogoRight));
                        var headerBrandSlide = brandTextSlide('brandLogo', 2000, 300);
                        headerBrandSlide && headerBrandSlide();
                    }
                }
            }).error(function (error) {

            })
        }

        getBrandLogo()


        var headerBrandTextSlide = brandTextSlide('brandTextSlide', 2000, 300);
        headerBrandTextSlide && headerBrandTextSlide();



    })
</script>