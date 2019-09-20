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
        <el-breadcrumb-item>手机信息</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="user_avatar-sidebar">
        <div class="user-avatar">
            <div class="avatar-pic">
                <img :src="userPhoto | ftpHttpFilter(ftpHttp) | studentPicFilter">
            </div>
        </div>
        <ul class="user-info_menu">
            <li><a :href="userTypeHref"><i class="iconfont icon-user"></i>{{userTypeLabel}}</a></li>
            <li v-if="casHasLogin"><a href="${ctxFront}/sys/frontStudentExpansion/frontUserPassword"><i
                    class="iconfont icon-14"></i>登录信息</a></li>
            <li class="active"><a href="javascript: void (0);"><i class="iconfont icon-unie64f"></i>手机信息</a></li>
        </ul>
    </div>
    <div class="user_detail-container">
        <div class="user_detail-title">
            <i class="iconfont icon-unie64f"></i><span class="text">修改手机信息</span>
        </div>
        <div class="user_detail-wrap" style="min-height: 287px;">
            <div class="user-detail_inner_pm" style="padding-top: 56px;">
                <mobile-form ref="dialogMobileForm" :mobile.sync="newMobile" :old-mobile="userMobile"
                             @update-user-mobile="updateUserMobile"
                             :is-add="!userMobile"></mobile-form>
                <div class="text-right">
                    <el-button size="mini" :disabled="isUpdating" type="primary"
                               @click.stop.prevent="handleUpdateMobile">保存
                    </el-button>
                </div>
            </div>
        </div>
    </div>
</div>


<script>

    'use strict';

    new Vue({
        el: '#app',
        mixins: [Vue.verifyExpressionMixin],
        data: function () {
            return {
                id: '${user.id}',
                userId: '${user.id}',
                userPhoto: '${user.photo}',
                userMobile: '${user.mobile}',
                isUpdating: false,
                dialogVisibleChangeMobile: false, //修改手机号
                newMobile: '',
                isTimeTick: false,
                userType: '${user.userType}'
            }
        },
        computed: {
            userTypeLabel: function () {
                return this.userType == '1' ? '双创简历' : '基本信息'
            },
            userTypeHref: function () {
                var link = '';
                link = this.userType == '1' ? '/sys/frontStudentExpansion/findUserInfoById?id=' : '/sys/frontTeacherExpansion/form?id=';
                return this.frontOrAdmin + link + this.userId;
            }
        },
        methods: {

            handleChangeMobileClose: function () {
                this.$refs.dialogMobileForm.clearMobileForm();
                this.dialogVisibleChangeMobile = false;
            },

            handleChangeMobile: function () {
                this.dialogVisibleChangeMobile = true;
            },

            handleUpdateMobile: function () {
                this.$refs.dialogMobileForm.mobileFormValidate()
            },

            updateUserMobile: function (mobileForm) {
                var self = this;
                this.isUpdating = true;
                this.$axios.post('/sys/frontStudentExpansion/updateUserMobile?mobile=' + mobileForm.mobile + '&userId=' + mobileForm.id).then(function (data) {
                    if (data.status) {
                        self.userMobile = mobileForm.mobile;
                        self.dialogVisibleChangeMobile = false;
                        self.$alert('修改手机号成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$message.error(data.msg)
                    }
                    self.isUpdating = false;
                    self.$refs.dialogMobileForm.clearMobileForm();
                    self.isTimeTick = false;
                }).catch(function () {
                    self.isUpdating = false;
                    self.$message.error(self.xhrErrorMsg)
                })
            }
        },
        beforeMount: function () {
        }
    })


</script>

</body>
</html>