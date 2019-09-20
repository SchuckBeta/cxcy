function SwitcherList(element, option) {
    this.$element = $(element);
    this.options = $.extend({}, SwitcherList.DEFAULT, option || {});
    this.$row = this.$element.find('.bc-row');
    this.init();
}


SwitcherList.DEFAULT = {};

SwitcherList.prototype.init = function () {
    this.addEvent();
}

SwitcherList.prototype.addEvent = function () {
    var $iconfonts = this.$element.find('[data-toggle="switcher"]');
    var $row = this.$row;
    $iconfonts.on('click', function (event) {
        event.stopPropagation();
        event.preventDefault();
        if ($(this).hasClass('active')) return;
        var type = $(this).attr('data-switcher');
        $(this).addClass('active').siblings().removeClass('active');
        if (type === 'horizontal') {
            $row.addClass('bc-row-4').removeClass('bc-row-column')
        } else {
            $row.addClass('bc-row-column').removeClass('bc-row-4')
        }

    })
}


function ELSelect(element, option) {
    this.$element = $(element);
    this.options = $.extend({}, ELSelect.DEFAULT, option || {});
    this.$input = this.$element.find('input[type="text"]');
    this.$dropdownList = this.$element.find('.el-select-dropdown__item');
    this.$elSelectDropdown = this.$element.find('.el-select-dropdown');
    this.$elSelectDropdownInner = this.$element.find('.el-select-dropdown-inner');
    this.pageSize = parseInt(this.options.pageSize);
    this.elSelectDropdownRealHeight = 0;


    this.init();
}

ELSelect.DEFAULT = {
    pageSize: 10
};

ELSelect.prototype.init = function () {
    this.setPageSize();
    this.addInputEvent();
    this.addTransitionEvent();
    this.addItemEvent();
    this.clickOtherArea();

}

ELSelect.prototype.getElSelectDropdownRealHeight = function () {
    return this.getDropdownMargin() + this.getDropDownInnerHeight();
}

ELSelect.prototype.getDropdownMargin = function () {
    return this.$elSelectDropdown.css('marginTop').replace('px', '') * 2;
}

ELSelect.prototype.getDropDownInnerHeight = function () {
    return this.$elSelectDropdownInner.height();
}


ELSelect.prototype.setPageSize = function (value) {
    if(value){
        this.pageSize = value;
    }
    var pageSize = this.pageSize;
    var $dropdownList = this.$dropdownList;
    var $input = this.$input;
    $dropdownList.each(function (index, item) {
        var text = $(item).text();
        var num = parseInt(text.match((/\d+/))[0]);
        $(item).attr('num', num);
        if (num === pageSize) {
            $(item).addClass('selected')
            $input.val(text);
        } else {
            $(item).removeClass('selected')
        }
    })
}

ELSelect.prototype.addInputEvent = function () {
    var self = this;
    this.$input.on('click', function (event) {
        event.stopPropagation();
        event.preventDefault();
        self.toggle();
    })
}

ELSelect.prototype.toggle = function () {
    var $elSelectDropdown = this.$elSelectDropdown;
    var isShow = $elSelectDropdown.attr('data-show') === 'true';
    if (isShow) {
        $elSelectDropdown.attr('data-show', false);
        $elSelectDropdown.css('opacity', 0);
        $elSelectDropdown.height(0);
    } else {
        $elSelectDropdown.css('opacity', 0);
        $elSelectDropdown.show().height(0);
        if (this.elSelectDropdownRealHeight === 0) {
            this.elSelectDropdownRealHeight = this.getElSelectDropdownRealHeight();
        }
        $elSelectDropdown.height(this.elSelectDropdownRealHeight)
        $elSelectDropdown.attr('data-show', true);
        $elSelectDropdown.css('opacity', 1);
    }
}

ELSelect.prototype.addTransitionEvent = function () {
    var $elSelectDropdown = this.$elSelectDropdown;
    $elSelectDropdown.on({
        'transitionend': this.transitionend,
        'webkitTransitionEnd': this.transitionend,
        'msTransitionEnd': this.transitionend
    })
}
ELSelect.prototype.transitionend = function () {
    var isShow = $(this).attr('data-show') === 'true';
    if (!isShow) {
        $(this).hide()
    }
    $(this).css('opacity', '');
}

