<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${fns:getConfig('frontTitle')}</title>
    <meta name="decorator" content="creative"/>

</head>
<body>
<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>双创项目</el-breadcrumb-item>
        <el-breadcrumb-item>${proProject.projectName}</el-breadcrumb-item>
    </el-breadcrumb>
    <row-step-apply>
        <step-item is-complete>第一步（填写项目基本信息）</step-item>
        <step-item is-complete>第二步（填写团队基本信息）</step-item>
        <step-item>第三步（提交项目申报附件）</step-item>
    </row-step-apply>
    <div class="apply-form-container">
        <div class="apply-form-topbar">
            <span class="apply-date">申报日期：{{applyDate}}</span>
        </div>
        <div class="apply-form-titlebar-wrap">
            <div class="apply-form-titlebar">
                <span class="title">项目基本信息</span>
            </div>
        </div>
        <div class="apply-form-body">
            <%--<div class="user-info-list">--%>
                <%--<el-row :gutter="15" label-width="120px">--%>
                    <%--<el-col :span="8">--%>
                        <%--<e-col-item align="right" label="姓名：">{{loginCurUser.name}}</e-col-item>--%>
                    <%--</el-col>--%>
                    <%--<el-col :span="8">--%>
                        <%--<e-col-item align="right" label="学院：">{{loginCurUser.officeName}}</e-col-item>--%>
                    <%--</el-col>--%>
                    <%--<el-col :span="8">--%>
                        <%--<e-col-item align="right" label="学号：">--%>
                            <%--{{loginCurUser.no}}--%>
                        <%--</e-col-item>--%>
                    <%--</el-col>--%>
                <%--</el-row>--%>
            <%--</div>--%>
            <el-form :model="proModelHsxmForm" ref="proModelHsxmForm" :rules="proModelHsxmRules" :disabled="disabled"
                     size="mini" label-width="120px">
                <el-form-item label="选择团队：" prop="proModel.teamId">

                    <el-select v-model="proModelHsxmForm.proModel.teamId" @change="isChangeProjectVal = true">
                        <el-option v-for="item in teamList" :key="item.id" :value="item.id"
                                   :label="item.name"></el-option>
                    </el-select>
                    <template v-if="teamList.length === 0" style="display: none"><span
                            class="el-form-item-expository">暂无团队，请去<a
                            href="${ctxFront}/team/indexMyTeamList">创建团队</a>吧</span></template>

                </el-form-item>
                <update-member :declare-id="loginCurUser.id" :disabled="disabled"
                               :team-id="proModelHsxmForm.proModel.teamId"
                               :operate-btn-show="false" :addable="false"
                               @change="handleChangeTeam"></update-member>
                <el-form-item v-show="!isSubmited" label-width="0" class="text-center">
                    <el-button @click.stop="validateProModelForm('prev')">上一步</el-button>
                    <el-button type="primary" @click.stop="validateProModelForm">下一步</el-button>
                    <el-button type="primary" @click.stop="validateProModelForm('save')">保存</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</div>

