'use strict';



Vue.component('applicant-info', {
    template: '<div v-loading="loading" class="applicant-info">\n' +
    '                <div class="user-card-photo">\n' +
    '                    <template v-if="!isBg"><img :src="pwEnter.declarePhoto | ftpHttpFilter(ftpHttp) | cardPhoto"></template>\n' +
    '                <template v-else><div class="card-photo common-upload-one-img"><div class="upload-box-border site-cover-size">' +
    '                   <el-upload :disabled="disabled" class="avatar-uploader" :action="\' + ctxFront + \'/ftp/ueditorUpload/uploadTemp?folder=pwEnter\'" :show-file-list="false"  :on-success="idPhotoSuccess" :on-error="idPhotoError"  name="upfile" accept="image/jpg, image/jpeg, image/png">' +
    '                       <img :src="pwEnter.declarePhoto | ftpHttpFilter(ftpHttp) | cardPhoto"></el-upload>' +
    '                       </div></div></template></div>\n' +
    '                <div class="user-intros">\n' +
    '                    <el-row :gutter="8">\n' +
    '                        <el-col :span="3">\n' +
    '                            <div class="user-name-sex">\n' +
    '                                <span class="name">{{applicant.name}}</span>\n' +
    '                                <i class="iconfont" :class="sexClassName"></i>\n' +
    '                            </div>\n' +
    '                        </el-col>\n' +
    '                        <el-col :span="21">\n' +
    '                            <div class="user-intro-items">\n' +
    '                                <span class="user-intro-item"><i class="el-icon-date"></i>{{applicant.birthday | formatDateFilter(\'YYYY-MM\')}}</span>\n' +
    '                                <span class="user-intro-item"><i class="iconfont icon-renshixuexiao"></i>' +
    '                               <template v-if="applicant.officeName">{{applicant.officeName}}</template><template v-if="applicant.professional">/{{applicant.professional | selectedFilter(officeEntries)}}</template></span>\n' +
    '                                <span class="user-intro-item"><i class="iconfont icon-yooxi"></i>{{applicant.no}}</span>\n' +
    '                                <span class="user-intro-item" style="width: 100px;"><i class="iconfont icon-dingwei"></i>{{applicant.residence}}</span>\n' +
    '                            </div>\n' +
    '                            <div class="user-intro-items">\n' +
    '                                <span class="user-intro-item"><i class="iconfont icon-unie64f"></i>{{applicant.mobile}}</span>\n' +
    '                                <span class="user-intro-item"><i class="iconfont icon-noread"></i>{{applicant.email}}</span>\n' +
    '                                <span class="user-intro-item"><i\n' +
    '                                        class="iconfont icon-shenfenzheng"></i>{{applicant.idNumber}}</span>\n' +
    '                            </div>\n' +
    '                            <div class="user-intro-items">\n' +
    '                                <span><i class="iconfont" :class="pwEnterTypeClass"></i>{{pwEnter.type | selectedFilter(pwEnterTypeEntries)}}</span>\n' +
    '                            </div>\n' +
    '                        </el-col>\n' +
    '                    </el-row>\n' +
    '                    <el-row :gutter="8">\n' +
    '                        <el-col :span="3" class="text-right">\n' +
    '                            <span class="text-second-color">个人简介：</span>\n' +
    '                        </el-col>\n' +
    '                        <el-col :span="21">\n' +
    '                            <div class="white-space-pre user-intro">{{applicant.introduction}}</div>\n' +
    '                        </el-col>\n' +
    '                    </el-row>\n' +
    '                </div>\n' +
    '            </div>',
    props: {
        pwEnterId: {
            type: String,
            required: true
        },
        isBg:  {
            type: Boolean,
            default: false
        }
    },
    mixins: [Vue.pwEnterMixin],
    data: function () {
        return {
            pwEnter: {},
            applicant: {},
            loading: false,
            disabled: false
        }
    },
    computed: {
        sexClassName: function () {
            return {
                'icon-xingbie-nan': this.applicant.sex === '1',
                'icon-xingbie-nv': this.applicant.sex !== '1'
            }
        },
        pwEnterTypeClass: function () {
            var name;
            if (this.pwEnter.type && this.pwEnterTypeEntries) {
                name = this.pwEnterTypeEntries[this.pwEnter.type]
            }
            if (name) {
                return {
                    'icon-tuandui': name.indexOf('团') > -1,
                    'icon-qi': name.indexOf('企') > -1
                }
            }
            return '';
        }
    },
    methods: {
        getApplicantInfo: function () {
            var self = this;
            this.loading = true;
            this.$axios.get('/pw/pwEnter/ajaxPwEnter?id=' + this.pwEnterId).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.pwEnter = data.data || {};
                    self.applicant = self.pwEnter.applicant || {};
                } else {
                    self.$message.error(data.msg)
                }
                self.loading = false;
            }).catch(function (error) {
                self.$message({
                    type: 'error',
                    message: self.xhrErrorMsg
                });
                self.loading = false;
            })
        },
        idPhotoSuccess: function (response, file) {
            if (response.state === 'SUCCESS') {
                this.pwEnter.declarePhoto = response.ftpUrl;
                this.$emit('change-photo', response.ftpUrl)
                return
            }
            this.$message.error("登记照上传失败，请重新上传")
        },

        idPhotoError: function (error) {
            this.$message.error(this.xhrErrorMsg)
        },
    },
    created: function () {
        this.getApplicantInfo();
    }
});