ELSelect.prototype.addItemEvent = function () {
    var self = this;
    var options = this.options;
    this.$dropdownList.on('click', function (event) {
        event.stopPropagation();
        event.preventDefault();
        var text = $(this).text();
        var num = parseInt(text.match((/\d+/))[0]);
        self.pageSize = num;
        self.toggle();
        self.setPageSize();
        options.changePageSize && options.changePageSize(num);

    })
};

ELSelect.prototype.clickOtherArea = function () {
    var $elSelectDropdown = this.$elSelectDropdown;
    var $input = this.$input;
    $(document).on('click.stop.prevent', function (event) {
        var $target = $(event.target);
        if ($target !== $input) {
            var isShow = $elSelectDropdown.attr('data-show') === 'true';
            if (isShow) {
                $elSelectDropdown.attr('data-show', false);
                $elSelectDropdown.css('opacity', 0);
                $elSelectDropdown.height(0);
            }
        }
    })
}

function ElPagination(element, option) {
    this.$element = $(element);
    this.options = $.extend({}, ElPagination.DEFAULT, option || {});
    this.currentPage = this.options.pageNo;
    this.init();


}

ElPagination.prototype.init = function () {
    this.appendElPaperBlock();
}

ElPagination.prototype.setPageNo = function (value) {
    var options = this.options;
    options.pageNo = parseInt(value);
    this.currentPage = options.pageNo;
    this.emptyHtml();
    this.appendElPaperBlock();
}

ElPagination.prototype.setPageSize = function (value) {
    var maxNum;
    this.options.pageSize = parseInt(value);
    maxNum = this.getMaxNum();
    if (maxNum <= this.currentPage) {
        this.setPageNo(maxNum)
    } else {
        this.setPageNo(this.currentPage)
    }
}

ElPagination.prototype.setPageCount = function (value) {
    this.options.count = parseInt(value);
}

ElPagination.prototype.prevBtnHtml = function (value) {
    return '<button type="button" ' + (value <= 1 ? 'disabled' : '') + ' class="btn-prev"><i class="el-icon el-icon-arrow-left"></i></button>'
}
ElPagination.prototype.nextBtnHtml = function (value, maxNum) {
    return '<button type="button" ' + (maxNum === 0 || value >= maxNum ? 'disabled' : '') + ' class="btn-next"><i class="el-icon el-icon-arrow-right"></i></button>'
}
ElPagination.prototype.totalHtml = function (total) {
    return '<span class="el-pagination__total">共 ' + total + ' 条</span>'
}

ElPagination.prototype.elPaperHtml = function () {
    return '<ul class="el-pager">' + this.paperLis() + '</ul>'
};
ElPagination.prototype.paperBlockHtml = function () {
    var options = this.options;
    return this.totalHtml(options.count) + this.prevBtnHtml(options.pageNo) + this.elPaperHtml() + this.nextBtnHtml(options.pageNo, this.getMaxNum())
}

ElPagination.prototype.getMaxNum = function () {
    var options = this.options;
    var count = options.count;
    var pageSize = options.pageSize;
    if (count % pageSize !== 0) {
        return Math.ceil(count / pageSize)
    } else {
        return Math.floor(count / pageSize)
    }
};

