/**
 * Created by Administrator on 2018/9/5.
 */
'use strict';

Vue.component('recommendation', {
    template: '<div><el-form :model="searchListForm" ref="searchListForm" size="mini">\n' +
    '            <div class="search-block_bar clearfix">\n' +
    '                <div class="search-btns">\n' +
    '                   <el-button size="mini" type="primary" @click.stop.prevent="batchRelateRecommend" :disabled="multipleSelectedId.length == 0">推荐</el-button>\n' +
    '                   <el-button size="mini" type="primary" @click.stop.prevent="batchCancelRelateRecommend" :disabled="multipleSelectedId.length == 0">取消推荐</el-button>\n' +
    '                </div>\n' +
    '                <div class="search-input">\n' +
    '                    <el-select v-model="searchListForm.dateType" size="mini" placeholder="请选择查询条件" @change="dateTypeChange" style="width:150px;">\n' +
    '                        <el-option v-for="item in searchDateList" :key="item.value" :label="item.label"\n' +
    '                                   :value="item.value"></el-option>\n' +
    '                    </el-select>\n' +
    '                    <el-date-picker\n' +
    '                            v-model="publishDate"\n' +
    '                            type="daterange"\n' +
    '                            size="mini"\n' +
    '                            align="right"\n' +
    '                            @change="getRecommendList"\n' +
    '                            unlink-panels :disabled="!searchListForm.dateType"\n' +
    '                            range-separator="至"\n' +
    '                            start-placeholder="开始日期"\n' +
    '                            end-placeholder="结束日期"\n' +
    '                            value-format="yyyy-MM-dd"\n' +
    '                            style="width: 270px;">\n' +
    '                    </el-date-picker>\n' +
    '                    <el-input\n' +
    '                            placeholder="项目名称"\n' +
    '                            size="mini"\n' +
    '                            name="queryStr"\n' +
    '                            v-model="searchListForm.queryStr"\n' +
    '                            @keyup.enter.native="getRecommendList"\n' +
    '                            style="width: 200px;">\n' +
    '                        <el-button slot="append" icon="el-icon-search"\n' +
    '                                   @click.stop.prevent="getRecommendList"></el-button>\n' +
    '                    </el-input>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </el-form>\n' +
    '        <div class="table-container">\n' +
    '            <el-table :data="relateRecommendList" size="mini" class="table" ref="relateRecommendList"\n' +
    '                      v-loading="loading"\n' +
    '                      @selection-change="handleChangeSelection">\n' +
    '                <el-table-column\n' +
    '                        type="selection"\n' +
    '                        width="60">\n' +
    '                </el-table-column>\n' +
    '                <el-table-column label="项目名称" prop="name" align="center"></el-table-column>\n' +
    '                <el-table-column label="所属栏目" prop="module" align="center"></el-table-column>\n' +
    '                <el-table-column label="到期时间" prop="viewCount" align="center">\n' +
    '                </el-table-column>\n' +
    '                <el-table-column label="发布时间" prop="publishDate" align="center">\n' +
    '                </el-table-column>\n' +
    '                <el-table-column label="操作" align="center">\n' +
    '                   <template slot-scope="scope">\n' +
    '                       <div class="table-btns-action">\n' +
    '                           <el-button size="mini" type="text" v-show="relatedIds.indexOf(scope.row.id) == -1" @click.stop.prevent="relateRecommend(scope.row)">推荐</el-button>\n' +
    '                           <el-button size="mini" type="text" v-show="relatedIds.indexOf(scope.row.id) > -1" @click.stop.prevent="cancelRelateRecommend(scope.row)">取消推荐</el-button>\n' +
    '                       </div>\n' +
    '                   </template>\n' +
    '                </el-table-column>\n' +
    '            </el-table>\n' +
    '            <div class="text-right mgb-20">\n' +
    '                <el-pagination\n' +
    '                        size="small"\n' +
    '                        @size-change="handlePaginationSizeChange"\n' +
    '                        background\n' +
    '                        @current-change="handlePaginationPageChange"\n' +
    '                        :current-page.sync="searchListForm.pageNo"\n' +
    '                        :page-sizes="[5,10,20,50,100]"\n' +
    '                        :page-size="searchListForm.pageSize"\n' +
    '                        layout="total, prev, pager, next, sizes"\n' +
    '                        :total="pageCount">\n' +
    '                </el-pagination>\n' +
    '            </div>\n' +
    '        </div></div>',
    props:{
        relatedIds:Array
    },
    data: function () {
        return {
            searchDateList: [{label: '发布时间', value: '1'}, {label: '到期时间', value: '2'}],
            searchListForm: {
                pageSize: 10,
                pageNo: 1,
                queryStr: '',
                dateType: '',
                startDate: '',
                endDate: ''
            },
            pageCount: 0,
            relateRecommendList: [],
            loading: false,
            multipleSelection: [],
            multipleSelectedId: [],
            publishDate: [],
            recommendResponse: {
                ret: '1',
                msg: '成功',
                data: {
                    pageSize: 10,
                    pageNo: 1,
                    count: 12,
                    list: [
                        {
                            id: '1',
                            name: '1辉丰股份当矿工',
                            title: '刚发的改好',
                            sort: '1',
                            publishStatus: '1',
                            viewCount: '22',
                            expireDate: '2018-12-12',
                            publishDate: '2018-8-15',
                            isTop: '1',
                            module: '项目展示'
                        },
                        {
                            id: '2',
                            name: '2开个会电风扇',
                            title: '周末很懒',
                            sort: '2',
                            publishStatus: '2',
                            viewCount: '12',
                            expireDate: '2018-11-11',
                            publishDate: '2018-8-16',
                            isTop: '0',
                            module: '项目展示'
                        },
                        {
                            id: '3',
                            name: '3富华大厦改好',
                            title: '韩国锦湖',
                            sort: '3',
                            publishStatus: '1',
                            viewCount: '76',
                            expireDate: '2018-10-15',
                            publishDate: '2018-3-25',
                            isTop: '0',
                            module: '项目展示'
                        },
                        {
                            id: '4',
                            name: '4接口规范化',
                            title: '风格很快乐',
                            sort: '4',
                            publishStatus: '2',
                            viewCount: '152',
                            expireDate: '2018-12-17',
                            publishDate: '2018-5-20',
                            isTop: '0',
                            module: '项目展示'
                        }
                    ]
                }
            }
        }
    },
    watch: {
        publishDate: function (value) {
            value = value || [];
            this.searchListForm.startDate = value[0];
            this.searchListForm.endDate = value[1];
        }
    },
    computed: {
        publishStatusEntries: {
            get: function () {
                return this.getEntries(this.publishStatuses)
            }
        }
    },
    methods:{
        handlePaginationSizeChange: function (value) {
            this.searchListForm.pageSize = value;
            this.getRecommendList();
        },

        handlePaginationPageChange: function (value) {
            this.searchListForm.pageNo = value;
            this.getRecommendList();
        },

        getRecommendList: function () {
            // var self = this;
            // this.loading = true;
            // this.$axios({
            //     method: 'POST',
            //     url: '?' + Object.toURLSearchParams(this.searchListForm)
            // }).then(function (response) {
            //     var data = response.data;
            //     if (data.status == '1') {
            //         self.pageCount = data.data.count;
            //         self.searchListForm.pageSize = data.data.pageSize;
            //         self.relateRecommendList = data.data.list || [];
            //     }
            //     self.loading = false;
            // }).catch(function () {
            //     self.loading = false;
            //     self.$message({
            //         message: '请求失败',
            //         type: 'error'
            //     })
            // });
            this.relateRecommendList = this.recommendResponse.data.list || [];
        },
        handleChangeSelection: function (value) {
            this.multipleSelection = value;
            this.multipleSelectedId = [];
            for (var i = 0; i < value.length; i++) {
                this.multipleSelectedId.push(value[i].id);
            }
        },
        dateTypeChange:function () {
            if(this.searchListForm.dateType && this.publishDate.length > 0){
                this.getRecommendList();
            }
        },
        batchRelateRecommend:function () {
            this.$emit('recommend',{
                selections:this.multipleSelection
            });
        },
        relateRecommend:function (row) {
            this.$emit('recommend',{
                selections:[row]
            });
        },
        batchCancelRelateRecommend:function () {
            this.$emit('cancelrecommend',{
                ids:this.multipleSelectedId
            });
        },
        cancelRelateRecommend:function (row) {
            this.$emit('cancelrecommend',{
                ids:[row.id]
            });
        }


    },
    mounted: function () {
        this.getRecommendList();
    }
});