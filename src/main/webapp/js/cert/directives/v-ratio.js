/**
 * Created by Administrator on 2017/11/10.
 */


Vue.directive('ele-ratio', {
    update: function (el, binding, vnode) {
        var $parent = $(el).parents(binding.value.box);
        $parent.width($parent.width() * binding.value.ratio);
    }
});