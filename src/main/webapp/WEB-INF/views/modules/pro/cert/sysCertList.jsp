<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>--%>
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
        <el-button type="primary" size="mini" icon="el-icon-circle-plus" @click.stop.prevent="dialogVisible = true">
            创建证书
        </el-button>
        <el-tooltip class="item" content="请选择需要删除的证书" :disabled="multipleSelectionId.length > 0"
                    popper-class="white" placement="bottom">
                        <span>
            <el-button size="mini" type="primary" icon="el-icon-delete" :disabled="multipleSelectionId.length == 0"
                       @click.stop.prevent="batchDelete">删除
            </el-button>
                            </span>
        </el-tooltip>
    </div>


    <div class="table-container mgb-20 new-cert" v-loading="loading">
        <div class="articles-comments-list">


            <div v-for="cert in certList" class="article-comments">

                <el-container class="article-comments-header">
                    <el-aside width="142px" class="checkbox-category-title">
                        <el-checkbox v-model="cert.isChecked" @change="handleChangeIsChecked"></el-checkbox>
                        <span class="category-name">证书名称(双击修改):</span>
                    </el-aside>
                    <el-main>
                        <el-row>
                            <el-col class="checkbox-category-title" :span="8">
                                <e-set-name :editing.sync="controlEditings[cert.id]" :max="24"
                                            :text="cert.name" :row="cert" @change="handleChangeText"></e-set-name>
                            </el-col>
                            <el-col :span="8" class="title-center-box">
                                <span>发布时间：<span v-show="cert.releaseDate">{{cert.releaseDate | formatDateFilter('YYYY-MM-DD HH:mm:ss')}}</span><span
                                        v-show="!cert.releaseDate" class="empty">未发布</span></span>
                            </el-col>
                            <el-col :span="8">
                                <div class="cert-edit">
                                    <el-tooltip class="item" effect="dark" content="预览证书" popper-class="white"
                                                placement="top">
                                        <i class="iconfont icon-yulan" v-if="cert.scp && cert.imgs.length > 0"
                                           @click.stop.prevent="lookImg(cert.scp)"></i>
                                    </el-tooltip>
                                    <el-tooltip class="item" effect="dark" content="取消发布" popper-class="white"
                                                placement="top">
                                        <i class="iconfont icon-quxiaofabu" v-if="cert.releases == '1'"
                                           @click.stop.prevent="cancelPublish(cert.id)"></i>
                                    </el-tooltip>
                                    <el-tooltip class="item" effect="dark" content="发布证书" popper-class="white"
                                                placement="top">
                                        <i class="iconfont icon-fabu" v-if="cert.releases == '0'"
                                           @click.stop.prevent="publish(cert.id)"></i>
                                    </el-tooltip>
                                    <el-tooltip class="item" effect="dark" content="删除证书" popper-class="white"
                                                placement="top">
                                        <i class="el-icon-remove" @click.stop.prevent="singleDelete(cert.id)"></i>
                                    </el-tooltip>
                                    <el-tooltip class="item" effect="dark" content="添加证书版面" popper-class="white"
                                                placement="top">
                                        <i class="el-icon-circle-plus" @click.stop.prevent="handleTabsAdd(cert)"></i>
                                    </el-tooltip>
                                </div>
                            </el-col>
                        </el-row>
                    </el-main>
                </el-container>


                <div class="article-comments-body control-article-comments">

                    <div class="control-article-comment">

                        <el-container style="position:relative;">
                            <div class="cert-publish-label" v-if="cert.releases == '1'">
                                已发布
                            </div>
                            <el-aside width="330px">
                                <el-carousel indicator-position="outside" :autoplay="false" trigger="click"
                                             height="180px">
                                    <el-carousel-item v-for="(item, index) in cert.scp" :key="item.id" :label="index+1">
                                        <div class="preview-cert-box">
                                            <img :src="item.imgUrl | proGConPicFilter" alt="">
                                        </div>
                                        <div class="cert-cover-shade">
                                            <a href="javascript:void(0);" @click.stop.prevent="handleTabsChange(item)">
                                                <i class="iconfont icon-bianji"></i>
                                                <span @click.stop.prevent="handleTabsChange(item)">设计证书</span>
                                            </a>
                                        </div>
                                    </el-carousel-item>

                                </el-carousel>


                            </el-aside>
                            <el-main>
                                <div class="conditions">
                                    <div class="condition-item clearfix">
                                        <el-select v-model="cert.selectedProjectId" placeholder="-请选择要关联的项目-"
                                                   size="mini"
                                                   @change="handleRelatedProjectChange(cert)">
                                            <el-option
                                                    v-for="item in relatedProjectList"
                                                    :key="item.id"
                                                    :label="item.projectName"
                                                    :value="item.id"
                                                    :disabled="cert.relatedProjectIds.indexOf(item.id) > -1">
                                            </el-option>
                                        </el-select>
                                    </div>
                                </div>


                                <cert-node v-for="(nodeList,key) in cert.nodeAllObj" :key="key"
                                           :label="key | selectedFilter(relatedLabelEntries)"
                                           v-model="cert.nodeIds[key]" :node-list="cert.nodeAllObj[key]"
                                           :all-related-nodes="allRelatedNodes"
                                           :component-key="key" :cert="cert" @node-click="nodeClick"
                                           @batch-cancel-node="batchCancelNodes"></cert-node>

                            </el-main>
                        </el-container>


                    </div>
                </div>

            </div>

            <div v-if="certList.length === 0" class="pdt-60 mgb-60 empty">暂无证书， 去创建证书吧</div>

        </div>
    </div>


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

    <el-dialog title="创建证书" :visible.sync="dialogVisible" :close-on-click-modal="isClose"
               :before-close="handleClose" width="560px">

        <el-form size="mini" :model="dialogForm" ref="dialogForm" label-width="140px">

            <el-form-item label="证书名称" prop="certname">
                <el-input v-model="dialogForm.certname" class="w300" placeholder="请设置证书名称" maxlength="24"></el-input>
            </el-form-item>

        </el-form>

        <div slot="footer" class="dialog-footer">
            <el-button size="mini" @click.stop.prevent="handleClose">取 消</el-button>
            <el-button size="mini" type="primary" @click.stop.prevent="saveDialogForm('dialogForm')">确 定</el-button>
        </div>
    </el-dialog>


    <el-dialog title="证书预览" :visible.sync="dialogCertVisible" width="60%">
        <el-carousel indicator-position="outside" ref="elCarousel" :height="bannerHeight + 'px'" :autoplay="false"
                     trigger="click">
            <el-carousel-item v-for="item in bannerImg" :key="item.id">
                <div class="preview-cert-content">
                    <img :src="item.imgUrl" alt="截图" v-auto-img="bannerScale">
                </div>
            </el-carousel-item>
        </el-carousel>
    </el-dialog>


