<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script src="${ctxJs}/components/pwSpaceTree/pwSpaceTree.js?v=10"></script>


</head>
<body>
<div id="app"  class="container-fluid container-fluid_bg mgb-60" v-show="pageLoad"
     style="display: none">
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>
    <div class="text-right mgb-20">
        <el-button type="primary" size="mini" @click.stop="openWarningDialog">添加预警规则</el-button>
    </div>
    <div class="table-container mgb-20">
        <el-table :data="paginationTable" size="mini" class="table" ref="warningTable"  v-loading="loading" highlight-current-row>
            <el-table-column
                    prop="name"
                    align="center"
                    width="200"
                    label="名称">
            </el-table-column>
            <el-table-column
                    prop="erspace"
                    align="center"
                    width="500"
                    label="预警门禁组">
                <template slot-scope="scope">
                    <div v-if="scope.row.drCreGitems">
                        <div v-for="drCreGitem in scope.row.drCreGitems">
                            {{drCreGitem.erspace.rspaceName}}({{drCreGitem.erspace.name}}/{{drCreGitem.erspace.epment ? drCreGitem.erspace.epment.name : ''}})
                        </div>
                    </div>
                </template>
            </el-table-column>
            <<!-- el-table-column
                    prop="estatus"
                    align="center"
                    label="进出/进/出">
                <template slot-scope="scope">
                {{scope.row.estatus}}
                   {{getEstatus(scope.row.estatus)}}
                </template>
            </el-table-column> -->
            <el-table-column
                    align="center"
                    width="100"
                    label="进出/进/出">
                <template slot-scope="scope">
                    <div v-if="scope.row.drCreGitems">
                        <div v-for="drCreGitem in scope.row.drCreGitems">
                            {{getEstatus(drCreGitem.estatus)}}
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                    prop="remarks"
                    align="center"
                    label="备注">
                <template slot-scope="scope">
                    {{scope.row.remarks}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-button type="text" size="mini" @click.stop.prevent="getWarningRule(scope.row)">修改</el-button>
                    <el-button type="text" size="mini" @click.stop.prevent="delWarningRule(scope.row, scope.$index)">{{scope.row.isShow? '禁用' : '启用'}}</el-button>
                    <el-button type="text" size="mini" @click.stop.prevent="removeWarningRule(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="text-right mgb-20">



            <el-pagination
                    size="small"
                    @size-change="sizeChange"
                    background
                    @current-change="sizeChange"
                    :current-page.sync="currentPage"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="pageSize"
                    layout="prev, pager, next, sizes"
                    :total="total">
            </el-pagination>
        </div>
    </div>

    <pw-space-tree v-show="pwSpaceShow" :id="rowId" :pw-space-list="pwSpaceList" :space-gitems="spaceGitems"
                   @save-rule="saveRule"></pw-space-tree>


    <el-dialog
            title="提示"
            :visible.sync="dialogVisible"
            width="520px"
            center
            :close-on-click-modal="false"
            :before-close="handleClose">
        <div class="text-center">
            <el-form :model="warningRuleForm" ref="warningRuleForm" :rules="warningRules" label-width="72px"
                     :disabled="warningRuleFormDisabled">
                <el-form-item size="mini" prop="name" label="名称">
                    <el-input v-model="warningRuleForm.name" placeholder="请输入预警规则名称"></el-input>
                </el-form-item>
                <el-form-item size="mini" prop="remarks" label="备注">
                    <el-input type="textarea" :autosize="false" :rows="2" v-model="warningRuleForm.remarks"></el-input>
                </el-form-item>
            </el-form>
        </div>
        <span slot="footer" class="dialog-footer">
                <el-button size="mini" @click="cancelAdd">取 消</el-button>
                <el-button :disabled="warningRuleFormDisabled" type="primary" size="mini" @click.stop="addWarningRule">确 定</el-button>
        </span>
    </el-dialog>

</div>


<script type="text/javascript">
    +function ($, Vue) {
        var app = new Vue({
            el: '#app',
            mixins: [paginationMixin],
            data: function () {
            	var gitemEstatuss = JSON.parse('${gitemEstatuss}');

                return {
                    tableData: [],
                    warningRuleForm: {
                        name: '',
                        remarks: ''
                    },
                    pageSize: 5,
                    currentPage: 1,
                    total: 0,
                    warningRules: {
                        name: [
                            {required: true, message: '请填写预警规则名称', trigger: 'blur'},
                            {min: 2, max: 64, message: '请填写2-64位名称', trigger: 'blur'}
                        ],
                        remarks: [{max: 128, message: '最大字符位数128', trigger: 'blur'}]
                    },
                    dialogVisible: false,

                    pwSpaceList: [],
                    spaceGitems: {},
                    pwSpaceShow: false,
                    pwSpaceListLoad: false,
                    rowId: '',
                    uuids: [],
                    warningRuleFormDisabled: false,
                    gitemEstatuss: gitemEstatuss,
                    loading: true
                }
            },
            <%--watch: {--%>
                <%--'uuids': {--%>
                    <%--deep: true,--%>
                    <%--handler: function (value) {--%>
                        <%--if (value.length < 100) {--%>
                            <%--this.getUUIds(1000);--%>
                        <%--}--%>
                    <%--}--%>
                <%--}--%>
            <%--},--%>
            directives: {
                treeTable: {
                    inserted: function (element, binding, vnode) {
                        $(element).treeTable({expandLevel: 10}).show();
                    }
                }
            },
            computed: {
                paginationTable: {
                    get: function () {
                        var currentPage = this.currentPage;
                        var pageSize = this.pageSize;
                        var tableData = this.tableData;
                        var startNum = (currentPage - 1) * pageSize;
                        var endNum = startNum + pageSize;
                        endNum = endNum > this.total ? this.total : endNum;
                        if (!tableData) {
                            return []
                        }
                        return tableData.slice(startNum, endNum)
                    }
                },
                gitemEstatussEntries: function(){
					var entries = {};
					this.gitemEstatuss.forEach(function(item){
						entries[item.key] = item.name;
					})
					return entries;
            	}
            },
            methods: {
				getEstatus: function(value){
					if(!value) return '-';
					return this.gitemEstatussEntries[value] || '-';
				},
                openWarningDialog: function () {
                    this.dialogVisible = true;
                    this.pwSpaceShow = false;
                },
                //添加预警规则
                addWarningRule: function () {
                    var self = this;
//                    this.axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';
                    var waringRuleXhr = this.$axios({
                        method: 'POST',
                        url: '/dr/drCardreGroup/ajaxSaveGroup',
                        params: this.warningRuleForm
                    });
                    this.warningRuleFormDisabled = true;
                    waringRuleXhr.then(function (response) {
                        var data = response.data;
                        if (data.status) {
                            self.getGroupAll();
                            self.dialogVisible = false;
                            self.$refs['warningRuleForm'].resetFields();
                        }
                        self.show$message(data)
                        self.warningRuleFormDisabled = false;
                    });
                    waringRuleXhr.catch(function (error) {
                        self.show$message({}, error.response.data)
                        self.warningRuleFormDisabled = false;
                    });


                },
                //删除规则
                delWarningRule: function (row, index) {
                    var self = this;
                    var waringRuleXhr = this.$axios({
                        method: 'POST',
                        url: '/dr/drCardreGroup/ajaxIsShow/' + row.id,
                        params: {
                        	isShow: !row.isShow
                        }
                    });
                    waringRuleXhr.then(function (repsonse) {
                        var data = repsonse.data;
                        if (data.status) {
                        	self.tableData[index].isShow = !row.isShow;
                        }
                        self.show$message(data)
                    });

                    waringRuleXhr.catch(function (error) {
                        self.show$message({}, error.response.data)
                    })
                },
                //删除规则
                removeWarningRule: function (row) {
                    var self = this;
                    var waringRuleXhr = this.$axios.post('/dr/drCardreGroup/ajaxDelete/' + row.id);
                    waringRuleXhr.then(function (repsonse) {
                        var data = repsonse.data;
                        if (data.status) {
                            self.getGroupAll();
                            self.pwSpaceShow = false;
                        }
                        self.show$message(data)
                    });

                    waringRuleXhr.catch(function (error) {
                        self.show$message({}, error.response.data)
                    })
                },

                cancelAdd: function () {
                    this.$refs['warningRuleForm'].resetFields();
                    this.dialogVisible = false;
                },

                saveRule: function (spaceList) {
                    var self = this;
                    spaceList.drCreGitems.forEach(function (item, index) {
                        if(!item.id){
                            item.id = self.uuids.splice(0, 1)[0];
                        }
                    });
//                    ajaxSavePl
                    var gItemsXhr = this.$axios({
                        method: 'POST',
                        url: '/dr/drCardreGitem/ajaxSavePl',
                        data: JSON.stringify(spaceList),
                        headers: {
                            'Content-Type': 'application/json;charset=utf-8'
                        }
                    });

                    gItemsXhr.then(function (response) {
                        var data = response.data;
                        if (data.status) {
                            self.pwSpaceShow = false;
                            self.getGroupAll();
                        }
                        self.show$message(data)
                    })

                    gItemsXhr.catch(function (error) {
                        self.show$message({}, error.response.data)
                    })

                },

                //获取规则
                getWarningRule: function (row) {
                    var self = this;
                    if (row.id === this.rowId) {
                        if(!this.pwSpaceShow){
                            this.pwSpaceShow = true;
                        }
                        return false;
                    }
                    var waringRuleXhr = this.$axios.post('/dr/drCardreGitem/ajaxGitemsSelByGid/' + row.id);
                    waringRuleXhr.then(function (response) {
                        var data = response.data;
                        var nSpaceGitems = {};
                        self.spaceGitems = {};
                        if (data.status && data.datas) {
                            data.datas.forEach(function (item) {
                                if (!nSpaceGitems[item.spaceId]) {
                                    nSpaceGitems[item.spaceId] = [];
                                }
                                nSpaceGitems[item.spaceId].push(item)
                            });
                            if(self.uuids.length < data.datas.length  * 2){
                                self.getUUIds(data.datas.length)
                            }
                            self.pwSpaceShow = true;
                            self.rowId = row.id;
                            self.spaceGitems = nSpaceGitems;
                        }
                    });
                    waringRuleXhr.catch(function (error) {
                        self.show$message({}, error.response.data)
                    });
                    this.$refs.warningTable.setCurrentRow(row);
                },

                getGroupAll: function () {
                    var self = this;
                    var groupAllXhr;
                    this.loading = true;
                    groupAllXhr = this.$axios.post('/dr/drCardreGroup/ajaxGroupAlls?isAll=true');
                    groupAllXhr.then(function (response) {
                        var data = response.data;
                        if (data.status) {
                            var datas = data.datas;
                            self.tableData = datas.list || [];
                            self.total = self.tableData ? self.tableData.length : 0;
                        }
                        self.loading = false;
                    });
                    groupAllXhr.catch(function (error) {
                        self.loading = false;
                    })
                },

                handleClose: function () {
                    this.dialogVisible = false;
                    this.$refs['warningRuleForm'].resetFields();
                },

                getSpaceList: function () {
                    var self = this;
                    return this.$axios.get('/dr/drCardreGitem/ajaxPwSpaceList').then(function (response) {
                        var data = response.data;
                        if (data.status) {
                            self.pwSpaceList = data.datas;
                            self.pwSpaceListLoad = true;
                        }
                    })
                },

                getUUIds: function (num) {
                    var self = this;
                    return this.$axios.get('/sys/uuids/' + num).then(function (respons) {
                        var data = respons.data;
                        if (data.status) {
                            self.uuids = self.uuids.concat(JSON.parse(data.id));
                        }
                    });
                },

                sizeChange: function () {
                    this.getGroupAll();
                }
            },
            created: function () {
//                this.getUUIds(1000);
            },
            beforeMount: function () {
                this.getGroupAll();
//                this.getSpaceList();
            }
        })
    }(jQuery, Vue)
</script>
</body>
</html>