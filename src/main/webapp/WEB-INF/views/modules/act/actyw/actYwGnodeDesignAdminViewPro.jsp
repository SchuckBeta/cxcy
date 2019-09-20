<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>流程管理</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <!-- <meta name="decorator" content="default"/> -->
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue.min.js"></script>
    <script src="${ctxStatic}/jsPlumb/jsplumb.js" type="text/javascript"></script>


    <style type="text/css">
        .top-header{
            width: 100%;
            height:93px;
            overflow: hidden;
            background-color:#e9432d ;
        }
        .flowLevelOne {
            min-width: 120px;
            text-align: center;
        }

        .flowEnd {
            margin-bottom: 60px;
        }

        .rStatus2 {
            border: 1px solid #e9432d;
        }

        .node-title {
            margin: 0;
        }

        .rStatus2 .node-title {
            margin: 0;
            line-height: 30px;
            background-color: #e9432d;
            color: #fff;
        }

        .rStatus0 {
            border: 1px solid #ddd;
        }

        .rStatus0 .node-title {
            margin: 0;
            line-height: 30px;
            background-color: #ddd;
            color: #333;
        }

        .rStatus1 {
            border: 1px solid #ffcc99;
        }

        .rStatus1 .node-title {
            margin: 0;
            line-height: 30px;
            background-color: #ffcc99;
            color: #333;
        }

        .flowStart.rStatus2, .flowEnd.rStatus2 {
            padding: 5px 20px;
            text-align: center;
            border-radius: 50%;
            background-color: #e9432d;
        }

        .flowStart.rStatus1, .flowEnd.rStatus1 {
            padding: 5px 20px;
            text-align: center;
            border-radius: 50%;
            background-color: #ffcc99;
        }

        .flowStart.rStatus0, .flowEnd.rStatus0 {
            padding: 5px 20px;
            text-align: center;
            border-radius: 50%;
            background-color: #ddd;
        }

        .flowLevelOne > span {
            line-height: 20px;
            color: #333;
            padding: 0 5px;
        }

        .flowLevelTwo {
            display: inline-block;
            padding: 6px 4px;
            min-width: 60px;
            text-align: center;
            border: 5px solid rgba(128, 128, 128, .3);
            background-color: rgba(255, 204, 153, 0.45);
        }

        .flowLevelTwo > span {
            line-height: 20px;
            color: #333;
            padding: 0 5px;
        }

        .jkt-node-box {
            position: absolute;
            min-width: 120px;
            padding: 0 10px;
            text-align: center;

        }

        .jtk-group-expanded {

        }

        .jktLabel {
            width: 42px;
            height: 20px;
            font-size: 12px;
            border: 1px dashed #ffcc99;
            background-color: rgba(255, 255, 255, .8);
            text-align: center;
            z-index: 2000;
        }

        .legends {
            position: absolute;
            top: 30px;
            right: 30px;
        }

        .legends .legend {
            margin-bottom: 8px;
        }

        .legends .legend > span {
            display: inline-block;
            width: 20px;
            height: 8px;
            margin-right: 5px;
            vertical-align: middle;
        }

        .legends .legend .green {
            background-color: #e9432d;
        }

        .legends .legend .blue {
            background-color: #ffcc99;
        }

        .legends .legend .orange {
            background-color: #ddd;
        }

        .node-flow-group {
            padding: 3px 8px;
        }

        .sub-flow-title {

        }

        .sub-flow-title, .sub-flow-sh {
            margin: 0;
            line-height: 24px;
            padding: 0 10px;
        }

        .sub-node-flow-name, .sub-flow {
            margin: 10px 20px;
            width: 300px;
        }

        .sub-node-flow-name > p, .sub-flow > p {
            margin: 0;
        }

        .sub-flow.rStatus2 {
            border: 1px solid #e9432d;
        }

        .sub-flow.rStatus1 {
            border: 1px solid #fff;
        }

        .sub-flow.rStatus0 {
            border: 1px solid #ddd;
        }

        .sub-flow.rStatus2 .sub-flow-title {
            background-color: #e9432d;
            color: #fff;
        }

        .sub-flow.rStatus1 .sub-flow-title {
            background-color: #ffcc99;
            color: #333;
        }

        .sub-flow.rStatus0 .sub-flow-title {
            background-color: #ddd;
            color: #333;
        }

        .sub-box + .sub-box {
            margin-top: 60px;
        }

        .jkt-node-box .user-list {
            display: inline-block;
            margin-bottom: 0;
        }

        .jkt-node-box .user-list span, .jkt-node-box .user-list a {
            margin-left: 8px;
        }

        .users-more-container {
            padding-left: 10px;
        }

        .popover {
            display: block;
        }

        .popover-title {
            background-color: #ffcc99;
        }

        .popover .popover-content span {
            display: inline-block;
            white-space: nowrap;
            padding: 0 4px;
        }

        .fade-enter-active, .fade-leave-active {
            transition: opacity .5s
        }

        .fade-enter, .fade-leave-to /* .fade-leave-active in below version 2.1.8 */
        {
            opacity: 0
        }
    </style>

