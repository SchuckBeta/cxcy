<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="creative"/>
</head>
<body>


<div id="doubleProject" v-show="pageLoad" style="display: none" class="page-container">
    <el-breadcrumb separator-class="el-icon-arrow-right" class="mgb-20">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a>
        </el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/page-innovation">双创项目</a></el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/project/projectDeclare/curProject">我的项目</a></el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/project/projectDeclare/list">项目列表</a></el-breadcrumb-item>
        <el-breadcrumb-item>项目详情</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="panel panel-project mgb-20">
        <div class="panel-header panel-header-title">
            <span>${projectDeclare.name}</span>
        </div>
        <div class="panel-body">
            <div class="project-pic">
                <img :src="projectDeclare.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proGConPicFilter"  alt="" align="left" hspace="5" vspace="5">
            </div>
            <div class="project-info_list">
                <e-col-item label="项目编号：" align="right"
                            style="margin-bottom: 10px;">${projectDeclare.number}</e-col-item>
                <e-col-item label="项目类别：" align="right" style="margin-bottom: 10px;"><c:forEach
                        var="proType" items="${project_type}">
                    <c:if test="${proType.value eq projectDeclare.type }">${proType.label}</c:if>
                </c:forEach>
                    <c:forEach var="gcType" items="${competition_type}">
                        <c:if test="${gcType.value eq projectDeclare.type }">${gcType.label}</c:if>
                    </c:forEach></e-col-item>
                <e-col-item label="所属学院：" align="right"
                            style="margin-bottom: 10px;"> ${leader.office.name}</e-col-item>
                <e-col-item label="申报日期：" align="right"
                            style="margin-bottom: 10px;"> ${sysdate}</e-col-item>
            </div>
            <ul class="project-category-tab">
                <li :class="{active: tabIndex == '0'}"><a href="javascript: void(0);"
                                                          @click.stop.prevent="tabIndex = '0'">负责人信息</a></li>
                <li :class="{active: tabIndex == '1'}"><a href="javascript: void(0);"
                                                          @click.stop.prevent="tabIndex = '1'">项目信息</a></li>
                <li :class="{active: tabIndex == '2'}"><a href="javascript: void(0);"
                                                          @click.stop.prevent="tabIndex = '2'">团队成员</a></li>
                <c:if test="${projectDeclare.status!=null&&projectDeclare.status!='0'&&(not empty projectDeclareVo.auditInfo.level_d||not empty projectDeclareVo.auditInfo.mid_d||not empty projectDeclareVo.auditInfo.final_d)}">
                    <li :class="{active: tabIndex == '3'}"><a href="javascript: void(0);"
                                                              @click.stop.prevent="tabIndex = '3'">审核记录</a></li>
                </c:if>
            </ul>
        </div>
    </div>
    <div class="project-info-wrapper">
        <div v-show="tabIndex == '0'" class="tab-content">
            <div class="panel panel-project">
                <div class="panel-header">负责人信息</div>
                <div class="panel-body panel-project-detail-info">
                    <el-row :gutter="20" label-width="84px">
                        <el-col :span="8">
                            <e-col-item label="项目负责人：" align="right">{{leader.name}}</e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="学院：" align="right">${leader.office.name}</e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="学号：" align="right">{{leader.no}}</e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="专业年级："
                                        align="right">${fns:getProfessional(leader.professional)}</e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="联系电话：" align="right">${leader.mobile}</e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="E-mail：" align="right">${leader.email}</e-col-item>
                        </el-col>
                    </el-row>
                </div>
            </div>
        </div>
        <div v-show="tabIndex == '1'" class="tab-content">
            <div class="panel panel-project">
                <div class="panel-header">项目基本信息</div>
                <div class="panel-body panel-project-detail-info">
                    <el-row :gutter="20" label-width="110px">
                        <el-col :span="24">
                            <e-col-item label="项目名称：" align="right">${projectDeclare.name}</e-col-item>
                        </el-col>
                        <el-col :span="6">
                            <e-col-item label="项目类别：" align="right">{{projectType
                                |selectedFilter(projectTypesEntries)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="8">
                            <e-col-item label="项目来源：" align="right">{{projectSource
                                |selectedFilter(projectSourcesEntries)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="10">
                            <e-col-item label="项目拓展及传承：" align="right">{{projectExtend
                                |selectedFilter(projectExtendsEntries)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="项目介绍：" align="right" class="white-space-pre-static word-break">${projectDeclare.introduction}</e-col-item>
                        </el-col>

                        <el-col :span="24">
                            <e-col-item label="前期调研准备：" align="right"
                            >${projectDeclare.innovation}</e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="项目预案：" align="right">
                                <table class="table table-bordered table-default table-hover">
                                    <thead>
                                    <tr>
                                        <th>实施预案</th>
                                        <th width="160">时间安排</th>
                                        <th>保障措施</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td data-line-feed="textarea"
                                            style="text-align: left;">${projectDeclare.planContent}</td>
                                        <td style="vertical-align: middle"><fmt:formatDate
                                                value="${projectDeclare.planStartDate }"
                                                pattern="yyyy-MM-dd"/>至<fmt:formatDate
                                                value="${projectDeclare.planEndDate }"
                                                pattern="yyyy-MM-dd"/>
                                        </td>
                                        <td data-line-feed="textarea"
                                            style="text-align: left;">${projectDeclare.planStep}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </e-col-item>
                        </el-col>

                        <el-col :span="24">
                            <e-col-item label="任务分工：" align="right">
                                <table class="table table-bordered table-default table-hover">
                                    <thead>
                                    <tr>
                                        <th width="48">序号</th>
                                        <th>工作任务</th>
                                        <th>任务描述</th>
                                        <th>时间安排</th>
                                        <th>成本（元）</th>
                                        <th>质量评价</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:if
                                            test="${projectDeclareVo.plans!=null&&projectDeclareVo.plans.size() >0}">
                                        <c:forEach items="${projectDeclareVo.plans}" var="item"
                                                   varStatus="status">
                                            <tr>
                                                <td>${status.index+1}</td>
                                                <td data-line-feed="textarea"
                                                    style="text-align: left;">${projectDeclareVo.plans[status.index].content }</td>
                                                <td data-line-feed="textarea"
                                                    style="text-align: left;">${projectDeclareVo.plans[status.index].description }</td>
                                                <td style="vertical-align: middle">
                                                    <div class="time-input-inline">
                                                        <fmt:formatDate
                                                                value="${projectDeclareVo.plans[status.index].startDate }"
                                                                pattern="yyyy-MM-dd"/>
                                                        至
                                                        <fmt:formatDate
                                                                value="${projectDeclareVo.plans[status.index].endDate }"
                                                                pattern="yyyy-MM-dd"/>
                                                    </div>
                                                </td>
                                                <td style="vertical-align: middle">${projectDeclareVo.plans[status.index].cost }</td>
                                                <td data-line-feed="textarea"
                                                    style="text-align: left;">${projectDeclareVo.plans[status.index].quality }</td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                    </tbody>
                                </table>
                            </e-col-item>
                        </el-col>

                    </el-row>
                </div>
            </div>

            <div class="panel panel-project">
                <p class="panel-header">预期成果 </p>
                <div class="panel-body panel-project-detail-info">
                    <e-col-item label="成果形式：" align="right" label-width="96px">
                        {{projectResultType |
                        checkboxFilter(projectResultTypesEntries)}}
                    </e-col-item>
                    <e-col-item label="成果说明：" align="right" label-width="96px">
                        ${projectDeclare.resultContent}
                    </e-col-item>
                </div>
            </div>


            <c:if test="${projectDeclare.budget!= null && projectDeclare.budget != '' && projectDeclare.budget != undefined}">
                <div class="panel panel-project">
                    <div class="panel-header">经费预算</div>
                    <div class="panel-body panel-project-detail-info">
                            ${projectDeclare.budget}
                    </div>
                </div>
            </c:if>

            <div class="panel panel-project"
                 v-show="projectDeclareVoFileInfo != null && projectDeclareVoFileInfo != '' && projectDeclareVoFileInfo != undefined">
                <div class="panel-header">项目材料<a class="pull-right" href="${ctxFront}/project/projectDeclare/curProject">提交材料</a>
                </div>
                <div class="panel-body panel-project-detail-info">
                    <ul class="timeline">
                        <li class="work" v-for="file in projectDeclareVoFileInfo">
                            <span class="contest-date">{{file.createDate | formatDateFilter('YYYY-MM-DD HH:mm')}}</span>
                            <img src="${ctxImages}/time-line.png" alt="">
                            <div class="relative">
                                <e-file-item :file="file" size="mini" :show="false"></e-file-item>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>

        </div>
        <div v-show="tabIndex == '2'" class="tab-content">
            <div class="panel panel-project">
                <div class="panel-header">团队成员</div>
                <div class="panel-body panel-project-detail-info">
                    <el-row :gutter="20" class="team-list" label-width="76px" v-show="dutyFirstTeamStu.length > 0">
                        <el-col v-for="student in dutyFirstTeamStu" :key="student.id" :span="12">
                            <div class="user-pic">
                                <img :src="student.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt=""
                                     align="left" hspace="5" vspace="5">
                            </div>
                            <div class="user-detail">
                                <e-col-item label="姓名：" align="right">{{student.name}}
                                    <el-tag v-show="leader.id == student.userId" type="info" size="mini"> 项目负责人
                                    </el-tag>
                                </e-col-item>
                                <e-col-item label="学号：" align="right">{{student.no}}
                                    <el-tag v-show="student.instudy" type="info" size="mini"> {{student.instudy}}
                                    </el-tag>
                                </e-col-item>
                                <e-col-item label="学院/专业：" align="right">
                                    {{student.org_name}}/{{student.professional}} <span
                                        v-show="checkMenuByNum && student.weightVal">学分配比：{{student.weightVal}}</span>
                                </e-col-item>
                                <e-col-item label="联系方式：" align="right">{{student.mobile}}
                                </e-col-item>
                                <e-col-item label="技术领域：" align="right">{{student.domain}}
                                </e-col-item>
                            </div>
                        </el-col>
                    </el-row>
                    <div v-show="!dutyFirstTeamStu || dutyFirstTeamStu.length == 0" class="empty-color text-center">
                        目前没有学生加入到团队中，快去邀请吧...
                    </div>

                    <%--<ul v-if="teamStudent" class="tab-team-info">--%>
                    <%--<li >--%>

                    <%--<p class="name"><span>{{student.name}}<span v-show="leader.id == student.userId">（项目负责人）</span></span></p>--%>
                    <%--<p>学号：{{student.no}} &nbsp;&nbsp;&nbsp;&nbsp;{{student.instudy}}</p>--%>
                    <%--<p>{{student.org_name}}/{{student.professional}} &nbsp;&nbsp;&nbsp;&nbsp;--%>
                    <%--<span v-show="checkMenuByNum && student.weightVal">学分配比：{{student.weightVal}}</span>--%>
                    <%--</p>--%>
                    <%--<p>联系方式：{{student.mobile}}</p>--%>
                    <%--<p>技术领域：{{student.domain}}</p>--%>

                    <%--</li>--%>
                    <%--</ul>--%>
                </div>


            </div>
            <div class="panel panel-project">
                <div class="panel-header">指导老师</div>
                <div class="panel-body panel-project-detail-info">
                    <el-row :gutter="20" class="team-list team-list_teacher" label-width="76px"
                            v-show="teamTeacher.length > 0">
                        <el-col v-for="teacher in teamTeacher" :key="teacher.id" :span="12">
                            <div class="user-pic">
                                <img :src="teacher.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt=""
                                     align="left" hspace="5" vspace="5">
                            </div>
                            <div class="user-detail">
                                <e-col-item label="姓名：" align="right">{{teacher.name}}
                                    <el-tag v-show="teacher.teacherType" type="info" size="mini">
                                        {{teacher.teacherType}}
                                    </el-tag>
                                </e-col-item>
                                <e-col-item label="工号：" align="right">{{teacher.no}}</e-col-item>
                                <e-col-item label="学院/专业：" align="right">
                                    {{teacher.org_name}}
                                </e-col-item>
                                <e-col-item label="联系方式：" align="right">{{teacher.mobile}}</e-col-item>
                                <e-col-item label="技术领域：" align="right">{{teacher.domain}}</e-col-item>
                            </div>
                        </el-col>
                    </el-row>
                    <div v-show="!teamTeacher || teamTeacher.length == 0" class="empty-color text-center">
                        暂无导师
                    </div>
                </div>
            </div>
        </div>
        <c:if test="${projectDeclare.status !=null && projectDeclare.status !='0' && (not empty projectDeclareVo.auditInfo.level_d || not empty projectDeclareVo.auditInfo.mid_d || not empty projectDeclareVo.auditInfo.final_d)}">
            <div v-show="tabIndex == '3'" class="tab-content">
                <div class="panel panel-project">
                    <div class="panel-header">审核记录</div>
                    <div class="panel-body panel-project-detail-info">
                        <table class="table table-bordered table-default table-hover">
                            <thead>
                            <tr>
                                <th colspan="3">立项审核</th>
                            </tr>
                            <tr>
                                <th>评级结果</th>
                                <th>建议及意见</th>
                                <th>评审时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${not empty projectDeclareVo.auditInfo.level_d}">
                                <tr>
                                    <td>${projectDeclareVo.auditInfo.level }</td>
                                    <td>${projectDeclareVo.auditInfo.level_s }</td>
                                    <td>${projectDeclareVo.auditInfo.level_d }</td>
                                </tr>
                            </c:if>
                            <c:if test="${empty projectDeclareVo.auditInfo.level_d}">
                                <tr>
                                    <td colspan="3">暂无数据</td>
                                </tr>
                            </c:if>
                            </tbody>
                            <thead>
                            <tr>
                                <th class="empty" colspan="3">中期审核</th>
                            </tr>
                            <tr>
                                <th>评级结果</th>
                                <th>建议及意见</th>
                                <th>评审时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${not empty projectDeclareVo.auditInfo.mid_d}">
                                <tr>
                                    <td>${projectDeclareVo.auditInfo.mid_result }</td>
                                    <td>${projectDeclareVo.auditInfo.mid_s }</td>
                                    <td>${projectDeclareVo.auditInfo.mid_d }</td>
                                </tr>
                            </c:if>
                            <c:if test="${empty projectDeclareVo.auditInfo.mid_d}">
                                <tr>
                                    <td class="empty" colspan="3">暂无数据</td>
                                </tr>
                            </c:if>
                            </tbody>
                            <thead>
                            <tr>
                                <th colspan="3">结项审核</th>
                            </tr>
                            <tr>
                                <th>评级结果</th>
                                <th>建议及意见</th>
                                <th>评审时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${not empty projectDeclareVo.auditInfo.final_d}">
                                <tr>
                                    <td>${projectDeclareVo.auditInfo.final_result }</td>
                                    <td>${projectDeclareVo.auditInfo.final_s }</td>
                                    <td>${projectDeclareVo.auditInfo.final_d }</td>
                                </tr>
                            </c:if>
                            <c:if test="${empty projectDeclareVo.auditInfo.final_d}">
                                <tr>
                                    <td class="empty" colspan="3">暂无数据</td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</div>
</div>


<script>
    +function (Vue) {
        'use strict';

        var doubleProject = new Vue({
            el: '#doubleProject',
            data: function () {
                var checkMenuByNum = '${fns:checkMenuByNum(5)}';

                var projectDeclare = ${fns:toJson(projectDeclare)};
//                projectDeclare = projectDeclare.replace(/\n/g, '\\\\n').replace(/\r/g, '\\\\r');
//                projectDeclare = JSON.parse(projectDeclare);

                <%--var projectDeclareVo = JSON.parse('${fns:toJson(projectDeclareVo)}');--%>
                var projectTypes = JSON.parse('${fns:toJson(project_type)}');
                var projectExtends = JSON.parse('${fns:toJson(project_extend)}');
                var projectResultTypes = JSON.parse('${fns:toJson(resultTypeList)}');
                var projectSources = JSON.parse('${fns:toJson(project_source)}');
                var projectDeclareVoFileInfo = JSON.parse('${fns:toJson(projectDeclareVo.fileInfo)}') || [];
                var leader = JSON.parse('${fns:toJson(leader)}')
                return {
                    leader: leader,
                    teamStudent: JSON.parse('${fns:toJson(teamStudents)}'),
                    dutyFirstTeamStu:[],
                    teamTeacher: JSON.parse('${fns:toJson(teamTeachers)}'),
                    //teamStudent: projectDeclareVo.teamStudent,
                    //teamTeacher: projectDeclareVo.teamTeacher,
                    projectDeclare: projectDeclare,
                    checkMenuByNum: checkMenuByNum,
                    projectTypes: projectTypes,
                    projectExtends: projectExtends,
                    projectSources: projectSources,
                    projectResultTypes: projectResultTypes,
                    projectType: '${projectDeclare.type}',
                    projectSource: '${projectDeclare.source}',
                    projectExtend: '${projectDeclare.development}',
                    projectResultType: '${projectDeclare.resultType}',
                    projectDeclareVoFileInfo: projectDeclareVoFileInfo,
                    tabIndex: 0
                }
            },
            computed: {
                projectTypesEntries: {
                    get: function () {
                        return this.getEntries(this.projectTypes)
                    }
                },
                projectExtendsEntries: {
                    get: function () {
                        return this.getEntries(this.projectExtends)
                    }
                },
                projectResultTypesEntries: {
                    get: function () {
                        return this.getEntries(this.projectResultTypes)
                    }
                },
                projectSourcesEntries: {
                    get: function () {
                        return this.getEntries(this.projectSources)
                    }
                }
            },
            methods: {
                getDutyFirst:function () {
                    for(var i = 0; i < this.teamStudent.length; i++){
                        if(this.leader.id == this.teamStudent[i].userId){
                            this.dutyFirstTeamStu.push(this.teamStudent[i]);
                        }
                    }
                    for(var j = 0; j < this.teamStudent.length; j++){
                        if(this.leader.id != this.teamStudent[j].userId){
                            this.dutyFirstTeamStu.push(this.teamStudent[j]);
                        }
                    }
                }
            },
            created: function () {
                this.getDutyFirst();
            }
        })
    }(Vue)
</script>


</body>
</html>
