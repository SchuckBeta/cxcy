<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>


    <script src="${ctxJs}/components/cityDropDown/cityDropDown.js"></script>
    <script src="${ctxJs}/components/cityDropDown/cityPicker.js"></script>



</head>

<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar :second-name="teacherForm.id ? '编辑': '添加'" href="/sys/expert"></edit-bar>
    </div>
    <div class="user_avatar-sidebar">
        <div class="user-avatar">
            <div class="avatar-pic">
                <img :src="teacherForm.user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter">
            </div>
            <el-button type="primary" size="mini"
                       @click.stop.prevent="handleChangeUserPicOpen">{{teacherForm.user.photo ? '更换' : '添加'}}图像
            </el-button>
        </div>
    </div>
    <div class="user_detail-container">
        <div class="user_detail-title">
            <i class="iconfont icon-user"></i><span class="text">专家基本信息</span>
        </div>
        <div v-loading="loading" class="user_detail-wrap">
            <div class="user_detail-inner">
                <div class="user_detail-title-handler">
                    <div class="ud-row-handler">
                        <el-button type="primary" title="保存" size="mini" :disabled="isUpdating"
                                   style="vertical-align: top"
                                   @click.stop.prevent="validateForm">保存
                        </el-button>
                        <el-button :disabled="isUpdating" size="mini" @click.stop.prevent="goToBack"
                                   style="vertical-align: top">返回
                        </el-button>
                    </div>
                </div>
                <el-form :model="teacherForm" ref="teacherForm" autocomplete="off"
                         :disabled="isUpdating" size="mini" label-width="90px" class="el-form-builder">
                    <el-row :gutter="10">
                        <el-col :span="24">
                            <el-form-item label="专家来源：" prop="expertTypeList" :rules="expertTypeListRules">
                                <el-checkbox-group v-model="teacherForm.expertTypeList" name="expertTypeList"
                                                   class="multi-row">
                                    <el-checkbox v-for="role in expertSources" :label="role.value" :key="role.value">
                                        {{role.label}}
                                    </el-checkbox>
                                </el-checkbox-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="user.name" label="姓名：" :rules="userNameRules">
                                <el-input v-model="teacherForm.user.name" maxlength="15" placeholder="限15位字符"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="user.sex" label="性别：" :rules="isCompanyMaster ? [] : userSexRules">
                                <el-radio-group v-model="teacherForm.user.sex">
                                    <el-radio-button v-for="item in sexes" :key="item.value" :label="item.value"
                                                     style="vertical-align: top">{{item.label}}
                                    </el-radio-button>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>

                        <el-col :span="8">
                            <el-form-item prop="user.no" label="职工号：" :rules="userAsStuNoRules">
                                <el-input v-model="teacherForm.user.no" maxlength="24" placeholder="限24位字符"></el-input>
                            </el-form-item>
                        </el-col>

                        <el-col :span="8">
                            <el-form-item label="国家/户籍：" prop="user.country"
                                          class="el-input--mini">
                                <city-dropdown v-model="teacherForm.user.country" class-name="el-input__inner"
                                               placeholder="填写/选择"
                                               :city-data="cityIdKeyData">
                                    <a slot="rightSelected" href="javascript:void (0)"
                                       @click="dialogVisibleCityPicker=true">选择</a>
                                </city-dropdown>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="user.national" label="民族：">
                                <el-input v-model="teacherForm.user.national" maxlength="24" placeholder="限24位字符"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="院系/专业：" prop="user.professionalIds">
                                <el-cascader
                                        ref="cascader"
                                        :options="officeTree"
                                        :clearable="true"
                                        filterable
                                        change-on-select
                                        style="width: 100%"
                                        v-model="teacherForm.user.professionalIds"
                                        :props="cascaderCollegesProps">
                                </el-cascader>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="user.political" label="政治面貌：">
                                <el-select v-model="teacherForm.user.political" placeholder="请选择">
                                    <el-option
                                            v-for="item in politicsStatuses"
                                            :key="item"
                                            :label="item"
                                            :value="item">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="user.birthday" label="出生年月：">
                                <el-date-picker
                                        style="width:100%"
                                        v-model="teacherForm.user.birthday"
                                        type="month"
                                        :editable="false"
                                        value-format="yyyy-MM-dd"
                                        :default-value="defaultBirthdayDate"
                                        :picker-options="userBirthdayPickerOptions"
                                        placeholder="选择出生时间">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="24">
                            <el-form-item prop="serviceIntentionIds" label="服务意向：" :rules="isCompanyMaster ? [] : serviceIntentionIdsRules">
                                <el-checkbox-group v-model="teacherForm.serviceIntentionIds" class="multi-row">
                                    <el-checkbox v-for="item in masterServices" name="serviceIntentionIds"
                                                 :key="item.value"
                                                 :label="item.value">{{item.label}}
                                    </el-checkbox>
                                </el-checkbox-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="16">
                            <el-form-item prop="user.idNumber" label="证件号：" class="teacherform-id-type" :rules="identityRulesAsTea">
                                <el-input placeholder="请输入证件号" v-model="teacherForm.user.idNumber"
                                          class="input-with-select_identity">
                                    <el-form-item prop="user.idType" slot="prepend"
                                                  style="margin-bottom: 0;height: 26px;">
                                        <el-select v-model="teacherForm.user.idType" placeholder="请选择证件类型">
                                            <el-option v-for="idType in idTypes" :label="idType.label"
                                                       :value="idType.value" :key="idType.id"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="educationType" label="学历类别：" :rules="isCompanyMaster ? [] : educationTypeRules">
                                <el-select v-model="teacherForm.educationType" clearable>
                                    <el-option v-for="item in educationTypes" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="user.education" label="学历：" :rules="isCompanyMaster ? [] : educationRules">
                                <el-select v-model="teacherForm.user.education" clearable>
                                    <el-option v-for="item in enducationLevels" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="discipline" label="学科门类：">
                                <el-select v-model="teacherForm.discipline" clearable>
                                    <el-option v-for="item in professionalTypes" :value="item.value" :key="item.id"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="user.degree" label="学位类别：">
                                <el-select v-model="teacherForm.user.degree" clearable>
                                    <el-option v-for="item in degreeTypes" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>

                        <el-col :span="8">
                            <el-form-item prop="user.mobile" label="手机号：" :rules="userMobileRules">
                                <el-input name="user.mobile" v-model="teacherForm.user.mobile"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="industry" label="行业：">
                                <el-input name="industry" v-model="teacherForm.industry" maxlength="48" placeholder="限48位字符"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="postTitle" label="职务：">
                                <el-input name="postTitle" v-model="teacherForm.postTitle" maxlength="48" placeholder="限48位字符"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="technicalTitle" label="职称：">
                                <el-select v-model="teacherForm.technicalTitle" clearable>
                                    <el-option v-for="item in expertJobNames" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="firstBank" label="开户银行：">
                                <el-input name="firstBank" v-model="teacherForm.firstBank" maxlength="48" placeholder="限48位字符"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="bankAccount" label="账号：">
                                <el-input name="bankAccount" v-model="teacherForm.bankAccount" maxlength="128" placeholder="限128位字符"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="user.email" label="邮箱：" :rules="userEmailRules">
                                <el-input v-model="teacherForm.user.email" maxlength="128"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="workUnitType" label="单位类别：">
                                <el-select v-model="teacherForm.workUnitType" clearable>
                                    <el-option v-for="item in workTypes" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="workUnit" label="工作单位：">
                                <el-input name="workUnit" v-model="teacherForm.workUnit" maxlength="128" placeholder="限128位字符"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="recommendedUnits" label="推荐单位：">
                                <el-input name="recommendedUnits" v-model="teacherForm.recommendedUnits" maxlength="128" placeholder="限128位字符"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="firstShow" label="首页展示：">
                                <el-select v-model="teacherForm.firstShow" clearable>
                                    <el-option v-for="item in yesNo" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="siteShow" label="栏目展示：">
                                <el-select v-model="teacherForm.siteShow" clearable>
                                    <el-option v-for="item in yesNo" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="24">
                            <el-form-item label="技术领域：" prop="user.domainIdList">
                                <el-select v-model="teacherForm.user.domainIdList" name multiple placeholder="请选择"
                                           style="width: 100%">
                                    <el-option
                                            v-for="item in technologyFields"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>

                        <el-col :span="24">
                            <el-form-item label="关键词：" prop="keyWords">
                                <el-tag
                                        :key="tag"
                                        v-for="(tag, index) in teacherForm.keywords"
                                        closable
                                        type="info"
                                        size="medium"
                                        :disable-transitions="false"
                                        @close="handleCloseKeyWord(tag, index)">
                                    {{tag}}
                                </el-tag>
                                <el-input
                                        class="input-new-tag"
                                        v-if="inputKeyWordVisible"
                                        v-model="keyWord"
                                        ref="saveKeyWordInput"
                                        @keyup.enter.native="handleInputKeyWordConfirm"
                                        @blur="handleInputKeyWordConfirm"
                                >
                                </el-input>
                                <el-button v-else class="button-new-tag" size="mini" @click="showInputKeyWord">添加关键词
                                </el-button>
                            </el-form-item>
                        </el-col>
                        <el-col :span="24" style="height: auto">
                            <el-form-item label="工作经历：">
                                <el-input type="textarea" name="mainExp" :rows="3" maxlength="1024" placeholder="限1024位字符"
                                          v-model="teacherForm.mainExp"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <template v-if="!!teacherForm.user.id">
                <div class="user_detail-inner user_detail-inner-experience">
                    <div class="user_detail-title-handler">
                        <div class="ud-row-title"><span class="name">指导项目</span></div>
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
                                        <e-col-item label="担任角色：" label-width="72px">{{ getRoleName(project) }}</e-col-item>
                                        <e-col-item label="项目级别：" label-width="72px">{{project.level}}</e-col-item>
                                        <e-col-item label="项目结果：" label-width="72px">{{project.result}}</e-col-item>
                                        <e-col-item label="项目周期：" label-width="72px">{{getProjectRange(project)}}</e-col-item>
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
                        <div v-show="projectList.length < 1" class="text-center">
                            <div class="user_experience-title none">
                                <span>暂无指导项目经历</span>
                            </div>
                        </div>
                    </el-row>
                </div>

                <div class="user_detail-inner user_detail-inner-experience">
                    <div class="user_detail-title-handler">
                        <div class="ud-row-title"><span class="name">指导大赛</span></div>
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
                                        <e-col-item label="担任角色：" label-width="72px">{{ getRoleName(contest) }}</e-col-item>
                                        <e-col-item label="大赛级别：" label-width="72px">{{contest.level}}</e-col-item>
                                        <e-col-item label="大赛获奖：" label-width="72px">{{contest.award}}</e-col-item>
                                        <e-col-item label="大赛周期：" label-width="72px">{{getProjectRange(contest)}}
                                            <%--</e-col-item>--%>
                                    </div>
                                    <div class="exp-intro">
                                        <e-col-item label="大赛简介：" label-width="72px" class="white-space-pre-static">
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
                        <div v-show="contestList.length < 1" class="text-center">
                            <div class="user_experience-title none">
                                <span>暂无指导大赛经历</span>
                            </div>
                        </div>
                    </el-row>
                </div>


                <review-task :user-id="teacherForm.user.id"></review-task>
            </template>

        </div>
    </div>

    <el-dialog
            title="选择城市"
            :close-on-click-modal="false"
            :visible.sync="dialogVisibleCityPicker"
            :before-close="handleCityPickerClose">
        <city-picker v-model="teacherForm.user.country" :city-data="city" :tab-active-key.sync="cityTab"
                     @selected="cityHandleSelected"></city-picker>
    </el-dialog>

    <el-dialog title="上传图像"
               width="440px"
               :close-on-click-modal="false"
               :visible.sync="dialogVisibleChangeUserPic"
               :before-close="handleChangeUserPicClose">
        <e-pic-file v-model="userAvatar" :disabled="isUpdating" @get-file="getUserPicFile"></e-pic-file>
        <cropper-pic :img-src="userAvatar" :disabled="isUpdating" ref="cropperPic"></cropper-pic>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="isUpdating" @click="handleChangeUserPicClose">取消</el-button>
            <el-button size="mini" :disabled="!userPicFile || isUpdating" type="primary" @click="updateUserPic">上传
            </el-button>
        </div>
    </el-dialog>


