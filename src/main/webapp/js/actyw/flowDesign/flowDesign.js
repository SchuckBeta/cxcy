+function ($, Vue) {

    function FlowDesignPaper() {
        // console.log(this)
    }

    FlowDesignPaper.prototype.$toDesignPaper = function () {
        console.log()
    }

    FlowDesignPaper.install = function (Vue, option) {
        // 1. 添加全局方法或属性
        // Vue.flowDesignPaper = function () {
        //     // 逻辑...
        //     // return new FlowDesignPaper();
        // }

        // // 2. 添加全局资源
        Vue.directive('design-paper', {
            inserted: function (element, binding, vnode) {
                var designPaper = Snap(element);
                var overallGroup = designPaper.group().attr('transform', 'matrix(1,0,0,1,0,0)');
                overallGroup.addClass('v-overall-group');
                designPaper.add(overallGroup);
                vnode.context.designPaper = designPaper;
                vnode.context.overallGroup = overallGroup;

            }
        });

        // 3. 注入组件
        // Vue.mixin({
        //     mounted: function () {
        //         Vue.flowDesignPaper = new FlowDesignPaper(option)
        //     }
        // });

        // 4. 添加实例方法
        // Vue.prototype.$toDesignPaper = function (transform, ele) {
        //     // 逻辑...
        //     this.designPaper.append(ele.attr(transform))
        // };


        /* return 布尔值 */


        var validateRules = {
            userTask: {
                name: {
                    required: true,
                    firstLessNumber: /^[^\d+]/ //开头不能为数字
                },
                formIds: {
                    required: function (value, parentId, roleIds) {
                        if(roleIds.length > 0 && roleIds[0].id === '13757518f4da45ecaa32a3b582e8396a'){
                            return true;
                        }
                        // if(parentId != 1){
                        //     return true;
                        // }
                        return (value && value.length > 0);
                    }
                },
                formListIds: {
                    required: function (value, parentId, roleIds) {
                        if(roleIds.length > 0 && roleIds[0].id === '13757518f4da45ecaa32a3b582e8396a'){
                            return true;
                        }
                        // if(parentId != 1){
                        //     return true;
                        // }
                        return (value && value.length > 0);
                    }
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
                    required: function (value) {
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
                        if (type == 140 || type == 240) {
                            return (value && value.length > 0)
                        }
                        return true;
                    }
                }
            }
        }

        var errMessages = {
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
        }


        


        Vue.prototype.$validateNodes = function (nodeList, overallGroup) {
            var nodeError = {};


            for (var i = 0, len = nodeList.length; i < len; i++) {
                var item = nodeList[i];
                var rules;
                var errs;
                var content;
                var nodeKey = item.nodeKey;
                var name = item.name;
                var isAllValidate = true;


                nodeKey = nodeKey.replace(/^\w{1}/i, function ($1) {
                    return $1.toLowerCase()
                });
                rules = validateRules[nodeKey];
                errs = errMessages[nodeKey];
                if (rules && errs) {
                    bk: for (var rule in rules) {
                        if (rules.hasOwnProperty(rule)) {
                            var value = item[rule];
                            var validates = rules[rule];
                            // var ftype = $.type(validates);



                            for (var rk in validates) {
                                if (validates.hasOwnProperty(rk)) {
                                    var type = $.type(validates[rk]);
                                    if (type === 'boolean') {
                                        if(!validates[rk]){
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
                                        if (item.nodeKey === 'SequenceFlow') {
                                            var source = overallGroup.select('g[model-id="' + item.id + '"]');
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

                                            if(item.nodeKey === 'UserTask' && rule == 'formIds'){
                                                bool = validates[rk](value, item.parentId, item.roleIds);

                                            }else if(item.nodeKey === 'UserTask' && rule == 'formListIds'){
                                                bool = validates[rk](value, item.parentId, item.roleIds);
                                            }else{
                                                bool = validates[rk](value, item.parentId);
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

                            // if(ftype === 'function'){
                            //     var bool = validates(value);
                            //     if(!bool) {
                            //         isAllValidate = false;
                            //         content = errs[rule]();
                            //         break bk;
                            //     }
                            // }
                            // if(!isAllValidate){
                            //     break;
                            // }
                        }
                    }
                }
                if (!isAllValidate) {
                    nodeError = {
                        id: item.id,
                        msg: content
                    };
                    break;
                }
            }
            return nodeError['id'] ? nodeError : true;

            // nodeList.forEach(function (item, i) {
            //     var nodeKey = item.nodeKey;
            //
            // })

            //
            // return {
            //     isX: Bloone,
            //     msg: '',
            //     id: '',
            // }

        }

    }

    window['FlowDesignPaper'] = FlowDesignPaper;
    if (Vue) {
        Vue.use(FlowDesignPaper, {id: '#flowDesignSvg'})
    }
}(jQuery, window.Vue);