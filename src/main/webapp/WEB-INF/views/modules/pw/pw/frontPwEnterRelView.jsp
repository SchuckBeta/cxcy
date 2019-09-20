<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${fns:getConfig('frontTitle')}</title>
    <meta name="decorator" content="creative"/>

</head>
<body>
<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>创业基地</el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/pw/pwEnterRel/list">入驻查询</a></el-breadcrumb-item>
        <el-breadcrumb-item>查看入驻详情</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="panel panel-pw-enter mgb-20">
        <div class="panel-body">
            <applicant-info :pw-enter-id="pwEnterId"></applicant-info>
            <div class="pro-category-placeholder"></div>
        </div>
    </div>
    <div class="pro-contest-content panel-padding-space mgb-20">
        <el-tabs class="pro-contest-tabs" v-model="tabActiveName">
            <el-tab-pane v-if="type == '2'" label="企业信息" name="firstPwEnterTab">
                <tab-pane-content>
                    <pw-enter-company :pw-enter-id="pwEnterId"></pw-enter-company>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="团队信息" name="secondPwEnterTab">
                <tab-pane-content>
                    <pw-enter-team :pw-enter-id="pwEnterId"></pw-enter-team>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="项目信息" name="thirdPwEnterTab">
                <tab-pane-content>
                    <pw-enter-project-view :pw-enter-id="pwEnterId"></pw-enter-project-view>
                </tab-pane-content>
            </el-tab-pane>
            <template v-if="pwEnter.isTemp === '0'">
                <el-tab-pane label="场地要求" name="fourPwEnterTab">
                    <tab-pane-content>
                        <pw-enter-site :pw-enter-id="pwEnterId"></pw-enter-site>
                    </tab-pane-content>
                </el-tab-pane>
                <el-tab-pane label="入驻记录" name="fivePwEnterTab">
                    <tab-pane-content>
                        <pw-enter-record-list :pw-enter-id="pwEnterId"></pw-enter-record-list>
                    </tab-pane-content>
                </el-tab-pane>
                <el-tab-pane label="成果记录" name="sixPwEnterTab">
                    <tab-pane-content>
                        <pw-enter-result-list :pw-enter-id="pwEnterId"></pw-enter-result-list>
                    </tab-pane-content>
                </el-tab-pane>
                <el-tab-pane v-if="pwEnter.status != '0'" label="审核记录" name="sevenPwEnterTab">
                    <tab-pane-content>
                        <pw-enter-result :pw-enter="pwEnter"></pw-enter-result>
                    </tab-pane-content>
                </el-tab-pane>
            </template>
        </el-tabs>
    </div>
    <div class="text-center">
        <el-button size="mini" @click.stop.prevent="goToBack">返回</el-button>
    </div>
</div>

<script type="text/javascript">
    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var pwEnterId = '${pwEnterId}';
            var type = '${type}';
            var pwEnter = JSON.parse(JSON.stringify(${fns: toJson(pwEnter)}));
            return {
                pwEnterId: pwEnterId,
                type: type,
                pwEnter: pwEnter,
                tabActiveName: type === '2' ? 'firstPwEnterTab' : 'secondPwEnterTab'
            }
        },
        methods: {
            goToBack: function () {
              return  window.history.go(-1);
            }
        }
    })
</script>
</body>
</html>