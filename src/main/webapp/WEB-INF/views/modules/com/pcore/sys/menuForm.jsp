<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar :second-name="menuForm.id ? '修改': '添加'"></edit-bar>
    </div>
    <el-form :model="menuForm" ref="menuForm" size="mini" label-width="120px" auto-complete="off"
             :disabled="menuFormDisabled" style="width: 540px;">
        <el-form-item prop="tmpParentIds" label="上级菜单：" :rules="parentIdsRules">
            <el-cascader
                    placeholder="请输入名称"
                    change-on-select
                    :options="menuTree"
                    :props="cascaderProps"
                    style="width: 100%;"
                    v-model="menuForm.tmpParentIds"
                    filterable
            ></el-cascader>
        </el-form-item>
        <el-form-item prop="name" :rules="nameRules" label="名称：">
            <el-input name="name" v-model="menuForm.name" maxlength="24" placeholder="限24个字符"></el-input>
        </el-form-item>
        <el-form-item v-if="['1','10'].indexOf(loginCurUser.auth) > -1" prop="href" label="链接：">
            <el-col :span="11">
                <el-input maxlength="2000" placeholder="限2000个字符" name="href" v-model="menuForm.href"></el-input>
            </el-col>
            <el-col :span="13">
                <span class="el-form-item-expository">点击菜单跳转的页面</span>
            </el-col>
        </el-form-item>
        <el-form-item prop="sort" label="排序：">
            <el-input-number name="sort" :step="30" :min="0" v-model="menuForm.sort"></el-input-number>
        </el-form-item>
        <el-form-item prop="isShow" label="显示：">
            <el-switch name="isShow" v-model="menuForm.isShow" active-value="1" inactive-value="0"></el-switch>
        </el-form-item>
        <el-form-item prop="icon" label="图标：" :rules="imgUrlRules">
            <el-input :prefix-icon="prefixIcon" name="icon" disabled v-model="menuForm.icon">
                <el-button slot="append" @click.stop="dialogVisibleIcon = true">选择图标</el-button>
            </el-input>
        </el-form-item>
        <el-form-item prop="imgUrl" label="菜单图片：" :rules="imgUrlRules" class="common-upload-one-img">
            <div class="upload-box-border" style="width: 110px; height: 110px;">
                <el-upload
                        class="avatar-uploader"
                        action="${ctx}/ftp/ueditorUpload/uploadTemp?folder=menu"
                        :show-file-list="false"
                        name="upfile"
                        :on-success="imgUrlSuccess"
                        :on-error="imgUrlError"
                        accept="image/jpeg,image/png">
                    <img v-if="menuForm.imgUrl" :src="menuForm.imgUrl | ftpHttpFilter(ftpHttp)">
                    <i v-if="!menuForm.imgUrl"
                       class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
            </div>
            <div class="img-tip">
                仅支持上传jpg/png文件，建议大小：220*220（像素）
            </div>
        </el-form-item>
        <el-form-item prop="remarks" label="备注：">
            <el-input name="remarks" :rows="3" type="textarea" maxlength="200" placeholder="限200个字符"
                      v-model="menuForm.remarks" show-word-limit></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click.stop.prevent="saveMenu">保存</el-button>
            <el-button @click.stop.prevent="goToBack">返回</el-button>
        </el-form-item>
    </el-form>
    <el-dialog
            title="图标"
            :visible.sync="dialogVisibleIcon"
            width="520px"
            :before-close="handleCloseIcon">
        <div class="menu-icon_list-content">
            <ul class="icon_lists dib-box">
                <li v-for="item in iconList" :key="item.className" class="dib"
                    :class="{active: menuForm.icon === item.className}">
                    <span class="icon iconfont" :class="item.className" @click.stop="handleChangeIcon(item)"></span>
                    <div class="name">
                        {{item.name}}
                    </div>
                </li>
            </ul>
        </div>
    </el-dialog>
</div>


