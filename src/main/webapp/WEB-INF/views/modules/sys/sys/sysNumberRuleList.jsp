<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>


</head>
<body>

<div id="app" class="container-fluid container-fluid_bg" v-show="pageLoad" style="display: none"
     :style="{'min-height':height + 'px'}">
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>
    <div class="mgb-20">
        <el-button icon="el-icon-circle-plus" type="primary" size="mini" @click.stop="addAppRules">新建编号规则</el-button>
    </div>
    <div class="table-container">
        <table class="table el-table table-font12 table-center table-new-bordered" style="margin-bottom: 0">
            <thead>
            <tr>
                <th>规则名称</th>
                <th>示例</th>
                <th>关联项目</th>
                <th width="160">操作</th>
            </tr>
            </thead>
            <tr v-for="(appRule, index) in appList">
                <td>
                    <div class="cell">
                        {{appRule.name}}
                    </div>
                </td>
                <td>
                    <div class="cell">
                        <p v-show="!appRule.updateRule">{{appRule.rule}}</p>
                        <%--<p v-show="appRule.updateRule">{{getUpdateRule()}}</p>--%>
                    </div>
                </td>
                <td>
                    <div class="cell">
                        {{appRule.appTypeName}}
                    </div>
                </td>
                <td>

                    <div class="cell">
                        <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                            <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                       @click.stop.prevent="updateAppRule(appRule, index)">
                            </el-button>
                        </el-tooltip>
                        <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                            <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                       :disabled="dialogAppRulesFormVisible || appRule.isPublish == '1'"
                                       @click.stop.prevent="removeAppRule(appRule.id, index)">
                            </el-button>
                        </el-tooltip>
                    </div>
                </td>
            </tr>
            <tr v-show="!appList.length">
                <td colspan="4"><span class="empty-color">没有数据</span></td>
            </tr>
        </table>
    </div>
    <el-dialog
            :title="action + '编号规则'"
            :visible.sync="dialogAppRulesFormVisible"
            :close-on-click-modal="false"
            width="704px"
            :close-on-click-modal="false"
            :before-close="handleAppRulesFormClose">
        <div>

            <el-form :inline="true"
                     :model="appRulesForm"
                     label-width="100px"
                     size="mini"
                     auto-complete="off"
                     :disabled="appRuleIsSave"
                     ref="appRulesForm">
                <div class="app-rule-item" style="width: 100%;">
                    <el-form-item label="关联项目：" required prop="appType" :rules="appRuleFormRules['appType']">
                        <el-cascader ref="cascader"
                                     :options="appTypeTree"
                                     size="mini"
                                     :disabled="!isAddAppRule"
                                     clearable
                                     :props="cascaderDefault"
                                     style="width: 200px;"
                                     @change="handleChangeAppType"
                                     v-model="appRulesForm.appType"
                        >
                        </el-cascader>
                    </el-form-item>
                </div>
                <div class="app-rule-item" style="width: 100%;">
                    <el-form-item label="规则名称：" align="right" required>
                        <el-input v-model="appRulesForm.name" style="width: 200px;"></el-input>
                    </el-form-item>
                </div>

                <div class="app-rule-item" style="width: 100%">
                    <el-form-item label="编号规则：">
                        <div class="app-rule-item" v-for="(rule, index2) in appRulesForm.sysNumberRuleDetailList"
                             style="width: 100%">
                            <el-form-item size="mini" style="width: 270px;"
                                          :key="rule.key || rule.id"
                                          :prop="'sysNumberRuleDetailList.' + index2 + '.ruleType'">
                                <label :for="'sysNumberRuleDetailList.' + index2 + '.text'"
                                       class="el-form-item__label is-required" slot="label">{{index2 |
                                    ruleItemLabel}}</label>

                                <el-select v-model="rule.ruleType" placeholder="请选择" style="width: 115px;"
                                           @change="handleChangeRuleType(rule)">
                                    <el-option label="-请选择-" value=""></el-option>
                                    <el-option v-for="ruleType in ruleTypes"
                                               :disabled="appRuleRuleTypes(appRulesForm.sysNumberRuleDetailList).indexOf(ruleType.id) > -1"
                                               :label="ruleType.value"
                                               :value="ruleType.id" :key="ruleType.id"></el-option>
                                </el-select>
                                <el-button type="text"
                                           v-show="appRulesForm.sysNumberRuleDetailList.length < ruleTypes.length"
                                           @click.prevent="addRuleType(appRulesForm.sysNumberRuleDetailList)"><i
                                        class="iconfont icon-tianjia"></i></el-button>
                                <el-button type="text" v-show="appRulesForm.sysNumberRuleDetailList.length > 1"
                                           @click.prevent="removeRuleType(appRulesForm.sysNumberRuleDetailList, index2)">
                                    <i
                                            class="iconfont icon-shanchu"></i></el-button>
                            </el-form-item>
                            <template v-if="rule.ruleType == 'FIXED'">
                                <el-form-item size="mini" :prop="'sysNumberRuleDetailList.' + index2 + '.text'"
                                              :rules="appRuleFormRules['FIXED']">
                                    <el-input v-model="rule.text" placeholder="请输入值" style="width: 240px;"></el-input>
                                </el-form-item>
                            </template>
                            <template v-if="rule.ruleType == 'RANDOM'">
                                <el-form-item size="mini" :prop="'sysNumberRuleDetailList.' + index2 + '.numLength'"
                                              :rules="appRuleFormRules['RANDOM']">
                                    <el-input v-model.number="rule.numLength" placeholder="位数"
                                              style="width: 240px;"></el-input>
                                </el-form-item>
                            </template>
                            <template v-if="rule.ruleType == 'LEVEL'">
                                <el-form-item size="mini">
                                    <span v-show="rule.text">{{rule.text}}</span>
                                    <span class="empty-color" v-show="!rule.text">在项目立项完成后获取项目级别生成该部分</span>
                                </el-form-item>
                            </template>
                            <template v-if="rule.ruleType == 'CUSTOM'">
                                <el-form-item size="mini" :prop="'sysNumberRuleDetailList.' + index2 + '.text'"
                                              :rules="appRuleFormRules['CUSTOMTEXT']" style="width: 100px;">
                                    <el-input v-model.number="rule.text" placeholder="开始数"></el-input>
                                </el-form-item>
                                <el-form-item size="mini" :prop="'sysNumberRuleDetailList.' + index2 + '.numLength'"
                                              :rules="appRuleFormRules['CUSTOMNumLength']"
                                              style="width: 100px;">
                                    <el-input v-model.number="rule.numLength" placeholder="位数"></el-input>
                                </el-form-item>
                            </template>
                            <template v-if="rule.ruleType == 'DATE'">
                                <el-form-item size="mini" :prop="'sysNumberRuleDetailList.' + index2 + '.nDate'"
                                              :rules="appRuleFormRules['DATE']">
                                    <el-checkbox-group v-model="rule.nDate">
                                        <el-checkbox-button :label="date.value" v-for="date in dates"
                                                            :key="date.value">{{date.label}}
                                        </el-checkbox-button>
                                    </el-checkbox-group>
                                </el-form-item>
                            </template>
                            <template v-if="rule.ruleType == 'YEAR'">
                                <el-form-item size="mini" :prop="'sysNumberRuleDetailList.' + index2 + '.text'"
                                              :rules="appRuleFormRules['YEAR']">
                                    <%--<el-date-picker--%>
                                    <%--v-model="rule.text"--%>
                                    <%--type="year"--%>
                                    <%--value-format="yyyy"--%>
                                    <%--default-time="00:00:00"--%>
                                    <%--placeholder="选择项目年份">--%>
                                    <%--</el-date-picker>--%>
                                    <span v-show="rule.text">{{rule.text}}</span>
                                    <span class="empty-color" v-show="!rule.text">前台学生申报年份</span>
                                </el-form-item>

                            </template>
                            <template v-if="rule.ruleType == 'PROTYPE'">
                                <el-form-item size="mini" :prop="'sysNumberRuleDetailList.' + index2 + '.proType'"
                                              :rules="appRuleFormRules['PROTYPE']">
                                    <el-checkbox-group v-model="rule.proType" @change=handleChangeProType(rule)>
                                        <el-checkbox-button v-for="item in proTypes" :key="item.value"
                                                            :label="item.value">
                                            {{item.label}}
                                        </el-checkbox-button>
                                    </el-checkbox-group>

                                </el-form-item>
                                <el-form-item size="mini" :prop="'sysNumberRuleDetailList.' + index2 + '.map'"
                                              style="display:block" label-width="284px" label="类型补位"
                                              :rules="appRuleFormRules['map']">
                                    <slot name="label"></slot>
                                    <el-input v-for="item in rule.map"
                                              :disabled="rule.proType.indexOf(item.proType) == -1" :key="item.proType"
                                              v-model="item.ruleText" style="width: 80px;"
                                              placehoder="请输入自定义补位编号"></el-input>
                                </el-form-item>

                            </template>
                        </div>
                    </el-form-item>
                </div>
            </el-form>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" @click="handleAppRulesFormClose">取 消</el-button>
            <el-button size="mini" type="primary" @click="saveAppRule">确 定</el-button>
        </div>
    </el-dialog>
