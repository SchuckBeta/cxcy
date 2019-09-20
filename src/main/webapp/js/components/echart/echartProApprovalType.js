/**
 * Created by Administrator on 2018/7/11.
 */


'use strict';

var EchartProApprovalComp = Vue.component('echart-pro-approval', {
    template: '<e-chart-bar :option="option"></e-chart-bar>',
    name: 'EchartProApproval',
    componentName: 'EchartProApproval',
    props: {
        option: Object
    },
});



var EchartProTypeComp = Vue.component('echart-pro-type', {
    template: '<e-chart-bar :option="option"></e-chart-bar>',
    props: {
        option: Object
    },
    name: 'EchartProType'
});

