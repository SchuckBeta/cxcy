/**
 * Created by Administrator on 2018/6/14.
 */

;+function (Vue) {
    'use strict';
    Vue.userBaseFormMixin = {
        data: function () {
            var self = this;
            var validateIdentity = this.validateIdentity;
            var validateEmail = this.validateEmail;
            var validateMobile = this.validateMobile;
            var validateQQ = this.validateQQ;
            var validateLoginName = this.validateLoginName;
            var validateUserNo = this.validateUserNo;
            var validateUserName = this.validateUserName;
            var validateOfficeId = function (rules, value, callback) {
                if(value && value.length > 0){
                    var professionalId = value[value.length - 1];
                    var professional = self.collegeEntries[professionalId];
                    if(professional && parseInt(professional.grade) >= 3){
                        return callback();
                    }
                    return callback(new Error('请选择专业'))
                }
                return callback();
            }
            return {
                userBaseForm: {
                    id: '',
                    userLoginName: '', //登录名
                    userName: '', //用户名
                    userSex: '1', //用户性别
                    userIdType: '1', // 用户类型
                    userIdNumber: '', //用户号码
                    userBirthday: '', //用户生日
                    userEmail: '', //用户邮箱
                    userMobile: '',//用户手机号
                    address: '', //地址
                    userQq: '', //用户qq
                    userResidence: '', //户籍
                    userIntroduction: '', //简介
                    userCountry: '', //用户国家
                    userNational: '', //用户
                    userPolitical: '',
                    userId: '',

                    // id: '',
                    tClass: '', //班级
                    enterdate: '', //入学年份
                    cycle: '', //学制
                    userNo: '', //学号
                    graduation: '', //毕业时间
                    currState: '', //现状
                    instudy: '', //在读学位
                    userEducation: '',//学历
                    userDegree: '', //学位
                    temporaryDate: '', //休学时间
                    userDomainIdList: [], //技术领域,
                    userProfessional: []
                },
                userBaseFormRule: {
                    userResidence: [
                        {required: true, message: '请填写户籍', trigger: 'blur'},
                        { max: 64, message: '请输入大不于64个字', trigger: 'blur'},
                    ],
                    userIntroduction: [
                        {required: true, message: '请填写个人简介', trigger: 'blur'},
                        {max: 200, message: '请输入不大于200个字', trigger: 'blur'},
                    ],
                    userLoginName: [
                        {required: true, message: '请填写登录名', trigger: 'blur'},
                        {min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'},
                        {validator: validateLoginName, trigger: 'blur'}
                    ],
                    userName: [
                        {required: true, message: '请填写姓名', trigger: 'blur'},
                        {min: 2, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'},
                        {validator: validateUserName, trigger: 'blur'}
                    ],
                    userSex: [
                        {required: true, message: '请选择性别', trigger: 'change'}
                    ],
                    userIdType: [
                        {required: true, message: '请选择证件类别', trigger: 'change'}
                    ],
                    idNumberIdentity: [
                        {required: true, message: '请填写身份证号码', trigger: 'blur'},
                        {validator: validateIdentity, trigger: 'blur'}
                    ],
                    idNumberPassport: [
                        {required: true, message: '请填写护照号码', trigger: 'blur'},
                        {min: 6, max: 24, message: '请填写6-24位字符', trigger: 'blur'}
                    ],
                    userBirthday: [
                        {required: false, message: '请填写出生时间', trigger: 'change'}
                    ],
                    userEmail: [
                        {required: true, message: '请填写邮箱', trigger: 'blur'},
                        {validator: validateEmail, trigger: 'blur'}
                    ],
                    userMobile: [
                        {required: true, message: '请添加手机号', trigger: 'blur'},
                        // {validator: validateMobile, trigger: 'blur'}
                    ],
                    userQq: [
                        {required: false, message: '请填入QQ号码', trigger: 'blur'},
                        {validator: validateQQ, trigger: 'blur'}
                    ],
                    address: [{
                        min: 3, max: 128, message: '请填写3-128位的字符', trigger: 'blur'
                    }],
                    userCountry: [{
                        required: false, message: '请选择国家/户籍', trigger: 'change'
                    }],
                    userNational: [{
                        required: false, message: '请选择国家/户籍', trigger: 'change'
                    }],
                    userPolitical: [{
                        required: false, message: '请选择国家/户籍', trigger: 'change'
                    }],
                    enterdate: [
                        {required: true, message: '请选择入学年份', trigger: 'change'}
                    ],
                    tClass: [
                        {max: 64, message: '请输入小于64位字符的班级名', trigger: 'blur'}
                    ],
                    userNo: [
                        {required: true, message: '请填入学号', trigger: 'blur'},
                        {min: 2, max: 24, message: '请输入2-24位字符的学号', trigger: 'blur'},
                        {validator: validateUserNo, trigger: 'blur'}
                    ],
                    cycle: [
                        {required: true, message: '请选择学制年', trigger: 'change'}
                    ],
                    userProfessional: [
                        {required: true, message: '请选择院系/专业', trigger: 'change'},
                        {validator: validateOfficeId, trigger: 'change'}
                    ],
                    userEducation: [
                        {required: true, message: '请选择学历', trigger: 'change'}
                    ],
                    userDegree: [
                        {required: true, message: '请选择学位', trigger: 'change'}
                    ],
                    instudy: [
                        {required: true, message: '请选择在读学位', trigger: 'change'}
                    ],
                    graduation: [
                        {required: true, message: '请选择毕业时间', trigger: 'change'}
                    ],
                    userDomainIdList: [
                        {required: true, message: '请选择技术领域', trigger: 'change'}
                    ],
                    temporaryDate: [
                        {required: true, message: '请选择休学时间', trigger: 'change'}
                    ],
                    currState: [
                        {required: true, message: '请选择现状', trigger: 'change'}
                    ]
                },

                userEducationForm: {
                    id: '',
                    tClass: '', //班级
                    enterdate: '', //入学年份
                    cycle: '', //学制
                    userNo: '', //学号
                    graduation: '', //毕业时间
                    currState: '', //现状
                    instudy: '', //在读学位
                    userEducation: '',//学历
                    userDegree: '', //学位
                    temporaryDate: '', //休学时间
                    userDomainIdList: [], //技术领域,
                    userProfessional: []
                },
                userEducationFormRule: {
                    // enterdate: [
                    //     {required: true, message: '请选择入学年份', trigger: 'change'}
                    // ],
                    // tClass: [
                    //     {max: 64, message: '请输入小于64位字符的班级名', trigger: 'blur'}
                    // ],
                    // userNo: [
                    //     {required: true, message: '请填入学号', trigger: 'blur'},
                    //     {min: 2, max: 24, message: '请输入2-14位字符的学号', trigger: 'blur'}
                    // ],
                    // cycle: [
                    //     {required: true, message: '请选择学制年', trigger: 'change'}
                    // ],
                    // userProfessional: [
                    //     {required: true, message: '请选择学制年', trigger: 'change'}
                    // ],
                    // userEducation: [
                    //     {required: true, message: '请选择学历', trigger: 'change'}
                    // ],
                    // userDegree: [
                    //     {required: true, message: '请选择学位', trigger: 'change'}
                    // ],
                    // instudy: [
                    //     {required: true, message: '请选择在读学位', trigger: 'change'}
                    // ],
                    // graduation: [
                    //     {required: true, message: '请选择毕业时间', trigger: 'change'}
                    // ],
                    // userDomainIdList: [
                    //     {required: true, message: '请选择技术领域', trigger: 'change'}
                    // ],
                    // temporaryDate: [
                    //     {required: true, message: '请选择休学时间', trigger: 'change'}
                    // ],
                    // currState: [
                    //     {required: true, message: '请选择现状', trigger: 'change'}
                    // ]
                }
            }
        }
    }
}(Vue)