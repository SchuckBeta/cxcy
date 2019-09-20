<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxJs}/cyjd/designFloor/shape.js"></script>
    <script src="${ctxStatic}/snap/snap.svg-min.js"></script>
    <script src="${ctxStatic}/vue/vue.min.js"></script>
    <script src="${ctxJs}/cyjd/designFloor/directives/v-panning.js"></script>
    <script src="${ctxJs}/cyjd/designFloor/directives/v-generate-room.js"></script>
    <script src="${ctxJs}/cyjd/designFloor/directives/v-generate-room-asset.js"></script>
    <script src="${ctxJs}/cyjd/designFloor/components/colorPicker.js"></script>
    <script src="${ctxJs}/cyjd/designFloor/components/groupShape.js"></script>
    <script src="${ctxJs}/cyjd/designFloor/components/groupDescription.js"></script>
    <script src="${ctxJs}/cyjd/designFloor/components/groupText.js"></script>
    <script src="${ctxJs}/cyjd/designFloor/components/groupRect.js"></script>
    <script src="${ctxJs}/cyjd/designFloor/components/groupImage.js"></script>
    <script src="${ctxJs}/cyjd/designFloor/components/groupTrimming.js"></script>
    <script src="${ctxJs}/cyjd/designFloor/rdPaper.js"></script>
    <style>
        @font-face {
            font-family: 'iconfont';  /* project id 482029 */
            src: url('//at.alicdn.com/t/font_482029_tfq45pwndohia4i.eot');
            src: url('//at.alicdn.com/t/font_482029_tfq45pwndohia4i.eot?#iefix') format('embedded-opentype'),
            url('//at.alicdn.com/t/font_482029_tfq45pwndohia4i.woff') format('woff'),
            url('//at.alicdn.com/t/font_482029_tfq45pwndohia4i.ttf') format('truetype'),
            url('//at.alicdn.com/t/font_482029_tfq45pwndohia4i.svg#iconfont') format('svg');
        }

        .iconfont {
            font-family: "iconfont" !important;
            font-size: 16px;
            font-style: normal;
            -webkit-font-smoothing: antialiased;
            -webkit-text-stroke-width: 0.2px;
            -moz-osx-font-smoothing: grayscale;
        }

        .paper-scroller {
            position: relative;
            overflow: scroll;
            box-sizing: border-box;
            width: 100%;
            height: 100%;
        }

        .paper-scroller-background {
            margin: 0;
            position: relative;
            display: inline-block;
            width: 3868px;
            height: 3604px;
        }

        .paper-scroller[data-cursor=grab] {
            cursor: all-scroll;
            cursor: -webkit-grab;
            cursor: -moz-grab;
            cursor: grab;
        }

        .paper-scroller.is-panning[data-cursor=grab] {
            cursor: -webkit-grabbing;
            cursor: -moz-grabbing;
            cursor: grabbing;
        }

        .paper-scroller .rd-move {
            cursor: move;
        }

        .paper-scroller text {
            cursor: default;
        }

        .rd-free-transform {
            display: none;
            position: absolute;
            pointer-events: none;
            -moz-user-select: none;
            -webkit-user-select: none;
            -ms-user-select: none;
            user-select: none;
            -webkit-user-drag: none;
            user-drag: none;
            border: 1px solid #3399ff;
            z-index: 1000;
        }

        #rdPaper > * {
            -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
        }

        .rd-free-transform .resize {
            position: absolute;
            background-color: #39f;
            height: 8px;
            opacity: .75;
            width: 8px;
        }

        .rd-free-transform .nw {
            cursor: nw-resize;
            left: -4px;
            top: -4px;
            pointer-events: auto;
        }

        .rd-free-transform .n {
            cursor: n-resize;
            left: 50%;
            margin-left: -4px;
            top: -4px;
            pointer-events: auto;
        }

        .rd-free-transform .ne {
            cursor: ne-resize;
            right: -4px;
            top: -4px;
            pointer-events: auto;
        }

        .rd-free-transform .se {
            cursor: se-resize;
            right: -4px;
            bottom: -4px;
            pointer-events: auto;
        }

        .rd-free-transform .e {
            cursor: e-resize;
            right: -4px;
            top: 50%;
            margin-top: -4px;
            pointer-events: auto;
        }

        .rd-free-transform .s {
            cursor: s-resize;
            left: 50%;
            bottom: -4px;
            margin-left: -4px;
            pointer-events: auto;
        }

        .rd-free-transform .sw {
            cursor: sw-resize;
            left: -3px;
            bottom: -3px;
            pointer-events: auto;
        }

        .rd-free-transform .w {
            cursor: w-resize;
            left: -4px;
            top: 50%;
            margin-top: -4px;
            pointer-events: auto;
        }

        html, body, .rd-app {
            position: relative;
            width: 100%;
            height: 100%;
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        .rd-body {
            position: relative;
            height: -moz-calc(100% - 60px);
            height: -webkit-calc(100% - 60px);
            height: calc(100% - 60px);
        }

        .stencil-container {
            position: absolute;
            left: 0;
            top: 0;
            width: 240px;
            height: 100%;
        }

        .rd-stencil {
            top: 0;
            bottom: 0;
            left: 0;
            position: absolute;
            right: 0;
            color: #c6c7e2;
            background: #383b61;
        }

        .paper-container {
            position: absolute;
            left: 240px;
            right: 240px;
            top: 0;
            height: 100%;
        }

        .rd-stencil .groups-toggle {
            position: relative;
            padding: 0 5px;
            height: 32px;
            line-height: 32px;
        }

        body {
            -webkit-user-select: none;
            -moz-user-select: -moz-none;
            user-select: none;
        }

    </style>
    <script>
        $(document).ready(function () {
            showfloor();
        })

        function showfloor() {
            $.ajax({
                url: '${ctx}/pw/pwFloorDesigner/floorDesDates',
                type: 'GET', //GET
                dataType: 'json',
                data: {floorId: ${floorId}},
                success: function (data) {
                    var floor = data.rooms;
                    $("#floor").html(JSON.stringify(floor));
                }
            })
        }


        function designfloor() {
            var param = {
                floorId: "1234uueecyeid",
                rooms: [{
                    id: "2fd8bf0527644d748c4bb704f898847d",
                    roomId: "123432juy1",
                    type: "桌子",
                    name: "会议桌",
                    x: "69.2",
                    y: "333.2",
                    isClickable: 1,
                    remarks: "aabbcc"
                }, {
                    id: "2fd8bf0527644d748c4bb704f898847d",
                    roomId: "123432juy2",
                    type: "桌子1",
                    name: "会议桌1",
                    x: "69.3",
                    y: "333.2",
                    isClickable: 1,
                    remarks: "aabbcc11"
                }]
            };
            $.ajax({
                url: '${ctx}/pw/pwFloorDesigner/save',
                type: 'POST', //POST
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(param),
                success: function (data) {
                    console.log("返回状态：" + data.status);
                }

            })
        }

    </script>
</head>




<body>
<div id="roomDefined" class="rd-app" style="background-color: #f6f6f6">

    <!--工具栏-->
    <div class="rd-header">
        <div class="rd-title">
            <h3>房间名</h3>
        </div>
        <div class="toolbar-container">
            <div class="rd-toolbar-group">
                <a href="javascript:void (0)">导出SVG</a>
                <a href="javascript:void (0)">导出PNG</a>
            </div>
            <div class="rd-toolbar-group">
                <a href="javascript: void (0)" @click="clearSnap" data-toggle="tooltip" data-placement="bottom" title=""
                   data-original-title="清除画布"><i
                        class="iconfont">&#xe66e;</i></a>
            </div>
            <div class="rd-toolbar-group">
                <a href="javascript: void (0)" @click="fullScreen" data-toggle="tooltip" data-placement="bottom"
                   title="" data-original-title="切换全屏显示"><i
                        class="iconfont">&#xe676;</i></a>
            </div>
            <div class="rd-toolbar-group">
                <a href="javascript: void (0)" data-toggle="tooltip" data-placement="bottom"
                   title="" data-original-title="适应屏幕"><i
                        class="iconfont">&#xe600;</i></a>
            </div>
            <div class="rd-toolbar-group">
                <span class="label-zoom">缩放</span>
                <input v-model="zoom" class="zoom" type="range" max="500" min="20" step="20">
                <output>{{zoom}}</output>
                <span>%</span>
            </div>
            <!--<div class="rd-toolbar-group">-->
            <!--<span class="label-zoom">网格大小</span>-->
            <!--<input v-model="gridSize" class="zoom" type="range" max="50" min="1" step="1">-->
            <!--<output>{{gridSize}}</output>-->
            <!--</div>-->
        </div>
        <div class="btn-group pull-right">
            <button type="button" class="btn btn-primary" @click="saveFloorRoom">保存</button>
        </div>
    </div>
    <div class="rd-body">
        <div class="stencil-container">
            <div class="rd-stencil">
                <div class="rds-content">
                    <group-shape :height="110*Math.ceil(basicShape.length/2)+'px'" title="基本房间形状">
                        <svg slot="svg" version="1.1" v-generate-room="{shapeData:basicShape, id: '#shapeDragSvg'}"
                             width="100%" height="100%" xmlns="http://www.w3.org/2000/svg"
                             xmlns:xlink="http://www.w3.org/1999/xlink"></svg>
                    </group-shape>
                    <group-shape :height="(60*Math.ceil(asset.length/3))+'px'" :shape="asset" title="房间配件">
                        <svg slot="svg" version="1.1"
                             v-generate-room-asset="{shapeData:asset, id: '#shapeDragSvg'}" width="100%"
                             height="100%" xmlns="http://www.w3.org/2000/svg"
                             xmlns:xlink="http://www.w3.org/1999/xlink"></svg>
                    </group-shape>
                    <group-shape :height="(60*Math.ceil(trimming.length/3))+'px'" :shape="trimming" title="房间补偿">
                        <svg slot="svg" version="1.1"
                             v-generate-room-asset="{shapeData:trimming, id: '#shapeDragSvg'}" width="100%"
                             height="100%" xmlns="http://www.w3.org/2000/svg"
                             xmlns:xlink="http://www.w3.org/1999/xlink"></svg>
                    </group-shape>

                </div>
            </div>
        </div>
        <div class="paper-container">
            <div class="paper-scroller" v-panning data-cursor="grab">
                <div class="paper-scroller-background">
                    <div class="rd-viewport">
                        <div class="rd-paper-background"></div>
                        <div class="rd-paper-grid"></div>
                        <div style="width: 100%; height: 100%;position: absolute;">
                            <svg id="rdPaper" height="100%" version="1.1" width="100%"
                                 xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" style="overflow: hidden; position: absolute;left: 0;right: 0; webkit-user-select: none;
    -moz-user-select: none;
    user-select: none;"></svg>
                            <div class="rd-free-transform">
                                <div draggable="false" class="resize nw" data-position="top-left"></div>
                                <div draggable="false" class="resize n" data-position="top"></div>
                                <div draggable="false" class="resize ne" data-position="top-right"></div>
                                <div draggable="false" class="resize e" data-position="right"></div>
                                <div draggable="false" class="resize se" data-position="bottom-right"></div>
                                <div draggable="false" class="resize s" data-position="bottom"></div>
                                <div draggable="false" class="resize sw" data-position="bottom-left"></div>
                                <div draggable="false" class="resize w" data-position="left"></div>
                                <!--<div draggable="false" class="rotate"></div>-->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="inspector-container">
            <div class="rd-inspector">
                <group-desc :desc="currentRoomData" :rooms="rooms" @change-room="changeRoom"
                            @delete-room="deleteRoom('currentRoomData')"></group-desc>
                <template v-for="(roomAsset, index) in currentRoomAssetData">
                    <group-text v-if="roomAsset.shapeType === 'text'" :index="index" :text="roomAsset"
                                @delete-room-asset="deleteRoomAsset"></group-text>
                    <group-rect v-if="roomAsset.shapeType === 'rect'" :index="index" :rect="roomAsset"
                                @delete-room-asset="deleteRoomAsset"></group-rect>
                    <group-image v-if="roomAsset.shapeType === 'image'" :index="index" :image="roomAsset"
                                 @delete-room-asset="deleteRoomAsset"></group-image>
                </template>
                <group-trimming :trimming="currentRoomTrimmingData"
                                @delete-trimming="deleteTrimming('currentRoomTrimmingData')"></group-trimming>
            </div>
        </div>
    </div>
    <div class="stencil-paper-drag" v-show="paperDrag.drag" :style="paperDrag.style">
        <svg id="shapeDragSvg" version="1.1" width="100%" height="100%" xmlns="http://www.w3.org/2000/svg"
             xmlns:xlink="http://www.w3.org/1999/xlink"></svg>
    </div>
    <color-picker v-model="colorPickerShow" value="false" :arrow-style="arrowStyle" :style="colorStyle"
                  @change-color="changeColor" @color-hide="colorPickerShow = false"
                  :color="color"></color-picker>
</div>
<script>

    var EventListener = new Vue();
    var RoomDefined = new Vue({
        el: '#roomDefined',
        data: function () {
            return {
                appPaper: '',
                isFullScreen: false,
                roomActiveData: {},
                roomAssetActiveData: {},
                zoom: 100,
                gridSize: 10,
                rooms: [{
                    name: '203路演大厅',
                    id: '203453'
                }, {
                    name: '204普通会议厅',
                    id: '2034568'
                }],
                roomForm: {
                    roomId: '',
                    description: ''
                },
                paperDrag: {
                    drag: false,
                    style: {
                        width: '',
                        height: '',
                        left: '',
                        top: ''
                    }
                },
                basicShape: BASICSHAPE,
                asset: ASSETS,
                trimming: TRIMMING,
                currentRoomData: {
                    children: []
                },
                currentRoomTrimmingData: {},
                currentRoomAssetData: [],
                currentRoomType: '',
                colorPickerShow: false,
                colorStyle: {},
                arrowStyle: {},
                color: '',
                roomAssetIndex: ''
            }
        },
        methods: {
//            全屏显示
            fullScreen: function (event) {
                var element = document.documentElement;
                if (!this.isFullScreen) {
                    if (element.requestFullscreen) {
                        element.requestFullscreen();
                    } else if (element.mozRequestFullScreen) {
                        element.mozRequestFullScreen();
                    } else if (element.webkitRequestFullscreen) {
                        element.webkitRequestFullscreen();
                    } else if (element.msRequestFullscreen) {
                        element.msRequestFullscreen();
                    }

                } else {
                    if (document.exitFullscreen) {
                        document.exitFullscreen();
                    } else if (document.mozCancelFullScreen) {
                        document.mozCancelFullScreen();
                    } else if (document.webkitCancelFullScreen) {
                        document.webkitCancelFullScreen();
                    } else if (document.msExitFullscreen) {
                        document.msExitFullscreen();
                    }
                }
                this.isFullScreen = !this.isFullScreen

            },


            calculateCoordinate: function () {
                var left = this.paperDrag.style.left.replace('px', '') * 1;
                var top = this.paperDrag.style.top.replace('px', '') * 1;
                var $paperScorll = $('.paper-scroller');
                var scrollLeft = $paperScorll.scrollLeft();
                var scrollTop = $paperScorll.scrollTop();
                var $rdViewport = $('.rd-viewport');
                var vpLeft = $rdViewport.css('left').replace('px', '') * 1;
                var vpHeight = $rdViewport.height();
                var vpWidth = $rdViewport.width();
                var maxHeight = vpHeight - 100;
                var maxWidth = vpWidth - 100;
                var translateX, translateY;

                if (vpLeft > scrollLeft) {
                    translateX = left - (vpLeft - scrollLeft) - 240
                } else {
                    translateX = left + (scrollLeft - vpLeft) - 240
                }

                translateY = top + scrollTop - 60;
                translateY = translateY < 0 ? 0 : translateY;
                translateY = translateY > maxHeight ? maxHeight : translateY;

                translateX = translateX < 0 ? 0 : translateX;
                translateX = translateX > maxWidth ? maxWidth : translateX;

                return {
                    translateX: translateX,
                    translateY: translateY
                }
            },

            addRoomToSvg: function () {
                var coordinate = this.calculateCoordinate();
                this.roomActiveData.transform = 'matrix(1,0,0,1,' + (coordinate.translateX) + ',' + (coordinate.translateY) + ')';
                this.appPaper.addRoom(this.roomActiveData)
            },

            addRoomAssetToSvg: function (parentEleGroup) {
                var coordinate = this.calculateCoordinate();
                var matrix = parentEleGroup.matrix;
                this.roomAssetActiveData.attr.x = coordinate.translateX - matrix.e;
                this.roomAssetActiveData.attr.y = coordinate.translateY - matrix.f;
                this.appPaper.addRoomAsset(this.roomAssetActiveData, parentEleGroup);
            },

            addTrimmingToSvg: function () {
                var coordinate = this.calculateCoordinate();
                this.roomAssetActiveData.transform = 'matrix(1,0,0,1,' + (coordinate.translateX) + ',' + (coordinate.translateY) + ')';
                this.appPaper.addRoomTrimming(this.roomAssetActiveData);
            },

            //修改颜色
            changeColor: function (color) {
                this.color = color;
                switch (this.currentRoomType) {
                    case 'roomFill':
                        this.currentRoomData.fill = color;
                        this.currentRoomData.ele.attr('fill', color);
                        break;
                    case 'roomStroke':
                        this.currentRoomData.stroke = color;
                        this.currentRoomData.ele.attr('stroke', color);
                        break;
                    case 'trimmingFill':
                        this.currentRoomTrimmingData.fill = color;
                        this.currentRoomTrimmingData.ele.attr('fill', color);
                        break;
                    case 'trimmingStroke':
                        this.currentRoomTrimmingData.stroke = color;
                        this.currentRoomTrimmingData.ele.attr('stroke', color);
                        break;
                    case 'roomChildren0Font':
                        this.currentRoomData.children[0].fill = color;
                        this.currentRoomData.children[0].ele.attr('fill', color);
                        break;
                    case 'roomAssetFill':
                        this.currentRoomAssetData[this.roomAssetIndex].fill = color;
                        this.currentRoomAssetData[this.roomAssetIndex].ele.attr('fill', color)
                        break;
                    case 'roomAssetStroke':
                        this.currentRoomAssetData[this.roomAssetIndex].stroke = color;
                        this.currentRoomAssetData[this.roomAssetIndex].ele.attr('stroke', color)
                        break;
                }
                this.colorPickerShow = false;
            },
            //修改房间ID
            changeRoom: function (roomData) {
                var g = roomData.g;
                var rectParent = g.select('.rect-parent');
                var textEle = roomData.children[0].ele;
                var textAttr = textEle.attr();
                var textStr = roomData.children[0].text;
                this.appPaper.changeText(textEle, rectParent, textStr, textAttr, g, roomData.roomId);
                this.currentRoomData = this.appPaper.getRoomData(g);
                if (this.currentRoomData.type === 'room') {
                    this.currentRoomAssetData = this.currentRoomData.children.filter(function (t, i) {
                        return i > 0;
                    })
                }

            },

            deleteRoom: function (key) {
                this[key].ele.parent().remove();
                this[key] = {};
                $('body .rd-free-transform').hide()
            },

            deleteRoomAsset: function (index) {
                this.currentRoomAssetData[index].ele.remove();
                this.currentRoomAssetData.splice(index, 1)
            },

            deleteTrimming: function (key) {
                this[key].ele.parent().remove();
                this[key] = {};
                $('body .rd-free-transform').hide()
            },

            clearSnap: function () {
                this.currentRoomAssetData.length = 0;
                this.currentRoomData = {};
                this.currentRoomTrimmingData = {};
                this.appPaper.snap.select('g').clear();
                $('body .rd-free-transform').hide();
            },


            saveFloorRoom: function () {
                var data = this.appPaper.getRooms();
                data.floorId = '';
                var xhr = $.post('${ctx}/pw/pwDesignerCanvas/saveAll', {json: JSON.stringify(data)})
                xhr.success(function (data) {
                    console.log(data)
                })
            }

        },
        created: function () {
            var self = this;


            document.onkeydown = function (event) {
                if (event.key === 'F11') {
                    event.preventDefault();
                    self.fullScreen();
                }

            }
        },
        mounted: function () {
            $('a[data-toggle="tooltip"]').tooltip();
            this.appPaper = new AppPaper();
        }
    });


    EventListener.$on('changeBgColor', function (arr) {
        var obj = arr[0];
        var position = arr[1];
        RoomDefined.$data.colorStyle = {
            left: position.left + 'px',
            top: position.top + 'px'
        };
        RoomDefined.$data.arrowStyle = {
            left: position.arrowLeft + 'px'
        };


        RoomDefined.$data.currentRoomData.fill = obj.fill;
        RoomDefined.$data.color = obj.fill;
        RoomDefined.$data.colorPickerShow = true;
        RoomDefined.$data.currentRoomType = 'roomFill'
    })

    EventListener.$on('changeStrokeColor', function (arr) {
        var obj = arr[0];
        var position = arr[1];
        RoomDefined.$data.colorStyle = {
            left: position.left + 'px',
            top: position.top + 'px'
        };
        RoomDefined.$data.arrowStyle = {
            left: position.arrowLeft + 'px'
        };
        RoomDefined.$data.currentRoomData.stroke = obj.stroke;
        RoomDefined.$data.color = obj.stroke;
        RoomDefined.$data.colorPickerShow = true;
        RoomDefined.$data.currentRoomType = 'roomStroke'
    })

    EventListener.$on('changeTrimmingBgColor', function (arr) {
        var obj = arr[0];
        var position = arr[1];
        console.log(position)
        RoomDefined.$data.colorStyle = {
            left: position.left + 'px',
            top: position.top + 'px'
        };
        RoomDefined.$data.arrowStyle = {
            left: position.arrowLeft + 'px'
        };

        RoomDefined.$data.currentRoomTrimmingData.fill = obj.fill;
        RoomDefined.$data.color = obj.fill;
        RoomDefined.$data.colorPickerShow = true;
        RoomDefined.$data.currentRoomType = 'trimmingFill'
    })

    EventListener.$on('changeTrimmingStrokeColor', function (arr) {
        var obj = arr[0];
        var position = arr[1];
        RoomDefined.$data.colorStyle = {
            left: position.left + 'px',
            top: position.top + 'px'
        };
        RoomDefined.$data.arrowStyle = {
            left: position.arrowLeft + 'px'
        };
        RoomDefined.$data.currentRoomTrimmingData.stroke = obj.stroke;
        RoomDefined.$data.color = obj.stroke;
        RoomDefined.$data.colorPickerShow = true;
        RoomDefined.$data.currentRoomType = 'trimmingStroke'
    })

    EventListener.$on('changeRoomAssetBgColor', function (arr) {
        var index = arr[0];
        var position = arr[1];
        var obj = arr[2]
        RoomDefined.$data.colorStyle = {
            left: position.left + 'px',
            top: position.top + 'px'
        };
        RoomDefined.$data.arrowStyle = {
            left: position.arrowLeft + 'px'
        };
        RoomDefined.$data.roomAssetIndex = index;
        RoomDefined.$data.currentRoomAssetData[index].fill = obj.fill;
        RoomDefined.$data.color = obj.fill;
        RoomDefined.$data.colorPickerShow = true;
        RoomDefined.$data.currentRoomType = 'roomAssetFill'
    })

    EventListener.$on('changeRoomAssetText', function (arr) {
        var index = arr[0];
        var text = arr[1];
        var len;
        var ele = RoomDefined.$data.currentRoomAssetData[index].ele;
        var eleAttr = ele.attr();
        var position = [eleAttr.x, eleAttr.y];
        text = text.split('\n');
        len = text.length - 1;
        if (!text[len]) {
            text.splice(len, 1)
        }
        RoomDefined.$data.appPaper.changeRoomAssetText(ele, text, position);
        RoomDefined.$data.currentRoomAssetData[index].text = text.join('')
        RoomDefined.$data.roomAssetIndex = index;
        RoomDefined.$data.currentRoomType = ''
    })

    EventListener.$on('changeRoomAssetTextColor', function (arr) {
        var index = arr[0];
        var position = arr[1];
        RoomDefined.$data.colorStyle = {
            left: position.left + 'px',
            top: position.top + 'px'
        };
        RoomDefined.$data.arrowStyle = {
            left: position.arrowLeft + 'px'
        };
        RoomDefined.$data.roomAssetIndex = index;
        RoomDefined.$data.colorPickerShow = true;
        RoomDefined.$data.currentRoomType = 'roomAssetFill'
    })

    EventListener.$on('changeRoomAssetStrokeColor', function (arr) {
        var index = arr[0];
        var position = arr[1];
        var obj = arr[2]
        RoomDefined.$data.colorStyle = {
            left: position.left + 'px',
            top: position.top + 'px'
        };
        RoomDefined.$data.arrowStyle = {
            left: position.arrowLeft + 'px'
        };
        RoomDefined.$data.roomAssetIndex = index;
        RoomDefined.$data.currentRoomAssetData[index].stroke = obj.stroke;
        RoomDefined.$data.color = obj.stroke;
        RoomDefined.$data.colorPickerShow = true;
        RoomDefined.$data.currentRoomType = 'roomAssetStroke'
    })

    EventListener.$on('changeFontColor', function (arr) {
        var obj = arr[0];
        var position = arr[1];
        RoomDefined.$data.colorStyle = {
            left: position.left + 'px',
            top: position.top + 'px'
        };
        RoomDefined.$data.arrowStyle = {
            left: position.arrowLeft + 'px'
        };
        RoomDefined.$data.currentRoomData.children[0].fill = obj.fill;
        RoomDefined.$data.color = obj.fill;
        RoomDefined.$data.colorPickerShow = true;
        RoomDefined.$data.currentRoomType = 'roomChildren0Font'
    })


</script>
</body>

