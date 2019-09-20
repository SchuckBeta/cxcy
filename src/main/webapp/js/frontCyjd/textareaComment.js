/**
 * Created by Administrator on 2018/9/25.
 */
'use strict';


function TextareaComment(element, option) {
    this.$element = $(element);
    this.options = $.extend({}, TextareaComment.DEFAULT, option);
    this.$textarea = this.$element.find('textarea');
    this.$reset = this.$element.find('.reset');
    this.$btnSubmit = this.$element.find('.btn-submit');
    this.$unLoginComment = this.$element.find('.un-login-comment')
    this.validText = false;
    this.init();
}

TextareaComment.DEFAULT = {
    maxLength: 500,
    change: function (event, value) {

    }
}

TextareaComment.prototype.init = function () {
    this.showNoUser();
    this.addEventReset();
    this.addEventTextarea();
    this.addEventBtnSubmit();
}

TextareaComment.prototype.showNoUser = function () {
    var userId = this.options.userId;
    if (!userId) {
        this.$unLoginComment.show();
    }
}

TextareaComment.prototype.addEventTextarea = function () {
    var self = this;
    var $textarea = this.$textarea;
    var $btnSubmit = this.$btnSubmit;
    $textarea.on({
        'blur': function () {
            self.textareaEvent();
        },
        'input propertychange': function () {
            if ($btnSubmit.hasClass('is-disabled')) {
                self.textareaEvent();
            }
        }
    })
}

TextareaComment.prototype.textareaEvent = function () {
    var $textarea = this.$textarea;
    var $btnSubmit = this.$btnSubmit;
    var options = this.options;
    var value = $textarea.val();
    var maxLength = options.maxLength;
    if (!value) {
        $btnSubmit.prop('disabled', true).addClass('is-disabled');
        return false;
    }
    this.validText = value.length <= maxLength;
    if (!this.validText) {
        if ($textarea.next().length < 1) {
            $('<p style="color: red; margin-top: 4px; margin-bottom: 0" class="error invalid-text">请不要超出' + options.maxLength + '字符</p>').insertAfter($textarea);
        }
        return false;
    }
    $textarea.next().remove();
    $btnSubmit.prop('disabled', false).removeClass('is-disabled');
}

TextareaComment.prototype.addEventBtnSubmit = function () {
    var options = this.options;
    var self = this;
    var $textarea = this.$textarea;
    this.$btnSubmit.on('click', function (event) {
        event.stopPropagation();
        event.preventDefault();
        if (self.validText) {
            options.change && options.change.call(self, event, $textarea.val())
        }
    })
}

TextareaComment.prototype.addEventReset = function () {
    var $textarea = this.$textarea;
    var self = this;
    this.$reset.on('click', function (event) {
        event.stopPropagation();
        event.preventDefault();
        self.reset();
    })
}

TextareaComment.prototype.reset = function () {
    var $textarea = this.$textarea;
    $textarea.val('');
    this.textareaEvent();
}