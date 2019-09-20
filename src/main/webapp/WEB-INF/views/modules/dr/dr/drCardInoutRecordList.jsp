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
    <div class="mgb-20"><edit-bar></edit-bar></div>

    <el-form action="${ctx}/dr/drInoutRecord/list" ref="searchListForm" method="post">
        <input id="pageNo" name="pageNo" type="hidden" v-model.number="pageNo"/>
        <input id="pageSize" name="pageSize" type="hidden" v-model.number="pageSize"/>
        <input type="hidden" v-for="(qryTime, index) in rangeDate" :name="index == 0 ? 'qryStartTime' :'qryEndTime'" :value="qryTime">
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button type="primary" size="mini" :disabled="exporting" @click.stop="dialogVisible = true"><i
                        class="iconfont icon-daochu"></i>导出
                </el-button>
            </div>
            <div class="search-input">
                <el-date-picker
                        v-model="rangeDate"
                        type="daterange"
                        size="mini"
                        align="right"
                        unlink-panels
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd"
                        :unlink-panels="true"
                        :picker-options="pickerOptions2"
                        style="width: 270px;">
                </el-date-picker>
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
    <div class="selected-list_len-bar">选择<span class="selected-num">{{listLen}}</span>条数据
        <a href="javascript:void (0)" @click.stop.prevent="ajaxUpdate"><i
                class="iconfont icon-jianchagengxin"></i>更新</a>
        <a href="javascript:void (0)" @click.stop.prevent="clearStorage"><i
                class="iconfont icon-delete"></i>清空</a></div>
    <div class="table-container">
        <el-table :data="tableData" size="mini" ref="multipleTable" @selection-change="handleSelectionChange" class="table"
                  style="width: 100%">
            <el-table-column type="selection" width="60"></el-table-column>
            <el-table-column align="center" prop="cardNo" label="卡号" width="120">
            	<template slot-scope="scope">
            		{{scope.row.cardNo || '-'}}
            	</template>
            </el-table-column>
            <el-table-column align="center" prop="uno" label="学号" width="140">
            	<template slot-scope="scope">
            		{{scope.row.uno || '-'}}
            	</template>
            </el-table-column>
            <el-table-column align="center" prop="office" label="所属学院" width="90">
            	<template slot-scope="scope">
            		{{scope.row.office || '-'}}
            	</template>
            </el-table-column>
            <el-table-column align="center" prop="uname" label="姓名" width="80"></el-table-column>
            <el-table-column align="center" prop="umobile" label="电话"  width="130">
            	<template slot-scope="scope">
            		{{scope.row.umobile || '-'}}
            	</template>
            </el-table-column>
            <el-table-column align="center" prop="fullSpace" label="出入地点"></el-table-column>
            <el-table-column align="center" label="进入时间" width="160">
                <template slot-scope="scope">
                 {{scope.row.enterTime | formatDateFilter('YYYY-MM-DD HH:mm:ss')}}
                </template>
            </el-table-column>
            <el-table-column align="center" label="退出时间" width="160">
                <template slot-scope="scope">
                   {{scope.row.exitTime | formatDateFilter('YYYY-MM-DD HH:mm:ss')}}
                </template>
            </el-table-column>
            <!-- <el-table-column align="center" prop="timestr" label="进入时长" width="150"></el-table-column> -->
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
                <el-radio :label="false">导出选择的记录</el-radio>
            </el-radio-group>
        </div>
        <span slot="footer" class="dialog-footer">
                <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" size="mini" @click="handleExportCardRecord">确 定</el-button>
        </span>
    </el-dialog>
</div>

