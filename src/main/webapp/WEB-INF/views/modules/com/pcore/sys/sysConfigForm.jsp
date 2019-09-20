<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid scroll-bar-style"
     style="margin-bottom:40px;">
    <edit-bar></edit-bar>

    <el-row :gutter="40" style="margin: 20px 20px 0 20px;">
        <el-col :span="isFullJsonData?24:12">
            <form ref="form" action="${ctx}/sysconfig/sysConfig/save" method="post">
                <div class="json-string-transform">
                    <input type="hidden" name="id" value="${sysConfig.id}">
                    <input type="hidden" name="content" :value="stringData">
                    <el-input type="textarea" :rows="27" placeholder="请输入内容" v-model="textarea"></el-input>
                    <div class="transition-tab" @click="isFullJsonData = !isFullJsonData">
                        <div v-show="isFullJsonData" class="show-sys-config-tip">查看操作指南</div>
                        <div v-show="!isFullJsonData" class="show-sys-config-tip">隐藏操作指南</div>
                    </div>
                </div>
                <div class="text-center" style="margin-top:20px;">
                    <el-button type="primary" size="mini" @click.stop.prevent="saveString">保存</el-button>
                </div>
            </form>
        </el-col>
        <el-col :span="12" v-show="!isFullJsonData">
            <div class="sys-config-tip">
                <h2>操作指南</h2>
                <pre>
{
    "applyConf": {//项目、大赛申报的配置
        "gconConf": {//双创大赛配置
            "cOnOff": 1,//是否允许同一个学生用同一个项目申报不同类型的大赛,1-允许，0-不允许
            "bOnOff": 1,//是否允许同一个学生申报多个不同类型的大赛,1-允许，0-不允许
            "aOnOff": 1,//同一个项目周期内，是否允许同一个学生在同一类大赛中申报多个,1-允许，0-不允许
            "gconSubTypeConf": [//不同大赛类型的配置，互联网+  创青春
                {
                    "subType": 1,//大赛类型，字典值
                    "personNumConf": {//申报人数配置
                        "schoolTeacherNumOnOff": 1,//校园导师人数限制开关，1-限制，0不限制
                        "schoolTeacherNum": {//校园导师人数范围
                            "min": 0,
                            "max": 1
                        },
                        "enTeacherNumOnOff": 1,//企业导师人数限制开关，1-限制，0不限制
                        "teamNumOnOff": 1,//团队人数限制开关，1-限制，0不限制
                        "enTeacherNum": {//企业导师人数范围
                            "min": 0,
                            "max": 1
                        },
                        "teamNum": {//团队人数范围
                            "min": 0,
                            "max": 1
                        }
                    }
                }
            ]
        },
        "bOnOff": 1,//是否允许同一个学生用同一个项目既申报项目又参加大赛，1-允许，0-不允许
        "aOnOff": 1,//是否允许同一个学生用不同项目既申报项目又参加大赛，1-允许，0-不允许
        "proConf": {//双创项目配置
            "cOnOff": 1,//是否允许同一个学生用同一个项目申报不同类型的项目,1-允许，0-不允许
            "proSubTypeConf": [//不同项目类型的配置，大创、小创之类
                {
                    "lowTypeConf": [//不同项目类别配置，创新创业、创新实践
                        {
                            "lowType": 1,//项目类别,字典值
                            "personNumConf": {//申报人数配置
                                "schoolTeacherNumOnOff": 1,//校园导师人数限制开关，1-限制，0不限制
                                "schoolTeacherNum": {//校园导师人数范围
                                    "min": 0,
                                    "max": 1
                                },
                                "enTeacherNumOnOff": 1,//企业导师人数限制开关，1-限制，0不限制
                                "teamNumOnOff": 1,//团队人数限制开关，1-限制，0不限制
                                "enTeacherNum": {//企业导师人数范围
                                    "min": 0,
                                    "max": 1
                                },
                                "teamNum": {//团队人数范围
                                    "min": 1,
                                    "max": 5
                                }
                            }
                        },
                        {
                            "lowType": 2,//项目类别,字典值
                            "personNumConf": {//申报人数配置
                                "schoolTeacherNumOnOff": 1,//校园导师人数限制开关，1-限制，0不限制
                                "schoolTeacherNum": {//校园导师人数范围
                                    "min": 1,
                                    "max": 2
                                },
                                "enTeacherNumOnOff": 1,//企业导师人数限制开关，1-限制，0不限制
                                "teamNumOnOff": 1,//团队人数限制开关，1-限制，0不限制
                                "enTeacherNum": {//企业导师人数范围
                                    "min": 0,
                                    "max": 1
                                },
                                "teamNum": {//团队人数范围
                                    "min": 1,
                                    "max": 5
                                }
                            }
                        },
                        {
                            "lowType": 3,//项目类别,字典值
                            "personNumConf": {//申报人数配置
                                "schoolTeacherNumOnOff": 1,//校园导师人数限制开关，1-限制，0不限制
                                "schoolTeacherNum": {//校园导师人数范围
                                    "min": 1,
                                    "max": 2
                                },
                                "teamNumOnOff": 1,//团队人数限制开关，1-限制，0不限制
                                "enTeacherNumOnOff": 1,//企业导师人数限制开关，1-限制，0不限制
                                "enTeacherNum": {//企业导师人数范围
                                    "min": 0,
                                    "max": 1
                                }，
                                "teamNum": {//团队人数范围
                                    "min": 1,
                                    "max": 7
                                }
                            }
                        }
                    ],
                    "subType": 1//项目类型，字典值
                }
            ],
            "bOnOff": 1,//是否允许同一个学生申报多个不同类型的项目,1-允许，0-不允许
            "aOnOff": 1,//同一个项目周期内，是否允许同一个学生在同一类项目中申报多个项目,1-允许，0-不允许
        }
    }，
    "teamConf": {//团队创建的配置
        "intramuralValia": {//团队创建时，校园导师数量范围
            "min": 0,
            "max": 1
        },
        "maxMembers":5,//团队成员最大人数
        "maxOnOff": 1,//每个人创建团队数量的限制，1-有限制，0-无限制
        "invitationOnOff": 1,//邀请加入团队限制 1-有限制，0-无限制
        "teamUpdateReject": 2,//团队修改审核驳回次数限制，0-无限制
        "max": 5,//每个人创建的最大团队数
        "enterYear": "1",//入学N年才能创建团队
        "intramuralValiaOnOff": 1,//团队创建时，校园导师数量限制 1-有限制，0-无限制
        "teamCheckOnOff": 1,//团队创建、修改是否需要审核 1-需审核，0-无需审核
        "joinOnOff": 1,//申请加入团队限制 1-有限制，0-无限制
        "teamCreateReject": 2,//团队创建审核驳回次数限制，0-无限制
    },
    "graduateMonth": "7",//毕业月份
    "registerConf":{
        "autoRegister":"0", //控制全局注册功能
        "teacherRegister":"0" //控制导师注册功能
    },"sentuxueyuan":{
         "isencrypt": "1",//是否加密
		 "httpWeb": "http://oe.sentuxueyuan.com/",
         "pidSso": "",//固定参数
         "m": "",//接口模型名称
         "signJg": "",//全站当前机构标识
         "signSy": "sentu_nt",//DES加密算法需使用8位签名
         "cntInterface": "nt_interface"//接口控制器名称,
         "oectype": "0",//退出重定向Url
         "oecurl": "http://f.os-easyedu.com:28235/f"//退出重定向Url类型：0，固定Url;1，动态域名Url
    }
}
                </pre>
            </div>
        </el-col>
    </el-row>

</div>

<script>

    +function (Vue) {
        var app = new Vue({
            el: '#app',
            data: function () {
                return {
                    textarea: '${sysConfig.content}'.replace(/\\/g, '').replace(/&quot;/g, '"'),
                    sysConfigId: '${sysConfig.id}',
                    stringData: '',
                    jsonObject: [],
                    isFullJsonData: true
                }
            },
            methods: {
                toJson: function () {
                    this.jsonObject = JSON.parse(this.textarea);
                    this.textarea = JSON.stringify(this.jsonObject, null, 4);
                },
                saveString: function () {
                    var self = this;
                    this.jsonObject = JSON.parse(this.textarea);
                    this.stringData = JSON.stringify(this.jsonObject);
                    this.$nextTick(function () {
                        self.$refs.form.submit();
                    })
                }
            },
            created: function () {
                this.toJson();
            }
        })

    }(Vue)


</script>

</body>
</html>