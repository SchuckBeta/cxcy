+function ($, Vue) {
    var selectMultiple = Vue.component('select-multiple', {
        template: '<div class="select-multiple" :class="{selectedMultiple: selectedList.length > 0}">' +
        '<div class="smu-header">' +
        '<ul class="selected-list">' +
        '<li v-for="(item, index) in selectedList">' +
        '<span :title="item.name || item.alias">{{item.name || item.alias}}</span>' +
        '<a class="unselected" href="javascript:void(0);" @click="unSelectItem(item, index)">&times;</a></li></ul>' +
        '<input type="text" name="selectName" readonly v-model="selectName" ref="selectInput" :disabled="filterAble && !regType" :placeholder="placeholder" @focus="inputFocus" @blur="dropShow=false"><slot name="selectId"></slot> </div>' +
        '<div v-show="dropShow" class="smu-dropdown" tabindex="0" @blur="dropShow = false" ref="smuDropDown" @mousedown="dropMousedown">' +
        '<ul class="smu-list"><li v-show="!item.disabled" v-for="(item, index) in dropList"><a href="javascript: void(0);" :class="{disabled: item.disabled}" @click="selectItem(item, index, $event)">{{item.name}}-{{item.doorName}}</a></li>' +
        '<li v-show="isLess"><a href="javascript: void(0);">{{lessList}}</a></li> </ul></div></div>',
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
            },
        },
        data: function () {
            return {
                dropList: [],
                selectName: '',
                dropShow: false,
                timer: null,
                isLess: false,
                unselectedList: []
            }
        },
        watch: {
            selectName: function (value) {
                var reg;
                this.getUnselectedList();
                if (!value) {
                    this.dropList = this.unselectedList;
                    return
                }
                this.dropShow = true;
                reg = new RegExp(value, 'i');
                this.dropList = this.unselectedList.filter(function (t) {
                    return reg.test(t.name)
                })
            }
        },
        computed: {
            // isLess: function () {
            //     return this.dropList.length = (this.dropList.filter(function (item) {
            //         return item.disabled = true;
            //     })).length;
            // }
        },
        methods: {
            inputFocus: function () {
                this.getUnselectedList();
                this.dropShow = true;
            },
            getUnselectedList: function () {
                var list = this.list.slice(0);
                var selectedList = this.selectedList;
                var self = this;
                list = list.filter(function (item) {
                    if (!self.filterAble) {
                        return true;
                    }
                    return self.regType == item.sgtype;
                })

                selectedList.forEach(function (t) {
                    list.forEach(function (t2, i) {
                        // t2.disabled = t.id === t2.id;
                        if (t.id === t2.id && t.doorPort === t2.doorPort) {
                            // list.splice(i, 1)
                            t2.disabled = true;
                        }
                    })
                });
                if (list.length > this.listLength) {
                    list.length = this.listLength
                }
                this.unselectedList = list;
                this.dropList = this.unselectedList;
                this.isLess = (this.selectedList.length >= this.dropList.length);

                this.isLess = this.dropList.every(function (item) {
                    return item.disabled
                })
            },


            selectItem: function (item, index, $event) {
                var $target = $($event.target);
                if ($target.hasClass('disabled')) {
                    return false;
                }
                if (!this.isMany) {
                    this.selectedList.shift();
                }
                this.selectedList.push(item);
                this.getUnselectedList();
                this.selectName = '';
                this.$refs['selectInput'].focus();

                this.$emit('select', [this.selectedList, this.unselectedList])
            },
            dropMousedown: function ($event) {
                $event.preventDefault();
                $(this.$refs['selectInput']).focus();
            },
            unSelectItem: function (item, index) {
                this.selectedList.splice(index, 1);
                for(var i = 0; i< this.unselectedList.length;i++){
                    if(this.unselectedList[i].id === item.id && this.unselectedList[i].doorPort === item.doorPort){
                        this.unselectedList[i].disabled = false;
                        break;
                    }
                }
                this.getUnselectedList();
                this.$emit('select', [this.selectedList, this.unselectedList])
            }
        },
        beforeMount: function () {

        },
        mounted: function () {
        }
    })

}(jQuery, Vue);
