<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="creative"/>

</head>
<body>
<!--顶部header公用部分-->

<div id="app" v-show="pageLoad" style="display: none;" class="user-register">
    <div class="container page-container" style="padding-top:45px;">
        <div class="user-register-box">
            <p>{{tip}}</p>
            <div class="user-register-form text-center">
                <img src="${ctxImg}/u4110.png" alt=""/>
                <p style="font-size: 16px;margin: 30px 0;">恭喜你，成功注册！</p>
                <el-form :action="action" :model="registerSucForm" ref="registerSucForm" size="mini" >
                    <el-form-item>
                        <el-button type="primary" @click.stop.prevent="completeInfo">完善基本信息</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var userId = '${fns:getUser().id}';
            return {
                'userType': '${userType}',
                userId: userId,
                registerSucForm: {}
            }
        },
        computed:  {
            action: function () {
                return this.frontOrAdmin + (this.loginCurUser.userType === '1' ? '/sys/frontStudentExpansion/findUserInfoById?id=' : '/sys/frontTeacherExpansion/findUserInfoById?userId=')+this.loginCurUser.id;
            },
            tip: function () {
                return this.loginCurUser.userType === '1' ? '学生注册账号' : '导师注册账号'
            }
        },
        methods: {
            completeInfo: function () {
                location.href = this.action;
            }
        }
    })

</script>

</body>
</html>
