<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

<body>


<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>

    <div class="mgb-20 text-right">
        <el-button size="mini" type="primary" icon="el-icon-setting" @click.stop="reset">重置</el-button>
    </div>

    <div class="table-container">
        <el-table :data="indexComponents" v-loading="loading" size="mini" class="table" ref="indexComponents" style="margin-bottom: 0">
            <el-table-column prop="sname"label="名称"></el-table-column>
            <el-table-column prop="remarks" label="备注"></el-table-column>
            <el-table-column prop="enable" align="center" label="显示/隐藏">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.enable" @change="saveIsShow(scope.row)"></el-switch>
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

</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data:  function () {
            return {
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1
                },
                pageCount: 0,
                indexComponents: [],
                loading:false
            }
        },
		methods:{
            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList();
            },

            handlePCPChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList();
            },

			reset:function(){
				var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/actyw/actYwGtheme/ajaxRest'
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getDataList();
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                });
			},
            getDataList:function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/actyw/actYwGtheme/ajaxList',
                    params: this.searchListForm
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.indexComponents = data.data.list || [];
                        self.pageCount = data.data.count || 0;
                        self.searchListForm.pageNo = data.data.pageNo || 1;
                        self.searchListForm.pageSize = data.data.pageSize || 10;
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                });
            },
            saveIsShow:function (value) {
                var self = this;
                this.loading = true;
                this.$axios({
                    method:'POST',
                    url:'/actyw/actYwGtheme/ajaxUpdateEnable?id=' + value.id + '&enable=' + value.enable
                }).then(function (response) {
                    var data = response.data;
                    if(data.status == '1'){
                        self.getDataList();
                    }else{
                        self.$message({
                            message: '操作失败',
                            type: 'error'
                        });
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                });
            }
		},
        created:function () {
            this.getDataList();
        }
    })

</script>

</body>
</html>