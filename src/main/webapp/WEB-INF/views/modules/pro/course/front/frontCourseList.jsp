<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>

</head>


<body>

<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb class="mgb-20" separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a :href="frontOrAdmin"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>${cmsCourse.modelname}</el-breadcrumb-item>
    </el-breadcrumb>
    <el-form :model="searchListForm" ref="searchListForm" size="mini"
             :show-message="false">
        <div class="conditions">
            <e-condition type="checkbox" v-model="searchListForm.categoryValueList" label="专业课程" @change="getCourseList"
                         :options="professCourseList" name="categoryValueList"></e-condition>
            <e-condition type="checkbox" v-model="searchListForm.typeList" label="课程类型" :options="courseTypes" @change="getCourseList"
                         name="typeList"></e-condition>
            <e-condition type="checkbox" v-model="searchListForm.statusList" label="状态" :options="courseStatuses" @change="getCourseList"
                         name="statusList"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-input" style="width: 300px;">
                <input type="text" style="display: none">
                <el-input placeholder="请输入课程名称或者教师名" size="mini" v-model="searchListForm.name" @keyup.enter.native="getCourseList" class="w300">
                    <el-button slot="append" icon="el-icon-search" @click.stop.prevent="getCourseList"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>
    <div v-loading="courseLoading">
        <el-row v-show="courseList.length > 0" :gutter="30">
            <el-col :span="19" class="course-row">
                <div class="bc-block-action">
                    <i data-toggle="switcher" data-switcher="horizontal" class="iconfont icon-liebiao"
                       :class="{active: columnSpan == 8}" @click.stop.prevent="columnSpan=8"></i>
                    <i data-toggle="switcher" data-switcher="vertical" class="iconfont icon-liebiao1"
                       :class="{active: columnSpan == 24}" @click.stop.prevent="columnSpan=24"></i>
                </div>
                <el-row :gutter="30" :class="rowClass">
                    <el-col v-for="(course, index) in courseList" :key="index" :span="columnSpan">
                        <div class="bc-article-block">
                            <div class="bc-article-pic">
                                <a :href="frontOrAdmin + '/course/view?id='+ course.id">
                                    <img :src="course.coverImg | ftpHttpFilter(ftpHttp) | proGConPicFilter">
                                </a>
                            </div>
                            <div class="bc-article-content">
                                <h5 class="title"><a
                                        :href="frontOrAdmin + '/course/view?id='+ course.id">{{course.name}}</a></h5>
                                <p class="description">
                                    {{course.description}}
                                </p>
                                <div class="teacher-list">
                                    <template v-if="course.teacherList">
                                        课程讲师：<span class="teacher-item"
                                                   v-for="(teacher, teacherIndex) in course.teacherList" :key="teacher.id">{{teacher.teacherName}}<i
                                            v-if="teacherIndex < course.teacherList.length - 1">，</i></span>
                                    </template>
                                    <template v-else>
                                        <span class="empty-color">暂无课程讲师</span>
                                    </template>
                                </div>
                                <div class="actions">
                                    <div class="view-good-date">
                                        <span class="date"><i class="el-icon-date"></i>{{course.publishDate}}</span>
                                        <span class="views-goods"><i class="iconfont icon-dianzan1"></i>{{course.views}}</span>
                                        <span class="views-goods likes"><i class="iconfont icon-yanjing1"></i>{{course.likes}}</span>
                                    </div>
                                    <div class="courseware">
                                        <el-dropdown v-if="course.attachmentList" trigger="click" @command="downCourse">
                                          <span class="el-dropdown-link">
                                            下载课件<i class="el-icon-arrow-down el-icon--right"></i>
                                          </span>
                                            <el-dropdown-menu slot="dropdown">
                                                <el-dropdown-item v-for="item in course.attachmentList" :command="{course: course, attachmentItem: item}"
                                                                  :key="item.id">
                                                    {{item.name | textEllipsis(20)}}
                                                </el-dropdown-item>
                                            </el-dropdown-menu>
                                        </el-dropdown>
                                        <span v-else class="empty-color">暂无课件</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </el-col>
                </el-row>
                <div class="mgb-20" style="margin-top: 20px;">
                    <el-pagination
                            size="small"
                            @size-change="handlePSChange"
                            background
                            @current-change="handlePNChange"
                            :current-page.sync="searchListForm.pageNo"
                            :page-sizes="[6,10,24,48]"
                            :page-size="searchListForm.pageSize"
                            layout="total,prev, pager, next, sizes"
                            :total="pageCount">
                    </el-pagination>
                </div>
            </el-col>
            <el-col :span="5" class="course-teacher-list">
                <div class="teacher-recommend-title">
                    <span>相关导师</span>
                </div>
                <div v-for="teacher in teacherList" :key="teacher.id" class="course-teacher-item">
                    <a :href="frontOrAdmin + '/sys/frontTeacherExpansion/view?id='+ teacher.id" target="_blank" class="teacher-avatar">
                        <img :src="teacher.photo | ftpHttpFilter(ftpHttp) | studentPicFilter">
                    </a>
                    <div class="teacher-name-title">
                        <p class="name">{{teacher.name}}</p>
                        <div v-show="teacher.officeName" class="title">
                            <el-tag type="info" size="mini">{{teacher.officeName}}</el-tag>
                        </div>
                    </div>
                </div>
            </el-col>
        </el-row>
        <div v-show="!courseList.length" class="empty-color text-center pdt-60" style="font-size: 16px;">
            暂无课程数据，请重新查找
        </div>
    </div>
