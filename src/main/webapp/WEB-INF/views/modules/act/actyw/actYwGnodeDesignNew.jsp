<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <script src="${ctxStatic}/snap/snap.svg-min.js"></script>
    <script src="${ctxStatic}/vue/vuex.js"></script>
    <script src="${ctxJs}/components/flowDesignComponents/store.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/flowDesignConfig.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/aside-nodes.js"></script>
    <script src="${ctxJs}/components/flowDesignComponents/el-user-task.js?version=1${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/control-node-rect-box.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/el-end-node-event.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/el-end-terminate-event.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/el-ex-clusive-gateway.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/el-fdc-panning.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/el-flow-header.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/el-process-node.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/el-sequence-flow.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/el-start-node.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/el-svg-move-node.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/el-svg-paper.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/el-svg-task-node.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/components/flowDesignComponents/flow-design-form-side.js?version=${fns: getVevison()}"></script>
    <style>
        html, body {
            width: 100%;
            height: 100%;
            overflow: hidden;
        }
    </style>
</head>
<body>

<div id="app" style="height: 100%;overflow: hidden">
    <el-flow-header></el-flow-header>
    <div element-loading-text="保存中..." class="flow-design-container">
        <aside-nodes></aside-nodes>
        <div class="flow-design-content">
            <el-fdc-panning>
                <div class="fdc-viewport">
                    <el-svg-paper></el-svg-paper>
                </div>
                <control-node-rect-box></control-node-rect-box>
            </el-fdc-panning>
        </div>
        <flow-design-form-side></flow-design-form-side>
        <el-svg-move-node v-show="visibleSvgMove"></el-svg-move-node>
    </div>
</div>

</body>

<script>

    //重构需要注意回退的和基本的preFunId和preIds两个数据正确性
    new Vue({
        el: '#app',
        store: store,
        data: function(){
            return {
                studentId: '${frontUid}',
                groupName: '${group.name}',
                groupId: '${group.id}',
                rootId: '${root}',
                theme: '${group.theme}',
                flowType: '${group.flowType}',
                expertId:'${roleZjId}',
                proType: '${group.type}', //项目类型
                isShowZp:${group.showZp},
                isShowWp:${group.showWp}
            }
        },
        computed: {
            visibleSvgMove: function () {
                return this.$store.state.visibleSvgMove
            },
            svgPaperStyle: function () {
                var svgPaperStyle = this.$store.state.svgPaperStyle;
                return {
                    width: svgPaperStyle.width + 'px',
                    height: svgPaperStyle.height + 'px'
                };
            },
            hasTeacherAudit: function () {
                return this.theme === '70';
            }
        },
        created: function () {
        }
    })

</script>


</html>