<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>

<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container page-container pdt-60">
    <el-breadcrumb class="mgb-20" separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="/f"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>${cmsProject.modelname}</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="exc-pros-tab-nav">
        <el-button :type="proType == 'all'? 'primary': 'default' " size="medium"
                   @click.stop.prevent="handleChangeProjectList('all')">全部
        </el-button>
        <el-button :type="proType == 'project'? 'primary': 'default' " size="medium"
                   @click.stop.prevent="handleChangeProjectList('project')">优秀双创项目
        </el-button>
        <el-button :type="proType == 'gcontest'? 'primary': 'default' " size="medium"
                   @click.stop.prevent="handleChangeProjectList('gcontest')">大赛获奖作品
        </el-button>
    </div>
    <div class="text-right mgb-20">
        <el-input placeholder="请输入关键字" size="mini" v-model="searchListForm.queryStr" class="w300"
                  @keyup.enter.native="searchExcProjectList">
            <el-button slot="append" icon="el-icon-search" @click.stop.prevent="searchExcProjectList"></el-button>
        </el-input>
    </div>
    <div class="exc-pros-tab-contents">
        <div class="exc-pros-tab-content" v-loading="listLoading">
            <el-row :gutter="30" class="bc-row-4 exc-pros">
                <el-col v-for="(item, index) in excProList" :key="item.id" :span="6">
                    <div class="bc-article-block">
                        <div class="bc-article-pic">
                            <a :href="item.id | hrefFilter(frontOrAdmin)">
                                <img :src="item.thumbnail | ftpHttpFilter(ftpHttp) | proGConPicFilter">
                            </a>
                        </div>
                        <div class="bc-article-content">
                            <h5 class="title" style="height: 15px;"><a
                                    :href="item.id | hrefFilter(frontOrAdmin)">{{item.title}}</a></h5>
                            <e-col-item align="left" label="来源：">{{item.sourceName}}</e-col-item>
                            <e-col-item align="left" label="项目负责人：">{{item.leaderName}}</e-col-item>
                            <e-col-item align="left" label="学院：">{{item.collgeName}}</e-col-item>
                            <div class="actions">
                                <div class="view-good-date">
                                    <span class="date"><i class="el-icon-date"></i>{{item.articlepulishDate | formatDateFilter('YYYY-MM-DD')}}</span>
                                    <span class="views-goods likes"><i class="iconfont icon-dianzan1"></i>{{item.cmsArticleData ? item.cmsArticleData.likes : '0'}}</span>
                                    <span class="views-goods"><i
                                            class="iconfont icon-yanjing1"></i>{{item.views}}</span>

                                </div>
                            </div>
                        </div>
                    </div>
                    <!--<div class="exc-pro-block">-->
                    <!--<div class="exc-pro-pic">-->
                    <!--<a href="#">-->
                    <!--<img :src="item.thumbnail">-->
                    <!--</a>-->
                    <!--</div>-->
                    <!--<div class="exc-pro-content">-->
                    <!--<h5 class="title"><a href="#">{{item.title}}</a></h5>-->
                    <!--<e-col-item align="left" label="项目来源">：{{item.ptypename}}</e-col-item>-->
                    <!--<e-col-item align="left" label="项目负责人">：{{item.leadername}}</e-col-item>-->
                    <!--<e-col-item align="left" label="学院">：{{item.officeName}}</e-col-item>-->
                    <!--&lt;!&ndash;<p class="source">项目来源：大创项目</p>&ndash;&gt;-->
                    <!--&lt;!&ndash;<p class="leader">项目负责人： 宜文</p>&ndash;&gt;-->
                    <!--&lt;!&ndash;<p class="school">学院：其他</p>&ndash;&gt;-->
                    <!--</div>-->
                    <!--<div class="exc-pro-actions">-->
                    <!--<span class="views-goods"><i class="iconfont icon-dianzan1"></i>{{item.views}}</span>-->
                    <!--<span class="views-goods"><i class="iconfont icon-yanjing1"></i>{{item.likes}}</span>-->
                    <!--<span class="date"><i class="el-icon-date"></i>{{item.date}}</span>-->
                    <!--</div>-->
                    <!--</div>-->
                </el-col>
            </el-row>
            <div v-show="excProList.length < 1" class="empty-color text-center pdt-60">
                暂时无数据
            </div>
        </div>
    </div>
    <div class="text-right">
        <el-pagination
                size="small"
                @size-change="handlePaginationSizeChange"
                background
                @current-change="handlePaginationPageChange"
                :current-page.sync="searchListForm.pageNo"
                :page-sizes="[4,8,16,32,64]"
                :page-size="searchListForm.pageSize"
                layout="prev, pager, next, sizes"
                :total="pageCount">
        </el-pagination>
    </div>
</div>
<script type="text/javascript">
    'use strict';
    new Vue({
        el: '#app',
        data: function () {
            var hash = location.hash || 'all';
            if (hash) {
                hash = hash.replace('#', '');
                hash = (hash.indexOf('project') > -1 || hash.indexOf('gcontest') > -1) ? hash : 'all';
            }
            return {
                searchListForm: {
                    queryStr: '',
                    pageNo: 1,
                    pageSize: 16
                },
                pageCount: 0,
                proType: hash,
                listUrls: {
                    'all': '/cms/index/projectSecondList',
                    'project': '/cms/index/excellent/projectList',
                    'gcontest': '/cms/index/excellent/gcontestList'
                },
                listLoading: true,
                excProList: []
            }
        },
        filters: {
            hrefFilter: function (value, frontOrAdmin) {
                return frontOrAdmin + '/getOneCmsArticle?id=' + value;
            }
        },
        methods: {
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getExcProList(this.proType, true)
            },
            handlePaginationPageChange: function () {
                this.getExcProList(this.proType, true)
            },

            searchExcProjectList: function () {
                this.getExcProList(this.proType)
            },

            handleChangeProjectList: function (proType) {
                this.proType = proType;
                this.searchListForm.queryStr = '';
                this.searchListForm.pageNo = 1;
                this.searchListForm.pageSize = 16;
                location.hash = this.proType;
//                this.getExcProList(this.proType)
            },

            getExcProList: function (type, isSearch) {
                var url = this.listUrls[type || 'all'];
                var self = this;
//                if (this.proType === type && !isSearch) {
//                    return;
//                }
//                if (!isSearch) {
//                    this.searchListForm.pageNo = 1;
//                    this.searchListForm.pageSize = 16;
//                }
//                this.proType = type || 'all';
                this.listLoading = true;
                this.$axios.get(url + '?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status == 1 && data.data) {
                        data = data.data;
                        self.excProList = data.list || [];
                        self.searchListForm.pageNo = data.pageNo || 1;
                        self.searchListForm.pageSize = data.pageSize || 16;
                        self.pageCount = data.count || 0;
                    } else {
                        self.excProList = [];
                    }
                    self.listLoading = false;
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    })
                    self.listLoading = false;
                })
            }
        },
        beforeMount: function () {
            this.getExcProList(this.proType);
        },
        created: function () {
            var self = this;
            location.hash = this.proType;

            window.onhashchange = function () {
                var hash = location.hash || 'all';
                if (hash) {
                    hash = hash.replace('#', '');
                    hash = (hash.indexOf('project') > -1 || hash.indexOf('gcontest') > -1) ? hash : 'all';
                }
                self.proType = hash;
                self.getExcProList(self.proType);
            }
        }
    })

</script>

</body>
</html>