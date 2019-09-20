<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
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
        <el-breadcrumb-item>{{bcrumbsName}}</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="base-content-container base-content-list-container">
        <div class="mgb-20 text-center">
            <el-button v-for="(value, key, index) in notiyNames" :key="key"
                       :type="key == currentKey ? 'primary': 'default' " size="medium"
                       @click.stop.prevent="getNotifyList(key)">{{value}}
            </el-button>
        </div>
        <div class="recommend-block mgb-60">
            <ul class="recommend-list">
                <li v-for="item in notifyList" :key="item.id" class="recommend-item">
                    <a :href="frontOrAdmin + '/oa/oaNotify/viewDynamic?id='+item.id"><i
                            class="recommend-circle"></i>{{item.title}}</a>
                    <span class="date">{{item.updateDate}}</span>
                </li>
            </ul>
            <div v-if="notifyList.length < 1" class="empty-color text-center pdt-60">
                暂无{{notiyNames[currentKey]}}消息
            </div>
        </div>
        <div class="text-right">
            <el-pagination
                    size="small"
                    @size-change="handlePaginationSizeChange"
                    background
                    @current-change="handlePaginationPageChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="searchListForm.pageSize"
                    layout="prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>
</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            return {
                notiyNames: {
                    'homeSCDT': '',
                    'homeSCTZ': '',
                    'homeSSDT': ''
                },
                currentKey: '',
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    type: '4',
                    funcName: 'page1'
                },
                pageCount: 0,
                notifyList: [],
                homeSCDT: {
                    type: '4',
                    funcName: 'page1'
                },
                homeSCTZ: {
                    type: '8',
                    funcName: 'page2'
                },
                homeSSDT: {
                    type: '9',
                    funcName: 'page3'
                },
                bcrumbsName: ''
            }
        },
        methods: {
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getNotifyList(this.currentKey, true)
            },
            handlePaginationPageChange: function () {
                this.getNotifyList(this.currentKey, true)
            },
            getIndexInLayout: function () {
                var self = this;
                this.$axios.get('/cms/index/indexInLayout').then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg, false);
                    if (checkResponseCode) {
                        var notiyNames = data.data;
                        notiyNames.forEach(function (item) {
                            self.notiyNames[item.modelename] = item.modelname;
                        })
                    }
                }).catch(function (error) {
                    console.log(error)
                })
            },
            getNotifyList: function (key, isSearch) {
                var self = this;
                if (this.currentKey === key && !isSearch) {
                    return;
                }
                if (!isSearch) {
                    this.searchListForm.pageNo = 1;
                    this.searchListForm.pageSize = 10;
                }
                this.currentKey = key || 'homeSCDT';
                Object.assign(this.searchListForm, this[key || 'homeSCDT']);
                this.$axios.get('/oa/oaNotify/getPageJson?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data) {
                        self.notifyList = data.list || [];
                        self.pageNo = data.pageNo || 1;
                        self.pageSize = data.pageSize || 10;
                        self.pageCount = data.count || 0;
                    } else {
                        self.notifyList = [];
                    }
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    })
                })
            },

            getBcrumbsName: function () {
                var self = this;
                this.$axios.get('/cms/index/getBcrumbsName?code=homeNotice').then(function (response) {
                    var data = response.data;
                    self.bcrumbsName = data.data.modelname
                })
            }
        },
        beforeMount: function () {
            this.getIndexInLayout();
        },
        created: function () {
            var hash = location.hash;
            this.getBcrumbsName();
            this.getNotifyList(hash ? hash.replace('#', '') : false);
        }
    })

</script>

</body>
</html>
