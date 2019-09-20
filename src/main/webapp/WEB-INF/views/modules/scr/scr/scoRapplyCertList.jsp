<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>


</head>
<body>
<div id="app" v-show="pageLoad" class="container-fluid mgb-60" style="display: none">
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>
    <div class="text-right mgb-20">
        <el-button size="mini" type="primary" @click.stop="addCert">添加证书</el-button>
        <el-button size="mini" type="primary" @click.stop="saveSort">保存排序</el-button>
    </div>
    <div v-loading="loading" class="table-container">
        <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
               cellspacing="0" cellpadding="0">
            <thead>
            <tr>
                <th width="20%"><span class="e-table-tree-dot"></span>名称</th>
                <th width="20%" class="text-center">排序</th>
                <th>备注</th>
                <th width="180" class="text-center">操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(item, index) in certTreeList" v-show="!item.isCollapsed">
                <td style="white-space: nowrap">
                        <span class="e-table-tree-dot"
                              v-for="(dot, index1) in item.dots.split('-')"></span>
                    <i class="el-icon-caret-right" :class="elIconCaret(item)"
                       @click.stop.prevent="handleExpandCell(item)"></i>
                    <el-tooltip class="item" popper-class="white" effect="dark"
                                :content="item.name"
                                placement="right"><span class="break-ellipsis">{{item.name}}</span></el-tooltip>
                </td>
                <td class="text-center">
                    <div v-if="item.sort" class="el-form-item-credit" :class="{'is-error': !numReg.test(item.sort)}">
                        <el-input size="mini" v-model="item.sort" maxlength="10" style="width: 120px"></el-input>
                        <%--{{numReg.test(item.sort)}}--%>
                    </div>
                    <div v-else>
                        <el-input size="mini" v-model.number="item.sort" maxlength="10" style="width: 120px"></el-input>
                    </div>
                </td>
                <td>
                    {{item.remarks}}
                </td>
                <td class="text-center">
                    <div class="table-btns-action">
                        <el-button type="text" v-if="item.parentId === '0'" size="mini"
                                   @click.stop="addChildCert(item)">添加下级证书
                        </el-button>
                        <el-button type="text" size="mini" @click.stop="editCert(item)">编辑</el-button>
                        <el-button type="text" size="mini" @click.stop="confirmDelCert(item)">删除</el-button>
                    </div>
                </td>
            </tr>
            <tr v-if="certTreeList.length == 0" style="text-align: center"><td colspan="4">暂无数据</td></tr>
            </tbody>
        </table>
    </div>
    <el-dialog
            :title="dialogTitle"
            :visible.sync="dialogVisibleCert"
            width="520px"
            :close-on-click-modal="false"
            :before-close="handleCloseCert">
        <el-form :model="certForm" ref="certForm" :rules="certRules" :disabled="disabled" size="mini"
                 label-width="120px">
            <el-form-item prop="name" label="名称：">
                <el-input v-model="certForm.name" maxlength="64"></el-input>
            </el-form-item>
            <el-form-item prop="sort" label="排序：">
                <el-input v-model.number="certForm.sort" maxlength="10"></el-input>
            </el-form-item>
            <el-form-item prop="sysAttachmentList" label="上传证书图片：">
                <e-pw-upload-file v-model="certForm.sysAttachmentList" tip=""
                                  accept="image/jpg, image/jpeg, image/png"
                                  @change="handleChangeSysAttachmentList"
                                  action="/ftp/ueditorUpload/uploadPwTemp?folder=scr"></e-pw-upload-file>
            </el-form-item>
            <el-form-item prop="remarks" label="备注：">
                <el-input type="textarea" :rows="3" v-model="certForm.remarks" maxlength="2000"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button size="mini" :disabled="disabled" @click="handleCloseCert">取 消</el-button>
    <el-button size="mini" :disabled="disabled" type="primary" @click="validateCertForm">确 定</el-button>
  </span>
    </el-dialog>
