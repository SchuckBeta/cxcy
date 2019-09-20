/**
 * Created by Administrator on 2017/11/10.
 */

var CertEle = Vue.component('cert-ele', {
    template: '<div class="certificate-crop-box" v-show="data.isShow" :class="className" :style="style" @click="chooseCropBox">' +
    '<template v-if="boxType !== font"><span class="certificate-view-box"> <img v-dragged.prevent="draggedWater":src="data.resource"> </span></template>' +
    '<template v-if="boxType === font"><span class="certificate-view-box"> <img v-dragged.prevent="draggedWater" v-generate-text="{data:data,ratio:ratio}"> </span> </template>' +
    '<drag-line v-if="boxType !== font" direction="east" :min="50" :max="300" :ratio="ratio" @drag-complete="dragLineComplete"></drag-line>' +
    '<drag-line v-if="boxType !== font" direction="north" :min="50" :max="300" :ratio="ratio" @drag-complete="dragLineComplete"></drag-line>' +
    '<drag-line v-if="boxType !== font" direction="west" :min="50" :max="300" :ratio="ratio" @drag-complete="dragLineComplete"></drag-line>' +
    '<drag-line v-if="boxType !== font" direction="south" :min="50" :max="300" :ratio="ratio" @drag-complete="dragLineComplete"></drag-line></div>',
    props: {
        data: {
            type: Object,
            default: function () {
                return {}
            }
        },
        ratio: {
            type: [String, Number],
            default: 1
        },
        boxType: {
            type: String,
            default: 'pic'
        },
        isSelected: {
            type: Boolean,
            default: false
        },
        certEleType: {
            type: String,
            default: ''
        },
        certTextCur: {
            type: Number,
            default: ''
        }
    },
    data: function () {
        return {
            font: 'font'
        }
    },
    computed: {
        width: function () {
            return (this.data.width * this.ratio + 'px')
        },
        style: function () {
            return {
                width: this.width,
                left: this.data.xlt + 'px',
                top: this.data.ylt + 'px',
                opacity: this.data.opacity / 100,
                transform: 'rotate(' + this.data.rate + 'deg)'
            }
        },
        className: function () {
            return {
                'certificate-crop-box-selected': this.isSelected
            }
        }
    },
    methods: {
        dragLineComplete: function (params) {
        },
        draggedWater: function (params) {
            if (params.first) {
                this.dragged = true;
                return
            }

            if (params.last) {
                this.dragged = false;
                return
            }
            var l = +window.getComputedStyle(params.el)['left'].slice(0, -2) || 0;
            var t = +window.getComputedStyle(params.el)['top'].slice(0, -2) || 0;
            var offsetX = l + params.deltaX;
            var offsetY = t + params.deltaY;
            var $certPicBox = $('.cert-pic-box');
            var maxWidth = ($certPicBox.width() - $(params.el).width());
            var maxHeight = ($certPicBox.height() - $(params.el).height());


           if(offsetX  < 0){
               offsetX = 0
           }
           if(offsetY < 0){
               offsetY = 0
           }

           if(offsetX > maxWidth){
               offsetX = maxWidth
           }

           if(offsetY > maxHeight){
               offsetY = maxHeight
           }

            params.el.style.left = (offsetX) + 'px';
            params.el.style.top = (offsetY) + 'px';


            $(params.el).parents('.certificate-crop-box').css({
                'left': (offsetX),
                'top': (offsetY)
            })
        },
        chooseCropBox: function () {
            this.$emit('choose-crop-box', [this.certEleType, this.certTextCur])
        }
    },
    mounted: function () {
    }
})