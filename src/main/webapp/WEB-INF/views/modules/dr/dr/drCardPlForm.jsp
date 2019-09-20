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
        <edit-bar second-name="${secondName}"></edit-bar>
    </div>
    <e-panel label="成员信息">
        <div class="table-container">
            <el-table :data="userDrCardList" class="table" size="mini">
                <el-table-column align="center" label="卡号">
                    <template slot-scope="scope">{{scope.row.no}}</template>
                </el-table-column>
                <el-table-column align="center" label="姓名">
                    <template slot-scope="scope">{{scope.row.tmpName}}</template>
                </el-table-column>
                <el-table-column align="center" label="学号">
                    <template slot-scope="scope">{{scope.row.tmpNo}}</template>
                </el-table-column>
                <el-table-column align="center" label="所属学院">
                    <template slot-scope="scope">{{scope.row.tmpOfficeXy}}</template>
                </el-table-column>
                <el-table-column align="center" label="有效期">
                    <template slot-scope="scope">{{scope.row.expiry | formatDateFilter('YYYY-MM-DD hh:mm')}}</template>
                </el-table-column>
            </el-table>
            <div class="mgb-20" style="padding-left: 15px;">
                <el-checkbox class="el-checkbox__label_font12" v-model="setAbleExpiry" @change="handleChangeSetAbleExpiry" style="margin-right: 15px;">是否统一设置有效时间</el-checkbox>
                <el-date-picker
                        v-show="setAbleExpiry"
                        size="mini"
                        v-model="expiry"
                        value-format="yyyy-MM-dd"
                        type="date"
                        :clearable="false"
                        :picker-options="expiryPickerOptions"
                        @change="handleChangeExpiryDate"
                        placeholder="选择有效时间">
                </el-date-picker>
            </div>
        </div>
    </e-panel>
    <e-panel label="授权其它房间使用权限">
        <div class="table-container">
            <el-table class="e-table-tree" :data="flattenPwSpaceList" size="mini" :cell-style="eTableCellStyle">
                <el-table-column label="可使用场地" width="320">
                    <template slot-scope="scope">
                        <span class="e-table-tree-dot" v-if="index>0" v-for="(dot, index) in scope.row.dots.split('-').length"></span>
                        <i :class="elIconCaret(scope.row)" class="el-icon-caret-right" @click.stop.prevent="handleExpandCell(scope.row)"></i>
                        <el-checkbox v-model="scope.row.allChecked" :indeterminate="scope.row.indeterminate"
                                     @change="handleSpaceAllChecked(scope.row)">
                        </el-checkbox>
                        <span class="e-checkbox__label_dr_card">{{scope.row.sname}}</span>
                    </template>
                </el-table-column>
                <el-table-column label="门禁">
                    <template slot-scope="scope">
                        <el-checkbox v-if="scope.row.doors.length > 0 && scope.row.children" class="el-checkbox-group__dr_card"
                                     :indeterminate="scope.row.doorIsIndeterminate" v-model="scope.row.doorAllChecked"
                                     @change="handleDoorAllCheckChange(scope.row)">全选
                        </el-checkbox>
                        <el-checkbox-group v-model="scope.row.doorsChecked" class="el-checkbox-group__dr_cards" @change="handleDoorSingleChange(scope.row)">
                            <el-checkbox :label="door.eptId + ',' + door.eptNo + ',' + door.drNo"
                                         v-for="door in scope.row.doors"
                                         :key="door.eptId + ',' + door.eptNo + ',' + door.drNo">{{door.dname}}
                            </el-checkbox>
                        </el-checkbox-group>
                    </template>
                </el-table-column>
            </el-table>
            <div class="mgb-20" style="padding-left: 15px;padding-top: 15px;">
                <el-button size="mini" type="primary" :disabled="isSubmit" @click.stop.prevent="postDrCardPlForm">发卡</el-button>
                <el-button size="mini" type="default" :disabled="isSubmit" @click.stop.prevent="backToPage">返回</el-button>
            </div>
        </div>
    </e-panel>

</div>


