<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar :second-name="officeForm.id ? '修改': '添加'"></edit-bar>
    </div>
    <el-form :model="officeForm" ref="officeForm" :rules="officeRules" :disabled="officeFormDisabled"
             :action="ctx + '/sys/office/save'" method="post" size="mini" label-width="120px"
             style="width: 400px;">
        <input type="hidden" name="id" :value="officeForm.id">
        <input type="hidden" name="sort" :value="officeForm.sort">
        <input type="hidden" name="type" :value="officeForm.type">
        <el-form-item v-if="officeForm.parentId !== '0'" prop="officeIds" label="上级机构：">
            <input type="hidden" name="parent.id" :value="officeForm.parentId">
            <el-cascader
                    style="width: 100%"
                    ref="cascader"
                    filterable
                    :options="officeTree"
                    change-on-select
                    @change="handleChangeParentIds"
                    v-model="officeForm.officeIds"
                    :props="{label: 'name',value: 'id',children: 'children'}">
            </el-cascader>
        </el-form-item>
        <el-form-item prop="name" label="机构名称：">
            <el-input name="name" v-model="officeForm.name"></el-input>
        </el-form-item>
        <el-form-item prop="code" label="机构编码：">
            <el-input name="code" v-model="officeForm.code"></el-input>
        </el-form-item>
        <template v-if="officeForm.parentId === '0'">
            <el-form-item prop="cityIds" label="所属区域：">
                <input type="hidden" name="cityCode" :value="officeForm.cityCode">
                <el-cascader
                        style="width: 100%"
                        ref="cascaderCity"
                        filterable
                        :options="cityTree"
                        change-on-select
                        v-model="officeForm.cityIds"
                        @change="handleChangeCityIds"
                        :props="{label: 'name',value: 'id',children: 'children'}"
                >
                </el-cascader>
            </el-form-item>
            <el-form-item prop="schoolCode" label="高校代码：">
                <el-input name="schoolCode" v-model="officeForm.schoolCode"></el-input>
            </el-form-item>
        </template>
        <el-form-item>
            <el-button type="primary" @click.stop.prevent="validateOfficeForm">保存</el-button>
            <el-button @click.stop.prevent="goToBack">返回</el-button>
        </el-form-item>
    </el-form>
</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var officeFormData = JSON.parse('${fns: toJson(office)}');
            var self = this;

            var validateOfficeName = function (rule, value, callback) {
                if (value) {
                    if (/[@#\$%\^&\*\s]+/g.test(value)) {
                        return callback(new Error('请不要输入特殊符号'))
                    } else if (value.length > 64) {
                        return callback(new Error('请输入不大于64位字符'))
                    } else {
                        return self.$axios.get('/sys/office/checkOfficeName', {
                            params: {
                                name: value,
                                id: self.officeForm.id,
                                'parent.id': self.officeForm.parentId
                            }
                        }).then(function (response) {
                            if (!response.data) return callback(new Error('机构名称已存在'));
                            return callback()
                        }).catch(function (error) {
                            return callback(new Error(self.xhrErrorMsg));
                        })
                    }
                }
                return callback();
            };

            var validateOfficeCode = function (rule, value, callback) {
                if (value) {
                    if (!(/^[A-Za-z0-9]+$/.test(value))) {
                        return callback(new Error('请输入字母或者数字'))
                    } else if (value.length > 64) {
                        return callback(new Error('请输入不大于64位字符'))
                    }
                    return callback();
                }
                return callback();
            };

            return {
                officeForm: {
                    id: '',
                    officeIds: [],
                    cityIds: [],
                    parentId: '',
                    name: '',
                    code: '',
                    cityCode: '',
                    schoolCode: '',
                    sort: '',
                    type: ''
                },
                officeFormData: officeFormData,
                officeList: [],
                officeTree: [],
                officeEntity: {},
                cities: [],
                officeRules: {
                    officeIds: [{required: true, message: '请选择上级栏目', trigger: 'change'}],
                    name: [
                        {required: true, message: '请输入机构名称', trigger: 'blur'},
                        {validator: validateOfficeName, trigger: 'blur'}
                    ],
                    cityIds: [{required: true, message: '请选择所属区域', trigger: 'change'}],
                    code: [{validator: validateOfficeCode, trigger: 'blur'}],
                    schoolCode: [{validator: validateOfficeCode, trigger: 'blur'}]
                },
                officeFormDisabled: false,
                cityTree: []
            }
        },
        methods: {
            handleChangeParentIds: function (value) {
                if(value && value.length > 0){
                    this.officeForm.parentId = value[value.length - 1];
                }
            },

            goToBack: function () {
                location.href = this.ctx + '/sys/office'
            },

            validateOfficeForm: function () {
                var self = this;
                this.$refs.officeForm.validate(function (valid) {
                    if (valid) {
                        self.$refs.officeForm.$el.submit();
                        self.officeFormDisabled = true;
                    }
                })
            },
            handleChangeCityIds: function () {
                var cityIds = this.officeForm.cityIds;
                this.officeForm.cityCode = cityIds && cityIds.length > 0 ? cityIds[cityIds.length - 1] : '';
            },

            getCities: function () {
                var self = this;
                this.$axios.get('/sys/area/listpage').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.cityTree = data.data || [];
                    }
                })
            },


            getOfficeList: function () {
                var self = this;
                this.$axios.get('/sys/office/getOfficeList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var officeList = data.data || [];
                        officeList = officeList.filter(function (item) {
                            return item.parentIds.split(',').length <= 3;
                        });
                        var treeEntity = self.listToTreeEntity(officeList);
                        self.officeTree = JSON.parse(JSON.stringify(treeEntity.tree));
                        self.officeEntity = treeEntity.entity;
                        self.officeList = officeList;
                    }
                })
            },

            setCityIds: function (data) {
                var cityIds = [];
                if (data.area && data.area.parentIds) {
                    cityIds = data.area.parentIds.replace(/\,$/, '');
                    cityIds = cityIds.split(',');
                    if (cityIds.indexOf('0') > -1) {
                        cityIds = cityIds.slice(1);
                    }
                    cityIds.push(data.area.id)
                }
                data.cityIds = cityIds;
            },

            setOfficeParentIds: function (data) {
                var parentIds = data.parentIds.replace(/\,$/, '');
                parentIds = parentIds.split(',');
                if (parentIds.indexOf('0') > -1) {
                    parentIds = parentIds.slice(1);
                }
                data.officeIds = parentIds;
            }
        },
        mounted: function () {
            this.setCityIds(this.officeFormData);
            this.setOfficeParentIds(this.officeFormData);
            this.assignFormData(this.officeForm, this.officeFormData);
            if (this.officeForm.parentId === '0') {
                this.getCities();
            }else {
                this.getOfficeList();
            }
        }
    })

</script>
</body>
</html>