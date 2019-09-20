/**
 * Created by Administrator on 2018/11/23.
 */


Vue.component('enter-form-group', {
    template: '<div class="enter-form-group"><slot></slot></div>',
    componentName: 'EnterFormGroup',
    props: {
        formNames: Array,
        isAdmin: Boolean,
        pwEnterId: String,
        disabled: Boolean
    },
    data: function () {
        return {
            formGroups: [],
            formErrorMessages: {
                'linkTeamForm': "入驻团队不合格或者不存在，请检查",
                'linkProjectForm': "入驻项目不存在，请检查",
                'enterpriseForm': "入驻企业不合格或者存在部分表单未填写，请检查",
                'pwSiteForm': "场地要求不合格，请检查",
                'LinkTeamFormAdmin': "入驻团队不合格或者不存在，请检查",
            }
        }
    },
    computed: {
        //通过父级找到refs component;
        appComponent: function () {
            var formNames = this.formNames;
            var $parent = this.$parent;
            if (formNames.length === 0) {
                return false;
            }
            while ($parent) {
                if (!$parent) {
                    break;
                }
                var $refs = $parent.$refs;
                if (this.hasFormGroup($refs)) {
                    return $parent;
                } else {
                    $parent = $parent.$parent;
                }
            }
            return false
        },


    },
    methods: {


        updateFormGroups: function () {
            var formNames = this.formNames;
            if (!this.isAdmin) {
                this.formGroups = this.$children.filter(function (item) {
                    return formNames.indexOf(item.$options.componentName) > -1;
                })
                return false;
            }
            if (this.appComponent) {
                this.formGroups = this.getFormGroups(this.appComponent.$refs);
            }
            // this.formGroups = this.getFormGroups();
            // console.log(this.formGroups)
        },
        hasFormGroup: function ($refs) {
            var formNames = this.formNames;
            for (var k in $refs) {
                if ($refs.hasOwnProperty(k)) {
                    var nk = k.replace(/^\w{1}/, function ($1) {
                        return $1.toUpperCase();
                    })
                    if (formNames.indexOf(nk) > -1) {
                        return true;
                    }
                }
            }
            return false;
        },

        getFormGroups: function ($refs) {
            var components = [];
            var formNames = this.formNames;
            for (var k in $refs) {
                if ($refs.hasOwnProperty(k)) {
                    var nk = k.replace(/^\w{1}/, function ($1) {
                        return $1.toUpperCase();
                    })
                    if (formNames.indexOf(nk) > -1) {
                        components.unshift($refs[k])
                    }

                }
            }
            return components;
        },

        getEnterFormData: function (component, componentName) {
            return component.$data[componentName];
        },

        getEnterFormValidatePromise: function () {
            var formGroups = this.formGroups;
            var self = this;
            var promises = [];
            for (var i = 0; i < formGroups.length; i++) {
                var item = formGroups[i];
                var componentName = item.$options.componentName;
                componentName = componentName.replace(/^[A-Z]+/, function ($1, $2) {
                    return $1.toLowerCase();
                });
                if (componentName === 'linkProjectForm') {
                    promises.push(self.promiseLinkProjectForm(item, componentName));
                } else {
                    promises.push(self.validateFormPromise(item, componentName));
                }
            }
            return Promise.all(promises)
        },

        promiseLinkProjectForm: function (item, componentName) {
            var self = this;
            item.$refs[componentName] && item.$refs[componentName].resetFields();
            return new Promise(function (reslove, reject) {
                if (item.$data.projectList.length === 0) {
                    reject({
                        component: item,
                        formData: item.$data.projectList,
                        error: self.formErrorMessages[componentName]
                    });
                }
                var formGroupData = self.getFormGroupDataByName(item, componentName);
                reslove(formGroupData);
            })
        },

        validateFormPromise: function (item, componentName) {
            var formGroupData;
            var self = this;
            return new Promise(function (resolve, reject) {
                item.$refs[componentName].validate(function (valid) {
                    if (valid) {
                        formGroupData = self.getFormGroupDataByName(item, componentName);
                        resolve(formGroupData);
                    } else {
                        reject({
                            component: item,
                            formData: self.getEnterFormData(item, componentName),
                            error: self.formErrorMessages[componentName]
                        })
                    }
                })
            })
        },

        //保存不用验证
        getEnterFormSave: function () {
            var formGroups = this.formGroups;
            var self = this;
            var promises = [];
            for (var i = 0; i < formGroups.length; i++) {
                var item = formGroups[i];
                var componentName = item.$options.componentName;
                componentName = componentName.replace(/^[A-Z]+/, function ($1, $2) {
                    return $1.toLowerCase();
                });
                if (componentName === 'linkProjectForm') {
                    promises.push(self.promiseLinkProjectFormSave(item, componentName));
                } else {
                    promises.push(self.validateFormPromiseSave(item, componentName));
                }
            }
            return Promise.all(promises)
        },

        promiseLinkProjectFormSave: function (item, componentName) {
            var self = this;
            item.$refs[componentName] && item.$refs[componentName].resetFields();
            return new Promise(function (reslove, reject) {
                var formGroupData = self.getFormGroupDataByName(item, componentName);
                reslove(formGroupData);
            })
        },

        validateFormPromiseSave: function (item, componentName) {
            var formGroupData;
            var self = this;
            item.$refs[componentName] && item.$refs[componentName].clearValidate();
            return new Promise(function (resolve, reject) {
                formGroupData = self.getFormGroupDataByName(item, componentName);
                resolve(formGroupData);
            })
        },

        getFormGroupDataByName: function (item, componentName) {
            var formGroupData = {};
            switch (componentName) {
                case "linkProjectForm":
                    formGroupData['pwProjectList'] = item.projectList.map(function (item) {
                        var id = item.id.indexOf('temp') > -1 ? '' : item.id;
                        return {
                            // eid: eid,
                            id: id,
                            name: item.name,
                            remarks: item.remarks,
                            sysAttachmentList: item.attachMentEntity.map(function (item) {
                                item.id = '';
                                return item;
                            }),
                        }
                    });
                    break;
                case "linkTeamForm":
                    formGroupData["teamId"] = this.getEnterFormData(item, componentName).teamId;
                    formGroupData['stus'] = item.teamData.studentList || [];
                    formGroupData['teas'] = item.teamData.teacherList || [];
                    break;
                case "enterpriseForm":
                    var pwCompany = JSON.parse(JSON.stringify(this.getEnterFormData(item, componentName)));
                    var pwCompanySysAttachmentList = pwCompany.sysAttachmentList;
                    pwCompany.regMoney = parseInt(pwCompany.regMoney);
                    pwCompany.sysAttachmentList = pwCompanySysAttachmentList.map(function (item) {
                        return {
                            "uid": item.response ? '' : item.uid,
                            "fileName": item.title || item.name,
                            "name": item.title || item.name,
                            "suffix": item.suffix,
                            "url": item.url,
                            "ftpUrl": item.ftpUrl,
                            "fielSize": item.fielSize,
                            "tempUrl": item.tempUrl,
                            "tempFtpUrl": item.tempFtpUrl,
                        }
                    });
                    formGroupData['pwCompany'] = pwCompany
                    break;
                default:
                    Object.assign(formGroupData, this.getEnterFormData(item, componentName));
            }
            return formGroupData;
        },


        saveEnterForm: function () {
            var self = this;
            return this.getEnterFormValidatePromise()
        },

        noValidateForm: function () {
            return this.getEnterFormSave()
        }
    }
})

