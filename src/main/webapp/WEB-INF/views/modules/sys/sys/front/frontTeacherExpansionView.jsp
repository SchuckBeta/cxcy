<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>

<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>

</head>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="container page-container pdt-60">
    <el-breadcrumb class="mgb-20" separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="/f"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/sys/frontTeacherExpansion">{{role}}师资源</a></el-breadcrumb-item>
        <el-breadcrumb-item>{{role}}信息</el-breadcrumb-item>
    </el-breadcrumb>
        <div class="user-detail-view">
            <el-row :gutter="100" style="margin-left: 15px;margin-right:15px;">
                <div class="user-header-box">
                    <img :src="user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter">
                    <div class="box-name"><span>{{user.name}}</span></div>
                    <div class="box-icon">
                        <span><i class="iconfont icon-yanjing1"></i>{{user.views}}</span>
                        <span v-if="user.id != loginCurUser.id && loginCurUser.id"><i class="iconfont icon-dianzan1" :class="{'give-like': isLiked}" @click.stop.prevent="doLike"></i>{{user.likes}}</span>
                    </div>
                    <div class="box-key">
                        <el-tag type="info" size="mini" v-for="(value,index) in backTeacherExpansion.keywords" key="index">{{value}}</el-tag>
                    </div>
                </div>
                <el-col :span="18">
                    <p class="user-exp">工作经历</p>
                    <div class="white-space-pre-static word-break"><span v-html="backTeacherExpansion.mainExp"></span></div>
                </el-col>
                <el-col :span="6">
                    <div class="user-info-right">出生年月：{{user.birthday | formatDateFilter('YYYY-MM')}}</div>
                    <div>性别：{{user.sex | selectedFilter(sexEntries)}}</div>
                    <div>学历：{{user.education | selectedFilter(educationLevelEntries)}}</div>
                    <div>职称：{{backTeacherExpansion.technicalTitle | selectedFilter(expertJobNamesEntries, true)}}</div>
                    <div>学位：{{user.degree | selectedFilter(degreeTypeEntries)}}</div>
                </el-col>
            </el-row>

            <div class="user_detail-container">
                <div class="user_detail-inner">
                    <div class="user_detail-title-handler">
                        <div class="ud-row-title no-border-title"><span class="name">项目指导经历</span></div>
                    </div>
                    <el-row :gutter="10">
                        <el-col :span="12" v-for="project in projectList" :key="project.id">
                            <div class="experience-card" style="margin-bottom: 10px;">
                                <div class="experience-card-header">
                                    <h4 class="experience-card-title">{{project.name}}</h4>
                                </div>
                                <div class="experience-card-body">
                                    <div class="exp-pic">
                                        <a href="javascript: void(0);"><img
                                                :src="project.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proGConPicFilter"></a>
                                    </div>
                                    <div class="exp-info">
                                        <e-col-item label="担任角色：" label-width="72px">{{ project |
                                            userRoleName(user.id) }}
                                        </e-col-item>
                                        <e-col-item label="项目级别：" label-width="72px">{{project.level}}</e-col-item>
                                        <e-col-item label="项目结果：" label-width="72px">{{project.result}}</e-col-item>
                                        <e-col-item label="项目周期：" label-width="72px">{{project| userProContestDRange}}
                                        </e-col-item>
                                    </div>
                                    <div class="exp-intro">
                                        <e-col-item label="项目简介：" label-width="72px" class="white-space-pre-static">
                                            {{project.introduction}}
                                        </e-col-item>
                                    </div>
                                </div>
                                <div class="experience-card-footer">
                                    <div class="text-right">
                                        <el-tag v-show="project.proName" type="info" size="mini">{{project.proName}}
                                        </el-tag>
                                        <el-tag v-show="project.year" type="info" size="mini">{{project.year}}</el-tag>
                                    </div>
                                </div>
                            </div>
                        </el-col>
                        <div v-show="!projectList.length" class="text-center">
                            <div class="user_experience-title none">
                                <span>暂无项目经历</span>
                            </div>
                        </div>
                    </el-row>
                </div>

                <div class="user_detail-inner user_detail-inner-experience">
                    <div class="user_detail-title-handler">
                        <div class="ud-row-title no-border-title"><span class="name">大赛指导经历</span></div>
                    </div>
                    <el-row :gutter="10">
                        <el-col :span="12" v-for="contest in contestList" :key="contest.id">
                            <div class="experience-card">
                                <div class="experience-card-header">
                                    <h4 class="experience-card-title">{{contest.pName}}</h4>
                                </div>
                                <div class="experience-card-body">
                                    <div class="exp-pic">
                                        <a href="javascript: void(0);"><img
                                                :src="contest.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proGConPicFilter"></a>
                                    </div>
                                    <div class="exp-info">
                                        <e-col-item label="担任角色：" label-width="72px" class="white-space-pre-static">{{ contest |
                                            userRoleName(user.id) }}
                                        </e-col-item>
                                        <e-col-item label="大赛级别：" label-width="72px">{{contest.level}}</e-col-item>
                                        <e-col-item label="大赛获奖：" label-width="72px">{{contest.award}}</e-col-item>
                                        <e-col-item label="大赛周期：" label-width="72px">{{contest | userProContestDRange}}
                                        </e-col-item>
                                    </div>
                                    <div class="exp-intro">
                                        <e-col-item label="大赛简介：" label-width="72px">
                                            {{contest.introduction}}
                                        </e-col-item>
                                    </div>
                                </div>
                                <div class="experience-card-footer">
                                    <div class="text-right">
                                        <el-tag v-show="contest.type" type="info" size="mini">{{contest.type}}</el-tag>
                                        <el-tag v-show="contest.year" type="info" size="mini">{{contest.year}}</el-tag>
                                    </div>
                                </div>
                            </div>
                        </el-col>
                        <div v-show="!contestList.length" class="text-center">
                            <div class="user_experience-title none">
                                <span>暂无大赛经历</span>
                            </div>
                        </div>
                    </el-row>
                </div>

                <review-task :user-id="user.id"></review-task>



            </div>
        </div>

