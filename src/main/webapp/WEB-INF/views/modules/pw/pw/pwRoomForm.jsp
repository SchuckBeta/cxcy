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

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60 space-room-form">
    <div class="mgb-20">
        <shiro:hasPermission name="pw:pwRoom:edit">
            <edit-bar :second-name="roomForm.id ? '修改': '添加'" href="/pw/pwRoom/tree"></edit-bar>
        </shiro:hasPermission>
        <shiro:lacksPermission name="pw:pwRoom:edit">
            <edit-bar second-name="查看" href="/pw/pwRoom/tree"></edit-bar>
        </shiro:lacksPermission>
    </div>

    <el-form size="mini" :model="roomForm" :rules="roomFormRules" ref="roomForm" :disabled="roomFormDisabled"
             label-width="120px">
        <el-form-item prop="pwSpace.id" label="选择场地：">
            <el-cascader name="space" :options="collegesTree" :clearable="true"
                         class="w300" filterable placeholder="请选场地（可搜索）"
                         :props="cascaderProps"
                         change-on-select
                         @change="handleChangePlace"
                         @blur="handleBlurPlace"
                         v-model="cascaderList">
            </el-cascader>
        </el-form-item>
        <el-form-item prop="type" label="房间类型：">
            <div class="button-border">
                <el-radio-group v-model="roomForm.type">
                    <el-radio v-for="item in pwRoomTypes" :key="item.id" :label="item.value" class="break-ellipsis">{{item.label}}</el-radio>
                </el-radio-group>
                <a href="javascript:void(0)" @click.stop.prevent="dialogVisible = true"><i
                        class="el-icon-circle-plus"></i></a>
            </div>
            <div class="room-type-tip">可到基础数据维护页面中对房间类型进行维护</div>
        </el-form-item>
        <el-form-item prop="name" label="房间名称：">
            <el-input v-if="!roomForm.id" v-model="roomForm.name" class="w300"></el-input>
            <span v-else>{{roomForm.name}}</span>
        </el-form-item>
        <el-form-item prop="num" label="选填房间容量：">

            <el-input v-model="roomForm.num" class="input-with-select w300" placeholder="请输入房间容量">
                <el-select v-model="roomForm.numtype" slot="prepend" placeholder="请选择">
                    <el-option v-for="item in numTypes" :key="item.id" :label="item.label"
                               :value="item.value"></el-option>
                </el-select>
                <template slot="append">{{numLabel}}</template>
            </el-input>

        </el-form-item>
        <el-form-item prop="area" label="占地面积：">
            <el-input v-model="roomForm.area" class="w300">
                <template slot="append">平方米</template>
            </el-input>
        </el-form-item>
        <el-form-item prop="person" label="负责人：">
            <el-input v-model="roomForm.person" class="w300"></el-input>
        </el-form-item>
        <el-form-item prop="mobile" label="联系电话：">
            <el-input v-model="roomForm.mobile" class="w300"></el-input>
        </el-form-item>
        <el-form-item prop="color" label="预约房间色值：">
            <el-color-picker size="medium"
                             v-model="roomForm.color"
                             :predefine="predefineColors">
            </el-color-picker>
        </el-form-item>
        <el-form-item prop="remarks" label="备注：">
            <el-input type="textarea" :rows="5" v-model="roomForm.remarks" maxlength="200" style="width:500px;"></el-input>
        </el-form-item>


        <el-form-item>
            <shiro:hasPermission name="pw:pwRoom:edit">
                <el-button type="primary" :disabled="roomFormDisabled"
                           @click.stop.prevent="saveRoomForm('roomForm')">保存
                </el-button>
            </shiro:hasPermission>
        </el-form-item>

    </el-form>


    <el-dialog title="添加房间类型" :visible.sync="dialogVisible" width="430px" :before-close="handleClose"
               :close-on-click-modal="isClose">
        <el-form size="mini" :model="dialogForm" :rules="dialogFormRules" ref="dialogForm" :disabled="dialogFormDisabled"
                 label-width="120px">
            <el-form-item prop="name" label="名称：" style="margin-top:20px;">
                <el-input v-model="dialogForm.name" style="width:200px;"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button size="mini" @click.stop.prevent="handleClose">取消</el-button>
          <el-button size="mini" type="primary" :disabled="dialogFormDisabled" @click.stop.prevent="saveDialog('dialogForm')">保存</el-button>
        </span>
    </el-dialog>