Vue.component('enterprise-form', {
    template: '<el-form :model="enterpriseForm" :disabled="isDisabled" ref="enterpriseForm" :rules="enterpriseRules" size="mini" label-width="110px"\n' +
    '                 autocomplete="off">\n' +
    '            <el-form-item prop="name" label="企业名称：">\n' +
    '                <el-input name="name" :disabled="isDisabledName" v-model="enterpriseForm.name"></el-input>\n' +
    '            </el-form-item>\n' +
    '            <el-form-item prop="no" label="工商注册号：">\n' +
    '                <el-input name="no" :disabled="isDisabledName" v-model="enterpriseForm.no"></el-input>\n' +
    '            </el-form-item>\n' +
    '            <el-form-item prop="regMoney" label="注册资金：">\n' +
    '                <el-input name="regMoney" v-model="enterpriseForm.regMoney" maxlength="10" class="w300">\n' +
    '                    <template slot="prepend">￥</template>\n' +
    '                    <template slot="append">万元</template>\n' +
    '                </el-input>\n' +
    '            </el-form-item>\n' +
    '            <el-form-item prop="regMtypes" label="资金来源：">\n' +
    '                <el-checkbox-group v-model="enterpriseForm.regMtypes">\n' +
    '                    <el-checkbox v-for="item in regMtypes" :key="item.value" :label="item.value">{{item.label}}\n' +
    '                    </el-checkbox>\n' +
    '                </el-checkbox-group>\n' +
    '            </el-form-item>\n' +
    '            <el-form-item prop="address" label="注册地址：">\n' +
    '                <el-input name="address" v-model="enterpriseForm.address"></el-input>\n' +
    '            </el-form-item>\n' +
    '            <el-form-item prop="remarks" label="公司主营业务：">\n' +
    '                <el-input name="remarks" type="textarea" :rows="4" v-model="enterpriseForm.remarks"></el-input>\n' +
    '            </el-form-item>\n' +
    '            <el-form-item prop="sysAttachmentList" label="附件：">\n' +
    '                <e-pw-upload-file v-model="enterpriseForm.sysAttachmentList"  @change="handleChangeFiles"\n' +
    '                               action="/ftp/ueditorUpload/uploadPwTemp?folder=pwEnter"\n' +
    '                               :upload-file-vars="{}" tip="上传企业营业执照等证明文件"></e-pw-upload-file>\n' +
    '            </el-form-item>\n' +
    '        </el-form>',
    props: {
        appType: String,
        enterType: String,
        isAdmin: Boolean
    },
    mixins: [Vue.pwApplyRules],
    componentName: 'EnterpriseForm',
    data: function () {
        var validateEnterpriseName = this.validateEnterpriseName;
        var validateEnterpriseNo = this.validateEnterpriseNo;
        var validateRegMoney = this.validateRegMoney;
        return {
            enterpriseForm: {
                id: '',
                name: '',
                no: '',
                regMoney: '',
                regMtypes: [],
                address: '',
                remarks: '',
                sysAttachmentList: []
            },
            enterpriseRules: {
                name: [
                    {required: true, message: '请输入企业名称', trigger: 'blur'},
                    {min: 3, max: 100, message: '请输入3至100位间字符', trigger: 'blur'},
                    {validator: validateEnterpriseName, trigger: 'blur'}
                ],
                no: [
                    {required: true, message: '请输入工商注册号', trigger: 'blur'},
                    {min: 13, max: 18, message: '请输入13至18位间字符', trigger: 'blur'},
                    {validator: validateEnterpriseNo, trigger: 'blur'}
                ],
                regMoney: [
                    {required: true, message: '请输入注册资金', trigger: 'blur'},
                    {max: 10, message: '请输入不超过10位字符', trigger: 'blur'},
                    {validator: validateRegMoney, trigger: 'blur'}
                ],
                regMtypes: [
                    {required: true, message: '请选择资金来源', trigger: 'change'}
                ],
                address: [
                    {required: true, message: '请输入注册地址', trigger: 'blur'},
                    {min: 2, max: 150, message: '请输入2至150位字符', trigger: 'blur'},
                ],
                remarks: [
                    {required: true, message: '请输入公司主营业务', trigger: 'blur'},
                    {max: 300, message: '请输入不大于300位字符', trigger: 'blur'},
                ],
                sysAttachmentList: [
                    {required: true, message: '请上传附件', trigger: 'change'},
                ]
            },
            regMtypes: [],
            disabled: false,
        }
    },
    computed: {
        isDisabled: function () {
            return this.isGroup ? (this._enterFormGroup.disabled || this.disabled) : this.disabled
        },
        isGroup: function () {
            var $parent = this.$parent;
            while ($parent) {
                if ($parent.$options.componentName !== 'EnterFormGroup') {
                    $parent = $parent.$parent;
                } else {
                    this._enterFormGroup = $parent;
                    return true;
                }
            }
            return false;
        },
        pwEnterId: function () {
            return this._enterFormGroup.pwEnterId;
        },
        isDisabledName: function () {
            if (this.isAdmin) {
                return false;
            }
            return this.pwEnterId && this.appType === '2' && this.enterType === '2'
        }
    },
    methods: {
        getRegMtypes: function () {
            var self = this;
            this.$axios.get('/sys/dict/getDictList?type=pw_reg_mtype').then(function (response) {
                self.regMtypes = response.data || [];
            })
        },
        handleChangeFiles: function () {
            this.$refs.enterpriseForm.validateField('sysAttachmentList')
        },
        getPwEnterCompany: function () {
            var self = this;
            this.$axios.get('/pw/pwEnter/ajaxFindPwEnterCompany?pwEnterId=' + this.pwEnterId).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    var pageData = data.data || {};
                    if (pageData.pwCompany) {
                        var pwCompany = pageData.pwCompany;
                        pwCompany.regMoney = pwCompany.regMoney.toString();
                        pwCompany.sysAttachmentList = pwCompany.fileInfo || [];
                        delete  pwCompany.fileInfo;
                        delete pwCompany.regMtype;
                        Object.assign(self.enterpriseForm, pwCompany);
                    }
                    return false;
                }
                self.$message.error(data.msg)
            }).catch(function (error) {
                self.$message.error(self.xhrErrorMsg)
            })
        }
    },
    created: function () {
        this.getRegMtypes();

    },
    mounted: function () {
        if (this._enterFormGroup) {
            this._enterFormGroup.updateFormGroups();
        }
        if (this.pwEnterId) {
            this.getPwEnterCompany();
        }
    },
    destroyed: function () {
        this._enterFormGroup.updateFormGroups();
    }
});

