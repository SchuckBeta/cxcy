/**
 * Created by Administrator on 2018/11/19.
 */


'use strict';

Vue.component('row-step-apply', {
    template: '<div class="row-step-cyjd" :style="{width: stepWidth}"><div class="step-indicator"><slot></slot></div></div>',
    data: function () {
        return {
            steps: []
        }
    },
    componentName: 'RowStepApply',
    computed: {
        stepWidth: function () {
            return this.steps.length * (240 + 33) + 'px'
        }
    },
    methods: {
        updateItems: function () {
            this.steps = this.$children.filter(function (child) {
                return child.$options.name === 'StepItem'
            })
        }
    }
});

Vue.component('step-item', {
    template: '<a class="step" :class="cls"><slot></slot></a>',
    props: {
        isComplete: Boolean
    },
    name: 'StepItem',
    componentName: 'StepItem',
    computed: {
        cls: function () {
            return {
                completed: this.isComplete
            }
        },
        rowStepApply: function () {
            var $parent = this.$parent;
            while ($parent) {
                if ($parent.$options.componentName !== 'RowStepApply') {
                    $parent = $parent.$parent;
                } else {
                    return $parent;
                }
            }
            return false;
        }
    },
    created: function () {
        if (this.rowStepApply) {
            this.rowStepApply.updateItems();
        }
    }
})


Vue.component('tab-pane-content', {
    template: '<div class="tab-pane-content"><slot v-if="(tabPane && tabPane.active) || firstShow"></slot></div>',
    data: function () {
        return {
            firstShow: false
        }
    },
    componentName: 'TabPaneContent',
    name: 'TabPaneContent',
    computed: {
        tabPane: function () {
            var $parent = this.$parent;
            while ($parent) {
                if ($parent.$options.componentName !== 'ElTabPane') {
                    $parent = $parent.$parent;
                } else {
                    if (!this.firstShow) {
                        this.firstShow = $parent.active;
                    }
                    return $parent;
                }
            }
            return false;
        }
    }
});


Vue.directive('auto-img', {
    componentUpdated: function (element, binding) {
        var ratio = binding.value;
        element.onload = function () {
            var imgScale = element.naturalWidth / element.naturalHeight;
            element.style.width = imgScale > ratio ? '100%' : 'auto';
            element.style.height = imgScale >= ratio ? 'auto' : '100%'
        }
    },
    unbind: function (element) {
        element.onload = null;
    }
});

Vue.component('skill-cert', {
    template: '<div>\n' +
    '    <div class="user_detail-inner user_detail-cert-experience">\n' +
    '        <div class="user_detail-title-handler">\n' +
    '            <div class="ud-row-title"><span class="name">技能证书</span></div>\n' +
    '        </div>\n' +
    '        <el-row :gutter="10">\n' +
    '            <el-col :span="6" v-for="item in certList" :key="item.id">\n' +
    '                <div class="experience-card">\n' +
    '                    <div class="card-img"><img v-for="(item,index) in item.sysAttachmentList" v-if="index==0" :src="item.ftpUrl" alt=""></div>\n' +
    '                    <div class="label-mark">' +
    '                        <el-tooltip :content="item.name" popper-class="white" placement="right">\n' +
    '                           <span class="break-ellipsis">证书：{{item.name}}</span>' +
    '                        </el-tooltip>\n' +
    '                    </div>\n' +
    '                    <div class="label-mark">\n' +
    '                        <span>获得时间：{{item.time | formatDateFilter(\'YYYY-MM-DD\')}}</span>\n' +
    '                        <el-tooltip content="预览证书" popper-class="white" placement="bottom">\n' +
    '                                    <span><a href="javascript:void(0)"><i class="iconfont icon-yulan"\n' +
    '                                                                          @click.stop.prevent="lookImg(item.sysAttachmentList)"></i></a></span>\n' +
    '                        </el-tooltip>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '            </el-col>\n' +
    '            <div v-show="certList.length < 1" class="text-center">\n' +
    '                <div class="user_experience-title none">\n' +
    '                    <span>暂无技能证书</span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </el-row>\n' +
    '    </div>\n' +
    '    <el-dialog title="证书预览" :visible.sync="dialogCertVisible" width="50%" :before-close="handleClose">\n' +
    '        <el-carousel indicator-position="outside" ref="elCarousel" :height="bannerHeight + \'px\'" :autoplay="false"\n' +
    '                     trigger="click">\n' +
    '            <el-carousel-item v-for="item in bannerImg" :key="item.id">\n' +
    '                <div class="preview-cert-content">\n' +
    '                    <img :src="item.ftpUrl" alt="证书图片" v-auto-img="bannerScale">\n' +
    '                </div>\n' +
    '            </el-carousel-item>\n' +
    '        </el-carousel>\n' +
    '    </el-dialog>' +
    '</div>',
    props: {
        userId: String
    },
    data: function () {
        return {
            certList: [],
            dialogCertVisible: false,
            bannerHeight: 400,
            bannerImg: [],
            bannerScale: 1
        }
    },
    methods: {
        getCertList: function () {
            var self = this;
            this.$axios({
                method: 'GET',
                url: '/sys/frontStudentExpansion/ajaxGetUserCertById?userId=' + self.userId
            }).then(function (response) {
                var data = response.data;
                if (data.status == '1') {
                    self.certList = data.data || [];
                }
            }).catch(function (error) {
                self.$message({
                    message: self.xhrErrorMsg,
                    type: 'error'
                })
            })
        },
        lookImg: function (sysAttachmentList) {
            var self = this;
            this.bannerImg = [];
            sysAttachmentList.forEach(function (item) {
                self.bannerImg.push({id: item.id, ftpUrl: item.ftpUrl})
            });
            this.imgCenter();
            this.dialogCertVisible = true;
        },
        imgCenter: function () {
            var self = this;
            var bannerWidth = 0;
            self.$nextTick(function () {
                bannerWidth = self.$refs.elCarousel.$el.offsetWidth;
                self.bannerHeight = bannerWidth * 0.55;
                self.bannerScale = bannerWidth / self.bannerHeight;
            });
        },
        handleClose: function () {
            this.dialogCertVisible = false;
            this.bannerImg = [];
        }
    },
    created: function () {
        this.getCertList();
    }
});


