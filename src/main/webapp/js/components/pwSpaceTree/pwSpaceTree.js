/**
 * Created by Administrator on 2018/5/18.
 */

+function ($, Vue) {
    var pwSpaceTree = Vue.component('pw-space-tree', {
        template: '<div class="table-container">' +
        '<div class="mgb-20" style="padding-left: 15px;padding-top: 15px;"><el-button type="primary" size="mini" :disabled="disabled" @click="saveRule">保存</el-button></div> ' +
        '<el-table class="e-table-tree" :data="flattenPwSpaceList" size="mini" :cell-style="eTableCellStyle">' +
        '<el-table-column label="可使用场地" width="320"><template slot-scope="scope"> <span class="e-table-tree-dot" v-if="index>0" v-for="(dot, index) in scope.row.dots.split(\'-\').length"></span> ' +
        '<i :class="elIconCaret(scope.row)" class="el-icon-caret-right" @click.stop.prevent="handleExpandCell(scope.row)"></i> ' +
        '<el-checkbox  v-model="scope.row.allChecked" :indeterminate="scope.row.indeterminate" @change="handleSpaceAllChecked(scope.row)"> </el-checkbox> ' +
        '<span class="e-checkbox__label_dr_card">{{scope.row.sname}}</span> </template></el-table-column><el-table-column label="门禁">' +
        '<template slot-scope="scope">' +
        '<el-checkbox v-if="spaceGitems[scope.row.sid] && scope.row.children" class="el-checkbox-group__dr_card" :indeterminate="scope.row.doorIsIndeterminate" v-model="scope.row.doorAllChecked" @change="handleDoorAllCheckChange(scope.row)">全选 ' +
        '</el-checkbox><el-checkbox-group v-model="scope.row.doorsChecked" class="el-checkbox-group__dr_cards" @change="handleDoorSingleChange(scope.row)" v-if="spaceGitems[scope.row.sid]"><el-checkbox size="mini" v-for="(door, index2) in spaceGitems[scope.row.sid]" :disabled="door.canSel === \'0\' && door.sel !== \'1\'" :key="door.id" :label="door.id" >{{door.dname}}</el-checkbox></el-checkbox-group></template></el-table-column></el-table></div>',
        mixins: [Vue.drPwSpaceListMixin],
        props: {
            spaceGitems: {
                type: Object,
                default: function () {
                    return {}
                }
            },
            disabled: Boolean,
            id: String
        },
        data: function () {
            return {
                drPwSpaceList: [],
                flattenPwSpaceList: [],
            }
        },
        watch: {
            spaceGitems: function () {
                var pwSpaceList = this.flattenPwSpaceList;
                for (var i = 0; i < pwSpaceList.length; i++) {
                    var pwSpace = pwSpaceList[i];
                    var doorIsIndeterminates = [];
                    var doors = this.spaceGitems[pwSpace.sid];
                    if (!doors) {
                        continue;
                    }
                    Vue.set(pwSpace,'doors', doors);
                    pwSpace.doorsChecked = [];
                    for (var d = 0; d < doors.length; d++) {
                        if (doors[d].sel == '1') {
                            var door = doors[d];
                            pwSpace.doorsChecked.push(door.id);
                            if(door.canSel === '0' && door.sel !== '1'){
                                continue;
                            }
                            doorIsIndeterminates.push(door.id)
                        }
                    }
                    pwSpace.doorIsIndeterminate = doorIsIndeterminates.length < doors.length && doorIsIndeterminates.length != 0;
                    pwSpace.doorAllChecked = pwSpace.doorsChecked.length === doors.length;
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
                if (!children) {
                    return;
                }
                row.isExpand = !row.isExpand;
                if (row.isExpand) {
                    this.expandCellTrue(children, row.isExpand);
                    return
                }
                this.expandCellFalse(children, row.isExpand);
            },

            expandCellTrue: function (list, b) {
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    item.isCollapsed = !b;
                    item.isExpand = false;
                }
            },

            expandCellFalse: function (list, b) {
                var childrenIds = this.getPwSpaceChildrenIds(list);
                for (var i = 0; i < this.flattenPwSpaceList.length; i++) {
                    var item = this.flattenPwSpaceList[i];
                    if (childrenIds.indexOf(item.sid) > -1) {
                        item.isCollapsed = !b;
                        item.isExpand = false;
                    }
                }
            },

            handleDoorSingleChange: function (row) {
                var doorsCheckedLen = row.doorsChecked.length;
                row.doorAllChecked = doorsCheckedLen === row.doors.length;
                row.doorIsIndeterminate = doorsCheckedLen > 0 && doorsCheckedLen < row.doors.length;
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

            //
            handleDoorAllCheckChange: function (row) {
                if (row.doorAllChecked) {
                    row.doorsChecked = this.getDoorIds(this.spaceGitems[row.sid]);
                } else {
                    row.doorsChecked = [];
                }
                row.doorIsIndeterminate = false;
            },

            getDoorIds: function (doors) {
                var ids = [];
                if(!doors){
                    return []
                }
                for (var d = 0; d < doors.length; d++) {
                    var door = doors[d];
                    if(door.canSel === '0' && door.sel !== '1'){
                        continue;
                    }
                    ids.push(door.id);
                }
                return ids;
            },

            extendPwSpaceList: function () {
                var pwSpaceList = this.flattenPwSpaceList;
                for (var i = 0; i < pwSpaceList.length; i++) {
                    var pwSpace = pwSpaceList[i];
                    var doorIsIndeterminates = [];
                    var doors = this.spaceGitems[pwSpace.id];
                    if (!doors) {
                        continue;
                    }
                    Vue.set(pwSpace,'doors', doors);
                    Vue.set(pwSpace, 'doorsChecked', []);
                    for (var d = 0; d < doors.length; d++) {
                        if (doors[d].sel == '1') {
                            var door = doors[d];
                            pwSpace.doorsChecked.push(door.id);
                            if(door.canSel === '0' && door.sel !== '1'){
                                continue;
                            }
                            doorIsIndeterminates.push(door.id)
                        }
                    }
                    pwSpace.doorIsIndeterminate = doorIsIndeterminates.length < doors.length && doorIsIndeterminates.length != 0;
                    pwSpace.doorAllChecked = pwSpace.doorsChecked.length === doors.length;
                }
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

            getSpaceList: function () {
                var self = this;
                return this.$axios.get('/dr/drCardreGitem/ajaxPwSpaceList').then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.drPwSpaceList = data.datas;
                        self.setDrPwSpaceEntries(self.drPwSpaceListCopy);
                        var rootIds = self.setDrPwSpaceRootIds(self.drPwSpaceListCopy);
                        self.drPwSpaceTree = self.getDrPwSpaceTreeTree(rootIds, self.drPwSpaceProps, self.drPwSpaceListCopy);
                        self.flattenPwSpaceList = self.getFlattenPwSpaceList(5);
                        self.extendPwSpaceList();
                    }
                })
            },
            handleSpaceAllChecked: function (row) {
                this.setSpaceChildrenAllChecked(row, row.allChecked);
                row.doorAllChecked = row.allChecked;
                this.handleDoorAllCheckChange(row);
            },

            saveRule: function () {
                var gItemIds = [];
                for (var i = 0; i < this.flattenPwSpaceList.length; i++) {
                    var space = this.flattenPwSpaceList[i];
                    var gitems = space.doorsChecked;
                    if (!gitems) {
                        continue;
                    }
                    for (var g = 0; g < gitems.length; g++) {
                        gItemIds.push({
                            'group': {
                                id: this.id
                            },
                            'erspace': {
                                id: gitems[g]
                            }
                        })
                    }
                }
                this.$emit('save-rule', {
                    id: this.id,
                    drCreGitems: gItemIds
                });
            }
        },
        beforeMount: function () {
            this.getSpaceList();
        }
    })
}(jQuery, Vue);