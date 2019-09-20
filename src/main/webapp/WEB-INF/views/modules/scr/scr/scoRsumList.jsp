<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <edit-bar></edit-bar>
    <div class="conditions">
        <e-condition type="radio" v-model="searchListForm['user.office.id']" label="学院"
                     :options="colleges" :default-props="{label: 'name', value: 'id'}"
                     @change="getScoCourseGradeList"></e-condition>
    </div>
    <div class="mgb-20">
        <div class="search-input">
            <el-input size="mini" v-model="searchListForm.keys" placeholder="学号/姓名/专业"
                      @keyup.enter.native="getScoCourseGradeList" class="w300">
                <el-button slot="append" icon="el-icon-search"
                           @click.stop.prevent="getScoCourseGradeList"></el-button>
            </el-input>
        </div>
    </div>
    <div class="table-container table-container-credit">
        <div ref="tableFixedContainer" class="table-fixed-container">
            <div v-show="hasTranslate" class="table-fixed-left" :class="leftShadow">
                <span class="btn-el-icon-left" @click.stop="scrollLeft" :style="{top: arrowTop}">
                      <i class="el-icon-arrow-left"></i>
                </span>
                <div :style="{width: maxWidth+'px'}">
                    <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
                           style="table-layout: fixed" cellspacing="0" cellpadding="0">
                        <thead>
                        <tr>
                            <th width="286"
                                :class="sortClassName('user.no')"
                                @click.stop="handleChangeSort('user.no')"
                                style="cursor: pointer;">学生信息
                                <span class="caret-wrapper">
                                    <i class="sort-caret ascending"></i>
                                    <i class="sort-caret descending"></i>
                                </span>
                            </th>
                            <th v-for="item in scrProjectThs" width="100" class="text-center"
                                :class="sortClassName(item)" @click.stop="handleChangeSort(item)"
                                style="cursor: pointer;white-space: normal">
                                {{item}}
                                <span class="caret-wrapper">
                                    <i class="sort-caret ascending"></i>
                                    <i class="sort-caret descending"></i>
                                </span>
                            </th>
                            <th width="100" class="text-center" :class="sortClassName('总分')"
                                @click.stop="handleChangeSort('总分')"
                                style="cursor: pointer;">总分
                                <span class="caret-wrapper">
                                    <i class="sort-caret ascending"></i>
                                    <i class="sort-caret descending"></i>
                                </span>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="item in cpScrApplyUser">
                            <td width="286">
                                <credit-user-column :user="item.user" cn-name></credit-user-column>
                            </td>
                            <td v-for="(item2, key) in scrProjectThs" width="100" class="text-center">
                                {{item.projectMap[key] ? item.projectMap[key].value : '-'}}
                            </td>
                            <td class="text-center">{{item.total}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div ref="tableBody" class="table-body">
                <div class="table-body-wrap" :style="style">
                    <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
                           style="table-layout: fixed" cellspacing="0" cellpadding="0">
                        <thead>
                        <tr>
                            <th ref="tableTh" width="286"
                                :class="sortClassName('user.no')"
                                @click.stop="handleChangeSort('user.no')"
                                style="cursor: pointer;">学生信息
                                <span class="caret-wrapper">
                                    <i class="sort-caret ascending"></i>
                                    <i class="sort-caret descending"></i>
                                </span>
                            </th>
                            <th v-for="item in scrProjectThs" width="100" class="text-center"
                                :class="sortClassName(item)" @click.stop="handleChangeSort(item)"
                                style="cursor: pointer;white-space: normal">
                                {{item}}
                                <span class="caret-wrapper">
                                    <i class="sort-caret ascending"></i>
                                    <i class="sort-caret descending"></i>
                                </span>
                            </th>
                            <th width="100" class="text-center" :class="sortClassName('总分')"
                                @click.stop="handleChangeSort('总分')"
                                style="cursor: pointer;">总分
                                <span class="caret-wrapper">
                                    <i class="sort-caret ascending"></i>
                                    <i class="sort-caret descending"></i>
                                </span>
                            </th>
                        </tr>
                        </thead>
                        <tbody ref="tableTBody">
                        <tr v-for="item in cpScrApplyUser">
                            <td width="286">
                                <credit-user-column :user="item.user" cn-name></credit-user-column>
                            </td>
                            <td v-for="(item2, key) in scrProjectThs" width="100" class="text-center">
                                {{item.projectMap[key] ? item.projectMap[key].value : '-'}}
                            </td>
                            <%--<td>cces1</td>--%>
                            <td class="text-center">{{item.total}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div v-show="hasTranslate" class="table-fixed-right" :class="rightShadow">
                <span class="btn-el-icon-right" @click.stop="scrollRight" :style="{top: arrowTop}">
                    <i class="el-icon-arrow-right"></i>
                </span>
                <div class="table-fixed-right-wrap" :style="{width: maxWidth+'px'}">
                    <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
                           style="table-layout: fixed" cellspacing="0" cellpadding="0">
                        <thead>
                        <tr>
                            <th width="286"
                                :class="sortClassName('user.no')"
                                @click.stop="handleChangeSort('user.no')"
                                style="cursor: pointer;">学生信息
                                <span class="caret-wrapper">
                                    <i class="sort-caret ascending"></i>
                                    <i class="sort-caret descending"></i>
                                </span>
                            </th>
                            <th v-for="item in scrProjectThs" width="100" class="text-center"
                                :class="sortClassName(item)" @click.stop="handleChangeSort(item)"
                                style="cursor: pointer;white-space: normal">
                                {{item}}
                                <span class="caret-wrapper">
                                    <i class="sort-caret ascending"></i>
                                    <i class="sort-caret descending"></i>
                                </span>
                            </th>
                            <th width="100" class="text-center" :class="sortClassName('总分')"
                                @click.stop="handleChangeSort('总分')"
                                style="cursor: pointer;">总分
                                <span class="caret-wrapper">
                                    <i class="sort-caret ascending"></i>
                                    <i class="sort-caret descending"></i>
                                </span>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="item in cpScrApplyUser">
                            <td width="286">
                                <credit-user-column :user="item.user" cn-name></credit-user-column>
                            </td>
                            <td v-for="(item2, key) in scrProjectThs" width="100" class="text-center">
                                {{item.projectMap[key] ? item.projectMap[key].value : '-'}}
                            </td>
                            <td class="text-center">{{item.total}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="text-right mgb-20">
            <el-pagination
                    size="small"
                    @size-change="handlePSizeChange"
                    background
                    @current-change="handlePCPChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20]"
                    :page-size="searchListForm.pageSize"
                    layout="total,prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>
</div>

<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var officeList = JSON.parse(JSON.stringify(${fns: toJson(fns: getOfficeList())}));
            return {
                officeList: officeList,
                searchListForm: {
                    'user.office.id': '',
                    pageNo: 1,
                    pageSize: 10,
                    keys: '',
                    'orderBy': '',
                    'orderByType': '',
                },
                loading: false,
                scrApplyUser: [],

                pageCount: 0,
                columnIndex: 0,
                translateX: 0,
                winWidth: 0,
                bodyHeight: '',
                sortEntry: {}
            }
        },
        computed: {

            generateApplyList: function () {
                var scrApplyUser = this.scrApplyUser;
                var entryAll = {};
                return scrApplyUser.map(function (item, index) {
                    item.scrProjectList = [];
                    item.total = '';
                    for (var k in item) {
                        var entry = {};
                        var i = 1;
                        i *= index;
                        if (item.hasOwnProperty(k)) {
                            if(k === 'user' || k === 'scrProjectList' || k === 'total'){
                                continue;
                            }
                            if (!entryAll[k]) {
                                var time = new Date().getTime();
                                entryAll[k] = (time + Math.random() * time + i + index).toString();
                            }
                            entry['name'] = k;
                            entry['id'] = entryAll[k];
                            entry['value'] = item[k];
                            if(k === '创新创业课程'){
                                item.scrProjectList.unshift(entry);
                            }else if(k === '总分'){
                                item.total = item[k]
                            }else {
                                item.scrProjectList.push(entry);
                            }
                        }
                        i++;
                    }
                    return item;
                });
            },

            cpScrApplyUser: function () {
                return this.generateApplyList.map(function (applyUser) {
                    var projectList = applyUser.scrProjectList || [];
                    var entries = {};
                    projectList.forEach(function (item) {
                        entries[item.id] = item;
                    });
                    applyUser.projectMap = entries;
                    return applyUser;
                });
            },

            scrProjectThs: function () {
                var entries = {};
                this.generateApplyList.forEach(function (applyUser) {
                    var projectList = applyUser.scrProjectList || [];
                    projectList.forEach(function (item) {
                        if (!entries[item.id]) {
                            entries[item.id] = item.name
                        }
                    })
                });
                return entries;
            },

            thWidths: function () {
                var scrProjectThs = this.scrProjectThs;
                var arr = [286];
                for (var k in scrProjectThs) {
                    if (scrProjectThs.hasOwnProperty(k)) {
                        arr.push(100)
                    }
                }
                arr.push(100);
                return arr;
            },

            colleges: function () {
                return this.officeList.filter(function (item) {
                    return item.grade === '2';
                })
            },


            maxWidth: function () {
                var widths = 0;
                for (var k in this.scrProjectThs) {
                    if (this.scrProjectThs.hasOwnProperty(k)) {
                        widths += 100;
                    }
                }
                widths += 100;
                widths += 286;
                if (widths < this.winWidth) {
                    return this.winWidth
                }
                return widths;
            },

            hasTranslate: function () {
                return this.winWidth < this.maxWidth;
            },

            minTranslateX: function () {
                return this.winWidth - this.maxWidth;
            },

            maxColumnIndex: function () {
                return Math.ceil(-this.minTranslateX / 100);
            },

            style: function () {
                return {
                    width: (this.maxWidth) + 'px',
                    transform: 'translate(' + (this.translateX) + 'px, 0px)'
                }
            },

            leftShadow: function () {
                return {
                    'table-fixed-left-shadow': this.translateX != 0
                }
            },


            rightShadow: function () {
                return {
                    'table-fixed-right-shadow': this.translateX !== this.minTranslateX
                }
            },

            arrowTop: function () {
                return this.hasTranslate ? (this.bodyHeight - 30) / 2 + $(this.$refs.tableTh).innerHeight() + 'px' : ''
            }

        },
        watch: {
            hasTranslate: function (value) {
                if (value) {
                    this.getBodyHeight();
                }
            }
        },
        methods: {

            getBodyHeight: function () {
                this.bodyHeight = $(this.$refs.tableTBody).innerHeight();
            },

            sortClassName: function (key) {
                var isSortClass = this.sortEntry[key];
                return {
                    ascending: isSortClass && isSortClass.indexOf('asc') > -1,
                    descending: isSortClass && isSortClass.indexOf('desc') > -1
                }
            },

            handleChangeSort: function (key) {
                var sortValue = this.sortEntry[key];
                for (var k in this.sortEntry) {
                    if (this.sortEntry.hasOwnProperty(k)) {
                        if (k !== key) {
                            Vue.set(this.sortEntry, k, null);
                        }
                    }
                }
                if (!sortValue) {
                    sortValue = 'asc';
                    this.sortEntry[key] = sortValue;
                } else if (sortValue === 'asc') {
                    sortValue = 'desc';
                    this.sortEntry[key] = sortValue;
                } else {
                    Vue.set(this.sortEntry, key, null);
                    sortValue = '';
                }
                if(key === 'user.no'){
                    key = '`'+key+'`';
                }
                if(sortValue){
                    this.searchListForm.orderBy = 't.'+key;
                    this.searchListForm.orderByType = sortValue ? ( sortValue.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                }else {
                    this.searchListForm.orderBy = '';
                    this.searchListForm.orderByType = '';
                }
                this.getScoCourseGradeList();
            },

            getScoCourseGradeList: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/scr/scoRsum/listPage?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || {};
                        self.scrApplyUser = data.list || [];
                        self.searchListForm.pageNo = data.pageNo || 1;
                        self.searchListForm.pageSize = data.pageSize || 10;
                        self.pageCount = data.count || 0;
                        self.$nextTick(function () {
                            self.getBodyHeight();
                        })
                    } else {
                        self.scrApplyUser = [];
                        self.pageCount = 0;
                        self.$message.error(data.msg);
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            },
            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getScoCourseGradeList();
            },

            handlePCPChange: function () {
                this.getScoCourseGradeList();
            },

            scrollRight: function () {
                this.columnIndex += 1;
                if (this.columnIndex > this.maxColumnIndex) {
                    this.columnIndex = this.maxColumnIndex;
                }
                var translateX = this.translateX;
                translateX -= 100;
                this.translateX = translateX < this.minTranslateX ? this.minTranslateX : translateX
            },
            scrollLeft: function () {
                this.columnIndex -= 1;
                if (this.columnIndex < 0) {
                    this.columnIndex = 0;
                }
                var translateX = this.translateX;
                translateX += 100;
                this.translateX = translateX > 0 ? 0 : translateX;
            },

            getWinWidth: function () {
                this.winWidth = $(this.$refs.tableFixedContainer).width();
            }
        },


        created: function () {

            this.getScoCourseGradeList();
        },

        mounted: function () {
            var timer = null;
            var self = this;
            this.getWinWidth();
            this.getBodyHeight();
            $(window).on('resize', function (event) {
                timer && clearTimeout(timer);
                timer = setTimeout(function () {
                    self.getWinWidth();
                    if (!self.hasTranslate) {
                        self.translateX = 0;
                        self.columnIndex = 0;
                    }
                }, 13)
            })
        }
    })

</script>
</body>
</html>