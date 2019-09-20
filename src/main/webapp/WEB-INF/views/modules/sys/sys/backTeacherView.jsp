<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <script src="${ctxJs}/cityData/citydataNew.js"></script>

</head>

<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar second-name="详情" href="/sys/backTeacherExpansion"></edit-bar>
    </div>
    <div class="user_avatar-sidebar">
        <div class="user-avatar">
            <div class="avatar-pic">
                <img :src="userPhoto | ftpHttpFilter(ftpHttp) | studentPicFilter">
            </div>
        </div>
    </div>
    <div class="user_detail-container">
        <div class="user_detail-title">
            <i class="iconfont icon-user"></i><span class="text">导师基本信息</span>
        </div>
        <div class="user_detail-wrap">
            <div class="user_detail-inner">

                <el-row :gutter="10" label-width="90px">
                    <el-col :span="8">
                        <e-col-item label="导师来源：" align="right">{{teacherForm.teachertype | selectedFilter(masterTypesEntries)}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="姓名：" align="right">{{teacherForm.userName}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="性别：" align="right">{{teacherForm.userSex | selectedFilter(sexesEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="职工号：" align="right">{{teacherForm.userNo}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="出生年月：" align="right">{{teacherForm.userBirthday | formatDateFilter('YYYY-MM')}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="国家/户籍：" align="right">{{teacherForm.userCountry |
                            userCountryName(cityIdKeyData)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="民族：" align="right">{{teacherForm.userNational}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="政治面貌：" align="right">{{teacherForm.userPolitical}}</e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="服务意向：" align="right">{{teacherForm.serviceIntentionIds | checkboxFilter(masterServicesEntries)}}</e-col-item>
                    </el-col>
                    <el-col :span="16">
                        <e-col-item label="证件号：" align="right">
                            {{teacherForm.userIdType | selectedFilter(idTypesEntries)}}
                            {{teacherForm.userIdNumber}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="学历类别：" align="right">{{teacherForm.educationType |
                            selectedFilter(educationTypeEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="学历：" align="right">{{teacherForm.userEducation |
                            selectedFilter(enducationLevelEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="学科门类：" align="right">{{teacherForm.discipline |
                            selectedFilter(professionalTypeEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="学位类别：" align="right">{{teacherForm.userDegree |
                            selectedFilter(degreeTypeEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="16">
                        <e-col-item label="院系/专业：" align="right">{{teacherForm.userProfessional |
                            cascaderCollegeFilter(collegeEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="手机号：" align="right">{{teacherForm.userMobile}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="行业：" align="right">{{teacherForm.industry}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="职务：" align="right">{{teacherForm.postTitle}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="职称：" align="right">{{teacherForm.technicalTitle | selectedFilter(expertJobNamesEntries, true)}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="开户银行：" align="right">{{teacherForm.firstBank}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="账号：" align="right">{{teacherForm.bankAccount}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="邮箱：" align="right">{{teacherForm.userEmail}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="单位类别：" align="right">{{teacherForm.workUnitType | selectedFilter(workTypesEntries)}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="工作单位：" align="right">{{teacherForm.workUnit}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="推荐单位：" align="right">{{teacherForm.recommendedUnits}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="首页展示：" align="right">{{teacherForm.firstShow | selectedFilter(yesNoEntries)}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="栏目展示：" align="right">{{teacherForm.siteShow | selectedFilter(yesNoEntries)}}</e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="技术领域：" align="right">{{teacherForm.userDomainIdList |
                            checkboxFilter(technologyFieldEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="关键词：" align="right">
                            <el-tag
                                    :key="tag.id"
                                    v-for="(tag, index) in teacherForm.keywords"
                                    type="info"
                                    size="medium"
                                    :disable-transitions="false">
                                {{tag}}
                            </el-tag>
                        </e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="工作经历：" align="right" class="white-space-pre-static word-break"><span v-html="teacherForm.mainExp"></span></e-col-item>
                    </el-col>

                </el-row>

            </div>

            <project-experience :project-list="projectList" :user-id="teacherForm.userid" title-label="指导项目"></project-experience>

            <contest-experience :contest-list="contestList" :user-id="teacherForm.userid" title-label="指导大赛"></contest-experience>

            <review-task :user-id="teacherForm.userid"></review-task>


        </div>
    </div>


</div>

<script type="text/javascript">

    'use strict';
    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin],
        data: function () {
            var teacherData = JSON.parse(JSON.stringify(${fns: toJson(teacherData)})) || {};
            var backTeacherExpansion = teacherData.backTeacherExpansion;
            var user = backTeacherExpansion.user || {};
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
                    userPhoto: user.photo,
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
                    userIdType: user.idType,
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
                    roleType: backTeacherExpansion.roleType || '0'
                },

                userPhoto: user.photo,

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
                politicsStatuses: ['中共党员', '共青团员', '民主党派人士', '无党派民主人士', '普通公民'],

//                isDefaultCollegeRootId: false, //去掉学院;
                city: cityData,

                projectList: projectList,
                contestList: contestList,

                saveMessage: '${message}'
            }
        },
        computed: {
            masterTypesEntries:function () {
                return this.getEntries(this.masterTypes);
            },
            sexesEntries: function () {
                return this.getEntries(this.sexes);
            },
            masterServicesEntries:function () {
                return this.getEntries(this.masterServices);
            },
            idTypesEntries: function () {
                return this.getEntries(this.idTypes)
            },
            educationTypeEntries: function () {
                return this.getEntries(this.educationTypes)
            },
            enducationLevelEntries: function () {
                return this.getEntries(this.enducationLevels)
            },
            professionalTypeEntries: function () {
                return this.getEntries(this.professionalTypes)
            },
            technologyFieldEntries: function () {
                return this.getEntries(this.technologyFields)
            },
            degreeTypeEntries: function () {
                return this.getEntries(this.degreeTypes)
            },
            workTypesEntries:function () {
                return this.getEntries(this.workTypes);
            },
            yesNoEntries:function () {
                return this.getEntries(this.yesNo);
            },
            expertJobNamesEntries:function () {
                return this.getEntries(this.expertJobNames);
            },

            cityIdKeyData: function () {
                var data = {};
                for (var i = 0; i < this.city.length; i++) {
                    var city = this.city[i];
                    data[city.id] = city;
                }
                return data;
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