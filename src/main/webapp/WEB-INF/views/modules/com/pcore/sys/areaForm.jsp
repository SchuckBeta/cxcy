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
        <shiro:hasPermission name="sys:area:edit">
            <edit-bar :second-name="saveForm.id ? '修改': '添加'"></edit-bar>
        </shiro:hasPermission>
        <shiro:lacksPermission name="sys:area:edit">
            <edit-bar second-name="查看"></edit-bar>
        </shiro:lacksPermission>
    </div>


    <el-form size="mini" :model="saveForm" :rules="saveFormRules" ref="saveForm" :disabled="formDisabled"
             label-width="120px">

        <el-form-item prop="parent.id" label="上级区域：">
            <el-cascader name="parent.id" :options="treeData" :clearable="true" :disabled="saveForm.id != ''"
                         class="w300" filterable placeholder="请选择上级区域（可搜索）"
                         :props="cascaderProps"
                         change-on-select
                         @change="handleChangeArea"
                         v-model="cascaderList">
            </el-cascader>
        </el-form-item>

        <el-form-item prop="name" label="区域名称：">
            <el-input v-model="saveForm.name" class="w300" maxlength="50" :disabled="saveForm.id != ''"></el-input>
        </el-form-item>

        <el-form-item prop="code" label="区域编码：">
            <el-input v-model="saveForm.code" class="w300" maxlength="50"></el-input>
        </el-form-item>

        <el-form-item prop="type" label="区域类型：">
            <el-select name="type" size="mini" v-model="saveForm.type" :disabled="saveForm.parent.id != '' && saveForm.parent.id != '0' || saveForm.id != ''"
                       placeholder="请选择区域类型（可搜索）" filterable class="w300">
                <el-option
                        v-for="item in areaTypes"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
            </el-select>
        </el-form-item>


        <el-form-item prop="remarks" label="备注：">
            <el-input type="textarea" name="remarks" :rows="5" v-model="saveForm.remarks" maxlength="200" style="width:500px;"></el-input>
        </el-form-item>


        <el-form-item>
            <shiro:hasPermission name="sys:area:edit">
                <el-button type="primary" :disabled="formDisabled"
                           @click.stop.prevent="save('saveForm')">保存
                </el-button>
            </shiro:hasPermission>
        </el-form-item>

    </el-form>


</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin],
        data: function () {
            var area = JSON.parse(JSON.stringify(${fns:toJson(area)})) || [];
            var areaTypes = JSON.parse(JSON.stringify(${fns:toJson(fns:getDictList('sys_area_type'))})) || [];
            var cascaderArea = [],type = '';
            if(area.parentId && area.parentId != '0'){
                cascaderArea = area.parentIds.split(',') || [];
                if(cascaderArea.indexOf('0') > -1){
                    cascaderArea = cascaderArea.slice(1);
                }
                cascaderArea.pop();
                if(!area.id){
                    cascaderArea = cascaderArea.concat([area.parentId]);
                    type = (cascaderArea.length + 2).toString();
                }
            }

            return {
                areaTypes:areaTypes,
				saveForm:{
                    id: area.id || '',
                    parent:{
                        id:area.parentId || ''
                    },
                    name:area.name || '',
                    code:area.code || '',
                    type:area.type || type || '1',
                    remarks:area.remarks || ''
                },
                formDisabled:false,
                message:'${message}',

                cascaderProps: {
                    label: 'name',
                    value: 'id',
                    childern: 'childern'
                },
                cascaderList: cascaderArea || [],
                treeData:[]


            }
        },
        computed:{
            saveFormRules:function () {
                var self = this;
                var nameReg = /['"\s“”‘’]+/;
                var validateName = function (rule, value, callback) {
                    if (value && nameReg.test(value)) {
                        callback(new Error('存在空格或者引号'))
                    } else {
                        callback();
                    }
                };
                var validateArea = function (rule, value, callback) {
                    if (value && self.cascaderList.length > 2) {
                        callback(new Error('最多可以选到第二级的区域'))
                    } else {
                        callback();
                    }
                };
                return {
                    parent:{
                        id:[
                            {validator: validateArea, trigger: 'change'}
                        ]
                    },
                    name:[
                        {required: true, message: '请输入区域名称', trigger: 'change'},
                        {validator: validateName, trigger: 'change'}
                    ]
                }
            }
        },
        methods: {
            handleChangeArea:function (val) {
                var len = val.length;
                this.saveForm.parent.id = val[len-1] || '';
                if(len == 0){
                    this.saveForm.type = '1';
                    return false;
                }
                if(len + 2 > 4){
                    this.saveForm.type = '';
                    return false;
                }
                this.saveForm.type = (len + 2).toString();
            },
            getTreeData:function () {
                var self = this;
                self.$axios.get('/sys/area/listpage').then(function (response) {
                    self.treeData = response.data.data;
                })
            },
            save:function (formName) {
                var self = this;
                self.$refs[formName].validate(function (valid) {
                    if (valid) {
                    	self.saveAjax();
                    }
                })
            },
			saveAjax:function () {
                var self = this;
                this.formDisabled = true;
                this.$axios({
                    method:'POST',
                    url:'/sys/area/save',
                    data:self.saveForm
                }).then(function (response) {
                    var data = response.data;
                    if(data.status == '1'){
                        window.location.href = self.frontOrAdmin + '/sys/area/';
                    }
                    self.formDisabled = false;
                    self.$message({
                        message: data.status == '1' ? '保存成功' : data.msg || '保存失败',
                        type: data.status == '1' ? 'success' : 'error'
                    })
                }).catch(function (error) {
                    self.formDisabled = false;
                    self.$message({
                        message: self.xhrErrorMsg,
                        type:'error'
                    })
                });
			}
        },
        created: function () {
            this.getTreeData();
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