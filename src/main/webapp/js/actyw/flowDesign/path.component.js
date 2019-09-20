+function ($, Vue) {
    var sequenceFlow = Vue.component('sequence-flow', {
        template: '<div class="group" :class="{groupOpened: open}">' +
        '<h4 class="group-label"><i class="iconfont iconfont-open" @click="open = true">&#xe63b;</i>' +
        '<i class="iconfont iconfont-close" @click="open = false">&#xe63e;</i><span>编辑{{sequenceFlow.name}}</span>' +
        '<a href="javascript:void(0);"><i class="iconfont iconfont-delete">&#xe625;</i></a></h4>' +
        '<form class="form-horizontal">' +
        '<div class="control-group"><label class="control-label"><i>*</i>节点名称：</label><div class="controls">' +
        '<input type="text" disabled autocomplete="off" name="name" v-model="sequenceFlow.name" @change="changeNodeName"></div></div>' +
        '<div class="control-group"><label class="control-label">节点类型：</label><div class="controls"><p class="control-static">{{sequenceFlow.typeName}}</p></div></div>' +
        '<div  v-show="hasCondition && showCd" class="control-group"><label class="control-label"><i>*</i>条件：</label>' +
        '<div class="controls"><slot name="condition"></slot></div></div>' +
        '<div v-show="hasCondition && !showCd" class="control-group"><label class="control-label"><i>*</i>条件：</label><div class="controls"><p class="control-static gray-color">请选择网关判定类型</p></div></div>' +
        '<div class="control-group"><label class="control-label">备注：</label><div class="controls"><textarea rows="3" placeholder="最多33个字" v-model="sequenceFlow.remarks"></textarea></div></div>' +
        '</form></div>',
        model: {
            prop: 'sequenceFlow',
            event: 'change'
        },
        props: {
            sequenceFlow: {
                type: Object,
                default: {}
            },
            forms: {
                type: Array,
                default: function () {
                    return []
                }
            },
            roles: {
                type: Array,
                default: function () {
                    return []
                }
            },
            users: {
                type: Array,
                default: function () {
                    return []
                }
            },
            taskTypes: {
                type: Array,
                default: function () {
                    return []
                }
            },
            hasCondition: {
                type: Boolean,
                default: false
            },
            conditions: {
                type: Array,
                default: function () {
                    return []
                }
            },
            icon: {
                type: String,
                default: '/images/upload-default-image100X100.png'
            }
        },
        data: function () {
            return {
                open: true
            }
        },
        filters: {
            ftpHttp: function (val) {
                return val ? (ftpHttp + val.replace('/tool', '')) : '/images/upload-default-image100X100.png';
            }
        },
        computed: {
            showCd: function () {
                var statusIds, conditions = this.conditions;
                statusIds = this.sequenceFlow.statusIds;
                if(statusIds){
                    return statusIds.length || conditions.length;
                }
                return false;
            }
        },
        watch: {
            'sequenceFlow.name': function (value) {
                this.$emit('change-node-name', value)
            }
        },
        methods: {
            changeNodeName: function () {

            },
            openUploadModal: function () {
                var nodeKey = this.userTask.nodeKey;
                this.$emit('open-modal', nodeKey.replace(/^\w{1}/i, function ($1) {
                    return $1.toLowerCase()
                }))
            }
        },
        mounted: function () {

        }
    })

}(jQuery, Vue);