</div>

<script>


    'use strict';


    Vue.component('cert-node', {
        template: '<div class="conditions">' +
        '               <div class="condition-item clearfix">' +
        '                   <div class="condition-label">' +
        '                       <el-tooltip class="item" effect="dark" content="批量取消关联流程" placement="bottom">' +
        '                           <i class="iconfont icon-shanchu" @click.stop.prevent="batchDeleteNodes"></i>' +
        '                       </el-tooltip>' +
        '                       {{label + \'：\'}}' +
        '                   </div>' +
        '                   <div class="condition-control" :class="{\'condition-control_has-collapse\': isCollapse, opened: opened}">' +
        '                       <div ref="collapse" class="condition-collapse">\n' +
        '                           <e-checkbox-group v-model="model">' +
        '                               <e-checkbox v-for="(item, index) in nodeList" :class="{\'e-checkbox-disable\':lineAllDisabledNodeIds.indexOf(item.id) > -1}" :disabled="lineAllDisabledNodeIds.indexOf(item.id) > -1" :key="item.id" :label="item.id" @change="handleNodeChange">{{item.name}}</e-checkbox>' +
        '                           </e-checkbox-group>' +
        '                           <a v-show="isCollapse" href="javascript: void(0);" class="btn-handle_collapse" @click.stop="opened = !opened"><i class="iconfont icon-down"></i></a>\n' +
        '                       </div>' +
        '                   </div>' +
        '               </div>' +
        '           </div>',
        props: {
            nodeList: Array,
            cert: Object,
            componentKey: String,
            label: String,
            value: {
                type: Array,
                require: true,
                default: function () {
                    return []
                }
            },
            allRelatedNodes: Array
        },
        data: function () {
            return {
                isCollapse: false,
                opened: false,
                lineAllDisabledNodeIds: []
            }
        },
        watch: {
            optionsLen: function () {
                this.$nextTick(function () {
                    this.isCollapse = this.getIsCollapse();
                })
            },
            allRelatedNodes: function () {
                this.getLineAllDisabledNodeIds();
            }
        },
        computed: {
            optionsLen: {
                get: function () {
                    return this.nodeList ? this.nodeList.length : 0;
                }
            },
            model: {
                get: function () {
                    return this.value;
                },
                set: function (value) {
                    this.$emit('input', value)
                }
            }
        },
        methods: {
            getIsCollapse: function () {
                var height, parentH;
                var collapse = this.$refs.collapse;
                if (!collapse) return false;
                height = collapse.clientHeight;
                parentH = collapse.parentNode.clientHeight;
                return height - 10 > parentH;
            },
            handleNodeChange: function (value, ev, isChecked) {
                this.getLineAllDisabledNodeIds();
                this.$emit('node-click', {
                    value: value,
                    key: this.componentKey,
                    checked: isChecked,
                    cert: this.cert
                })
            },
            batchDeleteNodes: function () {
                this.$emit('batch-cancel-node', {
                    key: this.componentKey,
                    cert: this.cert
                })
            },
            getLineAllDisabledNodeIds: function () {
                var all = Object.assign([], this.allRelatedNodes);
                var lineAll = Object.assign([], this.cert.lineAllRelatedNodes);
                for (var i = 0; i < all.length; i++) {
                    for (var j = 0; j < lineAll.length; j++) {
                        if (all[i] == lineAll[j]) {
                            all.splice(i, 1);
                        }
                    }
                }
                this.lineAllDisabledNodeIds = all;
            }
        },
        created: function () {
            this.getLineAllDisabledNodeIds();
        },
        mounted: function () {
            this.isCollapse = this.getIsCollapse();
        }
    });


    Vue.directive('auto-img', {
        componentUpdated: function (element, binding) {
            var ratio = binding.value;
            element.onload = function () {
                var imgScale = element.naturalWidth / element.naturalHeight;
                element.style.width = imgScale > ratio ? '100%' : 'auto';
                element.style.height = imgScale >= ratio ? 'auto' : '100%'
            }
        },
        unbind: function (element) {
            element.onload = null;
        }
    });

    new Vue({
        el: '#app',
        data: function () {
            var relatedProjectList = JSON.parse(JSON.stringify(${fns: toJson(fns:getActListData(''))})) || [];
            return {
                loading: false,
                certList: [],
                relatedProjectList: relatedProjectList,
                relatedLabel: [],
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1
                },
                pageCount: 0,
                isAllChecked: false,
                isIndeterminate: false,
                multipleSelectionId: [],
                dialogVisible: false,
                dialogForm: {
                    certid: '',
                    certname: '',
                    certpageid: '',
                    certpagename: '1'
                },
                dialogCertVisible: false,
                bannerHeight: 400,
                bannerImg: [],
                bannerScale: 1,
                controlEditings: {},
                allRelatedNodes: [],
                isClose: false,


            }
        },
        watch: {
            relatedProjectList: function (value) {
                var self = this;
                value.forEach(function (item) {
                    self.relatedLabel.push({label: item.projectName, value: item.id});
                });
            }
        },
        computed: {
            relatedLabelEntries: {
                get: function () {
                    return this.getEntries(this.relatedLabel)
                }
            }
        },
        methods: {
            handleChangeText: function (obj) {
                var self = this;
                var text = obj.text;
                var row = obj.row;
                var list = this.certList;
                var listI = {};

                for (var i = 0; i < list.length; i++) {
                    if (list[i].id === row.id) {
                        listI = list[i];
                        break;
                    }
                }

                if (text == row.name) {
                    self.controlEditings[listI.id] = false;
                } else {
                    self.loading = true;
                    self.$axios({
                        method: 'POST',
                        url: '/cert/editSysCertName?id=' + row.id + '&name=' + text
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            Vue.set(listI, 'name', text);
                            self.controlEditings[listI.id] = false;
                            self.getDataList();
                            self.isAllChecked = false;
                            self.isIndeterminate = false;
                        }
                        self.$message({
                            message: data.status == '1' ? '修改证书名称成功！' : '修改证书名称失败！',
                            type: data.status == '1' ? 'success' : 'error'
                        });
                        self.loading = false;
                    }).catch(function () {
                        self.loading = false;
                        self.$message({
                            message: '请求失败',
                            type: 'error'
                        })
                    });
                }
            },
            lookImg: function (scp) {
                var self = this;
                this.bannerImg = [];
                scp.forEach(function (item) {
                    self.bannerImg.push({id: item.id, imgUrl: item.imgUrl})
                });
                this.imgCenter();
                this.dialogCertVisible = true;
            },
            imgCenter: function () {
                var self = this;
                var bannerWidth = 0;
                self.$nextTick(function () {
                    bannerWidth = self.$refs.elCarousel.$el.offsetWidth;
                    self.bannerHeight = bannerWidth * 0.55;
                    self.bannerScale = bannerWidth / self.bannerHeight;
                });
            },
            handleChangeIsChecked: function () {
                var self = this;
                this.multipleSelectionId = [];
                this.certList.forEach(function (item) {
                    if (item.isChecked) {
                        self.multipleSelectionId.push(item.id);
                    }
                });
                var len = this.multipleSelectionId.length;
                this.isAllChecked = len == this.certList.length;
                this.isIndeterminate = len > 0 && len < this.certList.length;

            },
            handleChangeIsAllChecked: function () {
                var self = this;
                this.isIndeterminate = false;
                if (this.isAllChecked) {
                    this.certList.forEach(function (item) {
                        item.isChecked = true;
                        self.multipleSelectionId.push(item.id);
                    })
                } else {
                    this.multipleSelectionId = [];
                    this.certList.forEach(function (item) {
                        item.isChecked = false;
                    })
                }
            },
            batchDelete: function () {
                var self = this;
                this.$confirm('是否确定删除？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    var url = '/cert/deleteAll?ids=' + self.multipleSelectionId.join(',');
                    self.axiosRequest(url, true, {});
                }).catch(function () {

                })
            },

            getDataList: function () {
                var self = this;
                this.getControlEditings();
                this.$axios({
                    method: 'POST',
                    url: '/cert/certList?' + Object.toURLSearchParams(self.searchListForm)
                }).then(function (response) {
                    var data = response.data;
                    self.pageCount = data.data.count;
                    self.searchListForm.pageSize = data.data.pageSize;
                    self.searchListForm.pageNo = data.data.pageNo;
                    self.allRelatedNodes = [];
                    var list = data.data.list || [];
                    list.forEach(function (item) {
                        item.relatedProjectIds = [];
                        item.selectedProjectId = '';
                        item.nodeAllObj = {};
                        item.nodeIds = {};
                        item.lineAllRelatedNodes = [];
                        item.isDisabled = false;
                        item.isChecked = false;
                        item.imgs = [];
                    });


                    self.relatedProjectList.forEach(function (item) {
                        item.projectName = item.proProject.projectName;
                    });
                    self.relatedProjectList = self.relatedProjectList.filter(function (item) {
                        return item.proProject.proType == '1,' && item.proProject.type != '1';
                    });

                    list.forEach(function (item) {
                        var projectIds = [];
                        var nodeList = {};
                        if (item.scp) {
                            item.scp.forEach(function (value) {
                                if (value.imgUrl) {
                                    item.imgs.push(value.imgUrl);
                                }
                            });
                        }
                        if (item.scf != undefined) {
                            item.scf.forEach(function (value) {
                                if (projectIds.indexOf(value.flow) === -1) {
                                    projectIds.push(value.flow);
                                }
                            });
                            projectIds.forEach(function (p) {
                                nodeList[p] = [];
                                item.scf.forEach(function (s) {
                                    if (p == s.flow) {
                                        nodeList[p].push(s.node);
                                        if (self.allRelatedNodes.indexOf(s.node) === -1) {
                                            self.allRelatedNodes.push(s.node);
                                            item.lineAllRelatedNodes.push(s.node);
                                        }
                                    }
                                })
                            });
                            item.relatedProjectIds = projectIds;
                            item.relatedProjectIds.forEach(function (t) {
                                self.$axios({
                                    method: 'GET',
                                    url: '/actyw/actYwGnode/treeDataByYwId?level=1&ywId=' + t
                                }).then(function (response) {
                                    var data = response.data;
                                    if (data) {
                                        Vue.set(item.nodeAllObj, t, data);
                                        item.nodeIds[t] = nodeList[t];
                                    }
                                }).catch(function () {
                                    self.$message({
                                        message: '请求失败',
                                        type: 'error'
                                    })
                                });

                            });
                        }

                    });

                    self.certList = list;
                });

            },
            updateBatchDelete: function (item, key, obj) {
                item.selectedProjectId = '';
                Vue.set(item.nodeAllObj, key, null);
                delete item.nodeAllObj[key];
                Vue.set(item.nodeIds, key, null);
                delete item.nodeIds[key];
                var ids = obj.cert.relatedProjectIds;
                for (var i = 0; i < ids.length; i++) {
                    if (ids[i] == key) {
                        ids.splice(i, 1);
                    }
                }
            },
            updateRelatedNodes: function (allRelatedNodes, lineAllRelatedNodes, nodeIds) {
                var all = allRelatedNodes;
                var lineAll = lineAllRelatedNodes;
                for (var m = 0; m < all.length; m++) {
                    for (var n = 0; n < nodeIds.length; n++) {
                        if (all[m] == nodeIds[n]) {
                            all.splice(m, 1);
                        }
                    }
                }
                for (var a = 0; a < lineAll.length; a++) {
                    for (var b = 0; b < nodeIds.length; b++) {
                        if (lineAll[a] == nodeIds[b]) {
                            lineAll.splice(a, 1);
                        }
                    }
                }
            },
            batchCancelNodes: function (obj) {
                var self = this;
                if (obj.cert.nodeIds[obj.key].length == 0) {
                    self.certList.forEach(function (item) {
                        if (item.id == obj.cert.id) {
                            for (var key in item.nodeAllObj) {
                                if (obj.key == key) {
                                    self.updateBatchDelete(item, key, obj);
                                }
                            }
                        }
                    });
                    return false;
                }
                this.getControlEditings();
                this.$axios({
                    method: 'POST',
                    url: '/cert/clearSysCertFlow?certId=' + obj.cert.id + '&flow=' + obj.key
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.certList.forEach(function (item) {
                            if (item.id == obj.cert.id) {
                                for (var key in item.nodeAllObj) {
                                    if (obj.key == key) {
                                        self.updateRelatedNodes(self.allRelatedNodes, item.lineAllRelatedNodes, item.nodeIds[key]);
                                        self.updateBatchDelete(item, key, obj);
                                    }
                                }
                            }
                        })
                    }
                    self.$message({
                        message: data.status == '1' ? '取消关联成功' : '取消关联失败',
                        type: data.status == '1' ? 'success' : 'error'
                    })
                }).catch(function () {
                    self.$message({
                        message: '请求失败',
                        type: 'error'
                    })
                });
            },
            nodeClick: function (obj) {
                var cancelScfId = '';
                var url = '';
                if (obj.checked) {
                    url = '/cert/saveCertFlow?certId=' + obj.cert.id + '&flow=' + obj.key + '&node=' + obj.value;
                    this.axiosRelatedNodeRequest(url, obj, true);
                } else {
                    obj.cert.scf.forEach(function (item) {
                        if (item.node == obj.value) {
                            cancelScfId = item.id;
                        }
                    });
                    url = '/cert/delCertFlow?id=' + cancelScfId;
                    this.axiosRelatedNodeRequest(url, obj, false);
                }
            },
            axiosRelatedNodeRequest: function (url, obj, isRelated) {
                var self = this;
                this.loading = true;
                this.getControlEditings();
                this.$axios({
                    method: 'POST',
                    url: url
                }).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        //改状态
                        self.certList.forEach(function (item, index) {
                            if (obj.cert.id == item.id) {
                                if (isRelated) {
                                    if (!self.certList[index].scf) {
                                        self.certList[index].scf = [];
                                    }
                                    self.certList[index].scf.push({node: obj.value, id: data.sysCertFlowId});
                                    if (self.allRelatedNodes.indexOf(obj.value) == -1) {
                                        self.allRelatedNodes.push(obj.value);
                                        item.lineAllRelatedNodes.push(obj.value);
                                    }
                                } else {
                                    self.updateRelatedNodes(self.allRelatedNodes, item.lineAllRelatedNodes, [obj.value]);
                                }

                            }
                        });
                        self.$message({
                            message: isRelated ? '关联成功' : '取消关联成功',
                            type: 'success'
                        });
                    } else {
                        self.$message({
                            message: data.msg ? data.msg : (isRelated ? '关联失败' : '取消关联失败'),
                            type: 'error'
                        });
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                    self.$message({
                        message: '请求失败',
                        type: 'error'
                    })
                })
            },

            handleRelatedProjectChange: function (cert) {
                var self = this;
                if (cert.selectedProjectId != '') {  //选择项目
                    this.$axios({
                        method: 'GET',
                        url: '/actyw/actYwGnode/treeDataByYwId?level=1&ywId=' + cert.selectedProjectId
                    }).then(function (response) {
                        var data = response.data;
                        Vue.set(cert.nodeAllObj, cert.selectedProjectId, data);
                        Vue.set(cert.nodeIds, cert.selectedProjectId, []);
                        cert.relatedProjectIds.push(cert.selectedProjectId);
                    }).catch(function () {
                        self.$message({
                            message: '请求失败',
                            type: 'error'
                        })
                    })
                }

            },
            singleDelete: function (id) {
                var url = '/cert/deleteAll?ids=' + id;
                this.axiosRequest(url, true, {});
            },
            publish: function (id) {
                var url = '/cert/release?id=' + id;
                var p = {id: id, state: '1'};
                this.axiosRequest(url, true, p);
            },
            cancelPublish: function (id) {
                var url = '/cert/unrelease?id=' + id;
                var p = {id: id, state: '0'};
                this.axiosRequest(url, true, p);
            },
            handleTabsChange: function (page) {
                window.location.href = "/a/cert/form?certpageid=" + page.id + "&certid=" + page.certid;
            },
            handleTabsAdd: function (cert) {
                window.location.href = '/a/cert/form?certid=' + cert.id + '&certpagename=' + (cert.scp.length + 1);
            },
            addCert: function () {
                window.location.href = "/a/cert/form?secondName=添加证书模板";
            },
            handleClose: function () {
                this.dialogVisible = false;
                this.$refs.dialogForm.resetFields();
            },
            saveDialogForm: function (formName) {
                var self = this;
                this.$refs[formName].validate(function (valid) {
                    if (valid) {
                        var url = '/cert/save';
                        var data = self.dialogForm;
                        self.axiosRequest(url, true, {}, data);
                    }
                })
            },
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList();
            },

            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList();
            },
            getControlEditings: function () {
                var list = this.certList;
                for (var i = 0; i < list.length; i++) {
                    Vue.set(this.controlEditings, list[i].id, false);
                }
            },
            axiosRequest: function (url, showMsg, p, obj) {
                var self = this, path;
                this.getControlEditings();
                this.loading = true;
                if (obj) {
                    path = {
                        method: 'POST',
                        url: url,
                        data: obj
                    }
                } else {
                    path = {
                        method: 'POST',
                        url: url
                    }
                }
                this.$axios(path).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        if (p.state == '1') {
                            for (var i = 0; i < self.certList.length; i++) {
                                if (self.certList[i].id == p.id) {
                                    self.certList[i].releases = '1';
                                    self.getSysDate(self.certList[i])
                                    break;
                                }
                            }
                        } else if (p.state == '0') {
                            for (var j = 0; j < self.certList.length; j++) {
                                if (self.certList[j].id == p.id) {
                                    self.certList[j].releases = '0';
                                    self.certList[j].releaseDate = '';
                                    break;
                                }
                            }
                        } else {
                            self.getDataList();
                        }
                        self.isAllChecked = false;
                        self.isIndeterminate = false;
                        self.dialogVisible = false;
                        self.dialogForm.certname = '';
                        if (showMsg) {
                            self.$message({
                                message: '操作成功',
                                type: 'success'
                            });
                        }
                    } else {
                        self.$message({
                            message: data.msg || '操作失败',
                            type: 'error'
                        });
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                    self.$message({
                        message: '请求失败',
                        type: 'error'
                    })
                })
            },

            getSysDate: function (cert) {
                var self = this;
                this.$axios.get('/sys/sysCurDateYmdHms').then(function (response) {
                    cert.releaseDate = moment(response.data).format('YYYY-MM-DD HH:mm:ss');
                })
            }
        },
        created: function () {
            this.getDataList();

        }
    })

</script>

</body>
</html>