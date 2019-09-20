
Vue.component('el-svg-move-node', {
    template: '<div class="el-svg-move-node" :style="moveNodeStyle" v-html="nodeData.nodeXml"></div>',
    data: function () {
      return {

      }
    },
    computed: {
        nodeMoveData: function () {
            return this.$store.state.nodeMoveData
        },
        nodeData: function () {
            return this.nodeMoveData.nodeData;
        },
        moveNodeStyle: function () {
            var nodeMoveData = this.nodeMoveData;
            var width = nodeMoveData.width;
            var height = nodeMoveData.height;
            return {
                left: (nodeMoveData.x + nodeMoveData.dx - width / 2) + 'px',
                top: (nodeMoveData.y + nodeMoveData.dy - height / 2) + 'px',
                width: width + 'px',
                height: height + 'px',
            }
        }
    }
})