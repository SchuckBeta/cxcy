/**
 * Created by Administrator on 2018/6/19.
 */
'use strict';

var ePicFile = Vue.component('e-pic-file', {
    template: ' <div class="user-pic_file"> <el-button type="primary" :disabled="disabled" size="mini" @click.stop.prevent="$refs.userPic.click()">{{imgSrc ? \'修改图片\' : \'上传图片\'}} </el-button> <input type="file" ref="userPic" @change="handleChangePic($event)" accept="image/jpeg,image/png" style="position: absolute; clip: rect(0px, 0px, 0px, 0px);"> <input type="hidden" :name="name"> </div>',
    model: {
        prop: 'imgSrc',
        event: 'change'
    },
    props: {
        imgSrc: String,
        name: String,
        disabled: Boolean
    },
    methods: {
        handleChangePic: function (event) {
            var files = event.target.files;
            var file, fileReader;
            var self = this;
            if (files && files.length < 1) {
                // this.$refs.userPic.value = '';
                self.$refs.userPic.setAttribute('type', 'text');
                self.$refs.userPic.setAttribute('type', 'file');
                this.$emit('get-file', null);
                return false;
            }
            try {
                file = files[0];
                fileReader = new FileReader();
                fileReader.onload = function (e) {
                    self.$emit('change', e.target.result);
                    self.$emit('get-file', file);
                    // event.target.files = null;
                    // self.$refs.userPic.value = '';
                    self.$refs.userPic.setAttribute('type', 'text');
                    self.$refs.userPic.setAttribute('type', 'file');
                };
                fileReader.readAsDataURL(file);
            }catch (e){
                this.$alert('该浏览器版本过低，请更换浏览器版本， 或者更换谷歌浏览器，上传图片，已获取更好的体验', {
                    type:'warning'
                })
            }
        }
    },
    emptyFile: function () {
        this.$refs.userPic.files = null;
        this.$emit('get-file', null);
    }
})
