/**
 * Created by Administrator on 2018/6/4.
 */


;+function (Vue) {
    Vue.collegesMixin = {
        data: function () {
            return {
                collegeEntries: {},
                collegeRooIds: [],
                collegesTree: [],
                collegesProps: {
                    parentKey: 'parentId',
                    id: 'id'
                },
                collegesTranscript: [],
                isDefaultCollegeRootId: true
            }
        },
        methods: {

            getFlattenColleges: function (expandLevel) {
                expandLevel = expandLevel || 0;
                return this.handleFlattenColleges(this.collegesTree.slice(0), expandLevel);
            },

            handleFlattenColleges: function (data, expandLevel) {
                var collegeEntries = this.collegeEntries;
                var parentKey = this.collegesProps['parentKey'];
                function flatten(data) {
                    return data.reduce(function (p1, p2, index) {

                        var children = p2.children || [];
                        if (!p2.dots && collegeEntries[p2[parentKey]]) {
                            p2.dots = collegeEntries[p2[parentKey]].dots + '-' + (index + 1)
                        }
                        Vue.set(p2, 'isCollapsed', p2.dots.split('-').length > expandLevel + 1);
                        Vue.set(p2, 'isExpand', p2.dots.split('-').length <= expandLevel);


                        // var children = p2.children || [];
                        // var parent = collegeEntries[p2.parentId];
                        // if(!p2.dots && parent){
                        //     p2.dots = parent.dots + '-' + (index + 1)
                        // }
                        // Vue.set(p2, 'isCollapsed',  p2.dots.split('-').length > expandLevel);
                        // if(parent){
                        //     Vue.set(parent, 'isExpandTriangle',  !p2.isCollapsed);
                        // }

                        // delete p2.children;
                        return p1.concat(p2, flatten(children))
                    }, [])
                }
                return flatten(data);
            },


            getCollegesTranscript: function(){
                return JSON.parse(JSON.stringify(this.colleges));
            },

            setCollegeEntries: function (colleges) {
                var i = 0;
                while (i < colleges.length) {
                    this.collegeEntries[colleges[i].id] = colleges[i];
                    i++;
                }
            },

            setCollegeRooIds: function (colleges) {

                var parentKey = this.collegesProps["parentKey"];
                var collegeEntries = this.collegeEntries;
                this.collegeRooIds = [];
                for (var i = 0; i < colleges.length; i++) {
                    var parentId = colleges[i][this.collegesProps['id']];
                    while (parentId) {
                        var college = this.collegeEntries[this.collegeEntries[parentId][parentKey].toString()];
                        if (!college) {
                            if (this.collegeRooIds.indexOf(parentId) === -1) {
                                this.collegeRooIds.push(parentId);
                            }
                            break;
                        }
                        parentId = this.collegeEntries[parentId][parentKey];
                    }
                }
                this.collegeRooIds = this.collegeRooIds.sort(function (a, b) {
                    return parseInt(collegeEntries[a].sort) - parseInt(collegeEntries[b].sort);
                })
                return this.collegeRooIds;
            },

            getCollegesTree: function (collegeRooIds, props) {
                // var collegeEntries = this.collegeEntries;
                // var colleges = this.colleges.sort(function (a, b) {
                //     return parseInt(a.grade) - parseInt(b.grade);
                // });
                // var collegesChildrenObj = {};
                // var collegesArr = [];
                // var c = 0;
                // var parentKey = props["parentKey"];
                //
                // for(var l = 0; l < collegeRooIds.length; l++){
                //     collegesChildrenObj[collegeRooIds[l]] = collegeEntries[collegeRooIds[l]];
                // }
                //
                // for (var i = 0; i < collegeRooIds.length; i++) {
                //     c = 0;
                //     while (c < colleges.length) {
                //         if(colleges[c].id !== collegeRooIds[i]){
                //             if (collegesChildrenObj[colleges[c][parentKey]]) {
                //                 if (!collegesChildrenObj[colleges[c][parentKey]].children) {
                //                     Vue.set(collegesChildrenObj[colleges[c][parentKey]], 'children', [])
                //                 }
                //                 if(!colleges[c].isPushed){
                //                     collegesChildrenObj[colleges[c][parentKey]].children.push(colleges[c]);
                //                     colleges[c].isPushed = true;
                //                 }
                //                 collegesChildrenObj[colleges[c].id] = colleges[c];
                //             } else {
                //                 collegesChildrenObj[colleges[c][parentKey]] = collegeEntries[colleges[c][parentKey]]
                //             }
                //         }
                //         c++;
                //     }
                //     collegesArr.push(collegesChildrenObj[collegeRooIds[i]])
                // }
                // collegesArr.forEach(function (item) {
                //     Vue.set(item, 'dots', '1')
                // })
                var tree = [];
                var colleges = this.colleges.sort(function (a, b) {
                    return parseInt(a.grade) - parseInt(b.grade);
                });
                var entity = this.collegeEntries;
                for(var j = 0; j < colleges.length; j++){
                    var menu = colleges[j];
                    var id = menu.id;
                    var parentId = menu.parentId;
                    if(entity[parentId]){
                        if(!entity[parentId].children){
                            entity[parentId].children = [];
                        }
                        entity[parentId].children.push(menu);
                    }else {
                        Vue.set(entity[id], 'dots', '1');
                        tree.push(entity[id]);
                    }
                }
                return tree;
            },
            getMenuTreeByRootId: function (tRootIds, props, list) {
                var self = this;
                var parentKey = props["parentKey"];
                var id = props['id'];
                var rootIds = [];
                if(tRootIds.length < 1){
                    return false;
                }
                tRootIds.forEach(function (item) {
                    if(!self.collegeEntries[item].children){
                        self.collegeEntries[item].children = []
                    }
                    list.forEach(function (item2) {
                        if(item2[parentKey] === item){
                            self.collegeEntries[item].children.push(item2);
                            rootIds.push(item2[id]);
                        }
                    })
                    if(self.collegeEntries[item].children.length < 1){
                        delete self.collegeEntries[item].children
                    }
                })
                this.getMenuTreeByRootId(rootIds, props, list)
            }

        },

        created: function () {
            if(this.colleges && this.colleges.length > 1){
                this.setCollegeEntries(this.colleges);
                this.collegesTranscript = this.getCollegesTranscript();
                //若果是去掉学院，需要重新重新获取rootIds, 此时需要设置isDefaultCollegeRootId 未false
                var rootIds = !this.isDefaultCollegeRootId ?  this.setCollegeRooIds(this.colleges) : ['1'];
                this.collegesTree = this.getCollegesTree(rootIds, this.collegesProps);

            }

        }
    }
}(Vue)