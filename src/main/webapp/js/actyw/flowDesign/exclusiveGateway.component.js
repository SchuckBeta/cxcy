+function ($, Vue) {
    var exclusiveGateway = Vue.component('exclusive-gateway', {
        template: '<div class="group" :class="{groupOpened: open}">' +
        '<h4 class="group-label"><i class="iconfont iconfont-open" @click="open = true">&#xe63b;</i>' +
        '<i class="iconfont iconfont-close" @click="open = false">&#xe63e;</i><span>编辑{{exclusiveGateway.name}}节点</span>' +
        '<a href="javascript:void(0);"><i class="iconfont iconfont-delete">&#xe625;</i></a></h4>' +
        '<form class="form-horizontal">' +
        '<div class="control-group"><label class="control-label"><i>*</i>节点名称：</label><div class="controls">' +
        '<input type="text" autocomplete="off" name="name" v-model="exclusiveGateway.name" @change="changeNodeName"></div></div>' +
        '<div class="control-group"><label class="control-label">节点类型：</label><div class="controls"><p class="control-static">{{exclusiveGateway.typeName}}</p></div></div>' +
        '<div v-show=" prevModelKey == \'UserTask\'" class="control-group"><label class="control-label"  @mouseenter="parseLabel(\'exclusiveGatewayRegType\',$event)" @mouseleave="leaveLabel">筛选表单：</label><div class="controls"><p class="control-static" style="white-space: normal;" :class="{\'gray-color\': !regTypeName}">{{regTypeName || rtPlaceholder}}</p></div></div>' +
        '<div class="control-group"><label class="control-label" @mouseenter="parseLabel(\'exclusiveGatewayRegTypeName\',$event)" @mouseleave="leaveLabel"><i>*</i>判定类型：</label>' +
        '<div class="controls">' +
        '<div style="padding: 3px;border: 1px solid #eee"><select :disabled="!regTypeName && !prevModelKey" v-model="exclusiveGateway.gstatusTtype" @change="changeGStatusType">' +
        '<option value="">--请选择--</option>' +
        '<option v-for="(item, index) in rNodeStatusTypes" :value="item.id" :key="item.id">{{item.name}}</option></select><button type="button" class="btn btn-primary btn-small" @click="openJudgeModal" style="display: block;width: 100%;box-sizing: border-box;margin-top: 3px;">添加判定类型</button></div></div></div>' +
        '<div class="control-group"><label class="control-label" @mouseenter="parseLabel(\'exclusiveGatewayGStatus\',$event)" @mouseleave="leaveLabel"><i>*</i>判定条件：</label>' +
        '<div class="controls"><slot name="gStatus"></slot></div></div>' +
        '<div v-if="hasForm" class="control-group"><label class="control-label"><i>*</i>审核表单：</label><div class="controls">' +
        '<slot name="form"></slot></div></div>' +
        '<div class="control-group" v-show="visiableShow"><label class="control-label">显示：</label>' +
        '<div class="controls"><label class="radio inline"><input type="radio" name="isShow" v-model="exclusiveGateway.isShow" value="1">是</label><label class="radio inline"><input type="radio" name="isShow" v-model="exclusiveGateway.isShow" value="0">否</label></div></div>' +
        '<div class="control-group"><label class="control-label">图标：</label><div class="controls controls-iconbox">' +
        '<img title="更换图标" @click="openUploadModal($event)" class="node-icon" :src="exclusiveGateway.iconUrl | ftpHttp"></div></div>' +
        '<div class="control-group"><label class="control-label" @mouseenter="parseLabel(\'exclusiveGatewayOtherType\',$event)">监听事件：</label><div class="controls"><slot name="otherType"></slot></div></div>' +
        '<div class="control-group"><label class="control-label">备注：</label><div class="controls"><textarea rows="3" placeholder="最多33个字" v-model="exclusiveGateway.remarks"></textarea></div></div>' +
        '</form></div>',
        model: {
            prop: 'exclusiveGateway',
            event: 'change'
        },
        props: {
            exclusiveGateway: {
                type: Object,
                default: {}
            },
            visiableShow: {
                type: Boolean,
                default: false
            },
            hasForm: {
                type: Boolean,
                default: false,
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
            prevNodes: {
                type: Array,
                default: function () {
                    return []
                }
            },
            icon: {
                type: String,
                default: '/images/upload-default-image100X100.png'
            },
            nodeName: {
                type: String,
                default: ''
            },
            regTypeName: {
                type: String,
                default: ''
            },
            regTypeValue: {
                type: String,
                default: ''
            },
            prevModelKey: {
              type: String,
                default: ''
            },
            nodeStatusTypes: {
                type: Array,
                default: function () {
                    return []
                }
            }
        },
        data: function () {
            return {
                open: true,
                gStatus: []
            }
        },
        computed: {
            rtPlaceholder: function () {
                if (!this.nodeName) {
                    return '请先连接节点';
                }
                return '请选择' + this.nodeName + '筛选表单'
            },
            rNodeStatusTypes: function () {
                var self = this;
                if(this.prevModelKey !== 'UserTask' || self.regTypeValue == '999'){
                    return this.nodeStatusTypes;
                }
                return this.nodeStatusTypes.filter(function (item) {
                    return self.regTypeValue === item.regType;
                })
            }
        },
        filters: {
            ftpHttp: function (val) {
                return val ? (ftpHttp + val.replace('/tool', '')) : '/images/upload-default-image100X100.png';
            }
        },
        watch: {
            'exclusiveGateway.name': function (value) {
                this.$emit('change-node-name', value)
            }
        },
        methods: {
            changeNodeName: function () {

            },
            openUploadModal: function () {
                var nodeKey = this.exclusiveGateway.nodeKey;
                this.$emit('open-modal', nodeKey.replace(/^\w{1}/i, function ($1) {
                    return $1.toLowerCase()
                }))
            },
            changeGStatusType: function () {
                this.$emit('change-g-status-type', this.exclusiveGateway.gstatusTtype);
            },
            selectStatus: function () {

            },
            openJudgeModal: function () {
                this.$emit('open-judge-modal');
            },
            parseLabel: function (type, $event) {
                this.$emit('parse-label', {type: type, $target: $($event.target)})
            },
            leaveLabel: function () {
                this.$emit('leave-label')
            }
        },
        mounted: function () {

        }
    })

}(jQuery, Vue);