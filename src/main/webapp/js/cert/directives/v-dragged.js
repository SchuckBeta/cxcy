/*
 * v-dragged v0.0.3
 * https://github.com/zhanziyang/v-dragged
 *
 * Copyright (c) 2017 zhanziyang
 * Released under the ISC license
 */

var draggedElem;
var eventListener = {
    addEventListeners: function addEventListeners(el, events, handler) {
        for (var i = 0, len = events.length; i < len; i++) {
            el.addEventListener(events[i], handler);
        }
    },
    removeEventListeners: function removeEventListeners(el, events, handler) {
        for (var i = 0, len = events.length; i < len; i++) {
            el.removeEventListener(events[i], handler);
        }
    }
};

Vue.directive('dragged', {
    inserted: function (el, binding, vnode) {
        var direction;
        if (!document) {
            return false;
        }

        function onPointerStart(evt) {
            el.lastCoords = el.firstCoords = {
                x: evt.clientX,
                y: evt.clientY
            };
            binding.value({
                el: el,
                first: true,
                clientX: evt.clientX,
                clientY: evt.clientY
            });
            draggedElem = el;
        }

        function onPointerEnd(evt) {
            if (el !== draggedElem) return;
            evt.preventDefault();
            el.lastCoords = null;
            binding.value({
                el: el,
                last: true,
                clientX: evt.clientX,
                clientY: evt.clientY,
                direction:direction
            });
            draggedElem = null;
        }

        function onPointerMove(evt) {
            if (el !== draggedElem) return;
            evt.preventDefault();
            if (el.lastCoords) {
                var deltaX = evt.clientX - el.lastCoords.x;
                var deltaY = evt.clientY - el.lastCoords.y;
                var offsetX = evt.clientX - el.firstCoords.x;
                var offsetY = evt.clientY - el.firstCoords.y;
                var clientX = evt.clientX;
                var clientY = evt.clientY;

                binding.value({
                    el: el,
                    deltaX: deltaX,
                    deltaY: deltaY,
                    offsetX: offsetX,
                    offsetY: offsetY,
                    clientX: clientX,
                    clientY: clientY
                });
                el.lastCoords = {
                    x: evt.clientX,
                    y: evt.clientY
                };
                direction = deltaX > 0 ? 'right' : 'left';
            }
        }

        eventListener.addEventListeners(el, ['mousedown'], onPointerStart);
        eventListener.addEventListeners(document.documentElement, ['mouseup'], onPointerEnd);
        eventListener.addEventListeners(document.documentElement, ['mousemove'], onPointerMove);
    },
    unbind: function (el) {
        eventListener.removeEventListeners(el, ['mousedown']);
        eventListener.removeEventListeners(document.documentElement, ['mouseup']);
        eventListener.removeEventListeners(document.documentElement, ['mousemove']);
    }
});