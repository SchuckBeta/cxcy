Vue.directive('generate-room', {
    inserted: function (element, binding, vnode) {
        var snap = Snap(element);
        var shapeData = binding.value.shapeData;
        var paperId = binding.value.id;
        var dragSnap = Snap(paperId);


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


        shapeData.forEach(function (t) {
            var group = snap.group();
            var position = [t.attr.x, t.attr.y, t.attr.width, t.attr.height];
            var shape = generateShape(t.shapeType, position).attr(t.attr);
            var children = t.children;
            group.add(shape);
            group.attr('transform', t.transform);
            if (children.length > 0) {
                children.forEach(function (t2) {
                    var cShapeType = t2.shapeType;
                    var position2 = [t2.attr.x, t2.attr.y, t2.attr.width, t2.attr.height];
                    var content = cShapeType === 'text' ? t2.text : t2.attr.href || '';
                    var cShape = generateShape(cShapeType, position2, content).attr(t2.attr);
                    group.add(cShape)
                })
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
                });
                dragSnap.append(clone.attr('transform', 'matrix(1,0,0,1,0,0)'));
            }, function (event) {
                var target = event.target;
                if (event.clientX <= 240 || event.clientX > $('.rd-inspector').offset().left) {
                    dragSnap.clear();
                    return false;
                }
                if ($(target).parents('#rdPaper').size() > 0 || target.id === 'rdPaper') {
                    if(t.type === 'room'){
                        vnode.context.roomActiveData = t;
                        vnode.context.addRoomToSvg(t);
                    }
                }
                dragSnap.clear();
                vnode.context.paperDrag.drag = false;
            })
        })

    }
});