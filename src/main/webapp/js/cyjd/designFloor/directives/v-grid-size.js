
Vue.directives('grid-size', {
    inserted: function (element, binding, vnode) {
        var value = element.value;
        var canvas = document.createElement('canvas');




        element.addEventListener('change', function (e) {
            value = element.value;

            binding.value({
                url: ''
            })
        })
    }
})