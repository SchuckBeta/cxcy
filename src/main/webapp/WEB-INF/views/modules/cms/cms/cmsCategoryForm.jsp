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
        <edit-bar :second-name="categoryForm.id ? '修改' : '添加'"></edit-bar>
    </div>
    <el-form :model="categoryForm" :rules="categoryRules" :disabled="categoryFormDisabled" ref="categoryForm"
             size="mini" label-width="120px"
             style="width: 520px;">
        <input type="hidden" name="id" :value="categoryForm.id">
        <el-form-item prop="localParentId" label="上级栏目：">
            <input type="hidden" name="parentId" :value="categoryForm.parentId">
            <el-cascader :options="menuTree" @change="handleChangeTopCategory" change-on-select
                         v-model="categoryForm.localParentId"
                         :props="{label: 'name', children: 'children', value: 'id'}" class="w300"></el-cascader>
        </el-form-item>
        <el-form-item prop="module" label="栏目模型：">
            <input type="hidden" name="module" :value="categoryForm.module">
            <el-select v-model="categoryForm.module" placeholder="请选择" @change="handleChangeModule">
                <el-option
                        v-for="item in cmsModuleList"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
            </el-select>
            <span class="el-form-item-expository">栏目下文章的类型</span>
        </el-form-item>
        <el-form-item prop="publishCategory" label="栏目标识：">
            <el-select v-model="categoryForm.publishCategory" clearable placeholder="请选择栏目标识">
                <el-option
                        v-for="item in publishCategories"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="name" label="栏目名称：">
            <el-col :span="18">
                <el-input name="name" placeholder="请输入1-15位之间字符" v-model="categoryForm.name"></el-input>
            </el-col>
        </el-form-item>
        <el-form-item label="访问路径：">
            <el-col :span="18">
                <el-form-item prop="href" label-width="0" style="margin-bottom: 0">
                    <el-input name="href" v-model="categoryForm.href">
                        <template v-if="isOutLink" slot="prepend">http://</template>
                    </el-input>
                </el-form-item>
            </el-col>
            <el-col :span="6">
                <el-checkbox style="margin-left: 8px;" v-model="isOutLink">添加外部链接
                </el-checkbox>
            </el-col>
        </el-form-item>
        <el-form-item prop="isShow" label="显示：">
            <el-switch v-model="categoryForm.isShow" name="isShow" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
        <el-form-item prop="isNewtab" label="是否打开新窗口：">
            <el-checkbox v-model="categoryForm.isNewtab" name="isNewtab" :true-label="1" :false-label="0">是
            </el-checkbox>
        </el-form-item>
        <el-form-item prop="sort" label="排序：">
            <el-input-number v-model="categoryForm.sort" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item v-if="categoryForm.module != '0000000278'" prop="showModes" label="展现方式：">
            <el-radio-group v-model="categoryForm.showModes">
                <el-radio-button name="showModes" v-for="cmsShowMode in cmsShowModeList" :key="cmsShowMode.value"
                                 :label="cmsShowMode.value">{{cmsShowMode.label}}
                </el-radio-button>
            </el-radio-group>
            <div v-show="categoryForm.showModes != '0'" class="show-modes-example-imgs"
                 style="width: 400px; height: 257px;">
                <img v-show="categoryForm.showModes == '1'" src="${ctxImg}/category-th-02.jpg">
                <img v-show="categoryForm.showModes == '2'" src="${ctxImg}/category-th-01.jpg">
            </div>
        </el-form-item>
        <el-form-item v-if="categoryForm.module != '0000000278'" prop="contenttype" label="内容类型：">
            <el-radio-group v-model="categoryForm.contenttype">
                <el-radio-button name="contenttype" v-for="item in contentTypes" :key="item.value"
                                 :label="item.value">{{item.label}}
                </el-radio-button>
            </el-radio-group>
            <div v-show="!!categoryForm.contenttype" class="show-modes-example-imgs"
                 style="width: 400px; height: 257px;">
                <img v-show="categoryForm.contenttype == '0000000272'" src="${ctxImg}/category-img-list.jpg">
                <img v-show="categoryForm.contenttype == '0000000273'" src="${ctxImg}/category-th-02.jpg">
            </div>
        </el-form-item>
        <el-form-item prop="allowComment" label="评论：">
            <el-switch v-model="categoryForm.allowComment" name="allowComment" active-value="1"
                       inactive-value="0"></el-switch>
        </el-form-item>
        <el-form-item prop="isAudit" label="审核：">
            <el-switch v-model="categoryForm.isAudit" name="isAudit" active-value="1" inactive-value="0"></el-switch>
        </el-form-item>
        <el-form-item prop="isContentstatic" label="内容静态化：">
            <el-checkbox v-model="categoryForm.isContentstatic" name="isContentstatic" :true-label="1" :false-label="0">
                开启
            </el-checkbox>
        </el-form-item>
        <el-form-item prop="description" label="描述：">
            <el-input type="textarea" v-model="categoryForm.description" name="description" :rows="3"
                      placeholder="最大200个字"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click.stop.prevent="submitCategoryForm">保存</el-button>
            <el-button @click.stop.prevent="goToBack">返回</el-button>
        </el-form-item>
    </el-form>
</div>