</div>

<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        mixins: [Vue.verifyRules],
        data: function () {

            return {
                teacherForm: {
                    id: '',
                    user: {
                        id: '',
                        name: '',
                        loginName: '',
                        photo: '',
                        no: '',
                        sex: '',
                        birthday: '',
//                        area: '',
                        national: '',
                        political: '',
                        mobile: '',
                        idType: '1',
                        idNumber: '',
                        education: '',
                        degree: '',
                        email: '',
                        domainIdList: [],
                        professional: '',
                        professionalIds: [],
                        userType: '2',
                        country: ''
                    },
                    teachertype: '',
                    expertTypeList: [],
                    serviceIntentionIds:[],
                    educationType: '',
                    discipline: '',
                    industry: '',
                    postTitle: '',
                    technicalTitle: '',
                    firstBank: '',
                    bankAccount: '',
                    workUnitType: '',
                    workUnit: '',
                    recommendedUnits: '',
                    firstShow: '',
                    siteShow: '',
                    topShow: '',
                    keywords: [],
                    mainExp: '',
                    roleType: '1'
                },
                userAsExpertId: '${userAsExpertId}',
                loading: true,
                userAvatar: '',
                userPicFile: null,
                isUpdating: false,

                expertJobNames: [],
                masterTypes: [],
                sexes: [],
                masterServices: [],
                idTypes: [],
                educationTypes: [],
                enducationLevels: [],
                professionalTypes: [],
                degreeTypes: [],
                colleges: [],
                workTypes: [],
                yesNo: [],
                technologyFields: [],
                officeTree: [],
                officeEntity: {},
//                teacherCategories: [],
                politicsStatuses: ['中共党员', '共青团员', '民主党派人士', '无党派民主人士', '普通公民'],

//                isDefaultCollegeRootId: false, //去掉学院;
                city: cityData,
                cityDropdownVisible: false,
                cityTab: 'hotCities',
                dialogVisibleCityPicker: false,

                dialogVisibleChangeUserPic: false,//修改用户图像

                userBirthdayPickerOptions: {
                    disabledDate: function (time) {
                        return time.getTime() > Date.now()
                    }
                },

                inputKeyWordVisible: false,
                keyWord: '',

                projectList: [],
                contestList: [],

                cascaderCollegesProps: {
                    label: 'name',
                    value: 'id',
                    children: 'children'
                },
                saveMessage: '${message}',

                hideRoleNames: ['2494442643c24193bc1aa480eddcf43f', 'ecee0da215d04186bdeea0373bf8eeea', 'ef8b7924557747e2ac71fe5b52771c08'],
                roleList: [],
//                isRoleDisable: false,
                expertSources: [],
                expertTypeListRules: [{required: true, message: '请选择专家角色', trigger: 'change'}],
                userSexRules: [{required: true, message: '请选择性别', trigger: 'change'}],
                serviceIntentionIdsRules: [{required: true, message: '请选择服务意向', trigger: 'change'}],
                educationTypeRules: [{required: true, message: '请选择学历类别', trigger: 'change'}],
                educationRules: [{required: true, message: '请选择学历', trigger: 'change'}]
            }
        },
        computed: {
            cpRoleList: function () {
                var names = this.hideRoleNames;
                return this.roleList.filter(function (item) {
                    return names.indexOf(item.id) > -1
                })
            },
            defaultBirthdayDate: function () {
                return moment(new Date()).subtract(20, 'year').format('YYYY-MM');
            },

            cityIdKeyData: function () {
                var data = {};
                for (var i = 0; i < this.city.length; i++) {
                    var city = this.city[i];
                    data[city.id] = city;
                }
                return data;
            },
            userId: function () {
                return this.teacherForm.user.id;
            },
            isCompanyMaster: function () {
                return this.teacherForm.expertTypeList.length == 1 && this.teacherForm.expertTypeList.indexOf('2494442643c24193bc1aa480eddcf43f') > -1;
            },
            userAsStuNoRules: function () {
                var userNoRules = this.userNoRules;
                var rules = [{required: true, message: '请输入职工号', trigger: 'blur'}];
                return rules.concat(userNoRules);
            },
            userMobileRules: function () {
                var isCompanyMaster = this.isCompanyMaster;
                var mobileRules = this.mobileRules;
                var rules = [{required: true, message: '请输入手机号', trigger: 'blur'}]
                if (isCompanyMaster) {
                    return mobileRules;
                }
                return rules.concat(mobileRules);
            },
            identityRulesAsTea: function () {
                var isCompanyMaster = this.isCompanyMaster;
                var identityRules = this.identityRules;
                var idNumberPassport = this.idNumberPassport;
                var showRules = [];
                var rules = [{required: true, message: '请输入证件号', trigger: 'blur'}];
                var idType = this.teacherForm.user.idType;
                if(idType == '1'){
                    showRules = identityRules
                }else {
                    showRules = idNumberPassport;
                }
                if (isCompanyMaster) {
                    return showRules;
                }
                return rules.concat(showRules);
            },
            userEmailRules: function () {
                var isCompanyMaster = this.isCompanyMaster;
                var emailRules = this.emailRules;
                var rules = [{required: true, message: '请输入邮箱', trigger: 'blur'}]
                if (isCompanyMaster) {
                    return emailRules;
                }
                return rules.concat(emailRules);
            },
            professionalTeaRules: function () {
                var professionRulesAsTeacher = this.professionRulesAsTeacher;
                var isCompanyMaster = this.isCompanyMaster;
                var rules = [{required: true, message: '请选择学院', trigger: 'change'}]
                if (isCompanyMaster) {
                    return professionRulesAsTeacher;
                }
                return rules.concat(professionRulesAsTeacher);
            },
        },
        methods: {

            getRoleList: function () {
                var self = this;
                this.$axios({
                    method: 'GET',
                    url: '/sys/role/getRoleList'
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.roleList = data.data || [];
                    }
                }).catch(function () {

                })
            },

            validateForm: function () {
                var self = this;
                this.$refs.teacherForm.validate(function (valid) {
                    if (valid) {
                        self.submitTeacherForm();
                    }
                });
            },


            submitTeacherForm: function () {
                var teacherForm = JSON.parse(JSON.stringify(this.teacherForm));
                var professionalIds = teacherForm.user.professionalIds;
                var self = this;
                if (professionalIds.length > 2) {
                    teacherForm.user.professional = professionalIds[2];
                    teacherForm.user.officeId = professionalIds[1];
                } else {
                    teacherForm.user.professional = '';
                    teacherForm.user.officeId = professionalIds[professionalIds.length - 1];
                }
                teacherForm.user.office = {
                    id: teacherForm.user.officeId
                };
                if (teacherForm.user.birthday) {
                    teacherForm.user.birthday = moment(teacherForm.user.birthday).valueOf();
                }
                delete teacherForm.user.professionalIds;
                this.isUpdating = true;
                this.$axios.post('/sys/backTeacherExpansion/saveExpertOrTea', teacherForm).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.$msgbox({
                            type: 'success',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            showClose: false,
                            message: data.msg
                        }).then(function () {
                            window.location.href = self.ctx + '/sys/expert';
                        }).catch(function () {

                        });
                    } else {
                        self.$alert(data.msg, {
                            type: 'error'
                        })
                    }
                    self.isUpdating = false;
                }).catch(function (error) {
                    self.isUpdating = false;
                });

            },

            goToBack: function () {
                return window.history.go(-1);
            },

            handleChangeUserPicClose: function () {
                this.userPicFile = null;
                this.dialogVisibleChangeUserPic = false;
            },

            handleChangeUserPicOpen: function () {
                var userPhoto = this.teacherForm.user.photo;
                this.dialogVisibleChangeUserPic = true;
                this.$nextTick(function () {
                    this.userAvatar = this.addFtpHttp(userPhoto) || '/img/u4110.png';
                })
            },

            getUserPicFile: function (file) {
                this.userPicFile = file;
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
                formData.append('upfile', this.userPicFile);
                self.$axios({
                    method: 'POST',
                    url: '/ftp/ueditorUpload/cutImgToTempDir?folder=user&x=' + parseInt(data.x) + '&y=' + parseInt(data.y) + '&width=' + parseInt(data.width) + '&height=' + parseInt(data.height),
                    data: formData
                }).then(function (response) {
                    var data = response.data;
                    if (data.state === 'SUCCESS') {
                        self.moveFile(data.ftpUrl);
                    } else {
                        self.userPicFile = null;
                        self.isUpdating = false;
                        self.dialogVisibleChangeUserPic = false;
                        self.$message({
                            type: 'error',
                            message: data.msg
                        })
                    }
                }).catch(function (error) {
                    self.isUpdating = false;
                })
            },

            moveFile: function (url) {
                var self = this;
                if (!this.teacherForm.user.id) {
                    this.teacherForm.user.photo = url;
                    this.dialogVisibleChangeUserPic = false;
                    this.userAvatar = this.addFtpHttp(url);
                    this.userPicFile = null;
                    this.isUpdating = false;
                    return false;
                }
                return this.$axios.post('/sys/backTeacherExpansion/ajaxUpdatePhoto?photo=' + url + "&userId=" + this.teacherForm.user.id).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        data = data.datas;
                        self.dialogVisibleChangeUserPic = false;
                        self.userAvatar = self.addFtpHttp(data.photo);
                        self.teacherForm.user.photo = data.photo;
                        self.userPicFile = null;
                        self.$message.success(response.data.msg);
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.isUpdating = false;

                }).catch(function (error) {
                    self.isUpdating = false;
                })
            },


            handleCityPickerClose: function () {
                this.dialogVisibleCityPicker = false;
                this.cityTab = 'hotCities'
            },

            cityHandleSelected: function () {
                this.dialogVisibleCityPicker = false;
            },


            handleCloseKeyWord: function (tag, index) {
                this.teacherForm.keywords.splice(index, 1);
            },

            showInputKeyWord: function () {
                this.inputKeyWordVisible = true;
                this.$nextTick(function () {
                    this.$refs.saveKeyWordInput.$refs.input.focus();
                });
            },
            handleInputKeyWordConfirm: function () {
                var inputValue = this.keyWord;
                if (inputValue) {
                    if (inputValue.length > 12) {
                        this.$message({
                            type: 'error',
                            message: '请输入小于12个字符的关键字'
                        })
                        return false;
                    }
                    if (this.hasEveryKeyword(inputValue)) {
                        this.teacherForm.keywords.push(inputValue);
                        this.inputKeyWordVisible = false;
                        this.keyWord = '';
                    } else {
                        this.$message({
                            type: 'error',
                            message: '存在相同的关键字'
                        })
                    }
                } else {
                    this.inputKeyWordVisible = false;
                    this.keyWord = '';
                }

            },

            hasEveryKeyword: function (value) {
                return this.teacherForm.keywords.every(function (item) {
                    return item.keyword !== value;
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
            },
            getOfficeList: function () {
                var self = this;
                this.$axios.get('/sys/office/getOfficeList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var officeList = data.data || [];
                        var treeEntity = self.listToTreeEntity(officeList);
                        self.officeTree = JSON.parse(JSON.stringify(treeEntity.tree));
                        self.officeEntity = treeEntity.entity;
                    }
                    self.getTeaExDataBySId();
                    self.getDictLists();
                })
            },
            getTeaExDataBySId: function () {
                var self = this;
                if (!this.userAsExpertId) {
                    this.loading = false;
                    return;
                }
                this.loading = true;
                this.$axios.get('/sys/backTeacherExpansion/getStuExDataByUserId/' + this.userAsExpertId).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var teacher = data.data || {};
                        self.setTeacherForm(teacher);
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                })
            },
            setTeacherForm: function (data) {
                var parentIds;
                if (data.user.birthday) {
                    data.user.birthday = moment(data.user.birthday).format('YYYY-MM')
                }
                var professional = data.user.professional || data.user.officeId;
                if (professional && this.officeEntity[professional]) {
                    parentIds = this.officeEntity[professional].parentIds;
                }
                if (parentIds) {
                    parentIds = parentIds.split(',');
                    parentIds = parentIds.slice(1, parentIds.length - 1);
                    parentIds.push(professional);
                    data.user.professionalIds = parentIds
                }
                this.assignFormData(this.teacherForm, data);
            },
            getDictLists: function () {
                var dicts = {
                    'expert_source': 'expertSources',
                    'expert_type': 'masterTypes',
                    'sex': 'sexes',
                    'postTitle_type': 'expertJobNames',
                    'enducation_level': 'enducationLevels',
                    'degree_type': 'degreeTypes',
                    'master_help': 'masterServices',
                    'id_type': 'idTypes',
                    'enducation_type': 'educationTypes',
                    'professional_type': 'professionalTypes',
                    '0000000218': 'workTypes',
//                    '0000000215': 'teacherCategories',
                    'yes_no': 'yesNo',
                    'technology_field': 'technologyFields'
                };
                this.getBatchDictList(dicts)
            },

            ajaxGetUserProjectById: function () {
                var self = this;
                this.$axios.get('/sys/studentExpansion/ajaxGetUserProjectById?userId=' + this.teacherForm.user.id).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.projectList = data.datas || [];
                    }
                })
            },

            ajaxGetUserGContestById: function () {
                var self = this;
                this.$axios.get('/sys/studentExpansion/ajaxGetUserGContestById?userId=' + this.teacherForm.user.id).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.contestList = data.datas || [];
                    }
                })
            }
        },
        created: function () {
            this.getOfficeList();
            if(this.teacherForm.user.id){
                this.ajaxGetUserProjectById();
                this.ajaxGetUserGContestById();
            }
        }
    })

</script>

</body>
</html>