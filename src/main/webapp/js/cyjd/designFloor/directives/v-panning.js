var PanningEle;

Vue.directive('panning', {

    inserted: function (el, binding, vnode) {

        if (!document) {
            return false;
        }
        var scrollLeft = $(el).scrollLeft();
        var scrollTop = $(el).scrollTop();
        var startClientX, startClientY;

        $(el).on('mousedown', function (event) {
            var $eventTarget = $(event.target);
            if ($eventTarget.parents('svg').size() || $eventTarget.parents('.rd-free-transform').size()) {
                return
            }

            $(el).addClass('is-panning');
            scrollLeft = $(el).scrollLeft();
            scrollTop = $(el).scrollTop();
            startClientX = event.clientX;
            startClientY = event.clientY;
            PanningEle = el;
        });

        $(document.documentElement).on('mousemove', function (event) {
            if (PanningEle !== el) {
                return
            }
            $(el).scrollLeft(scrollLeft + event.clientX - startClientX)
            $(el).scrollTop(scrollTop + event.clientY - startClientY)
        })

        $(document.documentElement).on('mouseup', function () {
            if (PanningEle !== el) {
                return
            }
            $(el).removeClass('is-panning')
            PanningEle = null;
        })

    },
    unbind: function (el) {
        $(el).off()
    }
})