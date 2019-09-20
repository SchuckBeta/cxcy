var ElProcessNode = Vue.component('el-process-node', {
    template: '<div class="flow-design-form-container"><div class="fdf-title">编辑 {{flowNodeTitleName}} 节点</div><div class="flow-design-form-wrap">' +
    '<el-form :model="flowNodeForm" :rules="flowNodeRules" ref="flowNodeForm" size="mini" label-width="90px">' +
    '<el-form-item prop="name" label="节点名称："><el-input name="name" v-model="flowNodeForm.name" maxlength="16" @change="handleChangeName"></el-input></el-form-item>' +
    '<el-form-item label="节点类型："><div class="el-form-item-content_static">{{flowNodeForm.typeName}}</div></el-form-item>' +
    '<el-form-item prop="formListId"><span slot="label"><el-tooltip class="item" effect="dark" content="根据筛选表单类型，该列表只显示单一类型的表单" placement="left"><span>列表表单：</span></el-tooltip></span>' +
    '<el-select v-model="flowNodeForm.formListId" @change="handleChangeFormListId"><el-option v-for="item in formList" :label="item.name" :value="item.id" :key="item.id"></el-option></el-select></el-form-item>' +
    '<el-form-item prop="remarks" label="备注："><el-input name="remarks" :rows="3" maxlength="125" type="textarea" v-model="flowNodeForm.remarks" @change="handleChangeRemark"></el-input></el-form-item></el-form></div></div>',
    data: function () {
        return {
            flowNodeForm: {
                id: '',
                name: '',
                isShow: 1,
                preIds: [],
                iconUrl: '',
                nodeId: '',
                parentId: '',
                nodeKey: '',
                type: '',
                statusIds: [],
                typeName: '',
                remarks: '',
                childShapes: [],
                outgoing: [],
                formListId: '',
                level: ''
            },
            flowNodeRules: {
                name: [{
                    required: true, message: '请填写节点名称', trigger: 'blur'
                }],
                formListId: [{required: true, message: '请选择列表表单', trigger: 'change'}]
            }
        }
    },
    name: 'ElProcessNode',
    watch: {
        'flowNode.id': function (value) {
            if (value !== this.flowNodeForm.id) {
                this.resetFlowNodeForm();
                Object.assign(this.flowNodeForm, this.flowNode);
                this.setFormListId();
            }
        }
    },

    computed: {

        formList: function () {
            return this.$store.state.formList;
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
        }
    },
    methods: {
        handleChangeName: function () {
            this.$store.dispatch('changeFlowNodeList', {
                type: 'updateFlowNodeList',
                node: JSON.parse(JSON.stringify(this.flowNodeForm)),
                index: this.flowNodeIndex
            });
            gBusEvent.$emit('handleChangeName', this.flowNodeForm)
        },

        handleChangeRemark: function () {
            this.$store.dispatch('changeFlowNodeList', {
                type: 'updateFlowNodeList',
                node: JSON.parse(JSON.stringify(this.flowNodeForm)),
                index: this.flowNodeIndex
            });
        },

        handleChangeFormListId: function (value) {
            this.flowNodeForm.formIds = this.formList.filter(function (item) {
                return item.id === value
            })
            this.$store.dispatch('changeFlowNodeList', {
                type: 'updateFlowNodeList',
                node: this.getPureFlowNodeForm(),
                index: this.flowNodeIndex
            });
        },

        setFormListId: function () {
            var formIds = this.flowNodeForm.formIds;
            if (formIds && formIds.length > 0) {
                this.flowNodeForm.formListId = formIds[0].id;
            } else {
                this.flowNodeForm.formListId = '';
            }
        },

        getPureFlowNodeForm: function () {
            var flowNodeForm = JSON.parse(JSON.stringify(this.flowNodeForm));
            delete flowNodeForm.formListId;
            return flowNodeForm;
        },

        resetFlowNodeForm: function () {
            this.flowNodeForm = {
                id: '',
                name: '',
                isShow: 1,
                preIds: [],
                iconUrl: '',
                nodeId: '',
                parentId: '',
                nodeKey: '',
                type: '',
                statusIds: [],
                typeName: '',
                remarks: '',
                childShapes: [],
                outgoing: [],
                formListId: '',
                level: ''
            }
        }
    },
    created: function () {
        Object.assign(this.flowNodeForm, this.flowNode);
        this.setFormListId();
    }
})

