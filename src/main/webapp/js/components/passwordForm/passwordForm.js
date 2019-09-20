/**
 * Created by Administrator on 2018/6/21.
 */



'use strict';

Vue.component('password-form', {
    template: '<div><el-form :model="passwordForm" :disabled="isUpdateing"  autocomplete="off" ref="passwordForm" :rules="passwordFormRules" label-width="100px" size="mini"> ' +
    '<el-form-item label="登录名：" ><el-input disabled v-model="loginCurUser.loginName" maxlength="100"><el-button slot="append" @click.stop="openDialogLoginName">修改</el-button></el-input></el-form-item><el-form-item label="旧密码：" prop="oldPassword"> ' +
    '<el-input type="password" name="oldPassword" v-model="passwordForm.oldPassword" auto-complete="off" maxlength="100"></el-input> </el-form-item> ' +
    '<el-form-item label="新密码：" prop="newPassword"> <el-input type="password" name="newPassword" v-model="passwordForm.newPassword" auto-complete="off" maxlength="100"></el-input> </el-form-item> ' +
    '<el-form-item label="确认新密码：" required prop="confirmNewPassword"> <el-input type="password" name="confirmNewPassword" v-model="passwordForm.confirmNewPassword" auto-complete="off" maxlength="100"></el-input> </el-form-item>' +
    '<slot><template v-if="!$slots.default"><el-form-item :class="deCls"><el-button type="primary" @click.stop.prevent="validateForm">保存</el-button></el-form-item></template></slot></template> ' +
    '</el-form><el-dialog title="修改登录名" :visible.sync="dialogVisibleLoginName" width="520px" :close-on-click-modal="false" :before-close="handleCloseLoginName">' +
    '<el-form :model="loginUserForm" ref="loginUserForm" :rules="loginUserRules" auto-complete="off" autocomplete="off" :disabled="loginUserFormDisabled" size="mini" label-width="120px">' +
    '<el-form-item prop="loginName" label="登录名："><el-input v-model="loginUserForm.loginName" maxlength="100"></el-input></el-form-item>' +
    '<el-form-item v-if="false" prop="password" label="确认密码："><el-input type="password" v-model="loginUserForm.password" maxlength="100"></el-input></el-form-item></el-form><span slot="footer" class="dialog-footer"><el-button :disabled="loginUserFormDisabled" type="primary" size="mini" @click="validateLoginForm">确 定</el-button></span></el-dialog></div>',
    mixins: [Vue.verifyExpressionMixin],
    props: {
        id: {
            required: true,
            type: String
        },
        isAdmin: Boolean
    },
    data: function () {
        var commonRule = {min: 6, max: 20, message: '请输入6-20位字符数字、字母', trigger: 'blur'};
        var self = this;
        var equalToPassword = function (rule, value, callback) {
            if (!value) return callback(new Error('请确认新密码'));
            if (self.passwordForm.newPassword !== value) {
                return callback(new Error('两次输入的密码不同'));
            }
            callback();
        };
        var equalToLoginName = function (rule, value, callback) {
            if (value) {
                if (value === self.loginCurUser.loginName) {
                    return callback(new Error('请修改登录名'));
                }
                return callback();
            }
            return callback();
        };
        var validateLoginNameFromPassword = this.validateLoginNameFromPassword;
        return {
            passwordForm: {
                oldPassword: '',
                newPassword: '',
                confirmNewPassword: ''
            },
            isUpdateing: false,
            passwordFormRules: {
                oldPassword: [
                    {required: true, message: '请填入旧密码', trigger: 'blur'},
                    commonRule
                ],
                newPassword: [
                    {required: true, message: '请填入新密码', trigger: 'blur'},
                    commonRule
                ],
                confirmNewPassword: [
                    {validator: equalToPassword}
                ]
            },

            dialogVisibleLoginName: false,
            loginUserForm: {
                loginName: '',
                password: ''
            },
            loginUserFormDisabled: false,
            loginUserRules: {
                loginName: [
                    {required: true, message: '请输入登录名', trigger: 'blur'},
                    {validator: equalToLoginName, trigger: 'blur'},
                    {validator: validateLoginNameFromPassword, trigger: 'blur'}
                ],
                password: [
                    {required: true, message: '请输入确认密码', trigger: 'blur'},
                    commonRule
                ]
            }
        }
    },
    computed: {
        deCls: function () {
            return {
                'text-right': !this.isAdmin
            }
        }
    },
    methods: {
        validateForm: function () {
            var self = this;
            this.$refs.passwordForm.validate(function (valid) {
                if (valid) {
                    self.isUpdateing = true;
                    self.updatePassword()
                }
            })
        },

        openDialogLoginName: function () {
            this.dialogVisibleLoginName = true;
        },

        handleCloseLoginName: function () {
            this.loginUserForm.password = '';
            this.$refs.loginUserForm.clearValidate();
            this.dialogVisibleLoginName = false;
        },

        validateLoginForm: function () {
            var self = this;
            this.$refs.loginUserForm.validate(function (valid) {
                if (valid) {
                    self.submitLoginForm();
                }
            })
        },

        submitLoginForm: function () {
            var self = this;
            this.loginUserFormDisabled = true;
            this.$axios({
                method: 'GET',
                url: '/sys/user/updateUserLoginName/' + this.loginCurUser.id,
                params: this.loginUserForm
            }).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.$message.success('保存成功');
                    self.loginCurUser.loginName = self.loginUserForm.loginName;
                    self.handleCloseLoginName();
                } else {
                    self.$message.error(data.msg);
                }
                self.loginUserFormDisabled = false;
            }).catch(function (error) {
                self.$message.error(self.errorMsg);
            })
        },

        updatePassword: function () {
            var self = this;


            this.$axios({
                method: 'GET',
                url: '/sys/user/ajaxUpdatePassWord/' + this.id,
                params: this.passwordForm
            }).then(function (response) {
                var data = response.data;
                var type = data.status ? 'success' : 'error';
                if (data.status) {
                    self.$refs.passwordForm.resetFields();
                }
                self.$msgbox({
                    message: data.msg,
                    title: '提示',
                    showClose: false,
                    closeOnClickModal: false,
                    type: type
                }).then(function () {
                    var url = '', user = {};
                    if (data.status) {
                        var referrer = document.referrer;
                        if (!self.isAdmin) {
                            if (referrer.indexOf('toLogin') > -1) {
                                referrer = '/f';
                            }
                            location.href = referrer;
                        }
                    }
                })
                self.isUpdateing = false;
            }).catch(function (error) {
                self.isUpdateing = false;
                self.$msgbox({
                    message: self.xhrErrorMsg,
                    title: '提示',
                    type: 'error'
                })
            })
        }
    },
    created: function () {
        this.assignFormData(this.loginUserForm, this.loginCurUser);
    }
})


