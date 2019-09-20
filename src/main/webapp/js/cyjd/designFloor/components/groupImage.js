var groupImage = Vue.component('group-image', {
    template: '<div class="group" :class="{groupOpened: open}"><h4 class="group-label">' +
    '<i class="iconfont iconfont-open" @click="open = true">&#xe63b;</i>' +
    '<i class="iconfont iconfont-close" @click="open = false">&#xe63e;</i>' +
    '<span>{{image.name}}</span><a href="javascript:void(0);" @click="deleteEle($event)"><i class="iconfont iconfont-delete">&#xe625;</i></a></h4>' +
    '<div data-field="attrs/rect/stroke-width" class="field range-field"><img :src="image.attr.href" style="display: block;margin: 0 auto;max-width: 50px;"></div>' +
    '<div data-field="attrs/image/1" class="field range-field"><label class="with-output">放大</label>' +
    ' <output>{{image.attr.width}}</output> <span class="units">px</span>' +
    ' <input type="range" name="range" min="5" max="200" step="1" data-type="range" v-model="image.attr.width" @change="changeWidth($event)" data-attribute="attrs/rect/stroke-width" class="range"></div></div>',
    props: {
        image: {
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
            transparent: 'transparent',
            radio: '',
            open: false
        }
    },
    methods: {
        changeWidth: function ($event) {
            var radio = this.radio;
            var width = this.image.attr.width;
            var height = this.image.attr.height;
            this.image.ele.attr({
                'width': width,
                'height': width / radio
            });
        },
        deleteEle: function ($event) {
            this.$emit('delete-room-asset', this.index)
        }
    },
    mounted: function () {
        this.radio = this.image.attr.width / this.image.attr.height;
    }
})