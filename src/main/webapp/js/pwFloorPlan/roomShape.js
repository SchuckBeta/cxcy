+function ($, Vue) {
    var roomShape = Vue.component('room-shape', {
        template: '<div class="group" :class="[customClass, {groupOpened: visibleIndex === index}]">\n' +
        '        <h4 class="group-label"><i class="iconfont iconfont-open" @click.stop="handleChangeIndex"></i><i class="iconfont iconfont-close" @click.stop="handleChangeIndex"></i>{{title}}\n' +
        '        </h4>\n' +
        '        <div class="shape-elements" v-show="visibleIndex === index">\n' +
        '            <svg v-generate-group="{shapesXml: shapesXml}" version="1.1" width="100%" height="100%" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"></svg>\n' +
        '        </div>\n' +
        '    </div>',
        props: {
            visibleIndex: {
                type: Number,
                default: 0
            },
            index: {
                type: Number,
            },
            shapesXml: Array,
            title: String,
            customClass: String
        },
        directives: {
            generateGroup: {
                inserted: function (element, binding, vnode) {
                    var shapesXml = binding.value.shapesXml;
                    var snap, html = '', group;
                    var groups, height = 0, width;
                    var groupWidth, groupHeight;
                    vnode.context.snap = Snap(element);
                    snap = vnode.context.snap;
                    group = snap.group();
                    for (var s = 0; s< shapesXml.length; s++) {
                        var shape = shapesXml[s];
                        html += shape.content;
                    }
                    group.node.innerHTML = html;
                    groups = group.selectAll('g.room-translate');
                    for (var i = 0; i < groups.length; i++) {
                        var item = groups[i];
                        var BBox = item.getBBox();
                        if (i % 2 === 0) {
                            width = 0;
                        } else {
                            width = groups[i - 1].getBBox().width + 10
                        }
                        item.attr('transform', 'translate(' + width + ',' + height + ')');
                        if (i % 2 === 1) {
                            height += BBox.y + BBox.height + 15;
                        }
                        item.data('shape-index', i);
                        item.drag(function move(dx, dy, x, y, event) {
                            vnode.context.$emit('mousemove', {shape: shapesXml[this.data('shape-index')], offset: {left: x - groupWidth / 2, top: y - groupHeight / 2}})
                        }, function start(x, y, event) {
                            var BBox = this.getBBox();
                            groupWidth = BBox.width;
                            groupHeight = BBox.height;
                            vnode.context.$emit('mousedown')
                        }, function up(event) {
                            vnode.context.$emit('mouseup', {e: event, innerSVG: this.innerSVG()})

                        })
                    }
                }
            }
        },
        data: function () {
            return {
                open: true,
                snap: ''
            }
        },
        methods: {
            handleChangeIndex: function () {
                this.$emit('change-index', {
                    visibleIndex: this.visibleIndex,
                    index: this.index
                })
            }
        }
    });
}(jQuery, Vue)