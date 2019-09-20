<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="creative"/>
    <style>
        .user-register-form .el-tabs__nav{
            text-align: left;
        }
        .user-register-form .el-tabs__active-bar, .user-register-form .el-tabs__item{
            text-align: center;
        }

    </style>
</head>
<body>

<div id="app" v-show="pageLoad" style="display: none;" class="user-register">
    <div class="container page-container">
        <div class="user-register-box" style="margin-top:55px;">
            <p>登录</p>
            <input type="hidden" id="imFrontLoginPage"/>
            <input type="hidden" name="fromLogin" value="${loginType}"/>
            <div class="user-register-form">
                <el-tabs v-model="activeName" @tab-click="handleClick">
                    <el-tab-pane label="账号密码登录" name="first">
                        <el-form action="login" :model="accountPwdForm" ref="accountPwdForm" size="mini" :rules="rules" method="POST" class="demo-ruleForm" :disabled="disabled">
                            <input type="hidden" name="loginType" :value="loginType"/>
                            <el-form-item prop="username">
                                <el-input type="text" name="username" v-model="accountPwdForm.username" @keyup.enter.native="submitForm" auto-complete="off" placeholder="请输入登录名或学号">
                                    <el-button slot="append"><i class="iconfont icon-yonghudianji"></i></el-button>
                                </el-input>
                            </el-form-item>

                            <el-form-item prop="password">
                                <el-input type="password" name="password" v-model="accountPwdForm.password" @keyup.enter.native="submitForm" auto-complete="off" placeholder="密码">
                                    <el-button v-if="hasMvcode" slot="append" @click.stop.prevent="findPwd">找回密码</el-button>
                                </el-input>
                            </el-form-item>

                            <el-form-item prop="validateCode" class="reg-validate-code" v-show="isValidateCodeLogin == 'true'">
                                <el-input type="text" name="validateCode" v-model="accountPwdForm.validateCode" @keyup.enter.native="submitForm" auto-complete="off" placeholder="请输入验证码"></el-input>
                                <img :src="imgCode" @click.stop.prevent="validateCodeRefresh">
                            </el-form-item>

                            <span v-show="message && fromLogin == '2'" style="color:#E9432D;">{{message}}</span>
                        </el-form>
                    </el-tab-pane>

                    <el-tab-pane v-if="hasMvcode" label="短信登录" name="second">
                        <el-form action="login" :model="messageForm" ref="messageForm" size="mini" :rules="rules"  method="POST" class="demo-ruleForm" :disabled="disabled">
                            <input type="hidden" name="loginType" :value="loginType"/>
                            <el-form-item prop="username">
                                <el-input type="number" name="username" v-model.number="messageForm.username" @keyup.enter.native="submitForm" auto-complete="off" placeholder="请输入手机号">
                                    <el-button slot="append"><i class="iconfont icon-unie64f"></i></el-button>
                                </el-input>
                            </el-form-item>

                            <el-form-item prop="password">
                                <el-input type="text" name="password" v-model="messageForm.password" @keyup.enter.native="submitForm" auto-complete="off" placeholder="请输入验证码">
                                    <verification-code-btn slot="append" ref="verificationCode" :time-count.sync="timeCount" @post-code="getVerity('messageForm')">
                                    </verification-code-btn>
                                </el-input>
                            </el-form-item>

                            <span v-show="message && fromLogin == '1'" style="color:#E9432D;">{{message}}</span>
                        </el-form>
                    </el-tab-pane>

                    <div class="text-center register-btn">
                        <el-button type="primary" @click="submitForm" :disabled="isSubmit || disabled">登录</el-button>
                        <a href="/f/toRegister">没有账号，去注册</a>
                    </div>

                </el-tabs>
            </div>
            <div class="use-tip">
                <i class="el-icon-warning"></i>
                <span>为了您可以获得最佳的使用体验，推荐您使用谷歌浏览器操作本系统</span>
            </div>
        </div>
    </div>
</div>

<script>
        'use strict';

         new Vue({
            el:'#app',
            mixins:[Vue.frontLoginMixin],
            data:function(){

                return {
                    activeName: 'first',
                    loginType:2,
                    fromLogin:'${loginType}',
                    userName:'${username }',
                    isValidateCodeLogin:'${isValidateCodeLogin}',
                    message:'${message}',
                    accountPwdForm:{
                        username:'',
                        password:'',
                        validateCode:''
                    },
                    messageForm:{
                        username:'',
                        password:''
                    },
                    imgCode:'/f/validateCode/createValidateCode',
                    timeCount:60,
                    disabled: false,
                }
            },
            computed:{
                isSubmit:{
                    get:function(){
                        if(this.loginType == '2'){
                            return (!this.accountPwdForm.username || !this.accountPwdForm.password)
                        }else{
                            return (!this.messageForm.username || !this.messageForm.password)
                        }
                    }
                },
                formName:{
                    get: function(){
                        return this.loginType == '2' ? 'accountPwdForm' : 'messageForm';
                    }
                }
            },
            methods:{
                handleClick:function(tab){
                    if(tab.name == 'first'){
                        this.$refs['accountPwdForm'].resetFields();
                        this.loginType = 2;
                    }else{
                        this.$refs['messageForm'].resetFields();
                        this.loginType = 1;
                        this.timeCount = -1;
                    }
                },
                submitForm:function(){
                    var self = this;
                    this.$refs[this.formName].validate(function(valid){
                        if(valid){
                            self.disabled = true;
                            self.$refs[self.formName].$el.submit();
                        }
                    })
                },
                getVerity:function(formName){
                    var self = this;
                    this.$refs[formName].validateField('username',function(valid){
                        if(valid == ''){
                            self.$axios({
                                method:'GET',
                                url:'sendSmsCode?mobile=' + self.messageForm.username
                            }).then(function(response){
                                var data = response.data;
                                if(data){
                                    self.$refs.verificationCode.timeTick();
                                }
                            }).catch(function () {
                                self.$message.error('请求失败');
                            })
                        }
                    })
                },
                findPwd:function () {
                    location.href = '/f/page-seekpassword';
                },
                validateCodeRefresh:function () {
                    this.imgCode = '/f/validateCode/createValidateCode?'+new Date().getTime();
                }

            },
            created:function(){
                if(this.fromLogin == '1'){
                    this.accountPwdForm.username = '';
                    this.messageForm.username = this.userName;
                    this.activeName = 'second';
                    this.loginType = 1;
                }else{
                    this.accountPwdForm.username = this.userName;
                    this.messageForm.username = '';
                    this.activeName = 'first';
                    this.loginType = 2;
                }
            }
        })


</script>

</body>
</html>
