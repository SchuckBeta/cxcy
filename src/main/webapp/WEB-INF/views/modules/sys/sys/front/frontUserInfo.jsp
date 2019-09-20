<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="creative"/>


    <script src="/js/components/cityDropDown/cityDropDown.js"></script>
    <script src="/js/components/cityDropDown/cityPicker.js"></script>


</head>
<body>

<div id="app" v-show="pageLoad" style="display: none;" class="container page-container mgb-60">
    <el-breadcrumb class="mgb-20" separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>双创简历</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="user_avatar-sidebar">
        <div class="user-avatar">
            <div class="avatar-pic">
                <img :src="studentForm.user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter">
            </div>
            <el-button type="primary" size="mini" @click.stop.prevent="handleChangeUserPicOpen">更换图像
            </el-button>
        </div>
        <ul class="user-info_menu">
            <li class="active"><a href="javascript: void(0);"><i class="iconfont icon-user"></i>双创简历</a></li>
            <li v-if="casHasLogin"><a href="${ctxFront}/sys/frontStudentExpansion/frontUserPassword"><i
                    class="iconfont icon-14"></i>登录信息</a></li>
            <li v-if="hasMvcode"><a href="${ctxFront}/sys/frontStudentExpansion/frontUserMobile"><i
                    class="iconfont icon-unie64f"></i>手机信息</a>
            </li>
        </ul>
    </div>
    <div class="user_detail-container">
        <%--<div class="print-page">--%>
        <%--<a href="javascript: void(0);"><i class="iconfont icon-dayin"></i>打印</a>--%>
        <%--</div>--%>
        <div class="user_detail-title">
            <i class="iconfont icon-user"></i><span class="text">双创简历</span>
        </div>
        <div class="user_detail-wrap">
            <div class="user_detail-inner">
                <div class="user_detail-title-handler">
                    <div class="ud-row-handler">
                        <el-button type="text" title="编辑" size="mini" v-show="!isEditBaseInfo"
                                   :disabled="disabled || isEditBaseInfo"
                                   @click.stop="showEditForm"><i
                                class="iconfont icon-bianji"></i></el-button>
                        <el-button type="text" title="取消" size="mini" v-show="isEditBaseInfo"
                                   :disabled="disabled || !isEditBaseInfo"
                                   @click.stop="isEditBaseInfo = false"><i
                                class="iconfont icon-mianfeiquxiao"></i></el-button>
                        <el-button type="text" title="保存" size="mini" :disabled="disabled || !isEditBaseInfo"
                                   @click.stop="updateForm"><i
                                class="iconfont icon-iconset0237"></i></el-button>
                    </div>
                    <div class="ud-row-title"><span class="name">基本信息</span></div>
                </div>
                <el-row v-show="!isEditBaseInfo" :gutter="10" label-width="90px">
                    <el-col :span="8">
                        <e-col-item label="姓名：" align="right">{{userBaseInfo.user.name}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="性别：" align="right">{{userBaseInfo.user.sex === '1' ? '男': '女'}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="出生时间：" align="right">{{userBaseInfo.user.birthday}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="电子邮箱：" align="right">{{userBaseInfo.user.email}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="手机号：" align="right">{{userBaseInfo.user.mobile}}
                            <span v-if="!userBaseInfo.user.mobile"
                                  :class="{'empty-color empty': !userBaseInfo.user.mobile}">请添加手机号</span>
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="国家/户籍：" align="right">{{userBaseInfo.user.country |
                            userCountryName(cityIdKeyData)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="民族：" align="right">{{userBaseInfo.user.national}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="政治面貌：" align="right">{{userBaseInfo.user.political}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="证件号：" align="right">
                            {{userBaseInfo.user.idType | selectedFilter(idTypesEntries)}}
                            {{userBaseInfo.user.idNumber}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="QQ：" align="right">{{userBaseInfo.user.qq}}</e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="联系地址：" align="right">{{userBaseInfo.address}}</e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="个人简介：" align="right">
                            <span class="white-space-pre" v-html="userBaseInfo.user.introduction"></span>
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="入学年月：" align="right">{{userBaseInfo.enterdate}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="学年制：" align="right">{{userBaseInfo.cycle |
                            selectedFilter(cyclesEntries)}}<span v-if="userBaseInfo.cycle">年</span>
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="现状：" align="right">{{userBaseInfo.currState |
                            selectedFilter(currStateEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="学号：" align="right">{{userBaseInfo.user.no}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="院系/专业：" align="right">
                            <el-tooltip
                                    :content="userBaseInfo.user.professionalIds | cascaderCollegeFilter(officeEntity)"
                                    popper-class="white"
                                    placement="bottom">
                                    <span class="break-ellipsis">
                                        {{userBaseInfo.user.professionalIds | cascaderCollegeFilter(officeEntity)}}
                                    </span>
                            </el-tooltip>
                        </e-col-item>
                    </el-col>
                    <el-col :span="8" v-show="isInCurStates('tClass')">
                        <e-col-item label="班级：" align="right">{{userBaseInfo.tClass}}班</e-col-item>
                    </el-col>
                    <el-col :span="8" v-show="isInCurStates('userEducation')">
                        <e-col-item label="学历：" align="right">{{userBaseInfo.user.education
                            |selectedFilter(enducationLevelsEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8" v-show="isInCurStates('userDegree')">
                        <e-col-item label="学位：" align="right">{{userBaseInfo.user.degree |
                            selectedFilter(degreeTypesEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8" v-show="isInCurStates('instudy')">
                        <e-col-item label="在读学位：" align="right">{{userBaseInfo.instudy |
                            selectedFilter(degreeTypesEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8" v-show="isInCurStates('temporaryDate')">
                        <e-col-item label="休学时间：" align="right">{{userBaseInfo.temporaryDate}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8" v-show="isInCurStates('graduation')">
                        <e-col-item label="毕业时间：" align="right">{{userBaseInfo.graduation}}</e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="技术领域：" align="right">{{userBaseInfo.user.domainIdList |
                            checkboxFilter(technologyFieldsEntries)}}
                        </e-col-item>
                    </el-col>
                </el-row>
                <el-form v-if="isEditBaseInfo" :model="studentForm" ref="studentForm" :disabled="disabled" size="mini"
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
                            <el-form-item label="性别：" prop="user.sex" :rules="sexRules">
                                <el-radio-group v-model="studentForm.user.sex">
                                    <el-radio-button v-for="item in sexes" :key="item.value" :label="item.value">
                                        {{item.label}}
                                    </el-radio-button>
                                </el-radio-group>
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
                            <el-form-item label="电子邮箱：" prop="user.email" :rules="stuEmailRules">
                                <el-input name="user.email" v-model="studentForm.user.email"
                                          maxlength="128"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="user.mobile" label="手机号" :rules="mobileRules">
                                <span style="font-size: 12px;" :class="{'empty-color empty': !studentForm.user.mobile}">{{studentForm.user.mobile}}</span>
                                <el-button type="text" size="mini" @click.stop.prevent="handleChangeMobile">
                                    {{studentForm.user.mobile ? '修改' : '添加'}}
                                </el-button>
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
                                          :rules="stuIdentityRules">
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
                            <el-form-item label="个人简介：" prop="user.introduction" :rules="introductionRules">
                                <el-input name="user.introduction" type="textarea" :rows="2"
                                          placeholder="请输入个人简介,限200字" maxlength="200"
                                          v-model="studentForm.user.introduction"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="入学年月：" prop="enterdate" :rules="enterdateRules">
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
                            <el-form-item label="学年制：" prop="cycle" :rules="cycleRules">
                                <el-select v-model="studentForm.cycle" @change="studentForm.currState = ''"
                                           placeholder="-请选择-">
                                    <el-option v-for="cycle in cycles" :value="cycle.value" :label="cycle.label+'年'"
                                               :key="cycle.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="现状：" prop="currState" :rules="currStateRules">
                                <el-select v-model="studentForm.currState" placeholder="-请选择-" clearable>
                                    <el-option v-for="currState in currStatesCp" :value="currState.value"
                                               :disabled="currState.disabled"
                                               :label="currState.label" :key="currState.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="学号：" prop="user.no" :rules="userAsStuNoRules">
                                <el-input name="user.no" v-model="studentForm.user.no" placeholder="请输入学号"
                                          maxlength="64"></el-input>
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
                        <el-col v-if="isInCurStates('tClass')" :span="8">
                            <el-form-item label="班级：" prop="tClass">
                                <el-input name="tClass" v-model="studentForm.tClass" placeholder="请输入班级，限64字符"
                                          maxlength="64"><span slot="append">班</span></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col v-if="isInCurStates('user.education')" :span="8">
                            <el-form-item label="学历：" prop="user.education" :rules="educationRules">
                                <el-select v-model="studentForm.user.education" placeholder="-请选择-">
                                    <el-option v-for="enducationLevel in enducationLevels"
                                               :value="enducationLevel.value" :label="enducationLevel.label"
                                               :key="enducationLevel.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col v-if="isInCurStates('user.degree')" :span="8">
                            <el-form-item label="学位：" prop="user.degree" :rules="degreeRules">
                                <el-select v-model="studentForm.user.degree" placeholder="-请选择-" clearable>
                                    <el-option v-for="degreeType in degreeTypes" :value="degreeType.value"
                                               :label="degreeType.label" :key="degreeType.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col v-if="isInCurStates('instudy')" :span="8">
                            <el-form-item label="在读学位：" prop="instudy" :rules="instudyRules">
                                <el-select v-model="studentForm.instudy" placeholder="-请选择-" clearable>
                                    <el-option v-for="degreeType in degreeTypes" :value="degreeType.value"
                                               :label="degreeType.label" :key="degreeType.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8" v-if="isInCurStates('temporaryDate')">
                            <el-form-item label="休学时间：" prop="temporaryDate" :rules="temporaryDateRules">
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
                            <el-form-item label="毕业时间：" prop="graduation" :rules="graduationRules">
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
                            <el-form-item label="技术领域：" prop="user.domainIdList" :rules="domainIdListRules">
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
                     style="margin: -20px;"
                     @selected="cityHandleSelected"></city-picker>
    </el-dialog>

    <el-dialog
            :title="userBaseInfo.user.mobile ? '修改手机号码' : '添加手机号码'"
            width="500px"
            :visible.sync="dialogVisibleChangeMobile"
            :close-on-click-modal="false"
            :before-close="handleChangeMobileClose">
        <mobile-form ref="dialogMobileForm" :mobile.sync="newMobile" :old-mobile="userBaseInfo.user.mobile"
                     @update-user-mobile="updateUserMobile"
                     :is-add="!userBaseInfo.userMobile"></mobile-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="disabled" @click.stop="handleChangeMobileClose">取消</el-button>
            <el-button size="mini" :disabled="disabled" type="primary" @click.stop="handleUpdateMobile">确定
            </el-button>
        </div>
    </el-dialog>


</div>


<script>

    'use strict';


    new Vue({
        el: '#app',
        mixins: [Vue.verifyRules],
        data: function () {
            var self = this;
            var isEdit = '${isEdit}';
            return {
                isEditBaseInfo: isEdit === '1',
                userBaseInfo: {
                    user: {}
                },
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
                studentId: '${cuser}',
                isUpdating: false,
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

                dialogVisibleChangeMobile: false, //修改手机号
                newMobile: '',

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

                currStateRules: [
                    {required: true, message: '请选择现状', trigger: 'change'}
                ],
                domainIdListRules: [
                    {required: true, message: '请选择技术领域', trigger: 'change'}
                ],
                cycleRules: [{required: true, message: '请选择学制年', trigger: 'change'}],
                enterdateRules: [{required: true, message: '请选择入学年份', trigger: 'change'}],
                sexRules: [{required: true, message: '请选择性别', trigger: 'change'}],
                temporaryDateRules: [{required: true, message: '请选择休学时间', trigger: 'change'}],
                graduationRules: [{required: true, message: '请选择毕业时间', trigger: 'change'}],
                instudyRules: [{required: true, message: '请选择在读学位', trigger: 'change'}],
                degreeRules: [{required: true, message: '请选择学位', trigger: 'change'}],
                educationRules: [{required: true, message: '请选择学历', trigger: 'change'}],
                introductionRules: [{required: true, message: '请填写个人简介', trigger: 'blur'}],
                mobileRules: [{required: true, message: '请添加手机号', trigger: 'blur'}]
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

            stuEmailRules: function () {
                var emailRules = this.emailRules;
                var rules = [{required: true, message: '请输入邮箱', trigger: 'change'}]
                return rules.concat(emailRules);
            },

            stuIdentityRules: function () {
                var idRules = this.studentForm.user.idType == 1 ? this.identityRules : this.idNumberPassport;
                var rules = [{required: true, message: '请输入证件号', trigger: 'change'}]
                return rules.concat(idRules);
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
            technologyFieldsEntries: {
                get: function () {
                    return this.getEntries(this.technologyFields)
                }
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
                this.$refs.dialogMobileForm.mobileFormValidate();

            },

            updateUserMobile: function (mobileForm) {
                var self = this;
                this.disabled = true;
                this.$axios.post('/sys/frontStudentExpansion/updateUserMobile?mobile=' + mobileForm.mobile + '&userId=' + mobileForm.id).then(function (data) {
                    if (data.status) {
                        self.$message.success(self.userBaseInfo.user.mobile  ? '修改手机号成功' : '添加手机号成功');
                        self.userBaseInfo.user.mobile = mobileForm.mobile;
                        self.studentForm.user.mobile = mobileForm.mobile;
                        self.dialogVisibleChangeMobile = false;
                        self.$refs.studentForm.validateField('user.mobile');
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.disabled = false;
                })
            },


            validateForm: function () {
                var self = this;
                this.$refs.studentForm.validate(function (valid) {
                    if (valid) {
                        self.saveStudentForm();
                    }
                })
            },

            showEditForm: function () {
                var self = this;
                if (this.isEditBaseInfo) {
                    this.$confirm('是否保存信息', '提示', {
                        confirmButtonText: '保存',
                        cancelButtonText: '取消',
                        type: 'warning',
                    }).then(function () {
                        self.updateForm()
                    }).catch(function () {
                        self.catchCancelShow();
                    })
                } else {
                    this.catchCancelShow()
                }
            },

            updateForm: function () {
                this.validateForm();
            },

            catchCancelShow: function () {
                this.assignFormData(this.studentForm, this.userBaseInfo)
                this.isEditBaseInfo = true;
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
                if (studentForm.user.birthday) {
                    studentForm.user.birthday = moment(studentForm.user.birthday).valueOf();
                }
                if (studentForm.enterdate) {
                    studentForm.enterdate = moment(studentForm.enterdate).valueOf();
                }
                if (studentForm.graduation) {
                    studentForm.graduation = moment(studentForm.graduation).valueOf();
                }
                if (studentForm.temporaryDate) {
                    studentForm.temporaryDate = moment(studentForm.temporaryDate).valueOf();
                }
                delete studentForm.user.professionalIds;
                this.disabled = true;
                this.$axios.post('/sys/frontStudentExpansion/saveStudentExp', studentForm).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$alert('保存成功', '提示', {
                            type: 'success',
                            showClose: false
                        })
                        self.setStudentForm(data.data);
                        self.isEditBaseInfo = false;
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

                return this.$axios.post('/sys/user/ajaxUpdatePhoto?photo=' + url + "&userId=" + this.studentForm.user.id).then(function (response) {
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
                this.$axios.get('/sys/frontStudentExpansion/ajaxGetUserProjectById?userId=' + this.studentForm.user.id).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.projectList = data.datas || [];
                    }
                })
            },

            ajaxGetUserGContestById: function () {
                var self = this;
                this.$axios.get('/sys/frontStudentExpansion/ajaxGetUserGContestById?userId=' + this.studentForm.user.id).then(function (response) {
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
                this.$axios.get('/sys/frontStudentExpansion/ajaxGetUserInfoById?userId=' + this.studentId).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        var student = data.datas || {};
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
                this.userBaseInfo = JSON.parse(JSON.stringify(this.studentForm))
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
            var self = this;

            window.onbeforeunload = function () {
                if (self.isEditBaseInfo) {
                    window.event.returnValue = '您有信息还未保存，确认离开吗？'
                    return window.event.returnValue
                }
            }
        }
    })


</script>

</body>
</html>