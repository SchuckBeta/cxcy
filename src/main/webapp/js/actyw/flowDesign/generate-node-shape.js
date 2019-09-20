+function () {

    function getTranslateEle(ele) {
        var translate;
        while (ele){
            var hasTranslate = ele.hasClass('v-translate');
            if(hasTranslate){
                translate = ele;
                break;
            }
            ele = ele.parent();
        }
        return ele;
    }



    function nodeMessages() {
        return {
            'StartNoneEvent': '子流程或者流程的开始' ,
            'EndTerminateEvent':'流程终止',
            'EndNoneEvent': '流程的结束',
            'UserTask': '用户任务，必须被人为触发（完成任务的动作）',
            'SubProcess': '分组用户任务',
            'ExclusiveGateway': '流程的走向'
        }
    }


    Vue.directive('generate-node', {
        inserted: function (element, binding, vnode) {
            var snap = Snap(element);
            var nodeList = binding.value.nodeList;
            var hasUser = binding.value.hasUser;
            var overallGroup = snap.group();
            var translateElements;
            var startX, startY, lastX, lastY;
            nodeList.forEach(function (t) {
                var nodeJson = JSON.parse(t.uiJson);
                var translateGroup = snap.group().addClass('v-translate');
                var rotateGroup = snap.group().addClass('v-rotate');
                var scaleGroup = snap.group().addClass('v-scale');
                var children = nodeJson.children;
                var title = nodeJson.title;
                var auditRoles = nodeJson.auditRoles;
                var auditUsers = nodeJson.auditUsers;
                var titleTranslateGroup, titleScaleGroup;
                var titleChildren;
                var roles, auditGroup, auditRoleGroup, auditChildren;
                var users, auditUGroup, auditUserGroup, auditUChildren;
                scaleGroup.attr('transform', nodeJson.scale);
                translateGroup.attr('transform', nodeJson.translate);
                translateGroup.attr('model-key', t.nodeKey)
                rotateGroup.attr('transform', nodeJson.rotate);
                translateGroup.attr('model-type', t.type);
                children.forEach(function (t2) {
                    var shape;
                    if (t2.type !== 'text') {
                        shape = snap[t2.type]();
                        shape.attr(t2.attr);
                        rotateGroup.add(scaleGroup);
                        scaleGroup.add(shape);
                        if(t2.controlSize){
                            shape.addClass('v-shape')
                        }
                    }
                    if (t2.type === 'text') {
                        shape = snap.text(0, 0, t.name)
                        shape.attr(t2.attr);
                        // shape.node.innerHTML = '<tspan>' + t2.text + '</tspan>';
                        rotateGroup.add(shape)
                    }
                    shape.attr('transform', t2.translate);
                });
                if (title) {
                    titleTranslateGroup = snap.group().addClass('v-title');
                    titleScaleGroup = snap.group();
                    titleTranslateGroup.attr('transform', title.translate);
                    titleTranslateGroup.add(titleScaleGroup);
                    titleScaleGroup.attr('transform', title.scale);
                    titleScaleGroup.addClass('v-title-scale');
                    titleChildren = title.children;
                    if(t.type == 40){
                        titleTranslateGroup.addClass('v-title-sub-process')
                    }
                    titleChildren.forEach(function (t2) {
                        var shape;
                        if (t2.type === 'text') {
                            shape = snap.text(0, 0, t2.text)
                            shape.attr(t2.attr);

                            // shape.node.innerHTML = '<tspan>' + t2.text + '</tspan>';
                            titleTranslateGroup.add(shape)
                        }
                        if (t2.type !== 'text') {
                            shape = snap[t2.type]();
                            shape.attr(t2.attr);
                            shape.addClass('v-shape');
                            titleScaleGroup.add(shape);
                        }

                        shape.attr('transform', t2.translate);
                    });
                    rotateGroup.add(titleTranslateGroup)
                }

                if (auditRoles) {
                    auditGroup = snap.group().addClass('v-audit-role-container');
                    auditGroup.attr('transform', auditRoles.translate);
                    auditRoleGroup = snap.group().addClass('v-audit-roles');
                    auditChildren = auditRoles.children;
                    roles = auditRoles.roles;
                    auditChildren.forEach(function (t2) {
                        var shape;

                        if (t2.type === 'text') {
                            shape = snap.text(0, 0, t2.text)
                            // shape.attr(t2.attr);
                            // shape.node.innerHTML = '<tspan>' + t2.text + '</tspan>';
                        }
                        if (t2.type !== 'text') {
                            shape = snap[t2.type]();
                            shape.addClass('v-shape')
                        }
                        shape.attr(t2.attr);
                        shape.attr('transform', t2.translate)
                        auditGroup.add(shape)
                    });
                    roles.forEach(function (t2) {
                        var shape;

                        if (t2.type === 'text') {
                            shape = snap.text(0, 0, t2.text)
                            // shape.attr(t2.attr);
                            // shape.node.innerHTML = '<tspan>' + t2.text + '</tspan>';
                        }
                        if (t2.type !== 'text') {
                            shape = snap[t2.type]();
                            shape.addClass('v-shape')
                        }
                        shape.attr(t2.attr);
                        shape.attr('transform', t2.translate)

                        auditRoleGroup.add(shape)
                    });
                    auditGroup.add(auditRoleGroup)
                    rotateGroup.add(auditGroup)
                }

                if (auditUsers) {
                    if (hasUser) {
                        auditUGroup = snap.group().addClass('v-audit-user-container');
                        auditUGroup.attr('transform', auditUsers.translate);
                        auditUserGroup = snap.group().addClass('v-audit-users');
                        auditUChildren = auditUsers.children;
                        users = auditUsers.users;
                        auditUChildren.forEach(function (t2) {
                            var shape;

                            if (t2.type === 'text') {
                                shape = snap.text(0, 0, t2.text)
                                // shape.attr(t2.attr);
                                // shape.node.innerHTML = '<tspan>' + t2.text + '</tspan>';
                            }
                            if (t2.type !== 'text') {
                                shape = snap[t2.type]();
                                shape.addClass('v-shape')
                            }
                            shape.attr(t2.attr);
                            shape.attr('transform', t2.translate)
                            auditUGroup.add(shape)
                        });
                        users.forEach(function (t2) {
                            var shape;

                            if (t2.type === 'text') {
                                shape = snap.text(0, 0, t2.text)
                                // shape.attr(t2.attr);
                                // shape.node.innerHTML = '<tspan>' + t2.text + '</tspan>';
                            }
                            if (t2.type !== 'text') {
                                shape = snap[t2.type]();
                                shape.addClass('v-shape')
                            }
                            shape.attr(t2.attr);
                            shape.attr('transform', t2.translate)
                            auditUserGroup.add(shape)
                        });
                        auditUGroup.add(auditUserGroup)
                        rotateGroup.add(auditUGroup)
                    }
                }

                translateGroup.add(rotateGroup);
                overallGroup.add(translateGroup)
            });
            translateElements = snap.selectAll('.v-translate');



            translateElements.forEach(function (t, i) {


                t.mouseover(function hover() {
                    var svg = this.parent().parent().node;
                    var offsetTop = $(svg).offset().top;
                    var BBox = this.getBBox();
                    var y = BBox.y;
                    var top = offsetTop + y;
                    vnode.context.tooltip.className = 'show right';
                    vnode.context.tooltip.style.top = top + 'px';
                    vnode.context.tooltip.style.left =  '175px';
                    vnode.context.tooltip.text = nodeMessages()[t.attr('model-key')]
                });

                t.mouseout(function hoverOut() {
                    vnode.context.tooltip.className = '';
                });

                t.drag(function move(dx, dy, x, y, event) {
                    lastX = startX + dx;
                    lastY = startY + dy;
                    vnode.context.toPaperData.style.left = lastX + 'px';
                    vnode.context.toPaperData.style.top = lastY + 'px';
                    var target, translate, modelType, snapTarget;
                    target = event.target;
                    if(target.nodeName === 'rect' || target.nodeName === 'rect'){
                        snapTarget = Snap(target);
                        translate = getTranslateEle(snapTarget);
                        modelType = this.attr('model-type');
                        if(translate != this){
                            if(modelType == translate.attr('model-type')){
                                // vnode.context.addableShow = true;
                                // vnode.context.addable = true;
                            }else {

                            }
                        }

                    }


                }, function start(x, y) {
                    var clone = this.clone();
                    var BBox = this.getBBox();
                    var left = x - BBox.width / 2;
                    var top = y - BBox.height / 2;

                    startX = left;
                    startY = top;

                    clone.attr('transform', 'matrix(1,0,0,1,0,0)')

                    vnode.context.toPaperData.show = true;
                    vnode.context.moveClone = clone;
                    vnode.context.toPaperData.style = {
                        left: left + 'px',
                        top: top + 'px',
                        width: BBox.width + 'px',
                        height: BBox.height + 'px'
                    };
                    vnode.context.movePaper.append(clone);
                }, function end(event) {
                    vnode.context.toPaperData.show = true;
                    vnode.context.movePaper.clear();
                    var left = parseInt(vnode.context.toPaperData.style.left);
                    var winWidth = $(window).width();
                    var stenWidth = $('.stencil-container').width();
                    var inWidth = $('.inspector-container').width();
                    var conWidth = $('.paper-container').width();
                    var viewWidth = parseInt(vnode.context.viewport.width);
                    var width = this.getBBox().width;
                    var scrollLeft = $('.paper-scroller').scrollLeft();
                    var jl;
                    var target, translateGroup, modelType, snapTarget, targetEle;
                    scrollLeft = scrollLeft < 0 ? 0 : (100 - scrollLeft);
                    jl = conWidth - viewWidth - scrollLeft;
                    jl = jl > 0 ? jl : 0;

                    if (left < vnode.context.stencilWidth) {
                        return
                    }
                    //
                    // if (left > winWidth - inWidth - jl - width / 2) {
                    //     return
                    // }
                    target = event.target;
                    if(target.getAttribute('id') === 'flowDesignSvg'){
                        targetEle = vnode.context.overallGroup;
                        vnode.context.toDesignPaper(vnode.context.moveClone, nodeList[i], targetEle, true);
                    }else {
                        if(target.nodeName === 'rect' || target.nodeName === 'rect'){
                            snapTarget = Snap(target);
                            translateGroup = getTranslateEle(snapTarget);
                            modelType = this.attr('model-type');
                            if(translateGroup != this){
                                if(modelType == translateGroup.attr('model-type')){
                                    // vnode.context.addableShow = true;
                                    // vnode.context.addable = true;
                                }else {

                                }
                            }

                        }
                        targetEle = translateGroup;
                        vnode.context.toDesignPaper(vnode.context.moveClone, nodeList[i], targetEle, false);
                    }




                })
            })
        }
    });
}(jQuery);