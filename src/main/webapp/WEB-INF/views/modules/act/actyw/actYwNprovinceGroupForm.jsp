<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar :second-name="secondName"></edit-bar>
    </div>
    <el-form :model="actYwGroupForm" :disabled="disabled" auto-complete="off" autocomplete="off" ref="actYwGroupForm"
             :rules="actYwGroupRules" size="mini" label-width="120px" style="width: 520px;">
        <el-form-item prop="name" label="流程名称：">
            <el-input name="name" v-model="actYwGroupForm.name"></el-input>
        </el-form-item>
        <template v-if="actYwGroupForm.status !== '1'">
            <el-form-item prop="theme" label="表单组：">
                <el-select v-model="actYwGroupForm.theme" placeholder="请选择" clearable filterable @change="handleChangeTheme">
                    <el-option v-for="item in formThemes" :key="item.idx" :value="item.idx" :label="item.name"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="flowType" label="流程类型：">
                <!-- <el-select v-model="actYwGroupForm.flowType" placeholder="请选择" clearable filterable>
                    <el-option v-for="item in flowTypes" :key="item.key" :value="item.key" :label="item.name"></el-option>
                </el-select> -->
                {{actYwGroupForm.flowType | selectedFilter(flowTypeEntries)}}
            </el-form-item>
        </template>
        <el-form-item prop="remarks" label="备注：">
            <el-input name="remarks" type="textarea" :rows="4" v-model="actYwGroupForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click.stop.prevent="validateActYwGroupForm">保存</el-button>
            <el-button @click.stop.prevent="goToBack">返回</el-button>
        </el-form-item>
    </el-form>
</div>

</body>


<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var actYwGroup = JSON.parse(JSON.stringify(${fns:toJson(actYwGroup)}));
            var formThemes = JSON.parse(JSON.stringify(${fns: toJson(formThemes)}));
            return {
                actYwGroupForm: {
                    id: actYwGroup.id,
                    name: actYwGroup.name,
                    theme: actYwGroup.theme,
                    flowType: actYwGroup.flowType,
                    remarks: actYwGroup.remarks,
                    temp: actYwGroup.temp,
                    status: actYwGroup.status
                },
                actYwGroupRules: {
                    name: [
                        {required: true, message: '请输入流程名称', trigger: 'blur'},
                        {max: 50, message: '请输入不大于50位字符', trigger: 'blur'}
                    ],
                    remarks: [
                        {max: 200, message: '请输入不大于200位字符', trigger: 'blur'}
                    ],
                    theme: [
                        {required: true, message: '请选择表单组', trigger: 'change'},
                    ],
                    flowType: [
                        {required: true, message: '流程类型', trigger: 'change'},
                    ]
                },
                formThemes: formThemes,
                flowTypes: [],
                disabled: false
            }
        },
        computed: {
            secondName: function () {
                return this.actYwGroupForm.id ? '修改' : '添加'
            },
            flowTypeEntries: function(){
            	return this.getEntries(this.flowTypes, {
            		label: 'name',
            		value: 'key'
            	})
            }
        },
        methods: {

            handleChangeTheme: function (value) {
				var theme = this.getThemeById(value);
				if(theme){
					this.actYwGroupForm.flowType = theme.ftype;
				}

                if(!this.actYwGroupForm.id){
                    return;
                }
                this.$alert("确认修改表单组属性吗？若修改，请更新设计页的关联表单，否则流程发布后无法正常审核","提示", {
                    type: 'warning'
                })
            },

            getThemeById: function(id){
            	for(var i = 0; i <this.formThemes.length; i++){
            		if(this.formThemes[i].idx === id){
            			return this.formThemes[i]
            		}
            	}
            	return false;
            },

            //获取流程类型
            getFlowTypes: function () {
                var self = this;
                this.$axios.get('/actyw/gnode/ajaxFlowTypes?isAll=true').then(function (response) {
                    self.flowTypes = JSON.parse(response.data.datas);
                })
            },

            validateActYwGroupForm: function () {
              var self = this;
                this.$refs.actYwGroupForm.validate(function (valid) {
                    if(valid){
                        self.submitActYwGroupForm();
                    }
                })
            },

            submitActYwGroupForm: function () {
                var self = this;
                this.disabled = true;
                this.$axios({
                    method: 'GET',
                    url: '/actyw/actYwGroup/ajaxSave',
                    params: this.actYwGroupForm
                }).then(function (response) {
                    var data = response.data;
                    if(data.status){
                        self.$msgbox({
                            type: 'success',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            showConfirmButton: self.actYwGroupForm.status != '1',
                            showCancelButton: true,
                            confirmButtonText: '设计流程图',
                            cancelButtonText: '返回列表',
                            showClose: false,
                            message: data.msg || '自定义流程创建成功'
                        }).then(function () {
                            location.href = '${ctx}/actyw/actYwGnode/designNew?group.id=' + data.datas + '&groupId=' + data.datas;
                        }).catch(function () {
                            location.href = ' ${ctx}/actyw/actYwGroup/list'
                        })
                    }else {
                        self.$message.error(data.msg);
                    }
                    self.disabled = false;
                }).catch(function (error) {
                    self.disabled = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            goToBack: function () {
                history.go(-1)
            }
        },
        created: function () {
            this.getFlowTypes();
        }
    })

</script>

</html>