<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="creative"/>
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue-router.js"></script>

</head>
<body>
<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none;">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home" style="font-size: 12px;"></i>首页</a>
        </el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/page-innovation">双创项目</a></el-breadcrumb-item>
        <el-breadcrumb-item>我的项目</el-breadcrumb-item>
    </el-breadcrumb>
    <%--<div v-show="isStudent" class="conditions">--%>
    <div class="conditions">
        <e-condition type="radio" v-model="mineProject" label="项目类型" :options="fTabList" :is-show-all="false"
                     @change="handleChangeProject" :default-props="projectProps"
                     name="projectId"></e-condition>
    </div>
    <div class="text-right mgb-20">
        <el-button type="primary" size="mini" @click.stop="goToCur">当前项目</el-button>
        <el-button size="mini" @click.stop="goToProList">项目列表</el-button>
    </div>
    <div class="project-time-index">
        <router-view></router-view>
    </div>
</div>

<script type="text/x-template" id="projectTimeLine">
    <div class="time-line-container">
        <div ref="timeSideBar" class="time-sidebar">
            <ul class="time-name-nav">
                <li :class="{active: $route.query.projectId === sTab.id}" v-for="sTab in secondTabs">
                    <router-link :title="sTab.name"
                                 :to="{path: $route.query.id, query: {type: $route.query.type, actYwId: $route.query.actYwId, projectId: sTab.id}}"
                                 :key="sTab.projectId">
                        <span>{{sTab.name}}</span>
                    </router-link>
                    <div v-if="sTab.result" class="tab-result-tag">
                        {{sTab.result}}
                    </div>
                </li>
            </ul>
        </div>
        <div class="time-content">
            <div class="time-line-header clearfix">
                <div class="project-info">
                    <span>项目年度：{{projectInfo.year}}</span><span>项目申报时间：{{projectInfo.apply_time}}</span><span>项目编号：{{projectInfo.number}}</span><span>项目负责人：{{projectInfo.leader}}</span>
                </div>
            </div>
            <div v-if="$route.query.projectId && isStudent" class="time-actions text-center">
                <el-button type="primary" size="mini" @click.stop="createWeek">新建周报</el-button>
                <el-button size="mini" icon="el-icon-download" @click.stop="downApplyForm('mid')">大学生创新创业项目中期检查表
                </el-button>
                <el-button size="mini" icon="el-icon-download" @click.stop="downApplyForm('close')">大学生创新创业项目结项报告
                </el-button>
                <el-button size="mini" icon="el-icon-download" @click.stop="downApplyForm('modify')">大学生创新创业项目调整申请表
                </el-button>
                <el-button size="mini" icon="el-icon-download" @click.stop="downApplyForm('spec')">大学生创新创业项目免鉴定申请表
                </el-button>
            </div>
            <div class="pro-time-line_content" v-loading="loading">
                <div v-show="pptype === '2' && milestones.length > 0" class="gc-tab_time">
                    <div class="gc-tab_pane_1">
                        <el-button size="mini">项目过程跟踪</el-button>
                    </div>
                    <div class="gc-tab_pane_2">
                        <el-button size="mini">项目发展进度更新</el-button>
                    </div>
                </div>

                <div class="time-line" :class="{'gc':pptype === '2'}">
                    <div class="time-line-box" v-for="(item, index) in milestones" :class="{completed: item.current}">
                        <div class="time-item"
                             :class="{active: item.current, hasContent: !!item.btn_option || item.approvalTime, last: (index === milestones.length - 1)}">
                            <%--<div class="time-item-bg"></div>--%>
                            <div class="time-inner clearfix">
                                <div class="time-inner-header">
                                    <div class="cell" :class="{'no-date': !item.start_date}">
                                        <span v-if="item.start_date">{{item.start_date | formatDateFilter('YYYY.MM.DD')}}至{{item.end_date | formatDateFilter('YYYY.MM.DD')}}<br></span>
                                        <span>{{item.title}}</span>
                                    </div>
                                </div>
                                <div v-if="!!item.btn_option || item.approvalTime" class="time-inner-content">
                                    <div class="cell">
                                        <template v-if="!!item.btn_option">
                                            <a class="btn btn-milestone"
                                               :href="item.btn_option.url">{{item.btn_option.name}}</a>
                                        </template>
                                        <template v-if="item.approvalTime">
                                            <span class="approval">{{item.approvalTime}}<br>{{item.approvalDesc}}</span>
                                        </template>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <template v-if="item.children" v-for="(item2, index2) in item.children">
                            <div class="time-item-intro clearfix"
                                 :style="{'height': item2.files && item2.files.length*30 + 'px'}"
                                 :class="{left: index2%2 == 1, right: index2%2 === 0, weekly: !item2.type}">
                                <span v-if="item2.type" class="pro-t-line"></span>
                                <div v-if="!item2.type" class="intro-weekly">
                                    <div class="weekly-content">
                                        <ul v-if="item2.files" class="weekly-list weekly-list-more"
                                            :style="{'margin-top': -(15*item2.files.length)+'px'}">
                                            <li v-for="file in item2.files">
                                                <a :title="file.name"
                                                   :href="file.url">{{file.name}}
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                                <div v-if="item2.type === 'date'" class="intro-inner">
                                    <div class="intro-header">
                                        <span class="arrow"></span>
                                        <p class="title">{{item2.desc}}</p></div>
                                    <div class="intro-content">{{item2.intro}}</div>
                                </div>
                                <div class="intro-date">{{item2.start_date || item2.create_date |
                                    formatDateFilter('YYYY-MM-DD')}}
                                </div>
                                <div class="intro-dot">
                                    <span></span>
                                </div>
                            </div>
                        </template>
                        <span v-show="index > 0" class="time-line-box-arrow"></span>
                    </div>
                </div>
                <div v-show="pptype === '2' && milestones.length > 0" class="company-process_progress">
                    <div class="company-process_item primary">
                        <div class="cpi-intro">
                            <h6 class="progress_name">实践阶段</h6>
                            <p class="progress_cate">主赛道：创意组<br>职教赛道：创意组</p>
                        </div>
                        <div class="cpi-dot">
                            <span class="cpi-dot_circle"></span>
                        </div>
                        <div class="cpi-operation">
                            <el-button size="mini">当前为实践阶段</el-button>
                        </div>
                    </div>
                    <div class="company-process_item primary">
                        <div class="cpi-intro">
                            <h6 class="progress_name">成长阶段</h6>
                            <p class="progress_cate">主赛道：初创组<br>职教赛道：创业组</p>
                        </div>
                        <div class="cpi-dot">
                            <span class="cpi-dot_circle"></span>
                        </div>
                        <div class="cpi-operation">
                            <el-button size="mini">申请进入成长阶段</el-button>
                        </div>
                    </div>
                    <div class="company-process_item">
                        <div class="cpi-intro">
                            <h6 class="progress_name">成熟阶段</h6>
                            <p class="progress_cate">主赛道：成长组<br>职教赛道：师生共创组</p>
                        </div>
                        <div class="cpi-dot">
                            <span class="cpi-dot_circle"></span>
                        </div>
                        <div class="cpi-operation">
                            <el-button size="mini">申请进入成熟阶段</el-button>
                        </div>
                    </div>
                </div>
            </div>

            <div v-if="secondTabs.length == 0 || loading && !milestones.length === 0" class="empty no-content">
                没有申报项目
            </div>
        </div>
    </div>
