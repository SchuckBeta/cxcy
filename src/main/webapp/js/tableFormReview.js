/**
 * Created by qingtengwang on 2017/7/25.
 */

!function ($) {
    var rn=0;
    function TableFormReview(element, option) {
        this.$element = $(element);
        this.options = $.extend({}, TableFormReview.DEFAULT, option || {});
        this.$deleteRow = this.$element.find('.btn-delete-row');
        this.$addRow = this.$element.find('.btn-add-row');
        this.$element.on('click.api.review', '.btn-add-row', $.proxy(this.addRow, this));
        this.$element.on('click.api.review', '.btn-delete-row', $.proxy(this.deleteRow, this));
        this.init();
    }

    TableFormReview.DEFAULT = {
        trTmp: '<tr> <td> <div class="form-control-box"> <textarea maxLength="50" class="form-control-check required" name="checkPoint_'+(++rn)+'"  rows="4"></textarea> </div> </td> ' +
        '<td> <div class="form-control-box"> <textarea maxLength="500" class="form-control-elements required" name="checkElement_'+(++rn)+'"  rows="4"></textarea> </div> </td> ' +
        '<td><div class="form-control-box"><input type="text" name="viewScore_'+(++rn)+'"  onchange="scoreChange()"  class="input-mini digits ckScore required"/> </div> </td> ' +
        '<td><a class="btn-add-row" href="javascript:void (0)"><img src="' + ctxImg + '/plus2.png"> </a><a class="btn-delete-row" href="javascript:void (0)"><img src="' + ctxImg + '/minuse.png"></a> </td> </tr>'
    };

    TableFormReview.prototype.init = function () {
        var status = this.$element.data('status');
        if (status === 'edit') {
            this.$element.find('tr').not(':last-child').find('.btn-add-row').addClass('hide');
        } else {
            this.$deleteRow.eq(0).addClass('hide');
        }
    };

    TableFormReview.prototype.addRow = function (e) {
        var $element = this.$element;
        var options = this.options;
        var trTmp = options.trTmp;
        var status = $element.data('status');
        var isOneTr;
        $(trTmp).appendTo($element.find('tbody'));
        isOneTr = this.isOneTr();
        if (status === 'edit') {
            if (isOneTr) {
                this.$element.find('tr').not(':last-child').find('.btn-add-row').addClass('hide');
            }
        } else {
            if (isOneTr) {
                this.$deleteRow = this.$element.find('.btn-delete-row');
                this.$deleteRow.eq(0).removeClass('hide');
            }
        }
    };

    TableFormReview.prototype.deleteRow = function (e) {
        var $element = this.$element;
        var isOneTr;
        var $target = $(e.target);
        var status = $element.data('status');
        $target.parents('tr').detach();
        isOneTr = this.isOneTr();
        if (status === 'edit') {
            this.$element.find('tr:last-child').find('.btn-add-row').removeClass('hide');
        } else {
            if (!isOneTr) {
                this.$deleteRow = $element.find('.btn-delete-row');
                this.$deleteRow.eq(0).addClass('hide');
            }
        }
    };

    TableFormReview.prototype.isOneTr = function () {
        return this.$element.find('tbody>tr').size() > 1 || false;
    };

    $(function () {
        var tableFormReview = new TableFormReview('#tableFormReview');
    })


}(jQuery);