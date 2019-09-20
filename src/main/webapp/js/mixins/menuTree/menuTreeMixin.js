/**
 * Created by Administrator on 2018/7/24.
 */

'use strict';


Vue.menuTreeMixin = {
    data: function () {
        return {
            menuEntries: {},
            menuRootIds: [],
            menuTree: [],
            // flattenPwSpaceList:[],
            menuProps: {
                parentKey: 'parentId',
                id: 'id'
            }
        }
    },
    computed: {
        menuListCopy: {
            get: function () {
                return [].concat(this.menuList);
            }
        }
    },
    methods: {

        getFlattenMenuList: function (expandLevel) {
            expandLevel = expandLevel || 0;
            return this.handleFlattenMenuList(this.menuTree.slice(0), expandLevel);
        },

        handleFlattenMenuList: function (data, expandLevel) {
            var menuEntries = this.menuEntries;
            var parentKey = this.menuProps['parentKey'];

            function flatten(data) {
                return data.reduce(function (p1, p2, index) {
                    var children = p2.children || [];
                    if (!p2.dots && menuEntries[p2[parentKey]]) {
                        p2.dots = menuEntries[p2[parentKey]].dots + '-' + (index + 1)
                    }
                    Vue.set(p2, 'isCollapsed', p2.dots.split('-').length > expandLevel + 1);
                    Vue.set(p2, 'isExpand', p2.dots.split('-').length <= expandLevel);
                    // Vue.set(p2, 'allChecked', false);
                    // Vue.set(p2, 'doorsChecked', []);
                    // Vue.set(p2, 'indeterminate', false);
                    // Vue.set(p2, 'doorIsIndeterminate', false);
                    // Vue.set(p2, 'doorAllChecked', false);
                    // delete p2.children;
                    return p1.concat(p2, flatten(children))
                }, [])
            }

            return flatten(data);
        },

        handleFlattenTree: function (data, expandLevel) {
            expandLevel = expandLevel || 0;
            function flatten(data) {
                return data.reduce(function (p1, p2) {
                    var children = p2.children || [];
                    var parentIds = p2.parentIds;
                    parentIds = parentIds.replace(/\,$/, '');
                    parentIds = parentIds.split(',');
                    parentIds = parentIds.slice(1);
                    Vue.set(p2, 'dots', parentIds.join('-'))
                    Vue.set(p2, 'isCollapsed', parentIds.length > expandLevel + 1);
                    Vue.set(p2, 'isExpand', parentIds.length <= expandLevel);
                    return p1.concat(p2, flatten(children))
                }, [])
            }

            return flatten(data);
        },

        setMenuEntries: function (menuList) {
            var i = 0;
            this.menuEntries = {};
            while (i < menuList.length) {
                this.menuEntries[menuList[i][this.menuProps['id']].toString()] = menuList[i];
                i++;
            }
        },

        setMenuRootIds: function (menuList) {
            var parentKey = this.menuProps["parentKey"];
            var menuEntries = this.menuEntries;
            this.menuRootIds = [];
            for (var i = 0; i < menuList.length; i++) {
                var parentId = menuList[i][this.menuProps['id']];
                while (parentId) {
                    var college = this.menuEntries[this.menuEntries[parentId][parentKey].toString()];
                    if (!college) {
                        if (this.menuRootIds.indexOf(parentId) === -1) {
                            this.menuRootIds.push(parentId);
                        }
                        break;
                    }
                    parentId = this.menuEntries[parentId][parentKey];
                }
            }
            this.menuRootIds = this.menuRootIds.sort(function (a, b) {
                return parseInt(menuEntries[a].sort) - parseInt(menuEntries[b].sort);
            })
            return this.menuRootIds;
        },


        getMenuTreeTree: function (menuRootIds, props, menuList) {
            // var menuEntries = this.menuEntries;
            // menuList = menuList.sort(function (a, b) {
            //     var createDataA = a.createDate ? new Date(a.createDate).getTime() : 0;
            //     var createDateB = b.createDate ? new Date(b.createDate).getTime() : 0;
            //     return createDataA - createDateB;
            // });
            // var menuListChildrenObj = {};
            // var menuListArr = [];
            // var c = 0;
            // var parentKey = props["parentKey"];
            // var id = props['id'];
            // for (var l = 0; l < menuRootIds.length; l++) {
            //     menuListChildrenObj[menuRootIds[l]] = menuEntries[menuRootIds[l]];
            // }
            //
            // for (var i = 0; i < menuRootIds.length; i++) {
            //     c = 0;
            //     while (c < menuList.length) {
            //         if (menuList[c][id] !== menuRootIds[i]) {
            //             if (menuListChildrenObj[menuList[c][parentKey]]) {
            //                 if (!menuListChildrenObj[menuList[c][parentKey]].children) {
            //                     menuListChildrenObj[menuList[c][parentKey]].children = [];
            //                 }
            //                 if (!menuList[c].isPushed) {
            //                     menuListChildrenObj[menuList[c][parentKey]].children.push(menuList[c]);
            //                     menuList[c].isPushed = true;
            //                 }
            //                 menuListChildrenObj[menuList[c][id]] = menuList[c];
            //             } else {
            //                 menuListChildrenObj[menuList[c][parentKey]] = menuEntries[menuList[c][parentKey]]
            //
            //             }
            //         }
            //         c++;
            //     }
            //     menuListArr.push(menuListChildrenObj[menuRootIds[i]])
            // }
            // menuListArr.forEach(function (item) {
            //     Vue.set(item, 'dots', '1')
            // });
            var tree = [];
            var parentKey = this.menuProps['parentKey'];
            menuList = menuList.sort(function (a, b) {
                return parseInt(a.sort) - parseInt(b.sort);
            });
            var entity = this.menuEntries;
            for(var j = 0; j < menuList.length; j++){
                var menu = menuList[j];
                var id = menu.id;
                var parentId = menu[parentKey];
                if(entity[parentId]){
                    if(!entity[parentId].children){
                        entity[parentId].children = [];
                    }
                    entity[parentId].children.push(menu);
                }else {
                    tree.push(entity[id]);
                }
            }
            return tree;
        },


        getMenuTreeByRootId: function (menuRootIds, props, menuList) {
            var self = this;
            var parentKey = props["parentKey"];
            var id = props['id'];
            var rootIds = [];
            if (menuRootIds.length < 1) {
                return false;
            }
            menuRootIds.forEach(function (item) {
                if (!self.menuEntries[item].children) {
                    self.menuEntries[item].children = []
                }
                menuList.forEach(function (item2) {
                    if (item2[parentKey] === item) {
                        self.menuEntries[item].children.push(item2);
                        rootIds.push(item2[id]);
                    }
                })
                if (self.menuEntries[item].children.length < 1) {
                    delete self.menuEntries[item].children
                }
            })
            this.getMenuTreeByRootId(rootIds, props, menuList)
        }
    },
    created: function () {
        if (this.menuListCopy.length > 0) {
            this.setMenuEntries(this.menuListCopy);
            var rootIds = this.setMenuRootIds(this.menuListCopy);
            this.menuTree = this.getMenuTreeTree(rootIds, this.menuProps, this.menuListCopy);
        }
    }
};