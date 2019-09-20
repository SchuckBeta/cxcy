<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
    <script src="${ctxJs}/frontCyjd/noticeList.js"></script>
</head>
<body>
<div class="container pdt-60">
    <div aria-label="Breadcrumb" role="navigation" class="el-breadcrumb">
        <span class="el-breadcrumb__item">
            <span role="link" class="el-breadcrumb__inner">
                <a href="/f"><i class="iconfont icon-ai-home" style="font-size: 12px;"></i>首页</a>
            </span>
            <i class="el-breadcrumb__separator el-icon-arrow-right"></i>
        </span>
        <span class="el-breadcrumb__item">
            <span role="link" class="el-breadcrumb__inner">${category.name}</span>
            <i class="el-breadcrumb__separator el-icon-arrow-right"></i>
        </span>
    </div>
    <div class="base-content-container">
        <div id="bcListSwitcher" class="bc-list-switcher">
            <div class="bc-block-action">
                <i data-toggle="switcher" data-switcher="horizontal" class="iconfont icon-liebiao active"></i>
                <i data-toggle="switcher" data-switcher="vertical" class="iconfont icon-liebiao1"></i>
            </div>
            <div id="bcRow" class="bc-row bc-row-4">
                <c:forEach items="${page.list}" var="article">


                    <div class="bc-col">
                        <div class="bc-article-block">
                            <div class="bc-article-pic">
                                <a href="${ctx}/getOneCmsArticle?id=${article.id}">
                                    <img src="${not empty article.thumbnail ? fns:ftpImgUrl(article.thumbnail) : '/images/default-pic.png'}">
                                </a>
                            </div>
                            <div class="bc-article-content">
                                <h5 class="title"><a title="${article.title}"
                                        href="${ctx}/getOneCmsArticle?id=${article.id}">${article.title}</a></h5>

                                    <p class="description">
                                        <c:if test="${article.isshowdescription eq 1}">
                                            ${article.description}
                                        </c:if>
                                    </p>

                                <div class="actions">
                                    <c:if test="${article.isshowwriter eq 1}">
                                        <span><i class="iconfont icon-gaojian-zuozhe"></i>${article.writer}</span>
                                    </c:if>
                                    <c:if test="${article.cmsArticleData.isshowcopyfrom eq 1}">
                                        <span><i class="iconfont icon-laiyuan"></i>${article.cmsArticleData.copyfrom}</span>
                                    </c:if>
                                    <c:if test="${article.isshowpublishdate eq 1}">
                                        <span><i class="el-icon-date"></i><fmt:formatDate value="${article.articlepulishDate}"
                                                                   pattern="yyyy-MM-dd"/></span>
                                    </c:if>
                                </div>

                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

        </div>

    </div>
    <div class="text-right mgb-60">
        <div id="elPagination" class="el-pagination is-background" size="small">
            <span class="el-pagination__sizes"><div id="elSelectJq" class="el-select"><!---->
                    <div class="el-input el-input--suffix"><!---->
                        <input type="text" autocomplete="off" placeholder="请选择" readonly="readonly"
                               class="el-input__inner">
                        <!----><span class="el-input__suffix">
                                     <span class="el-input__suffix-inner">
                                          <i class="el-select__caret el-input__icon el-icon-arrow-up"></i><!---->
                                     </span><!---->
                             </span><!---->
                    </div>
                    <div class="el-select-dropdown el-popper el-select-dropdown-transition"
                         style="display: none; min-width: 110px;overflow: hidden">
                        <div class="el-scrollbar" style="">
                            <div class="el-select-dropdown__wrap el-scrollbar__wrap el-select-dropdown-inner">
                                <ul class="el-scrollbar__view el-select-dropdown__list"><!---->
                                    <li class="el-select-dropdown__item"><span>5条/页</span></li>
                                    <li class="el-select-dropdown__item"><span>10条/页</span></li>
                                    <li class="el-select-dropdown__item"><span>20条/页</span></li>
                                    <li class="el-select-dropdown__item"><span>50条/页</span></li>
                                    <li class="el-select-dropdown__item"><span>100条/页</span></li></ul>
                            </div>
                            <div class="el-scrollbar__bar is-horizontal">
                                <div class="el-scrollbar__thumb" style="transform: translateX(0%);"></div>
                            </div>
                            <div class="el-scrollbar__bar is-vertical">
                                <div class="el-scrollbar__thumb" style="transform: translateY(0%);"></div>
                            </div>
                        </div>
                        <!----></div>
                </div></span>
        </div>
    </div>


