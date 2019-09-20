<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
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
        <el-breadcrumb-item>创业基地</el-breadcrumb-item>
        <el-breadcrumb-item>入驻查询</el-breadcrumb-item>
    </el-breadcrumb>
    <div v-if="isStudent" class="mgb-20 text-right">
        <el-button type="primary" size="mini" @click.stop.prevent="goToPwEnterForm">新建入驻申请</el-button>
    </div>
    <div v-loading="loading" element-loading-text="加载中" style="min-height: 300px;">
        <pw-enter-column v-for="item in pwEnterList" :key="item.id">
            <div class="pec-header-bar" slot="header">
                <a class="go-to-detail" :href="'${ctxFront}/pw/pwEnter/view?id='+item.id">查看详情</a>
                <span class="apply-user">申请人：{{item.applicant.name}}</span>
                <span>所属学院：{{item.applicant.officeName}}</span>
            </div>
            <div class="pw-enter-table-column">
                <el-table :data="item.data" size="mini" class="pw-enter-table">
                    <el-table-column width="110" label="申请日期" align="center">
                        <template slot-scope="scope">
                            {{scope.row.createDate | formatDateFilter('YYYY-MM-DD')}}
                        </template>
                    </el-table-column>
                    <el-table-column label="入驻信息" align="center">
                        <template slot-scope="scope">
                            <el-tooltip :content="scope.row.name" popper-class="white" placement="right">
                                <span class="break-ellipsis">{{scope.row.label}}：{{scope.row.name}}</span>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column label="入驻房间（工位数）" align="center">
                        <template slot-scope="scope">
                            <template v-if="scope.row.rooms.length > 0">
                                <el-tooltip v-for="room in scope.row.rooms" :key="room.id"
                                            :content="room.pwRoom.pwSpace.id | getRoomNames(baseTreeEntries, 'pId', room.pwRoom.name, room.num)"
                                            popper-class="white" placement="right">
                            <span class="break-ellipsis">
                            {{room.pwRoom.pwSpace.id | getRoomNames(baseTreeEntries, 'pId', room.pwRoom.name, room.num)}}
                                <br>
                            </span>
                                </el-tooltip>
                            </template>
                            <template v-else>-</template>
                        </template>
                    </el-table-column>
                    <el-table-column label="入驻期限" align="center">
                        <template slot-scope="scope">
                            {{scope.row.startDate | formatDateFilter('YYYY-MM-DD')}}至{{scope.row.endDate |
                            formatDateFilter('YYYY-MM-DD')}}
                        </template>
                    </el-table-column>
                    <el-table-column label="退孵日期" align="center">
                        <template slot-scope="scope">
                            {{scope.row.exitDate | formatDateFilter('YYYY-MM-DD')}}
                        </template>
                    </el-table-column>
                    <el-table-column label="续期次数" align="center">
                        <template slot-scope="scope">
                            {{scope.row.termNum}}
                        </template>
                    </el-table-column>
                    <el-table-column v-if="isStudent"  label="操作" align="center" width="102">
                        <template slot-scope="scope">
                            <template v-if="loginCurUser.id == item.declareId" >
                                <div v-if="item.status === '60'">
                                    <el-button type="text" size="mini"
                                               :disabled="disabled"
                                               @click.stop.prevent="confirmDelPwEnterApply(item)">删除
                                    </el-button>
                                </div>
                                <template v-else-if="item.isTemp == '0'">
                                    <div v-if="item.status == '20' || item.auditStatus == '2'">
                                        <el-button type="text" size="mini" @click.stop.prevent="goOnApply(item)">重新申请
                                        </el-button>
                                        <br>
                                        <el-button type="text" size="mini"
                                                   :disabled="disabled"
                                                   @click.stop.prevent="confirmDelPwEnterApply(item)">删除
                                        </el-button>
                                    </div>
                                    <div v-else>
                                        <el-button type="text"
                                                   :disabled="disabled || item.status === '60'"
                                                   size="mini"
                                                   @click.stop.prevent="openDialogVisibleResult(item)">成果提交
                                        </el-button>
                                        <br>
                                        <el-button type="text"
                                                   :disabled="disabled || item.status === '60' "
                                                   size="mini"
                                                   @click.stop.prevent="openDialogVisibleRenewal(item)">申请续期
                                        </el-button>
                                        <br>
                                        <el-button type="text"
                                                   :disabled="disabled || item.status === '60'"
                                                   size="mini"
                                                   @click.stop.prevent="applyOut(item)">申请退孵
                                        </el-button>
                                        <br>
                                        <el-button type="text" size="mini"
                                                   :disabled="disabled || item.status === '60'"
                                                   @click.stop.prevent="applyCompanyChange(item)">变更申请
                                        </el-button>
                                    </div>
                                </template>
                                <div v-else-if="item.isTemp == '1'">
                                    <el-button type="text" size="mini" @click.stop.prevent="goOnApply(item)">继续申请
                                    </el-button>
                                    <br>
                                    <el-button type="text" size="mini"
                                               :disabled="disabled"
                                               @click.stop.prevent="confirmDelPwEnterApply(item)">删除
                                    </el-button>
                                </div>
                            </template>
                            <template v-else>-</template>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div slot="footer" v-if="item.isTemp == '0' && loginCurUser.id == item.declareId">
                <pw-record-line :params="{eid: item.id}" :audit-entries="pwEnterAuditEntries" :ref="'PRL'+item.id" :key="item.id"
                                @get-audit-status="getAuditStatus"></pw-record-line>
            </div>

        </pw-enter-column>
        <div v-if="pwEnterList.length === 0" class="empty pdt-60">没有入驻数据</div>
        <div class="text-right mgb-20">
            <el-pagination
                    size="small"
                    @size-change="handlePSizeChange"
                    background
                    @current-change="handlePCPChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20]"
                    :page-size="searchListForm.pageSize"
                    layout="total,prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>


    <el-dialog
            title="提示"
            :visible.sync="dialogVisibleResult"
            width="520px"
            :close-on-click-modal="false"
            :before-close="handleCloseResult">
        <el-form :model="resultForm" ref="resultForm" :rules="resultRules" label-width="120px" size="mini"
                 v-loading="resultLoading"
                 autocomplete="off" :disabled="resultFormDisabled">
            <el-form-item label="成果附件：" prop="sysAttachmentList">
                <e-pw-upload-file v-model="resultForm.sysAttachmentList"
                               action="/ftp/ueditorUpload/uploadPwTemp?folder=pwEnter" :upload-file-vars="{}"
                               tip="支持上传：jpg、pdf、word、excel、ppt及压缩文件等 <br/> 说明：可上传，如：企业财务报表、阶段性成果报告"
                               @change="handleChangeResultFiles"></e-pw-upload-file>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button size="mini" @click.stop.prevent="handleCloseResult" :disabled="resultFormDisabled">取 消</el-button>
    <el-button size="mini" type="primary" @click.stop.prevent="submitResult"
               :disabled="resultFormDisabled">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
            title="申请续期"
            :visible.sync="dialogVisibleRenewal"
            width="520px"
            :close-on-click-modal="false"
            :before-close="handleCloseRenewal">
        <el-form :model="renewalForm" ref="renewalForm" :rules="renewalRules" label-width="120px" size="mini"
                 autocomplete="off" :disabled="renewalFormDisabled">
            <el-form-item label="到期时间：">
                <span>{{renewalForm.endDate | formatDateFilter('YYYY-MM-DD')}}</span>
                <span class="el-form-item-expository">包含当天</span>
            </el-form-item>
            <el-form-item prop="hasTerm" label="续期时间：">
                <el-select v-model="renewalForm.hasTerm" @change="handleChangeHasTerm" style="width: 220px;">
                    <el-option v-for="item in yearOptions" :key="item.num" :value="item.num"
                               :label="item.remarks | dateAddDays(renewalForm.endDate, item.num)"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item v-if="renewalForm.hasTerm == '-1'" label="自定义时间：" prop="termDate">
                <el-date-picker
                        v-model="renewalForm.termDate"
                        type="date"
                        :default-value="renewalForm.endDate"
                        :picker-options="pickerOptions"
                        placeholder="选择日期">
                </el-date-picker>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button size="mini" @click.stop.prevent="handleCloseRenewal" :disabled="renewalFormDisabled">取 消</el-button>
    <el-button size="mini" type="primary" @click.stop.prevent="applyRenewal"
               :disabled="renewalFormDisabled">确 定</el-button>
  </span>
    </el-dialog>
