/**
 * Created by Administrator on 2018/6/14.
 */

    'use strict';

    var eColItem = Vue.component('e-col-item', {
        template: '<div class="e-col-item" :class="[{\'is-required\': required}]">\n' +
        '        <label class="e-col-item_label" :class="[alignCls]" :style="labelWidthStyle">{{label}}</label>\n' +
        '        <div class="e-col-item_content" :style="{marginLeft: labelWidthStyle.width}">\n' +
        '            <div class="e-col-item_static">\n' +
        '                <template v-if="$slots.default"><slot></slot></template>\n' +
        '                <template v-if="!$slots.default && item && prop">{{item[prop]}}</template>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '    </div>',
        props: {
            item: {
                type: Object
            },
            label: String,
            required: Boolean,
            prop: {
                type: String
            },
            labelWidth: String,
            size: String,
            align: {
                type: String,
                default: 'right'
            }
        },
        computed: {
            labelWidthStyle: {
                get: function () {
                    var width, colRowComponent;
                    if (this.labelWidth) {
                        width = this.labelWidth;
                        return {
                            width: width
                        }
                    }
                    colRowComponent = this.getColRowComponent();
                    if (colRowComponent) {
                        width = colRowComponent.$attrs['label-width'] || ''
                    }
                    return {
                        width: width
                    }
                }
            },
            alignCls: {
                get: function () {
                    return {
                        'text-right': this.align === 'right',
                        'text-left': this.align === 'left'
                    }
                }
            }
        },
        methods: {
            getColRowComponent: function () {
                var $parent = this.$parent;
                while ($parent) {
                    if ($parent.$options.componentName !== "ElRow" && $parent.$options.componentName !== "EColCon") {
                        $parent = $parent.$parent;
                    } else {
                        return $parent;
                    }
                }
                return null;
            }
        }
    });

    var EColCon = Vue.component('e-col-con', {
        template: '<div class="e-col-content_content" style="padding: 0 15px"> <slot></slot> </div>',
        componentName: 'EColCon'
    })