<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/8/8
  Time: 14:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="site-decorator"/>

    <title>${frontTitle}</title>
    <style>
         *{
             box-sizing: content-box;
         }
         .container-big-match{
             background: #fff url('/images/bigMatchIconbg.jpg') no-repeat center;
         }
        .container-big-match .wrap {
            position: relative;
            width: 100%;
            margin: 0 auto;
            max-width: 800px;
        }

        .container-big-match .circles-bg {
            padding-top: 50%;
            background: url('/images/circles.png') no-repeat center/cover;
        }

        .container-big-match .projects {
            position: absolute;
            left: 0;
            top:0;
            z-index: 1000;
        }

        .container-big-match .project-intro {
            position: absolute;
            padding-top: 40px;

        }

        .container-big-match .project-pic {
            position: absolute;
            left: 50%;
            bottom:70px;
            margin-left: -35px;
            width: 70px;
            height: 70px;
            background: url('/images/bigMatchIcon.png') no-repeat;
        }

        .container-big-match .circle {
            width: 101px;
            height: 101px;
            padding: 8px;
            border-radius: 50%;
        }

        .container-big-match .circle-inner {
            padding-top: 40px;
            height: 61px;
            border-radius: 50%;
        }

        .container-big-match .circle-pink {
            background-color: rgba(254, 211, 219, 0.43);
        }

        .container-big-match .circle-pink .circle-inner {
            background-color: #fc98ab;
        }

         .container-big-match .circle-purple {
             background-color: rgba(213, 165, 251, 0.54);
         }

         .container-big-match .circle-purple .circle-inner {
             background-color: #d5a5fb;
         }


         .container-big-match .circle-blue {
             background-color: rgba(134, 189, 245, 0.54);
         }

         .container-big-match .circle-blue .circle-inner {
             background-color: #288aee;
         }

         .container-big-match .circle-yellow {
             background-color: rgba(249, 216, 190, 0.54);
         }

         .container-big-match .circle-yellow .circle-inner {
             background-color: #f3b786;
         }

        .container-big-match .circle-inner p {
            margin-bottom: 0;
            color: #fff;
            font-weight: bold;
            text-align: center;
        }

        .container-big-match .circle-inner .circle-label {
            position: relative;
            font-size: 16px;
        }

        .container-big-match .circle-inner .number {
            position: relative;
            font-size: 20px;
        }

        .project-declaration{
            left: 121px;
            top: 137px;
        }

        .project-creative{
            top: 121px;
            left: 318px;
        }

        .project-creating{
            top: 40px;
            left: 531px;
        }

        .project-teacher{
            top: 228px;
            left: 500px;
        }

         .container-big-match .project-pic-declaration{
            background-position: 2px 0;
        }
         .container-big-match .project-pic-declaration:hover{
             background-position: -81px 0;
         }

         .container-big-match .project-pic-creative{
             background-position: -6px -84px;
         }
         .container-big-match .project-pic-creative:hover{
             background-position: -89px -84px;
         }

         .container-big-match .project-pic-creating{
             background-position: -10px -164px;
         }
         .container-big-match .project-pic-creating:hover{
             background-position: -93px -164px;
         }


         .container-big-match .project-pic-teacher{
             background-position: -10px -263px;
         }
         .container-big-match .project-pic-teacher:hover{
             background-position: -93px -263px;
         }
    </style>
</head>
<body>


<div class="container-fluid container-big-match">
    <div class="wrap">
        <div class="circles-bg"></div>
        <div class="projects">
            <div class="project-intro project-declaration">
                <div class="project-pic project-pic-declaration"></div>
                <div class="circle circle-pink">
                    <div class="circle-inner">
                        <p class="circle-label">申报学生</p>
                        <p class="number">1800人</p>
                    </div>
                </div>
            </div>
            <div class="project-intro project-creative">
                <div class="project-pic project-pic-creative"></div>
                <div class="circle circle-purple">
                    <div class="circle-inner">
                        <p class="circle-label">创新项目</p>
                        <p class="number">1800人</p>
                    </div>
                </div>
            </div>
            <div class="project-intro project-creating">
                <div class="project-pic project-pic-creating"></div>
                <div class="circle circle-blue">
                    <div class="circle-inner">
                        <p class="circle-label">创业项目</p>
                        <p class="number">1800人</p>
                    </div>
                </div>
            </div>
            <div class="project-intro project-teacher">
                <div class="project-pic project-pic-teacher"></div>
                <div class="circle circle-yellow">
                    <div class="circle-inner">
                        <p class="circle-label">项目导师</p>
                        <p class="number">1800人</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
