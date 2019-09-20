<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>--%>
<!DOCTYPE html>
<html>
<head>
    <title>{{backgroundTitle}}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>
<div id="app" class="container-fluid container-fluid_bg" v-show="pageLoad" style="display: none">
    <edit-bar></edit-bar>
    <el-form action="${actionUrl}" :model="searchListForm" ref="searchListForm" method="post">
        <input name="pageNo" type="hidden" :value="searchListForm.pageNo"/>
        <input name="pageSize" type="hidden" :value="searchListForm.pageSize"/>
        <input type="hidden" name="orderBy" :value="searchListForm.orderBy"/>
        <input type="hidden"  name="orderByType" :value="searchListForm.orderByType"/>
        <input name="actywId" type="hidden" :value="searchListForm.actywId"/>
        <input name="gnodeId" type="hidden" :value="searchListForm.gnodeId"/>
        <div class="conditions">
            <e-condition type="radio" label="项目类别"  :options="projectCategories" v-model="searchListForm.proCategory" name="proCategory"></e-condition>
            <e-condition type="checkbox" label="学院"  :options="colleges" v-model="searchListForm.officeId" name="officeId" :default-props="{label: 'name', value: 'id'}"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <%--<el-button type="primary" size="mini" :disabled="exporting" @click.stop="dialogVisible = true"><i--%>
                <%--class="iconfont icon-daochu"></i>导出--%>
                <%--</el-button>--%>
            </div>
            <div class="search-input">
                <el-input
                        name="queryStr"
                        placeholder="项目编号/项目名称/负责人模糊搜索"
                        v-model="searchListForm.queryStr"
                        size="mini"
                        style="width: 225px;">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="$refs.searchListForm.$el.submit"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>
    <div class="table-container">
        <el-table :data="pageList" size="mini">
            <el-table-column label="项目编号">
                <template slot-scope="scope">{{scope.row.competitionNumber}}</template>
            </el-table-column>
            <el-table-column label="项目名称">
                <template slot-scope="scope">{{scope.row.pName}}</template>
            </el-table-column>
            <el-table-column label="项目类别">
                <template slot-scope="scope">{{scope.row.pName | selectedFilter(projectTypesEntries)}}</template>
            </el-table-column>
            <el-table-column label="负责人">
                <template slot-scope="scope">{{scope.row.pName | selectedFilter(projectTypesEntries)}}</template>
            </el-table-column>
        </el-table>
    </div>
</div>

<script>

    ;+function (Vue) {
        var app = new Vue({
            el: '#app',
            data: function () {
                var colleges = JSON.parse('${fns: toJson(fns: findColleges())}') || [];
                var projectCategories = JSON.parse('${fns: toJson(fns:getProCategoryByActywId(actywId))}') || [];
                var pageList = JSON.parse('${fns: toJson(page.list)}');
                var projectTypes = JSON.parse('${fns: toJson(fns: getDictList('projectType'))}');
                <%--var pageList2 = JSON.parse('${pageList}');--%>
                <%--console.log(JSON.parse('${fns: toJson(pageList)}'))--%>
                console.log(JSON.parse('${pageList}'))
                return {
                    colleges: colleges,
                    projectCategories: projectCategories,
                    projectTypes: projectTypes,
                    searchListForm: {
                        proCategory: '${proModel.proCategory}',
                        officeId: ['${proModel.deuser.office.id}'],
                        pageNo: '${page.pageNo}',
                        pageSize: '${page. pageSize}',
                        queryStr: '${proModel.queryStr}',
                        actywId: '${actywId}',
                        gnodeId: '${gnodeId}',
                        orderByType: '${page.orderByType}',
                        orderBy: '${page.orderBy}',
                    },
                    pageList: []
                }
            },
            computed: {
                projectTypesEntries: {
                    get: function () {
                        return this.getEntries(this.projectTypes)
                    }
                }
            }
        })
    }(Vue)

</script>
</body>
</html>