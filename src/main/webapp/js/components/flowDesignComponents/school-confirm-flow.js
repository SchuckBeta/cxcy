
Vue.component('school-confirm-flow', {
    template:
                '<div :style="svgCenter"><div :style="svgPaperStyle">' +
                    '<svg ref="svgPaper" version="1.1" width="100%" height="100%" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">' +
                        '<g id="g" v-html="actYwGroup.uiHtml"></g>' +
                    '</svg>' +
                '</div></div>',
    props: {
        actYwGroup:Object
    },
    data: function () {
       return {
           svgPaper: null,
           svgCenter: {
               position: 'relative',
               margin: '20px auto',
               width: 0,
               height: 0
           },
           svgPaperStyle: {
               width: 0,
               height: 0,
               left: 0,
               top: 0,
               position: 'absolute'
           }
       }
    },
    mounted: function () {
        var self = this;
        this.$nextTick(function () {
            self.svgPaper = Snap(self.$refs.svgPaper);
            var gBox = self.svgPaper.select('g[id="g"]').getBBox();
            self.svgCenter.width = gBox.width + 'px';
            self.svgCenter.height = gBox.height + 'px';
            self.svgPaperStyle.width = gBox.width + gBox.x + 'px';
            self.svgPaperStyle.height = gBox.height + gBox.y + 'px';
            self.svgPaperStyle.left = -gBox.x+ 'px';
            self.svgPaperStyle.top = -gBox.y + 'px';
        });
    }
});