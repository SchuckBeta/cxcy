<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <style>
        .addable-element img {
            display: block;
            width: 100%;
        }

        html, body {
            width: 100%;
            height: 100%;
            overflow: hidden;
        }

        .cert-container {
            width: 100%;
            height: 100%;
        }

        .cert-container .cert-body {
            height: calc(100% - 150px);
            overflow: hidden;
        }

        .cert-drag-element {
            position: absolute;
            left: auto;
            top: auto;
            overflow: hidden;
            -webkit-user-select: none;
            -moz-user-select: none;
            user-select: none;
            user-drag: none;
            -webkit-user-drag: none;
            z-index: 1000;
        }

        .cert-drag-element .drag-el-image {
            width: 128px;
            height: 104px;
            -webkit-user-select: none;
            -moz-user-select: none;
            user-select: none;
            user-drag: none;
            -webkit-user-drag: none;
            overflow: hidden;
        }

        .cert-drag-element .drag-el-image img {
            display: block;
            width: 100%;
            -webkit-user-select: none;
            -moz-user-select: none;
            user-select: none;
            user-drag: none;
            -webkit-user-drag: none;
        }

        .cert-drag-element .drag-el-text {
            line-height: 1;
            height: 14px;
            width: 50px;
            text-align: center;
            overflow: hidden;
            -webkit-user-select: none;
            -moz-user-select: none;
            user-select: none;
            user-drag: none;
            -webkit-user-drag: none;
        }

        .cert-write-font textarea, .cert-write-font .contenteditable-area {
            background-color: #fff;
            border-radius: 4px;
            border: 1px solid #dcdfe6;
            outline: none;
            font-size: 12px;
            line-height: 20px;
            height: 28px;
            padding: 4px 10px;
            width: 300px;
            box-sizing: border-box;
        }

        .cert-write-font textarea, .cert-write-font .contenteditable-area:focus {
            border-color: #E9432D;
            outline: none;
        }

        .cert-multiple-border {
            position: absolute;
            pointer-events: none;
            -moz-user-select: none;
            -webkit-user-select: none;
            -ms-user-select: none;
            user-select: none;
            -webkit-user-drag: none;
            user-drag: none;
            border: 1px solid #39f;
            z-index: 1000;
        }
    </style>
