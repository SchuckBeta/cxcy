var groupText = Vue.component('group-text', {
    template: '<div> ' +
    '<div data-field="attrs/text/text" class="field content-editable-field"><label>文字</label> ' +
    '<div class="input-wrapper"><textarea @change="changeText($event)" rows="3" style="width: 188px;">{{text.text}}</textarea>' +
    '<div tabindex="1" contenteditable="true" @blur="changeText($event)" data-type="content-editable" data-attribute="attrs/text/text"' +
    ' class="content-editable" style="display: none;"></div>' +
    '</div>' +
    '</div> <div data-field="attrs/text/font-size" class="field range-field"><label class="with-output">字体大小</label>' +
    ' <output>{{text.fontSize}}</output> ' +
    '<span class="units">px</span> ' +
    '<input type="range" name="range" min="5" max="80" v-model="text.fontSize" data-type="range" @change="changeRoomNameFontSize($event)" data-attribute="attrs/text/font-size" class="range"></div>' +
    ' <div data-field="attrs/text/font-family" class="field select-box-field"><div><label>字体</label> ' +
    '<div data-attribute="attrs/text/font-family" data-type="select-box" class="joint-select-box joint-theme-modern">' +
    '<div class="select-box-selection">' +
    '<select class="select-control" v-model="text.fontFamily" @change="changeRoomNameFontFamily($event)"><option value="Helvetica">Helvetica</option><option value="Arial">Arial</option></select></div>' +
    '</div></div></div> ' +
    '<div data-field="attrs/text/fill" class="field color-palette-field"><div><label>字体颜色</label> ' +
    '<div data-attribute="attrs/text/fill" data-type="color-palette" class="joint-select-box joint-color-palette joint-theme-modern">' +
    '<div class="select-box-selection"><div class="select-box-option-content" @click="fillTextColor($event)" :style="{backgroundColor: text.fill}">' +
    '<img v-if="text.fill === transparent" src="' + ctxImages + '/transparent-icon.png" class="select-box-option-icon"></div></div></div></div></div></div>',
    props: {
        text: {
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
    data: function () {
        return {
            fill: 'fill',
            transparent: 'transparent',
            open: true
        }
    },
    filters: {
      textBlank: function (val) {
          var textArr = val.split('\n');
          return textArr.join('<br>')
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
        changeRoomNameFontSize: function () {
            this.text.ele.attr('fontSize', this.text.fontSize + 'px')
        },
        changeRoomNameFontFamily: function ($event) {
            this.text.ele.attr('fontFamily', this.text.fontFamily);
        },
        changeText: function ($event) {
            EventListener.$emit('changeTRText',  $event.target.value);
        },
        fillTextColor: function ($event) {
            var position = this.getPosition($event);
            EventListener.$emit('changeTRTextColor', position);
        },
    },
    mounted: function () {
    }
})