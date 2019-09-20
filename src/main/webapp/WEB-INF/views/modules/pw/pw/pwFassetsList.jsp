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

            <e-condition label="资产类别" type="radio" v-model="searchListForm['pwCategory.parent.id']" :default-props="{label:'name',value:'id'}"
                         name="type" :options="assetsTypes" @change="handleChangeAssetsTypes">
            </e-condition>
            <e-condition label="资产名称" type="radio" v-model="searchListForm['pwCategory.id']" :default-props="{label:'name',value:'id'}"
                         name="name" :options="assetsNames" @change="getDataList">
            </e-condition>
            <e-condition label="状态" type="radio" v-model="searchListForm.status"
                         name="status" :options="states" @change="getDataList">
            </e-condition>

        </div>

        <div class="search-block_bar clearfix mgt-20">
            <div class="search-btns">
                <shiro:hasPermission name="pw:pwFassets:edit">
                    <el-button type="primary" size="mini" @click.stop.prevent="goAddAsset">添加资产</el-button>
                    <el-button type="primary" size="mini" @click.stop.prevent="goAddPAsset">批量添加资产</el-button>
                    <el-button type="primary" size="mini" @click.stop.prevent="goAddPAssign">批量分配资产</el-button>
                </shiro:hasPermission>
            </div>
            <div class="search-input">
                <input type="text" style="display:none">
                <el-input name="pwRoomName" size="mini" class="w300" v-model="searchListForm['pwRoom.name']"
                          placeholder="房间名称" @keyup.enter.native="getDataList">
                    <el-button slot="append" class="el-icon-search" @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>

    </el-form>

    <div class="table-container">
        <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading"
                  @selection-change="handleSelectionChange" @sort-change="handleTableSortChange">
            <el-table-column
                    type="selection"
                    width="45">
            </el-table-column>
            <el-table-column prop="name" label="资产编号" align="left" min-width="80"  sortable="name">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.name" popper-class="white" placement="right">
                        <span class="break-ellipsis underline-pointer" @click.stop.prevent="goInfo(scope.row.id)">{{scope.row.name || ''}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column prop="c.name" label="资产名称" align="center" min-width="80" sortable="c.name">
                <template slot-scope="scope">
                        {{scope.row.pwCategory.name || ''}}
                </template>
            </el-table-column>
            <el-table-column prop="pwCategory.parentId" label="资产类别" align="center" min-width="80" >
                <template slot-scope="scope">
                    {{scope.row.pwCategory.parentId | selectedFilter(assetsTypesEntries)}}
                </template>
            </el-table-column>
            <el-table-column prop="respName" label="使用人" align="center" min-width="70">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.respName" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.respName}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column prop="startDate" label="开始使用时间" align="center" min-width="110">
                <template slot-scope="scope">
                    {{scope.row.startDate | formatDateFilter('YYYY-MM-DD HH:mm:ss')}}
                </template>
            </el-table-column>
            <el-table-column label="使用场地" align="center" min-width="90">
                <template slot-scope="scope">
                    <span v-if="scope.row.status == '1' && scope.row.pwRoom.pwSpace && scope.row.pwRoom.pwSpace.parentId != '0'">{{scope.row.pwRoom.pwSpace | filterPwRoomAddress(pwSpaceListEntries)}}-{{scope.row.pwRoom.name}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="respMobile" label="联系方式" align="center" min-width="85">
            </el-table-column>
            <el-table-column prop="status" label="状态" align="center" min-width="70" sortable="status">
                <template slot-scope="scope">
                    {{scope.row.status | selectedFilter(statesEntries)}}
                </template>
            </el-table-column>
            <shiro:hasPermission name="pw:pwFassets:edit">
                <el-table-column label="操作" align="center" min-width="90">
                    <template slot-scope="scope">
                        <div class="table-btns-action">
                            <el-button size="mini" type="text" :disabled="scope.row.status == '1'" @click.stop.prevent="goChange(scope.row.id)">修改</el-button>
                            <el-button size="mini" type="text" v-if="scope.row.status == '0'" @click.stop.prevent="goAssign(scope.row.id)">分配</el-button>
                            <el-button size="mini" type="text" v-if="scope.row.status == '0'" @click.stop.prevent="markDestory(scope.row.id)">标记损坏</el-button>
                            <el-button size="mini" type="text" v-if="scope.row.status == '1'" @click.stop.prevent="cancelAssign(scope.row.id)">取消分配</el-button>
                            <el-button size="mini" type="text" v-if="scope.row.status == '2'" @click.stop.prevent="markEmpty(scope.row.id)">标记闲置</el-button>
                            <el-button size="mini" type="text" v-if="scope.row.status == '0' || scope.row.status == '2'" @click.stop.prevent="singleDelete(scope.row.id)">删除</el-button>
                        </div>
                    </template>
                </el-table-column>
            </shiro:hasPermission>

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
            var states = JSON.parse(JSON.stringify(${fns:toJson(fns:getDictList('0000000226'))}));
            return {
                assetsTypes:assetsTypes,
                assetsNames:[],
                states:states,
                pageCount: 0,
                message: '${message}',
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    orderBy: '',
                    orderByType: '',
                    'pwCategory.parent.id':'',
                    'pwCategory.id':'',
                    'pwRoom.name':'',
                    status:''

                },
                loading: false,
                pageList: [],
                spaceList:[],
                multipleSelection: [],
                multipleSelectedId: [],
                multipleSelectedStatus: []
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
            },
            statesEntries:{
                get:function () {
                    return this.getEntries(this.states);
                }
            }
        },
        methods: {
            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwFassets/listpage?' + Object.toURLSearchParams(this.searchListForm)
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
                this.searchListForm['pwCategory.id'] = '';
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
            goInfo:function (id) {
                window.location.href = this.frontOrAdmin + '/pw/pwFassets/details?id=' + id;
            },
            goAddAsset:function () {
                window.location.href = this.frontOrAdmin + '/pw/pwFassets/form';
            },
            goAddPAsset:function () {
                window.location.href = this.frontOrAdmin + '/pw/pwFassets/batchForm';
            },
            goAddPAssign:function () {
                var flag = true;
                if(this.multipleSelectedId.length == 0){
                    this.$message({
                        message:'请选择资产',
                        type:'warning'
                    });
                    return false;
                }
                for(var i = 0; i < this.multipleSelectedStatus.length; i++){
                    if(this.multipleSelectedStatus[i] != '0'){
                        this.$message({
                            message:'资产存在不是闲置的状态',
                            type:'warning'
                        });
                        flag = false;
                        break;
                    }
                }
                if(!flag){
                    return false;
                }
                window.location.href = this.frontOrAdmin + '/pw/pwFassets/assignForm?fassetsIds=' + this.multipleSelectedId.join(',');
            },

            goChange:function (id) {
                window.location.href = this.frontOrAdmin + '/pw/pwFassets/form?id=' + id;
            },
            goAssign:function (id) {
                window.location.href = this.frontOrAdmin + '/pw/pwFassets/assignForm?fassetsIds=' + id;
            },
            markDestory:function (id) {
                var self = this;
                this.$confirm('确认标记损坏吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method:'GET',
                        url:'/pw/pwFassets/changeBroken',
                        params:{
                            id:id
                        }
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        self.$message({
                            message: data.status == '1' ? '标记损坏成功' : data.msg || '标记损坏失败',
                            type: data.status == '1' ? 'success' : 'error'
                        })
                    }).catch(function (error) {
                        self.$message({
                            message: self.xhrErrorMsg,
                            type:'error'
                        })
                    })
                }).catch(function () {

                });
            },
            cancelAssign:function (id) {
                var self = this;
                this.$confirm('确认取消分配吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method:'GET',
                        url:'/pw/pwFassets/cancelFassets',
                        params:{
                            ids:id
                        }
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        self.$message({
                            message: data.status == '1' ? '取消分配成功' : data.msg || '取消分配失败',
                            type: data.status == '1' ? 'success' : 'error'
                        })
                    }).catch(function (error) {
                        self.$message({
                            message: self.xhrErrorMsg,
                            type:'error'
                        })
                    })
                }).catch(function () {

                });
            },
            markEmpty:function (id) {
                var self = this;
                this.$confirm('确认标记闲置吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method:'GET',
                        url:'/pw/pwFassets/changeUnused',
                        params:{
                            id:id
                        }
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        self.$message({
                            message: data.status == '1' ? '标记闲置成功' : data.msg || '标记闲置失败',
                            type: data.status == '1' ? 'success' : 'error'
                        })
                    }).catch(function (error) {
                        self.$message({
                            message: self.xhrErrorMsg,
                            type:'error'
                        })
                    })
                }).catch(function () {

                });
            },
            singleDelete:function (id) {
                var self = this;
                this.$confirm('确认要删除该固定资产吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method:'GET',
                        url:'/pw/pwFassets/delete',
                        params:{
                            id:id
                        }
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        self.$message({
                            message: data.status == '1' ? '删除成功' : data.msg || '删除失败',
                            type: data.status == '1' ? 'success' : 'error'
                        })
                    }).catch(function (error) {
                        self.$message({
                            message: self.xhrErrorMsg,
                            type:'error'
                        })
                    })
                }).catch(function () {

                });
            },

            handleSelectionChange: function (value) {
                this.multipleSelection = value;
                this.multipleSelectedId = [];
                this.multipleSelectedStatus = [];
                for (var i = 0; i < value.length; i++) {
                    this.multipleSelectedId.push(value[i].id);
                    this.multipleSelectedStatus.push(value[i].status);
                }
            },
            handleTableSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? (row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
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