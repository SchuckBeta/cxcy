/**
 * Created by Administrator on 2018/7/11.
 */


'use strict';



Vue.component('e-chart-bar', {
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
                tooltip: {trigger: 'item'},
                legend: {x: 'center', y: 'bottom', data: []},
                xAxis:
                    {
                        type: 'category',
                        data: [],
                        axisLabel: {
                            interval: 0,
                            rotate: 30
                        }
                    },
                yAxis: {type: 'value'},
                series: []
            }
        }
    }
})