<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        mixins: [Vue.drPwSpaceListMixin],
        data: function () {
            var drPwSpaceList = JSON.parse('${pwSpaceInfo}');
            var rooms = JSON.parse('${fns: toJson(pwEnterInfo.rooms)}') || [];
            var drCards = JSON.parse('${fns: toJson(drCards)}');
            return {
                userDrCardList: drCards,
                drPwSpaceList: drPwSpaceList,
                flattenPwSpaceList: [],
                rooms: rooms,
                isSubmit: false,
                setAbleExpiry: false,
                expiry: new Date(),
                originExpiryEntire: {},
                expiryPickerOptions: {
                    disabledDate: function (time) {
                        return time.getTime() < Date.now();
                    }
                }
            }
        },
        computed: {
            backHref: {
                get: function () {
                    return this.frontOrAdmin + '/dr/drCard/' + (this.userId ? 'listByUser' : 'listByTemp')
                }
            }
        },
        methods: {
            //控制图标
            elIconCaret: function (row) {
                return {
                    'is-leaf': !row.children || !row.children.length,
                    'expand-icon': row.isExpand
                }
            },
            //控制行的样式
            eTableCellStyle: function (row) {
                return {
                    'display': row.row.isCollapsed ? 'none' : ''

                }
            },

            //控制行的展开收起
            handleExpandCell: function (row) {
                var children = row.children;
                if(!children){
                    return;
                }
                row.isExpand = !row.isExpand;
               if(row.isExpand){
                    this.expandCellTrue(children, row.isExpand);
                   return
               }
               this.expandCellFalse(children, row.isExpand);
            },

            expandCellTrue: function (list, b) {
                for(var i = 0; i < list.length; i++){
                    var item = list[i];
                    item.isCollapsed = !b;
                    item.isExpand = false;
                }
            },

            expandCellFalse: function (list, b) {
                var childrenIds = this.getPwSpaceChildrenIds(list);
                for(var i = 0; i < this.flattenPwSpaceList.length; i++){
                    var item = this.flattenPwSpaceList[i];
                    if(childrenIds.indexOf(item.sid) > -1){
                        item.isCollapsed = !b;
                        item.isExpand = false;
                    }
                }
            },

            handleSpaceAllChecked: function (row) {
                this.setSpaceChildrenAllChecked(row, row.allChecked);
                row.doorAllChecked = row.allChecked;
                this.handleDoorAllCheckChange(row);
            },


            backToPage: function () {
                location.href=this.backHref
            },

            handleChangeExpiryDate: function (value) {
                this.userDrCardList.forEach(function (item) {
                    item.expiry = value;
                })
            },

            handleChangeSetAbleExpiry: function (value) {
                var self = this;
                if(!value){
                    this.expiry = new Date();
                    this.userDrCardList.forEach(function (item) {
                        item.expiry = self.originExpiryEntire[item.id];
                    })
                }
            },

            //忽略
            setPwSpaceListIndeterminate: function (row) {
                var parents = this.getParentPwSpaceList(row);
                var isChildrenEveryChecked, isChildrenSomeChecked;
            },
            //忽略
            getParentPwSpaceList: function (row) {
                var parent = this.drPwSpaceEntries[row.pid];
                var parents = [];
                if (!parent) return parents;
                while (parent) {
                    parents.unshift(parent);
                    parent = this.drPwSpaceEntries[parent.pid];
                }
                return parents;
            },



            //判断是否全部被选中
            isChildrenEveryChecked: function (parent, row) {
                var pwSpace = this.drPwSpaceEntries[row.sid];
                var everyChecked = true;
                var childrenPwSpace;
                childrenPwSpace = pwSpace.children;
                var pwSpaceChildrenIds = this.getPwSpaceChildrenIds(childrenPwSpace);
                if(!pwSpaceChildrenIds.length){
                    return row.allChecked;
                }
                for (var i = 0; i < pwSpaceChildrenIds.length; i++) {
                    if (!this.drPwSpaceEntries[pwSpaceChildrenIds[i]].allChecked) {
                        everyChecked = false;
                        break;
                    }
                }
                return everyChecked;
            },

            //判断是否存在被选中的
            isChildrenSomeChecked: function (parent, row) {
                var pwSpace = this.drPwSpaceEntries[row.sid];
                var someChecked = false;
                var childrenPwSpace;
                childrenPwSpace = pwSpace.children;
                var pwSpaceChildrenIds = this.getPwSpaceChildrenIds(childrenPwSpace);
                if(!pwSpaceChildrenIds.length){
                    return row.allChecked;
                }
                for (var i = 0; i < pwSpaceChildrenIds.length; i++) {
                    if (!this.drPwSpaceEntries[pwSpaceChildrenIds[i]].allChecked) {
                        someChecked = true;
                        break;
                    }
                }
                return someChecked;
            },

            //获取所有子的ID
            getPwSpaceChildrenIds: function (list) {
                var ids = [];

                function getIds(list) {
                    if (!list) return ids;
                    for (var i = 0; i < list.length; i++) {
                        ids.push(list[i].sid);
                        getIds(list[i].children);
                    }
                }

                getIds(list);
                return ids;
            },

            //忽略
            setPwSpaceSiblings: function (pwSpace) {
                var flattenPwSpaceList = this.flattenPwSpaceList;
                var siblings = [];
                for (var i = 0; i < flattenPwSpaceList.length; i++) {
                    if (pwSpace.id === flattenPwSpaceList[i].pid) {
                        siblings.push(flattenPwSpaceList[i]);
                        pwSpace.siblings = siblings;
                    }
                }
            },

            //获取postData
            getDrCardPostData: function () {
                var nPostData = [];
                var dataKeys = ['id', 'no', 'status', 'password', 'user', 'cardType', 'tmpName', 'tmpTel', 'tmpNo', 'expiry'];
                var drEmentNos = this.getDrEmentNos();
                if(!drEmentNos.length){
                    return false;
                }
                for(var i = 0; i < this.userDrCardList.length; i++){
                    var item = this.userDrCardList[i];
                    var obj = {
                        'drEmentNos': drEmentNos
                    };
                    for(var k in item){
                        if(item.hasOwnProperty(k) && dataKeys.indexOf(k) > -1){
                            if(k === 'expiry'){
                                obj[k] = moment(item[k]).format('YYYY-MM-DD')
                            }else {
                                obj[k] = item[k];
                            }
                        }
                    }
                    nPostData.push(obj);
                }
                return nPostData
            },

            getDrEmentNos: function () {
                var flattenPwSpaceList = this.flattenPwSpaceList;
                var drEmentNos = [];
                for(var i = 0; i <flattenPwSpaceList.length; i++){
                    var item = flattenPwSpaceList[i];
                    var doorsChecked = item.doorsChecked;
                    if(!doorsChecked || !doorsChecked.length){
                        continue;
                    }
                    for (var d = 0; d < doorsChecked.length; d++) {
                        var door = doorsChecked[d].split(',');
                        drEmentNos.push({
                            etId: door[0],
                            etNo: door[1],
                            drNo: door[2]
                        })
                    }
                }
                return drEmentNos;
            },
            //发送数据
            postDrCardPlForm: function () {
                var self = this;
                var postData = this.getDrCardPostData();
                if(!postData){
                    this.$alert('请勾选门禁', '提示', {
                        confirmButtonText: '确定',
                        type: 'warning'
                    });
                    return;
                }
                this.isSubmit = true;
                this.$axios.post('/dr/drCard/ajaxCardPublishPl', {list: postData}).then(function (response) {
                    var data = response.data;
                    self.isSubmit = false;
                    self.show$message(data);
                    location.href = "${ctx}/dr/drCard/listByTemp?cardType=0";
                }).catch(function (error) {
                    self.show$message({
                        status: false,
                        msg: error.response.data
                    })
                    self.isSubmit = false;
                })
            },



            setSpaceChildrenAllChecked: function (row, isChecked) {
                var children = row.children;
                if (!children) {
                    return false;
                }
                for (var i = 0; i < children.length; i++) {
                    children[i].allChecked = isChecked;
                    children[i].doorAllChecked = children[i].allChecked;
                    this.handleDoorAllCheckChange(children[i]);
                    this.setSpaceChildrenAllChecked(children[i], isChecked);
                }
            },

            extendPwSpaceList: function () {
                var flattenPwSpaceList = this.flattenPwSpaceList;
                for (var i = 0; i < flattenPwSpaceList.length; i++) {
                    var fPwSpace = flattenPwSpaceList[i];
                    var doors = fPwSpace.doors;
                    for (var d = 0; d < doors.length; d++) {
                        if (doors[d].sel == '1') {
                            var door = doors[d];
                            fPwSpace.allChecked = true;//有问题
                            fPwSpace.doorsChecked.push(door.eptId + ',' + door.eptNo + ',' + door.drNo);
                        }
                    }
                }
            },

            handleDoorAllCheckChange: function (row) {
                if (row.doorAllChecked) {
                    row.doorsChecked = this.getDoorIds(row.doors);
                } else {
                    row.doorsChecked = [];
                }
                row.doorIsIndeterminate = false;
            },

            handleDoorSingleChange: function (row) {
                var doorsCheckedLen = row.doorsChecked.length;
                row.doorAllChecked = doorsCheckedLen === row.doors.length;
                row.doorIsIndeterminate = doorsCheckedLen > 0 && doorsCheckedLen < row.doors.length;
            },

            getDoorIds: function (doors) {
                var ids = [];
                for (var d = 0; d < doors.length; d++) {
                    var door = doors[d];
                    ids.push(door.eptId + ',' + door.eptNo + ',' + door.drNo);
                }
                return ids;
            },

            setOriginExpiryEntire: function () {
                var userCardList = JSON.parse(JSON.stringify(this.userDrCardList));
                var self = this;
                userCardList.forEach(function (item) {
                    self.originExpiryEntire[item.id] = item.expiry;
                })
            }
        },
        created: function () {
            this.flattenPwSpaceList = this.getFlattenPwSpaceList(3);
            this.extendPwSpaceList();
            this.setOriginExpiryEntire();

        },
        mounted: function () {
        }
    })

</script>
</body>
</html>