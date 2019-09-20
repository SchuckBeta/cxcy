+function ($) {
    var groupNode = Vue.component('group-node', {
        template: '<div class="group" :class="{groupOpened: open}">' +
        '<h4 class="group-label">' +
        '<i class="iconfont iconfont-open" @click="open = true">&#xe63b;</i><i class="iconfont iconfont-close" @click="open = false">&#xe63e;</i>{{title}}</h4><div v-show="open" class="shape-elements" :style="{height: eHeight}"><slot name="svg"></slot></div></h4>',
        props: {
            nodeList: {
              type: String,
              default: function () {
                  return []
              }
            },
            title: {
                type: String,
                default: ''
            },
            eHeight: {
                type: String,
                default: function () {
                    return ''
                }
            }
        },
        data: function () {
            return {
                open: true
            }
        },
        methods: {},
        beforeMount: function () {

        },
        mounted: function () {

        }
    })
}(jQuery);