</div>

<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var appList = JSON.parse(JSON.stringify(${fns: toJson(list)}));
            var self = this;
            var height = $(window).height();

            //重新构建数据
            appList.forEach(function (app) {
                var sysNumberRuleDetailList = app.sysNumberRuleDetailList || [];
                sysNumberRuleDetailList.forEach(function (item) {
                    item.typeValue = item.jsMap || {"map": [{"1": "S"}, {"2": "X"}, {"3": ""}]};
                    var typeValue = item.typeValue;
                    var maps = typeValue.map;
                    item.proType = []
                    item.map = maps.map(function (item1) {
                        var nItem = {};
                        for (var k in item1) {
                            if (item1.hasOwnProperty(k)) {
                                nItem['proType'] = k;
                                nItem['ruleText'] = item1[k];
                                item.proType.push(k)
                            }
                        }
                        return nItem
                    });

                })
            })


            var validateFixedText = function (rule, value, callback) {
                if (value === '') {
                    callback(new Error('请输入值'));
                } else {
                    if (!(/^[a-zA-Z0-9]+$/.test(value))) {
                        callback(new Error('请输入字母或者数字'));
                    } else {
                        callback();
                    }
                }
            };
            var validateFixedTextType = function (rule, value, callback) {
                var index = rule.field.split('.')[1];
                var sysNumberRuleDetailList = self.appRulesForm.sysNumberRuleDetailList;
                var map = sysNumberRuleDetailList[index].map
                for (var i = 0; i < map.length; i++) {
                    if (!map[i]['ruleText']) {
                        continue;
                    }
                    if (!(/^[a-zA-Z0-9]+$/.test(map[i]['ruleText']))) {
                        return callback(new Error('请输入字母或者数字'));
                    } else if (map[i]['ruleText'].length > 10) {
                        return callback(new Error('补位类别单个区间字符大不于10位'));
                    }
                }
                return callback();
            };
            var validateRANDOMNum = function (rule, value, callback) {
                if (value === '') {
                    callback(new Error('请输入数字'));
                } else {
                    if (!(value >= 0 && value <= 12 && /^[0-9]+$/.test(value))) {
                        callback(new Error('请输入0-12之间的整位数'));
                    } else {
                        callback();
                    }
                }
            };

            var validateCUSTOMNum = function (rule, value, callback) {
                var fullField = rule.fullField;
                fullField = fullField.split('.');
                var text = (self.appRulesForm[fullField[0]])[fullField[1]].text;
                if (value === '') {
                    callback(new Error('请输入位数'));
                } else {
                    var len = text.toString().length;
                    if (!(value >= len && value <= len + 12 && /^[0-9]+$/.test(value))) {
                        callback(new Error('请输入' + len + '-' + (len + 12) + '之间的整位数'));
                    } else {
                        callback();
                    }
                }
            };

            var validateCUSTOMTEXT = function (rule, value, callback) {
                if (value === '') {
                    callback(new Error('请输入开始数'));
                } else {
                    if (!(value >= 0 && /^[0-9]+$/.test(value))) {
                        callback(new Error('请输入正整数'));
                    } else {
                        callback();
                    }
                }
            };

            var validateAppType = function (rule, value, callback) {
                var treeEntries = self.treeEntries();
                if (!value || !value.length) return callback(new Error('请关联项目'));
                if (value.length === 1) {
                    return callback(new Error('关联项目数据错误，请联系管理员修改'));
                }
                ;
                return self.$axios.post('/sys/sysNumberRule/checkAppTypeUnique?appType=' + treeEntries[value[1]].id + "&id=" + self.appRulesForm.id).then(function (response) {
                    var data = response.data;
                    if (data.code != 0) {
                        callback(new Error(data.msg));
                    } else {
                        callback();
                    }
                })
            }

            return {
                appList: appList,
                pathAppType: '${appType}',
                appListFb: [],
                appTypeTree: [],
                selectedAppRuleIds: [],
                ruleTypes: [],
                appRuleEditForm: {
                    appRules: []
                },
                height: height,
                cascaderDefault: {
                    label: 'text',
                    value: 'id',
                    children: 'childList'
                },

                appRulesForm: {
                    id: '',
                    name: '',
                    appType: [],
                    appTypeName: '',
                    isPublish: '0',
                    sysNumberRuleDetailList: [{
                        ruleType: 'FIXED',
                        nDate: [],
                        numLength: '',
                        text: '',
                        proType: ['1', '2', '3'],
                        map: [
                            {proType: '1', ruleText: 'S'},
                            {proType: '2', ruleText: 'X'},
                            {proType: '3', ruleText: ''}
                        ],
                        typeValue: '',
                        key: Date.now()
                    }]
                },

                appRuleIsSave: false,
                dates: [{label: '年', value: 'yyyy'}, {label: '月', value: 'MM'}, {
                    label: '日',
                    value: 'dd'
                }, {label: '时', value: 'HH'}, {label: '分', value: 'mm'}, {label: '秒', value: 'ss'}],
                proTypes: [
                    {label: '创新训练', value: '1'},
                    {label: '创业训练', value: '2'},
                    {label: '创业实践', value: '3'}
                ],

                appRuleFormRules: {
                    'appType': [{
                        validator: validateAppType,
                        trigger: 'change'
                    }],
                    'FIXED': [{validator: validateFixedText, trigger: 'blur'}, {
                        min: 1,
                        max: 12,
                        message: '长度在 1 到 12 个字符',
                        trigger: 'blur'
                    }],
                    'RANDOM': [{validator: validateRANDOMNum, trigger: 'blur'}],
                    'DATE': [{required: true, message: '请选择日期', trigger: 'change'}],
                    "CUSTOMTEXT": [{validator: validateCUSTOMTEXT, trigger: 'blur'}],
                    'CUSTOMNumLength': [{validator: validateCUSTOMNum, trigger: 'blur'}],
                    'PROTYPE': [{required: false, message: '请选择项目类别', trigger: 'blur'}],
                    'map': [
                        {validator: validateFixedTextType, trigger: 'blur'},
                    ]
                },
                dialogAppRulesFormVisible: false,
                isAddAppRule: true,
                updateAppRuleIndex: '',
                action: ''
            }
        },
        computed: {
            ruleTypeEntries: {
                get: function () {
                    var ruleTypes = this.ruleTypes;
                    var entries = {};
                    for (var i = 0; i < ruleTypes.length; i++) {
                        entries[ruleTypes[i].id] = ruleTypes[i].value;
                    }
                    return entries;
                }
            }

        },
        filters: {
            ruleItemLabel: function (index) {
                var str = '第X部分';
                ++index;
                switch (index.toString()) {
                    case '1':
                        return str.replace('X', '一');
                    case '2':
                        return str.replace('X', '二');
                    case '3':
                        return str.replace('X', '三');
                    case '4':
                        return str.replace('X', '四');
                    case '5':
                        return str.replace('X', '五');
                    case '6':
                        return str.replace('X', '六');
                }
            }
        },
        methods: {

            handleChangeProType: function (rule) {
                var proTypes = rule.proType;
                var map = rule.map;
                map.forEach(function (item) {
                    if (proTypes.indexOf(item.proType) == -1) {
                        item.ruleText = '';
                    }
                })
//                rule.ruleText = this.ruleTextOb[rule.proType]
            },

            handleAppRulesFormClose: function () {
                this.dialogAppRulesFormVisible = false;
            },
            isRuleTextDisabled: function (value) {
                return value == '1' || value == '2'
            },

            handleChangeRuleType: function (rule) {
                this.$refs.appRulesForm.clearValidate()
                rule.text = '';
                if (rule.ruleType == 'DATE' && !rule.nDate) {
                    Vue.set(rule, 'nDate', []);
                }
            },

            addAppRules: function () {

                this.dialogAppRulesFormVisible = true;
                this.isAddAppRule = true;
                this.action = '新建';
                this.$nextTick(function () {
                    this.$refs.appRulesForm.resetFields();
                    this.appRulesForm.id = '';
                    this.appRulesForm.isPublish = '0';
                    this.appRulesForm.name = '';
                    this.appRulesForm.appType = [];
                    this.appRulesForm.appTypeName = '';
                    this.appRulesForm.sysNumberRuleDetailList = [{
                        ruleType: 'FIXED',
                        nDate: [],
                        numLength: '',
                        text: '',
                        proType: ['1', '2', '3'],
                        map: [
                            {proType: '1', ruleText: 'S'},
                            {proType: '2', ruleText: 'X'},
                            {proType: '3', ruleText: ''}
                        ],
                        ruleText: '',
                        typeValue: '',
                        key: Date.now()
                    }];
                })

            },

            appRuleRuleTypes: function (appRules) {
                var rTs = [];
                for (var i = 0; i < appRules.length; i++) {
                    rTs.push(appRules[i].ruleType)
                }
                return rTs
            },

            removeRuleType: function (appRules, ruleTypeIndex) {
                appRules.splice(ruleTypeIndex, 1)
            },

            addRuleType: function (appRules) {
                appRules.push({
                    ruleType: '',
                    nDate: [],
                    numLength: '',
                    text: '',
                    proType: ['1', '2', '3'], //类型
                    map: [                      //类型map自定义
                        {proType: '1', ruleText: 'S'},
                        {proType: '2', ruleText: 'X'},
                        {proType: '3', ruleText: ''}
                    ],
                    ruleText: '',
                    typeValue: '',
                    key: Date.now()
                })
            },

            getAppTypeTree: function () {
                var self = this;
                var pathAppType = this.pathAppType;
                if(!!pathAppType){
                    self.addAppRules();
                }
                this.$axios.get('/sys/sysNumberRule/getAppTypeTree').then(function (response) {
                    var data = response.data;
                    if(data.code == '0'){
                        var appTypeTree = data.data || [];
                        var list = self.treeFlatten(appTypeTree);
                        if(!!pathAppType){
                            for(var i = 0; i < list.length; i++){
                                var item = list[i];
                                if(item.id === pathAppType){
                                    self.appRulesForm.appType = [item.parentId, item.id];
                                    break;
                                }
                            }
                        }
                        self.appTypeTree = appTypeTree;

                    }

                }).catch(function (error) {

                });
            },

            treeFlatten: function (data) {
                function flatten(data) {
                    return data.reduce(function (cur, next) {
                        return cur.concat(next, flatten((next.childList || [])), [])
                    }, [])
                }

                return flatten(data);
            },

            treeEntries: function () {
                var nAppTypeTree = this.treeFlatten(this.appTypeTree);
                var i = 0, entries = {};
                while (i < nAppTypeTree.length) {
                    entries[nAppTypeTree[i].id] = nAppTypeTree[i];
                    i++;
                }
                return entries
            },


//            addAppRuleDefaultData: function (selectedAppRuleIds, treeEntries) {
//                return {
//                    id: '',
//                    appType: selectedAppRuleIds[1],
//                    appTypeName: (treeEntries[selectedAppRuleIds[0]].text) + '/' + (treeEntries[selectedAppRuleIds[1]].text),
//                    sysNumberRuleDetailList: [{
//                        ruleType: 'FIXED',
//                        nDate: [],
//                        numLength: '',
//                        text: '',
//                        proType: ['1', '2', '3'], //类型
//                        map: [                      //类型map自定义
//                            {proType: '1', ruleText: 'S'},
//                            {proType: '2', ruleText: 'X'},
//                            {proType: '3', ruleText: ''}
//                        ],
//                        ruleText: '',
//                        typeValue: '',
//                        key: Date.now()
//                    }]
//                }
//            },

//            //创建一条规则
//            addAppRule: function () {
//                var selectedAppRuleIds = this.selectedAppRuleIds;
//                var treeEntries = this.treeEntries();
//                var self = this;
//                this.$axios.post('/sys/sysNumberRule/checkAppTypeUnique?appType=' + treeEntries[selectedAppRuleIds[1]].id).then(function (response) {
//                    var data = response.data;
//                    if (data.code === 0) {
//                        self.appList.unshift(self.addAppRuleDefaultData(selectedAppRuleIds, treeEntries))
//                    } else {
//                        self.$message.error(data.msg)
//                    }
//                }).catch(function (error) {
//                    self.$message.error(error.response.data)
//                });
//            },
            //获取规则
            getRuleTypes: function () {
                var self = this;
                return this.$axios.get('/sys/sysNumberRule/getRuleTypes').then(function (response) {
                    var data = response.data;
                    if (data) {
                        self.ruleTypes = data.data;
                    }
                }).catch(function (error) {

                })
            },


            saveValidate: function (appRule) {
                var postData = JSON.stringify(this.getPostAppRulesForm(appRule));
                var self = this;
                this.appRuleIsSave = true;
                Vue.set(appRule, 'isSave', true);

                this.$axios({
                    method: 'POST',
                    url: '/sys/sysNumberRule/save',
                    data: JSON.parse(postData)
                }).then(function (response) {
                    var data = response.data;
                    if (data.code === 0) {
                        if (!appRule.id) {
                            self.appList.unshift(self.fixedAppRuleData(data.data));
                        } else {
                            self.appList.splice(self.updateAppRuleIndex, 1, self.fixedAppRuleData(data.data))
                        }
                        self.dialogAppRulesFormVisible = false;
                        self.$alert(data.msg, '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.appRuleIsSave = false;

                }).catch(function (error) {
                    self.appRuleIsSave = false;
                })
            },

            //构建数据
            fixedAppRuleData: function (data) {
                data.sysNumberRuleDetailList.forEach(function (item) {
                    //更新
                    item.typeValue = item.typeValue ? JSON.parse(item.typeValue) : ( item.jsMap || {"map": [{"1": "S"}, {"2": "X"}, {"3": ""}]});
                    var typeValue = item.typeValue;
                    var maps = typeValue.map;
                    item.proType = [];
                    item.map = maps.map(function (item1) {
                        var nItem = {};
                        for (var k in item1) {
                            if (item1.hasOwnProperty(k)) {
                                nItem['proType'] = k;
                                nItem['ruleText'] = item1[k];
                                item.proType.push(k)
                            }
                        }
                        return nItem
                    });
                })
                return data;
            },

            //保存
            saveAppRule: function () {
                var self = this;
                this.$refs.appRulesForm.validate(function (vaild) {
                    if (vaild) {
                        self.saveValidate(self.appRulesForm)
                    }
                });
            },


            //新版表单
            getPostAppRulesForm: function (appRule) {
                var appRules = this.appRulesForm.sysNumberRuleDetailList;
                var levelSort = -1;
                var posText = '';
                var nAppRules = JSON.parse(JSON.stringify(appRules));
                var treeEntries = this.treeEntries();
                for (var i = 0; i < nAppRules.length; i++) {
                    if (!nAppRules[i].ruleType) {
                        nAppRules.splice(i, 1);
                        continue;
                    }
                    nAppRules[i].sort = (i + 1);
                    if (nAppRules[i].ruleType === 'DATE') {
                        nAppRules[i].text = nAppRules[i].nDate.join('');
                        posText += nAppRules[i].text;
                    }
                    if (nAppRules[i].ruleType === 'LEVEL') {
                        levelSort = nAppRules[i].sort;
                        posText += '{LEVEL}'
                    }
                    if (nAppRules[i].ruleType === 'FIXED') {
                        posText += nAppRules[i].text;
                    }
                    if (nAppRules[i].ruleType === 'YEAR') {
                        posText += '{YEAR}'
                    }
                    if (nAppRules[i].ruleType === 'RANDOM' || nAppRules[i].ruleType === 'CUSTOM') {
                        Array.apply(null, {length: parseInt(nAppRules[i].numLength)}).forEach(function (item) {
                            posText += 'A'
                        })
                    }
                }
                var name;
                var appType;
                var appTypeName;

                if (appRule.appType.length > 0) {
                    name = appRule.name;
                    appType = appRule.appType[1];
                    appTypeName = (treeEntries[appRule.appType[1]].text) + '/' + (treeEntries[appRule.appType[1]].text);
                }

                return {
                    id: appRule.id,
                    name: name,
                    appType: appType,
                    appTypeName: appTypeName,
                    isPublish: appRule.isPublish || '0',
                    increNum: 1,
                    levelIndex: posText.indexOf('{LEVEL}'),
                    sysNumberRuleDetailList: this.getNAppRulesParams(nAppRules)
                }
            },


            getNAppRulesParams: function (nAppRules) {
                var arr = [];
                for (var i = 0; i < nAppRules.length; i++) {
                    var rules = nAppRules[i];
                    var nRules = {};
                    var isNeedNumLen = rules['ruleType'] === 'CUSTOM' || rules['ruleType'] === 'RANDOM';
                    if (rules.proType.length > 0) {
                        var nMap = rules.map.map(function (item) {
                            var rMap = {};
                            rMap[item.proType] = item.ruleText;
                            return rMap
                        });
                        rules.typeValue = JSON.stringify({map: nMap})
                    } else {
                        rules.typeValue = '{"map":[{"1":""},{"2":""},{"3":""}]}';
                    }
                    for (var k in rules) {
                        if (!rules.hasOwnProperty(k)) {
                            continue;
                        }
                        if (k !== 'key' && k !== 'nDate') {
                            if (!isNeedNumLen) {
                                if (k === 'numLength') {
                                    continue;
                                }
                            }
                            if (rules[k] == '') {
                                continue;
                            }
                            nRules[k] = rules[k];
                        }
                        delete nRules.map;
                        delete nRules.proType;
                        delete nRules.ruleText;
                        delete nRules.jsMap;
                    }
                    arr.push(nRules)
                }
                return arr;
            },


            updateAppRule: function (appRule, index) {
                if (appRule.updateRule) {
                    return false;
                }
                var treeEntries = this.treeEntries();
                this.dialogAppRulesFormVisible = true;
                this.isAddAppRule = false;
                this.action = '编辑';
                var appRulesForm = JSON.parse(JSON.stringify((appRule)));
                var parentId = treeEntries[appRulesForm.appType].parentId;
                var currentId = appRulesForm.appType;
                appRulesForm.appType = [parentId, currentId];
                appRulesForm.appTypeName = [treeEntries[parentId].text, treeEntries[currentId].text].join('/');
                this.appRulesForm = appRulesForm;
                var appRules = this.appRulesForm.sysNumberRuleDetailList;
                for (var i = 0; i < appRules.length; i++) {
                    var item = appRules[i];
                    if (item.ruleType === 'DATE') {
                        if (!item.nDate) {
                            Vue.set(item, 'nDate', []);
                            var n = 0;
                            var len = item.text ? item.text.length : 0;
                            while (n < len) {
                                if (item.text[n] === 'y') {
                                    n += 4;
                                    item.nDate.push(item.text.substring(n - 4, n))
                                } else {
                                    n += 2;
                                    item.nDate.push(item.text.substring(n - 2, n))
                                }
                            }
                        }
                        break;
                    }
                }
                this.updateAppRuleIndex = index;
            },

            cancelAppRule: function (appRule) {
                Vue.set(appRule, 'updateRule', false);
            },


            //删除
            removeAppRule: function (id, index) {
                var self = this;
                return this.$axios.post('/sys/sysNumberRule/delete', {id: id}).then(function (response) {
                    var data = response.data;
                    if (data.code === 0) {
                        self.appList.splice(index, 1)
                        self.$alert(data.msg, '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                })
            },

            getUpdateRule: function () {
                var appRules = this.appRuleEditForm.appRules;
                var text = '';

                for (var i = 0; i < appRules.length; i++) {
                    if (appRules[i].ruleType === 'DATE') {
                        if (appRules[i].nDate.length > 0) {
                            text += '{DATE-' + appRules[i].nDate.join('') + '}';
                        }
                    } else if (appRules[i].ruleType === 'FIXED') {
                        if (appRules[i].text) {
                            text += appRules[i].text;
                        }
                    } else if (appRules[i].ruleType === 'RANDOM') {
                        if (appRules[i].numLength) {
                            text += '{' + appRules[i].ruleType + '-' + appRules[i].numLength + '}';
                        }
                    } else {
                        if (appRules[i].text) {
                            text += '{' + appRules[i].ruleType + '-' + appRules[i].text + '}';
                        }
                    }
                }
                return text;
            },

            createdAppListFb: function () {
                return this.appListFb = [].concat(this.appList);
            },

            //获取name
            handleChangeAppType: function (value) {

                if (!value || !value.length) {
                    return;
                }
                var treeEntries = this.treeEntries();
                this.appRulesForm.appTypeName = (treeEntries[value[1]].text) + '/' + (treeEntries[value[1]].text)

            }
        },
        mounted: function () {
            this.getAppTypeTree();
            this.getRuleTypes();
            this.createdAppListFb();
        }
    })
</script>
</body>
</html>