</div>
<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var self = this;
            var validateName = function (rules, value, callback) {
                var certForm = self.certForm;
                if (value) {
                    return self.$axios.post('/scr/scoRapplyCert/checkScoRaCertName', {
                        id: certForm.id,
                        name: certForm.name
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status === 1) {
                            if (data.data) {
                                return callback();
                            }
                            return callback(new Error("证书名称重复"));
                        }
                    }).catch(function () {
                        return callback(new Error(self.xhrErrorMsg))
                    })
                }
                return callback();
            };
            var validateSort = function (rules, value, callback) {
                if (value) {
                    if (/^[1-9]{1}\d*$/.test(value.toString())) {
                        return callback()
                    }
                    return callback(new Error('请输入数字'))
                }
                return callback()
            };
            return {
                loading: false,
                certTree: [],
                dialogVisibleCert: false,
                disabled: false,
                certTreeList: [],
                certForm: {
                    id: '',
                    parent: {
                        id: ''
                    },
                    name: '',
                    sort: '',
                    sysAttachmentList: [],
                    remarks: ''
                },
                certRules: {
                    name: [
                        {required: true, message: '请输入证书名称', trigger: 'blur'},
                        {validator: validateName, trigger: 'blur'}
                    ],
                    sort: [
                        {validator: validateSort, trigger: 'blur'}
                    ],
//                    sysAttachmentList: [
//                        {required: true, message: '请上传证书图片', trigger: 'change'}
//                    ]
                },
                numReg: /^\d*$/,
                dialogTitle:'证书'
            }
        },
        methods: {
            saveSort: function () {
                var numReg = this.numReg;
                var validSorts = this.certTreeList.every(function (item) {
                    if (item.sort != '' && typeof item.sort !== 'undefined') {
                        return numReg.test(item.sort.toString())
                    }
                    return true;
                });
                if (!validSorts) {
                    this.$message.error('排序必须为正整数或空，请检查排序是否正确');
                    return false;
                }
                var sorts = this.certTreeList.map(function (item) {
                    return {
                        id: item.id,
                        sort: item.sort
                    }
                });
                this.submitSorts(sorts)
            },

            submitSorts: function (sorts) {
                var self = this;
                this.$axios.post('/scr/scoRapplyCert/ajaxUpdateSort', {
                    scoRapplyCertList: sorts
                }).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getCertList();
                        self.$message.success('保存成功')
                    } else {
                        self.$message.error(data.msg)
                    }
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg)
                })
            },

            handleChangeSysAttachmentList: function () {
                this.$refs.certForm.validateField('sysAttachmentList');
            },

            elIconCaret: function (row) {
                return {
                    'is-leaf': !row.children || !row.children.length,
                    'expand-icon': row.isExpand
                }
            },

            //控制行的展开收起
            handleExpandCell: function (row) {
                var children = row.children;
                if (!children) {
                    return;
                }
                row.isExpand = !row.isExpand;
                if (row.isExpand) {
                    this.expandCellTrue(children, row.isExpand);
                    return
                }
                this.expandCellFalse(children, row.isExpand);
            },

            expandCellTrue: function (list, b) {
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    item.isCollapsed = !b;
                    item.isExpand = false;
                }
            },

            expandCellFalse: function (list, b) {
                var childrenIds = this.getCreditRuleChildrenIds(list);
                for (var i = 0; i < this.certTreeList.length; i++) {
                    var item = this.certTreeList[i];
                    if (childrenIds.indexOf(item.id) > -1) {
                        item.isCollapsed = !b;
                        item.isExpand = false;
                    }
                }
            },

            //获取所有子的ID
            getCreditRuleChildrenIds: function (list) {
                var ids = [];

                function getIds(list) {
                    if (!list) return ids;
                    for (var i = 0; i < list.length; i++) {
                        ids.push(list[i].id);
                        getIds(list[i].children);
                    }
                }

                getIds(list);
                return ids;
            },

            addCert: function () {
                this.dialogTitle = '添加证书';
                this.certForm.parent.id = '0';
                this.certForm.id = '';
                this.dialogVisibleCert = true;
            },

            addChildCert: function (item) {
                this.dialogTitle = item.name + ' - 添加下级证书';
                this.certForm.parent.id = item.id;
                this.certForm.id = '';
                this.dialogVisibleCert = true;
            },

            editCert: function (row) {
                this.dialogTitle = '编辑';
                this.dialogVisibleCert = true;
                this.$nextTick(function () {
                    this.assignFormData(this.certForm, row);
                    this.certForm.parent.id = row.parentId;
                })
            },

            handleCloseCert: function () {
                this.certForm.id = '';
                this.$refs.certForm.resetFields();
                this.$nextTick(function () {
                    this.dialogVisibleCert = false;
                    this.dialogTitle = '证书';
                });
            },

            validateCertForm: function () {
                var self = this;
                this.$refs.certForm.validate(function (valid) {
                    if (valid) {
                        self.submitCertForm();
                    }
                })
            },

            getParams: function () {
                var sysAttachmentList = this.certForm.sysAttachmentList;
                this.certForm.sysAttachmentList = sysAttachmentList.map(function (item) {
                    return {
                        "uid": item.response ? '' : item.uid,
                        "fileName": item.title || item.name,
                        "name": item.title || item.name,
                        "suffix": item.suffix,
                        "url": item.url,
                        "ftpUrl": item.ftpUrl,
                        "fielSize": item.fielSize,
                        "tempUrl": item.tempUrl,
                        "tempFtpUrl": item.tempFtpUrl
                    }
                })
                return this.certForm;
            },

            submitCertForm: function () {
                var self = this;
                this.disabled = true;
                this.$axios.post('/scr/scoRapplyCert/ajaxAuditScoRapplyCert', this.getParams()).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$message.success('保存成功');
                        self.getCertList();
                        if(self.dialogTitle == '编辑'){
                            self.handleCloseCert();
                        }else{
                            self.certForm.id = '';
                            self.$refs.certForm.resetFields();
                        }
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg);
                    self.disabled = false;
                })
            },

            getCertTreeList: function () {
                return this.treeToList(this.certTree, 2)
            },

            getCertList: function () {
                var self = this;
                this.$axios.get('/scr/scoRapplyCert/listPage').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.certTree = data.data || [];
                        self.certTreeList = self.getCertTreeList();
                    } else {
                        self.$message.error(data.msg)
                    }
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg)
                })
            },

            confirmDelCert: function (row) {
                var self = this;
                this.$confirm("确认删除证书吗？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delCert(row)
                }).catch(function () {

                })
            },

            delCert: function (row) {
                var self = this;
                this.$axios.get('/scr/scoRapplyCert/ajaxDeleteScoRapplyCert?id=' + row.id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getCertList();
                        self.$message.success('删除成功');
                    } else {
                        self.$message.error(data.msg);
                    }
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg);
                })
            }
        },
        created: function () {
            this.getCertList();
        }
    })

</script>
</body>
</html>