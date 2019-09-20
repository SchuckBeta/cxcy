/**
 * Created by Administrator on 2018/6/19.
 */

;+function (Vue) {
    'use strict';

    var cropperPic = Vue.component('cropper-pic', {
        template: ' <div class="cropper-pic-container" :class="[customClass]" style="width: 400px; height: 400px;"> <img ref="cropperImg" v-cropper="{params: cParams}" :src="imgSrc || defaultImg" style="max-width: 100%;"> </div>',
        props: {
            imgSrc: String,
            defaultImg: {
                type: String,
                default: '/img/u4110.png'
            },
            copperParams: {
                type: Object,
                default: function () {
                    return {}
                }
            },
            disabled: Boolean,
            customClass: String
        },
        computed: {
            cParams: {
                get: function () {
                    return Object.assign({}, this.defaultParams, this.copperParams)
                }
            }
        },
        watch: {
            imgSrc: function (value) {
                $(this.$refs.cropperImg).cropper('replace', (value || this.defaultImg));
            },
            disabled: function (value) {
                $(this.$refs.cropperImg).cropper(value ? 'disabled' : 'enable');
            }
        },
        data: function () {
            return {
                defaultParams: {
                    aspectRatio: 1,
                    viewMode: 1,
                    checkCrossOrigin: false,
                    movable: false,
                    rotatable: false,
                    scalable: false,
                    zoomable: false,
                    zoomOnWheel: false,
                    minContainerWidth: 50,
                    minContainerHeight: 50,
                    minCropBoxWidth: 50,
                    minCropBoxHeight: 50
                }
            }
        },
        directives: {
            cropper: {
                inserted: function (element, binding, vnode) {
                    var copperParams = binding.value.params;
                    $(element).cropper(copperParams);
                }
            }
        },
        methods: {
            getImageData: function () {
                return $(this.$refs.cropperImg).cropper('getImageData');
            },
            getData: function () {
                return $(this.$refs.cropperImg).cropper('getData');
            },
            getCroppedCanvas: function () {
                return $(this.$refs.cropperImg).cropper('getCroppedCanvas');
            }
        }
    })
}(Vue)