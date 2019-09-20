/**
 * Created by Administrator on 2018/7/17.
 */
'use strict';


Vue.component('button-import', {
    template: '<el-button type="primary" v-show="isFirstMenu || isFirstTask" size="mini" icon="iconfont icon-daoru" @click.stop="toImpPage">导入</el-button>',
    props: {
        isFirstMenu: {
            type: Boolean,
            default: false
        },
        href: String,
        actywId: String,
        gnodeId: String,
        isQueryMenu: Boolean
    },
    data: function () {
        return {
            isFirstTask: false
        }
    },
    methods: {
        getIsFirstTask: function () {
            var self = this;
            var gnodeId = this.gnodeId;
            return this.$axios.get('/actyw/gnode/ajaxGetFirstTaskByYwid/' + this.actywId).then(function (response) {
                var data = response.data;
                var datas = data.datas;
                if(data.status){
                    if (datas.id === gnodeId || datas.parentId === gnodeId) {
                        self.isFirstTask = true;
                    }
                }
            })
        },
        toImpPage: function () {
            var actywId = this.actywId;
            var gnodeId = this.gnodeId;
            if(!this.isQueryMenu){
                if (!gnodeId) {
                    this.$alert('流程节点不能为空', '提示', {
                        confirmButtonText: '确定',
                        type: 'warning'
                    });
                    return false;
                }
            }
            location.href = this.ctx +  this.href + Object.toURLSearchParams({
                    gnodeId: gnodeId,
                    actywId: actywId
                })
        }
    },
    created: function () {
        this.getIsFirstTask();
    }
})