<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var menu = JSON.parse(JSON.stringify(${fns: toJson(menu)}));
            var validateName = function (rules, value, callback) {
                if (value) {
                    if (/[@#\$%\^&\*\s]+/g.test(value)) {
                        return callback(new Error("请不要输入特殊符号"));
                    }
                    return callback();
                }
                return callback();
            };
            return {
                menuData: menu,
                menuTree: [],
                menuForm: {
                    id: '',
                    parent: {
                        id: ''
                    },
                    parentId: '',
                    tmpParentIds: [],
                    name: '',
                    href: '',
                    target: '_self',
                    sort: '',
                    isShow: '',
                    imgUrl: '',
                    icon: '',
                    remarks: ''
                },

                cascaderProps: {
                    label: 'name',
                    value: 'id',
                    children: 'children'
                },
                menuProps: {
                    id: 'id',
                    parentKey: 'parentId'
                },
                menuFormDisabled: false,
                nameRules: [{required: true, message: '请输入菜单名称', trigger: 'blur'}, {
                    validator: validateName,
                    trigger: 'blur'
                }],
                parentIdsRules: [{required: true, message: '请输入菜单名称', trigger: 'change'}],
                dialogVisibleIcon: false,
                iconList: [
                    {name: '博士帽', className: 'icon-boshimao', codeName: '.icon-boshimao'},
                    {name: '奖杯', className: 'icon-icon--', codeName: '.icon-icon--'},
                    {name: '内容管理', className: 'icon-neirongguanli', codeName: '.icon-neirongguanli'},
                    {name: '饼图', className: 'icon-bingtu1', codeName: '.icon-bingtu1'},
                    {name: '数据管理', className: 'icon-shujuguanli', codeName: '.icon-shujuguanli'},
                    {name: '天平', className: 'icon-huabankaobei-', codeName: '.icon-huabankaobei-'},
                    {name: '奖标-白色', className: 'icon-jiangbiao-baise', codeName: '.icon-jiangbiao-baise'},
                    {name: '网站', className: 'icon-wangzhan', codeName: '.icon-wangzhan'},
                    {name: '基础', className: 'icon-jichu', codeName: '.icon-jichu'},
                    {name: '互联网+', className: 'icon-hulianwang', codeName: '.icon-hulianwang'},
                    {name: '训练计划', className: 'icon-xunlianjihua', codeName: '.icon-xunlianjihua'},
                    {name: '大数据', className: 'icon-dashuju', codeName: '.icon-dashuju'},
                    {name: '导师库', className: 'icon-daoshiku', codeName: '.icon-daoshiku'}
                ]
            }
        },
        computed: {
            'parentId': function () {
                var tmpParentIds = this.menuForm.tmpParentIds;
                if (tmpParentIds.length > 0) {
                    return tmpParentIds[tmpParentIds - 1]
                }
            },
            prefixIcon: function () {
                return 'iconfont ' + this.menuForm.icon
            },
            imgUrlRules: function () {
                var tmpParentIds = this.menuForm.tmpParentIds;
                return [{required: tmpParentIds.length === 1, message: '请上传', trigger: 'change'}]
            }
        },
        methods: {
            handleCloseIcon: function () {
                this.dialogVisibleIcon = false;
            },

            handleChangeIcon: function (item) {
                this.menuForm.icon = item.className;
                this.dialogVisibleIcon = false;
            },

            saveMenu: function () {
                var self = this;
                this.$refs.menuForm.validate(function (valid) {
                    if (valid) {
                        self.postMenu();
                    }
                })
            },

            getUserRoleAuth: function () {
                var self = this;
                this.$axios.post('/sys/role/getUserRoleAuth', {
                    id: this.loginCurUser.id
                }).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data;
                        if (data.auth) {
                            Vue.set(self.loginCurUser, 'auth', data.auth)
                        }
                    }
                })
            },

            goToBack: function () {
                location.href = this.ctx + '/sys/menu/'
            },

            imgUrlSuccess: function (response) {
                if (response.state === 'SUCCESS') {
                    this.menuForm.imgUrl = response.ftpUrl;
                    return false;
                }
                this.$message({
                    type: 'error',
                    message: '上传失败'
                })
            },

            imgUrlError: function (error) {
                this.$message({
                    type: 'error',
                    message: this.xhrErrorMsg
                })
            },

            postMenu: function () {
                var self = this;
                self.menuFormDisabled = true;
                var menuForm = this.dealMenuFormParam();
                this.$axios.post('/sys/menu/saveMenu', menuForm).then(function (response) {
                    var data = response.data;
                    if (data.status == 1) {
                        self.$msgbox({
                            message: "保存成功",
                            title: '提示',
                            confirmButtonText: '确定',
                            showClose: false,
                            closeOnClickModal: false,
                            type: 'success'
                        }).then(function () {
                            self.goToBack();
                        }).catch(function () {

                        })
                    } else {
                        self.$alert(data.msg, '提示', {type: 'error'})
                    }
                    self.menuFormDisabled = false;
                }).catch(function (error) {
                    self.menuFormDisabled = false;
                })
            },

            getMenuList: function () {
                var self = this;
                this.$axios.get('/sys/menu/getMenuList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || [];
                        self.menuTree = self.menuListToTree(data).tree;
                    }
                })
            },

            menuListToTree: function (list) {
                var tree = [];
                var listToTreeEntity = this.listToTreeEntity(list);
                var parentIds = list[0].parentIds;
                tree = listToTreeEntity.tree || [];
                tree = tree.filter(function (menu) {
                    return menu.parentId === parentIds[0]
                });
                return {
                    entity: listToTreeEntity.entity,
                    tree: tree
                }
            },


            dealMenuFormParam: function () {
                var menuForm = JSON.parse(JSON.stringify(this.menuForm));
                var parentId = menuForm.tmpParentIds[menuForm.tmpParentIds.length - 1];
                menuForm.parent.id = parentId;
                menuForm.parentId = parentId;
                delete  menuForm.tmpParentIds;
                return menuForm;
            },

            setParentFormId: function () {
                var menuData = this.menuData;
                var parentIds = menuData.parentIds;
                if (parentIds) {
                    parentIds = menuData.parentIds.split(',');
                    parentIds = parentIds.filter(function (item) {
                        return !!item
                    });
                    parentIds = parentIds.slice(1);
                    menuData.tmpParentIds = parentIds;
                }
                menuData.parent = {
                    id: menuData.parentId
                }
            },
        },
        mounted: function () {
            this.getMenuList();
            this.getUserRoleAuth();
            this.setParentFormId();
            this.assignFormData(this.menuForm, this.menuData);
            this.$refs.menuForm.clearValidate();
        }
    })

</script>
</body>
</html>