/**
 * Created by Administrator on 2018/8/2.
 */


'use strict';


Vue.component('export-ex-pro', {
    template: '<el-button type="primary" size="mini" @click.stop.prevent="exportExPro" :disabled="disabled"><i class="iconfont icon-icon-project"></i>发布优秀项目</el-button>',
    props: {
        disabled: Boolean,
        fids: Array,
        projectType:String
    },
    computed: {
        postIds: {
            get: function () {
                var ids = [];
                for (var i = 0; i < this.fids.length; i++) {
                    ids.push(this.fids[i].id);
                }
                return ids;
            }
        }
    },
    methods: {
        exportExPro: function () {
            var self = this;
            this.$confirm('确定发布所选项目到门户网站？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(function () {
                self.exportXhr();
            })
        },
        exportXhr: function () {
            var self = this;
            var url = '/cms/cmsArticle/excellent/projectPublish?ids=' + this.postIds.join(',');
            if(this.projectType == 'gcontest'){
                url = '/cms/cmsArticle/excellent/gcontestPublish?ids=' + this.postIds.join(',');
            }
            this.$axios.post(url).then(function (response) {
                var data = response.data || {};
                if(data){
                    self.$message({
                        message: data.status == '1' ? '发布成功' : data.msg || '发布失败',
                        type: data.status == '1' ? 'success' : 'error'
                    });
                }
            })
        }
    }

})