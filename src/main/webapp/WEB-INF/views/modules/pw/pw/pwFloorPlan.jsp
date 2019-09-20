<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxStatic}/snap/snap.svg-min.js"></script>
    <script src="${ctxJs}/pwFloorPlan/roomShapes.js?v=1"></script>
    <script src="${ctxJs}/pwFloorPlan/roomElements.js"></script>
    <script src="${ctxStatic}/vue/vue.min.js"></script>
    <%--<script src="${ctxJs}/cyjd/designFloor/directives/v-tooltip.js"></script>--%>
    <!--<script src="src/vue/roomDefined/generateBase64.js"></script>-->
    <script src="${ctxJs}/pwFloorPlan/movingElement.js"></script>
    <script src="${ctxJs}/pwFloorPlan/movingRoomShape.js"></script>
    <script src="${ctxJs}/pwFloorPlan/roomElementGroup.js?v=12"></script>
    <script src="${ctxJs}/pwFloorPlan/roomShape.js?v=1"></script>

    <script src="${ctxJs}/cyjd/designFloor/saveSvgAsPng.js"></script>
    <style>
        @font-face {
            font-family: 'iconfont';  /* project id 482029 */
            src: url('//at.alicdn.com/t/font_482029_zrcpe9kb3a98uxr.eot');
            src: url('//at.alicdn.com/t/font_482029_zrcpe9kb3a98uxr.eot?#iefix') format('embedded-opentype'),
            url('//at.alicdn.com/t/font_482029_zrcpe9kb3a98uxr.woff') format('woff'),
            url('//at.alicdn.com/t/font_482029_zrcpe9kb3a98uxr.ttf') format('truetype'),
            url('//at.alicdn.com/t/font_482029_zrcpe9kb3a98uxr.svg#iconfont') format('svg');
        }

        html, body, .rd-app {
            position: relative;
            width: 100%;
            height: 100%;
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        .rd-app {
            overflow: hidden;
        }

        body {
            -webkit-user-select: none;
            -moz-user-select: -moz-none;
            user-select: none;
        }

        .room-translate {
            cursor: move;
        }

        .tooltip-inner {
            padding: 3px;
            font-size: 12px;
        }

        .rd-viewport {
            background-position: left top;
            background-repeat: no-repeat;
        }

        .rd-stencil .rds-content-room .group {
            padding-bottom: 0;
        }

        .floor-copy-box {
            position: absolute;
        }


        .select-color-group_inline{
            display: inline-block;
            margin-top: 9px;
            margin-right: 6px;
            vertical-align: middle;
            text-align: center;
        }
        .room-shape-properties{
            height: 60px;
            overflow: hidden;
        }
        .select-color-group_inline .select-color-label{
            font-size: 12px;
        }

        .select-color-group_inline .select-color{
            margin-right: 0;
        }


    </style>

</head>


<body>
<div id="roomDefined" class="rd-app">
    <div class="rd-header">
        <div v-show="isLoad" class="rd-title rd-title_inline" style="display: none;"><h3>{{floor.name}}</h3></div>
        <room-shape-properties v-model="selectedRoomShapeElement.data" :visible.sync="roomShapeProVisible"
                               @change-color="showSelectColorVisible"
                               @handle-change="changeRoomShapeElementProperty"></room-shape-properties>
        <div class="rd-right">
            <div class="room-shape-properties rsp-background">
                <div class="select-color-group select-color-group_inline">
                    <div class="select-color select-color_inline">
                        <div class="select-color-content" :style="{backgroundColor: floorStyle.backgroundColor}"
                             @click.stop="showFloorBackColor($event)">
                        </div>
                    </div>
                    <div class="select-color-label">背景色</div>
                </div>
                <%--<div class="select-color-group select-color-group_inline">--%>
                    <%--<div class="select-color select-color_inline select-color_image">--%>
                        <%--<div class="select-color-content" :style="{backgroundImage: floorStyle.backgroundImage}"--%>
                             <%--@click.stop="triggerUploadImage">--%>
                            <%--<input type="file" ref="floorBackgroundImage" name="floorBackgroundImage"--%>
                                   <%--class="floorBackgroundImage" @change="uploadFBI($event)"--%>
                                   <%--accept="image/jpeg,image/png,image/jpg">--%>
                        <%--</div>--%>
                        <%--<img class="delete-background_image" @click.stop="handleDeleteBackgroundImage"--%>
                             <%--src="${ctxImg}/btn-hover-delete-file.png">--%>
                    <%--</div>--%>
                    <%--<div class="select-color-label">背景图</div>--%>
                <%--</div>--%>
            </div>
            <div class="btn-group">
                <button type="button" :disabled="isSave" @click.stop="clearPaper" class="btn btn-small btn-default">清空
                </button>
                <button type="button" :disabled="isSave" class="btn btn-small btn-primary" @click.stop="saveFloor">保存
                </button>
                <%--<button type="button" :disabled="isSave" class="btn btn-small btn-primary">提交</button>--%>
                <button type="button" :disabled="isSave" class="btn btn-small btn-default" @click.stop="history.go(-1)">返回</button>
            </div>
        </div>
    </div>
    <div class="rd-body">
        <div class="stencil-container" :class="{closed: isFull}">
            <a href="javascript:void(0);" class="handle-close" @click.stop="isFull = !isFull">
                <img src="${ctxImages}/floor-design-closed.png">
            </a>
            <div class="rd-stencil">
                <div class="rds-content rds-content-room" @click.stop="unSelected">
                    <room-shape title="房间形状" :shapes-xml="shapesXml" @mousedown="handleRoomMousedown"
                                :visible-index="roomShapeVIndex" :index="0" @change-index="handleChangeRoomShapeIndex"
                                @mousemove="handleRoomMousemove" @mouseup="handleRoomMouseup"></room-shape>
                    <room-element-group title="房间基础设施" :room-elements="roomElements" @mousedown="handleElementMousedown"
                                        :visible-index="roomShapeVIndex" :index="1"
                                        @change-index="handleChangeRoomShapeIndex"
                                        @mousemove="handleElementMousemove"
                                        @mouseup="handleElementMouseup"></room-element-group>
                </div>
            </div>
        </div>
        <div class="paper-container" style="right: 0" :class="{full: isFull}" @click.stop="unSelected">
            <div class="paper-scroller" ref="paperScroller" data-cursor="grab" style="background-color: #ddd">
                <div class="paper-scroller-background" :style="paperBackgroundStyle">
                    <div class="rd-viewport" v-viewport="{minPaperSize: minPaperSize}"
                         :style="{width: paperStyle.width+'px', height: paperStyle.height+'px',backgroundColor: floorStyle.backgroundColor, backgroundImage: floorStyle.backgroundImage}">
                        <svg ref="mainPaper" id="mainPaper" v-g-main-paper version="1.1"
                             width="100%" height="100%"
                             xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"></svg>
                        <single-room-element v-model="singleFocus" @copy="handleCopySingle"
                                             @paste="handlePasteSingle"
                                             :pos-style="focusStyle"
                                             @handle-delete="handleDeleteSingle"
                                             @handle-blur="handleBlurSingle"></single-room-element>
                        <multiple-group-room-element v-model="multipleFocus" :m-room-elements="selectedRoomElements"
                                                     :pos-style="focusStyle"
                                                     @copy="handleCopy" @paste="handlePaste"
                                                     @handle-delete="handleDeleteBatches"
                                                     @handle-blur="handleBlur"></multiple-group-room-element>
                        <control-paper-size :visible.sync="controlPaperSizeShow" :custom-class="controlPaperClass"
                                            :rotate="controlPaperSizeRotate"
                                            :custom-style="controlPaperSizeStyle"
                                            :ele="selectedRoomShapeElement.ele"
                                            @mousemove="handleControlSize"
                        ></control-paper-size>
                        <control-paper-tools :visible.sync="controlPaperSizeShow"
                                             :rotate="controlPaperSizeRotate"
                                             :ele="selectedRoomShapeElement.ele"
                                             :custom-style="controlPaperSizeStyle"
                                             @delete="handleDelete"
                                             @handle-rotate="handleRotate"
                                             @up="handleUp"
                                             @down="handleDown"
                        ></control-paper-tools>
                        <control-svg-size :c-style="paperStyle" :min-paper-size="minPaperSize"
                                          @mouseup="handleControlSvgSize"></control-svg-size>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <moving-element :visible.sync="movingElementVisible" :element="movingElement"
                    :move-style="movingElementStyle"></moving-element>
    <moving-room-shape :visible.sync="movingRoomShapeVisible" :move-style="movingElementStyle"
                       :room-shape="movingRoomShape"></moving-room-shape>

    <select-color :visible.sync="selectColorVisible" v-model="selectedColor" :pos="selectColorPos"
                  @change-color="handleChangeColor"></select-color>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>

<script type="text/x-template" id="control-paper-size-template">
    <div v-show="visible" class="control-paper-size" :class="customClass" :style="nCustomStyle">
        <div draggable="false" class="resize nw" data-position="top-left" @click.stop.prevent
             @mousedown.stop.prevent="mousedown('nw',$event)"></div>
        <div v-show="center" draggable="false" class="resize n" data-position="top"></div>
        <div draggable="false" class="resize ne" data-position="top-right" @click.stop.prevent
             @mousedown.stop.prevent="mousedown('ne',$event)"></div>
        <div v-show="center" draggable="false" class="resize e" data-position="right"></div>
        <div draggable="false" class="resize se" data-position="bottom-right" @click.stop.prevent
             @mousedown.stop.prevent="mousedown('se', $event)"></div>
        <div v-show="center" draggable="false" class="resize s" data-position="bottom"></div>
        <div draggable="false" class="resize sw" data-position="bottom-left" @click.stop.prevent
             @mousedown.stop.prevent="mousedown('sw',$event)"></div>
        <div v-show="center" draggable="false" class="resize w" data-position="left"></div>
    </div>
</script>

<script type="text/x-template" id="control-paper-tools-template">
    <div v-show="visible" class="control-paper-tools" :class="customClass" :style="nCustomStyle">
        <div class="control-paper-handles">
            <div data-action="rotate" draggable="false" class="handle rotate ne" @click.stop.prevent="handleRotate"
                 v-tooltip data-toggle="tooltip"
                 data-placement="bottom" data-original-title="旋转">
                <img src="${ctxImages}/floor-design-rotate.png">
            </div>
            <div data-action="delete" draggable="false" class="handle rotate se" @click.stop.prevent="handleDelete"
                 v-tooltip data-toggle="tooltip"
                 data-placement="bottom" data-original-title="删除">
                <img src="${ctxImages}/floor-design-delete.png">
            </div>
            <div data-action="up" draggable="false" class="handle rotate nw" @click.stop.prevent="handleUp" v-tooltip
                 data-toggle="tooltip"
                 data-placement="bottom" data-original-title="上移一层">
                <img src="${ctxImages}/floor-design-up.png">
            </div>
            <div data-action="down" draggable="false" class="handle rotate sw" @click.stop.prevent="handleDown"
                 v-tooltip data-toggle="tooltip"
                 data-placement="bottom" data-original-title="下移一层">
                <img src="${ctxImages}/floor-design-down.png">
            </div>
        </div>
    </div>
</script>

<script type="text/x-template" id="single-room-element-template">
    <div style="width: 0; height: 0; overflow: hidden" class="floor-copy-box" :style="posStyle">
        <input type="text"  style="width: 0; height: 0; padding: 0;border: none" name="selected_input" readonly
                ref="selected_input"
                @keydown.ctrl.67.stop="handleCtrlC"
                @keyup.ctrl.86.stop="handleCtrlV"
                @keyup.delete.stop="handleDelete"
                @blur="handleBlur">
    </div>
</script>

<script type="text/x-template" id="room-shape-properties-template">
    <div v-show="visible" class="room-shape-properties" :class="[customClass]">
        <div class="rsp-text-box">
            <span class="rsp-text-label">文字</span>
            <div class="rsp-text-controls">
                <div class="rsp-text-control">
                    <textarea rows="2" placeholder="房间名" class="input-small" v-model="rsData.textText"
                              :style="{color: rsData.textFill}"
                              @change.stop="handleChange('textText')"></textarea>
                    <div class="select-color select-color_text" v-tooltip data-toggle="tooltip" data-placement="bottom"
                         data-original-title="字体颜色">
                        <div class="select-color-content" @click.stop="changeColor('textFill', $event)"
                             :style="{background: rsData.textFill}">
                            <img class="select-image">
                        </div>
                    </div>
                </div>
                <div class="rsp-text-control">
                    <select class="input-small" v-model="rsData.textFontSize" v-tooltip data-toggle="tooltip"
                            data-placement="bottom" data-original-title="字体大小"
                            @change.stop="handleChange('textFontSize')">
                        <option v-for="fontSize in fontSizes" :value="fontSize.value">{{fontSize.label}}</option>
                    </select>
                    <select class="input-small input-block-level" v-model="rsData.textFontFamily" v-tooltip
                            data-toggle="tooltip" data-placement="bottom" data-original-title="字体"
                            @change.stop="handleChange('textFontFamily')">
                        <option v-for="fontFamily in fontFamilies" :value="fontFamily.value">{{fontFamily.label}}
                        </option>
                    </select>
                </div>
            </div>
        </div>
        <div class="rsp-text-box">
            <span class="rsp-text-label">房间</span>
            <div class="rsp-text-controls">
                <div class="rsp-text-control rsp-select-color">
                    <select class="input-small" v-model="rsData.shapeStrokeWidth"
                            v-tooltip data-toggle="tooltip"
                            data-placement="bottom" data-original-title="房间边框"
                            @change.stop="handleChange('shapeStrokeWidth')">
                        <option v-for="border in borders" :value="border.value">{{border.label}}</option>
                    </select>
                    <div class="select-color-group">
                        <div class="select-color select-color_inline">
                            <!--房间背景色-->
                            <div class="select-color-content" @click.stop="changeColor('shapeFill', $event)"
                                 :style="{background: rsData.shapeFill}" v-tooltip data-toggle="tooltip"
                                 data-placement="bottom" data-original-title="房间背景">
                                <img class="select-image">
                            </div>
                        </div>
                        <div class="select-color select-color_inline">
                            <div class="select-color-content" @click.stop="changeColor('shapeStroke', $event)"
                                 :style="{background: rsData.shapeStroke}" v-tooltip data-toggle="tooltip"
                                 data-placement="bottom" data-original-title="房间边框">
                                <img class="select-image">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>

<script type="text/x-template" id="multiple-group-room-element-template">
    <div>
        <div class="multiple-group_room_element" style="width: 0; height: 0;overflow: hidden;border: none"
             class="floor-copy-box" :style="posStyle">
            <input ref="multiple-group_input" name="multiple-group_input"
                   style="width: 0;height: 0;padding: 0; border: none" readonly type="text"
                   @keydown.ctrl.67.stop="handleCtrlC" @keyup.ctrl.86.stop="handleCtrlV" @keyup.delete.stop="handleDelete"
                   @blur="handleBlur">

        </div>
        <div class="mg_room_element-item" v-for="mre in mRoomElements" :style="mre.style" :key="mre.id"></div>
    </div>
</script>

<script type="text/x-template" id="select-color-template">
    <div v-show="visible" class="select-color-dropdown" :class="[customClass]" :style="nStyle">
        <div class="select-color-group">
            <div v-for="colorItem in nColors" class="select-color" :class="{active: colorItem === color}"
                 @click.stop="changeColor(colorItem)">
                <div class="select-color-content" :style="{backgroundColor: colorItem}"></div>
            </div>
        </div>
        <div class="arrow"></div>
    </div>
</script>

<script type="text/x-template" id="control-paper-svg-template">
    <div class="control-svg_size-handle">
        <div class="control-svg-handle" @mousedown.stop="mousedown($event)"></div>
        <div class="svg-size" v-show="visible" :style="mStyle"></div>
    </div>
</script>

<script>


    +function ($, Vue) {

        Vue.directive('tooltip', {
            inserted: function (element) {
                $(element).tooltip({
                    container: 'body'
                })
            }
        });

        var controlSvgSize = Vue.component('control-svg-size', {
            template: '#control-paper-svg-template',
            props: {
                cStyle: {
                    type: Object,
                    default: {
                        width: 0,
                        height: 0
                    }
                },
                minPaperSize: {
                    type: Object,
                    default: {
                        width: '',
                        height: ''
                    }
                }
            },
            computed: {
                mStyle: {
                    get: function () {
                        var nStyle = this.nStyle;
                        return {
                            width: nStyle.width + 'px',
                            height: nStyle.height + 'px'
                        }
                    }
                }
            },
            data: function () {
                return {
                    visible: false,
                    nStyle: {
                        width: '',
                        height: ''
                    }
                }
            },
            methods: {
                mousedown: function ($event) {
                    var startX = $event.clientX;
                    var startY = $event.clientY;
                    var minWidth = this.minPaperSize.width;
                    var minHeight = this.minPaperSize.height;
                    var self = this;
                    this.visible = true;
                    $(document).on('mousemove.c.svg', function (ev) {
                        ev.stopPropagation();
                        var dx = ev.clientX - startX;
                        var dy = ev.clientY - startY;
                        self.nStyle.width = Math.max(self.cStyle.width + dx, minWidth);
                        self.nStyle.height = Math.max(self.cStyle.height + dy, minHeight);
                    });
                    $(document).on('mouseup.c.svg', function (ev) {
                        ev.stopPropagation();
                        $(document).off('mousemove.c.svg');
                        $(document).off('mouseup.c.svg');
                        self.visible = false;
                        self.$emit('mouseup', self.nStyle)

                    })
                }
            },
            mounted: function () {
                var cStyle = this.cStyle;
                this.nStyle = {
                    width: cStyle.width + 'px',
                    height: cStyle.height + 'px'
                }
            }
        })

        var selectColor = Vue.component('select-color', {
            template: '#select-color-template',
            model: {
                prop: 'color',
                event: 'change'
            },
            props: {
                color: {
                    type: String
                },
                customClass: String,
                visible: {
                    type: Boolean,
                    default: false
                },
                colors: {
                    type: Array,
                    default: function () {
                        return []
                    }
                },
                pos: {
                    type: Object,
                    default: function () {
                        return {
                            left: '',
                            top: ''
                        }
                    }
                }
            },
            computed: {
                rgbColor: {
                    get: function () {

                    }
                },
                nColors: {
                    get: function () {
                        [].push.apply(this.colors, this.dColors);
                        return this.colors;
                    }
                },
                nStyle: {
                    get: function () {
                        var pos = this.pos;
                        return {
                            left: pos.left + 'px',
                            top: pos.top + 'px'
                        }
                    }
                }
            },
            data: function () {
                return {
                    dColors: ['#ffffff', '#e9442d', '#dcd7d7', '#8f8f8f', '#c6c7e2', '#feb663', '#b75d32', '#31d0c6', '#7c68fc', '#61549c', '#6a6c8a', '#4b4a67', '#3c4260', '#222138', '#333333']
                }
            },
            methods: {
                changeColor: function (color) {
                    this.$emit('change', color);
                    this.$emit('change-color', color);
                    this.$emit('update:visible', false)
                }
            },
            mounted: function () {
                var self = this;
                $(document).on('click.hide', function (event) {
                    event.stopPropagation();
                    var $parent = $(event.target);
                    var isClickableArea = false;
                    if ($parent.hasClass('select-color-content')) {
                        return false;
                    }
                    while ($parent.size() > 0) {
                        if ($parent.hasClass('select-color-dropdown')) {
                            isClickableArea = true;
                            return
                        }
                        $parent = $parent.parent();
                    }
                    if (!isClickableArea) {
                        self.$emit('update:visible', false);
                    }

                })
            }
        })

        var controlPaperTools = Vue.component('control-paper-tools', {
            template: '#control-paper-tools-template',
            props: {
                visible: Boolean,
                ele: {},
                customClass: Object,
                customStyle: Object,
                rotate: {
                    type: Number,
                    default: 0
                },
            },
            computed: {
                nCustomStyle: {
                    get: function () {
                        var customStyle = this.customStyle;
                        return {
                            left: customStyle.left + 'px',
                            top: customStyle.top + 'px',
                            width: customStyle.width + 'px',
                            height: customStyle.height + 'px'
                        }
                    }
                }
            },
            methods: {
                handleRotate: function () {
                    var ele = this.ele;
                    var BBox = ele.getBBox();
                    var width = BBox.width;
                    var height = BBox.height;
                    this.$emit('handle-rotate', {
                        ele: ele,
                        x: width / 2,
                        y: height / 2
                    })
                },
                handleDelete: function () {
                    this.$emit('delete', this.ele)
                },
                handleUp: function () {
                    this.$emit('up', this.ele);
                },
                handleDown: function () {
                    this.$emit('down', this.ele);
                }
            }
        })

        var singleRoomElement = Vue.component('single-room-element', {
            template: '#single-room-element-template',
            model: {
                prop: 'isFocus',
                event: 'change'
            },
            props: {
                isFocus: {
                    type: Boolean,
                    default: false
                },
                posStyle: {
                    type: Object,
                    default: function () {
                        return {
                            left: '',
                            top: ''
                        }
                    }
                },
            },
            watch: {
                isFocus: function (value) {
                    if (value) {
                        this.$refs['selected_input'].focus();
                    }
                }
            },
            data: function () {
                return {
                    posStyle: {
                        left: '',
                        top: ''
                    }
                }
            },
            methods: {
                handleCtrlC: function () {
                    this.$emit('copy')
                },
                handleCtrlV: function () {
                    this.$emit('paste')
                },
                handleBlur: function () {
                    this.$emit('handle-blur');
                    this.$emit('change', false);
                },
                handleDelete: function () {
                    this.$emit('handle-delete')
                }
            },
            mounted: function () {
                var self = this;
//                $(window).on('scroll', function (ev) {
//                    self.posStyle.left = $(window).scrollLeft() + 'px';
//                    self.posStyle.top = $(window).scrollTop() + 'px';
//                })
//                $(document).on('keydown.ctrl.67.single', function (event) {
//                    if(event.ctrlKey && event.which === 67){
//                        self.handleCtrlC();
//                    }
//                });
//                $(document).on('keyup.ctrl.86.single', function (event) {
//                    if(event.ctrlKey && event.which === 86){
//                        self.handleCtrlV();
//                    }
//                })
//                $(document).on('keyup.delete.46.single', function (event) {
//                    if(event.which === 46){
//                        self.handleDelete();
//                    }
//                })
            }
        })

        var controlPaperSize = Vue.component('control-paper-size', {
            template: '#control-paper-size-template',
            props: {
                roomPos: Object,
                center: {
                    type: Boolean,
                    default: false
                },
                visible: Boolean,
                customClass: Object,
                customStyle: Object,
                rotate: {
                    type: Number,
                    default: 0
                },
                ele: {}
            },
            computed: {
                nCustomStyle: {
                    get: function () {
                        var customStyle = this.customStyle;
                        return {
                            left: customStyle.left + 'px',
                            top: customStyle.top + 'px',
                            width: customStyle.width + 'px',
                            height: customStyle.height + 'px'
                        }
                    }
                }
            },
            data: function () {
                return {
                    startDetail: {}
                }
            },
            methods: {
                mousedown: function (direction, ev) {
                    var startX, startY, width, height;
                    var customStyle = this.customStyle;
                    var ele = this.ele;
                    var scaleGroup = ele.select('g.room-scale') || ele.select('g.room-element-rotate');
                    var scaleGroupBBox = scaleGroup.getBBox();
                    var rotateGroup = ele.select('g.room-rotate');
                    var rotateGroupMatrix = rotateGroup.matrix, rotate;
                    var scaleMatrix = scaleGroup.matrix, scaleX, scaleY;
                    var eleMatrix = ele.matrix;
                    if (!scaleMatrix) {
                        scaleGroup.attr('transform', scaleGroup.attr('transform'))
                    }

                    if (!rotateGroupMatrix) {
                        var rotateGroupTransform = rotateGroup.attr('transform');
                        if ({}.toString.call(rotateGroupTransform) === "[object Object]") {
                            rotateGroup.attr('transform', rotateGroupTransform.localMatrix.toTransformString())
                        } else {
                            rotateGroup.attr('transform', rotateGroup.attr('transform'))
                        }

                    }
                    rotateGroupMatrix = rotateGroup.matrix;
                    rotate = rotateGroupMatrix.split().rotate;
                    scaleMatrix = scaleGroup.matrix;
                    scaleX = scaleMatrix.split().scalex;
                    scaleY = scaleMatrix.split().scaley;
                    startX = ev.clientX;
                    startY = ev.clientY;
                    width = scaleGroupBBox.width - 2;
                    height = scaleGroupBBox.height - 2;
                    this.startDetail = {
                        direction: direction,
                        width: width,
                        height: height,
                        left: customStyle.left + 1,
                        top: customStyle.top + 1,
                        startX: startX,
                        startY: startY,
                        scaleX: scaleX,
                        scaleY: scaleY,
                        matrix: scaleMatrix,
                        rotate: rotate,
                        eleMatrixE: eleMatrix.e,
                        eleMatrixF: eleMatrix.f,
                        eleMatrix: eleMatrix.clone(),
                        originWidth: width / scaleX,
                        originHeight: height / scaleY,
                        scaleGroup: scaleGroup
                    };
                    $(document).on('mousemove.ls', this.mousemove);
                    $(document).on('mouseup.ls', this.mouseup)
                },
                mousemove: function (ev) {
                    ev.stopPropagation();
                    var dx, dy, width, height;
                    var startDetail = this.startDetail;
                    var clientX = ev.clientX;
                    var clientY = ev.clientY;
                    dx = clientX - startDetail.startX;
                    dy = clientY - startDetail.startY;
                    this.$emit('mousemove', {
                        dx: dx,
                        dy: dy,
                        startDetail: startDetail
                    })
                },
                mouseup: function (ev) {
                    ev.stopPropagation();
                    $(document).off('mousemove.ls')
                    $(document).off('mouseup.ls')
                }
            }

        })


        var roomShapeProperties = Vue.component('room-shape-properties', {
            template: '#room-shape-properties-template',
            model: {
                prop: 'rsData',
                event: 'change'
            },
            props: {
                rsData: {
                    type: Object,
                    default: function () {
                        return {}
                    }
                },
                customClass: String,
                visible: {
                    type: Boolean,
                    default: false
                }
            },
            data: function () {
                return {
                    fontSizes: [{
                        value: 12,
                        label: '12px'
                    }, {
                        value: 14,
                        label: '14px'
                    }],
                    fontFamilies: [{
                        value: '"Helvetica Neue"',
                        label: '苹果体'
                    }, {
                        value: '"Microsoft YaHei"',
                        label: '微软雅黑'
                    }],
                    borders: [{
                        value: 1,
                        label: '1px'
                    }, {
                        value: 2,
                        label: '2px'
                    }]
                }
            },
            methods: {
                handleChange: function (key) {
                    this.$emit('handle-change', {key: key, value: this.rsData[key]});
//                    this.$emit('change');
                },
                changeColor: function (key, $event) {
                    this.$emit('change-color', {key: key, ev: $event, value: this.rsData[key]})
                }
            }
        })

        var multipleGroupRoomElement = Vue.component('multiple-group-room-element', {
            template: '#multiple-group-room-element-template',
            model: {
                prop: 'isFocus',
                event: 'change'
            },
            props: {
                posStyle: {
                    type: Object,
                    default: function () {
                        return {
                            left: '',
                            top: ''
                        }
                    }
                },
                isFocus: {
                    type: Boolean,
                    default: false
                },
                mRoomElements: Array
            },
            watch: {
                isFocus: function (value) {
                    if (value) {
                        this.$refs['multiple-group_input'].focus();
                    }
                }
            },
            methods: {
                handleCtrlC: function () {
                    this.$emit('copy')

                },
                handleCtrlV: function () {
                    this.$emit('paste')
                },
                handleBlur: function () {
                    this.$emit('handle-blur');
                    this.$emit('change', false);
                },
                handleDelete: function () {
                    this.$emit('handle-delete');
                }
            },
            mounted: function () {
                var self = this;

//                $(window).on('scroll', function (ev) {
//                    self.posStyle.left = $(window).scrollLeft() + 'px';
//                    self.posStyle.top = $(window).scrollTop() + 'px';
//                })
//                $(document).on('keydown.ctrl.67.multiple', function (event) {
//                    if(event.ctrlKey && event.which === 67){
//                        self.handleCtrlC();
//                    }
//                });
//                $(document).on('keyup.ctrl.86.multiple', function (event) {
//                    if(event.ctrlKey && event.which === 86){
//                        self.handleCtrlV();
//                    }
//                })
//                $(document).on('keyup.delete.46.multiple', function (event) {
//                    if(event.which === 46){
//                        self.handleDelete();
//                    }
//                })
            }
        })


        var roomDefined = new Vue({
            el: '#roomDefined',
            data: function () {
                return {
                    movingElementVisible: false,
                    movingElement: {},
                    movingElementStyle: {
                        left: '',
                        top: ''
                    },
                    roomElements: ROOMELEMENTS,
                    shapesXml: ROOMSHAPES,
                    movingRoomShape: {},
                    movingRoomShapeVisible: false,

                    id: '${pdCanvasId}',
//                    floorData
                    floor: {
                        id: '${floorId}',
                        name: '${floorName}',
                        backgroundColor: '#ffffff',
                        backgroundImage: ''
                    },

                    isFull: false,
                    //room ownProperties
                    snap: '',
                    allScaleGroup: '',
                    roomShapeElementsGroup: '',
                    selectedRoomShapeElement: {
                        ele: {},
                        data: {}
                    },
                    //元素属性修改显示修改的开关
                    roomShapeProVisible: false,
                    paperConOffset: {},
                    paperStyle: {
                        width: '',
                        height: ''
                    },
                    minPaperSize: {
                        width: 1000,
                        height: 500
                    },
                    isCoping: false,
                    singleCoping: false,
                    roomElementDefaultWidth: 38,
                    //默认最小画布宽高
                    viewportMin: {},
                    copyRoomElements: [],
                    selectedRoomElements: [],
                    selectedRESStartPos: {},
                    multipleFocus: false,
                    isMultipleSelected: false,
                    //controlPaperSize
                    singleFocus: false,
                    controlPaperSizeShow: false,
                    controlPaperSizeStyle: {
                        left: '',
                        top: '',
                        width: '',
                        height: ''
                    },
                    controlPaperClass: {
                        rotate90: false,
                        rotate180: false,
                        rotate270: false
                    },
                    controlPaperSizeRotate: 0,

                    //select-color
                    isFloorBackgroundColor: false,
                    selectColorVisible: false,
                    selectedColor: '',
                    selectColorPos: {
                        left: '',
                        top: ''
                    },
                    selectColorKey: '',
                    fileObj: '',
                    linkPath: '/images/trimming/',
                    isSave: false,
                    isLoad: false,
                    roomShapeVIndex: 0,
                    focusStyle: {
                        left: '0px',
                        top: '0px'
                    }
                }
            },
            computed: {
                paperBackgroundStyle: {
                    get: function () {
                        var paperStyle = this.paperStyle;
                        return {
                            width: parseInt(paperStyle.width) + 40 + 'px',
                            height: parseInt(paperStyle.height) + 30 + 'px'
                        }
                    },
                    set: function () {

                    }
                },
                floorStyle: {
                    get: function () {
                        var floor = this.floor;
                        return {
                            backgroundColor: floor.backgroundColor,
                            backgroundImage: floor.backgroundImage ? ('url(' + ftpHttp + floor.backgroundImage.replace('tool/', '') + ')') : ''
                        }
                    },
                    set: function () {

                    }
                }
            },
            directives: {
                gMainPaper: {
                    inserted: function (element, binding, vnode) {
                        var snap = Snap(element);
                        var group = snap.group().addClass('room-container');
                        vnode.context.snap = snap;
                        vnode.context.paperConOffset = $(element).parents('.paper-container').offset();

                    }
                },
                viewport: {
                    inserted: function (element, binding, vnode) {
                        var width, height, $paperScroller, paperWidth, paperHeight;
                        var minPaperSize = binding.value.minPaperSize;
                        var minWidth = minPaperSize.width;
                        var minHeight = minPaperSize.height;
                        if (vnode.context.paperStyle.width) {
                            return false;
                        }
                        $paperScroller = $(element).parents('.paper-scroller');
                        paperHeight = $paperScroller.height();
                        paperWidth = $paperScroller.width();
                        paperWidth = Math.max(minWidth, paperWidth);
                        paperHeight = Math.max(minHeight, paperHeight);
                        width = paperWidth - 40;
                        height = paperHeight - 40;
                        vnode.context.paperStyle.width = width;
                        vnode.context.paperStyle.height = height;
                        console.log(vnode.context.paperStyle, paperHeight, width, height)
                        vnode.context.viewportMin = {
                            width: width,
                            height: height
                        }
                    }
                }
            },
            methods: {
                //utils
                isMainPaper: function (target) {
                    var $parent = $(target);
                    while ($parent.size() > 0) {
                        if ($parent.attr('id') === 'mainPaper') {
                            return true;
                        }
                        $parent = $parent.parent();
                        if ($parent.is('body')) {
                            return false;
                        }
                    }
                    return false;
                },
                handleChangeRoomShapeIndex: function (obj) {
                    var visbleIndex = obj.visbleIndex;
                    var index = obj.index;
                    if (visbleIndex !== index) {
                        this.roomShapeVIndex = index;
                    }
                },
                //获取添加到画布位置
                getToPaperPos: function () {
                    var paperConOffset = this.paperConOffset;
                    var movingElementStyle = this.movingElementStyle;
                    var $paperScroller = $(this.$refs.paperScroller);
                    var scrollTop = $paperScroller.scrollTop();
                    var scrollLeft = $paperScroller.scrollLeft();
                    return {
                        x: parseInt(movingElementStyle.left) - paperConOffset.left + scrollLeft,
                        y: parseInt(movingElementStyle.top) - paperConOffset.top + scrollTop
                    }
                },

                hasMatrix: function (group) {
                    return group.matrix;
                },

                addMatrix: function (group) {
                    var matrix = group.attr('transform'), transformString;
                    if (matrix) {
                        if ({}.toString.call(matrix) === "[object Object]") {
                            group.attr('transform', matrix.localMatrix.toTransformString());
                        } else {
                            group.attr('transform', matrix);
                        }
                        return group;
                    }
                    matrix = new Snap.Matrix();
                    transformString = matrix.toTransformString();
                    group.attr('transform', transformString);
                    return group;
                },

                //随机Id
                getRandomId: function () {
                    return 'osEasy|' + new Date().getTime() + '|' + Math.floor(Math.random() * 10000);
                },

                /*
                 @return min top min left
                 * */
                getLimitMin: function () {
                    return {
                        left: 1,
                        top: 1
                    }
                },
                /*
                 @return Bool
                 * */
                isOverLimitMax: function () {

                },

                uriToBlob: function (uri) {
                    var byteString = window.atob(uri.split(',')[1]);
                    var mimeString = uri.split(',')[0].split(':')[1].split(';')[0];
                    var buffer = new ArrayBuffer(byteString.length);
                    var intArray = new Uint8Array(buffer);
                    for (var i = 0; i < byteString.length; i++) {
                        intArray[i] = byteString.charCodeAt(i);
                    }
                    return new Blob([buffer], {type: mimeString});
                },
                //获取outerSvg；
                getOuterSvg: function () {
                    var allScaleGroupClone = this.allScaleGroup.clone();
                    var elementGroups = allScaleGroupClone.selectAll('g[model-type="element"]');
                    var linkPath = this.linkPath;
                    var svgHtml;
                    elementGroups.forEach(function (t) {
                        var link = t.attr('model-link');
                        var image = t.select('image');
                        if (image) {
                            image.attr('href', linkPath + link + '.png');
                        }
                    });
                    svgHtml = allScaleGroupClone.outerSVG();
                    allScaleGroupClone.remove();
                    return svgHtml
                },

                //获取postData
                getPostData: function () {
                    var svgHtml, width, height, backgroundColor, backgroundImage, floorId, id;
                    svgHtml = this.getOuterSvg();
                    width = this.paperStyle.width;
                    height = this.paperStyle.height;
                    backgroundColor = this.floor.backgroundColor;
                    backgroundImage = this.floor.backgroundImage;
                    floorId = this.floor.id;
                    id = this.id;
                    return {
                        width: width,
                        id: id,
                        height: height,
                        backgroundColor: backgroundColor,
                        backgroundImage: backgroundImage,
                        floorId: floorId,
                        picUrl: '',
                        svgHtml: svgHtml
                    }
                },

                //保存数据
                saveFloorData: function (xhr) {
                    var self = this;
                    xhr.done(function (data) {
                        if (data.state === 'SUCCESS') {
                            var saveFloorXhr;
                            var postData = self.getPostData();
                            postData.picUrl = data.ftpUrl;
                            saveFloorXhr = $.ajax({
                                type: 'POST',
                                url: '${ctx}/pw/pwDesignerCanvas/ajaxSave',
                                data: JSON.stringify(postData),
                                contentType: 'application/json; charset=utf-8',
                                dataType: 'JSON',
                                beforeSend: function () {
                                }
                            });
                            saveFloorXhr.done(function (data, textStatus, jqXHR) {
                                dialogCyjd.createDialog(data.status ? '1' : '0', data.msg);
                                if (data.status) {
                                    self.id = data.datas.id;
                                }
                            });
                            saveFloorXhr.fail(function (jqXHR, textStatus, errorThrown) {
                                dialogCyjd.createDialog('0', '网络连接失败,错误代码' + jqXHR.status)
                            });
                            saveFloorXhr.always(function (data, textStatus) {
                                self.isSave = false;
                            })
                        } else {
                            dialogCyjd.createDialog('0', '生成图片上传失败')
                            self.isSave = false;
                        }
                    }).fail(function (jqXHR, textStatus, errorThrown) {
                        dialogCyjd.createDialog('0', '网络连接失败,错误代码' + jqXHR.status)
                        self.isSave = false;
                    });
                },

                //saveFloor 保存
                saveFloor: function () {
                    var formData, blob, name, uploadGeneratePicXhr;
                    var self = this;
                    name = this.floor.name;
                    formData = new FormData();
                    svgAsPngUri(document.getElementById('mainPaper'), {}, function (url) {
                        //上传文件之后
                        try {
                            blob = self.uriToBlob(url);
                        } catch (e) {
                            console.log('浏览器不支持，blob对象')
                        }
                        if (!blob) {
                            return false;
                        }
                        self.isSave = true;
                        formData.append('upfile', blob);
                        formData.append('filename', name + '.png');
                        uploadGeneratePicXhr = $.ajax({
                            url: '/a/ftp/ueditorUpload/normal?folder=temp/floorDesigner',
                            type: 'post',
                            processData: false,
                            contentType: false,
                            data: formData,
                            dataType: 'json'
                        });
                        self.saveFloorData(uploadGeneratePicXhr)
                    });

                },

                //初始化楼层
                initializeAllScaleGroup: function () {
                    var snap, allScaleGroup, roomShapeElementsGroup;
                    snap = this.snap;
                    allScaleGroup = snap.select('g.all-scale');
                    if (!allScaleGroup) {
                        var backgroundTranslate = snap.group().addClass('background-translate');
                        var backgroundRotate = snap.group().addClass('background-rotate');
                        var backgroundScale = snap.group().addClass('background-scale');
                        var backgroundImage = snap.image().addClass('background-image');
                        allScaleGroup = snap.group().addClass('all-scale');
                        roomShapeElementsGroup = snap.group().addClass('room-shape-elements');
                        allScaleGroup.add(backgroundTranslate);
                        allScaleGroup.add(roomShapeElementsGroup);
                        backgroundTranslate.add(backgroundRotate);
                        backgroundRotate.add(backgroundScale);
                        backgroundScale.add(backgroundImage);
                    } else {
                        roomShapeElementsGroup = snap.select('.room-shape-elements');
                    }
                    snap.select('g.room-container').add(allScaleGroup);
                    this.roomShapeElementsGroup = roomShapeElementsGroup;
                    this.allScaleGroup = allScaleGroup;
                },


                /*change room shape property 改变元素的属性*/
                changeRoomShapeElementProperty: function (pro) {

                    var ele = this.selectedRoomShapeElement.ele;
                    var key = pro.key;
                    var text = ele.select('text'), roomShape = ele.select('.room-shape');
                    var attrKey;
                    if (/text/.test(key)) {
                        if (key === 'textText') {
                            text.node.innerHTML = pro.value;
                        } else {
                            attrKey = key.replace(/^text([A-Z]){1}/, function ($1, $2) {
                                return $2.toLowerCase()
                            });
                            text.attr(attrKey, pro.value)
                        }
                    } else {
                        attrKey = key.replace(/^shape([A-Z]){1}/, function ($1, $2) {
                            return $2.toLowerCase()
                        });
                        roomShape.attr(attrKey, pro.value)
                    }
                },


                triggerUploadImage: function () {
                    this.$refs.floorBackgroundImage.click();
                },

                uploadFBI: function (ev) {
                    var files = ev.target.files;
                    var file, self = this;
                    var formData = new FormData();
                    if (!files && files.length < 0) {
                        return false;
                    }
                    file = files[0];
                    formData.append('upfile', file);
                    $.ajax({
                        url: '/a/ftp/ueditorUpload/normal?folder=temp/floorDesigner',
                        type: 'post',
                        processData: false,
                        contentType: false,
                        data: formData,
                        dataType: 'json',
                        success: function (data) {
                            ev.target.files = null;
                            if (data.state === 'SUCCESS') {
                                self.floor.backgroundImage = data.ftpUrl;
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            ev.target.files = null;
                        }
                    })
                },

                //拖动设置背景大小
                handleControlSvgSize: function (obj) {
                    this.paperStyle.width = obj.width;
                    this.paperStyle.height = obj.height;
                },

                //删除背景图像
                handleDeleteBackgroundImage: function () {
                    this.floor.backgroundImage = '';
                },
                //弹出背景背景色
                showFloorBackColor: function (ev) {
                    var $target, offset, height, color;
                    color = this.floor.backgroundColor;
                    $target = $(ev.target);
                    height = $target.height();
                    offset = $target.offset();
                    this.selectColorPos.left = offset.left;
                    this.selectColorPos.top = offset.top + height;
                    this.selectedColor = color;
                    this.selectColorVisible = true;
                    this.isFloorBackgroundColor = true;
                },

                //弹出选择颜色框
                showSelectColorVisible: function (obj) {
                    var k, ev, $target, offset, height, color;
                    k = obj.key;
                    ev = obj.ev;
                    color = obj.value;
                    $target = $(ev.target);
                    height = $target.height();
                    offset = $target.offset();
                    this.selectColorPos.left = offset.left;
                    this.selectColorPos.top = offset.top + height;
                    this.selectColorKey = k;
                    this.selectedColor = color;
                    this.selectColorVisible = true;
                    this.isFloorBackgroundColor = false;
//                    this.selectedColor =
                },

                //选择颜色
                handleChangeColor: function (color) {
                    var k = this.selectColorKey;
                    if (!this.isFloorBackgroundColor) {
                        this.changeRoomShapeElementProperty({
                            key: k,
                            value: color
                        });
                        //修改当前的数据颜色
                        this.selectedRoomShapeElement.data[k] = color;
                    } else {
                        //修改背景颜色
//                        this.snap.attr('fill', color);
                        this.floor.backgroundColor = color;
                    }

                },

                //获取元素的属性
                getRoomShapeElementProperties: function (roomShapeElement) {
                    var roomShape, roomShapeAttr, text, textAttr;
                    if (roomShapeElement.attr('model-type') !== 'shape') {
                        return {
                            ele: roomShapeElement,
                            data: {}
                        };
                    }
                    roomShape = roomShapeElement.select('.room-shape');
                    roomShapeAttr = roomShape.attr();
                    text = roomShapeElement.select('text');
                    textAttr = text.attr();
                    return {
                        ele: roomShapeElement,
                        data: {
                            shapeStrokeWidth: roomShape.attr('strokeWidth').replace('px', '') * 1,
                            shapeStroke: roomShapeAttr.stroke,
                            shapeFill: roomShapeAttr.fill,
                            textFontFamily: text.attr('fontFamily'),
                            textFill: textAttr.fill,
                            textFontSize: text.attr('fontSize').replace('px', '') * 1,
                            textText: text.innerSVG()
                        }
                    }

                },

                //设置元素的属性
                setSelectedRoomShapeElementData: function (value) {
                    this.selectedRoomShapeElement.ele = value.ele;
                    Vue.set(this.selectedRoomShapeElement, 'data', value.data);
                },


                setControlPaperSizeShow: function (value) {
                    return this.controlPaperSizeShow = value;
                },

                getControlPaperSizeStyle: function (roomShapeElement) {
                    var BBox = roomShapeElement.getBBox();
                    return {
                        left: BBox.x - 1,
                        top: BBox.y - 1,
                        width: BBox.width + 2,
                        height: BBox.height + 2
                    }
                },

                setControlPaperSizeStyle: function (value) {
                    return this.controlPaperSizeStyle = value;
                },

                setRoomShapeElementStyle: function (moveDetail, selectedRoomShapeElementEle) {
                    var ele = selectedRoomShapeElementEle;
                    var startDetail = moveDetail.startDetail;
                    var rotate = startDetail.rotate;
                    var matrix, eleMatrix, matrixs;
                    var scaleGroup = startDetail.scaleGroup;
                    switch (rotate) {
                        case 0:
                            matrixs = this.scaleRotateAngleZero(moveDetail);
                            break;
                        case 90:
                            matrixs = this.scaleRotateAngleNinety(moveDetail);
                            break;
                        case 180:
                            matrixs = this.scaleRotateAngleNinetyX2(moveDetail);
                            break;
                        case -90:
                            matrixs = this.scaleRotateAngleFNinety(moveDetail);
                            break;

                    }

                    eleMatrix = matrixs.eleMatrix;
                    matrix = matrixs.matrix;
                    scaleGroup.attr('transform', matrix.toTransformString());
                    ele.attr('transform', eleMatrix.toTransformString());

                },
                //0
                scaleRotateAngleZero: function (moveDetail) {
                    var dx = moveDetail.dx;
                    var dy = moveDetail.dy;
                    var startDetail = moveDetail.startDetail;
                    var eleMatrix = startDetail.eleMatrix;
                    var originWidth = startDetail.originWidth;
                    var originHeight = startDetail.originHeight;
                    var scaleGroup = startDetail.scaleGroup;
                    var matrix = scaleGroup.matrix;
                    var direction = startDetail.direction;
                    var eleMatrixE = startDetail.eleMatrixE;
                    var eleMatrixF = startDetail.eleMatrixF;
                    switch (direction) {
                        case 'se':
                            matrix.a = (startDetail.width + dx) / originWidth;
                            matrix.d = (startDetail.height + dy) / originHeight;
                            break;
                        case 'sw':
                            eleMatrix.e = eleMatrixE + dx;
                            matrix.a = (startDetail.width - dx) / originWidth;
                            matrix.d = (startDetail.height + dy) / originHeight;
                            break;
                        case 'nw':
                            eleMatrix.e = eleMatrixE + dx;
                            eleMatrix.f = eleMatrixF + dy;
                            matrix.a = (startDetail.width - dx) / originWidth;
                            matrix.d = (startDetail.height - dy) / originHeight;
                            break;
                        case 'ne':
                            eleMatrix.f = eleMatrixF + dy;
                            matrix.a = (startDetail.width + dx) / originWidth;
                            matrix.d = (startDetail.height - dy) / originHeight;
                    }
                    return {
                        matrix: matrix,
                        eleMatrix: eleMatrix
                    };
                },
                //180
                scaleRotateAngleNinetyX2: function (moveDetail) {
                    var dx = moveDetail.dx;
                    var dy = moveDetail.dy;
                    var startDetail = moveDetail.startDetail;
                    var eleMatrix = startDetail.eleMatrix;
                    var originWidth = startDetail.originWidth;
                    var originHeight = startDetail.originHeight;
                    var scaleGroup = startDetail.scaleGroup;
                    var matrix = scaleGroup.matrix;
                    var direction = startDetail.direction;
                    var eleMatrixE = startDetail.eleMatrixE;
                    var eleMatrixF = startDetail.eleMatrixF;
                    switch (direction) {
                        case 'se':
                            eleMatrix.e = eleMatrixE + dx;
                            eleMatrix.f = eleMatrixF + dy;
                            matrix.a = (startDetail.width + dx) / originWidth;
                            matrix.d = (startDetail.height + dy) / originHeight;
                            break;
                        case 'sw':
                            eleMatrix.f = eleMatrixF + dy;
                            matrix.a = (startDetail.width - dx) / originWidth;
                            matrix.d = (startDetail.height + dy) / originHeight;
                            break;
                        case 'nw':
                            matrix.a = (startDetail.width - dx) / originWidth;
                            matrix.d = (startDetail.height - dy) / originHeight;
                            break;
                        case 'ne':
                            eleMatrix.e = eleMatrixE + dx;
                            matrix.a = (startDetail.width + dx) / originWidth;
                            matrix.d = (startDetail.height - dy) / originHeight;
                    }
                    return {
                        matrix: matrix,
                        eleMatrix: eleMatrix
                    };
                },

                //90度
                scaleRotateAngleNinety: function (moveDetail) {
                    var dx = moveDetail.dx;
                    var dy = moveDetail.dy;
                    var startDetail = moveDetail.startDetail;
                    var eleMatrix = startDetail.eleMatrix;
                    var originWidth = startDetail.originWidth;
                    var originHeight = startDetail.originHeight;
                    var scaleGroup = startDetail.scaleGroup;
                    var matrix = scaleGroup.matrix;
                    var direction = startDetail.direction;
                    var eleMatrixE = startDetail.eleMatrixE;
                    var eleMatrixF = startDetail.eleMatrixF;
                    switch (direction) {
                        case 'se':
                            eleMatrix.e = eleMatrixE + dx;
                            matrix.a = (startDetail.width + dy) / originWidth;
                            matrix.d = (startDetail.height + dx) / originHeight;
                            break;
                        case 'sw':
                            matrix.a = (startDetail.width + dy) / originWidth;
                            matrix.d = (startDetail.height - dx) / originHeight;
                            break;
                        case 'nw':
                            eleMatrix.f = eleMatrixF + dy;
                            matrix.a = (startDetail.width - dy) / originWidth;
                            matrix.d = (startDetail.height - dx) / originHeight;
                            break;
                        case 'ne':
                            eleMatrix.e = eleMatrixE + dx;
                            eleMatrix.f = eleMatrixF + dy;
                            matrix.a = (startDetail.width - dy) / originWidth;
                            matrix.d = (startDetail.height + dx) / originHeight;
                    }
                    return {
                        matrix: matrix,
                        eleMatrix: eleMatrix
                    };
                },
                //-90度
                scaleRotateAngleFNinety: function (moveDetail) {
                    var dx = moveDetail.dx;
                    var dy = moveDetail.dy;
                    var startDetail = moveDetail.startDetail;
                    var eleMatrix = startDetail.eleMatrix;
                    var originWidth = startDetail.originWidth;
                    var originHeight = startDetail.originHeight;
                    var scaleGroup = startDetail.scaleGroup;
                    var matrix = scaleGroup.matrix;
                    var direction = startDetail.direction;
                    var eleMatrixE = startDetail.eleMatrixE;
                    var eleMatrixF = startDetail.eleMatrixF;
                    switch (direction) {
                        case 'se':
                            eleMatrix.f = eleMatrixF + dy;
                            matrix.a = (startDetail.width + dy) / originWidth;
                            matrix.d = (startDetail.height + dx) / originHeight;
                            break;
                        case 'sw':
                            eleMatrix.e = eleMatrixE + dx;
                            eleMatrix.f = eleMatrixF + dy;
                            matrix.a = (startDetail.width + dy) / originWidth;
                            matrix.d = (startDetail.height - dx) / originHeight;
                            break;
                        case 'nw':
                            eleMatrix.e = eleMatrixE + dx;
                            matrix.a = (startDetail.width - dy) / originWidth;
                            matrix.d = (startDetail.height - dx) / originHeight;
                            break;
                        case 'ne':
                            matrix.a = (startDetail.width - dy) / originWidth;
                            matrix.d = (startDetail.height + dx) / originHeight;
                    }
                    return {
                        matrix: matrix,
                        eleMatrix: eleMatrix
                    };
                },

                //控制controlPaperSize
                controlPaperSize: function (roomShapeElement, start) {
                    var controlPaperSizeStyle;
                    if (start === 'start') {
                        this.setControlPaperSizeShow(true);
                    }
                    controlPaperSizeStyle = this.getControlPaperSizeStyle(roomShapeElement);
                    this.setControlPaperSizeStyle(controlPaperSizeStyle)
                },

                handleCopySingle: function () {
                    this.singleCoping = true;
                },
                //单个粘贴
                handlePasteSingle: function () {
                    var roomElement, clone, matrix, cloneMatrix, randomId;
//                    if (!this.singleCoping) {
//                        return;
//                    }
                    roomElement = this.selectedRoomShapeElement.ele;
                    clone = roomElement.clone();
                    matrix = roomElement.matrix;
                    cloneMatrix = matrix.clone();
                    randomId = this.getRandomId();
                    cloneMatrix.translate(30, 30);
                    clone.attr({
                        'transform': cloneMatrix.toTransformString(),
                        'model-id': randomId
                    });
                    this.handleRoomShapeDrag(clone);
                    this.handleRoomShapeClick(clone);
                    if (clone.attr('model-type') === 'shape') {
                        this.handleTextDrag(clone.select('g.text-group'))
                    }
                    this.controlPaperSize(clone);
                    this.selectedRoomShapeElement.ele = clone;
                },

                handleBlurSingle: function () {
                    this.singleCoping = false;
                },

                handleCopy: function () {
                    this.copyRoomElements = this.getMovingSelectedRoomElements(true);
                    return this.isCoping = true;
                },
                //多个粘贴
                handlePaste: function () {
                    var copyRoomElements = this.copyRoomElements;
                    var copyEd = [];
                    var nCopySelectedE = [];
//                    if (!this.isCoping) {
//                        return
//                    }
                    //清空之前选择的
                    this.emptyMultiples();
                    for (var i = 0; i < copyRoomElements.length; i++) {
                        var roomElement = copyRoomElements[i].ele;
                        var clone = roomElement.clone();
                        var matrix = roomElement.matrix;
                        var cloneMatrix = matrix.clone();
                        var randomId = this.getRandomId();
                        cloneMatrix.translate(30, 30);
                        clone.attr({
                            'transform': cloneMatrix.toTransformString(),
                            'model-id': randomId
                        });
                        this.handleRoomShapeDrag(clone);
                        this.handleRoomShapeClick(clone);
                        if (clone.attr('model-type') === 'shape') {
                            this.handleTextDrag(clone.select('g.text-group'))
                        }
                        copyEd.push({ele: clone});
                        nCopySelectedE = this.handleRoomElementMultiple(clone)
                    }
                    this.setSelectedRESStartPos(copyEd);
                    this.copyRoomElements = nCopySelectedE;
                },

                handleBlur: function () {
                    this.emptyMultiples();
                    this.isCoping = false;
                },

                handleControlSize: function (moveDetail) {
                    var controlPaperSizeStyle, selectedRoomShapeElementEle;
                    selectedRoomShapeElementEle = this.selectedRoomShapeElement.ele;
                    this.setRoomShapeElementStyle(moveDetail, selectedRoomShapeElementEle);
                    controlPaperSizeStyle = this.getControlPaperSizeStyle(selectedRoomShapeElementEle);
                    this.setControlPaperSizeStyle(controlPaperSizeStyle)

                },

                //是否已经存在
                hasSelectedRoomElement: function (roomElement) {
                    var modelId = roomElement.attr('model-id');
                    var selectedRoomElements = this.selectedRoomElements, i = 0;
                    var has = false;
                    for (; i < selectedRoomElements.length; i++) {
                        if (selectedRoomElements[i].id === modelId) {
                            has = true;
                            break;
                        }
                    }
                    return has;
                },

                //选中元素
                addSelectedRoomElements: function (roomElement) {
                    var modelId = roomElement.attr('model-id');
                    var BBox = roomElement.getBBox();
                    this.selectedRoomElements.push({
                        id: modelId,
                        ele: roomElement,
                        style: {
                            left: BBox.x - 2 + 'px',
                            top: BBox.y - 2 + 'px',
                            width: BBox.width + 2 + 'px',
                            height: BBox.height + 2 + 'px'
                        }
                    });
                    return roomElement
                },

                removesSelectedRoomElements: function (roomElement) {
                    var modelId = roomElement.attr('model-id');
                    var selectedRoomElements = this.selectedRoomElements, i = 0;
                    for (; i < selectedRoomElements.length; i++) {
                        if (selectedRoomElements[i].id === modelId) {
                            selectedRoomElements.splice(i, 1);
                            break;
                        }
                    }
                    return roomElement;
                },


                //generate default element group 生成元素
                generateElementGroup: function (elementObj, fnName) {
                    var snap = this.snap;
                    var matrix = new Snap.Matrix();
                    var transformString, translateGroup, rotateGroup, scaleGroup, element, roomElementDefaultWidth;
                    var randomId = this.getRandomId();
                    roomElementDefaultWidth = this.roomElementDefaultWidth;
                    matrix.translate(elementObj.x, elementObj.y);
                    transformString = matrix.toTransformString();
                    translateGroup = snap.group().addClass('room-translate').attr({
                        'model-id': randomId,
                        'model-link': elementObj.name,
                        'transform': transformString
                    });

                    matrix.e = 0;
                    matrix.f = 0;
                    transformString = matrix.toTransformString();

                    rotateGroup = snap.group().addClass('room-rotate').attr('transform', transformString);
                    matrix.scale(roomElementDefaultWidth / elementObj.width);
                    transformString = matrix.toTransformString();

                    scaleGroup = snap.group().addClass('room-scale').attr('transform', transformString);
                    element = this.generateElement(elementObj, fnName);

                    scaleGroup.add(element);
                    rotateGroup.add(scaleGroup);
                    translateGroup.add(rotateGroup);

                    return translateGroup
                },

                generateElement: function (elementObj, fnName) {
                    var snap = this.snap;
                    var element;
                    if (fnName === 'image') {
                        element = snap.image(elementObj.base64Code, 0, 0, elementObj.width, elementObj.height).addClass('room-element_image')
                    }
                    return element;
                },

                //generate roomShape 生成房间形状
                generateRoomShape: function (offset, innerSVG) {
                    var matrix = new Snap.Matrix(), transformString;
                    var randomId = this.getRandomId();
                    var allScaleGroup = this.allScaleGroup;
                    var group = this.snap.group();
                    group.addClass('room-translate');
                    matrix.translate(offset.x, offset.y);
                    transformString = matrix.toTransformString();
                    group.attr({
                        'model-id': randomId,
                        'transform': transformString
                    });
                    group.node.innerHTML = innerSVG;
                    allScaleGroup.add(group);
                    return group;
                },


                //multiple handler 多选
                handleRoomElementMultiple: function (roomElement) {
                    if (!roomElement) {
                        return;
                    }
                    if (this.hasSelectedRoomElement(roomElement)) {
                        this.removesSelectedRoomElements(roomElement)
                    } else {
                        this.addSelectedRoomElements(roomElement);
                    }
                    return this.selectedRoomElements;
                },


                //清空所有选中的
                emptyMultiples: function () {
                    this.selectedRoomElements = [];
                },

                //清除画布
                clearPaper: function () {
                    this.roomShapeElementsGroup.clear();
                },

                //删除
                handleDelete: function (ele) {
                    ele.remove();
                    this.controlPaperSizeShow = false;
                    this.roomShapeProVisible = false;
                },

                //单个删除
                handleDeleteSingle: function () {
                    this.handleDelete(this.selectedRoomShapeElement.ele)
                    this.roomShapeProVisible = false;
                },

                //批量删除
                handleDeleteBatches: function () {
                    this.selectedRoomElements.forEach(function (t) {
                        t.ele.remove();
                    });
                    this.emptyMultiples();
                },

                //旋转
                handleRotate: function (obj) {
                    var ele = obj.ele;
                    var rotateGroup = ele.select('g.room-rotate');
                    var matrix, rotate, paperSizeStyle;
                    if (!this.hasMatrix(rotateGroup)) {
                        this.addMatrix(rotateGroup);
                    }
                    matrix = rotateGroup.matrix;
                    matrix.rotate(90, obj.x, obj.y);
//                    rotate = this.getRotate(matrix);
                    this.setRotate(rotateGroup, matrix, 0);
                    paperSizeStyle = this.getControlPaperSizeStyle(ele);
                    this.setControlPaperSizeStyle(paperSizeStyle)
                },

                //获取当前位置， 并找到前后元素
                getCurrentPosBeforeAfter: function (ele) {
                    var id = ele.attr('model-id');
                    var roomTranslateGroups = this.allScaleGroup.selectAll('g.room-translate');
                    var currentIndex, beforeElement, afterElement;
                    var len = roomTranslateGroups.length;
                    if (len === 1) {
                        return {
                            currentIndex: 0,
                            beforeElement: null,
                            afterElement: null
                        }
                    }
                    for (var i = 0; i < len; i++) {
                        var roomTranslateGroup = roomTranslateGroups[i];
                        var rTGModelID = roomTranslateGroup.attr('model-id');
                        if (rTGModelID === id) {
                            currentIndex = i;
                            beforeElement = roomTranslateGroups[i - 1];
                            afterElement = roomTranslateGroups[i + 1];
                            break;
                        }
                    }
                    return {
                        currentIndex: 0,
                        beforeElement: beforeElement,
                        afterElement: afterElement
                    }
                },

                //上移顶部
                handleUp: function (ele) {
                    var currentPostBeforeAfter = this.getCurrentPosBeforeAfter(ele);
                    var afterElement = currentPostBeforeAfter.afterElement;
                    if (afterElement) {
                        ele.insertAfter(afterElement)
                    }
                },

                handleDown: function (ele) {
                    var currentPostBeforeAfter = this.getCurrentPosBeforeAfter(ele);
                    var beforeElement = currentPostBeforeAfter.beforeElement;
                    if (beforeElement) {
                        ele.insertBefore(beforeElement)
                    }
                },

                //获取角度
                getRotate: function (matrix) {
                    var matrixSplit = matrix.split();
                    var rotate = matrixSplit.rotate;
                    if (rotate < 0) {
                        rotate = 270;
                    }
                    return rotate
                },

                //设置角度
                setRotate: function (group, matrix, rotate) {
                    group.attr('transform', matrix.toTransformString());
                    this.controlPaperSizeRotate = rotate;
                },


                //取消单个选中的
                unSelected: function () {
                    this.setControlPaperSizeShow(false);
                    //关闭选择颜色框
                    this.selectColorVisible = false;
                },

                //单个元素拖动
                singleRoomEleMove: function (roomEle, dx, dy) {
                    var selectedRESStartPos = this.selectedRESStartPos;
                    var matrix = roomEle.ele.matrix;
                    var modelId = roomEle.ele.attr('model-id');
                    var x = selectedRESStartPos[modelId].x;
                    var y = selectedRESStartPos[modelId].y;
                    var tx = Math.max(dx + x);
                    var ty = Math.max(dy + y);
                    var left = selectedRESStartPos[modelId].left;
                    var top = selectedRESStartPos[modelId].top;
                    matrix.e = tx;
                    matrix.f = ty;
                    roomEle.ele.attr('transform', matrix.toTransformString());
                    if (roomEle.style) {
                        roomEle.style.left = left + dx - 2 + 'px';
                        roomEle.style.top = top + dy - 2 + 'px';
                    }
                },


                //阻止点击事件冒泡
                handleRoomShapeClick: function (roomShape) {
                    var self = this;
                    roomShape.click(function (e) {
                        e.stopPropagation();
                        var _thisRoomShape = this;
                        self.roomShapeProVisible = _thisRoomShape.attr('model-type') === 'shape' && self.controlPaperSizeShow;
                        //关闭颜色选择器
                        self.selectColorVisible = false;
//                        if (self.isMultipleSelected) {
//                            self.multipleFocus = false;
//                            self.isMultipleSelected = false;
//                        }
//                        e.preventDefault()
                    });
                },

                //拖动
                handleRoomShapeDrag: function (roomShape) {
                    var self = this;
                    var roomElements;
                    roomShape.drag(function move(dx, dy, x, y, ev) {
                        var _thisRoomShape = this;
                        ev.stopPropagation();
                        ev.preventDefault();
                        if (self.multipleFocus) {
                            self.isMultipleSelected = true;
                            if (!self.hasSelectedRoomElement(_thisRoomShape)) {
                                self.addSelectedRoomElements(_thisRoomShape);
                                self.setSelectedRESStartPos(roomElements);
                            }
                            self.setControlPaperSizeShow(false)
                        } else {
                            self.controlPaperSize(_thisRoomShape)
                        }
                        self.multipleRoomElementsMove(roomElements, dx, dy)
                    }, function start(x, y, ev) {
                        var _thisRoomShape = this;
                        var roomShapeElementData;
                        var rotateGroup = _thisRoomShape.select('g.room-rotate'), rotate, rotateGroupMatrix;
                        var modelType = _thisRoomShape.attr('model-type');
                        if (!self.hasMatrix(_thisRoomShape)) {
                            self.addMatrix(_thisRoomShape);
                        }
                        self.isMultipleSelected = false;
                        self.roomShapeProVisible = modelType !== 'element';
                        //按下ctrl键
                        if (ev.ctrlKey) {
                            self.multipleFocus = true;
                            if (self.controlPaperSizeShow) {
                                //如果存在已经被选中的
                                self.handleRoomElementMultiple((self.selectedRoomShapeElement.ele || _thisRoomShape));
                            }
                            self.handleRoomElementMultiple(_thisRoomShape);
                            self.setControlPaperSizeShow(false)
                        } else {
                            if (!self.hasSelectedRoomElement(_thisRoomShape)) {
                                //未按下ctrl键，并且没有任何选中的

                                self.multipleFocus = false;
                                self.controlPaperSize(_thisRoomShape, 'start');
                                document.activeElement.blur();
                                self.$nextTick(function () {
                                    if (!self.singleFocus) {
                                        self.singleFocus = true;
                                    }
                                });
                                if (!self.hasMatrix(rotateGroup)) {
                                    self.addMatrix(rotateGroup);
                                }
                                rotateGroupMatrix = rotateGroup.matrix;
                                rotate = self.getRotate(rotateGroupMatrix);
                                self.setRotate(rotateGroup, rotateGroupMatrix, rotate)

                            } else {
                                self.multipleFocus = true;
                            }
//                            if (modelType === 'shape') {
                            roomShapeElementData = self.getRoomShapeElementProperties(_thisRoomShape);
                            self.setSelectedRoomShapeElementData(roomShapeElementData);
//                            }
                        }
                        roomElements = self.getMovingSelectedRoomElements(self.multipleFocus, _thisRoomShape);
                        self.setSelectedRESStartPos(roomElements);
//                        self.copyRoomElements = roomElements;
                    }, function up(ev) {
                        var _thisRoomShape = this;
                        if (!ev.ctrlKey && !self.isMultipleSelected) {
                            self.multipleFocus = false;
                            self.isMultipleSelected = false;
                            self.emptyMultiples();
                            self.singleFocus = true;
                            self.controlPaperSize(_thisRoomShape, 'start');
                        }
                        self.controlPaperSize(_thisRoomShape)
                    })
                },

                //控制字体移动
                handleTextDrag: function (text) {
                    var self = this;
                    if (!text) {
                        return false;
                    }
                    text.drag(function move(dx, dy, x, y, event) {
                        event.stopPropagation();
                        var _thisGroup = this;
                        var matrix = _thisGroup.matrix;
                        var me = _thisGroup.startX + dx;
                        var mf = _thisGroup.startY + dy;
                        var parentBBox = _thisGroup.parentBBox;
                        var BBox = _thisGroup.getBBox();
                        var rotate = _thisGroup.rotate;
                        var mx, my;
                        switch (rotate) {
                            case 90:
                                mx = me;
                                my = mf;
                                break;
                            case 180:
                                mx = me;
                                my = mf;
                                break;
                            case -90:
                                mx = me;
                                my = mf;
                                break;
                            default:
                                mx = Math.max(10, Math.min(me, parentBBox.width - BBox.width));
                                my = Math.max(20, Math.min(mf, parentBBox.height));
                        }
                        matrix.e = mx;
                        matrix.f = my;
                        _thisGroup.attr('transform', matrix.toTransformString());
                    }, function start(x, y, event) {
                        event.stopPropagation();
                        var _thisGroup = this;
                        var matrix, parentBBox;
                        var translateGroup, rotateGroup, rotateMatrix;
                        var roomShapeElementData;
                        if (!self.hasMatrix(_thisGroup)) {
                            self.addMatrix(_thisGroup);
                        }
                        matrix = _thisGroup.matrix;
                        translateGroup = _thisGroup.parent();
                        rotateGroup = translateGroup.select('g.room-rotate');
                        if (!self.hasMatrix(rotateGroup)) {
                            self.addMatrix(rotateGroup);
                        }
                        parentBBox = translateGroup.getBBox();
                        rotateMatrix = rotateGroup.matrix;
                        _thisGroup.startX = matrix.e;
                        _thisGroup.startY = matrix.f;
                        _thisGroup.parentBBox = parentBBox;
                        _thisGroup.rotate = rotateMatrix.split().rotate;
                        _thisGroup.rW = rotateGroup.getBBox().width;
                        _thisGroup.rH = rotateGroup.getBBox().height;
                        roomShapeElementData = self.getRoomShapeElementProperties(translateGroup);
                        self.setSelectedRoomShapeElementData(roomShapeElementData);
//                        if (!self.selectedRoomElements.length) {
//                            self.controlPaperSize(translateGroup, 'start');
//                        }

                    })
                },

                //获取选择中移动的元素
                getMovingSelectedRoomElements: function (isMultipleSelected, roomShape) {
                    var roomElements;
                    if (isMultipleSelected) {
                        roomElements = this.selectedRoomElements;
                    } else {
                        roomElements = [];
                        roomElements.push({
                            ele: roomShape
                        })
                    }
                    return roomElements
                },

                setSelectedRESStartPos: function (roomElements) {
                    for (var i = 0; i < roomElements.length; i++) {
                        var ele = roomElements[i].ele;
                        var matrix = ele.matrix;
                        var box = ele.getBBox();
                        var modelId = ele.attr('model-id');
                        this.changeSelectedRESStartPos(modelId, matrix, box)
                    }
                },

                multipleRoomElementsMove: function (roomElements, dx, dy) {
                    for (var i = 0; i < roomElements.length; i++) {
                        this.singleRoomEleMove(roomElements[i], dx, dy)
                    }
                },

                //改变多个选择的元素的初始位置
                changeSelectedRESStartPos: function (modelId, matrix, box) {
                    if (!this.selectedRESStartPos[modelId]) {
                        this.selectedRESStartPos[modelId] = {}
                    }
                    this.selectedRESStartPos[modelId].x = matrix.e;
                    this.selectedRESStartPos[modelId].y = matrix.f;
                    this.selectedRESStartPos[modelId].left = box.x;
                    this.selectedRESStartPos[modelId].top = box.y;
                },

                handleElementMousedown: function (data) {
                    var offset = data.offset;
                    this.movingElementVisible = true;
                    this.movingElement = data.element;
                    this.movingElementStyle.left = offset.left + 'px';
                    this.movingElementStyle.top = offset.top + 'px';
                },

                handleElementMousemove: function (data) {
                    var offset = data.offset;
                    this.movingElementStyle.left = (offset.left + data.dx) + 'px';
                    this.movingElementStyle.top = (offset.top + data.dy) + 'px';
                },

                //添加房间形状
                handleElementMouseup: function (data) {
                    var isMainPaper;
                    var elementData = data.element;
                    var elementObj, offset, elementGroup, roomShapeElementData;
                    isMainPaper = this.isMainPaper(data.e.target);
                    if (!isMainPaper) {
                        this.movingElementVisible = false;
                        return false;
                    }

                    offset = this.getToPaperPos();
                    elementObj = {
                        base64Code: elementData.base64Code,
                        x: offset.x,
                        y: offset.y,
                        name: elementData.name,
                        width: data.imgWidth,
                        height: data.imgHeight
                    };
                    elementGroup = this.generateElementGroup(elementObj, 'image');
                    elementGroup.attr('model-type', 'element');
                    this.roomShapeElementsGroup.add(elementGroup);
                    this.handleRoomShapeDrag(elementGroup);
                    this.handleRoomShapeClick(elementGroup);
                    this.movingElementVisible = false;
                    this.roomShapeProVisible = false;
                    roomShapeElementData = this.getRoomShapeElementProperties(elementGroup);
                    this.setSelectedRoomShapeElementData(roomShapeElementData);
                    this.controlPaperSize(elementGroup, 'start');
                    this.singleFocus = true;
                    this.controlPaperSizeRotate = 0;

                },

                handleRoomMousedown: function () {
                    this.multipleFocus = false;
                },

                handleRoomMousemove: function (data) {
                    var offset = data.offset;
                    this.movingRoomShape = data.shape;
                    this.movingElementStyle.left = (offset.left) + 'px';
                    this.movingElementStyle.top = (offset.top) + 'px';
                    this.movingRoomShapeVisible = true;
                },

                //房间形状
                handleRoomMouseup: function (data) {
                    var isMainPaper;
                    var offset, roomShapeGroup, roomShapeElementData;
                    isMainPaper = this.isMainPaper(data.e.target);
                    if (!isMainPaper) {
                        this.movingRoomShapeVisible = false;
                        return false;
                    }
                    offset = this.getToPaperPos();
                    roomShapeGroup = this.generateRoomShape(offset, data.innerSVG);
                    roomShapeGroup.attr('model-type', 'shape');
                    this.roomShapeElementsGroup.add(roomShapeGroup);
                    this.handleRoomShapeDrag(roomShapeGroup);
                    this.handleRoomShapeClick(roomShapeGroup);
                    this.handleTextDrag(roomShapeGroup.select('g.text-group'));
                    this.movingRoomShapeVisible = false;
                    roomShapeElementData = this.getRoomShapeElementProperties(roomShapeGroup);
                    this.setSelectedRoomShapeElementData(roomShapeElementData);
                    this.roomShapeProVisible = true;
                    this.controlPaperSize(roomShapeGroup, 'start');
                    this.singleFocus = true;
                    this.controlPaperSizeRotate = 0;
                },

                //添加已存在的数据
                appendOuterSvg: function (outerSvg) {
                    if (!outerSvg) {
                        return
                    }
                    this.snap.select('.room-container').node.innerHTML = outerSvg;
                },


                initializeRoomTranslateGroupsEvent: function () {
                    var groups = this.snap.selectAll('g.room-translate');
                    for (var i = 0; i < groups.length; i++) {
                        var roomShapeGroup = groups[i];
                        this.handleRoomShapeDrag(roomShapeGroup);
                        this.handleRoomShapeClick(roomShapeGroup);
                        if (roomShapeGroup.attr('model-type') === 'element') {
                            //修改为base64code
                            this.changeBase64Code(roomShapeGroup, roomShapeGroup.attr('model-link'))
                        } else {
                            this.handleTextDrag(roomShapeGroup.select('g.text-group'));
                        }
                    }
                },

                changeBase64Code: function (group, link) {
                    group.select('image').attr('href', this.roomElements[link].base64Code);
                },

                //设置值
                setFloorDesigner: function (data) {
                    this.floor.backgroundColor = data.backgroundColor;
                    this.floor.backgroundImage = data.backgroundImage;
                    this.paperStyle.width = parseInt(data.width) || this.minPaperSize.width;
                    this.paperStyle.height = parseInt(data.height) || this.minPaperSize.height;

                },

                //获取元素
                getFloorDesigner: function () {
                    var getFloorDXhr, self = this;
                    getFloorDXhr = $.ajax({
                        type: 'GET',
                        url: '${ctx}/pw/pwDesignerCanvas/ajaxGet/' + this.id,
                        dataType: 'JSON'
                    });
                    getFloorDXhr.done(function (data) {
                        if (data.status) {
                            self.setFloorDesigner(data.datas);
                            return
                        }
                        dialogCyjd.createDialog(0, '请求失败', {
                            dialogClass: 'dialog-cyjd-container none-close',
                            buttons: [{
                                text: '确定',
                                'class': 'btn btn-primary',
                                'click': function () {
                                    $(this).dialog('close');
                                    location.reload();
                                }
                            }]
                        })
                    });
                    getFloorDXhr.fail(function (jqxhr) {
                        dialogCyjd.createDialog(0, '网络连接失败，错误代码' + jqxhr.status, {
                            dialogClass: 'dialog-cyjd-container none-close',
                            buttons: [{
                                text: '确定',
                                'class': 'btn btn-primary',
                                'click': function () {
                                    $(this).dialog('close');
                                    location.reload();
                                }
                            }]
                        })
                    });

                    getFloorDXhr.always(function (data, textStatus) {
                        var outerSvg = textStatus === 'success' ? data.datas.svgHtml : '';
                        self.appendOuterSvg(outerSvg);
                        self.initializeAllScaleGroup();
                        self.initializeRoomTranslateGroupsEvent();
                    })
                },

                listenerScroller: function () {
                    var $paperScroller = $(this.$refs.paperScroller);
                    var self = this;
                    $paperScroller.on('scroll', function (ev) {
                        ev.stopPropagation();
                        self.focusStyle.left = $paperScroller.scrollLeft() + 'px';
                        self.focusStyle.top = $paperScroller.scrollTop() + 'px';
                    })
                },


            },
            beforeMount: function () {

            },
            created: function () {

            },
            mounted: function () {
                this.isLoad = true;
                this.listenerScroller();
                if (!this.id) {
                    this.initializeAllScaleGroup();
                    this.initializeRoomTranslateGroupsEvent();
                    return false;
                }
                this.getFloorDesigner();
            }
        })


    }(jQuery, Vue)

</script>
</body>