</head>
<body>
<div class="top-header">
    <img src="${ctxImg}/managetoplogo_01.png">
</div>
<div class="container-fluid" id="gNodeDesignView">
    <input type="hidden" id="gnodeId" value="${gnodeId}">
    <input type="hidden" id="groupId" value="${groupId}">
    <div style="position: relative">
        <h3 class="text-center">${group.name}</h3>
        <%--<div class="mybreadcrumbs">--%>
        <%--<span>流程</span>--%>
        <%--</div>--%>

        <%--<ul class="nav nav-tabs">--%>
        <%--<li class="active"><a href="javascript:void(0);">${group.name}流程查看</a></li>--%>
        <%--</ul>--%>
        <div ref="nodeView" id="nodeView" class="node-view" style="position: relative" v-if="nodeList.length > 0">
            <jkt-node node-level="0" node-name="开始" :id="startNode.id" :r-status="startNode.rstatus"
                      :flow-group="false"
                      :has-sub="false"></jkt-node>
            <jkt-node node-level="4" node-name="结束" :id="endNode.id" :flow-group="false"
                      :pre-id="endNode.preId"
                      :has-sub="false"
                      :r-status="endNode.rstatus"></jkt-node>
            <jkt-node v-for="(item,idx) in nodeList"
                      :key="item.id"
                      :ref="item.id"
                      :id="item.id"
                      :node-level="item.node.level"
                      :node-name="item.name"
                      :pre-id="item.preId"
                      :group-style="item.style"
                      :flow-group="item.flowGroup"
                      :node="item"
                      @know-more="knowMore"
                      :has-sub="item.sub && item.sub.length > 0"
                      :r-status="item.rstatus"></jkt-node>
        </div>
        <div class="legends">
            <div class="legend"><span class="green"></span>已完成</div>
            <div class="legend"><span class="blue"></span>进行中</div>
            <div class="legend"><span class="orange"></span>未开始</div>
        </div>
        <transition name="fade">
            <div ref="popover" class="popover right" v-show="moreUserShow" :style="userStyle">
                <div class="arrow"></div>
                <h3 class="popover-title">审核人</h3>
                <div class="popover-content">
                    <span v-for="(user, index) in moreUser">{{user.name}}</span>
                </div>
            </div>
        </transition>
    </div>
</div>
<div id="dialog-message" title="信息">
    <p id="dialog-content"></p>
</div>

