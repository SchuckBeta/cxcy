<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${fns:getConfig('productName')} 登录</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <link rel="shortcut icon" href="${ctxImages}/bitbug_favicon.ico"/>
</head>
<body>
<div id="app" v-show="pageLoad" style="display: none">
    <div class="sys-login-bg">
        <img class="up img-responsive" v-if="currTconfig.currpn == 1" src="${ctxImg}/login2bg_yunying.jpg"/>
        <img class="up img-responsive" v-else src="${ctxImg}/login2bg_01.jpg"/>
    </div>
    <div class="sys-login-box">
        <el-form :model="loginForm" ref="loginForm" :rules="loginRules" :disabled="disabled" size="mini"
                 autocomplete="off" action="${ctx}/login" method="post">
            <el-form-item prop="username">
                <el-input type="text" name="username" v-model="loginForm.username" @keyup.enter.native="submitForm"
                          auto-complete="off" placeholder="请输入用户名">
                    <span slot="append"><i class="iconfont icon-yonghudianji"></i></span>
                </el-input>
            </el-form-item>

            <el-form-item prop="password">
                <el-input type="password" name="password" v-model="loginForm.password" @keyup.enter.native="submitForm"
                          auto-complete="off" placeholder="密码">
                    <span slot="append" @click.stop.prevent="findPwd"><i class="iconfont icon-xinmima"></i>
                    </span>
                </el-input>
            </el-form-item>

            <el-form-item prop="validateCode" class="reg-validate-code" v-if="isValidateCodeLogin == 'true'">
                <el-input type="text" name="validateCode" v-model="loginForm.validateCode"
                          @keyup.enter.native="submitForm" auto-complete="off" placeholder="请输入验证码"></el-input>
                <img :src="imgCode" @click.stop.prevent="validateCodeRefresh">
            </el-form-item>
            <el-form-item v-if="message">
                <span class="red">{{message}}</span>
            </el-form-item>
            <el-form-item class="text-center">
                <el-button type="primary" @click.stop.prevent="submitForm">登&nbsp;&nbsp;&nbsp;&nbsp;录</el-button>
            </el-form-item>
        </el-form>
    </div>
    <div class="use-tip">
        <i class="el-icon-warning"></i>
        <span>为了您可以获得最佳的使用体验，推荐您使用谷歌浏览器操作本系统</span>
    </div>
    <%--<div v-if="upIe10" class="browser-dialog"><div class="bro-dia-tip">请使用IE9以上的版本、谷歌等浏览器以获得更好的体验</div></div>--%>
</div>
<%@ include file="/WEB-INF/views/layouts/website/copyright.jsp" %>

<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        mixins: [Vue.verifyRegMixin],
        data: function () {
            var self = this;
            var validateCode = function (rules, value, callback) {
                if (value) {
                    return self.$axios.get('/validateCode/checkValidateCode?validateCode=' + value).then(function (response) {
                        if (response.data) {
                            return callback();
                        }
                        return callback(new Error("验证码错误"))
                    }).catch(function (error) {
                        return callback(new Error("连接失败"))
                    })
                }
                return callback();
            };

            return {
                loginForm: {
                    username: '${username}',
                    password: '',
                    validateCode: ''
                },
                loginRules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'},
                        {max: 120, message: '请输入不大于64位字符', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                        {min: 6, max: 20, message: '请输入6-20位间密码', trigger: 'blur'}
                    ],
                    validateCode: [
                        {required: true, message: '请输入验证码', trigger: 'blur'},
                        {validator: validateCode, trigger: 'blur'}
                    ]
                },
                isValidateCodeLogin: '${isValidateCodeLogin}',
                imgCode: '${ctx}/validateCode/createValidateCode',
                message: '${message}',
                disabled: false,
                upIe10: false
            }
        },
        watch: {
            loginForm: {
                deep: true,
                handler: function () {
                    this.message = '';
                }
            }
        },
        methods: {
            submitForm: function () {
                var self = this;
                this.$refs.loginForm.validate(function (valid) {
                    if (valid) {
                        self.disabled = true;
                        self.$refs.loginForm.$el.submit();
                    }
                })
            },
            validateCodeRefresh: function () {
                this.imgCode = '${ctx}/validateCode/createValidateCode?' + new Date().getTime();
            }
        },
        mounted: function () {
            this.$refs.loginForm.resetFields();
            this.upIe10 = ($.browser.msie && $.browser.version<10);
        }
    })

    $(function () {
        if (self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0) {
            //dialogCyjd.createDialog(0, '未登录或登录超时。请重新登录，谢谢！')
            top.location = "${ctx}";
        }
    })

</script>
</body>
</html>

