<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="creative"/>
</head>
<body>

<div id="app" v-show="pageLoad" style="display: none;">
    <div class="container page-container">

        <div v-if="hasMvcode" class="seek-pwd-box">
            <h2>找回密码</h2>
            <div v-if="firstStep">
                <img class="procedure" src="${ctxImg}/seekpassprocedure_03.png" alt="" />
                <div class="write-form">
                    <p class="please-write">请填写您需要找回的账号</p>

                    <el-form :model="seekPwdFirstForm" ref="seekPwdFirstForm" size="mini" :rules="rules" class="demo-ruleForm">
                        <el-form-item prop="phonemailxuehao">
                            <el-input type="text" name="phonemailxuehao" v-model="seekPwdFirstForm.phonemailxuehao" auto-complete="off" placeholder="请您输入手机号">
                                <el-button slot="append"><i class="iconfont icon-unie64f"></i></el-button>
                            </el-input>
                        </el-form-item>

                        <el-form-item prop="yanzhengma" class="validate-code-img">
                            <el-input type="text" name="yanzhengma" v-model="seekPwdFirstForm.yanzhengma" auto-complete="off" placeholder="请输入验证码"></el-input>
                            <img :src="imgCode" @click.stop.prevent="validateCodeRefresh">
                        </el-form-item>

                        <div style="color:#E9432D;height:15px;margin-bottom:10px;">{{firstError}}</div>

                        <el-button type="primary" size="mini" class="next-step-btn" @click.stop.prevent="firstNextStep('seekPwdFirstForm')">下 一 步</el-button>

                    </el-form>
                </div>
            </div>

            <div v-if="secondStep">
                <img class="procedure" src="${ctxImg}/seekpassprocedure2_03.png" alt="" />
                <div class="write-form">
                    <p class="please-write">为了你的账号安全，请完成验证信息</p>
                    <p style="margin-bottom:20px;">手机号：{{seekPwdFirstForm.phonemailxuehao}}</p>

                    <el-form :model="seekPwdSecondForm" ref="seekPwdSecondForm" size="mini" :rules="rules" class="demo-ruleForm">

                        <el-form-item prop="validateCodePhone">
                            <el-input type="text" name="yanzhengma" v-model="seekPwdSecondForm.validateCodePhone" auto-complete="off" placeholder="请输入短信验证码">
                                <verification-code-btn slot="append" ref="verificationCode" :time-count.sync="timeCount" @post-code="getVerity">
                                </verification-code-btn>
                            </el-input>
                        </el-form-item>

                        <div style="color:#E9432D;height:15px;margin-bottom:10px;">{{secondError}}</div>

                        <el-button type="primary" size="mini" class="next-step-btn" @click.stop.prevent="secondNextStep('seekPwdSecondForm')">下 一 步</el-button>

                    </el-form>
                </div>
            </div>


            <div v-if="thirdStep">
                <img class="procedure" src="${ctxImg}/seekpassprocedure3_03.png" alt="" />
                <div class="write-form">
                    <p class="please-write">为了你的账号安全，请完成验证信息</p>

                    <el-form :model="seekPwdThirdForm" ref="seekPwdThirdForm" size="mini" :rules="rules" class="demo-ruleForm">

                        <el-form-item prop="password">
                            <el-input type="password" name="password" v-model="seekPwdThirdForm.password" auto-complete="off" placeholder="请输入新密码">
                                <el-button slot="append"><i class="iconfont icon-jiesuo"></i></el-button>
                            </el-input>
                        </el-form-item>

                        <el-form-item prop="confirmPassword">
                            <el-input type="password" name="confirm_password" v-model="seekPwdThirdForm.confirmPassword" auto-complete="off" placeholder="请再次输入新密码">
                            </el-input>
                        </el-form-item>

                        <div style="color:#E9432D;height:15px;margin-bottom:10px;">{{thirdError}}</div>

                        <el-button type="primary" size="mini" class="next-step-btn" @click.stop.prevent="confirmSubmit('seekPwdThirdForm')">确　认</el-button>

                    </el-form>
                </div>
            </div>

            <div v-if="seekPwdSuccess">
                <img class="procedure" src="${ctxImg}/seekpassprocedure3_03.png" alt="" />
                <div class="seek-success-box">
                    <img src="${ctxImg}/seek-pwd-success.jpg" alt="" class="seek-success-img">
                    <div class="seek-success-advice">
                        <p>尊敬的用户：</p>
                        <p>修改密码成功！请前往登录页面进行<a href="${ctxFront}/toLogin">登录</a> 。</p>
                    </div>
                </div>
            </div>

        </div>

    </div>
