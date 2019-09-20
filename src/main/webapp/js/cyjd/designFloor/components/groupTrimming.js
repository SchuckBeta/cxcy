var groupTrimming = Vue.component('group-trimming', {
    template: '<div class="group" :class="{groupOpened: open}"><template v-if="false">' +
    '<h4 class="group-label">' +
    '<i class="iconfont iconfont-open" @click="open = true"></i>' +
    '<i class="iconfont iconfont-close" @click="open = false"></i>' +
    '<span>{{trimming.name}}</span>' +
    '<a href="javascript:void(0);" @click="deleteEle($event)"><i class="iconfont iconfont-delete">&#xe625;</i></a></h4>' +
    '<div data-field="attrs/rect/stroke-width" class="field range-field">' +
    '<img :src="trimming.attr.href" style="display: block;margin: 0 auto;max-width: 50px;"></div>' +
    '</template><template v-if="trimming.shapeType === rect">' +
    '<h4 class="group-label"><i class="iconfont iconfont-open" @click="open = true"></i><i class="iconfont iconfont-close" @click="open = false"></i><span>{{trimming.children[0].text | trim}}</span>' +
    '<a href="javascript:void(0);" @click="deleteEle($event)"><i class="iconfont iconfont-delete">&#xe625;</i></a></h4>' +
    '<div data-field="attrs/rect/fill" class="field color-palette-field"><div><label>填充背景色</label>' +
    ' <div data-attribute="attrs/rect/fill" data-type="color-palette" class="joint-select-box">' +
    '<div class="select-box-selection"><div class="select-box-option-content" :style="{backgroundColor: trimming.fill}" ' +
    '@click="fillBackgroundColor($event)"><img v-if="trimming.fill === transparent" :src="ctxImages + \'/transparent-icon.png\'" class="select-box-option-icon"></div></div></div></div></div>' +
    '<div data-field="attrs/rect/stroke" class="field color-palette-field"><div><label>描边</label> ' +
    '<div data-attribute="attrs/rect/stroke" data-type="color-palette" class="joint-select-box">' +
    '<div class="select-box-selection"><div class="select-box-option-content" :style="{backgroundColor: trimming.stroke}" @click="strokeColor($event)">' +
    '<img v-if="trimming.stroke === transparent" :src="ctxImages + \'/transparent-icon.png\'" class="select-box-option-icon"></div></div></div></div></div>' +
    '<div data-field="attrs/rect/stroke-width" class="field range-field">' +
    '<label class="with-output">边框粗细</label> <output>{{trimming.strokeWidth}}</output> ' +
    '<span class="units">px</span>' +
    ' <input type="range" name="range" min="0" max="30" step="1" data-type="range" v-model="trimming.strokeWidth" @change="changeWidth($event)" data-attribute="attrs/rect/stroke-width" class="range">' +
    '</div><group-text v-if="trimming.children" :text="trimming.children[0]"></group-text></template></div>',
    props: {
        trimming: {
            type: Object,
            default: function () {
                return {}
            }
        }
    },
    data: function () {
        return {
            image: 'image',
            radio: '',
            rect: 'rect',
            transparent: 'transparent',
            open: true
        }
    },
    filters: {
      trim: function (val) {
          return val.replace(/(\r\n)|(\n)/g, '')
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
        fillBackgroundColor: function ($event) {
            var position = this.getPosition($event);
            EventListener.$emit('changeTrimmingBgColor', [this.trimming, position]);
        },
        strokeColor: function ($event) {
            var position = this.getPosition($event)
            EventListener.$emit('changeTrimmingStrokeColor', [this.trimming, position])
        },
        changeWidth: function () {
            this.trimming.ele.attr('strokeWidth', this.trimming.strokeWidth + 'px')
        },
        deleteEle: function ($event) {
            this.$emit('delete-trimming')
        },
    },
    mounted: function () {

    }
})