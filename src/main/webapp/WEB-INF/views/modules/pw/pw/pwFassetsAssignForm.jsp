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
        <edit-bar second-name="固定资产分配" href="/pw/pwFassets/list"></edit-bar>
    </div>


    <el-form size="mini" :model="saveForm" :rules="saveFormRules" ref="saveForm" :disabled="formDisabled"
             label-width="120px">

        <el-form-item prop="campuses" label="校区：" v-if="hasCampus">
            <el-select name="campuses" size="mini" v-model="saveForm.campuses" @change="changeCampus"
                       placeholder="请选择校区" clearable class="w300">
                <el-option
                        v-for="item in campuses"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>

        <el-form-item prop="base" label="基地：" v-if="hasBase">
            <el-select name="base" size="mini" v-model="saveForm.base" @change="changeBase"
                       placeholder="请选择基地" clearable class="w300">
                <el-option
                        v-for="item in bases"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>

        <el-form-item prop="building" label="楼栋：">
            <el-select name="building" size="mini" v-model="saveForm.building" @change="changeBuilding"
                       placeholder="请选择楼栋" clearable class="w300">
                <el-option
                        v-for="item in buildings"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>

        <el-form-item prop="floor" label="楼层：">
            <el-select name="floor" size="mini" v-model="saveForm.floor" @change="changeFloor"
                       placeholder="请选择楼层" clearable class="w300">
                <el-option
                        v-for="item in floors"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>

        <el-form-item prop="roomId" label="房间：">
            <el-select name="roomId" size="mini" v-model="saveForm.roomId" @change="getEnterList"
                       placeholder="请选择房间" clearable class="w300">
                <el-option
                        v-for="item in rooms"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>

        <el-form-item prop="respName" label="使用人：">
            <el-input name="respName" v-model="saveForm.respName" class="w300" maxlength="20"></el-input>
        </el-form-item>

        <el-form-item prop="respMobile" label="手机号码：">
            <el-input name="respMobile" v-model="saveForm.respMobile" class="w300"></el-input>
        </el-form-item>

        <el-form-item label="资产列表：">
            <div class="table-container">
                <el-table size="mini" :data="tableList" class="table">

                    <el-table-column prop="name" align="center" label="编号">
                    </el-table-column>
                    <el-table-column prop="pwCategory.parentId" align="center" label="资产类别">
                        <template slot-scope="scope">
                            {{scope.row.pwCategory.parentId | selectedFilter(assetsTypesEntries)}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="pwCategory.name" align="center" label="资产名称">
                    </el-table-column>
                    <el-table-column prop="brand" align="center" label="品牌型号">
                    </el-table-column>
                </el-table>
            </div>
        </el-form-item>


        <el-form-item>
            <shiro:hasPermission name="pw:pwFassets:edit">
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
        data: function () {
            var tableList = JSON.parse(JSON.stringify(${fns:toJson(list)})) || [];
            var assetsTypes = JSON.parse(JSON.stringify(${fns:toJson(fns:findChildrenCategorys(null))}));
            var fassetsIds = tableList.map(function (item) {
                return item.id;
            });
            var nameReg = /['"\s“”‘’]+/;
            var mobileReg = /^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\d{8}$$/;
            var validateName = function (rule, value, callback) {
                if (nameReg.test(value)) {
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

            return {
                tableList:tableList,
                assetsTypes:assetsTypes,
                saveForm: {
                    fassetsIds: fassetsIds.join(','),
                    campuses: '',
                    base: '',
                    building: '',
                    floor: '${pwRoom.pwSpace.id}',
                    roomId: '${pwRoom.pwSpace.id}',
                    respName: '',
                    respMobile: ''
                },
                formDisabled: false,
                message: '${message}',

                tree: [],

                campuses: [],
                bases: [],
                buildings: [],
                floors: [],

                campusesClone: [],
                basesClone: [],
                buildingsClone: [],

                hasCampus: false,
                hasBase: false,

                rooms: [],
                enterList: [],


                saveFormRules: {
                    building: [
                        {required: true, message: '请选择楼栋', trigger: 'change'}
                    ],
                    floor: [
                        {required: true, message: '请选择楼层', trigger: 'change'}
                    ],
                    roomId: [
                        {required: true, message: '请选择房间', trigger: 'change'}
                    ],
                    respName: [
                        {required: true, message: '请填写使用人', trigger: 'change'},
                        {validator: validateName, trigger: 'change'}
                    ],
                    respMobile: [
                        {validator: validatePhone, trigger: 'change'}
                    ]
                }
            }
        },
        computed:{
            assetsTypesEntries:{
                get:function () {
                    return this.getEntries(this.assetsTypes,{label:'name',value:'id'});
                }
            }
        },
        methods: {
            changeCampus: function () {
                if (!this.saveForm.campuses) {
                    this.bases = this.basesClone.slice(0);
                    this.buildings = this.buildingsClone.slice(0);
                } else {
                    this.bases = this.getChildrenByParentId(this.saveForm.campuses, '2');
                    this.buildings = this.getChildrenByParentId(this.saveForm.campuses, '3');
                }
                this.saveForm.base = '';
                this.saveForm.building = '';
                this.saveForm.floor = '';
                this.saveForm.roomId = '';
                this.floors.length = 0;
                this.rooms.length = 0;
            },
            changeBase: function () {
                var parentId, campus;
                if (!this.saveForm.base) {
                    this.buildings = this.buildingsClone.slice(0);
                } else {
                    this.buildings = this.getChildrenByParentId(this.saveForm.base, '3');
                    parentId = this.getObjById(this.saveForm.base).pId;
                    campus = this.getObjById(parentId);
                    if (campus.type === '0') {
                        this.saveForm.campuses = '';
                        return false;
                    }
                    this.saveForm.campuses = campus.id || '';
                }
                this.floors.length = 0;
                this.rooms.length = 0;
                this.saveForm.building = '';
                this.saveForm.floor = '';
                this.saveForm.roomId = '';
            },

            changeBuilding: function () {
                var parentId, base, campus;
                this.floors = this.getChildrenByParentId(this.saveForm.building, '4');
                this.saveForm.floor = '';
                this.rooms.length = 0;
                this.saveForm.roomId = '';
                if (this.saveForm.building) {
                    parentId = this.getObjById(this.saveForm.building).pId;
                    base = this.getObjById(parentId);
                    if (base.type === '1') {
                        this.saveForm.campuses = parentId || '';
                        this.saveForm.base = '';
                    } else if (base.type === '2') {
                        this.saveForm.base = parentId || '';
                        if (!this.saveForm.base) {
                            this.saveForm.campuses = '';
                            return false;
                        }
                        parentId = this.getObjById(this.saveForm.base).pId;
                        campus = this.getObjById(parentId);
                        if (campus.type === '0') {
                            this.saveForm.campuses = '';
                            return false;
                        }
                        this.saveForm.campuses = campus.id || '';
                    }
                }
            },
            changeFloor: function () {
                var self = this;
                this.saveForm.roomId = '';
                if (!this.saveForm.floor) {
                    this.rooms.length = 0;
                    return false;
                }
                this.$axios.post('/pw/pwRoom/jsonList?pwSpace.id=' + this.saveForm.floor).then(function (response) {
                    if (response.data) {
                        self.rooms = response.data;
                    }
                });
            },

            getEnterList: function () {
                var self = this;
                if (!this.saveForm.roomId) {
                    this.enterList.length = 0;
                    return false;
                }
//                this.$axios.post('/pw/pwRoom/roomEnters?id=' + this.saveForm.roomId).then(function (response) {
//                    if (response.data) {
//                        self.enterList = response.data.enters;
//                    }
//                })
            },

            getObjById: function (id) {
                var floor = {};
                this.tree.forEach(function (item) {
                    if (item.id === id) {
                        floor = item;
                        return false;
                    }
                });
                return floor;
            },
            getChildrenByParentId: function (id, type) {
                var arr = [];
                if (!id) {
                    return arr;
                }
                this.tree.forEach(function (item) {
                    if (item.pId === id && item.type === type) {
                        arr.push(item);
                    }
                });
                return arr;
            },
            getTree: function () {
                var self = this;
                this.$axios.get('/pw/pwSpace/treeData').then(function (response) {
                    var data = response.data;
                    if (data) {
                        self.campuses = data.filter(function (item) {
                            return item.type === '1'
                        });
                        self.bases = data.filter(function (item) {
                            return item.type === '2'
                        });
                        self.buildings = data.filter(function (item) {
                            return item.type === '3'
                        });
                        self.floors = [];
                        self.hasCampus = self.campuses.length > 0;
                        self.hasBase = self.bases.length > 0;
                        self.tree = data;
                        self.campusesClone = self.campuses.slice(0);
                        self.buildingsClone = self.buildings.slice(0);
                        self.basesClone = self.bases.slice(0);
                    }
                })
            },
            save: function (formName) {
                var self = this;
                self.$refs[formName].validate(function (valid) {
                    if (valid) {
                        self.saveAjax();
                    }
                })
            },
            saveAjax: function () {
                var self = this;
                this.formDisabled = true;
                this.$axios({
                    method: 'POST',
                    url: '/pw/pwFassets/assign',
                    data: self.saveForm
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
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
                        type: 'error'
                    })
                });
            }
        },
        created: function () {
            this.getTree();
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