
Vue.component('fl-according', {
    template: '<div class="fl-according"><slot></slot></div>',
    componentName: 'FlAccording',
    model: {
        prop: 'name',
        event: 'change'
    },
    props: {
        name: [String, Number, Array]
    },
    data: function () {
        return {
            items: []
        }
    },
    methods: {
        updateItems: function () {
            this.items = this.$children.filter(function (child) {
                return child.$options.name === 'FlAccordingItem'
            })
        },
        updateName: function (value) {
            this.$emit('change', value)
        }
    },
    mounted: function () {
    }
})

Vue.component('fl-according-item', {
    template: '<div class="fl-according-item"><div class="fl-according-item-header">{{label}}<span class="fl-ac-control" :class="{open: isShow}" @click.stop="handleChangeIsShow"><i class="el-icon-arrow-right"></i></span></div>' +
    '<transition  name="fl-transition" tag="div">' +
    '<div class="fl-according-item-wrapper" :style="style"><div ref="flAccordingItemContent"  class="fl-according-item-content"><slot></slot></div></div></transition></div>',
    componentName: 'FlAccordingItem',
    props: {
        label: String,
        name: [String, Number]
    },
    data: function () {
      return {
          height: '',
          realHeight: ''
      }
    },
    computed: {
        isShow: function () {
            return [].concat(this.$parent.name).indexOf(this.name) > -1;
        },
        style: function () {
            return {
                height: (!this.isShow ? '0px' : '')
            }
        }
    },
    methods: {
        handleChangeIsShow: function () {
            var names = this.$parent.name;
            if(this.isShow){
                var index = this.$parent.name.indexOf(this.name);
                names.splice(index, 1);
            }else {
                names.push(this.name)
            }
            this.$parent.updateName(names);
        },
    },
    created: function () {
        this.$parent && this.$parent.updateItems();
    },
    mounted: function () {
        this.realHeight = this.$refs.flAccordingItemContent.offsetHeight;
    },
    destroy: function () {
        this.$parent && this.$parent.updateItems();
    }
})

Vue.component('aside-nodes', {
    template: '<div class="aside-nodes"><fl-according v-model="activeName">' +
    '<fl-according-item label="任务节点" name="1"><el-svg-task-node v-for="item in taskNodes" :key="item.id" :node-data="item"></el-svg-task-node></fl-according-item>' +
    '<fl-according-item label="子流程" name="2"><el-svg-task-node v-for="item in processNodes" :key="item.id" :node-data="item"></el-svg-task-node></fl-according-item>' +
    '<fl-according-item label="网关" name="3"><el-svg-task-node v-for="item in gatewayNodes" :key="item.id" :node-data="item"></el-svg-task-node></fl-according-item></fl-according>' +
    '</div>',
    data: function () {
        return {
            taskNodes: [],
            processNodes: [],
            gatewayNodes: [],
            lineDefaultData: {},
            activeName: ['1','2','3']
        }
    },
    methods: {
        getActYwNodes: function () {
            var self = this;
            this.$axios.get('/actyw/actYwNode/query?isVisible=true').then(function (response) {
                var data = response.data;
                var list = data.datas || [];
                var taskNodes = [];
                if (data.status) {
                    list.forEach(function (item) {
                        if (item.uiJson) {
                            var type = parseInt(item.type);
                            if (FlowDesignConfig.taskNodes.indexOf(type) > -1) {
                                item.tSort = FlowDesignConfig.taskNodeSorts.indexOf(item.nodeKey);
                                taskNodes.push(item)
                            } else if (FlowDesignConfig.processNodes.indexOf(type) > -1) {
                                self.processNodes.push(item)
                            } else if (FlowDesignConfig.gatewayNodes.indexOf(type) > -1) {
                                self.gatewayNodes.push(item)
                            }
                        } else {
                            if (item.nodeType === 'edge') {
                                self.$store.dispatch('updateSequenceFlowData', item)
                            }
                        }
                    })
                    taskNodes.sort(function (item1, item2) {
                        return item1.tSort - item2.tSort;
                    });
                    self.taskNodes = taskNodes;
                }
            })
        }
    },
    created: function () {
        this.getActYwNodes();
    }
})