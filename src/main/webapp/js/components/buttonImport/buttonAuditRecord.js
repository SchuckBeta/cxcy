/**
 * Created by Administrator on 2018/7/19.
 */

'use strict';


Vue.component('button-audit-record', {
    template: '<span class="btn-audit-record-content"><el-tooltip class="item" content="审核记录" popper-class="white" placement="bottom"><el-button type="text" class="btn-icon-font" icon="iconfont icon-genzongjilu" @click.stop="getAuditRecord"></el-button></el-tooltip>' +
    '<el-dialog :visible.sync="auditRecordVisible" title="提示" width="70%" :before-close="handleClose">' +
    '<div class="table-container" v-loading="loading"><table class="table table-default" style="margin-bottom: 0"><thead><tr><th>审核动作</th><th>审核时间</th><th>审核人</th><th>审核结果</th><th style="width:55%;">建议及意见</th></tr></thead><tbody>' +
    '<tr v-for="(auditRecord, index) in auditRecordList" :key="index"><template v-if="auditRecord.id">' +
    '<td>{{auditRecord.auditName}}</td><td>{{auditRecord.updateDate}}</td><td>{{auditRecord.user ? auditRecord.user.name : \'-\'}}</td><td>{{auditRecord.result}}</td><td style="word-break: break-all">{{auditRecord.suggest}}</td>' +
    '</template><template v-else><td colspan="5" class="score-row" style="text-align: right">{{auditRecord.auditName}}：{{auditRecord.result}}</td></template></tr>' +
    '<tr v-show="!auditRecordList.length"><td colspan="5"><span class="emtpy empty-color">无审核记录</span></td></tr></tbody></table>' +
    '</div>' +
    '<div slot="footer" class="dialog-footer"> <el-button type="primary" @click="auditRecordVisible = false" size="mini" style="padding-left: 10px;padding-right: 10px;">确定</el-button></div></el-dialog></span>',
    props: {
        row: Object
    },
    data: function () {
        return {
            auditRecordVisible: false,
            auditRecordList: [],
            loading: true
        }
    },
    methods: {
        handleClose: function () {
            this.auditRecordList = [];
            this.auditRecordVisible = false;
        },
        getAuditRecord: function () {
            var self = this;
            this.auditRecordVisible = true;
            this.loading = true;
            this.$axios.get('/promodel/proModel/auditInfo/'+ this.row.id).then(function (response) {
                self.auditRecordList = response.data;
                self.loading = false;
            })
        }
    }
});