</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin],
        data: function () {
            <%--var currentUser = JSON.parse(JSON.stringify(${fns: toJson(currentUser)}));--%>
            var backTeacherExpansion = JSON.parse(JSON.stringify(${fns: toJson(backTeacherExpansion)})) || {};
            var masterTypes = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList('master_type'))}));
            var sexes = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList('sex'))}));
            var colleges = JSON.parse(JSON.stringify(${fns: toJson(fns: getOfficeList())})) || [];
            var educationLevel = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('enducation_level'))}));
            <%--var postTitleType = JSON.parse('${fns:toJson(fns: getDictList('postTitle_type'))}');--%>
            var degreeType = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('degree_type'))}));
            var technical = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("postTitle_type"))})) || [];
            <%--var visitors = JSON.parse(JSON.stringify(${fns: toJson(visitors)})) || [];--%>
            var backUser = backTeacherExpansion.user || {};
            var userProfessional = []
//            colleges = colleges.filter(function (item) {
//                return item.id !== '1';
//            });
            if (backUser.officeId) {
                userProfessional.push('1');
                userProfessional.push(backUser.officeId);
            }
            if (backUser.professional) {
                userProfessional.push(backUser.professional)
            }


            return {
                projectList: [],
                contestList: [],
                educationLevel:educationLevel,
                postTitleType:[],
                degreeType:degreeType,
                backTeacherExpansion: backTeacherExpansion,
                user: backTeacherExpansion.user || {},
                visitors: [],
                masterTypes: masterTypes,
//                currentUser: currentUser,
                sexes: sexes,
                colleges: colleges,
                userMobile: '${mobile}',
                userEmail: '${email}',
                userProfessional: userProfessional,
                isLiked: false,
                expertJobNames: technical
            }
        },
        computed: {
            masterTypeEntries: function () {
                return this.getEntries(this.masterTypes)
            },
            sexEntries: function () {
                return this.getEntries(this.sexes)
            },
            likeClass: function () {
                return {
                    'icon-xin': this.isLiked,
                    'icon-xin1': !this.isLiked
                }
            },
            educationLevelEntries:{
                get:function(){
                    return this.getEntries(this.educationLevel)
                }
            },
            postTitleTypeEntries:{
                get:function(){
                    return this.getEntries(this.postTitleType)
                }
            },
            degreeTypeEntries:{
                get:function(){
                    return this.getEntries(this.degreeType)
                }
            },
            expertJobNamesEntries:function () {
                return this.getEntries(this.expertJobNames);
            },
            role:function () {
                var roleType = this.backTeacherExpansion.roleType.split(','),roleName = '';
                if(roleType.indexOf('0') > -1 && roleType.length == 1){
                    roleName = '导师';
                }else if(roleType.indexOf('1') > -1 && roleType.length == 1){
                    roleName = '专家';
                }else if(roleType.length == 2){
                    roleName = '导师专家';
                }
                return roleName;
            }
        },
        methods: {

            doLike: function () {
                var userId = this.user.id;
                var self = this;
                if (this.isLiked) {
                    return;
                }
                this.$axios.get('/interactive/sysLikes/doLike?doUserId=' + userId).then(function (response) {
                    var data = response.data;
                    if (data.status == 1) {
                        self.user.likes = parseInt(self.user.likes) + 1;
                        self.isLiked = true;
                        return;
                    }
                    self.$message({
                        type: 'error',
                        message: data.msg
                    })
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    })
                })
            },

            checkedIsLiked: function () {
                var self = this;
                this.$axios.get('/sys/frontStudentExpansion/checkedIsLiked?userId=' + this.user.id).then(function (response) {
                    var data = response.data;
                    if (data.status == 1) {
                        self.isLiked = data.data;
                    }
                })
            },


            ajaxGetUserProjectById: function () {
                var self = this;
                this.$axios.get('/sys/frontStudentExpansion/ajaxGetUserProjectById?userId=' + this.user.id).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.projectList = data.datas || [];
                    }
                })
            },
            ajaxGetUserGContestById: function () {
                var self = this;
                this.$axios.get('/sys/frontStudentExpansion/ajaxGetUserGContestById?userId=' + this.user.id).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.contestList = data.datas || [];
                    }
                })
            },

            getVisitors: function () {
                var self = this;
                this.$axios.get('/sys/frontTeacherExpansion/ajaxGetViewAndLike?id=' + this.backTeacherExpansion.id).then(function (response) {
                    var data = response.data || {};
                    self.visitors = data.visitors;
                    self.user.likes = data.likes || '0';
                    self.user.views = data.views || '1';
                })
            }
        },
        beforeMount: function () {
            this.checkedIsLiked();
        },
        created: function () {
            this.ajaxGetUserProjectById();
            this.ajaxGetUserGContestById();
            this.getVisitors();
        }
    })

</script>
</body>
</html>