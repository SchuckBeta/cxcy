<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<%@ attribute name="region" type="com.oseasy.cms.modules.cms.entity.CmsIndexRegion" required="true" description="栏目区域对象"%>


				<c:if test="${region.id eq '99ddbe37972f444b80715b8427ef9ce8'}">


					<!--装饰用的图-->
					<a href="${ctxFront}/course/frontCourseList"><img class="indexdeco" src="${ctxImg}/indexdecopic2_09.png" alt="" /></a>
					<div class="teacher-wrap">
						<div class="teacherClassBox">
							<c:forEach var="course" items="${courseList}">
							<div class="teach-box">
								<div class="media">
									<div class="date">
										<strong><fmt:formatDate value="${course.publishDate}" pattern="dd"/></strong>/<fmt:formatDate value="${course.publishDate}" pattern="MM"/>
									</div>
									<a href="${ctxFront}/course/view?id=${course.id}"  >
										<c:if test="${not empty course.coverImg}">
											<img src="${fns:ftpImgUrl(course.coverImg)}" />
										</c:if>
										<c:if test="${ empty course.coverImg}">
											<img src="${ctxImg}/course/200X150.png" />   <!--默认图片-->
										</c:if>
									</a>

								</div>
								<div class="description">
									<h4>${course.name}</h4>
									<p>${course.description}</p>
									<div class="btns">
										<img src="${ctxImg}/pl.png"/>
										<a class="view-count">${course.comments}</a>
										 &nbsp;&nbsp;
										 <img src="${ctxImg}/ll.png"/>
										<a class="voteup">${course.views}</a>
										&nbsp;&nbsp;
										 <img src="${ctxImg}/z.png"/>
										<a class="votedown">${course.likes}</a>
									</div>
								</div>
							</div>
							</c:forEach>
						</div>


					</div>


				</c:if>