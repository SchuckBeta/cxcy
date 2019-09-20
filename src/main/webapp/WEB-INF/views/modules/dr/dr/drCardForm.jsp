<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>

<div id="app" class="container-fluid" v-show="pageLoad" style="display: none">
    <div class="mgb-20">
        <edit-bar second-name="${secondName}"></edit-bar>
    </div>
    <el-form :model="drCardForm" ref="drCardForm" :rules="drCardFormRules" size="mini" label-width="100px">
        <e-panel :label="userPanelLabel">
            <!--通用卡-->
            <template v-if="drCard.cardType === '1'">
                <el-row :gutter="20" label-width="100px">
                    <el-col :span="6">
                        <e-col-item label="姓名：">{{user.name}}</e-col-item>
                    </el-col>
                    <el-col :span="6">
                        <e-col-item label="学号：">{{user.no}}</e-col-item>
                    </el-col>
                    <el-col :span="6">
                        <e-col-item label="所属学院：">{{user.office && user.office.name}}</e-col-item>
                    </el-col>
                    <el-col :span="6">
                        <e-col-item label="入驻类型：">{{pwEnterInfo.enterTypeStr}}</e-col-item>
                    </el-col>
                    <el-col :span="6">
                        <e-col-item label="入驻有效期：">{{pwEnterInfo.enterEndTime}}</e-col-item>
                    </el-col>
                    <el-col :span="18">
                        <e-col-item label="已分配房间：">
                        <div v-for="room in rooms">
                            {{room.roomFullName}}
                        </div>
                        </e-col-item>
                    </el-col>
                </el-row>
            </template>
            <!--临时卡-->
            <template v-else>
                <div class="mgb-20">
                    <el-checkbox class="el-checkbox__label_font12" :disabled="hasUser" v-model="hasExistUser">是否选择已存在用户开卡</el-checkbox>
                </div>

                <template v-if="!hasUser && !hasExistUser">
                    <el-row :gutter="20">
                        <el-col :span="6">
                            <el-form-item label="姓名：" prop="tmpName">
                                <el-input name="tempUser.name" v-model="drCardForm['tmpName']"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="手机号：" prop="tmpTel">
                                <el-input name="tempUser.phone" v-model="drCardForm['tmpTel']"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="工号/学号：" prop="tmpNo">
                                <el-input name="tempUser.no" v-model="drCardForm['tmpNo']"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="院系/专业"
                                          prop="professional"
                            >
                                <el-cascader
                                        style="width: 100%"
                                        ref="cascader"
                                        :options="collegesTree"
                                        :clearable="true"
                                        v-model="drCardForm['professional']"
                                        @change="handleChangeOffice"
                                        placeholder="请选择院系/专业"
                                        :props="{
                                            label: 'name',
                                            value: 'id',
                                            children: 'children'
                                        }"
                                >
                                </el-cascader>
                            </el-form-item>
                        </el-col>
                        <el-col :span="24">
                            <el-form-item>
                                <el-button type="primary" :disabled="isSaveUser" @click.stop.prevent="saveNewUser">保存用户</el-button>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </template>

                <template v-else>
                    <el-form-item prop="userId" label-width="0px"
                                  :rules="[{required: true, message: '请输入已存在用户姓名', trigger: 'change'}]">
                        <el-autocomplete class="el-autocomplete-font12"
                                         size="mini"
                                         v-model="drCardForm.userId"
                                         value-key="name"
                                         value="id"
                                         :fetch-suggestions="querySearchAsync"
                                         placeholder="请输入已存在用户姓名"
                                         @select="handleSelectedUser"
                        ></el-autocomplete>
                    </el-form-item>
                    <el-row v-show="pwEnterQueryUser.name" :gutter="20">
                        <el-col :span="6">
                            <e-col-item label="姓名：">{{pwEnterQueryUser.name}}</e-col-item>
                        </el-col>
                        <el-col :span="6">
                            <e-col-item label="学号：">{{pwEnterQueryUser.no}}</e-col-item>
                        </el-col>
                        <el-col :span="6">
                            <e-col-item label="所属学院：">{{pwEnterQueryUser.officeName}}</e-col-item>
                        </el-col>
                    </el-row>
                </template>
            </template>
        </e-panel>
        <e-panel label="发卡信息">
            <el-row :gutter="20">
                <el-col :span="6">
                    <el-form-item label="卡号：" prop="no" :rules="[{required: !isSaveUser, message: '请输入卡号', trigger: 'blur'},{validator: validateCardNo}]">
                        <el-input name="no" v-model="drCardForm.no" placeholder="请输入4至10位且不能以0开头的数字卡号"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="有效期：" prop="expiry" :rules="[{required: !isSaveUser, message: '请输入有效期', trigger:'change'}]">
                        <el-date-picker
                                name="expiry"
                                v-model="drCardForm.expiry"
                                type="date"
                                :picker-options="expiryPickerOptions"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                :default-time="['23:59:59']"
                                placeholder="选择日期">
                        </el-date-picker>
                    </el-form-item>
                </el-col>
            </el-row>
        </e-panel>

        <e-panel label="授权其它房间使用权限">
            <div class="table-container">
                <el-table class="e-table-tree" :data="flattenPwSpaceList" size="mini" :cell-style="eTableCellStyle">
                    <el-table-column label="可使用场地" align="left" width="320">
                        <template slot-scope="scope">
                            <span class="e-table-tree-dot" v-if="index>0"
                                  v-for="(dot, index) in scope.row.dots.split('-').length"></span>
                            <i :class="elIconCaret(scope.row)" class="el-icon-caret-right"
                               @click.stop.prevent="handleExpandCell(scope.row)"></i>
                            <el-checkbox v-model="scope.row.allChecked" :indeterminate="scope.row.indeterminate"
                                         @change="handleSpaceAllChecked(scope.row)">
                            </el-checkbox>
                            <span class="e-checkbox__label_dr_card">{{scope.row.sname}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="门禁">
                        <template slot-scope="scope">
                            <el-checkbox v-if="scope.row.doors.length > 0 && scope.row.children"
                                         class="el-checkbox-group__dr_card"
                                         :indeterminate="scope.row.doorIsIndeterminate"
                                         v-model="scope.row.doorAllChecked"
                                         @change="handleDoorAllCheckChange(scope.row)">全选
                            </el-checkbox>
                            <el-checkbox-group v-model="scope.row.doorsChecked" class="el-checkbox-group__dr_cards"
                                               @change="handleDoorSingleChange(scope.row)">
                                <span v-for="door in scope.row.doors"
                                      :key="door.eptId + ',' + door.eptNo + ',' + door.drNo">
                                    <el-checkbox
                                            :label="door.eptId + ',' + door.eptNo + ',' + door.drNo">{{door.dname}}</el-checkbox>
                                    <span v-show="door.selStatus == 1" class="door-status dealId">授权中</span>
                                    <span v-show="door.selStatus == 2" class="door-status">授权失败</span>
                                </span>
                            </el-checkbox-group>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="mgb-20" style="padding-left: 15px;padding-top: 15px;">
                    <el-button size="mini" type="primary" :disabled="isSubmit" @click.stop.prevent="submitDrCardForm">
                        发卡
                    </el-button>
                    <el-button size="mini" type="default" :disabled="isSubmit"
                               @click.stop.prevent="backToPage">返回
                    </el-button>
                </div>
            </div>

        </e-panel>
    </el-form>
</div>
<script>

    'use strict';


    new Vue({
        el: '#app',
        mixins: [Vue.drPwSpaceListMixin, Vue.collegesMixin, Vue.verifyExpressionBackMixin],
        data: function () {
            var self = this;
            var colleges = JSON.parse('${fns: toJson(fns: getOfficeList())}') || [];
            var drPwSpaceList = JSON.parse('${pwSpaceInfo}');
            var rooms = JSON.parse('${fns: toJson(pwEnterInfo.rooms)}') || [];
            var drCard = JSON.parse('${fns: toJson(drCard)}') || {};
            var drCstatuss = '${drCstatuss}' || '[]';
            var user = JSON.parse('${fns: toJson(drCard.user)}') || {};
            var pwEnterInfo = JSON.parse('${fns: toJson(pwEnterInfo)}');

            colleges = colleges.filter(function (item) {
                return item.id !== '1';
            });



            var validateUserNo = function (rule, value, callback) {
                if (value) {
                    var drCardForm = self.drCardForm;
                    var userid = drCardForm.userId;
                    if (!/^[a-zA-Z0-9]+$/.test(value)) {
                        return callback(new Error('请输入英文或者数字'));
                    } else {
                        return self.$axios.post('/sys/user/checkNo', Object.toURLSearchParams({
                                    userid: userid,
                                    no: value
                                })).then(function (response) {
                            var data = response.data;
                            if (data) {
                                return callback();
                            }
                            return callback('学号已经存在')
                        })
                    }
                }
                return callback()
            };
            var validateUserName = this.validateUserName;
            var validateMobile = this.validateMobile;
            var validateMobileXhr = this.validateMobileXhr;

            var validateCheckRepeatXhr = function (rule, value, callback) {
                if(value){
                    return self.$axios.get('/dr/drCard/ajaxCheckRepeat?param='+value).then(function (response) {
                        var data = response.data;
                        if(!data.status){
                            return callback(new Error(data.msg))
                        }
                        return callback();
                    }).catch(function (error) {
                        return callback(new Error('请求失败'))
                    })
                }
                return callback()
            };
            var validateCardNo = function (rule, value, callback) {
                if (value) {
                	var index = value.indexOf('0');
                	if(index == 0){
                        return callback(new Error('卡号不能以0开头'))
                	}
                    if (/^[0-9]{4,10}/.test(value)) {
                        return self.$axios.post('/dr/drCard/checkCardNo?cardno=' + self.drCardForm.no + '&cardid=' + drCard.id).then(function (response) {
                            var data = response.data;
                            if (data) {
                                return callback();
                            }
                            return callback(new Error('卡号已经存在'))
                        })
                    }
                    return callback(new Error('请输入4至10位数字卡号'))
                }
                return callback();
            };



            var drProfessional = [];
            if(drCard.tmpOfficeXyId){
                drProfessional.push(drCard.tmpOfficeXyId);
                if(drCard.tmpOfficeZyId){
                    drProfessional.push(drCard.tmpOfficeZyId)
                }
            }
            return {
                user: user,
                drCard: drCard,
                drCstatuss: JSON.parse(drCstatuss),
                drPwSpaceList: drPwSpaceList,
                flattenPwSpaceList: [],
                rooms: rooms,
                pwEnterInfo: pwEnterInfo,
                colleges: colleges,
                isDefaultCollegeRootId: false, //去掉学院;
                drCardForm: {
                    'tmpName': drCard.tmpName,
                    'tmpTel': drCard.tmpTel,
                    'tmpNo': drCard.tmpNo,
                    'no': drCard.no,
                    'expiry': moment(drCard.expiry).format('YYYY-MM-DD'),
                    'userId': user.name,
                    'tmpOfficeXyId': drCard.tmpOfficeXyId,
                    'tmpOfficeZyId': drCard.tmpOfficeZyId,
                    'tmpOfficeXy': drCard.tmpOfficeXy,
                    'tmpOfficeZy': drCard.tmpOfficeZy,
                    professional: drProfessional
                },
                drCardFormRules: {
                    'tmpTel': [
                        {required: true, message: '请输入手机号码', trigger: 'blur'},
                        {validator: validateMobile, trigger: 'blur'},
                        {validator: validateMobileXhr, trigger: 'blur'},
                        {validator: validateCheckRepeatXhr, trigger: 'blur'}
                    ],
                    'tmpName': [
                        {required: true, message: '请输入姓名', trigger: 'blur'},
                        {validator: validateUserName, trigger: 'blur'}
                    ],
                    'tmpNo': [
                        {validator: validateUserNo, trigger: 'blur'},
                        {validator: validateCheckRepeatXhr, trigger: 'blur'}
                    ],
//                    'no': [
//                        {required: true, message: '请输入卡号', trigger: 'blur'},
//                        {validator: validateCardNo, trigger: 'blur'}
//                    ],
//                    'expiry': [
//                        {required: true, message: '请选择有效期时间', trigger: 'change'}
//                    ],
                    'professional': [{required: true, message: '请选择院系/专业', trigger: 'change'}]
                },
                validateCardNo: validateCardNo,
                expiryPickerOptions: {
                    disabledDate: function (time) {
                        return time.getTime() < Date.now();
                    }
                },
                tickTimer: null,
                isSubmit: false,
                isProcesses: [],
                flattenDoors: [],
                hasExistUser: !!user.id,
                hasUser: !!user.id,
                userList: [],
                pwEnterQueryUser: {},
                isSaveUser: false
            }
        },
        computed: {
            userPanelLabel: {
                get: function () {
                    return this.user.id ? '入驻人信息' : '个人信息'
                }
            },
            backHref: {
                get: function () {
                    return document.referrer;
                }
            }
        },
        methods: {
            //控制图标
            elIconCaret: function (row) {
                return {
                    'is-leaf': !row.children || !row.children.length,
                    'expand-icon': row.isExpand
                }
            },
            //控制行的样式
            eTableCellStyle: function (row) {
                return {
                    'display': row.row.isCollapsed ? 'none' : ''

                }
            },

            //控制行的展开收起
            handleExpandCell: function (row) {
                var children = row.children;
                if (!children) {
                    return;
                }
                row.isExpand = !row.isExpand;
                if (row.isExpand) {
                    this.expandCellTrue(children, row.isExpand);
                    return
                }
                this.expandCellFalse(children, row.isExpand);
            },

            expandCellTrue: function (list, b) {
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    item.isCollapsed = !b;
                    item.isExpand = false;
                }
            },

            expandCellFalse: function (list, b) {
                var childrenIds = this.getPwSpaceChildrenIds(list);
                for (var i = 0; i < this.flattenPwSpaceList.length; i++) {
                    var item = this.flattenPwSpaceList[i];
                    if (childrenIds.indexOf(item.sid) > -1) {
                        item.isCollapsed = !b;
                        item.isExpand = false;
                    }
                }
            },

            handleSpaceAllChecked: function (row) {
                this.setSpaceChildrenAllChecked(row, row.allChecked);
                row.doorAllChecked = row.allChecked;
                this.handleDoorAllCheckChange(row);
            },

            backToPage: function () {
                location.href = this.backHref
            },

            handleSelectedUser: function (value) {
                this.pwEnterQueryUser = value
            },
            handleChangeOffice: function (value) {
                var tempObj = {};
                if(value.length > 0){
                    tempObj = {
                        'tmpOfficeXyId': value[0],
                        'tmpOfficeZyId': value[1],
                        'tmpOfficeXy': this.collegeEntries[value[0]].name,
                        'tmpOfficeZy': this.collegeEntries[value[1]].name
                    };
                }else {
                    tempObj = {
                        'tmpOfficeXyId': '',
                        'tmpOfficeZyId': '',
                        'tmpOfficeXy': '',
                        'tmpOfficeZy': ''
                    }
                }
                Object.assign(this.drCardForm, tempObj)
            },
            querySearchAsync: function (queryString, cb) {
                var self = this;
                this.$axios.post('/dr/drCard/ajaxAllNoCardUsers?' + Object.toURLSearchParams({qryStr: this.drCardForm.userId})).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.userList = data.datas;
                        cb(data.datas)
                    } else {
                        cb([])
                    }
                })
            },
            saveNewUser: function () {
                var self =  this;

                this.$refs.drCardForm.validateField('professional',function (errorMessage) {
                    if(!errorMessage){
                        self.postUser();
                    }
                })

            },

            postUser: function () {
                var postData = this.getSaveNewUserPostData();
                var self = this;
                this.isSaveUser = true;
                this.$refs.drCardForm.validate(function (valid) {
                    if(valid){
                        self.$axios({
                            method: 'post',
                            url: '/dr/drCard/ajaxSaveUser',
                            data: postData,
                        }).then(function (response) {
                            var data = response.data;
                            if (data.status) {
                                self.user.id = data.datas;
                            }
                            self.$message({
                                type: data.status ? 'success' : 'error',
                                message: data.msg
                            });
                            self.isSaveUser = false;
                        }).catch(function (error) {
                            self.isSaveUser = false;
                            self.$message({
                                type: 'error',
                                message: self.xhrErrorMsg
                            });
                        })
                    }else{
                        self.isSaveUser = false;
                    }
                })

            },

            getSaveNewUserPostData: function () {
                var drCardForm = this.drCardForm
                var user = this.user;
                var collegeEntries = this.collegeEntries;
                return {
                    id: user.id || '11111',
                    name: drCardForm.tmpName,
                    mobile: drCardForm.tmpTel,
                    no: drCardForm.tmpNo,
                    office: collegeEntries[drCardForm.professional[0]].name,
                    professional: collegeEntries[drCardForm.professional[1]].name,
                    officeId: drCardForm.professional[0],
                    professionalId: drCardForm.professional[1]
                };
            },
            //获取所有子的ID
            getPwSpaceChildrenIds: function (list) {
                var ids = [];

                function getIds(list) {
                    if (!list) return ids;
                    for (var i = 0; i < list.length; i++) {
                        ids.push(list[i].sid);
                        getIds(list[i].children);
                    }
                }

                getIds(list);
                return ids;
            },

            setSpaceChildrenAllChecked: function (row, isChecked) {
                var children = row.children;
                if (!children) {
                    return false;
                }
                for (var i = 0; i < children.length; i++) {
                    children[i].allChecked = isChecked;
                    children[i].doorAllChecked = children[i].allChecked;
                    this.handleDoorAllCheckChange(children[i]);
                    this.setSpaceChildrenAllChecked(children[i], isChecked);
                }
            },

            getPostData: function () {
                this.drCard.drEmentNos = this.getDrEmentNos();
                var drCardForm = this.drCardForm;
                if (this.drCard.drEmentNos.length < 1) {
                    return false;
                }
                Object.assign(this.drCard, {
                    'tmpName': drCardForm.tmpName,
                    'tmpTel': drCardForm.tmpTel,
                    'tmpNo': drCardForm.tmpNo,
                    'no': drCardForm.no,
                    'tmpOfficeXyId': !this.hasExistUser ? drCardForm.professional[0] : '',
                    'tmpOfficeZyId': !this.hasExistUser ? drCardForm.professional[1] : '',
                    'tmpOfficeXy': !this.hasExistUser ? (this.collegeEntries[drCardForm.professional[0]] ? this.collegeEntries[drCardForm.professional[0]].name : '') : '',
                    'tmpOfficeZy': !this.hasExistUser ? (this.collegeEntries[drCardForm.professional[1]] ? this.collegeEntries[drCardForm.professional[1]].name : '') : '',
                    //'tmpOfficeXy': !this.hasExistUser ? this.collegeEntries[drCardForm.professional[0]].name : '',
                    //'tmpOfficeZy': !this.hasExistUser ? this.collegeEntries[drCardForm.professional[1]].name : '',
                    'expiry': drCardForm.expiry
                });
                this.drCard.user = {};


                this.drCard.user.id = this.pwEnterQueryUser.id || this.user.id;

                //if (this.drCard.cardType !== '1') {
                //    this.drCard.cardType = this.user.id ? '1' : '0';
                //}
                return [this.drCard];
            },

            getDrEmentNos: function () {
                var flattenPwSpaceList = this.flattenPwSpaceList;
                var drEmentNos = [];
                for (var i = 0; i < flattenPwSpaceList.length; i++) {
                    var item = flattenPwSpaceList[i];
                    var doorsChecked = item.doorsChecked;
                    if (!doorsChecked || !doorsChecked.length) {
                        continue;
                    }
                    for (var d = 0; d < doorsChecked.length; d++) {
                        var door = doorsChecked[d].split(',');
                        drEmentNos.push({
                            etId: door[0],
                            etNo: door[1],
                            drNo: door[2]
                        })
                    }
                }
                return drEmentNos;
            },

            submitDrCardForm: function () {
                var postData;
                var self = this;
                this.$refs.drCardForm.validate(function (valid) {
                    if (valid) {
                        postData = self.getPostData();
                        self.postDrCardForm(postData);
                    }
                })
            },

