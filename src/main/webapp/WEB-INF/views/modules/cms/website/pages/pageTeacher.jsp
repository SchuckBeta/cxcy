<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>

<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
</head>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="container page-container pdt-60">
    <el-breadcrumb class="mgb-20" separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="/f"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>${cmsTeacher.modelname}</el-breadcrumb-item>
    </el-breadcrumb>



    <div class="exc-pros-tab-nav">
        <el-button :type="searchListForm.teachertype == ''? 'primary': 'default' " size="medium"
                   @click.stop.prevent="handleChangeTabBtn('')">全部
        </el-button>
        <el-button :type="searchListForm.teachertype == '1'? 'primary': 'default' " size="medium"
                   @click.stop.prevent="handleChangeTabBtn('1')">校内导师
        </el-button>
        <el-button :type="searchListForm.teachertype == '2'? 'primary': 'default' " size="medium"
                   @click.stop.prevent="handleChangeTabBtn('2')">企业导师
        </el-button>
        <el-button :type="searchListForm.teachertype == '3'? 'primary': 'default' " size="medium"
                   @click.stop.prevent="handleChangeTabBtn('3')">专家评委
        </el-button>
    </div>


    <div class="teacher-mien-content" v-loading="loading">
        <div class="line-box" v-for="item in pageList">
            <div class="line-img">
                <a :href="frontOrAdmin+'/sys/frontTeacherExpansion/view?id='+item.id">
                    <img :src="item.user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt="">
                </a>
                <div class="img-like">
                    <span>点赞数：{{item.user.likes}}</span>
                    <i class="iconfont icon-dianzan1" :class="{liked:item.isLike == '1' && loginCurUser.id}" @click.stop.prevent="giveLike(item)"></i>
                </div>
                <div class="img-view">
                    <span>浏览数：{{item.user.views}}</span>
                </div>
            </div>
            <div class="line-experience">
                <div class="experience-name"><span @click.stop.prevent="goTeacherInfo(item.id)">{{item.user.name}}</span></div>
                <div class="experience-title">
                    <div class="title-job">工作经历</div>
                    <div class="title-keyWords">
                        <el-tag type="info" size="small" v-for="(value,index) in item.keywords" key="index">{{value}}</el-tag>
                    </div>
                </div>
                <div class="white-space-pre-static experience-content word-break" style="-webkit-box-orient: vertical;">
                    <span v-html="item.mainExp"></span>
                </div>
            </div>
            <div class="line-info">
                <div>出生日期：{{item.user.birthday | formatDateFilter('YYYY-MM')}}</div>
                <div>性别：{{item.user.sex | selectedFilter(sexListEntries)}}</div>
                <div>学历：{{item.user.education | selectedFilter(educationLevelEntries)}}</div>
                <div>职称：{{item.technicalTitle | selectedFilter(expertJobNamesEntries)}}</div>
                <div>学位：{{item.user.degree | selectedFilter(degreeTypeEntries)}}</div>
            </div>

        </div>
    </div>
    <div class="text-right mgb-20 mgt-20" v-if="pageCount">
        <el-pagination
                size="small"
                @size-change="handlePaginationSizeChange"
                background
                @current-change="handlePaginationPageChange"
                :current-page.sync="searchListForm.pageNo"
                :page-sizes="[5,10,20,50,100]"
                :page-size="searchListForm.pageSize"
                layout="total, prev, pager, next, sizes"
                :total="pageCount">
        </el-pagination>
    </div>
</div>
<script type="text/javascript">
    'use strict';
    new Vue({
        el: '#app',
        data: function () {
            var sexList = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('sex'))}));
            var educationLevel = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('enducation_level'))}));
            var postTitleType = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('postTitle_type'))}));
            var degreeType = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('degree_type'))}));
            var technical = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("postTitle_type"))})) || [];
            var teacherType = '${teacherType}';

            return {
                pageList: {},
                sexList:sexList,
                educationLevel:educationLevel,
                postTitleType:postTitleType,
                degreeType:degreeType,
                expertJobNames: technical,
                searchListForm: {
                    pageNo: 1,
                    pageSize: 5,
                    teachertype:teacherType
                },
                pageCount:0,
                loading: false
            }
        },
        computed:{
            sexListEntries:{
                get:function(){
                    return this.getEntries(this.sexList)
                }
            },
            educationLevelEntries:{
                get:function(){
                    return this.getEntries(this.educationLevel)
                }
            },
            postTitleTypeEntries:{
                get:function(){
                    return this.getEntries(this.postTitleType)
                }
            },
            degreeTypeEntries:{
                get:function(){
                    return this.getEntries(this.degreeType)
                }
            },
            expertJobNamesEntries:function () {
                return this.getEntries(this.expertJobNames);
            }
        },
        methods: {
            giveLike:function (item) {
                var self = this;
                if(item.isLike == '1'){
                    return false;
                }
                this.$axios({
                    method:'GET',
                    url:'/interactive/sysLikes/doLike?doUserId=' + item.user.id
                }).then(function (response) {
                    var data = response.data;
                    if(data.status == '1'){
                        item.user.likes ++;
                        item.isLike = '1';
                    }
                }).catch(function () {
                    self.$message({
                        message:'请求失败',
                        type:'error'
                    })
                })
            },
            goTeacherInfo:function (id) {
                window.location.href = this.frontOrAdmin + '/sys/frontTeacherExpansion/view?id=' + id;
            },
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList(this.searchListForm.teachertype);
            },
            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList(this.searchListForm.teachertype);
            },

            handleChangeTabBtn:function(type){
           		this.searchListForm.teachertype = type;
                this.searchListForm.pageNo = 1;
                this.searchListForm.pageSize = 5;
           		this.getDataList();
           	},
           	getDataList:function(){
                var self = this;
                this.loading = true;
                this.$axios({
                    method:'POST',
                    url:'/cms/index/teacherPageList?' + Object.toURLSearchParams(this.searchListForm)
                }).then(function (response) {
                    var data = response.data;
                    if(data.status == '1'){
                        self.pageList = data.data.list || [];
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                    self.$message({
                        message:'请求失败',
                        type:'error'
                    })
                })
           	}

        },
        beforeMount: function () {
            this.getDataList();
        }
    })

</script>

</body>
</html>