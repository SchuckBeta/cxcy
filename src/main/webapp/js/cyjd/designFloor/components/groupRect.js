var groupRect = Vue.component('group-rect', {
    template: '<div class="group" :class="{groupOpened: open}">' +
    '<h4 class="group-label">' +
    '<i class="iconfont iconfont-open" @click="open = true">&#xe63b;</i>' +
    '<i class="iconfont iconfont-close" @click="open = false">&#xe63e;</i><span>背景</span>' +
    '<a href="javascript:void(0);" @click="deleteEle($event)"><i class="iconfont iconfont-delete">&#xe625;</i></a></h4>' +
    '<div data-field="attrs/rect/fill" class="field color-palette-field"><div><label>填充背景色</label>' +
    ' <div data-attribute="attrs/rect/fill" data-type="color-palette" class="joint-select-box">' +
    '<div class="select-box-selection"><div class="select-box-option-content" :style="{backgroundColor: rect.fill}" @click="fillBackgroundColor($event)">' +
    '<img v-if="rect.fill === transparent" src="' + ctxImages + '/transparent-icon.png" class="select-box-option-icon"></div></div></div></div></div>' +
    '<div data-field="attrs/rect/stroke" class="field color-palette-field"><div><label>描边</label> ' +
    '<div data-attribute="attrs/rect/stroke" data-type="color-palette" class="joint-select-box"><div class="select-box-selection">' +
    '<div class="select-box-option-content" :style="{backgroundColor: rect.stroke}" @click="strokeColor($event)"><img v-if="rect.stroke === transparent" src="' + ctxImages + '/transparent-icon.png" class="select-box-option-icon">' +
    '</div></div></div></div></div><div data-field="attrs/rect/stroke-width" class="field range-field"><label class="with-output">边框粗细</label> ' +
    '<output>{{rect.strokeWidth}}</output> <span class="units">px</span> ' +
    '<input type="range" v-model="rect.strokeWidth" @change="changeStrokeWidth($event)" name="range" min="0" max="30" step="1" data-type="range" data-attribute="attrs/rect/stroke-width" class="range"></div>' +
    '<div data-field="attrs/rect/stroke-width" class="field range-field"><label class="with-output">放大</label> ' +
    '<output>{{rect.attr.width}}</output> <span class="units">px</span>' +
    ' <input type="range" name="range" min="5" max="200" step="1" @change="changeWidth($event)" v-model="rect.attr.width" data-type="range" data-attribute="attrs/rect/stroke-width" class="range"></div></div>',
    props: {
        rect: {
            type: Object,
            default: function () {
                return {}
            }
        },
        index: {
            type: Number,
            default: ''
        }
    },
    computed: {},
    data: function () {
        return {
            transparent: 'transparent',
            radio: '',
            open: false
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
        changeStrokeWidth: function ($event) {
            this.rect.ele.attr('strokeWidth', this.rect.strokeWidth)
        },
        changeWidth: function ($event) {
            var radio = this.radio;
            var width = this.rect.attr.width;
            this.rect.ele.attr({
                'width': width,
                'height': width / radio
            });
        },
        fillBackgroundColor: function ($event) {
            var position = this.getPosition($event);
            EventListener.$emit('changeRoomAssetBgColor', [this.index, position, this.rect]);
        },
        strokeColor: function ($event) {
            var position = this.getPosition($event)
            EventListener.$emit('changeRoomAssetStrokeColor', [this.index, position, this.rect])
        },
        deleteEle: function ($event) {
            this.$emit('delete-room-asset', this.index)
        }
    },
    mounted: function () {
        this.radio = this.rect.attr.width / this.rect.attr.height;
    }
})