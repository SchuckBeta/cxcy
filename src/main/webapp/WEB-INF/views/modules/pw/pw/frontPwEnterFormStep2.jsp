<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>--%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${fns:getConfig('frontTitle')}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <link rel="stylesheet" href="${ctxStatic}/webuploader/webuploader.css">
    <script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/vue/vue.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/frontCyjd/v-upload-file.js?v=11111"></script>
    <script type="text/javascript" src="${ctxJs}/frontCyjd/uploader.component.js?v=110"></script>
    <%--<script type="text/javascript" src="${ctxJs}/frontCyjd/uploaderFile.js"></script>--%>
    <style>
        .tab-content > .tab-pane {
            display: block;
            height: 0;
            overflow: hidden;
        }

        .tab-content > .tab-pane.active {
            height: auto;
        }

        .dialog-cy-sub .ui-dialog-titlebar-close {
            display: none;
        }

        .file-error {
            display: block;
        }
        .form-horizontal-tabs label.error{
            font-size: 12px;
        }
        .form-horizontal-tabs{
            border-bottom: none;
        }
        .form-horizontal-tabs .tab-item-block{
            position: relative;
            float: left;
            padding-left: 27px;
            width: 180px;
            height: 34px;
        }
        .form-horizontal-tabs .tab-item-block .checkbox-fake-box{
            display: block;
            position: absolute;
            left: 15px;
            top: 10px;
            margin: 0;

        }
        .form-horizontal-tabs .tab-item-block.active{
            border: solid #ddd;
            border-width:  1px 1px 0;
            border-radius: 3px 3px 0 0;
            background-color: #fff;
            margin-bottom: -1px;
        }
        .form-horizontal-tabs .active a{
            border-bottom: 0;
            color: #333;
        }
        .form-horizontal-tabs input[type="checkbox"]{
            display: block;
            position: relative;
            z-index: 10;
        }
        .form-horizontal-tabs .checkbox-fake{
            position: absolute;
            left: 0;
            top: 0;
            background-color: #e9432d;
        }
        .form-horizontal-tabs input[type="checkbox"]:checked + .checkbox-fake:after{
            color: #fff;
        }
        .form-horizontal-tabs .tab-item-block input[type="radio"],    .form-horizontal-tabs .tab-item-block input[type="checkbox"]{
            margin: 0;
        }
        .form-horizontal-tabs .tab-item{
            padding-bottom: 0;
            line-height: 32px;
        }
        .form-horizontal-tabs  .tab-item{
            padding-left: 13px;
        }
        .form-horizontal-tabs .form-group{
            border-bottom: 1px solid #eee;
            height: 34px;
        }

    </style>