Vue.component('personal-score', {
    template: '<div class="user_detail-inner user_detail-score-experience" style="margin-bottom: 60px;">\n' +
    '                <div class="user_detail-title-handler">\n' +
    '                    <div class="ud-row-title"><span class="name">双创学分</span></div>\n' +
    '                </div>\n' +
    '                <div style="padding: 0 50px;" v-show="scoreList.length >= 1">\n' +
    '                    <table class="el-table table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree">\n' +
    '                        <thead>\n' +
    '                        <tr>\n' +
    '                            <th width="200px" class="text-left">学分类别</th>\n' +
    '                            <th width="250px" class="text-left">获得学分项</th>\n' +
    '                            <th width="200px" class="text-left">获得学分时间</th>\n' +
    '                            <th width="200px">获得学分</th>\n' +
    '                        </tr>\n' +
    '                        </thead>\n' +
    '                        <tbody>\n' +
    '                        <tr v-for="item in scoreList" :key="item.id">\n' +
    '                            <td class="text-left">{{item.creditName}}</td>\n' +
    '                            <td class="text-left">{{item.name}}</td>\n' +
    '                            <td class="text-left">{{item.auditDate | formatDateFilter(\'YYYY-MM-DD\')}}</td>\n' +
    '                            <td class="text-center">{{item.credit}}</td>\n' +
    '                        </tr>\n' +
    '                        </tbody>\n' +
    '                    </table>\n' +
    '                </div>\n' +
    '                <div v-show="scoreList.length == 0" class="text-center">\n' +
    '                    <div class="user_experience-title none">\n' +
    '                        <span>暂无双创学分</span>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '            </div>',
    props: {
        userId: String
    },
    data: function () {
        return {
            scoreList: []
        }
    },
    methods: {
        getScoreList: function () {
            var self = this;
            this.$axios({
                method: 'GET',
                url: '/sys/frontStudentExpansion/ajaxGetCreditById?userId=' + self.userId
            }).then(function (response) {
                var data = response.data;
                if (data.status == '1') {
                    self.scoreList = data.data.list || [];
                }
            }).catch(function (error) {
                self.$message({
                    message: self.xhrErrorMsg,
                    type: 'error'
                })
            })
        }
    },
    created: function () {
        this.getScoreList();
    }
});


