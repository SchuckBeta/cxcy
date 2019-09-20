<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="cyjd-site-default"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/teacherGrace.css?v=1">
    <title>${frontTitle}</title>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#ps").val($("#pageSize").val());
        });
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
    <style>
        .ti-content:after {
            display: none;
        }
        .ti-main{
            position: relative;
        }
        .ti-main .ti-side{
            position: absolute;
            left: 165px;
            top: 0;
            padding: 0;
        }
        .ti-main  .ti-side .name{
            border-right: 0;
        }
        .ti-side .keywords{
            padding-left: 0;
            padding-top: 22px;
        }
    </style>
</head>
<body>

    <%--<div class="banner-sec" style="background-image:url('/img/banner-teacher.jpg');"></div>--%>
<div class="container container-ct">
    <c:if test="${teacherType eq 2}">
        <ol class="breadcrumb" style="margin-top: 0">
            <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
            <li class="active">导师风采</li>
            <li class="active">企业导师</li>
        </ol>
    </c:if>
    <c:if test="${teacherType eq 1}">
        <ol class="breadcrumb" style="margin-top: 0">
            <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
            <li class="active">导师风采</li>
            <li class="active">校园导师</li>
        </ol>
    </c:if>
    <%--<div class="h-title_bar" style="padding-top: 0; margin-bottom: 30px"><div class="h-column-wrap h-title_line"><h3 class="h-title">导师风采</h3> <p class="h-title_en">TOP TEACHERS</p></div> </div>--%>
        <%--<ol class="breadcrumb" style="margin-top: 0">--%>
            <%--<li>--%>
                <%--<a href="${ctxFront}#topTeachers"><i class="icon-home"></i>首页</a>--%>
            <%--</li>--%>
            <%--<li class="active">导师风采</li>--%>
        <%--</ol>--%>
        <%--<div class="title-bar title-bar-ts">--%>
            <%--<span class="triangle"></span>--%>
            <%--<c:if test="${teacherType eq 1}">--%>
                <%--<!-- <h4>企业导师</h4> -->--%>
                <%--<h4>校内导师</h4>--%>
            <%--</c:if>--%>
            <%--<c:if test="${teacherType eq 2}">--%>
                <%--<h4>企业导师</h4>--%>
                <%--&lt;%&ndash;<h4>校内导师</h4>&ndash;%&gt;--%>
            <%--</c:if>--%>
        <%--</div>--%>
        <div class="slider-teacher slider-teacher-company">
            <div class="slider-tc-wrapper">
                <div class="stc-item">
                    <c:choose>
                        <c:when test="${backTeacherExpansionNew!=null && backTeacherExpansionNew!=''}">
                            <div class="row">
                                <div class="col-md-3 col-lg-2 col-md-push-9 col-lg-push-10">
                                    <div class="pic-teacher-block">
                                            <%--<img class="img-responsive" src="${ctxImg}/tg-daiweifen.jpg" alt="戴伟芬">--%>

                                        <c:choose>
                                            <c:when test="${backTeacherExpansionNew.user.photo!=null && backTeacherExpansionNew.user.photo!='' }">
                                                <img class="img-responsive"
                                                     src="${fns:ftpImgUrl(backTeacherExpansionNew.user.photo) }"
                                                     alt="${backTeacherExpansionNew.user.name}">
                                            </c:when>
                                            <c:otherwise>
                                                <img class="img-responsive" src="${ctxImg}/u4110.png"
                                                     alt="${backTeacherExpansionNew.user.name}">
                                            </c:otherwise>
                                        </c:choose>

                                            <%--  <img class="img-responsive" src="${fns:ftpImgUrl(backTeacherExpansionNew.user.photo) }" alt="${backTeacherExpansionNew.user.name}">--%>
                                    </div>
                                    <p class="name-teacher text-center">
                                        <span>${backTeacherExpansionNew.user.name}</span></p>
                                </div>
                                <div class="col-md-9 col-lg-10 col-md-pull-3 col-lg-pull-2">
                                    <ul class="info-list info-pill">
                                        <li class="pill"><span>出生日期：
                                              <fmt:formatDate value="${backTeacherExpansionNew.user.birthday}"
                                                              pattern="yyyy-MM-dd"/>
                                           </span><span>性别：${fns:getDictLabel(backTeacherExpansionNew.user.sex, "sex", "")}
                                           </span></li>
                                            <%--<li>毕业学院：华中师范学院</li>--%>
                                        <li class="pill">学历：
                                                <%--${backTeacherExpansionNew.arrangement}--%>
                                                ${fns:getDictLabel(backTeacherExpansionNew.user.education, "enducation_level", "")}
                                        </li>
                                        <li class="pill">职称：
                                                ${fns:getDictLabel(backTeacherExpansionNew.technicalTitle, "postTitle_type", "")}
                                        </li>
                                        <li class="pill">
                                            学位：${fns:getDictLabel(backTeacherExpansionNew.user.degree, "degree_type", "")}
                                        </li>
                                        <li class="pill">
                                            主要经历：${backTeacherExpansionNew.mainExp}
                                        </li>
                                            <%-- <li class="pill">荣获奖项及证书：<br>
                                                 1、论文《论美国教师教育课程的学术整合取向》获湖北省第七次优秀高等教育研究成果二等奖 <br>
                                                 2、011年10月在“第一届全球教师教育峰会”被授予“田家炳-教师研究青年学者”称号 <br>
                                                 3、新华社特约分析师，曾编写《新标准与评价：美国教师教育课程的改革》，《美国高质量中小学教师的培训》等约10篇美国教师教育前沿报告<br>
                                             </li>--%>
                                    </ul>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <%--        <div class="row">
                                <div class="col-md-3 col-lg-2 col-md-push-9 col-lg-push-10">
                                    <div class="pic-teacher-block">
                                        <img class="img-responsive" src="${ctxImg}/tg-daiweifen.jpg" alt="戴伟芬">
                                    </div>
                                    <p class="name-teacher text-center"><span>戴伟芬</span></p>
                                </div>
                                <div class="col-md-9 col-lg-10 col-md-pull-3 col-lg-pull-2">
                                    <ul class="info-list info-pill">
                                        <li class="pill"><span>年龄：</span><span>性别：女</span></li>
                                        <li>毕业学院：华中师范学院</li>
                                        <li class="pill">学位：博士</li>
                                        <li class="pill">职称：副教授</li>
                                        <li class="pill">学位：博士，硕士</li>
                                        <li class="pill">
                                            主要经历：教育部人文社科2011年度青年基金项目“基于专业取向的美国教师教育课程思想研究”（11YJC880015）
                                            　　全国教育科学“十二五”规划2011年度教育部重点“美国社区学院兼职教师的专业发展及启示——基于“双师型”教师培养的视角”（GJA114021）
                                            　　湖北省教育科学“十二五”规划2011年度专项资助重点“免费师范生在教师教育课程中的学习经验与成效研究”（2011B096）
                                            　　湖北省教育厅人文社科2011年度指导性项目“推进大学生健康发展——闲暇教育探讨”（405）
                                            　　中央高校基本科研业务费专项资金资助“当代美国教师教育课程思想的发展及其启示”（2010DG027）
                                            　　湖北省基础教育研究中心“启航”项目“我国免费师范生课程设置研究”（11QH16 0012）
                                            　　参与哥伦比亚大学教育学院Goodwin教授 《新教师入职培养项目》（获联邦政府资助975万美元）
                                            　　参与教育部[2007]重大研究项目“教育公平研究”子项目“国际教育公平比较研究”
                                        </li>
                                        <li class="pill">荣获奖项及证书：<br>
                                            1、论文《论美国教师教育课程的学术整合取向》获湖北省第七次优秀高等教育研究成果二等奖 <br>
                                            2、011年10月在“第一届全球教师教育峰会”被授予“田家炳-教师研究青年学者”称号 <br>
                                            3、新华社特约分析师，曾编写《新标准与评价：美国教师教育课程的改革》，《美国高质量中小学教师的培训》等约10篇美国教师教育前沿报告<br>
                                        </li>
                                    </ul>
                                </div>
                            </div>--%>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <!-- <div class="title-bar title-bar-ts">
            <span class="triangle"></span>
            <h4>校园导师</h4>
        </div> -->
        <form:form id="searchForm" modelAttribute="backTeacherExpansion" action="${ctxFront}/pageTeacher" method="post"
                   class="breadcrumb form-search clearfix" cssStyle="padding: 0">
            <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
            <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
            <input id="teacherType" name="teacherType" type="hidden" value="${teacherType}"/>
        </form:form>
        <div class="slider-teacher slider-teacher-school">
            <div class="slider-tc-wrapper">
                <div class="stc-item">
                    <c:forEach var="backTeacherExpansion" items="${page.list}" varStatus="idx">
                        <div class="col-md-6" style="display: none">
                            <div class="teacher-intro">
                                <div class="ti-main">
                                    <div class="pic-teacher-box">
                                        <i></i>
                                        <c:choose>
                                            <c:when test="${backTeacherExpansion.user.photo!=null && backTeacherExpansion.user.photo!='' }">
                                                <img class="img-responsive"
                                                     src="${fns:ftpImgUrl(backTeacherExpansion.user.photo) }"
                                                     alt="${backTeacherExpansion.user.name}">
                                            </c:when>
                                            <c:otherwise>
                                                <img class="img-responsive" src="${ctxImg}/u4110.png"
                                                     alt="${backTeacherExpansion.user.name}">
                                            </c:otherwise>
                                        </c:choose>
                                        <%--<img class="img-responsive" src="${ctxImg}/u4110.png"--%>
                                             <%--alt="${backTeacherExpansion.user.name}">--%>
                                    </div>
                                    <div class="ti-side">
                                        <span class="name">${backTeacherExpansion.user.name}</span>
                                        <div class="keywords">
                                                <%-- <p>主要研究方向：${backTeacherExpansion.user.domain}</p>--%>
                                            <p>
                                                <c:forEach var="item" items="${backTeacherExpansion.keywords}">
                                                    <span>${item}</span>
                                                </c:forEach>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="ti-content" >
                                        <p style="height: 56px; overflow : hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-line-clamp: 2;-webkit-box-orient: vertical;">${backTeacherExpansion.mainExp}</p>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
        ${page.courseFooter}
    </div>


<script>
    $(function () {
        var $stcItemColMd6 = $('.slider-teacher-school .stc-item .col-md-6')
        $stcItemColMd6.each(function (i, item) {
            $(item).show();
            $stcItemColMd6.slice(i*2, i*2+2).wrapAll('<div class="row"/>')
        });


    })
</script>
</body>
</html>