</head>
<body>
<div id="app" v-show="pageLoad" style="display: none;padding: 0" class="container-fluid cert-container">
    <edit-bar second-name="设计" style="margin: 0 0;"></edit-bar>
    <div class="cert-header" style="border-bottom: 1px solid #ccc;box-sizing: border-box">
        <div style="float: right; padding-top: 30px;">
            <el-button type="primary" :disabled="disabled" size="mini" @click.stop.prevent="viewCert">预览</el-button>
            <el-button type="primary" :disabled="disabled" size="mini" @click.stop.prevent="saveCert">提交</el-button>
            <el-button size="mini" :disabled="disabled" @click.stop.prevent="clearCert">清空</el-button>
        </div>
        <div class="cth-addable-elements cth-col" style="padding: 13px 8px; width: 126px;">
            <div v-for="(item, key) in elementsDraggable" :key="key" class="addable-element addable-element-image"
                 style="height: 64px;">
                <el-tooltip class="item" effect="dark" popper-class="white" :content="'拖入画布，双击上传' + item.text"
                            placement="bottom">
                    <div style="margin-top: 6px;">
                        <img @mousedown.stop.prevent="mousedownToPager(item, $event)" :src="item.url">
                    </div>
                </el-tooltip>
                <span>{{item.typeName}}</span>
            </div>
            <%--<div class="addable-element addable-element-image" style="height: 64px;">--%>
            <%--<el-tooltip class="item" effect="dark" popper-class="white" content="拖入画布，双击改变文字" placement="bottom">--%>
            <%--<div style="margin-top: 13px;">--%>
            <%--<img src="${ctxImages}/md/cert-label.png">--%>
            <%--</div>--%>
            <%--</el-tooltip>--%>
            <%--<span>标签</span>--%>
            <%--</div>--%>
        </div>
        <div class="cth-paper-attributes cth-col">
            <div class="paper-attributes-box">
                <div class="paper-size-item">画布宽：{{pageWidth}}px</div>
                <div class="paper-size-item">画布高：{{pageHeight}}px</div>
            </div>
        </div>
        <div v-show="currentElement.id && currentElement.elementType == '1'" class="cth-element-attributes cth-col">
            <div class="image-attributes">
                <div class="attr-group">
                    <label>透明度：</label>
                    <div class="attr-control">
                        <input type="range" step="0.1" min="0.1" max="1" class="input-range" style="width: 160px;"
                               v-model="currentElement.fillOpacity">
                        <output>{{currentElement.fillOpacity * 100}}</output>
                        <span class="unit">%</span>
                    </div>
                </div>
                <div class="attr-group">
                    <label>层级：</label>
                    <div class="attr-control" style="width: 52px;">
                        <el-input type="number" size="mini" v-model="currentElement.sort"></el-input>
                    </div>
                </div>
            </div>
        </div>
        <div v-show="currentElement.id && currentElement.elementType == '2'  || currentElement.elementType == '3'"
             class="cth-element-attributes cth-col">
            <div class="text-attributes">
                <div class="attr-group attr-group-type">
                    <label>类型：</label>
                    <div class="attr-control" style="width: 116px;">
                        <el-select size="mini" placeholder="请选择" clearable filterable v-model="currentElement.varCol"
                                   @change="handleChangeVarCol">
                            <el-option v-for="varCol in varCols" :key="varCol.value" :label="varCol.name"
                                       :value="varCol.value"></el-option>
                        </el-select>
                    </div>
                </div>
                <div class="attr-group attr-group-range" style="width: 244px;">
                    <div class="attr-control">
                        <!-- 色彩块 -->
                        <label>透明度：</label>
                        <input type="range" title="透明度" step="0.1" min="0.1" style="width: 157px;" max="1"
                               class="input-range"
                               v-model="currentElement.fillOpacity">
                        <output>{{currentElement.fillOpacity * 100}}</output>
                        <span class="unit">%</span>
                    </div>
                </div>
                <div class="attr-group attr-group-zindex" style="width: 88px;">
                    <label>层级：</label>
                    <div class="attr-control" style="width: 52px;">
                        <el-input type="number" size="mini" v-model="currentElement.sort"></el-input>
                    </div>
                </div>
                <div class="attr-group" style="width: 100px;">
                    <el-tooltip class="item" effect="dark" content="字体" popper-class="white" placement="top">
                        <el-select size="mini" placeholder="请选择" clearable filterable
                                   v-model="currentElement.fontFamily">
                            <el-option v-for="fontFamily in fontFamilies" :key="fontFamily" :label="fontFamily"
                                       :value="fontFamily"></el-option>
                        </el-select>
                    </el-tooltip>
                </div>
                <div class="attr-group" style="margin-left: 8px;">
                    <el-tooltip class="item" effect="dark" content="字体大小" popper-class="white" placement="top">
                        <el-select size="mini" placeholder="请选择" clearable filterable v-model="currentElement.fontSize"
                                   @change="handleChangeFontSize">
                            <el-option v-for="fontSize in fontSizes" :key="fontSize" :label="fontSize+'px'"
                                       :value="fontSize"></el-option>
                        </el-select>
                    </el-tooltip>
                </div>
                <div class="attr-group attr-style-group">
                    <div class="style-font_item" :class="{'style-font_item-selected': currentElement.fontWeight}">
                        <el-tooltip class="item" effect="dark" content="加粗" popper-class="white" placement="top">
                            <img src="${ctxImages}/md/cert-bold.png" @click.stop="handleChangeFontWeight">
                        </el-tooltip>
                    </div>
                    <div class="style-font_item" :class="{'style-font_item-selected': currentElement.fontStyle}">
                        <el-tooltip class="item" effect="dark" content="斜体" popper-class="white" placement="top">
                            <img src="${ctxImages}/md/cert-itailc.png" @click.stop="handleChangeFontStyle">
                        </el-tooltip>
                    </div>
                    <div class="style-font_item" :class="{'style-font_item-selected': currentElement.textDecoration}">
                        <el-tooltip class="item" effect="dark" content="下划线" popper-class="white" placement="top">
                            <img src="${ctxImages}/md/cert-underline.png" @click.stop="handleChangeTextDecoration">
                        </el-tooltip>
                    </div>
                </div>
                <div class="color-picker-attr">
                    <el-tooltip class="item" effect="dark" content="字体颜色" popper-class="white" placement="top">
                        <el-color-picker v-model="currentElement.color" size="mini"></el-color-picker>
                    </el-tooltip>
                </div>
            </div>
        </div>
    </div>
    <div class="cert-body" v-loading="loading">
        <div class="cert-paper-scroller" data-cursor="grab" ref="certPaperScroller">
            <div class="cert-paper-scroller-background"
                 :style="pageStyle">
                <div class="cert-viewport" :style="pageStyle">
                    <div class="cert-design-paper" @click.stop.prevent="clearSelectElement">
                        <div class="cert-element"
                             v-for="(element, index) in elements" tabindex="-1"
                             :key="element.id"
                             :ref="element.id"
                             :data-id="element.id"
                             @mousedown.stop="elementMousedown(element,$event)"
                             @dblclick.stop.prevent="elementDoubleClick(element, $event)"
                             @keyup.delete="keyUpDelete(element)"
                             :style="{ left: element.x + 'px', top: element.y + 'px',  opacity: element.fillOpacity, zIndex: element.sort}">
                            <div v-if="element.elementType === '1'"
                                 :style="{width: element.width + 'px'}">
                                <img :src="element.url"
                                     :style="{width: element.width + 'px', height: element.height + 'px'}">
                            </div>
                            <div v-else
                                 :style="{height: element.height+'px'}"
                            >
                                <div class="cert-text" :ref="'certText'+element.id"
                                     :style="{fontSize: element.fontSize+'px',
                                    fontWeight: element.fontWeight,
                                    fontStyle: element.fontStyle,
                                    textDecoration: element.textDecoration,
                                     color: element.color,
                                    fontFamily: element.fontFamily}">{{element.text}}
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="cert-control-sizes"
                         :class="{show: controlSize}"
                         ref="certControlSizes"
                         style="z-index:999;"
                         :style="controlSizesStyle">
                        <div v-show="isImage">
                            <div draggable="false" class="resize nw" data-position="top-left"
                                 @mousedown.stop.prevent="stretchNode('tl', $event)"></div>
                            <%--<div draggable="false" class="resize n" data-position="top"--%>
                            <%--@mousedown.stop.prevent="stretchNode($event)"></div>--%>
                            <div draggable="false" class="resize ne" data-position="top-right"
                                 @mousedown.stop.prevent="stretchNode('tr', $event)"></div>
                            <%--<div draggable="false" class="resize e" data-position="right"--%>
                            <%--@mousedown.stop.prevent="stretchNode($event)"></div>--%>
                            <div draggable="false" class="resize se" data-position="bottom-right"
                                 @mousedown.stop.prevent="stretchNode('br', $event)"></div>
                            <%--<div draggable="false" class="resize s" data-position="bottom"--%>
                            <%--@mousedown.stop.prevent="stretchNode($event)"></div>--%>
                            <div draggable="false" class="resize sw" data-position="bottom-left"
                                 @mousedown.stop.prevent="stretchNode('bl', $event)"></div>
                            <%--<div draggable="false" class="resize w" data-position="left"--%>
                            <%--@mousedown.stop.prevent="stretchNode($event)"></div>--%>
                        </div>
                        <div draggable="false" class="cert-delete-element"
                             @mousedown.stop
                             @click.stop.prevent="elementDelete">
                            <i class="el-icon-close"></i>
                        </div>
                    </div>
                    <div v-if="elementTextStyle.left" class="cert-write-font" :class="{show: elementTextShow}"
                         :style="elementTextStyle">
                        <input type="text" class="contenteditable-area" :style="elementTextAreaFontStyle"
                               v-model="elementText" @blur="handleBlurElementText" @keyup.enter="keyupEnterText"
                               ref="elementTextarea">
                    </div>
                    <div v-show="isMultiple" class="cert-multiple-border" :style="multipleStyle"></div>
                </div>
            </div>
        </div>
    </div>
    <div v-show="elementDragShow" class="cert-drag-element" :style="elementDragShowStyle">
        <template v-if="elementDragItem.elementType">
            <div class="drag-el-image" v-if="elementDragItem.elementType === '1'">
                <img :src="elementDragItem.url">
            </div>
            <div class="drag-el-text" v-else>
                {{elementDragItem.text}}
            </div>
        </template>
    </div>
    <form ref="previewForm" style="display: none;" action="${ctx}/cert/preview" method="POST" target="_blank">
        <input type="text" ref="previewData" name="data">
    </form>
    <input type="file" name="upfile" accept="image/jpeg,image/x-png" ref="uploadFile" @change="uploadFile($event)">