Vue.component('contest-experience', {
    template: '<div class="user_detail-inner user_detail-inner-experience">\n' +
    '                <div v-if="showTitle" class="user_detail-title-handler">\n' +
    '                    <div class="ud-row-title"><span class="name">{{titleLabel}}</span></div>\n' +
    '                </div>\n' +
    '                <el-row :gutter="10">\n' +
    '                    <el-col :span="12" v-for="contest in cpProjectList" :key="contest.id">\n' +
    '                        <div class="experience-card">\n' +
    '                            <div class="experience-card-header">\n' +
    '                                <h4 class="experience-card-title">{{contest.pName}}</h4>\n' +
    '                            </div>\n' +
    '                            <div class="experience-card-body">\n' +
    '                                <div class="exp-pic">\n' +
    '                                    <a href="javascript: void(0);"><img\n' +
    '                                            :src="contest.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proGConPicFilter"></a>\n' +
    '                                </div>\n' +
    '                                <div class="exp-info">\n' +
    '                                    <e-col-item v-if="showTitle" label="担任角色：" label-width="72px">{{contest.roleName}}</e-col-item>\n' +
    '                                    <e-col-item label="大赛级别：" label-width="72px">{{contest.level}}</e-col-item>\n' +
    '                                    <e-col-item label="大赛获奖：" label-width="72px">{{contest.award}}</e-col-item>\n' +
    '                                    <e-col-item label="大赛周期：" label-width="72px">{{contest.period}}\n' +
    '                                    </e-col-item>\n' +
    '                                </div>\n' +
    '                                <div class="exp-intro">\n' +
    '                                    <e-col-item label="大赛简介：" label-width="72px">\n' +
    '                                        <span class="white-space-pre">{{contest.introduction}}</span>\n' +
    '                                    </e-col-item>\n' +
    '                                </div>\n' +
    '                            </div>\n' +
    '                            <div class="experience-card-footer">\n' +
    '                                <div class="text-right">\n' +
    '                                    <el-tag v-show="contest.type" type="info" size="mini">{{contest.type}}</el-tag>\n' +
    '                                    <el-tag v-show="contest.year" type="info" size="mini">{{contest.year}}</el-tag>\n' +
    '                                </div>\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                    </el-col>\n' +
    '                    <div v-show="cpProjectList.length < 1" class="text-center">\n' +
    '                        <div class="user_experience-title none">\n' +
    '                            <span>暂无大赛经历</span>\n' +
    '                        </div>\n' +
    '                    </div>\n' +
    '                </el-row>\n' +
    '            </div>',
    props: {
        userId: {
            type: String,
            default: function () {
                return '';
            }
        },
        contestList: {
            type: Array,
            default: function () {
                return [];
            }
        },
        isRequest: {
            type: Boolean,
            default: function () {
                return false;
            }
        },
        titleLabel: {
            type: String,
            default: function () {
                return '大赛经历';
            }
        },
        showTitle: {
            type: Boolean,
            default: function () {
                return true
            }
        }
    },
    data: function () {
        var self = this;
        return {
            contestListData:  []
        }
    },
    computed: {
        cpProjectList: function () {
            var list = [];
            var userId = this.userId;
            list = this.isRequest ? this.contestListData : this.contestList;
            list.forEach(function (item) {
                var startDate, endDate;
                item.period = '';
                item.roleName = '导师';
                if (item.startDate) {
                    startDate = moment(item.startDate).format('YYYY-MM-DD');
                }
                if (item.endDate) {
                    endDate = moment(item.endDate).format('YYYY-MM-DD');
                }
                if (startDate) {
                    item.period = startDate + '至' + endDate;
                }
                if (userId === item.leaderId) {
                    item.roleName = '项目负责人';
                } else {
                    if (item.userType === '1') {
                        item.roleName = '组成员';
                    }
                }
            })
            return list;
        }
    },
    methods: {
        getContestList: function () {
            var self = this;
            this.$axios.get('/sys/frontStudentExpansion/ajaxGetUserGContestById?userId=' + this.userId).then(function (response) {
                var data = response.data;
                if (data.status) {
                    self.contestListData = data.datas || [];
                }
            })
        },
        getRoleName: function (project) {
            if (this.userId === project.leaderId) {
                return '项目负责人'
            } else {
                if (project.userType === '1') {
                    return '组成员'
                }
            }
            return '导师'
        },
        getProjectRange: function (project) {
            var startDate, endDate;
            if (project.startDate) {
                startDate = moment(project.startDate).format('YYYY-MM-DD');
            }
            if (project.endDate) {
                endDate = moment(project.endDate).format('YYYY-MM-DD');
            }
            if (startDate) {
                return startDate + '至' + endDate;
            }
            return ''
        }
    },
    created: function () {
        if (this.isRequest) {
            this.getContestList();
        }
    }
});


