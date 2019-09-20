<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="cyjd-site-default"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/innovation.css"/>
    <title>${frontTitle}</title>
</head>
<body>
<div class="container container-ct">
    <%--<div class="edit-bar clearfix" style="margin-top:0;">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<div class="mybreadcrumbs" style="margin:0 0 20px 9px;">--%>
                <%--<i class="icon-home" aria-hidden="true"></i>&nbsp;&nbsp;<a href="/f" style="color:#333;text-decoration: underline;">首页</a>&nbsp;&gt;&nbsp;双创项目--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
    <ol class="breadcrumb" style="margin-top: 0">
        <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
        <li class="active">双创项目</li>
    </ol>

    <div class="introduceBox">
        <span class="title"></span>
        <h6>国家级大学生创新创业训练计划内容包括创新训练项目、创业训练项目和创业实践项目三类</h6>
        <ul>
            <li>
                <img src="${ctxImg}/innovation1.png" alt=""/>
                <h5>创新训练项目</h5>
                <p>创新训练项目是本科生个人或团队，在导师指导下，自主完成创新性研究项目设计、研究条件准备和项目实施、研究报告撰写、成果（学术）交流等工作。</p>
            </li>
            <li>
                <img src="${ctxImg}/innovation2.png" alt=""/>
                <h5>创业训练项目</h5>
                <p>创业训练项目是本科生团队，在导师指导下，团队中每个学生在项目实施过程中扮演一个或多个具体的角色，通过编制商业计划书、开展可行性研究、模拟企业运行、参加企业实践、撰写创业报告等工作。</p>
            </li>
            <li>
                <img src="${ctxImg}/innovation3.png" alt=""/>
                <h5>创业实践项目</h5>
                <p>创业实践项目是学生团队，在学院导师和企业导师共同指导下，采用前期创新训练项目（或创新性实验）的成果，提出一项具有市场前景的创新性产品或者服务，以此为基础开展创业实践活动。</p>
            </li>
        </ul>
    </div>
        <div style="margin: -150px 0 20px;">
            <img src="${ctxImg}/innovation11 (1).png" style="display: block;width: auto;max-width: 100%;margin: 0 auto"/>
        </div>
    <div class="aimBox">
        <span class="title"></span>
        <div class="content">
            通过实施国家级大学生创新创业训练计划，促进高等学院转变教育思想观念，改革人才培养模式，强化创新创业能力训练，增强高校学生的创新能力和在创新基础上的创业能力，培养适应创新型国家建设需要的高水平创新人才。
        </div>
    </div>
    <div class="policyBox">
        <span class="title"></span>
        <ul>
            <li>国家级大学生创新创业训练计划由中央财政、地方财政共同支持，参与高校按照不低于1:1的比例，自筹经费配套</li>
            <li>地方所属高校参加国家级大学生创新创业训练计划，由地方财政参照中央财政经费支持标准予以支持</li>
            <li>中央部委所属高校参与国家级大学生创新创业训练计划，由中央财政按照平均一个项目1万元的资助数额，予以经费支持</li>
            <li>各高校可根据申报项目的具体情况适当增减单个项目资助经费</li>
        </ul>

    </div>
    <div class="procedureBox">
        <span class="title"></span>
        <ul>
            <li class="right one">项目负责人向学院提交“大学生创新创业训练计划项目申报书”</li>
            <li class="left two">各学院组织“大学生创新创业训练计划项目”答辩小组对项目进行评审，确定院级项目，并提交校级项目到教务处（或创新创业相关部门）作进一步评级审核</li>
            <li class="right three">教务处（或创新创业相关部门）组织专家对学院推荐的项目进行审查，确定国家级和校级项目，报校领导审批，学院发文公布</li>
            <li class="left four">省属高校将国家级项目提交给省教育厅作进一步论证，通过论证后再提交给教育部进行审核（部属高校直接提交给教育部进行审核），审核通过后即可加入“国创计划”</li>
            <li class="right five">各高校需制定本校“大学生创新创业训练计划”项目管理办法，规范项目中期检查、项目结项、项目答辩等事项的管理，建立质量监控机制</li>
            <li class="left six">项目结束后，由学院组织项目验收，并将验收结果报教育部，由教育部在指定网站公布项目总结报告</li>
        </ul>
    </div>

</div>

<img class="decorationpic2" src="${ctxImg}/innovation11 (2).png"/>
<img class="decorationpic3" src="${ctxImg}/innovation11 (3).png"/>
<img class="decorationpic5" src="${ctxImg}/innovation11 (5).png"/>
<img class="decorationpic4" src="${ctxImg}/innovation11 (4).png"/>


</body>
</html>
