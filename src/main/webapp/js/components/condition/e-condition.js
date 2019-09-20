/**
 * Created by Administrator on 2018/5/16.
 */

+function ($, Vue) {
    var eCondition = Vue.component('e-condition', {
        template: '<div class="condition-item clearfix" :class="[customClass]">\n' +
        '            <div class="condition-label">{{label}}</div>\n' +
        '            <div class="condition-control" :class="{\'condition-control_has-collapse\': isCollapse, opened: opened}">\n' +
        '                <div ref="collapse" class="condition-collapse">\n' +
        '                    <template v-if="!$slots.default">' +
        '                       <template v-if="type===\'checkbox\'">' +
        '                           <e-checkbox v-if="isShowAll" v-model="checkAll" class="e-checkbox-all" @change="handleCheckAll">全部 </e-checkbox>' +
        '                                <e-checkbox-group v-model="checkedIds" @old-change="oldChange"> ' +
        '                               <e-checkbox v-for="option in options" :name="name" :disabled="option.disabled" :label="option[defaultProps.value]" :key="option[defaultProps.value]" @change="handleCheckSingle">{{option[defaultProps.label]}}</e-checkbox>' +
        '                           </e-checkbox-group>' +
        '                       </template>' +
        '                       <template v-if="type===\'radio\'">' +
        '                           <e-radio v-if="isShowAll" class="e-checkbox-all" :name="name" v-model="radio" label="" @change="handleRadio">{{radioLabel}}</e-radio><e-radio-group v-model="radio">' +
        '                           <e-radio v-for="option in options" :name="name" :label="option[defaultProps.value]" :disabled="option.disabled" :key="option[defaultProps.value]" @change="handleRadioSingle">{{option[defaultProps.label]}}</e-radio></e-radio-group></template>' +
        '                   </template><slot></slot>' +
        '                </div>\n' +
        '                <a v-show="isCollapse" href="javascript: void(0);" class="btn-handle_collapse" @click.stop="opened = !opened"><i class="iconfont icon-down"></i></a>\n' +
        '            </div>\n' +
        '        </div>',
        model: {
            prop: 'conditionValue',
            event: 'change'
        },
        props: {
            conditionValue: {
                type: [String, Array]
            },
            label: String,
            customClass: String,
            name: String,
            options: {
                type: Array,
                default: function () {
                    return []
                }
            },
            type: {
                type: String,
                default: ''
            },
            isShowAll:{
                type: Boolean,
                default: true
            },
            radioLabel: {
                type: String,
                default: '全部'
            },
            defaultProps: {
                type: Object,
                default: function () {
                    return {
                        label: 'label',
                        value: 'value'
                    }
                }
            }
        },
        data: function () {
            return {
                isCollapse: false,
                opened: false,
                // checkAll: false,
                checkedIds: [],
                radio: ''
            }
        },
        computed: {
            optionsLen: {
                get: function () {
                    return this.options ? this.options.length : 0;
                }
            },
            checkAll: {
                get: function () {
                    // this.isCheckedAll();
                    return this.checkedIds.length === 0 || this.checkedIds.length === this.options.length;
                },
                set: function () {

                }
            },
            // radio: {
            //     get: function () {
            //         return this.conditionValue;
            //     },
            //     set: function () {
            //
            //     }
            // }
        },
        watch: {
            conditionValue: function (value) {
                this.isCheckedAll();
            },
            optionsLen: function () {
                this.$nextTick(function () {
                    this.isCollapse = this.getIsCollapse();
                })
            }
        },
        methods: {

            oldChange: function (value) {
                this.$emit('get-cur-value', value)
            },
            getIsCollapse: function () {
                var height, parentH;
                var collapse = this.$refs.collapse;
                if (!collapse) return false;
                height = collapse.clientHeight;
                parentH = collapse.parentNode.clientHeight;
                return height - 10 > parentH;
            },
            handleCheckAll: function () {
                var self = this;
                if (this.checkAll && this.checkedIds.length === 0) {
                    this.options.forEach(function (item) {
                        if (self.checkedIds.indexOf(item[self.defaultProps.value]) === -1) {
                            self.checkedIds.push(item[self.defaultProps.value])
                        }
                    });
                } else {
                    this.checkedIds = [];
                }
                this.$emit('change', this.checkedIds)
            },
            handleCheckSingle: function () {
                this.checkAll = this.options.length === this.checkedIds.length;
                this.$emit('change', this.checkedIds)
            },
            isCheckedAll: function () {
                if (this.type === 'checkbox') {
                    this.checkedIds = this.conditionValue;
                    this.checkAll = this.checkedIds.length === 0;
                }
                if (this.type === 'radio') {
                    this.radio = this.conditionValue;
                }
            },
            handleRadioSingle: function () {
                this.$emit('change', this.radio)
            },
            handleRadio: function () {
                this.radio = '';
                this.$emit('change', '')
            }
        },
        mounted: function () {
            this.isCollapse = this.getIsCollapse();
            this.isCheckedAll();
        }
    })
}(jQuery, Vue);