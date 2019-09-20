/**
 * Created by Administrator on 2018/6/14.
 */


;+function (Vue) {
    'use strict';
    Vue.verifyExpressionMixin = {
        data: function () {
            return {
                identityReg: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
                emailReg: /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/,
                mobileReg: /^((19[0-9])|(16[0-9])|(13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\d{8}$$/,
                qqReg: /^[1-9][0-9]{4,14}$/,
                userNoReg: /^[a-zA-Z0-9]+$/,
                loginNameReg: /['"\s]+/,
                userNameReg: /['"\s“”‘’]+/,
                specificSymbol: /[@#\$%\^&\*\s]+/g
            }
        },
        methods: {
            validateUserName: function (rule, value, callback) {
                if (value) {
                    if (this.userNameReg.test(value)) {
                        return callback(new Error('姓名存在空格或者引号'))
                    }
                }
                return callback()
            },
            validateLoginName: function (rule, value, callback) {
                var self = this;
                if (value) {
                    var userBaseForm = this.userBaseForm;
                    var userId = userBaseForm.userId;
                    // var no = userBaseForm.userNo;
                    var loginName = userBaseForm.userLoginName;
                    if (this.loginNameReg.test(value)) {
                        return callback(new Error('登录名存在空格或者引号'))
                    }
                    return this.$axios.post('/sys/user/checkLoginName?' + Object.toURLSearchParams({
                            userid: userId,
                            loginName: loginName
                        })).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            return callback(new Error('登录名已存在'))
                        }
                        return callback();
                    }).catch(function (error) {
                        return callback(new Error('请求失败'))
                    })
                }
                return callback()
            },

            validateLoginNameFromPassword: function (rule, value, callback) {
                var self = this;
                if (value) {
                    var loginUser = this.loginCurUser;
                    var userId = loginUser.id;
                    // var no = userBaseForm.userNo;
                    var loginName = this.loginUserForm.loginName;
                    if (this.loginNameReg.test(value)) {
                        return callback(new Error('登录名存在空格或者引号'))
                    }
                    return this.$axios.post('/sys/user/checkLoginName?' + Object.toURLSearchParams({
                            userid: userId,
                            loginName: loginName
                        })).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            return callback(new Error('登录名已存在'))
                        }
                        return callback();
                    }).catch(function (error) {
                        return callback(new Error('请求失败'))
                    })
                }
                return callback()
            },

            validateLoginNameTeacher: function (rule, value, callback) {
                var self = this;
                if (value) {
                    var teacherForm = this.teacherForm;
                    var userId = teacherForm.userid;
                    var no = teacherForm.userNo;
                    var loginName = teacherForm.userLoginName;
                    if (this.loginNameReg.test(value)) {
                        return callback(new Error('登录名存在空格或者引号'))
                    }
                    return this.$axios.post('/sys/user/checkLoginName?' + Object.toURLSearchParams({
                            userid: userId,
                            loginName: loginName
                        })).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            return callback(new Error('登录名已存在'))
                        }
                        return callback();
                    }).catch(function (error) {
                        return callback(new Error('请求失败'))
                    })
                }
                return callback()
            },


            validateIdentity: function (rule, value, callback) {
                if (value) {
                    if (!this.identityReg.test(value)) {
                        return callback(new Error('请输入有效的有效的身份证号码'));
                    } else {
                        return callback()
                    }
                }
                return callback()
            },
            validateEmail: function (rule, value, callback) {
                if (value) {
                    if (!this.emailReg.test(value)) {
                        return callback(new Error('请输入有效邮箱地址'));
                    } else {
                        return callback()
                    }
                }
                return callback()
            },
            validateMobile: function (rule, value, callback) {
                if (value) {
                    if (!this.mobileReg.test(value)) {
                        return callback(new Error('请输入有效手机号'));
                    } else {
                        return callback()
                    }
                }
                return callback()
            },

            validateMobileXhr: function (rule, value, callback) {
                if (value) {
                    return this.$axios.post('/sys/user/checkMobileExist?mobile=' + value).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            return callback(new Error('手机号已经存在'))
                        }
                        return callback();
                    }).catch(function (error) {
                        return callback(new Error('请求失败'))
                    })
                }
                return callback()
            },
            //后台修改手机号验证
            validateMobileBackXhr: function (rule, value, callback) {
                if (value) {
                    return this.$axios.post('/sys/user/checkMobile?mobile=' + value + "&id=" + this.userBaseForm.userId).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            return callback(new Error('手机号已经存在'))
                        }
                        return callback();
                    }).catch(function (error) {
                        return callback(new Error('请求失败'))
                    })
                }
                return callback()
            },

            validateVerityCodeXhr: function (rule, value, callback) {
                if (value) {
                    return this.$axios.post('/mobile/checkMobileValidateCode?yzm=' + value).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            return callback(new Error('验证码错误'))
                        }
                        return callback();
                    }).catch(function (error) {
                        return callback(new Error('请求失败'))
                    })
                }
                return callback()
            },

            validateEmailYZMXhr: function (rule, value, callback) {
                if (value) {
                    return this.$axios.get('/sys/frontStudentExpansion/checkEmailVerityCode?code=' + value).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            return callback(new Error('验证码错误'))
                        }
                        return callback();
                    }).catch(function (error) {
                        return callback(new Error('请求失败'))
                    })
                }
                return callback()
            },

            validateQQ: function (rule, value, callback) {
                if (value) {
                    if (!this.qqReg.test(value)) {
                        return callback(new Error('请输入有效QQ号'));
                    } else {
                        return callback()
                    }
                }
                return callback()
            },
            validateUserNo: function (rule, value, callback) {
                var self = this;
                if (value) {
                    var isUserForm = !!this.userBaseForm;
                    var userid = (isUserForm ? this.userBaseForm.userId : this.teacherForm.userid);
                    var label = (isUserForm ? '学号' : '职工号');
                    if (!this.userNoReg.test(value)) {
                        return callback(new Error('请输入英文或者数字'));
                    } else {
                        return this.$axios.post('/sys/user/checkNo?' + Object.toURLSearchParams({
                                userid: userid,
                                no: value
                            })).then(function (response) {
                            var data = response.data;
                            if (data) {
                                return callback();
                            }
                            return callback(label + '已经存在')
                        })
                    }
                }
                return callback()
            }
        }
    };


    Vue.verifyRegMixin = {
        data: function () {
            var self = this;
            var mobileReg = /^((19[0-9])|(16[0-9])|(13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\d{8}$$/;
            var yanzhengma = /^([a-zA-Z0-9]+)$/;
            var passwordReg = /^[0-9A-Za-z]{6,20}$/;
            var userNoReg = /^[a-zA-Z0-9]+$/;
            var loginNameReg = /['"\s]+/;

            var validateLoginName = function (rule, value, callback) {
                if (value) {
                    if (loginNameReg.test(value)) {
                        return callback(new Error('登录名存在空格或者引号'))
                    }
                    return self.$axios.post('/sys/user/checkLoginName?loginName=' + value).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            return callback(new Error('登录名已存在'))
                        }
                        return callback();
                    }).catch(function (error) {
                        return callback(new Error('请求失败'))
                    })
                }
                return callback()
            };

            var validateMobile = function (rule, value, callback) {
                if (self.selectedRegType == 'mobile') {
                    if (value) {
                        if (!mobileReg.test(value)) {
                            callback(new Error('请输入有效手机号'));
                        } else {
                            self.$axios({
                                method: 'POST',
                                url: '/register/validatePhone?mobile=' + value
                            }).then(function (response) {
                                var data = response.data;
                                if (!data) {
                                    callback(new Error('该手机号已被注册'));
                                }
                                callback();
                            }).catch(function () {
                                callback(new Error('请求失败'))
                            })
                        }
                    } else {
                        callback(new Error('手机号必填'));
                    }
                } else {
                    callback();
                }
            };

            var validateYanzhengma = function (rule, value, callback) {
                if (self.selectedRegType == 'mobile') {
                    if (value) {
                        if (!yanzhengma.test(value)) {
                            callback(new Error('验证码必须为数字或字母组成'));
                        } else {
                            self.$axios({
                                method: 'POST',
                                url: '/register/validateYZM?mobileVcode=' + value
                            }).then(function (response) {
                                var data = response.data;
                                if (!data) {
                                    callback(new Error('验证码错误'));
                                }
                                callback();
                            }).catch(function () {
                                callback(new Error('请求失败'))
                            })
                        }
                    } else {
                        callback(new Error('验证码必填'));
                    }
                } else {
                    callback();
                }
            };


            var validateNo = function (rule, value, callback) {
                if (value) {
                    if (!userNoReg.test(value)) {
                        callback(new Error('请输入英文或者数字'));
                    } else {
                        self.$axios.post('/sys/user/checkNo?no=' + value).then(function (response) {
                            var data = response.data;
                            if (!data) {
                                callback(new Error('该工号/学号已存在'))
                            }
                            callback();
                        }).catch(function () {
                            callback(new Error('请求失败'))
                        });
                    }
                } else {
                    callback();
                }
            };

            var validateValidateCode = function (rule, value, callback) {
                if (self.selectedRegType == 'no') {
                    if (value) {
                        self.$axios({
                            method: 'POST',
                            url: '/register/checkCode?regType=' + self.selectedRegType + '&code=' + value
                        }).then(function (response) {
                            var data = response.data;
                            if (!data) {
                                callback(new Error('验证码错误'));
                            }
                            callback();
                        }).catch(function () {
                            callback(new Error('请求失败'))
                        })
                    } else {
                        callback(new Error('验证码必填'));
                    }
                } else {
                    callback();
                }
            };
            var validatePasswordReg = function (rule, value, callback) {
                if (value) {
                    if (!passwordReg.test(value)) {
                        callback(new Error('密码必须由6~20位数字或字母组成'));
                    } else {
                        callback();
                    }
                }
                callback();
            };
            var validateConfirmPassword = function (rule, value, callback) {
                var pwd;
                if (self.userType == '1') {
                    pwd = self.studentForm.password;
                } else if (self.userType == '2') {
                    pwd = self.teacherForm.password;
                }
                if (value) {
                    if (value != pwd) {
                        callback(new Error('两次密码输入不一致'));
                    } else {
                        callback();
                    }
                }
                callback();
            };
            return {
                rules: {
                    loginName: [
                        {required: true, message: '登录名必填', trigger: 'blur'},
                        {validator: validateLoginName, trigger: 'blur'}
                    ],
                    name: [
                        {required: true, message: '真实姓名必填', trigger: 'blur'},
                        {min: 2, message: '真实姓名至少两个字符', trigger: 'blur'}
                    ],
                    mobile: [
                        {validator: validateMobile, trigger: 'blur'}
                    ],
                    yanzhengma: [
                        {validator: validateYanzhengma, trigger: 'blur'}
                    ],
                    no: [
                        {required: true, message: '请输入学号/工号', trigger: 'blur'},
                        {validator: validateNo, trigger: 'blur'}
                    ],
                    validateCode: [
                        {validator: validateValidateCode, trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '密码必填', trigger: 'blur'},
                        {validator: validatePasswordReg, trigger: 'blur'}
                    ],
                    confirmPassword: [
                        {required: true, message: '确认密码必填', trigger: 'blur'},
                        {validator: validateConfirmPassword, trigger: 'blur'}
                    ]
                }
            }
        },
        methods: {}
    };

    Vue.frontLoginMixin = {
        data: function () {
            var self = this;
            var mobileReg = /^((19[0-9])|(16[0-9])|(13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\d{8}$$/;

            var validateUsername = function (rule, value, callback) {
                if (self.loginType == '1') {
                    if (value) {
                        if (!mobileReg.test(value)) {
                            callback(new Error('请输入有效手机号'));
                        } else {
                            self.$axios({
                                method: 'POST',
                                url: '/sys/user/isExistMobile?mobile=' + self.messageForm.username
                            }).then(function (response) {
                                var data = response.data;
                                if (!data) {
                                    callback(new Error('该手机号码未注册'))
                                }
                                callback();
                            }).catch(function () {
                                callback(new Error('请求失败'))
                            })
                        }
                    } else {
                        callback(new Error('手机号必填'));
                    }
                } else {
                    if (value) {
                        callback();
                    } else {
                        callback(new Error('登录名或学号必填'));
                    }
                }
            };

            var validatePassword = function (rule, value, callback) {
                if (self.loginType == '1') {
                    if (value) {
                        callback();
                    } else {
                        callback(new Error('验证码必填'));
                    }
                } else {
                    if (value) {
                        callback();
                    } else {
                        callback(new Error('密码必填'));
                    }
                }
            };

            var validateValidateCode = function (rule, value, callback) {
                if (self.loginType == '2' && self.isValidateCodeLogin == 'true') {
                    if (value) {
                        callback();
                    } else {
                        callback(new Error('验证码必填'));
                    }
                } else {
                    callback();
                }
            };

            return {
                rules: {
                    username: [
                        {validator: validateUsername, trigger: 'blur'}
                    ],
                    password: [
                        {validator: validatePassword, trigger: 'blur'}
                    ],
                    validateCode: [
                        {validator: validateValidateCode, trigger: 'blur'}
                    ]
                }
            }
        }
    };

    Vue.seekPwdMixin = {
        data: function () {
            var self = this;
            var mobileReg = /^((19[0-9])|(16[0-9])|(13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\d{8}$$/;
            var passwordReg = /^[0-9A-Za-z]{6,20}$/;

            var validatePhone = function (rule, value, callback) {
                if (value) {
                    if (!mobileReg.test(value)) {
                        callback(new Error('请输入有效手机号'));
                    } else {
                        callback();
                    }
                } else {
                    callback(new Error('手机号必填'));
                }
            };

            var validatePassword = function (rule, value, callback) {
                if (value) {
                    if (!passwordReg.test(value)) {
                        callback(new Error('密码必须由6~20位数字或字母组成'));
                    } else {
                        callback();
                    }
                } else {
                    callback(new Error('密码必填'));
                }
            };
            var validateConfirmPassword = function (rule, value, callback) {
                if (value) {
                    if (value != self.seekPwdThirdForm.password) {
                        callback(new Error('两次密码输入不一致'));
                    } else {
                        callback();
                    }
                } else {
                    callback(new Error('确认密码必填'));
                }
            };

            return {
                rules: {
                    phonemailxuehao: [
                        {validator: validatePhone, trigger: 'blur'}
                    ],
                    yanzhengma: [
                        {required: true, message: '验证码必填', trigger: 'blur'}
                    ],
                    validateCodePhone: [
                        {required: true, message: '短信验证码必填', trigger: 'blur'}
                    ],
                    password: [
                        {validator: validatePassword, trigger: 'blur'}
                    ],
                    confirmPassword: [
                        {validator: validateConfirmPassword, trigger: 'blur'}
                    ]
                }
            }
        }
    };

    Vue.leaveMsgMixin = {
        data: function () {
            var mobileReg = /^((19[0-9])|(16[0-9])|(13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\d{8}$$/;
            var emailReg = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/;
            var qqReg = /^[1-9][0-9]{4,14}$/;


            var validatePhone = function (rule, value, callback) {
                if (value) {
                    if (!mobileReg.test(value)) {
                        callback(new Error('请输入有效的手机号'));
                    } else {
                        callback();
                    }
                } else {
                    callback();
                }
            };

            var validateEmail = function (rule, value, callback) {
                if (value) {
                    if (!emailReg.test(value)) {
                        callback(new Error('请输入有效的邮箱'));
                    } else {
                        callback();
                    }
                } else {
                    callback();
                }
            };
            var validateQQ = function (rule, value, callback) {
                if (value) {
                    if (!qqReg.test(value)) {
                        callback(new Error('请输入有效的QQ'));
                    } else {
                        callback();
                    }
                } else {
                    callback();
                }
            };
            return {
                leaveMsgFormRules: {
                    // name: [
                    //     {required: true, message: '请输入名称', trigger: 'blur'}
                    // ],
                    phone: [
                        // {required: true, message: '请输入手机号', trigger: 'blur'},
                        {validator: validatePhone, trigger: 'blur'}
                    ],
                    email: [
                        // {required: false, message: '请输入邮箱', trigger: 'blur'},
                        {validator: validateEmail, trigger: 'blur'}
                    ],
                    qq: [
                        // {required: false, message: '请输入QQ', trigger: 'blur'},
                        {validator: validateQQ, trigger: 'blur'}
                    ],
                    files: [
                        {required: false, message: '请上传图片', trigger: 'blur'}
                    ],
                    type: [
                        {required: true, message: '请选留言类型', trigger: 'blur'}
                    ],
                    content: [
                        {required: true, message: '请输入内容', trigger: 'blur'}
                    ]
                }
            }
        }
    };

    Vue.userManageMixin = {
        data: function () {
            var self = this;
            var nameReg = /['"\s“”‘’]+/;
            var userNoReg = /^[a-zA-Z0-9]+$/;
            var mobileReg = /^((19[0-9])|(16[0-9])|(13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\d{8}$$/;
            var validateLoginName = function (rule, value, callback) {
                if (value) {
                    var createUserForm = self.createUserForm;
                    var userId = createUserForm.id;
                    var loginName = createUserForm.loginName;
                    if (nameReg.test(value)) {
                        return callback(new Error('登录名存在空格或者引号'))
                    }
                    return self.$axios.post('/sys/user/checkLoginName?' + Object.toURLSearchParams({
                            userid: userId,
                            loginName: loginName
                        })).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            return callback(new Error('登录名已存在'))
                        }
                        return callback();
                    }).catch(function (error) {
                        return callback(new Error('请求失败'))
                    })
                }
                return callback();
            };
            var validateLoginNameChange = function (rule, value, callback) {
                if (value) {
                    var changeUserForm = self.changeUserForm;
                    var userId = changeUserForm.id;
                    var loginName = changeUserForm.loginName;
                    if (nameReg.test(value)) {
                        return callback(new Error('登录名存在空格或者引号'))
                    }
                    return self.$axios.post('/sys/user/checkLoginName?' + Object.toURLSearchParams({
                            userid: userId,
                            loginName: loginName
                        })).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            return callback(new Error('登录名已存在'))
                        }
                        return callback();
                    }).catch(function (error) {
                        return callback(new Error('请求失败'))
                    })
                }
                return callback();
            };
            var validateName = function (rule, value, callback) {
                if (nameReg.test(value)) {
                    callback(new Error('姓名存在空格或者引号'))
                } else {
                    callback();
                }
            };
            var validateNo = function (rule, value, callback) {
                if (value) {
                    if (!userNoReg.test(value)) {
                        callback(new Error('请输入英文或者数字'));
                    } else {
                        self.$axios.post('/sys/user/checkNo?no=' + value + '&userid=' + self.createUserForm.id).then(function (response) {
                            var data = response.data;
                            if (!data) {
                                callback(new Error('该工号/学号已存在'))
                            }
                            callback();
                        }).catch(function () {
                            callback(new Error('请求失败'))
                        });
                    }
                } else {
                    callback();
                }
            };
            var validateMobile = function (rule, value, callback) {
                if (value) {
                    if (!mobileReg.test(value)) {
                        callback(new Error('请输入有效的手机号'));
                    } else {
                        self.$axios.post('/sys/user/checkMobile?mobile=' + value + '&id=' + self.createUserForm.id).then(function (response) {
                            var data = response.data;
                            if (!data) {
                                callback(new Error('手机号已存在'))
                            }
                            callback();
                        }).catch(function () {
                            callback(new Error('请求失败'))
                        });
                    }
                } else {
                    callback();
                }
            };
            var validateEmail = function (rule, value, callback) {
                var emailReg = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/;
                if (value) {
                    if (!emailReg.test(value)) {
                        callback(new Error('请输入有效的邮箱'));
                    } else {
                        callback();
                    }
                } else {
                    callback();
                }
            };
            var validateOfficeId = function (rule, value, callback) {
                if (self.cascaderList.length > 0 && self.roles.indexOf('ef8b7924557747e2ac71fe5b52771c08') > -1 && self.createUserForm.professional == '') {
                    callback(new Error('请选择到专业'));
                } else {
                    callback();
                }
            };

            var validateStuOfficeId = function (rule, value, callback) {
                if (value) {
                    var professional = self.changeUserForm.professional;
                    var collegeEntries = self.collegeEntries;
                    var proObj = collegeEntries[professional];
                    if (proObj && parseInt(proObj.grade) >= 3) {
                        return callback();
                    }
                    return callback('请选择专业')
                }
                return callback();
            }

            return {
                rules: {
                    loginName: [
                        {required: true, message: '请输入登录名', trigger: 'blur'},
                        {min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'},
                        {validator: validateLoginName, trigger: 'blur'}
                    ],
                    roleIdList: [
                        {required: true, message: '请选择用户角色', trigger: 'blur'}
                    ],
                    officeId: [
                        {required: true, message: '请选择所属机构', trigger: 'change'},
                        // {validator: validateOfficeId, trigger: 'change'}
                    ],
                    name: [
                        {required: true, message: '请输入姓名', trigger: 'blur'},
                        {min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'},
                        {validator: validateName, trigger: 'blur'}
                    ],
                    no: [
                        {required: true, message: '请输入学号/工号', trigger: 'blur'},
                        {min: 2, max: 24, message: '长度在 2 到 24 个字符', trigger: 'blur'},
                        {validator: validateNo, trigger: 'blur'}
                    ],
                    mobile: [
                        {validator: validateMobile, trigger: 'blur'}
                    ],
                    email: [
                        {validator: validateEmail, trigger: 'blur'}
                    ],
                    // expertTypeList:[
                    //     {required: true, message: '请选择专家来源', trigger: 'change'}
                    // ]
                },
                changeRules: {
                    loginName: [
                        {required: true, message: '请输入登录名', trigger: 'blur'},
                        {min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'},
                        {validator: validateLoginNameChange, trigger: 'blur'}
                    ],
                    officeId: [
                        {required: true, message: '请选择所属机构', trigger: 'change'},
                        {validator: validateStuOfficeId, trigger: 'change'}
                    ]
                }
            }
        }
    };


    Vue.roomFormMixin = {
        data: function () {
            var self = this;
            var nameReg = /['"\s“”‘’]+/;
            var mobileReg = /^((19[0-9])|(16[0-9])|(13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\d{8}$$/;
            var reg = /^[1-9][0-9]*$/;
            var areaReg = /^([1-9]{1}[0-9]{0,5}(\.{1}(?=\d+))|[1-9]{1}[0-9]{0,1})\d{0,4}$/;
            var validatePlace = function (rule, value, callback) {
                if (self.spaceListEntries[value].type != '4') {
                    callback(new Error('请选择楼层'))
                } else {
                    callback();
                }
            };
            var validateName = function (rule, value, callback) {
                if (nameReg.test(value)) {
                    callback(new Error('房间名称存在空格或者引号'))
                } else if (value != self.currentName) {
                    if (!self.roomForm.pwSpace.id) {
                        callback();
                    } else {
                        self.$axios.get('/pw/pwRoom/ajaxVerifyName?sid=' + self.roomForm.pwSpace.id + '&name=' + value).then(function (response) {
                            var data = response.data;
                            if (data.status == '0') {
                                callback(new Error('同楼层房间名已存在'))
                            }
                            callback();
                        }).catch(function () {
                            callback(new Error('请求失败'))
                        });
                    }
                } else {
                    callback();
                }
            };

            var validatePerson = function (rule, value, callback) {
                if (nameReg.test(value)) {
                    callback(new Error('负责人存在空格或者引号'))
                } else {
                    callback();
                }
            };
            var validateMobile = function (rule, value, callback) {
                if (value) {
                    if (!mobileReg.test(value)) {
                        callback(new Error('请输入有效的联系电话'));
                    } else {
                        callback();
                    }
                }
            };
            var validateArea = function (rule, value, callback) {
                if (value && !areaReg.test(value)) {
                    callback(new Error('请输入数字，如为小数，则小数点前最多6位数且最多保留4位小数'));
                } else {
                    callback();
                }
            };
            var validateNum = function (rule, value, callback) {
                var len = value.toString().length;
                if (!reg.test(value)) {
                    callback(new Error('请输入正整数'));
                } else if ((len > 6)) {
                    callback(new Error('房间容量最大6位数'))
                } else if (self.roomForm.numtype == '') {
                    callback(new Error('请选择容量类型'))
                } else {
                    callback();
                }
            };
            return {
                roomFormRules: {
                    pwSpace: {
                        id: [
                            {required: true, message: '请选择场地', trigger: 'change'},
                            {validator: validatePlace, trigger: 'change'}
                        ]
                    },
                    name: [
                        {required: true, message: '请输入房间名称', trigger: 'change'},
                        {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'change'},
                        {validator: validateName, trigger: 'blur'}
                    ],
                    mobile: [
                        {required: true, message: '请输入联系电话', trigger: 'change'},
                        {validator: validateMobile, trigger: 'change'}
                    ],
                    person: [
                        {required: true, message: '请输入负责人', trigger: 'change'},
                        {min: 1, max: 100, message: '长度在 1 到 15 个字符', trigger: 'change'},
                        {validator: validatePerson, trigger: 'change'}
                    ],
                    type: [
                        {required: true, message: '请选择房间类型', trigger: 'change'}
                    ],
                    num: [
                        {required: true, message: '请输入房间容量', trigger: 'change'},
                        {validator: validateNum, trigger: 'change'}
                    ],
                    color: [
                        {required: true, message: '请选择预约房间色值', trigger: 'change'}
                    ],
                    area: [
                        {validator: validateArea, trigger: 'change'}
                    ]
                }
            }
        }
    };


    Vue.verifyRules = {
        data: function () {
            var self = this;
            var userNameReg = /['"\s“”‘’]+/;
            var identityReg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
            var emailReg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
            var mobileReg = /^((19[0-9])|(16[0-9])|(13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\d{8}$$/;
            var qqReg = /^[1-9][0-9]{4,14}$/;
            var userNoReg = /^[a-zA-Z0-9]+$/;
            var loginNameReg = /['"\s]+/;
            var validateUserName = function (rule, value, callback) {
                if (value) {
                    if (userNameReg.test(value)) {
                        return callback(new Error('姓名存在空格或者引号'))
                    }
                    return callback();
                }
                return callback();
            };
            var validateIdNumber = function (rule, value, callback) {
                if (value) {
                    if (identityReg.test(value)) {
                        return callback()
                    }
                    return callback(new Error('请输入有效的有效的身份证号码'));
                }
                return callback();
            };
            var validateEmail = function (rule, value, callback) {
                if (value) {
                    if (!emailReg.test(value)) {
                        return callback(new Error('请输入有效邮箱地址'));
                    }
                    return callback()
                }
                return callback()
            };
            var validateMobile = function (rule, value, callback) {
                if (value) {
                    if (!mobileReg.test(value)) {
                        return callback(new Error('请输入有效手机号'));
                    }
                    return self.$axios.post('/sys/user/checkMobile?mobile=' + value + "&id=" + self.userId).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            return callback(new Error('手机号已经存在'))
                        }
                        return callback();
                    }).catch(function (error) {
                        return callback(new Error('请求失败'))
                    })
                }
                return callback()
            };
            var validateQQ = function (rule, value, callback) {
                if (value) {
                    if (!qqReg.test(value)) {
                        return callback(new Error('请输入有效QQ号'));
                    } else {
                        return callback()
                    }
                }
                return callback()
            };
            var validateUserNo = function (rule, value, callback) {
                if (value) {
                    var userid = self.userId;
                    var label = '号码';
                    if (!userNoReg.test(value)) {
                        return callback(new Error('请输入英文或者数字'));
                    } else {
                        return self.$axios.get('/sys/user/checkNo', {
                            params: {
                                userid: userid,
                                no: value
                            }
                        }).then(function (response) {
                            var data = response.data;
                            if (data) {
                                return callback();
                            }
                            return callback(label + '已经存在')
                        })
                    }
                }
                return callback()
            };
            var validateProfessional = function (rule, value, callback) {
                if (value && value.length > 0) {
                    var professionalId = value[value.length - 1];
                    if (!self.officeEntity) {
                        return callback(new Error("officeEntity is undefined"))
                    }
                    var professional = self.officeEntity[professionalId];
                    if (professional && parseInt(professional.grade) >= 3) {
                        return callback();
                    }
                    return callback(new Error('请选择专业'))
                }
                return callback();
            };

            var validateProfessionalAsTeacher = function (rule, value, callback) {
                if (value && value.length > 0) {
                    var professionalId = value[value.length - 1];
                    if (!self.officeEntity) {
                        return callback(new Error("officeEntity is undefined"))
                    }
                    var professional = self.officeEntity[professionalId];
                    if (professional && parseInt(professional.grade) >= 2) {
                        return callback();
                    }
                    return callback(new Error('请选择学院'))
                }
                return callback();
            };

            var validateLoginName = function (rule, value, callback) {
                if (value) {
                    var userId = self.userId;
                    var loginName = self.loginName;
                    if (loginNameReg.test(value)) {
                        return callback(new Error('登录名存在空格或者引号'))
                    }
                    if ((/[\u4e00-\u9fa5]+/.test(value))) {
                        return callback(new Error('登录名不能含有中文'))
                    }
                    return self.$axios.get('/sys/user/checkLoginName', {
                        params: {
                            userid: userId,
                            loginName: loginName
                        }
                    }).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            return callback(new Error('登录名已存在'))
                        }
                        return callback();
                    }).catch(function (error) {
                        return callback(new Error('请求失败'))
                    })
                }
                return callback()
            };

            return {
                loginNameRules: [
                    {required: true, message: '请输入登录名', trigger: 'blur'},
                    {min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'},
                    {validator: validateLoginName, trigger: 'blur'}
                ],
                userNameRules: [
                    {required: true, message: '请填写姓名', trigger: 'blur'},
                    {min: 2, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'},
                    {validator: validateUserName, trigger: 'blur'}
                ],
                identityRules: [
                    {validator: validateIdNumber, trigger: 'blur'}
                ],
                idNumberPassport: [
                    {min: 6, max: 24, message: '请填写6-24位字符', trigger: 'blur'}
                ],
                emailRules: [
                    {validator: validateEmail, trigger: 'blur'}
                ],
                mobileRules: [
                    {validator: validateMobile, trigger: 'blur'}
                ],
                userNoRules: [
                    {validator: validateUserNo, trigger: 'blur'}
                ],
                qqRules: [
                    {validator: validateQQ, trigger: 'blur'}
                ],
                professionRules: [
                    {validator: validateProfessional, trigger: 'change'}
                ],
                professionRulesAsTeacher: [
                    {validator: validateProfessionalAsTeacher, trigger: 'change'}
                ]
            }
        }
    };


    Vue.commonProContestRules = {
        data: function () {
            var self = this;
            var validateCheckSpSt = function (rules, value, callback) {
                return self.$axios.get('/sys/checkNotSpSteel?str=' + encodeURI(value))
            };

            var validatePName = function (rules, value, callback) {
                var proNameParams = self.proNameParams;
                return self.$axios.get('/promodel/proModel/checkProName', {params: proNameParams})
            };

            var validatePNameAndSt = function (rules, value, callback) {
                return self.$axios.all([validateCheckSpSt(rules, value, callback), validatePName(rules, value, callback)])
                    .then(self.$axios.spread(function (spStrReq, nameReq) {
                        if (spStrReq.data.data && nameReq.data) {
                            return callback();
                        }
                        if (!spStrReq.data.data) {
                            return callback(new Error(spStrReq.data.msg));
                        }
                        return callback(new Error("名称已经存在"));
                    }));
            };

            var validateCompetitionNumber = function (rules, value, callback) {
                if (value) {
                    var proNumberParams = self.proNumberParams;
                    if (!(/^[a-zA-Z0-9]+$/.test(value))) {
                        return callback(new Error('请输入英文或者数字'))
                    }
                    return self.$axios.get('/promodel/proModel/checkNumberByActYwId', {params: proNumberParams}).then(function (response) {
                        var data = response.data;
                        if (data) {
                            return callback();
                        }
                        return callback(new Error('编号已经存在'))
                    })
                }
                return callback();
            };

            return {
                competitionNumberRules: [
                    {required: true, message: '请输入编号', trigger: 'blur'},
                    {validator: validateCompetitionNumber, trigger: 'blur'}
                ],
                pNameRules: [
                    {required: true, message: '请输入名称', trigger: 'blur'},
                    {validator: validatePNameAndSt, trigger: 'blur'},
                ],
            }
        }
    };

    Vue.contestRules = {
        computed: {
            competitionRules: function () {
                var pNameRules = this.pNameRules;
                var competitionNumberRules = this.competitionNumberRules;
                return {
                    pName: pNameRules,
                    competitionNumber: competitionNumberRules,
                    bankName: [
                        {required: true, message: '请输入开户银行名称', trigger: 'blur'}
                    ],
                    bankNumber: [
                        {required: true, message: '请输入开户账号', trigger: 'blur'}
                    ],
                    gcTrack: [
                        {required: true, message: '请选择参赛赛道', trigger: 'change'}
                    ],
                    achievementTf: [
                        {required: true, message: '请选择高校科研成果转化', trigger: 'change'}
                    ],
                    belongsField: [
                        {required: true, message: '请选择所属领域', trigger: 'change'}
                    ],
                    ideaStage: [
                        {required: true, message: '请选择项目进展', trigger: 'change'}
                    ],
                    coCreation: [
                        {required: true, message: '请选择师生共创', trigger: 'change'}
                    ],
                    achievementUser: [
                        {required: true, message: '请选择创始人为科技成果的完成人或所有人', trigger: 'change'}
                    ],
                    proCategory: [
                        {required: true, message: '请选择大赛类别', trigger: 'change'}
                    ],
                    level: [
                        {required: true, message: '请选择参赛组别', trigger: 'change'}
                    ],
                    teamId: [
                        {required: true, message: '请选择团队', trigger: 'blur'}
                    ],
                    introduction: [
                        {required: true, message: '请输入项目概述', trigger: 'blur'}
                    ],
                    sysAttachmentList: [
                        {required: true, message: '请上传附件', trigger: 'change'}
                    ],
                    fileInfo: [
                        {required: true, message: '请上传附件', trigger: 'change'}
                    ]
                }
            }
        }
    }

    Vue.commonProRules = {
        computed: {
            commonProjectRules: function () {
                var pNameRules = this.pNameRules;
                var competitionNumberRules = this.competitionNumberRules;
                return {
                    pName: pNameRules,
                    competitionNumber: competitionNumberRules,
                    proCategory: [
                        {required: true, message: '请选择项目类别', trigger: 'change'}
                    ],
                    teamId: [
                        {required: true, message: '请选择团队', trigger: 'change'}
                    ],
                    introduction: [
                        {required: true, message: '请输入项目简介', trigger: 'blur'}
                    ],
                    fileInfo: [
                        {required: true, message: '请上传附件', trigger: 'blur'}
                    ],
                }
            }
        }
    }


    Vue.hsxmProRules = {
        computed: {
            proModelHsxmRules: function () {
                var pNameRules = this.pNameRules;
                var competitionNumberRules = this.competitionNumberRules;
                return {
                    resultType: [
                        {required: true, message: "请选择成果形式", trigger: 'change'}
                    ],
                    resultContent: [
                        {required: true, message: "请输入成果说明", trigger: 'blur'}
                    ],
                    budget: [
                        {required: true, message: "请输入经费预算明细", trigger: 'blur'}
                    ],
                    proModel: {
                        subTime: [
                            {required: true, message: "请选择申报日期", trigger: 'change'}
                        ],
                        pName: pNameRules,
                        competitionNumber: competitionNumberRules,
                        proCategory: [
                            {required: true, message: "请选择项目类别", trigger: 'change'}
                        ],
                        introduction: [
                            {required: true, message: "请输入项目简介", trigger: 'blur'}
                        ],
                        fileInfo: [
                            {required: true, message: '请上传附件', trigger: 'change'}
                        ],
                        teamId: [
                            {required: true, message: '请选择团队', trigger: 'change'}
                        ]
                    },
                }
            }
        }
    }


}(Vue)