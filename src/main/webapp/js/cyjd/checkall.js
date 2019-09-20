/**
 * Created by Administrator on 2018/7/11.
 */


'use strict';

function CheckAll(elements, option) {
    this.$elements = $(elements);
    this.options = $.extend({}, CheckAll.DEFAULT, option);
    this.$checkAllSource =  $(this.options.checkAllSource);
    this.init();

}


CheckAll.DEFAULT = {
    isCheckedAll: false,
    checkAllSource: 'input[data-toggle="checkall"]'
}

CheckAll.prototype.init = function () {
    this.validSource();
    this.checkedAll();
    this.handleChangeElements();
    this.handleChangeCheckAllElement();
}

CheckAll.prototype.validSource = function () {
  if(this.$checkAllSource.length != 1){
      throw Error('请检查input checkall');
  }
};

CheckAll.prototype.checkedAll = function () {
    var isCheckedAll = this.options.isCheckedAll;
    if(!isCheckedAll){
        return;
    }
    this.$elements.each(function (index, item) {
        $(item).prop('checked', true);
    });
    this.$checkAllSource.prop('checked', true);
}


CheckAll.prototype.everyChecked = function () {
    var checked = true;
    this.$elements.each(function (index, item) {
        if(!$(item).prop('checked')){
            checked = false;
            return false;
        }
    })
    return checked;
}

CheckAll.prototype.someChecked = function () {
    var checked = false;
    this.$elements.each(function (index, item) {
        if($(item).prop('checked')){
            checked = true;
            return false;
        }
    })
    return checked;
}

CheckAll.prototype.handleChangeElements =function () {
    var self = this;
    var $checkAllSource = this.$checkAllSource;
    this.$elements.on('change', function (e) {
        $checkAllSource.prop('checked', self.everyChecked());
    })
}

CheckAll.prototype.handleChangeCheckAllElement =function () {
    var $elements = this.$elements;
    this.$checkAllSource.on('change', function (e) {
        var $target = $(this);
        $elements.each(function (index, item) {
            $(item).prop('checked', $target.prop('checked'))
        })
    })
}

CheckAll.prototype.getCheckedIds =function () {
    var $elements = this.$elements;
    var ids = [];
    this.$elements.each(function(index, item){
    	if($(item).prop('checked')){
    		ids.push($(item).val())
    	}
    })
    return ids;
}

$(function () {
    window['checkAllInstance'] = new CheckAll($('input[data-toggle="checkitem"]'))
})