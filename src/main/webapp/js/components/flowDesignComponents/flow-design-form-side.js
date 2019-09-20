

Vue.component('flow-design-form-side', {
    template: '<div class="flow-design-form-side"><component :is="componentName" :key="keyId"></component></div>',
    components: {
        ElStartNode: ElStartNode,
        ElProcessNode: ElProcessNode,
        ElUserTask: ElUserTask,
        ElExclusiveGateway: ElExclusiveGateway,
        ElEndNoneEvent: ElEndNoneEvent,
        ElEndTerminateEvent: ElEndTerminateEvent,
        ElSequenceFlow: ElSequenceFlow
    },
    computed: {
        componentName: function () {
            var curFlowNode = this.$store.state.curFlowNode;
            if (curFlowNode.node) {
                return FlowDesignConfig.componentNames[curFlowNode.node.nodeKey]
            }
            return null;
        },
        keyId: function () {
            var curFlowNode = this.$store.state.curFlowNode;
            if (curFlowNode.node) {
                return curFlowNode.node.id;
            }
            return ''
        },
        proType: function () {
            return this.$root.proType
        },
        flowType: function () {
            return this.$root.flowType
        },
        theme: function () {
            return this.$root.theme
        }
    },
    methods: {
        //获取列表表单
        getFormList: function () {
            var self = this;
            this.$axios.get('/actyw/gnode/ajaxGnodeForm/'+this.flowType+'?theme='+this.theme+'&hasList=true&proType='+this.proType).then(function (response) {
                var data = response.data || [];
                data = data.filter(function (item) {
                    return item.listId != '';
                });
                self.$store.dispatch('setFormList', data);
            })
        },
        //获取任务列表
        getTaskTypes: function () {
            var self = this;
            this.$axios.get('/actyw/gnode/ajaxGnodeTaskTypes').then(function (response) {
                var data = response.data;
                if (data.status) {
                    self.$store.dispatch('setTaskTypes', JSON.parse(data.datas));
                }
            })
        },

        //获取角色列表
        getRoleList: function () {
            var self = this;
            this.$axios.get('/actyw/gnode/ajaxGnodeRole').then(function (response) {
                var data = response.data;
                if (data && data.length > 0) {
                    var roles = data.map(function (item) {
                        if (item.name === '学生/导师') {
                            item.name = '学生'
                        }
                        return item;
                    });
                    // roles = roles.filter(function (item) {
                    //     return item.name.indexOf('导师') === -1;
                    // })
                    self.$store.dispatch('setUserRoles', roles);
                }
            })
        },
        //获取审核类型列表
        getRegTypes: function () {
            var self = this;
            this.$axios.get('/actyw/gnode/ajaxRegTypes?isAll=true').then(function (response) {
                var data = response.data;
                if (data.status) {
                    self.$store.dispatch('setRegTypes', JSON.parse(data.datas));
                }
            })
        },
        //获取审核表单
        getShForms: function () {
            var self = this;
            this.$axios.get('/actyw/gnode/ajaxGnodeForm/'+this.flowType+'?theme='+this.theme+'&hasList=false&proType='+this.proType).then(function (response) {
                var data = response.data;
                if (data && data.length > 0) {
                    data.forEach(function (t) {
                        t.name = t.name.replace(/项目流程\/|大赛流程\//, '');
                    });
                    self.$store.dispatch('setShForms', data);
                }
            })
        },
        //获取判定类型
        getGStates: function () {
            var self = this;
            this.$axios.get('/actyw/gnode/ajaxDictStatusType').then(function (response) {
                var data = response.data;
                if (data.status) {
                    self.$store.dispatch('setNodeStatusTypes', data.datas);
                }
            })
        },

        //获取监听事件

        getJtClassIds: function () {
            var self = this;
            this.$axios.get('/actyw/gnode/ajaxQueryClazz?theme='+this.theme).then(function (response) {
                var data = response.data;
                if (data.status) {
                    self.$store.dispatch('setJtClazzIds', data.datas);
                }
            })
        },

        getGnodeTypes: function () {
            var self = this;
            this.$axios.get('/actyw/gnode/ajaxGnodeTypes').then(function (response) {
                var data = response.data;
                if (data.status) {
                    self.$store.dispatch('updateGNodeTypes', JSON.parse(data.datas));
                }
            })
        }
    },
    mounted: function () {
        this.getFormList();
        this.getTaskTypes();
        this.getRoleList();
        this.getRegTypes();
        this.getShForms();
        this.getGStates();
        this.getJtClassIds();
        this.getGnodeTypes();
    }
})