<script>
    //组件
    var jktNode = Vue.component('jkt-node', {
        template: '<div class="jkt-node-box"><div class="jkt-node" :style="groupStyle" :class="jktNodeClass"> <p class="node-title">{{nodeName | replaceXm}}</p>' +
        '<div v-if="hasSub" v-for="(item, idx) in node.sub" :id="item.id" class="sub-box">' +
        '<div v-if="item.rstatus == 1 && !item.isFront && item.roles" class="sub-node-flow-name"><div>审核角色：<p class="user-list"><span>{{item.roles[0].name}}</span><template v-if="false">(<span v-for="(user, index) in userList[idx]">{{user.name}}</span>' +
        '<a v-if="item.users.length > 2" @click="knowMore(item.users, $event)"  href="javascript: void(0)">查看更多</a> )</template></p></div></div>' +
        '</div>' +
        '</div></div> ',
        props: {
            hasSub: {
                type: Boolean,
                default: false
            },
            node: {
                type: Object,
                default: function () {
                    return {
                        gnode: {
                            sub: []
                        }
                    }
                }
            },
            nodeLevel: {
                type: [String, Boolean]
            },
            nodeName: {
                type: String
            },
            flowGroup: {
                type: [String, Boolean],
                default: ''
            },
            groupStyle: {
                type: Object,
                default: function () {
                    return {}
                }
            },
            rStatus: {
                type: [String, Number]
            }
        },
        computed: {
            jktNodeClass: function () {
                return {
                    'flowLevelOne': this.nodeLevel === '1',
                    'flowLevelTwo': this.nodeLevel === '2',
//                    'flowLevelThree': this.rStatus == '0' && this.nodeLevel === '2',
//                    'flowLevelGoing': this.nodeLevel === '2' && this.rStatus == '1',
                    'rStatus1': this.rStatus == '1',
                    'rStatus0': this.rStatus == '0',
                    'rStatus2': this.rStatus == '2',
                    'flowStart': this.nodeLevel === '0',
                    'flowEnd': this.nodeLevel === '4',
                    'commonNode': true
                }
            },
            userList: function () {
                var nodeList = this.node.sub;
                var userList = [];
                nodeList.forEach(function (item) {
                    if (item.users.length >= 2) {
                        userList.push(item.users.slice(0, 2));
                    } else {
                        userList.push(item.users)
                    }
                });

                return userList
            }
        },
        data: function () {
            return {}
        },
        filters: {
            replaceXm: function (val) {
                return val.replace(/(\（项目\）)|(\（大赛\）)/, '')
            }
        },
        methods: {
            knowMore: function (users, $event) {
                var $target = $($event.target);
                var offsetX = $target.offset().left;
                var offsetY = $target.offset().top;
                var pos = {
                    left: (offsetX + 56) + 'px',
                    top: (offsetY + 10) + 'px'
                };
                this.$emit('know-more', [users, pos]);
            }
        }
    });

    //链接点样式


    var pointStyles = {
        complete: {
            endpoint: "Dot",
            paintStyle: {
                stroke: "transparent",
                fill: "transparent",
                radius: 1,
                strokeWidth: 1
            },
            isSource: true,
            connector: ["Flowchart", {stub: [40, 60], gap: 1, cornerRadius: 1, alwaysRespectStubs: true}],
            connectorStyle: {
                strokeWidth: 1,
                stroke: "#e9432d",
                joinstyle: "round",
                outlineStroke: "white",
                outlineWidth: 1
            },
            maxConnections: 1,
            isTarget: true,
            draggable: false,
            dropOptions: {hoverClass: "hover", activeClass: "active"}
        },
        going: {
            endpoint: "Dot",
            paintStyle: {
                stroke: "transparent",
                fill: "transparent",
                radius: 1,
                strokeWidth: 1
            },
            isSource: true,
            connector: ["Flowchart", {stub: [40, 60], gap: 3, cornerRadius: 1, alwaysRespectStubs: true}],
            connectorStyle: {
                strokeWidth: 1,
                stroke: "#ffcc99",
                joinstyle: "round",
                outlineStroke: "white",
                outlineWidth: 1
            },
            maxConnections: 1,
            isTarget: true,
            draggable: false,
            dropOptions: {hoverClass: "hover", activeClass: "active"}
        },
        NotStarted: {
            endpoint: "Dot",
            paintStyle: {
                stroke: "transparent",
                fill: "transparent",
                radius: 1,
                strokeWidth: 1
            },
            isSource: true,
            connector: ["Flowchart", {stub: [40, 60], gap: 1, cornerRadius: 1, alwaysRespectStubs: true}],
            connectorStyle: {
                strokeWidth: 1,
                stroke: "#ddd",
                joinstyle: "round",
                outlineStroke: "white",
                outlineWidth: 1
            },
            maxConnections: 1,
            isTarget: true,
            draggable: false,
            dropOptions: {hoverClass: "hover", activeClass: "active"}
        }
    };


    var gNodeDesignView = new Vue({
        el: '#gNodeDesignView',
        data: function () {
            return {
                nodeList: [],
                snap: 62,
                totalTop: 0,
                startNode: {
                    gnode: {
                        id: ''
                    }
                },
                endNode: {
                    gnode: {
                        id: ''
                    }
                },
                subNodeList: [],
                plaintEdgesArr: [],
                userList: [],
                moreUserShow: false,
                userStyle: {},
                popoverTimerId: null
            }
        },
        computed: {
            flowInstance: function () {
                return jsPlumb.getInstance({
                    ConnectionOverlays: [
                        ["Arrow", {
                            location: 1,
                            visible: true,
                            width: 11,
                            length: 11,
                            id: "ARROW",
                            events: {
                                click: function () {
//                                    alert("you clicked on the arrow overlay")
                                }
                            }
                        }]
//                        ["Label", {
//                            location: 0.2,
//                            id: "label",
//                            cssClass: "jktLabel",
//                            events: {
////                                tap:function() { alert("hey"); }
//                            }
//                        }]
                    ],
                    Container: 'nodeView',
                    draggable: false
                })
            },
            moreUser: function () {
                return this.userList.length ? this.userList.slice(2, this.userList.length > 15 ? 15 : this.userList.length - 1) : [];
            }
        },
        watch: {
            moreUserShow: function (val) {
                var self = this;
                if (!val) {
                    this.popoverTimerId && clearTimeout(this.popoverTimerId);
                } else {
                    this.popoverTimerId && clearTimeout(this.popoverTimerId);
                    this.popoverTimerId = setTimeout(function () {
                        self.moreUserShow = false;
                    }, 2500)
                }
            }
        },
        methods: {
            _addGroup: function (ele, groupId) {
                this.flowInstance.addGroup({
                    el: ele,
                    id: groupId,
                    constrain: false,
                    anchor: "TopCenter",
                    endpoint: "Blank",
                    droppable: false,
                    draggable: false
                });
            },

            _addEndpoints: function (toId, sourceAnchors, targetAnchors, rstatus) {
                for (var i = 0; i < sourceAnchors.length; i++) {
                    var sourceUUID = toId + sourceAnchors[i];
                    var pointStyle;
                    if (rstatus == 2) {
                        pointStyle = pointStyles['complete']
                    } else if (rstatus == 1) {
                        pointStyle = pointStyles['going']
                    } else {
                        pointStyle = pointStyles['NotStarted']
                    }
                    this.flowInstance.addEndpoint(toId, pointStyle, {anchor: sourceAnchors[i], uuid: sourceUUID});
                }
                for (var j = 0; j < targetAnchors.length; j++) {
                    var targetUUID = toId + targetAnchors[j];
                    this.flowInstance.addEndpoint(toId, pointStyle, {anchor: targetAnchors[j], uuid: targetUUID});
                }
            },

            getLineData: function (id) {
                var line;
                var nodeList = this.nodeList;
                for (var i = 0; i < nodeList.length; i++) {
                    if (nodeList[i].id === id) {
                        line = nodeList[i];
                        break;
                    }
                }
                if (!line) {
                    line = this.startNode.id === id ? this.startNode : this.endNode;
                }
                return line;
            },

            flowInstanceBatch: function () {
                var self = this;
                this.flowInstance.batch(function () {
                    var startId = self.startNode.id;
                    var endId = self.endNode.id;
                    self._addEndpoints(startId, ["BottomCenter"], ["TopCenter"], self.startNode.rstatus);
                    self._addEndpoints(endId, ["BottomCenter"], ["TopCenter"], self.endNode.rstatus);
                    self.nodeList.forEach(function (item, i) {
                        self._addEndpoints(item.id, ["BottomCenter"], ["TopCenter"], item.rstatus);
                        var sub = item.sub;
                        if (item.hasGroup && sub.length) {
                            sub.forEach(function (subItem) {
                                self._addEndpoints(subItem.id, ["BottomCenter"], ["TopCenter"], subItem.rstatus);
                            })
                        }
                    });
                    self.nodeList.forEach(function (item, i) {
                        var uuids;
                        if (i < self.nodeList.length - 1) {
                            uuids = [item.id + 'BottomCenter', self.nodeList[i + 1].id + 'TopCenter'];
                        }
                        if (uuids) {
                            self.flowInstance.connect({uuids: uuids});
                        }
                        var ele = self.$refs[item.id][0].$el;
                        self._addGroup(ele, item.id);

                        var sub = item.sub;
                        if (item.hasGroup && sub.length > 1) {
                            sub.forEach(function (subItem, subI) {
                                if (subI < sub.length - 1) {
                                    var subUuids = [subItem.id + 'BottomCenter', sub[subI + 1].id + 'TopCenter'];
                                    self.flowInstance.connect({uuids: subUuids});
                                }
                            })
                        }

                    });
                    self.flowInstance.connect({uuids: [startId + 'BottomCenter', self.nodeList[0].id + 'TopCenter']});
                    self.flowInstance.connect({uuids: [self.nodeList[self.nodeList.length - 1].id + 'BottomCenter', endId + 'TopCenter']});
                    var $flowEnd = $('.flowEnd ').parent();
                    var fTop = parseInt($flowEnd.css('top')) || 0;
                    var fHeight = $flowEnd.height();
                    $('#nodeView').css("height", fTop + 100)
                    //子类添加

                })
            },
            nodePositions: function () {
                var self = this;
                var winWidth = $('#nodeView').width();
                var $startNode = $('#' + this.startNode.id);
                var $endNode = $('#' + this.endNode.id);
                var $ds = $(this.$refs[this.nodeList[this.nodeList.length - 1].id][0].$el);

                $startNode.css({
                    'left': (winWidth - $startNode.width()) / 2,
                    'top': 20
                });

                self.nodeList.forEach(function (item, i) {
                    var $item = $('#' + item.id);
                    var beforeOffsetTop;
                    var beforeEle;
                    if (i > 0) {
                        beforeEle = $item.prev();
                        beforeOffsetTop = parseInt(beforeEle.css('top'));
                        $item.css({
                            'top': beforeOffsetTop + self.snap + beforeEle.height(),
                            'left': (winWidth - $item.width()) / 2
                        })
                    } else {
                        $item.css({
                            'top': self.snap + 42 + 20,
                            'left': (winWidth - $item.width()) / 2
                        })
                    }

                });

                $endNode.css({
                    'left': (winWidth - $startNode.width()) / 2,
                    'top': parseInt($ds.css('top')) + self.snap + $ds.height()
                })

            },
            knowMore: function (users) {
                var popover = this.$refs.popover;
                var self = this;
                var userStyle = users[1];
                this.userList = users[0];

                if (!this.moreUserShow) {
                    this.moreUserShow = true;
                }
                setTimeout(function () {
                    userStyle.top = (parseInt(userStyle.top) - $(popover).height() / 2 ) + 'px';
                    self.userStyle = userStyle;
                })

            },
            getNodeList: function () {
                var gnodeId = $('#gnodeId').val();
                var groupId = $('#groupId').val();
                var url = ('/${faUrl}/actyw/actYwGnode/queryStatusTreeByGnode/' + groupId + '?gnodeId=' + gnodeId + '&grade=${grade}');
                var xhr = $.get(url);
                var self = this;
                xhr.success(function (data) {
                    if (data.status) {
                        var nodeList = data.datas.slice(0);
                        var fakeNode = [];
                        var filterNodeList;
                        var startNode = data.datas[0];
                        filterNodeList = nodeList.filter(function (item) {
                            return item.node.nodeType == 'node' && (item.node.nodeKey != 'EndNoneEvent' && item.node.nodeKey != 'StartNoneEvent')
                        });

                        $.each(filterNodeList, function (i, item) {
                            if (item.parentId == 1) {
                                var id = item.id;
                                item['sub'] = [];
                                $.each(filterNodeList, function (i2, item2) {
                                    if (item2.parentId == id) {
                                        item['sub'].push(item2);
                                        self.subNodeList.push(item2);
                                    }
                                })
                            }
                        });

                        $.each(filterNodeList, function (i, item) {
                            if (item.parentId == 1) {
                                var sub = item.sub;
                                var isComplete;
                                if (sub && sub.length) {
                                    if (item.rstatus == '2') {
                                        isComplete = sub.every(function (subItem) {
                                            return subItem.rstatus == '2'
                                        });
                                        if (isComplete) {
                                            item.rstatus = '2';
                                        } else {
                                            item.rstatus = '1';
                                        }
                                    }
                                }
                                if (item.hasGroup) {
                                    if (sub && sub.length) {
                                        item.style = {
                                            height: ''
                                        }
                                    }
                                } else {
                                    item.style = {
                                        height: ''
                                    }
                                }
                                fakeNode.push(item)
                            }
                        });

                        var isJx = fakeNode.every(function (item) {
                            return item.rstatus == '2';
                        });

                        var isSomeStart = fakeNode.some(function (item) {
                            return item.rstatus == '2';
                        });

                        var endNode = data.datas[data.datas.length - 1];

                        if (isJx) {
                            endNode['rstatus'] = '2';
                            self.endNode = endNode;
                        } else {
                            self.endNode = endNode;
                        }

                        if (isSomeStart) {
                            startNode['rstatus'] = 2;
                        }
                        var filterFakeNode = fakeNode.slice(0);
                        filterFakeNode.forEach(function (item) {
                            var sub = item.sub;
                            var updateSub = [];
                            if (sub.length > 0) {
                                updateSub = sub.filter(function (subItem) {
                                    return subItem.rstatus == 1
                                });
                                item.sub = updateSub;
                            }
                        });
                        self.startNode = startNode;
                        self.nodeList = filterFakeNode;
                        self.$nextTick(function () {
                            self.nodePositions();
                            self.flowInstanceBatch();


                        })
                    } else {
                        console.log(data)
                    }
                });
                xhr.error(function (error) {
                    console.log(error)
                })
            }
        },
        beforeMount: function () {
            this.getNodeList();
        },
        mounted: function () {

        }
    })

</script>
</body>
</html>