Vue.component('link-project-form', {
    template: '<div class="pw-apply-project">\n' +
    '            <el-collapse-transition v-if="renderForm"><div v-show="collapseShow"><div class="table-container mgb-20" style="padding-top: 20px;padding-right: 20px;"><el-form :model="linkProjectForm" ref="linkProjectForm" :rules="linkProjectRules" size="mini" :disabled="isDisabled"\n' +
    '                     label-width="110px" autocomplete="off">\n' +
    '                <el-row>\n' +
    '                    <el-col :span="12">\n' +
    '                        <el-form-item prop="id" label="关联项目：">\n' +
    '                            <el-select v-model="linkProjectForm.id" :disabled="!!editId" name="id" filterable @change="handleChangeLinkProject">\n' +
    '                                <el-option value="-1" label="无"></el-option>\n' +
    '                                <el-option v-for="item in tempProjectMineIds" :key="item.id" :label="item.name" :disabled="projectListNames.indexOf(item.name) > -1"\n' +
    '                                           :value="item.id"></el-option>\n' +
    '                            </el-select>\n' +
    '                        </el-form-item>\n' +
    '                    </el-col>\n' +
    '                    <el-col :span="12">\n' +
    '                        <el-form-item prop="name" label="项目名称：">\n' +
    '                            <el-input name="name" v-model="linkProjectForm.name"></el-input>\n' +
    '                        </el-form-item>\n' +
    '                    </el-col>\n' +
    '                </el-row>\n' +
    '                <el-form-item prop="remarks" label="项目简介：">\n' +
    '                    <el-input name="remarks"  type="textarea" :rows="4" v-model="linkProjectForm.remarks"></el-input>\n' +
    '                </el-form-item>\n' +
    '                <el-form-item prop="attachMentEntity" label="附件：">\n' +
    '                    <e-pw-upload-file v-model="linkProjectForm.attachMentEntity"  @change="changeLinkProjectFile"\n' +
    '                                   action="/ftp/ueditorUpload/uploadPwTemp?folder=pwEnter"\n' +
    '                                   :upload-file-vars="{}"></e-pw-upload-file>\n' +
    '                </el-form-item><el-form-item class="text-right"><el-button type="primary" :disabled="isFileUpdating" @click.stop.prevent="addProjectToList">保存</el-button><el-button @click.stop.prevent="cancel">取消</el-button></el-form-item>\n' +
    '            </el-form></div></div></el-collapse-transition>\n' +
    '            <div class="mgb-20 text-right"><el-button class="btn-add" type="primary" :disabled="isDisabled" size="mini" @click.stop.prevent="openLinkProjectForm">创建项目\n' +
    '                </el-button></div><e-col-item label-width="110px" label="项目列表：">\n' +
    '            <div class="table-container"><el-table :data="projectList" border size="mini" class="table">\n' +
    '                <el-table-column prop="name" label="项目名称" width="300">\n' +
    '                    <template slot-scope="scope">\n' +
    '                        <el-tooltip class="item" effect="dark" :content="scope.row.name" popper-class="white"\n' +
    '                                    placement="right">\n' +
    '                            <span>{{scope.row.name | textEllipsis(20)}}</span>\n' +
    '                        </el-tooltip>\n' +
    '                    </template>\n' +
    '                </el-table-column>\n' +
    '                <el-table-column label="项目简介">\n' +
    '                    <template slot-scope="scope">\n' +
    '                        {{scope.row.remarks | textEllipsis(80)}}\n' +
    '                    </template>\n' +
    '                </el-table-column>\n' +
    '                <el-table-column label="附件" align="left">\n' +
    '                    <template slot-scope="scope"> <e-file-item v-for="item in scope.row.attachMentEntity" :key="item.uid" :file="item" size="mini" :show="false"></e-file-item></template>\n' +
    '                </el-table-column>\n' +
    '                <el-table-column label="操作" align="center" width="128">\n' +
    '                    <template slot-scope="scope"><el-button  v-if="isAdmin || scope.row.status == \'0\'" :disabled="isDisabled" icon="el-icon-edit" size="mini" @click.stop.prevent="editProject(scope.row, scope.$index)"></el-button>\n' +
    '                        <el-button :disabled="isDisabled  || (scope.row.status && scope.row.status !== \'0\') && !isAdmin" icon="el-icon-delete" size="mini"\n' +
    '                                   @click.stop.prevent="projectList.splice(scope.row.index, 1)"></el-button>\n' +
    '                    </template>\n' +
    '                </el-table-column>\n' +
    '            </el-table></div>\n' +
    '            </e-col-item>\n' +
    '        </div>',
    mixins: [Vue.pwApplyRules],
    componentName: 'LinkProjectForm',
    props: {
        isAdmin: Boolean,
        status: String
    },
    data: function () {
        var self = this;
        var validateNames = function (rule, value, callback) {
            if (value) {
                if (self.specificSymbol.test(value)) {
                    return callback(new Error("请不要输入特殊符号"))
                }else if(self.editId){
                    return callback();
                } else if (self.projectListNames.indexOf(value) > -1 && self.projectListIds.indexOf(self.linkProjectForm.id) === -1) {
                    return callback(new Error("项目名称已经存在"))
                } else {
                    return self.$axios.post('/pw/pwEnter/checkPwEnterProject', {name: value}).then(function (response) {
                        if (response.data) {
                            return callback();
                        }
                        return callback(new Error("项目名称已经存在"))
                    }).catch(function (error) {
                        return callback(new Error("请求失败"))
                    })
                }
            }
            return callback();
        };
        return {
            linkProjectForm: {
                id: '-1',
                // eid: '-1',
                name: '',
                remarks: '',
                attachMentEntity: []
            },
            linkProjectRules: {
                name: [
                    {required: true, message: '请输入项目名称', trigger: 'blur'},
                    {max: 128, message: '请输入不大于128位字符', trigger: 'blur'},
                    {validator: validateNames, trigger: 'blur'}
                ],
                remarks: [
                    {required: true, message: '请输入项目简介', trigger: 'blur'},
                    {max: 2000, message: '请输入不大于2000位字符', trigger: 'blur'},
                ],
                attachMentEntity: [
                    {required: true, message: '请上传附件', trigger: 'change'},
                ]
            },
            projectList: [],
            linkProjectPropDisabled: false,
            linkProjectDisabled: false,
            projectMines: [],
            editId: '',
            editIndex: -1,
            disabledStatus: ['0', ''],
            collapseShow: false,
            renderForm: false
        }
    },
    computed: {
        isFileUpdating: function () {
            return this.linkProjectForm.attachMentEntity.length > 0 && this.linkProjectForm.attachMentEntity.some(function (item) {
                    return !item.ftpUrl;
                })
        },
        tempProjectMineIds: function () {
            var projectMines = JSON.parse(JSON.stringify(this.projectMines));
            if (this.editId) {
                projectMines.push(this.projectList[this.editIndex]);
            }
            return projectMines;
        },

        projectMineIds: function () {
            return this.projectMines.map(function (item) {
                return item.id
            })
        },

        projectListNames: function () {
            return this.projectList.map(function (item) {
                return item.name
            })
        },

        projectListIds: function () {
            return this.projectList.map(function (item) {
                return item.id
            })
        },

        isDisabled: function () {
            return this.isGroup ? (this._enterFormGroup.disabled || this.linkProjectDisabled) : this.disabled
        },
        isGroup: function () {
            var $parent = this.$parent;
            while ($parent) {
                if ($parent.$options.componentName !== 'EnterFormGroup') {
                    $parent = $parent.$parent;
                } else {
                    this._enterFormGroup = $parent;
                    return true;
                }
            }
            return false;
        },
        pwEnterId: function () {
            return this._enterFormGroup.pwEnterId;
        },
    },
    methods: {

        cancel: function () {
            this.$refs.linkProjectForm.resetFields();
            this.editId = '';
            this.collapseShow = false;
        },

        openLinkProjectForm: function () {
            this.openLinkProPromise();
        },

        openLinkProPromise: function () {
            var self = this;
            return new Promise(function (resolve, reject) {
                if (!self.renderForm) {
                    self.renderForm = true;
                    self.$nextTick(function () {
                        self.collapseShow = true;
                        self.$nextTick(function () {
                            resolve()
                        })
                    })
                } else {
                    self.collapseShow = true;
                    self.$nextTick(function () {
                        resolve()
                    })
                }
            })
        },


        getProject: function (value) {
            var self = this;
            var projectMine = this.getProjectMine(value);
            Object.assign(self.linkProjectForm, projectMine);
        },

        getProjectMine: function (value) {
            for (var i = 0; i < this.projectMines.length; i++) {
                var item = this.projectMines[i];
                if (item.id === value) {
                    return JSON.parse(JSON.stringify(item));
                }
            }
            return false;
        },

        getProjectList: function () {
            var self = this;
            this.$axios.get('/pw/pwEnter/ajaxFindPwEnterProjects?pwEnterId=' + this.pwEnterId).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    var pageData = data.data || {};
                    var pwProjectList = pageData.pwProjectList || [];
                    var status = self.status;
                    self.projectList = pwProjectList.map(function (item) {
                        return {
                            id: item.id,
                            // eid: item.id,
                            status: status,
                            name: item.name,
                            remarks: item.remarks,
                            attachMentEntity: item.fileInfo || []
                        }
                    });
                    return false;
                }
                self.$message.error(data.msg)
            }).catch(function (error) {
                self.$message.error(self.xhrErrorMsg)
            })
        },

        getProjectMines: function () {
            var self = this;
            var url = '/pw/pwEnter/ajaxGetProjects';
            if (this.isAdmin) {
                url += ('?pwEnterId=' + this.pwEnterId);
            }
            this.$axios.get(url).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    var projectMines = data.data || [];
                    projectMines = projectMines.map(function (item) {
                        var attachMentEntity = item.fileInfo || [];
                        attachMentEntity = attachMentEntity.map(function (item) {
                            item.remotePath = '';
                            return item;
                        });
                        return {
                            id: item.id,
                            // eid: item.id,
                            name: item.pName,
                            remarks: item.introduction,
                            attachMentEntity: attachMentEntity
                        }
                    })
                    self.projectMines = projectMines;
                }
            })
        },

        handleChangeLinkProject: function (value) {
            this.linkProjectPropDisabled = value !== '-1';
            if (this.linkProjectPropDisabled) {
                this.getProject(value);
                this.$refs.linkProjectForm.clearValidate();
            } else {
                this.$refs.linkProjectForm.resetFields();
            }

        },

        addProjectToList: function () {
            var self = this;
            var ftpHttp = this.ftpHttp;
            this.$refs.linkProjectForm.validate(function (valid) {
                if (valid) {
                    if (self.editId) {
                        var linkProjectForm = JSON.parse(JSON.stringify(self.linkProjectForm));
                        var attachMentEntity = linkProjectForm.attachMentEntity;
                        linkProjectForm.attachMentEntity = attachMentEntity.map(function (item) {
                            return {
                                fileName: item.name,
                                name: item.name,
                                "suffix": item.suffix,
                                "uid": item.response ? '' : item.uid,
                                url: item.url,
                                ftpUrl: item.ftpUrl,
                                fielSize: item.size,
                                tempUrl: item.tempUrl,
                                tempFtpUrl: item.tempFtpUrl,
                                viewUrl: item.viewUrl || (['jpg', 'jpeg', 'png', 'JPG', 'PDF'].indexOf(item.type) > -1 ? (ftpHttp + item.ftpUrl.replace('/tool', '')) : '')
                            }
                        });
                        self.projectList.splice(self.editIndex, 1, linkProjectForm);
                        self.$refs.linkProjectForm.resetFields();
                        self.editId = '';
                        self.editIndex = -1;
                    } else {
                        self.postProject();
                    }
                }
            })
        },


        postProject: function () {
            this.linkProjectDisabled = true;
            var linkProjectForm = JSON.parse(JSON.stringify(this.linkProjectForm));
            var attachMentEntity = linkProjectForm.attachMentEntity;
            var ftpHttp = this.ftpHttp;
            linkProjectForm.attachMentEntity = attachMentEntity.map(function (item) {
                return {
                    fileName: item.name,
                    name: item.name,
                    "suffix": item.suffix,
                    "uid": item.response ? '' : item.uid,
                    url: item.url,
                    ftpUrl: item.ftpUrl,
                    fielSize: item.size,
                    tempUrl: item.tempUrl,
                    tempFtpUrl: item.tempFtpUrl,
                    viewUrl: ['jpg', 'jpeg', 'png', 'JPG', 'PDF'].indexOf(item.type) > -1 ? (ftpHttp + item.ftpUrl.replace('/tool', '')) : '',
                    // fielTitle: item.name,
                    // fielType: item.type,
                    // fielFtpUrl: item.ftpUrl,
                    // fileTypeEnum: '13',
                    // fileStepEnum: '1301',
                    // gnodeId: ''
                }
            })
            // linkProjectForm.eid = (this.projectList.length + 1) + 'temp';
            linkProjectForm.id = (this.projectList.length + 1) + 'temp';
            this.projectList.push(linkProjectForm);
            this.linkProjectDisabled = false;
            this.linkProjectPropDisabled = false;
            this.$refs.linkProjectForm.resetFields();
        },

        changeLinkProjectFile: function () {
            this.$refs.linkProjectForm.validateField('attachMentEntity');
        },

        editProject: function (row, $index) {
            var self = this;
            this.openLinkProPromise().then(function () {
                self.editId = row.id;
                self.editIndex = $index;
                Object.assign(self.linkProjectForm, row);
            })
        }
    },
    created: function () {
        // if(!this.isAdmin){
        //     this.renderForm = true;
        //     this.collapseShow = true;
        // }
    },
    mounted: function () {
        if (this._enterFormGroup) {
            this._enterFormGroup.updateFormGroups();
        }
        this.getProjectMines();
        if (this.pwEnterId) {
            this.getProjectList();
        }
    }
})


