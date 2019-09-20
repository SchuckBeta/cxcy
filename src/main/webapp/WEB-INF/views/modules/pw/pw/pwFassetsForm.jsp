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
        <shiro:hasPermission name="pw:pwFassets:edit">
            <edit-bar :second-name="saveForm.id ? '修改': '添加'" href="/pw/pwFassets/list"></edit-bar>
        </shiro:hasPermission>
        <shiro:lacksPermission name="pw:pwFassets:edit">
            <edit-bar second-name="查看" href="/pw/pwFassets/list"></edit-bar>
        </shiro:lacksPermission>
    </div>


    <el-form size="mini" :model="saveForm" :rules="saveFormRules" ref="saveForm" :disabled="formDisabled"
             label-width="120px">

        <el-form-item prop="pwCategory.parent.id" label="资产类别：">
            <el-select name="parentId" size="mini" v-model="saveForm.pwCategory.parent.id" @change="handleChangeAssetsTypes"
                       placeholder="请选择资产类别" clearable class="w300">
                <el-option
                        v-for="item in assetsTypes"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                </el-option>
            </el-select>
            <a href="javascript:void(0)" @click.stop.prevent="addAssetsTypes">找不到？添加</a>
        </el-form-item>

        <el-form-item prop="pwCategory.id" label="资产名称：">
            <el-select name="pwCategoryId" size="mini" v-model="saveForm.pwCategory.id" clearable placeholder="请选择资产名称" class="w300">
                <el-option
                        v-for="item in assetsNames"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                </el-option>
            </el-select>
            <a href="javascript:void(0)" @click.stop.prevent="addAssetsNames">找不到？添加</a>
        </el-form-item>

        <el-form-item prop="brand" label="品牌：">
            <el-input name="brand" v-model="saveForm.brand" class="w300"></el-input>
        </el-form-item>

        <el-form-item prop="specification" label="规格：">
            <el-input name="specification" v-model="saveForm.specification" class="w300"></el-input>
        </el-form-item>

        <el-form-item prop="prname" label="购买人：">
            <el-input name="prname" v-model="saveForm.prname" class="w300" maxlength="20"></el-input>
        </el-form-item>

        <el-form-item prop="phone" label="手机号码：">
            <el-input name="phone" v-model="saveForm.phone" class="w300"></el-input>
        </el-form-item>

        <el-form-item prop="time" label="购买时间：">
            <el-date-picker
                    v-model="saveForm.time"
                    type="date"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="选择日期">
            </el-date-picker>
        </el-form-item>

        <el-form-item prop="price" label="价格：">
            <el-input name="price" v-model="saveForm.price" class="w300">
                <template slot="append">元（人民币）</template>
            </el-input>
        </el-form-item>

        <el-form-item prop="remarks" label="备注：">
            <el-input type="textarea" name="remarks" :rows="5" v-model="saveForm.remarks" maxlength="200" style="width:500px;"></el-input>
        </el-form-item>


        <el-form-item>
            <shiro:hasPermission name="pw:pwFassets:edit">
                <el-button type="primary" :disabled="formDisabled"
                           @click.stop.prevent="save('saveForm')">保存
                </el-button>
            </shiro:hasPermission>
        </el-form-item>

    </el-form>

    <el-dialog title="添加资产类别" :visible.sync="assetVisible" width="400px" :before-close="handleCloseAsset"
               :close-on-click-modal="isCloseAsset">
        <el-form size="mini" :model="assetForm" :rules="assetFormRules" ref="assetForm" :disabled="assetFormDisabled"
                 label-width="120px">
            <el-form-item prop="name" label="资产类别：">
                <el-input v-model="assetForm.name" maxlength="30" style="width:200px;"></el-input>
            </el-form-item>
            <el-form-item prop="pwFassetsnoRule.prefix" label="前缀(编号规则)：">
                <el-input v-model="assetForm.pwFassetsnoRule.prefix" maxlength="3" style="width:200px;"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button size="mini" @click.stop.prevent="handleCloseAsset">取消</el-button>
          <el-button size="mini" type="primary" :disabled="assetFormDisabled" @click.stop.prevent="saveAsset('assetForm')">保存</el-button>
        </span>
    </el-dialog>



    <el-dialog title="添加资产名称" :visible.sync="assetNameVisible" width="600px" :before-close="handleCloseAssetName"
               :close-on-click-modal="isCloseAssetName">
        <el-form size="mini" :model="assetNameForm" :rules="assetNameFormRules" ref="assetNameForm" :disabled="assetNameFormDisabled"
                 label-width="120px">
            <el-form-item prop="parent.id" label="父类别：">
                <el-select name="parentId" size="mini" v-model="assetNameForm.parent.id" style="width:200px;"
                           placeholder="请选择父类别" clearable class="w300">
                    <el-option
                            v-for="item in assetsTypes"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="name" label="资产名称：">
                <el-input v-model="assetNameForm.name" maxlength="30" style="width:200px;"></el-input>
            </el-form-item>

            <el-form-item label="编号规则" class="item-bottom-border"></el-form-item>

            <el-form-item prop="pwFassetsnoRule.prefix" label="前缀：">
                <el-input v-model="assetNameForm.pwFassetsnoRule.prefix" maxlength="24" style="width:200px;"></el-input>
            </el-form-item>

            <el-form-item prop="pwFassetsnoRule.startNumber" label="开始编号：">
                <el-input name="pwFassetsnoRulStartNumber" v-model="assetNameForm.pwFassetsnoRule.startNumber" @change="handleChangeStartNumber" style="width:200px;"></el-input>
            </el-form-item>

            <el-form-item prop="pwFassetsnoRule.numberLen" label="编号位数：">
                <el-input name="pwFassetsnoRulNumberLen" v-model="assetNameForm.pwFassetsnoRule.numberLen" style="width:200px;"></el-input>
                <span style="color:#999;margin-left: 10px;">表示数字最小的位数，不足位数的前面补0</span>
            </el-form-item>

        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button size="mini" @click.stop.prevent="handleCloseAssetName">取消</el-button>
          <el-button size="mini" type="primary" :disabled="assetNameFormDisabled" @click.stop.prevent="saveAssetName('assetNameForm')">保存</el-button>
        </span>
    </el-dialog>


