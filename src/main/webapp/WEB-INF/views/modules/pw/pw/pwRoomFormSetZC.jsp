<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <%--<script type="text/javascript" src="${ctxStatic}/treeTransfer/treeTransfer.min.js"></script>--%>
<body>


<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60 room-asset-assign">
    <div class="mgb-20">
        <shiro:hasPermission name="pw:pwRoom:edit">
            <edit-bar second-name="资产分配" href="/pw/pwRoom/tree"></edit-bar>
        </shiro:hasPermission>
        <shiro:lacksPermission name="pw:pwRoom:edit">
            <edit-bar second-name="查看" href="/pw/pwRoom/tree"></edit-bar>
        </shiro:lacksPermission>
    </div>
    <e-panel class="assign-header">
        <span class="current-room">当前房间：{{pwRoom.pwSpace | filterPwRoomAddress(pwSpaceListEntries)}}<span
                v-if="pwRoom.name">-{{pwRoom.name}}</span></span>
        <el-button size="mini" type="primary" :disabled="multipleSelectedId.length == 0" @click.stop.prevent="batchCancelAssign(multipleSelectedId.join(','))">
            批量取消分配
        </el-button>
    </e-panel>

    <el-container class="mgt-20">
        <el-aside width="250px" :style="{height:asideHeight + 'px'}" ref="assetTree">
            <div class="aside-search">
                <input type="text" style="display: none">
                <el-input name="roomAsset" size="mini" v-model="filterTreeText" placeholder="搜索"
                          @keyup.enter.native="searchTree">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="searchTree"></el-button>
                </el-input>
            </div>
            <div class="aside-tree" :style="{height:asideHeight-59 + 'px'}">
                <el-tree
                        ref="tree"
                        :props="props"
                        :data="collegesTree"
                        show-checkbox
                        node-key="level"
                        :default-expanded-keys="defaultExpandedKeys"
                        @check-change="handleCheckChange">
                </el-tree>
            </div>
        </el-aside>
        <el-main>

            <div class="assign-main-box">

                <div class="assign-arrow-box">
                    <el-button :class="{active:checkAssetIds.length > 0}" :disabled="checkAssetIds.length == 0" icon="el-icon-jiantou"
                               @click.stop.prevent="assignAsset"></el-button>
                </div>

                <div class="table-container" v-loading="loading">
                    <el-table size="mini" :data="pageList" class="table" @selection-change="handleSelectionChange">
                        <el-table-column
                                type="selection"
                                width="60">
                        </el-table-column>
                        <el-table-column prop="pwCategory.name" align="center" label="资产类别">
                        </el-table-column>
                        <el-table-column prop="name" align="center" label="资产编号">
                        </el-table-column>
                        <el-table-column prop="specification" align="center" label="型号">
                        </el-table-column>
                        <el-table-column align="left" label="负责人">
                            <template slot-scope="scope">
                                <div>
                                    <el-tooltip :content="scope.row.respName" popper-class="white" placement="right">
                                        <span class="break-ellipsis">{{scope.row.respName}}</span>
                                    </el-tooltip>
                                </div>
                                <div>{{scope.row.respMobile}}</div>
                            </template>
                        </el-table-column>
                        <el-table-column align="center" label="操作">
                            <template slot-scope="scope">
                                <div class="table-btns-action">
                                    <el-button type="text" size="mini"
                                               @click.stop.prevent="batchCancelAssign(scope.row.id)">取消分配
                                    </el-button>
                                </div>
                            </template>
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

        </el-main>
    </el-container>


</div>

