<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<%@ attribute name="region" type="com.oseasy.cms.modules.cms.entity.CmsIndexRegion" required="true"
              description="栏目区域对象" %>
<c:if test="${region.id eq '2e4e49416866441c86f8468589bab3b9'}">
    <!--装饰用的图-->
    <img class="indexdeco" src="${ctxImg}/indexdecopic_14.png" alt=""/>
    <!--优秀项目展示板块-->
    <div class="itemDisplayBox">
        <a href="${ctxFront}/pageProject">
            <img class="title" src="${ctxImg}/indexItemTitle.png"/>
        </a>

        <ul class="threeBlock">
            <c:forEach var="resource" items="${region.childResourceList}" varStatus="idx">
                <c:if test="${idx.index eq 0 }">
                    <li class="active">
                        <a href="#project${idx.index}" data-toggle="tab">${resource.title }</a>
                    </li>
                </c:if>
                <c:if test="${idx.index ne 0 }">
                    <li><a href="#project${idx.index}" data-toggle="tab">${resource.title }</a></li>
                </c:if>
            </c:forEach>
        </ul>
        <div class="tab-content">
            <ul id="project0" class="active tab-pane content">
                <c:forEach var="porject" items="${excellentShowList.gcontest}">
                    <li>
                        <a href="${ctxFront}/frontExcellentView-${porject.id}">
                            <div class="project-thumbnail">

                                <img src="${not empty porject.coverImg ? fns:ftpImgUrl(porject.coverImg) : '/img/Video.png'}"
                                     alt="${porject.name}">
                            </div>
                            <h5>${porject.name}</h5>
                            <p>
                                    ${porject.introduction}
                            </p>
                        </a>
                        <div class="project-show-footer">
                            <span class="view-count"><img src="${ctxImg}/pl.png"/>
                                 ${porject.comments}
                                </span>
                            <span class="voteup"><img src="${ctxImg}/ll.png"/>
                                 ${porject.views}
                                </span>
                            <span class="votedown"><img src="${ctxImg}/z.png"/>
                                 ${porject.likes}
                                </span>
                        </div>
                    </li>
                </c:forEach>
            </ul>
            <ul id="project1" class="tab-pane content">
                <c:forEach var="porject" items="${excellentShowList.project}">
                    <li>
                        <a href="${ctxFront}/frontExcellentView-${porject.id}">
                            <div class="project-thumbnail">

                                <img src="${not empty porject.coverImg ? fns:ftpImgUrl(porject.coverImg) : '/img/Video.png'}"
                                     alt="${porject.name}">
                            </div>
                            <h5>${porject.name}</h5>
                            <p>
                                    ${porject.introduction}
                            </p>
                        </a>
                        <div class="project-show-footer">
                            <span class="view-count"><img src="${ctxImg}/pl.png"/>
                                 ${porject.comments}
                                </span>
                            <span class="voteup"><img src="${ctxImg}/ll.png"/>
                                 ${porject.views}
                                </span>
                            <span class="votedown"><img src="${ctxImg}/z.png"/>
                                 ${porject.likes}
                                </span>
                        </div>
                    </li>
                </c:forEach>
            </ul>
            <ul id="project2" class="tab-pane content">
                <c:forEach var="porject" items="${excellentShowList.scientific}">
                    <li>
                        <a href="${ctxFront}/frontExcellentView-${porject.id}">
                            <div class="project-thumbnail">

                                <img src="${not empty porject.coverImg ? fns:ftpImgUrl(porject.coverImg) : '/img/Video.png'}"
                                     alt="${porject.name}">
                            </div>
                            <h5>${porject.name}</h5>
                            <p>
                                    ${porject.introduction}
                            </p>
                        </a>
                        <div class="project-show-footer">
                            <span class="view-count"><img src="${ctxImg}/pl.png"/>
                                 ${porject.comments}
                                </span>
                            <span class="voteup"><img src="${ctxImg}/ll.png"/>
                                 ${porject.views}
                                </span>
                            <span class="votedown"><img src="${ctxImg}/z.png"/>
                                 ${porject.likes}
                                </span>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <script type="text/javascript">

        $(function () {
            var $emptyContent = $('.itemDisplayBox').find('.tab-pane:empty');
            if ($emptyContent.size() > 0) {
                $emptyContent.each(function () {
                    $('a[href="#' + $(this).attr('id') + '"]').attr('href','javascript:void(0);');
                })
            }
        })
    </script>
</c:if>
