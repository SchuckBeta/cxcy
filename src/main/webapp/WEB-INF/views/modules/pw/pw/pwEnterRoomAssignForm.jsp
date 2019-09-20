<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" v-show="pageLoad" class="container-fluid mgb-60" style="display: none">
    <div class="mgb-20">
        <edit-bar second-name="分配"></edit-bar>
    </div>
    <div class="pw-enter-container--admin">
        <div class="pw-assign-room-titlebar">
            <span>场地搜索</span>
        </div>
        <el-form :model="assignRoomForm" ref="assignRoomForm" :rules="assignRoomRules" size="mini">
            <el-row :gutter="15">
                <el-col :span="10">
                    <el-form-item prop="num" :label="numTypeLabel">
                        <el-col :span="6">
                            <input type="text" style="display: none">
                            <el-input type="number" :disabled="hasNum" v-model.number="assignRoomForm.num"></el-input>
                        </el-col>
                        <el-col :span="8">
                            <el-checkbox v-model="hasNum" @change="assignRoomForm.num = ''">不限</el-checkbox>
                        </el-col>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item prop="isAlone" label-width="0">
                        <el-checkbox v-model="assignRoomForm.isAlone" :true-label="0" :false-label="1">与其他团队共用房间</el-checkbox>
                    </el-form-item>
                </el-col>
                <el-col :span="4">
                    <el-form-item class="text-right">
                        <el-button type="primary" @click.stop.prevent="searchRoomListAssigned">搜索房间</el-button>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <div class="pw-assign-room-titlebar">
            <span>场地分配</span>
        </div>
        <%--<div class="base-tree-block">--%>
        <%--<div class="conditions">--%>
        <%--<e-condition label="基地" type="radio" :options="baseList" v-model="baseId" @change="handleChangeBase"--%>
        <%--:is-show-all="false"--%>
        <%--:default-props="{label: 'name', value: 'id'}"></e-condition>--%>

        <%--<e-condition label="楼栋" :options="buildList">--%>
        <%--<e-radio-group class="e-radio-spaces" v-model="buildId" @change="handleChangeBuild">--%>
        <%--<e-radio v-for="build in buildList" name="buildId" :class="{'is-siblings': build.isSiblings}"--%>
        <%--:label="build.id" :key="build.id">{{build.name}}--%>
        <%--</e-radio>--%>
        <%--</e-radio-group>--%>
        <%--</e-condition>--%>

        <%--<e-condition label="楼层" type="radio" :options="floorList" v-model="floorId" @change="handleChangeFloor"--%>
        <%--:is-show-all="false"--%>
        <%--:default-props="{label: 'name', value: 'id'}"></e-condition>--%>
        <%--</div>--%>
        <%--</div>--%>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-tooltip content="请选择房间后批量分配" :disabled="multipleSelectionRoom.length > 0"
                            popper-class="white" placement="top">
                <span>
                    <el-button type="primary" size="mini" :disabled="multipleSelectionRoom.length == 0"
                               @click.stop.prevent="assignRooms">批量分配</el-button>
                </span>
                </el-tooltip>
            </div>
            <div class="search-input">
                <input type="text" style="display: none">
                <el-input
                        placeholder="房间名称"
                        size="mini"
                        name="keys"
                        v-model="searchRoomName"
                        keyup.enter.native="getFilterRoomNameList"
                        class="w300">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getFilterRoomNameList"></el-button>
                </el-input>
            </div>
        </div>
        <el-table v-loading="loading" :data="floorRoomList" size="small" class="table" ref="assignRoomTable"
                  style="margin-bottom: 20px;"
                  ref="multipleTableRoomList"
                  @sort-change="handleSortChange"
                  @selection-change="handleSelectionChangeRoom">
            <el-table-column
                    type="selection"
                    :selectable="handleSelectable"
                    width="60">
            </el-table-column>
            <el-table-column prop="name" label="房间名称">
                <template slot-scope="scope">
                    {{scope.row.name}}({{scope.row.pwSpace.id | getRoomNames(baseTreeEntries)}})
                </template>
            </el-table-column>
            <el-table-column prop="num" label="总工位数" align="center" sortable="custom"></el-table-column>
            <el-table-column prop="remaindernum" label="剩余工位数" align="center" sortable="custom">
                <template slot-scope="scope">
                    {{scope.row.remaindernum}}
                </template>
            </el-table-column>
            <el-table-column prop="needNum" label="分配工位数" align="center">
                <template slot-scope="scope">
                    <template v-if="scope.row.isAssigned === '1'">
                        {{scope.row.needNum}}
                    </template>
                    <template v-else>
                        <%--<el-input-number v-model.number="scope.row.needNum" :step="1" size="mini" :min="1"--%>
                                         <%--:max="getMaxNum(scope.row)"></el-input-number>--%>
                        <el-input type="number" v-model.number="scope.row.needNum" size="mini" style="width: 130px;"></el-input>
                    </template>
                </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" align="center">
                <template slot-scope="scope">
                    {{scope.row.isAssigned === '1' ? '已分配' : '未分配'}}
                </template>
            </el-table-column>
            <el-table-column prop="status" label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <template v-if="scope.row.isAssigned != '1'">
                            <el-button v-if="!scope.row.id" type="text" size="mini"
                                       @click.stop.prevent="assignRoom(scope.row)">分配
                            </el-button>
                            <el-button v-else-if="scope.row.id" type="text" size="mini"
                                       @click.stop.prevent="updateRoom(scope.row)">分配
                            </el-button>
                            <el-button v-if="scope.row.assignAble == '1'" type="text" size="mini"
                                       @click.stop.prevent="cancelAbelRoom(scope.row)">取消分配
                            </el-button>
                        </template>
                        <template v-else>
                            <el-button type="text" size="mini" @click.stop.prevent="setAssignRoomAble(scope.row)">重新分配
                            </el-button>
                            <el-button type="text" size="mini" @click.stop.prevent="confirmCancelRooms(scope.row)">取消分配
                            </el-button>
                        </template>

                    </div>
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