Vue.component('pw-enter-company', {
    template: '<e-panel label="企业信息">\n' +
    '                        <e-col-item label-width="96px" label="企业名称：">{{pwCompany.name}}</e-col-item>\n' +
    '                        <e-col-item label-width="96px" label="工商注册号：">{{pwCompany.no}}</e-col-item>\n' +
    '                        <e-col-item label-width="96px" label="注册资金：">￥{{pwCompany.regMoney}}万元</e-col-item>\n' +
    '                        <e-col-item label-width="96px" label="资金来源：">{{pwCompany.regMtypes | checkboxFilter(regMTypeEntries)}}</e-col-item>\n' +
    '                        <e-col-item label-width="96px" label="注册地址：">{{pwCompany.address}}</e-col-item>\n' +
    '                        <e-col-item label-width="96px" class="white-space-pre-static" label="公司主营业务：">{{pwCompany.remarks}}</e-col-item>\n' +
    '                        <e-col-item label-width="96px" label="附件：">' +
    '                           <e-file-item v-if="pwCompany.sysAttachmentList" v-for="item in pwCompany.sysAttachmentList" :key="item.uid" :file="item" size="mini" :show="false"></e-file-item>' +
    '                       </e-col-item>\n' +
    '                    </e-panel>',
    props: {
        pwEnterId: {
            type: String,
            required: true
        },
        companyId: String
    },
    mixins: [Vue.pwEnterMixin],
    data: function () {
        return {
            pwCompany: {},
            loading: false
        }
    },
    methods: {
        getPwEnterCompany: function () {
            var self = this;
            this.loading = true;
            this.$axios.get('/pw/pwEnter/ajaxFindPwEnterCompany?pwEnterId=' + this.pwEnterId).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    var pageData = data.data || {};
                    var pwCompany = pageData.pwCompany;
                    pwCompany.sysAttachmentList = pwCompany.fileInfo;
                    delete  pwCompany.fileInfo;
                    self.pwCompany = pwCompany
                }else {
                    self.$message.error(data.msg)
                    self.loading = false;
                }

            }).catch(function (error) {
                self.$message.error(self.xhrErrorMsg)
                self.loading = false;
            })
        }
    },
    created: function () {
        this.getPwEnterCompany();
    }
});


Vue.component('pw-enter-team', {
    template: '<div class="pw-enter-team-panels" v-loading="loading"><e-panel label="团队信息">\n' +
    '                        <e-col-item label-width="96px" label="团队名称：">{{team.name}}</e-col-item>\n' +
    '                        <e-col-item class="white-space-pre-static" label-width="96px" label="团队介绍：">{{team.summary}}</e-col-item>\n' +
    '                    </e-panel><e-panel label="团队成员"><team-student-list :team-student="studentList" :leader="leader"></team-student-list></e-panel>' +
    '               <e-panel label="指导老师"><team-teacher-list :team-teacher="teacherList" :leader="leader"></team-teacher-list></e-panel></div>',
    props: {
        pwEnterId: {
            type: String,
            required: true
        },
        teamId: String
    },
    data: function () {
        return {
            team: {},
            studentList: [],
            teacherList: [],
            leader: {},
            declareId: '',
            loading: false
        }
    },
    methods: {
        getPwEnterTeam: function () {
            var self = this;
            this.loading = true;
            this.$axios.get('/pw/pwEnter/ajaxFindPwEnterTeam?pwEnterId='+this.pwEnterId).then(function (response) {
                var data = response.data;
                var pageData;
                if (data.status === 1) {
                    pageData = data.data || {};
                    self.team = pageData.team || {};
                    self.declareId = self.team.sponsor;
                    self.leader.id = self.declareId;
                    self.studentList = pageData.stus || [];
                    self.teacherList = pageData.teas || [];
                    // self.getMember();
                } else {
                    self.$message.error(data.msg)
                }
                self.loading = false;
            }).catch(function (error) {
                self.$message.error(self.xhrErrorMsg)
                self.loading = false;
            })
        },
        getMember: function () {
            var self = this;
            this.$axios.get('/team/findTeamPerson?teamId=' + self.team.id).then(function (response) {
                var data = response.data || {};
                if (data.status === 1) {
                    data = data.data;
                    var stuList = data.stuList || [];
                    self.studentList = stuList.map(function (item) {
                        item['userzz'] = item.userId === self.declareId ? '1' : '0';
                        return item;
                    });
                    self.teacherList = data.teaList || [];
                }
            })
        },
    },
    created: function () {
        this.getPwEnterTeam();
    }
})

