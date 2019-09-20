/**
 * Created by Administrator on 2017/11/10.
 */


Vue.directive('auto-pic', {
    inserted: function (el, binding, vnode) {
        el.addEventListener('load', function () {
            var parentNode = $(el).parents(binding.value.parentEleCls);
            var pWidth = $(parentNode).width();
            var pHeight = $(parentNode).height();
            var pRate = pWidth / pHeight;
            var nWidth = el.naturalWidth;
            var nHeight = el.naturalHeight;
            var iRate = nWidth / nHeight;
            var direction = iRate > pRate ? 'horizontal' : 'vertical';
            var imgWidth = nWidth;
            if (direction === 'vertical') {
                if (nHeight > pHeight) {
                    imgWidth = (pHeight * iRate).toFixed(0);
                    $(el).width(imgWidth);
                }
                binding.value.ratio = nHeight > pHeight ? imgWidth / nWidth : 1;
            } else {
                binding.value.ratio = nWidth < pWidth ? 1 : $(el).width() / nWidth;
            }
            if(imgWidth < pWidth){
                $(el).parent().width(imgWidth)
            }
            vnode.context.ratio = binding.value.ratio;
        })
    }
});