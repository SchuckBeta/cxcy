<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <edit-bar></edit-bar>
    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="conditions">

            <e-condition label="资产类别" type="radio" v-model="searchListForm['pwFassets.pwCategory.parent.id']" :default-props="{label:'name',value:'id'}"
                         name="type" :options="assetsTypes" @change="handleChangeAssetsTypes">
            </e-condition>
            <e-condition label="资产名称" type="radio" v-model="searchListForm['pwFassets.pwCategory.id']" :default-props="{label:'name',value:'id'}"
                         name="name" :options="assetsNames" @change="getDataList">
            </e-condition>

        </div>

        <div class="search-block_bar clearfix mgt-20">
            <div class="search-input">
                <input type="text" style="display:none">
                <el-input name="keys" size="mini" class="w300" v-model="searchListForm.keys"
                          placeholder="编号/房间名称/使用人" @keyup.enter.native="getDataList">
                    <el-button slot="append" class="el-icon-search" @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>

    </el-form>

    <div class="table-container">
        <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading" @sort-change="handleTableSortChange">

            <el-table-column prop="f.name" label="资产编号" align="left" min-width="80"  sortable="f.name">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.pwFassets.name" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.pwFassets.name || ''}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column prop="c.name" label="资产名称" align="center" min-width="80" sortable="c.name">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.pwFassets.pwCategory.name" popper-class="white" placement="right" v-if="scope.row.pwFassets.pwCategory">
                        <span class="break-ellipsis">{{scope.row.pwFassets.pwCategory.name || ''}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column prop="c.name" label="资产类别" align="center" min-width="80" sortable="c.name">
                <template slot-scope="scope">
                    <span v-if="scope.row.pwFassets.pwCategory">{{scope.row.pwFassets.pwCategory.parentId | selectedFilter(assetsTypesEntries)}}</span>
                </template>
            </el-table-column>
            <el-table-column label="使用场地" align="center" min-width="100">
                <template slot-scope="scope">
                    <span v-if="scope.row.pwRoom && scope.row.pwRoom.pwSpace && scope.row.pwRoom.pwSpace.parentId != '0'">{{scope.row.pwRoom.pwSpace | filterPwRoomAddress(pwSpaceListEntries)}}-{{scope.row.pwRoom.name}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="respName" label="使用人" align="center" sortable="respName" min-width="80">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.respName" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.respName}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column prop="startDate" label="领用时间" align="center" min-width="105">
                <template slot-scope="scope">
                    {{scope.row.startDate | formatDateFilter('YYYY-MM-DD HH:mm:ss')}}
                </template>
            </el-table-column>
            <el-table-column prop="endDate" label="归还时间" align="center" min-width="105">
                <template slot-scope="scope">
                    {{scope.row.endDate | formatDateFilter('YYYY-MM-DD HH:mm:ss')}}
                </template>
            </el-table-column>
            <el-table-column prop="respMobile" label="联系方式" align="center" min-width="90">
            </el-table-column>

        </el-table>
        <div class="text-right mgb-20" v-if="pageCount">
            <el-pagination
                    size="small"
                    @size-change="handlePaginationSizeChange"
                    background
                    @current-change="handlePaginationPageChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="searchListForm.pageSize"
                    layout="total, prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>


</div>


<script>
    new Vue({
        el: '#app',
        data: function () {
            var assetsTypes = JSON.parse(JSON.stringify(${fns:toJson(fns:findChildrenCategorys(null))}));
            return {
                assetsTypes:assetsTypes,
                assetsNames:[],
                pageCount: 0,
                message: '${message}',
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    orderBy: '',
                    orderByType: '',
                    'pwFassets.pwCategory.parent.id':'',
                    'pwFassets.pwCategory.id':'',
                    keys:''

                },
                loading: false,
                pageList: [],
                spaceList:[]
            }
        },
        computed:{
            pwSpaceListEntries: function () {
                var entries = {};
                this.spaceList.forEach(function (item) {
                    entries[item.id] = item;
                });
                return entries;
            },
            assetsTypesEntries:{
                get:function () {
                    return this.getEntries(this.assetsTypes,{label:'name',value:'id'});
                }
            }
        },
        methods: {
            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwFassetsUhistory/listpage?' + Object.toURLSearchParams(this.searchListForm)
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        self.pageList = data.data.list || [];
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                    self.$message({
                        message: self.xhrErrorMsg,
                        type: 'error'
                    })
                });
            },

            handleChangeAssetsTypes:function (value) {
                this.searchListForm['pwFassets.pwCategory.id'] = '';
                if(!value){
                    this.assetsNames = [];
                    this.getDataList();
                    return false;
                }
                this.getDataList();
                var self = this;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwCategory/childrenCategory',
                    params:{
                        categoryId:value
                    }
                }).then(function (response) {
                    var data = response.data;
                    if (data) {
                        self.assetsNames = [];
                        self.assetsNames = data || [];
                    }
                });
            },

            handleTableSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? ( row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getDataList();
            },
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList();
            },

            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList();
            },
            getSpaceList: function () {
                var self = this;
                return this.$axios.get('/pw/pwSpace/treeData').then(function (response) {
                    self.spaceList = response.data;
                })
            }
        },
        created: function () {
            this.getSpaceList();
            this.getDataList();
            if (this.message) {
                this.$message({
                    message: this.message,
                    type: this.message.indexOf('成功') > -1 ? 'success' : 'warning'
                });
                this.message = '';
            }
        }
    })
</script>


</body>
</html>