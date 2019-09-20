/**
 * Created by Administrator on 2018/7/11.
 */

'use strict';
Vue.component('e-chart-pie', {
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
            defaultOption: {
                width: 'auto',
                title: {
                    x: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    x: 'center',
                    y: 'bottom',
                    data: []
                },
                series: [{
                    name: '',
                    type: 'pie',
                    radius: ['50%', '70%'],
                    avoidLabelOverlap: false,
                    // center: ['50%', '50%'],
                    data: [],
                    label: {
                        normal: {
                            show: false,
                            position: 'center'
                        },
                        emphasis: {
                            show: true,
                            textStyle: {
                                fontSize: '12',
                            }
                        }
                    },
                    labelLine: {
                        normal: {
                            show: false
                        }
                    },
                    // itemStyle: {
                    //     emphasis: {
                    //         shadowBlur: 10,
                    //         shadowOffsetX: 0,
                    //         shadowColor: 'rgba(0, 0, 0, 0.2)'
                    //     }
                    // }
                }]
            },
            echartInstance: '',
            isUpdate: false
        }
    },
    mounted: function () {

    }
})