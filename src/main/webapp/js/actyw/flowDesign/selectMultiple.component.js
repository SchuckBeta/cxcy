+function ($, Vue) {
    var selectMultiple = Vue.component('select-multiple', {
        template: '<div class="select-multiple" :class="{selectedMultiple: selectedList.length > 0}">' +
        '<div class="smu-header">' +
        '<ul class="selected-list">' +
        '<li v-for="(item, index) in selectedList">' +
        '<span :title="item.name || item.alias">{{item.name || item.alias}}</span>' +
        '<a class="unselected" href="javascript:void(0);" @click="unSelectItem(item, index)">&times;</a></li></ul>' +
        '<input type="text" name="selectName" v-model="selectName" ref="selectInput" :disabled="filterAble" :placeholder="placeholder" @focus="inputFocus" @blur="dropShow=false"><slot name="selectId"></slot> </div>' +
        '<div v-show="dropShow" class="smu-dropdown" tabindex="0" @blur="dropShow = false" ref="smuDropDown" @mousedown="dropMousedown">' +
        '<ul class="smu-list"><li v-for="(item, index) in dropList"><a :title="item.name || item.alias" href="javascript: void(0);" @click="selectItem(item, index, $event)">{{item.name || item.alias}}</a></li>' +
        '<li v-if="dropList.length < 1"><a href="javascript: void(0);">{{lessList}}</a></li> </ul></div></div>',
        props: {
            list: {
                type: Array,
                default: function () {
                    return []
                }
            },

            selectedList: {
                type: Array,
                default: function () {
                    return []
                }
            },
            placeholder: {
                type: String,
                default: '请选择角色'
            },
            lessList: {
                type: String,
                default: '没有更多数据了'
            },
            listLength: {
                type: Number,
                default: 10
            },
            isMany: {
                type: Boolean,
                default: true
            },
            regType: {
                type: String,
                default: ''
            },
            filterAble: {
                type: Boolean,
                default: false,
            }
        },
        data: function () {
            return {
                selectName: '',
                dropShow: false,
                timer: null,
                unselectedList: []
            }
        },
        watch: {
            selectName: function (value) {
                // var reg;
                // this.getUnselectedList();
                // if (!value) {
                //     this.dropList = this.unselectedList;
                //     return
                // }
                // this.dropShow = true;
                // reg = new RegExp(value, 'i');
                // this.dropList = this.unselectedList.filter(function (t) {
                //     return reg.test(t.name)
                // })
            }
        },
        computed: {
            selectedListIds: function () {
                return this.selectedList.map(function (item) {
                    return item.id;
                })
            },

            dropList: function () {
                if (!this.selectName && this.selectedListIds.length === 0) {
                    return this.list.slice(0, this.listLength);
                }
                var selectedListIds = this.selectedListIds;
                var reg = new RegExp(this.selectName, 'i');
                var list = this.list.filter(function (t) {
                    return reg.test(t.name) && selectedListIds.indexOf(t.id) === -1
                });
                return list.slice(0, this.listLength)
            }
        },
        methods: {
            inputFocus: function () {
                // this.getUnselectedList();
                this.dropShow = true;
            },
            getUnselectedList: function () {
                var list = this.list.slice(0);
                var selectedList = this.selectedList;
                var self = this;
                // list = list.filter(function (item) {
                //     if(!self.filterAble){
                //         return true;
                //     }
                //     return self.regType == item.sgtype;
                // })

                selectedList.forEach(function (t) {
                    list.forEach(function (t2, i) {
                        if (t.id === t2.id) {
                            list.splice(i, 1)
                        }
                    })
                });
                if (list.length > this.listLength) {
                    list.length = this.listLength
                }
                this.unselectedList = list;
            },


            selectItem: function (item, index, $event) {
                if (!this.isMany) {
                    this.selectedList.shift();
                }
                this.selectedList.push(item);
                // this.getUnselectedList();
                this.selectName = '';
                //点击后开启
                // this.$refs['selectInput'].focus();
                //点击后关闭
                this.$refs['selectInput'].blur();
                this.$emit('select', this.selectedList)
            },
            dropMousedown: function ($event) {
                $event.preventDefault();
                $(this.$refs['selectInput']).focus();
            },
            unSelectItem: function (item, index) {
                this.selectedList.splice(index, 1);
                // this.getUnselectedList();
                this.$emit('select', this.selectedList)
            }
        },
        beforeMount: function () {

        },
        mounted: function () {
        }
    })

}(jQuery, Vue);