ElPagination.prototype.getPageTagArr = function () {
    var tagArr = [];
    var maxNum = this.getMaxNum();
    var options = this.options;
    var pageNo = parseInt(options.pageNo);
    var pageSize = parseInt(options.pageSize);
    var pageTagNum = options.pageTagNum; //基本超出的li個數
    var count = parseInt(options.count);

    if (count <= pageTagNum * pageSize) {
        for (var i = 0; i < Math.ceil(count / pageSize); i++) {
            tagArr.push({
                type: pageNo === i + 1 ? 'currentPage' : 'base',
                value: i + 1
            })
        }
    } else {
        if (pageNo <= Math.ceil(pageTagNum / 2)) {
            for (var i = 0; i < pageTagNum - 1; i++) {
                tagArr.push({
                    type: pageNo === i + 1 ? 'currentPage' : 'base',
                    value: i + 1
                })
            }
            tagArr.push({
                type: 'next',
                value: i
            }, {
                type: 'base',
                value: maxNum
            })
        } else if (pageNo >= maxNum - Math.floor(pageTagNum / 2)) {
            var initValue = maxNum - pageTagNum + 1;
            for (; initValue < maxNum; initValue++) {
                tagArr.push({
                    type: initValue + 1 === pageNo ? 'currentPage' : 'base',
                    value: initValue + 1
                })
            }
            tagArr.unshift({
                type: 'base',
                value: 1
            }, {
                type: 'prev',
                value: maxNum - pageTagNum - 1
            })
        } else {
            return [
                {type: 'base', value: 1},
                {type: 'prev', value: pageNo - 3},
                {type: 'base', value: pageNo - 2},
                {type: 'base', value: pageNo - 1},
                {type: 'currentPage', value: pageNo},
                {type: 'base', value: pageNo + 1},
                {type: 'base', value: pageNo + 2},
                {type: 'next', value: pageNo + 3},
                {type: 'base', value: maxNum}
            ]
        }
    }
    return tagArr;
}

ElPagination.prototype.paperLis = function () {
    var tagArr = this.getPageTagArr()
    var html = tagArr.map(function (item) {
        if (item.type === 'base') {
            return '<li class="number" data-page="' + item.value + '">' + item.value + '</li>'
        } else if (item.type === 'prev' || item.type === 'next') {
            return '<li class="el-icon more btn-quick' + item.type + ' el-icon-more" data-page="' + item.value + '"></li>'
        } else {
            return '<li class="number active" data-page="' + item.value + '">' + item.value + '</li>'
        }
    })
    return html.join('')
}


ElPagination.prototype.addEventDirection = function () {
    var self = this;
    var $elPager = this.$element.find('.el-pager');
    var $btnQuicknext = $elPager.find('.btn-quicknext');
    var $btnQuickPrev = $elPager.find('.btn-quickprev');
    var $lis = $elPager.find('>li');
    var $btnNext = this.$element.find('.btn-next');
    var $btnPrev = this.$element.find('.btn-prev');
    var options = this.options;

    $btnQuicknext.hover(function () {
        self.prevNextToggle($(this), 'right')
    }, function () {
        self.prevNextToggle($(this), 'right')
    });

    $btnQuickPrev.hover(function () {
        self.prevNextToggle($(this), 'left')
    }, function () {
        self.prevNextToggle($(this), 'left')
    });

    $lis.on('click', function (event) {
        event.preventDefault();
        event.stopPropagation();
        if ($(this).hasClass('active')) return false;
        // self.setPageNo(parseInt($(this).attr('data-page')));
        options.pageChange && options.pageChange.call(self, parseInt($(this).attr('data-page')), options.pageSize);
        return false;
    })

    $btnNext.on('click', function (event) {
        self.currentPage++;
        self.setPageNo(self.currentPage);
        options.pageChange && options.pageChange.call(self, self.currentPage, options.pageSize);
        return false;
    })

    $btnPrev.on('click', function (event) {
        self.currentPage--;
        self.setPageNo(self.currentPage);
        options.pageChange && options.pageChange.call(self, self.currentPage, options.pageSize);
        return false;
    })

}

ElPagination.prototype.emptyHtml = function () {
    var $elPager = this.$element.find('.el-pager');
    var $btnNext = this.$element.find('.btn-next');
    var $btnPrev = this.$element.find('.btn-prev');
    var $elPagination__total = this.$element.find('.el-pagination__total');
    $elPager.remove();
    $btnNext.remove();
    $btnPrev.remove();
    $elPagination__total.remove();
}


ElPagination.prototype.prevNextToggle = function (obj, dir) {
    obj.toggleClass('el-icon-more el-icon-d-arrow-' + dir)
}

ElPagination.prototype.appendElPaperBlock = function () {
    var $elPagination__sizes = this.$element.find('.el-pagination__sizes');
    $(this.paperBlockHtml()).insertBefore($elPagination__sizes);
    this.addEventDirection();
}


ElPagination.DEFAULT = {
    pageNo: 50,
    pageSize: 10,
    count: 1000,
    pageTagNum: 7,
    pageChange: function (pageNo, pageSize, count) {
        this.setPageNo(pageNo);
    }
}