<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var cmsCategory = JSON.parse(JSON.stringify(${fns: toJson(category)})) || [];

            return {
                cmsCategory: cmsCategory,
                categoryForm: {
                    id: '',
                    localParentId: [],
                    parentId: cmsCategory.parentId || '1',
                    parentIds: '',
                    module: '',
                    name: '',
                    href: '',
                    isShow: '',
                    isNewtab: '',
                    sort: '',
                    showModes: '0',
                    contenttype: '',
                    allowComment: '',
                    isAudit: '',
                    isContentstatic: '',
                    description: '',
                    publishCategory: ''
                },
                categoryRules: {
                    localParentId: [{required: true, message: '请选择上级栏目', trigger: 'change'}],
                    module: [{required: true, message: '请选择栏目模型', trigger: 'change'}],
                    name: [
                        {required: true, message: '请输入栏目名称', trigger: 'blur'},
                        {max: 15, message: '请输入1-15位之间字符', trigger: 'blur'}
                    ],
                    showModes: [{required: true, message: '请选择展现方式', trigger: 'change'}],
                    contenttype: [{required: true, message: '请选择内容类型', trigger: 'change'}],
                    description: [{max: 200, message: '请不要超过200个字符描述', trigger: 'blur'}],
                    href: [
                        {max: 255, message: '请不要超过255个字符链接', trigger: 'blur'}
                    ]
                },
                categoryFormDisabled: true,
                menuList: [],
                menuTree: [],
                cmsModuleList: [],
                cmsShowModeList: [],
                publishCategories: [],
                contentTypes: [],
                isOutLink: false
            }
        },
        computed: {
            contentTypeEntries: function () {
                return this.getEntries(this.contentTypes)
            },
            contentTypeName: function () {
                if (!this.categoryForm.contenttype) {
                    return ''
                }
                return this.contentTypeEntries[this.categoryForm.contenttype];
            }
        },
        methods: {

            handleChangeModule: function (value) {
                if (value == '0000000278') {
                    this.categoryForm.showModes = '0';
                    this.categoryForm.contenttype = '';
                }
            },


            getCategories: function () {
                var self = this;
                this.$axios.get('/cms/category/cmsCategoryList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var menuList = data.data.list || [];
                        var nMenuList = [];
                        menuList.unshift({
                            id: '1',
                            name: '顶层栏目',
                            parentId: '0'
                        });
                        var treeEntity = self.listToTreeEntity(menuList);
                        self.menuEntity = treeEntity.entity;
                        menuList.forEach(function (item) {
                            item.parentIds = self.getParentIds(item, self.menuEntity);
                        });
                        nMenuList = menuList.filter(function (item) {
                          return item.parentIds.split(',').length < 5;
                        });
                        var tree = self.listToTreeEntity(nMenuList);
                        self.menuTree = tree.tree;
                        self.categoryForm.localParentId = self.getCategoryLocalParentId();
                    }
                    self.categoryFormDisabled = false;
                })
            },

            getParentIds: function (item, menuEntity) {
                var parentId = item.id;
                var parentIds = [];
                var parent = menuEntity[parentId];
                while (!!parent){
                    parentIds.unshift(parent.parentId);
                    parentId = parent.parentId;
                    parent = menuEntity[parentId];
                    if(!parent){
                        break;
                    }
                }
                return parentIds.join(',') + ','
            },


            handleChangeTopCategory: function (value) {
                if (value && value.length > 0) {
                    this.categoryForm.parentId = value[value.length - 1]
                } else {
                    this.categoryForm.parentId = '0'
                }
            },


            submitCategoryForm: function () {
                var self = this;
                this.$refs.categoryForm.validate(function (valid) {
                    if (valid) {
                        self.postCategoryForm();
                    }
                })
            },

            postCategoryForm: function () {
                var self = this;
                var categoryForm = JSON.parse(JSON.stringify(this.categoryForm));
                var parentObj = this.menuEntity[categoryForm.parentId];
                categoryForm.href = this.isOutLink ? 'http://' + categoryForm.href : categoryForm.href;
                categoryForm.parentIds = parentObj.parentIds + parentObj.id + ',';
                delete categoryForm.localParentId;
                this.categoryFormDisabled = true;
                this.$axios({
                    method: 'POST',
                    url: '/cms/category/editSaveCmsCategory',
                    params: categoryForm
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == 1) {
                        data = data.data;
                        if (data) {
                            self.categoryForm.id = data.id;
                        }
                        self.$msgbox({
                            type: 'success',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            confirmButtonText: '确定',
                            showClose: false,
                            message: '保存成功'
                        }).then(function () {
                            location.href = self.ctx + '/cms/category'
                        }).catch(function () {

                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        });
                        self.categoryFormDisabled = false;
                    }

                }).catch(function (error) {
                    self.categoryFormDisabled = false;
                })
            },


            getCategoryLocalParentId: function () {
                var parentId = this.categoryForm.parentId;
                var result = [];
                if (!parentId) {
                    return result;
                }
                while (parentId) {
                    var parent = this.menuEntity[parentId];
                    if (!parent) {
                        break;
                    }
                    result.unshift(parentId);
                    parentId = parent.parentId;

                }
                return result
            },

            goToBack: function () {
                window.history.go(-1);
            },

            getDictLists: function () {
                var dicts = {
                    '0000000274': 'cmsModuleList',
                    'cms_show_modes': 'cmsShowModeList',
                    '0000000271': 'contentTypes',
                    '0000000282': 'publishCategories',
                };
                this.getBatchDictList(dicts)

            },
        },
        mounted: function () {
            this.assignFormData(this.categoryForm, this.cmsCategory);
            this.getCategories();
            this.getDictLists();
        }
    })

</script>

</body>
</html>