<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>
<div id="app" v-show="pageLoad" class="container-fluid mgb-60" style="display: none">
    <edit-bar></edit-bar>
    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="conditions">
            <e-condition type="radio" label="所属学院" :options="colleges" :default-props="officeProps"
                         v-model="searchListForm['user.office.id']" @change="getScrApplyList"></e-condition>
            <e-condition type="radio" label="学分类别" :options="scrRuleCategories" :default-props="officeProps"
                         v-model="searchListForm['creditName']" @change="getScrApplyList"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-input">
                <el-date-picker
                        v-model="createDateRange"
                        type="daterange"
                        range-separator="至"
                        size="mini"
                        clearable
                        class="w300"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        :default-time="defaultTime"
                        @change="handleChangeCreateDateRange"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
                </el-date-picker>
                <input type="text" style="display: none">
                <el-input size="mini" name="keyWords" v-model="searchListForm.keys" placeholder="学号/姓名/认定内容（级别/标准）/专业"
                          @keyup.enter.native="getScrApplyList" class="w300">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getScrApplyList"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>
    <div class="table-container">
        <el-table v-loading="loading" :data="cpScrApplyList" size="mini" class="table"
                  @sort-change="handleSortScoApplyList">
            <el-table-column prop="u.no" width="286" label="申请人信息" sortable="u.no">
                <template slot-scope="scope">
                    <credit-user-column :user="scope.row.user" cn-name></credit-user-column>
                </template>
            </el-table-column>
            <el-table-column prop="creditName" label="学分类别" sortable="custom" align="center">
                <template slot-scope="scope">
                    {{scope.row.creditName}}
                </template>
            </el-table-column>
            <el-table-column prop="srd.name" label="认定内容及标准" sortable="srd.name">
                <template slot-scope="scope">
                    <el-tooltip
                            v-if="!!scope.row.scoRuleNames"
                            :content="scope.row.scoRuleNames"
                            popper-class="white" placement="right"><span class="break-ellipsis">{{scope.row.scoRuleNames}}</span>
                    </el-tooltip>
                    <div>
                        {{scope.row.name}}
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="credit" label="认定学分" align="center" sortable="credit">
                <template slot-scope="scope">
                    {{scope.row.credit}}
                </template>
            </el-table-column>
            <el-table-column prop="auditDate" label="认定日期" sortable="custom" align="center">
                <template slot-scope="scope">
                    {{scope.row.auditDate | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>

            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button size="mini" type="text" @click.stop="handleChangeCredit(scope.row)">修改学分</el-button>
                        <el-button size="mini" type="text" @click.stop="goToView(scope.row)">查看</el-button>
                        <el-button size="mini" type="text" @click.stop="confirmDelApplyItem(scope.row)">删除</el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>

        <div class="text-right mgb-20">
            <el-pagination
                    size="small"
                    @size-change="handlePSizeChange"
                    background
                    @current-change="handlePCPChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="searchListForm.pageSize"
                    layout="total,prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>

    </div>
    <el-dialog
            title="修改学分"
            :visible.sync="dialogVisibleCredit"
            width="520px"
            :close-on-click-modal="false"
            :before-close="handleCloseCredit">
        <component ref="formComponent" :is="formName" :form="forms[formName]" :disabled="disabled"></component>
        <span slot="footer" class="dialog-footer">
    <el-button type="primary" size="mini" @click.stop="validateCredit" :disabled="disabled">确 定</el-button>
  </span>
    </el-dialog>
</div>


<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        mixins: [Vue.creditDetailMixin],
        components: {CreditChangeFormSingle: CreditChangeFormSingle, CreditChangeFormMany: CreditChangeFormMany},
        data: function () {


            return {
                searchListForm: {
                    'user.office.id': '',
                    'creditName': '',
                    pageSize: 10,
                    pageNo: 1,
                    auditStartDate: '',
                    auditEndDate: '',
                    keys: ''
                },
                defaultTime: ['00:00:00', '23:59:59'],
                pageCount: 0,
                createDateRange: [],
                officeList: [],
                scoRuleList: [],
                scrApplyList: [],
                loading: false,
                officeProps: {
                    label: 'name',
                    value: 'id'
                },

                dialogVisibleCredit: false,
                forms: {
                    CreditChangeFormSingle: {},
                    CreditChangeFormMany: {
                        scoRsumList: []
                    }
                },
                formName: 'CreditChangeFormSingle',
                disabled: false,
                keepNpoint: '0'
            }
        },
        computed: {
            valRules: function () {
                var validateScore = this.validateScore;
                return [
                    {required: true, message: '请输入学分', trigger: 'blur'},
                    {validator: validateScore, trigger: 'blur'}
                ]
            },

            cpScrApplyList: function () {
                return this.scrApplyList.map(function (item) {
                    var scoRuleList = item.scoRuleList || [];
                    var scoRuleNames = scoRuleList.reduce(function (name, rule) {
                        var ruleName = rule.name;
                        if (rule.parentId === '1') {
                            ruleName = '';
                        }
                        if (ruleName) {
                            return name + ' ' + ruleName;
                        }
                        return name;
                    }, '1');
                    item.scoRuleNames = scoRuleNames.substring(1);
                    return item
                })
            },
            colleges: function () {
                return this.officeList.filter(function (item) {
                    return item.grade === '2';
                })
            },
            scrRuleCategories: function () {
                return this.scoRuleList.filter(function (item) {
                    return item.type === '2';
                })
            },
            scoRuleListEntryNames: function () {
                return this.getEntries(this.scoRuleList, this.officeProps)
            },
            auditStatueEntries: function () {
                return this.getEntries(this.auditStatues, {
                    label: 'name',
                    value: 'key'
                })
            },
            scoRuleListEntries: function () {
                var entries = {};
                var list = [].concat(this.scoRuleList, []);
                var i = 0;
                while (i < list.length) {
                    var item = list[i];
                    entries[item.id] = item;
                    i++;
                }
                return entries;
            },
        },
        methods: {


            confirmDelApplyItem: function (item) {
                var self = this;
                this.$confirm('确认删除这条学分申请记录吗？', '提示', {
                    type: 'warning',
                    confirmButtonText: '确定',
                    cancelButtonText: '取消'
                }).then(function () {
                    self.delApplyItem(item);
                }).catch(function () {

                })
            },

            delApplyItem: function (item) {
                var self = this;
                this.$axios.get('/scr/scoCreditQuery/ajaxDeleteScoCredit?id=' + item.id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$message.success('删除成功');
                        self.getScrApplyList();
                    } else {
                        self.$message.error(data.msg);
                    }
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg);
                })
            },


            handleChangeCreateDateRange: function (value) {
                if (value && value.length > 0) {
                    this.searchListForm.auditStartDate = value[0];
                    this.searchListForm.auditEndDate = value[1];
                } else {
                    this.searchListForm.auditStartDate = '';
                    this.searchListForm.auditEndDate = '';
                }
                this.getScrApplyList();
            },

            getScrApplyList: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/scr/scoCreditQuery/listPage?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || {};
                        self.scrApplyList = data.list || [];
                        self.searchListForm.pageNo = data.pageNo || 1;
                        self.searchListForm.pageSize = data.pageSize || 10;
                        self.pageCount = data.count || 0;
                    } else {
                        self.scrApplyList = [];
                        self.pageCount = 0;
                        self.$message.error(data.msg);
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            handleSortScoApplyList: function (obj) {
                this.searchListForm.orderBy = obj.prop;
                this.searchListForm.orderByType = obj.order ? (obj.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getScrApplyList();
            },


            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getScrApplyList();
            },

            handlePCPChange: function () {
                this.getScrApplyList();
            },

            getScrRuleList: function () {
                var self = this;
                this.$axios.get('/scr/scoRule/getScoRuleList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.scoRuleList = data.data || [];
                    }
                })
            },


            getOfficeList: function () {
                var self = this;
                this.$axios.get('/sys/office/getOfficeList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.officeList = data.data || []
                    }
                })
            },

            goToView: function (row) {
                location.href = this.frontOrAdmin + '/scr/scoRapply/view?scrId=' + row.id + '&creditType=1';
            },

            getCreditList: function (row) {
                var self = this;
                this.$axios.get('/scr/scoCreditQuery/ajaxupdateValList?apply.id=' + row.id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data;
                        if (data && data.length > 0) {
                            if (data.length === 1) {
                                self.forms.CreditChangeFormSingle = data[0];
                                self.formName = 'CreditChangeFormSingle';
                            } else {
                                self.forms.CreditChangeFormMany = {
                                    scoRsumList: data
                                };
                                self.formName = 'CreditChangeFormMany';
                            }
                            self.$nextTick(function () {
                                self.$refs.formComponent && self.$refs.formComponent.updateCreditForm();
                            })
                        } else {
                            self.formName = '';
                            self.$message.error(data.msg)
                        }
                    }
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg)
                })
            },

            handleChangeCredit: function (row) {
                this.dialogVisibleCredit = true;
                this.getCreditList(row);
            },

            handleCloseCredit: function () {
                this.dialogVisibleCredit = false;
            },

            validateCredit: function () {
                var self = this;
                var formComponent = this.$refs.formComponent;
                formComponent.$refs.creditForm.validate(function (valid) {
                    if (valid) {
                        self.submitCredit();
                    }
                })
            },
            submitCredit: function () {
                var self = this;
                this.disabled = true;
                var params = this.getCreditDataParams();
                this.$axios.post('/scr/scoCreditQuery/ajaxupdateVal', params).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.handleCloseCredit();
                        self.getScrApplyList();
                        self.$message.success('保存成功');
                    } else {
                        self.$message.error(data.msg)
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg);
                    self.disabled = false;
                })
            },

            getCreditDataParams: function () {
                var formComponent = this.$refs.formComponent;
                var self = this;
                if (this.formName === 'CreditChangeFormSingle') {
                    return {
                        scoRsumList: [formComponent.$data.creditForm]
                    }
                } else {
                    var scoRsumList = formComponent.$data.creditForm.scoRsumList.map(function (item) {
                        var rsum = {
                            apply: {
                                id: ''
                            },
                            user: {
                                id: ''
                            },
                            val: ''
                        };
                        return self.assignFormData(rsum, item)
                    })
                    return {
                        scoRsumList: scoRsumList
                    }
                }
            },

            getCreditRule: function () {
                var self = this;
                this.$axios.get('/scr/scoRset/list').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || [];
                        if (data.length > 0) {
                            self.keepNpoint = data[0].keepNpoint;
                        }
                    }
                }).catch(function () {

                })
            },

        },
        created: function () {
            this.getScrApplyList();
            this.getOfficeList();
            this.getScrRuleList();
            this.getCreditRule();
        }
    })

</script>
</body>
</html>