</head>
<body>
<div id="enterApplyStepTwo" class="container container-ct">
    <ol class="breadcrumb" style="margin-top: 0">
        <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
        <li class="active">创业基地</li>
        <li class="active">入驻申请</li>
    </ol>
    <div class="row-step-cyjd" style="width: 540px;">
        <div class="step-indicator" style="margin-right: -20px;">
            <a class="step completed">第一步（完善基本信息）</a> <a class="step completed">第二步（输入申报信息）</a>
        </div>
    </div>
    <div class="row-apply">
        <%--<div class="topbar clearfix">--%>
        <%--<a href="javascript:void (0)" class="pull-right btn-print">打印申请表</a>--%>
        <%--<span>填报日期：</span>--%>
        <%--</div>--%>
        <h4 class="titlebar">第二步：<span class="step-title">填写入驻申请信息</span><span class="gray-color"
                                                                               style="font-size: 14px;font-weight: normal">（选择入驻类别，并填写对应的申请信息内容，支持多选）</span>
        </h4>
        <form class="form-horizontal form-horizontal-tabs"
              v-validate="{form: 'enterTypeForm'}">
            <div class="form-group clearfix">
					<div class="tab-item-block" :class="{active: tabsActive.companyShow}">
                        <div class="checkbox-fake-box">
                            <input type="checkbox" name="enterType" v-model="formApply.hasCompany" value="true">
                            <div class="checkbox-fake"></div>
                        </div>
                        <a class="tab-item" href="javascript: void (0);" @click="tabToggle('companyShow')">申请入驻创业企业</a>
					</div>
                <div class="tab-item-block" :class="{active: tabsActive.projectShow}">
                    <div class="checkbox-fake-box">
                        <input type="checkbox" name="enterType" v-model="formApply.hasProject" value="true">
                        <div class="checkbox-fake"></div>
                    </div>
                    <a class="tab-item" href="javascript: void (0);" @click="tabToggle('projectShow')">申请入驻创业项目</a>
					</div>
                <div class="tab-item-block" :class="{active: tabsActive.teamShow}">
                    <div class="checkbox-fake-box">
                        <input type="checkbox" name="enterType" v-model="formApply.hasTeam" value="true">
                        <div class="checkbox-fake"></div>
                    </div>
                    <a class="tab-item" href="javascript: void (0);" @click="tabToggle('teamShow')">申请入驻创业团队</a>
                </div>
            </div>
        </form>

        <div class="tab-content">
            <div class="tab-pane" :class="{active: tabsActive.companyShow}">
                <form class="form-horizontal form-enter-apply" autocomplete="off"
                      v-validate="{form: 'companyForm'}">
                    <div class="form-group">
                        <label class="control-label col-xs-2"><i>*</i>企业名称：</label>
                        <div class="col-xs-8">
                            <input type="text" class="form-control required" minlength="3"
                                   maxlength="100" name="name" v-model="formApply.pwCompany.name"
                                   placeholder="输入长度为3-100位字符">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2"><i>*</i>联系方式：</label>
                        <div class="col-xs-8">
                            <input type="text" class="form-control required isMobileNumber"
                                   name="mobile" v-model="formApply.pwCompany.mobile" placeholder="输入合法的手机号码">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2"><i>*</i>企业注册地址：</label>
                        <div class="col-xs-8">
                            <input type="text" class="form-control required" name="address"
                                   minlength="3" maxlength="200"
                                   v-model="formApply.pwCompany.address" placeholder="输入最大长度为200位字符">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2"><i>*</i>工商注册号：</label>
                        <div class="col-xs-8">
                            <input type="text" class="form-control required letterNumber" maxlength="64" name="no"
                                   v-model="formApply.pwCompany.no" placeholder="输入最大长度为64位字符">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2"><i>*</i>公司注册资金：</label>
                        <div class="col-xs-2">
                            <div class="input-group">
                                <input style="width:200px;" type="text"
                                       class="form-control number required positiveNumber" maxlength="7"
                                       name="regMoney" v-model="formApply.pwCompany.regMoney" placeholder="输入最大长度为7位数">
                                <div class="input-group-addon">万元</div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2"><i>*</i>资金来源：</label>
                        <div class="col-xs-10">
                            <div class="input-group">
                                <c:forEach items="${fns:getDictList('pw_reg_mtype')}"
                                           var="pwRegMType">
                                    <label class="checkbox-inline"> <input type="checkbox"
                                                                           v-model="formApply.pwCompany.regMtypes"
                                                                           name="pwRegMType"
                                                                           class="required"
                                                                           value="${pwRegMType.value}">${pwRegMType.label}
                                    </label>
                                </c:forEach>
                                <%-- <form:checkboxes path="regMtypes" items="${fns:getDictList('pw_reg_mtype')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/> --%>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2"><i>*</i>企业法人：</label>
                        <div class="col-xs-8">
                            <input type="text" class="form-control required" minlength="2"
                                   maxlength="20" name="regPerson"
                                   v-model="formApply.pwCompany.regPerson" placeholder="输入长度为2-20位字符">
                        </div>
                    </div>
                    <!-- <div class="form-group">
                        <label class="control-label col-xs-2">备注：</label>
                        <div class="col-xs-8">
                            <textarea class="form-control"
                                      rows="3"
                                      maxlength="300"
                                      v-model="formApply.pwCompany.remarks"
                                      name="remarks" placeholder="输入最大长度为300位字符"></textarea>
                        </div>
                    </div> -->
                    <div class="form-group">
                        <label class="control-label col-xs-2">企业入驻说明：</label>
                        <div class="col-xs-8">
                            <textarea class="form-control"
                                      rows="3"
                                      maxlength="300"
                                      v-model="formApply.pwCompanyRemarks" placeholder="输入最大长度为300位字符"></textarea>
                        </div>
                    </div>
                </form>
                <div class="form-horizontal form-enter-apply">
                    <div class="form-group">
                        <label class="control-label col-xs-2"><i>*</i>附件：</label>
                        <div class="col-xs-8">
                            <uploader class-name="accessories-h34" :file-list="formApply.cfiles" @change-saveing="changeSaveing"
                                      tip="请上传公司营业执照，建议格式图片jpeg，png" :file-error-show="fileErrorShow"
                                      url="${ctxFront}/attachment/sysAttachment/ajaxUpload?ftype=30000&fileStep=33000&uid=${pwEnter.id}"></uploader>
                        </div>
                    </div>
                </div>
            </div>
            <div class="tab-pane" :class="{active: tabsActive.teamShow}">
                <form class="form-horizontal form-enter-apply" autocomplete="off"
                      v-validate="{form: 'teamForm'}">
                    <div class="form-group">
                        <label class="control-label col-xs-2"> <i>*</i>选择团队：
                        </label>
                        <div class="col-xs-6">
                            <select class="form-control required" name="team" @change="changeTeam"
                                    :disabled="formApply.projectPteam == '1'"
                                    v-model="formApply.teamId">
                                <option value="">-请选择-</option>
                                <option v-for="team in teamList" :value="team.id" :key="team.id">{{team.name | subs}}
                                </option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">学生团队：</label>
                        <div class="col-xs-10">
                            <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>姓名</th>
                                    <th>学号</th>
                                    <th>学院</th>
                                    <th>专业</th>
                                    <th>联系电话</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="(team, index) in teamDataStudents" :key="team.id">
                                    <td>{{index+1}}</td>
                                    <td>{{team.name}}</td>
                                    <td>{{team.no}}</td>
                                    <td>{{team.orgName}}</td>
                                    <td>{{team.professional}}</td>
                                    <td>{{team.mobile}}</td>
                                </tr>
                                <tr v-if="!teamDataStudents.length">
                                    <td colspan="6" class="gray-color">没有数据，请选择团队</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">导师团队：</label>
                        <div class="col-xs-10">
                            <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>姓名</th>
                                    <th>工号</th>
                                    <th>导师类型</th>
                                    <th>联系电话</th>
                                    <th>E-mail</th>
                                    <th>单位（学院或企业、机构）</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="(item, index) in teamDataTeachers" :key="item.id">
                                    <td>{{index+1}}</td>
                                    <td>{{item.name}}</td>
                                    <td>{{item.no}}</td>
                                    <td>{{item.teacherType}}</td>
                                    <td>{{item.mobile}}</td>
                                    <td>{{item.email}}</td>
                                    <td>{{item.orgName}}</td>
                                </tr>
                                <tr v-if="!teamDataTeachers.length">
                                    <td colspan="8" class="gray-color">没有数据，请选择团队</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">团队入驻说明：</label>
                        <div class="col-xs-10">
                           <textarea class="form-control"
                                     rows="3"
                                     maxlength="300"
                                     v-model="formApply.teamRemarks" placeholder="输入最大长度为300位字符"></textarea>
                        </div>
                    </div>
                </form>
                <div class="form-horizontal form-enter-apply">
                    <div class="form-group">
                        <label class="control-label col-xs-2">附件：</label>
                        <div class="col-xs-8">
                            <uploader class-name="accessories-h34" :file-list="formApply.tfiles" @change-saveing="changeSaveing"
                                      url="${ctxFront}/attachment/sysAttachment/ajaxUpload?ftype=30000&fileStep=31000&uid=${pwEnter.id}"></uploader>
                        </div>
                    </div>
                </div>
            </div>
            <div class="tab-pane" :class="{active: tabsActive.projectShow}">
                <form class="form-horizontal form-enter-apply" autocomplete="off"
                      v-validate="{form: 'projectForm'}">
                    <div class="form-group">
                        <label class="control-label col-xs-2"><i>*</i>关联项目：</label>
                        <div class="col-xs-6">
                            <select class="form-control required" name="project"
                                    @change="changeProject"
                                    v-model="formApply.projectId">
                                <option value="">-请选择-</option>
                                <option v-for="project in projectList" :value="project.id" :key="project.id">
                                    {{project.pName | subs}}
                                </option>
                            </select>
                        </div>
                    </div>
                    <!-- <div class="form-group">
                        <label class="control-label col-xs-2">项目名称：</label>
                        <div class="col-xs-10">
                            <p class="form-control-static" :class="{'gray-color': !projectInfo.name}">{{projectInfo.name
                                || '请选择项目'}}</p>
                        </div>
                    </div> -->
                    <div v-show="projectTypeLabel" class="form-group">
                        <label class="control-label col-xs-2">{{cateProTypeLabel}}：</label>
                        <div class="col-xs-10">
                            <p class="form-control-static">
                                <%-- <c:if test="${fn:contains(pwEnter.eproject.project.proType, '1,')}">
                                           ${fns:getDictLabel(pwEnter.eproject.project.type, 'project_style', '')}
                                       </c:if>
                                       <c:if test="${fn:contains(pwEnter.eproject.project.proType, '7,')}">
                                           ${fns:getDictLabel(pwEnter.eproject.project.type, 'competition_type', '')}
                                       </c:if> --%>
                                {{projectProTypeLabel}}
                            </p>
                        </div>
                    </div>
                    <div v-show="projectTypeLabel" class="form-group">
                        <label class="control-label col-xs-2">{{cateTypeLabel}}：</label>
                        <div class="col-xs-10">
                            <p class="form-control-static">
                                <%-- ${fns:getDictLabel(pwEnter.eproject.project.type, 'project_style', '')} --%>
                                <%-- <input id="proType" type="hidden" value="${pwEnter.eproject.project.proType}" />
                                <input id="proTyped" type="hidden" value="${pwEnter.eproject.project.proTyped}" />
                                <input id="proTypes" type="hidden" value="${pwEnter.eproject.project.proTypes}" />
                                ${fns:getDictLabel(pwEnter.eproject.project.proTyped, 'act_project_type', '')} --%>

                                {{projectTypeLabel}}
                            </p>
                        </div>
                    </div>

                    <div v-show="proTypes" class="form-group">
                        <label class="control-label col-xs-2">{{proTypes == '7' ? '大赛': '项目' }}简介：</label>
                        <div class="col-xs-8">
                            <p class="form-control-static" :class="{'gray-color': !projectInfo.introduction}">
                                {{projectInfo.introduction || '无说明'}}</p>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="control-label col-xs-2">关联团队：</label>
                        <div class="col-xs-10">
                            <label class="radio-inline">
                                <input type="radio" @change="linkTeam" v-model="formApply.projectPteam" value="1">项目团队
                            </label>
                            <label class="radio-inline">
                                <input type="radio" @change="linkTeam" v-model="formApply.projectPteam" value="0">其它团队
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2"><i>*</i>团队：</label>
                        <div class="col-xs-6">
                            <select class="form-control required" name="team" @change="changeLinkTeam"
                                    :disabled="formApply.projectPteam == '1'"
                                    v-model="linkTeamId">
                                <option value="">-请选择-</option>
                                <option v-for="team in teamList" :value="team.id" :key="team.id">{{team.name | subs}}
                                </option>
                            </select>

                            <%--<p class="form-control-static" style="overflow: hidden"--%>
                            <%--:class="{'gray-color': !projectTeamName}">{{projectTeamName ||--%>
                            <%--'请确认该项目是否有团队'}}</p>--%>
                        </div>
                        <div class="col-xs-3">
                            <span v-show="formApply.projectPteam == '0' && linkTeamId" class="help-inline primary-color"
                                  style="display: none;line-height: 34px;top: auto;left: auto">团队可变更</span>
                            <span v-show="projectInfo.team && projectInfo.team.state != '1' &&  formApply.projectPteam == '1'"
                                  class="help-inline primary-color"
                                  style="display: none;line-height: 34px;top: auto;left: auto">团队不存在或不是建设完成状态</span>
                        </div>
                    </div>
                    <!--   <div class="form-group">
                         <label class="control-label col-xs-2">项目拓展及传承：</label>
                         <div class="col-xs-10">
                             <p class="form-control-static">{{projectInfo.projectExpand}}</p>
                         </div>
                     </div> -->
                    <div class="form-group">
                        <label class="control-label col-xs-2">学生团队：</label>
                        <div class="col-xs-10">
                            <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>姓名</th>
                                    <th>学号</th>
                                    <th>学院</th>
                                    <th>专业</th>
                                    <th>联系电话</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="(team, index) in projectStudents" :key="team.id">
                                    <td>{{index+1}}</td>
                                    <td>{{team.name}}</td>
                                    <td>{{team.no}}</td>
                                    <td>{{team.orgName}}</td>
                                    <td>{{team.professional}}</td>
                                    <td>{{team.mobile}}</td>
                                </tr>
                                <tr v-if="!projectStudents.length">
                                    <td colspan="6" class="gray-color">没有数据，请选择项目</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">导师团队：</label>
                        <div class="col-xs-10">
                            <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>姓名</th>
                                    <th>工号</th>
                                    <th>导师类型</th>
                                    <th>联系电话</th>
                                    <th>E-mail</th>
                                    <th>单位（学院或企业、机构）</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="(item, index) in projectTeachers" :key="item.id">
                                    <td>{{index+1}}</td>
                                    <td>{{item.name}}</td>
                                    <td>{{item.no}}</td>
                                    <td>{{item.teacherType}}</td>
                                    <td>{{item.mobile}}</td>
                                    <td>{{item.email}}</td>
                                    <td>{{item.orgName}}</td>
                                </tr>
                                <tr v-if="!projectTeachers.length">
                                    <td colspan="8" class="gray-color">没有数据，请选择项目</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-xs-2">项目入驻说明：</label>
                        <div class="col-xs-8">
                            <textarea class="form-control"
                                      rows="3"
                                      maxlength="300"
                                      v-model="formApply.projectRemarks" placeholder="输入最大长度为300位字符"></textarea>
                        </div>
                    </div>
                </form>
                <div class="form-horizontal form-enter-apply">
                    <div class="form-group">
                        <label class="control-label col-xs-2">附件：</label>
                        <div class="col-xs-8">
                            <uploader class-name="accessories-h34" :file-list="formApply.projectFiles"
                                      :is-upload-file="false"
                                      url="${ctxFront}/attachment/sysAttachment/ajaxUpload?ftype=30000&fileStep=32000&uid=${pwEnter.id}"></uploader>
                            <uploader class-name="accessories-h34" :file-list="formApply.pfiles" @change-saveing="changeSaveing"
                                      url="${ctxFront}/attachment/sysAttachment/ajaxUpload?ftype=30000&fileStep=32000&uid=${pwEnter.id}"></uploader>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-actions-cyjd text-center">
            <button type="button" class="btn btn-primary" :disabled="saveIng" onclick="location.href='${ctxFront}/pw/pwEnter/form?id=${pwEnter.id}'">上一步</button>
            <c:if test="${!pwEnter.isAudited }">
                <button v-if="!pageData.isSave" type="button" :disabled="saveIng" class="btn btn-primary"
                        @click="save('save')">
                    {{pageData.isSave ? '更新' : '保存'}}
                </button>
                <button v-if="!pageData.isSave" type="button" :disabled="saveIng" class="btn btn-primary"
                        @click="submit('submit')">提交申请
                </button>
                <a v-if="pageData.isSave" class="btn btn-default" href="${ctxFront}/pw/pwEnterRel/list">返回入驻列表</a>
            </c:if>
        </div>
    </div>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>