</script>

<script type="text/x-template" id="noneTimeLine">
    <div class="time-line-container">
        <div class="time-content">
            <div class="time-line-header clearfix">
            </div>
            <div>
                <p style="margin: 60px auto;" class="text-center">没有项目数据，请发布项目</p>
            </div>
        </div>
    </div>
</script>

<script type="text/javascript">
    'use strict';
    var fTabList = JSON.parse(JSON.stringify(${pp})) || [];
    function canSumitClose(projectId, url) {
        $.ajax({
            type: "GET",
            url: "canSumitClose",
            data: "projectId=" + projectId,
            dataType: "text",
            success: function (data) {
                if (data == "false") {
                    dialogCyjd.createDialog(0, "未创建学分配比，请先完成该信息", {
                        dialogClass: 'dialog-cyjd-container none-close',
                        buttons: [{
                            text: "创建学分配比",
                            'class': 'btn btn-sm btn-primary',
                            click: function () {
                                $(this).dialog('close')
                                top.location = "scoreConfig?projectId=" + projectId;
                            }
                        }, {
                            text: '取消',
                            'class': 'btn btn-sm btn-primary',
                            click: function () {
                                $(this).dialog('close');
                            }
                        }]
                    });
                } else {
                    top.location = url + "?projectId=" + projectId;
                }
            }
        });
    }
    var TimeLine = Vue.component('time-line', {
        template: '#projectTimeLine',
        data: function () {
            return {
                secondTabs: [],
                projectInfo: {
                    proname: '',
                    pid: '',
                    number: '',
                    apply_time: '',
                    year: '',
                    leader: ''
                },
                milestones: [],
                loading: false
            }
        },
        computed: {
            weeklyLink: function () {
                var type = this.$route.query.type;
                var linkPart = type == 1 ? '/project/weekly/createWeekly?projectId=' : '/project/weekly/createWeeklyPlus?projectId=';
                linkPart = this.ctxFront + linkPart;
                linkPart += this.projectInfo.pid;
                return linkPart;
            },
            ppid: function () {
                return this.$route.params.id;
            },
            pptype: function () {
                return this.$route.query.type;
            },
            projectId: function () {
                return this.$route.query.projectId;
            }
        },
        watch: {
            // 如果路由有变化，会再次执行该方法
            'pptype': function () {
                this.getSecondTabs();
            },
            projectId: function (value) {
//                if(!value){
//                    this.getSecondTabs();
//                }else {
//                    this.getTimeList();
//                }
                this.getTimeList();
            }
        },
        methods: {
            getSecondTabs: function () {
                var self = this;
                var $route = this.$route;
                var postData = {
                    pptype: $route.query.type,
                    actywId: $route.query.actYwId
                };
                this.$axios.get('/project/projectDeclare/getTimeIndexSecondTabs', {params: postData}).then(function (response) {
                    var data = response.data;
                    if (data.length > 0) {
                        self.secondTabs = data;
                        self.$router.push({
                            path: '/' + $route.params.id,
                            query: {
                                type: $route.query.type,
                                actYwId: $route.query.actYwId,
                                projectId: $route.query.projectId ? $route.query.projectId : data[0].id
                            }
                        })
                    } else {
                        self.secondTabs = [];
                        self.resetData();
                    }
                })
            },

            createWeek: function () {
                location.href = this.weeklyLink;
            },

            downApplyForm: function (type) {
                location.href = this.ctxFront + '/proproject/downTemplate?type=' + type
            },

            getTimeList: function () {
                var $route = this.$route;
                var postData = {
                    pptype: $route.query.type,
                    actywId: $route.query.actYwId,
                    ppid: this.ppid,
                    projectId: $route.query.projectId
                };
                var self = this;
                this.loading = true;
                this.$axios.get('/project/projectDeclare/getTimeIndexData', {params: postData}).then(function (response) {
                    var data = response.data;
                    if (data && data.pid) {
                        self.assignFormData(self.projectInfo, data);
                        var timeList = data.contents;
                        var milestones = timeList.filter(function (item) {
                            return item.type === 'milestone'
                        });
                        self.dates = timeList.filter(function (item) {
                            return item.type === 'date'
                        });
                        var files = data.files || [];
                        var typeDates = timeList.filter(function (item) {
                            return item.type === 'date'
                        });
                        var filesDate = files.concat(typeDates);
                        var sortFilesDates = filesDate.sort(self.sortTime);
                        self.getTimestamp(milestones).forEach(function (t, index) {
                            var startTimes = t.startTime;
                            var endTimes = t.endTime;
                            milestones[index].children = [];
                            sortFilesDates.forEach(function (t2, i) {
                                var times = new Date(t2.start_date || t2.create_date).getTime();
                                if (times >= startTimes && times <= endTimes) {
                                    milestones[index].children.push(t2);
                                }
                            })
                        });
                        milestones = milestones.filter(function (item) {
                            return item.type === 'milestone'
                        })
                        self.milestones = milestones;
                    } else {
                        self.resetData();
                    }
                    self.loading = false;
                })
            },

            resetData: function () {
                this.milestones = [];
                this.projectInfo = {
                    proname: '',
                    pid: '',
                    number: '',
                    apply_time: '',
                    year: '',
                    leader: ''
                }
            },

            getTimestamp: function (milestones) {
                var i = 1;
                var timesTamp = [];

                if (milestones.length <= 1) {
                    return timesTamp;
                }
                while (i < milestones.length) {
                    var startTime = new Date(milestones[i - 1].start_date).getTime();
                    var endTime = new Date(milestones[i].start_date).getTime();
                    timesTamp.push({
                        startTime: startTime,
                        endTime: endTime,
                    })
                    i++;
                }
                return timesTamp;
            },

            sortTime: function (item1, item2) {
                var date1 = item1['start_date'] || item1['create_date'];
                var date2 = item2['start_date'] || item2['create_date'];


                var time1 = new Date(date1).getTime();
                var time2 = new Date(date2).getTime();
                if (time1 < time2) {
                    return -1;
                } else if (time1 > time2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        },
        created: function () {
            this.getSecondTabs();
            this.getTimeList();
        }
    });

    var NoneTimeLine = Vue.component('none-time-line', {
        template: '#noneTimeLine',
        data: function () {
            return {}
        },
        beforeMount: function () {
        }
    });


    function CurRouter() {
        var router;
        if (fTabList && fTabList.length > 0) {
            var firstId = fTabList[0].id;
            var redirect = '/' + firstId;
            router = new VueRouter({
                routes: [
                    {
                        path: '',
                        redirect: redirect
                    }, //默认指向第一个
                    // 动态路径参数 以冒号开头
                    {path: '/:id', name: 'timeLine', component: TimeLine}
                ]
            })
        } else {
            router = new VueRouter({
                routes: [
                    {
                        path: '/noneTimeLine',
                        component: NoneTimeLine,
                    }
                ]
            });
        }
        return router;
    }


    new Vue({
        el: '#app',
        router: new CurRouter(),
        data: function () {
            return {
                fTabList: fTabList || [],
                mineProject: '',
                projectProps: {
                    label: 'label',
                    value: 'id'
                },
                frProId: ''
            }
        },
        computed: {
            fType: function () {
                var fTabList = this.fTabList;
                return fTabList.length > 0 ? fTabList[0].type : '';
            },
            fActYwId: function () {
                var fTabList = this.fTabList;
                return fTabList.length > 0 ? fTabList[0].actywId : '';
            },
            fProjectId: function () {
                var fTabList = this.fTabList;
                return fTabList.length > 0 ? fTabList[0].id : '';
            }
        },
        methods: {
            handleChangeProject: function (id) {
                var projectTab = this.getProjectTab(id);
                if (projectTab) {
                    this.$router.push({
                        path: '/' + projectTab.id,
                        query: {
                            type: projectTab.type,
                            actYwId: projectTab.actywId
                        }
                    })
                } else {
                    this.$router.push({
                        path: '/noneTimeLine'
                    })
                }
            },

            getFrProId: function (postData) {
                var self= this;
                this.$axios.get('/project/projectDeclare/getTimeIndexSecondTabs', {params: postData}).then(function (response) {
                    var data = response.data;
                    if (data.length > 0) {
                        self.frProId = data[0].id;
                    } else {
                        self.frProId = '';
                    }
                })
            },

            getProjectTab: function (id) {
                var i = 0;
                var fTabList = this.fTabList;
                while (i < fTabList.length) {
                    if (fTabList[i].id === id) {
                        return fTabList[i];
                    }
                    i++
                }
                return false;
            },

            goToCur: function () {
                var fProjectId = this.fProjectId;
                var fType = this.fType;
                var fActYwId = this.fActYwId;
                var frProId = this.frProId;
                this.$router.push({
                    path: '/' + fProjectId,
                    query: {
                        type: fType,
                        actYwId: fActYwId,
                        projectId: frProId
                    }
                })
                this.mineProject = fProjectId;
            },
            goToProList: function () {
                location.href = this.ctxFront + '/project/projectDeclare/list';
            },

            fRoute: function () {
                var fType = this.fType;
                var fActYwId = this.fActYwId;
//                if (this.isStudent && !this.$route.query.type) {
                if (!this.$route.query.type) {
                    if (this.$route.params.id) {
                        this.$router.push({
                            path: '/' + (this.$route.params.id || ''),
                            query: {
                                type: fType,
                                actYwId: fActYwId
                            }
                        })
                        this.getFrProId({
                            pptype: fType,
                            actywId: fActYwId
                        })
                    } else {
                        this.$router.push({
                            path: '/noneTimeLine',
                        })
                    }
                }
                this.mineProject = this.$route.params.id;
            }
        },
        created: function () {
            this.fRoute();
        }
    })

</script>
</body>
</html>