Vue.component('pw-enter-project-view', {
    template: '<div v-loading="loading"><e-panel v-for="item in projectList" :key="item.id" :label="item.name">' +
    '<e-col-item class="white-space-pre-static" label-width="96px" label="项目简介：">{{item.remarks}}</e-col-item>' +
    '<e-col-item label-width="96px" label="附件："><e-file-item v-if="item.sysAttachmentList && item.sysAttachmentList.length > 0" v-for="file in item.sysAttachmentList" :key="file.uid" :file="file" size="mini" :show="false"></e-file-item>' +
    '</e-col-item></e-panel></div>',
    props: {
        pwEnterId: String
    },
    data: function () {
      return {
          loading: false,
          projectList: []
      }
    },
    methods: {
        getProjectList: function () {
            var self = this;
            this.loading = true;
            this.$axios.get('/pw/pwEnter/ajaxFindPwEnterProjects?pwEnterId=' + this.pwEnterId).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    var pageData = data.data || {};
                    var pwProjectList = pageData.pwProjectList || [];
                    self.projectList = pwProjectList.map(function (item) {
                        return {
                            id: item.id,
                            // eid: item.id,
                            name: item.name,
                            remarks: item.remarks,
                            sysAttachmentList: item.fileInfo || []
                        }
                    });
                }else {
                    self.$message.error(data.msg)
                }
                self.loading = false;

            }).catch(function (error) {
                self.loading = false;
                self.$message.error(self.xhrErrorMsg)
            })
        },
    },
    created: function () {
        this.getProjectList();
    }
})

Vue.component('pw-enter-project', {
    template: '<div v-loading="loading" class="pw-project-list-panels">\n' +
    '                        <e-panel v-if="projectUnAuditedList.length > 0" label="待审核项目">\n' +
    '                            <div v-for="item in projectUnAuditedList" :key="item.id" class="pw-projects-unaudited">\n' +
    '                                <e-col-item label-width="96px" label="项目名称：">{{item.name}}</e-col-item>\n' +
    '                                <e-col-item class="white-space-pre-static" label-width="96px" label="项目简介：">{{item.remarks}}</e-col-item>\n' +
    '                                <e-col-item label-width="96px" label="附件：">' +
    '                               <e-file-item v-if="item.sysAttachmentList.length > 0" v-for="item in item.sysAttachmentList" :key="item.uid" :file="item" size="mini" :show="false"></e-file-item></e-col-item>\n' +
    '                            </div>\n' +
    '                        </e-panel>\n' +
    '                        <e-panel label="已入驻项目">\n' +
    '                            <div v-if="projectAuditedList.length > 0" v-for="item in projectAuditedList" :key="item.id"  class="pw-projects-unaudited">\n' +
    '                                <e-col-item label-width="96px" label="项目名称：">{{item.name}}</e-col-item>\n' +
    '                                <e-col-item class="white-space-pre-static" label-width="96px" label="项目简介：">{{item.remarks}}</e-col-item>\n' +
    '                                <e-col-item label-width="96px" label="附件：">' +
    '                                   <e-file-item v-if="item.sysAttachmentList.length > 0" v-for="item in item.sysAttachmentList" :key="item.uid" :file="item" size="mini" :show="false"></e-file-item></e-col-item>\n' +
    '                            </div><div v-if="projectAuditedList.length === 0" class="empty">还未入驻项目，或者入驻的项目待审核</div>\n' +
    '                        </e-panel>\n' +
    '                    </div>',
    props: {
        pwEnterId: {
            type: String,
            required: true
        }
    },
    data: function () {
        return {
            pwProjectList: [],
            loading: false
        }
    },
    computed: {
        projectAuditedList: function () {
            return this.pwProjectList.filter(function (item) {
                return item.audited === '1';
            })
        },
        projectUnAuditedList: function () {
            return this.pwProjectList.filter(function (item) {
                return item.audited !== '1';
            })
        }
    },
    methods: {
        getPwProjectList: function () {
            var self = this;
            this.loading = true;
            this.$axios.post('/pw/pwEnter/ajaxProject', {id: this.pwEnterId}).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.pwProjectList = data.data || [];
                } else {
                    self.$message.error(data.msg)
                }
                self.loading = false;
            }).catch(function (error) {
                self.$message.error(self.xhrErrorMsg);
                self.loading = false;
            })
        }
    },
    created: function () {
        this.getPwProjectList();
    }
})