</div>

<script type="text/javascript">
    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            return {
                certpageid: '${certpageid}',
                certpagename: '${certpagename}',
                certname: '${certname}',
                certid: '${certid}',
                varCols: JSON.parse(JSON.stringify(${colsJson})),
                pageWidth: '',
                pageHeight: '',
                pageMinWidth: '',
                pageMinHeight: '',
                imgPath: '',
                currentElement: {
                    fillOpacity: 1,
                    sort: 0,
                    color: '#000000',
                    fontFamily: '微软雅黑',
                    fontSize: 12
                },
                fontFamilies: ['微软雅黑', '宋体', '幼圆', '新宋体', '方正姚体', '方正舒体', '楷体', '隶书', '黑体'],
                fontSizes: [12, 14, 16, 20, 24, 36],
                elements: [],
                controlSizesStyle: {
                    left: '',
                    top: '',
                    width: '',
                    height: '',
                },
                controlSize: false,
                isImage: false,

                elementsDraggable: { //default elements
                    image: {
                        fillOpacity: 1,
                        url: '/images/cert-image-default.png',
                        width: 128,
                        height: 104,
                        text: '画布',
                        elementType: '1',
                        typeName: '图片'
                    },
                    textLabel: {
                        fillOpacity: 1,
                        text: '我的文本',
                        url: '/images/md/cert-label.png',
                        width: 64,
                        height: 14,
                        fontSize: 14,
                        elementType: '2',
                        typeName: '标签'
                    }
                },
                elementDragItem: {},
                elementDragShow: false,
                elementDragShowStyle: {},
                eDStyle: {},
                elUpTop: 151,


                //证书文字
                elementTextStyle: {},
                elementTextShow: false,
                elementTextAreaFontStyle: {},
                elementText: '',

                //多选的元素
                selectedElements: [],
                isMultiple: false,
                multipleStyle: {},

                disabled: false,
                loading: false,

                isLeftBorder: false,
                isTopBorder: false,

                multipleStyleCopy: {}

            }
        },
        computed: {
            pageStyle: function () {
                return {
                    width: this.pageWidth + 'px',
                    height: this.pageHeight + 'px',
                }
            },
        },
        filters: {
            urlFtp: function (value, ftpHttp) {
                if (value.indexOf('cert-image-default') > -1) {
                    return value;
                }
                return ftpHttp + value
            }
        },
        methods: {

            clearSelectElement: function ($event) {
                if ($($event.target).parents('.cert-element').size() === 0) {
                    this.currentElement = {};
                    this.controlSize = false;
                    this.isMultiple = false;
                    this.selectedElements = [];
                }
            },

            //删除元素
            elementDelete: function (element) {
                var id = this.currentElement.id;
                var i = 0;
                var elements = this.elements;
                while (i < elements.length) {
                    if (id === elements[i].id) {
                        elements.splice(i, 1);
                        break;
                    }
                    i++;
                }
                this.controlSize = false;
                this.$nextTick(function () {
                    this.setPaperSizeReset();
                })
            },


            keyUpDelete: function (element) {
                if (this.controlSize && this.currentElement.id) {
                    this.elementDelete(this.currentElement);
                }
                //多删除
                if (this.isMultiple) {
                    console.log('no deal')
                }
            },

            handleChangeFontWeight: function () {
                var fontWeight = this.currentElement.fontWeight;
                this.currentElement.fontWeight = fontWeight ? '' : 'bold'
            },

            handleChangeFontStyle: function () {
                var fontStyle = this.currentElement.fontStyle;
                this.currentElement.fontStyle = fontStyle ? '' : 'italic'
            },

            handleChangeTextDecoration: function () {
                var textDecoration = this.currentElement.textDecoration;
                this.currentElement.textDecoration = textDecoration ? '' : 'underline'
            },

            //修改字体大小
            handleChangeFontSize: function () {
                this.setCurTextHeight();
                this.setCurTextWidth();
            },

            //修改类型
            handleChangeVarCol: function (value) {
                this.currentElement.text = this.getVarColNameById(value);
                this.currentElement.elementType = '3';//修改elementType绑定
                this.setCurTextWidth();
            },

            //修改文字后获取宽度
            getCurTextWidth: function () {
                return $(this.$refs['certText' + this.currentElement.id][0]).width();
            },

            getCurTextHeight: function () {
                return $(this.$refs['certText' + this.currentElement.id][0]).height();
            },

            setCurTextWidth: function () {
                this.$nextTick(function () {
                    var width = this.getCurTextWidth();
                    this.controlSizesStyle.width = (width + 2) + 'px';
                    this.currentElement.width = width;
                });
            },

            setCurTextHeight: function () {
                this.$nextTick(function () {
                    var height = this.getCurTextHeight();
                    this.controlSizesStyle.height = (height + 2) + 'px';
                    this.currentElement.height = height;
                });
            },

            getVarColNameById: function (value) {
                var varCols = this.varCols;
                var i = 0;
                var name = '我的文本';
                if (!value) {
                    return name;
                }
                while (i < varCols.length) {
                    var item = varCols[i];
                    if (item.value === value) {
                        name = item.name;
                        break;
                    }
                    i++;
                }
                return name;
            },

            //是否已经再选中中
            hasElement: function (element) {
                var elements = this.selectedElements;
                var i = 0;
                var has = -1;
                while (i < elements.length) {
                    var item = elements[i];
                    if (item.id === element.id) {
                        has = i;
                        break;
                    }
                    i++;
                }
                return has;
            },

            elementMousedown: function (element, event) {
                var ctrlKey = event.ctrlKey;
                var isClick = true;
                var self = this;
                var elementsInitPost = [];
                var startElements = [];
                var hasElement = -1;
                this.isImage = element.elementType === '1';

                if (!ctrlKey) {
                    if (!this.isMultiple) {
                        this.setControlSizesStyle(element);
                        this.controlSize = true;
                    }
                } else {
                    //多选
                    hasElement = this.hasElement(element);
                    if (this.currentElement.id && this.hasElement(this.currentElement) === -1) {
                        this.selectedElements.push(this.currentElement);
                    }
                    if (hasElement == -1) {
                        this.selectedElements.push(element);
                    }
                    this.isMultiple = true;
                    this.controlSize = false;
                    this.currentElement = {};
                }
                if (this.isMultiple) {
                    this.multipleStyle = this.getMultipleStyle();
                    var multipleStyle = JSON.parse(JSON.stringify(this.multipleStyle));
                    this.multipleStyleCopy = {
                        width: parseInt(multipleStyle.width),
                        height: parseInt(multipleStyle.height),
                        top: parseInt(multipleStyle.top),
                        left: parseInt(multipleStyle.left)
                    }
                    console.log(this.multipleStyleCopy)
                }

                startElements = this.isMultiple ? this.selectedElements : [element];
                elementsInitPost = this.getElementsInitPos(startElements, event);

                $(document).on('mousemove.cert.element', function (eventMove) {
                    isClick = false;
                    var moveElements = self.isMultiple ? self.selectedElements : [element];
                    self.elementsMove(moveElements, eventMove, elementsInitPost);
                    if (self.isMultiple) {
                        self.multipleStyle = self.getMultipleStyle();
                    } else {
                        self.setControlSizesStyle(element);
                    }

                });

                $(document).on('mouseup.cert.element', function (eventUp) {
                    $(document).off('mousemove.cert.element');
                    $(document).off('mouseup.cert.element');
                    //点击事件， 没有多选
                    if (isClick && !ctrlKey) {
                        self.currentElement = element;
                        self.selectedElements = [];
                        self.isMultiple = false;
                        self.setControlSizesStyle(element);
                        self.controlSize = true;
                    } else {
                        if (self.isMultiple) {
                            self.currentElement = {};
                        }
                    }
                })
            },


            getSelectedElesSize: function () {
                var widths = [];
                var heights = [];
                var elements = this.selectedElements;
                var xes = [];
                var yes = [];
                for (var i = 0, len = elements.length; i < len; i++) {
                    var item = elements[i];
                    widths.push((item.x + item.width));
                    heights.push((item.height + item.y));
                    xes.push(item.x);
                    yes.push(item.y);
                }
                var maxWidth = Math.max.apply(null, widths);
                var maxHeight = Math.max.apply(null, heights);
                var minX = Math.min.apply(null, xes);
                var minY = Math.min.apply(null, yes);

                return {
                    width: maxWidth - minX,
                    height: maxHeight - minY,
                    minX: minX,
                    minY: minY
                }
            },

            //设置多选框样式
            getMultipleStyle: function () {
                var size = this.getSelectedElesSize();
                this.isLeftBorder = size.minX <= 0;
                this.isTopBorder = size.minY <= 0;
                return {
                    width: (size.width + 2) + 'px',
                    height: (size.height + 2) + 'px',
                    left: (size.minX - 2) + 'px',
                    top: (size.minY - 2) + 'px'
                }
            },


            stretchNode: function (der, $event) {
                var mdCurrentImage = JSON.parse(JSON.stringify(this.currentElement))
                var startX = $event.clientX;
                var startY = $event.clientY;
                var self = this;


                $(document).on('mousemove.resize', function (moveEvent) {
                    var moveX = moveEvent.clientX;
                    var moveY = moveEvent.clientY;
                    var movePos = {};
                    var dx = moveX - startX;
                    var dy = moveY - startY;

                    if (der === 'tl') {
                        movePos = self.getTlElementPos(mdCurrentImage, dx, dy);
                    } else if (der === 'tr') {
                        movePos = self.getTrElementPos(mdCurrentImage, dx, dy);
                    } else if (der === 'br') {
                        movePos = self.getBrElementPos(mdCurrentImage, dx, dy);
                    } else {
                        movePos = self.getBlElementPos(mdCurrentImage, dx, dy);
                    }
                    Object.assign(self.currentElement, movePos);
                    self.setControlSizesStyle(self.currentElement)
                })

                $(document).on('mouseup.resize', function () {
                    $(document).off('mousemove.resize');
                    $(document).off('mouseup.resize');
                })
            },

            getTlElementPos: function (element, dx, dy) {
                return {
                    x: element.x + dx,
                    y: element.y + dy,
                    width: element.width - dx,
                    height: element.height - dy
                }
            },

            getTrElementPos: function (element, dx, dy) {
                return {
                    y: element.y + dy,
                    width: element.width + dx,
                    height: element.height - dy
                }
            },

            getBrElementPos: function (element, dx, dy) {
                return {
                    width: element.width + dx,
                    height: element.height + dy
                }
            },

            getBlElementPos: function (element, dx, dy) {
                return {
                    x: element.x + dx,
                    width: element.width - dx,
                    height: element.height + dy
                }
            },

            getElementsInitPos: function (elements, event) {
                var elementsObj = {};
                elements.forEach(function (element) {
                    elementsObj[element.id] = {
                        startX: event.clientX,
                        startY: event.clientY,
                        elementX: element.x,
                        elementY: element.y
                    }
                });
                return JSON.parse(JSON.stringify(elementsObj));
            },

            elementsMove: function (elements, moveEvent, elementsPos) {
                var moveX = moveEvent.clientX;
                var moveY = moveEvent.clientY;
                var isMultiple = this.isMultiple;
                var multipleStyleCopy = this.multipleStyleCopy;
                elements.forEach(function (element) {
                    var startElement = elementsPos[element.id];
                    var startX = startElement.startX;
                    var startY = startElement.startY;
                    var elementX = startElement.elementX;
                    var elementY = startElement.elementY;
                    var dx = moveX - startX;
                    var dy = moveY - startY;
                    var left = dx + elementX;
                    var top = dy + elementY;
                    if (isMultiple) {
                        if (multipleStyleCopy.left + dx < 0) {
                            element.x = element.x + 0;
                        } else {
                            element.x = left;
                        }
                        if (multipleStyleCopy.top + dy < 0) {
                            element.y = element.y + 0;
                        } else {
                            element.y = top;
                        }
                    } else {
                        element.x = left < 0 ? 0 : left;
                        element.y = top < 0 ? 0 : top;
                    }

                })
                this.setPaperSizeReset();
            },

            //拖拽元素到画布
            mousedownToPager: function (item, $event) {
                var startX, startY, elWidth, elHeight, itemInitX, itemInitY;
                var self = this;
                startX = $event.clientX;
                startY = $event.clientY;
                elWidth = item.width;
                elHeight = item.height;
                itemInitX = (startX);
                itemInitY = (startY);
                this.elementDragItem = item;
                this.elementDragShowStyle = {
                    left: (startX - elWidth / 2) + 'px',
                    top: (startY - elHeight / 2) + 'px'
                };

                $(document).on('mousemove.topager', function (eventMove) {
                    eventMove.stopPropagation();
                    eventMove.preventDefault();
                    var dx, dy, left, top;
                    var moveX = eventMove.clientX;
                    var moveY = eventMove.clientY;
                    dx = moveX - startX;
                    dy = moveY - startY;
                    left = (itemInitX + dx - elWidth / 2);
                    top = (itemInitY + dy - elHeight / 2);
                    left = left < 0 ? 0 : left;
                    top = top < 0 ? 0 : top;
                    self.elementDragShowStyle = {
                        left: left + 'px',
                        top: top + 'px'
                    };
                    self.elementDragShow = true;
                });


                $(document).on('mouseup.topager', function (eventUp) {
                    $(document).off('mousemove.topager');
                    $(document).off('mouseup.topager');
                    var elementDragShowStyle = self.elementDragShowStyle;
                    var style = {
                        left: parseInt(elementDragShowStyle.left),
                        top: parseInt(elementDragShowStyle.top),
                    }
                    if (self.isToPaperInSide(parseInt(elementDragShowStyle.top))) {
                        self.addItemToElements(item, style)
                    }
                    self.elementDragShow = false;
                })
            },

            //双击上传图片
            elementDoubleClick: function (element) {
                if (element.elementType === '1') {
                    this.uploadImage();
                } else {
                    if (element.varCol === '') {
                        this.showElementText(element);
                    }
                }
            },

            //处理双击文字
            showElementText: function (element) {
                this.elementTextShow = true;
                this.setElementTextStyle(element);
                this.setElementText(element);
                this.$nextTick(function () {
                    this.$refs.elementTextarea.focus();
                })
            },

            setElementTextStyle: function (element) {
                this.elementTextStyle = {
                    left: element.x + 'px',
                    top: element.y + 'px'
                }
            },

            handleBlurElementText: function (value) {
                var element = this.getElementById(this.currentElement.id);
                if (!value) {
                    element.text = '我的文本';
                } else {
                    if (element.text.length > 64) {
                        this.$message.error('请输入大不于64个文字');
                        return false;
                    }
                    element.text = this.elementText;
                }
                this.setCurTextWidth();
                this.elementTextShow = false;
            },

            keyupEnterText: function (value) {
                var element = this.getElementById(this.currentElement.id);
                element.text = this.elementText;
                this.elementTextShow = false;
            },

            setElementText: function (element) {
                this.elementText = element.text;
            },

            //上传图片
            uploadImage: function () {
                this.$refs.uploadFile.click();
            },

            //上传图片
            uploadFile: function ($event) {
                var files = $event.target.files;
                var file, fd, self = this;
                if (files.length < 1) {
                    $event.target.files = null;
                    return false;
                }
                file = files[0];
                if (!file.size) {
                    this.$message.error('图片大小为OKB，请重新上传');
                    return false;
                }
                fd = new FormData();
                fd.append('upfile', file);
                this.$axios.post('/ftp/ueditorUpload/uploadTempFtp?folder=cert', fd).then(function (response) {
                    var data = response.data;
                    if (data.state !== 'SUCCESS') {
                        self.$message.error('上传失败');
                        return false;
                    }
                    self.setElementUrl(data.url);
                    $event.target.files = null;
                }).catch(function (error) {
                    $event.target.files = null;
                    self.$message.error(self.xhrErrorMsg);
                });
            },

            setElementUrl: function (url) {
                var id = this.currentElement.id;
                var element = this.getElementById(id);
                element.url = url;
                this.setImageSize(element);
            },

            setImageSize: function (element) {
                var image = new Image();
                var self = this;
                image.onload = function () {
                    image.onload = null;
                    element.width = image.naturalWidth;
                    element.height = image.naturalHeight;
                    self.setControlSizesStyle(element);
                    self.setPaperSizeReset(element);
                };
                image.src = element.url
            },

            setPaperSizeReset: function () {
                var maxSize = this.getMaxSize();
                var maxWidth = maxSize.maxWidth + 100;
                var maxHeight = maxSize.maxHeight + 100;
                var minWidth = this.pageWidth - 100;
                var minHeight = this.pageHeight - 100;
                var pageWidth, pageHeight;
                if (this.pageWidth < maxWidth) {
                    this.pageWidth = maxWidth;
                }

                if (this.pageHeight < maxHeight) {
                    this.pageHeight = maxHeight;
                }
                pageWidth = this.pageWidth;
                pageHeight = this.pageHeight;
                if (minWidth > maxWidth) {
                    pageWidth -= 100;
                    this.pageWidth = pageWidth < this.pageMinWidth ? this.pageMinWidth : pageWidth;
                }

                if (minHeight > maxHeight) {
                    pageHeight -= 100;
                    this.pageHeight = pageHeight < this.pageMinHeight ? this.pageMinHeight : pageHeight;
                }
            },

            getElementById: function (id) {
                var elements = this.elements;
                var element = null;
                for (var i = 0; i < elements.length; i++) {
                    var item = elements[i];
                    if (item.id === id) {
                        element = item;
                        break;
                    }
                }
                return element;
            },

            //是否需要拖拽到画布
            isToPaperInSide: function (top) {
                return top > this.elUpTop;
            },

            //添加元素到画布
            addItemToElements: function (item, style) {
                var element = this.getElementItemObj(item, style);
                this.elements.push(element);
                return element;
            },

            //获取元素对象
            getElementItemObj: function (item, style) {
                var certPaperScroller = this.$refs.certPaperScroller;
                var props = {
                    id: 'UU' + (Math.ceil(Math.random() * 10000) + new Date().getTime()),
                    x: style.left + certPaperScroller.scrollLeft,
                    y: style.top + certPaperScroller.scrollTop - 151,
                    matrix: 'transform(1,0,0,1,0,0)',
                    angle: 0,
                    sort: this.getMaxZIndex() + 1,
                    varCol: '',
                    repeat: '',
                    color: 'rgb(51,51,51)',
                    fontSize: '',
                    fontStyle: '',
                    fontWeight: '',
                    textDecoration: '',
                    fontFamily: "微软雅黑"
                };
                var nItem = item;
                if (item.elementType === '2') {
                    nItem = JSON.parse(JSON.stringify(item));
                    nItem.url = '';
                }
                return Object.assign(props, nItem);
            },

            //获取最大的index;
            getMaxZIndex: function () {
                var sorts = [0];
                this.elements.forEach(function (t) {
                    sorts.push(t.sort);
                });
                return Math.max.apply(Math, sorts)
            },

            //设置画布大小
            setControlSizesStyle: function (element) {
                this.controlSizesStyle = {
                    left: (element.x - 2) + 'px',
                    top: (element.y - 2) + 'px',
                    width: (element.width + 2) + 'px',
                    height: (element.height + 2) + 'px'
                }
            },

            //获取画布数据
            getCertPage: function () {
                var self = this;
                this.$axios.get('/cert/getCertPage?pageid=' + this.certpageid).then(function (response) {
                    var data = response.data;
                    if (data && data.list) {
                        self.clearCert();
                        self.imgPath = data.imgPath;
                        self.pageWidth = parseInt(data.width);
                        self.pageHeight = parseInt(data.height);
                        self.elements = data.list;
                        self.elements.forEach(function (item) {
                            item.width = parseInt(item.width)
                            item.height = parseInt(item.height)
                        })
                    }
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg);
                });
            },

            //清除画布
            clearCert: function () {
                this.elements = [];
                this.controlSize = false;
                this.selectedElements = [];
                this.isMultiple = false;
                this.getCertPageSizeBase();
            },

            //获取画布基本大小
            getCertPageSizeBase: function () {
                var element = this.$refs.certPaperScroller;
                var width = $(element).width();
                var height = $(element).height();
                this.pageWidth = width;
                this.pageHeight = height;
                this.pageMinWidth = width;
                this.pageMinHeight = height;
            },

            //验证是否有默认图片
            validateElements: function () {
                var elements = this.elements;
                var len = elements.length;
                var valid = true;
                for (var i = 0; i < len; i++) {
                    var elementItem = elements[i];
                    if (/cert\-image\-default/.test(elementItem.url)) {
                        valid = false;
                        break;
                    }
                }
                return valid;
            },

            //预览
            viewCert: function () {
                var data = this.getCertData();
                if (!data) {
                    return
                }
                var fitSize = this.getFitElements();
                data.width = fitSize.width;
                data.height = fitSize.height;
                data.list = fitSize.elements;
                this.$refs.previewData.value = JSON.stringify(data);
                this.$refs.previewForm.submit();
            },

            saveCert: function () {
                var data = this.getCertData();
                if (!data) {
                    return
                }
                var fitSize = this.getFitElements();
                var self = this;
                data.width = fitSize.width;
                data.height = fitSize.height;
                data.list = fitSize.elements;
                this.disabled = true;
                this.loading = true;
                this.$axios.post('/cert/save', data).then(function (response) {
                    var nData = response.data;
                    if (nData.ret == '1') {
                        self.$alert(nData.msg, '提示', {
                            type: 'success'
                        })
                        self.resetElementImage();
                    } else {
                        self.$message.error(nData.msg);
                    }
                    self.disabled = false;
                    self.loading = false;
                }).catch(function (error) {
                    self.disabled = false;
                    self.loading = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            resetElementImage: function () {
                var elements = this.elements;
                var i = 0;
                while (i < elements.length) {
                    var element = elements[i];
                    if (element.elementType === '1') {
                        if (element.url.indexOf('?fileSize=') > -1) {
                            element.url = element.url.match(/^.*(?=(\?fileSize))/)[0];
                            element.url =  element.url.replace('/temp', '')
                        }
                    }
                    i++
                }
            },

            getCertData: function () {
                var data = {
                    certid: this.certid,
                    certname: this.certname,
                    certpageid: this.certpageid,
                    certpagename: this.certpagename,
                    uiHtml: '1',
                    uiJson: '1'
                };
                var elements = this.elements;
                var len = elements.length;
                var isValid;
                if (len === 0) {
                    this.$message.error("请上传图片或者写入文字", "提示");
                    return false;
                }
                isValid = this.validateElements();
                if (!isValid) {
                    this.$message.error("存在默认图片，请双击上传图片", "提示");
                    return false;
                }
                return data;
            },

            //获取铺满的元素数据
            getFitElements: function () {
                var elementCopy = [];
                var elements = JSON.parse(JSON.stringify(this.elements))
                var maxSize = this.getMaxSize();
                var maxWidth = maxSize.maxWidth;
                var maxHeight = maxSize.maxHeight;
                var minX = maxSize.minX;
                var minY = maxSize.minY;

                elements.forEach(function (item, i) {
                    elementCopy.push(item);
                    elementCopy[i].x = item.x - minX;
                    elementCopy[i].y = item.y - minY;
                });
                return {
                    elements: elementCopy,
                    width: maxWidth - minX,
                    height: maxHeight - minY
                }
            },

            //获取所有元素的最大高度最大宽度
            getMaxSize: function () {
                var widths = [];
                var heights = [];
                var elements = this.elements;
                var xes = [];
                var yes = [];
                for (var i = 0, len = elements.length; i < len; i++) {
                    var item = elements[i];
                    widths.push((item.x + item.width));
                    heights.push((item.height + item.y));
                    xes.push(item.x);
                    yes.push(item.y);
                }
                return {
                    maxWidth: Math.max.apply(null, widths),
                    maxHeight: Math.max.apply(null, heights),
                    minX: Math.min.apply(null, xes),
                    minY: Math.min.apply(null, yes)
                }
            },
        },

        created: function () {
            if (this.certpageid) {
                this.getCertPage();
            }
        },
        mounted: function () {
            this.getCertPageSizeBase();
        }
    })
</script>

</body>
</html>