/**
 * Created by Administrator on 2018/7/24.
 */


Vue.drPwSpaceListMixin = {
    data: function () {
        return {
            drPwSpaceEntries: {},
            drPwSpaceRootIds: [],
            drPwSpaceTree: [],
            // flattenPwSpaceList:[],
            drPwSpaceProps: {
                parentKey: 'pid',
                id: 'sid'
            }
        }
    },
    computed: {
      drPwSpaceListCopy: {
          get: function () {
              return [].concat(this.drPwSpaceList);
          }
      }
    },
    methods: {

        getFlattenPwSpaceList: function (expandLevel) {
            expandLevel = expandLevel || 1;
            return this.handleFlattenPwSpaceList(this.drPwSpaceTree.slice(0), expandLevel);
        },

        handleFlattenPwSpaceList: function (data, expandLevel) {
            var drPwSpaceEntries = this.drPwSpaceEntries;
            var parentKey = this.drPwSpaceProps['parentKey'];

            function flatten(data) {
                return data.reduce(function (p1, p2, index) {
                    var children = p2.children || [];
                    if (!p2.dots && drPwSpaceEntries[p2[parentKey]]) {
                        p2.dots = drPwSpaceEntries[p2[parentKey]].dots + '-' + (index + 1)
                    }
                    Vue.set(p2, 'isCollapsed', p2.dots.split('-').length > expandLevel + 1);
                    Vue.set(p2, 'isExpand', p2.dots.split('-').length <= expandLevel);
                    Vue.set(p2, 'allChecked', false);
                    Vue.set(p2, 'doorsChecked', []);
                    Vue.set(p2, 'indeterminate', false);
                    Vue.set(p2, 'doorIsIndeterminate', false);
                    Vue.set(p2, 'doorAllChecked', false);
                    // delete p2.children;
                    return p1.concat(p2, flatten(children))
                }, [])
            }

            return flatten(data);
        },

        setDrPwSpaceEntries: function (drPwSpaceList) {
            var i = 0;
            while (i < drPwSpaceList.length) {
                this.drPwSpaceEntries[drPwSpaceList[i][this.drPwSpaceProps['id']]] = drPwSpaceList[i];
                i++;
            }
        },

        setDrPwSpaceRootIds: function (drPwSpaceList) {
            var parentKey = this.drPwSpaceProps["parentKey"];
            for (var i = 0; i < drPwSpaceList.length; i++) {
                var parentId = drPwSpaceList[i][this.drPwSpaceProps['id']];
                while (parentId) {
                    var college = this.drPwSpaceEntries[this.drPwSpaceEntries[parentId][parentKey]];
                    if (!college) {
                        if (this.drPwSpaceRootIds.indexOf(parentId) === -1) {
                            this.drPwSpaceRootIds.push(parentId);
                        }
                        break;
                    }
                    parentId = this.drPwSpaceEntries[parentId][parentKey];
                }
            }
            return this.drPwSpaceRootIds;
        },


        getDrPwSpaceTreeTree: function (drPwSpaceRootIds, props, pwSpaceList) {
            var drPwSpaceEntries = this.drPwSpaceEntries;
            var drPwSpaceList = pwSpaceList.sort(function (a, b) {
                a.type = a.type || 100;
                return parseInt(a.type) - parseInt(b.type);
            });
            var drPwSpaceListChildrenObj = {};
            var drPwSpaceListArr = [];
            var c = 0;
            var parentKey = props["parentKey"];
            var id = props['id'];
            for (var l = 0; l < drPwSpaceRootIds.length; l++) {
                drPwSpaceListChildrenObj[drPwSpaceRootIds[l]] = drPwSpaceEntries[drPwSpaceRootIds[l]];
            }

            for (var i = 0; i < drPwSpaceRootIds.length; i++) {
                c = 0;
                while (c < drPwSpaceList.length) {
                    if (drPwSpaceList[c][id] !== drPwSpaceRootIds[i]) {
                        if (drPwSpaceListChildrenObj[drPwSpaceList[c][parentKey]]) {
                            if (!drPwSpaceListChildrenObj[drPwSpaceList[c][parentKey]].children) {
                                drPwSpaceListChildrenObj[drPwSpaceList[c][parentKey]].children = [];
                            }
                            if (!drPwSpaceList[c].isPushed) {
                                drPwSpaceListChildrenObj[drPwSpaceList[c][parentKey]].children.push(drPwSpaceList[c]);
                                drPwSpaceList[c].isPushed = true;
                            }
                            drPwSpaceListChildrenObj[drPwSpaceList[c][id]] = drPwSpaceList[c];
                        } else {
                            drPwSpaceListChildrenObj[drPwSpaceList[c][parentKey]] = drPwSpaceEntries[drPwSpaceList[c][parentKey]]

                        }
                    }
                    c++;
                }
                drPwSpaceListArr.push(drPwSpaceListChildrenObj[drPwSpaceRootIds[i]])
            }
            drPwSpaceListArr.forEach(function (item) {
                Vue.set(item, 'dots', '1')
            });
            return drPwSpaceListArr;
        }
    },
    created: function () {
        if(this.drPwSpaceListCopy.length > 0){
            this.setDrPwSpaceEntries(this.drPwSpaceListCopy);
            var rootIds = this.setDrPwSpaceRootIds(this.drPwSpaceListCopy);
            this.drPwSpaceTree = this.getDrPwSpaceTreeTree(rootIds, this.drPwSpaceProps, this.drPwSpaceListCopy);
        }
        // this.flattenPwSpaceList = this.getFlattenPwSpaceList(10)
    }
};