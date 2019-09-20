var colorPicker = Vue.component('color-picker', {
    template: '<div v-show="colorPickerShow" @blur="blur" tabindex="-1" class="rd-select-box-options rd-select-box rd-color-palette rd-color-picker" :style="style">' +
    '<div class="select-box-option-content select-box-option" :class="{hover: color === transparent}" @click="changeColor(transparent, $event)" style="background-color: transparent;"><img :src="transparentImg" class="select-box-option-icon"></div>' +
    '<div class="select-box-option-content select-box-option" v-for="item in colors" @click="changeColor(item, $event)" :class="{hover: hexColor === item}" :style="{backgroundColor: item}"></div>' +
    '<div class="select-box-options-arrow" :style="arrowStyle"></div></div>',
    model: {
        prop: 'colorPickerShow',
        event: 'change'
    },
    props: {
        colorPickerShow: {
            type: Boolean,
            default: false
        },
        roomData: {
            type: Object,
            default: function () {
                return {}
            }
        },
        arrowStyle: {
            type: Object,
            default: function () {
                return {
                    left: '8px'
                }
            }
        },
        color: {
            type: String,
            default: ''
        },
        colors: {
            type: Array,
            default: function () {
                return ['#ffffff', '#e9442d', '#dcd7d7', '#8f8f8f', '#c6c7e2', '#feb663', '#b75d32', '#31d0c6', '#7c68fc', '#61549c', '#6a6c8a', '#4b4a67', '#3c4260','#222138', '#333333']
            }
        },
        transparentImg: {
            type: String,
            default: '/images/transparent-icon.png'
        },
        style: {
            type: Object,
            default: function () {
                return {}
            }
        }
    },
    data: function () {
        return {
            transparent: 'transparent'
        }
    },
    watch: {
        colorPickerShow: function (val) {
            var self = this;
            if (val) {
                setTimeout(function () {
                    $(self.$el).focus()
                }, 13)
            }
        }
    },
    computed: {
        hexColor: function () {
            if (this.color.indexOf('rgb') > -1) {
                var color = this.color.replace('rgb(', '');
                color = color.replace(')', '');
                color = color.split(',');
                return '#' + this.rgbaToHex(color[0], color[1], color[2])
            } else {
                return this.color;
            }
        }
    },
    methods: {
        changeColor: function (color, $event) {
            this.$emit('change-color', color);
        },
        rgbaToHex: function (r, g, b) {
            return ((r << 16) | (g << 8) | b).toString(16);
        },
        blur: function () {
            this.$emit('color-hide')
        }
    },
    mounted: function () {
    }
})