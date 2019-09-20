/**
 * Created by Administrator on 2019/1/10.
 */
'use strict';


Vue.component('applicant-info-credit', {
    template: '<div v-loading="loading" class="applicant-info applicant-info-credit">\n' +
    '                <div class="user-card-photo">\n' +
    '                    <img :src="user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter">' +
    '               </div>\n' +
    '                <div class="user-intros">\n' +
    '                    <el-row :gutter="8">\n' +
    '                        <el-col :span="3" style="min-height: 1px">\n' +
    '                            <div class="user-name-sex">\n' +
    '                                <span class="name">{{user.name}}</span>\n' +
    '                                <i class="iconfont" :class="sexClassName"></i>\n' +
    '                            </div>\n' +
    '                        </el-col>\n' +
    '                        <el-col :span="21" style="min-height: 1px">\n' +
    '                            <div class="user-intro-items">\n' +
    '                                <span class="user-intro-item"><i class="el-icon-date"></i>{{user.birthday | formatDateFilter(\'YYYY-MM\')}}</span>\n' +
    '                                <span class="user-intro-item"><i class="iconfont icon-renshixuexiao"></i>{{user.officeName}}<template v-if="user.professional">/{{user.professional}}</template><template v-if="!!student.tClass">{{student.tClass}}班</template></span>' +
    '                                <span class="user-intro-item"><i class="iconfont icon-yooxi"></i>{{user.no}}</span>\n' +
    '                            </div>\n' +
    '                            <div class="user-intro-items">\n' +
    '                                <span class="user-intro-item"><i class="iconfont icon-unie64f"></i>{{user.mobile}}</span>\n' +
    '                                <span class="user-intro-item"><i class="iconfont icon-noread"></i>{{user.email}}</span>\n' +
    '                            </div>\n' +
    '                        </el-col>\n' +
    '                    </el-row>\n' +
    '                </div>\n' +
    '            </div>',
    props: {
        user: {
            type: Object,
            required: true
        },
        student: {
            type: Object,
            default: function () {
                return {
                    tClass: ''
                }
            }
        },
        loading: {
            type: Boolean,
            default: false
        }
    },
    computed: {
        sexClassName: function () {
            return {
                'icon-xingbie-nan': this.user.sex === '1',
                'icon-xingbie-nv': this.user.sex !== '1'
            }
        }
    }
});