Vue.component('link-team-form', {
    template: '<div class="link-team-form"><el-form :model="linkTeamForm" ref="linkTeamForm" :rules="linkTeamRules" size="mini" label-width="110px" :disabled="isDisabled">' +
    '<template v-if="!isAdmin"><el-form-item  prop="teamId" label="选择团队：">' +
    '<el-select v-model="linkTeamForm.teamId" clearable @change="handleChangeTeamId">' +
    '<el-option v-for="item in teamList" :key="item.id" :value="item.id" :label="item.name"></el-option>' +
    '</el-select><template v-if="teamList.length === 0" style="display: none"><span class="el-form-item-expository">暂无团队，请去<a :href="ctxFront + \'/team/indexMyTeamList\'">创建团队</a>吧</span></template></el-form-item></template>' +
    '<template v-else><el-form-item label="团队名称：">{{linkTeamForm.name}}</el-form-item><el-form-item label="团队介绍：">{{linkTeamForm.summary}}</el-form-item></template></el-form>' +
    '<div v-if="isAdmin || isRenderMember"><update-member :is-admin="isAdmin" :declare-id="declareId"  v-show="linkTeamForm.teamId" :disabled="isDisabled" :team-id="linkTeamForm.teamId" :saved-team-url="savedTeamUrl" @change="handleChangeTeam"></update-member></div></div>',
    props: {
        isAdmin: Boolean
    },
    data: function () {
        var self = this;
        var validateTeam = function (rules, value, callback) {
            if (value) {
                var teamId = self.linkTeamForm.teamId;
                return self.$axios.post('/pw/pwEnter/checkPwEnterTeam', {
                    id: self.pwEnterId,
                    team: {
                        id: teamId
                    }
                }).then(function (response) {
                    var data = response.data;
                    if (data) {
                        return callback()
                    }
                    return callback(new Error("当前团队已提交入驻申请，请重新选择"))
                }).catch(function (error) {
                    return callback(new Error("请求失败"));
                })
            }
            return callback();
        }
        return {
            linkTeamForm: {
                teamId: '',
                name: '',
                summary: '',
                sponsor: ''
            },
            linkTeamRules: {
                teamId: [
                    {required: true, message: '请选择团队', trigger: 'change'},
                    {validator: validateTeam, trigger: 'change'}
                ]
            },
            teamList: [],
            teamData: {},
            savedTeamUrl: '',
            isRenderMember: false
        }
    },
    componentName: 'LinkTeamForm',
    computed: {
        // savedTeamUrl: function () {
        //     if (this.pwEnterId && this.linkTeamForm.teamId) {
        //         return '/pw/pwEnter/ajaxFindPwEnterTeam?pwEnterId=' + this.pwEnterId;
        //     }
        //     return false;
        // },
        isDisabled: function () {
            return this.isGroup ? (this._enterFormGroup.disabled || this.linkProjectDisabled) : this.disabled
        },
        isGroup: function () {
            var $parent = this.$parent;
            while ($parent) {
                if ($parent.$options.componentName !== 'EnterFormGroup') {
                    $parent = $parent.$parent;
                } else {
                    this._enterFormGroup = $parent;
                    return true;
                }
            }
            return false;
        },
        pwEnterId: function () {
            return this._enterFormGroup.pwEnterId;
        },
        declareId: function () {
            if (this.linkTeamForm.sponsor) {
                return this.linkTeamForm.sponsor;
            }
            if (!this.linkTeamForm.teamId) {
                return '';
            }
            for (var i = 0; i < this.teamList.length; i++) {
                var team = this.teamList[i];
                if (team.id === this.linkTeamForm.teamId) {
                    return team.sponsor;
                }
            }
            return '';
        }
    },
    methods: {


        handleChangeTeamId: function (value) {
            this.teamData = {};
            if(value){
                this.isRenderMember = true;
            }
        },

        getTeamList: function () {
            var self = this;
            this.$axios.get('/pw/pwEnter/ajaxGetTeams').then(function (response) {
                var data = response.data;
                self.teamList = data.data || []

            })
        },


        getPwEnterTeam: function () {
            var self = this;
            this.$axios.get('/pw/pwEnter/ajaxFindPwEnterTeam?pwEnterId=' + this.pwEnterId).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    if(data.data){
                        var team = data.data.team || {};
                        self.linkTeamForm.teamId = team.id;
                        self.linkTeamForm.name = team.name;
                        self.linkTeamForm.summary = team.summary;
                        self.linkTeamForm.sponsor = team.sponsor;
                        self.savedTeamUrl = '/pw/pwEnter/ajaxFindPwEnterTeam?pwEnterId=' + self.pwEnterId;
                        self.isRenderMember = true;
                    }
                }else {
                    self.$message.error(data.msg)
                }
            }).catch(function (error) {
                self.$message.error(self.xhrErrorMsg);
            })
        },

        handleChangeTeam: function (data) {
            this.teamData = data;
        }
    },
    mounted: function () {
        if (this._enterFormGroup) {
            this._enterFormGroup.updateFormGroups();
        }
        if (this.pwEnterId) {
            this.getPwEnterTeam();
        }
        if (!this.isAdmin) {
            this.getTeamList();
        }
    }

})


