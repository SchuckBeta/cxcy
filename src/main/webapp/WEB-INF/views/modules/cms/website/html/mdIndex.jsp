<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="site-decorator"/>
    <title>${frontTitle}</title>
    <style>
        .main-title-md{
            position: relative;
            font-size: 24px;
            padding-bottom: 19px;
            line-height: 1.1;
            color: #4b4b4b;
            text-align: center;
        }
        .main-title-md:after{
            content: '';
            position: absolute;
            left: 50%;
            bottom: 0;
            width: 52px;
            height: 4px;
            background-color: #df3b0a;
            margin-left: -26px;
        }

        .school-intro{
            width: 1025px;
            margin: 40px auto 62px;
            font-size: 14px;
            line-height: 32px;
            color: #505050;
            text-indent: 32px;
            text-align: justify;
        }

        .activity-photos {
            width: 1210px;
            padding-bottom: 10px;
            margin-left: auto;
            margin-right: auto;
            margin-top: 56px;
            overflow: hidden;
        }

        .activity-photos .photo {
            position: relative;
            float: left;
            border: 6px solid #fff;
            border-radius: 10px;
            box-shadow: 5px 5px 10px 0 rgba(217, 217, 217, .75);
        }

        .activity-photos .photo8 {
            margin-left: 67px;
            margin-right: 10px;
        }

        .activity-photos .photo .photo-pic-box {
            border-radius: 15px;
            overflow: hidden;
        }

        .activity-photos .photo8 .photo-pic-box {
            width: 507px;
            height: 318px;
        }

        .activity-photos .photo7 .photo-pic-box {
            width: 536px;
            height: 361px;
        }

        .activity-photos .photo6 {
            margin-top: -65px;
            z-index: 10;
        }

        .activity-photos .photo6 .photo-pic-box {
            width: 668px;
            height: 413px;
        }

        .activity-photos .photo5 {
            margin-top: 30px;
            margin-left: -45px;
            z-index: 12;
        }

        .activity-photos .photo5 .photo-pic-box {
            width: 517px;
            height: 327px;
        }

        .activity-photos .photo4 {
            margin-top: -76px;
            margin-left: 30px;
        }

        .activity-photos .photo4 .photo-pic-box {
            width: 566px;
            height: 417px;
        }

        .activity-photos .photo3 {
            margin-top: 45px;
            margin-left: 30px;
        }

        .activity-photos .photo3 .photo-pic-box {
            width: 529px;
            height: 336px;
        }

        .activity-photos .photo2 {
            margin-top: -68px;
            margin-left: 153px;
        }

        .activity-photos .photo2 .photo-pic-box {
            width: 459px;
            height: 341px;
        }

        .activity-photos .photo1 {
            margin-top: 72px;
            margin-left: -54px;
        }

        .activity-photos .photo1 .photo-pic-box {
            width: 506px;
            height: 281px;
        }

        .footer-banner-md{
            position: relative;
            margin-top: -300px;
            height: 800px;
            background: url('/images/md/footer-banner-md.jpg') no-repeat center bottom/cover;
            z-index: -1;
        }

        /*@media (min-width: 1200px) {*/
            /*.footer-banner-md {*/
                /*height: 500px;*/
            /*}*/
        /*}*/

        /*@media (min-width: 1600px) {*/
            /*.footer-banner-md {*/
                /*margin-top: -300px;*/
                /*height: 1102px;*/
            /*}*/
        /*}*/

        .skills {
            width: 843px;
            margin: 40px auto 100px;
            overflow: hidden;
        }

        .skills .skill {
            float: left;
            position: relative;
            width: 165px;
            height: 165px;
            margin-left: -52px;
            /*transition: all 0.15s linear;*/
        }

        .skills .skill:first-child{
            margin-left: 0;
        }

        .skills .skill-opacity20 {
            background: url('/images/md/function-bg20.png') no-repeat center;
        }

        .skills .skill-opacity40 {
            background: url('/images/md/function-bg40.png') no-repeat center;
        }

        .skills .skill-opacity60 {
            background: url('/images/md/function-bg60.png') no-repeat center;
        }

        .skills .skill-current {
            width: 165px;
            height: 165px;
            background-image: url('/images/md/function-hover-bg.png');
            z-index: 100;
        }

        .skills .skill .skill-inner{
            width: 122px;
            height: 122px;
            text-align: center;
            border-radius: 50%;
            overflow: hidden;
            margin: 23px auto 0;
        }
        .skills .skill .skill-inner{
            line-height: 122px;
        }
        .skills .skill .name{
            display: inline-block;
            font-size: 14px;
            color: #6c6c6c;
            line-height: 20px;
            vertical-align: middle;
            /*transition: all 0.3s linear;*/
        }
        .skills .skill-current .skill-inner{
            height: 137px;
            width: 137px;
            margin-left: 14px;
            margin-top: 14px;
            line-height: 137px;
            background-image: -webkit-linear-gradient(285deg,#df4526,#fb9402);
            background-image: linear-gradient(285deg,#df4526,#fb9402);
            border: 0;
        }
        .skills .skill-current .name{
            display: inline-block;
            font-size: 18px;
            color: #fff;
            line-height: 25px;
            vertical-align: middle;
        }
    </style>
</head>
<body>
<div class="container" style="width: 1240px; padding-top: 60px;">
    <h3 class="main-title-md">创新创业学院</h3>
    <p class="school-intro">
        创新创业中心成立于2005年3月，2017年1月更名为中南民族大学创新创业学院，是学院开展大学生创新创业教育活动的组织管理机构，是协助在校大学生获取创新创业学分的职能部门，是支持创新活动、孵化创业企业、培养优秀毕业生和壮大双创生力军的综合服务平台。</p>
    <h3 class="main-title-md">主要功能</h3>
    <div class="skills">
        <div class="skill skill-opacity20">
            <div class="skill-inner">
                <span class="name">大学生<br>创新创业<br>成果奖<br>评选</span>
            </div>
        </div>
        <div class="skill skill-opacity40">
            <div class="skill-inner">
                <span class="name">大学生<br>创业孵化<br>基地管理</span>
            </div>
        </div>
        <div class="skill skill-opacity60">
            <div class="skill-inner">
                <span class="name">大学生<br>创业教育<br>与实践</span>
            </div>
        </div>
        <div class="skill skill-opacity60 skill-current">
            <div class="skill-inner">
                <span class="name">创新创业<br>教育</span>
            </div>
        </div>
        <div class="skill skill-opacity60">
            <div class="skill-inner">
                <span class="name">学科<br>竞赛</span>
            </div>
        </div>
        <div class="skill skill-opacity40">
            <div class="skill-inner">
                <span class="name">大学生<br>创新创业<br>训练<br>计划项目</span>
            </div>
        </div>
        <div class="skill skill-opacity20">
            <div class="skill-inner">
                <span class="name">大学生<br>就业<br>教育与指导</span>
            </div>
        </div>
    </div>
    <h3 class="main-title-md">活动风采</h3>
    <div class="activity-photos">
        <div class="photo photo8">
            <div class="photo-pic-box">
                <img class="img-responsive" src="${ctxImages}/md/znmd-pic8.jpg">
            </div>
        </div>
        <div class="photo photo7">
            <div class="photo-pic-box">
                <img class="img-responsive" src="${ctxImages}/md/znmd-pic7.jpg">
            </div>
        </div>
        <div class="photo photo6">
            <div class="photo-pic-box">
                <img class="img-responsive" src="${ctxImages}/md/znmd-pic6.jpg">
            </div>
        </div>
        <div class="photo photo5">
            <div class="photo-pic-box">
                <img class="img-responsive" src="${ctxImages}/md/znmd-pic5.jpg">
            </div>
        </div>
        <div class="photo photo4">
            <div class="photo-pic-box">
                <img class="img-responsive" src="${ctxImages}/md/znmd-pic4.jpg">
            </div>
        </div>
        <div class="photo photo3">
            <div class="photo-pic-box">
                <img class="img-responsive" src="${ctxImages}/md/znmd-pic3.jpg">
            </div>
        </div>
        <div class="photo photo2">
            <div class="photo-pic-box">
                <img class="img-responsive" src="${ctxImages}/md/znmd-pic2.jpg">
            </div>
        </div>
        <div class="photo photo1">
            <div class="photo-pic-box">
                <img class="img-responsive" src="${ctxImages}/md/znmd-pic1.jpg">
            </div>
        </div>
    </div>
    <div class="footer-banner-md"></div>
</div>
<script type="text/javascript">
    $(function () {
        var $skills = $('.skills');
        var $skill = $skills.find('.skill');
        $skill.hover(function () {
            $(this).addClass('skill-current').siblings().removeClass('skill-current')
        },function () {

        })
    })
</script>
</body>
</html>
