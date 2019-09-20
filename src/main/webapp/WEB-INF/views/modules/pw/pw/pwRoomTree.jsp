<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<style>
    .e-col-item .e-col-item_label{
        color:#e9432d;
    }
</style>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60 pw-room-tree">
    <edit-bar></edit-bar>
    <el-form ref="searchListForm" method="post" size="mini" :inline="true">
        <input name="pageNo" type="hidden" v-model.number="searchListForm.pageNo"/>
        <input name="pageSize" type="hidden" v-model.number="searchListForm.pageSize"/>
        <input name="pwSpace.id" type="hidden" v-model="searchListForm['pwSpace.id']">
        <div class="conditions">

            <e-condition label="基地" type="radio" :options="baseList" v-model="baseId" @change="handleChangeBase"
                         :default-props="{label: 'name', value: 'id'}"></e-condition>

            <e-condition label="楼栋" :options="buildList">

                <e-radio class="e-checkbox-all" name="build" v-model="buildId" label="" @change="handleChangeBuild">全部
                </e-radio>
                <e-radio-group class="e-radio-spaces" v-model="buildId" @change="handleChangeBuild">
                    <e-radio v-for="build in buildList" name="buildId" :class="{'is-siblings': build.isSiblings}"
                             :label="build.id" :key="build.id">{{build.name}}
                    </e-radio>
                </e-radio-group>
            </e-condition>

            <e-condition label="楼层" type="radio" :options="floorList" v-model="floorId" @change="handleChangeFloor"
                         :default-props="{label: 'name', value: 'id'}"></e-condition>

            <e-condition label="类型" type="radio" :options="pwRoomTypes" v-model="searchListForm.type"
                         @change="getRoomList"></e-condition>

        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <shiro:hasPermission name="pw:pwRoom:edit">
                    <el-button type="primary" size="mini" @click.stop.prevent="createRoom">
                        <i class="el-icon-circle-plus el-icon--left"></i>创建房间
                    </el-button>
                </shiro:hasPermission>
                <el-button size="mini" :disabled="multipleSelectedId.length == 0"
                           @click.stop.prevent="batchDelete(multipleSelectedId.join(','))"><i
                        class="iconfont icon-delete"></i>批量删除
                </el-button>
            </div>
            <div class="search-input">
                <input type="text" style="display:none">
                <el-input @keyup.enter.native="getRoomList"
                          name="keys"
                          placeholder="房间名/所属场地"
                          v-model="searchListForm.keys"
                          size="mini"
                          class="w300">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getRoomList"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>
    <div class="table-container" v-loading="loading">
        <el-table size="mini" :data="roomList" class="table" @selection-change="handleSelectionChange"
                  @sort-change="handleSortChange">
            <el-table-column
                    type="selection"
                    :selectable="selectable"
                    width="60">
            </el-table-column>
            <el-table-column align="left" label="房间信息">
                <template slot-scope="scope">
                    <div>
                        <el-tooltip :content="scope.row.name" popper-class="white" placement="right">
                            <span class="break-ellipsis">{{scope.row.name}}</span>
                        </el-tooltip>
                    </div>
                    <div>{{scope.row.type | selectedFilter(pwRoomTypeEntries)}}</div>
                </template>
            </el-table-column>
            <el-table-column align="left" prop="num" label="房间容量/占地面积">
                <template slot-scope="scope">
                    <div>{{scope.row.numtype | selectedFilter(numTypesEntries)}}：{{scope.row.num}}<span
                            v-if="scope.row.numtype == '1'">人</span><span v-else>个</span></div>
                    <div>占地面积：{{scope.row.area}}平方米</div>
                </template>
            </el-table-column>
            <el-table-column align="left" prop="num" label="房间负责人">
                <template slot-scope="scope">
                    <div>
                        <el-tooltip :content="scope.row.person" popper-class="white" placement="right">
                            <span class="break-ellipsis">{{scope.row.person}}</span>
                        </el-tooltip>
                    </div>
                    <div>{{scope.row.mobile}}</div>
                </template>
            </el-table-column>
            <el-table-column align="left" label="所属场地">
                <template slot-scope="scope">
                    {{scope.row.pwSpace | filterPwRoomAddress(pwSpaceListEntries)}}
                </template>
            </el-table-column>
            <el-table-column align="left" label="属性设置">
                <template slot-scope="scope">
                    <div><span class="attribute-set-label">临时预约</span>
                        <el-switch
                                size="mini"
                                :disabled="scope.row.numtype == 2"
                                v-model="scope.row.isUsable"
                                @change="handleIsUsableChange(scope.row)"
                                active-value="1"
                                inactive-value="0">
                        </el-switch>
                    </div>
                    <div><span class="attribute-set-label">定期租用</span>
                        <el-switch
                                size="mini"
                                :disabled="scope.row.numtype == 1"
                                v-model="scope.row.isAssign"
                                @change="handleIsAssignChange(scope.row)"
                                active-value="1"
                                inactive-value="0">
                        </el-switch>
                    </div>
                    <div><span class="attribute-set-label">多团队共用</span>
                        <el-switch
                                size="mini"
                                :disabled="scope.row.numtype == 1"
                                v-model="scope.row.isAllowm"
                                @change="handleIsAllowmChange(scope.row)"
                                active-value="1"
                                inactive-value="0">
                        </el-switch>
                    </div>
                </template>
            </el-table-column>
            <shiro:hasPermission name="pw:pwRoom:edit">
                <el-table-column align="center" label="操作">
                    <template slot-scope="scope">
                        <div><a :href="frontOrAdmin+'/pw/pwRoom/formSetZC?id='+scope.row.id">资产分配</a></div>
                        <div>
                            <el-button type="text" size="mini" @click.stop.prevent="isEdit(scope.row)">编辑</el-button>
                        </div>
                        <div>
                            <el-button type="text" size="mini" @click.stop.prevent="batchDelete(scope.row.id)">删除</el-button>
                        </div>
                    </template>
                </el-table-column>
            </shiro:hasPermission>
        </el-table>
        <div class="text-right mgb-20" v-if="total">
            <el-pagination
                    size="small"
                    @size-change="handlePaginationSizeChange"
                    background
                    @current-change="handlePaginationPageChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="searchListForm.pageSize"
                    layout="total, prev, pager, next, sizes"
                    :total="total">
            </el-pagination>
        </div>
    </div>
    <el-row :gutter="20" label-width="75px" style="color:#e9432d;margin-top: 10px;">
        <el-col>
            <e-col-item label="温馨提示：">
                <div>1、房间容量为工位数的，只能进行定期租用</div>
                <div>2、房间容量为容纳人数的，只能进行临时预约</div>
            </e-col-item>
        </el-col>
    </el-row>