Vue.component('credit-apply-tab-view', {
    template: '<e-panel label="申请认定学分"><div v-if="creditApply.rdetail"><e-col-item label-width="110px" label="申请日期：">{{creditApply.applyDate | formatDateFilter(\'YYYY-MM-DD\')}}</e-col-item>' +
    '<e-col-item label-width="110px" label="学分类别："><span>{{creditApply.rule.name}}</span></e-col-item>' +
    '<e-col-item  label-width="110px" label="认定内容及标准：">{{contentNames}}<div>{{creditApply.rdetail.name}}</div></e-col-item>' +
    '<e-col-item label-width="110px" label="认定学分："><template v-if="total != \'\'">{{total}}分</template></e-col-item>' +
    '<e-col-item v-if="creditApply.rdetail.isCondcheck == \'1\'" label-width="110px" label="实际分值：">{{creditApply.condVal}}分</e-col-item>' +
    '<e-col-item v-if="creditApply.scoRapplyCert" label-width="110px" label="证书名称：">{{creditApply.scoRapplyCert.name}}</e-col-item>' +
    '<e-col-item v-if="creditApply.name" label-width="110px" label="名称：">{{creditApply.name}}</e-col-item>' +
    '<e-col-item v-if="creditApply.scoRapplyMemberList" label-width="110px" label="认定组成员："><div class="table-container update-member-box">' +
    '<el-table :data="memberList" size="mini" class="table">' +
    '<el-table-column label="姓名"  align="center" width="210"><template slot-scope="scope"><div class="user-info-box"><span class="user-pic"><img :src="scope.row.user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter"></span>{{scope.row.user.name}}</div></template></el-table-column>' +
    '<el-table-column label="所属学院/专业" align="center"><template slot-scope="scope"><div>{{scope.row.user.office || scope.row.user.officeName}}<br/>{{scope.row.user.professional}}</div></template></el-table-column>' +
    '<el-table-column  prop="user.mobile" label="联系电话" align="center"><template slot-scope="scope">{{scope.row.user.mobile}}</template></el-table-column>' +
    '<el-table-column  prop="user.email" label="电子邮箱" align="center"><template slot-scope="scope">{{scope.row.user.email}}</template></el-table-column>' +
    '<el-table-column label="团队职责" align="center"><template slot-scope="scope"><span v-if="scope.row.user.id == declareId">负责人</span><span v-else>组成员</span></template></el-table-column>' +
    '<el-table-column :prop="rateKey" label="学分配比" align="center"></el-table-column>' +
    '<el-table-column  v-if="hasScore" label="学分" align="center"><template slot-scope="scope">{{scope.row.scoRapplyPb ? scope.row.scoRapplyPb.score : \'\'}}</template></el-table-column></el-table></div></e-col-item>' +
    '<e-col-item label-width="110px" label="证明材料："><e-file-item v-if="creditApply.sysAttachmentList" v-for="item in creditApply.sysAttachmentList" :key="item.uid" :file="item" :show="false"></e-file-item></e-col-item><e-col-item label-width="110px" label="备注：" class="white-space-pre-static">{{creditApply.remarks}}</e-col-item></div></e-panel>',
    props: {
        creditApply: Object,
        default: function () {
            return {
                id: '',
                applyDate: '',
                rule: {
                    name: '',
                },
                rdetail: {
                    name: '',
                    isCondcheck: ''
                },
                sysAttachmentList: [],
                remarks: ''
            }
        },
        loading: Boolean
    },
    computed: {
        declareId: function () {
            return this.creditApply.user ? this.creditApply.user.id : '';
        },
        rateKey: function () {
            return this.hasScore ? 'scoRapplyPb.val' : 'rate';
        },
        hasScore: function () {
            return this.creditApply.status === '3'
        },
        scoRuleListEntries: function () {
            return this.$root.scoRuleListEntries;
        },
        contentNames: function () {
            var creditApply = this.creditApply;
            var rdetail = creditApply.rdetail;
            var parentIds = rdetail.rule.parentIds;
            var scoRuleListEntries = this.scoRuleListEntries;
            var contentNames = '';
            if (parentIds) {
                parentIds += rdetail.rule.id;
                parentIds = parentIds.split(',');
                parentIds = parentIds.slice(3);
                var names = [];
                parentIds.forEach(function (id) {
                    if (scoRuleListEntries && scoRuleListEntries[id]) {
                        contentNames += scoRuleListEntries[id] + ' ';
                        names.push(scoRuleListEntries[id])
                    }
                })
            }
            return contentNames;
        }
    },
    data: function () {
        return {
            total: '',
            memberList: []
        }
    },
    watch: {
        'creditApply.id': function (value) {
            if (value) {
                this.total = parseFloat(this.creditApply.rdetail.score);
                this.getScoSum();
                var scoRapplyMemberList = this.creditApply.scoRapplyMemberList;
                var scoreList = [];
                var pbList = [];
                var pbTotal = 0;
                var scoreTotal = 0;
                var isSame = true;
                if (this.hasScore  && scoRapplyMemberList && scoRapplyMemberList.length > 0) {
                    for (var i = 0; i < scoRapplyMemberList.length; i++) {
                        var itemSco = scoRapplyMemberList[i];
                        scoreList.push(parseFloat(itemSco.scoRapplyPb.score));
                        pbList.push(parseFloat(itemSco.scoRapplyPb.val))
                    }
                    pbTotal = pbList.reduce(function (item1, item2) {
                        return item1 + item2;
                    }, 0);
                    scoreTotal = scoreList.reduce(function (item1, item2) {
                        return item1 + item2;
                    }, 0);
                    scoreList = scoreList.map(function (item) {
                        return item/scoreTotal
                    });
                    pbList = pbList.map(function (item) {
                        return item/pbTotal
                    });
                    for(var j = 0; j < scoreList.length; j++){
                        if(scoreList[j] !== pbList[j]){
                            isSame = false;
                            break;
                        }
                    }
                    if(!isSame){
                        this.memberList = scoRapplyMemberList.map(function (item) {
                            item.scoRapplyPb.val = '-';
                            return item;
                        })
                    }else {
                        this.memberList = scoRapplyMemberList
                    }
                }else {
                    this.memberList = scoRapplyMemberList
                }
            }
        }
    },
    methods: {
        getScoSum: function () {
            var self = this;
            this.$axios.get('/scr/scoCreditQuery/ajaxupdateValList?apply.id=' + this.creditApply.id).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    if (data.data) {
                        var list = data.data || [];
                        self.total = list.reduce(function (total, item) {
                            return total + parseFloat(item.val);
                        }, 0);
                    }
                }
            })
        }
    },
})


