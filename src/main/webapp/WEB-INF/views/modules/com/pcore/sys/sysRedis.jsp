<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>

<div id="app" v-show="pageLoad" class="container-fluid" style="display: none">
    <edit-bar></edit-bar>
    <div class="redis-manage">
        <el-select v-model="selected" placeholder="-请选择-" size="mini" class="w300" @change="changeSchool">
            <el-option v-for="item in schoolList" :key="item.id" :label="item.schoolName" :value="item.tenantId"></el-option>
        </el-select>

        <el-row :gutter="20" class="redis-container">
            <el-col :span="7" v-show="showLeft">
                <div class="main-content">
                    <div class="box-header">
                        <div class="box-title">
                            <i class="iconfont icon-06"></i>缓存列表
                        </div>
                        <i class="el-icon-refresh box-tools" @click.stop.prevent="getCacheNameList"></i>
                    </div>

                    <div class="table-box">
                        <el-table size="mini" :data="cacheNameList" class="table" @row-click="clickNameRow"
                                  v-loading="loading1" border :height="tableHeight" :row-style="nameRowStyle">
                            <el-table-column type="index" label=" " min-width="50">
                            </el-table-column>
                            <el-table-column prop="cache" label="缓存名称" min-width="160">
                            </el-table-column>
                            <el-table-column label="操作" align="center" min-width="60">
                                <template slot-scope="scope">
                                    <div class="table-btns-action">
                                        <el-button type="text" size="mini" icon="el-icon-delete" @click.stop.prevent="deleteCacheName(scope.row.cache)"></el-button>
                                    </div>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>

                </div>
            </el-col>
            <el-col :span="showLeft && showRight ? 7 : (!showLeft && !showRight ? 24 : (showLeft && !showRight ? 17 : 14))">
                <div class="main-content">
                    <div class="common-collapse left-collapse">
                        <div @click.stop.prevent="showLeft = !showLeft">
                            <i :class="showLeft ? 'el-icon-caret-left' : 'el-icon-caret-right'"></i>
                        </div>
                    </div>
                    <div class="common-collapse right-collapse">
                        <div @click.stop.prevent="showRight = !showRight">
                            <i :class="showRight ? 'el-icon-caret-right' : 'el-icon-caret-left'"></i>
                        </div>
                    </div>
                    <div class="box-header">
                        <div class="box-title">
                            <i class="iconfont icon-caidan2"></i>键名列表
                        </div>
                        <i class="el-icon-refresh box-tools" @click.stop.prevent="getCacheKeyList"></i>
                    </div>
                    <div class="table-box">
                        <el-table size="mini" :data="cacheKeyList" class="table" @row-click="clickKeyRow"
                                  v-loading="loading2" border :height="tableHeight" :row-style="keyRowStyle">
                            <el-table-column type="index" label=" " min-width="50">
                            </el-table-column>
                            <el-table-column prop="key" label="缓存键名" min-width="160">
                            </el-table-column>
                            <el-table-column label="操作" align="center" min-width="60">
                                <template slot-scope="scope">
                                    <div class="table-btns-action">
                                        <el-button type="text" size="mini" icon="el-icon-delete" @click.stop.prevent="deleteCacheKey(scope.row)"></el-button>
                                    </div>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
            </el-col>
            <el-col :span="10" v-show="showRight">
                <div class="main-content">
                    <div class="box-header">
                        <div class="box-title">
                            <i class="iconfont icon-benzi"></i>缓存内容
                        </div>
                        <%--<el-button type="primary" size="mini" class="box-tools">清理全部缓存</el-button>--%>
                    </div>
                    <div class="form-box" :style="{height: tableHeight + 'px'}">
                        <el-form :model="cacheForm" :rules="cacheFormRules" ref="cacheForm" v-loading="loading3"
                                  label-width="100px" size="mini">
                            <el-form-item prop="cache" label="缓存名称：">
                                <el-input v-model="cacheForm.cache" class="w300"></el-input>
                            </el-form-item>
                            <el-form-item prop="key" label="缓存键名：">
                                <el-input v-model="cacheForm.key" class="w300"></el-input>
                            </el-form-item>
                            <el-form-item prop="value" label="缓存内容：">
                                <div class="el-textarea el-input--mini">
                                    <textarea rows="6" class="el-textarea__inner" v-model="cacheForm.value" :style="{height: (tableHeight - 110) + 'px'}"></textarea>
                                </div>
                            </el-form-item>
                        </el-form>
                    </div>
                </div>
            </el-col>
        </el-row>

    </div>