Vue.component('pw-enter-site', {
    template: '<e-panel v-loading="loading" label="场地要求">\n' +
    '                    <e-col-item label-width="96px" label="所需工位：">{{expectWorkNum}}</e-col-item>\n' +
    '                    <e-col-item label-width="96px" label="预计孵化期：">{{expectBornDate}}</e-col-item>\n' +
    '                    <e-col-item class="white-space-pre-static" label-width="96px" label="备注：">{{pwEnter.expectRemark}}</e-col-item>' +
    '                </e-panel>',
    props: {
        pwEnterId: {
            type: String,
            required: true
        }
    },
    data: function () {
      return {
          pwEnter: {},
          loading: false
      }
    },
    computed: {
        expectWorkNum: function () {
            var isShowWorkNum = this.pwEnter.isShowWorkNum;
            var expectWorkNum = this.pwEnter.expectWorkNum;
            if(isShowWorkNum === '1'){
                return (expectWorkNum)+'位'
            }
            return '无'
        },
        expectBornDate: function () {
            var startDate = this.pwEnter.startDate;
            if(!startDate) return '';
            var date = new Date(moment(startDate).format());
            var days = date.getDate();
            date.setDate(days + parseFloat(this.pwEnter.expectTerm));
            return moment(date).format('YYYY-MM-DD')
        }
    },
    methods: {
        getPwEnterSite: function () {
            var self = this;
            this.loading = true;
            this.$axios.get('/pw/pwEnter/ajaxFindPwEnterPwSpace?pwEnterId='+this.pwEnterId).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.pwEnter = data.data || {};
                } else {
                    self.$message.error(data.msg)
                }
                self.loading = false;
            }).catch(function (error) {
                self.loading = false;
                self.$message.error(self.xhrErrorMsg);
            })
        }
    },
    created: function () {
        this.getPwEnterSite();
    }
})


Vue.component('pw-enter-record-list', {
    template: '<e-panel v-loading="loading" label="入驻记录"><ul v-if="recordList.length > 0" class="timeline">\n' +
    '                        <li class="work" v-for="item in recordList" :key="item.id">\n' +
    '                            <span class="contest-date">{{item.createDate | formatDateFilter(\'YYYY-MM-DD HH:mm\')}}</span>\n' +
    '                            <img :src="ctxImages + \'/time-line.png\'" alt="">\n' +
    '                            <div class="relative">\n' +
    '                                <span>{{item.typeString}}</span>\n' +
    '                            </div>\n' +
    '                        </li>\n' +
    '                    </ul><div class="empty" v-if="recordList.length === 0">暂无入驻记录</div></e-panel>',
    props: {
      pwEnterId: {
          type: String,
          required: true
      }
    },
    data: function () {
      return {
          recordList: [],
          loading: false
      }
    },
    methods: {
        getRecordList: function () {
            var self = this;
            this.$axios.post('/pw/pwApplyRecord/ajaxFindAppList', {eid: this.pwEnterId}).then(function (response) {
                var data = response.data;
                if(data.status === 1){
                    var recordList = data.data || [];
                    recordList = recordList.sort(function (item1, item2) {
                        var time1 = moment(item1.createDate).valueOf();
                        var time2 = moment(item2.createDate).valueOf();
                        if (time1 > time2) {
                            return 1
                        } else if (time1 < time2) {
                            return -1
                        } else {
                            return 0
                        }
                    });
                    self.recordList = recordList
                }else {
                    self.$message.error(data.msg)
                }
                self.loading = false;
            }).catch(function (error) {
                self.loading = false;
                self.$message.error(data.msg)
            })
        }
    },
    mounted: function () {
        if(this.pwEnterId){
            this.getRecordList();
        }
    }
});

