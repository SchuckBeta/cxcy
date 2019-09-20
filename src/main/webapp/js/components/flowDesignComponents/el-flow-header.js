Vue.component('el-flow-header', {
    template: '<div class="el-flow-header"><el-row><el-col :span="10"><h2 class="el-flow-name">{{groupName}}</h2></el-col><el-col :span="14"  class="el-flow-btn-group"><el-button-group>' +
    '<el-button size="mini" type="primary" :disabled="disabled" @click.stop="submitNodeList">提交</el-button><el-button size="mini" :disabled="disabled" type="primary" @click.sotp="saveFlowNodeList">保存</el-button>' +
    '<el-button size="mini" :disabled="disabled" @click.stop="clearSvgPaper">清空画布</el-button><el-button size="mini" :disabled="disabled" @click.stop="confirmSaveFlowNodeList">返回</el-button></el-button-group></el-col></el-row></div>',
    data: function () {
        var self = this;
        var formIdsValidate = function (value, parentId, roleIds) {
            if (roleIds && roleIds.length > 0 && roleIds[0].id === self.studentId) {
                return true;
            }
            return (value && value.length > 0);
        };
        return {

            flowRules: {
                userTask: {
                    name: {
                        required: true,
                        firstLessNumber: /^[^\d+]/ //开头不能为数字
                    },
                    formIds: {
                        required: formIdsValidate
                    },
                    formListIds: {
                        required: formIdsValidate
                    },
                    formNlistIds: {
                        required: function (value, parentId) {
                            // if(parentId != 1){
                            //     return true;
                            // }
                            return (value && value.length > 0);
                        }
                    },
                    iconUrl: {
                        required: false
                    },
                    roleIds: {
                        required: function (value, hasRole) {
                            if(hasRole){
                                return true;
                            }
                            return (value && value.length > 0);
                        }
                    }

                },
                subProcess: {
                    name: {
                        required: true,
                        firstLessNumber: /^[^\d+]/ //开头不能为数字
                    },
                    formIds: {
                        required: function (value) {
                            return (value && value.length > 0);
                        }
                    },
                    iconUrl: {
                        required: false
                    }
                },
                exclusiveGateway: {
                    iconUrl: {
                        required: false
                    },
                    gstatusTtype: {
                        required: true
                    }
                },
                sequenceFlow: {
                    statusIds: {
                        required: function (value, type) {
                            if (type === '140' || type === '240') {
                                return (value && value.length > 0)
                            }
                            return true;
                        }
                    }
                }
            },
            errMessages: {
                userTask: {
                    name: {
                        required: '节点名称必填',
                        firstLessNumber: '节点名称开头不能为数字'
                    },
                    formIds: {
                        required: '列表表单必选'
                    },
                    formListIds: {
                        required: '列表表单必选'
                    },
                    formNlistIds: {
                        required: '审核表单必选'
                    },
                    iconUrl: {
                        required: '节点图片必选上传'
                    },
                    roleIds: {
                        required: '节点角色必选'
                    }
                },
                subProcess: {
                    name: {
                        required: '子流程名称必填',
                        firstLessNumber: '子流程开头不能为数字'
                    },
                    formIds: {
                        required: '子流程表单必选'
                    },
                    iconUrl: {
                        required: '子流程图片必选上传'
                    }
                },
                exclusiveGateway: {
                    iconUrl: {
                        required: '网关节点图片必须上传'
                    },
                    gstatusTtype: {
                        required: '判定类型必选'
                    }
                },
                sequenceFlow: {
                    statusIds: {
                        required: '网关后的条件必填'
                    }
                }
            },
            regNodeTypes: /^110{1}.120{1}.(([(^110)|(120)|(150)|(190)|(130)|(140)]+(120|130|.))*.?)(130|120)$/,
            regSubNodeTypes: /^210{1}.220{1}.(([(^210)|(220)|(250)|(290)|(230)|(240)]+(220|230|.))*.?)(220|230)$/,
            parentIdList: [],
            allNodeTypes: {}
        }
    },
    computed: {
        studentId: function () {
          return this.$root.studentId;
        },
        disabled: function () {
            return this.$store.state.disabledAll;
        },

        flowNodeList: function () {
            return this.$store.state.flowNodeList;
        },

        svgPaperStyle: function () {
            return this.$store.state.svgPaperStyle;
        },

        flowNodeListParams: function () {
            var flowNodeList = JSON.parse(JSON.stringify(this.flowNodeList));
            flowNodeList = flowNodeList.map(function (item) {
                if (item.nodeKey === 'SubProcess') {
                    delete item.formListId;
                } else if (item.nodeKey === 'UserTask') {
                    delete item.roleIdList;
                    delete item.formNIdList;
                    if(item.formNlistIds && item.formNlistIds.length > 0){
                        item.formListIds = item.formNlistIds.map(function (iItem) {
                            return {id: iItem.listId}
                        });
                        item.formListIds = item.formListIds.filter(function (iItem) {
                            return iItem.id !== '';
                        });
                        item.formIds = [].concat(item.formNlistIds, item.formListIds);
                        item.formIds = item.formIds.filter(function (iItem) {
                            return iItem.id !== '';
                        })
                    }
                } else if (item.nodeKey === 'ExclusiveGateway') {
                    delete item.classIdList;
                } else if (item.nodeKey === 'SequenceFlow') {
                    delete item.statusIdList;
                }
                return item;
            })
            return flowNodeList;
        },

        snapSvgPaper: function () {
            return this.$store.state.snapSvgPaper;
        },

        groupName: function () {
            return this.$root.groupName;
        },

        overallGroup: function () {
            return this.snapSvgPaper.select('g.v-overall-group')
        },

        rootId: function () {
            return this.$root.rootId;
        },

        allTypeNodesLen: function () {
            var allNodeTypes = this.allNodeTypes;
            var nArr = [];
            for (var k in allNodeTypes) {
                if (allNodeTypes.hasOwnProperty(k)) {
                    nArr = nArr.concat(allNodeTypes[k])
                }
            }
            return nArr.length;
        }
    },
    methods: {

        getPostData: function (isSave) {
            gBusEvent.$emit('clearPathColor');
            var postData = {
                groupId: this.$root.groupId,
                uiJson: this.svgPaperStyle,
                uiHtml: this.overallGroup.node.innerHTML,
                list: this.flowNodeListParams
            };
            postData.temp = isSave;
            return postData;
        },

        saveFlowNodeList: function (isGoBack) {
            var postData = this.getPostData(true);
            var self = this;
            this.$store.dispatch('updateDisabledAll', true);
            this.$axios.post('/actyw/gnode/saveAll', postData).then(function (response) {
                var data = response.data;
                if (data.status) {
                    if(isGoBack === 'back'){
                        location.href = '/a/actyw/actYwGroup/list'
                    }else {
                        self.$alert(data.msg, '提示', {
                            type: 'success'
                        })
                    }
                } else {
                    self.$alert(data.msg, '提示', {
                        type: 'error',
                    })
                }
                self.$store.dispatch('updateDisabledAll', false);
            }).catch(function () {

            })
            // console.log(this.flowNodeList)
        },

        isPathLinked: function () {
            var overallGroup = this.overallGroup;
            var flowNodeList = this.flowNodeList;
            var i = 0;
            var gateways = flowNodeList.filter(function (item) {
                return FlowDesignConfig.gatewayNodes.indexOf(item.type) > -1;
            });
            var length = gateways.length;
            if (length === 0) {
                return true;
            }
            while (i < length) {
                var gateway = gateways[i];
                var statusIds = gateway.statusIds;
                var id = gateway.id;
                var statusIdsLen = statusIds.length;
                var pathSources = overallGroup.selectAll('g[source-id="' + id + '"]');
                if (statusIdsLen.length === 0) {
                    this.$message({
                        type: 'error',
                        message: gateway.name + '至少有一个条件'
                    });
                    return false;
                }
                if (pathSources.length < 2) {
                    this.$message({
                        type: 'error',
                        message: gateway.name + '网关后面最少需要有两条线'
                    });
                    return false;
                }
                i++;
            }
            return true;
        },
        getNodeData: function (id) {
            var i = 0;
            var flowNodeList = this.flowNodeList;
            while (i < flowNodeList.length) {
                var item = flowNodeList[i];
                if (item.id === id) {
                    return item;
                }
                i++;
            }
            return {};
        },
        validateFlowNodeProps: function () {
            var validateNodeProps = {
                type: 'success'
            };
            var flowNodeList = this.flowNodeListParams;
            var i = 0;
            var flowRules = this.flowRules;
            var errMessages = this.errMessages;
            var overallGroup = this.overallGroup;
            while (i < flowNodeList.length) {
                var flowNode = flowNodeList[i];
                var nodeKey = flowNode.nodeKey;
                var name = flowNode.name;
                nodeKey = nodeKey.replace(/^\w{1}/i, function ($1) {
                    return $1.toLowerCase()
                });
                var rules = flowRules[nodeKey];
                var errs = errMessages[nodeKey];
                var isAllValidate = true;
                var content;

                if (rules && errs) {
                    bk: for (var rule in rules) {
                        if (!rules.hasOwnProperty(rule)) {
                            continue;
                        }
                        var value = flowNode[rule];
                        var validates = rules[rule];
                        for (var rk in validates) {
                            if (validates.hasOwnProperty(rk)) {
                                var type = $.type(validates[rk]);
                                if (type === 'boolean') {
                                    if (!validates[rk]) {
                                        continue;
                                    }
                                    if (!value === !!validates[rk]) {
                                        isAllValidate = false;
                                        content = name + '\n' + errs[rule][rk];
                                        break bk;
                                    }
                                } else if (type === 'regexp') {
                                    if (!validates[rk].test(value)) {
                                        isAllValidate = false;
                                        content = name + '\n' + errs[rule][rk];
                                        break bk;
                                    }
                                } else if (type === 'function') {
                                    if (nodeKey === 'sequenceFlow') {
                                        var source = overallGroup.select('g[model-id="' + flowNode.id + '"]');
                                        var sourceId, nodeType;
                                        if (source) {
                                            sourceId = source.attr('source-id');
                                            nodeType = this.getNodeData(sourceId).type;
                                            if (!validates[rk].call(null, value, nodeType)) {
                                                isAllValidate = false;
                                                content = name + '\n' + errs[rule][rk];
                                                break bk;
                                            }
                                        }
                                    } else {
                                        var bool;
                                        if (nodeKey === 'userTask' && ['formListIds', 'formIds', 'formNlistIds'].indexOf(rule) > -1) {
                                            bool = validates[rk](value, flowNode.parentId, flowNode.roleIds);

                                        } else if(nodeKey.nodeKey === 'userTask' && rule === 'roleIds') {
                                            var hasRole = (flowNode.taskType === 'Parallel' && flowNode.regType === '2' && flowNode.isAssign === '1');
                                            bool = validates[rk](value, hasRole);
                                        }else {
                                            bool = validates[rk](value, flowNode.parentId);
                                        }

                                        if (!bool) {
                                            isAllValidate = false;
                                            content = name + '\n' + errs[rule][rk];
                                            break bk;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!isAllValidate) {
                    validateNodeProps = {
                        type: 'error',
                        id: flowNode.id,
                        message: content
                    };
                    break;
                }
                i++;
            }
            return validateNodeProps;
        },

        validateIsAuditNode: function () {
            var overallGroup = this.overallGroup;
            var elPathList = overallGroup.selectAll('g.v-path-group');
            var isValid = true;
            for(var i = 0; i < elPathList.length; i++){
                var elPath = elPathList[i];
                var sourceId = elPath.attr('source-id');
                var targetId = elPath.attr('target-id');
                var sourceNode = this.getFlowNodeById(sourceId);
                var targetNode;
                if(sourceNode.regType !== '1'){
                    continue;
                }
                targetNode = this.getFlowNodeById(targetId);
                if(targetNode.nodeKey !== 'ExclusiveGateway'){
                    this.$alert('请在审核节点后面添加网关判定节点','提示', {
                        type: 'error'
                    });
                    isValid = false;
                    break;
                }
            }
            return isValid
        },

        getFlowNodeById: function (id) {
            var flowNodeList = this.flowNodeList;
            var i = 0;
            while (i < flowNodeList.length){
                if(flowNodeList[i].id === id){
                    return flowNodeList[i]
                }
                i++;
            }
        },


        validateFlow: function () {
            var flowNodeList = this.flowNodeList;
            var isPathLinked;
            var validateFlowNodeProps;
            var overallGroup = this.overallGroup;
            if (flowNodeList.length === 0) {
                this.$message({
                    type: 'error',
                    message: '流程还未创建'
                });
                return false;
            }
            isPathLinked = this.isPathLinked();
            if (!isPathLinked) {
                return false;
            }
            var childrenLen = overallGroup.children().length;
            var subProcess = overallGroup.selectAll('g.v-translate[model-key="SubProcess"]');
            if(subProcess && subProcess.length > 0){
                subProcess.forEach(function (item) {
                    var itemChildShapes = item.select('.v-childShapes');
                    var childShapeChildren;
                    if(itemChildShapes){
                        childShapeChildren = itemChildShapes.children();
                        if(childShapeChildren && childShapeChildren.length > 0){
                            childrenLen += childShapeChildren.length;
                        }
                    }
                })
            }
            if(flowNodeList.length !== childrenLen){
                this.$message({
                    type: 'error',
                    message: '流程存在多余数据，请清空画布，重新设计'
                });
                return false;
            }
            validateFlowNodeProps = this.validateFlowNodeProps();
            if (validateFlowNodeProps.type === 'error') {
                this.$message({
                    type: 'error',
                    message: validateFlowNodeProps.message
                });
                return false;
            }
            this.updateFlowLevel();
            var validateFlowNodeLink = this.validateFlowNodeLink();
            if (!validateFlowNodeLink || flowNodeList.length !== this.allTypeNodesLen) {
                this.$message({
                    type: 'error',
                    message: '流程不合格，请检查'
                });
                return false;
            }
            return this.validateIsAuditNode();
        },

        validateFlowNodeLink: function () {
            var i = 0;
            var parentIdList = this.parentIdList; //存储所有的父级id, 流程组；
            var allNodeTypes = this.allNodeTypes; //存储所有的流程type
            var regNodeTypes = this.regNodeTypes;
            var regSubNodeTypes = this.regSubNodeTypes;
            var rootId = this.rootId;
            while (i < parentIdList.length) {
                var id = parentIdList[i];
                var nodeTypes = allNodeTypes[id];
                var ids = nodeTypes.join(',');
                var reg = id === rootId ? regNodeTypes : regSubNodeTypes;
                if (!reg.test(ids)) {
                    return false;
                }
                i++;
            }
            return true;
        },

        updateFlowLevel: function () {
            var flowNodeList = this.flowNodeList.map(function (item) {
                item.level = -1;
                return item;
            });
            var self = this;
            var allNodeTypes = this.allNodeTypes = [];
            var parentIdList = [];
            flowNodeList.forEach(function (item) {
                if (FlowDesignConfig.startNoneEventTypes.indexOf(item.type) > -1) {
                    item.level = 1;
                    var parentId = item.parentId;
                    if (parentIdList.indexOf(parentId) === -1) {
                        parentIdList.push(parentId);
                    }
                    if (!allNodeTypes[parentId]) {
                        allNodeTypes[parentId] = [];
                    }
                    allNodeTypes[parentId].push(item.type);
                    self.nextNodeUpdateLevel(item, 1, flowNodeList);
                }
            });
            this.parentIdList = parentIdList;
            this.$store.dispatch('setFlowNodeList', flowNodeList);
        },

        nextNodeUpdateLevel: function (item, level, flowNodeList) {
            var overallGroup = this.overallGroup;
            var pathGroupsSource = overallGroup.selectAll('g[source-id="' + item.id + '"]'); //以开始为源目标的线
            var self = this;
            if (pathGroupsSource.length === 0) {
                //没有连接的目标节点
                return;
            }

            pathGroupsSource.forEach(function (path) {
                var modelId = path.attr('model-id');
                var targetId = path.attr('target-id');
                var sourceId = path.attr('source-id');
                self.setFlowNodeLevel(flowNodeList, modelId, level + 1, sourceId); // 设置当前的线

                if (targetId) {
                    var targetNodeLevel = self.getFlowNodeLevel(flowNodeList, targetId);
                    if (item.id !== targetId && targetNodeLevel === -1) { //目标的level如果不等于-1， 是设置过的， 过滤掉
                        self.setFlowNodeLevel(flowNodeList, targetId, level + 2, modelId);
                        self.nextNodeUpdateLevel({id: targetId}, level + 2, flowNodeList)
                    }else {
                        //处理回调preFunId
                        if(targetNodeLevel !== -1){
                            var sourceData = self.getNodeData(targetId);
                            var targetPath = overallGroup.select('g[source-id="' + item.id + '"][target-id="'+sourceData.id+'"]')
                            sourceData.preFunId = item.id;
                            sourceData.preIds.push({
                                resourceId: targetPath.attr('model-id')
                            })
                        }
                    }
                }
            })

        },

        getFlowNodeLevel: function (flowNodeList, modelId) {
            var i = 0;
            while (i < flowNodeList.length) {
                var item = flowNodeList[i];
                if (item.id === modelId) {
                    return item.level;
                }
                i++;
            }
        },

        setFlowNodeLevel: function (flowNodeList, modelId, level, sourceId) {
            var i = 0;
            var allNodeTypes = this.allNodeTypes;
            while (i < flowNodeList.length) {
                var item = flowNodeList[i];
                if (item.id === modelId) {
                    item.level = level;
                    if(sourceId){

                        if(item.nodeKey === 'SequenceFlow'){
                            item.preFunId = sourceId;
                        }else {
                            var sourceData = this.getNodeData(sourceId);
                            if(sourceData.preIds && sourceData.preIds.length > 0){
                                item.preFunId = sourceData.preIds[0].resourceId;
                            }
                        }

                        item.preIds = [{
                            resourceId: sourceId
                        }]
                    }
                    var parentId = item.parentId;
                    if (!allNodeTypes[parentId]) {
                        allNodeTypes[parentId] = [];
                    }
                    allNodeTypes[parentId].push(item.type);
                    return item;
                }
                i++;
            }
        },

        submitNodeList: function () {
            var validateFlow = this.validateFlow();
            var groupName = this.groupName;
            if (validateFlow) {
                var postData = this.getPostData(false);
                var self = this;
                this.$store.dispatch('updateDisabledAll', true);
                this.$axios.post('/actyw/gnode/saveAll', postData).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        if(self.showActrel){
                            self.$alert('流程设计完成，请关联省级流程！', '提示', {
                                type: 'success',
                                confirmButtonText: '关联流程'
                            }).then(function () {
                                location.href = '/a/actyw/actYwGroup/list?name=' + encodeURI(groupName);
                            })
                        }else {
                            self.$alert('流程提交成功，请去流程页面发布列表流程', '提示', {
                                type: 'success',
                                confirmButtonText: '发布流程'
                            }).then(function () {
                                location.href = '/a/actyw/actYwGroup/list?name=' + encodeURI(groupName);
                            })
                        }
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error',
                        })
                    }
                    self.$store.dispatch('updateDisabledAll', false);
                }).catch(function () {

                })
            }
        },

        clearSvgPaper: function () {
            this.overallGroup.clear();
            this.$store.dispatch('setFlowNodeList', []);
            this.$store.dispatch('changeableRectVisible', false);
            this.$store.dispatch('setCurFlowNode', {
                node: {},
                index: -1
            })
        },

        confirmSaveFlowNodeList: function () {
            var self = this;
            this.$confirm('是否保存 ' + this.groupName + ' 流程', '提示', {
                type: 'warning'
            }).then(function () {
                self.saveFlowNodeList('back');
            }).catch(function () {
                history.go(-1)
            })
        }
    }
})