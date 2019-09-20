<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" class="container-fluid container-fluid_bg" v-show="pageLoad" style="display: none">
    <edit-bar></edit-bar>
    <el-form action="${ctx}/dr/drCardRecord/warnList" ref="searchListForm" method="post">
        <input id="pageNo" name="pageNo" type="hidden" v-model.number="pageNo"/>
        <input id="pageSize" name="pageSize" type="hidden" v-model.number="pageSize"/>
        <div class="conditions">

            <e-condition label="预警类型" type="radio" v-model="queryWarn" name="queryWarn"
                         :options="[{label:'当天未出基地', value:'当天未出基地'}, {label:'超过7天未打卡', value: '超过'}]"></e-condition>
            <e-condition type="checkbox" label="学院" :options="colleges" :default-props="{label: 'name', value: 'id'}"
                         v-model="officeIds"
                         name="officeIds"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button type="primary" size="mini" :disabled="exporting" @click.stop="dialogVisible = true"><i
                        class="iconfont icon-daochu"></i>导出
                </el-button>
            </div>
            <div class="search-input">
                <el-input
                        name="qryStr"
                        placeholder="卡号/学号/姓名/场地/门禁"
                        v-model="qryStr"
                        size="mini"
                        style="width: 225px;">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="cardRecordFormSubmit('searchListForm')"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>

    <%--<div class="selected-list_len-bar">选择<span class="selected-num">{{listLen}}</span>条数据--%>
    <%--<a href="javascript:void (0)" @click.stop.prevent="clearStorage"><i--%>
    <%--class="iconfont icon-delete"></i>清空</a></div>--%>

    <div class="table-container">
        <el-table :data="tableData" class="table" size="mini" ref="multipleTable"
                  @selection-change="handleSelectionChange">
            <%--<el-table-column type="selection" width="60"></el-table-column>--%>
            <%--<el-table-column align="center" prop="sortid" label="序号">--%>
            <%--<template slot-scope="scope">{{scope.row.sortid}}</template>--%>
            <%--</el-table-column>--%>
            <el-table-column align="center" prop="cardNo" label="卡号">
                <template slot-scope="scope">{{scope.row.cardNo}}</template>
            </el-table-column>
            <el-table-column align="center" prop="uno" label="学号"></el-table-column>
            <el-table-column align="center" label="学院/姓名">
                <template slot-scope="scope">
                    <p>{{scope.row.office || ''}}<br>{{scope.row.uname || '-'}}</p>
                </template>
            </el-table-column>
            <el-table-column align="center" label="手机">
                <template slot-scope="scope">{{scope.row.umobile || '-'}}</template>
            </el-table-column>
            <!-- <el-table-column align="center" label="预警规则">
                <template slot-scope="scope">{{scope.row.gname || '-'}}</template>
            </el-table-column> -->
            <!-- <el-table-column align="center" label="场地">
                <template slot-scope="scope">{{scope.row.rspaceName || '-'}}</template>
            </el-table-column> -->
            <el-table-column align="center" label="卡状态">
                <template slot-scope="scope">{{getCardStatus(scope.row.cardStatus)}}</template>
            </el-table-column>
            <el-table-column align="left" label="最后一次时间">
                <template slot-scope="scope">
                    <e-col-item label="进入：" label-width="48px" style="margin-bottom: 0">
                        {{scope.row.lastEnterDate | formatDateFilter('YYYY-MM-DD HH:mm:ss')}}
                    </e-col-item>
                    <e-col-item v-show="scope.row.lastExitDate" label="退出：" label-width="48px"
                                style="margin-bottom: 0" v-if="scope.row.lastEnterDate < scope.row.lastExitDate">
                        {{scope.row.lastExitDate | formatDateFilter('YYYY-MM-DD HH:mm:ss')}}
                    </e-col-item>
                </template>
            </el-table-column>
            <%--<el-table-column align="center" label="最后一次退出时间">--%>
            <%--<template slot-scope="scope"></template>--%>
            <%--</el-table-column>--%>
            <!-- <el-table-column align="center" label="进入总时长">
                <template slot-scope="scope">{{scope.row.enterAllTime || '-'}}</template>
            </el-table-column> -->

            <el-table-column align="left" width="166" label="预警类型">
                <template slot-scope="scope">
                    <div v-if="scope.row.warnOver">
                        <template v-if="originQueryWarn == '超过' || !originQueryWarn">
                            {{scope.row.warnOver || '-'}}
                            <!-- <el-button size="mini" type="text">取消</el-button> -->
                        </template>
                    </div>
                    <div v-if="scope.row.warnH24">
                        <template v-if="originQueryWarn == scope.row.warnH24 || !originQueryWarn">
                            {{scope.row.warnH24 || '-'}}
                            <!-- <el-button size="mini" type="text">取消</el-button> -->
                        </template>
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <div class="text-right mgb-20">
            <el-pagination
                    size="small"
                    @size-change="handlePaginationSizeChange"
                    background
                    @current-change="handlePaginationPageChange"
                    :current-page.sync="pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="pageSize"
                    layout="prev, pager, next, sizes"
                    :total="total">
            </el-pagination>
        </div>
    </div>
    <el-dialog
            title="提示"
            :visible.sync="dialogVisible"
            width="520px"
            center
            :close-on-click-modal="false"
            :before-close="handleClose">
        <div class="text-center">
            <el-radio-group v-model="isExportAll">
                <el-radio :label="true">导出所有记录</el-radio>
                <el-radio :label="false">导出查询条件的记录</el-radio>
            </el-radio-group>
        </div>
        <span slot="footer" class="dialog-footer">
                <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" size="mini" @click="handleExportCardRecord">确 定</el-button>
        </span>
    </el-dialog>
