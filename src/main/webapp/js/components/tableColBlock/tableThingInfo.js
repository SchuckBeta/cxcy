'use strict';


Vue.component('table-thing-info', {
    template: '' +
    '   <div>\n' +
    '        <div>\n' +
    '            <el-tooltip :content="row.name" popper-class="white" placement="right">\n' +
    '                <span class="break-ellipsis">\n' +
    '                    <a :href="row.href" :class="{\'underline-pointer\':row.href}" style="color:#333"><span v-if="row.label">{{row.label}}：</span>{{row.name}}</a>\n' +
    '                </span>\n' +
    '            </el-tooltip>\n' +
    '        </div>\n' +
    '        <el-tooltip :content="row.officePro" popper-class="white" placement="right">\n' +
    '            <span class="break-ellipsis">{{row.officePro}}</span>\n' +
    '        </el-tooltip>\n' +
    '    </div>',
    props: {
        row: {
            type: Object,          //一个迭代的数据对象     这里 学院/专业  我没有对数据处理  字段也是随便写的一个
            default: function () {
                return {
                    label: '',
                    name: '',
                    href: '',
                    officePro: ''
                }
            }
        }
    }
});