<script>

    'use strict';
    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin],
        data: function () {
            var pwRoom = JSON.parse(JSON.stringify(${fns:toJson(pwRoom)})) || [];
            return {
                pwRoom: pwRoom,
                filterTreeText: '',
                filterTreePIds: [],
                props: {
                    label: 'name',
                    children: 'children'
                },
                colleges: [],
                collegesProps: {
                    parentKey: 'pId',
                    id: 'id'
                },
                checkAssetIds: [],

                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    'pwRoom.id': pwRoom.id || ''
                },
                pageCount: 9,
                pageList: [],
                loading: false,
                multipleSelectedId: [],
                spaceList: [],
                asideHeight: 0,
                message:'${message}',
                treeList:[],
                flattenColleges:[],
                defaultExpandedKeys:[1]
            }
        },
        watch: {},
        computed: {
            pwSpaceListEntries: function () {
                var entries = {};
                this.spaceList.forEach(function (item) {
                    entries[item.id] = item;
                });
                return entries;
            },
            treeListIdEntries:function () {
                var entries = {};
                this.treeList.forEach(function (item) {
                    entries[item.id] = item;
                });
                return entries;
            }
        },
        methods: {
            getDataGroup:function (parent,child) {
                var arr = [];
                parent.forEach(function (p) {
                    arr.push(p);
                    if(child.status){
                        child.datas.forEach(function (c) {
                            if(p.id == c.pwCategory.id){
                                var obj = {
                                    id: c.id,
                                    isParent: false,
                                    name: c.name,
                                    pId: p.id,
                                    open: false
                                };
                                arr.push(obj);
                            }
                        })
                    }
                });
                return arr;
            },
            getAssetTree: function () {
                var self = this,dataParent = [],dataChild = {},tree = [];
                this.$axios.get('/pw/pwCategory/treeData?isParent=true').then(function (response) {
                    dataParent = response.data || [];
                    if (dataParent.length > 0) {
                        self.$axios.get('/pw/pwFassets/treeDataAll').then(function (response) {
                            var dataChild = response.data || {};
                            tree = self.getDataGroup(dataParent,dataChild);
                            self.treeList = JSON.parse(JSON.stringify(tree));
                            self.setColleges(self.treeList);
                            self.searchTree();
                        })
                    }
                })
            },
            setLevel:function (tree) {
                var self = this;
                tree.forEach(function (item) {
                    item.level = item.dots.split('-').length;
                    if(item.children && item.children.length > 0){
                        self.setLevel(item.children);
                    }
                })
            },
            setColleges:function (college) {
                this.collegeEntries = {};
                this.colleges = JSON.parse(JSON.stringify(college));
                this.setCollegeEntries(this.colleges);
                var rootIds = this.setCollegeRooIds(this.colleges);
                this.collegesTree = this.getCollegesTree(rootIds, this.collegesProps);
                this.flattenColleges = this.getFlattenColleges(1);
                this.setLevel(this.collegesTree);
            },
            getConnectData:function (arr) {
                var self = this,parentIds = [],childrenIds = [],relateIdsCf = [],relateList = [];
                arr.forEach(function (item) {
                    parentIds = parentIds.concat(self.getParentIds(item,self.treeListIdEntries));
                    childrenIds = childrenIds.concat(self.getChildrenIds(item,self.collegeEntries));
                    relateIdsCf = parentIds.concat(childrenIds);
                });
                relateIdsCf = relateIdsCf.concat(arr.map(function (item) {
                    return item.id;
                }));
                var qc = {};
                relateIdsCf.forEach(function (item) {
                    if(!qc[item]){
                        qc[item] = true;
                        if(item in self.treeListIdEntries){
                            relateList.push(self.treeListIdEntries[item]);
                        }
                    }
                });
                return relateList;
            },
            getParentIds:function (obj,entries) {
                var parentId = obj.pId;
                var pIds = [];
                while (parentId){
                    var parent = entries[parentId];
                    if(!parent) break;
                    pIds.push(parent.id);
                    parentId = parent.pId;
                }
                return pIds;
            },
            getChildrenIds:function (obj,entries) {
                var child = [],cIds = [];
                child = entries[obj.id].children ? entries[obj.id].children : [];
                cIds = this.cycle(child);
                return cIds;
            },
            cycle:function (child) {
                var self = this;
                var ids = [];
                if(child && child.length > 0){
                    child.forEach(function (item) {
                        ids.push(item.id);
                        ids = ids.concat(self.cycle(item.children));
                    })
                }
                return ids;
            },
            searchTree: function () {
                var reg = new RegExp(this.filterTreeText),list = [],relateList = [];
                this.defaultExpandedKeys = [1];
                if(this.filterTreeText){
                    this.defaultExpandedKeys = [1,2,3];
                }
                this.setColleges(this.treeList);
                list = [].concat(this.treeList.filter(function (item) {
                    return reg.test(item.name);
                }));
                relateList = this.getConnectData(list);
                this.setColleges(relateList);

            },
            handleCheckChange: function () {
                var checkNodes = this.$refs.tree.getCheckedNodes();
                var ids = [];
                checkNodes.forEach(function (item) {
                    if (!item.isParent) {
                        ids.push(item.id)
                    }
                });
                this.checkAssetIds = ids;
            },
            assignAsset: function () {
                var self = this;
                this.$axios.get('/pw/pwFassets/ajaxSetPL?pwRoom.id=' + self.pwRoom.id + '&id=' + self.checkAssetIds.join(',')).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getAssetTree();
                        self.getDataList();
                    }
                    self.$message({
                        message: data.status == '1' ? '分配成功' : data.msg || '分配失败',
                        type: data.status == '1' ? 'success' : 'error'
                    });
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    });
                })
            },
            batchCancelAssign: function (ids) {
                var self = this;
                this.$axios({
                    method: 'POST',
                    url: '/pw/pwFassets/cancelFassets',
                    params: {
                        ids: ids
                    }
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getAssetTree();
                        self.getDataList();
                    }
                    self.$message({
                        message: data.status == '1' ? '取消分配成功' : data.msg || '取消分配失败',
                        type: data.status == '1' ? 'success' : 'error'
                    });
                }).catch(function () {
                    self.$message({
                        type: 'error',
                        message: '请求失败'
                    });
                })
            },

            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwFassets/listYfp?' + Object.toURLSearchParams(this.searchListForm)
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        self.pageList = data.data.list || [];
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
            getSpaceList: function () {
                var self = this;
                return this.$axios.get('/pw/pwSpace/treeData').then(function (response) {
                    self.spaceList = response.data;
                })
            },
            handleSelectionChange: function (val) {
                this.multipleSelectedId = [];
                for (var i = 0; i < val.length; i++) {
                    this.multipleSelectedId.push(val[i].id);
                }
            },
            handlePaginationSizeChange: function (pageSize) {
                this.searchListForm.pageSize = pageSize;
                this.getDataList();
            },
            handlePaginationPageChange: function (curPage) {
                this.searchListForm.pageNo = curPage;
                this.getDataList();
            }
        },
        created: function () {
            this.getSpaceList();
            this.getAssetTree();
            this.getDataList();
            this.$nextTick(function () {
                this.asideHeight = document.documentElement.clientHeight - this.$refs.assetTree.$el.offsetTop - 40;
            });
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