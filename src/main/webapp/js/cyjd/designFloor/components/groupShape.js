/**
 * Created by 王清腾 on 2017/12/17.
 */

+function ($) {
    var groupShape = Vue.component('group-shape', {
        template: '<div class="group" :class="{groupOpened: open}"> ' +
        '<h4 class="group-label"><i class="iconfont iconfont-open" @click="open = true">&#xe63b;</i><i class="iconfont iconfont-close" @click="open = false">&#xe63e;</i>{{title}}</h4> ' +
        '<div v-show="open" class="shape-elements" :style="{height: height}"> ' +
        '<slot name="svg"></slot> ' +
        '</div> </div>',
        props: {
            height: {
                type: String,
                default: ''
            },
            shape: {
                type: Object,
                default: function () {
                    return {}
                }
            },
            title: {
                type: String,
                default: ''
            }
        },
        computed: {
            shapeData: function () {
                return {
                    shapeData: this.shape,
                    id: '#shapeDragSvg'
                }
            }
        },
        data: function () {
            return {
                open: true
            }
        }
    })
}(jQuery)