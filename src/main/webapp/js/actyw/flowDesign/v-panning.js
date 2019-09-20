+function ($) {
    var PanningEle;

    Vue.directive('panning', {

        inserted: function (element, binding, vnode) {
            if (!document) {
                return false;
            }
            var panningLeft = binding.value.panningLeft;
            var panningTop = binding.value.panningTop;
            var startClientX, startClientY, left, top, lastScrollLeft, lastScrollTop;

            // $(element).scrollLeft(panningLeft);
            // $(element).scrollTop(panningTop);

            $(element).on('mousedown', function (event) {
                var $eventTarget = $(event.target);
                if ($eventTarget.parents('svg').size() || $eventTarget.parents('.rd-free-transform').size()) {
                    return
                }

                $(element).addClass('is-panning');
                left = $(element).scrollLeft();
                top = $(element).scrollTop();
                startClientX = event.clientX;
                startClientY = event.clientY;
                PanningEle = element;
                vnode.context.rdFreeShow = false;
                vnode.context.rdHaloShow = false;
                vnode.context.resetPathColor();
            });

            $(document).on('mousemove', function (event) {
                if (PanningEle !== element) {
                    return
                }
                lastScrollLeft = (left + event.clientX - startClientX);
                lastScrollTop = top + event.clientY - startClientY;
                $(element).scrollLeft(lastScrollLeft);
                $(element).scrollTop(lastScrollTop)
            })

            $(document).on('mouseup', function () {
                if (PanningEle !== element) {
                    return
                }
                $(element).removeClass('is-panning');
                vnode.context.panningTop = $(element).scrollTop();
                vnode.context.panningLeft = $(element).scrollLeft();
                PanningEle = null;
            })

        },
        unbind: function (element) {
            $(element).off()
        }
    })
}(jQuery)