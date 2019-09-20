/**
 * Created by Administrator on 2018/7/13.
 */
'use strict';



Vue.component('e-chart-base', {
    template: '<div v-echart="{default: defaultOption,  option: this.option, isUpdate: isUpdate}" style="width: 100%; height: 100%"></div>',
    props: {
        option: {
            type: Object
        }
    },
    watch: {
        option: {
            deep: true,
            handler: function () {
                this.isUpdate = true;
            }
        }
    },
    data: function () {
        return {
            isUpdate: false,
            defaultOption: {
                title: {x: 'center'},
                tooltip: {trigger: 'axis'},
                legend: {x: 'center', y: 'bottom', data: []}
            }
        }
    }
})