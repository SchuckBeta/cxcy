/**
 * Created by qingtengwang on 2017/7/6.
 */


!function ($) {
    "use strict";
    function SortTable(element, option) {
        this.$element = $(element);
        this.options = $.extend({}, SortTable.DEFAULT, option);
        this.sortStatus = null;
    }

    SortTable.DEFAULT = {
        formId:'#searchForm',
        statusClass:{
            desc: 'icon-sort-down',
            asc: 'icon-sort-up'
        }
    };

    SortTable.prototype.initStatus = function () {
        var options = this.options;
        var name = options.name;
        var status = options.status;
        var $form = $(options.formId);

        if(!name && !status){
            return false;
        }

        //添加input至form表单
        $form.append($('<input type="hidden" name="orderBy" value="'+name+'"><input type="hidden" name="orderByType" value="'+status+'">'));
        //初始化table-sort状态
        this.$element.find('i').addClass(options.statusClass[status]);
        this.sortStatus = status;
    };

    SortTable.prototype.sortAsc = function(){
        var options = this.options;
        var $form = $(options.formId);
        var name = options.name;
        this.sortStatus = 'asc';
        this.$element.find('i').addClass(options.statusClass[this.sortStatus]).removeClass('icon-sort-down');
        this.$element.parent().siblings().find('i').removeClass('icon-sort-down icon-sort-up');

        if($('input[name="orderByType"]').size() < 1 ){
            $form.append($('<input type="hidden" name="orderBy" value="'+name+'"><input type="hidden" name="orderByType" value="'+status+'">'));
        }else{
            $form.find('input[name="orderByType"]').val(this.sortStatus);
            $form.find('input[name="orderBy"]').val(options.sortName);
        }


    };

    SortTable.prototype.sortDesc = function(){
        var options = this.options;
        var $form = $(options.formId);
        var name = options.name;
        this.sortStatus = 'desc';
        this.$element.find('i').addClass(options.statusClass[this.sortStatus]).removeClass('icon-sort-up');
        this.$element.parent().siblings().find('i').removeClass('icon-sort-down icon-sort-up');

        if($('input[name="orderByType"]').size() < 1 ){
            $form.append($('<input type="hidden" name="orderBy" value="'+name+'"><input type="hidden" name="orderByType" value="'+this.sortStatus+'">'));
        }else{
            $form.find('input[name="orderByType"]').val(this.sortStatus);
            $form.find('input[name="orderBy"]').val(options.sortName);
        }
    };

    SortTable.prototype.toggle = function(){
        var status = this.toggleStatus();
        var event = $.Event('sortComplete');
        if(status === 'desc' || !status){
            this.sortDesc();
        }else{
            this.sortAsc();
        }
        this.$element.trigger(event);
    };

    SortTable.prototype.toggleStatus = function(){
      return this.sortStatus === 'desc'? 'asc' : 'desc';
    };



    $.fn.sortTable = function (option) {
        return this.each(function () {
            var $this = $(this);
            var data = $(this).data('sort');
            var options = $.extend({},$this.data(), typeof option === 'object' && option);
            if (!data) {
                $this.data('sort', (data = new SortTable(this,options)));
            }
            if (typeof option === 'string') {
                data[option]()
            }
        })
    };

    $.fn.sortTable.Constructor = SortTable;

    $(function () {
        $(document).on('click', 'a[data-toggle="sort"]', function (e) {
            e.preventDefault();
            $(this).sortTable('toggle');
        })
    })

}(jQuery);