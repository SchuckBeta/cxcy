var Slider = Vue.component('slider', {
    template: '<div class="slider" :class="{disabled: disabled}"><div class="slider-handler" :class="handlerClass" :style="{left: this.handlerLeft}" v-dragged.prevent="onDragged"><div v-if="hasTooltip" class="slider-tooltip"><div class="arrow"></div><div class="slider-val">{{moveVal}}</div></div></div></div>',
    props: {
        min: {
            type: Number,
            default: 0
        },
        max: {
            type: Number,
            default: 100
        },
        step: {
            type: Number,
            default: 1
        },
        value: {
            type: Number,
            default: 0
        },
        hasTooltip: {
            type: Boolean,
            default: true
        },
        disabled: {
            type: Boolean,
            default: false
        }
    },
    data: function () {
        return {
            width: '',
            moveVal: 0,
            isMove: false,
            moveEndValue: 0
        }
    },
    computed: {
        handlerLeft: function () {
            return (this.value / this.max) * 100 + '%'
        },
        handlerClass: function () {
            return {
                'slider-handler-hover': this.isMove,
                'disabled': this.disabled
            }
        }
    },
    watch: {
        // value: function (value) {
        //     this.moveVal = value;
        // },
        moveEndValue: function (value) {
            this.$emit('input', value);
        }
    },
    methods: {
        onDragged: function (params) {
            if (this.disabled) {
                return
            }
            if (params.first) {
                this.width = +window.getComputedStyle(params.el.parentNode)['width'].slice(0, -2) || 0;
                this.isMove = true;
                this.dragged = true;
                return
            }

            if (params.last) {
                if (this.step > 1) {
                    var ys = this.moveVal % this.step;
                    this.moveVal = this.moveVal - ys;
                }
                this.moveEndValue = this.moveVal;
                this.isMove = false;
                this.dragged = false;
                return
            }

            var l = +window.getComputedStyle(params.el)['left'].slice(0, -2) || 0;
            if (l + params.deltaX > this.width) {
                return
            }
            if (l + params.deltaX < (this.min / this.max) * this.width) {
                return
            }
            var moveLeft = (l + params.deltaX) / this.width * 100;
            moveLeft = moveLeft > 100 ? 100 : moveLeft;
            params.el.style.left = moveLeft + '%';
            this.moveVal = (moveLeft / 100 * this.max).toFixed(0);
            this.isMove = true;
        }
    },
    beforeMount: function () {

    },
    mounted: function () {

    }
});