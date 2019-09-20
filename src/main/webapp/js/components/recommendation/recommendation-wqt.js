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
    // '                    <el-select v-model="dateType" size="mini" placeholder="请选择查询条件" clearable @change="handleChangeDateType" style="width:150px;">\n' +
        // '                        <el-option v-for="item in dateTypes" :key="item.value" :label="item.label"\n' +
        // '                                   :value="item.value"></el-option>\n' +
        // '                    </el-select>\n' +
        // '                    <el-date-picker\n' +
        // '                            v-model="dateQuery"\n' +
        // '                            type="daterange"\n' +
        // '                            size="mini"\n' +
        // '                            align="right"\n' +
        // '                            @change="handleChangeDateQuery"\n' +
        // '                            unlink-panels :disabled="!dateType"\n' +
        // '                            range-separator="至"\n' +
        // '                            start-placeholder="开始日期"\n' +
        // '                            end-placeholder="结束日期"\n' +
        // '                            value-format="yyyy-MM-dd"\n' +
        // '                            style="width: 270px;">\n' +
        // '                    </el-date-picker>\n' +
        '                    <el-input\n' +
        '                            placeholder="标题"\n' +
        '                            size="mini"\n' +
        '                            :name="queryStr"\n' +
        '                            v-model="searchListForm[queryStr]"\n' +
        '                            @keyup.enter.native="getRecommendList"\n' +
        '                            class="w300">\n' +
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
    // '                <el-table-column label="项目名称"  align="center"><template slot-scope="scope">{{scope.row.title | textEllipsis}}</template></el-table-column>\n' +
    '                <el-table-column label="标题" align="center"><template slot-scope="scope">\n'+
    '                   <el-tooltip class="item" effect="dark" :content="scope.row.title" placement="bottom">\n'+
    '                       <span class="over-flow-tooltip project-office-tooltip">{{scope.row.title | textEllipsis}}</span>\n'+
    '                   </el-tooltip></template></el-table-column>\n' +
    '                <el-table-column label="栏目名称" align="center"><template slot-scope="scope">\n'+
    '                   <el-tooltip class="item" effect="dark" :content="scope.row.cmsCategory ? scope.row.cmsCategory.name : \'-\'" placement="bottom">\n'+
    '                       <span class="over-flow-tooltip project-office-tooltip">{{scope.row.cmsCategory ? scope.row.cmsCategory.name : \'-\'}}</span>\n'+
    '                   </el-tooltip></template></el-table-column>\n' +
    // '                <el-table-column label="所属栏目" align="center"><template slot-scope="scope">{{scope.row.module | selectedFilter(moduleEntries)}}</template></el-table-column>\n' +
    '                <el-table-column label="状态" align="center"><template slot-scope="scope">{{scope.row.publishStatus == \'1\' ? \'发布\' : \'未发布\'}}</template>\n' +
    '                </el-table-column>\n' +
    '                <el-table-column label="发布时间" align="center"><template slot-scope="scope"><span v-if="scope.row.publishStatus == \'1\'">{{scope.row.publishStartdate | formatDateFilter(\'YYYY-MM-DD\')}}</span></template>\n' +
    '                </el-table-column>\n' +
    '                <el-table-column label="发布到期" align="center"><template slot-scope="scope">\n' +
