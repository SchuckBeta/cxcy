<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar :second-name="courseForm.id ? '修改' : '添加'"></edit-bar>
    </div>
    <el-form :model="courseForm" ref="courseForm" :rules="courseRules" :disabled="courseDisabled" size="mini"
             label-width="120px" method="post" :action="frontOrAdmin+'/course/save'" style="width: 580px;">
        <input type="hidden" name="id" :value="courseForm.id">
        <el-form-item prop="name" label="名称：">
            <el-input name="name" v-model="courseForm.name"></el-input>
        </el-form-item>
        <el-form-item prop="coverImg" label="封面：">
            <input type="hidden" name="coverImg" :value="courseForm.coverImg"/>
            <div v-show="courseForm.coverImg" style="width: 187px; height: 187px; overflow: hidden">
                <img class="img-responsive" :src="courseForm.coverImg | ftpHttpFilter(ftpHttp) | defaultProPic">
            </div>
            <el-button type="primary" size="mini" @click.stop.prevent="handleChangeExlProPicOpen">更换封面
            </el-button>
        </el-form-item>
        <el-form-item prop="video" label="视频：">
            <div v-if="courseForm.video" class="video-preview" style="width: 400px; height: 200px;">
                <video style="width: 100%; height: 100%" controls>
                    <source :src="courseForm.video | ftpHttpFilter(ftpHttp)">
                </video>
            </div>
            <input type="hidden" name="video" :value="courseForm.video">
            <el-upload
                    class="upload-demo"
                    :action="frontOrAdmin+'/ftp/ueditorUpload/upload'"
                    :show-file-list="false"
                    name="upfile"
                    :on-success="uploadVideoSuccess"
                    :on-error="uploadVideoError"
            <%--:on-preview="handlePreview"--%>
            <%--:on-remove="handleRemove"--%>
            <%--:before-remove="beforeRemove"--%>
                    accept="video/flv,video/swf,video/mkv,video/avi,video/mp4,video/rmvb,video/rm"
            <%--:on-exceed="handleExceed"--%>
                    :file-list="videoList">
                <el-button size="mini" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">只能上传flv,swf,mkv,avi,rm,rmvb,mp4文件</div>
            </el-upload>
        </el-form-item>
        <el-form-item prop="description" label="课程描述：">
            <el-input type="textarea" name="description" :rows="3" v-model="courseForm.description"></el-input>
        </el-form-item>
        <el-form-item label="添加授课老师：">
            <el-autocomplete style="width: 100%"
                    popper-class="my-autocomplete"
                    v-model="inputTeacher"
                    value-key="teacherName"
                    :fetch-suggestions="querySearch"
                    placeholder="请输入导师姓名"
                    @select="handleSelectCourTeacher">
                <i class="el-icon-edit el-input__icon" slot="suffix" @click="handleIconClick"></i>
                <template slot-scope="{item}">
                    <div class="name"
                         style="text-overflow: ellipsis; overflow: hidden; font-size: 12px; line-height: 24px;">
                        {{item.teacherName}}
                        <span class="addr" style="color: #b4b4b4; margin-left: 5px">{{item.collegeName}}</span>
                    </div>
                </template>
            </el-autocomplete>
        </el-form-item>
        <el-form-item prop="teachers" label="授课导师列表：">
            <div class="table-container">
                <el-table :data="courseForm.teachers" size="mini" empty-text="请添加授课导师">
                    <el-table-column label="导师姓名" align="center" prop="teacherName"></el-table-column>
                    <el-table-column label="学院名称" align="center">
                        <template slot-scope="scope">
                            <input type="checkbox" style="display: none"
                                   :name="'teacherList['+scope.$index+'].teacherId'" :value="scope.row.teacherId"
                                   checked>
                            <input type="checkbox" style="display: none"
                                   :name="'teacherList['+scope.$index+'].teacherName'" :value="scope.row.teacherName"
                                   checked>
                            <span v-if="scope.row.collegeName">
                                {{scope.row.collegeName}}
                                <input type="checkbox" style="display: none"
                                       :name="'teacherList['+scope.$index+'].collegeName'"
                                       :value="scope.row.collegeName" checked>
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center">
                        <template slot-scope="scope">
                            <el-button type="text" size="mini"
                                       @click.stop.prevent="courseForm.teachers.splice(scope.$index, 1)">删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-form-item>
        <el-form-item prop="categoryValueList" label="专业课程分类：">
            <el-checkbox-group v-model="courseForm.categoryValueList">
                <el-checkbox name="categoryValueList" v-for="category in categories"
                             :key="category.value" :label="category.value">
                    {{category.label}}
                </el-checkbox>
            </el-checkbox-group>
        </el-form-item>
        <el-form-item prop="type" label="课程类型分类：">
            <el-radio-group v-model="courseForm.type">
                <el-radio-button name="type" v-for="item in courseTypes" :key="item.value" :label="item.value">
                    {{item.label}}
                </el-radio-button>
            </el-radio-group>
        </el-form-item>
        <el-form-item prop="status" label="状态分类：">
            <el-radio-group v-model="courseForm.status">
                <el-radio-button name="status" v-for="item in courseStatusList" :key="item.value" :label="item.value">
                    {{item.label}}
                </el-radio-button>
            </el-radio-group>
        </el-form-item>
        <el-form-item prop="courseSummary" label="课程介绍：">
            <el-input type="textarea" name="courseSummary" :rows="3" v-model="courseForm.courseSummary"></el-input>
        </el-form-item>
        <el-form-item prop="teacherSummary" label="导师介绍：">
            <el-input type="textarea" name="teacherSummary" :rows="3" v-model="courseForm.teacherSummary"></el-input>
        </el-form-item>
        <el-form-item prop="attachmentList" label="课件：">
            <e-upload-file v-model="courseForm.attachmentList"
                           action="/ftp/ueditorUpload/uploadTemp?folder=course"
                           :upload-file-vars="{}"
                           ></e-upload-file>
        </el-form-item>
        <el-row>
            <el-col :span="12">
                <el-form-item prop="publishFlag" label="发布：">
                    <input type="hidden" name="publishFlag" :value="courseForm.publishFlag">
                    <el-switch v-model="courseForm.publishFlag" active-value="1"
                               inactive-value="0"></el-switch>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item prop="topFlag" label="置顶：">
                    <input type="hidden" name="topFlag" :value="courseForm.topFlag">
                    <el-switch v-model="courseForm.topFlag" active-value="1"
                               inactive-value="0"></el-switch>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item prop="commentFlag" label="评论：">
                    <input type="hidden" name="commentFlag" :value="courseForm.commentFlag">
                    <el-switch v-model="courseForm.commentFlag" active-value="1"
                               inactive-value="0"></el-switch>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item prop="recommendFlag" label="推荐：">
                    <input type="hidden" name="recommendFlag" :value="courseForm.recommendFlag">
                    <el-switch v-model="courseForm.recommendFlag" active-value="1"
                               inactive-value="0"></el-switch>
                </el-form-item>
            </el-col>
        </el-row>
        <el-form-item>
            <el-button type="primary" @click.stop.prevent="submitCourseForm">保存</el-button>
            <el-button @click.stop.prevent="goToBack">返回</el-button>
        </el-form-item>
    </el-form>
    <el-dialog title="上传封面"
               width="440px"
               :close-on-click-modal="false"
               :visible.sync="dialogVisibleCoursePic"
               :before-close="handleChangeCoursePicClose">
        <e-pic-file v-model="uploadCourseProPic" :disabled="isUpdating" @get-file="getExlProPicFile"></e-pic-file>
        <cropper-pic :img-src="uploadCourseProPic" :disabled="isUpdating" ref="cropperPic"
                     :copper-params="{aspectRatio: 187/187}"></cropper-pic>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="isUpdating" @click.stop.prevent="handleChangeCoursePicClose">取消
            </el-button>
            <el-button size="mini" :disabled="!coursePicFile || isUpdating" type="primary"
                       @click.stop.prevent="updateCoursePic">上传
            </el-button>
        </div>
    </el-dialog>
