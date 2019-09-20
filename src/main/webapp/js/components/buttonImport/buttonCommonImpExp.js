'use strict';

Vue.component('button-common-imp-exp', {
    template: '<div>\n' +
    '    <el-button type="primary" size="mini" @click.stop.prevent="commonImpExp"><i class="iconfont icon-daoru"></i>导入</el-button>\n' +
    '\n' +
    '\n' +
    '    <el-dialog :title="iepTpl.name" :visible.sync="dialogVisible" top="2vh" width="900px" class="tem-step-dialog"\n' +
    '               :before-close="handleClose"\n' +
    '               :close-on-click-modal="false">\n' +
    '\n' +
    '        <div style="text-align: center;">\n' +
    '            <div class="tem-step common-step">\n' +
    '                <div v-if="iepTpl.level == \'10\'" class="tem-step-item" v-for="(item,index) in iepTpl.childrens"\n' +
    '                     :key="item.id">\n' +
    '                    <div class="tem-step-arrow" v-if="index != 0">\n' +
    '                        <i :class="{\'iconfont\':true,\'icon-iconset0438\':true,\'tem-step-arrow-color\':item.curr == 0}"></i>\n' +
    '                    </div>\n' +
    '                    <div class="tem-step-todo">\n' +
    '                        <div v-for="(child,index) in item.childrens">\n' +
    '                            <p class="one-step" v-if="index == \'0\'" :class="{\'one-step-background\':item.curr == 0}">\n' +
    '                                第{{item.step}}步：{{child.name}}{{item.name}}</p>\n' +
    '                            <button-import-contest v-if="child.operType == \'10\'" :child="child" :actyw-id="actywId"\n' +
    '                                                   :gnode-id="gnodeId" :level="iepTpl.level" :item="item"\n' +
    '                                                   :item-curr="item.curr"\n' +
    '                                                   @upload-success="getDataList"></button-import-contest>\n' +
    '                            <el-button size="mini" type="primary" v-if="child.operType == \'20\'"\n' +
    '                                       style="margin-bottom: 10px;"\n' +
    '                                       @click.stop.prevent="exportFile(child.id)">{{child.name}}{{item.name}}\n' +
    '                            </el-button>\n' +
    '                            <p v-if="child.operType == \'30\'" class="download-tem"\n' +
    '                               @click.stop.prevent="downloadFile(item.id)">\n' +
    '                                {{child.name}}</p>\n' +
    '                        </div>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '\n' +
    '            </div>\n' +
    '        </div>\n' +
    '\n' +
    '\n' +
    '        <div v-if="iepTpl.level == \'20\'" class="tem-one-item common-step">\n' +
    '            <div class="search-block_bar clearfix">\n' +
    '                <div class="search-btns">\n' +
    '                <span v-for="item in iepTpl.childrens" :key="item.id">\n' +
    '                    <el-button v-if="item.operType == \'30\'" size="mini" type="primary"\n' +
    '                               @click.stop.prevent="downloadFile(item.id)">{{item.name}}</el-button>\n' +
    '\n' +
    '                    <button-import-contest v-if="item.operType == \'10\'" :child="item" :actyw-id="actywId"\n' +
    '                                           :level="iepTpl.level"\n' +
    '                                           :gnode-id="gnodeId" @upload-success="getDataList"></button-import-contest>\n' +
    '\n' +
    '                    <el-button v-if="item.operType == \'20\'" size="mini" type="primary"\n' +
    '                               @click.stop.prevent="exportFile(item.id)">{{item.name}}\n' +
    '                    </el-button>\n' +
    '                </span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </div>\n' +
    '\n' +
    '        <div class="tem-data">\n' +
    '            <div class="tem-data-title">\n' +
    '            <span>查看导入结果<i :class="{\'el-icon-caret-top\':isShowDataList,\'el-icon-caret-bottom\':!isShowDataList}"\n' +
    '                           @click.stop.prevent="isShowDataList = !isShowDataList"></i></span>\n' +
    '                <hr>\n' +
    '            </div>\n' +
    '            <div class="table-container" v-show="isShowDataList" style="box-shadow: none">\n' +
    '                <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading" :span-method="arraySpanMethod"\n' +
    '                          max-height="263">\n' +
    '                    <el-table-column prop="filename" label="数据类型" align="left" min-width="100">\n' +
    '                        <template slot-scope="scope">\n' +
    '                            <el-tooltip :content="scope.row.filename" popper-class="white"\n' +
    '                                        placement="right">\n' +
    '                                <span class="break-ellipsis">{{scope.row.filename}}</span>\n' +
    '                            </el-tooltip>\n' +
    '                        </template>\n' +
    '                    </el-table-column>\n' +
    '                    <el-table-column prop="createDate" label="导入时间" align="center" min-width="150">\n' +
    '                        <template slot-scope="scope">\n' +
    '                            {{scope.row.createDate}}\n' +
    '                        </template>\n' +
    '                    </el-table-column>\n' +
    '                    <el-table-column prop="total" label="数据总数" align="center" min-width="80">\n' +
    '                        <template slot-scope="scope">\n' +
    '                            <span v-if="!scope.row.errmsg">{{scope.row.total}}</span>   \n' +
        '                        <span v-else style="color:#E9432D">\n' +
        '                            {{scope.row.errmsg}}\n' +
        '                        </span>\n' +
    '                        </template>\n' +
    '                    </el-table-column>\n' +
    '                    <el-table-column prop="todoNum" label="导入成功数" align="center" min-width="100">\n' +
    '                        <template slot-scope="scope">\n' +
    '                            {{scope.row.success}}\n' +
    '                        </template>\n' +
    '                    </el-table-column>\n' +
    '                    <el-table-column prop="fail" label="导入失败数" align="center" min-width="100">\n' +
    '                        <template slot-scope="scope">\n' +
    '                            {{scope.row.fail}}\n' +
    '                        </template>\n' +
    '                    </el-table-column>\n' +
    '                    <el-table-column prop="isComplete" label="状态" align="center" min-width="70">\n' +
    '                        <template slot-scope="scope">\n' +
    '                            {{scope.row.isComplete | selectedFilter(isCompleteLabelEntries)}}\n' +
    '                        </template>\n' +
    '                    </el-table-column>\n' +
    '                    <el-table-column label="操作" align="center" min-width="140">\n' +
    '                        <template slot-scope="scope">\n' +
    '                            <div class="table-btns-action">\n' +
    '                                <el-button  v-if="!scope.row.errmsg && (scope.row.total != scope.row.success) && scope.row.isComplete == \'1\'" type="text" size="mini" @click.stop.prevent="downEdata(scope.row.id)">\n' +
    '                                    下载错误数据\n' +
    '                                </el-button>\n' +
    '                                <el-button type="text" size="mini" @click.stop.prevent="delRecord(scope.row)">删除\n' +
    '                                </el-button>\n' +
    '                            </div>\n' +
    '                        </template>\n' +
    '                    </el-table-column>\n' +
    '                </el-table>\n' +
    '                <div class="text-right mgb-20" v-if="pageCount">\n' +
    '                    <el-pagination\n' +
    '                            size="small"\n' +
    '                            @size-change="handlePaginationSizeChange"\n' +
    '                            background\n' +
    '                            @current-change="handlePaginationPageChange"\n' +
    '                            :current-page.sync="searchListForm.pageNo"\n' +
    '                            :page-sizes="[5,10,20,50,100]"\n' +
    '                            :page-size="searchListForm.pageSize"\n' +
    '                            layout="total, prev, pager, next, sizes"\n' +
    '                            :total="pageCount">\n' +
    '                    </el-pagination>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '\n' +
    '        </div>\n' +
    '    </el-dialog></div>\n',
    props: {
        iepTplId:String,
        operTypeBtn:String
    },
    data: function () {
        return {
            iepTpl: {},
            pageList: [],
            loading: false,
            pageCount: 0,
            searchListForm: {
                pageNo: 1,
                pageSize: 5
            },
            isShowDataList: true,
            isCompleteLabel: [
                {label: '完成', value: '1'},
                {label: '处理中', value: '0'}
            ],
            originalPageList: [],
            timerInterval: null,
            dialogVisible:false
        }
    },
    computed: {
        actywId: function () {
            return this.$root.actywId;
        },
        gnodeId: function () {
            return this.$root.gnodeId;
        },
        isCompleteLabelEntries: function () {
            return this.getEntries(this.isCompleteLabel);
        }
    },
    methods: {
        arraySpanMethod:function (row) {
            if(row.row.errmsg){
                if(row.columnIndex === 2){
                    return [1,3];
                }else if(row.columnIndex === 3 || row.columnIndex === 4){
                    return [0,0];
                }
            }
        },
        commonImpExp: function () {
            this.dialogVisible = true;
            this.getImpExpData();
            this.getDataList();
        },
        handleClose:function () {
            this.dialogVisible = false;
            this.searchListForm.pageNo = 1;
            this.searchListForm.pageSize = 5;
            this.isShowDataList = true;
            this.timerInterval && clearInterval(this.timerInterval);
            this.$emit('imp-exp-success');
        },
        getImpExpData: function () {
            var self = this;
            this.$axios({
                method: 'POST',
                url: '/iep/iepTpl/ajaxIepTpl?id=' + self.iepTplId
            }).then(function (response) {
                var data = response.data;
                self.iepTpl = data || {};
                if (self.iepTpl.level == '10') {
                    self.iepTpl.childrens.forEach(function (item) {
                        item.childrens.sort(function (a, b) {
                            return parseInt(a.operType) - parseInt(b.operType);
                        });
                        item.childrens.forEach(function (child) {
                            child.isUploading = false;
                        })
                    });
                } else if (self.iepTpl.level == '20') {
                    self.iepTpl.childrens.forEach(function (item) {
                        item.isUploading = false;
                    });
                }
            }).catch(function () {

            })
        },
        exportFile: function (id) {
            // <%--location.href = '${ctx}/iep/ie/downEdata?id=' + id;--%>
        },
        downloadFile: function (id) {
            location.href = '/a/iep/ie/downTpl?id=' + id;
        },
        getImpInfoList: function () {
            var self = this;
            var pageList = self.pageList;
            var unCompleteList = pageList.filter(function (item) {
                return item.isComplete != '1';
            });
            var params = unCompleteList.map(function (item) {
                return {id: item.id};
            });

            if (unCompleteList.length == 0) {
                this.timerInterval && clearInterval(this.timerInterval);
                return false;
            }
            this.$axios({
                method: 'POST',
                url: '/iep/ie/ajaxIepInfo?iepId=' + self.iepTplId + '&operType=' + self.operTypeBtn,
                data: params
            }).then(function (response) {
                var data = response.data.datas || {};
                self.pageList.forEach(function (item) {
                    var impInfo = data[item.id];
                    if (impInfo) {
                        Object.assign(item, impInfo);
                        item.isComplete = impInfo.isComplete;
                        self.getImpExpData();
                    }
                });
            });
        },
        getDataList: function () {
            var self = this;
            this.loading = true;
            this.$axios({
                method: 'GET',
                url: '/iep/ie/ajaxIepInfos?id=' + self.iepTplId + '&operType=' + self.operTypeBtn
            }).then(function (response) {
                var data = response.data;
                if (data.status) {
                    self.originalPageList = data.datas ? JSON.parse(JSON.stringify(data.datas)) : [];
                    self.pageCount = data.datas ? data.datas.length : 0;
                    self.pageList = self.getCurrentPageList();
                }else {
                    self.$message({
                        message: data.status ? '导入成功' : data.msg || '导入失败',
                        type: data.status ? 'success' : 'error'
                    })
                }
                self.loading = false;
                self.requestUnComplete();
            }).catch(function () {
                self.loading = false;
            })
        },
        getCurrentPageList: function () {
            var pageMaxNo = Math.ceil(this.pageCount / this.searchListForm.pageSize) || 1;
            if (this.searchListForm.pageNo > pageMaxNo) {
                this.searchListForm.pageNo = pageMaxNo;
            }
            var list = [], startIndex = this.searchListForm.pageNo * this.searchListForm.pageSize;
            list = this.originalPageList.slice(startIndex - this.searchListForm.pageSize, startIndex);
            return list;
        },
        requestUnComplete: function () {
            var self = this;
            var unCompleteList = this.pageList.filter(function (item) {
                return item.isComplete != '1';
            });
            if (unCompleteList.length < 1) {
                return false;
            }
            this.timerInterval = setInterval(function () {
                self.getImpInfoList()
            }, 8000);
        },
        downEdata: function (id) {
            location.href = '/a/iep/ie/downEdata?id=' + id + '&operType=' + this.operTypeBtn;
        },
        delRecord: function (row) {
            var self = this;
            this.$confirm('确认删除吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(function () {
                self.delRecordRequest(row)
            }).catch(function () {

            })
        },
        delRecordRequest: function (row) {
            var self = this;
            this.$axios({
                method: 'POST',
                url: '/iep/ie/delRecord?id=' + row.id + '&operType=' + self.operTypeBtn + '&iepId=' + row.impTpye
            }).then(function (response) {
                var data = response.data;
                if (data.status) {
                    self.getDataList();
                }
                self.$message({
                    message: data.status ? '删除成功' : '删除失败',
                    type: data.status ? 'success' : 'error'
                })
            }).catch(function (error) {
                self.$message({
                    type: 'error',
                    message: self.xhrErrorMsg
                })
            })
        },
        handlePaginationSizeChange: function (value) {
            this.searchListForm.pageSize = value;
            this.timerInterval && clearInterval(this.timerInterval);
            this.pageList = this.getCurrentPageList();
            this.requestUnComplete();
        },
        handlePaginationPageChange: function (value) {
            this.searchListForm.pageNo = value;
            this.timerInterval && clearInterval(this.timerInterval);
            this.pageList = this.getCurrentPageList();
            this.requestUnComplete();
        }
    }
});
