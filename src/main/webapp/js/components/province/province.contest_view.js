Vue.component('pro-view-contest', {
    template: '<transition :name="transitionName" class="child-view"><div v-show="showing" class="project-view-container child-view">' +
    '<div v-loading="loading"><pro-view-header :project="proModel" :user="sse" :pro-model="proModel"><slot></slot></pro-view-header><div class="pro-vm-body">\n' +
    '    <div class="pro-vm-border"></div>\n' +
    '    <div class="pro-vmb-wrap">\n' +
    '      <el-tabs v-model="tabActiveName" size="mini">\n' +
    '        <el-tab-pane label="项目信息" name="first">\n' +
    '          <div class="pro-vmb-tab-wrap">\n' +
    '            <div class="pro-tab-cate-title"><span>基本信息</span></div>\n' +
    '            <e-col-con label-width="120px">\n' +
    '              <e-col-item label="参赛赛道：">{{proModel.gcTrack | selectedFilter(trackEntries)}}</e-col-item>\n' +
    '              <e-col-item label="项目名称：">{{proModel.pName || \'\'}}</e-col-item>\n' +
    '              <e-col-item label="大赛类别：">{{proModel.proCategory | selectedFilter(proCategoryEntries)}}</e-col-item>\n' +
    '              <e-col-item label="参赛组别：">{{proModel.level | selectedFilter(proLevelEntries)}}</e-col-item>\n' +
    '              <e-col-item label="所属领域：">{{proModel.belongsField | checkboxFilter(probelongsFieldsEntries)}}</e-col-item>\n' +
    '              <e-col-item label="项目标签："><el-tag v-if="proModel.achievementTf === \'1\'" size="medium">高校科研成果转化</el-tag><el-tag v-if="proModel.achievementUser === \'1\'" size="medium">创始人为科技成果的完成人或所有人</el-tag><el-tag v-if="proModel.coCreation === \'1\'" size="medium">师生共创</el-tag></e-col-item>\n' +
    '              <e-col-item label="项目概述："><span class="white-space-pre" v-html="proModel.introduction"></span></e-col-item>\n' +
    '            </e-col-con>\n' +
    '              <div class="pro-tab-cate-title"><span>项目材料</span></div>\n' +
    '              <ul class="timeline">\n' +
    '                <li class="work" v-for="file in applyFiles" :key="file.id">\n' +
    '                  <span class="contest-date">{{file.createDate | formatDateFilter(\'YYYY-MM-DD HH:mm\')}}</span>\n' +
    '                  <img :src="ctxImages+\'/time-line.png\'" alt="">\n' +
    '                  <div class="relative">\n' +
    '                    <e-file-item :file="file" size="mini" :show="false"></e-file-item>\n' +
    '                  </div>\n' +
    '                </li>\n' +
    '              </ul>\n' +
    '              <template v-if="reports.length > 0">\n' +
    '                <div v-for="item in reports" :key="item.id">\n' +
    '                  <div class="pro-tab-cate-title"><span>{{item.gnodeName}}</span></div>\n' +
    '                  <ul class="timeline">\n' +
    '                    <li class="work" v-for="file in item.files" :key="file.id">\n' +
    '                      <span class="contest-date">{{file.createDate | formatDateFilter(\'YYYY-MM-DD HH:mm\')}}</span>\n' +
    '                      <img :src="ctxImages+\'/time-line.png\'" alt="">\n' +
    '                      <div class="relative">\n' +
    '                        <e-file-item :file="file" size="mini" :show="false"></e-file-item>\n' +
    '                      </div>\n' +
    '                    </li>\n' +
    '                  </ul>\n' +
    '                  <e-col-item label="已取得阶段性成果：" label-width="206px"><span class="white-space-pre" v-html="item.stageResult"></span></e-col-item>\n' +
    '                </div>\n' +
    '              </template>\n' +
    '          </div>\n' +
    '        </el-tab-pane>\n' +
    '        <el-tab-pane label="团队信息" name="second">\n' +
    '          <div class="pro-vmb-tab-wrap">\n' +
    '            <div class="pro-tab-cate-title"><span>团队介绍</span></div><e-col-item label-width="20px"><span class="white-space-pre" v-html="team.summary"></span></e-col-item><div class="pro-tab-cate-title"><span>团队成员</span></div>\n' +
    '            <div><team-student-list :team-student="teamStu" :leader="proModel.deuser"></team-student-list></div>\n' +
    '            <div class="pro-tab-cate-title"><span>指导老师</span></div>\n' +
    '            <div><team-teacher-list :team-teacher="teamTea"></team-teacher-list></div>\n' +
    '          </div>\n' +
    '        </el-tab-pane>\n' +
    '        <el-tab-pane v-if="proProcess && proProcess.length > 0" label="项目过程" name="proProcess">\n' +
    '          <div class="pro-vmb-tab-wrap">\n' +
    '            <div class="pro-process-shaft">\n' +
    '              <div v-for="item in proProcess" :key="item.id" class="pro-process-st-item">\n' +
    '                <div class="date-stage-process">\n' +
    '                  <span class="date">{{ item.date }}年</span>\n' +
    '                  <p class="stage">{{ item.stage }}</p>\n' +
    '                </div>\n' +
    '                <div class="pro-process-sti-content">\n' +
    '                  <div class="pro-process-mark">\n' +
    '                    <i class="el-icon-location"></i>\n' +
    '                  </div>\n' +
    '                  <div class="pro-process-container">\n' +
    '                    <div class="triangle">\n' +
    '                      <i class="el-icon-arrow-left"></i>\n' +
    '                    </div>\n' +
    '                    <div class="pro-process-popover">\n' +
    '                      <div class="stage-tag">\n' +
    '                        <span>{{ item.incident }}</span>\n' +
    '                      </div>\n' +
    '                      <div class="stage-content">\n' +
    '                        <e-col-item label="带动就业人数" label-width="90px" align="left"><span class="stage-item-weight">{{ item.workPersonNum }}人</span></e-col-item>\n' +
    '                        <e-col-item label="年收入" label-width="90px" align="left">{{item.income}}</e-col-item>\n' +
    '                      </div>\n' +
    '                    </div>\n' +
    '                  </div>\n' +
    '                </div>\n' +
    '              </div>\n' +
    '            </div>\n' +
    '          </div>\n' +
    '        </el-tab-pane>\n' +
    '        <el-tab-pane v-if="auditRecordList.length > 0" label="审核记录" name="five">\n' +
    '          <div class="pro-vmb-tab-wrap">\n' +
    '            <div class="pro-tab-cate-title">\n' +
    '              <span>审核</span>\n' +
    '            </div>\n' +
    '            <div class="pro-audit-container">\n' +
    '              <div v-for="item in auditListCp" :key="item.id" class="pro-audit-item">\n' +
    '                <div class="pro-audit-user">\n' +
    '                  <div class="user-pic">\n' +
    '                    <img :src="item.user.photo |  ftpHttpFilter(ftpHttp) | studentPicFilter" />\n' +
    '                  </div>\n' +
    '                  <div class="user-info-row">\n' +
    '                    <p class="name">审核人：{{ item.user.name }}</p>\n' +
    '                    <p class="date">{{ item.createDate | formatDateFilter(\'YYYY-MM-DD\') }}</p>\n' +
    '                  </div>\n' +
    '                </div>\n' +
    '                <div class="pro-audit-info-block">\n' +
    '                  <div class="pro-audit-info-wrap">\n' +
    '                    <div class="triangle">\n' +
    '                      <i class="el-icon-arrow-left"></i>\n' +
    '                    </div>\n' +
    '                    <div class="pro-audit-info-content">\n' +
    '                      <div class="result-box">\n' +
    '                        <template v-if="!!item.grade">\n' +
    '                          <label class="re-label">审核结果</label>\n' +
    '                          <p class="re-state">{{ item.result }}</p>\n' +
    '                        </template>\n' +
    '                        <template v-else>\n' +
    '                          <label class="re-label">评分</label>\n' +
    '                          <p class="re-state">{{ item.score }}分</p>\n' +
    '                        </template>\n' +
    '                      </div>\n' +
    '                      <div class="suggestion-box">\n' +
    '                        <label class="su-label">审核建议及意见</label>\n' +
    '                        <p class="su-txt">{{ item.suggest }}</p>\n' +
    '                      </div>\n' +
    '                    </div>\n' +
    '                  </div>\n' +
    '                </div>\n' +
    '              </div>\n' +
    '            </div>\n' +
    '          </div>\n' +
    '        </el-tab-pane>\n' +
    '      </el-tabs>\n' +
    '    </div>\n' +
    '  </div></div></div></transition>',
    props: {
        viewId: String
    },

    data: function(){
        return {
            proModel: {},
            teamStu: [],
            teamTea: [],
            sse: {},
            applyFiles: [],
            auditRecordList: [],
            reports: [],
            proProcess: [],
            team: {},
            tabActiveName: 'first',
            transitionName: "slide-left",
            loading: true,
            showing: true
        }
    },
    computed: {
        proCategoryEntries: function () {
            return this.$root.proCategoryEntries
        },
        proLevelEntries: function () {
            return this.$root.proLevelEntries
        },
        probelongsFieldsEntries: function () {
            return this.$root.probelongsFieldsEntries
        },
        trackEntries: function () {
            return this.$root.trackEntries
        },
        auditListCp: function(){
            return this.auditRecordList.filter(function (item) {
                return !!item.id;
            })
        }
    },
    methods:{
        getProvViewForm: function () {
            var self = this;
            this.loading = true;
            this.transitionName = 'slide-left';
            this.$axios.get('/promodel/proModel/ajaxProvViewForm', {params: {id: this.viewId}}).then(function (response) {
                var data = response.data;
                if(data.status === 1){
                    data = data.data;
                    self.proModel = data.proModel || {};
                    self.teamStu = data.teamStu || [];
                    self.teamTea = data.teamTea || [];
                    self.sse = data.sse || {};
                    self.applyFiles = data.applyFiles || [];
                    self.auditRecordList = data.actYwAuditInfos || [];
                    self.reports = data.reports || [];
                    self.team = data.team || {};
                    self.$nextTick(function () {
                        self.transitionName = 'slide-right';
                    })
                }else {
                    self.transitionName = 'slide-right';
                }
                self.loading = false;
            }).catch(function () {
                self.loading = false;
            })
        }
    },
    watch: {
        'viewId': function () {
            this.getProvViewForm();
        }
    },
    mounted: function () {
        this.getProvViewForm();
    }
})