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
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home" style="font-size: 12px;"></i>首页</a>
        </el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/html-sctzsj">双创大赛</a></el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/gcontest/gContest">我的大赛</a></el-breadcrumb-item>
        <el-breadcrumb-item>大赛详情</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="panel panel-g_contest mgb-20">
        <h5 class="panel-header panel-header-title">${gContest.pName}</h5>
        <div class="panel-body">
            <div class="g_contest-pic">
                <img :src="gContestVo.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proGConPicFilter"alt="">
            </div>
            <div class="g_contest-info_list">
                <c:if test="${not empty competitionNumber}">
                    <e-col-item label="大赛编号：" align="right"
                                style="margin-bottom: 10px;">${competitionNumber}</e-col-item>
                </c:if>
                <e-col-item label="大赛类别：" align="right"
                            style="margin-bottom: 10px;">${fns:getDictLabel(gContest.type, "competition_net_type", "")}</e-col-item>
                <e-col-item label="所属学院：" align="right" style="margin-bottom: 10px;">${sse.office.name}</e-col-item>
                <e-col-item label="申报日期：" align="right" style="margin-bottom: 10px;"><fmt:formatDate
                        value="${gContest.createDate}" pattern="yyyy-MM-dd"/></e-col-item>
            </div>
            <ul class="g_contest-category-tab">
                <li :class="{'active': tabIndex == '0'}"><a href="javascript:void(0);"
                                                                @click.stop.prevent="tabIndex = '0'">申报人信息</a></li>
                <li :class="{'active': tabIndex == '1'}"><a href="javascript:void(0);"
                                                                @click.stop.prevent="tabIndex = '1'">项目信息</a></li>
                <li :class="{'active': tabIndex == '2'}"><a href="javascript:void(0);"
                                                                @click.stop.prevent="tabIndex = '2'">团队成员</a></li>
                <li :class="{'active': tabIndex == '3'}"><a href="javascript:void(0);"
                                                                @click.stop.prevent="tabIndex = '3'">审核记录</a></li>
            </ul>
        </div>
    </div>

    <div v-show="tabIndex == '0'" class="tab-content">
        <div class="panel panel-g_contest">
            <div class="panel-header">
                申报人信息
            </div>
            <div class="panel-body panel-g_contest-detail-info">
                <el-row :gutter="20" label-width="100px">
                    <el-col :span="8">
                        <e-col-item label="申报人：" align="right">{{leader.name}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="学院：" align="right">${sse.office.name}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="学号/毕业年份：" align="right">${sse.no}
                            <c:if test="${studentExpansion.graduation!=null && studentExpansion.graduation!=''}">
                                （<fmt:formatDate value='${studentExpansion.graduation}' pattern='yyyy'/>）年
                            </c:if>
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="专业年级："
                                    align="right">${fns:getProfessional(sse.professional)}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="联系电话：" align="right">{{leader.mobile}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="E-mail：" align="right">{{leader.email}}</e-col-item>
                    </el-col>
                </el-row>
            </div>
        </div>

    </div>

    <div v-show="tabIndex == '1'" class="tab-content">
        <div class="panel panel-g_contest">
            <div class="panel-header">大赛基本信息</div>
            <div class="panel-body panel-g_contest-detail-info">
                <el-row :gutter="20" label-width="98px">
                    <c:if test="${not empty relationProject}">
                        <el-col :span="24">
                            <e-col-item label="关联项目：" align="right">${relationProject}</e-col-item>
                        </el-col>
                    </c:if>
                    <el-col :span="8">
                        <e-col-item label="参赛项目名称：" align="right">${gContest.pName}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="大赛类别："
                                    align="right">${fns:getDictLabel(gContest.type, "competition_net_type", "")}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item label="参赛组别："
                                    align="right">${fns:getDictLabel(gContest.level, "gcontest_level", "")}</e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="融资情况："
                                    align="right">${fns:getDictLabel(gContest.financingStat, "financing_stat", "")}</e-col-item>
                    </el-col>
                </el-row>
            </div>
        </div>
        <div class="panel pane-g_contest">
            <div class="panel-header">项目介绍</div>
            <div class="panel-body panel-g_contest-detail-info">
                <e-col-item label="项目介绍：" label-width="98px" align="right" class="white-space-pre-static word-break">${gContest.introduction}</e-col-item>
            </div>
        </div>

        <div class="panel pane-g_contest" v-show="sysAttachments != null && sysAttachments != ''">
            <div class="panel-header">项目材料</div>
            <div class="panel-body panel-g_contest-detail-info">
                <%--<sys:frontFileUploadNew fileitems="${sysAttachments}" className="accessories-h34" readonly="true"></sys:frontFileUploadNew>--%>

                <ul class="timeline">
                    <li class="work" v-for="file in sysAttachments">
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

    <div v-show="tabIndex == '2'" class="tab-content tab-p">
        <div class="panel panel-g_contest">
            <div class="panel-header">团队成员</div>
            <div class="panel-body panel-g_contest-detail-info">
                <el-row :gutter="20" class="team-list" label-width="76px" v-show="dutyFirstTeamStu.length > 0">
                    <el-col v-for="student in dutyFirstTeamStu" :key="student.id" :span="12">
                        <div class="user-pic">
                            <img :src="student.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt=""
                                 align="left" hspace="5" vspace="5">
                        </div>
                        <div class="user-detail">
                            <e-col-item label="姓名：" align="right">{{student.name}}
                                <el-tag v-show="leader.name == student.name" type="info" size="mini"> 项目负责人
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
            </div>
        </div>
        <div class="panel panel-g_contest">
            <div class="panel-header">指导导师</div>
            <div class="panel-body">
                <el-row :gutter="20" class="team-list team-list-teacher" label-width="76px"
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

    <div v-show="tabIndex == '3'" class="tab-content">
        <div class="panel panel-g_contest">
            <div class="panel-header">审核记录</div>
            <div class="panel-body panel-g_contest-detail-info">
                <table class="table table-bordered table-default table-hover">
                    <thead>
                    <tr>
                        <th>赛制</th>
                        <th>审核时间</th>
                        <th>评审人</th>
                        <th>建议及意见</th>
                        <th>评审内容</th>
                        <th>得分</th>
                        <th>当前排名</th>
                        <th>荣获奖项</th>
                        <th>荣获奖金</th>
                    </tr>
                    </thead>
                    <tbody>
                    <!-- 	学院 -->
                    <c:choose>
                        <c:when test="${collegeinfos[0]!=null && collegeinfos[0] != ''}">
                            <tr>
                                <td rowspan="3" class="ros_thr">校赛(学院及学院)</td>
                                <td>
                                    <fmt:formatDate value="${collegeinfos[0].createDate}"
                                                    pattern="yyyy-MM-dd hh:mm:ss"/>
                                </td>
                                <td>学院专家及教学秘书</td>
                                <td>${collegeinfos[0].suggest}</td>
                                <td>${collegeinfos[0].auditName}</td>
                                <td>${collegeinfos[0].score}</td>
                                <td>${collegeinfos[0].sort}</td>
                                <c:choose>
                                    <c:when test="${gca!=null}">
                                        <td rowspan="3">
                                                ${fns:getDictLabel(gca.award, "competition_college_prise", "")}
                                        </td>
                                        <td rowspan="3">${gca.money}</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td rowspan="3"></td>
                                        <td rowspan="3"></td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td rowspan="3">校赛(学院及学院)</td>
                                <td><span style="visibility: hidden">1</span></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td rowspan="3"></td>
                                <td rowspan="3"></td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <!-- 	学院 -->
                    <c:choose>
                        <c:when test="${wpinfos[0]!= null && wpinfos[0]!= ''}">
                            <tr>
                                <td>
                                    <fmt:formatDate value="${wpinfos[0].createDate}"
                                                    pattern="yyyy-MM-dd hh:mm:ss"/>
                                </td>
                                <td>校级专家及管理员</td>
                                <td>${wpinfos[0].suggest}</td>
                                <td>${wpinfos[0].auditName}</td>
                                <td>${wpinfos[0].score}</td>
                                <td>${wpinfos[0].sort}</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td><span style="visibility: hidden">1</span></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <!-- 	end -->
                    <c:choose>
                        <c:when test="${lyinfos[0]!=null && lyinfos[0]!=''}">
                            <tr>
                                <td>
                                    <fmt:formatDate value="${lyinfos[0].createDate}"
                                                    pattern="yyyy-MM-dd hh:mm:ss"/>
                                </td>
                                <td>校级管理员</td>
                                <td>${lyinfos[0].suggest}</td>
                                <td>${lyinfos[0].auditName}</td>
                                <td>${lyinfos[0].score}</td>
                                <td>${lyinfos[0].sort}</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td><span style="visibility: hidden">1</span></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
    +function (Vue) {


        var doubleProject = new Vue({
            el: '#doubleProject',
            data: function () {
                var checkMenuByNum = '${fns:checkMenuByNum(5)}';

                var gContestVo = ${fns:toJson(gContestVo)};
//                gContestVo = gContestVo.replace(/\n/g, '\\\\n').replace(/\r/g, '\\\\r');
//                gContestVo = JSON.parse(gContestVo);

                var sysAttachments = JSON.parse('${fns:toJson(sysAttachments)}');
                return {
                    leader: JSON.parse('${fns:toJson(sse)}'),
                    teamStudent: JSON.parse('${fns:toJson(teamStudents)}') || [],
                    dutyFirstTeamStu:[],
                    teamTeacher: JSON.parse('${fns:toJson(teamTeachers)}') || [],
                    gContestVo:gContestVo,
                    checkMenuByNum: checkMenuByNum,
                    sysAttachments: sysAttachments,
                    tabIndex: 0
                }
            },
            computed: {},
            methods: {
                getDutyFirst:function () {
                    for(var i = 0; i < this.teamStudent.length; i++){
                        if(this.leader.name == this.teamStudent[i].name){
                            this.dutyFirstTeamStu.push(this.teamStudent[i]);
                        }
                    }
                    for(var j = 0; j < this.teamStudent.length; j++){
                        if(this.leader.name != this.teamStudent[j].name){
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