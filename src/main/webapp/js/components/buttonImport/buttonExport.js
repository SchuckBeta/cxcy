'use strict';


Vue.component('button-export', {
    template: '<div style="display: inline-block;vertical-align: top"><el-button type="primary" size="mini" icon="iconfont icon-daochu" @click.stop.prevent="exportVisible = true">导出 </el-button>' +
    '       <el-dialog :title="\'导出\' + label" :visible.sync="exportVisible" width="598px" top="25vh">\n' +
    '        <el-form :model="exportForm" ref="exportForm" style="text-align: center;">\n' +
    '            <el-radio-group v-model="exportForm.exportRadio">\n' +
    '                <el-radio label="0" name="isAll">导出当前查询条件{{label}}</el-radio>\n' +
    '                <el-radio label="1" name="isAll">导出全部{{label}}</el-radio>\n' +
    '            </el-radio-group>\n' +
    '            <div class="export-symbol-component">\n' +
    '                <strong>自定义导出表单中的分隔符</strong>\n' +
    '                <div>\n' +
    '                    <span>团队成员姓名和学号之间的分隔符</span>\n' +
    '                    <el-select v-model="exportForm.defaultPref" size="mini" \n' +
    '                               style="width:60px;margin-right: 10px;">\n' +
    '                        <el-option v-for="stuSymbol in spiltPrefs" :label="stuSymbol.remark"\n' +
    '                                   :value="stuSymbol.key" :key="stuSymbol.key"></el-option>\n' +
    '                    </el-select>\n' +
    '\n' +
    '                    <span style="margin-left:20px;">多个成员之间的分隔符</span>\n' +
    '\n' +
    '                    <el-select v-model="exportForm.defaultPost" size="mini"\n' +
    '                               style="width:60px;">\n' +
    '                        <el-option v-for="teamSymbol in spiltPosts" :label="teamSymbol.remark"\n' +
    '                                   :value="teamSymbol.key" :key="teamSymbol.key"></el-option>\n' +
    '                    </el-select>\n' +
    '                    <div class="export-example">{{exampleText}}\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </el-form>\n' +
    '        <div slot="footer" class="dialog-footer">\n' +
    '            <el-button size="mini" @click.stop.prevent="exportVisible = false">取 消</el-button>\n' +
    '            <el-button size="mini" type="primary" @click.stop.prevent="exportSubmit">确 定</el-button>\n' +
    '        </div>\n' +
    '    </el-dialog></div>',

    props: {
        spiltPrefs: {
            type: Array,
            default: function () {
                return []
            }
        },
        spiltPosts: {
            type: Array,
            default: function () {
                return []
            }
        },
        label: String,
        searchListForm: Object,
        isHsxm: Boolean
    },

    data: function () {

        return {
            exportVisible: false,
            symbols: ['()'],
            exportForm: {
                exportRadio: '1',
                defaultPref: '',
                defaultPost: ''
            }
        }
    },
    computed: {
        spiltPrefsEntries: {
            get: function () {
                return this.getEntries(this.spiltPrefs, {
                    label: 'remark',
                    value: 'key'
                })
            }
        },
        spiltPostsEntries: {
            get: function () {
                return this.getEntries(this.spiltPrefs, {
                    label: 'remark',
                    value: 'key'
                })
            }
        },
        defaultPref: {
            get: function () {
                var value = '';
                for (var i = 0; i < this.spiltPrefs.length; i++) {
                    if (this.spiltPrefs[i].isDef) {
                        value = this.spiltPrefs[i].key;
                        break;
                    }
                }
                return value;
            }
        },
        defaultPost: {
            get: function () {
                var value = '';
                for (var i = 0; i < this.spiltPosts.length; i++) {
                    if (this.spiltPosts[i].isDef) {
                        value = this.spiltPosts[i].key;
                        break;
                    }
                }
                return value;
            }
        },

        exampleText: {
            get: function () {
                var txt = '';
                var defaultPref = this.exportForm.defaultPref || this.defaultPref;
                var defaultPost = this.exportForm.defaultPost || this.defaultPost;
                var prefText, postText;
                var prefSy = this.spiltPrefsEntries[defaultPref];
                var postSy =  this.spiltPrefsEntries[defaultPost];
                if(this.symbols.indexOf(prefSy) > -1){
                    prefText = prefSy.substring(0,1) + '123456' + prefSy.substring(1)
                }else {
                    prefText = prefSy + '123456'
                }
                txt += '示例：张三'+ prefText+postSy+'李四' + prefText + '（导师同理）';
                return txt;
            }
        },

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
    watch: {
        exportVisible: function (value) {
            if (value) {
                this.$nextTick(function () {
                    this.$refs['exportForm'].resetFields();

                    this.exportForm.defaultPref = this.defaultPref;
                    this.exportForm.defaultPost = this.defaultPost;
                })
            }
        }
    },
    methods: {
        getParams: function () {
            var paramsKey = this.paramsKey;
            var proCategory = this.searchListForm[paramsKey[0]] || '';
            var officeId = this.searchListForm[paramsKey[1]] || '';
            var queryStr = this.searchListForm[paramsKey[2]] || '';
            var finalStatus = this.searchListForm[paramsKey[3]] || '';
            var beginDate = this.searchListForm[paramsKey[4]] || '';
            var endDate = this.searchListForm[paramsKey[5]] || '';
            var checkedRadio = this.exportForm.exportRadio;
            var params = {
                isAll: checkedRadio,
                prefix: this.exportForm.defaultPref,
                postfix: this.exportForm.defaultPost,
                gnodeId: this.searchListForm.gnodeId,
                actywId: this.searchListForm.actywId,
                finalStatus: finalStatus
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
        },
        exportSubmit: function () {
            var self = this;
            location.href = self.frontOrAdmin + '/exp/expData/' + this.searchListForm.actywId + '?' + self.getParams();
            // this.$axios({
            //     method: 'get',
            //     url: '/exp/expData/' + this.searchListForm.actywId + '?' + this.getParams(),
            //     responseType: 'stream'
            // }).then(function (response) {
            //     var data = response.data;
            //     if (data.ret == 0) {
            //         self.$message({
            //             message: data.msg,
            //             type: 'error'
            //         })
            //     } else {
            //         location.href = self.frontOrAdmin + '/exp/expData/' + self.searchListForm.actywId + '?' + self.getParams()
            //     }
            // });
            this.exportVisible = false;
        }
    },

})