</div>
<script>

    function generateBcColDetail(data){
        var str = [];
        var actionStr = [];
        var description = '';
        if(data.isshowdescription == '1'){
            description = data.description;
        }
        str.push('<p class="description">'+description+'</p>')
        if(data.isshowwriter == '1'){
            actionStr.push('<span><i class="iconfont icon-gaojian-zuozhe"></i>'+data.writer+'</span>')
        }
        if(data.cmsArticleData.isshowcopyfrom == '1'){
            actionStr.push('<span><i class="iconfont icon-laiyuan"></i>'+data.cmsArticleData.copyfrom+'</span>')
        }
        if(data.isshowpublishdate == '1'){
            actionStr.push('<span><i class="el-icon-date"></i>'+moment(data.articlepulishDate).format("YYYY-MM-DD")+'</span>')
        }
        actionStr.unshift('<div class="actions">');
        actionStr.push('</div>');
        return str.join('') + actionStr.join('')
    }

    function generateBcColTemp(data) {
        var ftpHttp = '${ftpHttp}';
        var temp = '<div id={{id}} class="bc-col"><div class="bc-article-block">' +
                '<div class="bc-article-pic"><a href="${ctxFront}/getOneCmsArticle?id={{id}}"><img src="{{thumbnail}}"></a></div>' +
                '<div class="bc-article-content">' +
                '<h5 class="title"><a href="${ctxFront}/getOneCmsArticle?id={{id}}" title="{{title}}">{{title}}</a></h5>' + generateBcColDetail(data) +
                '</div></div></div>';
        temp = temp.replace(/\{\{([^\}]+)\}\}/g, function ($1, $2) {
            var $2Arr;
            if ($2 === 'articlepulishDate') {
                return data.articlepulishDate ? moment(data.articlepulishDate).format('YYYY-MM-DD') : '';
            } else if ($2 === 'thumbnail') {
                return data.thumbnail ? ftpHttp + data.thumbnail : '/images/default-pic.png'
            } else {
                if ($2.indexOf('.') > -1) {
                    $2Arr = $2.split('.');
                    return data[$2Arr[0]] ? data[$2Arr[0]][$2Arr[1]] : ''
                } else {
                    return data[$2]
                }
            }
        });
        return temp
    }

    new SwitcherList('#bcListSwitcher')

    var elPagination = new ElPagination('#elPagination', {
        pageNo: '${page.pageNo}' || 0,
        pageSize: '${page.pageSize}' || 10,
        count: '${page.count}' || 0,
        pageChange: function (pageNo, pageSize) {
            getArticleList(pageNo, pageSize)
        }
    })


    var elSelectJq = new ELSelect('#elSelectJq', {
        pageSize: '${page.pageSize}' || 10,
        changePageSize: function (value) {
            getArticleList(elPagination.options.pageNo, value)
        }
    })

    function getArticleList(pageNo, pageSize) {
        axios.post('/getArticleList?pageNo=' + pageNo + '&pageSize=' + pageSize, {id: '${category.id}'}).then(function (response) {
            var data = response.data;
            if (data.status == '1') {
                var pageList = data.data.list || [];
                var pageNo = data.data.pageNo || 1;
                var pageSize = data.data.pageSize || 10;
                var pageCount = data.data.count || 0;
                var bcCols = pageList.map(function (item) {
                    return generateBcColTemp(item);
                })
                $('#bcRow').html(bcCols.join(''))
                elPagination.setPageCount(pageCount)
                elPagination.setPageSize(pageSize)
                elPagination.setPageNo(pageNo)
                elSelectJq.setPageSize(pageSize)
            }

        })
    }


</script>
</body>
</html>