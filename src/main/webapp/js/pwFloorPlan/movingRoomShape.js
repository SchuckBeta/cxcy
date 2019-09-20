+function ($, Vue) {
    var movingRoomShape = Vue.component('moving-room-shape', {
        template: '<div v-show="visible" class="room-shape_box" :style="moveStyle" :class="[customClass]">\n' +
        '        <div class="room-shape_moving">\n' +
        '            <svg v-generate-shape version="1.1" width="100%" height="100%"\n' +
        '                 xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"></svg>\n' +
        '        </div>\n' +
        '    </div>',
        props: {
            visible: {
                type: Boolean,
                default: false
            },
            roomShape: Object,
            customClass: String,
            moveStyle: Object
        },
        data: function () {
            return {
                snap: ''
            }
        },
        watch: {
            roomShape: function (value) {
                if(!this.snap){
                    return
                }
                this.snap.select('g.shape-group').node.innerHTML = value.content;
            }
        },
        directives: {
            generateShape: {
                inserted: function (element, binding, vnode) {
                    var snap,group;
                    vnode.context.snap = Snap(element)
                    snap = vnode.context.snap
                    group = snap.group().addClass('shape-group');
                }
            }
        }
    })
}(jQuery, Vue)