<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

<body>


<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60 room-search">
    <edit-bar></edit-bar>
    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="conditions">

            <e-condition label="基地" type="radio" :options="baseList" v-model="baseId" @change="handleChangeBase"
                         :default-props="{label: 'name', value: 'id'}"></e-condition>

            <e-condition label="楼栋" :options="buildList">
                <e-radio class="e-checkbox-all" name="build" v-model="buildId" label="" @change="handleChangeBuild">不限
                </e-radio>
                <e-radio-group class="e-radio-spaces" v-model="buildId" @change="handleChangeBuild">
                    <e-radio v-for="build in buildList" name="buildId" :class="{'is-siblings': build.isSiblings}"
                             :label="build.id" :key="build.id">{{build.name}}
                    </e-radio>
                </e-radio-group>
            </e-condition>

            <e-condition label="楼层" type="radio" :options="floorList" v-model="floorId" @change="handleChangeFloor"
                         :default-props="{label: 'name', value: 'id'}"></e-condition>

            <e-condition label="房间类型" type="radio" v-model="searchListForm.type"
                         name="type" :options="pwRoomTypes" @change="getDataList">
            </e-condition>

        </div>

        <div class="search-block_bar clearfix mgt-20">
            <div class="search-input">
                <el-select size="mini" v-model="condition" placeholder="请选择条件"
                           @change="handleChangeCondition" style="width:135px;">
                    <el-option
                            v-for="item in conditions"
                            :key="item.id"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>

                <el-input name="numMin" size="mini" style="width:80px;" v-model="numMin" maxlength="6"
                          :disabled="!condition" @change="changeNum">
                </el-input>
                <span style="color:#dcdfe6;">-</span>
                <el-input name="numMax" size="mini" style="width:80px;" v-model="numMax" maxlength="6"
                          :disabled="!condition" @change="changeNum">
                </el-input>

                <input type="text" style="display:none">

                <el-input name="keys" size="mini" class="w300" v-model="searchListForm.keys"
                          placeholder="房间名称/所属场地" @keyup.enter.native="getDataList">
                    <el-button slot="append" class="el-icon-search" @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>

    </el-form>

    <div class="table-container">
        <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading"
                  @sort-change="handleTableSortChange">

            <el-table-column prop="r.name" label="房间信息" align="left" min-width="100" sortable="r.name">
                <template slot-scope="scope">
                    <div>
                        <el-tooltip :content="scope.row.name" popper-class="white" placement="right">
                            <span class="break-ellipsis">{{scope.row.name}}</span>
                        </el-tooltip>
                    </div>
                    <div>{{scope.row.type | selectedFilter(pwRoomTypesEntries)}}
                        <span v-if="scope.row.isAssign == '1'">定期租用</span>
                        <span v-if="scope.row.isUsable == '1'">临时预约</span>
                        <span v-if="scope.row.isAssign == '0' && scope.row.isUsable == '0'">其他</span>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="房间容量" align="left" min-width="110">
                <template slot-scope="scope">
                    <div>{{scope.row.numtype | selectedFilter(numTypesEntries)}}：共{{scope.row.num}}<span v-if="scope.row.numtype == '1'">人</span><span v-else>个</span></div>
                    <div>占地面积：{{scope.row.area}}平方米</div>
                </template>
            </el-table-column>
            <el-table-column prop="isAllowm" label="多团队共用" align="center" sortable="isAllowm" min-width="80">
                <template slot-scope="scope">
                    <span>{{scope.row.isAllowm | selectedFilter(yesOrNoEntries)}}</span>
                </template>
            </el-table-column>
            <el-table-column label="所属场地" align="center" min-width="100">
                <template slot-scope="scope">
                    {{scope.row.pwSpace | filterPwRoomAddress(pwSpaceListEntries)}}
                </template>
            </el-table-column>

        </el-table>
        <div class="text-right mgb-20" v-if="pageCount">
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

</div>


