var Slider = Vue.component('slider', {
    template: '<div class="slider"><div class="slider-handler" :class="handlerClass" :style="{left: this.handlerLeft}" v-dragged.prevent="onDragged"><div v-if="hasTooltip" class="slider-tooltip"><div class="arrow"></div><div class="slider-val">{{moveVal}}</div></div></div></div>',
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
        }
    },
    data: function () {
        return {
            width: '',
            moveVal: 0,
            isMove: false
        }
    },
    computed: {
        handlerLeft: function () {
            return (this.value / this.max) * 100 + '%'
        },
        handlerClass: function () {
            return {
                'slider-handler-hover': this.isMove
            }
        }
    },
    methods: {
        onDragged: function (params) {
            if (params.first) {
                this.width = +window.getComputedStyle(params.el.parentNode)['width'].slice(0, -2) || 0;
                this.isMove = true;
                this.dragged = true;
                return
            }

            if (params.last) {
                var lastLeft = +window.getComputedStyle(params.el)['left'].slice(0, -2) || 0;
                var stepWidth = this.width * (this.step / this.max);
                var lastVal;
                var moveVal;
                lastVal = params.direction === 'right' ? Math.ceil((lastLeft / stepWidth)) : Math.floor((lastLeft / stepWidth));
                moveVal = lastVal * stepWidth / this.width * 100;
                this.moveVal = moveVal = moveVal.toFixed(0);
                params.el.style.left = moveVal + '%';
                this.$emit('slide-complete', [moveVal]);
                this.isMove = false;
                this.dragged = false;
                return
            }

            var l = +window.getComputedStyle(params.el)['left'].slice(0, -2) || 0;
            if (l + params.deltaX > this.width) {
                return
            }
            if (l + params.deltaX < 0) {
                return
            }
            var moveLeft = (l + params.deltaX) / this.width * 100;
            params.el.style.left = moveLeft + '%';
            this.moveVal = moveLeft.toFixed(0);
            this.isMove = true;
        }
    },
    beforeMount: function () {

    },
    mounted: function () {

    }
});