'                       <span v-if="scope.row.publishStatus == \'1\' && scope.row.publishhforever == \'0\'">{{scope.row.publishEnddate | formatDateFilter(\'YYYY-MM-DD\')}}</span>\n'+
'                       <span v-if="scope.row.publishStatus == \'1\' && scope.row.publishhforever == \'1\'">永久</span>\n'+
    '                </template></el-table-column>\n' +
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
    props: {
        relatedIds: {
            type: Array,
            require: true
        },
        url: {
            type: String,
            require: true
        }
    },
    data: function () {
        return {
            searchListForm: {
                pageNo: 1,
                pageSize: 10,
                publishStartdate: '', //有效开始时间
                publishEnddate: '', //有效结束时间
                articlepulishStartDate: '', //发布有效期
                articlepulishEndDate: '', //发布有效期 实例2018-09-10 10:00:00
                publishStatus: '1'
            },
            pageCount: 0,
            relateRecommendList: [],
            loading: false,
            multipleSelection: [],
            multipleSelectedId: [],
            publishDate: [],
            moduleList: [],
            dateTypes: [{label: '发布时间', value: 'articlepulishDate'}, {label: '有效期', value: 'publishStartdate'}],
            dateType: '',
            dateQuery: [],
            queryStr:''
        }
    },

    computed: {
        moduleEntries: function () {
            return this.getEntries(this.moduleList)
        }
    },
    methods: {

        //改变时间
        handleChangeDateQuery: function (value) {
            var dateType = this.dateType;
            if(!value){
                this.searchListForm.publishStartdate = '';
                this.searchListForm.publishEnddate = '';
                this.searchListForm.articlepulishStartDate = '';
                this.searchListForm.articlepulishEndDate = '';
            }else {
                if(dateType === 'articlepulishDate'){
                    this.searchListForm.articlepulishStartDate = moment(value[0]).format('YYYY-MM-DD');
                    this.searchListForm.articlepulishEndDate = moment(value[1]).format('YYYY-MM-DD');
                }else if(dateType === 'publishStartdate'){
                    this.searchListForm.publishStartdate = moment(value[0]).format('YYYY-MM-DD');
                    this.searchListForm.publishEnddate = moment(value[1]).format('YYYY-MM-DD');
                }
            }
            this.getRecommendList();
        },

        handleChangeDateType: function (value) {
            this.dateQuery = [];
            this.handleChangeDateQuery();
        },


        handlePaginationSizeChange: function (value) {
            this.searchListForm.pageSize = value;
            this.getRecommendList();
        },

        handlePaginationPageChange: function (value) {
            this.searchListForm.pageNo = value;
            this.getRecommendList();
        },

        getRecommendList: function () {
            var self = this;
            this.loading = true;
            this.$axios({
                method: 'POST',
                url: this.url + '?' + Object.toURLSearchParams(this.searchListForm)
            }).then(function (response) {
                var data = response.data;
                if (data.status == '1') {
                    self.pageCount = data.data.count;
                    self.searchListForm.pageSize = data.data.pageSize;
                    self.relateRecommendList = data.data.list || [];
                }
                self.loading = false;
            }).catch(function () {
                self.loading = false;
                self.$message({
                    message: '请求失败',
                    type: 'error'
                })
            });
        },

        getModuleList: function () {
            var self = this;
            this.$axios.get('/cms/cmsArticle/getModuleList').then(function (response) {
                self.moduleList = response.data.data;
            })
        },

        handleChangeSelection: function (value) {
            this.multipleSelection = value;
            this.multipleSelectedId = [];
            for (var i = 0; i < value.length; i++) {
                this.multipleSelectedId.push(value[i].id);
            }
        },

        dateTypeChange: function () {
            if (this.searchListForm.dateType && this.publishDate.length > 0) {
                this.getRecommendList();
            }
        },

        relateRecommend: function (row) {
            var relatedIds = [].concat(this.relatedIds);
            relatedIds.push(row.id)
            this.$emit('change', {
                relatedIds: relatedIds,
                selection: row,
                type: 'single'
            })
        },
        cancelRelateRecommend: function (row) {
            var relatedIds = [].concat(this.relatedIds);
            relatedIds = relatedIds.filter(function (item) {
                return item != row.id;
            });
            this.$emit('change', {
                relatedIds: relatedIds,
                selection: row,
                type: 'single'
            })
        },
        batchCancelRelateRecommend: function () {
            var relatedIds = this.relatedIds, nRelatedIds = [];
            var multipleSelectedId = this.multipleSelectedId;
            relatedIds.forEach(function (item) {
                if(multipleSelectedId.indexOf(item) == -1){
                    nRelatedIds.push(item)
                }
            });
            this.$emit('change', {
                relatedIds: nRelatedIds,
                selection: this.multipleSelection,
                type: 'batch'
            })
        },

        batchRelateRecommend: function () {
            var relatedIds = this.relatedIds, nRelatedIds = [].concat(relatedIds);
            var multipleSelectedId = this.multipleSelectedId;
            multipleSelectedId.forEach(function (item) {
                if(relatedIds.indexOf(item) == -1){
                    nRelatedIds.push(item)
                }
            });
            this.$emit('change', {
                relatedIds: nRelatedIds,
                selection: this.multipleSelection,
                type: 'batch'
            })
        }


    },
    beforeMount: function () {
        this.getModuleList();
        this.getRecommendList();
        if(this.url.indexOf('cmsArticleList') > -1){
            this.queryStr = 'title';
        }else{
            this.queryStr = 'queryStr';
        }
        this.searchListForm[this.queryStr] = '';
    },
    mounted: function () {

    }
});