Vue.component('pw-enter-result-list', {
    template: '<e-panel v-loading="loading" label="成果记录"><ul v-if="resultList.length > 0" class="timeline">\n' +
    '                        <li class="work" v-for="(list, key) in resultGroups" >\n' +
    '                            <span class="contest-date">{{key}}</span>\n' +
    '                            <img :src="ctxImages + \'/time-line.png\'" alt="">\n' +
    '                            <div class="relative">\n' +
    '                                <e-file-item v-for="file in list" :key="file.uid" :file="file" size="mini" :show="false"></e-file-item>\n' +
    '                            </div>\n' +
    '                        </li>\n' +
    '                    </ul><div class="empty" v-if="resultList.length === 0">暂无成果记录</div></e-panel>',
    props: {
        pwEnterId: {
            type: String,
            required: true
        }
    },
    data: function () {
        return {
            resultList: [],
            loading: false
        }
    },
    computed: {
      resultGroups: function () {
          var dateGroups = {};
          var resultList = this.resultList;
          var i = 0;
          while (i < resultList.length){
              var item = resultList[i];
              var createDate = moment(item.createDate).format('YYYY-MM-DD');
              if(!dateGroups[createDate]){
                  dateGroups[createDate] = [];
              }
              dateGroups[createDate].push(item);
              i++;
          }
          return dateGroups;
      }
    },
    methods: {
        getResultList: function () {
            var self = this;
            this.$axios.get('/pw/pwEnter/ajaxFindPwEnterResultList?pwEnterId='+this.pwEnterId).then(function (response) {
                var data = response.data;
                if(data.status === 1){
                    data = data.data || {};
                    self.resultList = data.sysAttachmentList || [];
                }
                self.loading = false;
            }).catch(function (error) {
                self.loading = false;
                self.$message.error(self.msg)
            })
        }
    },
    mounted: function () {
        if(this.pwEnterId){
            this.getResultList();
        }
    }
});

Vue.component('pw-enter-result', {
    template: '<div class="pw-enter-audited-result">\n' +
    '                        <e-panel label="入驻审核">\n' +
    '                            <div class="table-container"><el-table :data="auditedList" border size="small" empty-text="未审核">\n' +
    '                                <el-table-column label="审核人">\n' +
    '                                    <template slot-scope="scope">{{scope.row.auditName}}</template>\n' +
    '                                </el-table-column>' +
    '                           <el-table-column label="审核结果" align="center"><template slot-scope="scope">{{scope.row.status | selectedFilter(pwEnterStatusEntries)}}</template></el-table-column>' +
    '                           <el-table-column label="建议及意见" align="center"><template slot-scope="scope">{{scope.row.remarks}}</template></el-table-column>' +
    '                           <el-table-column label="入驻有效期" align="center"><template slot-scope="scope">{{scope.row.startDate | formatDateFilter(\'YYYY-MM-DD\')}}至{{scope.row.endDate |formatDateFilter(\'YYYY-MM-DD\')}}</template></el-table-column>' +
    '                           <el-table-column label="审核时间" align="center"><template slot-scope="scope">{{scope.row.createDate}}</template></el-table-column>\n' +
    '                            </el-table></div>\n' +
    '                        </e-panel>\n' +
    '                    </div>',
    props: {
        pwEnter: {
            type: Object,
            required: true
        }
    },
    data: function () {
        return {
            pwEnterAssignPlaceList: [],
            pwEnterAuditedList: [],
            pwEnterStatues: []
        }
    },
    computed: {
        pwEnterStatusEntries: function () {
            return this.getEntries(this.pwEnterStatues)
        },
        auditedList: function () {
            return this.pwEnterAuditedList.filter(function (item) {
                return item.status != '0'
            })
        }
    },
    methods: {
        getPwEnterAssignPlaceList: function () {

        },
        getPwEnterAuditedList: function () {
            var self = this;
            var pwEnter = this.pwEnter;
            this.$axios.post('/pw/pwApplyRecord/ajaxFindAuditList', {
                eid: this.pwEnter.id
            }).then(function (response) {
                var data = response.data;
                if(data.status === 1){
                    var pwEnterAuditedList = data.data || [];
                    pwEnterAuditedList.forEach(function (item) {
                        item.startDate = pwEnter.startDate;
                        item.endDate = pwEnter.endDate;
                    })
                    self.pwEnterAuditedList = pwEnterAuditedList
                }
            }).catch(function (error) {

            })
        },
        getPwEnterStatus: function () {
            var self = this;
            this.$axios.get('/pw/pwEnter/getPwEnterAuditList').then(function (response) {
                self.pwEnterStatues = response.data || []
            }).catch(function (error) {

            })
        },
    },
    created: function () {
        if(this.pwEnter.id){
            this.getPwEnterStatus();
            this.getPwEnterAuditedList();
        }

    }
})