</div>


<script type="text/javascript">


    'use strict';


    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin],
        data: function () {
            var courseForm = JSON.parse(JSON.stringify(${fns: toJson(course)})) || {};
            var teacherList = JSON.parse(JSON.stringify(${fns: toJson(teachers)})) || [];
            var categories = JSON.parse('${fns: toJson(fns: getDictList('0000000086'))}');
            var courseTypes = JSON.parse('${fns: toJson(fns: getDictList('0000000078'))}');
            var courseStatusList = JSON.parse('${fns: toJson(fns: getDictList('0000000082'))}');
            var colleges = JSON.parse('${fns: toJson(fns: getOfficeList())}') || [];
            var courseTeachers = courseForm.teacherList || [];
            var videoList = [];
            var attachmentList = courseForm.attachmentList || [];
            courseForm.video && videoList.push({
                id: Date.now(),
                video: courseForm.video
            });
            return {
                courseForm: {
                    id: courseForm.id,
                    name: courseForm.name,
                    coverImg: courseForm.coverImg,
                    video: courseForm.video,
                    description: courseForm.description,
                    teachers: courseTeachers,
                    categoryValueList: courseForm.categoryValueList || [],
                    type: courseForm.type,
                    status: courseForm.status,
                    courseSummary: courseForm.courseSummary,
                    teacherSummary: courseForm.teacherSummary,
                    attachmentList: attachmentList,
                    publishFlag: courseForm.publishFlag || 0,
                    topFlag: courseForm.topFlag || 0,
                    commentFlag: courseForm.commentFlag || 0,
                    recommendFlag: courseForm.recommendFlag || 0
                },
                courseRules: {
                    name: [
                        {required: true, message: '请输入课程名称', trigger: 'blur'},
                        {max: 50, message: '请输入小于50字课程名称', trigger: 'blur'}
                    ],
                    coverImg: [
                        {required: true, message: '请选择封面', trigger: 'blur'}
                    ],
                    description: [
                        {required: true, message: '请输入课程描述', trigger: 'blur'},
                        {max: 200, message: '请输入小于200字课程描述', trigger: 'blur'}
                    ],
                    teachers: [
                        {required: true, message: '请添加授课导师', trigger: 'change'}
                    ],
                    categoryValueList: [
                        {required: true, message: '请选择专业课程分类', trigger: 'change'}
                    ],
                    type: [
                        {required: true, message: '请选择课程类型分类', trigger: 'change'}
                    ],
                    status: [
                        {required: true, message: '请选择状态分类', trigger: 'change'}
                    ],
                    courseSummary: [
                        {required: true, message: '请输入课程介绍', trigger: 'blur'},
                        {max: 2000, message: '请输入小于2000字课程介绍', trigger: 'blur'}
                    ],
                    teacherSummary: [
                        {required: true, message: '请输入课程介绍', trigger: 'blur'},
                        {max: 2000, message: '请输入小于2000字课程介绍', trigger: 'blur'}
                    ],
                },
                inputTeacher: '',
                teacherList: teacherList,
                categories: categories,
                courseTypes: courseTypes,
                courseStatusList: courseStatusList,
                colleges: colleges,
                courseDisabled: false,
                isUpdating: false,
                uploadCourseProPic: '',
                dialogVisibleCoursePic: false,
                coursePicFile: null,
                videoList: videoList,
            }
        },
        watch: {
            videoList: function (value) {
                this.courseForm.video = value && value.length > 0 ? value[0].ftpUrl : ''
            }
        },
        computed: {


            dropTeacherList: {
                get: function () {
                    var teachers = this.courseForm.teachers;
                    teachers = teachers.map(function (item) {
                        return item.teacherId;
                    })
                    return this.teacherList.filter(function (item) {
                        return teachers.indexOf(item.teacherId) === -1;
                    })

                }
            }

        },
        methods: {


            uploadVideoSuccess: function (respsonse, file, fileList) {
                if (respsonse.state === 'SUCCESS') {
                    this.videoList.splice(0, 1, Object.assign(file, respsonse))
                }
                this.$message({
                    type:  respsonse.state === 'SUCCESS' ? 'success' : 'error',
                    message: respsonse.state === 'SUCCESS' ? '上传成功' : '上传失败'
                })
            },

            uploadVideoError: function (err, file, fileList) {
                this.$message({
                    type:  'error',
                    message: '上传失败'
                })
            },

            handleChangeCoursePicClose: function () {
                this.coursePicFile = null;
                this.dialogVisibleCoursePic = false;
            },

            updateCoursePic: function () {
                var data = this.$refs.cropperPic.getData();
                var self = this;
                var formData = new FormData();

                if (data.x < 0 || data.y < 0) {
                    this.show$message({
                        status: false,
                        msg: '超出边界，请缩小裁剪框，点击上传'
                    });
                    return false;
                }

                this.isUpdating = true;
                formData.append('upfile', this.coursePicFile);
                self.$axios({
                    method: 'POST',
                    url: '/ftp/ueditorUpload/cutImgToTempDir?x=' + parseInt(data.x) + '&y=' + parseInt(data.y) + '&width=' + parseInt(data.width) + '&height=' + parseInt(data.height),
                    data: formData
                }).then(function (response) {
                    var data = response.data;
                    if (data.state === 'SUCCESS') {
//                        self.moveFile(data.ftpUrl);
                        self.courseForm.coverImg = data.ftpUrl;

                    }else {
                        self.$message({
                            type: 'error',
                            message: data.msg
                        })
                    }
                    self.isUpdating = false;
                    self.handleChangeCoursePicClose();
                }).catch(function (error) {

                })
            },

            getExlProPicFile: function (file) {
                this.coursePicFile = file;
            },

            handleChangeExlProPicOpen: function () {
                var coverImg = this.courseForm.coverImg;
                this.dialogVisibleCoursePic = true;
                this.$nextTick(function (_) {
                    this.uploadCourseProPic = this.addFtpHttp(coverImg) || '/img/video-default.jpg';
                })
            },

            handleSelectCourTeacher: function (item) {
                this.courseForm.teachers.push(item);
                this.$refs.courseForm.validateField('teachers');
            },
            handleIconClick: function () {

            },
            querySearch: function (queryString, cb) {
                var teacherList = this.dropTeacherList;
                var results = queryString ? teacherList.filter(this.createFilter(queryString)) : teacherList;
                // 调用 callback 返回建议列表的数据
                cb(results);
            },
            createFilter: function (queryString) {
                return function (teacher) {
                    return (teacher.teacherName.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
                };
            },
            submitCourseForm: function () {
                var self = this;
                this.$refs.courseForm.validate(function (valid) {
                    if(valid){
                        self.courseDisabled = true;
                        self.$refs.courseForm.$el.submit();
                    }
                })
            },


            goToBack: function () {
                location.href = this.frontOrAdmin + '/course/list'
            }
        },
        beforeMount: function () {
//            this.getTeacherList();
        }
    })

</script>

</body>

</html>