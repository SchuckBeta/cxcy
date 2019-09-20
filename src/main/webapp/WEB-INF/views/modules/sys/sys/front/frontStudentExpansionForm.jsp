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
        <el-breadcrumb-item><a href="${ctxFront}/sys/frontStudentExpansion">学生库</a></el-breadcrumb-item>
        <el-breadcrumb-item>学生信息</el-breadcrumb-item>
    </el-breadcrumb>
    <el-row :gutter="30">
        <el-col :span="19" class="user-detail-view">
            <div class="user_detail-title-handler">
                <div class="ud-row-title"><span class="name">个人信息</span></div>
            </div>
            <el-row :gutter="30">
                <el-col :span="4">
                    <div class="user-detail-pic">
                        <img :src="user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter">
                        <div v-if="user.id != loginCurUser.id" class="text-right btn-like-action">
                            <a class="user-action-like" :class="{liked: isLiked}" @click.stop.prevent="doLike"
                               href="javascript: void(0);">
                                <i class="iconfont" :class="likeClass"></i>
                                赞
                            </a>
                        </div>
                    </div>
                </el-col>
                <el-col :span="20">
                    <el-row :gutter="16" label-width="84px">
                        <el-col :span="12">
                            <e-col-item label="姓名：" align="right">{{user.name}}</e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item label="性别：" align="right">{{user.sex | selectedFilter(sexEntries)}}</e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item label="班级：" align="right">{{studentExpansion.tClass}}<span class="empty-color"
                                                                                                   v-if="studentExpansion.tClass">班</span>
                            </e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item label="现状：" align="right">{{studentExpansion.currState |
                                selectedFilter(currentStateEntries)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item label="手机号：" align="right">{{userMobile}}</e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item label="电子邮箱：" align="right">{{userEmail}}</e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="学院/专业：" align="right">{{user.professional |
                                getProfessionName(collegeEntries)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="技术领域：" align="right">{{user.domainlt}}</e-col-item>
                        </el-col>
                    </el-row>
                </el-col>
            </el-row>

            <div class="user_detail-container" style="margin-left: 0">
                <div class="user_detail-inner user_detail-inner-experience">
                    <div class="user_detail-title-handler">
                        <div class="ud-row-title"><span class="name">项目经历</span></div>
                    </div>
                    <el-row :gutter="10">
                        <el-col :span="12" v-for="project in projectList" :key="project.id">
                            <div class="experience-card">
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
                                        <e-col-item label="项目简介：" label-width="72px" class="white-space-pre-static word-break">
                                            <span v-html="project.introduction"></span>
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
                        <div class="ud-row-title"><span class="name">大赛经历</span></div>
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
                                        <e-col-item label="担任角色：" label-width="72px">{{ contest |
                                            userRoleName(user.id) }}
                                        </e-col-item>
                                        <e-col-item label="大赛级别：" label-width="72px">{{contest.level}}</e-col-item>
                                        <e-col-item label="大赛获奖：" label-width="72px">{{contest.award}}</e-col-item>
                                        <e-col-item label="大赛周期：" label-width="72px">{{contest | userProContestDRange}}
                                        </e-col-item>
                                    </div>
                                    <div class="exp-intro">
                                        <e-col-item label="大赛简介：" label-width="72px" class="white-space-pre-static word-break">
                                            <span v-html="contest.introduction"></span>
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


                <skill-cert :user-id="user.id"></skill-cert>

                <personal-score :user-id="user.id"></personal-score>

            </div>
        </el-col>
        <el-col :span="5">
            <div class="user_detail-title-handler">
                <div class="ud-row-title"><span class="name">谁看过<template v-if="loginCurUser.id == user.id">我</template><template
                        v-else>{{user.sex == '0' ? '她' : '他'}}</template></span></div>
            </div>
            <div class="visitor-list">
                <div v-for="(item, index) in visitors" :key="item.id" v-if="index < 6" class="visitor-item">
                    <div class="visitor-item-inner">
                        <div class="visitor-pic">
                            <a :href="item | goToUserPage">
                                <img :src="item.photo | ftpHttpFilter(ftpHttp) | studentPicFilter">
                                <div class="visitor-date">{{item.create_date | formatDateFilter('YYYY-MM-DD')}}</div>
                            </a>
                        </div>
                        <div class="name">{{item.name}}</div>
                    </div>
                </div>
                <div v-if="!visitors.length" class="empty-color empty text-center mgb-20">暂无人访问过</div>
            </div>
            <div class="visitor-actions-num">
                <div class="visitor-action">
                    <div class="action">
                        浏览量
                    </div>
                    <div class="num">
                        {{user.views}}
                    </div>
                </div>
                <div class="visitor-action">
                    <div class="action">点赞数</div>
                    <div class="num">
                        {{user.likes}}
                    </div>
                </div>
            </div>
        </el-col>
    </el-row>

</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin],
        data: function () {
            var currentUser = JSON.parse(JSON.stringify(${fns: toJson(currentUser)}));
            var studentExpansion = JSON.parse(JSON.stringify(${fns: toJson(studentExpansion)})) || {};
            var currentStates = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList('current_sate'))}));
            var sexes = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList('sex'))}));
            var colleges = JSON.parse(JSON.stringify(${fns: toJson(fns: getOfficeList())})) || [];
            <%--var visitors = JSON.parse(JSON.stringify(${fns: toJson(visitors)})) || [];--%>

            return {
                projectList: [],
                contestList: [],
                studentExpansion: studentExpansion,
                user: studentExpansion.user || {},
                visitors: [],
                currentStates: currentStates,
                currentUser: currentUser,
                sexes: sexes,
                colleges: colleges,
                userMobile: '${mobile}',
                userEmail: '${email}',
                isLiked: false
            }
        },
        computed: {
            currentStateEntries: function () {
                return this.getEntries(this.currentStates)
            },
            sexEntries: function () {
                return this.getEntries(this.sexes)
            },
            likeClass: function () {
                return {
                    'icon-xin': this.isLiked,
                    'icon-xin1': !this.isLiked,
                }
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
                this.$axios.get('/sys/frontStudentExpansion/ajaxGetViewAndLike?id=' + this.studentExpansion.id).then(function (response) {
                    var data = response.data || {}
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