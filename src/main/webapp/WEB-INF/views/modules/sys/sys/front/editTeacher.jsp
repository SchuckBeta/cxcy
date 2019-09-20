<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>


    <script src="/js/components/cityDropDown/cityDropDown.js"></script>
    <script src="/js/components/cityDropDown/cityPicker.js"></script>

</head>

<body>
<div id="app" v-show="pageLoad" style="display: none" class="container page-container pdt-60">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="/f"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item><a href="/f/sys/frontTeacherExpansion">导师资源</a>
        </el-breadcrumb-item>
        <el-breadcrumb-item>导师信息</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="user_avatar-sidebar">
        <div class="user-avatar">
            <div class="avatar-pic">
                <img :src="userPhoto | ftpHttpFilter(ftpHttp) | studentPicFilter">
            </div>
            <el-button type="primary" size="mini" @click.stop.prevent="handleChangeUserPicOpen">更换图像
            </el-button>
        </div>
        <ul class="user-info_menu">
            <li class="active"><a href="javascript: void(0);"><i class="iconfont icon-user"></i>基本信息</a></li>
            <li v-if="casHasLogin"><a href="${ctxFront}/sys/frontStudentExpansion/frontUserPassword"><i class="iconfont icon-14"></i>登录信息</a></li>
            <li v-if="hasMvcode"><a href="${ctxFront}/sys/frontStudentExpansion/frontUserMobile"><i class="iconfont icon-unie64f"></i>手机信息</a></li>
        </ul>
    </div>
    <div class="user_detail-container">
        <div class="user_detail-title">
            <i class="iconfont icon-user"></i><span class="text">导师基本信息</span>
        </div>
        <div class="user_detail-wrap">
            <div class="user_detail-inner">
                <div class="user_detail-title-handler">
                    <div class="ud-row-handler">

                        <el-button type="text" title="编辑" size="mini" v-show="isShowStaticTeacherDetail"
                                   :disabled="isUpdating" @click.stop.prevent="isShowStaticTeacherDetail = false"><i
                                class="iconfont icon-bianji"></i></el-button>
                        <el-button type="text" title="取消" size="mini" v-show="!isShowStaticTeacherDetail"
                                   :disabled="isUpdating"
                                   @click.stop.prevent="cancleEditTeacherForm">
                            <i class="iconfont icon-mianfeiquxiao"></i></el-button>
                        <el-button type="text" title="保存" size="mini"
                                   :disabled="isUpdating || isShowStaticTeacherDetail"
                                   @click.prevent="validateForm"><i
                                class="iconfont icon-iconset0237"></i></el-button>

                        <%--<el-button type="primary" title="保存" size="mini" :disabled="isUpdating" style="vertical-align: top"--%>
                        <%--@click.stop.prevent="validateForm">保存</el-button>--%>
                        <%--<el-button :disabled="isUpdating" size="mini" @click.stop.prevent="goToBack" style="vertical-align: top">返回</el-button>--%>
                    </div>
                </div>
                <el-row v-show="isShowStaticTeacherDetail" :gutter="10" label-width="90px">
                    <el-col :span="8">
                        <e-col-item label="姓名：" align="right">{{teacherStaticDetail.userName}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="性别：" align="right">{{teacherStaticDetail.userSex |
                            selectedFilter(sexEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="导师来源：" align="right">{{teacherStaticDetail.teachertype |
                            selectedFilter(masterTypeEntries)}}
                        </e-col-item>
                    </el-col>
                    <%--<el-col :span="8">--%>
                        <%--<e-col-item label="登录名：" align="right">{{teacherStaticDetail.userLoginName}}</e-col-item>--%>
                    <%--</el-col>--%>
                    <el-col :span="8">
                        <e-col-item label="邮箱：" align="right">{{teacherStaticDetail.userEmail}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="手机号：" align="right">{{teacherStaticDetail.userMobile}}
                            <%--<span :class="{'empty-color empty': !teacherStaticDetail.userMobile}"--%>
                                  <%--v-show="!teacherStaticDetail.userMobile">请添加手机号</span>--%>
                            <%--<el-button type="text" size="mini" @click.stop.prevent="handleChangeMobile">--%>
                                <%--{{teacherStaticDetail.userMobile ? '修改' : '添加'}}--%>
                            <%--</el-button>--%>
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="国家/户籍：" align="right">{{teacherStaticDetail.userCountry |
                            userCountryName(cityIdKeyData)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="民族：" align="right">{{teacherStaticDetail.userNational}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="政治面貌：" align="right">{{teacherStaticDetail.userPolitical}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="职工号：" align="right">{{teacherStaticDetail.userNo}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="出生年月：" align="right">{{teacherStaticDetail.userBirthday}}</e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="服务意向：" align="right">{{teacherStaticDetail.serviceIntentionIds |
                            checkboxFilter(masterServicesEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="16">
                        <e-col-item label="证件号：" align="right">
                            {{teacherStaticDetail.userIdType | selectedFilter(idTypesEntries)}}
                            {{teacherStaticDetail.userIdNumber}}
                        </e-col-item>
                    </el-col>
                    <%--<el-col :span="8">--%>
                        <%--<e-col-item v-if="teacherForm.serviceIntentionIds.indexOf('4') > -1" label="导师类型："--%>
                                    <%--align="right">--%>
                            <%--{{teacherStaticDetail.category | selectedFilter(teacherCategoryEntries)}}--%>
                        <%--</e-col-item>--%>
                        <%--<div v-else style="height: 28px; margin-bottom: 18px;"></div>--%>
                    <%--</el-col>--%>
                    <el-col :span="8">
                        <e-col-item label="学历类别：" align="right">{{teacherStaticDetail.educationType |
                            selectedFilter(educationTypeEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="学历：" align="right">{{teacherStaticDetail.userEducation |
                            selectedFilter(enducationLevelEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="学科门类：" align="right">{{teacherStaticDetail.discipline |
                            selectedFilter(professionalTypeEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="学位类别：" align="right">{{teacherStaticDetail.userDegree |
                            selectedFilter(degreeTypeEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="16">
                        <e-col-item label="院系/专业：" align="right">{{teacherStaticDetail.userProfessional |
                            cascaderCollegeFilter(collegeEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="行业：" align="right">{{teacherStaticDetail.industry}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="职务：" align="right">{{teacherStaticDetail.postTitle}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="职称：" align="right">{{teacherStaticDetail.technicalTitle | selectedFilter(expertJobNamesEntries, true)}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="开户银行：" align="right">{{teacherStaticDetail.firstBank}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="账号：" align="right">{{teacherStaticDetail.bankAccount}}</e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="技术领域：" align="right">{{teacherStaticDetail.userDomainIdList |
                            checkboxFilter(technologyFieldEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="关键词：" align="right">
                            <el-tag
                                    :key="tag.id"
                                    v-for="(tag, index) in teacherStaticDetail.keywords"
                                    type="info"
                                    size="medium"
                                    :disable-transitions="false">
                                {{tag.keyword}}
                            </el-tag>
                        </e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="工作经历：" align="right" class="white-space-pre-static word-break"><span v-html="teacherStaticDetail.mainExp"></span></e-col-item>
                    </el-col>
                </el-row>
                <el-form v-show="!isShowStaticTeacherDetail" :model="teacherForm" ref="teacherForm"
                         :rules="teacherRules" :disabled="isUpdating"
                         autocomplete="off"
                         :action="frontOrAdmin + '/sys/frontTeacherExpansion/save'" method="POST" size="mini"
                         label-width="90px" class="el-form-builder">
                    <input type="hidden" name="user.photo" :value="userPhoto">
                    <input type="hidden" name="id" :value="teacherForm.id">
                    <input type="hidden" name="userType" value="2">
                    <input type="hidden" name="userid" :value="teacherForm.userid">
                    <el-row :gutter="10">
                        <el-col :span="8">
                            <el-form-item prop="userName" label="姓名：">
                                <el-input name="user.name" v-model="teacherForm.userName"></el-input>
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
                        <el-col :span="8">
                            <el-form-item prop="teachertype" label="导师来源：">
                                <input type="hidden" name="teachertype" :value="teacherForm.teachertype">
                                <el-select v-model="teacherForm.teachertype">
                                    <el-option v-for="item in masterTypes" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <%--<el-col :span="8">--%>
                            <%--<el-form-item prop="userLoginName" label="登录名：">--%>
                                <%--<el-input name="user.loginName"  :disabled="!casHasLogin" v-model="teacherForm.userLoginName"></el-input>--%>
                            <%--</el-form-item>--%>
                        <%--</el-col>--%>

                        <el-col :span="8">
                            <el-form-item prop="userEmail" label="邮箱：">
                                <el-input name="user.email" v-model="teacherForm.userEmail"></el-input>
                            </el-form-item>
                        </el-col>

                        <el-col :span="8">
                            <el-form-item prop="userMobile" label="手机号：">
                                <input type="hidden" name="user.mobile" :value="teacherForm.userMobile">
                                <span style="font-size: 12px;" :class="{'empty-color empty': !teacherForm.userMobile}">{{teacherForm.userMobile }}</span>
                                <el-button type="text" size="mini" @click.stop.prevent="handleChangeMobile">
                                    {{teacherStaticDetail.userMobile ? '修改' : '添加'}}
                                </el-button>
                            </el-form-item>
                        </el-col>

                        <el-col :span="8">
                            <el-form-item label="国家/户籍" prop="userCountry"
                                          class="el-input--mini">
                                <input type="hidden" name="user.area" :value="teacherForm.userCountry">
                                <city-dropdown v-model="teacherForm.userCountry" class-name="el-input__inner"
                                               placeholder="填写/选择"
                                               :city-data="cityIdKeyData">
                                    <a slot="rightSelected" href="javascript:void (0)"
                                       @click="dialogVisibleCityPicker=true">选择</a>
                                </city-dropdown>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="userNational" label="民族：">
                                <input type="hidden" name="user.national" :value="teacherForm.userNational">
                                <el-input v-model="teacherForm.userNational"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="userPolitical" label="政治面貌：">
                                <input type="hidden" name="user.political" :value="teacherForm.userPolitical">
                                <el-select v-model="teacherForm.userPolitical" placeholder="请选择">
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
                            <el-form-item prop="userNo" label="职工号：">
                                <input type="hidden" name="user.no" :value="teacherForm.userNo">
                                <el-input v-model="teacherForm.userNo"></el-input>
                            </el-form-item>
                        </el-col>

                        <el-col :span="16">
                            <el-form-item prop="userBirthday" label="出生年月：">
                                <input type="hidden" name="user.birthday" :value="teacherForm.userBirthday">
                                <el-date-picker
                                        v-model="teacherForm.userBirthday"
                                        type="month"
                                        :editable="false"
                                        value-format="yyyy-MM"
                                        :default-value="defaultBirthdayDate"
                                        :picker-options="userBirthdayPickerOptions"
                                        style="vertical-align: top"
                                        placeholder="选择出生时间">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="24">
                            <el-form-item prop="serviceIntentionIds" label="服务意向：">
                                <el-checkbox-group v-model="teacherForm.serviceIntentionIds">
                                    <el-checkbox v-for="item in masterServices" name="serviceIntentionIds"
                                                 :key="item.value"
                                                 :label="item.value">{{item.label}}
                                    </el-checkbox>
                                </el-checkbox-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="16">
                            <el-form-item label="证件号：" style="vertical-align: top "
                                          :rules="teacherRules[teacherForm.userIdType == 1 ? 'idNumberIdentity' : 'idNumberPassport']">
                                <input type="hidden" name="user.idType" :value="teacherForm.userIdType">
                                <input type="hidden" name="user.idNumber" :value="teacherForm.userIdNumber">
                                <el-input placeholder="请输入证件号" v-model="teacherForm.userIdNumber"
                                          style="vertical-align: top"
                                          class="input-with-select_identity">
                                    <el-form-item prop="userIdType" slot="prepend" style="margin-bottom: 0;height: 26px;">
                                        <el-select v-model="teacherForm.userIdType" placeholder="请选择证件类型">
                                            <el-option v-for="idType in idTypes" :label="idType.label"
                                                       :value="idType.value" :key="idType.id"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <%--<el-col :span="8">--%>
                            <%--<el-form-item v-if="teacherForm.serviceIntentionIds.indexOf('4') > -1" prop="category"--%>
                                          <%--label="导师类型：">--%>
                                <%--<input type="hidden" name="category" :value="teacherForm.category">--%>
                                <%--<el-select v-model="teacherForm.category" placeholder="请选择" clearable--%>
                                           <%--style="vertical-align: top">--%>
                                    <%--<el-option--%>
                                            <%--v-for="item in teacherCategories"--%>
                                            <%--:key="item.value"--%>
                                            <%--:label="item.label"--%>
                                            <%--:value="item.value">--%>
                                    <%--</el-option>--%>
                                <%--</el-select>--%>
                            <%--</el-form-item>--%>
                            <%--<div v-else style="height: 28px; margin-bottom: 18px;"></div>--%>
                        <%--</el-col>--%>

                        <el-col :span="8">
                            <el-form-item prop="educationType" label="学历类别：">
                                <input type="hidden" name="educationType" :value="teacherForm.educationType">
                                <el-select v-model="teacherForm.educationType" clearable>
                                    <el-option v-for="item in educationTypes" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="userEducation" label="学历：">
                                <input type="hidden" name="user.education" :value="teacherForm.userEducation">
                                <el-select v-model="teacherForm.userEducation" clearable>
                                    <el-option v-for="item in enducationLevels" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="discipline" label="学科门类：">
                                <input type="hidden" name="discipline" :value="teacherForm.discipline">
                                <el-select v-model="teacherForm.discipline" clearable>
                                    <el-option v-for="item in professionalTypes" :value="item.value" :key="item.id"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="userDegree" label="学位类别：">
                                <input type="hidden" name="user.degree" :value="teacherForm.userDegree">
                                <el-select v-model="teacherForm.userDegree" clearable>
                                    <el-option v-for="item in degreeTypes" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>

                        <el-col :span="16">
                            <el-form-item label="院系/专业：" prop="userProfessional">
                                <input type="hidden" name="user.office.id" :value="officeId">
                                <input type="hidden" name="user.professional" :value="professionalId">
                                <el-cascader
                                        style="width: 100%"
                                        ref="cascader"
                                        :options="collegesTree"
                                        :clearable="true"
                                        filterable
                                        change-on-select
                                        v-model="teacherForm.userProfessional"
                                        :props="cascaderCollegesProps"
                                >
                                </el-cascader>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="workUnitType" label="单位类别：">
                                <input type="hidden" name="workUnitType" :value="teacherForm.workUnitType">
                                <el-select v-model="teacherForm.workUnitType" clearable>
                                    <el-option v-for="item in workTypes" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="workUnit" label="工作单位：">
                                <el-input name="workUnit" v-model="teacherForm.workUnit"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="recommendedUnits" label="推荐单位：">
                                <el-input name="recommendedUnits" v-model="teacherForm.recommendedUnits"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="industry" label="行业：">
                                <el-input name="industry" v-model="teacherForm.industry"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="postTitle" label="职务：">
                                <el-input name="postTitle" v-model="teacherForm.postTitle"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="technicalTitle" label="职称：">
                                <%--<el-input name="technicalTitle" v-model="teacherForm.technicalTitle"></el-input>--%>
                                <el-select v-model="teacherForm.technicalTitle" clearable>
                                    <el-option v-for="item in expertJobNames" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="firstBank" label="开户银行：">
                                <el-input name="firstBank" v-model="teacherForm.firstBank"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="bankAccount" label="账号：">
                                <el-input name="bankAccount" v-model="teacherForm.bankAccount"></el-input>
                            </el-form-item>
                        </el-col>


                        <el-col :span="24">
                            <el-form-item label="技术领域：" prop="userDomainIdList"
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

                        <el-col :span="24" style="height:auto">
                            <el-form-item label="关键词：" prop="keyWords"
                            >
                                <el-tag
                                        :key="tag.id"
                                        v-for="(tag, index) in teacherForm.keywords"
                                        closable
                                        type="info"
                                        size="medium"
                                        :disable-transitions="false"
                                        @close="handleCloseKeyWord(tag, index)">
                                    {{tag.keyword}}
                                    <input type="hidden" name="keywords" :value="tag.keyword">
                                </el-tag>
                                <el-input
                                        style="display: inline-block;width:auto;"
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
                        <el-col :span="24">
                            <el-form-item label="工作经历：">
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
                                    <e-col-item label="项目简介：" label-width="72px">
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

            <div class="user_detail-inner user_detail-inner-experience" style="margin-bottom: 60px;">
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
                                    <e-col-item label="大赛简介：" label-width="72px">
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

            <review-task :user-id="teacherForm.userid"></review-task>

        </div>
    </div>

    <el-dialog
            title="选择城市"
            :visible.sync="dialogVisibleCityPicker"
            :before-close="handleCityPickerClose">
        <city-picker v-model="teacherForm.userCountry" :city-data="city" :tab-active-key.sync="cityTab"
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

    <el-dialog
            title="修改手机"
            width="500px"
            :visible.sync="dialogVisibleChangeMobile"
            :before-close="handleChangeMobileClose">
        <mobile-form ref="dialogMobileForm" :mobile.sync="newMobile" :old-mobile="teacherStaticDetail.userMobile"
                     @update-user-mobile="updateUserMobile"
                     :is-add="!teacherStaticDetail.userMobile"></mobile-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="isUpdating" @click.stop.prevent="handleChangeMobileClose">取消</el-button>
            <el-button size="mini" :disabled="isUpdating" type="primary" @click.stop.prevent="handleUpdateMobile">确定
            </el-button>
        </div>
    </el-dialog>


</div>

<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin, Vue.verifyExpressionMixin],
        data: function () {
            var teacherData = JSON.parse(JSON.stringify(${fns: toJson(teacherData)})) || {};
            var backTeacherExpansion = teacherData.backTeacherExpansion;
            var user = backTeacherExpansion.user || {};
            var keywords = teacherData.tes;
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
            var userBirthday = user.birthday ? moment(user.birthday).format('YYYY-MM') : '';
            var userProfessional = []
//            colleges = colleges.filter(function (item) {
//                return item.id !== '1';
//            });
            if(user.officeId){
                userProfessional.push('1');
                if(user.officeId != '1'){
                    userProfessional.push(user.officeId);
                }
            }
            if(user.professional && user.professional != user.officeId){
                userProfessional.push(user.professional)
            }

            return {
                teacherForm: {
                    id: backTeacherExpansion.id,
                    userid: user.id,
                    userPhoto: user.photo,
                    teachertype: backTeacherExpansion.teachertype == '0' || !backTeacherExpansion.teachertype  ? '' : backTeacherExpansion.teachertype,
                    userName: user.name,
                    userLoginName: user.loginName,
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
                    mainExp: backTeacherExpansion.mainExp
                },

                teacherStaticDetail: {},

                userPhoto: user.photo,
                userAvatar: '',
                userPicFile: null,
                isUpdating: false,


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
                expertJobNames: technical,
                yesNo: yesNo,
                technologyFields: technologyFields,
                teacherCategories: teacherCategories,
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

                isShowStaticTeacherDetail: true,
                dialogVisibleChangeMobile: false,
                newMobile: '',
                backMessage: '${message}',
                cascaderCollegesProps: {
                    label: 'name',
                    value: 'id',
                    children: 'children'
                }

            }
        },
        computed: {
            officeId: function () {
                var userProfessional = this.teacherForm.userProfessional;
                if(this.professionalId && userProfessional.length - 2 > -1){
                    return userProfessional[userProfessional.length - 2];
                }
                return ''
            },


            professionalId: function () {
                var userProfessional = this.teacherForm.userProfessional;
                if(userProfessional && userProfessional.length != 0){
                    return userProfessional[userProfessional.length - 1];
                }
            },
            defaultBirthdayDate: function () {
                return moment(new Date()).subtract(20, 'year').format('YYYY-MM');
            },
            masterTypeEntries: function () {
                return this.getEntries(this.masterTypes)
            },
            sexEntries: function () {
                return this.getEntries(this.sexes)
            },
            idTypesEntries: function () {
                return this.getEntries(this.idTypes)
            },
            masterServicesEntries: function () {
                return this.getEntries(this.masterServices)
            },
            teacherCategoryEntries: function () {
                return this.getEntries(this.teacherCategories)
            },
            technologyFieldEntries: function () {
                return this.getEntries(this.technologyFields)
            },
            degreeTypeEntries: function () {
                return this.getEntries(this.degreeTypes)
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
            cityIdKeyData: function () {
                var data = {};
                for (var i = 0; i < this.city.length; i++) {
                    var city = this.city[i];
                    data[city.id] = city;
                }
                return data;
            },
            expertJobNamesEntries:function () {
                return this.getEntries(this.expertJobNames);
            },


            teacherRules: {
                get: function () {
                    var self = this;
                    var validateUserName = this.validateUserName;
                    var validateUserNo = this.validateUserNo;
                    var isCompanyMaster = this.teacherForm.teachertype === '2';
                    var validateIdentity = this.validateIdentity;
                    var validateEmail = this.validateEmail;
                    var validateLoginNameTeacher = this.validateLoginNameTeacher;
                    return {
                        'teachertype': [{required: true, message: '请选择导师来源', trigger: 'blur'}],
                        'userLoginName': [
                            {required: true, message: '请填写登录名', trigger: 'blur'},
                            {min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'},
                            {validator: validateLoginNameTeacher, trigger: 'blur'}
                        ],
                        'userName': [
                            {required: true, message: '请输入姓名', trigger: 'blur'},
                            {max: 15, message: '请输入长度在 1 到 15 个字符', trigger: 'blur'},
                            {validator: validateUserName, trigger: 'blur'}
                        ],

                        'userNo': [
                            {required: !isCompanyMaster, message: '请输入职工号', trigger: 'blur'},
                            {min: 2, max: 24, message: '请输入2-24位字符的职工号', trigger: 'blur'},
                            {validator: validateUserNo, trigger: 'blur'}
                        ],
                        'userSex': [
                            {required: !isCompanyMaster, message: '请选择性别', trigger: 'change'},
                        ],
                        'userNational': [
                            {max: 24, message: '请输入不大于24位字符的民族', trigger: 'blur'},
                        ],
                        'serviceIntentionIds': [
                            {required: !isCompanyMaster, message: '请选择服务意向', trigger: 'change'},
                        ],
                        'category': [
                            {required: true, message: '请选择导师类型', trigger: 'change'},
                        ],
                        'userIdType': [
                            {required: !isCompanyMaster, message: '请选择证件类别', trigger: 'change'}
                        ],
                        'idNumberIdentity': [
                            {required: !isCompanyMaster, message: '请填写身份证号码', trigger: 'blur'},
                            {validator: validateIdentity, trigger: 'blur'}
                        ],
                        'idNumberPassport': [
                            {required: !isCompanyMaster, message: '请填写护照号码', trigger: 'blur'},
                            {min: 6, max: 24, message: '请填写6-24位字符', trigger: 'blur'}
                        ],
                        'educationType': [
                            {required: !isCompanyMaster, message: '请选择学历类别', trigger: 'change'}
                        ],
                        'userEducation': [
                            {required: !isCompanyMaster, message: '请选择学历', trigger: 'change'}
                        ],
                        'industry': [
                            {max: 48, message: '请输入不大于48位字符的行业', trigger: 'blur'},
                        ],
                        'postTitle': [
                            {max: 48, message: '请输入不大于48位字符的职务', trigger: 'blur'},
                        ],
                        'technicalTitle': [
                            {max: 48, message: '请输入不大于48位字符的职称', trigger: 'blur'},
                        ],
                        'firstBank': [
                            {max: 48, message: '请输入不大于48位字符的开户银行名称', trigger: 'blur'},
                        ],
                        'bankAccount': [
                            {max: 128, message: '请输入不大于128位字符的账号', trigger: 'blur'},
                        ],
                        'userEmail': [
                            {required: !isCompanyMaster, message: '请输入邮箱地址', trigger: 'blur'},
                            {validator: validateEmail, trigger: 'blur'}
                        ],
                        'workUnit': [
                            {max: 128, message: '请输入不大于128位字符单位名称', trigger: 'blur'},
                        ],
                        'recommendedUnits': [
                            {max: 128, message: '请输入不大于128位字符的推荐单位名称', trigger: 'blur'},
                        ],
                        'experience': [
                            {max: 2000, message: '请输入不大于2000位字符的个人简介', trigger: 'blur'},
                        ],
                    }
                }
            }
        },

        methods: {


            handleChangeMobileClose: function () {
                this.$refs.dialogMobileForm.clearMobileForm();
                this.dialogVisibleChangeMobile = false;
            },

            handleChangeMobile: function () {
                this.dialogVisibleChangeMobile = true;
            },


            handleUpdateMobile: function () {
                this.$refs.dialogMobileForm.mobileFormValidate()
            },

            updateUserMobile: function (mobileForm) {
                var self = this;
                this.isUpdating = true;
                this.$axios.post('/sys/frontStudentExpansion/updateUserMobile?mobile=' + mobileForm.mobile + '&userId=' + this.teacherForm.userid).then(function (data) {
                    if (data.status) {
                        self.$message.success(self.teacherForm.userMobile  ? '修改手机号成功' : '添加手机号成功');
                        self.teacherForm.userMobile = mobileForm.mobile;
                        self.teacherStaticDetail.userMobile = mobileForm.mobile;
                        self.dialogVisibleChangeMobile = false;
                    }else {
                        self.$message.error(data.msg);
                    }
                    self.isUpdating = false;
                }).catch(function () {
                    self.isUpdating = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            cancleEditTeacherForm: function () {
                this.$refs.teacherForm.resetFields();
                this.isShowStaticTeacherDetail = true;
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
                this.$nextTick(function () {
                    this.$refs.teacherForm.$el.submit()
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
                    url: '/ftp/ueditorUpload/cutImgToTempDir?folder=user.photo&x=' + parseInt(data.x) + '&y=' + parseInt(data.y) + '&width=' + parseInt(data.width) + '&height=' + parseInt(data.height),
                    data: formData
                }).then(function (response) {
                    var data = response.data;
                    if (data.state === 'SUCCESS') {
                        self.moveFile(data.ftpUrl);
                    }else {
                        self.userPicFile = null;
                        self.isUpdating = false;
                        self.dialogVisibleChangeUserPic = false;
                        self.$message.error(data.msg);
                    }
                }).catch(function (error) {

                })
            },

            moveFile: function (url) {
                var self = this;
                return this.$axios.post('/sys/user/ajaxUpdatePhoto?photo=' + url + "&userId=" + this.teacherForm.userid).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        data = data.datas;
                        self.dialogVisibleChangeUserPic = false;
                        self.userAvatar = self.addFtpHttp(data.photo);
                        self.userPhoto = data.photo;
                        self.userPicFile = null;
                        $('#headerUserBlock .user-small-pic,#headerUserBlock .user-info-pic img').attr('src', self.userAvatar)
                        self.$message.success(response.data.msg);
                    }else {
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
                    if(inputValue.length > 12){
                        this.$message({
                            type: 'error',
                            message: '请输入小于12个字符的关键字'
                        })
                        return false;
                    }
                    if (this.hasEveryKeyword(inputValue)) {
                        this.teacherForm.keywords.push({
                            id: Date.now(),
                            keyword: inputValue
                        });
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
        created: function () {
            this.teacherStaticDetail = JSON.parse(JSON.stringify(this.teacherForm));
        },
        mounted: function () {
            if(this.backMessage){
                this.$alert(this.backMessage, '提示', {
                    type: this.backMessage.indexOf('成功') > -1 ? 'success' :'error',
                }).catch(function () {

                })
                this.backMessage = '';
            }

        }
    })

</script>

</body>
</html>