</div>
<script type="text/javascript">
    +function (Vue) {
        axios.defaults.timeout = '5000';
        var app = new Vue({
            el: '#app',
            mixins: [paginationMixin],
            data: function () {
                var colleges = JSON.parse('${fns: toJson(fns:findColleges())}') || [];
                var officeIds = JSON.parse('${fns: toJson(drCardRecordWarnVo.officeIds)}') || [];
                var drCardreGroup = JSON.parse('${fns: toJson(drCardreGroup)}') || [];
                var drCardreIds = '${drCardRecordWarnVo.gid}' || '';
                var tableData = JSON.parse('${fns: toJson(page.list)}') || [];
                var drCstatuss = JSON.parse('${drCstatuss}') || [];
                var pageSize = '${page.pageSize}';
                pageSize = parseInt(pageSize) || 10;
                var pageNo = '${page.pageNo}';
                pageNo = parseInt(pageNo) || 1;
                var total = '${page.count}';
                total = parseInt(total) || 0;
                return {
                    colleges: colleges,
                    officeIds: officeIds,
                    pageSize: pageSize,
                    pageNo: pageNo,
                    total: total,
                    collegeAll: false,
                    warningAll: false,
                    qryStr: "${drCardRecordWarnVo.qryStr}",
                    drCardreGroup: drCardreGroup,
                    exporting: false,
                    isExportAll: false,
                    drCardreIds: drCardreIds,
                    tableData: tableData,
                    drCstatuss: drCstatuss,
                    cardWarningRecords: [],
                    storageDataNotCurPage: [],
                    allCardIDs: [],
                    storageData: {},
                    dialogVisible: false,
                    queryWarn: '${drCardRecordWarnVo.queryWarn}',
                    originQueryWarn: '',
                }
            },
            computed: {
                drCstatussEntries: function () {
                    var entries = {};
                    this.drCstatuss.forEach(function (item) {
                        entries['dr' + item.key] = item.sname;
                    });
                    return entries;
                },
                listLen: {
                    get: function () {
                        return this.storageDataNotCurPage.length + this.cardWarningRecords.length;
                    }
                }
            },
            methods: {

                handlePaginationSizeChange: function () {
                    this.cardRecordFormSubmit('searchListForm')
                },

                handlePaginationPageChange: function () {
                    this.cardRecordFormSubmit('searchListForm')
                },
                handleSelectionChange: function (value) {
                    this.cardWarningRecords = [];
                    for (var i = 0; i < value.length; i++) {
                        this.cardWarningRecords.push(value[i].id)
                    }
                    this.setLocalStorage();
                },
                setLocalStorage: function () {
                    for (var i = 0; i < this.allCardIDs.length; i++) {
                        if (this.cardWarningRecords.indexOf(this.allCardIDs[i]) > -1) {
                            this.storageData[this.allCardIDs[i]] = true;
                        } else {
                            delete this.storageData[this.allCardIDs[i]];
                        }
                    }
                    window.localStorage.setItem('cardWarningRecords', JSON.stringify(this.storageData));
                },
                getStorageData: function () {
                    var storageData = JSON.parse(window.localStorage.getItem('cardWarningRecords'));
                    if (!storageData) {
                        return {};
                    }
                    return storageData;
                },

                getCardRecordsByStorage: function () {
                    var ids = [];
                    for (var i = 0; i < this.tableData.length; i++) {
                        if (this.storageData[this.tableData[i].id]) {
                            ids.push(this.tableData[i].id)
                        }
                    }
                    return ids;
                },
                getStorageDataNotCurPage: function () {
                    var storageData = this.storageData;
                    var notCurPageStorageData = [];
                    for (var k in storageData) {
                        if (!storageData.hasOwnProperty(k)) {
                            continue;
                        }
                        if (storageData[k] && this.cardWarningRecords.indexOf(k) === -1) {
                            notCurPageStorageData.push(k);
                        }
                    }
                    return notCurPageStorageData;
                },
                handleExportCardRecord: function () {
                    var self = this;
                    var data = {
                        officeIds: this.officeIds,
                        gid: this.drCardreIds,
                        isExportAll: this.isExportAll,
                        qryStr: this.qryStr
                    };


                    this.exporting = true;
                    var exportCards = this.$axios({
                        method: 'POST',
                        url: '/dr/drCardRecord/ajaxCacheWarnRecord',
                        headers: {
                            'Content-Type': 'application/json;charset=UTF-8'
                        },
                        data: JSON.stringify(data)
                    });

                    exportCards.then(function (response) {
                        var data = response.data;
                        self.exporting = false;
                        if (data.status) {
                            location.href = ctx + data.datas;
                            return false;
                        }
                        self.$message.error(data.msg);
                    }).catch(function (error) {
                        self.$message.error(error.response.data);
                        self.exporting = false;
                    });
                    self.dialogVisible = false;

                },

                clearStorage: function () {
                    window.localStorage.removeItem('cardWarningRecords');
                    this.cardWarningRecords = [];
                    this.$refs.multipleTable.clearSelection();
                    this.storageDataNotCurPage = [];
                },

                getCardStatus: function (value) {
                    if (!value) return '-';
                    return this.drCstatussEntries['dr' + value];
                },


                handleCheckAll: function (allName, checkedIds, checkedGroup) {
                    if (this[allName]) {
                        for (var i = 0; i < this[checkedGroup].length; i++) {
                            if (this[checkedIds].indexOf(this[checkedGroup][i].id) > -1) {
                                continue;
                            }
                            this[checkedIds].push(this[checkedGroup][i].id);
                        }
                    } else {
                        this[checkedIds] = [];
                    }
                },

                handleCheckSingle: function (allName, checkedIds, checkedGroup) {
                    this[allName] = ((this[checkedIds].length === this[checkedGroup].length) && this[checkedGroup].length > 0)
                },


                cardRecordFormSubmit: function (formName) {
                    this.$refs[formName].$el.submit();
                },


                handleClose: function () {
                    this.dialogVisible = false;
                },
                getCurrentPageAllCard: function () {
                    var cardIds = [];
                    this.tableData.forEach(function (item) {
                        cardIds.push(item.id)
                    });
                    return cardIds;
                },


            },
            created: function () {
                this.allCardIDs = this.getCurrentPageAllCard();
                this.storageData = this.getStorageData();
                this.cardWarningRecords = this.getCardRecordsByStorage();
                this.storageDataNotCurPage = this.getStorageDataNotCurPage();
            },
            mounted: function () {
                var self = this;
                var cardInoutRecords = this.cardWarningRecords;
                this.originQueryWarn = this.queryWarn;
                this.tableData.forEach(function (row) {
                    self.$refs.multipleTable.toggleRowSelection(row, cardInoutRecords.indexOf(row.id) > -1);
                })
            }
        })
    }(Vue)
</script>

</body>
</html>