//            //return promise
//            ajaxCheckRepeat: function () {
//                return this.$axios.get('/dr/drCard/ajaxCheckRepeat?'+ Object.toURLSearchParams())
//            },

            postDrCardForm: function (postData) {
                var self = this;
                if (!postData) {
                    this.$alert('请勾选门禁设备', '提示', {
                        confirmButtonText: '确定',
                        type: 'warning'
                    });
                    return;
                }
                this.isSubmit = true;


                this.$axios.post('/dr/drCard/ajaxCardPublishPl', {list: postData}).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.drCard.id = data.datas[0];
                        self.openCardSuccess(data.msg);
                    } else {
                        self.$message({
                            type: data.status ? 'success' : 'error',
                            message: data.msg
                        });
                    }
                    self.isSubmit = false;

                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    });
                    self.isSubmit = false;
                })
            },

            openCardSuccess: function (msg) {
                var self = this;
                this.$alert(msg, '提示', {
                    confirmButtonText: '确定',
                    type: 'success',
                    callback: function () {
                        location.href = self.backHref;
//                        var tempObj = {
//                            id: self.drCard.id,
//                            'user.id': self.user.id,
//                            cardType: self.drCard.cardType
//                        };
//                        var hash = location.hash;
//                        var userId = globalUtils.getQueryString('user.id');
//                        if(hash && userId == tempObj['user.id']){
//                            location.reload();
//                            return;
//                        }
//                        location.href = self.frontOrAdmin + '/dr/drCard/form?' + Object.toURLSearchParams(tempObj) + '#time';
                    }
                })
            },


            extendPwSpaceList: function () {
                var flattenPwSpaceList = this.flattenPwSpaceList;
                for (var i = 0; i < flattenPwSpaceList.length; i++) {
                    var fPwSpace = flattenPwSpaceList[i];
                    var doors = fPwSpace.doors;
                    for (var d = 0; d < doors.length; d++) {
                        if (doors[d].sel == '1') {
                            var door = doors[d];
                            fPwSpace.allChecked = true;//有问题
                            fPwSpace.doorsChecked.push(door.eptId + ',' + door.eptNo + ',' + door.drNo);
                            fPwSpace.doorIsIndeterminate = fPwSpace.doorsChecked.length < doors.length;
                        }
                    }
                }
            },

            handleDoorAllCheckChange: function (row) {
                if (row.doorAllChecked) {
                    row.doorsChecked = this.getDoorIds(row.doors);
                } else {
                    row.doorsChecked = [];
                }
                row.doorIsIndeterminate = false;
            },

            handleDoorSingleChange: function (row) {
                var doorsCheckedLen = row.doorsChecked.length;
                row.doorAllChecked = doorsCheckedLen === row.doors.length;
                row.doorIsIndeterminate = doorsCheckedLen > 0 && doorsCheckedLen < row.doors.length;
            },

            getDoorIds: function (doors) {
                var ids = [];
                for (var d = 0; d < doors.length; d++) {
                    var door = doors[d];
                    ids.push(door.eptId + ',' + door.eptNo + ',' + door.drNo);
                }
                return ids;
            },

            getDrCardDoors: function () {
                var flattenPwSpaceList = this.flattenPwSpaceList;
                var self = this;
                this.flattenDoors = [];
                this.isProcesses = [];
                this.$axios.get('/dr/drCard/getCardData?id=' + this.drCard.id).then(function (response) {
                    var data = response.data || {};
                    for (var k in data) {
                        var val;
                        if (!data.hasOwnProperty(k)) {
                            continue;
                        }
                        val = data[k];
                        for (var i = 0; i < flattenPwSpaceList.length; i++) {
                            var item = flattenPwSpaceList[i];
                            var doors = item.doors;
                            if (doors.length < 1) {
                                continue;
                            }
                            for (var d = 0; d < doors.length; d++) {
                                var door = doors[d];
                                if (door.eptId + ',' + door.eptNo + ',' + door.drNo === k) {
                                    self.flattenDoors.push(k);
                                    door.selStatus = val;
                                    if (door.selStatus === '0' || door.selStatus === '2') {
                                        self.isProcesses.push(k);
                                    }
                                }
                            }
                        }
                    }
                    if (self.flattenDoors.length === self.isProcesses.length) {
                        clearInterval(self.tickTimer);
                    }
                })
            },

            getHash: function () {
                var hash = location.hash;
                if (!hash) {
                    return;
                }
                this.tick(this.getDrCardDoors)
            },

            tick: function (fn) {
                this.tickTimer = setInterval(function () {
                    fn && fn();
                }, '5000')

            }
        },
        created: function () {
            this.flattenPwSpaceList = this.getFlattenPwSpaceList(3);
            this.extendPwSpaceList();
            this.getHash();
        },
    })

</script>

</body>
</html>