<script>
    //请求企业接口.
    //用于判断是否使用已录入企业.
    <%--//${ctxFront}/pw/pwCompany/ajaxPwCompany/uid--%>
    //http://localhost:8093/f//pw/pwCompany/ajaxPwCompany/0681b4514bcd476499858cb656c0129d

    +function ($) {
        // 手机号码验证
        jQuery.validator.addMethod("isMobileNumber", function (value, element) {
            var length = value.length;
            var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
            return this.optional(element)
                    || (length == 11 && mobile.test(value));
        }, "请正确填写您的手机号码");

        jQuery.validator.addMethod("positiveNumber", function (value, element) {
            return this.optional(element) || value > 0;
        }, "请填写有效金额");

        var regMtypes = '${pwEnter.ecompany.pwCompany.regMtypes}';
        var pageData = '${fns:toJson(pwEnterVo)}' || '{}';
        pageData = JSON.parse(pageData);
        regMtypes = regMtypes ? JSON.parse(regMtypes) : [];
        regMtypes.forEach(function (t, i) {
            regMtypes[i] = t.toString()
        });

        var enterApplyStepTwo = new Vue({
            el: '#enterApplyStepTwo',
            data: function () {
                return {
                    //                uid: '84bcd363e9b24e25b8aaad7bed3d1dcf',
                    uid: '${pwEnter.applicant.id}',
                    pageData: pageData,
                    tabsActive: {
                        companyShow: 1,
                        projectShow: 0,
                        teamShow: 0
                    },
                    saveIng: false,
                    projectList: [],
                    teamList: [],
                    teamDataStudents: [],
                    teamDataTeachers: [],
                    projectStudents: [],
                    projectTeachers: [],
                    projectInfo: {},
                    projectTeamName: '',
                    cfilesData: {
                        type: 30000,
                        fileStep: 33000,
                        uid: '${pwEnter.id}'
                    },
                    projectProTypes: '${fns:getDictListJson("act_project_type")}',
                    projectTypes: '',
                    projectTypes1: '${fns:getDictListJson("project_style")}',
                    projectTypes7: '${fns:getDictListJson("competition_type")}',
                    projectTypeLabel: '',
                    projectProTypeLabel: '',
                    formApply: {
                        eid: '${pwEnter.id}',
                        hasProject: false,
                        cursel: '${pwEnter.cursel}',
                        hasCompany: true,
                        hasTeam: false,
                        projectRemarks: '${pwEnter.eproject.remarks}',
                        projectPteam: '${pwEnter.eproject.pteam}' || 0,
                        teamRemarks: '${pwEnter.eteam.remarks}',
                        pwCompanyRemarks: '${pwEnter.ecompany.remarks}',
                        projectId: '${pwEnter.eproject.project.id}',
                        teamId: '${pwEnter.eteam.team.id}',
                        isSave: false,
                        tfiles: [],
                        cfiles: [],
                        pfiles: [],
                        projectFiles: [],
                        pwCompany: {
                            id: '${pwEnter.ecompany.pwCompany.id}',
                            no: '${pwEnter.ecompany.pwCompany.no}',
                            name: '${pwEnter.ecompany.pwCompany.name}',
                            phone: '${pwEnter.ecompany.pwCompany.phone}',
                            mobile: '${pwEnter.ecompany.pwCompany.mobile}',
                            address: '${pwEnter.ecompany.pwCompany.address}',
                            remarks: '${pwEnter.ecompany.pwCompany.remarks}',
                            regMoney: '${pwEnter.ecompany.pwCompany.regMoney}',
                            regMtype: '',
                            regMtypes: regMtypes,
                            regPerson: '${pwEnter.ecompany.pwCompany.regPerson}'
                        }
                    },
                    projectForm: '',
                    teamForm: '',
                    companyForm: '',
                    linkTeamId: '',
                    enterTypeForm: '',
                    cateProTypeLabel: '项目类型',
                    cateTypeLabel: '项目类别',
                    fileErrorShow: false,
                    proTypes: '',
                    hashMaps: {
                        '1': 'company',
                        '2': 'project',
                        '3': 'team'
                    }
                }
            },
            computed: {
                projectTypesArr: function () {
                    return JSON.parse(this.projectProTypes);
                },
                projectTypes1Arr: function () {
                    return JSON.parse(this.projectTypes1);
                },
                projectTypes7Arr: function () {
                    return JSON.parse(this.projectTypes7);
                }
            },
            watch: {
                'projectInfo': {
                    deep: true,
                    handler: function (obj) {
                        if (obj.id) {
                            this.projectTeamName = this.projectInfo.team.name;
                        }
                    }
                },
                'formApply.cfiles': function (val) {
                    this.fileErrorShow = !(val.length > 0)
                }
            },
            filters: {
                subs: function (value) {
                    var dot = value.length > 40 ? '...' : ''
                    return value.substring(0, 40) + dot;
                }
            },
            directives: {
                validate: {
                    inserted: function (element, binding, vnode) {
                        vnode.context[binding.value.form] = $(element).validate({
                            rules: {
                                enterType: {
                                    required: true
                                }
                            },
                            messages: {
                                enterType: {
                                    required: '请至少勾选一项入驻类型'
                                }
                            },
                            errorPlacement: function (error, element) {
                                if(element.attr('name') === 'pwEnter' || element.attr('name') === 'enterType'){
                                    error.appendTo(element.parents('.form-group'));
                                }else if (element.is(":checkbox") || element.is(":radio") || element.parent().hasClass('input-group')) {
                                    error.appendTo(element.parent().parent());
                                } else if (element.attr('name') === 'pwRegMType') {
                                    error.appendTo(element.parent().parent());
                                } else {
                                    error.insertAfter(element);
                                }
                            }
                        })
                    }
                }
            },
            methods: {
                tabToggle: function (key) {
                    var tabsActive = this.tabsActive;
                    if (!tabsActive[key]) {
                        for (var tabKey in tabsActive) {
                            if (tabsActive.hasOwnProperty(tabKey)) {
                                if (tabKey === key) {
                                    tabsActive[tabKey] = 1;
                                    if (key === 'companyShow') {
                                        location.hash = 'company'
                                    } else if (key === 'projectShow') {
                                        location.hash = 'project'
                                    } else {
                                        location.hash = 'team'
                                        this.formApply.projectPteam = '0';
                                    }
                                    this[location.hash.replace('#', '') + 'Form'].resetForm();
                                } else {
                                    tabsActive[tabKey] = 0
                                }
                            }
                        }
                    }
                },
                changeSaveing: function (saveB) {
                    this.saveIng = saveB
                },

                //获取项目列表
                getProjectList: function () {
                    var self = this;
                    var xhr = $.get('${ctxFront}/project/projectDeclare/ajaxProByLeader/' + this.uid);
                    xhr.success(function (data) {
                        if (data.status) {
                            self.projectList = data.datas || [];
                            self.changeProject();
                        } else {
                            self.projectList.length = 0;
                        }
                    });
                    xhr.error(function (error) {
                        self.projectList.length = 0;
                        //                    dialogCyjd.createDialog(0, '请求项目列表失败')
                    })
                },


                //获取团队列表
                getTeamList: function () {
                    var self = this;
                    var xhr = $.get('${ctxFront}/project/projectDeclare/ajaxTeamByLeader/' + this.uid);
                    xhr.success(function (data) {
                        if (data.status) {
                            self.teamList = data.datas || [];
                        } else {
                            self.teamList.length = 0;
                        }
                    });
                    xhr.error(function (error) {
                        self.teamList.length = 0;
//                    dialogCyjd.createDialog(0, '请求团队列表失败')
                    })
                },
                //关联团队
                linkTeam: function () {
                    if (this.formApply.projectPteam == '1') {
                        this.getProjectTeamDetail((this.projectInfo.teamId || ''));
                        this.formApply.teamId = this.projectInfo.teamId || '';
                        this.linkTeamId = this.projectInfo.teamId || '';
                        this.formApply.hasTeam = true;
                        this.formApply.hasProject = true;
                    }
                },
                changeLinkTeam: function () {
                    this.formApply.teamId = this.linkTeamId;
                    this.formApply.hasTeam = true;
                    this.changeTeam();
                },
                //项目事件
                changeProject: function () {
                    var projectTeachers = this.projectTeachers;
                    var projectStudents = this.projectStudents;
                    var projectId = this.formApply.projectId;
                    var self = this;
                    if (!projectId) {
                        this.projectInfo = {};
                        projectStudents.length = 0;
                        projectTeachers.length = 0;
                        this.projectTypeLabel = '';
                        this.projectProTypeLabel = '';
                        this.linkTeamId = '';
                        this.formApply.projectFiles = [];
                        return false;
                    }


                    this.projectInfo = this.getProjectDetail(projectId)[0] || {};
                    this.projectTypeLabel = '';
                    this.projectProTypeLabel = '';
                    var proTypes = this.projectInfo.proType.replace(',', '');
                    var arr = proTypes == '7' ? this.projectTypes7Arr : this.projectTypes1Arr;
                    this.cateProTypeLabel = proTypes == '7' ? '大赛类型' : '项目类型';
                    this.cateTypeLabel = proTypes == '7' ? '大赛类别' : '项目类别';
                    this.proTypes = proTypes;
                    this.projectTypesArr.forEach(function (t) {
                        if (proTypes == t.value) {
//                            self.projectTypeLabel = t.label || ''
                            self.projectProTypeLabel = t.label || '';
                        }
                    })
                    arr.forEach(function (t) {
                        if (t.value == self.projectInfo.type) {
                            self.projectTypeLabel = t.label || ''
                        }
                    })
                    if (this.formApply.projectPteam == '1') {
                        this.getProjectTeamDetail(this.projectInfo.teamId);
                        this.formApply.teamId = this.projectInfo.teamId;
                        this.linkTeamId = this.projectInfo.teamId
                    }
                    var attachmentXhr;

                    this.formApply.projectFiles = [];
                    attachmentXhr = $.get('${ctxFront}/pw/pwEnter/moveAttachment?uId=' + this.formApply.projectId);

                    attachmentXhr.success(function (data) {
                        if (data.status) {
                            if (data.datas && data.datas.length) {
                                self.formApply.projectFiles = data.datas;
                                self.formApply.projectFiles.forEach(function (t) {
                                    t.suffix = t.type;
                                })
                            } else {
                                self.formApply.projectFiles = [];

                            }
                        } else {
                            self.formApply.projectFiles = [];
//                            alert(data.msg);
                        }
                    });
                    attachmentXhr.error(function (error) {
//                        alert("请选择项目");
                    });

                },


                getProjectDetail: function (id) {
                    return this.projectList.filter(function (t) {
                                return t.id == id;
                            }) || []
                },

                getProjectTeamDetail: function (id) {
                    var self = this;
                    var studentXhr;
                    var teacherXhr;
                    if (!id) {
                        this.projectStudents.length = 0;
                        this.projectTeachers.length = 0;
                    }
                    if ((this.projectInfo.team && this.projectInfo.team.state != '1' && this.formApply.projectPteam == '1')) {
                        this.projectStudents.length = 0;
                        this.projectTeachers.length = 0;
                        return false;
                    }
                    studentXhr = $.get('${ctxFront}/team/ajaxTeamStudent?teamid=' + id + "&proId=" + this.formApply.projectId);
                    teacherXhr = $.get('${ctxFront}/team/ajaxTeamTeacher?teamid=' + id + "&proId=" + this.formApply.projectId);
                    studentXhr.success(function (data) {
                        if (data.status) {
                            self.projectStudents = data.datas || [];
                        } else {
                            self.projectStudents.length = 0
                        }
                        self.teamDataStudents = self.projectStudents;
                    });
                    studentXhr.error(function (error) {
                        self.projectStudents.length = 0
                        self.teamDataStudents = self.projectStudents;
                    });

                    teacherXhr.success(function (data) {
                        if (data.status) {
                            self.projectTeachers = data.datas || [];
                        } else {
                            self.projectTeachers.length = 0
                        }
                        self.teamDataTeachers = self.projectTeachers;

                    });

                    teacherXhr.error(function (error) {
                        self.projectTeachers.length = 0
                        self.teamDataTeachers = self.projectTeachers;
                    })


                },

                //获取team详情
                changeTeam: function () {
                    var self = this;
                    var studentXhr;
                    var teacherXhr;
                    if (!this.formApply.teamId) {
                        this.teamDataStudents.length = 0;
                        this.teamDataTeachers.length = 0;
                        if (this.formApply.projectPteam == '0') {
                            this.projectStudents.length = 0;
                            this.projectTeachers.length = 0;
                            this.linkTeamId = '';
                        }
                        return false;
                    }
                    if (this.formApply.projectPteam == '0') {
                        this.linkTeamId = this.formApply.teamId;
                    }


                    studentXhr = $.get('${ctxFront}/team/ajaxTeamStudent?teamid=' + this.formApply.teamId);
                    teacherXhr = $.get('${ctxFront}/team/ajaxTeamTeacher?teamid=' + this.formApply.teamId);
                    studentXhr.success(function (data) {
                        if (data.status) {
                            self.teamDataStudents = data.datas || [];
                            if (self.formApply.projectPteam == '0') {
                                self.projectStudents = data.datas || [];
                            }
                        } else {
                            self.teamDataStudents.length = 0;
                            if (self.formApply.projectPteam == '0') {
                                self.projectStudents.length = 0;
                            }
                        }
                    });
                    studentXhr.error(function (error) {
                        self.teamDataStudents.length = 0
                        if (self.formApply.projectPteam == '0') {
                            self.projectStudents.length = 0;
                        }
                    });

                    teacherXhr.success(function (data) {
                        if (data.status) {
                            self.teamDataTeachers = data.datas || [];
                            if (self.formApply.projectPteam == '0') {
                                self.projectTeachers = data.datas || [];
                            }
                        } else {
                            self.teamDataTeachers.length = 0
                            if (self.formApply.projectPteam == '0') {
                                self.projectTeachers.length = 0;
                            }
                        }
                    });

                    teacherXhr.error(function (error) {
                        self.teamDataTeachers.length = 0
                        if (self.formApply.projectPteam == '0') {
                            self.projectTeachers.length = 0;
                        }
                    })

                },

                submit: function (type) {

                    var canSubmit = false;
                    var submitXhr;
                    var self = this;

                    if (!this.enterTypeForm.form()) {
                        dialogCyjd.createDialog(0, '请至少选择一个入驻类型，并提交');
                        this.enterTypeForm.focusInvalid();
                        return
                    }

                    if (this.enterTypeForm.form()) {
                        if (this.formApply.hasCompany) {
                            canSubmit = this.companyForm.form() && this.formApply.cfiles.length > 0;
                            this.fileErrorShow = !(this.formApply.cfiles.length > 0)
                            if (!canSubmit) {
                                dialogCyjd.createDialog(0, '请完善入驻创业企业信息');
                                this.tabToggle('companyShow')
                                this.companyForm.focusInvalid();
                                return
                            }
                        }
                        if (this.formApply.hasTeam) {
                            canSubmit = this.teamForm.form();
                            if (!canSubmit) {
                                dialogCyjd.createDialog(0, '请完善入驻创业团队信息');
                                this.tabToggle('teamShow')
                                this.companyForm.focusInvalid();
                                return
                            }

                        }
                        if (this.formApply.hasProject) {
                            canSubmit = this.projectForm.form();
                            if (!canSubmit) {
                                dialogCyjd.createDialog(0, '请完善入驻创业项目信息');
                                this.tabToggle('projectShow')
                                this.companyForm.focusInvalid();
                                return
                            }
                        }
                    }
                    this.formApply.cursel = (function () {
                        var val = '';
                        for (var key in self.hashMaps) {
                            if (self.hashMaps[key] == location.hash.replace('#', '')) {
                                val = key;
                            }
                        }
                        return val;
                    })()

                    this.saveIng = true;
                    this.formApply.isSave = type === 'submit';
//                    $.extend(true, this.formApply.pfiles, this.projectFiles);
//                    console.log( this.formApply)
                    submitXhr = $.ajax({
                        url: '${ctxFront}/pw/pwEnter/ajaxSaveAll',
                        type: 'POST',
                        data: JSON.stringify(this.formApply),
                        dataType: 'json',
                        contentType: 'application/json'
                    });
                    submitXhr.success(function (data) {
                        if (data.status) {
                            self.formApply.hasCompany && self.companyForm && self.companyForm.resetForm();
                            self.formApply.hasTeam && self.teamForm && self.teamForm.resetForm();
                            self.formApply.hasProject && self.hasProject && self.hasProject.resetForm();
//                            self.formApply.projectFiles = [];
                            if (self.formApply.hasCompany && !self.formApply.pwCompany.id) {
                                self.formApply.pwCompany.id = data.datas.ecompany.pwCompany.id;
                            }
                            //弹出保存成功
//                            dialogCyjd.createDialog(1, (type === 'submit' ? '提交' : '保存') + '成功');
                            if (type !== 'submit') {
                                self.saveIng = false;
                                dialogCyjd.createDialog(1, (type === 'submit' ? '提交' : '保存') + '成功');
                            } else {
                                dialogCyjd.createDialog(1, '提交成功', {
                                    dialogClass: 'dialog-cyjd-container dialog-cy-sub',
                                    buttons: [{
                                        text: '确定',
                                        'class': 'btn btn-primary',
                                        click: function () {
                                            $(this).dialog('close');
                                            location.href = '${ctxFront}/pw/pwEnterRel/list';
                                        }
                                    }]
                                });
//                                location.reload()
                            }
                        } else {
                            dialogCyjd.createDialog(0, data.msg);
                            self.saveIng = false;
                        }

                    });

                    submitXhr.error(function (error) {
                        self.saveIng = false;
                        dialogCyjd.createDialog(0, '请求失败')
                    })
                },

                save: function (type) {
                    var submitXhr;
                    var self = this;

                    if (!this.enterTypeForm.form()) {
                        dialogCyjd.createDialog(0, '请至少选择一个入驻类型，并保存')
                        return
                    }


                    this.formApply.cursel = (function () {
                        var val = '';
                        for (var key in self.hashMaps) {
                            if (self.hashMaps[key] == location.hash.replace('#', '')) {
                                val = key;
                            }
                        }
                        return val;
                    })()
                    this.saveIng = true;
                    this.formApply.isSave = type === 'submit';
                    submitXhr = $.ajax({
                        url: '${ctxFront}/pw/pwEnter/ajaxSaveAll',
                        type: 'POST',
                        data: JSON.stringify(this.formApply),
                        dataType: 'json',
                        contentType: 'application/json'
                    });
                    submitXhr.success(function (data) {
                        if (data.status) {
                            self.formApply.hasCompany && self.companyForm && self.companyForm.resetForm();
                            self.formApply.hasTeam && self.teamForm && self.teamForm.resetForm();
                            self.formApply.hasProject && self.hasProject && self.hasProject.resetForm();
//                            self.formApply.projectFiles = [];
                            if (self.formApply.hasCompany && !self.formApply.pwCompany.id) {
                                self.formApply.pwCompany.id = data.datas.ecompany.pwCompany.id;
                            }
                            self.saveIng = false;
                            self.getFiles('reset');
                            dialogCyjd.createDialog(1, (type === 'submit' ? '提交' : '保存') + '成功');
                        } else {
                            dialogCyjd.createDialog(0, data.msg);
                            self.saveIng = false;
                        }

                    });

                    submitXhr.error(function (error) {
                        self.saveIng = false;
                        dialogCyjd.createDialog(0, '请求失败')
                    })
                },

                changeHash: function () {
                    var hash = location.hash || '#' + this.hashMaps[this.formApply.cursel];
                    switch (hash) {
                        case '#company':
                            this.tabsActive.companyShow = 1;
                            this.tabsActive.projectShow = 0;
                            this.tabsActive.teamShow = 0;
                            break;
                        case '#project':
                            this.tabsActive.companyShow = 0;
                            this.tabsActive.projectShow = 1;
                            this.tabsActive.teamShow = 0;
                            break;
                        case '#team':
                            this.tabsActive.companyShow = 0;
                            this.tabsActive.projectShow = 0;
                            this.tabsActive.teamShow = 1;
                            break;
                    }
                },


                getFiles: function (handler) {
                    var self = this;
                    var xhr = $.get('${ctxFront}/attachment/sysAttachment/ajaxFiles/${pwEnter.id}?type=30000')
                    xhr.success(function (data) {
                        if (data.status && data.datas) {
                            if (handler == 'reset') {
                                self.formApply.cfiles.length = 0;
                                self.formApply.pfiles.length = 0;
                                self.formApply.tfiles.length = 0;
                            }
                            data.datas.forEach(function (t) {
                                if (t.fileStep === 'S_ENTER_COMPANY') {
                                    self.formApply.cfiles.push(t)
                                }
                                if (t.fileStep === 'S_ENTER_PROJECT') {
                                    self.formApply.pfiles.push(t)
                                }
                                if (t.fileStep === 'S_ENTER_TEAM') {
                                    self.formApply.tfiles.push(t)
                                }
                            })

                        }
                    })
                }
            },

            beforeMount: function () {
                this.getProjectList();
                this.getTeamList();

                this.formApply.hasCompany = this.pageData.hasCompany;
                this.formApply.hasProject = this.pageData.hasProject;
                this.formApply.hasTeam = this.pageData.hasTeam;
                this.formApply.projectId = this.pageData.projectId || '';

                this.changeHash();
                this.getFiles()

            },
            mounted: function () {
                this.changeTeam();
            }
        })
    }(jQuery)
</script>
</body>
</html>

