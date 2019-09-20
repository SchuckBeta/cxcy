<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
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
        <el-breadcrumb-item>双创项目</el-breadcrumb-item>
        <el-breadcrumb-item>${proProject.projectName}</el-breadcrumb-item>
    </el-breadcrumb>
    <row-step-apply>
        <step-item is-complete>第一步（填写项目基本信息）</step-item>
        <step-item>第二步（填写团队基本信息）</step-item>
        <step-item>第三步（提交项目申报附件）</step-item>
    </row-step-apply>
    <el-form :model="proModelHsxmForm" ref="proModelHsxmForm" :rules="proModelHsxmRules" :disabled="disabled" autocomplete="off"
             size="mini" label-width="120px">
        <div class="apply-form-container">
            <div class="apply-form-topbar" style="margin-bottom: 0">
                <el-form-item prop="proModel.subTime" label="申报日期：" inline-message>
                    <el-date-picker
                            v-model="proModelHsxmForm.proModel.subTime"
                            type="date"
                            value-format="yyyy-MM-dd"
                            placeholder="选择日期">
                    </el-date-picker>
                </el-form-item>
                <%--<span class="apply-date">申报日期：{{applyDate | formatDateFilter('YYYY-MM-DD')}}</span>--%>
            </div>
            <div class="apply-form-titlebar-wrap">
                <div class="apply-form-titlebar">
                    <span class="title">项目基本信息</span>
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
                <div class="enter-form-group-titlebar">
                    <span class="title">项目基本信息 <i class="el-icon-d-arrow-right"></i></span>
                </div>
                <el-form-item label="项目名称：" prop="proModel.pName">
                    <el-input v-model="proModelHsxmForm.proModel.pName" maxlength="128"
                              placeholder="最多128个字符"></el-input>
                </el-form-item>
                <el-form-item label="项目类别：" prop="proModel.proCategory">
                    <el-select v-model="proModelHsxmForm.proModel.proCategory">
                        <el-option v-for="item in projectStyles" :key="item.value" :value="item.value"
                                   :label="item.label"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="项目来源：" prop="source">
                    <el-select v-model="proModelHsxmForm.source" clearable>
                        <el-option v-for="item in projectSources" :key="item.value" :value="item.value"
                                   :label="item.label"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="项目拓展及传承：">
                    1、项目能与其他大型比赛、活动对接 2、可在低年级同学中传承 3、结项后能继续开展
                </el-form-item>
                <el-form-item label="项目Logo：" prop="proModel.logoUrl" class="common-upload-one-img">
                    <div class="upload-box-border site-logo-left-size">
                        <div class="avatar-uploader">
                            <div tabindex="0" class="el-upload el-upload--text" @click.stop="openDialogLogo">
                                <img :src="proModelHsxmForm.proModel.logoUrl | ftpHttpFilter(ftpHttp) | proGConPicFilter">
                            </div>
                        </div>
                        <div v-show="proModelHsxmForm.proModel.logoUrl && !disabled" class="arrow-block-delete"
                             @click.stop="proModelHsxmForm.proModel.logoUrl = ''">
                            <i class="el-icon-delete"></i>
                        </div>
                    </div>
                </el-form-item>
                <el-form-item prop="proModel.introduction" label="项目简介：">
                    <el-input type="textarea" v-model="proModelHsxmForm.proModel.introduction" :rows="3"
                              placeholder="1、请简要描述项目的立项背景、项目的主要内容及实施目标； 2、项目的特色或创新点。(最多2000个字符)"
                              maxlength="2000"></el-input>
                </el-form-item>
                <div class="enter-form-group-titlebar">
                    <span class="title">项目介绍 <i class="el-icon-d-arrow-right"></i></span>
                </div>
                <el-form-item prop="innovation" label="前期调研准备：">
                    <el-input type="textarea" v-model="proModelHsxmForm.innovation" :rows="3"
                              maxlength="2000"></el-input>
                </el-form-item>
                <el-form-item label="项目预案：" required>
                    <div class="table-container">
                        <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
                               cellspacing="0" cellpadding="0">
                            <thead>
                            <tr>
                                <th>实施预案</th>
                                <th width="300">时间安排</th>
                                <th>保障措施</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>
                                    <el-form-item prop="planContent" label-width="0" :rules="requiredTextRules">
                                        <el-input type="textarea" v-model="proModelHsxmForm.planContent" :rows="3"
                                                  maxlength="2000"></el-input>
                                    </el-form-item>
                                </td>
                                <td>
                                    <el-form-item prop="planDateRange" label-width="0" :rules="requiredDateRules">
                                        <el-date-picker
                                                v-model="proModelHsxmForm.planDateRange"
                                                type="daterange"
                                                unlink-panels
                                                :default-time="defaultTime"
                                                format="yyyy-MM-dd"
                                                value-format="yyyy-MM-dd"
                                                @change="handleChangePlanDateRange"
                                                range-separator="至"
                                                start-placeholder="开始日期"
                                                end-placeholder="结束日期">
                                        </el-date-picker>
                                    </el-form-item>
                                </td>
                                <td>
                                    <el-form-item prop="planStep" label-width="0" :rules="requiredTextRules">
                                        <el-input type="textarea" v-model="proModelHsxmForm.planStep" :rows="3"
                                                  maxlength="2000"></el-input>
                                    </el-form-item>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </el-form-item>
                <el-form-item label="任务分工：" required>
                    <div class="table-container">
                        <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
                               style="table-layout: fixed"
                               cellspacing="0" cellpadding="0">
                            <thead>
                            <tr>
                                <th>工作任务</th>
                                <th>任务描述</th>
                                <th width="346">时间安排</th>
                                <th width="80">成本</th>
                                <th>质量评价</th>
                                <th width="50">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="(item, index) in proModelHsxmForm.planList">
                                <td>
                                    <el-form-item :prop="'planList.'+index+'.content'" label-width="0"
                                                  :rules="requiredTextRules">
                                        <el-input type="textarea" v-model="item.content" :rows="3"
                                                  maxlength="2000"></el-input>
                                    </el-form-item>
                                </td>
                                <td>
                                    <el-form-item :prop="'planList.'+index+'.description'" label-width="0"
                                                  :rules="requiredTextRules">
                                        <el-input type="textarea" v-model="item.description" :rows="3"
                                                  maxlength="2000"></el-input>
                                    </el-form-item>
                                </td>
                                <td>
                                    <el-form-item :prop="'planList.'+index+'.dateRange'" :rules="requiredDateRules">
                                        <el-date-picker
                                                v-model="item.dateRange"
                                                type="daterange"
                                                unlink-panels
                                                :default-time="defaultTime"
                                                @change="handleChangeDateRange(index)"
                                                range-separator="至"
                                                start-placeholder="开始日期"
                                                style="width: 330px"
                                                format="yyyy-MM-dd"
                                                value-format="yyyy-MM-dd"
                                                end-placeholder="结束日期">
                                        </el-date-picker>
                                    </el-form-item>
                                </td>
                                <td>
                                    <el-form-item :prop="'planList.'+index+'.cost'" label-width="0"
                                                  :rules="requiredCostRules">
                                        <el-input v-model="item.cost" maxlength="20"></el-input>
                                    </el-form-item>
                                </td>
                                <td>
                                    <el-form-item :prop="'planList.'+index+'.quality'" label-width="0"
                                                  :rules="requiredTextRules">
                                        <el-input type="textarea" v-model="item.quality" :rows="3"
                                                  maxlength="2000"></el-input>
                                    </el-form-item>
                                </td>
                                <td>
                                    <el-form-item>
                                        <el-button style="margin-bottom: 5px;" icon="el-icon-plus"
                                                   @click.stop="addPlanList"></el-button>
                                        <br>
                                        <el-button v-show="proModelHsxmForm.planList.length > 1" icon="el-icon-minus"
                                                   @click.stop="proModelHsxmForm.planList.splice(index, 1)"></el-button>
                                    </el-form-item>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </el-form-item>
                <div class="enter-form-group-titlebar">
                    <span class="title">预期成果 <i class="el-icon-d-arrow-right"></i></span>
                </div>
                <el-form-item prop="resultType" label="成果形式：">
                    <el-checkbox-group v-model="proModelHsxmForm.resultType">
                        <el-checkbox v-for="item in projectResultTypes" :label="item.value" :key="item.value">
                            {{item.label}}
                        </el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item prop="resultContent" label="成果说明：">
                    <el-input type="textarea" v-model="proModelHsxmForm.resultContent" :rows="3"
                              maxlength="2000"></el-input>
                </el-form-item>
                <div class="enter-form-group-titlebar">
                    <span class="title">经费预算明细 <i class="el-icon-d-arrow-right"></i></span>
                </div>
                <el-form-item prop="budget" label="经费预算明细：">
                    <el-input type="textarea" v-model="proModelHsxmForm.budget" :rows="3"
                              placeholder="简要描述在项目各个阶段产生的费用项目及明细，如：硬件采购、耗材费、差旅费等"
                              maxlength="2000"></el-input>
                </el-form-item>
                <el-form-item v-show="!isSubmited" label-width="0" class="text-center">
                    <el-button v-if="isStudent" type="primary" @click.stop="validateForm">下一步</el-button>
                </el-form-item>
            </div>
        </div>
    </el-form>

    <el-dialog title="上传项目Logo"
               width="440px"
               :visible.sync="dialogVisibleChangeLogo"
               :close-on-click-modal="false"
               :before-close="handleChangeLogoPicClose">
        <e-pic-file v-model="logoPic" :disabled="isUpdating" @get-file="getLogoPicFile"></e-pic-file>
        <cropper-pic :img-src="logoPic" default-img="${ctxImages}/default-pic.png" :disabled="isUpdating"
                     ref="cropperPic"></cropper-pic>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="isUpdating" @click.stop.prevent="handleChangeLogoPicClose">取消</el-button>
            <el-button size="mini" :disabled="!logoPicFile || isUpdating" type="primary"
                       @click.stop.prevent="updateUserPic">上传
            </el-button>
        </div>
    </el-dialog>
