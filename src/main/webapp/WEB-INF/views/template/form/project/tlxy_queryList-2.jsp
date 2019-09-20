<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>



</head>
<body>

<div id="app" v-show="pageLoad" class="container-fluid" style="display: none">
    <edit-bar></edit-bar>
    <el-form :model="searchListForm" size="mini" autocomplete="off" ref="searchListForm">
        <input type="hidden" name="pageNo" :value="searchListForm.pageNo"/>
        <input type="hidden" name="pageSize" :value="searchListForm.pageSize"/>
        <input type="hidden" name="orderBy" :value="searchListForm.orderBy"/>
        <input type="hidden" name="orderByType" :value="searchListForm.orderByType"/>
        <input type="hidden" name="beginDate" :value="searchListForm['proModel.beginDate']"/>
        <input type="hidden" name="endDate" :value="searchListForm['proModel.endDate']"/>
        <input type="hidden" name="actywId" :value="searchListForm.actywId"/>
        <input type="hidden" name="gnodeId" :value="searchListForm.gnodeId"/>

        <div class="conditions">
            <e-condition type="checkbox" v-model="searchListForm['proModel.deuser.office.id']" label="学院"
                         :options="collegeList"
                         :default-props="{label: 'name', value: 'id'}" @change="searchCondition"
                         name="proModel.deuser.office.id"></e-condition>
            <e-condition type="checkbox" v-model="searchListForm['proModel.proCategory']" label="项目类别"
                         :options="proCategories"
                         name="proModel.proCategory" @change="searchCondition"></e-condition>
            <e-condition type="checkbox" v-model="searchListForm['proModel.finalStatus']" label="项目级别"
                         :options="adviceLevel"
                         name="proModel.finalStatus" @change="searchCondition"></e-condition>


        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">

                <button-import :is-first-menu="isFirstMenu" :actyw-id="searchListForm.actywId"
                               :gnode-id="searchListForm.gnodeId"></button-import>
                <button-export :spilt-prefs="spiltPrefs" :spilt-posts="spiltPosts"
                               :search-list-form="searchListForm"></button-export>
                <button-export-file :menu-name="menuName" :search-list-form="searchListForm"></button-export-file>

            </div>
            <div class="search-input">
                <el-input
                        placeholder="项目名称/编号/负责人/组成员/指导教师"
                        size="mini"
                        name="proModel.queryStr"
                        v-model="searchListForm['proModel.queryStr']"
                        style="width: 300px;">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="searchCondition"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>
</div>

<script>

    'use strict';
    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin],
        data: function () {
            var proCategories = JSON.parse('${fns: toJson(fns:getProCategoryByActywId(actywId))}');
            var projectDegrees = JSON.parse('${fns: toJson(fns:getDictList(levelDict))}');
            var professionals = JSON.parse('${fns: getOfficeListJson()}') || [];
            var isFirstMenu = '${fns:isFirstMenu(actywId,gnodeId)}';
            var spiltPrefs = JSON.parse('${fns:spiltPrefs()}');
            var spiltPosts = JSON.parse('${fns:spiltPosts()}');
            return {
                proCategories: proCategories,
                adviceLevel: projectDegrees,
                colleges: professionals,
                isFirstMenu: isFirstMenu === 'true',
                spiltPrefs: spiltPrefs,
                spiltPosts: spiltPosts,
                menuName: '${menuName}',
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    actywId: '${actywId}',
                    gnodeId: '${gnodeId}',
                    'proModel.beginDate': '',
                    'proModel.endDate': '',
                    'proModel.queryStr': '',
                    'proModel.finalStatus': '',
                    'proModel.proCategory': '',
                    'proModel.deuser.office.id': ''
                }
            }
        },
        computed: {
            officeList: {
                get: function () {
                    return this.getFlattenColleges();
                }
            },

            collegeList: {
                get: function () {
                    return this.officeList.filter(function (item) {
                        return item.grade === '2';
                    });
                }
            }
        },
        methods: {


            searchCondition: function () {
                
            }
        },
        created: function () {
            
        }
    })
    
</script>
</body>
</html>