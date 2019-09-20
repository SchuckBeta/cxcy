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
        <edit-bar></edit-bar>
    </div>
    <div class="mgb-20 text-right">
        <span>
        <el-button icon="el-icon-view" type="primary" size="mini" @click.stop.prevent="goToPreview">预览
        </el-button>
            </span>
        <span>
        <template v-if="showLinkType">
            <el-button v-if="linkType == '2'" icon="el-icon-edit-outline" type="primary" size="mini"
                       @click.stop.prevent="updateLinkType('1')">
                显示文字链接
            </el-button>

            <el-button v-else icon="el-icon-picture" type="primary" size="mini"
                       @click.stop.prevent="updateLinkType('2')">显示图片链接
            </el-button>
        </template>
            </span>
        <span>
        <el-button icon="el-icon-circle-plus" type="primary" size="mini" :disabled="pageCount >= maxNum"
                   @click.stop="addFriendlyLink">添加
        </el-button>
            </span>
        <span>
        <el-button icon="el-icon-sort" type="primary" size="mini" @click.stop.prevent="saveSort">保存排序</el-button>
            </span>
        <el-tooltip class="item" content="请选择需要删除的友情链接" :disabled="multipleSelectedId.length > 0"
                    popper-class="white" placement="bottom">
                        <span>
        <el-button icon="el-icon-remove" type="primary" size="mini" :disabled="multipleSelectedId.length == 0"
                   @click.stop="batchDelete">批量删除
        </el-button>
                            </span>
        </el-tooltip>
    </div>
    <div class="table-container">
        <el-table :data="friendlyLinkList" v-loading="loading" size="mini" class="table"
                  ref="multipleFriendlyLinkListTable"
                  @selection-change="handleChangeFriendlyLink" :default-sort="{prop:'sort',order:'ascending'}"
                  @sort-change="handleTableSortChange">
            <el-table-column
                    type="selection"
                    width="60">
            </el-table-column>
            <el-table-column prop="linkname" label="名称" sortable="linkname"></el-table-column>
            <el-table-column align="center" label="Logo">
                <template slot-scope="scope">
                    <div class="column-link-logo">
                        <img class="img-responsive" :src="addFtpHttp(scope.row.logo)" v-if="scope.row.logo">
                        <span v-else>-</span>
                    </div>
                </template>
            </el-table-column>
            <el-table-column align="center" label="链接">
                <template slot-scope="scope">
                    <span v-if="scope.row.sitelink">{{scope.row.sitelink}}</span>
                    <span v-else>-</span>
                </template>
            </el-table-column>
            <el-table-column prop="sort" align="center" label="排序" sortable="sort">
                <template slot-scope="scope">
                    <el-input size="mini" v-model="scope.row.sort" style="width: 70px;"></el-input>
                </template>
            </el-table-column>
            <el-table-column prop="isShow" align="center" label="显示" sortable="isShow">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.isShow" active-value="1" inactive-value="0"
                               @change="changeIsShow(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作">
                <template slot-scope="scope">
                    <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                   @click.stop.prevent="changeLineData(scope.row)"></el-button>
                    </el-tooltip>
                    <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                   @click.stop.prevent="deleteLineData(scope.row.id)"></el-button>
                    </el-tooltip>
                </template>
            </el-table-column>
        </el-table>
        <div class="text-right mgb-20" v-show="pageCount">
            <el-pagination
                    size="small"
                    @size-change="handlePaginationSizeChange"
                    background
                    @current-change="handlePaginationPageChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="searchListForm.pageSize"
                    layout="total, prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>


    <el-dialog :title="friendlyLinkFormAction + '友情链接'" :visible.sync="friendlyLinkFormVisible"
               :close-on-click-modal="isClose"
               :before-close="handleCloseFriendlyLinkForm" width="560px">
        <el-form size="mini" :model="friendlyLinkForm" :rules="friendlyLinkFormRules" ref="friendlyLinkForm"
                 :disabled="disabled"
                 label-width="100px">
            <el-form-item prop="linkname" label="链接名称：">
                <el-input v-model="friendlyLinkForm.linkname" class="w300"></el-input>
            </el-form-item>
            <el-form-item prop="sitelink" label="链接地址：">
                <el-input v-model="friendlyLinkForm.sitelink" class="w300">
                    <template slot="prepend">Http://</template>
                </el-input>
            </el-form-item>
            <el-form-item v-if="friendlyLinkForm.sitetype == '2'" prop="logo" label="Logo：">
                <input type="hidden" v-model="friendlyLinkForm.logo">
                <el-upload
                        class="friendly-link_logo-uploader"
                        action="${ctx}/ftp/ueditorUpload/uploadTemp"
                        :show-file-list="false"
                        :on-success="handleFriendlyLinkLogoSuccess"
                        :on-error="fileLogoError"
                        :before-upload="beforeFriendlyLinkLogoUpload"
                        name="upfile"
                        accept="image/jpg, image/jpeg, image/png">
                    <img v-for="item in friendlyLinkLogoList" :src="item.ftpUrl | ftpHttpFilter(ftpHttp)"
                         :key="item.uid" style="width:200px; height: auto"
                         class="friendly-link_logo">
                    <i v-if="friendlyLinkLogoList.length == 0"
                       class="el-icon-plus friendly-link_logo-uploader-icon"></i>
                </el-upload>
            </el-form-item>
            <el-form-item prop="sort" label="排序：" style="width: 220px;">
                <el-input v-model="friendlyLinkForm.sort"></el-input>
            </el-form-item>
            <el-form-item prop="isShow" label="是否显示：">
                <el-switch v-model="friendlyLinkForm.isShow" active-value="1" inactive-value="0"></el-switch>
            </el-form-item>
            <el-form-item prop="description" label="描述：">
                <el-input type="textarea" :rows="3" v-model="friendlyLinkForm.description"
                          style="width:380px;"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="disabled" @click="handleCloseFriendlyLinkForm">取 消</el-button>
            <el-button size="mini" :disabled="disabled" type="primary"
                       @click.stop.prevent="saveFriendlyLink('friendlyLinkForm')">确 定
            </el-button>
        </div>
    </el-dialog>


    <el-dialog
            title="友情链接预览"
            :visible.sync="dialogVisibleLinkPreview"
            width="600px"
            class="cms-link-preview-dialog"
            :before-close="handleCloseLinkPreview">
        <div class="friend-links">
            <div v-for="cmsLink in cmsLinkList" v-if="cmsLinkList.length < 11" class="friend-col">
                <div class="friend-link">
                    <a :href="'http://'+cmsLink.sitelink" target="_blank">
                        <template v-if="cmsLink.sitetype == '2'">
                            <img :src="cmsLink.logo | ftpHttpFilter(ftpHttp)">
                        </template>
                        <template v-else>{{cmsLink.linkname}}</template>
                    </a>
                </div>
            </div>
        </div>
    </el-dialog>

