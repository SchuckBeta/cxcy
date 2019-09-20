/**
 * Created by Administrator on 2018/5/18.
 */

+function ($, Vue) {
    var pwSpaceTree = Vue.component('pw-space-tree', {
        template: '<div><div class="text-right"> <el-button type="primary" :border="true" size="mini" :disabled="disabled" @click="saveRule">保存</el-button></div><table v-tree-table class="table table-white">\n' +
        '            <thead>\n' +
        '            <tr>\n' +
        '                <th width="240">可使用场地</th>\n' +
        '                <th>门禁</th>\n' +
        '            </tr>\n' +
        '            </thead>\n' +
        '            <tbody>\n' +
        '            <tr v-for="(space, index) in spaceList" :key="space.sid" :id="space.sid" :pId="space.pid == \'1\' ? 0 : space.pid">\n' +
        '                <td class="text-left door-all_check">\n' +
        '                    {{space.sname}}\n' +
        '                </td>\n' +
        '                <td>\n' +
        '                    <template v-if="spaceGitems[space.sid]"><el-checkbox size="mini" v-for="(door, index2) in spaceGitems[space.sid]" :disabled="door.canSel === \'0\' && door.sel !== \'1\'" :key="door.id" :label="door.id" v-model="space.doorsChecked">{{door.dname}}</el-checkbox></template>\n' +
        '                </td>\n' +
        '            </tr>\n' +
        '            </tbody>\n' +
        '        </table></div>',
        props: {
            pwSpaceList: {
                type: Array,
                default: function () {
                    return []
                }
            },
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
                spaceList: []
            }
        },
        watch: {
            spaceGitems: function () {
                var pwSpaceList = this.pwSpaceList;
                for (var i = 0; i < pwSpaceList.length; i++) {
                    var pwSpace = pwSpaceList[i];
                    var doors = this.spaceGitems[pwSpace.sid];
                    if (!doors) {
                        continue;
                    }
                    Vue.set(pwSpace, 'doorsChecked', []);
                    for (var d = 0; d < doors.length; d++) {
                        if (doors[d].sel == '1') {
                            var door = doors[d];
                            pwSpace.doorsChecked.push(door.id);
                        }
                    }
                }
            }
        },
        directives: {
            treeTable: {
                inserted: function (element, binding, vnode) {
                    $(element).treeTable({expandLevel: 10});
                }
            }
        },
        methods: {
            formatTreeList: function (rootId) {
                var list = this.pwSpaceList;
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    if (item.pid === rootId) {
                        this.spaceList.push(item);
                        this.formatTreeList(item.sid);
                    }
                }
            },

            extendPwSpaceList: function () {
                var pwSpaceList = this.pwSpaceList;
                for (var i = 0; i < pwSpaceList.length; i++) {
                    var pwSpace = pwSpaceList[i];
                    var doors = this.spaceGitems[pwSpace.id];
                    if (!doors) {
                        continue;
                    }
                    Vue.set(pwSpace, 'doorsChecked', []);
                    for (var d = 0; d < doors.length; d++) {
                        if (doors[d].sel == '1') {
                            var door = doors[d];
                            pwSpace.doorsChecked.push(door.id);
                        }
                    }
                }
            },


            setPwSpaceList: function () {
                this.formatTreeList('1');
            },
            saveRule: function () {
                var gItemIds = [];
                for (var i = 0; i < this.spaceList.length; i++) {
                    var space = this.spaceList[i];
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
            this.setPwSpaceList();
        }
    })
}(jQuery, Vue);