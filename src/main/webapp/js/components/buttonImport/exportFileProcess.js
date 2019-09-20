/**
 * Created by Administrator on 2018/7/20.
 */

'use strict';


Vue.component('export-file-process', {
    template: '<div v-show="visible" class="export-file-process-container"><a href="javascript: void(0);" @click.stop.prevent="isExportDetailShow = !isExportDetailShow"><i class="iconfont icon-daochubiaoge" :class="{\'is-show\': isExportDetailShow}"></i></a><transition name="exp-fp-wrap"><div v-show="isExportDetailShow" class="table-container"><el-row :gutter="10" label-width="92px">' +
    '       <el-col :span="12"><e-col-item align="right" label="名称：">{{file.name}}</e-col-item></el-col><el-col :span="12"><e-col-item align="right" label="总记录数：">{{file.total}}</e-col-item></el-col>' +
    '<el-col :span="12"><e-col-item align="right" label="开始时间：">{{file.startDate | formatDateFilter(\'YYYY-MM-DD\')}}</e-col-item></el-col><el-col :span="12"><e-col-item align="right" label="状态：">{{file.isComplete | selectedFilter(stateEntries)}}</e-col-item></el-col>' +
    '</el-row><div class="text-right" style="margin-bottom: 18px;"><el-button type="primary" size="mini" v-show="file.isComplete  == 1" @click.stop.prevent="downFile">下载文件</el-button></div></div></transition></div>',
    props: {
        gnodeId: String,
        visible: Boolean,
        menuName: String
    },
    data: function () {
        var menuName = this.menuName + '.zip';
        return {
            isExportDetailShow: false,
            exportTimer: null,
            hideTimer: null,
            isFirst: true,
            file: {
                name: menuName
            },
            stateEntries: {
                '0': '文件准备中...',
                '1': '可下载'
            }
        }
    },
    watch: {
        visible: function (value) {
            if(value) {
                this.isExportDetailShow = true
            }
        }
    },
    methods: {
        downFile: function () {
            if (this.file.sa) {
                location.href = this.frontOrAdmin + '/ftp/ueditorUpload/downFile?url=' + this.file.sa.url
                this.exportFileVisible = false;
            }
        },
        getExpInfoSub: function (id) {
            var self = this;
            this.$axios.get('/exp/getGnodeExpInfo?eid=' + id).then(function (response) {
                var data = response.data;
                if (data) {
                    Object.assign(self.file, data);
                    if (data.isComplete == '1') {
                        self.isExportDetailShow = true;
                        self.timeOut();
                        clearInterval(self.exportTimer)
                    }
                } else {
                    clearInterval(self.exportTimer);
                    self.$alert('请刷新页面，重新导出附件', '提示', {
                        confirmButtonText: '确定',
                        type: 'error'
                    }).then(function () {
                        location.reload();
                    });
                }
            })
        },
        getExpInfo: function () {
            var self = this;
            this.$axios({
                method: 'GET',
                url: '/exp/getExpGnode?gnodeId=' + this.gnodeId,
                timeout: 1000 * 10,
            }).then(function (response) {
                var data = response.data;
                if (!data) {
                    //如果不是第一次重新刷新页面
                    if (!self.isFirst) {
                        location.reload();
                    }
                    return
                }
                Object.assign(self.file, data);
                if (typeof data.id !== 'undefined') {
                    self.exportTimer = setInterval(function () {
                        self.getExpInfoSub(data.id);
                    }, 5000);
                }
                self.isFirst = false;
            })
        },

        timeOut: function () {
            var self = this;
            this.hideTimer && clearTimeout(this.hideTimer);
            this.hideTimer = setTimeout(function () {
                self.isExportDetailShow = false;
            }, 1000 * 2)
        }
    },
    mounted: function () {
        this.getExpInfo();
        var self = this;
        // this.timeOut();
        window.addEventListener('scroll', function () {
            self.isExportDetailShow = false;
        })
    }
})