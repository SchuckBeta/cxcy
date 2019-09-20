/**
 * Created by Administrator on 2018/7/17.
 */

'use strict';


Vue.component('buttonExportFile', {
    template: '<div style="display: inline-block;vertical-align: top">' +
    '<el-button type="primary" size="mini" :disabled="disabled" @click.stop.prevent="exportFileVisible = true"><i class="iconfont icon-daochubiaoge"></i>导出附件</el-button>' +
    '<el-dialog title="导出附件" :visible.sync="exportFileVisible" width="596px" top="25vh">\n' +
    '        <el-form :model="exportFileForm" class="mgb-20" ref="exportFileForm" style="text-align: center;">\n' +
    '            <el-radio-group v-model="exportFileForm.exportFileRadio">\n' +
    '                <el-radio label="0">导出当前查询条件{{label}}附件</el-radio>\n' +
    '                <el-radio label="1">导出全部{{label}}附件</el-radio>\n' +
    '            </el-radio-group>\n' +
    '        </el-form>\n' +
    '        <div slot="footer" class="dialog-footer">\n' +
    '            <el-button size="mini" @click.stop.prevent="exportFileVisible = false">取 消</el-button>\n' +
    '            <el-button size="mini" type="primary" @click.stop.prevent="exportFile">确 定\n' +
    '            </el-button>\n' +
    '        </div>\n' +
    '    </el-dialog></div>',
    props: {
        searchListForm: Object,
        disabled: Boolean,
        label:String,
        isHsxm: Boolean
    },
    data: function () {
        return {
            exportFileVisible: false,
            exportFileForm: {
                exportFileRadio: '1'
            },

        }
    },
    computed: {
        paramsKey: function () {
            var keys = ['proCategory','deuser.office.id', 'queryStr', 'finalStatus', 'beginDate', 'endDate'];
            if(this.isHsxm){
                return keys.map(function (item) {
                    return 'proModel.'+item;
                })
            }
            return keys;
        }
    },
    methods: {
        exportFile: function () {
            var self = this;
            this.$axios.get('/exp/expByGnode?' + this.getParams()).then(function (response) {
                var data = response.data;
                if (data.ret === 1) {
                    self.$emit('export-file-start')
                } else {
                    self.$alert(data.msg, '提示', {
                        confirmButtonText: '确定',
                        type: 'error'
                    });
                }
            });
            self.exportFileVisible = false;
        },
        getParams: function () {
            var paramsKey = this.paramsKey;
            var proCategory = this.searchListForm[paramsKey[0]] || '';
            var officeId = this.searchListForm[paramsKey[1]] || '';
            var queryStr = this.searchListForm[paramsKey[2]] || '';
            var finalStatus = this.searchListForm[paramsKey[3]] || '';
            var beginDate = this.searchListForm[paramsKey[4]] || '';
            var endDate = this.searchListForm[paramsKey[5]] || '';
            var checkedRadio = this.exportFileForm.exportFileRadio;
            var params = {
                isAll: checkedRadio,
                gnodeId: this.searchListForm.gnodeId,
                actywId: this.searchListForm.actywId
            };
            if(checkedRadio !== '1'){
                Object.assign(params, {
                    proCategory: proCategory,
                    officeId: officeId,
                    queryStr: queryStr,
                    beginDate: beginDate,
                    endDate:endDate
                })
            }
            return Object.toURLSearchParams(params)
        }
    },
    created: function () {
    }
})