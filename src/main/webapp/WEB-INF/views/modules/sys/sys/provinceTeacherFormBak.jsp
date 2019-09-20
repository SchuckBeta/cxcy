<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative-province.jsp" %>
    <style>

        .user_detail-container .el-form-builder .el-col {
            height: auto;
        }
        .edit-bar-province{
            padding-left: 10px;
            padding-right: 0;
            margin: 0;
        }
        .content-container{
            padding: 10px;
        }
    </style>
</head>

<body>
<div id="app" v-show="pageLoad" style="display: none">
    <edit-bar-province :second-name="teacherForm.id ? '编辑': '添加'" href="/sys/backTeacherExpansion/toProvinceTeacherList"></edit-bar-province>
    <div class="content-container">
        <div class="user_avatar-sidebar">
            <div class="user-avatar">
                <div class="avatar-pic">
                    <img :src="userPhoto | ftpHttpFilter(ftpHttp) | studentPicFilter">
                </div>
                <el-button type="primary" size="mini"
                           @click.stop.prevent="handleChangeUserPicOpen">{{userPhoto ? '更换' : '添加'}}图像
                </el-button>
            </div>
        </div>
        <div class="user_detail-container">
            <div class="user_detail-title">
                <i class="iconfont icon-user"></i><span class="text">导师基本信息</span>
            </div>
            <div class="user_detail-wrap">
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
                    <el-form :model="teacherForm" ref="teacherForm" :rules="teacherRules" autocomplete="off"
                             :disabled="isUpdating" size="mini" label-width="90px" class="el-form-builder">
                        <input type="hidden" v-if="userPhoto.indexOf('/tool/oseasy/temp/user') > -1" name="user.photo"
                               :value="userPhoto">
                        <input type="hidden" name="id" :value="teacherForm.id">
                        <input type="hidden" name="userid" :value="teacherForm.userid">
                        <el-row :gutter="10">
                            <el-col :span="8">
                                <el-form-item prop="userName" label="姓名：">
                                    <input type="hidden" name="user.name" :value="teacherForm.userName">
                                    <el-input v-model="teacherForm.userName"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item prop="userSex" label="性别：">
                                    <input type="hidden" name="user.sex" :value="teacherForm.userSex">
                                    <el-radio-group v-model="teacherForm.userSex">
                                        <el-radio-button v-for="item in sexes" :key="item.value" :label="item.value"
                                                         style="vertical-align: top">{{item.label}}
                                        </el-radio-button>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                            <%--<el-col :span="8">--%>
                                <%--<el-form-item prop="userNo" label="职工号：">--%>
                                    <%--<input type="hidden" name="user.no" :value="teacherForm.userNo">--%>
                                    <%--<el-input v-model="teacherForm.userNo"></el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <el-col :span="16">
                                <el-form-item label="证件号：" class="teacherform-id-type"
                                              :rules="teacherRules[teacherForm.userIdType == 1 ? 'idNumberIdentity' : 'idNumberPassport']">
                                    <input type="hidden" name="user.idType" :value="teacherForm.userIdType">
                                    <input type="hidden" name="user.idNumber" :value="teacherForm.userIdNumber">
                                    <el-input placeholder="请输入证件号" v-model="teacherForm.userIdNumber"
                                              class="input-with-select_identity">
                                        <el-form-item prop="userIdType" slot="prepend"
                                                      style="margin-bottom: 0;height: 26px;">
                                            <el-select v-model="teacherForm.userIdType" placeholder="请选择证件类型">
                                                <el-option v-for="idType in idTypes" :label="idType.label"
                                                           :value="idType.value" :key="idType.id"></el-option>
                                            </el-select>
                                        </el-form-item>
                                    </el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item prop="userMobile" label="手机号：">
                                    <el-input name="user.mobile" v-model="teacherForm.userMobile"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item prop="userEmail" label="邮箱：">
                                    <input type="hidden" name="user.email" :value="teacherForm.userEmail">
                                    <el-input v-model="teacherForm.userEmail"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item prop="workUnit" label="工作单位：">
                                    <el-input name="workUnit" v-model="teacherForm.workUnit"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item prop="firstBank" label="从业年限：">
                                    <el-input name="firstBank" v-model="teacherForm.firstBank">
                                        <template slot="append">年</template>
                                    </el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item prop="postTitle" label="职务：">
                                    <%--<el-input name="postTitle" v-model="teacherForm.postTitle"></el-input>--%>
                                    <el-select name="postTitle" v-model="teacherForm.postTitle" clearable>
                                        <el-option v-for="item in postTitles" :key="item.id" :value="item.label"
                                                   :label="item.label"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item prop="industry" label="行业：">
                                    <%--<el-input name="industry" v-model="teacherForm.industry"></el-input>--%>
                                    <el-select name="industry" v-model="teacherForm.industry" clearable>
                                        <el-option v-for="item in industries" :key="item.id" :value="item.label"
                                                   :label="item.label"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item prop="recommendedUnits" label="推荐机构：">
                                    <%--<el-input name="recommendedUnits" v-model="teacherForm.recommendedUnits"></el-input>--%>
                                    <el-select name="recommendedUnits" v-model="teacherForm.recommendedUnits" clearable>
                                        <el-option v-for="item in recommendedUnitsDict" :key="item.id" :value="item.label"
                                                   :label="item.label"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                <el-form-item label="擅长领域：" prop="userDomainIdList"
                                >
                                    <input style="display: none" type="checkbox" name="user.domainIdList"
                                           v-for="item in teacherForm.userDomainIdList" checked :value="item">
                                    <el-select v-model="teacherForm.userDomainIdList" name multiple placeholder="请选择"
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
                                <el-form-item prop="serviceIntentionIds" label="服务意向：">
                                    <el-checkbox-group v-model="teacherForm.serviceIntentionIds" class="multi-row">
                                        <el-checkbox v-for="item in masterServices" name="serviceIntentionIds"
                                                     :key="item.value"
                                                     :label="item.value">{{item.label}}
                                        </el-checkbox>
                                    </el-checkbox-group>
                                </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                <el-form-item label="个人简历：">
                                    <el-input type="textarea" name="mainExp" :rows="3" maxlength="1024"
                                              v-model="teacherForm.mainExp"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                </div>
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
                                        <e-col-item label="项目周期：" label-width="72px">{{getProjectRange(project)}}
                                            <%--</e-col-item>--%>
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
                        <div v-show="contestList.length < 1" class="text-center">
                            <div class="user_experience-title none">
                                <span>暂无指导大赛经历</span>
                            </div>
                        </div>
                    </el-row>
                </div>

                <review-task :user-id="teacherForm.userid"></review-task>


            </div>
        </div>
    </div>

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
        mixins: [Vue.collegesMixin, Vue.verifyExpressionBackMixin],
        data: function () {
            var teacherData = JSON.parse(JSON.stringify(${fns: toJson(teacherData)})) || {};
            var backTeacherExpansion = teacherData.backTeacherExpansion;
            var user = backTeacherExpansion.user || {};
            var originalMobile = user.mobile;
            var keywords = teacherData.tes || [];
            keywords = keywords.map(function (item) {
                return item.keyword;
            });
            var projectList = teacherData.projectExpVo || [];
            var contestList = teacherData.gContestExpVo || [];
            var masterTypes = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('master_type'))}));
            var sexes = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('sex'))}));
            var masterServices = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList('master_help'))}));
            var idTypes = JSON.parse(JSON.stringify(${fns:toJson(fns:getDictList('id_type'))})) || [];
            var educationTypes = JSON.parse(JSON.stringify(${fns:toJson(fns:getDictList('enducation_type'))})) || [];
            var enducationLevels = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('enducation_level'))}));
            var professionalTypes = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList('professional_type'))}));
            var degreeTypes = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList('degree_type'))}));
            var colleges = JSON.parse(JSON.stringify(${fns: toJson(fns: getOfficeList())})) || [];
            var workTypes = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList('0000000218'))})) || [];
            var yesNo = JSON.parse(JSON.stringify(${fns:toJson(fns:getDictList('yes_no'))}));
            var technologyFields = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("technology_field"))})) || [];
            var teacherCategories = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList('0000000215'))}));
            var technical = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("postTitle_type"))})) || [];
            var industries = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('20190506142903100035'))}));
            var postTitles = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('20190506144023100046'))}));
            var recommendedUnitsDict = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('20190507144620100060'))}));
            var userBirthday = user.birthday ? moment(user.birthday).format('YYYY-MM-DD') : '';
            var userProfessional = [];
            //            colleges = colleges.filter(function (item) {
