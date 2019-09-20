


Vue.component('el-svg-task-node', {
    template: '<div class="el-svg-exp-node-wrap"><el-tooltip class="item" effect="dark" :content="tooltip[nodeData.nodeKey]" placement="right"><div class="el-svg-exp-node" ref="elSvgExpNode" v-svg-exp-node="nodeData" @mousedown.stop.prevent="handleMousedownNode"></div></el-tooltip></div> ',
    props: {
        nodeData: {
            required: true,
            type: Object,
            default: function () {
                return {}
            }
        }
    },
    data: function () {
        return {
            tooltip: {
                'StartNoneEvent': '子流程或者流程的开始' ,
                'EndTerminateEvent':'流程终止',
                'EndNoneEvent': '流程的结束',
                'UserTask': '用户任务，必须被人为触发（完成任务的动作）',
                'SubProcess': '分组用户任务',
                'ExclusiveGateway': '流程的走向'
            }
        }
    },
    computed: {
        topHeight: function () {
            return this.$store.state.topHeight;
        }
    },
    directives: {
        svgExpNode: {
            inserted: function (element, binding, vnode) {
                var nodeData = binding.value;
                var snapXml = null;
                var bbox = {};
                element.innerHTML = nodeData.nodeXml;
                snapXml = Snap(element.querySelector('svg'));
                var gGroup = snapXml.select('.v-translate');
                bbox = gGroup.getBBox();
                element.style.width = bbox.width + 'px';
                element.style.height = bbox.height + 'px';
                snapXml.select('g.v-translate').attr({
                    'model-key': nodeData.nodeKey,
                    'model-type': nodeData.type,
                })
            }
        }
    },
    methods: {
        handleMousedownNode: function (event) {
            event.stopPropagation();
            var self = this;
            var $el = $(this.$refs.elSvgExpNode);
            var startX = event.clientX;
            var startY = event.clientY;
            var topHeight = this.topHeight;
            var elWidth = $el.width();
            var elHeight = $el.height();
            var winWidth = $(window).width();
            var clone = Snap($el[0].querySelector('svg')).select('g.v-translate').clone();
            var moveData = {
                x: startX,
                y: startY - topHeight,
                dx: 0,
                dy: 0,
                width: elWidth,
                height: elHeight,
                nodeData: this.nodeData,
                nodeSvg: clone
            }
            this.$store.dispatch('changeNodeMoveData', moveData);
            this.$store.dispatch('changeVisibleSvgMove', true);
            $(document).on('mousemove.svg', function (e) {
                e.stopPropagation();
                var dx = e.clientX - startX;
                var dy = e.clientY - startY;
                // var minY = -topHeight;
                // var maxX = winWidth - 300 - 189;
                // dx = dx < maxX ? dx : maxX;
                // dy = dy > minY ? dy : minY;
                moveData['dx'] = dx;
                moveData['dy'] = dy;
                self.$store.dispatch('changeNodeMoveData', moveData);
            })
            $(document).on('mouseup.svg', function (e) {
                e.stopPropagation();
                clone.remove();
                self.$store.dispatch('changeVisibleSvgMove', false);
                gBusEvent.$emit('addSvgTaskNode', e);
                $(document).off('mousemove.svg')
                $(document).off('mouseup.svg')
            })
        }
    },
    created: function () {
        // console.log(this.nodeList)
        if(this.nodeData.nodeKey === 'StartNoneEvent'){
            this.$store.dispatch('updateStartNodeEvent', this.nodeData)
        }
    },
    destroyed: function () {

    },
    mounted: function () {
    }
})