<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>


</head>
<body>

<div id="app" v-show="pageLoad" style="display: none;" class="container page-container mgb-60">
    <el-breadcrumb class="mgb-20" separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <c:if test="${user.userType eq 2}">
            <el-breadcrumb-item><a href="${ctxFront}/sys/frontTeacherExpansion/form?id=${user.id}">双创简历</a>
            </el-breadcrumb-item>
        </c:if>
        <c:if test="${user.userType eq 1}">
            <el-breadcrumb-item><a href="${ctxFront}/sys/frontStudentExpansion/findUserInfoById?id=${user.id}">双创简历</a>
            </el-breadcrumb-item>
        </c:if>
        <el-breadcrumb-item>登录信息</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="user_avatar-sidebar">
        <div class="user-avatar">
            <div class="avatar-pic">
                <img :src="userPhoto | ftpHttpFilter(ftpHttp) | studentPicFilter">
            </div>
        </div>
        <ul class="user-info_menu">
            <li><a :href="linkHref"><i class="iconfont icon-user"></i>{{userTypeLabel}}</a></li>
            <li class="active"><a href="javascript: void (0);"><i class="iconfont icon-14"></i>登录信息</a></li>
            <li v-if="hasMvcode"><a href="${ctxFront}/sys/frontStudentExpansion/frontUserMobile"><i class="iconfont icon-unie64f"></i>手机信息</a>
            </li>
        </ul>
    </div>
    <div class="user_detail-container">
        <div class="user_detail-title">
            <i class="iconfont icon-14"></i><span class="text">修改登录信息</span>
        </div>
        <div class="user_detail-wrap" style="min-height: 287px;">
            <div class="user-detail_inner_pm" style="padding-top: 56px;">
                <password-form :id="userId"></password-form>
            </div>
        </div>
    </div>
</div>


<script>

    'use strict';
     new Vue({
        el: '#app',
        data: function () {
            return {
                userPhoto: '${user.photo}',
                isUpdating: false,
                userId: '${user.id}',
                userType: '${user.userType}'
            }
        },
        computed: {
            userTypeLabel: function () {
                return this.userType == '1' ? '双创简历' : '基本信息'
            },
            linkHref: function () {
                var link = '';
                link = this.userType == '1' ? '/sys/frontStudentExpansion/findUserInfoById?id=' : '/sys/frontTeacherExpansion/form?id=';
                return (this.frontOrAdmin + link + this.userId);
            }
        }
    })


</script>

</body>
</html>