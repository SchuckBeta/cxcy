<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
</head>

<body>
<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>学分认定</el-breadcrumb-item>
        <el-breadcrumb-item>申请课程学分认定</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="apply-form-container">
        <div class="apply-form-topbar">
            <span class="apply-date">申报日期：{{applyDate | formatDateFilter('YYYY-MM-DD')}}</span>
        </div>
        <div class="apply-form-titlebar-wrap">
            <div class="apply-form-titlebar">
                <span class="title">填写申请信息</span>
            </div>
        </div>
        <div class="apply-form-body">
            <div class="user-info-list">
                <el-row :gutter="15" label-width="120px">
                    <el-col :span="8">
                        <e-col-item align="right" label="姓名：">{{loginCurUser.name}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="学院：">{{loginCurUser.officeName}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="专业班级：">
                            {{loginCurUser.professional | selectedFilter(officeEntries)}}
                            <template v-if="studentExpansion.tClass">{{studentExpansion.tClass}}</template>
                            <template v-else>-</template>
                            班
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="学号：">
                            {{loginCurUser.no}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="手机号：">{{loginCurUser.mobile}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="电子邮箱：">{{loginCurUser.email}}
                        </e-col-item>
                    </el-col>
                </el-row>
            </div>
            <div class="apply-form-group" v-loading="loading">
                <div class="apply-form-item-titlebar">
                    <span class="title">课程信息<i class="el-icon-d-arrow-right"></i></span>
                </div>
                <el-row :gutter="15" label-width="120px">
                    <el-col :span="8">
                        <e-col-item align="right" label="课程名称：">
                            <el-tooltip class="item" popper-class="white" effect="dark" :content="scoCourse.name"
                                        placement="right"><span class="break-ellipsis">{{scoCourse.name}}</span>
                            </el-tooltip>
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="课程代码：">{{scoCourse.code}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="课程性质：">{{scoCourse.nature | selectedFilter(natureEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="计划课时：">{{scoCourse.planTime}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="合格成绩：">{{scoCourse.overScore}}分及以上</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="计划学分：">{{scoCourse.planScore}}
                        </e-col-item>
                    </el-col>
                </el-row>
                <div class="apply-form-item-titlebar">
                    <span class="title">已经修读并获得学分<i class="el-icon-d-arrow-right"></i></span>
                </div>
                <el-form :model="creditApplyForm" ref="creditApplyForm" :rules="creditApplyRules" :disabled="disabled"
                         size="mini" label-width="120px">
                    <el-form-item prop="realTime" label="实际学时：" class="w300">
                        <el-input v-model="creditApplyForm.realTime" maxlength="8">
                            <span slot="append">课时</span>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="realScore" label="实际成绩：" class="w300">
                        <el-input v-model="creditApplyForm.realScore" maxlength="15">
                            <span slot="append">分</span>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="sysAttachmentList" label="上传成绩单：">
                        <e-pw-upload-file v-model="creditApplyForm.sysAttachmentList"
                                          @change="handleChangeSysAttachmentList"
                                          action="/ftp/ueditorUpload/uploadPwTemp?folder=scr"></e-pw-upload-file>
                    </el-form-item>
                    <el-form-item prop="remarks" label="认定理由：">
                        <el-input type="textarea" :rows="3" maxlength="2000"
                                  v-model="creditApplyForm.remarks"></el-input>
                    </el-form-item>
                    <el-form-item class="text-center">
                        <el-button :disabled="isFileUploading" type="primary" @click.stop="validateCreditApplyForm">提交</el-button>
                        <el-button :disabled="isFileUploading" @click.stop="goToBack">返回</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        mixins: [Vue.creditDetailMixin],
        data: function () {
            var scoCourse = JSON.parse(JSON.stringify(${fns: toJson(scoCourse)}));
            var scoApply = JSON.parse(JSON.stringify(${fns: toJson(scoApply)}));
            var validateScore = this.validateScoreCouldZero;
            var self = this;
            var validateTime = function (rules, value, callback) {
                if (value) {
                    if (/^[1-9]{1}\d{0,7}$/.test(value)) {
                        if(parseFloat(value) <= parseFloat(self.scoCourse.planTime)){
                            return callback()
                        }
                        return callback(new Error('请不要大于计划课时'))
                    }
                    return callback(new Error('请输入不大于8位数的正整数'))
                }
                return callback();
            };
            return {
                studentExpansion: {},
                scoCourse: scoCourse,
                scoCourseNatures: [],
                sysDate: '',
                scoApply: scoApply,
                creditApplyForm: {
                    id: scoApply.id,
                    userId: '',
                    courseId: scoCourse.id,
                    auditStatus: '2',
                    procInsId: '',
                    realTime: '',
                    realScore: '',
                    remarks: '',
                    sysAttachmentList: []
                },
                creditApplyRules: {
                    realTime: [
                        {required: true, message: '请输入实际学时', trigger: 'blur'},
                        {validator: validateTime, trigger: 'blur'}
                    ],
                    realScore: [
                        {required: true, message: '请输入实际成绩', trigger: 'blur'},
                        {validator: validateScore, trigger: 'blur'}
                    ],
                    sysAttachmentList: [
                        {required: true, message: '请上传成绩单', trigger: 'blur'}
                    ],
                    remarks: [
                        {required: true, message: '请输入认定理由', trigger: 'blur'}
                    ]
                },
                disabled: false,
                creditId: '${creditId}',
                collegesTree: [],
                creditApplyDate: '',
                loading: false
            }
        },
        computed: {
            isFileUploading: function(){
                return this.creditApplyForm.sysAttachmentList.length > 0 && this.creditApplyForm.sysAttachmentList.some(function (item) {
                            return !item.ftpUrl;
                        })
            },
            applyDate: function () {
                return this.creditApplyDate || this.sysDate;
            },
            officeList: function () {
                return this.treeToList(this.collegesTree)
            },
            officeEntries: function () {
                return this.getEntries(this.officeList, {
                    label: 'name',
                    value: 'id'
                })
            },
            natureEntries: function () {
                return this.getEntries(this.scoCourseNatures)
            }
        },
        methods: {

            validateCreditApplyForm: function () {
                var self = this;
                this.$refs.creditApplyForm.validate(function (valid) {
                    if (valid) {
                        self.submitForm();
                    }
                })
            },

            submitForm: function () {
                var self = this;
                this.disabled = true;
                var creditApplyForm = JSON.parse(JSON.stringify(this.creditApplyForm));
                creditApplyForm.sysAttachmentList = creditApplyForm.sysAttachmentList.map(function (item) {
                    return {
                        "uid": item.response ? '' : item.uid,
                        "fileName": item.title || item.name,
                        "name": item.title || item.name,
                        "suffix": item.suffix,
                        "url": item.url,
                        "ftpUrl": item.ftpUrl,
                        "fielSize": item.fielSize,
                        "tempUrl": item.tempUrl,
                        "tempFtpUrl": item.tempFtpUrl
                    }
                });
                this.$axios.post('/scoapply/saveForm', creditApplyForm).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$msgbox({
                            type: 'success',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            confirmButtonText: '确定',
                            showClose: false,
                            message: '提交成功'
                        }).then(function () {
                            location.href = '/f/scr/toFindScoForm';
                        });
                    } else {
                        self.$message.error(data.msg)
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg);
                    self.disabled = false;
                })
            },

            goToBack: function () {
                history.go(-1)
            },
            getCollegeTree: function () {
                var self = this;
                this.$axios.get('/sys/office/getOfficeTree').then(function (response) {
                    self.collegesTree = response.data;
                })
            },

            getSysDate: function () {
                var self = this;
                this.$axios.get('/sys/sysCurDateYmdHms').then(function (response) {
                    self.sysDate = moment(response.data).format('YYYY-MM-DD');
                })
            },

            checkedUser: function () {
                var self = this;
                if (!this.creditId && this.isStudent) {
                    this.getSysDate();
                    this.getUserIsCompleted().then(function (response) {
                        var data = response.data;
                        var loginCurUser = self.loginCurUser;
                        if (data.status !== 1) {
                            self.disabled = true;
                            self.$msgbox({
                                type: 'error',
                                title: '提示',
                                closeOnClickModal: false,
                                closeOnPressEscape: false,
                                confirmButtonText: '确定',
                                showClose: false,
                                message: data.msg
                            }).then(function () {
                                location.href = '/f/sys/frontStudentExpansion/findUserInfoById?userId=' + loginCurUser.id + '&custRedict=1&isEdit=1';

                            }).catch(function () {
                            });
                        } else {
                            self.checkUserIsUpScore();
                        }
                    })
                }
            },

            getStudentExpansion: function () {
                var self = this;
                this.$axios.get('/sys/frontStudentExpansion/getStudentExpansion?userId=' + this.loginCurUser.id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.studentExpansion = data.data || {};
                    }
                })
            },

            getNatures: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=0000000108').then(function (response) {
                    self.scoCourseNatures = response.data || [];
                })
            },
            handleChangeSysAttachmentList: function (file) {
                this.$refs.creditApplyForm.validateField('sysAttachmentList');
            },
            checkUserIsUpScore: function () {
                var self = this;
                self.disabled = true;
                this.$axios.get('/scr/ajaxRapplyUserSum?user.id=' + this.loginCurUser.id).then(function (response) {
                    var data = response.data;
                    if (data.data) {
                        self.$msgbox({
                            type: 'warning',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            confirmButtonText: '确定',
                            showClose: false,
                            message: data.msg
                        }).then(function () {
                            self.disabled = false;
                        }).catch(function () {
                        });
                    }else {
                        self.disabled = false;
                    }
                })
            },
        },
        created: function () {
            if (this.isStudent) {
                this.getCollegeTree();
            }
            if (!this.creditId && this.isStudent) {
                this.checkedUser();
                this.getStudentExpansion();
            }
            this.assignFormData(this.creditApplyForm, this.scoApply);
            if (this.scoApply.attachmentList) {
                var attachmentList = this.scoApply.attachmentList || [];
                attachmentList.map(function (item) {
                    return {
                        "uid": item.response ? '' : item.uid,
                        "fileName": item.title || item.name,
                        "name": item.title || item.name,
                        "suffix": item.suffix,
                        "url": item.url,
                        "ftpUrl": item.ftpUrl,
                        "fielSize": item.fielSize,
                        "tempUrl": item.tempUrl,
                        "tempFtpUrl": item.tempFtpUrl
                    }
                });
                this.creditApplyForm.sysAttachmentList = attachmentList;
            }
            this.getNatures();
            this.creditApplyForm.userId = this.loginCurUser.id;
        }
    })

</script>
</body>
</html>