Vue.component('project-experience', {
    template: '<div class="user_detail-inner user_detail-inner-experience">\n' +
    '                <div v-if="showTitle" class="user_detail-title-handler">\n' +
    '                    <div class="ud-row-title"><span class="name">{{titleLabel}}</span></div>\n' +
    '                </div>\n' +
    '                <el-row :gutter="10">\n' +
    '                    <el-col :span="12" v-for="project in cpProjectList" :key="project.id">\n' +
    '                        <div class="experience-card">\n' +
    '                            <div class="experience-card-header">\n' +
    '                                <h4 class="experience-card-title">{{project.name}}</h4>\n' +
    '                            </div>\n' +
    '                            <div class="experience-card-body">\n' +
    '                                <div class="exp-pic">\n' +
    '                                    <a href="javascript: void(0);"><img\n' +
    '                                            :src="project.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proGConPicFilter"></a>\n' +
    '                                </div>\n' +
    '                                <div class="exp-info">\n' +
    '                                    <e-col-item v-if="showTitle" label="担任角色：" label-width="72px">{{ project.roleName }}</e-col-item>\n' +
    '                                    <e-col-item label="项目级别：" label-width="72px">{{project.level}}</e-col-item>\n' +
    '                                    <e-col-item label="项目结果：" label-width="72px">{{project.result}}</e-col-item>\n' +
    '                                    <e-col-item label="项目周期：" label-width="72px">{{project.period}}\n' +
    '                                    </e-col-item>\n' +
    '                                </div>\n' +
    '                                <div class="exp-intro">\n' +
    '                                    <e-col-item label="项目简介：" label-width="72px">\n' +
    '                                        <span class="white-space-pre">{{project.introduction}}</span>\n' +
    '                                    </e-col-item>\n' +
    '                                </div>\n' +
    '                            </div>\n' +
    '                            <div class="experience-card-footer">\n' +
    '                                <div class="text-right">\n' +
    '                                    <el-tag v-show="project.proName" type="info" size="mini">{{project.proName}}\n' +
    '                                    </el-tag>\n' +
    '                                    <el-tag v-show="project.year" type="info" size="mini">{{project.year}}</el-tag>\n' +
    '                                </div>\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                    </el-col>\n' +
    '                    <div v-show="cpProjectList.length === 0" class="text-center">\n' +
    '                        <div class="user_experience-title none">\n' +
    '                            <span>暂无项目经历</span>\n' +
    '                        </div>\n' +
    '                    </div>\n' +
    '                </el-row>\n' +
    '            </div>',
    props: {
        userId: {
            type: String,
            default: function () {
                return '';
            }
        },
        projectList: {
            type: Array,
            default: function () {
                return [];
            }
        },
        isRequest: {
            type: Boolean,
            default: function () {
                return false;
            }
        },
        titleLabel: {
            type: String,
            default: function () {
                return '项目经历';
            }
        },
        showTitle: {
            type: Boolean,
            default: function () {
                return true
            }
        }
    },
    data: function () {
        return {
            projectListData: []
        }
    },
    computed: {
        cpProjectList: function () {
            var list = [];
            var userId = this.userId;
            list = this.isRequest ? this.projectListData : this.projectList;
            list.forEach(function (item) {
                var startDate, endDate;
                item.period = '';
                item.roleName = '导师';
                if (item.startDate) {
                    startDate = moment(item.startDate).format('YYYY-MM-DD');
                }
                if (item.endDate) {
                    endDate = moment(item.endDate).format('YYYY-MM-DD');
                }
                if (startDate) {
                    item.period = startDate + '至' + endDate;
                }
                if (userId === item.leaderId) {
                    item.roleName = '项目负责人';
                } else {
                    if (item.userType === '1') {
                        item.roleName = '组成员';
                    }
                }
            })
            return list;
        }
    },
    methods: {
        getProjectList: function () {
            var self = this;
            this.$axios.get('/sys/frontStudentExpansion/ajaxGetUserProjectById?userId=' + this.userId).then(function (response) {
                var data = response.data;
                if (data.status) {
                    self.projectListData = data.datas || [];
                }
            })
        },
        getRoleName: function (project) {
            if (this.userId === project.leaderId) {
                return '项目负责人'
            } else {
                if (project.userType === '1') {
                    return '组成员'
                }
            }
            return '导师'
        },
        getProjectRange: function (project) {
            var startDate, endDate;
            if (project.startDate) {
                startDate = moment(project.startDate).format('YYYY-MM-DD');
            }
            if (project.endDate) {
                endDate = moment(project.endDate).format('YYYY-MM-DD');
            }
            if (startDate) {
                return startDate + '至' + endDate;
            }
            return ''
        }
    },
    created: function () {
        if (this.isRequest) {
            this.getProjectList();
        }
    }
});


Vue.component('review-task', {
    template: '<div class="user_detail-inner user_detail-check-experience" style="margin-bottom: 60px;">\n' +
    '                <div class="user_detail-title-handler">\n' +
    '                    <div class="ud-row-title"><span class="name">评审任务</span></div>\n' +
    '                </div>\n' +
    '                <el-row :gutter="10" label-width="120px" v-for="item in taskList" :key="item.year">\n' +
    '                    <el-col :span="12">\n' +
    '                        <e-col-item label="参与评审年份：" align="right">{{item.year}}年</e-col-item>\n' +
    '                    </el-col>\n' +
    '                    <el-col :span="12">\n' +
    '                        <e-col-item label="参评项目：" align="right">{{item.name}}</e-col-item>\n' +
    '                    </el-col>\n' +
    '                </el-row>\n' +
    '                <div v-show="taskList.length < 1" class="text-center">\n' +
    '                    <div class="user_experience-title none">\n' +
    '                        <span>暂无评审任务</span>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '            </div>',
    props: {
        userId: String
    },
    data: function () {
        return {
            taskList: []
        }
    },
    methods: {
        getTaskList: function () {
            var self = this;
            this.$axios({
                method: 'GET',
                url: '/sys/frontTeacherExpansion/ajaxAuditTask?user.id=' + self.userId
            }).then(function (response) {
                var data = response.data;
                if (data.status == '1') {
                    self.taskList = data.data || [];
                }
            }).catch(function (error) {
                self.$message({
                    message: self.xhrErrorMsg,
                    type: 'error'
                })
            })
        }
    },
    created: function () {
        this.getTaskList();
    }
});


