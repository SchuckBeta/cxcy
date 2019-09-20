/**
 * Created by Administrator on 2018/1/22.
 */
+function ($, Vue) {
    var endTerminateEvent = Vue.component('end-terminate-event', {
        template: '<div class="group" :class="{groupOpened: open}">' +
        '<h4 class="group-label"><i class="iconfont iconfont-open" @click="open = true">&#xe63b;</i>' +
        '<i class="iconfont iconfont-close" @click="open = false">&#xe63e;</i><span>编辑{{endTerminateEvent.name}}节点</span>' +
        '<a href="javascript:void(0);"><i class="iconfont iconfont-delete">&#xe625;</i></a></h4>' +
        '<form class="form-horizontal">' +
        '<div class="control-group"><label class="control-label"><i>*</i>节点名称：</label><div class="controls">' +
        '<input type="text" autocomplete="off" name="name" disabled v-model="endTerminateEvent.name"></div></div>' +
        '<div class="control-group"><label class="control-label">节点类型：</label><div class="controls"><p class="control-static">{{endTerminateEvent.typeName}}</p></div></div>' +
        '<div class="control-group"><label class="control-label">备注：</label><div class="controls"><textarea rows="3" placeholder="最多33个字" v-model="endTerminateEvent.remarks"></textarea></div></div>' +
        '</form></div>',
        model: {
            prop: 'endTerminateEvent',
            event: 'change'
        },
        props: {
            endTerminateEvent: {
                type: Object,
                default: {}
            },
        },
        data: function () {
            return {
                open: true
            }
        },
        watch: {
            'endTerminateEvent.name': function (value) {
                this.$emit('change-node-name', value)
            }
        },
        mounted: function () {

        }
    })

}(jQuery, Vue);