/**
 * Created by qingtengwang on 2017/7/18.
 */




!function ($) {

    $(function () {
        var $formPeriod = $('.form-period-point .form-period');
        var $editableLabel = $('label.editable-label');
        var $tableCreditSetting = $('.table-credit-setting');
        var rn = 0;
        var $tbody = $('#dataTb');

        $(document).on('click', '.add-td', function (e) {
            e.preventDefault();
            var formPStr = formPeriodPoint();
            var $td = $(this).parents('td');
            $td.append(formPStr);
            handleSmallDel();
        });

        $(document).on('click', '.delete-td', function (e) {
            e.preventDefault();
            var $parentBox = $(this).parents('.form-period-point-box');
            if ($(this).parents('td').find('.form-period-point-box').size() === 1) {
                $(this).parents('tr').find('.btn-delete-row').trigger('click')
            } else {
                $parentBox.detach();
            }

            handleSmallDel();
        });

        $(document).on('click', '.btn-add-row', function (e) {
            e.preventDefault();
            var formPStr = formPeriodPoint();
            var row = '<tr>' + lessonTd() + '<td>' + formPStr + '</td></tr>';
            $tbody.append(row);
            handleBigDel();
        });

        $(document).on('click', '.btn-delete-row', function (e) {
            e.preventDefault();
            $(this).parents('tr').detach();
            handleBigDel();
        });

        handleSmallDel();

        function handleSmallDel() {
            var $tr = $tbody.find('tr');
            $tr.each(function (i, td) {
                var $td = $(td).find('.delete-td');
                if ($td.size() === 1) {
                    $td.addClass('hide')
                } else {
                    $td.removeClass('hide');
                }
            });
        }

        handleBigDel();

        function handleBigDel() {
            var $td = $tbody.find('.btn-delete-row');
            if ($td.size() === 1) {
                $td.addClass('hide')
            } else {
                $td.removeClass('hide');
            }
        }

        function formPeriodPoint() {
            return '<div class="form-period-point-box"><div class="pull-right">' +
                '<a title="添加成绩及学分标准"  class="add-td" href="javascript:void (0);"><img src="' + ctxImg + '/plus2.png"> </a> ' +
                '<a title="删除成绩及学分标准" class="delete-td hide" href="javascript:void (0);"><img src="' + ctxImg + '/minuse.png"> </a>' +
                '</div>' +
                '<div class="text-left"><div class="form-period-point"> ' +
                '<input name="' + (++rn) + '" type="text" class="form-period ckNumber">' +
                '<span> - </span>  ' +
                '<input name="' + (++rn) + '" type="text" class="form-period ckNumber"> ' +
                '<span style="margin-right: 15px;">分</span>  ' +
                '<span>认定</span> ' +
                '<input name="' + (++rn) + '" type="text" class="form-period ckScore"> ' +
                '<span>学分</span></div></div></div>'
        }

        function lessonTd() {
            return '<td> <div class=" form-period-point-box">' +
                '<div class="pull-right"> ' +
                '<a title="添加课时"  class="btn-add-row" href="javascript:void (0)"><img src="' + ctxImg + '/plus2.png"> </a> ' +
                '<a title="删除课时"  class="btn-delete-row" href="javascript:void (0)"><img src="' + ctxImg + '/minuse.png"></a>' +
                '</div>' +
                '<div class="text-left">' +
                '<div class="form-period-point">' +
                '<input name="' + (++rn) + '" type="text" class="form-period ckCouse"> ' +
                '<span>-</span> ' +
                '<input name="' + (++rn) + '" type="text" class="form-period  ckCouse "> ' +
                '<span>课时</span>' +
                ' </div></div> </td>'
        }
    })


}(jQuery);