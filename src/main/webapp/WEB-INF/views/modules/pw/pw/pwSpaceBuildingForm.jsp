<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <shiro:hasPermission name="pw:pwSpace:edit">
            <edit-bar :second-name="buildingForm.id ? '楼栋修改': '楼栋添加'" href="/pw/pwSpace/list"></edit-bar>
        </shiro:hasPermission>
        <shiro:lacksPermission name="pw:pwSpace:edit">
            <edit-bar second-name="楼栋查看" href="/pw/pwSpace/list"></edit-bar>
        </shiro:lacksPermission>
    </div>


    <el-form size="mini" :model="buildingForm" :rules="buildingFormRules" ref="buildingForm" :disabled="formDisabled"
             class="space-building-form"
             action="${ctx}/pw/pwSpace/save" method="POST" label-width="120px">

        <input type="hidden" id="id" name="id" :value="buildingForm.id">
        <input type="hidden" id="parent.id" name="parent.id" :value="buildingForm.parentId">
        <input type="hidden" id="type" name="type" value="3">
        <input type="hidden" id="secondName" name="secondName" :value="secondName">

        <el-form-item label="所属学院：" v-if="school">
            {{school}}
        </el-form-item>

        <el-form-item label="所属校区：" v-if="campus">
            {{campus}}
        </el-form-item>

        <el-form-item label="所属基地：" v-if="base">
            {{base}}
        </el-form-item>

        <el-form-item prop="name" label="楼栋名称：">
            <el-input id="name" name="name" v-model="buildingForm.name" class="w300"></el-input>
        </el-form-item>

        <el-form-item prop="person" label="负责人：">
            <el-input id="person" name="person" v-model="buildingForm.person" class="w300"></el-input>
        </el-form-item>

        <el-form-item prop="phone" label="联系方式：">
            <el-input id="phone" name="phone" v-model="buildingForm.phone" class="w300"></el-input>
        </el-form-item>

        <el-form-item label="开放时间：">
            <input type="hidden" name="openWeeks" :value="buildingForm.openWeeks">

            <control-rule-block title="选择开放日" class="control-rule_appointment-rule" style="padding-top:7px;">
                <el-checkbox-group v-model="buildingForm.openWeeks">
                    <el-checkbox-button v-for="week in pwSpaceWeek" :key="week.id"
                                        :label="week.value">{{week.label}}
                    </el-checkbox-button>
                </el-checkbox-group>
            </control-rule-block>

            <control-rule-block title="设置开放时间段" class="control-rule_appointment-rule" style="padding-top:7px;">
                <input type="hidden" name="amOpenStartTime" :value="buildingForm.amOpenStartTime">
                <input type="hidden" name="amOpenEndTime" :value="buildingForm.amOpenEndTime">
                <input type="hidden" name="pmOpenStartTime" :value="buildingForm.pmOpenStartTime">
                <input type="hidden" name="pmOpenEndTime" :value="buildingForm.pmOpenEndTime">
                <el-form-item label="上午" label-width="36px">
                    <el-col :span="6">
                        <el-form-item prop="amOpenStartTime">
                            <el-time-select style="width: 100%;"
                                            placeholder="起始时间"
                                            v-model="buildingForm.amOpenStartTime"
                                            default-value="10:00"
                                            :picker-options="{
                            start: '00:00',
                            step: '00:30',
                            end: '12:00'
                          }">
                            </el-time-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="1">
                        <div style="text-align: center;">至</div>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item prop="amOpenEndTime">
                            <el-time-select style="width: 100%;"
                                            placeholder="结束时间"
                                            v-model="buildingForm.amOpenEndTime"
                                            default-value="12:00"
                                            :picker-options="{
                            start: '00:00',
                            step: '00:30',
                            end: '12:00',
                            minTime: buildingForm.amOpenStartTime
                          }">
                            </el-time-select>
                        </el-form-item>
                    </el-col>
                </el-form-item>
                <el-form-item label="下午" label-width="36px">
                    <el-col :span="6">
                        <el-form-item prop="pmOpenStartTime">
                            <el-time-select style="width: 100%;"
                                            placeholder="起始时间"
                                            v-model="buildingForm.pmOpenStartTime"
                                            :picker-options="{
                            start: '12:00',
                            step: '00:30',
                            end: '24:00'
                          }">
                            </el-time-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="1">
                        <div style="text-align: center;">至</div>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item prop="pmOpenEndTime">
                            <el-time-select style="width: 100%;"
                                            placeholder="结束时间"
                                            v-model="buildingForm.pmOpenEndTime"
                                            default-value="18:00"
                                            :picker-options="{
                            start: '12:00',
                            step: '00:30',
                            end: '24:00',
                            minTime: buildingForm.pmOpenStartTime
                          }">
                            </el-time-select>
                        </el-form-item>
                    </el-col>
                </el-form-item>

            </control-rule-block>

        </el-form-item>

        <el-form-item prop="floorNum" label="楼层：">
            <el-input name="floorNum" v-model="buildingForm.floorNum" :readonly="bEmpty != 'y'"
                      class="w300">
                <template slot="append">层</template>
            </el-input>
        </el-form-item>

        <el-form-item prop="area" label="占地面积：">
            <el-input id="area" name="area" v-model="buildingForm.area" class="w300">
                <template slot="append">平方米</template>
            </el-input>
        </el-form-item>

        <el-form-item prop="imageUrl" label="建筑图片：" class="common-upload-one-img">
            <input type="hidden" name="imageUrl" v-model="buildingForm.imageUrl">

            <div class="upload-box-border" style="width:135px;height:100px;">
                <el-upload
                        class="avatar-uploader"
                        action="${ctx}/ftp/ueditorUpload/uploadTemp"
                        :show-file-list="false"
                        :on-success="fileSuccess"
                        :on-error="fileError"
                        name="upfile"
                        accept="image/jpg, image/jpeg, image/png">
                    <img v-if="fileBuildingImg" :src="fileBuildingImg | ftpHttpFilter(ftpHttp)">
                    <i v-if="!fileBuildingImg"
                       class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
                <div class="arrow-block-delete" v-if="fileBuildingImg">
                    <i class="el-icon-delete" @click.sotp.prevent="fileBuildingImg = ''"></i>
                </div>
            </div>
            <div class="img-tip">
                仅支持上传jpg/png文件，建议背景图片大小：270 × 200（像素）
            </div>

        </el-form-item>

        <el-form-item prop="remarks" label="备注：">
            <el-input type="textarea" name="remarks" :rows="5" v-model="buildingForm.remarks" maxlength="200"
                      style="width:500px;"></el-input>
        </el-form-item>


        <el-form-item>
            <shiro:hasPermission name="pw:pwSpace:edit">
                <el-button type="primary" :disabled="formDisabled"
                           @click.stop.prevent="saveForm('buildingForm')">保存
                </el-button>
            </shiro:hasPermission>
        </el-form-item>

    </el-form>