Vue.component('e-panel', {
    template: '<div class="panel"><template v-if="$slots.label || label"><div class="panel-header panel-header-title"><slot name="label"></slot><span v-if="label">{{label}}</span></div></template><div class="panel-body"><slot></slot></div></div> ',
    props: {
        label: String
    }
})

Vue.component('act-yw-gnode-label', {
    template: '<span>{{actYwGnode.name}}</span>',
    props: {
        url: String
    },
    data: function () {
        return {
            actYwGnode: {}
        }
    },
    methods: {
        getActYwGnode: function () {
            var self = this;
            this.$axios.post(this.url).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.actYwGnode = data.data || {};
                }
            })
        }
    },
    created: function () {
        this.getActYwGnode();
    }
})


//项目大赛公用个人信息快
Vue.component('applicant-info-pro-contest', {
    template: '<div class="applicant-info applicant-info-pro-contest"> <div class="user-card-photo"> <img :src="applyUser.photo | ftpHttpFilter(ftpHttp) | studentPicFilter"> </div>' +
    ' <div class="user-intros"> <el-row :gutter="8"> <el-col :span="3" style="min-height: 1px"> <div class="user-name-sex"> <span class="name">{{applyUser.name}}</span> <i class="iconfont" :class="sexClassName"></i> </div> </el-col> <el-col :span="21" style="min-height: 1px"> <div class="user-intro-items"> <span class="user-intro-item"><i class="el-icon-date"></i>{{applyUser.birthday | formatDateFilter(\'YYYY-MM\')}}</span> ' +
    '<span class="user-intro-item"><i class="iconfont icon-renshixuexiao"></i>{{applyUser.officeName}}<template v-if="applyUser.professional">/{{applyUser.professional | selectedFilter(officeEntries)}}</template></span> ' +
    '<span class="user-intro-item"><i class="iconfont icon-yooxi"></i>{{applyUser.no}}</span> </div> <div class="user-intro-items"> <span class="user-intro-item"><i class="iconfont icon-unie64f"></i>{{applyUser.mobile}}</span> <span class="user-intro-item"><i class="iconfont icon-noread"></i>{{applyUser.email}}</span> ' +
    '</div> </el-col> </el-row> </div> </div>',
    props: {
        applyUser: Object
    },
    computed: {
        officeEntries: function () {
            return this.$root.officeEntries
        },
        sexClassName: function () {
            return {
                'icon-xingbie-nan': this.applyUser.sex === '1',
                'icon-xingbie-nv': this.applyUser.sex !== '1'
            }
        },
    }
})


//项目大赛公用审核部分
Vue.component('promodel-audit-record', {
    template: '<e-panel label="审核记录">\n' +
    '                        <div class="table-container">\n' +
    '                            <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"\n' +
    '                                   style="table-layout: fixed;"\n' +
    '                                   cellspacing="0" cellpadding="0">\n' +
    '                                <thead>\n' +
    '                                <tr>\n' +
    '                                    <th>审核动作</th>\n' +
    '                                    <th>审核时间</th>\n' +
    '                                    <th>审核人</th>\n' +
    '                                    <th>审核结果</th>\n' +
    '                                    <th>建议及意见</th>\n' +
    '                                </tr>\n' +
    '                                </thead>\n' +
    '                                <tbody>\n' +
    '                                <tr v-for="item in actYwAuditInfos">\n' +
    '                                    <td v-if="item.id">{{item.auditName}}</td>\n' +
    '                                    <td v-if="item.id">{{item.updateDate | formatDateFilter(\'YYYY-MM-DD HH:mm:ss\')}}\n' +
    '                                    </td>\n' +
    '                                    <td v-if="item.id">{{item.user.name}}</td>\n' +
    '                                    <td v-if="item.id">{{item.result}}</td>\n' +
    '                                    <td v-if="item.id">{{item.suggest}}</td>\n' +
    '                                    <td colspan="5" v-if="!item.id" class="text-right red">\n' +
    '                                        {{item.auditName}}：{{item.result}}\n' +
    '                                    </td>\n' +
    '                                </tr><tr v-if="actYwAuditInfos.length === 0"><td colspan="5" class="text-center"><span class="empty">无审核记录</span></td></tr>\n' +
    '                                </tbody>\n' +
    '                            </table>\n' +
    '                        </div>\n' +
    '                    </e-panel>',
    props: {
        proModelId: String,
        notRequest: Boolean,
        auditList: {
            type: Array,
            default: function () {
                return []
            }
        }
    },
    data: function () {
        return {
            actYwAuditInfos: []
        }
    },
    methods: {
        getActYwAuditList: function () {
            var self = this;
            this.$axios.get('/promodel/proModel/getActYwAuditList/' + this.proModelId).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.actYwAuditInfos = data.data || []
                }
            })
        }
    },
    created: function () {
        if(!this.notRequset){
            this.getActYwAuditList();
        }else {
            this.actYwAuditInfos = this.auditList || []
        }

    }
})


