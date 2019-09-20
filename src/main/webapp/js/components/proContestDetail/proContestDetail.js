/**
 * Created by Administrator on 2018/7/20.
 */

'use strict';

//负责人

Vue.component('leader-info', {
    template: '<el-row :gutter="20" label-width="84px">\n' +
    '                        <el-col :span="8">\n' +
    '                            <e-col-item label="项目负责人：" align="right">{{leader.name}}</e-col-item>\n' +
    '                        </el-col>\n' +
    '                        <el-col :span="8">\n' +
    '                            <e-col-item label="学院：" align="right">{{leader.school || officeName}}</e-col-item>\n' +
    '                        </el-col>\n' +
    '                        <el-col :span="8">\n' +
    '                            <e-col-item label="学号：" align="right">{{leader.no}}</e-col-item>\n' +
    '                        </el-col>\n' +
    '                        <el-col :span="8">\n' +
    '                            <e-col-item label="专业："\n' +
    '                                        align="right">{{leader.professionalName || leader.professional | selectedFilter(professionalEntries, true)}}</e-col-item>\n' +
    '                        </el-col>\n' +
    '                        <el-col :span="8">\n' +
    '                            <e-col-item label="联系电话：" align="right">{{leader.mobile}}</e-col-item>\n' +
    '                        </el-col>\n' +
    '                        <el-col :span="8">\n' +
    '                            <e-col-item label="E-mail：" align="right">{{leader.email}}</e-col-item>\n' +
    '                        </el-col>\n' +
    '                    </el-row>',
    props: {
        leader: Object,
        officeName: String,
        professionalEntries: {
            type: Object,
            required: true
        }
    }
})

//学生团队
Vue.component('team-student-list', {
    template: '<div class="team-student-list">\n' +
    '        <el-row :gutter="20" class="team-list" label-width="76px" v-show="teamStudent.length > 0">\n' +
    '            <el-col v-for="student in teamStudent" :key="student.id" :span="12">\n' +
    '                <div class="user-pic">\n' +
    '                    <img :src="student.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt=""\n' +
    '                         align="left" hspace="5" vspace="5">\n' +
    '                </div>\n' +
    '                <div class="user-detail">\n' +
    '                    <e-col-item label="姓名：" align="right">{{student.name}}\n' +
    '                        <el-tag v-show="leader.id == student.userId" type="info" size="mini"> 项目负责人\n' +
    '                        </el-tag>\n' +
    '                    </e-col-item>\n' +
    '                    <e-col-item label="学号：" align="right">{{student.no}}\n' +
    '                        <el-tag v-show="false" type="info" size="mini"> {{student.instudy}}\n' +
    '                        </el-tag>\n' +
    '                    </e-col-item>\n' +
    '                    <e-col-item label="学院/专业：" align="right">\n' +
    '                        {{student.org_name ? student.org_name : student.orgName}}/{{student.professional}}</e-col-item>\n' +
    '                    <e-col-item label="联系方式：" align="right">{{student.mobile}}\n' +
    '                    </e-col-item>\n' +
    '                    <e-col-item label="技术领域：" align="right">{{student.domain}}\n' +
    '                    </e-col-item>\n' +
    '                </div>\n' +
    '            </el-col>\n' +
    '        </el-row>\n' +
    '        <div v-show="!teamStudent || teamStudent.length == 0" class="empty-color text-center">\n' +
    '            目前没有学生加入到团队中，快去邀请吧...\n' +
    '        </div>\n' +
    '    </div>',
    props: {
        teamStudent: Array,
        leader: Object
    }
})

//老师团队
Vue.component('team-teacher-list', {
    template: '<div class="team-teacher-list"><el-row :gutter="20" class="team-list team-list_teacher" label-width="76px"\n' +
    '                            v-show="teamTeacher.length > 0">\n' +
    '                        <el-col v-for="teacher in teamTeacher" :key="teacher.id" :span="12">\n' +
    '                         <div class="first-teacher-label" v-if="firstTeacherId && firstTeacherId == teacher.userId"><span>第一导师</span></div>\n'+
    '                            <div class="user-pic">\n' +
    '                                <img :src="teacher.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt=""\n' +
    '                                     align="left" hspace="5" vspace="5">\n' +
    '                            </div>\n' +
    '                            <div class="user-detail">\n' +
    '                                <e-col-item label="姓名：" align="right">{{teacher.name}}\n' +
    '                                    <el-tag v-show="teacher.teacherType" type="info" size="mini">\n' +
    '                                        {{teacher.teacherType}}\n' +
    '                                    </el-tag>\n' +
    '                                </e-col-item>\n' +
    '                                <e-col-item label="工号：" align="right">{{teacher.no}}\n' +
    '                                </e-col-item>\n' +
    '                                <e-col-item label="学院/专业：" align="right">\n' +
    '                                    {{teacher.org_name ? teacher.org_name : teacher.orgName}}\n' +
    '                                </e-col-item>\n' +
    '                                <e-col-item label="联系方式：" align="right">{{teacher.mobile}}</e-col-item>\n' +
    '                                <e-col-item label="技术领域：" align="right">{{teacher.domain}}</e-col-item>\n' +
    '                            </div>\n' +
    '                        </el-col>\n' +
    '                    </el-row>\n' +
    '                    <div v-show="!teamTeacher || teamTeacher.length == 0" class="empty-color text-center">\n' +
    '                        暂无导师\n' +
    '                    </div></div>',
    props: {
        teamTeacher: Array,
        firstTeacherId:{
            type:String,
            default:''
        }
    },
    created:function () {
        var list = this.teamTeacher;
        for(var i = 0; i < list.length; i ++){
            if(list[i].userId == this.firstTeacherId){
                list.unshift(list[i]);
                list.splice(i + 1,1);
                break;
            }
        }
    }
})