</div>

<script>

    +function(Vue){

        'use strict'

        var app = new Vue({
            el:'#app',
            mixins:[Vue.seekPwdMixin],
            data:function(){

                return {
                    seekPwdFirstForm:{
                        phonemailxuehao:'',
                        yanzhengma:''
                    },
                    seekPwdSecondForm:{
                        validateCodePhone:''
                    },
                    seekPwdThirdForm:{
                        password:'',
                        confirmPassword:''
                    },
                    imgCode:'${ctxFront}/validateCode/createValidateCode',
                    firstError:'',
                    secondError:'',
                    thirdError:'',
                    firstStep:true,
                    secondStep:false,
                    thirdStep:false,
                    seekPwdSuccess:false,
                    timeCount:60

                }
            },
            methods:{
                firstNextStep:function(formName){
                    var self = this;
                    this.firstError = '';
                    this.$refs[formName].validate(function(valid){
                        if(valid){
                            self.$axios({
                                method:'POST',
                                url:'/confirm?phonemailxuehao=' + self.seekPwdFirstForm.phonemailxuehao + '&accountVcode=' + self.seekPwdFirstForm.yanzhengma
                            }).then(function (response) {
                                var data = response.data;
                                if(data == '1'){
                                    self.firstStep = false;
                                    self.secondStep = true;
                                    self.thirdStep = false;
                                    self.seekPwdSuccess = false;
                                }else if(data == '0'){
                                    self.firstError = '该手机号未注册';
                                }else if(data == '2'){
                                    self.firstError = '验证码错误';
                                }else{
                                    self.firstError = '验证码不能为空';
                                }
                            }).catch(function () {
                                self.$message.error('请求失败');
                            })
                        }
                    })
                },
                secondNextStep:function(formName){
                    var self = this;
                    this.secondError = '';
                    this.$refs[formName].validate(function(valid){
                        if(valid){
                            self.$axios({
                                method:'POST',
                                url:'/safeConfirm?accountMvcode=' + self.seekPwdSecondForm.validateCodePhone + '&phone=' + self.seekPwdFirstForm.phonemailxuehao
                            }).then(function (response) {
                                var data = response.data;
                                if(data == '1'){
                                    self.firstStep = false;
                                    self.secondStep = false;
                                    self.thirdStep = true;
                                    self.seekPwdSuccess = false;
                                }else{
                                    self.secondError = '短信验证码错误'
                                }
                            }).catch(function () {
                                self.$message.error('请求失败');
                            })
                        }
                    })

                },
                confirmSubmit:function(formName){
                    var self = this;
                    this.thirdError = '';
                    this.$refs[formName].validate(function(valid){
                        if(valid){
                            self.$axios({
                                method:'POST',
                                url:'/resetPwd?password=' + self.seekPwdThirdForm.password + '&repassword=' + self.seekPwdThirdForm.confirmPassword + '&phoneNum=' + self.seekPwdFirstForm.phonemailxuehao
                            }).then(function (response) {
                                var data = response.data;
                                if(data == '1'){
                                    self.firstStep = false;
                                    self.secondStep = false;
                                    self.thirdStep = false;
                                    self.seekPwdSuccess = true;
                                }else{
                                    self.thirdError = '修改密码失败'
                                }
                            }).catch(function () {
                                self.$message.error('请求失败');
                            })
                        }
                    })
                },
                getVerity:function(){
                    var self = this;
                    self.$axios({
                        method:'POST',
                        url:'/sendMessage?phone=' + self.seekPwdFirstForm.phonemailxuehao
                    }).then(function(response){
                        var data = response.data;
                        if(data == '1'){
                            self.$refs.verificationCode.timeTick();
                        }
                    }).catch(function () {
                        self.$message.error('请求失败');
                    })

                },
                validateCodeRefresh:function(){
                    this.imgCode = '${ctxFront}/validateCode/createValidateCodeByAccount?'+new Date().getTime();
                }
            }

        })

    }(Vue)

</script>

</body>
</html>