Vue.component('credit-audit-list', {
    template: '<e-panel label="审核记录">\n' +
    '                            <div class="table-container"><el-table :data="auditedList" border size="small">\n' +
    '                                <el-table-column label="审核人">\n' +
    '                                    <template slot-scope="scope">{{scope.row.autBy ? scope.row.autBy.name : \'\'}}</template>\n' +
    '                                </el-table-column>' +
    '                           <el-table-column label="审核结果" align="center"><template slot-scope="scope">{{scope.row.status | selectedFilter(auditStatueEntries)}}</template></el-table-column>' +
    '                           <el-table-column label="建议及意见" align="center"><template slot-scope="scope">{{scope.row.remarks}}</template></el-table-column>' +
    '                           <el-table-column label="审核时间" align="center"><template slot-scope="scope">{{scope.row.createDate}}</template></el-table-column>\n' +
    '                            </el-table></div>\n' +
    '                        </e-panel>',
    props: {
        creditId: String,
        type: String,
        isAdmin: Boolean
    },
    data: function () {
        return {
            auditedList: []
        }
    },
    computed: {
        auditStatueEntries: function () {
            return this.$root.auditStatueEntries
        },
        ajaxUrl: function () {
            return this.isAdmin ? '/scr/scoRapply/ajaxFindScoAuditResult' : '/scr/ajaxFindScoAuditResult'
        }
    },
    methods: {
        getScoAuditedList: function () {
            var self = this;
            this.$axios.get(this.ajaxUrl + '?id=' + this.creditId + '&creditType=' + this.type).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.auditedList = data.data || [];
                } else {
                    self.$message.error(data.msg);
                }
            }).catch(function () {
                self.$message.error(self.xhrErrorMsg)
            })
        }
    },
    created: function () {
        this.getScoAuditedList();
    }
})


Vue.component('credit-course-apply', {
    template: '<e-panel label="申请认定学分">' +
    '<e-col-item label-width="110px" label="实际学时：">{{apply.realTime}}课时</e-col-item>' +
    '<e-col-item label-width="110px" label="实际成绩：">{{apply.realScore}}分</e-col-item>' +
    '<e-col-item label-width="110px" label="上传成绩单："><e-file-item v-if="apply.attachmentList" v-for="item in apply.attachmentList" :key="item.uid" :file="item" :show="false"></e-file-item></e-col-item>' +
    '<e-col-item label-width="110px" label="认定理由：" class="white-space-pre-static">{{apply.remarks}}</e-col-item></e-panel>',
    props: {
        apply: Object
    }
})

Vue.component('credit-course-info', {
    template: '<e-panel label="课程信息">' +
    ' <el-row :gutter="15" label-width="110px"><el-col :span="8"><e-col-item label-width="110px" label="课程名称：">{{course.name}}</e-col-item></el-col>' +
    '<el-col :span="8"><e-col-item label="课程代码：">{{course.code}}</e-col-item> </el-col>' +
    '<el-col :span="8"><e-col-item label="课程性质：">{{course.nature | selectedFilter(natureEntries)}} </e-col-item></el-col>' +
    '<el-col :span="8"><e-col-item  label="计划课时：">{{course.planTime}}</e-col-item></el-col>' +
    '<el-col :span="8"><e-col-item label="合格成绩：">{{course.overScore}}分及以上</e-col-item></el-col>' +
    '<el-col :span="8"><e-col-item  label="计划学分：">{{course.planScore}}</e-col-item></el-col></el-row></e-panel>',
    props: {
        course: Object
    },
    data: function () {
        return {
            scoCourseNatures: []
        }
    },
    computed: {
        natureEntries: function () {
            return this.getEntries(this.scoCourseNatures)
        }
    },
    methods: {
        getNatures: function () {
            var self = this;
            this.$axios.get('/sys/dict/getDictList?type=0000000108').then(function (response) {
                self.scoCourseNatures = response.data || [];
            })
        },
    },
    created: function () {
        this.getNatures();
    }
})

Vue.component('credit-course-audit', {
    template: '<e-panel label="审核记录"><div class="table-container"><el-table :data="auditList" size="mini"><el-table-column prop="user.name" label="审核人"></el-table-column><el-table-column prop="scoreVal" label="准予认定学分"></el-table-column><el-table-column prop="auditStatus" label="审核状态"><template slot-scope="scope">{{scope.row.auditStatus | selectedFilter(auditStatueEntries)}}</template></el-table-column><el-table-column prop="suggest" label="建议及意见"><template slot-scope="scope">{{scope.row.suggest}}</template></el-table-column><el-table-column prop="updateDate" label="审核时间"><template slot-scope="scope">{{scope.row.updateDate | formatDateFilter(\'YYYY-MM-DD\')}}</template></el-table-column></el-table></div></e-panel>',
    props: {
        id: String
    },
    data: function () {
        return {
            auditList: []
        }
    },
    computed: {
        auditStatueEntries: function () {
            return this.$root.auditStatueEntries;
        }
    },
    methods: {
        getAuditList: function () {
            var self = this;
            this.$axios.post('/scoapply/getCAAuditList/' + this.id).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.auditList = data.data || [];
                }
            }).catch(function () {

            })
        }
    },
    created: function () {
        this.getAuditList();
    }
})