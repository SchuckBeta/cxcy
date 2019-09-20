
$(document).ready(function () {
    var $contentTable = $('#contentTable');
    var $contentTableTHeadTh = $contentTable.find('>thead>tr>th');
    var $orderBy = $('#orderBy');
    var $orderByType = $('#orderByType');
    var $searchForm = $("#searchForm");
    var oldSortName = $orderBy.val();
    var sortTag = {
        desc: 'down',
        asc: 'up'
    };

    $contentTable.on('click', '.btn-sort', function (e) {
        var $th = $(this).parent();
        var sortKey = $orderByType.val();
        var iconName;
        var sortName = $th.attr('data-name');
        if (oldSortName != sortName || sortKey !== 'desc') {
            iconName = 'down';
            sortKey = 'desc'
        } else {
            iconName = 'up';
            sortKey = 'asc'
        }
        $th.find('i').attr('class', '').addClass('icon-sort-' + iconName);
        $orderByType.val(sortKey);
        $orderBy.val(sortName);
        oldSortName = sortName;
        $th.siblings().attr('data-sort', '').find('i').removeClass('icon-sort-up icon-sort-down');
        $searchForm.submit();
        return false;
    });

    function initSortIcon() {
        var sortKey = $orderByType.val();
        var sortName = $orderBy.val();
        $contentTableTHeadTh.each(function (i, th) {
            var $th = $(th);
            var iconName;
            var name = $th.attr('data-name');
            if (name === sortName) {
                iconName = sortTag[sortKey];
                $th.find('i').addClass('icon-sort-' + iconName);
                return false;
            }
        })
    }

    //初始化icon图标
    initSortIcon();
});