//项目主体部分
Vue.component('promodel-body-pro', {
    template: '<e-panel label="项目信息">\n' +
    '                        <el-row label-width="120px">\n' +
    '                            <e-col-item label="申报日期：">{{proModel.createDate | formatDateFilter(\'YYYY-MM-DD\')}}\n' +
    '                            </e-col-item>\n' +
    '                            <e-col-item label="项目编号：">{{proModel.competitionNumber}}</e-col-item>\n' +
    '                            <e-col-item label="项目名称：">{{proModel.pName}}</e-col-item>\n' +
    '                            <e-col-item label="项目简称：">{{proModel.shortName}}</e-col-item>\n' +
    '                            <e-col-item label="项目类别：">{{proModel.proCategory | selectedFilter(proCategoryEntries)}}</e-col-item>\n' +
    '                            <e-col-item label="项目简介：" class="white-space-pre-static">{{proModel.introduction}}\n' +
    '                            </e-col-item>\n' +
    '                            <e-col-item label="项目Logo：" class="common-upload-one-img">\n' +
    '                                <div class="upload-box-border site-logo-left-size">\n' +
    '                                    <div class="avatar-uploader">\n' +
    '                                        <div tabindex="0" class="el-upload el-upload--text"><img\n' +
    '                                                :src="proModel.logoUrl | ftpHttpFilter(ftpHttp) | proGConPicFilter">\n' +
    '                                        </div>\n' +
    '                                    </div>\n' +
    '                                </div>\n' +
    '                            </e-col-item>\n' +
    '                            <e-col-item label="附件：">\n' +
    '                                <e-file-item v-if="proModel.sysAttachmentList"\n' +
    '                                             v-for="item in proModel.sysAttachmentList"\n' +
    '                                             :key="item.uid" :file="item" :show="false"></e-file-item>\n' +
    '                            </e-col-item>\n' +
    '                            <e-col-item label="团队名称：">{{proModel.team ? proModel.team.name : \'\'}}</e-col-item><slot></slot>\n' +
    '                        </el-row>\n' +
    '                    </e-panel>',
    props: {
        proModel: Object
    },
    computed: {
        proCategoryEntries: function () {
            return this.$root.proCategoryEntries
        }
    }
})


