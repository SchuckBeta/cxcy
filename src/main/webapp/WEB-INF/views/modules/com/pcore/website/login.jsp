<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${fns:getConfig('productName')} 登录</title>
    <link rel="shortcut icon" href="${ctxImages}/bitbug_favicon.ico"/>
    <link rel="stylesheet" href="${ctxStatic}/element@2.3.8/index.css">
    <link rel="stylesheet" href="${ctxCss}/cyjd/common.css">
    <link rel="stylesheet" href="${ctxCss}/common/common.bundle.css">
    <link rel="stylesheet" href="${ctxCss}/common/headerFooter.bundle.css">
    <link rel="stylesheet" href="${ctxCss}/frontCyjd/frontCreative.bundle.css">
    <script src="${ctxStatic}/vue/vue.js"></script>
    <script src="${ctxStatic}/element@2.3.8/index.js"></script>
    <script src="${ctxStatic}/axios/axios.min.js"></script>

    <style>
        html,body{
            width: 100%;
            height: 100%;
        }
        .common-login{
            position: relative;
            width: 100%;
            height: 100%;
            background: url('/images/login-bg.jpg') no-repeat center center/cover;
            overflow: hidden;
        }
        .common-login .login-bg {
            width: 100%;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }

        .login-right-box {
            width: 380px;
            height: 310px;
            position: absolute;
            top: 55%;
            left: 75%;
            transform: translate(-50%, -50%);
        }

        .login-title {
            text-align: center;
        }

        .login-title span {
            font-weight: bold;
            font-size: 18px;
            letter-spacing: 4px;
            vertical-align: middle;
        }

        .login-title img {
            width: 38px;
            vertical-align: middle;
            margin-right: 18px;
        }

        .yellow-primary {
            padding-left: 38px;
            padding-right: 38px;
        }

        .el-input__inner {
            border: none;
            border-radius: 2px;
            padding: 0 15px 0 28px;
        }

        .login-right-box .iconfont {
            position: absolute;
            left: 7px;
            top: 0;
            right: auto;
            color: #9E9E9E;
        }

        .login-right-box .icon-yonghudianji {
            font-size: 14px;
        }

        .login-right-box .el-form-item__label {
            letter-spacing: 4px;
        }

        .type-box {
            border-bottom: 1px solid #AFB4AE;
            margin-bottom: 35px;
        }

        .login-right-box ul {
            display: inline-block;
        }

        .login-right-box ul li {
            float: left;
            padding: 2px 8px 2px 0;
            font-size: 18px;
            letter-spacing: 4px;
        }

        .login-right-box ul li:last-child {
            padding-right: 0;
        }

        .login-right-box ul li span:first-child {
            cursor: pointer;
        }

        .active-type span:first-child {
            color: #FDB02B;
            border-bottom: 1px solid #FDB02B;
            padding: 8px 0;
            font-weight: bold;
        }

        .el-form-item--mini.el-form-item {
            margin-bottom: 22px;
        }

        .login-right-box .use-tip {
            margin-top: 35px;
        }

        .yellow-primary.el-button--primary {
            color: #FFFFFF;
            background-color: #FDB02B;
            border-color: #FDB02B;
        }

        .yellow-primary.el-button--primary:hover, .yellow-primary.el-button--primary:focus {
            background: #fdc055;
            border-color: #fdc055;
            color: #FFFFFF;
        }

        .yellow-primary.el-button--primary:active {
            background: #e49e27;
            border-color: #e49e27;
            color: #FFFFFF;
            outline: none;
        }

        .yellow-primary.el-button--primary.is-disabled, .yellow-primary.el-button--primary.is-disabled:hover,
        .yellow-primary.el-button--primary.is-disabled:focus, .yellow-primary.el-button--primary.is-disabled:active {
            background-color: #fed895;
            border-color: #fed895;
            color: #FFFFFF;
            cursor: not-allowed;
        }
        .school-box{
            min-height: 300px;
            overflow: hidden;
        }
        .school-box .school-item{
            float: left;
            padding: 0 15px;
            font-size: 12px;
        }
        .school-box .school-item a{
            color: #333;
        }
        .school-box .school-item a:hover{
            color: #e9432d;
        }
    </style>

</head>
<body>

