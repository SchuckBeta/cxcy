<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.security.Principal" %>
<%@ page import="org.jasig.cas.client.authentication.AttributePrincipal" %>
<%@ page import="java.util.Iterator" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
    <style>
        .base-content-container:after, .bc-row:after, .cas-user-box .cas-user-wrapper:after, .base-content-container:before, .bc-row:before, .cas-user-box .cas-user-wrapper:before {
            content: '';
            display: table; }

        .base-content-container:after, .bc-row:after, .cas-user-box .cas-user-wrapper:after {
            clear: both; }

        .team-build .box-border, .team-member-container, .user-register-form, .base-content-list-container, .gcontest-calendars, .cas-user-box {
            border-radius: 5px;
            border: 1px solid #f2f2f2;
            box-shadow: 3px 3px 5px 0 #ededed; }

        .cas-user-box {
            width: 520px;
            margin: 100px auto 0; }
        .cas-user-box .cas-user-wrapper {
            width: 215px;
            margin: 30px auto; }
        .cas-user-box .cas-user-pic {
            border: 1px solid #f2f2f2;
            border-radius: 5px;
            box-sizing: border-box; }
        .cas-user-box .user-type-name {
            margin: 12px 0 0; }
        .cas-user-box .cas-user-item {
            float: left;
            width: 100px;
            margin-right: 15px;
            cursor: pointer;
            box-sizing: border-box; }
        .cas-user-box .cas-user-item.selected .cas-user-pic {
            border-color: #e9432d;
            box-shadow: 0 0 3px 3px rgba(233, 67, 45, 0.3); }
        .cas-user-box .cas-user-item:last-child {
            margin-right: 0; }
        .cas-user-box .cas-user-item:hover .cas-user-pic {
            border-color: #e9432d; }
        .cas-user-box .cas-user-item img {
            display: block;
            width: 100%; }

    </style>
</head>
<body>

<div id="app" v-show="pageLoad" style="display: none;" class="container page-container mgb-60 pdt-60">
    <div class="cas-user-box">
        <div class="cas-user-wrapper" style="text-align: left; margin-left: 30px; border-bottom: 1px solid #eee; width: 90%;">请点击头像选择当前登录的用户类型：</div>
        <div class="cas-user-wrapper">
            <div class="cas-user-item selected" v-for="item in userTypes" :class="{selected: item.value == user_type}" @click.stop.prevent="selectedUserType(item.value)">
                <div class="cas-user-pic">
                    <img :src="item.src">
                </div>
                <p class="text-center user-type-name">{{item.label}}</p>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            return {
                userTypes: [
                    {label: '学生', value: '1', src: '/img/student_112.55455712452px_1218516_easyicon.net.png'},
                    {label: '导师', value: '2', src:'/img/Teacher_72px_500807_easyicon.net.png'}
                ],
               user_type: '1'
            }
        },
        methods: {
            selectedUserType: function (value) {
                this.user_type = value;
                location.href = this.frontOrAdmin + '/cas/caslogin?user_type=' + this.user_type;
            }
        }
    })

</script>
</body>
</html>