<script>
    +function (Vue) {
        var app = new Vue({
            el: '#app',
            mixins: [paginationMixin],
            data: function () {
                var rangeDate = [];
                var qryStartTime = '${vo.qryStartTime}' || '';
                var qryEndTime = '${vo.qryEndTime}' || '';
                rangeDate.push(qryStartTime, qryEndTime);
                if (!qryStartTime || !qryEndTime) {
                    rangeDate = ''
                }
                var pageSize = '${page.pageSize}';
                pageSize = parseInt(pageSize) || 10;
                var pageNo = '${page.pageNo}';
                pageNo = parseInt(pageNo) || 1;
                var total = '${page.count}';
                total = parseInt(total) || 0;
                return {
                    qryStr: '${vo.qryStr}',
                    cardInoutRecords: [],
                    cardIdLabel: '',
                    pageNo: pageNo,
                    pageSize: pageSize,
                    isExportAll: true,
                    exporting: false,
                    pickerOptions2: {
                        shortcuts: [{
                            text: '最近一周',
                            onClick: function (picker) {
                                const end = new Date();
                                const start = new Date();
                                start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                                picker.$emit('pick', [start, end]);
                            }
                        }, {
                            text: '最近一个月',
                            onClick: function (picker) {
                                const end = new Date();
                                const start = new Date();
                                start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                                picker.$emit('pick', [start, end]);
                            }
                        }, {
                            text: '最近三个月',
                            onClick: function (picker) {
                                const end = new Date();
                                const start = new Date();
                                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                                picker.$emit('pick', [start, end]);
                            }
                        }]
                    },
                    rangeDate: rangeDate,
                    storageData: {},
                    tableData: JSON.parse('${fns: toJson(page.list)}'),
                    dialogVisible: false,
                    allCardIDs: [],
                    storageDataNotCurPage: [],
                    total: total
                }
            },
            computed: {
                listLen: {
                    get: function () {
                        return this.storageDataNotCurPage.length + this.cardInoutRecords.length;
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
                    this.cardInoutRecords = [];
                    for (var i = 0; i < value.length; i++) {
                        this.cardInoutRecords.push(value[i].id)
                    }
                    this.setLocalStorage();
                },
                setLocalStorage: function () {
                    for (var i = 0; i < this.allCardIDs.length; i++) {
                        if (this.cardInoutRecords.indexOf(this.allCardIDs[i]) > -1) {
                            this.storageData[this.allCardIDs[i]] = true;
                        } else {
                            delete this.storageData[this.allCardIDs[i]];
                        }
                    }
                    window.localStorage.setItem('cardInoutRecords', JSON.stringify(this.storageData));
                },
                getStorageData: function () {
                    var storageData = JSON.parse(window.localStorage.getItem('cardInoutRecords'));
                    if (!storageData) {
                        return {};
                    }
                    return storageData;
                },
                //导出
                handleExportCardRecord: function () {
                    var self = this;
                    var data = {
                        cardType: this.cardType,
                        officeIds: this.officeIds,
                        qryStartTime: this.rangeDate[0],
                        qryEndTime: this.rangeDate[1],
                        qryStr: this.qryStr,
                        ids: this.getStorageIds(),
                        isExportAll: this.isExportAll
                    };
                    this.exporting = true;
                    var exportCards = this.$axios({
                        method: 'POST',
                        url: '/dr/drCardRecord/ajaxCacheInoutRecord',
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
                getStorageIds: function () {
                    return this.storageDataNotCurPage.concat(this.cardInoutRecords)
                },
                //提交数据
                cardRecordFormSubmit: function (formName) {
                    this.$refs[formName].$el.submit();
                },
                handleClose: function () {
                    this.dialogVisible = false;
                },

                clearStorage: function () {
                    var self = this;
                    window.localStorage.removeItem('cardInoutRecords');
                    this.cardInoutRecords = [];
                    this.$refs.multipleTable.clearSelection();
                    this.storageDataNotCurPage = [];
                },

                ajaxUpdate: function () {
                    var self = this;
                    var updateXhr;
                    updateXhr = this.$axios.get('/dr/drInoutRecord/ajaxUpdate');
                    updateXhr.then(function (response) {
                        var data = response.data;
                        if (data.status) {
                            location.reload();
                        } else {
                            self.$message.error(data.msg);
                        }
                    }).catch(function (error) {
                        self.$message.error(error.response.data);
                    })
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

                getCurrentPageAllCard: function () {
                    var cardIds = [];
                    this.tableData.forEach(function (item) {
                        cardIds.push(item.id)
                    });
                    return cardIds;
                },

                getStorageDataNotCurPage: function () {
                    var storageData = this.storageData;
                    var notCurPageStorageData = [];
                    for (var k in storageData) {
                        if (!storageData.hasOwnProperty(k)) {
                            continue;
                        }
                        if (storageData[k] && this.cardInoutRecords.indexOf(k) === -1) {
                            notCurPageStorageData.push(k);
                        }
                    }
                    return notCurPageStorageData;
                },
            },
            created: function () {
                this.allCardIDs = this.getCurrentPageAllCard();
                this.storageData = this.getStorageData();
                this.cardInoutRecords = this.getCardRecordsByStorage();
                this.storageDataNotCurPage = this.getStorageDataNotCurPage();
            },
            mounted: function () {
                var self = this;
                var cardInoutRecords = this.cardInoutRecords;
                this.tableData.forEach(function (row) {
                    self.$refs.multipleTable.toggleRowSelection(row, cardInoutRecords.indexOf(row.id) > -1);
                })
            }
        })
    }(Vue)
</script>
</body>
</html>