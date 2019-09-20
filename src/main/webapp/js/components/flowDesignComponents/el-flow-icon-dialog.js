

Vue.component('el-flow-icon-dialog', {
    template: '<el-dialog\n' +
    '            title="修改"\n' +
    '            :visible.sync="dialogVisibleIconUrl" :close-on-click-modal="false"\n' +
    '            width="562px"\n' +
    '            :before-close="handleCloseIconUrl">\n' +
    '        <el-tabs v-model="activeTab" type="card" size="small" @tab-click="tabClick">\n' +
    '            <el-tab-pane class="fdf-icon-tab-pane" label="默认图片" name="defaultIcon">\n' +
    '                <el-row :gutter="10">\n' +
    '                    <el-col :span="4" v-for="(item, index) in iconUrls" :key="item.id">\n' +
    '                        <div class="fdf-icon" :class="{selected: selectIndex === index}" @click.stop.prevent="selectIndex = index">\n' +
    '                            <img :src="item.ftpUrl">\n' +
    '                        </div>\n' +
    '                    </el-col>\n' +
    '                </el-row>\n' +
    '            </el-tab-pane>\n' +
    '            <el-tab-pane label="本地图片" name="uploadIcon">\n' +
    '                <div class="text-center">\n' +
    '                    <el-upload\n' +
    '                            action="/a/ftp/ueditorUpload/uploadTemp?folder=ueditor"\n' +
    '                            :show-file-list="false"\n' +
    '                            name="upfile"\n' +
    '                            accept="jpeg/png">\n' +
    '                        <img src="http://localhost:8080/images/upload-default-image100X100.png">\n' +
    '                        <i class="el-icon-plus avatar-uploader-icon"></i>\n' +
    '                        <div class="el-upload__tip" slot="tip">\n' +
    '                            仅支持上传jpg/png文件，建议icon大小：100*100（像素）\n' +
    '                        </div>\n' +
    '                    </el-upload>\n' +
    '                </div>\n' +
    '            </el-tab-pane>\n' +
    '        </el-tabs>\n' +
    '        <span slot="footer" class="dialog-footer">\n' +
    '    <el-button @click.stop.prevent="handleCloseIconUrl" size="mini">取 消</el-button>\n' +
    '    <el-button type="primary" @click="dialogVisibleIconUrl = false" size="mini">确 定</el-button>\n' +
    '  </span>\n' +
    '    </el-dialog>',
    computed: {
        dialogVisibleIconUrl: function () {
            return this.$store.state.dialogVisibleIconUrl
        }
    },

    data: function () {
        return {
            selectIndex: -1,
            iconUrls: [],
            activeTab: 'defaultIcon',
            uploadDefaultUrl: 'http://localhost:8080/images/upload-default-image100X100.png'
        }
    },

    methods: {
        handleCloseIconUrl: function () {
            this.selectIndex = -1;
            this.activeTab = 'defaultIcon';
            this.$store.dispatch('changeDialogVisibleIconUrl', false);
        },
        getIconUrls: function () {
            var self = this;
            this.$axios.get('/a/attachment/sysAttachment/ajaxIcons/gnode').then(function (response) {
                var data = response.data;
                self.iconUrls = data || [];
            })
        },
        tabClick: function (item) {
            if (this.activeTab !== 'defaultIcon') {
                this.selectIndex = -1;
            }
        }
    },
    mounted: function () {
        this.getIconUrls();
    }
})