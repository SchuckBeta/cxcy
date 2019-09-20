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
        <edit-bar second-name="详情" href="/sys/studentExpansion"></edit-bar>
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
            <i class="iconfont icon-user"></i><span class="text">学生基本信息</span>
        </div>
        <div class="user_detail-wrap">
            <div class="user_detail-inner">
                <el-row :gutter="10" label-width="90px">
                    <el-col :span="8">
                        <e-col-item label="姓名：" align="right">{{userBaseForm.userName}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="学号：" align="right">{{userBaseForm.userNo}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="性别：" align="right">{{userBaseForm.userSex | selectedFilter(sexesEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="院系/专业：" align="right">{{userBaseForm.userProfessional |
                            cascaderCollegeFilter(collegeEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="出生时间：" align="right">{{userBaseForm.userBirthday}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="电子邮箱：" align="right">{{userBaseForm.userEmail}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="手机号：" align="right">{{userBaseForm.userMobile}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="国家/户籍：" align="right">{{userBaseForm.userCountry |
                            userCountryName(cityIdKeyData)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="民族：" align="right">{{userBaseForm.userNational}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="政治面貌：" align="right">{{userBaseForm.userPolitical}}</e-col-item>
                    </el-col>
                    <el-col :span="16">
                        <e-col-item label="证件号：" align="right">
                            {{userBaseForm.userIdType | selectedFilter(idTypesEntries)}}
                            {{userBaseForm.userIdNumber}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="QQ：" align="right">{{userBaseForm.userQq}}</e-col-item>
                    </el-col>
                    <%--<el-col :span="8">--%>
                        <%--<e-col-item label="户籍：" align="right">{{userBaseForm.userResidence}}</e-col-item>--%>
                    <%--</el-col>--%>
                    <el-col :span="24">
                        <e-col-item label="联系地址：" align="right">{{userBaseForm.address}}</e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="个人简介：" align="right" class="white-space-pre-static word-break"><span v-html="userBaseForm.userIntroduction"></span></e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="入学年月：" align="right">{{userBaseForm.enterdate}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="学年制：" align="right">{{userBaseForm.cycle |
                            selectedFilter(cyclesEntries)}}<span v-if="userBaseForm.cycle">年</span></e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="现状：" align="right">{{userBaseForm.currState |
                            selectedFilter(currStateEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8" v-show="isInCurStates('tClass')">
                        <e-col-item label="班级：" align="right">{{userBaseForm.tClass}}</e-col-item>
                    </el-col>
                    <el-col :span="8" v-show="isInCurStates('userEducation')">
                        <e-col-item label="学历：" align="right">{{userBaseForm.userEducation |
                            selectedFilter(enducationLevelsEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8" v-show="isInCurStates('userDegree')">
                        <e-col-item label="学位：" align="right">{{userBaseForm.userDegree |
                            selectedFilter(degreeTypesEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8" v-show="isInCurStates('instudy')">
                        <e-col-item label="在读学位：" align="right">{{userBaseForm.instudy |
                            selectedFilter(degreeTypesEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8" v-show="isInCurStates('temporaryDate')">
                        <e-col-item label="休学时间：" align="right">{{userBaseForm.temporaryDate}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8" v-show="isInCurStates('graduation')">
                        <e-col-item label="毕业时间：" align="right">{{userBaseForm.graduation}}</e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="技术领域：" align="right">{{userBaseForm.userDomainIdList |
                            checkboxFilter(technologyFieldsEntries)}}
                        </e-col-item>
                    </el-col>
                </el-row>
            </div>

            <project-experience :user-id="userBaseForm.userId" :is-request="true"></project-experience>

            <contest-experience :user-id="userBaseForm.userId" :is-request="true"></contest-experience>

            <skill-cert :user-id="userBaseForm.userId"></skill-cert>

            <personal-score :user-id="userBaseForm.userId"></personal-score>


        </div>
    </div>
</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin],
        data: function () {
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
            var userProfessional = [];

            if (user.officeId) {
                userProfessional.push('1');
                userProfessional.push(user.officeId);
            }
            if (user.professional) {
                userProfessional.push(user.professional);
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
                colleges: colleges,
                sexes: sexes,
                idTypes: idTypes,
                cycles: cycles,
                currStates: currStates,
                enducationLevels: enducationLevels,
                degreeTypes: degreeTypes,
                technologyFields: technologyFields,
                politicsStatuses: ['中共党员', '共青团员', '民主党派人士', '无党派民主人士', '普通公民'],
                city: cityData,
                userPhoto: user.photo,
                educationCurStateEntries: {
                    '1': ['tClass', 'instudy'],
                    '2': ['graduation', 'userEducation', 'userDegree'],
                    '3': ['temporaryDate']
                }

            }
        },
        computed: {
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
            sexesEntries: function () {
                return this.getEntries(this.sexes);
            },
            idTypesEntries: {
                get: function () {
                    return this.getEntries(this.idTypes)
                }
            },
            currStateEntries: {
                get: function () {
                    return this.getEntries(this.currStates)
                }
            },

            enducationLevelsEntries: {
                get: function () {
                    return this.getEntries(this.enducationLevels)
                }
            },
            degreeTypesEntries: {
                get: function () {
                    return this.getEntries(this.degreeTypes)
                }
            },

            technologyFieldsEntries: {
                get: function () {
                    return this.getEntries(this.technologyFields)
                }
            }
        },
        methods: {
            isInCurStates: function (k) {
                var currState = this.userBaseForm.currState;
                if (!currState) {
                    return false;
                }
                return this.educationCurStateEntries[currState].indexOf(k) > -1;
            }

        }
    })
</script>
</body>
</html>