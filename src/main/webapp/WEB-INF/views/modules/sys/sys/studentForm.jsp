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
        <edit-bar :second-name="studentForm.user.id ? '编辑': '添加'" href="/sys/studentExpansion"></edit-bar>
    </div>
    <div v-loading="loading">
        <div class="user_avatar-sidebar">
            <div class="user-avatar">
                <div class="avatar-pic">
                    <img :src="studentForm.user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter">
                </div>
                <el-button type="primary" size="mini"
                           @click.stop="handleChangeUserPicOpen">{{studentForm.user.photo ? '更换' : '添加'}}图像
                </el-button>
            </div>
        </div>
        <div class="user_detail-container">
            <div class="user_detail-title">
                <i class="iconfont icon-user"></i><span class="text">学生基本信息</span>
            </div>
            <div class="user_detail-wrap">
                <div class="user_detail-inner">
                    <div class="user_detail-title-handler">
                        <div class="ud-row-handler">
                            <el-button type="primary" title="保存" size="mini" :disabled="disabled"
                                       @click.stop="validateForm">保存
                            </el-button>
                            <el-button :disabled="disabled" size="mini" @click.stop="goToBack">返回
                            </el-button>
                        </div>
                    </div>
                    <el-form :model="studentForm" ref="studentForm" :disabled="disabled" size="mini"
                             class="el-form-builder" autocomplete="off"
                             label-width="90px">
                        <el-row :gutter="10">
                            <el-col :span="8">
                                <el-form-item label="姓名：" prop="user.name" :rules="userNameRules">
                                    <el-input name="user.name" v-model="studentForm.user.name"
                                              maxlength="64"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="学号：" prop="user.no" :rules="userAsStuNoRules">
                                    <el-input name="user.no" v-model="studentForm.user.no" placeholder="请输入学号"
                                              maxlength="64"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="性别：" prop="user.sex">
                                    <el-radio-group v-model="studentForm.user.sex">
                                        <el-radio-button v-for="item in sexes" :key="item.value" :label="item.value">
                                            {{item.label}}
                                        </el-radio-button>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="院系/专业："
                                              prop="user.professionalIds" :rules="stuProfessionalRules">
                                    <el-cascader
                                            style="width: 100%"
                                            ref="cascader"
                                            :options="officeTree"
                                            :clearable="true"
                                            filterable
                                            v-model="studentForm.user.professionalIds"
                                            :props="professionalDefaultTree">
                                    </el-cascader>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="出生时间：" prop="user.birthday">
                                    <el-date-picker
                                            v-model="studentForm.user.birthday"
                                            type="month"
                                            :editable="false"
                                            value-format="yyyy-MM"
                                            :default-value="defaultBirthdayDate"
                                            :picker-options="userBirthdayPickerOptions"
                                            placeholder="选择出生时间">
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="电子邮箱：" prop="user.email" :rules="emailRules">
                                    <el-input name="user.email" v-model="studentForm.user.email"
                                              maxlength="128"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="手机号：" prop="user.mobile" :rules="mobileRules">
                                    <el-input name="user.mobile" v-model="studentForm.user.mobile"
                                              maxlength="11"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="国家/户籍：" prop="user.country"
                                              class="el-input--mini">
                                    <city-dropdown v-model="studentForm.user.country" class-name="el-input__inner"
                                                   placeholder="填写/选择"
                                                   :city-data="cityIdKeyData">
                                        <a slot="rightSelected" href="javascript:void (0)"
                                           @click="dialogVisibleCityPicker=true">选择</a>
                                    </city-dropdown>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="民族：" prop="user.national">
                                    <el-input name="user.national" v-model="studentForm.user.national"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="政治面貌：" prop="user.political">
                                    <el-select v-model="studentForm.user.political" placeholder="请选择" clearable>
                                        <el-option
                                                v-for="item in politicsStatuses"
                                                :key="item"
                                                :label="item"
                                                :value="item">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="16">
                                <el-form-item label="证件号：" prop="user.idNumber"
                                              :rules="studentForm.user.idType === 1 ? identityRules : idNumberPassport">
                                    <el-input placeholder="请输入证件号" v-model="studentForm.user.idNumber"
                                              class="input-with-select_identity">
                                        <el-form-item prop="idType" slot="prepend"
                                                      style="margin-bottom: 0;height: 26px;">
                                            <el-select v-model="studentForm.user.idType" placeholder="请选择证件类型">
                                                <el-option v-for="idType in idTypes" :label="idType.label"
                                                           :value="idType.value" :key="idType.id"></el-option>
                                            </el-select>
                                        </el-form-item>
                                    </el-input>
                                </el-form-item>
                            </el-col>


                            <el-col :span="8">
                                <el-form-item label="QQ：" prop="user.qq" :rules="qqRules">
                                    <el-input name="user.qq" placeholder="请输入QQ号码" maxlength="64"
                                              v-model="studentForm.user.qq"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                <el-form-item label="联系地址：" prop="address">
                                    <el-input name="address" placeholder="请输入联系地址，限128字" maxlength="200"
                                              v-model="studentForm.address"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="24" style="height: auto;">
                                <el-form-item label="个人简介：" prop="user.introduction">
                                    <el-input name="user.introduction" type="textarea" :rows="2"
                                              placeholder="请输入个人简介,限200字" maxlength="200"
                                              v-model="studentForm.user.introduction"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="入学年月：" prop="enterdate">
                                    <el-date-picker
                                            v-model="studentForm.enterdate"
                                            value-format="yyyy-MM-dd"
                                            :default-value="defaultEnterDate"
                                            type="month"
                                            :editable="false"
                                            :picker-options="enterdatePickerOptions"
                                            @change="studentForm.currState = ''"
                                            placeholder="选择入学年月">
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="学年制：" prop="cycle">
                                    <el-select v-model="studentForm.cycle" @change="studentForm.currState = ''"
                                               placeholder="-请选择-">
                                        <el-option v-for="cycle in cycles" :value="cycle.value" :label="cycle.label+'年'"
                                                   :key="cycle.id"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="现状：" prop="currState">
                                    <el-select v-model="studentForm.currState" placeholder="-请选择-" clearable>
                                        <el-option v-for="currState in currStatesCp" :value="currState.value"
                                                   :disabled="currState.disabled"
                                                   :label="currState.label" :key="currState.id"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col v-if="isInCurStates('tClass')" :span="8">
                                <el-form-item label="班级：" prop="tClass">
                                    <el-input name="tClass" v-model="studentForm.tClass" placeholder="请输入班级，限64字符"
                                              maxlength="64"> <span slot="append">班</span></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col v-if="isInCurStates('user.education')" :span="8">
                                <el-form-item label="学历：" prop="user.education">
                                    <el-select v-model="studentForm.user.education" placeholder="-请选择-">
                                        <el-option v-for="enducationLevel in enducationLevels"
                                                   :value="enducationLevel.value" :label="enducationLevel.label"
                                                   :key="enducationLevel.id"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col v-if="isInCurStates('user.degree')" :span="8">
                                <el-form-item label="学位：" prop="user.degree">
                                    <el-select v-model="studentForm.user.degree" placeholder="-请选择-" clearable>
                                        <el-option v-for="degreeType in degreeTypes" :value="degreeType.value"
                                                   :label="degreeType.label" :key="degreeType.id"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col v-if="isInCurStates('instudy')" :span="8">
                                <el-form-item label="在读学位：" prop="instudy">
                                    <el-select v-model="studentForm.instudy" placeholder="-请选择-" clearable>
                                        <el-option v-for="degreeType in degreeTypes" :value="degreeType.value"
                                                   :label="degreeType.label" :key="degreeType.id"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8" v-if="isInCurStates('temporaryDate')">
                                <el-form-item label="休学时间：" prop="temporaryDate">
                                    <el-date-picker
                                            v-model="studentForm.temporaryDate"
                                            type="date"
                                            value-format="yyyy-MM-dd"
                                            :editable="false"
                                            :picker-options="temporaryDatePickerOption"
                                            placeholder="选择休学时间">
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8" v-if="isInCurStates('graduation')">
                                <el-form-item label="毕业时间：" prop="graduation">
                                    <el-date-picker
                                            v-model="studentForm.graduation"
                                            type="date"
                                            value-format="yyyy-MM-dd"
                                            :editable="false"
                                            :picker-options="graduationDatePickerOption"
                                            placeholder="选择毕业时间">
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="24">
                                <el-form-item label="技术领域：" prop="user.domainIdList">
                                    <el-select v-model="studentForm.user.domainIdList" name multiple placeholder="请选择"
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
                        </el-row>
                    </el-form>
                </div>
                <template v-if="!!studentForm.user.id">
                    <div class="user_detail-inner user_detail-inner-experience">
                        <div class="user_detail-title-handler">
                            <div class="ud-row-title"><span class="name">项目经历</span></div>
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
                                            <e-col-item label="担任角色：" label-width="72px">{{ getRoleName(project) }}
                                            </e-col-item>
                                            <e-col-item label="项目级别：" label-width="72px">{{project.level}}</e-col-item>
                                            <e-col-item label="项目结果：" label-width="72px">{{project.result}}</e-col-item>
                                            <e-col-item label="项目周期：" label-width="72px">{{getProjectRange(project)}}
                                            </e-col-item>
                                        </div>
                                        <div class="exp-intro">
                                            <e-col-item label="项目简介：" label-width="72px"
                                                        class="white-space-pre-static word-break">
                                                <span v-html="project.introduction"></span>
                                            </e-col-item>
                                        </div>
                                    </div>
                                    <div class="experience-card-footer">
                                        <div class="text-right">
                                            <el-tag v-show="project.proName" type="info" size="mini">{{project.proName}}
                                            </el-tag>
                                            <el-tag v-show="project.year" type="info" size="mini">{{project.year}}
                                            </el-tag>
                                        </div>
                                    </div>
                                </div>
                            </el-col>
                            <div v-show="!projectList.length" class="text-center">
                                <div class="user_experience-title none">
                                    <span>暂无项目经历</span>
                                </div>
                            </div>
                        </el-row>
                    </div>

                    <div class="user_detail-inner user_detail-inner-experience">
                        <div class="user_detail-title-handler">
                            <div class="ud-row-title"><span class="name">大赛经历</span></div>
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
                                            <e-col-item label="担任角色：" label-width="72px">{{ getRoleName(contest) }}
                                            </e-col-item>
                                            <e-col-item label="大赛级别：" label-width="72px">{{contest.level}}</e-col-item>
                                            <e-col-item label="大赛获奖：" label-width="72px">{{contest.award}}</e-col-item>
                                            <e-col-item label="大赛周期：" label-width="72px">{{getProjectRange(contest)}}
                                            </e-col-item>
                                        </div>
                                        <div class="exp-intro">
                                            <e-col-item label="大赛简介：" label-width="72px"
                                                        class="white-space-pre-static word-break">
                                                <span v-html="contest.introduction"></span>
                                            </e-col-item>
                                        </div>
                                    </div>
                                    <div class="experience-card-footer">
                                        <div class="text-right">
                                            <el-tag v-show="contest.type" type="info" size="mini">{{contest.type}}
                                            </el-tag>
                                            <el-tag v-show="contest.year" type="info" size="mini">{{contest.year}}
                                            </el-tag>
                                        </div>
                                    </div>
                                </div>
                            </el-col>
                            <div v-show="contestList.length < 1" class="text-center">
                                <div class="user_experience-title none">
                                    <span>暂无大赛经历</span>
                                </div>
                            </div>
                        </el-row>
                    </div>


                    <skill-cert :user-id="studentForm.user.id"></skill-cert>

                    <personal-score :user-id="studentForm.user.id"></personal-score>
                </template>
            </div>
        </div>
    </div>
    <el-dialog title="上传图像"
               width="440px"
               :visible.sync="dialogVisibleChangeUserPic"
               :close-on-click-modal="false"
               :before-close="handleChangeUserPicClose">
        <e-pic-file v-model="userAvatar" :disabled="disabled" @get-file="getUserPicFile"></e-pic-file>
        <cropper-pic :img-src="userAvatar" :disabled="disabled" ref="cropperPic"></cropper-pic>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="!userPicFile || disabled" type="primary"
                       @click.stop.prevent="updateUserPic">上传
            </el-button>
        </div>
    </el-dialog>
    <el-dialog
            title="选择城市"
            :visible.sync="dialogVisibleCityPicker"
            :close-on-click-modal="false"
            :before-close="handleCityPickerClose">
        <city-picker v-model="studentForm.user.country" :city-data="city" :tab-active-key.sync="cityTab"
                     @selected="cityHandleSelected"></city-picker>
    </el-dialog>
