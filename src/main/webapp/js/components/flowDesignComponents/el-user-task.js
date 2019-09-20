var ElUserTask = Vue.component('el-user-task', {
        template: '<div class="flow-design-form-container"><div class="fdf-title">编辑 {{flowNodeTitleName}} 节点</div><div class="flow-design-form-wrap">' +
        '<el-form :model="flowNodeForm" :rules="flowNodeRules" ref="flowNodeForm" size="mini" label-width="100px">' +
        '<el-form-item prop="name" label="节点名称："><el-input name="name" v-model="flowNodeForm.name" maxlength="16" @change="handleChangeName"></el-input></el-form-item>' +
        '<el-form-item label="节点类型："><div class="el-form-item-content_static">{{flowNodeForm.typeName}}</div></el-form-item>' +
        '<el-form-item v-show="!roleIsStudent" prop="taskType"><span slot="label"><el-tooltip class="item" effect="dark" content="并行：同时进行；签收：指定受理人" placement="left"><span>任务类型：</span></el-tooltip></span><el-select v-model="flowNodeForm.taskType" @change="handleChangeTaskType"><el-option v-for="item in taskTypes" :key="item.key" :value="item.key" :label="item.remark"></el-option></el-select></el-form-item>' +
        '<el-form-item v-show="!roleIsStudent" prop="regType"><span slot="label"><el-tooltip class="item" effect="dark" content="用于过滤审核表单（如.秘书审核）和评分（如.专家打分）" placement="left"><span>审核类型：</span></el-tooltip></span><el-select v-model="flowNodeForm.regType" @change="handleChangeRegType" clearable><el-option v-for="item in ftRegTypes" :key="item.id" :value="item.id" :label="item.name"></el-option></el-select></el-form-item>' +
        '<el-form-item prop="isAssign" label="指派专家：" v-if="isShowZp"><el-radio-group v-model="flowNodeForm.isAssign" @change="handleChangeIsAssign"><el-radio label="1">是</el-radio><el-radio label="0">否</el-radio></el-radio-group></el-form-item>' +
        '<el-form-item prop="roleIdList" v-if="hideRole" label="角色："><el-select multiple v-model="flowNodeForm.roleIdList" @change="handleChangeRole"><el-option v-for="item in ftUserRoles" :key="item.id" :value="item.id" :label="item.name"></el-option></el-select></el-form-item>' +
        '<el-form-item prop="isDelegate" label="委派专家评分：" v-if="isShowWp && flowNodeForm.regType == \'1\'"><el-radio-group v-model="flowNodeForm.isDelegate" @change="handleChangeIsDelegate"><el-radio label="1">是</el-radio><el-radio label="0">否</el-radio></el-radio-group></el-form-item>' +
        '<el-form-item prop="formNIdList"><span slot="label"><el-tooltip class="item" effect="dark" content="根据筛选表单类型，该列表只显示单一类型的表单" placement="left"><span>审核表单：</span></el-tooltip></span><el-select v-model="flowNodeForm.formNIdList" clearable placeholder="请先选择角色和审核类型" @change="handleChangeFormNIdList"><el-option v-for="item in ftShForms" :key="item.id" :value="item.id" :label="item.name"></el-option></el-select></el-form-item>' +
        '<el-form-item prop="remarks" label="备注："><el-input name="remarks" :rows="3" type="textarea" maxlength="125" v-model="flowNodeForm.remarks" @change="handleChangeRemark"></el-input></el-form-item>' +
        '</el-form></div></div>',
        name: 'ElUserTask',
        data: function () {
            var self = this;
            var validateRole = function (rules, value, callback) {
                if (!self.hideRole) {
                    return new callback();
                }
                if (!value) {
                    return new callback(new Error('请选择角色'));
                }
                return new callback();
            }
            return {
                flowNodeForm: {
                    id: '',
                    name: '',
                    isShow: 1,
                    typeName: '',
                    parentId: '',
                    taskType: 'None',
                    nodeId: '',
                    nodeKey: '',
                    type: '',
                    remarks: '',
                    roleIds: [],
                    roleIdList: [],
                    regType: '',
                    isAssign: '0',
                    isDelegate: '0',
                    dformIds: [],
                    droleIds: [],
                    formNlistIds: [],
                    formListIds: [],
                    formIds: [],
                    formNIdList: '',
                    preIds: [],
                    outgoing: [],
                    level: ''
                },
                taskTypesFilters: {
                    'None': ['1', '2'],
                    'Parallel': ['2']
                },
                hideTaskTypeRole: 'Parallel',
                hideRegType: '2',
                flowNodeRules: {
                    name: [{
                        required: true, message: '请填写节点名称', trigger: 'blur'
                    }],
                    roleIdList: [{
                        validator: validateRole, trigger: 'change'
                        // required: hideRole, message: '请选择角色', trigger: 'change'
                    }],
                    taskType: [{
                        required: true, message: '请选择任务类型', trigger: 'change'
                    }],
                    regType: [{
                        required: true, message: '请选择审核类型', trigger: 'change'
                    }],
                    formNIdList: [{
                        required: true, message: '请选择审核表单', trigger: 'change'
                    }],
                }
            }
        },

        computed: {
            isShowZp:function () {
                if(this.$root.isShowZp){
                    return this.hasIsAssign;
                }
                return false;
            },
            isShowWp:function () {
                return this.$root.isShowWp;
            },

            taskTypes: function () {
                return this.$store.state.taskTypes;
            },

            hasTeacherAudit: function () {
              return this.$root.hasTeacherAudit;
            },
            firstRole: function () {
                var i = 0;
                var roleIdList = this.flowNodeForm.roleIdList;
                var id;
                if (roleIdList.length > 0) {
                    id = roleIdList[0];
                }
                if (id) {
                    while (i < this.userRoles.length) {
                        if (this.userRoles[i].id === id) {
                            return this.userRoles[i]
                        }
                        i++;
                    }
                }
                return '';
            },

            userRoles: function () {
                var userRoles = this.$store.state.userRoles;
                if(this.hasTeacherAudit){
                    return userRoles;
                }
                return userRoles.filter(function (item) {
                    return item.id !== '21999752ae6049e2bc3d53e8baaac9a5'; //导师ID,写死
                })
            },

            ftUserRoles: function () {
                var userRoles = this.userRoles;
                var firstRole = this.firstRole;
                if (firstRole) {
                    return userRoles.filter(function (item) {
                        return item.roleGroup === firstRole.roleGroup;
                    })
                }
                return userRoles
            },

            regTypes: function () {
                return this.$store.state.regTypes;
            },

            ftRegTypes: function () {
                var taskTypesFilters = this.taskTypesFilters;
                var flowNodeForm = this.flowNodeForm;
                var regType = flowNodeForm.regType;
                return this.regTypes.filter(function (item) {
                    return taskTypesFilters[flowNodeForm.taskType].indexOf(item.id) > -1 || regType === item.id;
                })
            },

            shForms: function () {
                return this.$store.state.shForms;
            },

            ftShForms: function () {
                var regType = this.flowNodeForm.regType;
                return this.shForms.filter(function (item) {
                    return regType === item.sgtype;
                })
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
            flowNodeList: function () {
                return this.$store.state.flowNodeList;
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

            studentId: function () {
                return this.$root.studentId;
            },

            roleIsStudent: function () {
                return this.flowNodeForm.roleIdList.indexOf(this.studentId) > -1;
            },

            hideRole: function () {
                return !(this.flowNodeForm.taskType === this.hideTaskTypeRole && this.flowNodeForm.regType === this.hideRegType && this.flowNodeForm.isAssign === '1')
            },

            hasIsAssign: function () {
                return this.flowNodeForm.regType === '2';
            },
            expertId: function () {
                return this.$root.expertId;
            }
        },
        watch: {
            'flowNode.id': function (value) {
                if (value !== this.flowNodeForm.id) {
                    this.resetFlowForm();
                    this.$nextTick(function () {
                        Object.assign(this.flowNodeForm, this.flowNode);
                        // this.flowNodeForm.isAssign = this.flowNode.isAssign ? '1' : '0';
                        this.flowNodeForm.roleIdList = this.flowNodeForm.roleIds.map(function (item) {
                            return item.id;
                        });
                        if (this.flowNodeForm.formNlistIds.length > 0) {
                            this.flowNodeForm.formNIdList = this.flowNodeForm.formNlistIds[0].id;
                        }
                    })
                }
            },
            'roleIsStudent': function (value) {
                if (!value) {
                    this.flowNodeForm.regType = '';
                    this.flowNodeForm.formNIdList = '';
                }
            },
            // 'flowNodeForm.formNlistIds': function (value) {
            //     var formListIds = value.map(function (item) {
            //         return {id: item.listId}
            //     });
            //     this.flowNodeForm.formListIds = formListIds.filter(function (item) {
            //         return item.id != '';
            //     })
            //
            //     this.flowNodeForm.formIds = [].concat(this.flowNodeForm.formNlistIds, this.flowNodeForm.formListIds);
            //     this.flowNodeForm.formIds = this.flowNodeForm.formIds.filter(function (item) {
            //         return !!item.id;
            //     })
            //     this.$store.dispatch('changeFlowNodeList', {
            //         type: 'updateFlowNodeList',
            //         node: this.getPureFlowNodeForm(),
            //         index: this.flowNodeIndex
            //     });
            // },

            hideRole: function (value) {
                if (!value) {


                    // this.$store.dispatch('changeFlowNodeList', {
                    //     type: 'updateFlowNodeList',
                    //     node: this.getPureFlowNodeForm(),
                    //     index: this.flowNodeIndex
                    // });
                }
            },

        },
        methods: {

            handleChangeIsDelegate: function (value) {
                if(value == '1'){
                    if(this.flowNodeForm.droleIds.length == 0){
                        this.flowNodeForm.droleIds.push(this.$root.expertId);
                    }
                }else{
                    this.flowNodeForm.droleIds = [];
                }
                this.$store.dispatch('changeFlowNodeList', {
                    type: 'updateFlowNodeList',
                    node: this.getPureFlowNodeForm(),
                    index: this.flowNodeIndex
                });
            },

            handleChangeIsAssign: function (value) {
                this.$store.dispatch('changeFlowNodeList', {
                    type: 'updateFlowNodeList',
                    node: this.getPureFlowNodeForm(),
                    index: this.flowNodeIndex
                });
            },

            handleChangeName: function () {
                this.$store.dispatch('changeFlowNodeList', {
                    type: 'updateFlowNodeList',
                    node: this.getPureFlowNodeForm(),
                    index: this.flowNodeIndex
                });

                gBusEvent.$emit('handleChangeName', this.flowNodeForm)

            },

            handleChangeRemark: function () {
                this.$store.dispatch('changeFlowNodeList', {
                    type: 'updateFlowNodeList',
                    node: this.getPureFlowNodeForm(),
                    index: this.flowNodeIndex
                });
            },

            //修改审核类型
            handleChangeRegType: function (value) {
                this.flowNodeForm.formNIdList = '';
                this.flowNodeForm.formNlistIds = [];
                // this.flowNodeForm.formListIds = [];
                // this.flowNodeForm.formIds = [];
                if (this.flowNodeForm.taskType === this.hideTaskTypeRole && this.hideRegType === this.flowNodeForm.regType) {
                    var expertId = this.expertId;
                    this.flowNodeForm.roleIdList = [expertId];
                    this.flowNodeForm.roleIds = this.userRoles.filter(function (item) {
                        return expertId === item.id;
                    });
                    this.flowNodeForm.isAssign = '1';
                    gBusEvent.$emit('handleChangRole', this.flowNodeForm.roleIds);
                }else {
                    this.flowNodeForm.isAssign = '0';
                }
                this.$store.dispatch('changeFlowNodeList', {
                    type: 'updateFlowNodeList',
                    node: this.getPureFlowNodeForm(),
                    index: this.flowNodeIndex
                });
                gBusEvent.$emit('handleChangeRegType', this.flowNodeForm.id);

            },

            handleChangeTaskType: function () {
                if (this.flowNodeForm.taskType === this.hideTaskTypeRole && this.hideRegType === this.flowNodeForm.regType) {
                    var expertId = this.expertId;
                    this.flowNodeForm.roleIdList = [expertId];
                    this.flowNodeForm.roleIds = this.userRoles.filter(function (item) {
                        return expertId === item.id;
                    });
                    this.flowNodeForm.isAssign = '1';
                    gBusEvent.$emit('handleChangRole', this.flowNodeForm.roleIds);
                }else {
                    this.flowNodeForm.isAssign = '0';
                }
                this.flowNodeForm.formNIdList = '';

                this.$store.dispatch('changeFlowNodeList', {
                    type: 'updateFlowNodeList',
                    node: this.getPureFlowNodeForm(),
                    index: this.flowNodeIndex
                });
            },

            handleChangeRole: function (value) {
                this.flowNodeForm.roleIds = this.userRoles.filter(function (item) {
                    return value.indexOf(item.id) > -1;
                });

                if (this.roleIsStudent) {
                    this.flowNodeForm.regType = '999';
                    this.flowNodeForm.formNIdList = '';
                    this.handleChangeFormNIdList();
                }

                this.$store.dispatch('changeFlowNodeList', {
                    type: 'updateFlowNodeList',
                    node: this.getPureFlowNodeForm(),
                    index: this.flowNodeIndex
                });
                gBusEvent.$emit('handleChangRole', this.flowNodeForm.roleIds);
            },

            handleChangeFormNIdList: function (value) {
                this.flowNodeForm.formNlistIds = this.shForms.filter(function (item) {
                    return item.id === value;
                });

                this.$store.dispatch('changeFlowNodeList', {
                    type: 'updateFlowNodeList',
                    node: this.getPureFlowNodeForm(),
                    index: this.flowNodeIndex
                });

            },


            getPureFlowNodeForm: function () {
                var flowNodeForm = JSON.parse(JSON.stringify(this.flowNodeForm));
                delete flowNodeForm.roleIdList;
                delete flowNodeForm.formNIdList;
                return flowNodeForm;
            },

            resetFlowForm: function () {
                this.flowNodeForm = {
                    id: '',
                    name: '',
                    typeName: '',
                    parentId: '',
                    taskType: 'None',
                    nodeId: '',
                    nodeKey: '',
                    type: '',
                    remarks: '',
                    roleIds: [],
                    roleIdList: [],
                    regType: '',
                    isAssign: '0',
                    isShow: 1,
                    formNlistIds: [],
                    formListIds: [],
                    formIds: [],
                    formNIdList: '',
                    preIds: [],
                    outgoing: [],
                    level: ''
                }
            }
        },
        mounted: function () {
            Object.assign(this.flowNodeForm, this.flowNode);

            // this.flowNodeForm.isAssign = this.flowNode.isAssign ? '1' : '0';

            this.flowNodeForm.roleIdList = this.flowNodeForm.roleIds.map(function (item) {
                return item.id;
            });
            if (this.flowNodeForm.formNlistIds.length > 0) {
                this.flowNodeForm.formNIdList = this.flowNodeForm.formNlistIds[0].id;
            }
        },
        destroyed: function () {

        },
        beforeDestroy: function () {
            this.resetFlowForm();
        }
    }
)


