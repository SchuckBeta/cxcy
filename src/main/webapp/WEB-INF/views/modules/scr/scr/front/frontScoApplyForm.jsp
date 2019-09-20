<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
</head>

<body>
<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>学分认定</el-breadcrumb-item>
        <el-breadcrumb-item>申请认定创新创业学分</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="apply-form-container">
        <div class="apply-form-topbar">
            <span class="apply-date">申报日期：{{applyDate | formatDateFilter('YYYY-MM-DD')}}</span>
        </div>
        <div class="apply-form-titlebar-wrap">
            <div class="apply-form-titlebar">
                <span class="title">填写申请信息</span>
            </div>
        </div>
        <div class="apply-form-body">
            <div class="user-info-list">
                <el-row :gutter="15" label-width="120px">
                    <el-col :span="8">
                        <e-col-item align="right" label="姓名：">{{loginCurUser.name}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="学院：">{{loginCurUser.officeName}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="专业班级：">
                            {{loginCurUser.professional | selectedFilter(officeEntries)}}
                            <template v-if="studentExpansion.tClass">{{studentExpansion.tClass}}</template>
                            <template v-else>-</template>
                            班
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="学号：">
                            {{loginCurUser.no}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="手机号：">{{loginCurUser.mobile}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="电子邮箱：">{{loginCurUser.email}}
                        </e-col-item>
                    </el-col>
                </el-row>
            </div>
            <div class="apply-form-group" v-loading="loading">
                <div class="apply-form-item-titlebar">
                    <span class="title">学分认定内容 <i class="el-icon-d-arrow-right"></i></span>
                </div>
                <el-form :model="creditApplyForm" ref="creditApplyForm" :rules="creditApplyRules" size="mini"
                         :validate-on-rule-change="false"
                         :disabled="disabled || isTeacher" label-width="120px">
                    <el-form-item label="认定学分类别：" prop="rule.id">
                        <el-select v-model="creditApplyForm.rule.id" @change="handleChangeRuleId">
                            <el-option v-for="item in scoRuleListCategories" :key="item.id" :value="item.id"
                                       :label="item.name"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="认定内容及标准：" prop="rdetail.parentIdArr">
                        <el-cascader
                                :options="scoRuleTree"
                                v-model="creditApplyForm.rdetail.parentIdArr"
                                :props="cascaderProps"
                                :disabled="!creditApplyForm.rule.id"
                                filterable
                                style="width: 404px"
                                @change="handleChangerdetail">
                        </el-cascader>
                    </el-form-item>
                    <el-form-item v-show="scoRuleDetail.score" label="认定学分：">
                        {{scoRuleDetail.score}}分
                    </el-form-item>
                    <el-form-item prop="condVal" v-if="scoRuleDetail.isCondcheck === '1'" label="实际分值：">
                        <el-input v-model="creditApplyForm.condVal" maxlength="15" style="width: 193px;">
                            <span slot="append">分</span>
                        </el-input>
                    </el-form-item>
                    <div v-show="creditApplyForm.rule.id">
                        <template v-if="isSkill">
                            <el-form-item v-if="isHighest || isAccumulation" label="证书：" required>
                                <el-radio-group v-model="hasCert" @change="handleChangeHasCert">
                                    <el-radio label="1">有</el-radio>
                                    <el-radio label="0">无</el-radio>
                                </el-radio-group>
                            </el-form-item>
                            <el-form-item  prop="scoRapplyCert.certIds" label="获得证书：">
                                <el-cascader
                                        :options="certTree"
                                        v-model="creditApplyForm.scoRapplyCert.certIds"
                                        :props="cascaderProps"
                                        filterable
                                        :disabled="hasCert === '0'"
                                        style="width: 404px"
                                        @change="handleChangeCert">
                                </el-cascader>
                            </el-form-item>
                        </template>
                        <template v-if="scoRule.id && !isSkill">
                            <el-row style="width: 1044px;">
                                <el-col :span="12">
                                    <el-form-item prop="name" label="名称：">
                                        <el-input v-model="creditApplyForm.name" maxlength="64"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <span class="el-form-item-expository">项目作品、参赛作品、参与的活动或著作、荣获的专利等名称，并与提交证明文件上的一致</span>
                                </el-col>
                            </el-row>
                            <el-form-item v-if="scoRule.remarks" label="备注：">
                                <div class="white-space-pre" style="max-height: 84px; overflow: hidden"
                                     v-html="scoRule.remarks"></div>
                            </el-form-item>
                            <credit-team-member v-if="scoRule.ptype === '2'" addable :declare-id="loginCurUser.id"
                                                :sco-ratios="scoRatios"
                                                :ratio-max="ratioMax"
                                                :disabled="disabled"
                                                @change="handleChangeMemberList"
                                                :member-list="memberList"></credit-team-member>
                        </template>
                    </div>
                    <el-form-item prop="sysAttachmentList" label="上传证明材料：">
                        <e-pw-upload-file v-model="creditApplyForm.sysAttachmentList"
                                          @change="handleChangeSysAttachmentList"
                                          action="/ftp/ueditorUpload/uploadPwTemp?folder=scr"></e-pw-upload-file>
                    </el-form-item>
                    <el-form-item prop="remarks" label="申请说明：">
                        <el-input type="textarea" :rows="3" v-model="creditApplyForm.remarks"
                                  placeholder="请简要描述申请认定学分的项目、作品、参与的活动（组织）等相关信息" maxlength="2000"></el-input>
                    </el-form-item>
                    <el-form-item label-width="0" class="text-center">
                        <el-button :disabled="isFileUploading" type="primary" @click.stop="validateCreditApplyForm">提交</el-button>
                        <el-button :disabled="isFileUploading" @click.stop="goToBack">返回</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        mixins: [Vue.creditApplyMixin, Vue.creditDetailMixin],
        data: function () {
            return {
                creditId: '${id}',
                creditType: '${creditType}',
                creditApplyForm: {
                    id: '',
                    rule: {
                        id: ''
                    },
                    lrule: {
                        id: ''
                    },
                    rdetail: {
                        id: '',
                        parentIdArr: []
                    },
                    scoRapplyCert: {
                        id: '',
                        certIds: []
                    },
                    procInsId: '${procInsId}',
                    actYw: {
                        id: '${actywId}'
                    },
                    name: '',
                    condVal: "",
                    user: {
                        id: ''
                    },
                    sysAttachmentList: [],
                    scoRapplyMemberList: [], //用户
                    remarks: ''
                },
                creditApplyDate: '',
                SKILLCREDITID: '${SKILLCREDITID}',
                disabled: false,
                studentExpansion: {},
                sysDate: '',
                scoRuleList: [],
                scoRuleDetailList: [],
                scoRuleTree: [],
                cascaderProps: {
                    label: 'name',
                    value: 'id',
                    children: 'children'
                },
                memberList: [],
                collegesTree: [],
                scoRulePbs: [],
                certList: [],
                certEntries: [],
                loading: false,
                hasCert: '1'
            }
        },
        computed: {
            isFileUploading: function(){
                return this.creditApplyForm.sysAttachmentList.length > 0 && this.creditApplyForm.sysAttachmentList.some(function (item) {
                            return !item.ftpUrl;
                        })
            },
            isSkill: function () {
                return this.creditApplyForm.rule.id === this.SKILLCREDITID;
            },

            applyDate: function () {
                return this.creditApplyDate || this.sysDate;
            },

            officeList: function () {
                return this.treeToList(this.collegesTree)
            },

            officeEntries: function () {
                return this.getEntries(this.officeList, {
                    label: 'name',
                    value: 'id'
                })
            },

            scoRuleListCategories: function () {
                var oScoRuleList = [].concat(this.scoRuleList, this.scoRuleDetailList);
                var scoRuleList = this.scoRuleList.filter(function (item) {
                    return item.parentId === '1';
                })
                return scoRuleList.filter(function (item) {
                    return oScoRuleList.some(function (item2) {
                        return item.id === item2.parentId;
                    })
                })

            },

            scoRuleListIdCategories: function () {
                return this.scoRuleListCategories.map(function (item) {
                    return item.id;
                })
            },

            scoRule: function () {
                var lrule = this.creditApplyForm.lrule;
                var scoRule = {};
                for (var i = 0; i < this.scoRuleList.length; i++) {
                    var item = this.scoRuleList[i];
                    if (item.id === lrule.id) {
                        scoRule = item;
                        break;
                    }
                }
                return scoRule;
            },

            scoRuleDetail: function () {
                if (this.creditApplyForm.rdetail.id) {
                    return this.getRdetail(this.creditApplyForm.rdetail.id)
                }
                return {};
            },

            //是否累加
            isAccumulation: function () {
                return this.scoRuleDetail.maxOrSum === '2';
            },

            //是否最高
            isHighest: function () {
                return this.scoRuleDetail.maxOrSum === '1' && this.scoRuleDetail.isSpecial === '1';
            },

            isCertRequired: function () {
                if(this.isAccumulation || this.isHighest){
                    return this.hasCert === '1';
                }
                return true;
            },


            scoRatios: function () {
                var entries = {};
                var i = 0;
                var scoRulePbs = this.scoRulePbs;
                while (i < scoRulePbs.length) {
                    entries[scoRulePbs[i].num] = scoRulePbs[i].val;
                    i++
                }
                return entries
            },
            ratioMax: function () {
                var len = this.scoRulePbs.length;
                if (len > 0) {
                    return parseInt(this.scoRulePbs[len - 1].num);
                }
                return -1;
            },

            certTree: function () {
                var certList = [].concat(this.certList);
                var i = 0;
                var rootIds = [];
                while (i < certList.length) {
                    var item = certList[i];
                    var parentId = item.parentId;
                    var parent;
                    if (parentId !== '0') {
                        parent = this.certEntries[parentId];
                        if (!parent.children) {
                            parent.children = [];
                        }
                        parent.children.push(item);
                    } else {
                        rootIds.push(item.id);
                    }
                    i++;
                }
                return certList.filter(function (item) {
                    return rootIds.indexOf(item.id) > -1;
                })
            },

            creditApplyRules: {
                get: function () {
                    var validteRdetailIsSta = this.validteRdetailIsSta;
                    var validateScore = this.validateScore;
                    var isCertRequired = this.isCertRequired;
                    return {
                        rule: {
                            id: [
                                {required: true, message: '请选择认定学分类别', trigger: 'change'}
                            ]
                        },
                        rdetail: {
                            parentIdArr: [
                                {required: true, message: '请选择认定内容及标准', trigger: 'change'}
                            ]
                        },
                        condVal: [
                            {required: true, message: '请输入实际分值', trigger: 'blur'},
                            {validator: validateScore, trigger: 'blur'},
                        ],
                        name: [
                            {required: true, message: '请输入名称', trigger: 'blur'},
                            {validator: validteRdetailIsSta, trigger: 'blur'}
                        ],
                        scoRapplyCert: {
                            certIds: [
                                {required: isCertRequired, message: '请选择证书', trigger: 'change'},
                                {validator: validteRdetailIsSta, trigger: 'change'}
                            ]
                        },
                        sysAttachmentList: [
                            {required: true, message: '请上传证明材料', trigger: 'change'}
                        ]
                    }
                }
            }
        },
        watch: {
            scoRule: function (value) {
                if (value.ptype === '2') {
                    this.getScoPbList(value.id);
                }
            }
        },
        methods: {
            handleChangeHasCert: function (value) {
                if(this.isAccumulation || this.isHighest){
                    if(value === '0'){
                        this.creditApplyForm.scoRapplyCert.certIds = [];
                        this.creditApplyForm.scoRapplyCert.id = '';
                    }
                }
            },

            getCollegeTree: function () {
                var self = this;
                this.$axios.get('/sys/office/getOfficeTree').then(function (response) {
                    self.collegesTree = response.data;
                })
            },

            getSysDate: function () {
                var self = this;
                this.$axios.get('/sys/sysCurDateYmdHms').then(function (response) {
                    self.sysDate = moment(response.data).format('YYYY-MM-DD');
                })
            },

            handleChangerdetail: function (value) {
                var id = value.length > 0 ? value[value.length - 1] : '';
                var rdetail = this.getRdetail(id);
                var self = this;
                if (rdetail) {
                    this.creditApplyForm.rdetail.id = id;
                    this.creditApplyForm.lrule.id = rdetail.rule.id;
                } else {
                    this.creditApplyForm.rdetail.id = '';
                    this.creditApplyForm.lrule.id = '';
                }
                this.hasCert = '1';
                this.$refs.creditApplyForm.clearValidate();
                this.$refs.creditApplyForm.validateField('rdetail.parentIdArr', function (error) {
                    if (!error) {
                        if (value && value.length > 0) {
                            self.ajaxFindRdetailPersonalSum(value, rdetail)
                        }
                    }
                })
            },

            ajaxFindRdetailPersonalSum: function (value, rdetail) {
                var id = value[value.length - 1];
                var creditApplyForm = this.creditApplyForm;
                var user = creditApplyForm.user;
                var self = this;
                if (rdetail) {
                    var params = {
                        user: user,
                        apply: {
                            id: creditApplyForm.id,
                            rdetail: {
                                id: rdetail.id,
                                maxOrSum: rdetail.maxOrSum,
                                isSpecial:rdetail.isSpecial
                            }
                        }
                    };
                    this.$axios.post('/scr/ajaxFindRdetailPersonalSum', params).then(function (response) {
                        var data = response.data;
                        if (data.status === 0) {
                            self.disabled = true;
                            self.$confirm(data.msg, '提示', {
                                type: 'warning',
                                confirmButtonText: '继续申报'
                            }).then(function () {
                                self.disabled = false;
                            }).catch(function () {

                            })
                        }
                    })
                }
            },

            handleChangeRuleId: function (value) {
                var list = [].concat(this.scoRuleList, this.scoRuleDetailList);
                var rootScoRuleList = list.filter(function (item) {
                    return item.parentId === value;
                });
//                var scoRuleListIdCategories = this.scoRuleListIdCategories;
//                var scoRuleList = this.scoRuleList.filter(function (item) {
//                    return scoRuleListIdCategories.indexOf(item.id) === -1;
//                });
                this.scoRuleTree = this.generateTree(JSON.parse(JSON.stringify(list)), rootScoRuleList);
                this.creditApplyForm.lrule.id = '';
                this.creditApplyForm.rdetail.id = '';
                this.creditApplyForm.rdetail.parentIdArr = [];
            },

            handleChangeMemberList: function (value) {
                this.memberList = value;
            },



            generateTree: function (list, rootScoRuleList) {
//                var list = [].concat(this.scoRuleDetailList, scoRuleList);
                var entries = this.getScoRuleListEntries(list);
                var arr = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var parentId = item.parentId;
                    var parent = entries[parentId];
                    if (parent) {
                        if (!parent.children) {
                            parent.children = [];
                        }
                        parent.children.push(item);
                    }
                }
                for (var j = 0; j < rootScoRuleList.length; j++) {
                    arr.push(entries[rootScoRuleList[j].id])
                }
                return arr;
            },

            getScoRuleListEntries: function (scoRuleList) {
                var i = 0;
                var entries = {};
                while (i < scoRuleList.length) {
                    var item = scoRuleList[i];
                    entries[item.id] = item;
                    i++
                }
                return entries;
            },

            handleChangeSysAttachmentList: function () {
                this.$refs.creditApplyForm.validateField('sysAttachmentList');
            },

            getScoPbList: function (id) {
                var self = this;
                this.$axios.get('/scr/ajaxScoPbList?rule.id=' + id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.scoRulePbs = data.data || [];
                    }
                })
            },

            getScoRuleList: function () {
                var self = this;
//                this.$axios.get('/scr/fronScoRuleList').then(function (response) {
//                    var data = response.data;
//                    if (data.status === 1) {
//                        self.scoRuleList = data.data || [];
//                    }
//                })
                return this.$axios.get('/scr/fronScoRuleList')
            },

            getScoRuleDetailList: function () {
                var self = this;
//                this.$axios.get('/scr/fronScoRuleDetailList').then(function (response) {
//                    var data = response.data;
//                    if (data.status === 1) {
//                        var scoRuleDetailList = data.data || [];
//                        self.scoRuleDetailList = scoRuleDetailList.map(function (item) {
//                            item.parentId = item.rule.id;
//                            return item;
//                        })
//                    }
//                })
                return this.$axios.get('/scr/fronScoRuleDetailList')
            },

            getAllScoRuleList: function () {
                var self = this;
                this.$axios.all([this.getScoRuleList(), this.getScoRuleDetailList()]).then(this.$axios.spread(function (response1, response2) {
                    // Both requests are now complete
                    var data1 = response1.data;
                    var scoRuleList = [];
                    var scoRuleDetailList = [];
                    if (data1.status === 1) {
                        scoRuleList = data1.data || [];
                    }
                    var data2 = response2.data;
                    if (data2.status === 1) {
                        scoRuleDetailList = data2.data || [];
                        scoRuleDetailList.forEach(function (item) {
                            item.parentId = item.rule.id;
                            return item;
                        })
                    }
                    self.scoRuleList = self.getScoRuleListFiltered(scoRuleDetailList, scoRuleList);
                    self.scoRuleDetailList = scoRuleDetailList;
                    if (self.creditId) {
                        self.getCreditApplyForm();
                    }
                }))
            },

            getScoRuleListFiltered: function (scoRuleDetailList, scoRuleList) {
                var scoRuleEntity = {};
                var nScoRuleList = [];
                var self = this;
                var scoRuleEntityAdd = {};
                scoRuleList.forEach(function (item) {
                    scoRuleEntity[item.id] = item;
                });
                scoRuleDetailList.forEach(function (item) {
                    if (scoRuleEntity[item.parentId]) {
                        nScoRuleList = nScoRuleList.concat(self.getScoRuleListParents(scoRuleEntity[item.parentId], scoRuleEntity, scoRuleEntityAdd))
                    }
                });
                return nScoRuleList;
            },

            getScoRuleListParents: function (parent, scoRuleEntity, scoRuleEntityAdd) {
                var nScoRuleList = [];
                while (parent) {
                    if (parent.parentId === '0') {
                        break;
                    }
                    if (!scoRuleEntityAdd[parent.id]) {
                        scoRuleEntityAdd[parent.id] = true;
                        nScoRuleList.push(parent)
                    }
                    parent = scoRuleEntity[parent.parentId];
                }
                return nScoRuleList;
            },


            getStudentExpansion: function () {
                var self = this;
                this.$axios.get('/sys/frontStudentExpansion/getStudentExpansion?userId=' + this.loginCurUser.id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.studentExpansion = data.data || {};
                    }
                })
            },

            checkedUser: function () {
                var self = this;
                if (!this.creditId && this.isStudent) {
                    this.getSysDate();
                    this.getUserIsCompleted().then(function (response) {
                        var data = response.data;
                        var loginCurUser = self.loginCurUser;
                        if (data.status !== 1) {
                            self.disabled = true;
                            self.$msgbox({
                                type: 'error',
                                title: '提示',
                                closeOnClickModal: false,
                                closeOnPressEscape: false,
                                confirmButtonText: '确定',
                                showClose: false,
                                message: data.msg
                            }).then(function () {
                                location.href = '/f/sys/frontStudentExpansion/findUserInfoById?userId=' + loginCurUser.id + '&custRedict=1&isEdit=1';

                            }).catch(function () {
                            });
                        } else {
                            self.checkUserIsUpScore()
                        }
                    })
                }
            },

            memberMe: function () {
                var loginCurUser = this.loginCurUser;
                var self = this;
                this.$axios.post('/team/getUserStudentTeam', [loginCurUser.id]).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var student = data.data || [];
                        student = student.map(function (item) {
                            item['userzz'] = item.userId === self.declareId ? '0' : '1';
                            item["userType"] = item.userType;
                            return item;
                        })
                        self.memberList.push({
                            id: '',
                            user: student[0],
                            rate: ''
                        });
                    }
                })
            },

            validateCreditApplyForm: function () {
                var self = this;
                this.$refs.creditApplyForm.validate(function (valid) {
                    if (valid) {
                        if (self.scoRule.ptype == '2') {
                            if (!self.validateRatio()) {
                                return false;
                            }
                        }
                        self.submitCreditApplyForm();
                    }
                })
            },

            validateRatio: function () {
                var memberList = this.memberList;
                var len = memberList.length;
                var ratio = this.scoRatios[len.toString()];
                if (memberList.length === 1) {
//                    this.$message.error('认定组成员最小为2人');
//                    return false;
                    return true;
                }
                if (!ratio) {
                    this.$message.error('已超出团队最大人数限制');
                    return false;
                }
                var nRatio = ratio.split(':');
                var memberRatio = memberList.map(function (item) {
                    return parseInt(item.rate);
                });
                memberRatio = memberRatio.sort(function (item1, item2) {
                    if (item1 > item2) {
                        return 1
                    } else if (item1 < item2) {
                        return -1
                    } else {
                        return 0
                    }
                });
                nRatio = nRatio.sort(function (item1, item2) {
                    if (item1 > item2) {
                        return 1
                    } else if (item1 < item2) {
                        return -1
                    } else {
                        return 0
                    }
                });
                if (nRatio.join('-') !== memberRatio.join('-')) {
                    this.$message.error('请参照' + ratio + '学分配比填写');
                    return false;
                }
                return true;
            },

            getParamsCredit: function () {
                //技能学分参数
                var creditApplyForm = JSON.parse(JSON.stringify(this.creditApplyForm));
                if (this.creditApplyForm.rule.id === this.SKILLCREDITID) {
                    delete creditApplyForm.name;
                    delete creditApplyForm.scoRapplyMemberList;
                    delete creditApplyForm.scoRapplyCert.certIds;
                } else if (this.scoRule.ptype === '2') {
                    if (this.memberList.length === 1) {
                        this.memberList.forEach(function (item) {
                            item.rate = '1';
                        })
                    }
                    creditApplyForm.scoRapplyMemberList = this.memberList.map(function (item) {
                        return {
                            id: item.id,
                            user: {
                                id: item.user.userId
                            },
                            rate: item.rate
                        };
                    });
                    delete creditApplyForm.scoRapplyCert;
                } else {
                    delete creditApplyForm.scoRapplyCert;
                    delete creditApplyForm.scoRapplyMemberList;
                }
                creditApplyForm.sysAttachmentList = creditApplyForm.sysAttachmentList.map(function (item) {
                    return {
                        "uid": item.response ? '' : item.uid,
                        "fileName": item.title || item.name,
                        "name": item.title || item.name,
                        "suffix": item.suffix,
                        "url": item.url,
                        "ftpUrl": item.ftpUrl,
                        "fielSize": item.fielSize,
                        "tempUrl": item.tempUrl,
                        "tempFtpUrl": item.tempFtpUrl
                    }
                });
                delete creditApplyForm.rdetail.parentIdArr;
                return creditApplyForm
            },

            getRdetail: function (id) {
                var rdetail;
                if (!id) {
                    return rdetail;
                }
                for (var i = 0; i < this.scoRuleDetailList.length; i++) {
                    var item = this.scoRuleDetailList[i];
                    if (item.id === id) {
                        rdetail = item;
                        break;
                    }
                }
                return rdetail;
            },

            submitCreditApplyForm: function () {
                var params = this.getParamsCredit();
                var self = this;
                this.disabled = true;
                this.$axios.post('/scr/ajaxSave?actywId=${actywId}&flowType=${flowType}&ptype=${ptype}', params).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$msgbox({
                            type: 'success',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            confirmButtonText: '查看学分',
                            cancelButtonText: '继续申报',
                            showCancelButton: true,
                            showConfirmButton: true,
                            showClose: false,
                            message: '提交成功'
                        }).then(function () {
                            location.href = '/f/scr/list';
                        }).catch(function () {
                            location.href = '/f/scr/toFrontScrApplyForm?flowType=${flowType}&ptype=${ptype}';
                        });
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.disabled = false;
                }).catch(function (error) {
                    self.disabled = false;
                    self.$message.error(self.xhrErrorMsg)
                })
            },

            getCertList: function () {
                var self = this;
                this.$axios.get('/scr/frontScoRapplyCertList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.certList = data.data || [];
                        self.certEntries = self.getCertEntries(self.certList)
                    }
                })
            },

            getCertEntries: function (certList) {
                var i = 0;
                var entries = {};
                while (i < certList.length) {
                    var item = certList[i];
                    entries[item.id] = item;
                    i++;
                }
                return entries;
            },

            handleChangeCert: function (value) {
                this.creditApplyForm.scoRapplyCert.id = value.length > 0 ? value[value.length - 1] : '';
            },

            getCreditApplyForm: function () {
                var self = this;
                this.loading = true;
                var params = {
                    id: this.creditId,
                    creditType: this.creditType
                };
                this.$axios.get('/scr/ajaxFindScoDetail?' + Object.toURLSearchParams(params)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || {};
                        self.handleChangeRuleId(data.rule.id);
                        self.assignFormData(self.creditApplyForm, data);
                        self.studentExpansion = data.studentExpansion || {};
                        var rdetail = data.rdetail;
                        var rule = rdetail.rule;
                        var parentIds = rule.parentIds + rule.id;
                        parentIds += ',' + rdetail.id;
                        parentIds = parentIds.split(',');
                        self.creditApplyForm.rdetail.parentIdArr = parentIds.slice(3);
                        self.creditApplyForm.actYw.id = data.actYw.id;
                        self.creditApplyForm.procInsId = data.procInsId;
                        var scoRapplyCert = data.scoRapplyCert;
                        var scoRapplyMemberList = data.scoRapplyMemberList;
                        if (scoRapplyCert) {
                            var scoRapplyCertParentIds = scoRapplyCert.parentIds + ',' + scoRapplyCert.id;
                            scoRapplyCertParentIds = scoRapplyCertParentIds.split(',');
                            self.creditApplyForm.scoRapplyCert.certIds = scoRapplyCertParentIds.slice(1);
                        }
                        if (scoRapplyMemberList) {
                            var memberList = scoRapplyMemberList.map(function (item) {
                                item.user.userId = item.user.id;
                                item.user['userzz'] = item.userId === self.declareId ? '0' : '1';
                                item.user["userType"] = '1';
                                return item;
                            });
                            self.creditApplyForm.scoRapplyMemberList = memberList;
                            self.memberList = memberList;
                        }
                        self.creditApplyDate = data.applyDate;
                    } else {
                        self.$message.error(data.msg);
                        self.disabled = true;
                    }
                    self.loading = false;
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg);
                    self.disabled = true;
                    self.loading = false;
                })
            },

            checkUserIsUpScore: function () {
                var self = this;
                this.disabled = true;
                this.$axios.get('/scr/ajaxRapplyUserSum?user.id=' + this.loginCurUser.id).then(function (response) {
                    var data = response.data;
                    if (data.data) {
                        self.$msgbox({
                            type: 'warning',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            confirmButtonText: '确定',
                            message: data.msg
                        }).then(function () {
                            self.disabled = false;
                        }).catch(function () {
                        });
                    } else {
                        self.disabled = false;
                    }
                })
            },

            goToBack: function () {
                history.go(-1)
            }
        },
        created: function () {
            if (this.isStudent) {
                this.getAllScoRuleList();
                this.getCollegeTree();
                this.getCertList();
            }

            if (!this.creditId && this.isStudent) {
                this.checkedUser();
                this.getStudentExpansion();
            }

            if (this.creditType !== '1') {
                this.memberMe()
            }

            this.creditApplyForm.user.id = this.loginCurUser.id;
        }
    })

</script>
</body>
</html>
