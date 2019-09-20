<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <script src="${ctxJs}/cityData/citydataNew.js"></script>


    <script src="${ctxJs}/components/cityDropDown/cityDropDown.js"></script>
    <script src="${ctxJs}/components/cityDropDown/cityPicker.js"></script>
    <style>
        .el-date-editor.el-input, .el-date-editor.el-input__inner {
            max-width: 220px;
            width: auto;
        }
    </style>
</head>

<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar :second-name="userBaseForm.userId ? '编辑': '添加'" href="/sys/studentExpansion"></edit-bar>
    </div>
    <div class="user_avatar-sidebar">
        <div class="user-avatar">
            <div class="avatar-pic">
                <img :src="userBaseForm.userPhoto | ftpHttpFilter(ftpHttp) | studentPicFilter">
            </div>
            <el-button type="primary" size="mini"
                       @click.stop.prevent="handleChangeUserPicOpen">{{userBaseForm.userPhoto ? '更换' : '添加'}}图像
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
                        <el-button type="primary" title="保存" size="mini" :disabled="isUpdating"
                                   style="vertical-align: top"
                                   @click.stop.prevent="validateForm">保存
                        </el-button>
                        <el-button :disabled="isUpdating" size="mini" @click.stop.prevent="goToBack"
                                   style="vertical-align: top">返回
                        </el-button>
                        <%--<el-button :disabled="isUpdating" size="mini" @click.stop.prevent="reloadLo"--%>
                        <%--style="vertical-align: top">刷新--%>
                        <%--</el-button>--%>
                    </div>
                </div>
                <el-form :model="userBaseForm" ref="userBaseForm" :rules="userBaseFormRule" :disabled="isUpdating"
                         :action="frontOrAdmin + '/sys/studentExpansion/save'" method="POST" size="mini"
                         class="el-form-builder"
                         label-width="90px">
                    <input  type="hidden" v-if="userBaseForm.userPhoto.indexOf('/tool/oseasy/temp/user') > -1" name="user.photo" :value="userBaseForm.userPhoto">
                    <input type="hidden" name="id" :value="userBaseForm.id">
                    <%--<input type="hidden" name="userid" :value="userBaseForm.userId">--%>
                    <input type="hidden" name="custRedict" id="custRedict" :value="userBaseForm.custRedict"/>
                    <input type="hidden" name="okurl" id="okurl" :value="userBaseForm.okurl"/>
                    <input type="hidden" name="backurl" id="backurl" :value="userBaseForm.backurl"/>
                    <el-row :gutter="10">
                        <el-col :span="8">
                            <el-form-item label="姓名：" prop="userName">
                                <el-input name="user.name" v-model="userBaseForm.userName"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="学号：" prop="userNo"
                                          :rules="userBaseForm.currState != '2' ? userBaseFormRule.userNo: []">
                                <el-input placeholder="请输入学号" name="user.no" v-model="userBaseForm.userNo"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="性别：" prop="userSex" style="height: 28px;">
                                <input type="hidden" name="user.sex" :value="userBaseForm.userSex">
                                <el-radio-group v-model="userBaseForm.userSex" style="vertical-align: top">
                                    <el-radio-button v-for="item in sexes" :key="item.value" :label="item.value"
                                                     style="vertical-align: top">{{item.label}}
                                    </el-radio-button>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="院系/专业："
                                          prop="userProfessional"
                            >
                                <input type="hidden" name="user.office.id" :value="officeId">
                                <input type="hidden" name="user.professional" :value="professionalId">
                                <el-cascader
                                        style="width: 100%"
                                        ref="cascader"
                                        :options="collegesTree"
                                        :clearable="true"
                                        filterable
                                        v-model="userBaseForm.userProfessional"
                                        :props="professionalDefaultTree">
                                </el-cascader>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="出生时间：" prop="userBirthday">
                                <input type="hidden" name="user.birthday" :value="userBaseForm.userBirthday">
                                <el-date-picker
                                        v-model="userBaseForm.userBirthday"
                                        type="month"
                                        :editable="false"
                                        value-format="yyyy-MM"
                                        :default-value="defaultBirthdayDate"
                                        :picker-options="userBirthdayPickerOptions"
                                        placeholder="选择出生时间">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <%--<el-col :span="8">--%>
                            <%--<el-form-item label="登录名" prop="userLoginName">--%>
                                <%--<el-input name="user.loginName" v-model="userBaseForm.userLoginName"></el-input>--%>
                            <%--</el-form-item>--%>
                        <%--</el-col>--%>
                        <el-col :span="8">
                            <el-form-item label="电子邮箱：" prop="userEmail">
                                <el-input name="user.email" v-model="userBaseForm.userEmail"></el-input>
                            </el-form-item>
                        </el-col>

                        <el-col :span="8">
                            <el-form-item label="手机号：" prop="userMobile">
                                <el-input name="user.mobile" v-model="userBaseForm.userMobile"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="国家/户籍：" prop="userCountry"
                                          class="el-input--mini">
                                <input type="hidden" name="user.country" :value="userBaseForm.userCountry">
                                <city-dropdown v-model="userBaseForm.userCountry" class-name="el-input__inner"
                                               placeholder="填写/选择"
                                               :city-data="cityIdKeyData">
                                    <a slot="rightSelected" href="javascript:void (0)"
                                       @click="dialogVisibleCityPicker=true">选择</a>
                                </city-dropdown>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="民族：" prop="userNational">
                                <el-input name="user.national" v-model="userBaseForm.userNational"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="政治面貌：" prop="userPolitical">
                                <input type="hidden" name="user.political" :value="userBaseForm.userPolitical">
                                <el-select v-model="userBaseForm.userPolitical" placeholder="请选择" clearable>
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
                            <el-form-item label="证件号：" prop="userIdNumber"
                                          :rules="userBaseFormRule[userBaseForm.userIdType == 1 ? 'idNumberIdentity' : 'idNumberPassport']"
                            >
                                <el-input placeholder="请输入证件号" v-model="userBaseForm.userIdNumber"
                                          class="input-with-select_identity">
                                    <el-form-item prop="idType" slot="prepend" style="margin-bottom: 0;height: 26px;">
                                        <el-select v-model="userBaseForm.userIdType" placeholder="请选择证件类型">
                                            <el-option v-for="idType in idTypes" :label="idType.label"
                                                       :value="idType.value" :key="idType.id"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-input>
                                <input type="hidden" name="user.idType" :value="userBaseForm.userIdType">
                                <input type="hidden" name="user.idNumber" :value="userBaseForm.userIdNumber">
                            </el-form-item>
                        </el-col>


                        <el-col :span="8">
                            <el-form-item label="QQ：" prop="userQq">
                                <el-input name="user.qq" placeholder="请输入QQ号码" v-model="userBaseForm.userQq"></el-input>
                            </el-form-item>
                        </el-col>
                        <%--<el-col :span="8">--%>
                            <%--<el-form-item label="户籍：" prop="userResidence">--%>
                                <%--<el-input name="user.residence" placeholder="请输入户籍"--%>
                                          <%--v-model="userBaseForm.userResidence"></el-input>--%>
                            <%--</el-form-item>--%>
                        <%--</el-col>--%>
                        <el-col :span="24">
                            <el-form-item label="联系地址：" prop="address">
                                <el-input name="address" placeholder="请输入联系地址"
                                          v-model="userBaseForm.address"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="24" style="height: 66px;">
                            <el-form-item label="个人简介：" prop="userIntroduction">
                                <el-input name="user.introduction" type="textarea" :rows="2" placeholder="请输入个人简介"
                                          v-model="userBaseForm.userIntroduction"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="入学年月：" prop="enterdate">
                                <input type="hidden" name="enterdate" :value="userBaseForm.enterdate">
                                <el-date-picker
                                        v-model="userBaseForm.enterdate"
                                        value-format="yyyy-MM-dd"
                                        :default-value="defaultEnterDate"
                                        type="month"
                                        :editable="false"
                                        :picker-options="enterdatePickerOptions"
                                        @change="userBaseForm.currState = ''"
                                        placeholder="选择入学年月">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="学年制：" prop="cycle">
                                <input type="hidden" name="cycle" :value="userBaseForm.cycle">
                                <el-select v-model="userBaseForm.cycle" @change="userBaseForm.currState = ''"
                                           placeholder="-请选择-">
                                    <el-option v-for="cycle in cycles" :value="cycle.value" :label="cycle.label+'年'"
                                               :key="cycle.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="现状：" prop="currState">
                                <input type="hidden" name="currState" :value="userBaseForm.currState">
                                <el-select v-model="userBaseForm.currState" placeholder="-请选择-" clearable>
                                    <el-option v-for="currState in currStatesCp" :value="currState.value"
                                               :disabled="currState.disabled"
                                               :label="currState.label" :key="currState.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col v-if="isInCurStates('tClass')" :span="8">
                            <el-form-item label="班级：" prop="tClass">
                                <el-input name="tClass" v-model="userBaseForm.tClass"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col v-if="isInCurStates('userEducation')" :span="8">
                            <el-form-item label="学历：" prop="userEducation"
                            >
                                <input type="hidden" name="user.education" :value="userBaseForm.userEducation">
                                <el-select v-model="userBaseForm.userEducation" placeholder="-请选择-">
                                    <el-option v-for="enducationLevel in enducationLevels"
                                               :value="enducationLevel.value" :label="enducationLevel.label"
                                               :key="enducationLevel.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col v-if="isInCurStates('userDegree')" :span="8">
                            <el-form-item label="学位：" prop="userDegree"
                            >
                                <input type="hidden" name="user.degree" :value="userBaseForm.userDegree">
                                <el-select v-model="userBaseForm.userDegree" placeholder="-请选择-" clearable>
                                    <el-option v-for="degreeType in degreeTypes" :value="degreeType.value"
                                               :label="degreeType.label" :key="degreeType.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col v-if="isInCurStates('instudy')" :span="8">
                            <el-form-item label="在读学位：" prop="instudy"
                            >
                                <input type="hidden" name="instudy" :value="userBaseForm.instudy">
                                <el-select v-model="userBaseForm.instudy" placeholder="-请选择-" clearable>
                                    <el-option v-for="degreeType in degreeTypes" :value="degreeType.value"
                                               :label="degreeType.label" :key="degreeType.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8" v-if="isInCurStates('temporaryDate')">
                            <el-form-item label="休学时间：" prop="temporaryDate"
                            >
                                <input type="hidden" name="temporaryDate" :value="userBaseForm.temporaryDate">
                                <el-date-picker
                                        v-model="userBaseForm.temporaryDate"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        :editable="false"
                                        :picker-options="temporaryDatePickerOption"
                                        placeholder="选择休学时间">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8" v-if="isInCurStates('graduation')">
                            <el-form-item label="毕业时间：" prop="graduation"
                            >
                                <input type="hidden" name="graduation" :value="userBaseForm.graduation">
                                <el-date-picker
                                        v-model="userBaseForm.graduation"
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
                            <el-form-item label="技术领域：" prop="userDomainIdList"
                            >
                                <input type="hidden" name="user.domainIdList"
                                       :value="userBaseForm.userDomainIdList.join(',')">
                                <el-select v-model="userBaseForm.userDomainIdList" name multiple placeholder="请选择"
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
                                    <e-col-item label="担任角色：" label-width="72px">{{ getRoleName(project) }}</e-col-item>
                                    <e-col-item label="项目级别：" label-width="72px">{{project.level}}</e-col-item>
                                    <e-col-item label="项目结果：" label-width="72px">{{project.result}}</e-col-item>
                                    <e-col-item label="项目周期：" label-width="72px">{{getProjectRange(project)}}
                                    </e-col-item>
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
                                    <e-col-item label="担任角色：" label-width="72px">{{ getRoleName(contest) }}</e-col-item>
                                    <e-col-item label="大赛级别：" label-width="72px">{{contest.level}}</e-col-item>
                                    <e-col-item label="大赛获奖：" label-width="72px">{{contest.award}}</e-col-item>
                                    <e-col-item label="大赛周期：" label-width="72px">{{getProjectRange(contest)}}
                                    </e-col-item>
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
                            <span>暂无大赛经历</span>
                        </div>
                    </div>
                </el-row>
            </div>


            <skill-cert :user-id="userBaseForm.userId"></skill-cert>

            <personal-score :user-id="userBaseForm.userId"></personal-score>


        </div>
    </div>
    <el-dialog
            title="选择城市"
            :visible.sync="dialogVisibleCityPicker"
            :close-on-click-modal="false"
            :before-close="handleCityPickerClose">
        <city-picker v-model="userBaseForm.userCountry" :city-data="city" :tab-active-key.sync="cityTab"
                     @selected="cityHandleSelected"></city-picker>
    </el-dialog>
    <el-dialog title="上传图像"
               width="440px"
               :visible.sync="dialogVisibleChangeUserPic"
               :close-on-click-modal="false"
               :before-close="handleChangeUserPicClose">
        <e-pic-file v-model="userAvatar" :disabled="isUpdating" @get-file="getUserPicFile"></e-pic-file>
        <cropper-pic :img-src="userAvatar" :disabled="isUpdating" ref="cropperPic"></cropper-pic>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="isUpdating" @click.stop.prevent="handleChangeUserPicClose">取消</el-button>
            <el-button size="mini" :disabled="!userPicFile || isUpdating" type="primary"
                       @click.stop.prevent="updateUserPic">上传
            </el-button>
        </div>
    </el-dialog>
