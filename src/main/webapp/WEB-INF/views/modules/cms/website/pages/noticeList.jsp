<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="creative"/>

</head>
<body>

<div id="app" v-show="pageLoad" style="display: none;" class="container page-container mgb-60">
    <el-breadcrumb class="mgb-20" separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>通知公告</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="base-content-container base-content-list-container">
        <div class="base-content-container">
            <div class="recommend-block">
                <%--<h4 class="recommend-title"><span>通知公告</span></h4>--%>
                <ul class="recommend-list">
                    <li v-for="item in noticeList" class="recommend-item">
                        <a :href="frontOrAdmin + '/frontNotice/noticeView?id='+item.id"><i class="recommend-circle"></i>{{item.title}}</a>
                        <span class="date">
                        {{item.updateDate | formatDateFilter('YYYY-MM-DD')}}
                    </span>
                    </li>
                </ul>
                <div class="empty-color text-center" v-if="noticeList.length == 0">无通知公告...</div>
            </div>
        </div>
    </div>
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


<script type="text/javascript">
    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            return {
                noticeList: [],
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1
                },
                pageCount: 0
            }
        },
        methods: {
            getNoticeList: function () {
                var self = this;
                this.$axios.get('/frontNotice/getNoticeList?'+Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg);
                    if (checkResponseCode) {
                        var pageData = data.data;
                        if (pageData) {
                            self.noticeList = pageData.list || [];
                            self.pageCount = pageData.count;
                            self.searchListForm.pageSize = pageData.pageSize;
                            self.searchListForm.pageNo = pageData.pageNo;
                        }
                    }
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    });
                    self.noticeList = [];
                })
            },
            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getNoticeList();
            },

            handlePCPChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getNoticeList();
            }
        },
        beforeMount: function () {
            this.getNoticeList();
        }
    })
</script>
</body>
</html>