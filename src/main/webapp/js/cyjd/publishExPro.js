/**
 * Created by Administrator on 2018/7/11.
 */

'use strict';

function PublishExPro(element) {
    this.$element = $(element);
    this.checkAllInstance = null;
    this.$checkboxElements = [];
    this.init();
}


PublishExPro.prototype.init = function () {
    if (!this.hasCheckAllInstance()) {
        throw Error('required checkall.js')
    }
    this.getCheckAllInstance();
    this.get$checkboxElements();
    this.handleCheckboxesChange();
    this.handleChangeElement();
}

PublishExPro.prototype.hasCheckAllInstance = function () {
    return !!window['checkAllInstance'];
}

PublishExPro.prototype.getCheckAllInstance = function () {
    return this.checkAllInstance = window['checkAllInstance'];
}

PublishExPro.prototype.get$checkboxElements = function () {
    var $elements = this.checkAllInstance.$elements;
    var $checkAllSource = this.checkAllInstance.$checkAllSource;
    this.$checkboxElements = [].concat.call($elements, $checkAllSource)
    return this.$checkboxElements;
}
PublishExPro.prototype.someChecked = function () {
    return this.checkAllInstance.someChecked();
}
PublishExPro.prototype.handleCheckboxesChange = function () {
    var self = this;
    $(this.$checkboxElements).each(function (index, item) {
        item.on('change', function () {
            self.$element.prop('disabled', !self.someChecked())
        })
    })
}
PublishExPro.prototype.handleChangeElement = function () {
    var self = this;
    this.$element.on('click', function (e) {
        e.stopPropagation();
        e.preventDefault();
        var ids = self.checkAllInstance.getCheckedIds();
        confirmx("确定发布所选项目？", function () {
             $.ajax({
                 type: 'post',
                 // url: '/a/excellent/resall',
                 url: '/a/cms/cmsArticle/excellent/projectPublish',
                 dataType: "json",
                 data: {
                     // fids: ids.join(',')
                     ids: ids.join(',')
                 },
                 success: function (data) {
                     alertx(data.status == '1' ? '发布成功' : '发布失败');
                 }
             });
        })
    })
}


$(function () {
    new PublishExPro('button[data-toggle="publishExPro"]')
})