Vue.component('promodel-audit-form', {
    template: '<el-form :model="auditForm" ref="auditForm" :rules="auditFormRules" :disabled="isDisabled" size="mini" label-width="120px">' +
    '<el-form-item v-if="!isScore" prop="grade" :label="gradeLabel"><el-select v-model="auditForm.grade"><el-option v-for="option in sOptions"  :key="option[defaultProp.value]" :label="option[defaultProp.label]" :value="option[defaultProp.value]"></el-option></el-select></el-form-item>' +
    '<el-form-item v-else prop="gScore" label="评分："><el-input style="width: 150px" v-model="auditForm.gScore" maxlength="3"><span slot="append">分</span></el-input></el-form-item>' +
    '<el-form-item prop="source" :rules="sourceRules" label="建议或意见："><el-input type="textarea" :rows="3" v-model="auditForm.source" maxlength="100" placeholder="请输入建设或意见"></el-input></el-form-item></el-form>',
    props: {
        options: {
            type: Array
        },
        defaultProp: {
            type: Object,
            default: function () {
                return {
                    label: 'state',
                    value: 'status'
                }
            }
        },
        taskName: {
            required: false,
            type: String,
            default: '审核'
        },
        isScore: Boolean,
        // noPass: {
        //     type: String,
        //     default: '0'
        // },
        disabled: Boolean,
        proModel: Object,
        isFront: Boolean
    },
    computed: {
        isDisabled: function () {
            return this.disabled || this.disabledL
        },
        gradeLabel: function () {
            return this.taskName + '：'
        },
        sourceRules: {
            get: function () {
                var auditForm = this.auditForm;
                return [{
                    // required: auditForm.grade === this.noPass && !this.isScore,
                    required: true,
                    message: '请填写建议及意见',
                    trigger: 'blur'
                }]
            }
        },
        noPass: function () {
            var i = 0;
            while (i < this.sOptions.length) {
                if (this.sOptions[i][this.defaultProp.label] === '不通过') {
                    return this.sOptions[i][this.defaultProp.value]
                }
                i++;
            }
            return '0'
        },
        actParams: function () {
            return this.proModel.act;
        },
        sOptions: {
            get: function () {
                return (this.options && this.options.length > 0) ? this.options : this.defaultOptions;
            }
        },
        otherParams: function () {
            return {
                gnodeId: this.$root.gnodeId,
                actionPath: this.$root.actionPath
            }
        }
    },
    data: function () {
        var validateGScore = function (rules, value, callback) {
            if (value) {
                if ((/^[1-9]{1}\d?$|100/).test(value)) {
                    return callback();
                }
                return callback(new Error('填写0-100之间的整数'));
            }
            return callback();
        };
        return {
            auditForm: {
                id: '',
                grade: '',
                gScore: '',
                source: ''
            },
            defaultOptions: [
                {state: '通过', status: '0'},
                {state: '不通过', status: '1'}
            ],
            auditFormRules: {
                grade: [{required: true, message: '请选择审核', trigger: 'change'}],
                gScore: [{required: true, message: '请填写评分', trigger: 'blur'}, {
                    validator: validateGScore,
                    trigger: 'blur'
                }]
            },
            disabledL: false
        }
    },
    methods: {
        submitAuditForm: function () {
            var self = this;
            this.$refs.auditForm.validate(function (valid) {
                if (valid) {
                    self.saveAuditForm();
                }
            })
        },
        getParams: function () {
            this.auditForm.id = this.proModel.id;
            var auditForm = JSON.parse(JSON.stringify(this.auditForm));
            var auditFormParams = Object.toURLSearchParams(auditForm).toString();
            var otherParams = Object.toURLSearchParams(this.otherParams).toString();
            var actParams = this.getActParams();
            var params = [auditFormParams, otherParams];
            if (actParams) {
                params.push(actParams)
            }
            return params.join('&');
        },
        getActParams: function () {
            var act = this.actParams;
            var entry = {};
            for (var k in act) {
                if (act.hasOwnProperty(k)) {
                    entry['act.' + k] = act[k];
                }
            }
            return Object.toURLSearchParams(entry).toString()
        },
        saveAuditForm: function () {
            var self = this;
            self.$emit('submit-before');
            this.disabledL = true;
            var params = this.getParams();
            this.$axios({
                method: 'POST',
                url: '/promodel/proModel/saveProModelGateAudit',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                data: params
            }).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.$alert('保存成功', '提示', {
                        type: 'success',
                        cancelButtonText: '取消',
                        confirmButtonText: '确定',
                        showCancelButton: false,
                        showClose: false
                    }).then(function () {
                        var referrer = document.referrer;
                        location.href = referrer;
                    }).catch(function () {
                        location.reload();
                    });
                    if (!self.isFront) {
                        self.changeSideTodoNum(self.proModel.actYwId);
                    }
                    self.$emit('submit-success');
                } else {
                    self.$alert(data.msg, '提示', {
                        type: 'error'
                    });
                    self.disabledL = false;
                }
                self.$emit('submit-complete');
            }).catch(function () {
                self.$emit('submit-complete');
            })
        }
    }
})

Vue.component('competition-info-column-gc-school', {
    template: '<div class="project-info-column competition" :class="{\'school\': !isAdmin}">\n' +
    '    <div class="pro-pic">\n' +
    '      <a :href="to">\n' +
    '        <img :src="row.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proDefaultImg" />\n' +
    '      </a>\n' +
    '    </div>\n' +
    '    <div class="pro-info-box">\n' +
    '      <p v-show="!!row.competitionNumber" class="number">{{ row.competitionNumber }}</p>\n' +
    '      <h5 class="name">\n' +
    '        <el-tooltip class="item" effect="dark" popper-class="white" :content="row.pName" placement="bottom">\n' +
    '          <span><span v-if="!to">{{row.pName}}</span><a v-else :href="to">{{ row.pName }}</a></span>\n' +
    '        </el-tooltip>\n' +
    '      </h5>  <template><p class="area">{{ row.belongsField | getCheckboxValue(technologyFieldsEntity)}}</p><p  v-if="isAdmin" class="office-name">\n' +
    '       <span class="name-span">{{ row.officeName }}</span></p></template>\n' +
    '    </div>\n' +
    '  </div>',
    props: {
        row: {},
        to: [String, Object],
        isAdmin: {
            type: Boolean,
            default: true
        }
    },
    computed: {
        technologyFieldsEntity: function () {
            return this.$root.technologyFieldsEntity
        }
    }
})

