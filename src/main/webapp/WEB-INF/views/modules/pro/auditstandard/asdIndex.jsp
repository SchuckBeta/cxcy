<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${asdVo.name}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>
<div id="app" class="container-fluid" style="padding-top: 20px">
    <div class="container-fluid container-big-match">
        <div class="wrap">
            <div class="circles-bg"></div>
            <div class="projects">
                <c:if test="${asdVo.type=='1'}">
                    <div class="project-intro project-declaration">
                        <div class="project-pic project-pic-declaration"></div>
                        <div class="circle circle-pink">
                            <div class="circle-inner">
                                <p class="circle-label">申报学生</p>
                                <p class="number">${asdVo.applyNum}人</p>
                            </div>
                        </div>
                    </div>

                    <div class="project-intro project-creative">
                        <div class="project-pic project-pic-creative"></div>
                        <div class="circle circle-purple">
                            <div class="circle-inner">
                                <p class="circle-label">创新训练项目</p>
                                <p class="number">${asdVo.innovateNum}个</p>
                            </div>
                        </div>
                    </div>
                    <div class="project-intro project-creating" style="top: 15px;left: 240px;">
                        <div class="project-pic project-pic-creating"></div>
                        <div class="circle circle-blue">
                            <div class="circle-inner">
                                <p class="circle-label">创业训练项目</p>
                                <p class="number">${asdVo.innovateBusNum}个</p>
                            </div>
                        </div>
                    </div>
                    <div class="project-intro project-creating">
                        <div class="project-pic project-pic-creating"></div>
                        <div class="circle circle-blue">
                            <div class="circle-inner">
                                <p class="circle-label">创业项目</p>
                                <p class="number">${asdVo.businessNum}个</p>
                            </div>
                        </div>
                    </div>
                    <div class="project-intro project-teacher">
                        <div class="project-pic project-pic-teacher"></div>
                        <div class="circle circle-yellow">
                            <div class="circle-inner">
                                <p class="circle-label">项目导师</p>
                                <p class="number">${asdVo.teacherNum}人</p>
                            </div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${asdVo.type=='2'}">
                    <div class="project-intro project-declaration">
                        <div class="project-pic project-pic-declaration"></div>
                        <div class="circle circle-pink">
                            <div class="circle-inner">
                                <p class="circle-label">参赛总人数</p>
                                <p class="number">${asdVo.innovateNum}人</p>
                            </div>
                        </div>
                    </div>
                    <div class="project-intro project-creative">
                        <div class="project-pic project-pic-creative"></div>
                        <div class="circle circle-purple">
                            <div class="circle-inner">
                                <p class="circle-label">参赛项目数</p>
                                <p class="number">${asdVo.applyNum}个</p>
                            </div>
                        </div>
                    </div>
                    <div class="project-intro project-teacher">
                        <div class="project-pic project-pic-teacher"></div>
                        <div class="circle circle-yellow">
                            <div class="circle-inner">
                                <p class="circle-label">指导老师数</p>
                                <p class="number">${asdVo.teacherNum}人</p>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>