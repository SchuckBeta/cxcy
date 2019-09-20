<%@page import="com.oseasy.com.pcore.modules.sys.entity.User" %>
<%@page import="com.oseasy.com.pcore.modules.sys.utils.UserUtils" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%request.setAttribute("user", UserUtils.getUser());%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <script type="text/javascript">
        $frontOrAdmin = "${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}";
        var ftpHttp = '${ftpHttp}'; //修复bug使用
        var webMaxUpFileSize = "${fns:getMaxUpFileSize()}";//修复bug使用
    </script>
    <title><sitemesh:title/></title>
    <link rel="shortcut icon" href="${ctxImages}/bitbug_favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <%--<meta http-equiv="Cache-Control" content="no-store"/>--%>
    <%--<meta http-equiv="Pragma" content="no-cache"/>--%>
    <%--<meta http-equiv="Expires" content="0"/>--%>
    <meta name="isLogin" content="isLogin">

    <link rel="stylesheet" type="text/css" href="${ctxStatic}/frontCyjd/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/frontCyjd/jquery-ui.css?v=1"/>
    <%--<link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css"/>--%>
    <!--头部导航公用样式-->
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/header.css"/>

    <link rel="stylesheet" type="text/css" href="${ctxCss}/index.css?v=1"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/commonCyjd.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/frontCyjd/frontBime.css?v=q111121">


    <link rel="stylesheet" type="text/css" href="${ctxCss}/allCommonTest.css"/>
    <!--focus样式表-->

    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>

    <script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxCommon}/common-js/bootstrap.min.js" type="text/javascript"></script>
    <!--用于轮播的js-->
    <!--文本溢出-->
    <script src="${ctxJs}/common.js?v=1" type="text/javascript"></script>
    <script src="${ctxJs}/frontCyjd/frontCommon.js?v=21111" type="text/javascript"></script>


    <link rel="stylesheet" href="${ctxStatic}/element@2.3.8/index.css">
    <link rel="stylesheet" href="${ctxCss}/cyjd/common.css">
    <%--<link rel="stylesheet" href="${ctxCss}/cyjd/creatives.css?v=x">--%>
    <%--<link rel="stylesheet" href="${ctxCss}/frontCyjd/creatives.css?v=q12">--%>
    <%--<link rel="stylesheet" href="${ctxCss}/vendors/vendors.59d1419f63c48a7da6d0.css">--%>
    <link rel="stylesheet" href="${ctxCss}/common/common.bundle.css">
    <link rel="stylesheet" href="${ctxCss}/common/headerFooter.bundle.css">
    <link rel="stylesheet" href="${ctxCss}/frontCyjd/frontCreative.bundle.css">


    <script src="${ctxJs}/cityData/citydataNew.js?version=${fns: getVevison()}"></script>
    <script src="${ctxStatic}/vue/vue.js"></script>
    <script src="${ctxStatic}/moment/moment.min.js"></script>
    <script src="${ctxJs}/globalUtils/bluebird.min.js"></script> <!--兼容promise对象-->
    <script src="${ctxStatic}/element@2.3.8/index.js"></script>
    <script src="${ctxStatic}/axios/axios.min.js"></script>
    <script src="${ctxJs}/filters/filters.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/mixins/globalUtils/globalUtilsMixin.js?version=${fns: getVevison()}"></script> <!--全局entire-->
    <script src="${ctxJs}/globalUtils/globalUtils.js?version=${fns: getVevison()}"></script> <!--全局工具函数-->
    <script src="${ctxJs}/components/scr/ruleMixins.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/mixins/colleges/collegesMixin.js"></script> <!--所有学院混合宏-->
    <%--<script src="${ctxJs}/mixins/uploadFileMixin/uploadFileMixin.js"></script><!--element 上传文件函数-->--%>

    <script src="${ctxJs}/mixins/verifyExpression/verifyExpressionMixin.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/mixins/verifyExpression/verifyExpressionBackMixin.js"></script>

    <script src="${ctxJs}/components/home/home.js"></script>

    <script src="${ctxJs}/components/condition/e-condition.js?v=1x"></script>
    <script src="${ctxJs}/components/checkbox/checkboxGroup.js"></script>
    <script src="${ctxJs}/components/checkbox/checkbox.js"></script>
    <script src="${ctxJs}/components/radio/radioGroup.js"></script>
    <script src="${ctxJs}/components/radio/radio.js"></script>
    <script src="${ctxJs}/components/proContestDetail/proContestDetail.js"></script>
    <%--<script src="${ctxJs}/components/panel/e-panel.js"></script>--%>
    <script src="${ctxJs}/components/eFileItem/eFileItem.js"></script>
    <script src="${ctxJs}/components/uploadFile/uploadFile.component.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/uploadFile/uploadFile.pw.component.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/cropperPic/ePicFIle.js"></script>
    <script src="${ctxJs}/components/eColItem/eColItem.js"></script>
    <script src="${ctxJs}/components/stuTea/stuTea.js"></script>
    <script src="${ctxJs}/mixins/userInfo/userFormMixin.js??version=${fns: getVevison()}"></script>


    <script src="${ctxJs}/components/mobileForm/mobileForm.js"></script> <!--手机表单-->
    <script src="${ctxJs}/components/verificationCodeBtn/verificationCodeBtn.js"></script> <!--验证码-->

    <script src="${ctxJs}/components/cropperPic/cropperPic.js"></script> <!--图片裁剪-->
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/cropper/cropper.min.css">
    <script type="text/javascript" src="${ctxStatic}/cropper/cropper.min.js"></script>

    <script src="${ctxJs}/components/passwordForm/passwordForm.js?version=${fns: getVevison()}"></script><!---修改密码-->
    <script src="${ctxJs}/components/tableColBlock/tableTeamMember.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/auditForm/auditForm.js?version=${fns: getVevison()}"></script>

    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxJs}/directives/globalDirectives.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/common/commonComponents.js?version=${fns: getVevison()}"></script>
    <script type="text/javascript" src="${ctxJs}/components/pwEnter/pwEnterMixin.js?version=${fns: getVevison()}"></script>
    <script type="text/javascript" src="${ctxJs}/components/updateMember/updateMember.js?version=${fns: getVevison()}"></script>
    <script type="text/javascript" src="${ctxJs}/components/pwEnter/pwEnterApplyRules.js?version=${fns: getVevison()}"></script>
    <script type="text/javascript" src="${ctxJs}/components/pwEnter/pwEnterApplyForm.js?version=${fns: getVevison()}"></script>
    <script type="text/javascript" src="${ctxJs}/components/pwEnter/pwEnterList.js?version=${fns: getVevison()}"></script>
    <script type="text/javascript" src="${ctxJs}/components/pwEnter/pwEnterView.js?version=${fns: getVevison()}"></script>
    <script type="text/javascript" src="${ctxJs}/components/scr/scrCommon.js?version=${fns: getVevison()}"></script>
    <script type="text/javascript" src="${ctxJs}/components/scr/creditTeamMember.js?version=${fns: getVevison()}"></script>
    <script type="text/javascript" src="${ctxJs}/components/scr/scrView.js?version=${fns: getVevison()}"></script>
    <script>


        +function (Vue, axios, window) {

            'use strict';
            var ftpHttp = '${fns:ftpHttpUrl()}';
            var webMaxUpFileSize = "${fns:getMaxUpFileSize()}";
            var $frontOrAdmin = "${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}";
            var token = (function () {
                var arr, reg = new RegExp("(^| )" + 'token' + "=([^;]*)(;|$)");
                return (arr = document.cookie.match(reg)) ? unescape(arr[2]) : null;
            })();


            Vue.mixin({
                data: function () {
                    return {
                        pageLoad: false,
                        formSubmitMessage: '${message}',
                        ftpHttp: '${fns:ftpHttpUrl()}',
                        ctx: "${ctx}",
                        ctxFront: "${ctxFront}",
                        ctxJs: "${ctxJs}",
                        ctxImg: "${ctxImg}",
                        ctxImages: "${ctxImages}",
                        ctxStatic: "${ctxStatic}",
                        frontOrAdmin: "${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}",
                        domainPrefix: '${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}',
                        webMaxUpFileSize: parseInt(webMaxUpFileSize),
                        xhrErrorMsg: '请求失败',
                        hasMvcode: '${fns:getSmsHasMvcode()}' == 'true',
                        casHasLogin: JSON.parse('${fns: getCasHasLogin()}'),
                        loginCurUser: JSON.parse(JSON.stringify(${fns: toJson(user)})) || {},
                        fileStepEnum: {'apply': '1102', 'middle': '1103', 'end': '1104'}
                    }
                },
                computed: {
                    isStudent: function () {
                        return this.loginCurUser.userType === '1';
                    },
                    isTeacher: function () {
                        return this.loginCurUser.userType === '2';
                    }
                },
                methods: {
                    show$message: function (data, message) {
                        var type = (parseInt(data.ret) || data.status) ? 'success' : 'error';
                        var msg = data.msg || message;
                        if (!msg) {
                            return false;
                        }
                        this.$message({
                            type: type,
                            center: true,
                            message: msg
                        })
                    },
                },
                created: function () {
                    this.pageLoad = true;

//                    this.$message({
//                        duration: 1000
//                    })
                }
            });

            window.ctxFront = '${ctxFront}';
            //全局配置axios；
            axios.defaults.baseURL = '${ctxFront}';
            axios.defaults.timeout = 60 * 1000;
            axios.interceptors.request.use(function (config) {
                if (config.url.indexOf('getDictList') === -1) {
                    if (config.url.indexOf('?') == -1) {
                        config.url += '?';
                    } else {
                        config.url += '&';
                    }
                    config.url += 't=' + Date.parse(new Date()) / 1000;
                }
                return config
            }, function (error) {
                try {
                    return Promise.reject(error)
                } catch (e) {

                }
            })
//            console.log(Promise)
            axios.interceptors.response.use(function (response) {
                // Do something with response data
                var data = response;
                var resData = data.data;
                var isChecked;
                if({}.toString.call(resData)  !== '[object String]'){
                    return data;
                }
                isChecked = resData.indexOf("<meta name=\"isLogin\"") > -1;
                if(isChecked && !window.isLoginShowMsgBox){
                    window.isLoginShowMsgBox = true;
                    ELEMENT.MessageBox({
                        title: '提示',
                        type:  'error',
                        closeOnClickModal: false,
                        closeOnPressEscape: false,
                        showClose: false,
                        message: '未登录或登录超时。请重新登录，谢谢'
                    }).then(function () {
                        location.href = '/f/toLogin';
                    })
                }
                return data;
            }, function (error) {
                // Do something with response error
                var errorMessage;
                switch (error.response.status){
                    case 400:
                        errorMessage = '请求无效';
                        break;
                    case 404:
                        errorMessage = '请求路径错误';
                        break;
                    case 405:
                        errorMessage = '请求方法被禁用';
                        break;
                    case 500:
                        errorMessage = '服务器异常';
                        break;
                }
                if(errorMessage){
                    ELEMENT.MessageBox({
                        title: '提示',
                        type:  'error',
                        closeOnClickModal: false,
                        closeOnPressEscape: false,
                        showClose: false,
                        message: errorMessage
                    })
                }
                return Promise.reject(error);
            });
            axios.defaults.headers = {
                'token': token
            };

            Vue.prototype.$axios = axios;


            //封装object assign
            if (typeof Object.assign != 'function') {
                (function () {
                    Object.assign = function (target) {
                        'use strict';
                        if (target === undefined || target === null) {
                            throw new TypeError('Cannot convert undefined or null to object');
                        }

                        var output = Object(target);
                        for (var index = 1; index < arguments.length; index++) {
                            var source = arguments[index];
                            if (source !== undefined && source !== null) {
                                for (var nextKey in source) {
                                    if (source.hasOwnProperty(nextKey)) {
                                        output[nextKey] = source[nextKey];
                                    }
                                }
                            }
                        }
                        return output;
                    };
                })();
            }


            Object.toURLSearchParams = function (data) {
                'use strict';
                if (data === undefined || data === null) {
                    throw new TypeError('Cannot convert undefined or null to object');
                }
                if (typeof URLSearchParams === 'function') {
                    var params = new URLSearchParams();
                    for (var k in data) {
                        if (data.hasOwnProperty(k)) {
                            if (data[k]) {
                                params.append(k, data[k]);
                            }
                        }
                    }
                    return params;
                }
                var str = [];
                var strObj = {};
                for (var k2 in data) {
                    if (data.hasOwnProperty(k2)) {
                        if ({}.toString.call(data[k2]) === '[object Array]') {
                            var vStr = data[k2].join(',');
                            if (vStr) {
                                strObj[k2] = (k2 + '=' + encodeURI(vStr))
                            }
                        } else {
                            if (data[k2] != undefined) {
                                strObj[k2] = (k2 + '=' + encodeURI(data[k2]))
                            }
                        }

                        strObj[k2] && str.push(strObj[k2])
                    }
                }
                return str.join('&')
            }


        }(Vue, axios, window)

    </script>
    <sitemesh:head/>
</head>

<body>
<%@ include file="headerNew.jsp" %>
<div id="content">
    <sitemesh:body/>
</div>
<%@ include file="footer.jsp" %>
<div id="dialog-message" title="信息" style="display: none;">
    <p id="dialog-content"></p>
</div>
</body>
</html>