Vue.component('project-info-column-gc', {
    template: '<div class="project-info-column">\n' +
    '    <div class="pro-pic">\n' +
    '      <a :href="to">\n' +
    '        <img :src="row.proModel.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proDefaultImg" />\n' +
    '      </a>\n' +
    '    </div>\n' +
    '    <div class="pro-info-box">\n' +
    '      <p v-show="!!row.competitionNumber" class="number">{{ row.competitionNumber }}</p>\n' +
    '      <h5 class="name">\n' +
    '        <el-tooltip class="item" effect="dark" popper-class="white" :content="row.proModel.pName" placement="bottom">\n' +
    '          <a :href="to">\n' +
    '            {{ row.proModel.pName }}\n' +
    '          </a>\n' +
    '        </el-tooltip>\n' +
    '      </h5>  <p class="area">\n' +
    '        {{ row.proModel.belongsField | getSelectedVal(technologyFieldsEntity)}}\n' +
    '      </p>\n' +
    '      <p class="office-name"><span class="name-span">{{ row.proModel.schoolName }}</span\n' +
    '        ><span class="office-type">（{{ row.proModel.schoolType | getSelectedVal(schoolTypeEntity, {label:\'name\', value: \'key\'})}}）</span>\n' +
    '      </p>\n' +
    '    </div>\n' +
    '  </div>',
    props: {
        row: {
            proModel: {}
        },
        to: [String, Object]
    },
    computed: {
        technologyFieldsEntity: function () {
            return this.$root.technologyFieldsEntity
        },
        schoolTypeEntity: function () {
            return this.$root.schoolTypeEntity
        }
    }
})


Vue.component('project-info-column-gc-school', {
    template: '<div class="project-info-column">\n' +
    '    <div class="pro-pic">\n' +
    '      <a :href="to">\n' +
    '        <img :src="row.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proDefaultImg" />\n' +
    '      </a>\n' +
    '    </div>\n' +
    '    <div class="pro-info-box">\n' +
    '      <p v-show="!!row.competitionNumber" class="number">{{ row.competitionNumber }}</p>\n' +
    '      <h5 class="name">\n' +
    '        <el-tooltip class="item" effect="dark" popper-class="white" :content="row.pName" placement="bottom">\n' +
    '        <span><span v-if="!to">{{row.pName}}</span><a v-else :href="to">{{ row.pName }}</a></span></el-tooltip>\n' +
    '      </h5> ' +
    '      <p class="office-name">\n' +
    '        <template v-if="!isSchool"><span class="name-span">{{ row.schoolName }}</span>' +
    '     <span class="office-type">（{{row.officeName}}） </span></template><template v-else><span>{{row.officeName}}</span></template>\n' +
    '      </p>\n' +
    '    </div>\n' +
    '  </div>',
    props: {
        row: {
            proModel: {}
        },
        to: [String, Object],
        isSchool: Boolean
    }
})

Vue.component('project-info-column-gc-school-front', {
    template: '<div class="project-info-column">\n' +
    '    <div class="pro-pic">\n' +
    '      <a :href="to">\n' +
    '        <img :src="row.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proDefaultImg" />\n' +
    '      </a>\n' +
    '    </div>\n' +
    '    <div class="pro-info-box">\n' +
    '      <p v-show="!!row.number" class="number">{{ row.number }}</p>\n' +
    '      <h5 class="name">\n' +
    '        <el-tooltip class="item" effect="dark" popper-class="white" :content="row.project_name" placement="bottom">\n' +
    '        <span><span v-if="!to">{{row.project_name}}</span><a v-else :href="to">{{ row.project_name }}</a></span></el-tooltip>\n' +
    '      </h5> ' +
    '      <p class="office-name">\n' +
    '        <span class="name-span">{{ row.type }}</span></p><p>' +
    '     <span class="office-type">{{row.apply_time}} </span>\n' +
    '      </p>\n' +
    '    </div>\n' +
    '  </div>',
    props: {
        row: {
            proModel: {}
        },
        to: [String, Object],
        isSchool: Boolean
    }
})

Vue.component('project-team-column', {
    template: '<div class="project-team-column"> <p>负责人：{{ row.deuser.name }}</p> <p v-if="row.team && row.team.entName">组成员：<el-tooltip class="item"  effect="dark" popper-class="white" :content="row.team.entName" placement="bottom"><span>{{row.team.entName}}</span></el-tooltip> </p> <p v-if="row.team && row.team.uName">导师：<span >{{row.team.uName}}</span> </p> </div>',
    props: {
        row: {}
    }
})






