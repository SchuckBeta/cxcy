<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <style>
        .table-fixed-container {
            position: relative;
            width: 100%;
            overflow: hidden;
        }

        .table-fixed-container .table-body {
            width: 100%;
        }

        .table-fixed-container .table-body-wrap {
            transform: translate(0, 0);
            transition: transform 0.15s linear;
        }

        .table-fixed-left {
            position: absolute;
            left: 0;
            top: 0;
            width: 286px;
            overflow-x: hidden;
            overflow-y: auto;
            z-index: 100;
        }

        .table-fixed-left-shadow {
            box-shadow: 0 0 10px rgba(0, 0, 0, .12);
        }

        .table-fixed-right-shadow {
            box-shadow: 0 0 10px rgba(0, 0, 0, .12);
        }

        .table-fixed-right {
            position: absolute;
            right: 0;
            left: auto;
            top: 0;
            height: 190px;
            width: 100px;
            overflow-x: hidden;
            overflow-y: hidden;
        }

        .table-fixed-right-wrap {
            position: absolute;
            right: 0;
            top: 0;
            overflow: hidden;
        }

        .table-fixed-right .btn-el-icon-right {
            position: absolute;
            left: 0;
            top: 3px;
            z-index: 100;
        }

        .table-fixed-left .btn-el-icon-left {
            position: absolute;
            right: 0;
            top: 3px;
            z-index: 100;
        }

        .table-container-credit .btn-el-icon-left, .table-container-credit .btn-el-icon-right {
            width: 28px;
            height: 30px;
            cursor: pointer;
            font-size: 16px;
            text-align: center;
            line-height: 30px;
        }

    </style>
