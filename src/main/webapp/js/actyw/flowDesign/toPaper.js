+function ($) {
    Vue.directive('to-paper', {
        inserted: function (element, binding, vnode) {
            var snap = Snap(element);

        },
        update: function (element, binding, vnode) {
            var ele = binding.value;
            console.log(element)
            // var svg = element.children();
            // var snap = Snap(svg);
            // var ele = binding.value.ele;
            // var style = binding.value.style;
            // var isShow = binding.value.isShow;
            // var group = snap.group();
            // if(isShow){
            //     group.append(ele);
            //     snap.add(group);
            // }else {
            //     snap.clear()
            // }
            // element.css(style);
        }
    })
    
}(jQuery);