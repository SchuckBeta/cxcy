<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/nprovince/header.jsp" %>
</head>
<body>

<div id="app" class="hide" :class="{show: pageLoad}">
    <div class="login-page" style="background: url('${ctxImages}/test-province-bg.jpg')">
        <div class="login-container">
            <%--<h2 class="site-title">湖北省大学生创新创业云平台</h2>--%>
            <%--<p class="site-title-second">——管理端</p>--%>
            <div class="login-box" style="margin-top: 180px;">
                <div class="wrapper">
                    <div class="lb-header">
                        <span>登录</span>
                    </div>
                    <div class="lb-body">
                        <el-form
                                :model="loginForm"
                                ref="loginForm"
                                size="mini"
                                :rules="loginFormRules"
                                :disabled="disabled"
                                action="${ctx}/login"
                                method="post"
                                autocomplete="off"
                        >
                            <el-form-item prop="username">
                                <el-input
                                        v-model="loginForm.username"
                                        name="username"
                                        placeholder="请输入登录账号"
                                        maxlength="64"
                                        @keyup.enter.native="saveLoginForm"
                                >
                                    <i slot="append" class="iconfont icon-yonghu"></i>
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="password">
                                <el-input
                                        type="password"
                                        v-model="loginForm.password"
                                        placeholder="请输入登录密码"
                                        name="password"
                                        min="6"
                                        maxlength="20"
                                        @keyup.enter.native="saveLoginForm"
                                >
                                    <i slot="append" class="iconfont icon-mima"></i>
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="validateCode" class="reg-validate-code"
                                          v-if="isValidateCodeLogin == 'true'">
                                <el-input type="text" name="validateCode" v-model="loginForm.validateCode"
                                          maxlength="10"
                                          @keyup.enter.native="saveLoginForm" placeholder="请输入验证码">
                                    <span slot="append" class="validate-code_img"><img :src="imgCode"
                                                                                       @click.stop="validateCodeRefresh"></span>
                                </el-input>
                            </el-form-item>
                            <el-form-item>
                                <span class="error-color">{{message}}</span>
                            </el-form-item>
                            <el-form-item class="text-center">
                                <el-button
                                        type="primary"
                                        @click.stop="saveLoginForm"
                                        @keyup.enter.native="saveLoginForm"
                                >登 录
                                </el-button
                                >
                            </el-form-item>
                        </el-form>
                    </div>
                </div>
                <div class="use-tip">
                    <i class="el-icon-warning"></i>
                    <span>为了您可以获得最佳的使用体验，推荐您使用谷歌浏览器操作本系统</span>
                </div>
            </div>
        </div>
        <div class="footer"></div>
    </div>
</div>


<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var self = this;
            var validateCode = function (rules, value, callback) {
                if (value) {
                    return self.$axios.get('/validateCode/checkValidateCode?validateCode=' + value).then(function (data) {
                        if (data) {
                            return callback();
                        }
                        return callback(new Error("验证码错误"))
                    })
                }
                return callback();
            };
            return {
                loginForm: {
                    username: '${username}',
                    password: "",
                    validateCode: ''
                },
                loginFormRules: {
                    username: [{required: true, message: "请输入登录名", trigger: "blur"}, {
                        max: 64,
                        message: '请输入不大于64位字符',
                        trigger: 'blur'
                    }],
                    password: [{required: true, message: "请输入密码", trigger: "blur"}, {
                        min: 6,
                        max: 20,
                        message: '请输入6-20位间密码',
                        trigger: 'blur'
                    }],
                    validateCode: [
                        {required: true, message: "请输入验证码", trigger: "blur"},
                        {validator: validateCode, trigger: 'blur'}
                    ]
                },
                isValidateCodeLogin: '${isValidateCodeLogin}',
                imgCode: '${ctx}/validateCode/createValidateCode',
                message: '${message}',
                disabled: false
            }
        },
        methods: {
            saveLoginForm: function () {
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
        created: function () {
            if (self.frameElement && self.frameElement.tagName == "IFRAME" ) {
                //dialogCyjd.createDialog(0, '未登录或登录超时。请重新登录，谢谢！')
                top.location = "${ctx}";
            }
        }
    })

</script>
</body>
</html>

