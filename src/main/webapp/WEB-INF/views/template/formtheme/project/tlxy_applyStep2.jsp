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
            <el-form :model="proModelTlxyForm" ref="proModelTlxyForm" :rules="proModelTlxyRules" :disabled="disabled"
                     size="mini" label-width="120px">
                <el-form-item label="选择团队：" prop="proModel.teamId">

                    <el-select v-model="proModelTlxyForm.proModel.teamId" @change="changeTeam">
                        <el-option v-for="item in teamList" :key="item.id" :value="item.id"
                                   :label="item.name"></el-option>
                    </el-select>
                    <template v-if="teamList.length === 0" style="display: none"><span
                            class="el-form-item-expository">暂无团队，请去<a
                            href="${ctxFront}/team/indexMyTeamList">创建团队</a>吧</span></template>

                </el-form-item>
                <update-member :declare-id="loginCurUser.id" :disabled="disabled" :first-teacher-id-view="firstTeacherIdView"
                               :team-id="proModelTlxyForm.proModel.teamId"
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
            var proModelTlxy = JSON.parse(JSON.stringify(${fns: toJson(proModelTlxy)}));
            var teamList = JSON.parse(JSON.stringify(${fns: toJson(teams)}));
            return {
                proModelTlxy: proModelTlxy,
                proModelTlxyForm: {
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
                proModelTlxyRules: {
                    proModel: {
                        teamId: [
                            {required: true, message: '请选择团队', trigger: 'change'}
                        ]
                    }
                },
                disabled: false,
                isChangeProjectVal: false,
                firstTeacherIdView:''
            }
        },
        computed: {
            applyDate: function () {
                return moment(this.proModelTlxy.proModel.subTime).format('YYYY-MM-DD')
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
                return this.proModelTlxy.proModel.subStatus === '1';
            }
        },

        methods: {
            getFirstTeacher:function () {
                var self = this;
                this.teamList.forEach(function (item) {
                    if(self.proModelTlxyForm.proModel.teamId == item.id){
                        self.firstTeacherIdView = item.firstTeacher || '';
                    }
                });
                if(!this.firstTeacherIdView){
                    this.$message({
                        type:'warning',
                        message:'请去团队建设中设置第一导师'
                    })
                }
            },
            changeTeam:function () {
                this.getFirstTeacher();
                this.isChangeProjectVal = true;
            },
            handleChangeTeam: function (data) {
                this.studentList = data.studentList;
                this.teacherList = data.teacherList;
            },

            validateProModelForm: function (type) {
                var self = this;
                var proModelTlxy = this.proModelTlxy;
                var proModel = proModelTlxy.proModel;
                if(type === 'prev'){
                    self.saveProModel(type);
                    return;
                }
                this.$refs.proModelTlxyForm.validate(function (valid) {
                    if (valid) {
                        if (type === 'prev' || type === 'save') {
                            self.saveProModel(type);
                        } else {
                            if (!!proModelTlxy.proModel.teamId && !self.isChangeProjectVal) {
                                location.href = '/f/proModelTlxy/applyStep3?id=' + proModelTlxy.id + '&actYwId=' + proModel.actYwId;
                            } else {
                                self.nextProModel();
                            }
                        }
                    }
                })
            },

            getParams: function () {
                var proModelTlxyForm = this.proModelTlxyForm;
                var proModel = proModelTlxyForm.proModel;
                var proModelParams = {};
                for (var p in proModel) {
                    if (proModel.hasOwnProperty(p)) {
                        proModelParams['proModel.' + p] = proModel[p];
                    }
                }
                proModelParams.id = proModelTlxyForm.id;
                return proModelParams;
            },

            saveProModel: function (type) {
                var self = this;
                var proModelTlxy = this.proModelTlxy;
                var proModel = proModelTlxy.proModel;
                var params = this.getParams();
                var url = '/proModelTlxy/ajaxUncheckSave2';
                if (type === 'prev') {
                    if (!!proModelTlxy.proModel.teamId && !this.isChangeProjectVal) {
                        location.href = '/f/proModelTlxy/applyStep1?id=' + proModelTlxy.id + '&actYwId=' + proModel.actYwId;
                        return false;
                    }
                }
                if(!this.firstTeacherIdView && type === 'save'){
                    this.$message({
                        type:'warning',
                        message:'请去团队建设中设置第一导师'
                    });
                    return false;
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
                                location.href = '/f/proModelTlxy/applyStep3?id=' + proModelTlxy.id + '&actYwId=' + proModel.actYwId;
                            }).catch(function () {

                            });
                        } else {
                            location.href = '/f/proModelTlxy/applyStep1?id=' + proModelTlxy.id + '&actYwId=' + proModel.actYwId;
                        }
                        proModelTlxy.proModel.teamId = self.proModelTlxyForm.proModel.teamId;
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
                var proModelTlxy = this.proModelTlxy;
                var proModel = proModelTlxy.proModel;
                var params = this.getParams();
                var url = '/proModelTlxy/ajaxSave2';
                if(!this.firstTeacherIdView){
                    this.$message({
                        type:'warning',
                        message:'请去团队建设中设置第一导师'
                    });
                    return false;
                }
                if (!!proModelTlxy.proModel.teamId && !this.isChangeProjectVal) {
                    location.href = '/f/proModelTlxy/applyStep3?id=' + proModelTlxy.id + '&actYwId=' + proModel.actYwId;
                    return false;
                }
                Object.assign(params, this.studentParamObj, this.teacherParamObj);
                this.disabled = true;
                this.$axios.get(url+'?'+ Object.toURLSearchParams(params)).then(function (response) {
                    var data = response.data;
                    if (data.ret === 1) {
                        proModelTlxy.proModel.teamId = self.proModelTlxyForm.proModel.teamId;
                        self.isChangeProjectVal = false;
                        location.href = '/f/proModelTlxy/applyStep3?id=' + proModelTlxy.id + '&actYwId=' + proModel.actYwId;
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
            }
        },
        created: function () {
            this.assignFormData(this.proModelTlxyForm, this.proModelTlxy);
            this.isChangeProjectVal = false;
            if(this.proModelTlxyForm.proModel.teamId){
                this.isChangeProjectVal = true;
                this.getFirstTeacher();
            }
        }
    })


</script>
</body>
</html>