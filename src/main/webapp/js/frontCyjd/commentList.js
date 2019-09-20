/**
 * Created by Administrator on 2018/9/25.
 */

'use strict';

function CommentList(element, option) {
    this.$element = $(element);
    this.options = $.extend({}, CommentList.DEFAULT, option);
    this.init();
}

CommentList.DEFAULT = {
    list: [],
    type: '1',
    pageNo: 1,
    pageSize: 10,
    pageCount: 0,
    getCommentSuccess: function () {

    }
};

CommentList.prototype.init = function () {
    this.getCommentList();
    this.addEventDoLike();
};

CommentList.prototype.getCommentList = function () {
    var options = this.options;
    var self = this;
    var url = options.type === '1' ? '/cms/cmsConmment/ajaxList' : '/cms/cmsConmment/ajaxListByCuser';
    axios({
        method: 'POST',
        url: url,
        params: {
            auditstatus: '1',
            'cnt.id': options.cntId,
            pageNo: options.pageNo,
            pageSize: options.pageSize
        }
    }).then(function (response) {
        var data = response.data;
        if (data.status == '1') {
            var pageData = data.data;
            self.handleChangeList(pageData.list || []);
            self.setPageCount(pageData.count);
            self.setPageSize(pageData.pageSize);
            self.setPageNo(pageData.pageNo);
            options.getCommentSuccess && options.getCommentSuccess.call(self, options)
        }
    }).catch(function () {

    })

}

CommentList.prototype.setType = function (value) {
    this.options.type = value;
}

CommentList.prototype.setList = function (list) {
    this.options.list = list;
}

CommentList.prototype.parseItem = function (item) {
    var userId = this.options.userId;
    var ftpHttp = this.options.ftpHttp;
    var ctxImagesComment = typeof ctxImagesComment !== 'undefined' ? ctxImagesComment : '';
    var templateItem = '<div id="{{id}}" class="excellent-comment">\n' +
        '                        <div class="excellent-comment-head"><img src="{{user.photo}}" alt=""> <span class="excellent-comment-name">{{user.name}}</span> <span class="excellent-comment-date">{{createDate}}</span></div>\n' +
        '                        <div class="excellent-comment-content">\n' +
        '                            {{content}}\n' +
        '                        </div>\n' +
        '                        <div class="excellent-comment-meta"><a href="javascript:void(0)" class="excellent-comment-like {{clicked-like}}" data-id={{id}} data-likes={{likes}}><i class="iconfont icon-dianzan1"></i> <span>({{likes}})</span></a></div>\n' +
        '                        <div class="excellent-comment-reply-item {{hasReplay}}"><img src="' + ctxImagesComment + '/test/admin.jpg" alt=""> <span>{{reUser.name}}回复：</span>\n' +
        '                            <span>{{reply}}</span>\n' +
        '                            <div class="reply-meta"><span>{{reDate}}</span></div>\n' +
        '                        </div>\n' +
        '                    </div>';

    templateItem = templateItem.replace(/\{\{([^\}]+)\}\}/g, function ($1, $2) {
        var $2Arr;
        if ($2 === 'clicked-like') {
            if (item.ctLikeIds && item.ctLikeIds.indexOf(userId) > -1) {
                return 'clicked-like';
            }
            return '';
        } else if ($2 === 'hasReplay') {
            return item.reply ? '' : 'hide-remove';
        } else if ($2 === 'reDate') {
            return item.reDate ? moment(item.reDate).format('YYYY-MM-DD HH:mm:ss') : '';
        } else if ($2.indexOf('photo') > -1) {
            $2Arr = $2.split('.');
            var url = item[$2Arr[0]] ? item[$2Arr[0]][$2Arr[1]] : '';
            return url ? ftpHttp + url : '/img/u4110.png'
        } else {
            if ($2.indexOf('.') > -1) {
                $2Arr = $2.split('.');
                return item[$2Arr[0]] ? item[$2Arr[0]][$2Arr[1]] : ''
            } else {
                return item[$2]
            }
        }
    });
    return templateItem
};

CommentList.prototype.setPageNo = function (value) {
    this.options.pageNo = value;
}

CommentList.prototype.setPageSize = function (value) {
    this.options.pageSize = value;
}

CommentList.prototype.setPageCount = function (value) {
    this.options.pageCount = value;
}

CommentList.prototype.handleChangeList = function (list) {
    var self = this, htmlArr, $html;
    this.destroy();
    this.setList(list);
    htmlArr = list.map(function (item) {
        return self.parseItem(item)
    });
    $html = $(htmlArr.join(' '));
    $html.find('.hide-remove').remove();
    this.appendList($html)
}

CommentList.prototype.appendList = function (html) {
    if (html) {
        this.$element.append(html);
        return
    }
    this.$element.html('<div class="no-content">暂无评论，快去评论吧！ </div>')
}

CommentList.prototype.addEventDoLike = function () {
    var self = this;
    var options = this.options;
    var userId = options.userId;
    this.$element.on('click', '.excellent-comment-like', function (event) {
        event.preventDefault();
        event.stopPropagation();
        if (!userId) {
            $('#alertBox').show();
            return false;
        }
        if ($(this).hasClass('clicked-like')) {
            return false;
        }
        var likes = parseInt($(this).attr('data-likes'));
        self.postLikeXhr({id: $(this).attr('data-id'), likes: likes == 0 ? 1 : likes++})
    });
}

CommentList.prototype.postLikeXhr = function (params) {
    var self = this;
    var options = this.options;
    var userId = options.userId;
    if (!userId) {

    }
    axios({
        method: 'POST',
        url: '/cms/cmsConmment/ajaxUpdateLikes',
        data: {id: params.id, likes: params.likes}
    }).then(function (response) {
        var data = response.data;
        if (data.status == '1') {
            self.getCommentList();
        } else {
            ELEMENT.Message({
                type: 'error',
                message: data.msg
            })
            // $('#alertBoxError').show().find('.el-message__content').text(data.msg)
            // setTimeout(function () {
            //     $('#alertBoxError').hide();
            // }, 1500)
        }
    }).catch(function (error) {

    })
}

CommentList.prototype.destroy = function () {
    this.$element.children().remove();
}