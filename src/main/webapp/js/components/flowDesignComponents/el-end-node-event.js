


var ElEndNoneEvent = Vue.component('el-end-node-event', {
    template: '<div class="flow-design-form-container"><div class="fdf-title">编辑 {{flowNodeTitleName}} 节点</div><div class="flow-design-form-wrap">' +
    '<el-form :model="flowNodeForm" :rules="flowNodeRules" ref="flowNodeForm" size="mini" label-width="90px">' +
    '<el-form-item prop="name" label="节点名称："><div class="el-form-item-content_static">{{flowNodeForm.name}}</div><el-input v-if="false" name="name" maxlength="24" v-model="flowNodeForm.name" @change="handleChangeName"></el-input></el-form-item>' +
    '<el-form-item label="节点类型："><div class="el-form-item-content_static">{{flowNodeForm.typeName}}</div></el-form-item>' +
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
                outgoing: [],
                level: '',
                isShow: 1
            },
            flowNodeRules: {
                // name: [{
                //     required: true, message: '请填写节点名称', trigger: 'blur'
                // }]
            }
        }
    },
    name: 'ElEndNoneEvent',
    watch: {
        'flowNode.id': function (value) {
            if(value !== this.flowNodeForm.id){
                this.flowNodeForm.remarks = '';
                Object.assign(this.flowNodeForm, this.flowNode);
            }
        }
    },

    computed: {

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
        }
    },
    methods: {
        handleChangeName: function () {
            this.$store.dispatch('changeFlowNodeList', {
                type: 'updateFlowNodeList',
                node: JSON.parse(JSON.stringify(this.flowNodeForm)),
                index: this.flowNodeIndex
            });
            gBusEvent.$emit('handleChangeName', this.flowNodeForm.name)
        },

        handleChangeRemark: function () {
            this.$store.dispatch('changeFlowNodeList', {
                type: 'updateFlowNodeList',
                node: JSON.parse(JSON.stringify(this.flowNodeForm)),
                index: this.flowNodeIndex
            });
        }
    },
    created: function () {
        Object.assign(this.flowNodeForm, this.flowNode);
    }
})

