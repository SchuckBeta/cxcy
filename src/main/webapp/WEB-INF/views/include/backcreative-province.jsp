<%@ page  pageEncoding="UTF-8" %>
<%@page import="com.oseasy.com.pcore.modules.sys.utils.UserUtils" %>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Cache-Control" content="no-store">
<meta name="isLogin" content="isLogin">
<%request.setAttribute("user", UserUtils.getUser());%>
<link rel="stylesheet" type="text/css" href="${ctxCss}/cyjd/common.css?version=${fns: getVevison()}">
<link rel="stylesheet" href="${ctxStatic}/element@2.3.8/index.theme.default.css?version=${fns: getVevison()}">
<link rel="stylesheet" type="text/css" href="${ctxStatic}/cropper/cropper.min.css">

<%--<link rel="stylesheet" href="${ctxCss}/cyjd/creatives.css?v=1234">--%>

<%--<link rel="stylesheet" href="${ctxCss}/cyjd/index.bundle.css">--%>
<%--<link rel="stylesheet" href="${ctxCss}/cyjd/main.bundle.css">--%>

<link rel="stylesheet" href="${ctxCss}/common/common.bundle.province.css?version=${fns: getVevison()}">
<link rel="stylesheet" href="${ctxCss}/cyjd/backCreative.bundle.province.css?version=${fns: getVevison()}">



<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxJs}/jquery.cookie.js"></script>
<script src="${ctxCommon}/common-js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/cropper/cropper.min.js"></script>
<script src="${ctxStatic}/axios/axios.js"></script>
<%--<script src="https://cdn.bootcss.com/babel-polyfill/6.23.0/polyfill.min.js"></script>--%>
<script src="${ctxStatic}/vue/vue.js"></script>
<script src="${ctxJs}/components/flowDesignComponents/globalBus.js?version=${fns: getVevison()}"></script>
<script src="${ctxJs}/globalUtils/bluebird.min.js"></script> <!--兼容promise对象-->
<script src="${ctxStatic}/element@2.3.8/index.js"></script>
<script src="${ctxStatic}/vueDraggable/Sortable.min.js"></script>
<script src="${ctxStatic}/vueDraggable/vuedraggable.min.js"></script>
<script src="${ctxStatic}/moment/moment.min.js"></script>
<script src="${ctxJs}/cityData/citydataNew.js?version=${fns: getVevison()}"></script>
<script src="${ctxJs}/globalUtils/globalUtils.js?version=${fns: getVevison()}"></script>
<script src="${ctxJs}/mixins/globalUtils/regExpMixin.js?version=${fns: getVevison()}"></script>
<script src="${ctxJs}/filters/filters.js?version=${fns: getVevison()}"></script>
<script src="${ctxJs}/mixins/verifyExpression/verifyExpressionMixin.js"></script>
<%--<script src="${ctxStatic}/vue/vue.min.js"></script>--%>

