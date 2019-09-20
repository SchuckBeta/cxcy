<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${frontTitle}</title>
    <meta name="decorator" content="creative"/>
</head>
<body>

<style>
    .protype-info-list {
        margin-bottom: 60px;
        overflow: hidden;
    }

    .protype-info-list > li {
        float: left;
        width: 33.333%;
        height: 480px;
        padding: 0px 30px;
        text-align: center;
        box-sizing: border-box;
        overflow: hidden;
    }

    .protype-info-list > li p {
        font-family: "宋体";
        color: #666;
        font-size: 14px;
        line-height: 30px;
        text-align: justify;
        text-indent: 28px;
    }

    .protype-info-list > li h5 {
        color: #696969;
        font-size: 20px;
        font-weight: normal;
        line-height: 80px;
    }

    .invoation-title {
        font-family: "宋体";
        font-size: 14px;
        color: #666;
        font-weight: normal;
        text-align: center;
        line-height: 30px;
        margin-bottom: 50px;
    }

    .h-title_bar .h-title {
        font-weight: normal;
        font-family: "宋体";
        font-size: 28px;
        margin-bottom: 25px;
    }

    .innovation4-content {
        height: 431px;
        padding-top: 160px;
        box-sizing: border-box;
        background: url('/img/innovation4.png') center no-repeat;
        margin-bottom: 60px;
    }

    .innovation4-content p {
        width: 557px;
        font-family: "宋体";
        color: #666;
        font-size: 14px;
        line-height: 30px;
        margin: 0 auto;
    }

    .policyBox{
        margin-bottom: 60px;
    }

    .policyBox ul {
        width: 1017px;
        height: 314px;
        overflow: hidden;
        background: url('/img/innovation5.png') no-repeat;
        margin: 0 auto;
    }

    .policyBox ul li {
        width: 50%;
        height: 50%;
        float: left;
        box-sizing: border-box;
        -moz-box-sizing: border-box;
        -webkit-box-sizing: border-box;
        padding: 20px 160px 20px 20px;
        font-family: "宋体";
        color: #666;
        font-size: 14px;
        line-height: 30px;
    }

    .policyBox ul li:nth-child(2) {
        padding-right: 20px;
        padding-left: 160px;
    }

    .policyBox ul li:nth-last-child(2) {
        padding: 40px 160px 20px 20px;
    }

    .policyBox ul li:last-child {
        padding: 60px 20px 20px 160px;
    }
    .procedureBox{
        width: 100%;
        margin-bottom: 60px;
        overflow: hidden;
    }



    .procedureBox ul{
        width: 100%;
        min-height: 458px;
        overflow:hidden;
        background: url('/img/innovation6.png') no-repeat center center;
        box-sizing: border-box;
        -moz-box-sizing: border-box;
        -webkit-box-sizing: border-box;
        padding: 0px 180px;
    }

    .procedureBox ul li{
        width: 100%;
        overflow: hidden;
        color: #666;
        font-family: "宋体";
        font-size: 14px;
        box-sizing: border-box;
        -moz-box-sizing: border-box;
        -webkit-box-sizing: border-box;
        line-height: 20px;
    }

    .procedureBox ul li.left{
        float: left;
        padding-right: 55%;

    }

    .procedureBox ul li.right{
        float: right;
        padding-left: 55%;
    }

    .procedureBox ul li.one{
        margin-top: 35px;
    }

    .procedureBox ul li.two{
        margin-top: 40px;
    }

    .procedureBox ul li.three{
        margin-top: 10px;
    }

    .procedureBox ul li.four{
        margin-top: 28px;
    }

    .procedureBox ul li.five{
        margin-top: 15px;
    }

    .procedureBox ul li.six{
        margin-top: 15px;
    }

    .innovation-container{
        width: 1200px;
        padding-top: 60px;
        padding-bottom: 60px;
        margin: 0 auto;
    }
</style>

<div class="innovation-container">
    <div class="h-title_bar">
        <div class="h-column-wrap h-title_line"><h3
                class="h-title">大创项目介绍</h3>
        </div>
    </div>
    <p class="invoation-title">国家级大学生创新创业训练计划内容包括创新训练项目、创业训练项目和创业实践项目三类</p>
    <ul class="protype-info-list">
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
    <div class="h-title_bar">
        <div class="h-column-wrap h-title_line"><h3
                class="h-title">目标</h3>
        </div>
    </div>
    <div class="innovation4-content">
        <p>通过实施国家级大学生创新创业训练计划，促进高等学院转变教育思想观念，改革人才培养模式，强化创新创业能力训练，增强高校学生的创新能力和在创新基础上的创业能力，培养适应创新型国家建设需要的高水平创新人才。</p>
    </div>
    <div class="h-title_bar">
        <div class="h-column-wrap h-title_line"><h3
                class="h-title">经费扶持政策</h3>
        </div>
    </div>
    <div class="policyBox">
        <ul>
            <li>国家级大学生创新创业训练计划由中央财政、地方财政共同支持，参与高校按照不低于1:1的比例，自筹经费配套</li>
            <li>地方所属高校参加国家级大学生创新创业训练计划，由地方财政参照中央财政经费支持标准予以支持</li>
            <li>中央部委所属高校参与国家级大学生创新创业训练计划，由中央财政按照平均一个项目1万元的资助数额，予以经费支持</li>
            <li>各高校可根据申报项目的具体情况适当增减单个项目资助经费</li>
        </ul>
    </div>
    <div class="h-title_bar">
        <div class="h-column-wrap h-title_line"><h3
                class="h-title">审批流程</h3>
        </div>
    </div>
    <div class="procedureBox">
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


</body>
</html>
