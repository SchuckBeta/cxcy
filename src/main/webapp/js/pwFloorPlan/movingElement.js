+function ($, Vue) {
    var movingElement = Vue.component('moving-element', {
        template: '<div v-show="visible" class="room-ele_moving_box" :style="moveStyle" :class="[customClass]">\n' +
        '        <div class="room-element_moving">\n' +
        '            <div class="room-element_pic" draggable="false">\n' +
        '                <img :src="element.base64Code" draggable="false">\n' +
        '            </div>\n' +
        '            <span v-show="false" class="room-element_name" draggable="false">{{element.name}}</span>\n' +
        '        </div>\n' +
        '    </div>',
        props: {
            visible: {
                type: Boolean,
                default: false
            },
            element: Object,
            customClass: String,
            moveStyle: Object
        }
    });

}(jQuery, Vue)