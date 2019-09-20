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


<div id="app" v-show="pageLoad" style="display: none;" class="user-register">
    <div class="container page-container" style="padding-top:45px;">
        <div class="user-register-box">
            <p>注册用户</p>
            <div class="user-register-form">
                <el-tabs v-model="activeName" @tab-click="handleClick" :class="{'one-tab':teacherRegister != '1'}">
                    <el-tab-pane label="学生注册" name="first">
                        <el-form action="${ctxFront}/register/saveRegister" :model="studentForm" ref="studentForm"
                                 size="mini" :rules="rules" :disabled="disabled" class="demo-ruleForm">
                            <input type="hidden" name="userType" :value="userType"/>
                            <el-form-item prop="registerWay">
                                <input type="hidden" name="regType" :value="studentForm.defaultRegType">
                                <el-select v-model="studentForm.defaultRegType" class="reg-way"
                                           @change="stuRegTypeChange" placeholder="请选择">
                                    <el-option v-for="item in stuRegType" :key="item.value" :label="item.label"
                                               :value="item.value"></el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item prop="loginName">
                                <el-input type="text" name="loginName" v-model="studentForm.loginName"
                                          @keyup.enter.native="submitForm" auto-complete="off" placeholder="请输入登录名">
                                    <el-button slot="append"><i class="iconfont icon-yonghudianji"></i></el-button>
                                </el-input>
                            </el-form-item>

                            <el-form-item prop="name">
                                <el-input type="text" name="name" v-model="studentForm.name"
                                          @keyup.enter.native="submitForm" auto-complete="off" placeholder="请输入真实姓名">
                                    <el-button slot="append">
                                        <img src="/img/login-real-name.png" alt="" class="real-name-img">
                                    </el-button>
                                </el-input>
                            </el-form-item>

                            <el-form-item prop="mobile" v-if="stuMobileWay && hasMvcode">
                                <el-input type="number" name="mobile" v-model.number="studentForm.mobile"
                                          @keyup.enter.native="submitForm" auto-complete="off" placeholder="请输入手机号">
                                    <el-button slot="append"><i class="iconfont icon-unie64f"></i></el-button>
                                </el-input>
                            </el-form-item>

                            <el-form-item prop="yanzhengma" v-if="stuMobileWay">
                                <el-input type="text" name="mobileVcode" v-model="studentForm.yanzhengma"
                                          @keyup.enter.native="submitForm" auto-complete="off" placeholder="请输入验证码">
                                    <verification-code-btn slot="append" ref="StuVerificationCode"
                                                           :time-count.sync="stuTimeCount" @post-code="getVerity">
                                    </verification-code-btn>
                                </el-input>
                            </el-form-item>

                            <el-form-item prop="no" v-if="stuNoWay">
                                <el-input type="text" name="no" v-model="studentForm.no"
                                          @keyup.enter.native="submitForm" auto-complete="off"
                                          placeholder="请输入学号"></el-input>
                            </el-form-item>

                            <el-form-item prop="validateCode" v-show="stuNoWay" class="reg-validate-code">
                                <el-input type="text" name="validateCode" v-model="studentForm.validateCode"
                                          @keyup.enter.native="submitForm" auto-complete="off"
                                          placeholder="请输入验证码"></el-input>
                                <img :src="imgCode" @click.stop.prevent="validateCodeRefresh">
                            </el-form-item>

                            <el-form-item prop="password">
                                <el-input type="password" name="password" v-model="studentForm.password"
                                          @keyup.enter.native="submitForm" auto-complete="off"
                                          placeholder="请输入6~20位数字、字母登录密码">
                                    <el-button slot="append"><i class="iconfont icon-jiesuo"></i></el-button>
                                </el-input>
                            </el-form-item>

                            <el-form-item prop="confirmPassword">
                                <el-input type="password" name="confirm_password" v-model="studentForm.confirmPassword"
                                          @keyup.enter.native="submitForm" auto-complete="off"
                                          placeholder="请确认登录密码"></el-input>
                            </el-form-item>
                        </el-form>
                    </el-tab-pane>

                    <el-tab-pane label="导师注册" name="second" v-if="teacherRegister == '1'">
                        <el-form action="${ctxFront}/register/saveRegister" :model="teacherForm" ref="teacherForm"
                                 size="mini" :disabled="disabled" :rules="rules" class="demo-ruleForm">
                            <input type="hidden" name="userType" :value="userType"/>
                            <el-form-item prop="registerWay">
                                <input type="hidden" name="regType" :value="teacherForm.defaultRegType">
                                <el-select v-model="teacherForm.defaultRegType" class="reg-way"
                                           @change="teaRegTypeChange" placeholder="请选择">
                                    <el-option v-for="item in stuRegType" :key="item.value" :label="item.label"
                                               :value="item.value"></el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item prop="loginName">
                                <el-input type="text" name="loginName" v-model="teacherForm.loginName"
                                          @keyup.enter.native="submitForm" auto-complete="off" placeholder="请输入登录名">
                                    <el-button slot="append"><i class="iconfont icon-yonghudianji"></i></el-button>
                                </el-input>
                            </el-form-item>

                            <el-form-item prop="name">
                                <el-input type="text" name="name" v-model="teacherForm.name"
                                          @keyup.enter.native="submitForm" auto-complete="off" placeholder="请输入真实姓名">
                                    <el-button slot="append">
                                        <img src="/img/login-real-name.png" alt="" class="real-name-img">
                                    </el-button>
                                </el-input>
                            </el-form-item>

                            <el-form-item prop="mobile" v-if="teaMobileWay && hasMvcode">
                                <el-input type="number" name="mobile" v-model.number="teacherForm.mobile"
                                          @keyup.enter.native="submitForm" auto-complete="off" placeholder="请输入手机号">
                                    <el-button slot="append"><i class="iconfont icon-unie64f"></i></el-button>
                                </el-input>
                            </el-form-item>

                            <el-form-item prop="yanzhengma" v-if="teaMobileWay">
                                <el-input type="text" name="mobileVcode" v-model="teacherForm.yanzhengma"
                                          @keyup.enter.native="submitForm" auto-complete="off" placeholder="请输入验证码">
                                    <verification-code-btn slot="append" ref="TeaVerificationCode"
                                                           :time-count.sync="teaTimeCount" @post-code="getVerity">
                                    </verification-code-btn>
                                </el-input>
                            </el-form-item>

                            <el-form-item prop="no" v-if="teaNoWay">
                                <el-input type="text" name="no" v-model="teacherForm.no"
                                          @keyup.enter.native="submitForm" auto-complete="off"
                                          placeholder="请输入工号"></el-input>
                            </el-form-item>

                            <el-form-item prop="validateCode" v-show="teaNoWay" class="reg-validate-code">
                                <el-input type="text" name="validateCode" v-model="teacherForm.validateCode"
                                          @keyup.enter.native="submitForm" auto-complete="off"
                                          placeholder="请输入验证码"></el-input>
                                <img :src="imgCode" @click.stop.prevent="validateCodeRefresh">
                            </el-form-item>

                            <el-form-item prop="password">
                                <el-input type="password" name="password" v-model="teacherForm.password"
                                          @keyup.enter.native="submitForm" auto-complete="off"
                                          placeholder="请输入6~20位数字、字母登录密码">
                                    <el-button slot="append"><i class="iconfont icon-jiesuo"></i></el-button>
                                </el-input>
                            </el-form-item>

                            <el-form-item prop="confirmPassword">
                                <el-input type="password" name="confirm_password" v-model="teacherForm.confirmPassword"
                                          @keyup.enter.native="submitForm" auto-complete="off"
                                          placeholder="请确认登录密码"></el-input>
                            </el-form-item>
                        </el-form>
                    </el-tab-pane>

                    <div class="text-center register-btn">
                        <el-button type="primary" @click="submitForm" :disabled="disabled">注册</el-button>
                        <a href="/f/toLogin">已有账号，去登录</a>
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
    'use strict'

    new Vue({
        el: '#app',
        mixins: [Vue.verifyRegMixin],
        data: function () {

            return {
                teacherRegister: '${teacherRegister}',
                activeName: 'first',
//                stuRegType: [
//                    {label: '手机注册', value: 'mobile'},
//                    {label: '学号注册', value: 'no'}
//                ],
//                teaRegType: [
//                    {label: '手机注册', value: 'mobile'},
//                    {label: '工号注册', value: 'no'}
//                ],
                selectedRegType: 'no',
                userType: 1,
                studentForm: {
                    defaultRegType: 'no',
                    loginName: '',
                    name: '',
                    mobile: '',
                    yanzhengma: '',
                    no: '',
                    validateCode: '',
                    password: '',
                    confirmPassword: ''
                },
                teacherForm: {
                    defaultRegType: 'no',
                    loginName: '',
                    name: '',
                    mobile: '',
                    yanzhengma: '',
                    no: '',
                    validateCode: '',
                    password: '',
                    confirmPassword: ''
                },
                stuMobileWay: false,
                stuNoWay: true,
                teaMobileWay: false,
                teaNoWay: true,
                imgCode: '/f/validateCode/createValidateCode',
                stuTimeCount: 60,
                teaTimeCount: 60,
                disabled: false,

            }
        },
        computed: {
            formName: {
                get: function () {
                    return this.userType == '1' ? 'studentForm' : 'teacherForm';
                }
            },
            stuRegType: function () {
                var options = [{label: (this.userType == '1' ? '学号' : '工号') + '注册', value: 'no'}]
                if(this.hasMvcode){
                    options.push({label: '手机注册', value: 'mobile'})
                }
                return options;
            }
        },
        methods: {
            handleClick: function (tab) {
                this.validateCodeRefresh();
                if (tab.name == 'first') {
                    this.$refs['studentForm'].resetFields();
                    this.studentForm.defaultRegType = 'no';
                    this.selectedRegType = 'no';
                    this.stuMobileWay = false;
                    this.stuNoWay = true;
                    this.userType = 1;
                    this.stuTimeCount = -1;
                } else {
                    this.$refs['teacherForm'].resetFields();
                    this.teacherForm.defaultRegType = 'no';
                    this.selectedRegType = 'no';
                    this.teaMobileWay = false;
                    this.teaNoWay = true;
                    this.userType = 2;
                    this.teaTimeCount = -1;
                }
            },
            submitForm: function () {
                var self = this;
                this.$refs[this.formName].validate(function (valid,error) {
                    if (valid) {
                        self.disabled = true;
                        self.$refs[self.formName].$el.submit();
                    }
                })
            },
            stuRegTypeChange: function (val) {
                this.selectedRegType = val;
                this.studentForm.defaultRegType = val;
                this.$refs['studentForm'].resetFields();
                if (val == 'mobile') {
                    this.stuMobileWay = true;
                    this.stuNoWay = false;
                } else {
                    this.stuMobileWay = false;
                    this.stuNoWay = true;
                }
            },
            teaRegTypeChange: function (val) {
                this.selectedRegType = val;
                this.teacherForm.defaultRegType = val;
                this.$refs['teacherForm'].resetFields();
                if (val == 'mobile') {
                    this.teaMobileWay = true;
                    this.teaNoWay = false;
                } else {
                    this.teaMobileWay = false;
                    this.teaNoWay = true;
                }
            },
            getVerity: function () {
                var self = this;
                var refsCode = '';
                var mobile = '';
                if (this.userType == '1') {
                    refsCode = 'StuVerificationCode';
                    mobile = self.studentForm.mobile;
                } else {
                    refsCode = 'TeaVerificationCode';
                    mobile = self.teacherForm.mobile;
                }
                this.$refs[this.formName].validateField('mobile', function (valid) {
                    if (valid == '') {
                        self.$axios({
                            method: 'GET',
                            url: '/register/getVerificationCode?mobileVcode=' + mobile
                        }).then(function (response) {
                            var data = response.data;
                            if (data) {
                                self.$refs[refsCode].timeTick();
                            }
                        }).catch(function () {
                            self.$message.error('请求失败');
                        })
                    }
                })
            },
            validateCodeRefresh: function () {
                this.imgCode = '/f/validateCode/createValidateCode?' + new Date().getTime();
            }
        }
    })


</script>


</body>
</html>
