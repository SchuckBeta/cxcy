var groupDesc = Vue.component('group-desc', {
    template: '<div v-if="desc.type" class="group" :class="{groupOpened: open}">' +
    '<h4 class="group-label"><i class="iconfont iconfont-open" @click="open = true"></i>' +
    '<i class="iconfont iconfont-close" @click="open = false"></i><span>{{desc.children[0].text}}</span><a href="javascript:void(0);" @click="deleteRoom($event)"><i class="iconfont iconfont-delete">&#xe625;</i></a></h4>' +
    '<div  class="field select-box-field"><div><label>房间名</label>' +
    '<div data-type="select-box" class="joint-select-box">' +
    '<div class="select-box-selection">' +
    '<select class="select-control" v-model="desc.roomId" @change="changeRoomId($event)"><option value="">房间名</option> <option v-for="room in rooms" :value="room.roomId">{{room.roomName}}</option></select></div></div></div></div>' +
    '<div data-field="attrs/rect/fill" class="field color-palette-field"><div><label>填充背景色</label> ' +
    '<div data-attribute="attrs/rect/fill" data-type="color-palette" class="joint-select-box">' +
    '<div class="select-box-selection"><div class="select-box-option-content" :style="{backgroundColor: desc.fill}" @click="fillBackgroundColor(desc, $event)">' +
    '<img v-if="desc.fill === transparent" :src="transparentImg" class="select-box-option-icon">' +
    '</div></div></div></div></div><div data-field="attrs/rect/stroke" class="field color-palette-field"><div><label>描边</label>' +
    ' <div data-attribute="attrs/rect/stroke" data-type="color-palette" class="joint-select-box"><div class="select-box-selection">' +
    '<div class="select-box-option-content" :style="{backgroundColor: desc.stroke}" @click="strokeColor(desc, $event)"><img v-if="desc.stroke === transparent" :src="transparentImg" class="select-box-option-icon"></div></div></div></div>' +
    '</div><div data-field="attrs/rect/stroke-width" class="field range-field"><label class="with-output">边框粗细</label> <output>{{desc.strokeWidth}}</output> <span class="units">px</span> ' +
    '<input type="range" name="range" min="0" max="30" step="1" @change="changeBorderWidth($event)" v-model="desc.strokeWidth" data-type="range" data-attribute="attrs/rect/stroke-width" class="range"></div>' +
    '<div data-field="attrs/text/font-family" class="field select-box-field"><div><label>字体</label> <div data-attribute="attrs/text/font-family" data-type="select-box" class="joint-select-box joint-theme-modern"><div class="select-box-selection">' +
    '<select class="select-control" @change="changeRoomNameFontFamily" v-model="desc.children[0].fontFamily"><option value="Helvetica">Helvetica</option><option value="Arial">Arial</option></select></div></div></div></div>' +
    '<div  data-field="attrs/rect/stroke-width" class="field range-field"><label class="with-output">字体大小</label> ' +
    '<output>{{desc.children[0].fontSize}}</output> <span class="units">px</span> ' +
    '<input type="range" name="range" min="6" max="80" step="1" @change="changeRoomNameFontSize" v-model="desc.children[0].fontSize" data-type="range" data-attribute="attrs/rect/stroke-width" class="range"></div>' +
    '<div data-field="attrs/rect/stroke" class="field color-palette-field"><div><label>字体颜色</label> <div data-attribute="attrs/rect/stroke" data-type="color-palette" class="joint-select-box"><div class="select-box-selection">' +
    '<div class="select-box-option-content" :style="{backgroundColor: desc.children[0].fill}" @click="fontColor(desc.children[0], $event)"><img v-if="desc.children[0].fill === transparent" :src="transparentImg" class="select-box-option-icon"></div></div></div></div></div>' +
    '</div>',
    props: {
        transparentImg: {
            type: String,
            default: '/images/transparent-icon.png'
        },
        desc: {
            type: Object,
            default: function () {
                return {}
            }
        },
        rooms: {
            type: Array,
            default: function () {
                return []
            }
        }
    },
    data: function () {
        return {
            transparent: 'transparent',
            fill: 'fill',
            stroke: 'stroke',
            text: 'text',
            index: 'children',
            open: true
        }
    },
    methods: {
        getPosition: function ($event) {
            var winW = $(window).width();
            var winH = $(window).height();
            var maxWidth = winW - 148;
            var maxHeight = winH - 150;
            var $target = $($event.target);
            var tLeft = $target.offset().left;
            var tTop = $target.offset().top + 30;
            var left = Math.min(tLeft, maxWidth);
            var top;
            var isBottom = tTop <= maxHeight;
            var arrowLeft = tLeft === left ? 8 : ((tLeft - left) + 8);
            if (isBottom) {
                top = Math.min(tTop, maxHeight)
            } else {
                top = tTop - 150;
            }
            return {
                left: left,
                top: top,
                arrowLeft: arrowLeft
            }
        },

        changeRoomId: function ($event) {
            // var childrenEle = this.desc.children[0].ele;
            // var attr = childrenEle.attr();
            // ele.data('roomId', this.desc.roomId);
            // this.desc.children[0].ele.remove();
            this.desc.children[0].text = $($event.target).find('option:selected').text();
            this.$emit('change-room', this.desc);
        },
        changeBorderWidth: function ($event) {
            this.desc.ele.attr('strokeWidth', this.desc.strokeWidth)
        },
        changeRoomNameFontSize: function () {
            this.desc.children[0].ele.attr('fontSize', this.desc.children[0].fontSize + 'px')
        },
        changeRoomNameFontFamily: function () {
            this.desc.children[0].ele.attr('fontFamily', this.desc.children[0].fontFamily);
        },
        fillBackgroundColor: function (desc, $event) {
            var position = this.getPosition($event);
            EventListener.$emit('changeBgColor', [desc, position]);
        },
        strokeColor: function (desc, $event) {
            var position = this.getPosition($event)
            EventListener.$emit('changeStrokeColor', [desc, position])
        },
        fontColor: function (obj, $event) {
            var position = this.getPosition($event)
            EventListener.$emit('changeFontColor', [obj, position])
        },
        deleteRoom: function ($event) {
            this.$emit('delete-room')
        }
    },
    beforeMount: function () {
    },
    mounted: function () {
        // console.log(this.desc)
    }
});