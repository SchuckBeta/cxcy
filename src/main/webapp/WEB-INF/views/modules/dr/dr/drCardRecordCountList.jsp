<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>{{backgroundTitle}}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>
<div id="app" class="container-fluid container-fluid_bg" v-show="pageLoad" style="display: none">
    <edit-bar></edit-bar>
    <el-form action="${ctx}/dr/drCardRecord/list" ref="searchListForm" method="post">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input type="hidden" v-for="(qryTime, index) in rangeDate" :name="index == 0 ? 'qryStartTime' :'qryEndTime'"
               :value="qryTime">
        <div class="conditions">
            <%--<e-condition label="卡类型">--%>
                <%--<e-radio-group v-model="cardType">--%>
                    <%--<e-radio v-for="cardType in cardTypes" :label="cardType.key" name="card.cardType"--%>
                             <%--@change="cardRecordFormSubmit('searchListForm')"--%>
                             <%--:key="cardType.key">--%>
                        <%--{{cardType.name}}--%>
                    <%--</e-radio>--%>
                <%--</e-radio-group>--%>
            <%--</e-condition>--%>

            <e-condition type="checkbox" v-show="cardType === '1'" label="学院"  :options="colleges" :default-props="{label: 'name', value: 'id'}"
                         v-model="officeIds"
                         name="officeIds"></e-condition>

            <%--<e-condition v-show="cardType === '1'" label="学院">--%>
                <%--<e-checkbox v-model="collegeAll" custom-class="e-checkbox-all" @change="handleCheckAll('collegeAll', 'officeIds', 'colleges')">全部--%>
                <%--</e-checkbox>--%>
                <%--<e-checkbox-group v-model="officeIds">--%>
                    <%--<e-checkbox v-for="college in colleges" name="officeIds" :label="college.id" :key="college.id"--%>
                                <%--@change="handleCheckSingle('collegeAll', 'officeIds', 'colleges')">--%>
                        <%--{{college.name}}--%>
                    <%--</e-checkbox>--%>
                <%--</e-checkbox-group>--%>
            <%--</e-condition>--%>
        </div>
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
        <a href="javascript:void (0)" @click.stop.prevent="ajaxSynch"><i
                class="iconfont icon-jianchagengxin"></i>同步</a>
        <a href="javascript:void (0)" @click.stop.prevent="clearStorage"><i
                class="iconfont icon-delete"></i>清空</a>
    </div>
    <div class="table-container">
        <table class="table table-hover table-center table-nowrap table-white">
            <thead>
            <tr>
                <th width="32">
                    <el-checkbox v-model="isAllCard" @change="handleChangeAllCard">{{cardIdLabel}}</el-checkbox>
                </th>
                <th>卡号</th>
                <th>学号</th>
                <th>所属学院/姓名</th>
                <th>电话</th>
                <th>场地</th>
                <th>门禁</th>
                <th>刷卡时间</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.list}" var="drCardRecord">
                <tr>
                    <td>
                        <el-checkbox label="${drCardRecord.id}" v-model="cardRecords" @change="handleChangeCardId">
                            {{cardIdLabel}}
                        </el-checkbox>
                    </td>
                    <td>${drCardRecord.cardNo }</td>
                    <td title="${drCardRecord.msg }" <c:if test="${!fn:contains(drCardRecord.msg, '50')}"> style="background-color:#eee;" </c:if>>
                    	<c:if test="${not empty drCardRecord.user.no }">${drCardRecord.user.no }</c:if>
                        <c:if test="${empty drCardRecord.user.no }">-</c:if>
                    </td>
                    <td class="deal-separator">
                       	<c:if test="${not empty drCardRecord.user.name }"><c:if
                                test="${not empty drCardRecord.office }">${drCardRecord.office }/</c:if>${drCardRecord.user.name }
                        </c:if>
                        <c:if test="${empty drCardRecord.user.name }">-</c:if>
                    </td>
                    <td>
                       	<c:if test="${not empty drCardRecord.user.mobile }">${drCardRecord.user.mobile }</c:if>
                        <c:if test="${empty drCardRecord.user.mobile }">-</c:if>
                    </td>
                    <td>
                       	<c:if test="${not empty drCardRecord.rspaceName }">${drCardRecord.rspaceName }</c:if>
                        <c:if test="${empty drCardRecord.rspaceName }">-</c:if>
                    </td>
                    <td>
                       	<c:if test="${not empty drCardRecord.cerspace.erspace.name }">${drCardRecord.cerspace.erspace.name }</c:if>
                        <c:if test="${empty drCardRecord.cerspace.erspace.name }">${drCardRecord.name }</c:if>
                    </td>
                    <td><fmt:formatDate value="${drCardRecord.pcTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                </tr>
            </c:forEach>
            <tr v-if="!tableData.length">
                <td colspan="8">
                    <div class="cell">
                        <span class="empty-color">没有数据，请重新搜索</span>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    ${page.footer}
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

    +function ($, Vue) {
        var app = new Vue({
            el: '#app',
            mixins: [paginationMixin],
            data: function () {
                var colleges = JSON.parse('${fns: toJson(fns:findColleges())}') || [];
                var officeIds = JSON.parse('${fns: toJson(vo.officeIds)}') || [];
                var rangeDate = [];
                var qryStartTime = '${vo.qryStartTime}' || '';
                var qryEndTime = '${vo.qryEndTime}' || '';
                var pageNo = '${page.pageNo}';
                pageNo = pageNo || 1;
                rangeDate.push(qryStartTime, qryEndTime);
                if (!qryStartTime || !qryEndTime) {
                    rangeDate = ''
                }
                return {
                    colleges: colleges,
                    officeIds: officeIds,
                    collegeAll: false,
                    <%--cardTypes: JSON.parse('${drCardTypes}'),--%>
                    cardType: '${vo.card.cardType}',
                    qryStr: '${vo.qryStr}',
                    cardRecords: [],
                    cardIdLabel: '',
                    pageNo: pageNo,
                    isAllCard: false,
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
                    storageDataNotCurPage: []
                }
            },
            computed: {
                listLen: {
                    get: function () {
                        return this.storageDataNotCurPage.length + this.cardRecords.length;
                    }
                }
            },
            methods: {
                handleChangeAllCard: function () {
                    var self = this;
                    if (!this.isAllCard) {
                        this.cardRecords = [];
                    } else {
                        this.tableData.forEach(function (item) {
                            var id = item.id;
                            if (self.cardRecords.indexOf(id) === -1) {
                                self.cardRecords.push(id);
                            }
                        })
                    }
                    this.setLocalStorage();
                },
                handleChangeCardId: function () {
                    this.isAllCard = (this.cardRecords.length === this.tableData.length && this.tableData.length > 0);
                    this.setLocalStorage();
                },

                setLocalStorage: function () {
                    for (var i = 0; i < this.allCardIDs.length; i++) {
                        if (this.cardRecords.indexOf(this.allCardIDs[i]) > -1) {
                            this.storageData[this.allCardIDs[i]] = true;
                        } else {
                            delete this.storageData[this.allCardIDs[i]];
                        }
                    }
                    window.localStorage.setItem('cardRecords' + this.cardType, JSON.stringify(this.storageData));
                },

                getStorageData: function () {
                    var storageData = JSON.parse(window.localStorage.getItem('cardRecords' + this.cardType));
                    if (!storageData) {
                        return {};
                    }
                    return storageData;
                },

                clearStorage: function () {
                    window.localStorage.removeItem('cardRecords' + this.cardType);
                    this.cardRecords = [];
                    this.storageDataNotCurPage = [];
                    this.isAllCard = false;
                },

                //导出
                handleExportCardRecord: function () {
                    var self = this;
                    var data = {
                        card: {
                        	cardType:this.cardType
                        },
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
                        url: '/dr/drCardRecord/ajaxCacheCardRecord',
                        headers: {
                            'Content-Type': 'application/json;charset=UTF-8'
                        },
                        data: JSON.stringify(data)
                    });

                    exportCards.then(function (response) {
                        var data = response.data;
                        self.exporting = false;
                        console.info(data);
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
                    return this.storageDataNotCurPage.concat(this.cardRecords)
                },
                //提交数据
                cardRecordFormSubmit: function (formName) {
                    this.$refs[formName].$el.submit();
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
                        if (storageData[k] && this.cardRecords.indexOf(k) === -1) {
                            notCurPageStorageData.push(k);
                        }
                    }
                    return notCurPageStorageData;
                },
                ajaxUpdate: function () {
                    var self = this;
                    var updateXhr;
                    updateXhr = this.$axios.get('/dr/drCardRecord/ajaxUpdate');
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
                ajaxSynch: function () {
                    $.ajax({
                        type: 'POST',
                        url: '${ctx}/dr/drCardRecord/ajaxSynch',
                        dataType: 'json',
                        contentType: 'application/json;charset=UTF-8',
                        data: JSON.stringify({
                           	minPcTime: this.rangeDate[0],
                            maxPcTime: this.rangeDate[1]
                        }),
                        success: function (data) {
                            if (data.status) {
                                location.reload();
                            } else {
                                alertx(data.msg);
                            }
                        }
                    });
                }
            },
            created: function () {
                this.allCardIDs = this.getCurrentPageAllCard();
                this.storageData = this.getStorageData();
                this.cardRecords = this.getCardRecordsByStorage();
                this.storageDataNotCurPage = this.getStorageDataNotCurPage();
                this.isAllCard = (this.cardRecords.length === this.tableData.length && this.tableData.length > 0);
//                this.collegeAll = this.getCollegeAll();
            },
            mounted: function () {

            }
        })
    }(jQuery, Vue)


</script>
</body>
</html>