


Vue.component('el-fdc-panning', {
    template: '<div class="fdc-scroller el-cascader-menu" ref="fdcScroller" @click.stop="unSelected"><slot></slot></div>',
    data: function () {
        return {

        }
    },
    name: 'ElFdcPanning',
    componentName: 'ElFdcPanning',
    methods: {
        // panning: function (event) {
        //     var startX = event.clientX;
        //     var startY = event.clientY;
        //     var fdcScroller = this.$refs.fdcScroller;
        //     var offsetX = fdcScroller.scrollLeft;
        //     var offsetY = fdcScroller.scrollTop;
        //
        //     $(document).on('mousemove.scroller', function (e) {
        //         e.stopPropagation();
        //         // e.preventDefault();
        //         var dx = e.clientX - startX;
        //         var dy = e.clientY - startY;
        //         fdcScroller.scrollLeft = offsetX + dx;
        //         fdcScroller.scrollTop = offsetY + dy;
        //     })
        //
        //     $(document).on('mouseup.scroller', function (e) {
        //         e.stopPropagation();
        //         // e.preventDefault();
        //         $(document).off('mousemove.scroller')
        //         $(document).off('mouseup.scroller')
        //     })
        // }
        unSelected: function () {
            // this.$store.dispatch('setCurFlowNode', {
            //     node: {},
            //     index: -1
            // })
            // this.$store.dispatch('changeableRectVisible', false);
        }
    }
})