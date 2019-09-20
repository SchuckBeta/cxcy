+function ($, Vue) {
    var uploaderIcon = Vue.component('uploader-icon', {
        template: '<div class="upload-image-box" v-show="active"><input type="file" name="nodeIcon" ref="file" accept=".jpg,.jpeg,.png"  @change="selectImage($event)"><button type="button" class="btn btn-primary" v-show="active" @click="triggerSelectImage">本地图片</button></div>',
        props: {
            active: {
                type: Boolean,
                default: false
            }
        },
        methods: {
            selectImage: function ($event) {
                var self = this;
                var file, rFilter, oFReader, type;
                if (!$event.target.files.length) {
                    return false;
                }
                file = $event.target.files[0];
                oFReader = new FileReader();
                if (window.FileReader) {
                    oFReader.onload = function (oFREvent) {
                        // self.paperBgImage = oFREvent.target.result
                        self.$emit('local-image', [oFREvent.target.result, file])
                    }
                    oFReader.readAsDataURL(file);
                }
                if (navigator.appName === "Microsoft Internet Explorer") {
                    console.log($event)
//                    $modalAvatarArea[0].filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = $(this).value;
                }
            },
            triggerSelectImage: function () {
                this.$refs['file'].click();
            }
        }
    })
}(jQuery, Vue)