</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin,Vue.roomFormMixin],
        data: function () {
            var pwRoom = JSON.parse(JSON.stringify(${fns:toJson(pwRoom)})) || [];
            pwRoom.color = '#' + pwRoom.color;
            var pwSpace = pwRoom.pwSpace || {};
            return {
                pwRoom: pwRoom,
                pwRoomTypes: [],
                numTypes: [],
                predefineColors: [
                    '#e9432d',
                    '#ff8c00',
                    '#ffd700',
                    '#90ee90',
                    '#00ced1',
                    '#1e90ff',
                    '#c71585'
                ],
                colleges: [],
                currentName:pwRoom.name || '',
                roomForm: {
                    id: pwRoom.id || '',
                    name: pwRoom.name || '',
                    person: pwRoom.person || '',
                    area: pwRoom.area || '',
                    type: pwRoom.type || '',
                    mobile: pwRoom.mobile || '',
                    remarks: pwRoom.remarks || '',
                    num: pwRoom.num || '',
                    numtype: pwRoom.numtype || '',
                    color: pwRoom.color,
                    pwSpace: {
                        id: pwSpace.id || ''
                    }
                },
                parentIds: pwSpace.parentIds || '',
                cascaderProps: {
                    label: 'name',
                    value: 'id',
                    children: 'children'
                },
                cascaderList: [],
                collegesProps: {
                    parentKey: 'pId',
                    id: 'id'
                },
                dialogVisible: false,
                isClose: false,
                dialogForm: {
                    typeid: '${typeid}',
                    name: ''
                },
                dialogFormRules: {
                    name: [
                        {required: true, message: '请输入名称', trigger: 'change'},
                        {min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'change'}
                    ]
                },
                dialogFormDisabled:false,
                roomFormDisabled:false

            }
        },
        computed:{
            spaceListEntries: function () {
                var entries = {};
                this.colleges.forEach(function (item) {
                    entries[item.id] = item;
                });
                return entries;
            },
            numLabel:function () {
                if(this.roomForm.numtype == '2'){
                    return '位';
                }
                return '人';
            }
        },
        methods: {
            getTree: function () {
                var self = this;
                this.$axios.get('/pw/pwSpace/treeData').then(function (response) {
                    self.colleges = response.data;
                    self.setCollegeEntries(self.colleges);
                    var rootIds = self.setCollegeRooIds(self.colleges);
                    self.collegesTree = self.getCollegesTree(rootIds, self.collegesProps);
                })
            },
            getCascaderList:function () {
                var arr = [];
                arr = this.parentIds.split(',').slice(2);
                arr.splice(arr.length-1,1);
                arr.push(this.roomForm.pwSpace.id);
                this.cascaderList = arr || [];
            },
            handleChangePlace:function (value) {
                this.roomForm.pwSpace.id = value[value.length - 1];
            },
            handleBlurPlace:function () {
                if(this.roomForm.name){
                    this.$refs.roomForm.validateField('name');
                }
            },
            saveRoomForm:function (formName) {
                var self = this;
                this.roomForm.color = this.roomForm.color.split('').slice(1).join('');
                this.roomForm.pwSpace.id = this.cascaderList[this.cascaderList.length - 1];
                self.$refs[formName].validate(function (valid) {
                    if (valid) {
                        self.saveAjax();
                    }
                });
            },
            saveAjax:function () {
                var self = this;
                this.roomFormDisabled = true;
                this.$axios({
                    method:'POST',
                    url:'/pw/pwRoom/save',
                    data:self.roomForm
                }).then(function (response) {
                    var data = response.data;
                    if(data.status == '1'){
                        window.location.href = self.frontOrAdmin + '/pw/pwRoom/tree';
                    }
                    self.roomFormDisabled = false;
                    self.$message({
                        message: data.status == '1' ? '保存成功' : data.msg || '保存失败',
                        type: data.status == '1' ? 'success' : 'error'
                    })
                }).catch(function () {
                    self.roomFormDisabled = false;
                    self.$message({
                        message: '请求失败',
                        type:'error'
                    })
                });
            },
            handleClose: function () {
                this.dialogVisible = false;
                this.$refs.dialogForm.resetFields();
            },
            saveDialog: function (formName) {
                var self = this;
                self.$refs[formName].validate(function (valid) {
                    if (valid) {
                        self.saveDialogAjax();
                    }
                })
            },
            saveDialogAjax: function () {
                var self = this;
                this.dialogFormDisabled = true;
                this.$axios({
                    method:'GET',
                    url:'/sys/dict/addDict',
                    params:Object.toURLSearchParams(self.dialogForm)
                }).then(function (response) {
                    var data = response.data;
                    if(data.ret == '1'){
                        self.getPwRoomTypes();
                        self.handleClose();
                    }
                    self.dialogFormDisabled = false;
                    self.$message({
                        message: data.ret == '1' ? '房间类型添加成功' : data.msg || '房间类型添加成功失败',
                        type: data.ret == '1' ? 'success' : 'error'
                    })
                }).catch(function () {
                    self.dialogFormDisabled = false;+
                    self.$message({
                        message: '请求失败',
                        type:'error'
                    })
                });
            },
            getPwRoomTypes:function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=pw_room_type').then(function (response) {
                    self.pwRoomTypes = response.data;
                })
            },
            getNumTypes:function () {
                var self = this;
                this.$axios.post('/pw/pwRoom/pwRoomType').then(function (response) {
                    var data = response.data;
                    self.numTypes = data.data;
                })
            },
            goToBack:function () {
                window.location.href = this.frontOrAdmin + '/pw/pwRoom/tree';
            }
        },
        created: function () {
            this.getPwRoomTypes();
            this.getCascaderList();
            this.getTree();
            this.getNumTypes();
        }
    })

</script>

</body>
</html>