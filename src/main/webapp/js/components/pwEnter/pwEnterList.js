'use strict';
Vue.pwEnterAuditListMixin = {
    data: function () {
        return {
            pwEnterAuditList: []
        }
    },
    computed: {
        pwEnterAuditEntries: function () {
            return this.getEntries(this.pwEnterAuditList)
        },
    },
    methods: {
        getPwEnterStatus: function () {
            var self = this;
            this.$axios.get('/pw/pwEnter/getPwEnterAuditList').then(function (response) {
                self.pwEnterAuditList = response.data || []
            }).catch(function (error) {

            })
        },
    },
    created: function () {
        this.getPwEnterStatus();
    }
};

Vue.component('pw-enter-column', {
    template: '<div class="pw-enter-column">\n' +
    '        <div class="pec-header"><slot name="header"></slot></div>\n' +
    '        <div class="pec-body"><slot></slot>\n' +
    '\n' +
    '        </div>\n' +
    '        <slot name="footer"></slot>\n' +
    '    </div>'
})

Vue.component('pw-record-line', {
    template: '<div class="pec-footer"  v-show="pwRecordLine.length > 0"><div v-if="pwRecordLine.length > 0" class="pw-enter-record-list">\n' +
    '            <span class="per-label">入驻历史记录：</span>\n' +
    '            <div v-for="list in pwDoubleList" class="per-list-content">\n' +
    '                <div v-for="item in list" class="per-item">\n' +
    '                    <div class="tag">\n' +
    '                        <el-popover\n' +
    '                                placement="right"\n' +
    '                                title="建设及意见："\n' +
    '                                width="200"\n' +
    '                                trigger="hover"\n' +
    '                                :content="item.remarks" :disabled="item.status!=\'2\'">\n' +
    '                            <span slot="reference" class="base-tag" :class="baseTagClassName(item.status)">{{item.status | selectedFilter(auditEntries)}}</span>\n' +
    '                        </el-popover>\n' +
    '                    </div>\n' +
    '                    <h5 class="title"><a href="javascript: void(0);" @click.stop.prevent="goToForm(item)">{{item.frontBgremarks}}</a></h5>\n' +
    '                    <p class="date">{{item.createDate | formatDateFilter(\'YYYY-MM-DD HH:mm:ss\')}}</p>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </div></div>',
    props: {
        params: Object,
        auditEntries: Object
    },
    data: function () {
        return {
            pwRecordLine: [],
            isPwEnterAuditTypes: ['10', '20']//入驻类型
        }
    },
    computed: {
        pwDoubleList: function () {
            var step = 5;
            var result = [];
            for (var x = 0; x < Math.ceil(this.pwRecordLine.length / step); x++) {
                var start = x * 5;
                var end = start + 5;
                result.push(this.pwRecordLine.slice(start, end));
            }
            return result;
        },
        isPassed: function () {
            var recordItem;
            if(this.pwRecordLine.length > 0){
                recordItem = this.pwRecordLine[this.pwRecordLine.length - 1];
                return recordItem.status !== '2' && this.isPwEnterAuditTypes.indexOf(recordItem.type) === -1;
            }
            return false;
        }
    },

    methods: {
        getPwRecordLine: function () {
            var self = this;
            this.$axios.post('/pw/pwApplyRecord/ajaxFindAuditList', this.params).then(function (response) {
                var data = response.data;
                if(data.status === 1){
                    var pwRecordLine = data.data || [];
                    pwRecordLine = pwRecordLine.sort(function (item1, item2) {
                        var time1 = moment(item1.createDate).valueOf();
                        var time2 = moment(item2.createDate).valueOf();
                        if (time1 > time2) {
                            return 1
                        } else if (time1 < time2) {
                            return -1
                        } else {
                            return 0
                        }
                    })
                    self.pwRecordLine = pwRecordLine;
                    if(self.pwRecordLine.length > 0){
                        self.$emit('get-audit-status', {auditStatus: self.pwRecordLine[pwRecordLine.length - 1].status, id: self.params.eid})
                    }

                }
            }).catch(function (error) {

            })
        },

        baseTagClassName: function (audited) {
            return {
                'base-tag-success': audited === '1',
                'base-tag-error': audited === '2'
            }
        },

        goToForm: function (item) {
            var pageName = this.isPassed ? 'view' : 'view';
            var params = {id: item.parentId || item.eid};
            if(!this.isPassed){
                // params['status'] = '0';
            }
            location.href = this.frontOrAdmin + '/pw/pwEnter/'+pageName+'?'+ Object.toURLSearchParams(params);
        }
    },
    created: function () {
        this.getPwRecordLine();
    }
})