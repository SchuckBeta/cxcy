/**
 * Created by Administrator on 2017/11/13.
 */

Vue.directive('generate-text', {
    // inserted: function (el, binding, vnode) {
    //     var width = binding.value.width;
    //     var height = binding.value.height;
    //     var text = binding.value.resource;
    //     var canvas = document.createElement('canvas');
    //     var ctx = canvas.getContext('2d');
    //     var src;
    //     canvas.width = 48*4;
    //     canvas.height = 50;
    //     ctx.font = 'bold 48px Microsoft YaHei';
    //     ctx.textAlign = "left";
    //     ctx.strokeStyle = '#333';
    //     ctx.strokeText(text, 0, 48);
    //     ctx.fillStyle = '#333';
    //     ctx.fillText(text,0,48);
    //     src = canvas.toDataURL('image/png');
    //     el.src = src;
    //     canvas = null;
    // },
    update: function (el, binding, vnode) {
        // var width = binding.value.width;
        // var height = binding.value.height;
        var fontSize, font, text, len, width;
        var canvas = document.createElement('canvas');
        var ctx = canvas.getContext('2d');
        var src;
        if (!binding.value.data.resource) {
            canvas = null;
            ctx = null;
            return false
        }
        fontSize = binding.value.data.fontSize;
        font = binding.value.data.font;
        text = binding.value.data.resource;
        len = text.length;
        canvas.width = width = len * fontSize;
        canvas.height = +fontSize + 10;
        ctx.font = 'bold ' + fontSize + 'px ' + font;
        ctx.textAlign = "left";
        ctx.strokeStyle = '#333';
        ctx.strokeText(text, 0, +fontSize);
        ctx.fillStyle = '#333';
        ctx.fillText(text, 0, +fontSize);
        src = canvas.toDataURL('image/png');
        el.src = src;
        vnode.context.data.width = width * ( binding.value.ratio || 1);
        canvas = null;
    }
});