</div>

<script type="text/javascript">

    'use strict';
    new Vue({
        el: '#app',
        mixins: [Vue.pwEnterAuditListMixin],
        data: function () {
            return {
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10
                },
                pageCount: 0,
                pwEnterList: [],
                pwEnterTypes: [],
                disabled: false,
                loading: false,
                dialogVisibleResult: false,
                yearOptions: [],
                resultForm: {
                    id: '',
                    sysAttachmentList: [],
                },
                dialogVisibleRenewal: false,
                resultRules: {
                    sysAttachmentList: [
                        {required: true, message: '请选择成果附件', trigger: 'change'}
                    ]
                },
                resultFormDisabled: false,
                renewalForm: {
                    id: '',
                    term: '',
                    hasTerm: '',
                    termDate: '',
                    endDate: ''
                },
                renewalRules: {
                    hasTerm: [
                        {required: true, message: '请选择续期日期', trigger: 'change'}
                    ],
                    termDate: [
                        {required: true, message: '请选择自定义时间', trigger: 'change'}
                    ]
                },

                sysDate: '',
                renewalFormDisabled: false,
                baseTreeEntries: {},
                resultLoading: false,
                disabledNumbers: ['20', '60']
            }
        },
        computed: {

            pwEnterTypeEntries: function () {
                return this.getEntries(this.pwEnterTypes)
            },
            sysDateAfter: function () {
                var date = new Date();
                if (this.sysDate) {
                    date = new Date(this.sysDate);
                    date.setDate(date.getDate() + 1);
                }
                return moment(date).format('YYYY-MM-DD');
            },
            pickerOptions: function () {
                var endDate = this.renewalForm.endDate;
                return {
                    disabledDate: function (time) {
                        var dateTime = endDate ? new Date(endDate).getTime() : (Date.now() + 24 * 3600 * 1000)
                        return time.getTime() < dateTime;
                    }
                }
            },
        },
        methods: {

            goToPwEnterForm: function () {
                location.href = this.frontOrAdmin + '/pw/pwEnter/form';
            },

            goOnApply: function (row) {
                var params = {id: row.id};
                if(row.auditStatus === '2'){
                    params['status'] = '0';
                }
                location.href = this.frontOrAdmin + '/pw/pwEnter/form?'+ Object.toURLSearchParams(params);
            },

            getPwEnterTypes: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/getPwEnterTypes').then(function (response) {
                    var data = response.data;
                    self.pwEnterTypes = data || [];
                }).catch(function () {

                })
            },

            getAuditStatus: function (res) {
                var status = res.auditStatus;
                var pwEnterList = this.pwEnterList;
                var id = res.id;
                for (var i = 0; i < pwEnterList.length; i++) {
                    if (pwEnterList[i].id === id) {
                        Vue.set(pwEnterList[i], 'auditStatus', status);
                        break;

                    }
                }
            },

            getPwEnterList: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/pw/pwEnter/ajaxList?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var pageData = data.data || {};
                        var pwEnterList = pageData.list || [];
                        pwEnterList = pwEnterList.map(function (item) {
                            var label = self.pwEnterTypeEntries[item.type];
                            var name = (item.eteam && item.eteam.team) ? item.eteam.team.name : '';
                            if (item.type === '2') {
                                name = (item.ecompany && item.ecompany.pwCompany) ? item.ecompany.pwCompany.name : '';
                            }
                            item['data'] = [{
                                createDate: item.createDate,
                                startDate: item.startDate,
                                endDate: item.endDate,
                                exitDate: item.exitDate,
                                termNum: item.termNum,
                                rooms: item.erooms || [],
                                team: item.eteam || {},
                                name: name,
                                label: label
                            }]
                            return item;
                        });
                        self.pwEnterList = pwEnterList;
                        self.searchListForm.pageNo = pageData.pageNo || 1;
                        self.searchListForm.pageSize = pageData.pageSize || 10;
                        self.pageCount = pageData.count;
                    } else {
                        self.$message.error(data.msg)
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg);
                    self.loading = false;
                })
            },

            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getPwEnterList();
            },

            checkPwEnterPromise: function (row) {
                this.disabled = true;
                var self = this;
                return this.$axios.get('/pw/pwEnter/ajaxCheckPwEnter?id=' + row.id).catch(function (error) {
                    self.disabled = false;
                    self.$message.error(self.xhrErrorMsg);
                });
            },

            applyTeamChange: function (row) {
                var self = this;
                this.checkPwEnterPromise(row).then(function (response) {
                    var data = response.data;
                    self.goToApplyForm(data, row, 'team')
                })
            },

            applyCompanyChange: function (row) {
                var self = this;
                this.checkPwEnterPromise(row).then(function (response) {
                    var data = response.data;
                    self.goToApplyForm(data, row, 'company')
                })
            },


            applyProjectChange: function (row) {
                var self = this;
                this.checkPwEnterPromise(row).then(function (response) {
                    var data = response.data;
                    self.goToApplyForm(data, row, 'project')
                })
            },

            goToApplyForm: function (data, row, enterType) {
                var params = {
                    id: row.id,
                    appType: '2',
                    changeEnterType: enterType
                };
                if (data.status === 1) {
                    location.href = ('/f/pw/pwEnter/form?' + Object.toURLSearchParams(params));
                } else {
                    this.$message.error(data.msg);
                }
                this.disabled = false;
            },

            handlePCPChange: function () {
                this.getPwEnterList();
            },

            submitResult: function () {
                var self = this;
                this.$refs.resultForm.validate(function (valid) {
                    if (valid) {
                        self.postResultForm();
                    }
                })
            },

            postResultForm: function () {
                var self = this;
                this.resultFormDisabled = true;
                var resultForm = JSON.parse(JSON.stringify(this.resultForm));
                resultForm.sysAttachmentList = resultForm.sysAttachmentList.map(function (item) {
                    return {
                        "uid": item.response ? '' : item.uid,
                        "fileName": item.title || item.name,
                        "name": item.title || item.name,
                        "suffix": item.suffix,
                        "url": item.url,
                        "ftpUrl": item.ftpUrl,
                        "tempFtpUrl": item.ftpUrl,
                        "tempUrl": item.url,
                        "fielSize": item.fielSize,
                    }
                });
                this.$axios.post('/pw/pwEnter/ajaxPwEnterResultApply', resultForm).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$message.success('已成功提交成果');
                        var pwEnterList = self.pwEnterList;
                        for (var i = 0; i < pwEnterList.length; i++) {
                            if (pwEnterList[i].id === resultForm.id) {
                                Vue.set(pwEnterList[i], 'sysAttachmentList', resultForm.sysAttachmentList);
                                break;
                            }
                        }

                        self.dialogVisibleResult = false;
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.resultFormDisabled = false;
                }).catch(function (error) {
                    self.resultFormDisabled = false;
                    self.$message.error(self.xhrErrorMsg)
                })
            },

            applyRenewal: function () {
                var self = this;
                this.$refs.renewalForm.validate(function (valid) {
                    if (valid) {
                        self.postRenewalForm();
                    }
                })
            },

            postRenewalForm: function () {
                var self = this;
                this.renewalFormDisabled = true;
                var renewalParams = this.getRenewalParams();
                this.$axios.get('/pw/pwEnter/ajaxPwEnterRenewalApply?' + Object.toURLSearchParams(renewalParams)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$message.success('已发送续期申请，请等待审核');
                        self.dialogVisibleRenewal = false;
                        self.$refs['PRL'+ renewalParams.id][0].getPwRecordLine();
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.renewalFormDisabled = false;
                }).catch(function (error) {
                    self.renewalFormDisabled = false;
                    self.$message.error(self.xhrErrorMsg)
                })
            },

            getRenewalParams: function () {
                var renewalForm = this.renewalForm;
                var nTerm = renewalForm.hasTerm;
                var params = {
                    id: renewalForm.id,
                    term: nTerm
                };
                if (renewalForm.hasTerm === '-1') {
                    nTerm = renewalForm.termDate;
                    var endDate = this.renewalForm.endDate || this.sysDate
                    params['term'] = Math.ceil((new Date(nTerm).getTime() - new Date(endDate).getTime()) / 24 / 3600 / 1000);
                }
                return params;
            },

            //退孵申请
            applyOut: function (item) {
                var self = this;
                this.$confirm("确认发送退孵申请吗？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.disabled = true;
                    self.$axios.get('/pw/pwEnter/ajaxPwEnterHatchApply?pwEnterId=' + item.id).then(function (response) {
                        var data = response.data;
                        if (data.status === 1) {
                            self.$message.success('已发送退孵申请，请等待审核');
                            self.$refs['PRL'+ item.id][0].getPwRecordLine();
                        } else {
                            self.$message.error(data.msg)
                        }
                        self.disabled = false;
                    }).catch(function (error) {
                        self.disabled = false;
                        self.$message.error(self.xhrErrorMsg)
                    })
                }).catch(function () {

                })

            },

            getResultList: function (id) {
                var self = this;
                this.resultLoading = true;
                this.$axios.get('/pw/pwEnter/ajaxFindPwEnterResultList?pwEnterId=' + id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || {};
                        self.resultForm.sysAttachmentList = data.sysAttachmentList || [];
                    }
                    self.resultLoading = false;
                }).catch(function (error) {
                    self.resultLoading = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            openDialogVisibleResult: function (item) {
                this.dialogVisibleResult = true;
                this.resultForm.id = item.id;
                this.getResultList(item.id);
                console.log(this.$refs)
//                this.resultForm.attachmentList = item.attachmentList || [];

            },

            handleCloseResult: function () {
                this.dialogVisibleResult = false;
            },
            openDialogVisibleRenewal: function (item) {
                this.dialogVisibleRenewal = true;
                this.renewalForm.id = item.id;
                this.renewalForm.hasTerm = '';
                this.renewalForm.termDate = '';
                this.renewalForm.term = '';
                this.renewalForm.endDate = item.endDate;
            },
            handleCloseRenewal: function () {
                this.dialogVisibleRenewal = false;
            },

            getSysDate: function () {
                var self = this;
                this.$axios.get('/sys/sysCurDateYmdHms').then(function (response) {
                    self.sysDate = moment(response.data).format('YYYY-MM-DD');
//                    var date1 = new Date(self.sysDate);
//                    var date2 = new Date(self.sysDate);
//                    date1.setMonth(date1.getMonth() + 6);
//                    date2.setMonth(date2.getMonth() + 12);
//                    var halfYear = moment(date1).format('YYYY-MM-DD');
//                    var oneYear = moment(date2).format('YYYY-MM-DD');
//                    self.yearOptions = [
//                        {label: ('半年（' + halfYear + '）'), value: halfYear},
//                        {label: ('一年（' + oneYear + '）'), value: oneYear},
//                        {label: '自定义', value: '-1'}
//                    ]
                })
            },
            getYearOptions: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/ajaxDtypeTerms?isAll=false').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.yearOptions = JSON.parse(data.data) || [];
                        self.yearOptions.push({remarks: '自定义', num: '-1'})
                    }
                })
            },
            handleChangeHasTerm: function (value) {
                if (value !== '-1') {
                    this.renewalForm.termDate = '';
                }
            },

            handleChangeResultFiles: function () {
                this.$refs.resultForm.validateField('sysAttachmentList')
            },
            getSpaceList: function () {
                var self = this;
                return this.$axios.get('/pw/pwSpace/treeData').then(function (response) {
                    self.spaceList = response.data;
                    var entries = {};
                    self.spaceList.forEach(function (item) {
                        entries[item.id] = item;
                    })
                    self.baseTreeEntries = entries;
                })
            },
            confirmDelPwEnterApply: function (row) {
                var self = this;
                this.$confirm("确认删除这条入驻记录吗？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delPwEnterApply(row);
                }).catch(function () {

                })
            },

            //删除入驻记录
            delPwEnterApply: function (row) {
                var self = this;
                this.$axios.get('/pw/pwEnter/ajaxDelPwEnterApply?id=' + row.id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getPwEnterList();
                        self.$message.success("删除成功")
                    } else {
                        self.$message.error(data.msg)
                    }
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg)
                })
            },

        },
        created: function () {
            this.getPwEnterTypes();
            this.getPwEnterList();
            this.getSysDate();
            this.getSpaceList();
            this.getYearOptions();
        }
    })

</script>
</body>
</html>