</div>


<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var isAllowmList = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList('yes_no'))}));
            var pwRoomTypes = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList('pw_room_type'))}));
            return {
                numTypes: [],
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    'pwSpace.id': '',
                    keys: '',
                    type: ''
                },
                total: 0,
                isAllowmList: isAllowmList,
                pwRoomTypes: pwRoomTypes,
                baseId: '',
                buildId: '',
                floorId: '',
                roomList: [],
                spaceList: [],
                loading: true,
                multipleSelectedId: [],
                message: '${message}'
            }
        },


        computed: {
            pwRoomTypeEntries: function () {
                return this.getEntries(this.pwRoomTypes)
            },
            pwSpaceListEntries: function () {
                var entries = {};
                this.spaceList.forEach(function (item) {
                    entries[item.id] = item;
                });
                return entries;
            },

            isAllowmListEntries: function () {
                return this.getEntries(this.isAllowmList)
            },

            numTypesEntries: function () {
                return this.getEntries(this.numTypes);
            },

            baseList: {
                get: function () {
                    return this.spaceList.filter(function (item) {
                        return item.type === '2'
                    })
                }
            },

            buildList: {
                get: function () {
                    var buildList = [];
                    this.spaceList.forEach(function (item) {
                        if (item.type === '3') {
                            Vue.set(item, 'isSiblings', true);
                            buildList.push(item);
                        }
                    });
                    return buildList;
                }
            },

            floorList: function () {
                var self = this;
                if (!this.buildId) return [];
                return this.spaceList.filter(function (item) {
                    return item.pId === self.buildId;
                })
            }
        },
        methods: {
            selectable: function (row) {
                return row.isUsable === '0'
            },

            getRoomList: function () {
                var self = this;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwRoom/list',
                    params: Object.toURLSearchParams(this.searchListForm)
                }).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        data = data.data;
                        self.setRoomList(data)
                    } else {
                        self.emptyRoomList();
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.emptyRoomList();
                    self.loading = false;
                })
            },

            setRoomList: function (data) {
                this.roomList = data.list;
                this.searchListForm.pageNo = data.pageNo;
                this.searchListForm.pageSize = data.pageSize;
                this.total = data.count;
            },

            emptyRoomList: function () {
                this.searchListForm.pageNo = 1;
                this.total = 0;
                this.roomList = []
            },

            createRoom: function () {
                window.location.href = this.frontOrAdmin + '/pw/pwRoom/form'
            },

            handleIsUsableChange: function (row) {
                var self = this;
                this.$axios({
                    method: 'POST',
                    url: '/pw/pwRoom/updateRoom',
                    data: {
                        id: row.id,
                        isUsable: row.isUsable
                    }
                }).then(function (response) {
                    var data = response.data;
                    if (data.status != '1') {
                        self.$message({
                            message: data.msg || '临时预约开启失败',
                            type: 'success'
                        })
                    }
                }).catch(function () {
                    self.$message({
                        message: '请求失败',
                        type: 'error'
                    })
                })
            },

            handleIsAssignChange: function (row) {
                var self = this;
                this.$axios({
                    method: 'POST',
                    url: '/pw/pwRoom/updateRoom',
                    data: {
                        id: row.id,
                        isAssign: row.isAssign
                    }
                }).then(function (response) {
                    var data = response.data;
                    if (data.status != '1') {
                        self.getRoomList();
                        self.$message({
                            message: data.msg || '定期租用开启失败',
                            type: 'success'
                        });
                    }
                }).catch(function () {
                    self.$message({
                        message: '请求失败',
                        type: 'error'
                    })
                })
            },


            handleIsAllowmChange: function (row) {
                var self = this;
                this.$axios({
                    method: 'POST',
                    url: '/pw/pwRoom/updateRoom',
                    data: {
                        id: row.id,
                        isAllowm: row.isAllowm
                    }
                }).then(function (response) {
                    var data = response.data;
                    if (data.status != '1') {
                        self.$message({
                            message: data.msg || '多团队共用开启失败',
                            type: 'success'
                        })
                    }
                }).catch(function () {
                    self.$message({
                        message: '请求失败',
                        type: 'error'
                    })
                })
            },

            isEdit: function (row) {
                if (row.isEdit == '0') {
                    this.$message({
                        message: '房间被分配或预约,不能编辑该房间',
                        type: 'warning'
                    });
                    return false;
                }
                window.location.href = this.frontOrAdmin + '/pw/pwRoom/form?id=' + row.id;
            },

            batchDelete: function (ids) {
                var self = this;
                this.$confirm('是否删除房间？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method: 'GET',
                        url: '/pw/pwRoom/deleteRoom',
                        params: {
                            ids: ids
                        }
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getRoomList();
                        }
                        self.$message({
                            message: data.status == '1' ? '删除成功' : data.msg || '删除失败',
                            type: data.status == '1' ? 'success' : 'error'
                        })
                    }).catch(function (error) {
                        self.$message({
                            message: '请求失败',
                            type: 'error'
                        })
                    })
                })
            },

            handleChangeBuild: function () {
                var buildId = this.buildId;
                if (!buildId) {
                    this.floorId = '';
                    this.searchListForm['pwSpace.id'] = this.baseId;
                    this.getRoomList();
                    return false;
                }
                var build = this.buildList.filter(function (item) {
                    return item.id === buildId;
                });
                this.baseId = build[0].pId;
                this.floorId = '';
                this.searchListForm['pwSpace.id'] = buildId;
                this.getRoomList();
            },

            handleChangeBase: function () {
                var buildList = this.buildList;
                var value = this.baseId;
                buildList.forEach(function (item) {
                    Vue.set(item, 'isSiblings', !value ? true : item.pId === value);
                });
                this.buildId = '';
                this.floorId = '';
                this.searchListForm['pwSpace.id'] = value;
                this.getRoomList();
            },

            handleChangeFloor: function () {
                this.searchListForm['pwSpace.id'] = this.floorId;
                this.getRoomList();
            },

            getSpaceList: function () {
                var self = this;
                return this.$axios.get('/pw/pwSpace/treeData').then(function (response) {
                    self.spaceList = response.data;
                })
            },

            getNumTypes: function () {
                var self = this;
                this.$axios.post('/pw/pwRoom/pwRoomType').then(function (response) {
                    var data = response.data;
                    self.numTypes = data.data;
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
                this.getRoomList();
            },
            handlePaginationPageChange: function (curPage) {
                this.searchListForm.pageNo = curPage;
                this.getRoomList();
            },

            handleSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order;
                this.getRoomList();
            }
        },
        created: function () {
            this.getNumTypes();
            this.getSpaceList();
            this.getRoomList();
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