</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <edit-bar>总分查询</edit-bar>
    <div class="conditions">
        <e-condition type="radio" v-model="searchListForm['office.id']" label="学院"
                     :options="colleges" :default-props="{label: 'name', value: 'id'}"
                     @change="getScoCourseGradeList"></e-condition>
    </div>
    <div class="table-container table-container-credit">
        <div class="table-fixed-container">
            <div v-show="hasTranslate" class="table-fixed-left" :class="leftShadow">
                <span class="btn-el-icon-left" @click.stop="scrollLeft">
                      <i class="el-icon-arrow-left"></i>
                </span>
                <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
                       style="table-layout: fixed" cellspacing="0" cellpadding="0">
                    <thead>
                    <tr>
                        <th width="286">学生信息</th>
                        <th v-for="(item, key) in scrProjectThs" width="100" class="text-center">{{item}}</th>
                        <th width="100" class="text-center">总分</th>
                        <%--<th width="1920">11111</th>--%>
                        <%--<th width="100">cces1</th>--%>
                        <%--<th width="100">4444</th>--%>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="item in cpScrApplyUser">
                        <td width="286">
                            <credit-user-column :user="item"></credit-user-column>
                        </td>
                        <%--<td v-for="(item2, key) in scrProjectThs">{{item.projectMap}}{{item2}}{{key}}</td>--%>
                        <td v-for="(item2, key) in scrProjectThs" width="100" class="text-center">
                            {{item.projectMap[key] ? item.projectMap[key].value : '-'}}
                        </td>
                        <%--<td>cces1</td>--%>
                        <td class="text-center">{{item.total}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div ref="tableBody" class="table-body">
                <div class="table-body-wrap" :style="style">
                    <table
                            class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
                            style="table-layout: fixed" cellspacing="0" cellpadding="0">
                        <thead>
                        <tr>
                            <th width="286">学生信息</th>
                            <th v-for="item in scrProjectThs" width="100" class="text-center">{{item}}</th>
                            <%----%>
                            <%--<th width="1920">11111</th>--%>
                            <%--<th width="100">cces1</th>--%>
                            <th width="100" class="text-center">总分</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="item in cpScrApplyUser">
                            <td width="286">
                                <credit-user-column :user="item"></credit-user-column>
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
                <span class="btn-el-icon-right" @click.stop="scrollRight">
                    <i class="el-icon-arrow-right"></i>
                </span>
                <div class="table-fixed-right-wrap">
                    <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
                           style="table-layout: fixed" cellspacing="0" cellpadding="0">
                        <thead>
                        <tr>
                            <th width="286">学生信息</th>
                            <th v-for="item in scrProjectThs" width="100">{{item}}</th>
                            <%--<th width="1920">11111</th>--%>
                            <%--<th width="100">cces1</th>--%>
                            <th width="100" class="text-center">总分</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="item in cpScrApplyUser">
                            <td width="286">
                                <credit-user-column :user="item"></credit-user-column>
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
        </div>

        <div class="text-right mgb-20">
            <el-pagination
                    size="small"
                    @size-change="handlePSizeChange"
                    background
                    @current-change="handlePCPChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
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
                    'office.id': '',
                    pageNo: 1,
                    pageSize: 10,
                    'orderBy': '',
                    'orderByType': '',
                },
                scrApplyUser: [
                    {
                        admin: false,
                        age: "",
                        isNewRecord: true,
                        loginFlag: "1",
                        name: "王清腾",
                        no: "123456",
                        photo: "/tool/oseasy/user/2019-01-03/45a4fd92d2504e799e5effb6b88dd72a.jpg",
                        professional: "b1f6aa86365b486882e3166bd817ee99",
                        roleNames: "",
                        sysAdmin: false,
                        scrProjectList: [
                            {name: '创新创业课程', value: '1', id: '1'},
                            {name: '科学与技能学分', value: '2', id: '2'},
                            {name: '科研活动学分1', value: '3', id: '3'},
                            {name: '科研活动学分2', value: '3', id: '4'},
                            {name: '科研活动学分3', value: '3', id: '5'},
                            {name: '科研活动学分4', value: '3', id: '6'},
                            {name: '科研活动学分5', value: '3', id: '7'},
                            {name: '科研活动学分6', value: '3', id: '8'},
                            {name: '科研活动学分7', value: '3', id: '9'}
                        ]
                    },
                    {
                        admin: false,
                        age: "",
                        isNewRecord: true,
                        loginFlag: "1",
                        name: "王清腾",
                        no: "123456",
                        photo: "/tool/oseasy/user/2019-01-03/45a4fd92d2504e799e5effb6b88dd72a.jpg",
                        professional: "b1f6aa86365b486882e3166bd817ee99",
                        roleNames: "",
                        sysAdmin: false,
                        scrProjectList: [
                            {name: '创新创业课程', value: '1', id: '1'},
                            {name: '科研活动学分', value: '3', id: '3'}
                        ]
                    }
                ],

                pageCount: 0,
                columnIndex: 0,
                translateX: 0,
                winWidth: 0,
            }
        },
        computed: {

            cpScrApplyUser: function () {
                return this.scrApplyUser.map(function (applyUser) {
                    var projectList = applyUser.scrProjectList || [];
                    var entries = {};
                    var total = 0;
                    projectList.forEach(function (item) {
                        entries[item.id] = item;
                        total += parseFloat(item.value);
                    });
                    applyUser.projectMap = entries;
                    applyUser.total = total;
                    return applyUser;
                });
            },

            scrProjectThs: function () {
                var entries = {};
                this.scrApplyUser.forEach(function (applyUser) {
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
                    width: (this.maxWidth + 2) + 'px',
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
            }


        },
        methods: {
            getScoCourseGradeList: function () {

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
            }
        },

        created: function () {
            this.winWidth = $(window).width() - 40;
        },

        mounted: function () {
            var timer = null;
            var self = this;
            $(window).on('resize', function (event) {
                timer && clearTimeout(timer);
                timer = setTimeout(function () {
                    self.winWidth = $(window).width() - 40;
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