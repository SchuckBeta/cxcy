<%@ page  pageEncoding="UTF-8" %>
<%@page import="com.oseasy.com.pcore.modules.sys.utils.UserUtils" %>
<%@ page import="com.oseasy.com.pcore.modules.sys.utils.CoreUtils" %>
<%request.setAttribute("user", UserUtils.getUser());%>
<%request.setAttribute("tenantConfig", CoreUtils.getTconfig());%>
<title>${backgroundTitle}</title>
<link rel="shortcut icon" href="${ctxImages}/bitbug_favicon.ico"/>
<meta charset="UTF-8">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<meta name="isLogin" content="isLogin">
<link rel="stylesheet" href="${ctxStatic}/nprovince/element@2.3.8/index.theme.default.css"/>


<link rel="stylesheet" href="${ctxCss}/common/common.bundle.province.css?version=${fns: getVevison()}">
<link rel="stylesheet" href="${ctxCss}/cyjd/backCreative.bundle.province.css?version=${fns: getVevison()}">
<link rel="stylesheet" href="${ctxCss}/nprovince/style.css?version=${fns: getVevison()}"/>
<link rel="stylesheet" href="${ctxCss}/menuIcon/iconfont.css?version=${fns: getVevison()}"/>
<%--<link rel="stylesheet" href="${ctxJs}/nprovince/ui/condition/index.css?version=${fns: getVevison()}"/>--%>
<script type="text/javascript">
    window.ctx = '${ctx}';
    window.loginCurUser = JSON.parse(JSON.stringify(${fns: toJson(user)})) || {};
</script>
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctxJs}/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctxStatic}/axios/axios.js"></script>
<script type="text/javascript" src="${ctxStatic}/vue/vue.js"></script>
<script type="text/javascript" src="${ctxStatic}/element@2.3.8/index.js"></script>
<script type="text/javascript" src="${ctxJs}/globalUtils/bluebird.min.js"></script> <!--兼容promise对象-->
<script type="text/javascript" src="${ctxStatic}/moment/moment.min.js"></script>
<script type="text/javascript" src="${ctxJs}/mixins/globalUtils/globalUtilsMixin.js"></script>
<%--<script type="text/javascript" src="${ctxJs}/nprovince/common/notification.js?version=${fns: getVevison()}"></script>--%>
<script type="text/javascript" src="${ctxJs}/filters/filters.js?version=${fns: getVevison()}"></script>
<script type="text/javascript" src="${ctxJs}/components/province/province.components.js?version=${fns: getVevison()}"></script>
<%--<script type="text/javascript" src="${ctxJs}/nprovince/ui/condition/index.js?version=${fns: getVevison()}"></script>--%>
<script type="text/javascript" src="${ctxJs}/nprovince/components/index.js?version=${fns: getVevison()}"></script>
<script type="text/javascript" src="${ctxJs}/components/province/province.pro_view.js?version=${fns: getVevison()}"></script>
<script type="text/javascript" src="${ctxJs}/components/province/province.contest_view.js?version=${fns: getVevison()}"></script>
<script type="text/javascript" src="${ctxJs}/components/eColItem/eColItem.js"></script>
<script type="text/javascript" src="${ctxJs}/components/eFileItem/eFileItem.js"></script>
<script type="text/javascript" src="${ctxJs}/components/cropperPic/ePicFIle.js"></script>
<script type="text/javascript" src="${ctxJs}/components/uploadFile/uploadFile.pw.component.js?version=${fns: getVevison()}"></script>
<script type="text/javascript" src="${ctxJs}/components/common/commonComponents.js?version=${fns: getVevison()}"></script>
<script type="text/javascript" src="${ctxJs}/components/proContestDetail/proContestDetail.js?version=${fns: getVevison()}"></script>
<style>
    .app-header{
        background: url('${ctxImages}/province-top-bar-bg.png');
    }
</style>
<script>
    +function (window) {

        'use strict';

        var token = (function () {
            var arr, reg = new RegExp("(^| )" + 'token' + "=([^;]*)(;|$)");
            return (arr = document.cookie.match(reg)) ? unescape(arr[2]) : null;
        })();


        Vue.mixin({
            data: function () {
                return {
                    pageLoad: false,
                    ctx: "${ctx}",
                    ctxFront: "${ctxFront}",
                    ctxJs: "${ctxJs}",
                    ctxImg: "${ctxImg}",
                    ctxImages: "${ctxImages}",
                    ctxStatic: "${ctxStatic}",
                    ftpHttp: '${fns:ftpHttpUrl()}',
                    frontOrAdmin: "${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}",
                    xhrErrorMsg: '请求失败',
                    loginCurUser: JSON.parse(JSON.stringify(${fns: toJson(user)})) || {},
                    currTconfig: JSON.parse(JSON.stringify(${fns: toJson(tenantConfig)})) || {},
                    currTenantId: '',
                    showActrel: false,
                    showTenantPop: true
                }
            },
            created: function () {
                if(this.currTconfig.changeTcvo != null){
                    this.currTenantId = this.currTconfig.changeTcvo.val;
                }
                if((this.currTenantId == null) && (this.currTconfig.userTcvo != null)){
                    this.currTenantId = this.currTconfig.userTcvo.val;
                }
                if((this.currTenantId == null) && (this.currTconfig.wwwTcvo != null)){
                    this.currTenantId = this.currTconfig.wwwTcvo.val;
                }
                if(this.currTconfig.showTenantPop == 'false'){
                    this.showTenantPop = false;
                }
                if(this.currTconfig.showActrel == 'false'){
                    this.showActrel = false;
                }
                this.pageLoad = true;
            }
        });




        //全局配置axios；
        axios.defaults.baseURL = '${ctx}';
        axios.defaults.timeout = 10 * 6000;

        axios.interceptors.request.use(function (config) {
            if(config.url.indexOf('?') == -1){
                config.url += '?';
            }else {
                config.url += '&';
            }
            config.url += 't=' + Date.parse(new Date())/1000;
            return config
        },function(error){
            try {
                return Promise.reject(error)
            }catch (e){

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
                    location.reload();
                })
            }
            return data;
        }, function (error) {
            // Do something with response error
            var errorMessage;
            switch (error.response.status){
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

    }(window)
</script>