Vue.component('pw-site-form', {
    template: '<el-form :model="pwSiteForm" ref="pwSiteForm" :disabled="isDisabled" :rules="pwSiteRules" size="mini" autocomplete="off" label-width="120px">\n' +
    '            <el-form-item prop="expectWorkNum" label="是否需要工位：">\n' +
    '                <el-col :span="15">\n' +
    '                    <el-input type="number" :disabled="!pwSiteForm.isShowWorkNum" v-model.number="pwSiteForm.expectWorkNum">\n' +
    '                        <template slot="append">位</template>\n' +
    '                    </el-input>\n' +
    '                </el-col>\n' +
    '                <el-col :span="2">\n' +
    '                    <div style="min-height: 1px"></div>\n' +
    '                </el-col>\n' +
    '                <el-col :span="7">\n' +
    '                    <el-checkbox v-model="pwSiteForm.isShowWorkNum" @change="handleChangeHasExpectWorkNum" true-label="1" false-label="0">是</el-checkbox>\n' +
    '                </el-col>\n' +
    '            </el-form-item>\n' +
    '            <el-form-item v-if="hasExpectTerm" prop="expectTerm" label="预计孵化期：">\n' +
    '               <el-select size="mini" v-model="pwSiteForm.termDate" placeholder="变更预计孵化期" clearable style="width: 170px;" @change="handleChangeTermDate">\n' +
    '                   <el-option\n' +
    '                           v-for="item in yearOptions"\n' +
    '                           :key="item.num"\n' +
    '                           :label="item.remarks"\n' +
    '                           :value="item.num">\n' +
    '                   </el-option>\n' +
    '               </el-select>\n' +
    '            </el-form-item>\n' +
    '            <el-form-item prop="termDefinedDate" v-if="hasExpectTerm && pwSiteForm.termDate == \'-1\'">\n' +
    '               <el-date-picker clearable style="width: 170px;"\n' +
    '                       v-model="pwSiteForm.termDefinedDate"\n' +
    '                       @change="handleChangeTermDefinedDate"\n' +
    '                       type="date"\n' +
    '                       :default-value="sysDateAfter"\n' +
    '                       :picker-options="pickerOptions"\n' +
    '                       placeholder="选择日期">\n' +
    '               </el-date-picker>\n' +
    '           </el-form-item>\n' +
    '            <el-form-item prop="expectRemark" label="备注：">\n' +
    '                <el-input :rows="3" type="textarea" v-model="pwSiteForm.expectRemark"></el-input>\n' +
    '            </el-form-item>\n' +
    '        </el-form>',
    componentName: 'PwSiteForm',
    props: {
        isAdmin: Boolean,
        appType: String
    },
    computed: {

        hasExpectTerm: function () {
            if (this.isAdmin) {
                return true;
            }
            return this.appType !== '2'
        },

        expectTermLabel: function () {
            var label = this.pwSiteForm.expectTerm == -1 ? '' : this.pwSiteForm.expectTerm;
            return !!label ? label + '天' : '？天';
        },

        isDisabled: function () {
            return this.isGroup ? (this._enterFormGroup.disabled || this.linkProjectDisabled) : this.disabled
        },
        isGroup: function () {
            var $parent = this.$parent;
            while ($parent) {
                if ($parent.$options.componentName !== 'EnterFormGroup') {
                    $parent = $parent.$parent;
                } else {
                    this._enterFormGroup = $parent;
                    return true;
                }
            }
            return false;
        },
        pwEnterId: function () {
            return this._enterFormGroup.pwEnterId;
        },
        pwSiteRules: function () {
            var self = this;
            var validateExpectWorkNum = function (rules, value, callback) {
                if (typeof value !== 'undefined' && value !== '') {
                    if (value > 0 && value <= 20000 && !(/\./.test(value))) {
                        return callback();
                    }
                    return callback(new Error("请输入不小于0不大于20000的正整数"))
                }
                return callback();
            };
            var isShowWorkNum = this.pwSiteForm.isShowWorkNum;
            return {
                expectWorkNum: [
                    {required: isShowWorkNum === '1', message: '请输入需要工位数', trigger: 'blur'},
                    {validator: validateExpectWorkNum, trigger: 'blur'}
                ],
                expectTerm: [
                    {required: true, message: '预计孵化期不能为空', trigger: 'change'}
                ],
                termDefinedDate: [
                    {required: true, message: '请选择自定义时间', trigger: 'change'}
                ],
                expectRemark: [
                    {max: 125, message: '请输入不大于125个字符', trigger: 'change'}
                ]
            }
        },
        sysDateAfter: function () {
            var date = new Date();
            if (this.sysDate) {
                date = new Date(this.sysDate);
                date.setDate(date.getDate() + 1);
            }
            return moment(date).format('YYYY-MM-DD');
        },
        pickerOptions: {
            get: function () {
                var self = this;
                return {
                    disabledDate: function (time) {
                        return time.getTime() < new Date(self.sysDate);
                    }
                };
            }
        }
    },
    data: function () {
        return {
            pwSiteForm: {
                expectWorkNum: '',
                termDate: '',
                termDefinedDate: '',
                expectTerm: '',
                expectRemark: '',
                isShowWorkNum: '1'
            },
            pwEnter: {},
            yearOptions: [],
            sysDate: ''
        }
    },
    methods: {
        handleChangeHasExpectWorkNum: function (value) {
            if (value === '0') {
                this.pwSiteForm.expectWorkNum = '';
            }
            this.$refs.pwSiteForm.validateField('expectWorkNum')
        },
        getPwEnterSpace: function () {
            var self = this;
            this.$axios.get('/pw/pwEnter/ajaxFindPwEnterPwSpace?pwEnterId=' + this.pwEnterId).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    var pwEnter = data.data || {};
                    self.pwSiteForm = {
                        expectWorkNum: pwEnter.expectWorkNum,
                        expectTerm: pwEnter.expectTerm,
                        expectRemark: pwEnter.expectRemark,
                        isShowWorkNum: pwEnter.isShowWorkNum
                    };
                    self.pwEnter = pwEnter;
                }
            }).catch(function () {
                self.$message.error(self.xhrErrorMsg);
            })
        },
        handleChangeTermDate: function (value) {
            this.pwSiteForm.expectTerm = value;
            if(!value){
                this.pwSiteForm.expectTerm = '';
            }
            if(this.pwSiteForm.termDefinedDate){
                this.pwSiteForm.termDefinedDate = '';
            }
        },
        handleChangeTermDefinedDate: function (value) {
            if(!value){
                this.pwSiteForm.expectTerm = '';
                return false;
            }
            this.pwSiteForm.expectTerm = Math.ceil((new Date(this.pwSiteForm.termDefinedDate).getTime() - new Date(this.sysDate).getTime()) / 24 / 3600 / 1000).toString();
        },
        getSysDate: function () {
            var self = this;
            this.$axios.get('/sys/sysCurDateYmdHms').then(function (response) {
                self.sysDate = moment(response.data).format('YYYY-MM-DD');
                self.getYearOptions();
            })
        },
        getYearOptions: function () {
            var self = this;
            this.$axios.get('/pw/pwEnter/ajaxDtypeTerms?isAll=false').then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.yearOptions = JSON.parse(data.data) || [];
                    self.yearOptions.push({remarks: '自定义', num: '-1'});
                }
            })
        }
    },
    mounted: function () {
        this.getSysDate();
        if (this.pwEnterId) {
            this.getPwEnterSpace();
        }
        if (this._enterFormGroup && this.isAdmin) {
            this._enterFormGroup.updateFormGroups();
        }
    }
});