Vue.component('control-node-rect-box', {
    template: '<div v-show="moveableRectVisible" class="control-node-rect-box" :style="moveableRectStyle">\n' +
    '                    <div draggable="false" class="resize nw" data-position="top-left"\n' +
    '                         @mousedown="controlSizeNode($event)"></div>\n' +
    '                    <div draggable="false" class="resize ne" data-position="top-right"\n' +
    '                         @mousedown="controlSizeNode($event)"></div>\n' +
    '                    <div draggable="false" class="resize se" data-position="bottom-right"\n' +
    '                         @mousedown="controlSizeNode($event)"></div>\n' +
    '                    <div draggable="false" class="resize sw" data-position="bottom-left"\n' +
    '                         @mousedown="controlSizeNode($event)"></div>\n' +
    '                    <div draggable="false" class="del-top-right">\n' +
    '                        <span title="删除" class="el-icon-close" @click.stop="delNode"></span>\n' +
    '                    </div>\n' +
    '                    <div draggable="false" class="link-node">\n' +
    '                        <img title="连接节点" @mousedown.stop.prevent="connectNode($event)" src="/images/profile-line.png">\n' +
    '                    </div>\n' +
    '                </div>',
    computed: {
        moveableRectVisible: function () {
            return this.$store.state.moveableRectVisible
        },
        moveableRectStyle: function () {
            var moveableRect = this.$store.state.moveableRect;
            return {
                left: moveableRect.x + 'px',
                top: moveableRect.y + 'px',
                width: moveableRect.width + 'px',
                height: moveableRect.height + 'px'
            }
        },
        flowNode: function () {
            return this.$store.state.curFlowNode.node;
        },
        flowNodeIndex: function () {
            var flowNodeList = this.flowNodeList;
            var index = -1;
            for (var i = 0; i < flowNodeList.length; i++) {
                if (this.flowNode.id === flowNodeList[i].id) {
                    index = i;
                    break;
                }
            }
            return index;
        },
        flowNodeList: function () {
            return this.$store.state.flowNodeList;
        },
        snapSvgPaper: function () {
            return this.$store.state.snapSvgPaper
        },
        rootId: function () {
            return this.$root.rootId;
        },
        inSubProcess: function () {
            return this.flowNode.parentId !== this.rootId;
        },
        overallGroup: function () {
            return this.$store.state.snapSvgPaper.select('g.v-overall-group')
        },
        elFdcPanning: function () {
            var $parent = this.$parent;
            while ($parent) {
                if ($parent.$options.componentName !== 'ElFdcPanning') {
                    $parent = $parent.$parent;
                } else {
                    return $parent;
                }
            }
            return false;
        },
    },
    methods: {
        controlSizeNode: function ($event) {
            var snapSvgPaper = this.snapSvgPaper;
            var target = $event.target;
            var position = $(target).attr('data-position');
            var flowNode = this.flowNode;
            var elFlowNode = snapSvgPaper.select('g[model-id="' + flowNode.id + '"]');
            var vScale = elFlowNode.select('.v-scale');
            var vTitleScale = elFlowNode.select('.v-title-scale');
            var baseTitleText = elFlowNode.select('.v-rotate text');
            var baseTitleTextMatrix;
            var elMatrix = elFlowNode.matrix;
            var elGroupBBox = vScale.getBBox();
            var parentFlowNode, minX = 0, minY = 0;
            var vScaleMatrix, startScaleData, vTitleScaleMatrix, vScaleTitleText, vScaleTitleTextMatrix;
            var self = this;
            var startMatrix = {
                e: elMatrix.e,
                f: elMatrix.f
            };
            var startPos = {
                x: $event.clientX,
                y: $event.clientY
            };

            if (vScale) {
                vScaleMatrix = vScale.matrix;
                startScaleData = {
                    a: vScaleMatrix.a,
                    d: vScaleMatrix.d,
                    width: elGroupBBox.width / vScaleMatrix.a,
                    height: elGroupBBox.height / vScaleMatrix.d,
                };
                if (vTitleScale) {
                    vTitleScaleMatrix = vTitleScale.matrix;
                    vScaleTitleText = elFlowNode.select('.v-title text');
                    vScaleTitleTextMatrix = vScaleTitleText.matrix;
                }
            }

            if (this.inSubProcess) {
                parentFlowNode = snapSvgPaper.select('g[model-id="' + flowNode.parentId + '"]');
            }

            var isBaseTitleText = flowNode.nodeKey !== 'SubProcess' && flowNode.nodeKey !== 'UserTask';
            if (isBaseTitleText) {
                baseTitleTextMatrix = baseTitleText.matrix;
            }

            $(document).on('mousemove.control.size', function (moveEvent) {
                moveEvent.stopPropagation();
                var dx = moveEvent.clientX - startPos.x;
                var dy = moveEvent.clientY - startPos.y;
                var mre, mrf, width, height;
                if (position === 'top-left') {
                    mre = startMatrix.e + dx;
                    mrf = startMatrix.f + dy;
                }
                if (position === 'top-right') {
                    mre = startMatrix.e;
                    mrf = startMatrix.f + dy;
                }

                if (position === 'bottom-right') {
                    mre = startMatrix.e;
                    mrf = startMatrix.f;
                }

                if (position === 'bottom-left') {
                    mre = startMatrix.e + dx;
                    mrf = startMatrix.f;
                }

                mre = mre > 0 ? mre : 0;
                mrf = mrf > 0 ? mrf : 0;

                if (position === 'top-left') {
                    width = elGroupBBox.width - dx;
                    height = elGroupBBox.height - dy;
                }

                if (position === 'top-right') {
                    width = elGroupBBox.width + dx;
                    height = elGroupBBox.height - dy;
                }

                if (position === 'bottom-right') {
                    width = elGroupBBox.width + dx;
                    height = elGroupBBox.height + dy;
                }

                if (position === 'bottom-left') {
                    width = elGroupBBox.width - dx;
                    height = elGroupBBox.height + dy;
                }

                elMatrix.e = mre;
                elMatrix.f = mrf;
                elFlowNode.attr('transform', elMatrix.toTransformString());

                if (vScale) {
                    vScaleMatrix.a = width / startScaleData.width;
                    vScaleMatrix.d = height / startScaleData.height;
                    vScale.attr('transform', vScaleMatrix.toTransformString());
                    if (vTitleScale) {
                        vTitleScaleMatrix.a = vScaleMatrix.a;
                        vTitleScale.attr('transform', vTitleScaleMatrix.toTransformString());
                        vScaleTitleTextMatrix.e = vTitleScale.getBBox().width / 2;
                        vScaleTitleText.attr('transform', vScaleTitleTextMatrix.toTransformString())
                    }
                }

                if (isBaseTitleText) {
                    baseTitleTextMatrix.e = width / 2;
                    baseTitleTextMatrix.f = height / 2 + 5;
                    baseTitleText.attr('transform', baseTitleTextMatrix.toTransformString())
                }

                if (parentFlowNode) {
                    var rectX = mre + parentFlowNode.matrix.e;
                    var rectY = mrf + parentFlowNode.matrix.f;
                }


                self.$store.dispatch('changeMoveableRect', {
                    x: rectX,
                    y: rectY,
                    width: width,
                    height: height
                });
            });

            $(document).on('mouseup.control.size', function () {
                $(document).off('mousemove.control.size')
                $(document).off('mouseup.control.size')
            })
        },

        delNode: function () {
            gBusEvent.$emit('delNode', {
                node: this.flowNode,
                index: this.flowNodeIndex
            })
            this.$store.dispatch('removeFlowNodeList', this.flowNodeIndex);
            this.$store.dispatch('setCurFlowNode', {
                node: {},
                index: -1
            })
            this.$store.dispatch('changeableRectVisible', false);
        },


        connectNode: function ($event) {
            gBusEvent.$emit('mouseConnectNode', $event)
        }
    }
})