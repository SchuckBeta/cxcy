/**
 * Created by Administrator on 2018/7/11.
 */

'use strict';


Vue.directive('echart', {
    inserted: function (element, binding, vnode) {
        vnode.context.echartInstance = echarts.init(element, 'macarons');
        vnode.context.echartInstance.setOption(binding.value.default);

        window.addEventListener("resize", function () {
            vnode.context.echartInstance.resize();
        });
    },
    componentUpdated: function (element, binding, vnode) {
        if (!binding.value.isUpdate) {
            return false;
        }
        vnode.context.isUpdate = false;
        vnode.context.echartInstance.setOption(binding.value.option);
    }
})