</div>


<script>
    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var self = this;
            var proModelHsxm = JSON.parse(JSON.stringify(${fns: toJson(proModelHsxm)}));
            var validateNumber = function (rules, value, callback) {
                if (value) {
                    if (/^(([1-9]{1}\d{0,10}|0)(\.{1}(?=\d+))|[1-9]{1}[0-9]{0,7})\d{0,3}$/.test(value)) {
                        return callback();
                    }
                    return callback(new Error('请输入数字'))
                }
                return callback();
            };
            var validateCheckSpSt = function (rules, value, callback) {
                return self.$axios.get('/sys/checkNotSpSteel?str='+encodeURI(value))
            }
            var validatePName = function (rules, value, callback) {
                var proModelHsxmForm = self.proModelHsxmForm;
                var proModelForm = proModelHsxmForm.proModel;
                var params = {
                    id: proModelForm.id,
                    actYwId: proModelForm.actYwId,
                    pName: proModelForm.pName
                };
                return self.$axios.get('/promodel/proModel/checkProName', {params: params})
            }
            var validatePNameAndSt = function (rules, value, callback) {
                return self.$axios.all([validateCheckSpSt(rules, value, callback), validatePName(rules, value, callback)])
                        .then(self.$axios.spread(function (spStrReq, nameReq) {
                            if(spStrReq.data.data && nameReq.data){
                                return callback();
                            }
                            if(!spStrReq.data.data){
                                return callback(new Error(spStrReq.data.msg));
                            }
                            return callback(new Error("项目名称已存在"));
                            // Both requests are now complete
                        }));
            }

            return {
                proModelHsxm: proModelHsxm,
                proModelHsxmForm: {
                    id: '',
                    source: '',
                    innovation: '',
                    planContent: '',
                    planStartDate: '',
                    planEndDate: '',
                    planStep: '',
                    planDateRange: [], //时间安排
                    planList: [
                        {content: '', description: '', startDate: '', endDate: '', dateRange: [], cost: '', quality: ''}
                    ],
                    resultType: [],
                    resultContent: '',
                    budget: '',
                    proModel: {
                        id: '',
                        actYwId: '',
                        declareId: '',
                        subTime: '',
                        pName: '',
                        proCategory: '',
                        logoUrl: '',
                        introduction: ''
                    }
                },
                proModelHsxmRules: {
                    resultType: [
                        {required: true, message: "请选择成果形式", trigger: 'change'}
                    ],
                    resultContent: [
                        {required: true, message: "请输入成果说明", trigger: 'blur'}
                    ],
                    budget: [
                        {required: true, message: "请输入经费预算明细", trigger: 'blur'}
                    ],
                    proModel: {
                        subTime: [
                            {required: true, message: "请选择申报日期", trigger: 'change'}
                        ],
                        pName: [
                            {required: true, message: "请输入项目名称", trigger: 'blur'},
                            {validator: validatePNameAndSt, trigger: 'blur'},
                        ],
                        proCategory: [
                            {required: true, message: "请选择项目类别", trigger: 'change'}
                        ],
                        introduction: [
                            {required: true, message: "请输入项目简介", trigger: 'blur'}
                        ]
                    }
                },
                requiredTextRules: [
                    {required: true, message: "请输入文字", trigger: 'blur'}
                ],
                requiredDateRules: [
                    {required: true, message: "请选择时间", trigger: 'blur'}
                ],
                requiredCostRules: [
                    {required: true, message: "请输入数字", trigger: 'blur'},
                    {validator: validateNumber, trigger: 'blur'}
                ],
                officeList: JSON.parse('${fns: toJson(fns: getOfficeList())}'),
                disabled: false,
                projectStyles: [],
                projectSources: [],
                projectResultTypes: [],

                dialogVisibleChangeLogo: false,
                logoPicFile: null,
                isUpdating: false,
                logoPic: '',

                defaultTime: ['00:00:00', '23:59:59']
            }
        },
        computed: {
            officeEntries: function () {
                return this.getEntries(this.officeList, {
                    label: 'name',
                    value: 'id'
                })
            },
            applyDate: function () {
                return this.proModelHsxm.createDate
            },
            defaultPlanL: function () {
                return {
                    content: '',
                    description: '',
                    startDate: '',
                    endDate: '',
                    dateRange: [],
                    cost: '',
                    quality: ''
                }
            },
            isSubmited: function () {
                return this.proModelHsxm.proModel.subStatus === '1';
            }
        },
        methods: {
            proProjectApplyAble: function () {
                var proModelHsxm = this.proModelHsxm;
                var proModel = this.proModelHsxm.proModel;
                var self = this;
                if (!this.isStudent) {
                    return false;
                }
                if (proModel.id) {
                    this.disabled = false;
                    return false;
                }
                this.checkProProjectIsValid('/project/projectDeclare/onProjectApply',{
                    actywId: proModel.actYwId,
                    pid: proModelHsxm.id
                }).then(function () {
                    self.disabled = false;
                }).catch(function (error) {
                    var msgBoxOptions = {
                        type: 'error',
                        title: '提示',
                        closeOnClickModal: false,
                        closeOnPressEscape: false,
                        showClose: false,
                        message: error.message
                    };
                    var btns = error.btns;
                    if (error.code === '0') {
                        msgBoxOptions['confirmButtonText'] = '确定'
                    } else if (error.code === '2') {
                        msgBoxOptions['confirmButtonText'] = btns[0].name;
                        if (btns.length === 2) {
                            msgBoxOptions['cancelButtonText'] = btns[1].name;
                            msgBoxOptions['showCancelButton'] = true
                        }
                    } else {
                        msgBoxOptions['confirmButtonText'] = '刷新'
                    }
                    self.$msgbox(msgBoxOptions).then(function () {
                        if (error.code === '0') {
                            location.href = '/f'
                        } else if (error.code === '2') {
                            location.href = btns[0].url;
                        } else {
                            location.reload();
                        }
                    }).catch(function () {
                        location.href = btns[1].url
                    });
                })
            },

            validateForm: function () {
                var self = this;
                this.$refs.proModelHsxmForm.validate(function (valid) {
                    if (valid) {
                        self.saveProModel()
                    }
                })
            },

            saveProModel: function () {
                var self = this;
                var paramsPathStr = this.getParams();
                var proModelHsxm = this.proModelHsxm;
                var proModel = this.proModelHsxm.proModel;
                this.disabled = true;
                this.$axios({
                    method: 'POST',
                    url: '/proModelHsxm/ajaxSave',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    data: paramsPathStr
                }).then(function (response) {
                    var data = response.data;
                    if (data.ret === 1) {
//                        self.$alert(data.msg, '提示', {
//                            type: 'success',
//                            cancelButtonText: '取消',
//                            confirmButtonText: '下一步',
//                            showCancelButton: true
//                        }).then(function () {
//                            location.href = '/f/proModelHsxm/applyStep2?id=' + data.id + '&actYwId=' + proModel.actYwId;
//                        }).catch(function () {
//
//                        });
                        self.proModelHsxm.id = data.id;
                        proModel.id = data.proModelId;
                        location.href = '/f/proModelHsxm/applyStep2?id=' + data.id + '&actYwId=' + proModel.actYwId;
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error',
                            cancelButtonText: '确定'
                        }).then(function () {
                        })
                        self.disabled = false;
                    }
                }).catch(function () {
                    self.disabled = false;
                    self.$message.error(self.xhrErrorMsg)
                })
            },



            addPlanList: function () {
                this.proModelHsxmForm.planList.push(JSON.parse(JSON.stringify(this.defaultPlanL)))
            },

            handleChangePlanDateRange: function (value) {
                if (value && value.length > 0) {
                    this.proModelHsxmForm.planStartDate = value[0];
                    this.proModelHsxmForm.planEndDate = value[1];
                } else {
                    this.proModelHsxmForm.planStartDate = '';
                    this.proModelHsxmForm.planEndDate = '';
                }
            },

            handleChangeDateRange: function (index) {
                var proModelHsxmForm = this.proModelHsxmForm;
                var planList = proModelHsxmForm.planList;
                var dateRange = planList[index].dateRange;
                if (dateRange && dateRange.length > 0) {
                    planList[index].startDate = dateRange[0];
                    planList[index].endDate = dateRange[1]
                } else {
                    planList[index].startDate = '';
                    planList[index].endDate = ''
                }
            },

            getParams: function () {
                var proModelHsxmForm = this.proModelHsxmForm;
                var proModel = proModelHsxmForm.proModel;
                var proModelParams = {};
                var planList = proModelHsxmForm.planList;
                var planListEntry = {};
                var planListParams;
                var resultType = proModelHsxmForm.resultType;
                var resultTypeParams = [];
                var params = [];
                var proModelHsxmFormParams = {
                    id: '',
                    source: '',
                    innovation: '',
                    planContent: '',
                    planStartDate: '',
                    planEndDate: '',
                    planStep: '',
                    resultContent: '',
                    budget: ''
                };
                this.assignFormData(proModelHsxmFormParams, proModelHsxmForm);
                for (var p in proModel) {
                    if (proModel.hasOwnProperty(p)) {
                        if (p === 'logoUrl') {
                            if (proModel[p].indexOf('/temp/projectlogo') === -1) {
                                continue;
                            }
                        }
                        proModelParams['proModel.' + p] = proModel[p];
                    }
                }
                proModelParams = Object.toURLSearchParams(proModelParams);
                proModelHsxmFormParams = Object.toURLSearchParams(proModelHsxmFormParams);
                planList.forEach(function (item, index) {
                    for (var k in item) {
                        if (item.hasOwnProperty(k)) {
                            if (k === 'dateRange') {
                                continue;
                            }
                            planListEntry['planList[' + index + '].' + k] = item[k];
                        }
                    }
                });
                planListParams = Object.toURLSearchParams(planListEntry);
                resultType.forEach(function (item) {
                    resultTypeParams.push('resultType=' + item);
                });
                resultTypeParams = resultTypeParams.join('&');
                params.push(proModelHsxmFormParams.toString());
                params.push(planListParams.toString());
                params.push(proModelParams.toString());
                params.push(resultTypeParams);
                return params.join('&');
            },

            getProjectStyles: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=project_type').then(function (response) {
                    var data = response.data;
                    self.projectStyles = data || [];
                })
            },
            getProjectSources: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=project_source').then(function (response) {
                    var data = response.data;
                    self.projectSources = data || [];
                })
            },

            getProjectResultTypes: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=project_result_type').then(function (response) {
                    var data = response.data;
                    self.projectResultTypes = data || [];
                })
            },

            handleChangeLogoPicClose: function () {
                this.logoPicFile = null;
                this.logoPic = '';
                this.dialogVisibleChangeLogo = false;
            },

            openDialogLogo: function () {
                var url = this.proModelHsxmForm.proModel.logoUrl;
                if (this.disabled) return false;
                url = this.addFtpHttp(url);
                this.logoPic = url;
                this.dialogVisibleChangeLogo = true;

            },

            getLogoPicFile: function (file) {
                this.logoPicFile = file;
            },

            updateUserPic: function () {
                var data = this.$refs.cropperPic.getData();
                var self = this;
                var formData = new FormData();
                if (data.x < 0 || data.y < 0) {
                    this.$message.error('超出边界，请缩小裁剪框，点击上传');
                    return false;
                }
                this.isUpdating = true;
                formData.append('upfile', this.logoPicFile);
                self.$axios({
                    method: 'POST',
                    url: '/ftp/ueditorUpload/cutImgToTempDir?folder=projectlogo&x=' + parseInt(data.x) + '&y=' + parseInt(data.y) + '&width=' + parseInt(data.width) + '&height=' + parseInt(data.height),
                    data: formData
                }).then(function (response) {
                    var data = response.data;
                    if (data.state === 'SUCCESS') {
                        self.proModelHsxmForm.proModel.logoUrl = data.ftpUrl;
                        self.handleChangeLogoPicClose();
                        self.logoPic = '';
                        self.logoPicFile = null;
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.isUpdating = false;
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            setProModelHsxmForm: function () {
                var proModelHsxm = this.proModelHsxm;
                var planStartDate = proModelHsxm.planStartDate;
                var planEndDate = proModelHsxm.planEndDate;
                var planList = proModelHsxm.planList || [];
                planList = planList.map(function (item) {
                    var dateRange = [];
                    if (item.startDate) {
                        dateRange.push(item.startDate);
                        dateRange.push(item.endDate)
                    }
                    return {
                        content: item.content,
                        description: item.description,
                        startDate: item.startDate,
                        endDate: item.endDate,
                        dateRange: dateRange,
                        cost: item.cost,
                        quality: item.quality
                    }
                });
                proModelHsxm.planList = planList.length > 0 ? planList : this.defaultPlanL;
                if (planStartDate) {
                    proModelHsxm.planStartDate = moment(planStartDate).format('YYYY-MM-DD');
                    proModelHsxm.planEndDate = moment(planEndDate).format('YYYY-MM-DD');
                    proModelHsxm.planDateRange = [proModelHsxm.planStartDate, proModelHsxm.planEndDate].filter(function (item) {
                        return !!item
                    });
                }
                proModelHsxm.resultType = proModelHsxm.resultType ? proModelHsxm.resultType.split(',') : [];
                proModelHsxm.proModel.logoUrl = proModelHsxm.proModel.logo ? proModelHsxm.proModel.logo.tempUrl : '';
                proModelHsxm.proModel.subTime = proModelHsxm.proModel.subTime ? moment(proModelHsxm.proModel.subTime).format('YYYY-MM-DD') : '';
                this.proModelHsxmForm.proModel.declareId = this.loginCurUser.id;
                this.assignFormData(this.proModelHsxmForm, proModelHsxm);
            }
        },
        created: function () {
            this.proProjectApplyAble();
            this.getProjectStyles();
            this.getProjectSources();
            this.getProjectResultTypes();
            this.setProModelHsxmForm();
            this.disabled = this.isTeacher;
        }
    })

</script>

</body>
</html>
