/**
 * Created by lqz on 2018/6/19.
 */

'use strict';

var eFileItem = Vue.component('e-file-item', {
    template: '<div class="e-file-item" :class="{\'e-file-item_mini\': size == \'mini\'}">' +
    '<a class="e-file-item_name" href="javascript: void(0);" :title="file.name" @click.stop.prevent="viewFile"><img :src="file.suffix | fileSuffixFilter" >{{file.name | textMiddleEllipsis}}</a>' +
    '<a title="下载文件" class="e-file-item_downfile" href="javascript: void(0);" @click.stop.prevent="downFile"><i class="iconfont icon-custom-down"></i></a>' +
    '<a title="删除文件" class="e-file-item_delete_file" v-show="show" href="javascript: void(0);" @click.stop.prevent="$emit(\'delete-file\', file)"><i class="iconfont icon-delete1"></i></a></div>',
    props: {
        file: Object,
        size: String,
        show:Boolean,
        downUrl: String
    },
    methods: {
        viewFile: function () {
            if (!this.file.viewUrl) {
                this.$message({type: 'error', message: '暂不支持预览，请下载查阅'});
                return false;
            }
            window.open(this.file.viewUrl);
        },
        downFile: function () {
            location.href =this.downUrl || this.frontOrAdmin + "/ftp/ueditorUpload/downFile?url=" + this.file.url + "&fileName=" + encodeURI(encodeURI(this.file.name));
        }
    }
})