</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        mixins: [Vue.verifyExpressionMixin, Vue.collegesMixin],
        data: function () {
            var self = this;
            var studentExpVo = JSON.parse(JSON.stringify(${fns: toJson(studentExpVo)}));
            var studentForm = studentExpVo.studentExpansion;
            var user = studentForm.user || {};
            var custRedict = studentExpVo.custRedict;
            var okurl = studentExpVo.okurl;
            var backurl = studentExpVo.backurl;
            var sexes = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('sex'))}));
            var idTypes = JSON.parse(JSON.stringify(${fns:toJson(fns:getDictList('id_type'))})) || [];
            var cycles = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList('0000000262'))})) || [];
            var currStates = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList('current_sate'))})) || [];
            var colleges = JSON.parse(JSON.stringify(${fns: toJson(fns: getOfficeList())})) || [];
            var enducationLevels = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList('enducation_level'))})) || [];
            var degreeTypes = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList('degree_type'))})) || [];
            var technologyFields = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("technology_field"))})) || [];
            var tenYearsMilliseconds = moment.duration(10, 'years').asMilliseconds();
            var userProfessional = [];
            var validateIdentity = this.validateIdentity;
            var validateEmail = this.validateEmail;
            var validateMobile = this.validateMobile;
            var validateQQ = this.validateQQ;
            var validateLoginName = this.validateLoginName;
            var validateUserNo = this.validateUserNo;
            var validateUserName = this.validateUserName;
            var validateMobileBackXhr = this.validateMobileBackXhr;
            var validateOfficeId = function (rules, value, callback) {
                if(value && value.length > 0){
                    var professionalId = value[value.length - 1];
                    var professional = self.collegeEntries[professionalId];
                    if(professional && parseInt(professional.grade) >= 3){
                        return callback();
                    }
                    return callback(new Error('请选择专业'))
                }
                return callback();
            };
            colleges = colleges.filter(function (item) {
                return item.id !== '1';
            });

            if (user.officeId) {
//                userProfessional.push('1')
                userProfessional.push(user.officeId);
            }
            if (user.professional) {
                userProfessional.push(user.professional)
            }



            return {
                userBaseForm: {
                    id: studentForm.id,
                    userId: user.id,
                    custRedict: custRedict,
                    okurl: okurl,
                    backurl: backurl,
                    userName: user.name,//用户姓名
                    userSex: user.sex,//用户性别
                    userBirthday: user.birthday ? moment(user.birthday).format('YYYY-MM') : '',//用户生日
//                    userLoginName: user.loginName, //用户登录名
                    userEmail: user.email,
                    userMobile: user.mobile,
                    userCountry: user.country,
                    userIdNumber: user.idNumber,
                    userIdType: user.idType || '1',
                    userQq: user.qq,
                    userPhoto: user.photo || '',
                    residence: user.residence,
                    introduction: user.introduction,
                    userPolitical: user.political,
                    userNational: user.national,
                    address: studentForm.address,
                    enterdate: studentForm.enterdate ? moment(studentForm.enterdate).format('YYYY-MM') : '',//用户生日
                    currState: studentForm.currState,
                    cycle: studentForm.cycle,
                    userNo: user.no,
                    userProfessional: userProfessional,
                    tClass: studentForm.tClass,
                    userEducation: user.education,
                    userDegree: user.degree,
                    instudy: studentForm.instudy,
                    temporaryDate: studentForm.temporaryDate,
                    graduation: studentForm.graduation ? moment(studentForm.graduation).format('YYYY-MM') : '',
                    userDomainIdList: user.domainIdList || [],
                    userIntroduction: user.introduction,
                    userResidence: user.residence
                },
                userBaseFormRule: {
                    userResidence: [
//                        {required: true, message: '请填写户籍', trigger: 'blur'},
                        {max: 64, message: '请输入大不于64个字', trigger: 'blur'},
                    ],
                    userIntroduction: [
//                        {required: true, message: '请填写个人简介', trigger: 'blur'},
                        {max: 200, message: '请输入不大于200个字', trigger: 'blur'},
                    ],
//                    userLoginName: [
//                        {required: true, message: '请填写登录名', trigger: 'blur'},
//                        {min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'},
//                        {validator: validateLoginName, trigger: 'blur'}
//                    ],
                    userName: [
                        {required: true, message: '请填写姓名', trigger: 'blur'},
                        {min: 2, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'},
                        {validator: validateUserName, trigger: 'blur'}
                    ],
                    userSex: [
//                        {required: true, message: '请选择性别', trigger: 'change'}
                    ],
                    userIdType: [
//                        {required: true, message: '请选择证件类别', trigger: 'change'}
                    ],
                    idNumberIdentity: [
//                        {required: true, message: '请填写身份证号码', trigger: 'blur'},
                        {validator: validateIdentity, trigger: 'blur'}
                    ],
                    idNumberPassport: [
//                        {required: true, message: '请填写护照号码', trigger: 'blur'},
                        {min: 6, max: 24, message: '请填写6-24位字符', trigger: 'blur'}
                    ],
                    userBirthday: [
//                        {required: false, message: '请填写出生时间', trigger: 'change'}
                    ],
                    userEmail: [
//                        {required: true, message: '请填写邮箱', trigger: 'blur'},
                        {validator: validateEmail, trigger: 'blur'}
                    ],
                    userMobile: [
                        {validator: validateMobile, trigger: 'blur'},
                        {validator: validateMobileBackXhr, trigger: 'blur'}
                    ],
                    userQq: [
//                        {required: false, message: '请填入QQ号码', trigger: 'blur'},
                        {validator: validateQQ, trigger: 'blur'}
                    ],
                    address: [{
                        min: 3, max: 128, message: '请填写3-128位的字符', trigger: 'blur'
                    }],
                    userCountry: [{
//                        required: false, message: '请选择国家/地区', trigger: 'change'
                    }],
                    userNational: [{
//                        required: false, message: '请选择国家/地区', trigger: 'change'
                    }],
                    userPolitical: [{
//                        required: false, message: '请选择国家/地区', trigger: 'change'
                    }],
                    enterdate: [
//                        {required: true, message: '请选择入学年份', trigger: 'change'}
                    ],
                    tClass: [
//                        {max: 64, message: '请输入小于64位字符的班级名', trigger: 'blur'}
                    ],
                    userNo: [
                        {required: true, message: '请填入学号', trigger: 'blur'},
                        {min: 2, max: 24, message: '请输入2-24位字符的学号', trigger: 'blur'},
                        {validator: validateUserNo, trigger: 'blur'}
                    ],
                    cycle: [
//                        {required: true, message: '请选择学制年', trigger: 'change'}
                    ],
                    userProfessional: [
                        {required: true, message: '请选择院系/专业', trigger: 'change'},
                        {validator: validateOfficeId, trigger: 'change'}
                    ],
                    userEducation: [
//                        {required: true, message: '请选择学历', trigger: 'change'}
                    ],
                    userDegree: [
//                        {required: true, message: '请选择学位', trigger: 'change'}
                    ],
                    instudy: [
//                        {required: true, message: '请选择在读学位', trigger: 'change'}
                    ],
                    graduation: [
//                        {required: true, message: '请选择毕业时间', trigger: 'change'}
                    ],
                    userDomainIdList: [
//                        {required: true, message: '请选择技术领域', trigger: 'change'}
                    ],
                    temporaryDate: [
//                        {required: true, message: '请选择休学时间', trigger: 'change'}
                    ],
                    currState: [
//                        {required: true, message: '请选择现状', trigger: 'change'}
                    ]
                },

                colleges: colleges,
                sexes: sexes,
                idTypes: idTypes,
                cycles: cycles,
                currStates: currStates,
                enducationLevels: enducationLevels,
                degreeTypes: degreeTypes,
                technologyFields: technologyFields,
                politicsStatuses: ['中共党员', '共青团员', '民主党派人士', '无党派民主人士', '普通公民'],

                projectList: [],
                contestList: [],

                isDefaultCollegeRootId: false, //去掉学院;

                city: cityData,
                cityDropdownVisible: false,
                cityTab: 'hotCities',
                dialogVisibleCityPicker: false,

                userBirthdayPickerOptions: {
                    disabledDate: function (time) {
                        var maxDate = self.userBaseForm.enterdate || new Date().toString();
                        if (maxDate) {
                            return time.getTime() >= new Date(maxDate).getTime() - tenYearsMilliseconds;
                        }
                        return false;
                    }
                },
                enterdatePickerOptions: {
                    disabledDate: function (time) {
                        var minDate, maxDate;
                        var timeM = time.getTime();
                        if (self.userBaseForm.userBirthday) {
                            minDate = new Date(self.userBaseForm.userBirthday).getTime();
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
                graduationDatePickerOption: {
                    disabledDate: function (time) {
                        var minDate = self.userBaseForm.enterdate;
                        var cycle = self.userBaseForm.cycle;
                        var cycleMilliseconds = cycle ? moment.duration(parseInt(self.cyclesEntries[cycle]), 'years').asMilliseconds() : 0;
                        if (minDate) {
                            return time.getTime() < new Date(minDate).getTime() + cycleMilliseconds;
                        }
                        return false;
                    }
                },
                temporaryDatePickerOption: {
                    disabledDate: function (time) {
                        var minDate = self.userBaseForm.enterdate;
                        var cycle = self.userBaseForm.cycle;
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

                isUpdating: false,

                userPicFile: null,
                dialogVisibleChangeUserPic: false,
                userAvatar: '',
//                userPhoto: user.photo,


                educationCurStateEntries: {
                    '1': ['tClass', 'instudy'],
                    '2': ['graduation', 'userEducation', 'userDegree'],
                    '3': ['temporaryDate']
                },

                professionalDefaultTree: {
                    label: 'name',
                    value: 'id',
                    children: 'children'
                }

            }
        },
        computed: {

            officeId: function () {
                var userProfessional = this.userBaseForm.userProfessional;
                if (this.professionalId && userProfessional.length - 2 > -1) {
                    return userProfessional[userProfessional.length - 2];
                }
                return ''
            },

            professionalId: function () {
                var userProfessional = this.userBaseForm.userProfessional;
                if (userProfessional && userProfessional.length != 0) {
                    return userProfessional[userProfessional.length - 1];
                }
            },

            defaultEnterDate: function () {
                return moment(new Date().setMonth(8)).subtract(4, 'year').format('YYYY-MM');
            },
            defaultBirthdayDate: function () {
                return moment(new Date()).subtract(10, 'year').format('YYYY-MM');
            },
            cityIdKeyData: function () {
                var data = {};
                for (var i = 0; i < this.city.length; i++) {
                    var city = this.city[i];
                    data[city.id] = city;
                }
                return data;
            },
            cyclesEntries: function () {
                return this.getEntries(this.cycles)
            },
            currStatesCp: function () {
                var enterDate = this.userBaseForm.enterdate;
                var cycle = this.userBaseForm.cycle;
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
            }
        },
        filters: {
            userCountryName: function (value, cityIdKeyData) {
                if (!value) {
                    return '';
                }
                if (cityIdKeyData[value]) {
                    return cityIdKeyData[value].shortName
                }
                return value
            }
        },
        methods: {
            handleChangeUserPicOpen: function () {
                var userPhoto = this.userBaseForm.userPhoto;
                this.dialogVisibleChangeUserPic = true;
                this.$nextTick(function () {
                    this.userAvatar = (this.userBaseForm.userPhoto && this.userBaseForm.userPhoto.indexOf('/tool') > -1) ? this.addFtpHttp(userPhoto) : '/img/u4110.png';
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
                if(!this.userBaseForm.userId){
                    this.userBaseForm.userPhoto = url;
                    this.dialogVisibleChangeUserPic = false;
                    this.userAvatar = this.addFtpHttp(url);
                    this.userPicFile = null;
                    this.isUpdating = false;
                    return false;
                }

                return this.$axios.post('/sys/studentExpansion/ajaxUpdatePhoto?photo=' + url + "&userId=" + this.userBaseForm.userId).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        data = data.datas;
                        self.dialogVisibleChangeUserPic = false;
                        self.userAvatar = self.addFtpHttp(data.photo);
                        self.userBaseForm.userPhoto = data.photo;
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

            goToBack: function () {
                window.history.go(-1)
            },
            reloadLo: function () {
                location.reload();
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

            validateForm: function () {
                var self = this;
                this.$refs.userBaseForm.validate(function (valid) {
                    if (valid) {
                        self.submitUserBaseForm();
                    }
                });
            },
            submitUserBaseForm: function () {
                this.$nextTick(function () {
                    this.$refs.userBaseForm.$el.submit()
                })
            },

            isInCurStates: function (k) {
                var currState = this.userBaseForm.currState;
                if (!currState) {
                    return false;
                }
                return this.educationCurStateEntries[currState].indexOf(k) > -1;
            },

            ajaxGetUserProjectById: function () {
                var self = this;
                this.$axios.get('/sys/studentExpansion/ajaxGetUserProjectById?userId=' + this.userBaseForm.userId).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.projectList = data.datas || [];
                    }
                })
            },
            ajaxGetUserGContestById: function () {
                var self = this;
                this.$axios.get('/sys/studentExpansion/ajaxGetUserGContestById?userId=' + this.userBaseForm.userId).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.contestList = data.datas || [];
                    }
                })
            },

            getStudentExpansion: function () {
                var self = this;
                this.$axios.get('/sys/studentExpansion/getStudentExpansion?userId=' + this.userBaseForm.userId).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        console.log(data.data)
                    }
                })
            }
        },
        mounted: function () {
            this.ajaxGetUserGContestById();
            this.ajaxGetUserProjectById();
            this.getStudentExpansion();
        }
    })
</script>
</body>
</html>