<script>
    new Vue({
        el: '#app',
        data: function () {
            var yesOrNo = JSON.parse(JSON.stringify(${fns:toJson(fns:getDictList('yes_no'))}));
            var pwRoomTypes = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList('pw_room_type'))}));
            return {
                yesOrNo: yesOrNo,
                pwRoomTypes:pwRoomTypes,
                conditions: [
                    {
                        id: '111',
                        label: '工位数范围',
                        value: '1'
                    },
                    {
                        id: '222',
                        label: '容纳人数范围',
                        value: '2'
                    },
                    {
                        id: '333',
                        label: '占地面积',
                        value: '3'
                    }
                ],
                pageCount: 0,
                message: '${message}',
                condition: '',
                numMin: '',
                numMax: '',
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    orderBy: '',
                    orderByType: '',
                    'pwSpace.id': '',
                    type: '',
                    querynumbegin: '',
                    querynumend: '',
                    querypeoplebegin: '',
                    querypeopleend: '',
                    queryareabegin: '',
                    queryareaend: '',
                    keys: ''
                },
                loading: false,
                numTypes: [],
                baseId: '',
                buildId: '',
                floorId: '',
                spaceList: [],
                pageList: []
            }
        },
        computed: {
            pwRoomTypesEntries: function () {
                return this.getEntries(this.pwRoomTypes);
            },
            numTypesEntries: function () {
                return this.getEntries(this.numTypes);
            },
            yesOrNoEntries: function () {
                return this.getEntries(this.yesOrNo);
            },
            pwSpaceListEntries: function () {
                var entries = {};
                this.spaceList.forEach(function (item) {
                    entries[item.id] = item;
                });
                return entries;
            },

            baseList: {
                get: function () {
                    return this.spaceList.filter(function (item) {
                        return item.type === '2'
                    })
                }
            },

            buildList: {
                get: function () {
                    var buildList = [];
                    this.spaceList.forEach(function (item) {
                        if (item.type === '3') {
                            Vue.set(item, 'isSiblings', true);
                            buildList.push(item);
                        }
                    });
                    return buildList;
                }
            },

            floorList: function () {
                var self = this;
                if (!this.buildId) return [];
                return this.spaceList.filter(function (item) {
                    return item.pId === self.buildId;
                })
            }
        },
        methods: {
            getDataList: function () {
                var self = this;

                if(this.numMin){
                    var flagMin = this.getReg(this.numMin);
                    if(!flagMin){
                        return false;
                    }
                }
                if(this.numMax){
                    var flagMax = this.getReg(this.numMax);
                    if(!flagMax){
                        return false;
                    }
                    if(parseInt(this.numMax) < parseInt(this.numMin)){
                        this.$message({
                            message: '最大值不能小于' + self.numMin + '，请修改后，再次查询！',
                            type: 'warning'
                        });
                        return false;
                    }
                }

                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwRoom/roomOtherList',
                    params:Object.toURLSearchParams(this.searchListForm)
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        self.pageList = data.data.list || [];
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                    self.$message({
                        message: '请求失败',
                        type: 'error'
                    })
                });
            },
            getReg:function (value) {
                var reg = /^[1-9][0-9]*$/;
                if (!reg.test(value)) {
                    this.$message({
                        message: '范围必须为正整数，请修改后，再次查询！',
                        type: 'warning'
                    });
                    return false;
                }
                return true;
            },
            setNumSearch:function () {
                this.searchListForm.querynumbegin = '';
                this.searchListForm.querynumend = '';
                this.searchListForm.querypeoplebegin = '';
                this.searchListForm.querypeopleend = '';
                this.searchListForm.queryareabegin = '';
                this.searchListForm.queryareaend = '';
                if(this.condition == '1'){
                    this.searchListForm.querynumbegin = this.numMin;
                    this.searchListForm.querynumend = this.numMax;
                }else if(this.condition == '2'){
                    this.searchListForm.querypeoplebegin = this.numMin;
                    this.searchListForm.querypeopleend = this.numMax;
                }else if(this.condition == '3'){
                    this.searchListForm.queryareabegin = this.numMin;
                    this.searchListForm.queryareaend = this.numMax;
                }
            },
            handleChangeCondition: function () {
                if(!this.numMin && !this.numMax){
                    return false;
                }
                this.setNumSearch();
                this.getDataList();
            },
            changeNum:function () {
                this.setNumSearch();
                this.getDataList();
            },
            handleTableSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? ( row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getDataList();
            },
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList();
            },

            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList();
            },


            handleChangeBuild: function () {
                var buildId = this.buildId;
                if (!buildId) {
                    this.floorId = '';
                    this.searchListForm['pwSpace.id'] = this.baseId;
                    this.getDataList();
                    return false;
                }
                var build = this.buildList.filter(function (item) {
                    return item.id === buildId;
                });
                this.baseId = build[0].pId;
                this.floorId = '';
                this.searchListForm['pwSpace.id'] = buildId;
                this.getDataList();
            },

            handleChangeBase: function () {
                var buildList = this.buildList;
                var value = this.baseId;
                buildList.forEach(function (item) {
                    Vue.set(item, 'isSiblings', !value ? true : item.pId === value);
                });
                this.buildId = '';
                this.floorId = '';
                this.searchListForm['pwSpace.id'] = value;
                this.getDataList();
            },

            handleChangeFloor: function () {
                this.searchListForm['pwSpace.id'] = this.floorId;
                this.getDataList();
            },
            getSpaceList: function () {
                var self = this;
                return this.$axios.get('/pw/pwSpace/treeData').then(function (response) {
                    self.spaceList = response.data || [];
                })
            },
            getNumTypes:function () {
                var self = this;
                this.$axios.post('/pw/pwRoom/pwRoomType').then(function (response) {
                    var data = response.data;
                    self.numTypes = data.data || [];
                })
            }

        },
        created: function () {
            this.getSpaceList();
            this.getDataList();
            this.getNumTypes();
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