</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var pwSpace = JSON.parse(JSON.stringify(${fns:toJson(pwSpace)})) || [];
            var pwSpaceWeek = JSON.parse(JSON.stringify(${fns:toJson(fns:getDictList('pw_space_week'))}));
            var nameReg = /['"\s“”‘’]+/;
            var mobileReg = /^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\d{8}$$/;
            var areaReg = /^([1-9]{1}[0-9]{0,5}(\.{1}(?=\d+))|[1-9]{1}[0-9]{0,1})\d{0,4}$/;
            var reg = /^[1-9][0-9]*$/;
            var validateName = function (rule, value, callback) {
                if (value) {
                    if (nameReg.test(value)) {
                        callback(new Error('楼栋名称存在空格或者引号'))
                    } else {
                        callback();
                    }
                } else {
                    callback();
                }
            };
            var validatePhone = function (rule, value, callback) {
                if (value) {
                    if (!mobileReg.test(value)) {
                        callback(new Error('请输入有效的联系方式'));
                    } else {
                        callback();
                    }
                } else {
                    callback();
                }
            };
            var validateArea = function (rule, value, callback) {
                if (value && !areaReg.test(value)) {
                    callback(new Error('请输入数字，如为小数，则小数点前最多6位数且最多保留4位小数'));
                } else {
                    callback();
                }
            };
            var validateFloorNum = function (rule, value, callback) {
                var len = value.toString().length;
                if (value && !reg.test(value)) {
                    callback(new Error('请输入正整数'))
                } else if (value && len > 2) {
                    callback(new Error('楼层最大2位数'))
                } else {
                    callback();
                }
            };

            return {
                pwSpaceWeek: pwSpaceWeek,
                buildingForm: {
                    id: pwSpace.id || '',
                    name: pwSpace.name || '',
                    person: pwSpace.person || '',
                    phone: pwSpace.phone || '',
                    floorNum: pwSpace.floorNum || '',
                    remarks: pwSpace.remarks || '',
                    area: pwSpace.area || '',
                    imageUrl: pwSpace.imageUrl || '',
                    parentId: pwSpace.parentId || '',
                    openWeeks: pwSpace.openWeeks || [],
                    amOpenStartTime: pwSpace.amOpenStartTime || '',
                    amOpenEndTime: pwSpace.amOpenEndTime || '',
                    pmOpenStartTime: pwSpace.pmOpenStartTime || '',
                    pmOpenEndTime: pwSpace.pmOpenEndTime || ''
                },
                secondName: '${secondName}',
                school: '${school}',
                campus: '${campus}',
                base: '${base}',
                bEmpty: '${bEmpty}',
                formDisabled: false,
                message: '${message}',
                fileBuildingImg: '',
                buildingFormRules: {
                    name: [
                        {required: true, message: '请输入楼栋名称', trigger: 'change'},
                        {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'change'},
                        {validator: validateName, trigger: 'blur'}
                    ],
                    person: [
                        {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'change'},
                        {validator: validateName, trigger: 'change'}
                    ],
                    area: [
                        {validator: validateArea, trigger: 'change'}
                    ],
                    phone: [
                        {validator: validatePhone, trigger: 'change'}
                    ],
                    floorNum: [
                        {required: true, message: '请输入楼层', trigger: 'change'},
                        {validator: validateFloorNum, trigger: 'change'}
                    ]
                }
            }
        },
        computed: {},
        watch: {
            fileBuildingImg: function (value) {
                this.buildingForm.imageUrl = value;
            }
        },
        methods: {
            setFileBuildingImg: function (value) {
                if (!value) {
                    return;
                }
                this.fileBuildingImg = value;
            },
            fileError: function (err, file, fileList) {
                if (err.state == 'error') {
                    this.$message({
                        message: '上传失败',
                        type: 'error'
                    });
                }
            },

            fileSuccess: function (response, file) {
                if (response.state === 'SUCCESS') {
                    this.fileBuildingImg = file.response.ftpUrl;
                } else {
                    this.$message({
                        message: '上传失败',
                        type: 'error'
                    });
                }
            },

            saveForm: function (formName) {
                var self = this;
                self.$refs[formName].validate(function (valid) {
                    if (valid) {
                        self.formDisabled = true;
                        self.$refs.buildingForm.$el.submit();
                    }
                })
            },
            goToBack: function () {
                window.location.href = this.frontOrAdmin + '/pw/pwSpace/list';
            }
        },
        created: function () {
            this.setFileBuildingImg(this.buildingForm.imageUrl);
            this.isAll = this.buildingForm.openWeeks.length == this.pwSpaceWeek.length;
            if (this.message) {
                this.$message({
                    message: this.message,
                    type: this.message.indexOf('成功') > -1 ? 'success' : 'warning'
                });
                this.message = '';
            }
        }
    })

</script>

</body>
</html>