</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var pwFassets = JSON.parse(JSON.stringify(${fns:toJson(pwFassets)})) || [];
            var pwCategory = pwFassets.pwCategory || {};
            var assetsTypes = JSON.parse(JSON.stringify(${fns:toJson(fns:findChildrenCategorys(null))}));

            var nameReg = /['"\s“”‘’]+/;
            var positiveReg = /^[1-9][0-9]*$/;
            var mobileReg = /^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\d{8}$$/;
            var validateName = function (rule, value, callback) {
                if (value && nameReg.test(value)) {
                    callback(new Error('存在空格或者引号'))
                } else {
                    callback();
                }
            };
            var validatePhone = function (rule, value, callback) {
                if (value && !mobileReg.test(value)) {
                    callback(new Error('请输入有效的联系电话'));
                } else {
                    callback();
                }
            };
            var validatePrice = function (rule, value, callback) {
                var len = value.toString().length;
                if (!positiveReg.test(value)) {
                    callback(new Error('请输入正整数'));
                }else if(value && (len > 10)){
                    callback(new Error('价格最大10位数'))
                }else{
                    callback();
                }
            };

            return {
                assetsTypes:assetsTypes,
                assetsNames:[],
				saveForm:{
                    id: pwFassets.id || '',
                    pwCategory:{
                        parent:{
                            id:pwCategory.parentId
                        },
                        id:pwCategory.id
                    },
                    brand:pwFassets.brand || '',
                    specification:pwFassets.specification || '',
                    prname:pwFassets.prname || '',
                    phone:pwFassets.phone || '',
                    time:pwFassets.time || '',
                    price:pwFassets.price || '',
                    remarks:pwFassets.remarks || ''
                },
                formDisabled:false,
                message:'${message}',
                assetVisible:false,
                isCloseAsset:false,
                assetFormDisabled:false,
                assetForm:{
                    parent:{
                        id:'1'
                    },
                    name:'',
                    pwFassetsnoRule:{
                        prefix:''
                    }
                },
                assetNameVisible:false,
                isCloseAssetName:false,
                assetNameFormDisabled:false,
                assetNameForm:{
                    parent:{
                        id:''
                    },
                    name:'',
                    pwFassetsnoRule:{
                        prefix:'',
                        startNumber:'',
                        numberLen:''
                    }
                },
				saveFormRules:{
                    pwCategory:{
                        parent:{
                            id: [
                                {required: true, message: '请选择资产类别', trigger: 'change'}
                            ]
                        },
                        id: [
                            {required: true, message: '请选择资产名称', trigger: 'change'}
                        ]
                    },
                    prname: [
                        {validator: validateName, trigger: 'change'}
                    ],
                    phone: [
                        {validator: validatePhone, trigger: 'change'}
                    ],
                    price: [
                        {validator: validatePrice, trigger: 'change'}
                    ]
                },
                assetFormRules:{
                    name: [
                        {required: true, message: '请输入资产类别', trigger: 'change'},
                        {validator: validateName, trigger: 'change'}
                    ],
                    pwFassetsnoRule:{
                        prefix: [
                            {required: true, message: '请输入前缀', trigger: 'change'}
                        ]
                    }
                }
            }
        },
        computed:{
            numberLength: {
                get:function () {
                    return this.assetNameForm.pwFassetsnoRule.startNumber.length;
                }  
            },
            assetNameFormRules:{
                get:function () {
                    var self = this;
                    var nameReg = /['"\s“”‘’]+/;
                    var positiveReg = /^[1-9][0-9]*$/;
                    var validateName = function (rule, value, callback) {
                        if (value && nameReg.test(value)) {
                            callback(new Error('存在空格或者引号'))
                        } else {
                            callback();
                        }
                    };
                    var validatePrefix = function (rule, value, callback) {
                        var reg = /^[0-9a-zA-Z]*$/g;
                        if (!reg.test(value)) {
                            callback(new Error('只能输入数字或者字母'));
                        } else {
                            callback();
                        }
                    };
                    var validateStartNumber = function (rule, value, callback) {
                        var len = value.toString().length;
                        if (!positiveReg.test(value)) {
                            callback(new Error('请输入正整数'));
                        }else if((len > 3)){
                            callback(new Error('开始编号最大3位数'))
                        }else{
                            callback();
                        }
                    };
                    var validateNumberLen = function (rule, value, callback) {
                        var len = value.toString().length;
                        if (!positiveReg.test(value)) {
                            callback(new Error('请输入正整数'));
                        }else if((len > 3)){
                            callback(new Error('编号位数最大3位数'))
                        }else if(value < self.numberLength){
                            callback(new Error('编号位数必须大于开始编号的长度'));
                        }else{
                            callback();
                        }
                    };
                    return {
                        parent:{
                            id: [
                                {required: true, message: '请选择父类别', trigger: 'change'}
                            ]
                        },
                        name: [
                            {required: true, message: '请输入资产名称', trigger: 'change'},
                            {validator: validateName, trigger: 'change'}
                        ],
                        pwFassetsnoRule:{
                            prefix: [
                                {required: true, message: '请输入前缀', trigger: 'change'},
                                {validator: validatePrefix, trigger: 'change'}
                            ],
                            startNumber: [
                                {required: true, message: '请输入开始编号', trigger: 'change'},
                                {validator: validateStartNumber, trigger: 'change'}
                            ],
                            numberLen: [
                                {required: true, message: '请输入编号位数', trigger: 'change'},
                                {validator: validateNumberLen, trigger: 'change'}
                            ]
                        }
                    }
                }
            }
        },
        methods: {
            handleChangeStartNumber:function () {
                if(this.assetNameForm.pwFassetsnoRule.numberLen){
                    this.$refs.assetNameForm.validateField('pwFassetsnoRule.numberLen');
                }
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
                    url:'/pw/pwFassets/save',
                    data:self.saveForm
                }).then(function (response) {
                    var data = response.data;
                    if(data.status == '1'){
                        window.location.href = self.frontOrAdmin + '/pw/pwFassets/list';
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
			},
            handleChangeAssetsTypes:function (value) {
                if(!value){
                    this.saveForm.pwCategory.id = '';
                    this.assetsNames = [];
                    return false;
                }
                this.getChildrenCategory(value);
            },

            getChildrenCategory:function (id) {
                var self = this;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwCategory/childrenCategory',
                    params:{
                        categoryId:id
                    }
                }).then(function (response) {
                    var data = response.data;
                    if (data) {
                        if(!id){
                            self.assetsTypes = [];
                            self.assetsTypes = data || [];
                            return false;
                        }
                        self.assetsNames = [];
                        self.assetsNames = data || [];
                    }
                });
            },

            addAssetsTypes:function () {
                this.assetVisible = true;
            },
            handleCloseAsset:function () {
                this.assetVisible = false;
                this.$refs.assetForm.resetFields();
            },
            saveAsset:function (formName) {
                var self = this;
                self.$refs[formName].validate(function (valid) {
                    if (valid) {
                    	self.saveAssetAjax();
                    }
                })
            },
            saveAssetAjax:function () {
                var self = this;
                this.assetFormDisabled = true;
                this.$axios({
                    method:'POST',
                    url:'/pw/pwCategory/asySave',
                    data:self.assetForm
                }).then(function (response) {
                    var data = response.data;
                    if(data.success){
                        self.handleCloseAsset();
                        self.getChildrenCategory();
                        self.assetsNames = [];
                        self.saveForm.pwCategory.parent.id = data.id;
                    }
                    self.assetFormDisabled = false;
                    self.$message({
                        message: data.success ? '添加资产类别成功' : data.msg || '添加资产类别失败',
                        type: data.success ? 'success' : 'error'
                    })
                }).catch(function (error) {
                    self.assetFormDisabled = false;
                    self.$message({
                        message: self.xhrErrorMsg,
                        type:'error'
                    })
                });
            },

            addAssetsNames:function () {
                this.assetNameForm.parent.id = this.saveForm.pwCategory.parent.id;
                this.assetNameVisible = true;
            },
            handleCloseAssetName:function () {
                this.assetNameVisible = false;
                this.$refs.assetNameForm.resetFields();
            },
            saveAssetName:function (formName) {
                var self = this;
                self.$refs[formName].validate(function (valid) {
                    if (valid) {
                    	self.saveAssetNameAjax();
                    }
                })
            },
            saveAssetNameAjax:function () {
                var self = this;
                this.assetNameFormDisabled = true;
                this.$axios({
                    method:'POST',
                    url:'/pw/pwCategory/asySave',
                    data:self.assetNameForm
                }).then(function (response) {
                    var data = response.data;
                    if(data.success){
                        self.saveForm.pwCategory.parent.id = self.assetNameForm.parent.id;
                        self.getChildrenCategory(self.assetNameForm.parent.id);
                        self.saveForm.pwCategory.id = data.id;
                        self.handleCloseAssetName();
                    }
                    self.assetNameFormDisabled = false;
                    self.$message({
                        message: data.success ? '添加资产名称成功' : data.msg || '添加资产名称失败',
                        type: data.success ? 'success' : 'error'
                    })
                }).catch(function (error) {
                    self.assetNameFormDisabled = false;
                    self.$message({
                        message: self.xhrErrorMsg,
                        type:'error'
                    })
                });
            }
        },
        created: function () {
            if(this.saveForm.pwCategory.id){
                this.getChildrenCategory(this.saveForm.pwCategory.parent.id);
            }
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