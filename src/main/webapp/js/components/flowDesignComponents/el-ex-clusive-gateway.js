var ElExclusiveGateway = Vue.component('ElExclusiveGateway', {
    template: '<div class="flow-design-form-container"><div class="fdf-title">编辑 {{flowNodeTitleName}} 节点</div>' +
    '<div class="flow-design-form-wrap"><el-form :model="flowNodeForm" :rules="flowNodeRules" ref="flowNodeForm" size="mini" label-width="90px">' +
    '<el-form-item prop="name" label="节点名称："><div class="el-form-item-content_static">{{flowNodeForm.typeName}}</div><el-input v-if="false" name="name" v-model="flowNodeForm.name" @change="handleChangeName"></el-input></el-form-item>' +
    '<el-form-item label="节点类型："><div class="el-form-item-content_static">{{flowNodeForm.typeName}}</div></el-form-item>' +
    '<el-form-item><span slot="label"><el-tooltip class="item" effect="dark" content="根据筛选表单类型，该列表只显示单一类型的表单" placement="left"><span>筛选表单：</span></el-tooltip></span><div class="el-form-item-content_static">{{fltSourceType}}</div></el-form-item>' +
    '<el-form-item  prop="gstatusTtype"><span slot="label"><el-tooltip class="item" effect="dark" content="流程审核时，有那几种审核结果分类（如：立项审核结果有,审核通过-即通过，审核不通过-即不通过）" placement="left"><span>判断类型：</span></el-tooltip></span>' +
    '<el-select class="w160" clearable v-model="flowNodeForm.gstatusTtype" @change="handleChangeStatusList"><el-option v-for="item in ftNodeStatusTypes" :label="item.name" :value="item.id" :key="item.id"></el-option></el-select><el-button icon="el-icon-plus" @click.stop="openPdDialog"></el-button></el-form-item>' +
    '<el-form-item prop="statusListIdList"><span slot="label"><el-tooltip class="item" effect="dark" content="表示审核结果的具体项（如：立项审核结果有,通过、不通过）" placement="left"><span>判断条件：</span></el-tooltip></span>' +
    '<el-select :disabled="isDealing" class="w160" multiple v-model="flowNodeForm.statusListIdList" @change="delStatusId"><el-option v-for="item in statusListIdList" :label="item.state" disabled :value="item.id" :key="item.id"></el-option></el-select>' +
    '<el-tooltip class="item" effect="dark" content="请先选择判断类型" :disabled="!!flowNodeForm.gstatusTtype" placement="top"><span><el-button icon="el-icon-plus" :disabled="!flowNodeForm.gstatusTtype" @click.stop="openConditionDialog"></el-button></span></el-tooltip></el-form-item>' +
    '<el-form-item v-if="false" prop="classIdList"><span slot="label"><el-tooltip class="item" effect="dark" content="某一审核处需要使用之前的某一审核结果自动去判断处理审核业务" placement="left"><span>全局变量：</span></el-tooltip></span>' +
    '<el-select class="w160" clearable multiple v-model="flowNodeForm.classIdList" @change="handleChangeClassIdList"><el-option v-for="item in jtClazzIds" :label="item.remarks" :value="item.id" :key="item.id"></el-option></el-select></el-form-item>' +
    '<el-form-item prop="isGval" label="全局变量："><el-radio-group v-model="flowNodeForm.isGval" @change="handleChangeRemark"><el-radio label="1">是</el-radio><el-radio label="0">否</el-radio></el-radio-group></el-form-item><el-form-item prop="remarks" label="备注："><el-input name="remarks" :rows="3" type="textarea" v-model="flowNodeForm.remarks" @change="handleChangeRemark"></el-input></el-form-item></el-form></div>' +
    '<el-dialog title="添加判定类型" :visible.sync="dialogVisiblePdRegType" width="520px" :close-on-click-modal="false" :before-close="handleClosePdRegType">' +
    '<el-form :model="pdForm" ref="pdForm" size="mini" :rules="pdFormRules" :disabled="disabled" label-width="120px" autocomplete="off">' +
    '<input type="text" style="display:none"><el-form-item label="判定类型名称：" prop="name"><el-input v-model="pdForm.name" maxlength="64"></el-input></el-form-item>' +
    '<el-form-item label="网关类型：" prop="regType"><el-select v-model="pdForm.regType" clearable><el-option v-for="item in regTypes" :key="item.id" :value="item.id" :label="item.name"></el-option></el-select></el-form-item>' +
    '</el-form>' +
    '<span slot="footer" class="dialog-footer"> <el-button type="primary" size="mini" @click="validatePdForm">确 定</el-button></span></el-dialog>' +
    '<el-dialog title="添加条件状态" :visible.sync="dialogVisibleCondition" width="520px" :close-on-click-modal="false" :before-close="handleCloseCondition">' +
    '<el-form :model="conditionForm" :disabled="conditionDisabled" ref="conditionForm" :rules="conditionFormRules" size="mini" label-width="120px" autocomplete="off">' +
    '<input type="text" style="display:none"><el-form-item label="条件类型：" required>{{nodeStatusType.name}}</el-form-item>' +
    '<el-form-item prop="state" label="名称："><el-input v-model="conditionForm.state" maxlength="10"></el-input></el-form-item>' +
    '<el-form-item v-if="conditionForm.regtype == 2" prop="conditionValue" label="范围：">' +
    '<template v-if="conditionValue !== 100"><el-input-number v-model="conditionForm.conditionValue" :min="minConditionValue" :max="100" :step="1"></el-input-number><span class="el-form-item-expository">{{conditionForm.startNum}}-{{conditionForm.conditionValue}}</span></template><template v-else><span>没有可添加的范围，请删除最大值为100的条件</span></template></el-form-item>' +
    '<el-form-item label="备注："><el-input type="textarea" :rows="2" maxlength="255" v-model="conditionForm.remarks"></el-input></el-form-item>' +
    '</el-form><span slot="footer" class="dialog-footer"> <el-button :disabled="conditionDisabled" type="primary" size="mini" @click="validateConditionForm">确 定</el-button></span></el-dialog></div>',
    name: 'ElExclusiveGateway',
    data: function () {
        return {
            flowNodeForm: {
                id: '',
                name: '',
                typeName: '',
                isShow: 1,
                isGval: '0',
                formIds: [],
                statusIds: [],
                statusListIdList: [],
                iconUrl: '',
                nodeKey: '',
                parentId: '',
                type: '',
                nodeId: '',
                gstatusTtype: '',
                remarks: '',
                clazzIds: [],
                classIdList: [],
                level: '',
                childShapes: [],
                outgoing: []
            },
            flowNodeRules: {
                gstatusTtype: [{
                    required: true, message: '请选择判断类型', trigger: 'change'
                }],
                statusListIdList: [{
                    required: true, message: '请选择判断条件', trigger: 'change'
                }]
            },
            isDealing: false,
            statusListIdList: [],
            dialogVisiblePdRegType: false,
            disabled: false,
            pdForm: {
                name: '',
                regType: '',
            },
            pdFormRules: {
                name: [{
                    required: true, message: '请填写判定类型名称', trigger: 'blur'
                }],
                regType: [{
                    required: true, message: '请选择判断类型', trigger: 'change'
                }]
            },

            dialogVisibleCondition: false,
            conditionForm: {
                state: '',
                startNum: 0,
                conditionValue: 60,
                alias: '',
                remarks: '',
                gtypeName: '',
                regtype: '',
                gtype: '' //
            },
            conditionDisabled: false,
            conditionFormRules: {}
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

        regTypes: function () {
            return this.$store.state.regTypes;
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

        nodeStatusTypes: function () {
            return this.$store.state.nodeStatusTypes
        },

        ftNodeStatusTypes: function () {
            var nodeStatusTypes = this.nodeStatusTypes;
            var sourceNode = this.sourceNode || {};
            var regType = sourceNode.regType || '';
            return nodeStatusTypes.filter(function (item) {
                return item.regType === regType || regType === '999';
            })
        },

        jtClazzIds: function () {
            return this.$store.state.jtClazzIds
        },

        sourceNode: function () {
            return this.$store.state.sourceNode
        },

        sourceTypeName: function () {
            var regTypes = this.regTypes;
            var sourceNode = this.sourceNode || {};
            var i = 0;
            while (i < regTypes.length) {
                if (regTypes[i].id === sourceNode.regType) {
                    return regTypes[i].name;
                }
                i++;
            }
            return ''
        },

        fltSourceType: function () {
            var sourceNode = this.sourceNode || {};
            return this.sourceTypeName || '请选择' + (sourceNode.name || '') + '审核类型用于选择判定类型'
        },

        nodeStatusType: {
            get: function () {
                var nodeStatusTypes = this.nodeStatusTypes;
                var gstatusTtype = this.flowNodeForm.gstatusTtype;
                if (!gstatusTtype) return {};
                var i = 0;
                while (i < nodeStatusTypes.length) {
                    if (nodeStatusTypes[i].id === gstatusTtype) {
                        return nodeStatusTypes[i]
                    }
                    i++;
                }
                return {};
            },
            set: function () {
                this.conditionForm.regtype = this.nodeStatusType.regType;
            }
        },

        startNum: function () {
            var statusIds = this.flowNodeForm.statusIds;
            var arr = [];
            if (!statusIds.length) {
                return 0;
            }
            statusIds.forEach(function (item) {
                if (item.alias) {
                    arr = arr.concat(item.alias.split('-'));
                }
            });
            if (arr.length === 0) {
                return 0
            }
            return Math.max.apply(Math, arr);
        },

        conditionValue: function () {
            return Math.floor(this.startNum + (100 - this.startNum) / 2)
        },
        minConditionValue: function () {
            return this.startNum + 1
        }
    },
    watch: {
        'flowNode.id': function (value) {
            if (value !== this.flowNodeForm.id) {
                this.resetFlowNodeForm();
                Object.assign(this.flowNodeForm, this.flowNode);
                this.flowNodeForm.classIdList = this.flowNodeForm.clazzIds.map(function (item) {
                    return item.id;
                });
                this.getQueryStatues(this.flowNodeForm.gstatusTtype);
            }
        }
    },
    methods: {
        handleChangeName: function () {
            this.$store.dispatch('changeFlowNodeList', {
                type: 'updateFlowNodeList',
                node: this.getPureFlowNodeForm(),
                index: this.flowNodeIndex
            });
            gBusEvent.$emit('handleChangeName', this.flowNodeForm)
        },

        handleClosePdRegType: function () {
            this.dialogVisiblePdRegType = false;
        },

        openPdDialog: function () {
            this.dialogVisiblePdRegType = true;
            this.pdForm.regType = '';
            this.pdForm.name = '';
        },

        validatePdForm: function () {
            var self = this;
            this.$refs.pdForm.validate(function (valid) {
                if (valid) {
                    self.submitPdForm();
                }
            })
        },

        submitPdForm: function () {
            var self = this;
            this.disabled = false;
            this.$axios.post('/actyw/actYwSgtype/ajaxSave',Object.toURLSearchParams(this.pdForm)).then(function (response) {
                var data = response.data;
                if (data.ret === 1) {
                    self.handleClosePdRegType();
                    self.getGStates();
                    self.$message({
                        type: 'success',
                        message: data.msg
                    })
                } else {
                    self.$message({
                        type: 'error',
                        message: data.msg
                    })
                }
                self.disabled = false;
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

        handleChangeStatusList: function (type) {
            if (!type) {
                this.flowNodeForm.statusListIdList = [];
                this.flowNodeForm.statusIds = [];
                this.conditionForm.gtype = '';
            } else {
                this.getQueryStatues(type);
                this.conditionForm.gtype = this.flowNodeForm.gstatusTtype;
            }
            this.$store.dispatch('changeFlowNodeList', {
                type: 'updateFlowNodeList',
                node: this.getPureFlowNodeForm(),
                index: this.flowNodeIndex
            });
            gBusEvent.$emit('handleChangeStatusList', this.flowNodeForm.id);
        },

        //监听事件
        handleChangeClassIdList: function (value) {
            this.flowNodeForm.clazzIds = this.jtClazzIds.filter(function (item) {
                return value.indexOf(item.id) > -1;
            });
            this.$store.dispatch('changeFlowNodeList', {
                type: 'updateFlowNodeList',
                node: this.getPureFlowNodeForm(),
                index: this.flowNodeIndex
            });
        },

        handleChangeRemark: function () {
            this.$store.dispatch('changeFlowNodeList', {
                type: 'updateFlowNodeList',
                node: this.getPureFlowNodeForm(),
                index: this.flowNodeIndex
            });
        },

        getQueryStatues: function (type) {
            var self = this;
            if (!type) return;
            this.$axios.get('/actyw/gnode/ajaxQueryStates?gtype=' + type).then(function (response) {
                var data = response.data;
                if (data) {
                    self.flowNodeForm.statusIds = data;
                    self.statusListIdList = data;
                    self.flowNodeForm.statusListIdList = data.map(function (item) {
                        return item.id;
                    });
                    self.$store.dispatch('changeFlowNodeList', {
                        type: 'updateFlowNodeList',
                        node: self.getPureFlowNodeForm(),
                        index: self.flowNodeIndex
                    });
                }
            })
        },

        delStatusId: function (value) {
            var self = this;
            var statusListIdList = this.statusListIdList;
            var params = {
                gnodeId: this.flowNodeForm.id,
                statusId: ''
            };
            if (value && value.length === 0) {
                params['statusId'] = statusListIdList[0].id;
            } else {
                for (var i = 0; i < statusListIdList.length; i++) {
                    if (value && value.length > 0 && value.indexOf(statusListIdList[i].id) === -1) {
                        params['statusId'] = statusListIdList[i].id;
                        break;
                    }
                }
            }
            this.$axios.post('/actyw/actYwStatus/deleteStatus', Object.toURLSearchParams(params)).then(function (response) {
                var data = response.data;
                if (data.res === '1') {
                    self.handleChangeStatusList(self.flowNodeForm.gstatusTtype);
                    self.$message({
                        type: 'success',
                        message: data.msg
                    })
                } else {
                    self.flowNodeForm.statusListIdList = statusListIdList.map(function (item) {
                        return item.id;
                    });
                    self.$message({
                        type: 'error',
                        message: data.msg
                    })
                }
            })
        },

        openConditionDialog: function () {
            this.dialogVisibleCondition = true;
            this.conditionForm.alias = '';
            this.conditionForm.remarks = '';
            this.conditionForm.state = '';
            this.conditionForm.gtype = this.flowNodeForm.gstatusTtype;
            this.conditionForm.regtype = this.nodeStatusType.regType;
            this.conditionForm.startNum = this.startNum;
            this.conditionForm.conditionValue = this.conditionValue;
        },

        handleCloseCondition: function () {
            this.dialogVisibleCondition = false;
        },

        validateConditionForm: function () {
            var self = this;
            this.$refs.conditionForm.validate(function (valid) {
                if (valid) {
                    self.submitConditionForm();
                }
            })
        },

        submitConditionForm: function () {
            var self = this;
            var params = this.getConditionParams();
            this.conditionDisabled = true;
            this.$axios.post('/actyw/actYwStatus/saveCondition', Object.toURLSearchParams(params)).then(function (response) {
                var data = response.data;
                if (data.res === 1) {
                    self.$message({
                        type: 'success',
                        message: data.msg
                    });
                    self.getQueryStatues(self.flowNodeForm.gstatusTtype);
                    self.dialogVisibleCondition = false;
                } else {
                    self.$message({
                        type: 'error',
                        message: data.msg
                    })
                }
                self.conditionDisabled = false;
            })
        },

        getConditionParams: function () {
            var params = {
                state: this.conditionForm.state,
                remarks: this.conditionForm.remarks,
                regtype: this.conditionForm.regtype,
                gtype: this.conditionForm.gtype //
            };
            if (params.regtype === '2') {
                params['alias'] = [this.conditionForm.startNum, this.conditionForm.conditionValue].join('-')
            }
            return params;
        },


        getPureFlowNodeForm: function () {
            var flowNodeForm = JSON.parse(JSON.stringify(this.flowNodeForm));
            delete flowNodeForm.statusListIdList;
            delete flowNodeForm.classIdList;
            return flowNodeForm;
        },

        resetFlowNodeForm: function () {
            this.flowNodeForm = {
                id: '',
                name: '',
                typeName: '',
                isShow: 1,
                formIds: [],
                statusIds: [],
                statusListIdList: [],
                iconUrl: '',
                nodeKey: '',
                parentId: '',
                type: '',
                nodeId: '',
                gstatusTtype: '',
                remarks: '',
                clazzIds: [],
                classIdList: [],
                level: '',
                childShapes: [],
                outgoing: []
            }
        }
    },


    created: function () {
        Object.assign(this.flowNodeForm, this.flowNode);
        this.flowNodeForm.classIdList = this.flowNodeForm.clazzIds.map(function (item) {
            return item.id;
        });
        this.getQueryStatues(this.flowNodeForm.gstatusTtype);
    }
})


