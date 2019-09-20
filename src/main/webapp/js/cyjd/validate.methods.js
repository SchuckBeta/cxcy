/**
 * Created by Administrator on 2017/11/27.
 */



jQuery.validator.addMethod("phone_number", function (value, element) {
    var length = value.length;
    return this.optional(element) || (length == 11 && ((/^0?(13[0-9]|15[012356789]|18[0-9]|17[0-9])[0-9]{8}$/).test(value)));
}, "请正确填写您的手机号码");