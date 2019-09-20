+function ($, Vue) {
    var eventGateway = Vue.component('event-gateway', {
        template: '<div class="group" :class="{groupOpened: open}">' +
        '<h4 class="group-label"><i class="iconfont iconfont-open" @click="open = true">&#xe63b;</i>' +
        '<i class="iconfont iconfont-close" @click="open = false">&#xe63e;</i><span>编辑{{eventGateway.name}}节点</span>' +
        '<a href="javascript:void(0);"><i class="iconfont iconfont-delete">&#xe625;</i></a></h4>' +
        '<form class="form-horizontal">' +
        '<div class="control-group"><label class="control-label"><i>*</i>节点名称：</label><div class="controls">' +
        '<input type="text" autocomplete="off" name="name" v-model="eventGateway.name" @change="changeNodeName"></div></div>' +
        '<div class="control-group"><label class="control-label">节点类型：</label><div class="controls"><p class="control-static">{{eventGateway.typeName}}</p></div></div>' +
        '<div class="control-group"><label class="control-label"><i>*</i>条件类型：</label><div class="controls"><select v-model="eventGateway.gstatusTtype" @change="changeGStatusType"><option v-for="(item, index) in nodeStatusTypes" :value="item.value" :key="item.id">{{item.label}}</option></select></div></div>' +
        '<div class="control-group"><label class="control-label"><i>*</i>条件状态：</label><div class="controls"><slot name="gStatus"></slot></div></div>' +
        '<div class="control-group"><label class="control-label">表单：</label><div class="controls">' +
        '<slot name="form"></slot></div></div>' +
        '<div class="control-group"><label class="control-label">显示：</label>' +
        '<div class="controls"><label class="radio inline"><input type="radio" name="isShow" v-model="eventGateway.isShow" value="1">是</label><label class="radio inline"><input type="radio" name="isShow" v-model="eventGateway.isShow" value="0">否</label></div></div>' +
        '<div class="control-group"><label class="control-label">图标</label><div class="controls controls-iconbox">' +
        '<img title="更换图标" @click="openUploadModal($event)" class="node-icon" :src="eventGateway.iconUrl | ftpHttp"></div></div>' +
        '<div class="control-group"><label class="control-label">备注：</label><div class="controls"><textarea rows="3" placeholder="最多33个字" v-model="eventGateway.remarks"></textarea></div></div>' +
        '</form></div>',
        model: {
            prop: 'eventGateway',
            event: 'change'
        },
        props: {
            eventGateway: {
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
            nodeStatusTypes: {
                type: Array,
                default: function () {
                    return []
                }
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
        watch: {
          'eventGateway.name': function (value) {
              this.$emit('change-node-name', value)
          }
        },
        methods: {
            changeNodeName: function () {

            },
            openUploadModal: function () {
                var nodeKey = this.eventGateway.nodeKey
                this.$emit('open-modal', nodeKey.replace(/^\w{1}/i, function ($1) {
                    return $1.toLowerCase()
                }))
            },
            changeGStatusType: function () {
                this.$emit('change-g-status-type', this.eventGateway.gstatusTtype);
            },
        },
        mounted: function () {

        }
    })

}(jQuery, Vue);