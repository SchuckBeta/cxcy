<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>

<body>
<div id="app" v-show="pageLoad" style="display: none">
    <edit-bar class="edit-bar-province" second-name="详情"></edit-bar>
    <div class="content-container">
        <div class="user_avatar-sidebar">
            <div class="user-avatar">
                <div class="avatar-pic">
                    <img :src="userPhoto | ftpHttpFilter(ftpHttp) | studentPicFilter">
                </div>
            </div>
        </div>
        <div class="user_detail-container">
            <div class="user_detail-title">
                <i class="iconfont icon-user"></i><span class="text">导师基本信息</span>
            </div>
            <div class="user_detail-wrap">
                <div class="user_detail-inner">
                    <el-row :gutter="10" label-width="90px">
                        <el-col :span="8">
                            <e-col-item label="姓名：" align="right">{{backTeacherExpansion.user.name}}</e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="性别：" align="right">{{backTeacherExpansion.user.sex == '1' ? '男' : '女'}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="证件号：" align="right">
                                {{backTeacherExpansion.user.idType | selectedFilter(idTypesEntries)}}
                                {{backTeacherExpansion.user.idNumber}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="手机号：" align="right">{{backTeacherExpansion.user.mobile}}</e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="邮箱：" align="right">{{backTeacherExpansion.user.email}}</e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="从业年限：" align="right">{{backTeacherExpansion.firstBank}}年</e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="工作单位：" align="right">{{backTeacherExpansion.workUnit}}</e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="职务：" align="right">{{backTeacherExpansion.postTitle}}</e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="行业：" align="right">{{backTeacherExpansion.industry}}</e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="推荐机构：" align="right">{{backTeacherExpansion.recommendedUnits}}</e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="擅长领域：" align="right">{{backTeacherExpansion.user.domainIdList |
                                checkboxFilter(technologyFieldEntries)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="服务意向：" align="right">{{backTeacherExpansion.serviceIntentionIds | checkboxFilter(masterServicesEntries)}}</e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="个人简介：" align="right"><span class="white-space-pre" v-html="backTeacherExpansion.mainExp"></span></e-col-item>
                        </el-col>

                    </el-row>

                </div>

                <%--<project-experience :project-list="projectList" :user-id="teacherForm.userid" title-label="指导项目"></project-experience>--%>

                <%--<contest-experience :contest-list="contestList" :user-id="teacherForm.userid" title-label="指导大赛"></contest-experience>--%>

                <%--<review-task :user-id="teacherForm.userid"></review-task>--%>


            </div>
        </div>
    </div>


</div>

<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {


            return {
                backTeacherExpansion:{
                    user: {}
                },

                loading: true,
                userPhoto: '',
                userAvatar: '',
                userPicFile: null,
                isUpdating: false,
                userAsTeacherId: '${userAsTeacherId}',
                recommendedUnitsDict: [],
                industries: [],
                postTitles: [],
                sexes: [],
                masterServices: [],
                idTypes: [],
                educationTypes: [],
                enducationLevels: [],
                professionalTypes: [],
                degreeTypes: [],
                workTypes: [],
                yesNo: [],
                technologyFields: [],
                teacherCategories: [],
                officeTree: [],
                officeEntity: {},
                politicsStatuses: ['中共党员', '共青团员', '民主党派人士', '无党派民主人士', '普通公民'],
                projectList: [],
                contestList: [],

            }
        },
        computed: {
            masterTypesEntries:function () {
                return this.getEntries(this.masterTypes);
            },
            sexesEntries: function () {
                return this.getEntries(this.sexes);
            },
            masterServicesEntries:function () {
                return this.getEntries(this.masterServices);
            },
            idTypesEntries: function () {
                return this.getEntries(this.idTypes)
            },
            educationTypeEntries: function () {
                return this.getEntries(this.educationTypes)
            },
            enducationLevelEntries: function () {
                return this.getEntries(this.enducationLevels)
            },
            professionalTypeEntries: function () {
                return this.getEntries(this.professionalTypes)
            },
            technologyFieldEntries: function () {
                return this.getEntries(this.technologyFields)
            },
            degreeTypeEntries: function () {
                return this.getEntries(this.degreeTypes)
            },
            workTypesEntries:function () {
                return this.getEntries(this.workTypes);
            },
            yesNoEntries:function () {
                return this.getEntries(this.yesNo);
            },
            expertJobNamesEntries:function () {
                return this.getEntries(this.expertJobNames);
            },
        },
        methods: {
            getStuExDataByUserId: function () {
                var self = this;
                if (!this.userAsTeacherId) {
                    this.loading = false;
                    return;
                }
                this.loading = true;
                this.$axios.get('/sys/backTeacherExpansion/getStuExDataByUserId/' + this.userAsTeacherId).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.backTeacherExpansion = data.data || {};
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                })
            },
            getDictLists: function () {
                var dicts = {
                    'master_help': 'masterServices',
                    'id_type': 'idTypes',
                    'technology_field': 'technologyFields',
                };
                this.getBatchDictList(dicts)
            },
        },
        mounted: function () {
            this.getStuExDataByUserId();
            this.getDictLists();
        }
    })

</script>

</body>
</html>