<%--<script src="${ctxStatic}/axios/axios.min.js"></script>--%>
<script src="${ctxJs}/components/passwordForm/passwordForm.js?version=${fns: getVevison()}"></script><!---修改密码-->
<script src="${ctxJs}/mixins/colleges/collegesMixin.js"></script>
<script src="${ctxJs}/mixins/menuTree/menuTreeMixin.js"></script>
<script src="${ctxJs}/mixins/globalUtils/globalUtilsMixin.js"></script>
<script src="${ctxJs}/directives/globalDirectives.js"></script>
<script src="${ctxJs}/mixins/verifyExpression/verifyExpressionBackMixin.js"></script>
<script src="${ctxJs}/mixins/verifyExpression/verifyExpressionMixin.js"></script>
<script src="${ctxJs}/mixins/drPwSpaceList/drPwSpaceList.js"></script>
<script src="${ctxJs}/mixins/menuTree/menuTreeMixin.js"></script>
<script src="${ctxJs}/components/echart/echart-directive.js"></script>
<script src="${ctxJs}/components/province/province.components.js"></script>
<script src="${ctxJs}/components/checkbox/checkboxGroup.js"></script>
<script src="${ctxJs}/components/checkbox/checkbox.js"></script>
<script src="${ctxJs}/components/radio/radioGroup.js"></script>
<script src="${ctxJs}/components/radio/radio.js"></script>
<script src="${ctxJs}/components/stuTea/stuTea.js"></script>
<script src="${ctxJs}/components/eColItem/eColItem.js"></script>
<script src="${ctxJs}/components/eFileItem/eFileItem.js"></script>
<script src="${ctxJs}/components/proProgress/proProgress.js"></script>
<script src="${ctxJs}/components/condition/e-condition.js?v=1"></script>
<script src="${ctxJs}/components/cropperPic/ePicFIle.js"></script>
<script src="${ctxJs}/components/cropperPic/cropperPic.js"></script> <!--图片裁剪-->
<script src="${ctxJs}/components/controlRuleBlock/controlRuleBlock.js"></script>
<script src="${ctxJs}/components/echart/echartProApprovalType.js"></script>
<script src="${ctxJs}/components/buttonImport/buttonImport.js"></script>
<script src="${ctxJs}/components/buttonImport/buttonImportContest.js?v=1"></script>
<script src="${ctxJs}/components/buttonImport/buttonExport.js"></script>
<script src="${ctxJs}/components/buttonImport/buttonExportFile.js"></script>
<script src="${ctxJs}/components/buttonImport/buttonAdviceLevel.js"></script>
<script src="${ctxJs}/components/buttonImport/buttonBatchCheck.js"></script>
<script src="${ctxJs}/components/buttonImport/buttonAuditRecord.js"></script>
<script src="${ctxJs}/components/buttonImport/exportFileProcess.js"></script>
<script src="${ctxJs}/components/buttonImport/projectInfoList.js"></script>
<script src="${ctxJs}/components/buttonImport/exportExPro.js"></script>
<script src="${ctxJs}/components/buttonImport/buttonCommonImpExp.js"></script>
<script src="${ctxJs}/components/eSetText/eSetText.js"></script>
<script src="${ctxJs}/components/eSetName/eSetName.js"></script>
<script src="${ctxJs}/components/eSetWordText/eSetWordText.js"></script>
<script src="${ctxJs}/components/proContestDetail/proContestDetail.js?version=${fns: getVevison()}"></script>
<%--<script src="${ctxJs}/components/panel/e-panel.js?version=${fns: getVevison()}"></script>--%>
<script src="${ctxJs}/components/auditForm/auditForm.js?version=${fns: getVevison()}"></script>
<script src="${ctxJs}/components/auditForm/scoreForm.js?version=${fns: getVevison()}"></script>
<script src="${ctxJs}/components/uploadFile/uploadFile.pw.component.js?version=${fns: getVevison()}"></script>
<script src="${ctxJs}/components/common/commonComponents.js?version=${fns: getVevison()}"></script>
<script src="${ctxJs}/components/tableColBlock/tableTeamMember.js?version=${fns: getVevison()}"></script>
<script src="${ctxJs}/components/tableColBlock/tableThingInfo.js?version=${fns: getVevison()}"></script>
<script src="${ctxJs}/components/bigData/csPanel.js?version=${fns: getVevison()}"></script>
<script type="text/javascript" src="${ctxJs}/components/pwEnter/pwEnterMixin.js?version=${fns: getVevison()}"></script>
<script type="text/javascript" src="${ctxJs}/components/updateMember/updateMember.js?version=${fns: getVevison()}"></script>
<script type="text/javascript" src="${ctxJs}/components/colProNode/colProNode.province.js"></script>

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
                    formSubmitMessage: '${message}',
                    ctx: "${ctx}",
                    ctxFront: "${ctxFront}",
                    ctxJs: "${ctxJs}",
                    ctxImg: "${ctxImg}",
                    ctxImages: "${ctxImages}",
                    ctxStatic: "${ctxStatic}",
                    frontOrAdmin: "${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}",
                    ftpHttp: '${fns:ftpHttpUrl()}',
                    xhrErrorMsg: '请求失败',
                    loginCurUser: JSON.parse(JSON.stringify(${fns: toJson(user)})) || {},
                    showTenantPop: true,  //租户是否选择了运营平台,
                    currTenantKey:"tenant", //cookie的key值
                    currTenantId: $.cookie("tenant") //cookie的tenantId值
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
                }
            },
            created: function () {
                this.pageLoad = true;
            }
        });




        window.paginationMixin = {
            data: function () {
                return {
                    searchFormName: 'searchListForm'
                }
            },
            mounted: function () {
                var self = this;
                $("#ps").val($("#pageSize").val());
                window.page = function (n, s) {
                    $("#pageNo").val(n);
                    $("#pageSize").val(s);
                    self.$refs[self.searchFormName].$el.submit();
                }
            }
        };



        window.ctx = '${ctx}';
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
            try {
                return Promise.reject(error);
            }catch (e){

            }
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
            if(typeof URLSearchParams === 'function'){
                var params = new URLSearchParams();
                for(var k in data){
                    if(data.hasOwnProperty(k)){
                        if(data[k]){
                            params.append(k, data[k]);
                        }
                    }
                }
                return params;
            }
            var str = [];
            var strObj = {};
            for(var k2 in data){
                if(data.hasOwnProperty(k2)){
                    if({}.toString.call(data[k2]) === '[object Array]'){
                        var vStr = data[k2].join(',');
                        if(vStr){
                            strObj[k2] = (k2 + '='+ encodeURI(vStr))
                        }
                    }else {
                        if(data[k2] != undefined){
                            strObj[k2] = (k2 + '='+encodeURI(data[k2]))
                        }
                    }

                    strObj[k2] && str.push(strObj[k2])
                }
            }
            return str.join('&')
        }

    }(window)
</script>