//                return item.id !== '1';
//            });

            if (user.officeId) {
                userProfessional.push('1');
                userProfessional.push(user.officeId);
            }
            if (user.professional) {
                userProfessional.push(user.professional)
            }


            return {
                teacherForm: {
                    id: backTeacherExpansion.id,
                    userid: user.id,
//                    userPhoto: user.photo,
                    teachertype: backTeacherExpansion.teachertype == '0' || !backTeacherExpansion.teachertype ? '' : backTeacherExpansion.teachertype,
                    userName: user.name,
//                    userLoginName: user.loginName,
                    userNo: user.no,
                    userSex: user.sex || '1',
                    userBirthday: userBirthday,
                    userCountry: user.area,
                    userNational: user.national,
                    userPolitical: user.political,
                    userMobile: user.mobile,
                    serviceIntentionIds: backTeacherExpansion.serviceIntentionIds || [],
                    userIdType: user.idType || '1',
                    userIdNumber: user.idNumber,
                    category: backTeacherExpansion.category,
                    educationType: backTeacherExpansion.educationType,
                    userEducation: user.education,
                    discipline: backTeacherExpansion.discipline ? backTeacherExpansion.discipline.toString() : '',
                    userDegree: user.degree,
                    userProfessional: userProfessional,
                    industry: backTeacherExpansion.industry,
                    postTitle: backTeacherExpansion.postTitle,
                    technicalTitle: backTeacherExpansion.technicalTitle,
                    firstBank: backTeacherExpansion.firstBank,
                    bankAccount: backTeacherExpansion.bankAccount,
                    userEmail: user.email,
                    workUnitType: backTeacherExpansion.workUnitType,
                    workUnit: backTeacherExpansion.workUnit,
                    recommendedUnits: backTeacherExpansion.recommendedUnits,
                    firstShow: backTeacherExpansion.firstShow,
                    siteShow: backTeacherExpansion.siteShow,
                    topShow: backTeacherExpansion.topShow,
                    userDomainIdList: user.domainIdList,
                    keywords: keywords || [],
                    mainExp: backTeacherExpansion.mainExp,
                    roleType: backTeacherExpansion.roleType || '1'
                },
                originalMobile: originalMobile,

                userPhoto: user.photo || '',
                userAvatar: '',
                userPicFile: null,
                isUpdating: false,

                expertJobNames: technical,
                masterTypes: masterTypes,
                sexes: sexes,
                masterServices: masterServices,
                idTypes: idTypes,
                educationTypes: educationTypes,
                enducationLevels: enducationLevels,
                professionalTypes: professionalTypes,
                degreeTypes: degreeTypes,
                colleges: colleges,
                workTypes: workTypes,
                yesNo: yesNo,
                technologyFields: technologyFields,
                teacherCategories: teacherCategories,
                industries: industries,
                postTitles: postTitles,
                recommendedUnitsDict: recommendedUnitsDict,
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

                projectList: projectList,
                contestList: contestList,

                cascaderCollegesProps: {
                    label: 'name',
                    value: 'id',
                    children: 'children'
                },
                saveMessage: '${message}'
            }
        },
        computed: {
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

            officeId: function () {
                var userProfessional = this.teacherForm.userProfessional;
                if (this.professionalId && userProfessional.length - 2 > -1) {
                    return userProfessional[userProfessional.length - 2];
                }
                return ''
            },

            professionalId: function () {
                var userProfessional = this.teacherForm.userProfessional;
                if (userProfessional && userProfessional.length != 0) {
                    return userProfessional[userProfessional.length - 1];
                }
            },
            teacherRules: {
                get: function () {
                    var self = this;
                    var validateUserName = this.validateUserName;
                    var validateTeacherFormUserNo = this.validateTeacherFormUserNo;
                    var isCompanyMaster = this.teacherForm.teachertype === '2';
                    var validateMobile = this.validateMobile;
                    var validateTeacherFormMobileXhr = this.validateTeacherFormMobileXhr;
                    var validateIdentity = this.validateIdentity;
                    var validateEmail = this.validateEmail;
                    var validateLoginName = this.validateLoginName;
                    var validateFirstBank = this.validateFirstBank;
                    var hasMasterType = this.teacherForm.serviceIntentionIds.indexOf('4') > -1;
                    return {
                        'teachertype': [{required: true, message: '请选择导师来源', trigger: 'blur'}],
                        'userName': [
                            {required: true, message: '请输入姓名', trigger: 'blur'},
                            {max: 15, message: '请输入长度在 1 到 15 个字符', trigger: 'blur'},
                            {validator: validateUserName, trigger: 'blur'}
                        ],
//                        'userLoginName': [
//                            {required: true, message: '请输入登录名', trigger: 'blur'},
//                            {validator: validateLoginName, trigger: 'blur'}
//                        ],
                        'userNo': [
                            {required: !isCompanyMaster, message: '请输入职工号', trigger: 'blur'},
                            {min: 2, max: 24, message: '请输入1-24位字符的职工号', trigger: 'blur'},
                            {validator: validateTeacherFormUserNo, trigger: 'blur'}
                        ],
                        'userSex': [
                            {required: true, message: '请选择性别', trigger: 'change'},
                        ],
                        'userNational': [
                            {max: 24, message: '请输入不大于24位字符的民族', trigger: 'blur'},
                        ],
                        'userMobile': [
                            {required: true, message: '请输入手机号', trigger: 'blur'},
                            {validator: validateMobile, trigger: 'blur'},
                            {validator: self.originalMobile != self.teacherForm.userMobile && validateTeacherFormMobileXhr, trigger: 'blur'}
                        ],
                        'serviceIntentionIds': [
                            {required: true, message: '请选择服务意向', trigger: 'change'},
                        ],
                        'category': [
                            {required: hasMasterType, message: '请选择导师类型', trigger: 'change'},
                        ],
                        'userIdType': [
                            {required: true, message: '请选择证件类别', trigger: 'change'}
                        ],
                        'idNumberIdentity': [
                            {required: true, message: '请输入身份证号码', trigger: 'blur'},
                            {validator: validateIdentity, trigger: 'blur'}
                        ],
                        'idNumberPassport': [
                            {required: true, message: '请输入护照号码', trigger: 'blur'},
                            {min: 6, max: 24, message: '请输入6-24位字符', trigger: 'blur'}
                        ],
                        'educationType': [
                            {required: !isCompanyMaster, message: '请选择学历类别', trigger: 'change'}
                        ],
                        'userEducation': [
                            {required: !isCompanyMaster, message: '请选择学历', trigger: 'change'}
                        ],
                        'industry': [
                            {required: true, message: '请选择行业', trigger: 'change'},
//                            {max: 48, message: '请输入不大于48位字符的行业', trigger: 'blur'},
                        ],
                        'postTitle': [
                            {required: true, message: '请选择职务', trigger: 'change'},
//                            {max: 48, message: '请输入不大于48位字符的职务', trigger: 'blur'},
                        ],
                        'technicalTitle': [
                            {max: 48, message: '请输入不大于48位字符的职称', trigger: 'blur'},
                        ],
                        'firstBank': [
                            {validator: validateFirstBank, trigger: 'blur'}
                        ],
                        'bankAccount': [
                            {max: 128, message: '请输入不大于128位字符的账号', trigger: 'blur'},
                        ],
                        'userEmail': [
                            {required: true, message: '请输入邮箱地址', trigger: 'blur'},
                            {validator: validateEmail, trigger: 'blur'}
                        ],
                        'workUnit': [
                            {required: true, message: '请输入工作单位', trigger: 'blur'},
                            {max: 128, message: '请输入不大于128位字符单位名称', trigger: 'blur'},
                        ],
                        'userDomainIdList': [
                            {required: true, message: '请选择擅长领域', trigger: 'change'}
                        ],
                        'recommendedUnits': [
                            {required: true, message: '请选择推荐机构', trigger: 'change'},
//                            {max: 128, message: '请输入不大于128位字符的推荐机构名称', trigger: 'blur'},
                        ],
                        'experience': [
                            {max: 2000, message: '请输入不大于2000位字符的个人简介', trigger: 'blur'},
                        ]
                    }
                }
            }
        },

        methods: {


            validateForm: function () {
                var self = this;
                this.$refs.teacherForm.validate(function (valid) {
                    if (valid) {
                        self.submitTeacherForm();
                    }
                });
            },
            submitTeacherForm: function () {
                var self = this, dataForm = {};
                dataForm = {
                    id: this.teacherForm.id,
                    user: {
                        id: this.teacherForm.userid,
                        userType: '2',
//                        photo: this.teacherForm.userPhoto,
                        name: this.teacherForm.userName,
//                        loginName:this.teacherForm.userLoginName,
                        no: this.teacherForm.userNo,
                        sex: this.teacherForm.userSex,
                        birthday: this.teacherForm.userBirthday,
                        area: this.teacherForm.userCountry,
                        national: this.teacherForm.userNational,
                        political: this.teacherForm.userPolitical,
                        mobile: this.teacherForm.userMobile,
                        idType: this.teacherForm.userIdType,
                        idNumber: this.teacherForm.userIdNumber,
                        education: this.teacherForm.userEducation,
                        degree: this.teacherForm.userDegree,
                        officeId: this.officeId,
                        professional: this.professionalId,
                        email: this.teacherForm.userEmail,
                        domainIdList: this.teacherForm.userDomainIdList
                    },
                    teachertype: this.teacherForm.teachertype,
                    serviceIntentionIds: this.teacherForm.serviceIntentionIds,
                    category: this.teacherForm.category,
                    educationType: this.teacherForm.educationType,
                    discipline: this.teacherForm.discipline,
                    industry: this.teacherForm.industry,
                    postTitle: this.teacherForm.postTitle,
                    technicalTitle: this.teacherForm.technicalTitle,
                    firstBank: this.teacherForm.firstBank,
                    bankAccount: this.teacherForm.bankAccount,
                    workUnitType: this.teacherForm.workUnitType,
                    workUnit: this.teacherForm.workUnit,
                    recommendedUnits: this.teacherForm.recommendedUnits,
                    firstShow: this.teacherForm.firstShow,
                    siteShow: this.teacherForm.siteShow,
                    topShow: this.teacherForm.topShow,
                    keywords: this.teacherForm.keywords,
                    mainExp: this.teacherForm.mainExp,
                    roleType: this.teacherForm.roleType
                };
//                if (this.userPhoto.indexOf('/tool/oseasy/temp/user') > -1) {
                    dataForm.user.photo = this.userPhoto;
//                }

                this.$nextTick(function () {
                    this.isUpdating = true;
                    this.$axios({
                        method: 'POST',
                        url: '/sys/backTeacherExpansion/saveExpertOrTea',
                        data: dataForm
                    }).then(function (response) {
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
                                window.location.href = self.frontOrAdmin + '/sys/backTeacherExpansion/toProvinceTeacherList';
                            }).catch(function () {

                            });
                        }else {
                            self.$alert(data.msg, {
                                type: 'error'
                            })
                        }
                        self.isUpdating = false;
                    }).catch(function (error) {
                        self.isUpdating = false;
                        self.$message({
                            message: self.xhrErrorMsg,
                            type: 'error'
                        })
                    });
                })
            },

            goToBack: function () {
                return window.history.go(-1);
            },

            handleChangeUserPicClose: function () {
                this.userPicFile = null;
                this.dialogVisibleChangeUserPic = false;
            },

            handleChangeUserPicOpen: function () {
                var userPhoto = this.userPhoto;
                this.dialogVisibleChangeUserPic = true;
                this.$nextTick(function () {
                    this.userAvatar =  this.addFtpHttp(userPhoto) || '/img/u4110.png';
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
//                    var data = response.data;
//                    if (data.state === 'SUCCESS') {
//                        self.dialogVisibleChangeUserPic = false;
//                        self.userAvatar = self.addFtpHttp(data.ftpUrl);
//                        self.userPhoto = data.ftpUrl;
//                        self.userPicFile = null;
//                    }
//                    self.isUpdating = false;
                }).catch(function (error) {
                    self.isUpdating = false;
                    self.$message({
                        type: 'error',
                        message: error.response.data
                    })
                })
            },

            moveFile: function (url) {
                var self = this;
                if(!this.teacherForm.userid){
                    this.userPhoto = url;
                    this.dialogVisibleChangeUserPic = false;
                    this.userAvatar = this.addFtpHttp(url);
                    this.userPicFile = null;
                    this.isUpdating = false;
                    return false;
                }
                return this.$axios.post('/sys/backTeacherExpansion/ajaxUpdatePhoto?photo=' + url + "&userId=" + this.teacherForm.userid).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        data = data.datas;
                        self.dialogVisibleChangeUserPic = false;
                        self.userAvatar = self.addFtpHttp(data.photo);
                        self.userPhoto = data.photo;
                        self.userPicFile = null;
                        self.$message.success(response.data.msg);
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.isUpdating = false;

                }).catch(function (error) {
                    self.isUpdating = false;
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    })
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
            }
        },
        mounted: function () {
            if (this.saveMessage) {
                this.$alert(this.saveMessage, '提示', {
                    type: 'error'
                }).catch(function () {

                });
                this.saveMessage = '';
            }
        }
    })

</script>

</body>
</html>