<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var proModelHsxm = JSON.parse(JSON.stringify(${fns: toJson(proModelHsxm)}));
            var teamList = JSON.parse(JSON.stringify(${fns: toJson(teams)}));
            return {
                proModelHsxm: proModelHsxm,
                proModelHsxmForm: {
                    id: '',
                    proModel: {
                        id: '',
                        actYwId: '',
                        declareId: '',
//                        subTime: '',
                        year: '',
                        teamId: ''
                    }
                },
                teamList: teamList,
                studentList: [],
                teacherList: [],
                proModelHsxmRules: {
                    proModel: {
                        teamId: [
                            {required: true, message: '请选择团队', trigger: 'change'}
                        ]
                    }
                },
                disabled: false,
                isChangeProjectVal: false
            }
        },
        computed: {
            applyDate: function () {
                return moment(this.proModelHsxm.proModel.subTime).format('YYYY-MM-DD')
            },
            studentParamObj: function () {
                var studentList = this.studentList;
                var entry = {};
                studentList.forEach(function (item, index) {
                    entry['proModel.studentList[' + index + '].userId'] = item['userId']
                });
                return entry;
            },
            teacherParamObj: function () {
                var teacherList = this.teacherList;
                var entry = {};
                teacherList.forEach(function (item, index) {
                    entry['proModel.teacherList[' + index + '].userId'] = item['userId']
                });
                return entry;
            },
            isSubmited: function () {
                return this.proModelHsxm.proModel.subStatus === '1';
            }
        },

        methods: {
            handleChangeTeam: function (data) {
                this.studentList = data.studentList;
                this.teacherList = data.teacherList;
            },

            validateProModelForm: function (type) {
                var self = this;
                var proModelHsxm = this.proModelHsxm;
                var proModel = proModelHsxm.proModel;
                this.$refs.proModelHsxmForm.validate(function (valid) {
                    if (valid) {
                        if (type === 'prev' || type === 'save') {
                            self.saveProModel(type);
                        } else {
                            if (!!proModelHsxm.proModel.teamId && !self.isChangeProjectVal) {
                                location.href = '/f/proModelHsxm/applyStep3?id=' + proModelHsxm.id + '&actYwId=' + proModel.actYwId;
                            } else {
                                self.nextProModel();
                            }
                        }
                    }
                })
            },

            getParams: function () {
                var proModelHsxmForm = this.proModelHsxmForm;
                var proModel = proModelHsxmForm.proModel;
                var proModelParams = {};
                for (var p in proModel) {
                    if (proModel.hasOwnProperty(p)) {
                        proModelParams['proModel.' + p] = proModel[p];
                    }
                }
                proModelParams.id = proModelHsxmForm.id;
                return proModelParams;
            },

            saveProModel: function (type) {
                var self = this;
                var proModelHsxm = this.proModelHsxm;
                var proModel = proModelHsxm.proModel;
                var params = this.getParams();
                var url = '/proModelHsxm/ajaxUncheckSave2';

                if (type === 'prev') {
                    if (!!proModelHsxm.proModel.teamId && !this.isChangeProjectVal) {
                        location.href = '/f/proModelHsxm/applyStep1?id=' + proModelHsxm.id + '&actYwId=' + proModel.actYwId;
                        return false;
                    }
                }
                Object.assign(params, this.studentParamObj, this.teacherParamObj);
                this.disabled = true;
                this.$axios.get(url+'?'+ Object.toURLSearchParams(params)).then(function (response) {
                    var data = response.data;
                    if (data.ret === 1) {
                        if (type === 'save') {
                            self.$alert(data.msg, '提示', {
                                type: 'success',
                                cancelButtonText: '取消',
                                confirmButtonText: '下一步',
                                showCancelButton: true
                            }).then(function () {
                                location.href = '/f/proModelHsxm/applyStep3?id=' + proModelHsxm.id + '&actYwId=' + proModel.actYwId;
                            }).catch(function () {

                            });
                        } else {
                            location.href = '/f/proModelHsxm/applyStep1?id=' + proModelHsxm.id + '&actYwId=' + proModel.actYwId;
                        }
                        proModelHsxm.proModel.teamId = self.proModelHsxmForm.proModel.teamId;
                        self.isChangeProjectVal = false;
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error',
                            cancelButtonText: '确定'
                        }).then(function () {
                        })
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.disabled = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            nextProModel: function () {
                var self = this;
                var proModelHsxm = this.proModelHsxm;
                var proModel = proModelHsxm.proModel;
                var params = this.getParams();
                var url = '/proModelHsxm/ajaxSave2';
                if (!!proModelHsxm.proModel.teamId && !this.isChangeProjectVal) {
                    location.href = '/f/proModelHsxm/applyStep3?id=' + proModelHsxm.id + '&actYwId=' + proModel.actYwId;
                    return false;
                }
                Object.assign(params, this.studentParamObj, this.teacherParamObj);
                this.disabled = true;
                this.$axios.get(url+'?'+ Object.toURLSearchParams(params)).then(function (response) {
                    var data = response.data;
                    if (data.ret === 1) {
                        proModelHsxm.proModel.teamId = self.proModelHsxmForm.proModel.teamId;
                        self.isChangeProjectVal = false;
                        location.href = '/f/proModelHsxm/applyStep3?id=' + proModelHsxm.id + '&actYwId=' + proModel.actYwId;
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error',
                            cancelButtonText: '确定'
                        }).then(function () {
                        })
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.disabled = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            },
        },
        created: function () {
            this.assignFormData(this.proModelHsxmForm, this.proModelHsxm)
            this.isChangeProjectVal = false;
        }
    })


</script>
</body>
</html>