Vue.directive('generate-room-asset', {
    inserted: function (element, binding, vnode) {
        var snap = Snap(element);
        var shapeData = binding.value.shapeData;
        var paperId = binding.value.id;
        var dragSnap = Snap(paperId);
        var superG = snap.group();
        var imageShapeI = 0;
        var images = [];
        var imgLen;

        shapeData.forEach(function (t, i) {
            if(t.shapeType === 'image'){
                t.imageUrl = t.attr.href;
                images.push({
                    imageObj : new Image(),
                    src: t.attr.href,
                    i: i
                })
            }
        });
        imgLen = images.length;
        images.forEach(function (t) {
            t.imageObj.onload = function () {
                var canvas = document.createElement('canvas');
                var ctx = canvas.getContext('2d');
                var width = t.imageObj.naturalWidth;
                var height = t.imageObj.naturalHeight;
                var dataURL;
                canvas.width = width;
                canvas.height = height;
                ctx.drawImage(t.imageObj,0,0, width, height);
                dataURL = canvas.toDataURL();
                shapeData[t.i].attr.href = dataURL;
                imageShapeI++;
                t.imageObj.onload = null;
                t.imageObj = null;
                vnode.context.hrefMaps[t.src] = dataURL;
                if(imageShapeI == imgLen - 1){
                    generateShow();
                    images = null;
                    // vnode.context.trimmingBase64 = shapeData;
                    //完成回调
                    // vnode.context.base64Complete();
                }
            };
            t.imageObj.src = t.src;
        });


        function generateShape(shapeType, position, content) {
            var text;
            switch (shapeType) {
                case 'rect':
                    return snap.rect(position[0], position[1], position[2], position[3]);
                case 'image':
                    return snap.image(content, position[0], position[1], position[2], position[3]);
                case 'text':
                    text = snap.text(position[0], position[1], content.split('\n'));
                    text.children().forEach(function (t) {
                        t.attr({
                            x: position[0],
                            dy: '1em'
                        })
                    });
                    return text;
            }
        }


        function generateShow() {
            shapeData.forEach(function (t) {
                var cShapeType = t.shapeType;
                var content = cShapeType === 'text' ? t.text : t.attr.href || '';
                var position = [t.attr.x, t.attr.y, t.attr.width, t.attr.height];
                var shape = generateShape(cShapeType, position, content).attr(t.attr);
                var children = t.children;
                var group = snap.group();
                var nameShape;
                var namePosition;
                group.add(shape);
                group.attr('transform', t.transform);
                superG.add(group);
                if (children && children.length > 0) {
                    children.forEach(function (t2) {
                        var cShapeType = t2.shapeType;
                        var position2 = [t2.attr.x, t2.attr.y, t2.attr.width, t2.attr.height];
                        var content = cShapeType === 'text' ? t2.text : t2.attr.href || '';
                        var cShape = generateShape(cShapeType, position2, content).attr(t2.attr);
                        group.add(cShape)
                    })
                }else {
                    namePosition = [t.attr.x, t.attr.y];
                    nameShape = generateShape('text', namePosition, t.name).attr(t.nameAttr);
                    group.add(nameShape)
                }



                group.drag(function (dx, dy, x, y, event) {
                    var width = this.getBBox().width;
                    var height = this.getBBox().height;
                    vnode.context.paperDrag.style = {
                        'left': event.clientX - width / 2 + 'px',
                        'top': event.clientY - height / 2 + 'px',
                        'width': width + 'px',
                        'height': height + 'px'
                    };
                }, function (x, y, event) {
                    var width = this.getBBox().width;
                    var height = this.getBBox().height;
                    var clone = this.clone();
                    var children = clone.children();
                    vnode.context.paperDrag.drag = true;
                    vnode.context.paperDrag.style = {
                        'left': event.clientX - width / 2 + 'px',
                        'top': event.clientY - height / 2 + 'px',
                        'width': width + 'px',
                        'height': height + 'px'
                    };
                    clone.attr({
                        'x': 0,
                        'y': 0
                    })
                    children.forEach(function (t) {
                        if(t.type !== 'text'){
                            t.attr({
                                'x': 0,
                                'y' : 0
                            })
                        }
                    })
                    dragSnap.append(clone.attr('transform', 'matrix(1,0,0,1,0,0)'));

                }, function (event) {
                    var target = event.target;
                    var snapTarget;
                    var parentEleGroup;
                    var parentId;

                    snapTarget = Snap(target);

                    if (event.clientX <= 240 || event.clientX > $('.rd-inspector').offset().left) {
                        dragSnap.clear();
                        return false;
                    }

                    //出现在Svg上面
                    if (t.type === 'roomAsset') {
                        if (snapTarget.data('type') === 'room' || snapTarget.data('type') === 'roomAsset' || (snapTarget.type === 'tspan' && snapTarget.parent().data('type') === 'roomAsset')) {
                            if (snapTarget !== this) {
                                parentId = snapTarget.parent().data('parentId') || snapTarget.parent().hasClass('v-2');
                                if (typeof parentId === 'string') {
                                    parentEleGroup = snapTarget.parent().parent()
                                } else {
                                    parentEleGroup = snapTarget.parent()
                                }
                                vnode.context.roomAssetActiveData = t;
                                vnode.context.addRoomAssetToSvg(parentEleGroup, target);
                            } else {
                                dragSnap.clear();
                            }
                        } else {
                            dragSnap.clear();
                        }
                    } else if (t.type === 'trimming') {
                        if ($(target).parents('#rdPaper').size() > 0 || target.id === 'rdPaper') {
                            if (snapTarget !== this) {
                                vnode.context.roomAssetActiveData = t;
                                vnode.context.addTrimmingToSvg();
                            }
                        }
                    }

                    dragSnap.clear();
                    vnode.context.paperDrag.drag = true;
                })
            })
        }



    }
});