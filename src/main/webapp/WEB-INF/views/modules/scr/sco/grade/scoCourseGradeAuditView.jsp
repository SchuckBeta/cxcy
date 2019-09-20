<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar second-name="审核"></edit-bar>
    </div>
    <div class="panel panel-pw-enter mgb-20">
        <div class="panel-body">
            <applicant-info-credit :user="user" :student="studentExpansion"></applicant-info-credit>
            <div class="pro-category-placeholder"></div>
        </div>
    </div>
    <div class="pro-contest-content panel-padding-space mgb-20">
        <div v-if="!isAudited" class="btn-audit-box">
            <el-button type="primary" size="mini" @click.stop="openAuditDialog">审核</el-button>
        </div>
        <el-tabs class="pro-contest-tabs" v-model="tabActiveName">
            <el-tab-pane label="申请认定学分" name="firstCreditTab">
                <tab-pane-content>
                    <credit-course-apply :apply="creditApply"></credit-course-apply>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="课程信息" name="secondCreditTab">
                <tab-pane-content>
                    <credit-course-info :course="scoCourse"></credit-course-info>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="审核记录" name="threeCreditTab">
                <tab-pane-content>
                    <credit-course-audit :id="creditApply.id"></credit-course-audit>
                </tab-pane-content>
            </el-tab-pane>
        </el-tabs>
    </div>
    <div class="text-center">
        <el-button size="mini" @click.stop="goToBack">返回</el-button>
    </div>
    <el-dialog
            title="审核"
            :visible.sync="dialogVisibleAudit"
            width="520px"
            :close-on-click-modal="false"
            :before-close="handleCloseAudit">
        <el-form :model="creditAuditForm" ref="creditAuditForm" :rules="creditAuditRules" :disabled="disabled"
                 size="mini" label-width="130px">
            <el-form-item label="准予认定学分" prop="scoreVal">
                <el-input v-model="creditAuditForm.scoreVal" maxlength="15" style="width: 193px;">
                    <span slot="append">分</span>
                </el-input>
            </el-form-item>
            <el-form-item label="审核" prop="auditStatus">
                <el-select v-model="creditAuditForm.auditStatus">
                    <el-option v-for="item in auditStatues" :label="item.remark" :value="item.key"
                               :key="item.key"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="建议及意见：" prop="suggest" :rules="creditAuditForm.auditStatus == '0' ? remarksRules : []">
                <el-input type="textarea" maxlength="300" placeholder="最多300个字符" :rows="3"
                          v-model="creditAuditForm.suggest"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click.stop="validateAuditForm" :disabled="disabled"
               size="mini">提 交</el-button>
  </span>
    </el-dialog>

</div>


<script>
    'use strict';


    new Vue({
        el: '#app',
        mixins: [Vue.creditDetailMixin],
        data: function () {
            var studentExpansion = JSON.parse(JSON.stringify(${fns: toJson(studentExpansion)}));
            var officeList = JSON.parse(JSON.stringify(${fns: toJson(fns: getOfficeList())}));
            var scoCourse = JSON.parse(JSON.stringify(${fns: toJson(scoCourse)}));
            var scoApply = JSON.parse(JSON.stringify(${fns: toJson(scoApply)}));
            var applyUser = JSON.parse(JSON.stringify(${fns: toJson(applyUser)})) || {};

            var validateScoreCouldZero = this.validateScoreCouldZero;
            return {
                studentExpansion: studentExpansion,
                creditApply: scoApply,
                officeList: officeList,
                scoCourse: scoCourse,
                applyUser: applyUser,
                disabled: false,
                dialogVisibleAudit: false,
                tabActiveName: 'firstCreditTab',
                auditStatues: [],
                autoScore: '${autoScore}',
                creditAuditForm: {
                    applyId: scoApply.id,
                    auditStatus: '',
                    scoreVal: '${autoScore}',
                    suggest: ''
                },
                isAudited: false,
                creditAuditRules: {
                    scoreVal: [
                        {required: true, message: '请输入认定学分分值', trigger: 'blur'},
                        {validator: validateScoreCouldZero, trigger: 'blur'}
                    ],
                    auditStatus: [
                        {required: true, message: '请审核', trigger: 'blur'},
                    ]
                },
                remarksRules: [
                    {required: true, message: '请填写不通过理由', trigger: 'blur'}
                ],
            }
        },
        computed: {
            user: function () {
                var loginCurUser = this.applyUser;
                if (this.officeEntity) {
                    loginCurUser.professional = this.officeEntity[loginCurUser.professional]
                }
                return loginCurUser;
            },
            officeEntity: function () {
                return this.getEntries(this.officeList, {
                    label: 'name',
                    value: 'id'
                })
            },
            isReadonly: function () {
                return !!this.autoScore;
            }
        },
        methods: {
            goToBack: function () {
                history.go(-1);
            },
            handleCloseAudit: function () {
                this.dialogVisibleAudit = false;
            },
            openAuditDialog: function () {
                this.dialogVisibleAudit = true;
            },
            getAuditStatues: function () {
                var self = this;
                this.$axios.get('/sys/passNots').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.auditStatues = JSON.parse(data.data) || [];
                    }
                })
            },

            validateAuditForm: function () {
                var self = this;
                this.$refs.creditAuditForm.validate(function (valid) {
                    if (valid) {
                        self.submitAuditForm();
                    }
                })
            },

            submitAuditForm: function () {
                var self = this;
                var creditAuditForm = JSON.parse(JSON.stringify(this.creditAuditForm));
                creditAuditForm.auditStatus = creditAuditForm.auditStatus === '0' ? '4' : '3';
                this.disabled = true;
                this.$axios.post('/scoapply/saveAuditCourse', creditAuditForm).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        window.parent.sideNavModule.changeStaticUnreadTag("/a/scoapply/getCountToAudit");
                        self.$alert('保存成功', '提示', {
                            type: 'success',
                            cancelButtonText: '取消',
                            confirmButtonText: '确定',
                            showCancelButton: false,
                            showClose: false
                        }).then(function () {
                            location.href = '/a/sco/scoreGrade/courseList'
                        }).catch(function () {
                        });
                        self.handleCloseAudit();
                        self.isAudited = true;
                    } else {
                        self.$message.error(data.msg);
                        self.disabled = false;
                    }
                }).catch(function () {
                    self.disabled = false;
                    self.$message.error(data.msg)
                })
            }

        },
        created: function () {
            this.getAuditStatues();
            this.isAudited = ['3', '4'].indexOf(this.creditApply.auditStatus) > -1;
        }
    })
</script>

</body>
</html>