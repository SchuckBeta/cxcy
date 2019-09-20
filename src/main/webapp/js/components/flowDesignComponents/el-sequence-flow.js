var ElSequenceFlow = Vue.component('el-sequence-flow', {
    template: '<div class="flow-design-form-container"><div class="fdf-title">编辑 {{flowNodeTitleName}} 节点</div><div class="flow-design-form-wrap">' +
    '<el-form :model="flowNodeForm" :rules="flowNodeRules" ref="flowNodeForm" size="mini" label-width="90px">' +
    '<el-form-item prop="name" label="节点名称："><div class="el-form-item-content_static">{{flowNodeForm.name}}</div></el-form-item>' +
    '<el-form-item label="节点类型："><div class="el-form-item-content_static">{{flowNodeForm.typeName}}</div></el-form-item>' +
    '<el-form-item v-if="hasCondition" prop="statusIdList" label="条件：">' +
    '<el-select v-show="showCd" multiple v-model="flowNodeForm.statusIdList" @change="handleChangeStatusId">' +
    '<el-option v-for="item in pathConditionsAll" :disabled="localPathCon.indexOf(item.id)===-1" :key="item.id" :label="item.state" :value="item.id"><span style="float: left">{{ item.state }}</span><span style="float: right; color: #8492a6; font-size: 13px">{{ item.alias || \'-\' }}</span></el-option></el-select>' +
    '<span v-show="!showCd">请选择网关判定类型</span></el-form-item>' +
    '<el-form-item prop="remarks" label="备注："><el-input name="remarks" :rows="3" maxlength="125" type="textarea" v-model="flowNodeForm.remarks" @change="handleChangeRemark"></el-input></el-form-item></el-form></div></div>',
    data: function () {
        return {
            flowNodeForm: {
                id: '',
                name: '',
                typeName: '',
                parentId: '',
                nodeId: '',
                nodeKey: '',
                type: '',
                remarks: '',
                preIds: [],
                statusIds: [],
                statusIdList: [],
                outgoing: [],
                preFunId: '',
                level: '',
                isShow: false,
            },
            localPathCon: []
        }
    },
    name: 'ElSequenceFlow',
    watch: {
        'flowNode.id': function (value) {
            if (value !== this.flowNodeForm.id) {
                this.resetFlowNodeForm();
                Object.assign(this.flowNodeForm, this.flowNode);
                this.flowNodeForm.statusIdList = this.flowNodeForm.statusIds.map(function (item) {
                    return item.id;
                });
                this.localPathCon = [].concat(this.pathConditions, this.flowNodeForm.statusIdList)
            }
        },
        'flowNodeForm.statusIdList': function (val, oldVal) {
            var localPathCon = this.localPathCon;
            for (var i = 0; i < oldVal.length; i++) {
                if (val.indexOf(oldVal[i]) === -1) {
                    if (localPathCon.indexOf(oldVal[i]) === -1) {
                        var arr = [].concat(this.pathConditionsAll.filter(function (item) {
                            return item.id === oldVal[i] || localPathCon.indexOf(item.id) > -1;
                        }));
                        this.$store.dispatch('setPathConditions', arr);
                        break;
                    }
                }
            }
        }
    },

    computed: {

        flowNodeRules: function () {
            var hasCondition = this.hasCondition;
            return {
                statusIdList: [
                    {required: hasCondition, message: '请选择条件', trigger: 'change'}
                ]
            }
        },
        flowNodeList: function () {
          return this.$store.state.flowNodeList;
        },
        flowNodeTitleName: function () {
            if(this.flowNodeIndex != -1){
                return this.flowNodeList[this.flowNodeIndex].name;
            }
            return ''
        },

        flowNode: function () {
            return this.$store.state.curFlowNode.node;
        },
        flowNodeIndex: function () {
            var flowNodeList = this.flowNodeList;
            var index = -1;
            for (var i = 0; i < flowNodeList.length; i++) {
                if (this.flowNode.id === flowNodeList[i].id) {
                    index = i;
                    break;
                }
            }
            return index;
        },
        snapSvgPaper: function () {
            return this.$store.state.snapSvgPaper
        },

        pathConditions: function () {
            var ids = this.$store.state.pathConditions.map(function (item) {
                return item.id
            });
            return [].concat(ids)
        },
        pathConditionsAll: function () {
            return this.$store.state.pathConditionsAll;
        },

        hasCondition: function () {
            return this.$store.state.hasCondition;
        },
        showCd: function () {
            var statusIds, conditions = this.pathConditionsAll;
            statusIds = this.flowNodeForm.statusIdList;
            if (statusIds) {
                return statusIds.length || conditions.length;
            }
            return false;
        }
    },
    methods: {
        handleChangeRemark: function () {
            this.$store.dispatch('changeFlowNodeList', {
                type: 'updateFlowNodeList',
                node: this.getPureFlowNodeForm(),
                index: this.flowNodeIndex
            });
        },

        handleChangeStatusId: function (value) {
            this.flowNodeForm.statusIds = this.pathConditionsAll.filter(function (item) {
                return value.indexOf(item.id) > -1;
            });
            this.$store.dispatch('changeFlowNodeList', {
                type: 'updateFlowNodeList',
                node: this.getPureFlowNodeForm(),
                index: this.flowNodeIndex
            });
            gBusEvent.$emit('handleChangePathStatus', this.flowNodeForm.statusIds)
        },

        getPureFlowNodeForm: function () {
            var flowNodeForm = JSON.parse(JSON.stringify(this.flowNodeForm));
            delete flowNodeForm.statusIdList;
            return flowNodeForm;
        },
        resetFlowNodeForm: function () {
            this.flowNodeForm = {
                id: '',
                name: '',
                typeName: '',
                isShow: 1,
                parentId: '',
                preFunId: '',
                nodeId: '',
                nodeKey: '',
                type: '',
                remarks: '',
                preIds: [],
                statusIds: [],
                statusIdList: [],
                outgoing: [],
                level: ''
            }
        }
    },
    created: function () {
        Object.assign(this.flowNodeForm, this.flowNode);
        this.flowNodeForm.statusIdList = this.flowNodeForm.statusIds.map(function (item) {
            return item.id;
        });
        this.localPathCon = [].concat(this.pathConditions, this.flowNodeForm.statusIdList)
    }
})