</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            return {
                cacheForm: {
                    cache: '',
                    key: '',
                    value: ''
                },
                cacheFormRules:{},
                selected: "10",
                activeCacheName: '',
                activeKeyName: '',
                cacheNameList: [],
                cacheKeyList:[],
                schoolList: [],
                loading1:false,
                loading2:false,
                loading3:false,
                tableHeight: 0,
                showLeft: true,
                showRight: true
            }
        },
        computed: {

        },
        methods: {
            nameRowStyle: function (row) {
                return {
                    'background': row.row.cache == this.activeCacheName ? '#f5f5f5' : 'white'
                }
            },
            keyRowStyle: function (row) {
                return {
                    'background': row.row.key == this.activeKeyName ? '#f5f5f5' : 'white'
                }
            },
            getCacheNameList: function () {
                var self = this;
                this.loading1 = true;
                this.$axios.get("/sys/redis/ajaxGetAllCache").then(function (response) {
                    var data = response.data;
                    if(data.status == 1){
                        self.cacheNameList = response.data.data || [];
                    }
                    self.loading1 = false;
                });
            },
            getCacheKeyList: function () {
                this.cacheKeyList = [];
                var self = this;
                this.loading2 = true;
                this.$axios.get("/sys/redis/ajaxAllByKey", {
                    params:{
                        cache: self.activeCacheName,
                        tenantId: self.selected
                    }
                }).then(function (response) {
                    var data = response.data;
                    if(data.status == 1){
                        self.cacheKeyList = response.data.data || [];
                    }
                    self.loading2 = false;
                });
            },
            clickNameRow: function (row) {
                if(this.activeCacheName == row.cache) {
                    return false;
                }
                this.activeCacheName = row.cache;
                this.getCacheKeyList();
                this.cacheForm = {};
                this.activeKeyName = '';
            },
            clickKeyRow: function (row) {
                if(this.activeKeyName == row.key) {
                    return false;
                }
                this.activeKeyName = row.key;
                this.cacheForm = {};
                var self = this;
                this.loading3 = true;
                this.$axios.get("/sys/redis/ajaxValueByKey", {
                    params: {
                        cache: row.cache,
                        key: row.key,
                        tenantId: self.selected
                    }
                }).then(function (response) {
                    var data = response.data;
                    if(data.status == 1){
                        var obj = response.data.data;
                        Object.assign(self.cacheForm, obj);
                    }
                    self.loading3 = false;
                });
            },
            deleteCacheName: function (cache) {
                var self = this;
                this.$confirm('确认删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios.get('/sys/redis/ajaxDeleteCache', {
                        params:{
                            cache: cache,
                            tenantId: self.selected
                        }
                    }).then(function (response) {
                        var data = response.data;
                        if(data.status == '1'){
                            self.getCacheNameList();
                            if(self.activeCacheName == cache) {
                                self.activeCacheName = '';
                                self.activeKeyName = '';
                                self.cacheKeyList = [];
                                self.cacheForm = {};
                            }
                        }
                        self.$message({
                            message: data.status == '1' ? '删除成功' : '删除失败',
                            type: data.status == '1' ? 'success' : 'error'
                        });
                    });
                }).catch(function () {

                })
            },
            deleteCacheKey: function (row) {
                var self = this;
                this.$confirm('确认删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios.get('/sys/redis/ajaxDelValueByKey', {
                        params:{
                            cache: row.cache,
                            key: row.key,
                            tenantId: self.selected
                        }
                    }).then(function (response) {
                        var data = response.data;
                        if(data.status == '1'){
                            self.getCacheKeyList();
                            if(self.activeKeyName == row.key) {
                                self.activeKeyName = '';
                                self.cacheForm = {};
                            }
                        }
                        self.$message({
                            message: data.status == '1' ? '删除成功' : '删除失败',
                            type: data.status == '1' ? 'success' : 'error'
                        });
                    });
                }).catch(function () {

                })
            },
            changeSchool: function () {
                this.getCacheNameList();
                this.cacheKeyList = [];
                this.cacheForm = {};
                this.activeCacheName = '';
                this.activeKeyName = '';
            },
            getSchoolList: function () {
                var self = this;
                this.$axios.get("/sys/tenant/schoolList").then(function (response) {
                    self.schoolList = response.data.data || [];
                })
            }

        },
        created: function () {
            this.tableHeight = window.innerHeight - 190;
            this.getSchoolList();
            this.getCacheNameList();
        }
    })

</script>
</body>
</html>