</div>

<script>
    'use strict';
    new Vue({
        el: '#app',
        mixins: [Vue.verifyRules],
        data: function () {
            var self = this;
            return {
                studentForm: {
                    id: '',
                    enterdate: '',
                    address: '',
                    graduation: '',
                    cycle: '',
                    instudy: '',
                    tClass: '',
                    currState: '',
                    temporaryDate: '',
                    user: {
                        id: '',
                        photo: '',
                        name: '',
                        no: '',
                        sex: '',
                        loginName: '',
                        professionalIds: [],
                        birthday: '',
                        email: '',
                        mobile: '',
                        country: '',
                        national: '',
                        political: '',
                        idType: '1',
                        idNumber: '',
                        qq: '',
                        introduction: '',
                        domainIdList: [],
                        degree: '',
                        education: '',
                        officeId: '',
                        professional: '',
                        userType: '1'
                    }
                },
                studentId: '${studentId}',
                sexes: [],
                politicsStatuses: ['中共党员', '共青团员', '民主党派人士', '无党派民主人士', '普通公民'],
                idTypes: [],
                technologyFields: [],
                degreeTypes: [],
                enducationLevels: [],
                cycles: [],
                currStates: [],
                officeTree: [],
                officeEntity: {},

                projectList: [],
                contestList: [],

                city: cityData,
                cityDropdownVisible: false,
                cityTab: 'hotCities',

                loading: false,
                disabled: false,
                userPicFile: '',
                userAvatar: '',
                dialogVisibleChangeUserPic: false,
                dialogVisibleCityPicker: false,


                professionalDefaultTree: {
                    label: 'name',
                    value: 'id',
                    children: 'children'
                },

                educationCurStateEntries: {
                    '1': ['tClass', 'instudy'],
                    '2': ['graduation', 'user.education', 'user.degree'],
                    '3': ['temporaryDate']
                },

                userBirthdayPickerOptions: {
                    disabledDate: function (time) {
                        var maxDate = self.studentForm.enterdate || new Date().toString();
                        var tenYearsMilliseconds = moment.duration(10, 'years').asMilliseconds();
                        if (maxDate) {
                            return time.getTime() >= new Date(maxDate).getTime() - tenYearsMilliseconds;
                        }
                        return false;
                    }
                },

                graduationDatePickerOption: {
                    disabledDate: function (time) {
                        var minDate = self.studentForm.enterdate;
                        var cycle = self.studentForm.cycle;
                        var cycleMilliseconds = cycle ? moment.duration(parseInt(self.cyclesEntries[cycle]), 'years').asMilliseconds() : 0;
                        if (minDate) {
                            return time.getTime() < new Date(minDate).getTime() + cycleMilliseconds;
                        }
                        return false;
                    }
                },

                temporaryDatePickerOption: {
                    disabledDate: function (time) {
                        var minDate = self.studentForm.enterdate;
                        var cycle = self.studentForm.cycle;
                        var cycleMilliseconds = cycle ? moment.duration(parseInt(self.cyclesEntries[cycle]), 'years').asMilliseconds() : 0;
                        var tTime = time.getTime();
                        var minTime;
                        if (minDate) {
                            minTime = new Date(minDate).getTime()
                            return time.getTime() < minTime || tTime > cycleMilliseconds + minTime;
                        }
                        return false;
                    }
                },

                enterdatePickerOptions: {
                    disabledDate: function (time) {
                        var minDate, maxDate;
                        var timeM = time.getTime();
                        if (self.studentForm.user.birthday) {
                            minDate = new Date(self.studentForm.user.birthday).getTime();
                        }
                        maxDate = new Date().getTime();
                        if (minDate && maxDate) {
                            return timeM <= minDate || timeM >= maxDate
                        } else {
                            if (minDate) {
                                return timeM < minDate
                            }
                            if (maxDate) {
                                return timeM > maxDate
                            }
                        }

                        return false;
                    }
                },
            }
        },
        computed: {
            userAsStuNoRules: function () {
                var userNoRules = this.userNoRules;
                var rules = [{required: true, message: '请输入学号', trigger: 'blur'}]
                return rules.concat(userNoRules);
            },
            stuProfessionalRules: function () {
                var professionRules = this.professionRules;
                var rules = [{required: true, message: '请选择到专业', trigger: 'change'}]
                return rules.concat(professionRules);
            },

            userId: function () {
                return this.studentForm.user.id;
            },

            defaultBirthdayDate: function () {
                return moment(new Date()).subtract(10, 'year').format('YYYY-MM');
            },

            defaultEnterDate: function () {
                return moment(new Date().setMonth(8)).subtract(4, 'year').format('YYYY-MM');
            },
            cyclesEntries: function () {
                return this.getEntries(this.cycles)
            },
            currStatesCp: function () {
                var enterDate = this.studentForm.enterdate;
                var cycle = this.studentForm.cycle;
                var cycleLabel = cycle ? this.cyclesEntries[cycle] : 0;
                var now = Date.now();
                var currStates = [];
                var disabledValue = '';
                if (!enterDate || !cycleLabel) {
                    disabledValue = '';
                } else if (now > moment.duration(parseInt(cycleLabel), 'years').asMilliseconds() + new Date(enterDate).getTime()) {
                    disabledValue = '1,3';
                } else {
                    disabledValue = '2';
                }

                this.currStates.forEach(function (item, i) {
                    currStates.push({
                        id: item.id,
                        label: item.label,
                        value: item.value,
                        disabled: disabledValue.indexOf(item.value) > -1
                    })
                });
                return currStates
            },

            cityIdKeyData: function () {
                var data = {};
                for (var i = 0; i < this.city.length; i++) {
                    var city = this.city[i];
                    data[city.id] = city;
                }
                return data;
            },
        },
        methods: {
            validateForm: function () {
                var self = this;
                this.$refs.studentForm.validate(function (valid) {
                    if (valid) {
                        self.saveStudentForm();
                    }
                })
            },

            saveStudentForm: function () {
                var studentForm = JSON.parse(JSON.stringify(this.studentForm));
                var professionalIds = studentForm.user.professionalIds;
                var self = this;
                if (professionalIds.length > 2) {
                    studentForm.user.professional = professionalIds[2];
                    studentForm.user.officeId = professionalIds[1];
                } else {
                    studentForm.user.professional = '';
                    studentForm.user.officeId = professionalIds[professionalIds.length - 1];
//                    studentForm.user.professional = professionalIds[professionalIds.length - 1];
//                    studentForm.user.officeId = professionalIds[professionalIds.length - 2];
                }
                studentForm.user.office = {
                    id: studentForm.user.officeId
                };
                if(studentForm.user.birthday){
                    studentForm.user.birthday = moment(studentForm.user.birthday).valueOf();
                }
                if(studentForm.enterdate){
                    studentForm.enterdate = moment(studentForm.enterdate).valueOf();
                }
                if(studentForm.graduation){
                    studentForm.graduation  = moment(studentForm.graduation).valueOf();
                }
                if(studentForm.temporaryDate){
                    studentForm.temporaryDate  = moment(studentForm.temporaryDate).valueOf();
                }
                delete studentForm.user.professionalIds;
                this.disabled = true;
                this.$axios.post('/sys/studentExpansion/saveStudentExp', studentForm).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$alert('保存成功', '提示', {
                            type: 'success',
                            showClose: false
                        }).then(function () {
                            location.href = self.ctx + '/sys/studentExpansion'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error',
                        })
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.disabled = false;
                })
            },

            handleChangeUserPicClose: function () {
                this.userPicFile = null;
                this.dialogVisibleChangeUserPic = false;
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

                this.disabled = true;
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
                        self.disabled = false;
                        self.dialogVisibleChangeUserPic = false;
                        self.$message({
                            type: 'error',
                            message: data.msg
                        })
                    }
                }).catch(function (error) {
                    self.disabled = false;
                })
            },

            moveFile: function (url) {
                var self = this;
                if (!this.studentForm.user.id) {
                    this.studentForm.user.photo = url;
                    this.dialogVisibleChangeUserPic = false;
                    this.userAvatar = this.addFtpHttp(url);
                    this.userPicFile = null;
                    this.disabled = false;
                    return false;
                }

                return this.$axios.post('/sys/studentExpansion/ajaxUpdatePhoto?photo=' + url + "&userId=" + this.studentForm.user.id).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        data = data.datas;
                        self.dialogVisibleChangeUserPic = false;
                        self.userAvatar = self.addFtpHttp(data.photo);
                        self.studentForm.user.photo = data.photo;
                        self.userPicFile = null;
                        self.$message.success(response.data.msg);
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.disabled = false;

                }).catch(function (error) {
                    self.disabled = false;
                })
            },

            handleCityPickerClose: function () {
                this.dialogVisibleCityPicker = false;
                this.cityTab = 'hotCities'
            },

            cityHandleSelected: function () {
                this.dialogVisibleCityPicker = false;
            },

            goToBack: function () {
                window.history.go(-1)
            },

            isInCurStates: function (k) {
                var currState = this.studentForm.currState;
                if (!currState) {
                    return false;
                }
                return this.educationCurStateEntries[currState].indexOf(k) > -1;
            },

            handleChangeUserPicOpen: function () {
                var userPhoto = this.studentForm.user.photo;
                this.dialogVisibleChangeUserPic = true;
                this.$nextTick(function () {
                    this.userAvatar = this.addFtpHttp(userPhoto) || '/img/u4110.png';
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

            ajaxGetUserProjectById: function () {
                var self = this;
                this.$axios.get('/sys/studentExpansion/ajaxGetUserProjectById?userId=' + this.studentForm.user.id).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.projectList = data.datas || [];
                    }
                })
            },

            ajaxGetUserGContestById: function () {
                var self = this;
                this.$axios.get('/sys/studentExpansion/ajaxGetUserGContestById?userId=' + this.studentForm.user.id).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.contestList = data.datas || [];
                    }
                })
            },

            getStuExDataBySId: function () {
                var self = this;
                if (!this.studentId) {
                    return;
                }
                this.loading = true;
                this.$axios.get('/sys/studentExpansion/getStuExDataBySId/' + this.studentId).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var student = data.data || {};
                        self.setStudentForm(student);
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

            setStudentForm: function (student) {
                var parentIds;
                if (student.user.birthday) {
                    student.user.birthday = moment(student.user.birthday).format('YYYY-MM')
                }
                if (student.enterdate) {
                    student.enterdate = moment(student.enterdate).format('YYYY-MM')
                }
                var professional = student.user.professional || student.user.officeId;
                if (professional && this.officeEntity[professional]) {
                    parentIds = this.officeEntity[professional].parentIds;
                }
                if (parentIds) {
                    parentIds = parentIds.split(',');
                    parentIds = parentIds.slice(1, parentIds.length - 1);
                    parentIds.push(professional);
                    student.user.professionalIds = parentIds
                }
                this.assignFormData(this.studentForm, student);
                this.ajaxGetUserProjectById();
                this.ajaxGetUserGContestById();
            },

            getDictLists: function () {
                var dicts = {
                    'current_sate': 'currStates',
                    'enducation_level': 'enducationLevels',
                    'degree_type': 'degreeTypes',
                    'sex': 'sexes',
                    'id_type': 'idTypes',
                    'technology_field': 'technologyFields',
                    '0000000262': 'cycles'
                };
                this.getBatchDictList(dicts)

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
                    self.getStuExDataBySId();
                })
            },

        },
        created: function () {
            this.getOfficeList();
        },
        mounted: function () {
            this.getDictLists();
        }
    })
</script>
</body>
</html>