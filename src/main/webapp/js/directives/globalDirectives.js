/**
 * Created by Administrator on 2018/8/16.
 */
'use strict';



// 注册一个全局自定义指令 `v-focus`
Vue.directive('line-feed', {
    inserted: function (el, binding) {
        var text = binding.value;
        var htmlStr = '';
        if(!text){
            return ''
        }
        text.split('\n').forEach(function (item) {
            htmlStr += '<span>'+item+'<br/></span>'
        })
        el.innerHTML = htmlStr;
    }
})