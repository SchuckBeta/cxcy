var Utils = {
    fitSize: function (cw, ch, ow, oh) {
        var ratio1 = cw / ow; //100 60
        var ratio2 = ch / oh; //80 60
        var nw, nh;
        if (ratio1 <= ratio2) {
            nh = cw * oh / ow;
            return [0, (ch - nh) / 2, cw, nh]
        } else {
            nw = ch * ow / oh;
            return [(cw - nw) / 2, 0, nw, ch]
        }
    }
}
String.prototype.colorRgb = function () {
    var sColor = this.toLowerCase();
    //十六进制颜色值的正则表达式
    var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
    // 如果是16进制颜色
    if (sColor && reg.test(sColor)) {
        if (sColor.length === 4) {
            var sColorNew = "#";
            for (var i = 1; i < 4; i += 1) {
                sColorNew += sColor.slice(i, i + 1).concat(sColor.slice(i, i + 1));
            }
            sColor = sColorNew;
        }
        //处理六位的颜色值
        var sColorChange = [];
        for (var i = 1; i < 7; i += 2) {
            sColorChange.push(parseInt("0x" + sColor.slice(i, i + 2)));
        }
        return "RGB(" + sColorChange.join(",") + ")";
    }
    return sColor;
};

;(function (window, Snap, $) {
    function App(option) {
        this.options = $.extend(true, {}, App.DEFAULT, option);
        this.snap = this.createSnap(this.options.elId);
        this.sGroup = this.snap.group();
        this.resizeBoxEle = $(this.options.elId).next(this.options.resizeEle);
        this.rotateEle = this.resizeBoxEle.next();
        this.resizeElements = this.resizeBoxEle.children();
        this.rdPaperContent = $('#rdPaperContent');
        this.$rdViewport = $('.rd-viewport');
        this.maxWidth = this.$rdViewport.width();
        this.maxHeight = this.$rdViewport.height();
        this.startX = 0;
        this.startY = 0;
        this.currentParentElement = null;
        this.rooms = {
            paperSize: '',
            data: {
                list: []
            }
        };
        this.init();

    }

    App.prototype.init = function () {
        var paperSize = this.options.paperSize.split(',');
        var $win = $(window);
        var width = paperSize[0] == 'auto' ? ($win.width() < 1400 ? 600 : 1000) : paperSize[0];
        var height = paperSize[1] == 'auto' ? ($win.width() < 1400 ? 450 : 700) : paperSize[1];
        // RoomDefined.$data.paperWidth = width;
        // RoomDefined.$data.paperHeight = height;
        this.options.paperSize = [width, height].join(',');
        this.setPaperSize({
            width: width * 1,
            height: height * 1,
            left: 100,
            top: 0
        });
        this.setSGroupZoom();
        this.snapMousedown();

        // this.addBackground(this.options.data.background);
        // this.parseRoomList();
        // this.parseTrimmingList();
        this.resizeElementDrag();
        // this.rotate();
        // this.getRooms()
    };

    //生成画布
    App.prototype.createSnap = function (id) {
        return Snap(id);
    };

    App.prototype.setSGroupZoom = function () {
        this.sGroup.attr('transform', this.options.transform).attr('class', 'super-group');
    };

    App.prototype.snapMousedown = function () {
        var $rdFreeTransform = $('.rd-free-transform');
        this.snap.mousedown(function (event) {
            if (event.target.id === 'rdPaper') {
                $rdFreeTransform.hide();
                $rdFreeTransform.next().hide();
            }
        })
    }


    //生成形状
    App.prototype.shape = function (shapeType, position, content) {
        var text;
        switch (shapeType) {
            case 'rect':
                return this.snap.rect(position[0], position[1], position[2], position[3]);
            case 'image':
                return this.snap.image(content, position[0], position[1], position[2], position[3]);
            case 'text':
                // text = this.snap.text(position[0], position[1], $.isArray(content) ? content : content.split('\n'));
                text = this.snap.text(position[0], position[1], content.split('\n'));
                text.children().forEach(function (t) {
                    t.attr({
                        x: position[0],
                        dy: '1em'
                    })
                });
                return text;
        }
    };

    App.prototype.addBackground = function (background) {
        var fill = background.attr ? background.attr.fill : '#fff';
        var href = background.attr ? background.attr.href : '';
        this.rdPaperContent.css({
            'backgroundColor': fill,
            'backgroundImage': 'url(' + href + ')'
        })
        // this.backgroundDown(group)
        // this.addGroupMouseDownEvent(group);
    };

    App.prototype.backgroundDown = function (group) {
        var $rdFreeTransform = $('.rd-free-transform')
        group.mousedown(function (event) {
            $rdFreeTransform.hide();
            RoomDefined.$data.halo.show = false;
        })
    }


    //添加房间房间
    App.prototype.addRoom = function (data) {
        var group = this.snap.group();
        var position = [data.attr.x, data.attr.y, data.attr.width, data.attr.height];
        var rect = this.shape(data.shapeType, position).data('type', data.type);
        var children = data.children;
        var sGroup = this.sGroup;
        var rotateGroup = this.snap.group();
        group.attr('transform', data.transform);
        rotateGroup.attr('transform', data.rotate);
        // if(data.shapeType == 'image'){
        //     $.each(RoomDefined.$data.trimmingBase64, function (i, item) {
        //         if(item.imageUrl == data.attr.href){
        //             data.attr.href = item.attr.href;
        //             return false;
        //         }
        //     })
        // }
        rect.attr(data.attr);
        rect.data('url', data.attr.href)
        rect.addClass('rd-move rect-parent');
        if(data.shapeType == 'image'){
            rect.addClass('static-image');
        }
        group.data({
            'type': data.type
        });
        group.data({
            'roomId': data.roomId || ''
        })
        rotateGroup.add(rect).attr('class', 'v-rotate');
        rotateGroup.data('angle', data.angle)
        group.addClass('v-2');
        group.add(rotateGroup);
        sGroup.add(group);
        this.dealGroupDragEvent(rect);
        this.addGroupMouseDownEvent(group);
        this.parseRoomAssetList(children, rotateGroup);
    };


    //添加房间资产
    App.prototype.addRoomAsset = function (data, target) {
        var content = data.shapeType === 'text' ? data.text : data.attr.href || '';
        var position = [data.attr.x, data.attr.y, data.attr.width, data.attr.height];
        var shape = this.shape(data.shapeType, position, content).attr(data.attr).data({
            'type': 'roomAsset',
        }).data({
            'name': data.name
        });
        shape.addClass('rd-move rd-room-asset');
        var rect = target.children()[0];
        shape.data('parentId', rect.id);
        shape.parentElement = rect;
        target.add(shape);
        this.addRoomAssetDrag(shape);
    };

    //添加房间挂饰
    App.prototype.addRoomTrimming = function (data) {
        var group = this.snap.group();
        var rotateGroup = this.snap.group();
        var content = data.shapeType === 'text' ? data.text : data.attr.href || '';
        var position = [data.attr.x, data.attr.y, data.attr.width, data.attr.height];
        var shape = this.shape(data.shapeType, position, content).attr(data.attr);
        var sGroup = this.sGroup;
        var children = data.children;
        shape.data('name', data.name || '');
        shape.data('type', data.type).addClass('rd-move rect-parent')
        shape.data('url', data.imageUrl);
        shape.attr({
            'x': 0,
            'y': 0
        })
        rotateGroup.attr('transform', data.rotate);
        rotateGroup.add(shape).attr('class', 'v-rotate');
        rotateGroup.data('angle', data.angle)
        group.add(rotateGroup)
        group.attr('transform', data.transform);
        group.data('type', data.type)
        group.addClass('v-trimming');
        sGroup.add(group);
        this.dealGroupDragEvent(shape);
        this.addGroupMouseDownEvent(group);
        children && this.parseRoomAssetList(children, rotateGroup);
    };

    //修改房间id
    App.prototype.changeText = function (textEle, rect, text, attr, g, roomId) {
        var shape = this.shape('text', [attr.x, attr.y], text).attr(attr).data({
            'type': 'roomAsset'
        });
        textEle.remove();
        shape.insertAfter(rect);
        shape.data('parentId', rect.id);
        shape.parentElement = rect;
        g.data('roomId', roomId);
        this.addRoomAssetDrag(shape);
    };

    //修改资产text
    App.prototype.changeRoomAssetText = function (ele, text, position) {
        var tspan = [];
        ele.node.innerHTML = '';
        text.forEach(function (t) {
            tspan.push('<tspan x="' + position[0] + '" dy="1em">' + t + '</tspan>')
        });
        ele.node.innerHTML = tspan.join('')
    }

    //处理group拖拽事件
    App.prototype.dealGroupDragEvent = function (element) {
        var resizeBoxEle = this.resizeBoxEle;
        var rotateEle = this.rotateEle;
        var maxWidth = this.maxWidth;
        var maxHeight = this.maxHeight;
        var self = this;
        element.drag(function (dx, dy) {
            var totalMatrix = this.totalMatrix;
            var left = totalMatrix.e + dx;
            var top = totalMatrix.f + dy;
            var strokeWidth = this.attr('strokeWidth').replace('px', '') * 1;
            var tl, tt;
            var BBox = this.getBBox();
            var angle = this.parent().data('angle') * 1;
            var posSize = {
                width: resizeBoxEle.width(),
                height: resizeBoxEle.height()
            }
            left = Math.min(Math.max(left, strokeWidth), maxWidth - BBox.width - strokeWidth);
            top = Math.min(Math.max(top, strokeWidth), maxHeight - BBox.height - strokeWidth);

            tl = left - strokeWidth;
            tt = top - strokeWidth;

            this.parent().parent().attr({
                'transform': 'matrix(1,0,0,1,' + (left) + ',' + (top) + ')'
            });
            posSize.left = tl;
            posSize.top = tt;
            resizeBoxEle.css({
                left: tl,
                top: tt
            });
            rotateEle.show().css({
                left: tl,
                top: tt
            })
            if (angle == 0 || angle == 180) {
                rotateEle.css({
                    'width': posSize.width,
                    'height': posSize.height,
                    'left': posSize.left,
                    'top': posSize.top
                })
            } else {
                rotateEle.css({
                    'width': posSize.height,
                    'height': posSize.width,
                    'left': (posSize.left + posSize.width / 2 - posSize.height / 2),
                    'top': (posSize.top + posSize.height / 2 - posSize.width / 2)
                })
            }
        }, function (x, y) {
            this.totalMatrix = this.parent().parent().matrix;
            RoomDefined.$data.colorPickerShow = false;
            maxWidth = self.maxWidth;
            maxHeight = self.maxHeight;
        })
    };


    App.prototype.setScrollLimit = function (width, zoom, viewWidth) {
        var addWidth = viewWidth + width * zoom;
        this.$rdViewport.width(addWidth);
        this.$rdViewport.parent().width(addWidth + 200)
        this.$rdViewport.parent().parent().scrollLeft(width * zoom + 100)
    }

    //添加鼠标按下出现外框事件
    App.prototype.addGroupMouseDownEvent = function (ele) {
        var resizeBoxEle = this.resizeBoxEle;
        var rotateEle = this.rotateEle;
        var self = this;
        var $rdFreeTransform = $('.rd-free-transform')
        ele.mousedown(function (e) {
            var posSize, matrix, rectParentEle, roomData, strokeWidth, box, rotateGroup, angle;
            resizeBoxEle.show();
            rotateEle.show();
            rotateGroup = this.select('.v-rotate');
            angle = rotateGroup.data('angle') * 1;
            matrix = this.matrix;
            rectParentEle = this.select('.rect-parent');
            box = rectParentEle.getBBox();
            strokeWidth = rectParentEle.attr('strokeWidth').replace('px', '') * 1;
            posSize = {
                left: (matrix.e - strokeWidth),
                top: (matrix.f - strokeWidth),
                width: (box.width + strokeWidth),
                height: (box.height + strokeWidth)
            };
            $rdFreeTransform.css('transform', 'rotate(' + angle + 'deg)');
            self.currentParentElement = this;
            resizeBoxEle.css({
                'width': posSize.width,
                'height': posSize.height,
                'left': posSize.left,
                'top': posSize.top
            });
            if (angle == 0 || angle == 180) {
                rotateEle.css({
                    'width': posSize.width,
                    'height': posSize.height,
                    'left': posSize.left,
                    'top': posSize.top
                })
            } else {
                rotateEle.css({
                    'width': posSize.height,
                    'height': posSize.width,
                    'left': (posSize.left + posSize.width / 2 - posSize.height / 2),
                    'top': (posSize.top + posSize.height / 2 - posSize.width / 2)
                })
            }
            $rdFreeTransform.find('.resize').each(function (i, item) {
                var $item = $(item);
                var className;
                if (angle == 0) {
                    className = 'resize ' + RoomDefined.$data.angleClass['zero'][i]
                } else if (angle == 90) {
                    className = 'resize ' + RoomDefined.$data.angleClass['thirty'][i]
                } else if (angle == 180) {
                    className = 'resize ' + RoomDefined.$data.angleClass['an180'][i]
                } else if (angle == 270) {
                    className = 'resize ' + RoomDefined.$data.angleClass['an270'][i]
                }
                $item.attr('class', '').addClass(className)
            })

            roomData = self.getRoomData(this);
            if (roomData.type === 'room') {
                RoomDefined.$data.currentRoomTrimmingData = {};
                RoomDefined.$data.currentRoomData = roomData;
                RoomDefined.$data.currentRoomAssetData = roomData.children.filter(function (t, i) {
                    return i > 0
                });
            } else {
                RoomDefined.$data.currentRoomData = [];
                RoomDefined.$data.currentRoomAssetData = [];
                RoomDefined.$data.currentRoomTrimmingData = roomData;
            }
        })
    };

    //解析type === 'room'的数据
    App.prototype.parseRoomList = function (roomList) {
        var list = roomList || this.options.data.list || [];
        var self = this;
        // var roomList = list
        list.forEach(function (t, i) {
            self.addRoom(t, i)
        });
        this.rooms.data.list = list || [];
    };

    //解析type === 'roomAsset'的数据
    App.prototype.parseRoomAssetList = function (list, group) {
        var self = this;
        if (!list && list.length < 1) return;
        list.forEach(function (t, i) {
            self.addRoomAsset(t, group, i)
        })
    };

    //解析type==='trimming'的数据
    App.prototype.parseTrimmingList = function () {
        var list = this.options.data.list || [];
        var self = this;
        var trimmingList = list.filter(function (t) {
            return t.type === 'trimming'
        });
        trimmingList.forEach(function (t) {
            self.addRoomTrimming(t)
        })

    }

    //添加房间资产拖拽
    App.prototype.addRoomAssetDrag = function (element) {
        var self = this;
        element.drag(function (dx, dy) {
            var bBox = this.startBBox;
            var parentElementstrokeWidth = this.parentElementstrokeWidth;
            var x, y, dxZoomX, dxXZoomY;
            var tBBox = this.getBBox();
            var rotateGroup = this.parent();
            var angle = rotateGroup.data('angle') * 1;
            if (angle == 0) {
                y = Math.min(Math.max(this.startY + dy, bBox.y), bBox.y + bBox.height - tBBox.height);
                if (this.type === 'text') {
                    x = Math.min(Math.max(this.startX + tBBox.width / 2 + dx, bBox.x + tBBox.width / 2 + parentElementstrokeWidth), bBox.x + bBox.width - tBBox.width / 2 - parentElementstrokeWidth);
                    this.attr({
                        x: x,
                        y: y
                    });
                    this.children().forEach(function (t) {
                        t.attr({
                            x: x
                        })
                    })
                } else {
                    this.attr({
                        x: Math.min(Math.max(this.startX + dx, bBox.x), bBox.x + bBox.width - tBBox.width),
                        y: y
                    });
                }
            } else {

            }

        }, function (x, y) {
            this.startX = this.getBBox().x * 1;
            this.startY = this.getBBox().y * 1;
            this.startBBox = this.parentElement ? this.parentElement.getBBox() : '';
            this.parentElementstrokeWidth = this.parentElement ? this.parentElement.attr('strokeWidth').replace('px', '') * 2 : 0;
        })
    }

    //设置画布大小
    App.prototype.setPaperSize = function (params) {
        var scrollerWidth = params.width + (params.left * 2);
        var scrollerHeight = params.height + (params.top * 2);
        var $scroller = this.$rdViewport.parent();
        var $scrollerParent = $scroller.parent();
        var scrollerPW = $scrollerParent.width();
        var scrollerPH = $scrollerParent.height();
        var scrollLeft, scrollTop;
        this.$rdViewport.css({
            'left': params.left,
            'top': params.top,
            'width': params.width,
            'height': params.height
        });
        $scroller.css({
            'width': scrollerWidth,
            'height': scrollerHeight
        });
        scrollLeft = (scrollerWidth - scrollerPW) / 2;
        scrollTop = (scrollerHeight - scrollerPH) / 2;
        this.maxWidth = params.width;
        this.maxHeight = params.height;
        // this.setScrollerCenter({
        //     left: scrollLeft,
        //     top: scrollTop
        // });
        // this.rooms.paperSize = [params.width, params.height].join(',')
    }

    //设置画布居中
    App.prototype.setScrollerCenter = function (scroll) {
        var $scrollerP = this.$rdViewport.parent().parent()
        $scrollerP.scrollLeft(scroll.left);
        $scrollerP.scrollTop(scroll.top);
    }


    //获取所有json数据
    App.prototype.getRooms = function () {
        var superGroup = this.snap.select('.super-group');
        var groups = superGroup.children();
        var rooms = this.rooms;
        var list;
        var paperBgFill = RoomDefined.$data.paperBgColor == 'transparent' ? 'transparent' : (RoomDefined.$data.paperBgColor).colorRgb();
        rooms.data = {};
        rooms.data.list = [];
        list = rooms.data.list;

        rooms.data.background = {
            transform: 'matrix(1,0,0,1,0,0)',
            attr: {
                fill: paperBgFill,
                href: RoomDefined.$data.paperBgImage
            },
            shapeType: '',
            name: '',
            sort: '1'
        };

        groups.forEach(function (t, i) {
            var matrix = t.matrix;
            var rectParent = t.select('.rect-parent');
            var rpAttr = rectParent.attr();
            var roomAsset = t.selectAll('.rd-room-asset');
            var rotateGroup = t.select('.v-rotate');
            var rotateMatrix = rotateGroup.matrix;
            rpAttr.fill = rpAttr.fill.colorRgb();

            if(rectParent.type == 'image'){
                rpAttr.href = rectParent.data('url');
            }

            list.push({
                transform: 'matrix(1,0,0,1,' + matrix.e + ',' + matrix.f + ')',
                attr: rpAttr,
                shapeType: rectParent.type,
                type: t.data('type'),
                name: t.data('type') == 'trimming' ? rectParent.data('name') : t.data('name'),
                angle: rotateGroup.data('angle').toString(),
                sort: (i + 1).toString(),
                rotate: 'matrix(' + rotateMatrix.a + ',' + rotateMatrix.b + ',' + rotateMatrix.c + ',' + rotateMatrix.d + ',' + rotateMatrix.e + ',' + rotateMatrix.f + ')',
                roomId: t.data('roomId')
            });
            if (roomAsset.length) {
                list[i].children = [];
                roomAsset.forEach(function (t2, i2) {
                    var text = [], tspan, rgbAttr;
                    if (t2.type === 'text') {
                        tspan = t2.children();
                        tspan.forEach(function (t3) {
                            text.push(t3.node.innerHTML)
                        })
                    }
                    rgbAttr = t2.attr();
                    rgbAttr.fill = t2.attr().fill.colorRgb();
                    list[i].children.push({
                        attr: rgbAttr,
                        shapeType: t2.type,
                        type: t2.data('type'),
                        text: text.join('\n'),
                        name: t2.data('name'),
                        sort: (i2 + 1).toString()
                    })
                })
            }
        })
        return this.rooms;
    }

    //获取单个房间数据
    App.prototype.getRoomData = function (ele) {
        var data = {};
        var matrix = ele.matrix;
        var rectParent = ele.select('.rect-parent');
        var rpAttr = rectParent.attr();
        var roomAsset = ele.selectAll('.rd-room-asset');
        var fill = rpAttr.fill;
        var stroke = rectParent.attr('stroke');
        var strokeWidth;
        fill = fill.indexOf('rgba') > -1 ? 'transparent' : fill;

        if (stroke) {
            stroke = stroke.indexOf('rgba') > -1 || stroke === 'none' ? 'transparent' : stroke;
        }
        strokeWidth = rectParent.attr('strokeWidth').replace('px', '') * 1;
        data = {
            transform: 'matrix(1,0,0,1,' + matrix.e + ',' + matrix.f + ')',
            attr: rpAttr,
            fill: fill,
            ele: rectParent,
            g: ele,
            name: ele.data('type') === 'trimming' ? rectParent.data('name') : '',
            stroke: stroke,
            roomId: ele.data('roomId'),
            shapeType: rectParent.type,
            strokeWidth: strokeWidth,
            type: ele.data('type')
        };

        if (roomAsset.length) {
            data.children = [];
            roomAsset.forEach(function (t2, i) {
                var text = [], tspan, fill2, stroke2, strokeWidth2, fontFamily, fontSize, attr;
                attr = t2.attr();
                fill2 = attr.fill;
                fill2 = fill2.indexOf('rgba') > -1 ? 'transparent' : fill2;
                if (t2.type === 'text') {
                    fontFamily = t2.attr('fontFamily');
                    fontSize = t2.attr('fontSize').replace('px', '') * 1;
                    tspan = t2.children();
                    tspan.forEach(function (t3) {
                        text.push(t3.node.innerHTML)
                    });
                    data.children.push({
                        attr: attr,
                        fill: fill2,
                        ele: t2,
                        name: t2.data('name') || '',
                        fontFamily: fontFamily,
                        fontSize: fontSize,
                        shapeType: t2.type,
                        type: t2.data('type'),
                        text: text.join('\n')
                    })
                }

                if (t2.type === 'rect') {
                    stroke2 = t2.attr('stroke');
                    stroke2 = stroke2.indexOf('rgba') > -1 || stroke2 === 'none' ? 'transparent' : stroke2;
                    strokeWidth2 = t2.attr('strokeWidth').replace('px', '') * 1;
                    data.children.push({
                        attr: attr,
                        shapeType: t2.type,
                        type: t2.data('type'),
                        ele: t2,
                        fill: fill2,
                        name: t2.data('name') || '',
                        stroke: stroke2,
                        strokeWidth: strokeWidth2
                    })
                }

                if (t2.type === 'image') {
                    data.children.push({
                        attr: attr,
                        ele: t2,
                        name: t2.data('name') || '',
                        shapeType: t2.type,
                        type: t2.data('type'),
                        fill: fill2
                    })
                }
            })
        }
        return data;
    }

    //处理放大缩小事件
    App.prototype.resizeElementDrag = function () {
        var self = this;
        var rotateEle = this.rotateEle;
        var $parentBox = $('.rd-free-transform');
        this.resizeElements.on('mousedown', function (e) {
            var startX = e.clientX;
            var startY = e.clientY;
            var currentParentElement = self.currentParentElement;
            var rotateGroup = currentParentElement.select('.v-rotate');
            var rectParentElement = currentParentElement.select('.rect-parent');
            var BBox = rectParentElement.getBBox();
            var angle = rotateGroup.data('angle') * 1;
            var rectEH = BBox.height;
            var rectEW = BBox.width;
            var rectEY = currentParentElement.matrix.f * 1;
            var rectEX = currentParentElement.matrix.e * 1;
            var strokeWidth = currentParentElement.attr('strokeWidth').replace('px', '') * 1;
            var position = $(this).attr('class');
            var resizeBY = position === 'resize s';
            var resizeRX = position === 'resize e';
            var resizeTY = position === 'resize n';
            var resizeLX = position === 'resize w';
            var resizeTLXY = position === 'resize nw';
            var resizeBTXY = position === 'resize sw';
            var resizeTRXY = position === 'resize ne';
            var resizeBRXY = position === 'resize se';
            var strokeWidthDouble = strokeWidth * 2;
            var isImage = rectParentElement.type === 'image';
            $(document).on('mousemove.query', function (e) {
                var dx, dy, height, width, top, left;
                dy = (e.clientY - startY);
                dx = (e.clientX - startX);

                if (resizeBY) {
                    if (angle == 0 || angle == 180) {
                        height = Math.max(0, dy + strokeWidthDouble + rectEH);
                        width = rectEW;
                        top = rectEY;
                        left = rectEX
                    } else {
                        height = rectEH;
                        width = Math.max(0, dy + strokeWidthDouble + rectEW);
                        left = rectEX - dy / 2;
                        top = rectEY + dy / 2;
                    }
                }


                if (resizeRX) {
                    if (angle == 0 || angle == 180) {
                        width = Math.max(0, dx + strokeWidthDouble + rectEW);
                        height = rectEH;
                        top = rectEY;
                        left = rectEX;
                    } else {
                        width = rectEW;
                        height = Math.max(0, dx + strokeWidthDouble + rectEH);
                        left = rectEX + dx / 2;
                        top = rectEY - dx / 2;
                    }
                }

                if (resizeTY) {
                    if (angle == 0 || angle == 180) {
                        top = Math.max(0, rectEY + dy);
                        height = Math.max(0, -dy + strokeWidthDouble + rectEH);
                        width = rectEW;
                        left = rectEX
                    } else {
                        top = Math.max(0, rectEY + dy / 2);
                        width = Math.max(0, -dy + strokeWidthDouble + rectEW);
                        height = rectEH;
                        left = rectEX + dy / 2
                    }
                }

                if (resizeLX) {
                    if (angle == 0 || angle == 180) {
                        left = Math.max(0, rectEX + dx);
                        top = rectEY;
                        height = rectEH;
                        width = Math.max(0, -dx + strokeWidthDouble + rectEW);
                    } else {
                        left = Math.max(0, rectEX + dx / 2);
                        top = rectEY + dx / 2;
                        height = Math.max(0, -dx + strokeWidthDouble + rectEH);
                        width = rectEW;
                    }
                }

                if (resizeTLXY && !isImage) {
                    if (angle == 0 || angle == 180) {
                        left = Math.max(0, rectEX + dx);
                        top = Math.max(0, rectEY + dy);
                        height = Math.max(0, -dy + strokeWidthDouble + rectEH);
                        width = Math.max(0, -dx + strokeWidthDouble + rectEW);
                    } else {
                        left = rectEX + dy / 2 + dx / 2;
                        top = rectEY + dx / 2 + dy / 2;
                        height = Math.max(0, -dx + strokeWidthDouble + rectEH);
                        width = Math.max(0, -dy + strokeWidthDouble + rectEW);
                    }
                }
                if (resizeTLXY && isImage) {
                    if (angle == 0 || angle == 180) {
                        left = Math.max(0, rectEX + dy);
                        top = Math.max(0, rectEY + dy);
                        height = Math.max(0, -dy + strokeWidthDouble + rectEH);
                        width = Math.max(0, -dy + strokeWidthDouble + rectEW);
                    } else {
                        left = rectEX + dy / 2 + dx / 2;
                        top = rectEY + dx / 2 + dy / 2;
                        height = Math.max(0, -dy + strokeWidthDouble + rectEH);
                        width = Math.max(0, -dy + strokeWidthDouble + rectEW);
                    }
                }
                //待优化
                if (resizeTRXY && !isImage) {
                    if (angle == 0 || angle == 180) {
                        left = rectEX;
                        top = Math.max(0, rectEY + dy);
                        height = Math.max(0, -dy + strokeWidthDouble + rectEH);
                        width = Math.max(0, dx + strokeWidthDouble + rectEW);
                    } else {
                        height = Math.max(0, dx + strokeWidthDouble + rectEH);
                        width = Math.max(0, -dy + strokeWidthDouble + rectEW);
                        left = rectEX + dy / 2 + dx / 2;
                        top = rectEY - dx / 2 + dy / 2;
                    }
                }

                if (resizeTRXY && isImage) {
                    if (angle == 0 || angle == 180) {
                        left = rectEX;
                        top = Math.max(0, rectEY + dy);
                        height = Math.max(0, -dy + strokeWidthDouble + rectEH);
                        width = Math.max(0, -dy + strokeWidthDouble + rectEW);
                    } else {
                        height = Math.max(0, -dy + strokeWidthDouble + rectEH);
                        width = Math.max(0, -dy + strokeWidthDouble + rectEW);
                        left = rectEX + dy / 2 - dy / 2;
                        top = rectEY + dy / 2 + dy / 2;
                    }
                }

                if (resizeBRXY && !isImage) {
                    if (angle == 0 || angle == 180) {
                        left = rectEX;
                        top = rectEY;
                        height = Math.max(0, dy + strokeWidthDouble + rectEH);
                        width = Math.max(0, dx + strokeWidthDouble + rectEW);
                    } else {
                        height = Math.max(0, dx + strokeWidthDouble + rectEH);
                        width = Math.max(0, dy + strokeWidthDouble + rectEW);
                        left = rectEX - dy / 2 + dx / 2;
                        top = rectEY - dx / 2 + dy / 2;
                    }
                }

                if (resizeBRXY && isImage) {
                    if (angle == 0 || angle == 180) {
                        left = rectEX;
                        top = rectEY;
                        height = Math.max(0, dx + strokeWidthDouble + rectEH);
                        width = Math.max(0, dx + strokeWidthDouble + rectEW);
                    } else {
                        height = Math.max(0, dx + strokeWidthDouble + rectEH);
                        width = Math.max(0, dx + strokeWidthDouble + rectEW);
                        left = rectEX - dx / 2 + dx / 2;
                        top = rectEY - dx / 2 + dx / 2;
                    }
                }

                if (resizeBTXY && !isImage) {
                    if (angle == 0 || angle == 180) {
                        left = Math.max(0, rectEX + dx);
                        top = rectEY;
                        height = Math.max(0, dy + strokeWidthDouble + rectEH);
                        width = Math.max(0, -dx + strokeWidthDouble + rectEW);
                    } else {
                        height = Math.max(0, -dx + strokeWidthDouble + rectEH);
                        width = Math.max(0, dy + strokeWidthDouble + rectEW);
                        left = rectEX - dy / 2 + dx / 2;
                        top = rectEY + dx / 2 + dy / 2;
                    }
                }

                if (resizeBTXY && isImage) {
                    if (angle == 0 || angle == 180) {
                        left = Math.max(0, rectEX + dx);
                        top = rectEY;
                        height = Math.max(0, -dx + strokeWidthDouble + rectEH);
                        width = Math.max(0, -dx + strokeWidthDouble + rectEW);
                    } else {
                        height = Math.max(0, -dx + strokeWidthDouble + rectEH);
                        width = Math.max(0, -dx + strokeWidthDouble + rectEW);
                        left = rectEX + dx / 2 + dx / 2;
                        top = rectEY + dx / 2 - dx / 2;
                    }
                }


                rectParentElement.attr({
                    width: width,
                    height: height
                });


                currentParentElement.attr('transform', 'matrix(1,0,0,1,' + left + ',' + top + ')');

                rotateGroup.attr('transform', 'rotate(' + angle + ',' + width / 2 + ',' + height / 2 + ')');

                $parentBox.css({
                    'left': left - strokeWidthDouble,
                    'top': top - strokeWidthDouble,
                    'width': width + strokeWidthDouble,
                    'height': height + strokeWidthDouble
                });
                if (angle == 0 || angle == 180) {
                    rotateEle.css({
                        'left': left,
                        'top': top,
                        'width': width,
                        'height': height
                    })
                } else {
                    rotateEle.css({
                        'left': (left + width / 2 - height / 2),
                        'top': (top + height / 2 - width / 2),
                        'width': height,
                        'height': width
                    })
                }


            });

            $(document).on('mouseup.query', function () {
                $(document).off('mousemove.query');
                $(document).off('mouseup.query');
            })
        })
    };


    App.DEFAULT = {
        elId: '#rdPaper',
        paperSize: 'auto,auto',
        resizeEle: '.rd-free-transform',
        boxMinHeightValue: 30,
        boxMinWidthValue: 30,
        transform: 'matrix(1,0,0,1,0,0)',
        data: {
            background: {
                // type: 'background',
                // shapeType: 'rect',
                // sort: '0',
                // name: '背景',
                // transform: 'matrix(1,0,0,1,0,0)',
                // attr: {
                //     href: '/image/bg.jpg',
                //     style: 'stroke-width: none;',
                //     fill: '#f0e1c7',
                //     x: 0,
                //     y: 0,
                //     width: 1000,
                //     height: 1000
                // }
            },
            list: []
            // // list: [{
            //     type: 'room',
            //     shapeType: 'rect',
            //     roomId: '',
            //     transform: 'matrix(1,0,0,1,100,100)',
            //     attr: {
            //         style: 'stroke-width: 2px;',
            //         fill: 'transparent',
            //         stroke: '#222138',
            //         x: 0,
            //         y: 0,
            //         width: 300,
            //         height: 300
            //     },
            //     children: [{
            //         type: 'roomAsset',
            //         shapeType: 'text',
            //         text: '房间名',
            //         attr: {
            //             fill: '#e9442d',
            //             x: 190,
            //             y: 130,
            //             style: 'font-size: 12px;font-family:Arial;text-anchor:middle;'
            //         }
            //     }]
            // }]
        }
    };


    window.AppPaper = App;
})(window, Snap, jQuery);
