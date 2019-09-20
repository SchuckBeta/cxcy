Vue.component('el-svg-paper', {
    template: '<div class="el-svg-paper"  :style="svgPaperStyle"><svg ref="svgPaper" id="flowDesignSvg" version="1.1" width="100%" height="100%"  xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"></svg></div>',
    data: function () {
        return {
            // snapSvgPaper: {},
            overallGroup: null,
            isMoveEvent: false,
            globalMoveable: false,
            currentFlowNode: {},
            uuids: [],
            typeRules: {  //控制线能连接哪些节点规则
                '110': [150, 190],
                '130': [],
                '140': [130, 150, 140, 190],
                '150': [130, 150, 140, 190],
                '190': [130, 150, 140, 190],
                '210': [250, 290],
                '230': [],
                '240': [230, 250, 240, 290],
                '250': [230, 250, 240, 290],
                '290': [230, 250, 240, 290]
            },
        }
    },

    computed: {
        svgPaperStyle: function () {
            var svgPaperStyle = this.$store.state.svgPaperStyle;
            var minStyle = this.minStyle;
            var width = svgPaperStyle.width ? svgPaperStyle.width + 'px' : minStyle.width + 'px';
            var height = svgPaperStyle.height ? svgPaperStyle.height + 'px' : minStyle.height + 'px';
            return {
                width: width,
                height: height
            };
        },

        minStyle: function () {
            return {
                width: (this.$window.width() - 190 - 300),
                height: (this.$window.height() - 49)
            }
        },

        flowNodeList: function () {
            return this.$store.state.flowNodeList;
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

        snapSvgPaper: function () {
            return this.$store.state.snapSvgPaper;
        },

        flowNode: function () {
            return this.$store.state.curFlowNode.node;
        },

        flowStartNodes: function () {
            return this.flowNodeList.filter(function (item) {
                return FlowDesignConfig.startNoneEventTypes.indexOf(item.type) > -1;
            })
        },

        nodeMoveData: function () {
            return this.$store.state.nodeMoveData;
        },

        $window: function () {
            return $(window);
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

        startNodeEvent: function () {
            return this.$store.state.startNodeEvent;
        },

        rootId: function () {
            return this.$root.rootId;
        },
        sequenceFlowData: function () {
            return this.$store.state.sequenceFlowData;
        },
        gNodeTypes: function () {
            return this.$store.state.gNodeTypes;
        },

        groupId: function () {
            return this.$root.groupId;
        }

    },
    watch: {
        uuids: function (value) {
            if (value.length < 50) {
                this.getUUIds(100)
            }
        }
    },

    methods: {


        //获取所有的v-translate
        getVTranslateElements: function () {
            var overallGroup = this.overallGroup;
            return overallGroup.selectAll('g.v-translate')
        },

        //获取所有的线
        getVPathGroups: function () {
            var overallGroup = this.overallGroup;
            return overallGroup.selectAll('g.v-path-group')
        },


        //批量v-translate添加拖拽事件
        elementsAddDragEvent: function () {
            var elements = this.getVTranslateElements();
            var self = this;
            elements.forEach(function (item) {
                var modelType = parseInt(item.attr('model-type'));
                var configNodes = [].concat(FlowDesignConfig.taskNodes, FlowDesignConfig.gatewayNodes, FlowDesignConfig.processNodes);
                var isNode = configNodes.indexOf(modelType) > -1;
                if (isNode) {
                    self.addMatrix(item);
                    self.addDragEvent(item);
                    self.addElementClick(item);
                }
            })
        },

        addDragEvent: function (element) {
            var self = this;
            var startPos = {};
            var curFlowNodeData;
            var isInSubProcess;
            var rootId = this.rootId;
            var elMatrix, initPos;
            var overallGroup = this.overallGroup;
            var allElementNodeList;
            var targetBBox;
            var elSubProcess;
            var isSubProcess;
            var elNodeId;
            var allPaths, basePaths, subPaths = [];
            element.drag(function move(dx, dy, x, y, moveEvent) {
                    moveEvent.stopPropagation();
                    var target = this;
                    var mre = moveEvent.clientX - startPos.x + initPos.e;
                    var mrf = moveEvent.clientY - startPos.y + initPos.f;


                    mre = mre < 0 ? 0 : mre;
                    mrf = mrf < 0 ? 0 : mrf;
                    if (isInSubProcess) {
                        mrf = mrf > 30 ? mrf : 30;
                        var elSubProBBox = elSubProcess.select('.v-scale').getBBox();
                        var maxWidth = elSubProBBox.width - targetBBox.width;
                        var maxHeight = elSubProBBox.height - targetBBox.height;
                        mre = mre > maxWidth ? maxWidth : mre;
                        mrf = mrf > maxHeight ? maxHeight : mrf;
                    }
                    elMatrix.e = mre;
                    elMatrix.f = mrf;
                    target.attr('transform', elMatrix.toTransformString());
                    self.$store.dispatch('changeableRectVisible', false);


                    if (isInSubProcess) {
                        self.setRelatePathsPos(subPaths, elSubProcess.matrix.e, elSubProcess.matrix.f);
                    } else {
                        self.setRelatePathsPos(basePaths, 0, 0);
                        self.setRelatePathsPos(subPaths, elMatrix.e, elMatrix.f)
                    }

                    if (!isInSubProcess) {
                        self.isUpBoundary(elMatrix, targetBBox, allElementNodeList)
                    }


                }, function start(x, y, event) {
                    var target = this;
                    elNodeId = target.attr('model-id');
                    var targetParentId = target.attr('parent-id');
                    event.stopPropagation();
                    startPos = {
                        x: event.clientX,
                        y: event.clientY
                    };
                    elMatrix = target.matrix;
                    initPos = {
                        e: elMatrix.e,
                        f: elMatrix.f
                    };
                    targetBBox = target.select('.v-scale').getBBox();
                    curFlowNodeData = self.getNodeDataAndIndex(elNodeId);
                    isSubProcess = target.attr('model-key') === 'SubProcess';
                    isInSubProcess = targetParentId !== rootId; //在流程组内
                    if (isInSubProcess) {
                        elSubProcess = overallGroup.select('g[model-id="' + targetParentId + '"]');
                    }
                    allPaths = self.getNeedMoveablePaths(elNodeId, target, isSubProcess);

                    basePaths = allPaths.filter(function (item) {
                        return item.path.attr('parent-id') === rootId;
                    });
                    subPaths = allPaths.filter(function (item) {
                        return item.path.attr('parent-id') === elNodeId;
                    });
                    if (isInSubProcess) {
                        subPaths = allPaths;
                    }
                    allElementNodeList = overallGroup.selectAll('g.v-translate[parent-id="'+rootId+'"]');
                }, function up() {

                }
            )
        },

        isUpBoundary: function (matrix, BBox, elements) {
            var svgPaperStyle = this.svgPaperStyle;
            var width = svgPaperStyle.width.replace('px', '') * 1;
            var height = svgPaperStyle.height.replace('px', '') * 1;
            var elementsMaxSizeArr = this.getElementsMaxSizeArr(elements);
            var totalDisX = elementsMaxSizeArr.maxWidth;
            var totalDisY = elementsMaxSizeArr.maxHeight;
            var minStyle = this.minStyle;

            if (totalDisX > width - 100) {
                this.$store.dispatch('changeSvgPaperStyle', {
                    width: width + 400,
                })
            }
            if (totalDisY > height - 100) {
                this.$store.dispatch('changeSvgPaperStyle', {
                    height: height + 400
                })
            }

            if (width - totalDisX > 500) {
                var sWidth = width - 400;
                sWidth = sWidth < minStyle.width ? minStyle.width : sWidth;
                this.$store.dispatch('changeSvgPaperStyle', {
                    width: sWidth,
                })
            }
            if (height - totalDisY > 500) {
                var sHeight = height - 400;
                sHeight = sHeight < minStyle.height ? minStyle.height : sHeight;
                this.$store.dispatch('changeSvgPaperStyle', {
                    height: sHeight
                })
            }

        },

        getElementsMaxSizeArr: function (elements) {
            var widths = [];
            var heights = [];
            elements.forEach(function (item) {
                var vScale = item.select('.v-scale');
                var width = item.matrix.e + vScale.getBBox().width;
                var height = item.matrix.f + vScale.getBBox().height;
                widths.push(width);
                heights.push(height);
            });
            return {
                maxWidth: Math.max.apply(Math, widths),
                maxHeight: Math.max.apply(Math, heights)
            }
        },

        //获取所有可以需要移动的线条
        getNeedMoveablePaths: function (id, ele, isSubProcess) {
            var overallGroup = this.overallGroup;
            var paths = [];
            var sourcesPaths = overallGroup.selectAll('g[source-id="' + id + '"]');
            var targetPaths = overallGroup.selectAll('g[target-id="' + id + '"]');
            var vChildShapes = ele.select('.v-childShapes');
            var quc = {};
            if (sourcesPaths && sourcesPaths.length > 0) {
                sourcesPaths.forEach(function (item) {
                    var targetId = item.attr('target-id');
                    paths.push({
                        path: item,
                        source: ele,
                        target: overallGroup.select('g[model-id="' + targetId + '"]')
                    });
                    quc[item.id] = true;
                })
            }
            if (targetPaths && targetPaths.length > 0) {
                targetPaths.forEach(function (item) {
                    var sourceId = item.attr('source-id');
                    paths.push({
                        path: item,
                        source: overallGroup.select('g[model-id="' + sourceId + '"]'),
                        target: ele
                    })
                    quc[item.id] = true;
                })
            }
            if (isSubProcess) {
                var children = vChildShapes.children() || [];
                children.forEach(function (item) {
                    var nPaths = overallGroup.selectAll('g[source-id="' + item.attr('model-id') + '"]');
                    if (nPaths) {
                        nPaths.forEach(function (item2) {
                            var targetEle = overallGroup.select('g[model-id="' + item2.attr('target-id') + '"]');
                            if (targetEle) {
                                paths.push({
                                    path: item2,
                                    source: item,
                                    target: targetEle
                                })
                            }
                        })
                    }
                })
            }
            return paths;
        },


        pathsAddDragEvent: function () {
            var pathGroups = this.getVPathGroups();
            var self = this;
            pathGroups.forEach(function (item) {
                self.addMatrix(item);
                self.addPathEvent(item);
            })
        },


        getNodeData: function (id) {
            var i = 0;
            var flowNodeList = this.flowNodeList;
            while (i < flowNodeList.length) {
                var item = flowNodeList[i];
                if (item.id === id) {
                    return item;
                }
                i++;
            }
            return null;
        },

        getNodeDataAndIndex: function (id) {
            var i = 0;
            var flowNodeList = this.flowNodeList;
            while (i < flowNodeList.length) {
                var item = flowNodeList[i];
                if (item.id === id) {
                    return {
                        node: item,
                        index: i
                    };
                }
                i++;
            }
            return {};
        },

        //添加元素点击事件，取代mouse自定义点击
        addElementClick: function (element) {
            var self = this;
            element.click(function (event) {
                event.stopPropagation();
                var target = this;
                var modelKey = target.attr('model-key');
                self.clickElNodeFun(target);
                //如果是网关
                if ('ExclusiveGateway' === modelKey) {
                    var sourceType = self.getSourceNodeFilterForm(element);
                    self.$store.dispatch('setSourceNode', sourceType)
                }
            })
        },

        //获取网关筛选表单数据
        getSourceNodeFilterForm: function (element) {
            var overallGroup = this.overallGroup;
            var modelId = element.attr('model-id');
            var pathSource = overallGroup.select('g[target-id="' + modelId + '"]');
            var sourceId, sourceNode;
            if (!pathSource) return '';
            sourceId = pathSource.attr('source-id');
            if (!sourceId) return '';
            return this.getNodeData(sourceId);
        },


        //添加path event
        addPathEvent: function (path) {
            var self = this;
            path.click(function (event) {
                var target = this;
                var modelId = target.attr('model-id');
                var modelKey = target.attr('model-key');
                var sourceId = target.attr('source-id');
                var sourceData = self.getNodeData(sourceId);
                event.stopPropagation();
                self.setPathConditions(sourceData);
                self.setCurFlowNode(modelId);
                self.resetPathColor();
                self.$store.dispatch('changeableRectVisible', false);
                if(sourceData){
                    self.$store.dispatch('updateHasCondition', (sourceData.type == 140 || sourceData.type == 240));
                }
                target.select('.v-path').attr('stroke', '#39f');
                target.select('.v-triangle').attr('fill', '#39f');
            })
        },


        setCurFlowNode: function (modelId) {
            for (var i = 0; i < this.flowNodeList.length; i++) {
                if (this.flowNodeList[i].id === modelId) {
                    this.$store.dispatch('setCurFlowNode', {
                        node: this.flowNodeList[i],
                        index: i
                    });
                    break;
                }
            }
        },

        setPathConditions: function (sourceData) {
            var overallGroup, paths, existedCons, conditions, self, unexistedConditions;
            self = this;
            overallGroup = this.overallGroup;
            if (sourceData && sourceData.id) {
                paths = overallGroup.selectAll('g[source-id="' + sourceData.id + '"]');
                existedCons = [];
                unexistedConditions = [];
                conditions = sourceData.statusIds || [];
                this.$store.dispatch('setPathConditionsAll', conditions);
                paths.forEach(function (path) {
                    var obj = self.getNodeData(path.attr('model-id'));
                    existedCons = existedCons.concat((obj.statusIds || []))
                });
                existedCons = existedCons.map(function (item) {
                    return item.id;
                })
                conditions.forEach(function (item) {
                    var id = item.id;
                    if(existedCons.indexOf(id) === -1){
                        unexistedConditions.push(item);
                    }
                })
                this.$store.dispatch('setPathConditions', unexistedConditions)

            } else {
                this.$store.dispatch('setPathConditions', []);
                this.$store.dispatch('setPathConditionsAll', []);
            }
        },

        //重置线路颜色
        resetPathColor: function () {
            var overallGroup = this.overallGroup;
            var paths = overallGroup.selectAll('.v-path-group');
            paths.forEach(function (item) {
                item.select('.v-path') && item.select('.v-path').attr('stroke', '#ddcebb');
                item.select('.v-triangle') && item.select('.v-triangle').attr('fill', '#ddcebb');
            })
        },


        //移动结束后重新确定线的位置
        setRelatePathsPos: function (paths, parentGroupX, parentGroupY) {
            var self = this;
            paths.forEach(function (item) {
                var source = item.source;
                var target = item.target;
                var sourceMatrix = source.matrix;
                var targetMatrix = target.matrix;
                var sourceBBox = source.select('.v-scale').getBBox();
                var targetBBox = target.select('.v-scale').getBBox();
                var sourceBox = {
                    x: sourceMatrix.e,
                    y: sourceMatrix.f,
                    width: sourceBBox.width,
                    height: sourceBBox.height
                }
                var targetBox = {
                    x: targetMatrix.e,
                    y: targetMatrix.f,
                    width: targetBBox.width,
                    height: targetBBox.height
                }
                var position = self.getAllPosition(sourceBox, targetBox, parentGroupX, parentGroupY);
                var quadrant = self.quadrant(position);
                var dLine = self.generateLinkPos(position, quadrant);
                self.changePath(item.path.select('.v-path'), dLine);
                self.changePath(item.path.select('.v-path-wrap'), dLine);
                self.changePathTriangle(item.path.select('.v-triangle'), dLine, quadrant.tAngle)
                self.changePathText(item.path, item.path.select('text'), quadrant)
            });
        },


        //判断象限
        quadrant: function (p) {
            if (p[7].x < p[0].x && p[7].y < p[0].y) {
                return {
                    d: 'topCenter-leftCenter',
                    tAngle: 0
                };
            } else if (p[7].x >= p[0].x && p[6].x < p[3].x && p[7].y < p[0].y) {
                return {
                    d: 'topCenter-bottomCenter',
                    tAngle: 90
                };
            } else if (p[6].x >= p[3].x && p[5].y < p[0].y) {
                return {
                    d: 'topCenter-rightCenter',
                    tAngle: 180
                };
            } else if (p[5].y >= p[0].y && p[3].x < p[6].x && p[4].y < p[1].y) {
                return {
                    d: 'rightCenter-leftCenter',
                    tAngle: 180
                };
            } else if (p[4].y >= p[1].y && p[3].x < p[6].x) {
                return {
                    d: 'rightCenter-topCenter',
                    tAngle: 270
                };
            } else if (p[4].y > p[1].y && p[7].x >= p[2].x && p[6].x < p[3].x) {
                return {
                    d: 'bottomCenter-topCenter',
                    tAngle: 270
                };
            } else if (p[4].y > p[1].y && p[7].x < p[2].x) {
                return {
                    d: 'leftCenter-topCenter',
                    tAngle: 270
                };
            } else {
                return {
                    d: 'leftCenter-rightCenter',
                    tAngle: 0
                };
            }
        },

        //生成连接线的坐标
        generateLinkPos: function (p, direction) {
            if (direction.d === 'bottomCenter-topCenter') {
                return ['M', p[1].x, p[1].y, p[1].x, p[1].y + (p[4].y - p[1].y) / 2, p[4].x, p[1].y + (p[4].y - p[1].y) / 2, p[4].x, p[4].y]
            } else if (direction.d === 'rightCenter-leftCenter') {
                return ['M', p[3].x, p[3].y, p[3].x + (p[6].x - p[3].x) / 2, p[3].y, p[3].x + (p[6].x - p[3].x) / 2, p[6].y, p[6].x, p[6].y]
            } else if (direction.d === 'leftCenter-rightCenter') {
                return ['M', p[2].x, p[2].y, p[2].x - (p[2].x - p[7].x) / 2, p[2].y, p[2].x - (p[2].x - p[7].x) / 2, p[7].y, p[7].x, p[7].y]
            } else if (direction.d === 'topCenter-bottomCenter') {
                return ['M', p[0].x, p[0].y, p[0].x, p[0].y - (p[0].y - p[5].y) / 2, p[5].x, p[0].y - (p[0].y - p[5].y) / 2, p[5].x, p[5].y]
            } else if (direction.d === 'rightCenter-topCenter') {
                return ['M', p[3].x, p[3].y, p[5].x, p[3].y, p[4].x, p[4].y]
            } else if (direction.d === 'topCenter-leftCenter') {
                return ['M', p[0].x, p[0].y, p[0].x, p[7].y, p[7].x, p[7].y]
            } else if (direction.d === 'leftCenter-topCenter') {
                return ['M', p[2].x, p[2].y, p[4].x, p[2].y, p[4].x, p[4].y]
            } else if (direction.d === 'topCenter-rightCenter') {
                return ['M', p[0].x, p[0].y, p[0].x, p[6].y, p[6].x, p[6].y]
            }
        },

        //所有方向的坐标
        getAllPosition: function (BBox, targetBBox, parentX, parentY) {
            return [{
                x: BBox.x + parentX + BBox.width / 2, y: BBox.y + parentY  //中心点source topCenter
            }, {
                x: BBox.x + parentX + BBox.width / 2, y: BBox.y + parentY + BBox.height //bottomCenter
            }, {
                x: BBox.x + parentX, y: BBox.y + parentY + BBox.height / 2  //leftCenter
            }, {
                x: BBox.x + parentX + BBox.width, y: BBox.y + parentY + BBox.height / 2 //rightCenter
            }, {
                x: targetBBox.x + parentX + targetBBox.width / 2, y: targetBBox.y + parentY
            }, {
                x: targetBBox.x + parentX + targetBBox.width / 2, y: targetBBox.y + parentY + targetBBox.height
            }, {
                x: targetBBox.x + parentX, y: targetBBox.y + parentY + targetBBox.height / 2
            }, {
                x: targetBBox.x + parentX + targetBBox.width, y: targetBBox.y + parentY + targetBBox.height / 2
            }]
        },

        //改变线的坐标
        changePath: function (ele, dLine) {
            ele.attr('d', dLine.join(' '))
        },

        //改变箭头位置
        changePathTriangle: function (ele, dLine, tAngle) {
            var pathTargetX, pathTargetY, pathTargetMatrix;
            if (tAngle === 270) {
                pathTargetX = (dLine[dLine.length - 2] * 1 - 5);
                pathTargetY = (dLine[dLine.length - 1] * 1 - 9);
            } else if (tAngle === 180) {
                pathTargetX = (dLine[dLine.length - 2] * 1 - 9);
                pathTargetY = (dLine[dLine.length - 1] * 1 - 5)
            } else if (tAngle === 0) {
                pathTargetX = (dLine[dLine.length - 2]);
                pathTargetY = (dLine[dLine.length - 1] * 1 - 5)
            } else if (tAngle === 90) {
                pathTargetX = (dLine[dLine.length - 2] * 1 - 5);
                pathTargetY = (dLine[dLine.length - 1] * 1 - 9)
            }
            ele.attr({
                'transform': 'matrix(1,0,0,1,' + pathTargetX + ',' + pathTargetY + ')'
            })
            pathTargetMatrix = ele.matrix;
            pathTargetMatrix.rotate(tAngle, 5, 5);
            ele.attr('transform', pathTargetMatrix.toTransformString());
        },

        //改变文字位置
        changePathText: function (pathGroup, textElement, quadrant) {
            if (!textElement) return;
            var vPath = pathGroup.select('.v-path');
            var BBox = vPath.getBBox();
            var pointAtLength = vPath.getPointAtLength();
            var translateX = pointAtLength.x;
            var translateY = pointAtLength.y;
            var leftSide = ['leftCenter-rightCenter', 'topCenter-leftCenter'];
            if (leftSide.indexOf(quadrant.d) > -1) {
                translateX = pointAtLength.x + textElement.getBBox().width + 10;
                translateY = pointAtLength.y + textElement.getBBox().height - 10;
            } else {
                translateX = pointAtLength.x - textElement.getBBox().width - 10;
                translateY = pointAtLength.y - textElement.getBBox().height + 10;
            }

            textElement.attr({
                'transform': 'matrix(1,0,0,1,' + translateX + ',' + translateY + ')'
            })
        },

        //判断添加matrix
        addMatrix: function (ele) {
            var elMatrix = {};
            if (!ele) return null;
            if (!ele.matrix) {
                try {
                    ele.attr('transform', ele.attr('transform'));
                } catch (e) {
                    elMatrix = ele.attr('transform');
                    if (elMatrix.localMatrix) {
                        ele.attr('transform', elMatrix.localMatrix.toTransformString());
                    }
                }
            }
            return ele;
        },

        getNodeType: function (type, parentId) {
            var level = parentId === this.rootId ? '1' : '2';
            var i = 0;
            var gNodeTypes = this.gNodeTypes;
            while (i < gNodeTypes.length) {
                var item = gNodeTypes[i];
                if (item.ntype.id === type && level === item.level) {
                    return item.id;
                }
                i++;
            }
            return ''
        },

        //添加到flowNodeList并添加事件
        addFlowNodeList: function (data, isCurFlowNode, isNode) {
            var flowNodeList = this.flowNodeList;
            var nodeData = data.data.nodeData;
            var overallGroup = this.overallGroup;
            var type = this.getNodeType(nodeData.type, data.parentId);
            var nData = {
                id: data.uuid,
                parentId: data.parentId,
                nodeKey: nodeData.nodeKey,
                nodeId: nodeData.id,
                type: type,
                typeName: nodeData.name,
                name: nodeData.name,
                level: -1
            }
            if (nodeData.nodeKey === 'SequenceFlow') {
                nData.preFunId = data.sourceId;
                nData.preIds = [{resourceId: data.sourceId}];
            }
            flowNodeList.push(nData)
            this.$store.dispatch('setFlowNodeList', flowNodeList);
            // console.log(isCurFlowNode)
            // if (isCurFlowNode) {
            //     this.$store.dispatch('setCurFlowNode', {
            //         node: flowNodeList[flowNodeList.length - 1],
            //         index: flowNodeList.length - 1
            //     })
            // }
            var elNode = overallGroup.select('g[model-id="' + data.uuid + '"]');
            if (isNode) {
                this.addElementClick(elNode);
                this.clickElNodeFun(elNode);
                this.addDragEvent(elNode)
            }

        },

        //生成拉的时候的链接线 并返回path 对象
        generateConnectingPath: function () {
            var snapSvgPaper = this.snapSvgPaper;
            var pathGroup = snapSvgPaper.group().addClass('v-path-group');
            var path = snapSvgPaper.path();
            var overallGroup = this.overallGroup;
            path.attr({
                'stroke': '#ddcebb',
                'strokeWidth': '2',
                'fill': 'none'
            })
            pathGroup.add(path);
            overallGroup.add(pathGroup);
            return {
                path: path,
                pathGroup: pathGroup
            };
        },

        //获取endCenterPoint 返回数组
        getMoveCenterPoint: function (x, y, dx, dy, BBox, parentElementPos, isSubProcess) {
            var stencilWidth = 190; //左侧宽度
            var elFdcPanning = this.elFdcPanning;
            var scrollLeft = $(elFdcPanning.$el).scrollLeft();
            var scrollTop = $(elFdcPanning.$el).scrollTop();
            var headerTop = 49; //顶部高度
            var pointX = x - stencilWidth + scrollLeft;
            var pointY = y - headerTop + scrollTop;
            // if (isSubProcess) {
            //     pointX = (BBox.x + BBox.width) + dx + parentElementPos.x;
            //     pointY = dy + (BBox.y + BBox.height - headerTop) + parentElementPos.y;
            // }
            return [pointX, pointY]
        },

        //获取移动时候的角度
        getConnectingPathAngle: function (startCenterPoint, endCenterPoint) {
            return Snap.angle(startCenterPoint[0], startCenterPoint[1], endCenterPoint[0], endCenterPoint[1]);
        },

        //移动时候找到坐标
        moveLinkPos: function (element, angle, BBox, parentX, parentY) {
            var pos = {};
            if (angle > 215 && angle < 305) {
                pos.x = BBox.x + BBox.width / 2 + parentX;
                pos.y = BBox.y + BBox.height + parentY;
            } else if (angle > 45 && angle <= 135) {
                pos.x = BBox.x + BBox.width / 2 + parentX;
                pos.y = BBox.y + parentY;
            } else if (angle > 125 && angle < 215) {
                pos.x = BBox.x + BBox.width + parentX;
                pos.y = BBox.y + BBox.height / 2 + parentY;
            } else {
                pos.x = BBox.x + parentX + parentX;
                pos.y = BBox.y + BBox.height / 2 + parentY;
            }
            return [pos.x, pos.y];

        },

        //判断是否可以连接的对象, 并返回该对象
        isConnectNode: function (target, sourceId) {
            var parent = target;
            var node = null;
            var overallGroup = this.overallGroup;
            var modelId;
            while (parent){
                var className = parent.getAttribute('class');
                var nodeName = parent.nodeName;
                if(className && className.indexOf('v-translate') > -1){
                    node = parent;
                    modelId = node.getAttribute('model-id');
                    if(modelId === sourceId){
                        node = null;
                    }else {
                        node = overallGroup.select('g[model-id="'+modelId+'"]');
                    }
                    break;
                }
                if(nodeName === 'BODY'){
                    break;
                }
                parent = parent.parentNode;
            }
            return node;
        },

        //是否可连接线 并返回目标ID， 和原元素的group
        addableConnect: function (target, $startTarget, endPoint, sourceId) {
            var sourceTypeRules, targetType;
            var translateEle, targetId, startNodeData, endNodeData;
            var res = {
                connectAble: false,
                element: null,
                targetId: ''
            };
            translateEle = this.isConnectNode(target, sourceId);
            //过滤所有的非节点元素
            if (!translateEle) {
                return res;
            }
            targetId = translateEle.attr('model-id');
            startNodeData = this.getNodeData(sourceId);
            endNodeData = this.getNodeData(targetId);
            //不可以跨节点组连线
            if(startNodeData.parentId !== endNodeData.parentId){
                return res
            }
            if (this.hasConnectedPath(sourceId, targetId)) {
                return res;
            }
            sourceTypeRules = this.typeRules[startNodeData.type];
            targetType = endNodeData.type;
            res.connectAble = new RegExp(targetType).test(sourceTypeRules.join(','));
            res.element = translateEle;
            res.targetId = targetId;
            return res;
        },

        //判断是否已经连接了一根线
        hasConnectedPath: function (sourceId, targetId) {
            var overallGroup = this.overallGroup;
            return !!overallGroup.select('g[source-id="' + sourceId + '"][target-id="' + targetId + '"]');
        },

        //点击事件
        clickElNodeFun: function (element) {
            var target = element;
            var overallGroup = this.overallGroup;
            // document.getElementById('tempFocus').focus();
            this.resetPathColor();
            overallGroup.selectAll('.v-translate').forEach(function (item) {
                item.removeClass('el-selected')
            });
            target.addClass('el-selected');

            var targetMatrix = target.matrix;
            var left = targetMatrix.e;
            var top = targetMatrix.f;

            this.$store.dispatch('changeableRectVisible', true);
            if (target.attr('parent-id') !== this.rootId) {
                //流程组
                var subProcessParent = overallGroup.select('g[model-id="' + target.attr('parent-id') + '"]');
                this.addMatrix(subProcessParent);
                var subProcessParentMatrix = subProcessParent.matrix;
                left += subProcessParentMatrix.e;
                top += subProcessParentMatrix.f;
            }
            var vRotateBBox = target.select('.v-rotate').getBBox();
            var vScale = target.select('.v-scale');
            var vTitleScale = target.select('.v-title-scale');
            var vTitleText = target.select('.v-title text');
            var baseTitleText = target.select('.v-rotate text');
            this.addMatrix(vScale);
            this.addMatrix(vTitleScale);
            this.addMatrix(vTitleText);
            this.addMatrix(baseTitleText);
            this.setCurFlowNode(target.attr('model-id'));
            this.$store.dispatch('changeMoveableRect', {
                x: left,
                y: top + 5,
                width: vRotateBBox.width,
                height: vRotateBBox.height
            });
        },

        //计算放到画布坐标
        calculateCoordinate: function () {
            var nodeMoveData = this.nodeMoveData;
            var elFdcPanning = this.elFdcPanning;
            var panningLeft = $(elFdcPanning.$el).scrollLeft();
            var panningTop = $(elFdcPanning.$el).scrollTop();
            var width = nodeMoveData.width;
            var height = nodeMoveData.height;
            var left = (nodeMoveData.x + nodeMoveData.dx - width / 2);
            var top = (nodeMoveData.y + nodeMoveData.dy - height / 2);
            return {
                left: panningLeft + left - 190, //190左侧边距
                top: panningTop + top
            }
        },

        //判断添加的是到流程组
        hasAddToSubProcess: function (target) {
            while (target) {
                var nodeName = target.nodeName;
                if (nodeName === 'body') {
                    return false;
                }
                if (target.nodeName === 'g') {
                    if (target.getAttribute('class').indexOf('v-translate') > -1 && target.getAttribute("model-key") === 'SubProcess') {
                        return target;
                    }
                }
                target = target.parentNode;
            }
        },

        //添加不在流程组的节点
        addOutSubProcessNode: function (nodeMoveData) {
            var overallGroup = this.overallGroup;
            var pos = this.calculateCoordinate();
            var uuid = this.uuids.splice(0, 1)[0];
            var nodeFragment = nodeMoveData.nodeSvg;
            overallGroup.append(nodeFragment);
            nodeFragment.attr('model-id', uuid);
            var elNode = overallGroup.select('g[model-id="' + uuid + '"]');
            this.addMatrix(elNode);
            var matrix = elNode.matrix;
            matrix.e = pos.left;
            matrix.f = pos.top;
            //处理是流程组的情况
            if (nodeMoveData.nodeData.nodeKey === 'SubProcess') {
                var vSubScale = elNode.select('.v-scale');
                var vSubTitleScale = elNode.select('.v-title-scale');
                var text = elNode.select('.v-title text');
                var snapSvgPaper = this.snapSvgPaper;
                var subBBox;
                this.addMatrix(vSubTitleScale);
                this.addMatrix(vSubScale);
                this.addMatrix(text);
                var scaleMatrix = vSubScale.matrix;
                var scaleTitleMatrix = vSubTitleScale.matrix;
                var textMatrix = text.matrix;
                scaleMatrix.a = 1.5;
                scaleMatrix.d = 1.5;
                scaleTitleMatrix.a = 1.5;
                vSubScale.attr('transform', scaleMatrix.toTransformString());
                vSubTitleScale.attr('transform', scaleTitleMatrix.toTransformString());
                subBBox = vSubScale.getBBox();
                textMatrix.e = subBBox.width / 2;
                text.attr('transform', textMatrix.toTransformString());
                var vChildShapes = snapSvgPaper.group();
                vChildShapes.addClass('v-childShapes');
                elNode.append(vChildShapes);

            }
            elNode.attr('parent-id', this.rootId);
            elNode.attr('transform', matrix.toTransformString());
            this.addFlowNodeList({
                data: nodeMoveData,
                uuid: uuid,
                parentId: this.rootId
            }, true, true)
            if (nodeMoveData.nodeData.nodeKey === 'SubProcess') {
                //默认添加一个开始节点
                this.subAddStartNode(elNode);
            }
        },

        addInSubProcessNode: function (nodeMoveData, subProcessNode) {
            var pos = this.calculateCoordinate();
            var uuid = this.uuids.splice(0, 1)[0];
            var nodeFragment = nodeMoveData.nodeSvg;
            var vChildShapes = subProcessNode.select('.v-childShapes');
            var subMatrix = subProcessNode.matrix;
            var addNode;
            nodeFragment.attr('model-id', uuid);
            vChildShapes.append(nodeFragment);
            addNode = subProcessNode.select('g[model-id="' + uuid + '"]');
            this.addMatrix(addNode);
            var addNodeMatrix = addNode.matrix;
            var parentId = subProcessNode.attr('model-id');
            addNodeMatrix.e = pos.left - subMatrix.e;
            addNodeMatrix.f = pos.top - subMatrix.f;
            addNode.attr('transform', addNodeMatrix.toTransformString());
            addNode.attr('parent-id', parentId);
            this.addFlowNodeList({
                data: nodeMoveData,
                uuid: uuid,
                parentId: parentId
            }, true, true)
        },

        subAddStartNode: function (subNode) {
            var startNodeEvent = this.startNodeEvent;
            var fragment = new Snap.fragment(startNodeEvent.nodeXml);
            var vTranslate = fragment.select('g.v-translate');
            var vChildShapes = subNode.select('.v-childShapes');
            var vSubScale = subNode.select('.v-scale');
            var vSubTitleScale = subNode.select('.v-title-scale');
            var vSubScaleBBox = vSubScale.getBBox();
            var vSubTitleScaleBBox = vSubTitleScale.getBBox();
            var uuid = this.uuids.splice(0, 1)[0];
            vTranslate.attr('model-id', uuid);
            vChildShapes.append(vTranslate);
            var startNode = vChildShapes.select('g[model-id="' + uuid + '"]');
            this.addMatrix(startNode);
            var startNodeMatrix = startNode.matrix;
            startNodeMatrix.e = (vSubScaleBBox.width - startNode.getBBox().width) / 2;
            startNodeMatrix.f = vSubTitleScaleBBox.height + 10;
            startNode.attr('transform', startNodeMatrix.toTransformString());
            startNode.attr('parent-id', subNode.attr('model-id'));
            this.addFlowNodeList({
                data: {
                    nodeData: startNodeEvent
                },
                uuid: uuid,
                parentId: subNode.attr('model-id')
            }, false, true)
        },

        //生成线group
        generatePath: function (dLine, parentGroup, id, sourceId, targetId, parentId, tAngle, key) {
            var snapSvgPaper = this.snapSvgPaper;
            var group = snapSvgPaper.group();
            var path = snapSvgPaper.path();
            var pathTarget = snapSvgPaper.path();
            var targetNodeData = this.getNodeDataAndIndex(targetId)
            var targetData = targetNodeData.node;
            var pathWrapper = snapSvgPaper.path();
            group.addClass('v-path-group');
            this.changePath(path, dLine)
            path.attr({
                'strokeWidth': 2,
                'stroke': '#ddcebb',
                'fill': 'none'
            }).addClass('v-path');
            this.changePath(pathWrapper, dLine);
            pathWrapper.attr({
                'strokeWidth': 10,
                'stroke': 'transparent',
                'fill': 'none'
            });
            pathWrapper.addClass('v-path-wrap');
            pathTarget.attr({
                'd': 'M 10 0 L 0 5 L 10 10 z',
                'stroke': 'transparent',
                'fill': '#ddcebb',
            }).addClass('v-triangle');
            this.changePathTriangle(pathTarget, dLine, tAngle)
            group.attr({
                'model-id': id,
                'target-id': targetId,
                'source-id': sourceId,
                'parent-id': parentId,
                'model-key': key
            });
            // targetData.preIds = [];
            // targetData.preIds.push({
            //     resourceId: id
            // });
            // targetData.preFunId = sourceId;
            this.$store.dispatch('updateOtherFlowNode', {
                node: targetData,
                index: targetNodeData.index
            })
            group.add(path);
            group.add(pathTarget);
            group.add(pathWrapper);
            parentGroup.append(group);
            // if (targetData.nodeKey === 'EndNoneEvent') {
            //     var vPathGroups = parentGroup.selectAll('.v-path-group');
            //     if (vPathGroups.length > 0) {
            //         group.insertBefore(vPathGroups[0])
            //     } else {
            //         parentGroup.append(group);
            //     }
            //
            // } else {
            //     parentGroup.append(group);
            // }

            this.addPathEvent(group);
            // this.pathMousedown(group)

        },

        delFlowNode: function (flowNodeList, id) {
            for(var i = 0; i <flowNodeList.length;i++){
                if(flowNodeList[i].id === id){
                    flowNodeList.splice(i, 1);
                    break;
                }
            }
            return flowNodeList;
        },

        //global bus event 改变线上的条件
        listenChangePathStatus: function () {
            var self = this;
            var $window = this.$window;
            //左侧添加节点
            gBusEvent.$on('addSvgTaskNode', function (e) {
                var nodeMoveData = self.nodeMoveData;
                var toTarget = e.target;
                var width = nodeMoveData.width;
                var height = nodeMoveData.height;
                var left = (nodeMoveData.x + nodeMoveData.dx - width / 2);
                var top = (nodeMoveData.y + nodeMoveData.dy - height / 2);
                var overallGroup = self.overallGroup;
                var subProcessNode;

                if (left < 189 || top < 0 || left > $window.width() - 300) {
                    console.info('超出区域');
                    return false;
                }
                subProcessNode = self.hasAddToSubProcess(toTarget);
                if (subProcessNode) {
                    if (nodeMoveData.nodeData.nodeKey !== 'SubProcess') {
                        var subProcessId = subProcessNode.getAttribute('model-id');
                        subProcessNode = overallGroup.select('g[model-id="' + subProcessId + '"]');
                        self.addInSubProcessNode(nodeMoveData, subProcessNode);
                    } else {
                        self.$message({
                            type: 'error',
                            message: '流程组内不可添加流程组'
                        })
                    }
                } else {
                    self.addOutSubProcessNode(nodeMoveData);
                }


            });

            //删除节点
            gBusEvent.$on('delNode', function (data) {
                var overallGroup = self.overallGroup;
                var node = data.node;
                var id = node.id;
                var asSourcePaths = overallGroup.selectAll('g[source-id="' + id + '"]');
                var asTargetPaths = overallGroup.selectAll('g[target-id="' + id + '"]');
                var flowNodeList = JSON.parse(JSON.stringify(self.flowNodeList));
                if (asSourcePaths && asSourcePaths.length > 0) {
                    asSourcePaths.forEach(function (item) {
                        var id = item.attr('model-id');
                        // var nodeData = self.getNodeDataAndIndex(id);
                        // self.$store.dispatch('removeFlowNodeList', nodeData.index);
                        item.unclick().remove();
                        flowNodeList = self.delFlowNode(flowNodeList, id);

                    })
                }
                if (asTargetPaths && asTargetPaths.length > 0) {
                    asTargetPaths.forEach(function (item) {
                        var id = item.attr('model-id');
                        // self.$store.dispatch('removeFlowNodeList', nodeData.index);
                        item.unclick().remove();
                        flowNodeList = self.delFlowNode(flowNodeList, id);
                    })
                }
                if (node.nodeKey === 'SubProcess') {
                    overallGroup.selectAll('g[parent-id="' + id + '"]').forEach(function (item) {
                        item.unclick().remove();
                        flowNodeList = self.delFlowNode(flowNodeList, item.attr('model-id'));
                    });
                }
                overallGroup.select('g[model-id="' + id + '"]').remove();
                self.$store.dispatch('setFlowNodeList', flowNodeList);
            });


            gBusEvent.$on('mouseConnectNode', function ($event) { //连接线
                var startPos = {
                    x: $event.clientX,
                    y: $event.clientY
                };
                var parentX = 0;
                var parentY = 0;
                var elSubProcess;
                var overallGroup = self.overallGroup;
                var flowNode = self.flowNode;
                var rootId = self.rootId;
                var isInSubProcess = rootId !== flowNode.parentId;
                var elNode = overallGroup.select('g[model-id="' + flowNode.id + '"]');
                var BBox = elNode.select('.v-scale').getBBox();
                var elMatrix = elNode.matrix;
                var startCenterPoint;
                BBox = {
                    x: elMatrix.e,
                    y: elMatrix.f,
                    width: BBox.width,
                    height: BBox.height
                }
                startCenterPoint = [BBox.x + BBox.width / 2, BBox.y + BBox.height / 2]; //中心点
                if (isInSubProcess) {
                    elSubProcess = overallGroup.select('g[model-id="' + flowNode.parentId + '"]');
                    parentX = elSubProcess.matrix.e;
                    parentY = elSubProcess.matrix.f;
                    startCenterPoint = [BBox.x + parentX + BBox.width / 2, BBox.y + parentY + BBox.height / 2]; //中心点
                }

                var connectingPath = self.generateConnectingPath();
                var path = connectingPath.path;
                var pathGroup = connectingPath.pathGroup;
                var moveCenterPoint;



                $(document).on('mousemove.connect', function (moveEvent) {
                    moveEvent.stopPropagation();
                    var moveX = moveEvent.clientX;
                    var moveY = moveEvent.clientY;
                    var dx = moveX - startPos.x;
                    var dy = moveY - startPos.y;
                    var moveAngle;
                    var startPoint, endPoint;
                    moveCenterPoint = self.getMoveCenterPoint(moveX, moveY, dx, dy, BBox, {
                        x: parentX,
                        y: parentY
                    }, isInSubProcess);
                    moveAngle = self.getConnectingPathAngle(startCenterPoint, moveCenterPoint);
                    startPoint = self.moveLinkPos(path, moveAngle, BBox, parentX, parentY);
                    endPoint = [moveCenterPoint[0] - 5, moveCenterPoint[1] - 15];
                    path.attr('d', 'M ' + startPoint.join(' ') + ' ' + endPoint.join(' '));
                })

                $(document).on('mouseup.connect', function (upEvent) {
                    $(document).off('mousemove.connect')
                    $(document).off('mouseup.connect')
                    pathGroup.remove();
                    var $target = upEvent.target;
                    var connectAbleRes = self.addableConnect($target, $event.target, moveCenterPoint, flowNode.id);
                    if (!connectAbleRes.connectAble) {
                        return false;
                    }
                    var translateEle = connectAbleRes.element;
                    var targetId = connectAbleRes.targetId;
                    var targetBBox = translateEle.select('.v-scale').getBBox();
                    var targetMatrix = translateEle.matrix;
                    var position = self.getAllPosition(BBox, {
                        x: targetMatrix.e,
                        y: targetMatrix.f,
                        width: targetBBox.width,
                        height: targetBBox.height
                    }, parentX, parentY);
                    var direction = self.quadrant(position);
                    var generatePos = self.generateLinkPos(position, direction);
                    var uuid = self.uuids.splice(0, 1)[0];

                    self.generatePath(generatePos, overallGroup, uuid, flowNode.id, targetId, flowNode.parentId, direction.tAngle, 'SequenceFlow');
                    self.addFlowNodeList({
                        data: {
                            nodeData: self.sequenceFlowData
                        },
                        uuid: uuid,
                        parentId: flowNode.parentId,
                        sourceId: flowNode.id
                    }, true, false)

                })
            });


            //改变顶部文字
            gBusEvent.$on('handleChangeName', function (flowNode) {
                var overallGroup = self.overallGroup;
                var snapSvgPaper = self.snapSvgPaper;
                var flowNodeElement = overallGroup.select('g[model-id="' + flowNode.id + '"]');
                var vTitle = flowNodeElement.select('.v-title');
                var elementName = flowNodeElement.select('.v-rotate text') || flowNodeElement.select('.v-scale text');
                var elementAttr = elementName.attr();
                var textElement = snapSvgPaper.text(0, 0, flowNode.name);
                textElement.attr(elementAttr);
                elementName.remove();
                vTitle.append(textElement);
            });

            //改变路径的文字
            gBusEvent.$on('handleChangePathStatus', function (list) {
                var overallGroup = self.overallGroup;
                var flowNode = self.flowNode;
                var pathGroup = overallGroup.select('g[model-id="' +flowNode.id + '"]');
                var snapSvgPaper = self.snapSvgPaper;
                var reTextElement = pathGroup.select('text');
                var sourceId = pathGroup.attr('source-id');
                var targetId = pathGroup.attr('target-id');
                var source = overallGroup.select('g[model-id="' + sourceId + '"]');
                var target = overallGroup.select('g[model-id="' + targetId + '"]');
                var isInSubProcess = flowNode.parentId !== self.rootId;
                var parentX = 0, parentY = 0, sourceBBox, targetBBox;
                var sourceVScale = source.select('.v-scale');
                var targetVScale = target.select('.v-scale');
                if(isInSubProcess){
                    var subProcess = overallGroup.select('g[model-id="' + flowNode.parentId + '"]');
                    parentX = subProcess.matrix.e;
                    parentY = subProcess.matrix.f;
                }
                sourceBBox = {
                    x: source.matrix.e,
                    y: source.matrix.f,
                    width: sourceVScale.getBBox().width,
                    height: sourceVScale.getBBox().height
                };
                targetBBox = {
                    x: target.matrix.e,
                    y: target.matrix.f,
                    width: targetVScale.getBBox().width,
                    height: targetVScale.getBBox().height
                };
                var position = self.getAllPosition(sourceBBox, targetBBox, parentX, parentY);
                var quadrant = self.quadrant(position);
                var attr = {
                    fill: '#333333',
                    fontSize: 12
                };
                if (reTextElement) {
                    reTextElement.remove();
                }
                if (list.length > 0) {
                    var textes = [];
                    var path = pathGroup.select('.v-path');
                    list.forEach(function (item) {
                        textes.push(item.state)
                    })
                    var textElement = snapSvgPaper.text(0, 0, textes);
                    textElement.attr(attr);
                    textElement.selectAll('tspan').forEach(function (tspan, i) {
                        if (i > 0) {
                            tspan.attr({
                                'x': 0,
                                'dy': '1em'
                            })
                        }
                    })
                    pathGroup.add(textElement)
                    self.changePathText(pathGroup, textElement, quadrant)
                }
            });

            //改变角色
            gBusEvent.$on('handleChangRole', function (roleList, value) {
                var flowNode = self.flowNode;
                var snapSvgPaper = self.snapSvgPaper;
                var overallGroup = self.overallGroup;
                var userTaskElement = overallGroup.select('g[model-id="' + flowNode.id + '"]');
                var vRoles = userTaskElement.select('.v-audit-roles');
                var vScale = userTaskElement.select('.v-scale');
                var vRect = userTaskElement.select('.v-scale rect');
                var vTitleRect = userTaskElement.select('.v-title rect');
                var vTitleRectText = userTaskElement.select('.v-title text');
                var vAuditRoleContainer = userTaskElement.select('.v-audit-role-container');
                var vAuditWidth = vAuditRoleContainer.getBBox().width;
                var vText = vRoles.select('text');
                var vTextAttr = vText.attr();
                var scale;
                var names = [];
                var width, titleWidth, vTitleRectTextMatrix;
                self.addMatrix(vScale);
                self.addMatrix(vTitleRectText);
                scale = vScale.matrix.d;
                if (roleList.length === 0 && value) {
                    names.push('专家指派页面选择专家')
                } else {
                    roleList.forEach(function (item) {
                        names.push(item.name)
                    })
                }
                width = names.length * 12 + (names.length - 1) * 6;
                vTextAttr.width = width;
                vText.remove();
                vRoles.append(snapSvgPaper.text(0, 0, names.join('，')).attr(vTextAttr));
                vAuditWidth = vAuditRoleContainer.getBBox().width;
                vAuditWidth = vAuditWidth + 57 > 153 ? vAuditWidth + 57 : 153;
                titleWidth = (vAuditWidth) / scale + 5;
                vRect.attr('width', titleWidth);
                vTitleRect.attr('width', titleWidth);
                vTitleRectTextMatrix = vTitleRectText.matrix;
                vTitleRectTextMatrix.e = vAuditWidth / 2;
                vTitleRectText.attr('transform', vTitleRectTextMatrix.toTransformString());

                var left = userTaskElement.matrix.e;
                var top = userTaskElement.matrix.f + 5;
                if (userTaskElement.attr('parent-id') !== self.rootId) {
                    //流程组
                    var subProcessParent = overallGroup.select('g[model-id="' + userTaskElement.attr('parent-id') + '"]');
                    self.addMatrix(subProcessParent);
                    var subProcessParentMatrix = subProcessParent.matrix;
                    left += subProcessParentMatrix.e;
                    top += subProcessParentMatrix.f;
                }
                self.$store.dispatch('changeMoveableRect', {
                    x: left,
                    y: top,
                    width: vScale.getBBox().width,
                    height: vScale.getBBox().height
                });
                // self.changeTitleRectWidth()
            });

            //修改用户节点审核类型
            gBusEvent.$on('handleChangeRegType', function (modelId) {
                var overallGroup = self.overallGroup;
                var pathGroup = overallGroup.select('g[source-id="' + modelId + '"]');
                var elExCGateWay, targetId;
                if (pathGroup) {
                    targetId = pathGroup.attr('target-id');
                    elExCGateWay = overallGroup.select('g[model-id="' + targetId + '"]');
                    if (elExCGateWay) {
                        var nodeData = self.getNodeDataAndIndex(targetId);
                        if (nodeData.node) {
                            nodeData.node.gstatusTtype = '';
                            self.$store.dispatch('updateOtherFlowNode', {
                                node: nodeData.node,
                                index: nodeData.index
                            });


                            gBusEvent.$emit('handleChangeStatusList', nodeData.node.id);
                        }
                    }
                }

            });

            gBusEvent.$on('handleChangeStatusList', function (id) {
                self.emptyElExCGateWayLinkLinesStatus(id);
            });

            gBusEvent.$on('updateFlowNodeLevel', function () {
                var flowNodeList = self.flowNodeList;
                flowNodeList.forEach(function (item) {
                    if (FlowDesignConfig.startNoneEventTypes.indexOf(item.type) > -1) {
                        item.level = 1;
                    }
                });
                self.$store.dispatch('setFlowNodeList', flowNodeList);
            })

            gBusEvent.$on('clearPathColor', function () {
                self.resetPathColor();
            })
        },


        emptyElExCGateWayLinkLinesStatus: function (modelId) {
            var overallGroup = this.overallGroup;
            var asSourcePathGroups = overallGroup.selectAll('g[source-id="' + modelId + '"]');
            var asTargetPathGroups = overallGroup.selectAll('g[target-id="' + modelId + '"]');
            var self = this;
            if (asSourcePathGroups) {
                asSourcePathGroups.forEach(function (item) {
                    var nodeData = self.getNodeDataAndIndex(item.attr('model-id'));
                    nodeData.node.statusIds = [];
                    var textElement = item.select('text');
                    textElement && textElement.remove();
                    self.$store.dispatch('updateOtherFlowNode', nodeData);
                })
            }
            if (asTargetPathGroups) {
                asTargetPathGroups.forEach(function (item) {
                    var nodeData = self.getNodeDataAndIndex(item.attr('model-id'));
                    nodeData.node.statusIds = [];
                    var textElement = item.select('text');
                    textElement && textElement.remove();
                    self.$store.dispatch('updateOtherFlowNode', nodeData);
                })
            }
        },


        changeTitleRectWidth: function (element, width) {
            element.attr('width', width);
        },


        //获取数据
        getSvgNodeList: function () {
            var self = this;
            this.$axios.get('/actyw/gnode/queryByGroup/' + this.groupId).then(function (response) {
                var data = response.data;
                var uiJson = '';
                var group = {};
                // try {
                //     data = JSON.parse(data);
                // }catch (e){
                //     console.log(e)
                // }
                if (data.status) {
                    data = data.datas;
                    group = data.group;
                    uiJson = JSON.parse(group.uiJson);
                    self.addNodeListXml(data.group.uiHtml);
                    self.changeSvgPaperStyle(uiJson);
                    self.elementsAddDragEvent();
                    self.pathsAddDragEvent();
                    self.$store.dispatch('setFlowNodeList', uiJson.list)
                }
            }).catch(function (error) {

            })
        },

        //改变宽度
        changeSvgPaperStyle: function (style) {
            this.$store.dispatch('changeSvgPaperStyle', {
                width: style.width,
                height: style.height
            })
        },

        //添加已经存储好的数据
        addNodeListXml: function (xml) {
            var fragment = new Snap.fragment(xml);
            this.overallGroup.append(fragment);
        },

        //创建底层画布
        createdOverallGroup: function () {
            var snapSvgPaper = this.snapSvgPaper;
            this.overallGroup = snapSvgPaper.group().addClass('v-overall-group').attr({
                'transform': 'matrix(1,0,0,1,0,0)'
            })
            snapSvgPaper.add(this.overallGroup)
            this.snapSvgPaperEvent();
        },

        snapSvgPaperEvent: function () {
            var overallGroup = this.overallGroup;
            var self = this;
            this.snapSvgPaper.click(function (event) {
                event.stopPropagation();
                var selectedElement = overallGroup.selectAll('g.el-selected')
                if (selectedElement.length > 0) {
                    selectedElement.forEach(function (item) {
                        item.removeClass('el-selected el-moveable')
                    })
                }
                self.$store.dispatch('setCurFlowNode', {
                    node: {},
                    index: -1
                })
                self.$store.dispatch('changeableRectVisible', false);
            })
        },

        getUUIds: function (num) {
            var self = this;
            this.$axios({
                url: '/sys/uuids/' + num,
                method: 'GET',
            }).then(function (response) {
                var data = response.data;
                if (data.status) {
                    self.uuids = self.uuids.concat(JSON.parse(data.id));
                }
            });
        },


    },
    created: function () {
        this.getUUIds(100);
        this.getSvgNodeList();


    },
    mounted: function () {
        var self = this;
        this.$store.dispatch('setSnapSvgPaper', Snap(this.$refs.svgPaper));
        this.createdOverallGroup();
        this.listenChangePathStatus();


        $(document).on('keyup', function ($event) {
            var flowNode = self.flowNode;
            var flowNodeIndex = self.flowNodeIndex;
            if ($event.keyCode === 46) {
                if (!flowNode.id) {
                    return false;
                }
                gBusEvent.$emit('delNode', {
                    node: flowNode,
                    index: flowNodeIndex
                });
                self.$store.dispatch('removeFlowNodeList', flowNodeIndex);
                self.$store.dispatch('setCurFlowNode', {
                    node: {},
                    index: -1
                })
                self.$store.dispatch('changeableRectVisible', false);

            }
        })
    }
})