<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>--%>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxStatic}/vue/vue.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/backTable/sort.js"></script><!--排序js-->
<body>
<div class="container-fluid container-fluid-oe">
    <div class="edit-bar edit-bar-tag clearfix">
        <div class="edit-bar-left">
            <span>1${menuName}</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="searchForm" modelAttribute="proModel" action="${actionUrl}" method="post"
               class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input type="hidden" id="orderBy" name="orderBy" value="${page.orderBy}"/>
        <input type="hidden" id="orderByType" name="orderByType" value="${page.orderByType}"/>
        <input id="actywId" name="actywId" type="hidden" value="${actywId}"/>

        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">项目年份</label>
                <div class="controls">
                    <input type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
                           value="${proModel.subTime}"
                           onclick="WdatePicker({isShowToday: false, dateFmt:'yyyy',isShowClear:true});"/>

                </div>
            </div>
            <div class="control-group">
                <label class="control-label">项目类别</label>
                <div class="controls">
                    <form:select path="proCategory" class="input-medium">
                        <form:option value="" label="所有类别"/>
                        <form:options items="${fns:getDictList('project_type')}" itemLabel="label" itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">所属学院</label>
                <div class="controls">
                    <form:select path="deuser.office.id" class="input-medium form-control">
                        <form:option value="" label="所有学院"/>
                        <form:options items="${fns:findColleges()}" itemLabel="name" itemValue="id"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <form:input class="input-medium" cssStyle="width: 300px" path="queryStr" htmlEscape="false" maxlength="100" autocomplete="off"
                                placeholder="项目编号/项目名称/负责人模糊搜索"/>
                </div>
            </div>
        </div>
        <div class="search-btn-box">
            <button id="searchBtn" type="submit" class="btn btn-primary">查询</button>
        </div>

    </form:form>
    <div id="auditInfo">
        <table id="contentTable"
               class="table table-bordered table-condensed table-hover  table-sort table-theme-default table-center">
            <thead>
            <tr>
                <th width="160px" data-name="a.competition_number"><a class="btn-sort" href="javascript:void(0);">项目编号<i
                        class="icon-sort"></i></a></th>
                <th data-name="a.p_name"><a class="btn-sort" href="javascript:void(0);">项目名称<i
                        class="icon-sort"></i></a></th>
                <th>项目类别</th>
                <th data-name="u.name"><a class="btn-sort" href="javascript:void(0);">负责人<i
                        class="icon-sort"></i></a></th>
                <th data-name="o6.name"><a class="btn-sort" href="javascript:void(0);">学院<i
                        class="icon-sort"></i></a></th>
                <th>组人数</th>
                <th>指导老师</th>
                <th>结果</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.list}" var="item">
                <c:set var="act" value="${fns:getActByPromodelId(item.id)}" />
                <tr>
                    <td>${item.competitionNumber}</td>
                    <td><a href="${ctx}/promodel/proModel/viewForm?id=${proModelMd.proModel.id}">${item.pName}</a></td>
                    <td>${fns:getDictLabel(item.proCategory, "project_type", "")}</td>
                    <td>${fns:getUserById(item.declareId).name}</td>
                    <td>${item.deuser.office.name}</td>
                    <td>${item.team.memberNum}</td>
                    <td>
                            ${item.team.uName}
                    </td>
                    <td>xx结果</td>
                    <td>
                        <a href="#">
                            <c:if test="${act.status=='todo' or act.status == 'toChaim' or act.status == 'othersTodo'}">
                                待${act.taskName}
                            </c:if>
                            <c:if test="${act.status=='finished'}">
                                已${act.taskName}
                            </c:if>
                            <c:if test="${act.status=='over'}">
                                项目已结项
                            </c:if>
                        </a>
                    </td>
                    <td>
                        <button class="btn btn-small btn-primary" onclick="getAuditInfo('${item.id}')">查看审核记录</button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>

        </table>
        <div v-show="auditInfoShow"
             style="position: fixed;width: 100%;height: 100%;top: 0;left: 0;display: none;z-index: 1000">
            <div style="z-index: 1050" class="modal modal-calendar modal-large">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                            @click="auditInfoShow=false">&times;
                    </button>
                    <h3>审核记录</h3>
                </div>
                <div class="modal-body">
                    <table class="table table-bordered table-condensed table-hover  table-sort table-theme-default table-center">
                        <thead>
                        <tr>
                            <th>审核动作</th>
                            <th>审核时间</th>
                            <th>审核人</th>
                            <th>审核结果</th>
                            <th>建议及意见</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="item in auditInfoList">
                            <td>
                                {{item.auditName}}
                            </td>
                            <td>
                                {{item.updateDate}}
                            </td>
                            <td>
                                {{item.user ? item.user.name : ''}}
                            </td>
                            <td>
                                {{item.grade ? item.grade : item.score}}
                            </td>
                            <td>
                                {{item.suggest}}
                            </td>
                                <%----%>
                            <%--<td>--%>
                                <%----%>
                            <%--</td>--%>
                                <%----%>
                            <%--<td>--%>
                                <%----%>
                            <%--</td>--%>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-small btn-default" @click="auditInfoShow=false">关闭</button>
                </div>

            </div>
            <div class="modal-backdrop in"></div>
        </div>
    </div>
    ${page.footer}
</div>
<script>
   var auditInfo= new Vue({
        el: '#auditInfo',
        data : function () {
          return {
              auditInfoShow: false,
              auditInfoList: [{a: 'b'}]
          }
        },
        watch:{
            'auditInfoShow': function (val) {
                this.auditInfoShow = val;
                if(!val){
                    this.auditInfoList = [];
                }
            }
        },
       mounted: function () {
       }
    });

   function getAuditInfo(proModelId) {
       var buildListXhr = $.get('${ctx}/promodel/proModel/auditInfo/' + proModelId);
       buildListXhr.success(function (data) {
           $.each(data, function (index, item) {
               auditInfo.$data.auditInfoList.push(item);
           });
           auditInfo.$data.auditInfoList = data;
           auditInfo.$data.auditInfoShow = true;
       });
   }

</script>
</body>
</html>