</div>


<script>
    new Vue({
        el: '#app',
        data: function () {
            var userId = '${fns:getUser().id}';
            var professCourseList = JSON.parse('${fns: toJson(fns:getDictList('0000000086'))}');
            var courseTypes = JSON.parse('${fns: toJson(fns:getDictList('0000000078'))}');
            var courseStatuses = JSON.parse('${fns: toJson(fns:getDictList('0000000082'))}');

            return {
                courseList: [],
                columnSpan: 24,
                searchListForm: {
                    name: '',
                    categoryValueList: [],
                    typeList: [],
                    statusList: [],
                    pageNo: 1,
                    pageSize: 10,
                },
                userId: userId,
                pageCount: 0,
                teacherList: [],
                professCourseList: professCourseList,
                courseTypes: courseTypes,
                courseStatuses: courseStatuses,
                courseLoading: true
            }
        },
        computed: {
            rowClass: function () {
                return {
                    'bc-row-4': this.columnSpan === 8,
                    'bc-row-column': this.columnSpan === 24
                }
            }
        },
        methods: {
            downCourse: function (obj) {
                var course = obj.course;
                var attachmentItem = obj.attachmentItem;
                var self = this;
                if(!this.userId){
                    this.$alert('请登录后下载课件，是否登录？', '提示', {
                        confirmButtonText: '确定',
                        type: 'warning'
                    }).then(function () {
                        location.href = self.frontOrAdmin + '/login'
                    }).catch(function () {

                    });
                    return;
                }
                location.href = this.frontOrAdmin + '/course/downLoad?id='+course.id+'&url='+attachmentItem.url+'&fileName='+attachmentItem.name;
            },
            handlePSChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getCourseList();
            },
            handlePNChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getCourseList();
            },

            getCourseList: function () {
                var self = this;
                this.courseLoading = true;
                this.$axios({
                    method: 'GET',
                    url: '/course/getCourseList?'+Object.toURLSearchParams(this.searchListForm)
                }).then(function (response) {
                    var data = response.data;
                    var teacherIds = [];
                    if (data.status === 1) {
                        var pageData = data.data.page;
                        self.courseList = pageData.list || [];
                        self.searchListForm.pageNo = pageData.pageNo || 1;
                        self.searchListForm.pageSize = pageData.pageSize || 10;
                        self.pageCount = pageData.count || 0;
                        teacherIds = data.data.allTeachers || [];
                        teacherIds = teacherIds.map(function (item) {
                            return item.teacherId;
                        })
                    } else {
                        self.courseList = [];
                        self.$message({
                            type: 'error',
                            message: data.msg
                        })
                    }
                    self.getTeacherList(teacherIds);
                    self.courseLoading = false;
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: error.response.data
                    })
                    self.courseLoading = false;
                })
            },

            getTeacherList: function (teacherIds) {
                var self = this;
                this.$axios.get('/course/getTeacherList?ids='+ teacherIds.join(',')).then(function (response) {
                    var data = response.data;
                    var teacherIds = [];
                    if (data.status === 1) {
                        var list = data.data || [];
                        self.teacherList = list || [];
                    } else {
                        self.teacherList = [];
                        self.$message({
                            type: 'error',
                            message: data.msg
                        })
                    }
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: error.response.data
                    })
                })
            }
        },
        beforeMount: function () {
            this.getCourseList();
        }
    })
</script>

</body>

</html>