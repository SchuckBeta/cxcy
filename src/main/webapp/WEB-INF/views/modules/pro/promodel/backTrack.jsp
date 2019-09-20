<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar second-name="进度跟踪"></edit-bar>
    </div>
    <div class="pro_info-header mgb-20">
        <div class="pro_info-pic">
            <img :src="proModel.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proGConPicFilter">
        </div>
        <div class="pro_info-sider">
            <h4 class="title"> {{proModel.pName}}</h4>
            <div class="pro-persons">
                <p class="charge">负责人：<span> {{proModel.deuser.name}}</span></p>
                <p class="team-member">团队成员：
                    <span v-for="item in teamStu" :key="item.id">{{item.name}}</span>
                </p>
                <p class="teachers">指导教师：
                    <span v-for="item in teamTea" :key="item.id">{{item.name}}</span>
                </p>
            </div>
        </div>
    </div>
    <div class="panel">
        <div class="panel-header">
            项目简介
        </div>
        <div class="panel-body">
            <span v-if="!!proModel.introduction" class="white-space-pre" v-html="proModel.introduction"></span>
            <div v-else class="text-center"><span class="empty">暂无项目介绍</span></div>
        </div>
    </div>
    <div class="panel">
        <div class="panel-header">进度跟踪</div>
        <div class="panel-body">
            <pro-progress v-model="timeLineData"></pro-progress>
        </div>
    </div>
</div>
<script>
    'use strict';
    new Vue({
        el: '#app',
        data: function () {
            var timeLineData = JSON.parse(JSON.stringify(${fns: toJson(timeLineData)})) || [];
            var proModel = JSON.parse(JSON.stringify(${fns: toJson(proModel)})) || {};
            var teamTea = JSON.parse(JSON.stringify(${fns: toJson(teamTea)})) || [];
            var teamStu = JSON.parse(JSON.stringify(${fns: toJson(teamStu)})) || [];
            return {
                timeLineData: timeLineData,
                proModel: proModel,
                teamTea: teamTea,
                teamStu: teamStu
            }
        }
    })

</script>
</body>
</html>