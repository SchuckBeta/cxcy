/**
 * Created by Administrator on 2018/1/22.
 */
+function ($, Vue) {
    var endErrorEvent = Vue.component('end-error-event', {
        template: '<div class="group" :class="{groupOpened: open}">' +
        '<h4 class="group-label"><i class="iconfont iconfont-open" @click="open = true">&#xe63b;</i>' +
        '<i class="iconfont iconfont-close" @click="open = false">&#xe63e;</i><span>编辑{{endErrorEvent.name}}节点</span>' +
        '<a href="javascript:void(0);"><i class="iconfont iconfont-delete">&#xe625;</i></a></h4>' +
        '<form class="form-horizontal">' +
        '<div class="control-group"><label class="control-label"><i>*</i>流程节点：</label><div class="controls">' +
        '<input type="text" autocomplete="off" name="name" disabled v-model="endErrorEvent.name"></div></div>' +
        '<div class="control-group"><label class="control-label">节点类型：</label><div class="controls"><p class="control-static">{{endErrorEvent.typeName}}</p></div></div>' +
        '<div class="control-group"><label class="control-label">备注：</label><div class="controls"><textarea rows="3" placeholder="最多33个字" v-model="endErrorEvent.remarks"></textarea></div></div>' +
        '</form></div>',
        model: {
            prop: 'endErrorEvent',
            event: 'change'
        },
        props: {
            endErrorEvent: {
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
            'endErrorEvent.name': function (value) {
                this.$emit('change-node-name', value)
            }
        },
        mounted: function () {

        }
    })

}(jQuery, Vue);