</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var self = this;
            var reg = /^[1-9][0-9]*$/;
            var validateSort = function (rule, value, callback) {
                value = value.toString();
                if (!reg.test(value)) {
                    callback(new Error('请输入正整数'));
                }
                if (self.isAdd && self.sorts.indexOf(value) > -1) {
                    callback(new Error('请输入不重复的序号'));
                } else if (!self.isAdd && value != self.changeSort && self.sorts.indexOf(value) > -1) {
                    callback(new Error('请输入不重复的序号'));
                } else {
                    callback();
                }
            };

            var validateLinkName = function (rule, value, callback) {
                if (value) {
                    var params = {
                        linkname: value,
                        id: self.friendlyLinkForm.id
                    }
                    return self.$axios.get('/cms/cmsLink/checkLinkName', {params: params}).then(function (response) {
                        if (response.data) {
                            return callback();
                        }
                        return callback(new Error("链接名称已存在"))
                    }).catch(function () {
                        return callback(new Error("请求失败"))
                    })
                }
                return callback();
            }

            return {
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
//                    sitetype: '',
                    orderByType: ''
                },
                linkType: '',
                pageCount: 0,
                typeList: [{id: '111', label: '文字链接', value: '1'}, {id: '222', label: '图片链接', value: '2'}],
                friendlyLinkList: [],
                multipleFriendlyLink: [],
                multipleSelectedId: [],
                friendlyLinkFormVisible: false,
                isAdd: false,
                friendlyLinkForm: {
                    id: '',
                    linkname: '',
                    sitelink: '',
                    sitetype: '2',
                    logo: '',
                    sort: '',
                    isShow: '0',
                    description: ''
                },
                friendlyLinkLogoList: [],
                maxNum: 0,
                friendlyLinkFormRules: {
                    linkname: [
                        {required: true, message: '请输入链接名称', trigger: 'blur'},
                        {max: 64, message: '请输入不大于64位字符', trigger: 'blur'},
                        {validator: validateLinkName, trigger: 'blur'}
                    ],
                    sitelink: [
                        {required: true, message: '请输入链接地址', trigger: 'blur'}
                    ],
                    logo: [
                        {required: true, message: '请上传图片', trigger: 'blur'}
                    ],
                    sort: [
                        {required: true, message: '请输入排序序号', trigger: 'blur'},
                        {validator: validateSort, trigger: 'blur'}
                    ]
                },
                loading: false,
                sorts: [],
                changeSort: '',
                dialogVisibleLinkPreview: false,
                cmsLinkList: [],
                isClose: false,
                disabled: false,
                showLinkType: false
            }
        },
        watch: {
            friendlyLinkLogoList: function (value) {
                this.friendlyLinkForm.logo = value.map(function (item) {
                    return item.ftpUrl
                }).join(',');
            }
        },
        computed: {
            typeEntries: {
                get: function () {
                    return this.getEntries(this.typeList)
                }
            },
            friendlyLinkFormAction: {
                get: function () {
                    return this.isAdd ? '添加' : '修改'
                }
            }
        },
        methods: {


            updateLinkType: function (type) {
                this.linkType = type;

                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'POST',
                    url: '/cms/cmsSiteconfig/siteConfigLinkSave?linkType=' + this.linkType
                }).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.getDataList();
                    }
                    self.loading = false;
                    self.$message({
                        message: data.msg || '保存成功',
                        type: data.ret == '1' ? 'success' : 'error'
                    })
                }).catch(function (error) {
                    self.loading = false;
                });
            },

            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/cms/cmsLink/linkList', {params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        self.friendlyLinkList = data.data.list || [];
                        self.maxNum = data.data.maxResults;
                        self.friendlyLinkLogoList = [];
                        self.getSorts();
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                });
            },

            getLinkType: function () {
                var self = this;
                this.showLinkType = false;
                this.$axios.get('/cms/cmsLink/cmsLinkType').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.linkType = data.data.linkType;
                    }
                    self.showLinkType = true;
                }).catch(function (error) {
                });
            },

            handleTableSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? ( row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getDataList();
            },
            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList();
            },
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList();
            },
            handleChangeFriendlyLink: function (value) {
                this.multipleFriendlyLink = value;
                this.multipleSelectedId = [];
                for (var i = 0; i < value.length; i++) {
                    this.multipleSelectedId.push(value[i].id);
                }
            },

            setFriendlyLinkLogo: function (value) {
                if (!value) {
                    return false;
                }
                this.friendlyLinkLogoList.push({ftpUrl: value});
            },

            beforeFriendlyLinkLogoUpload: function (file) {
                var isJPG = (file.type === 'image/jpeg' || file.type === 'image/png');
                var isLt2M = file.size / 1024 / 1024 < 2;
                if (!isJPG) {
                    this.$message.error('上传LOGO图片只能是 JPG或者 PNG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传LOGO图片大小不能超过 2MB!');
                }
                return isJPG && isLt2M;
            },

            fileLogoError: function (err, file, fileList) {
                if (err.state == 'error') {
                    this.$message({
                        message: '上传失败',
                        type: 'error'
                    });
                }
            },

            handleFriendlyLinkLogoSuccess: function (response, file, fileList) {
                var nfile = Object.assign(file, response);
                if (response.state === 'SUCCESS') {
                    this.friendlyLinkLogoList = fileList.slice(-1);
                } else {
                    this.$message({
                        message: '上传失败',
                        type: 'error'
                    });
                }
            },

            handleCloseFriendlyLinkForm: function () {
                this.$refs.friendlyLinkForm.resetFields();
                this.friendlyLinkForm.id = '';
                this.$nextTick(function () {
                    this.friendlyLinkFormVisible = false;
                });

                this.friendlyLinkLogoList = [];
            },

            goToPreview: function () {
                this.getCmsLinkList();
                this.dialogVisibleLinkPreview = true;
            },
            handleCloseLinkPreview: function () {
                this.dialogVisibleLinkPreview = false;
            },
            axiosRequest: function (url, showMsg) {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'POST',
                    url: url
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getDataList();
                        if (showMsg) {
                            self.$message({
                                message: '操作成功',
                                type: 'success'
                            });
                        }
                    } else {
                        self.$message({
                            message: '操作失败',
                            type: 'error'
                        });
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                })
            },
            batchDelete: function () {
                var self = this;
                this.$confirm('确定批量删除吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    var url = '/cms/cmsLink/cmsLinkPlDel?ids=' + self.multipleSelectedId.join(',');
                    self.axiosRequest(url, true);
                }).catch(function () {

                })
            },
            deleteLineData: function (id) {
                var self = this;
                this.$confirm('确定删除吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    var url = '/cms/cmsLink/cmsLinkPlDel?ids=' + id;
                    self.axiosRequest(url, true);
                }).catch(function () {

                })
            },
            changeLineData: function (row) {
                this.changeSort = row.sort;
                this.friendlyLinkFormVisible = true;
                this.isAdd = false;
                this.friendlyLinkForm.id = '';
                this.$nextTick(function () {
                    this.friendlyLinkForm = Object.assign({}, row);
                });
                this.setFriendlyLinkLogo(row.logo);
            },
            addFriendlyLink: function () {
                this.friendlyLinkFormVisible = true;
                this.isAdd = true;
                this.friendlyLinkForm.id = '';
                this.$nextTick(function () {
                    this.$refs.friendlyLinkForm.resetFields();
                });
            },
            saveFriendlyLink: function (formName) {
                var self = this;
                this.friendlyLinkForm.sort = this.friendlyLinkForm.sort.toString();
                this.$refs[formName].validate(function (valid) {
                    if (valid) {
                        self.saveAjax();
                    }
                });

            },
            saveAjax: function () {
                var self = this;
                this.loading = true;
                this.disabled = true;
                this.$axios.get('/cms/cmsLink/cmsLinkSave', {params: self.friendlyLinkForm}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.handleCloseFriendlyLinkForm();
                        self.getDataList();
                        self.$alert('保存成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.disabled = false;
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                    self.disabled = false;
                });
            },
            getSorts: function () {
                var list = this.friendlyLinkList;
                this.sorts = [];
                return this.sorts = list.map(function (item) {
                    return item.sort;
                })
            },
            saveSort: function () {
                var list = this.friendlyLinkList;
                var ids = [];
                var reg = /^[1-9][0-9]*$/;
                var qc = {};
                var flag = true;
                this.sorts = [];
                for (var i = 0; i < list.length; i++) {
                    if (!reg.test(list[i].sort)) {
                        this.$message({
                            message: '排序值必须为正整数，请修改后，再次保存！',
                            type: 'warning'
                        });
                        flag = false;
                        break;
                    }
                    if (!qc[list[i].sort]) {
                        qc[list[i].sort] = true;
                    } else {
                        this.$message({
                            message: '有相同的排序，请修改后，再次保存！',
                            type: 'warning'
                        });
                        flag = false;
                        break;
                    }
                    ids.push(list[i].id);
                    this.sorts.push(list[i].sort.toString());
                }
                if (!flag) {
                    return false;
                }
                var url = '/cms/cmsLink/cmsLinkSaveSort?ids=' + ids.join(',') + '&sorts=' + this.sorts.join(',');
                this.axiosRequest(url, true);
            },
            changeIsShow: function (row) {
                var url = '/cms/cmsLink/cmsLinkSave?id=' + row.id + '&isShow=' + row.isShow;
                this.axiosRequest(url, false);
            },

            handleChangeSitetype: function (value) {
                if (value == '1') {
                    this.friendlyLinkForm.logo = ''
                }
            },

            getCmsLinkList: function () {
                var self = this;
                this.$axios.get('/cms/cmsIndex/cmsLinkList').then(function (response) {
                    var data = response.data;
                    var cmsLinkList = [];
                    if (data.data) {
                        cmsLinkList = data.data.cmsLinkList
                    }
                    self.cmsLinkList = cmsLinkList;
                }).catch(function () {

                })
            }


        },
        mounted: function () {
            this.getLinkType();
            this.getSorts();
        }
    })

</script>

</body>
</html>