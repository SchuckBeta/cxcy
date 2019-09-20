/**
 * Created by Administrator on 2018/7/19.
 */


'use strict';
Vue.component('pro-progress', {
    template: '<div class="progress-nodes" :class="hasData()">' +
    '                   <div class="progress-node" :class="nodeClass(node)" v-for="(node, index) in timeLineData">\n' +
    '                        <div class="prg-node-time">{{node.time | formatDateFilter(\'YYYY-MM-DD\')}}</div>\n' +
    '                        <div class="prg-node-icon">' +
    '                           <span v-show="!showIconOrCircle(node)" class="circle"></span>\n' +
    '                            <i v-show="showIconOrCircle(node)" class="iconfont icon-dingwei"></i>\n' +
    '                        </div>\n' +
    '                        <div class="prg-node-content">\n' +
    '                            <label class="node-name">{{node.name}}</label>\n' +
    '                            <div class="prg-files">\n' +
    '                                <div class="prg-file" v-for="(file, index2) in node.fileList"  :key="file.id">\n' +
    '                                    <e-file-item :file="file"  size="mini" @delete-file="handleDeleteFile(file, index2, node.fileList)"></e-file-item><p class="author"><span>提交人：{{file.userName}}</span></p>\n' +
    '                                </div>\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                    </div><div v-if="!timeLineData.length" class="empty empty-color"><span>暂无项目进度跟踪</span></div>' +
    '</div>',
    model: {
        prop: 'timeLineData',
        event: 'change'
    },
    props: {
        timeLineData: {
            type: Array,
            default: function () {
                return [{
                    files: []
                }]
            }
        },
        customClass: String
    },
    data: function () {
        return {}
    },
    methods: {
        nodeClass: function (node) {
            return [
                this.customClass,
                {
                    'progress-node-highlight': node.type === 'node' && node.status == '1',
                    'progress-node-not_start': node.type === 'node' && node.status == '0',
                    'progress-node-normal': !node.type
                }
            ]
        },
        showIconOrCircle: function (node) {
            return node.type === 'node'
        },
        hasData: function () {
            return [{
                'none-progress-node': !this.timeLineData.length
            }]
        },
        handleDeleteFile: function (file, index, fileList) {
            var self = this;
            if (!file.id) {
                fileList.splice(index, 1);
                return false;
            }
            this.$confirm('确定删除文件吗', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(function () {
                self.$axios({
                    method: 'POST',
                    url: '/ftp/ueditorUpload/delFile',
                    data: Object.toURLSearchParams({id: file.id, url: file.url})
                }).then(function (response) {
                    var data = response.data;
                    if (data) {
                        self.show$message({
                            status: true,
                            msg: '删除成功'
                        });
                        fileList.splice(index, 1);
                        return false;
                    }
                    self.show$message({
                        status: false,
                        msg: data.msg || '删除失败'
                    })
                })

            }).catch(function () {

            })
        }
    }
})