<script type="text/javascript">
    new Vue({
        el: '#app',
        data: function () {
            var pwEnterRoom = JSON.parse(JSON.stringify(${fns: toJson(pwEnterRoom)})) || {};
            var pwEnter = pwEnterRoom.pwEnter || {};
            return {
                assignRoomForm: {
                    num: pwEnter.expectWorkNum,
                    isAlone: 0
                },
                eid: pwEnter.id,
                hasFloorSpaceNum: false,
                hasNum: false,
                isAllowms: [{label: '否', value: '0'}, {label: '是', value: '1'}],
                assignRoomRules: {
                    num: [],
                },
                assignRoomList: [],
                baseTreeEntries: {},
                baseId: '',
                buildId: '',
                floorId: '',
                spaceList: [],
                multipleSelectionRoom: [],
                loading: false,
                roomPath: '',
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10
                },
                cacheRoom: {},
                originRoomList: [],
                searchRoomName: ''
            }
        },
        computed: {
            numTypeLabel: function () {
                return '所需要工位数：';
            },
            roomIds: function () {
                return this.assignRoomList.map(function (item) {
                    return item.id;
                })
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
            allFloorList: function () {
                return this.spaceList.filter(function (item) {
                    return item.type === '4'
                });
            },
            floorList: function () {
                var self = this;
                if (!this.buildId) return [];
                return this.spaceList.filter(function (item) {
                    return item.pId === self.buildId;
                })
            },


            floorIds: function () {
                var buildIds = [];
                var floorIds = [];
                if (this.floorId) {
                    return [this.floorId];
                }
                if (this.buildId) {
                    return this.floorList.map(function (item) {
                        return item.id;
                    })
                }
                if (this.baseId) {
                    var baseId = this.baseId;
                    this.buildList.forEach(function (item) {
                        if (item.pId === baseId) {
                            buildIds.push(item.id);
                        }
                    });
                    this.allFloorList.forEach(function (item) {
                        if (buildIds.indexOf(item.pId) > -1) {
                            floorIds.push(item.id)
                        }
                    });
                    return floorIds;
                }
                return this.allFloorList.map(function (item) {
                    return item.id
                });
            },
            allRoomList: function () {
                var floorIds = this.floorIds;
                return this.assignRoomList.filter(function (item) {
                    return floorIds.indexOf(item.pwSpace.id) > -1;
                });
            },
            floorRoomList: function () {
                var pageNo = this.searchListForm.pageNo;
                var pageSize = this.searchListForm.pageSize;
                return this.assignRoomList.slice((pageNo - 1) * pageSize, (pageNo) * pageSize)
            },
            pageCount: function () {
                return this.assignRoomList.length;
            }
        },
        methods: {

            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
            },

            handlePCPChange: function () {

            },

            handleSortChange: function (row) {
                var prop = row.prop;
                var order = row.order;
                if (!prop) {
                    this.assignRoomList = this.originRoomList;
                    return;
                }
                this.assignRoomList = this.assignRoomList.sort(function (item, item2) {
                    var val1 = item[prop];
                    var val2 = item2[prop];
                    if (order !== 'ascending') {
                        if (val1 > val2) {
                            return -1
                        } else if (val1 < val2) {
                            return 1
                        } else {
                            return 0
                        }
                    } else {
                        if (val1 < val2) {
                            return -1
                        } else if (val1 > val2) {
                            return 1
                        } else {
                            return 0
                        }
                    }
                })
            },

            getFilterRoomNameList: function () {
                var searchRoomName = this.searchRoomName;
                this.$refs.assignRoomTable.clearSort();
                if(!searchRoomName){
                    this.assignRoomList = this.originRoomList;
                }
                this.assignRoomList = this.assignRoomList.filter(function (item) {
                    return item.name.indexOf(searchRoomName) > -1
                })
            },

            getMaxNum: function (row) {
                if (row.isAssigned != '1' && !row.id) {
                    return row.remaindernum;
                }
                return row.remaindernum + row.originNeedNum;
            },


            searchRoomListAssigned: function () {
                this.baseId = '';
                this.buildId = '';
                this.floorId = '';
                this.roomPath = '';
                this.getRoomList();

            },

            handleChangeFloor: function (value) {
//                this.roomPath = this.getRoomNames(value);
            },

            handleSelectionChangeRoom: function (value) {
                this.multipleSelectionRoom = value;
            },

            handleSelectable: function (row) {
                return row.isAssigned !== '1' && row.assignAble !== '1';
            },

            confirmCancelRooms: function (row) {
                var self = this;
                this.$confirm("是否取消分配该房间？", "提示", {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.cancelRooms(row);
                }).catch(function () {

                })
            },

            setAssignRoomAble: function (row) {
                var self = this;
                this.$confirm("是否重新分配该房间？", "提示", {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.cacheRoom = JSON.parse(JSON.stringify(row));
                    Vue.set(row, 'assignAble', '1');
                    row.isAssigned = '0';
                }).catch(function () {

                })
            },

            cancelAbelRoom: function (row) {
                row.assignAble = '0';
                row.isAssigned = this.cacheRoom.isAssigned;
                row.needNum = this.cacheRoom.needNum;
            },

            assignRoom: function (row) {
                var params = this.getRoomParam(row);
                var reg = /^[1-9][0-9]*$/;
                var maxNum;
                if(!reg.test(row.needNum)){
                    this.$alert('请输入最小为1的整数值', '提示', {
                        type: 'error'
                    })
                    return false;
                }
                maxNum = this.getMaxNum(row);
                if(row.needNum > maxNum){
                    this.$alert('请输入不大于'+maxNum+'的数', '提示', {
                        type: 'error'
                    })
                    return false;
                }
                this.postAssignRooms(params);
            },

            updateRoom: function (row) {
                var params = this.getRoomParam(row);
                var reg = /^[1-9][0-9]*$/;
                var maxNum;
                if(!reg.test(row.needNum)){
                    this.$alert('请输入最小为1的整数值', '提示', {
                        type: 'error'
                    })
                    return false;
                }
                maxNum = this.getMaxNum(row);
                if(row.needNum > maxNum){
                    this.$alert('请输入不大于'+maxNum+'的数', '提示', {
                        type: 'error'
                    })
                    return false;
                }
                this.updateAssignRooms(row.id, row.needNum);
            },

            assignRooms: function () {
                var self = this;
                var params = this.multipleSelectionRoom.map(function (item) {
                    var params = self.getRoomParam(item);
                    return params.erooms[0];
                });
                var reg = /^[1-9][0-9]*$/;
                for(var i = 0; i <this.multipleSelectionRoom.length; i++){
                    var needNum = this.multipleSelectionRoom[i].needNum;
                    var maxNum;
                    if(!reg.test(needNum)){
                        this.$alert(this.multipleSelectionRoom[i].name+'房间请输入最小为1的整数值', '提示', {
                            type: 'error'
                        })
                        return false;
                    }
                    maxNum = this.getMaxNum(this.multipleSelectionRoom[i]);
                    if(needNum > maxNum){
                        this.$alert(this.multipleSelectionRoom[i].name+'房间请输入不大于'+maxNum+'的数', '提示', {
                            type: 'error'
                        })
                        return false;
                    }
                }
                this.postAssignRooms({
                    id: this.eid,
                    erooms: params
                });
            },

            postAssignRooms: function (params) {
                var self = this;
                this.$axios.post('/pw/pwEnterRoom/ajaxAssignRooms', params).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        window.parent.sideNavModule.changeStaticUnreadTag("/a/pw/pwEnter/getCountToFPCD");
                        self.getRoomList();
                        self.$message.success("分配成功")
                    } else {
                        self.$message.error(data.msg)
                    }
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            updateAssignRooms: function (id, num) {
                var self = this;
                this.$axios.post('/pw/pwEnterRoom/ajaxReassigned/' + id + '?num=' + num).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        window.parent.sideNavModule.changeStaticUnreadTag("/a/pw/pwEnter/getCountToFPCD");
                        self.getRoomList();
                        self.$message.success("重新分配成功")
                    } else {
                        self.$message.error(data.msg)
                    }
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg);
                })
            },


            cancelRooms: function (row) {
                var self = this;
                this.$axios.post('/pw/pwEnterRoom/ajaxCancelRooms', this.getRoomParam(row, true)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        window.parent.sideNavModule.changeStaticUnreadTag("/a/pw/pwEnter/getCountToFPCD");
                        self.getRoomList();
                        self.$message.success("取消分配成功")
                    } else {
                        self.$message.error(data.msg)
                    }
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            getRoomParam: function (row, isCancel) {
                return {
                    id: this.eid,
                    erooms: [
                        {id: row.roomId, num: (isCancel ? 1 : row.needNum)}
                    ]
                }
            },

            getSpaceList: function () {
                var self = this;
                return this.$axios.get('/pw/pwSpace/treeData').then(function (response) {
                    self.spaceList = response.data;
                    var entries = {};
                    self.spaceList.forEach(function (item) {
                        entries[item.id] = item;
                    })
                    self.baseTreeEntries = entries;
                })
            },

            handleChangeBuild: function (value) {
                var buildId = this.buildId;
//                this.roomPath = this.getRoomNames(value);
                if (!buildId) {
//                    this.baseId = '';
                    this.floorId = '';
                    return false;
                }
                var build = this.buildList.filter(function (item) {
                    return item.id === buildId;
                });
                this.baseId = build[0].pId;
                this.floorId = '';
            },

            handleChangeBase: function () {
                var buildList = this.buildList;
                var value = this.baseId;
                buildList.forEach(function (item) {
                    Vue.set(item, 'isSiblings', !value ? true : item.pId === value);
                });
//                this.roomPath = this.getRoomNames(value);
                this.buildId = '';
                this.floorId = '';
            },

//            getRoomNames: function (roomId) {
//                var names = [];
//                var parent = this.baseTreeEntries[roomId];
//                while (parent) {
//                    if (parent.pId === '1') {
//                        names.unshift(parent.name);
//                        break;
//                    }
//                    names.unshift(parent.name);
//                    parent = this.baseTreeEntries[parent.pId];
//                }
//                return names.join('/');
//            },

            getPwSpaceRooms: function () {
                return this.$axios.post('/pw/pwEnterRoom/ajaxPwRooms', this.assignRoomForm)
            },

            getPwRoomYfps: function () {
                return this.$axios.post('/pw/pwEnterRoom/ajaxPwRoomYfps', {eid: this.eid});
            },

            getRoomList: function () {
                var self = this;
                this.loading = true;
                this.searchRoomName = '';

                this.$axios.all([this.getPwSpaceRooms(), this.getPwRoomYfps()]).then(this.$axios.spread(function (responseRooms, responseRoomYfps) {
                    var dataRoom = responseRooms.data;
                    var dataYfps = responseRoomYfps.data;
                    var rooms = [];
                    var roomAssigned = {};
                    if (dataYfps.status === 1) {
                        var roomYfps = dataYfps.data || [];
                        roomYfps = roomYfps.map(function (item) {
                            var room = item.pwRoom;
                            room.needNum = item.num;
                            room.originNeedNum = item.num;
                            room.isAssigned = '1';
                            roomAssigned[room.id] = true;
                            room.roomId = room.id;
                            room.id = item.id;
                            return room;
                        });
                        rooms = rooms.concat(roomYfps)
                    }
                    if (dataRoom.status === 1) {
                        var roomNotAList = dataRoom.data || [];
                        roomNotAList = roomNotAList.map(function (item) {
                            item.needNum = '';
                            item.roomId = item.id;
                            item.id = '';
                            return item;
                        });
                        roomNotAList = roomNotAList.filter(function (item) {
                            return !roomAssigned[item.roomId]
                        })
                        rooms = rooms.concat(roomNotAList)
                    }
                    self.assignRoomList = rooms;
                    self.originRoomList = JSON.parse(JSON.stringify(rooms))
                    self.loading = false;
                    self.$refs.assignRoomTable.clearSort();
                    // Both requests are now complete
                })).catch(function (error) {
                    self.loading = false;
                    self.$message.error(self.xhrErrorMsg);
                });
            }
        },
        created: function () {
            this.getRoomList();
            this.getSpaceList();
        }
    })
</script>
</body>
</html>