<div id="app" v-show="pageLoad" style="display: none;" class="common-login">
    <div class="login-right-box">
            <div class="login-title mgb-20">
                <img src="/images/user-circle.png" alt=""><span>用户登录</span>
            </div>
            <el-form :model="loginForm" ref="loginForm" :disabled="disabled" size="mini" class="el-form-builder"
                     autocomplete="off" label-width="76px">
                <div class="type-box text-center">
                    <ul>
                        <li v-for="(item, i) in lgTypes" :key="i" @click.stop.prevent="chooseType(item)"
                            :class="{'active-type': loginForm.lgType == item.key}">
                            <span>{{item.name}}</span>
                            <span v-if="i != lgTypes.length-1">|</span>
                        </li>
                    </ul>
                </div>

                <el-form-item prop="loginName" label="账号：" :rules="loginNameRules">
                    <el-input type="text" name="loginName" v-model="loginForm.loginName" placeholder="请输入账号"
                              @keyup.enter.native="submitForm" auto-complete="off">
                    </el-input>
                    <i class="iconfont icon-yonghudianji"></i>
                </el-form-item>

                <el-form-item prop="password" label="密码：" :rules="passwordRules">
                    <el-input type="password" name="password" v-model="loginForm.password" placeholder="请输入密码"
                              @keyup.enter.native="submitForm" auto-complete="off">
                    </el-input>
                    <i class="iconfont icon-xinmima"></i>
                </el-form-item>

            </el-form>

            <div class="text-right mgt-40">
                <el-button type="primary" size="mini" @click="registerForm" :disabled="disabled" class="yellow-primary">
                    学生注册
                </el-button>
                <el-button type="primary" size="mini" @click="submitForm" :disabled="disabled" class="yellow-primary">
                    登　录
                </el-button>
            </div>
            <div class="use-tip">
                <i class="el-icon-warning"></i>
                <span>为了您可以获得最佳的使用体验，推荐您使用谷歌浏览器操作本系统</span>
            </div>

        </div>
    <form ref="otherLoginForm" id="rsubmit" style="display: none;" :action="iframeSrc" method="POST">
        <input type="text" name="loginType" :value="loginType"/>
        <input type="text" name="username" :value="loginForm.loginName"/>
        <input  type="password" name="password" :value="loginForm.password"/>
        <input type="text" name="validateCode" :value="validateCode"/>
        <button type="submit" ref="rfsubmitEl"></button>
    </form>


    <el-dialog
            title="选择学校"
            :visible.sync="dialogVisible"
            width="520px" :close-on-click-modal="false" :show-close="true" :before-close="handleClose">
        <div class="school-box" v-loading="tenantDialogLoading">
            <div class="school-item" v-for="(item, i) in schoolTypes" :key="i">
                <a :href="item.httpurl" target="_blank"><span>{{item.schoolName}}</span></a>
            </div>
        </div>
    </el-dialog>
</div>


<script>

    'use strict';


    Vue.prototype.$axios = axios;

    var LoginVue = new Vue({
        el: '#app',
        data: function () {
            var loginNameReg = /['"\s]+/;
            var passwordReg = /^[0-9A-Za-z]{6,20}$/;
            var validateLoginName = function (rule, value, callback) {
                if (loginNameReg.test(value)) {
                    return callback(new Error('账号存在空格或者引号'))
                }
                return callback()
            };
            var validatePassword = function (rule, value, callback) {
                if (!passwordReg.test(value)) {
                    return callback(new Error('密码必须由6~20位数字或字母组成'));
                }
                return callback()
            };
            return {
                pageLoad: false,
                loginForm: {
                    loginName: '',
                    password: '',
                    lgType: '10'
                },
                loginType: '',
                validateCode: '',
                iframeSrc: '',
                disabled: false,
                lgTypes: [],
                loginNameRules: [
                    {required: true, message: '必填', trigger: 'blur'},
                    {min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'},
                    {validator: validateLoginName, trigger: 'blur'}
                ],
                passwordRules: [
                    {required: true, message: '必填', trigger: 'blur'},
                    {validator: validatePassword, trigger: 'blur'}
                ],
                dialogVisible: false,
                tenantDialogLoading: false,
                schoolTypes: []
            }
        },
        computed: {
        },
        methods: {
            chooseType: function (item) {
                this.loginForm.lgType = item.key;
            },
            showErrorMessge: function () {

            },
            submitForm: function () {
                var self = this;
                this.$refs.loginForm.validate(function (valid) {
                    if (valid) {
                        self.disabled = true;
                        self.$axios.post('/login', self.loginForm).then(function (response) {
                            var data = response.data;
                            if(data.status){
                                self.iframeSrc = data.datas.referer;
                                self.loginType = data.datas.usvo.loginType;
                                self.validateCode = data.datas.usvo.allVcode;
                                self.$nextTick(function () {
                                    document.getElementById("rsubmit").submit();
                                })
                            }else {
                                self.disabled = false;
                                self.$message({
                                    type: 'error',
                                    message: data.msg || '请求失败'
                                });
                            }
                        }).catch(function () {
                            self.disabled = false;
                        });
                    }
                });
            },
            registerForm: function () {
                var self = this;
                self.dialogVisible = true;
            },
            getLgTypes: function () {
                var self = this;
                this.$axios.get('/ajaxLgTypes').then(function (response) {
                    var data = response.data;
                    self.lgTypes = JSON.parse(data.datas);
                });
            },
            getSchoolType: function () {
                var self = this;
                this.tenantDialogLoading = true;
                this.$axios.post('/aloginSchools').then(function (response) {
                    var data = response.data;
                    if(data.status){
                        self.schoolTypes = data.datas || [];
                    }
                    self.tenantDialogLoading = false;
                }).catch(function () {
                    self.tenantDialogLoading = false;
                });
            },
            handleClose: function () {
                this.dialogVisible = false;
            }
        },
        created: function () {
            this.pageLoad = true;
            this.getLgTypes();
            this.getSchoolType();
        }
    });


</script>


</body>
</html>