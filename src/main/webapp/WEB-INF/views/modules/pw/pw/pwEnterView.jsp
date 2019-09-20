<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>
<div id="app" v-show="pageLoad" class="container-fluid mgb-60" style="display: none">
    <div class="mgb-20">
        <edit-bar second-name="详情"></edit-bar>
    </div>
    <div class="panel panel-pw-enter mgb-20">
        <div class="panel-body" style="min-width: 1086px;">
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
            var pwEnter = JSON.parse(JSON.stringify(${fns: toJson(pwEnter)}));
            return {
                pwEnterId: pwEnter.id,
                type: pwEnter.type,
                pwEnter: pwEnter,
                tabActiveName: pwEnter.type === '2' ? 'firstPwEnterTab' : 'secondPwEnterTab'
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