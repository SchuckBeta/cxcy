

var  store = new Vuex.Store({
    state: {
        topHeight: 49,
        startNodeEvent: {},
        sequenceFlowData: {},
        disabledAll: false,
        visibleSvgMove: false,
        nodeMoveData: {
            x: 0,
            y: 0,
            dx: 0,
            dy: 0,
            width: 0,
            height: 0,
            nodeData: {},
            nodeSvg: ''
        },
        svgPaperStyle: {
            width: 0,
            height: 0
        },
        snapSvgPaper: {},
        moveableRectVisible: false,
        moveableRect: {
            x: 0,
            y: 0,
            width: 0,
            height: 0,
            startX: 0,
            startY: 0,
            groupBBox: null
        },
        dialogVisibleIconUrl: false,
        flowNodeList: [],
        curFlowNode: {
            node: {},
            index: -1
        },
        pathConditions: [], //选中路劲时候的条件禁用
        pathConditionsAll: [],//所有可以选的条件
        sourceNode: [], // 筛选表单名称
        formList: [], //列表表单
        taskTypes: [],//任务类型
        userRoles: [],//用户角色
        regTypes: [],//审核类型
        shForms: [],//审核类型
        nodeStatusTypes: [], //判定类型，
        jtClazzIds: [], //监听事件
        gNodeTypes: [],
        hasCondition: false, // 是否显示线上条件
    },
    mutations: {
        setSnapSvgPaper: function (state, svg) {
            state.snapSvgPaper = svg;
        },

        changeNodeMoveData: function (state, nData) {
            state.nodeMoveData = nData;
        },
        changeVisibleSvgMove: function (state, b) {
            state.visibleSvgMove = b;
        },
        changeSvgPaperStyle: function (state, style) {
            if(style.width){
                state.svgPaperStyle.width = style.width;
            }
            if(style.height){
                state.svgPaperStyle.height = style.height;
            }

        },
        changeableRectVisible: function (state, visible) {
            state.moveableRectVisible = visible;
        },
        changeMoveableRect: function (state, offset) {
            state.moveableRect = offset;
        },
        changeDialogVisibleIconUrl: function (state, b) {
            state.dialogVisibleIconUrl = b;
        },
        setFlowNodeList: function (state, list) {
            state.flowNodeList = list;
        },
        updateFlowNodeList: function (state, payload) {
            var flowNodeList = state.flowNodeList;
            for(var i = 0; i < flowNodeList.length; i++){
                if(payload.node.id === flowNodeList[i].id){
                    state.flowNodeList.splice(i, 1, payload.node);
                    break;
                }
            }

        },
        removeFlowNodeList: function (state, payload) {
            state.flowNodeList.splice(payload.index, 1);
        },
        setCurFlowNode: function (state, node) {
            state.curFlowNode = node;
        },
        setFormList: function (state, list) {
            state.formList = list;
        },
        setTaskTypes: function (state, data) {
            state.taskTypes = data;
        },
        setUserRoles: function (state, data) {
            state.userRoles = data;
        },
        setRegTypes: function (state, data) {
            state.regTypes = data;
        },
        setShForms: function (state, data) {
            state.shForms = data;
        },

        setNodeStatusTypes: function (state, data) {
            state.nodeStatusTypes = data;
        },

        setJtClazzIds: function (state, data) {
            state.jtClazzIds = data;
        },

        setSourceNode: function (state, data) {
            state.sourceNode = data;
        },
        changePathConditions: function (state, data) {
            state.pathConditions = data;
        },
        changePathConditionsAll: function (state, data) {
            state.pathConditionsAll = data;
        },
        changeDisabledAll: function (state, bo) {
            state.disabledAll = bo;
        },
        changeStartNodeEvent: function (state, data) {
            state.startNodeEvent = data;
        },
        changeSequenceFlowData: function (state, data) {
            state.sequenceFlowData = data;
        },
        changeGNodeTypes: function (state, data) {
            state.gNodeTypes = data;
        },
        changeHasCondition: function (state, data) {
            state.hasCondition = data;
        }
    },
    actions: {
        changeNodeMoveData: function (context, nData) {
            context.commit('changeNodeMoveData', nData)
        },
        changeVisibleSvgMove: function (context, b) {
            context.commit('changeVisibleSvgMove', b)
        },
        changeSvgPaperStyle: function (context, style) {
            context.commit('changeSvgPaperStyle', style)
        },
        changeMoveableRect: function (context, offset) {
            context.commit('changeMoveableRect', offset)
        },
        changeableRectVisible: function (context, visible) {
            context.commit('changeableRectVisible', visible)
        },
        changeDialogVisibleIconUrl: function (context, visible) {
            context.commit('changeDialogVisibleIconUrl', visible)
        },
        setFlowNodeList: function (context, list) {
            context.commit('setFlowNodeList', list)
        },

        changeFlowNodeList: function (context, params) {
            context.commit('updateFlowNodeList', params);
            // context.commit('setCurFlowNode', {
            //     node: params.node,
            //     index: params.index
            // });
        },

        updateOtherFlowNode: function (context, params) {
            context.commit('updateFlowNodeList', params);
        },

        removeFlowNodeList: function (context, data) {
            context.commit('removeFlowNodeList', {index: data});
        },

        setCurFlowNode: function (context, node) {
            context.commit('setCurFlowNode', node)
        },

        setSnapSvgPaper: function (context, svg) {
            context.commit('setSnapSvgPaper', svg)
        },

        setFormList: function (context, list) {
            context.commit('setFormList', list)
        },

        setTaskTypes: function (context, data) {
            context.commit('setTaskTypes', data)
        },
        setUserRoles: function (context, data) {
            context.commit('setUserRoles', data)
        },
        setRegTypes: function (context, data) {
            context.commit('setRegTypes', data)
        },
        setShForms: function (context, data) {
            context.commit('setShForms', data)
        },
        setNodeStatusTypes: function (context, data) {
            context.commit('setNodeStatusTypes', data);
        },

        setJtClazzIds: function (context, data) {
            context.commit('setJtClazzIds', data);
        },

        setSourceNode: function (context, data) {
            context.commit('setSourceNode', data);
        },

        setPathConditions: function (context, data) {
            context.commit('changePathConditions', data);
        },
        setPathConditionsAll: function (context, data) {
            context.commit('changePathConditionsAll', data);
        },
        updateDisabledAll: function (context, bo) {
            context.commit('changeDisabledAll', bo)
        },
        updateStartNodeEvent: function (context, data) {
            context.commit('changeStartNodeEvent', data)
        },
        updateSequenceFlowData: function (context, data) {
            context.commit('changeSequenceFlowData', data);
        },
        updateGNodeTypes: function (context,data) {
            context.commit('changeGNodeTypes', data)
        },
        updateHasCondition: function (context, data) {
            context.commit('changeHasCondition', data)
        }
    }
})

