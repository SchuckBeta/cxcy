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
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar :second-name="secondName"></edit-bar>
    </div>
    <el-form :model="actYwTimeForm" ref="actYwTimeForm" :rules="actYwTimeRules" :disabled="disabled" size="mini" label-width="120px" :style="styleMin">
        <input type="hidden" name="id" :value="actYwTimeForm.id">
        <div style="width: 520px;">
            <el-form-item label="功能类型：">${fpType.name}</el-form-item>
            <el-form-item label="名称：">${actYw.proProject.projectName}</el-form-item>
            <el-form-item label="关联流程：">${actYwGroup.name}</el-form-item>
            <el-form-item prop="year" label="项目年份：">
                <el-date-picker
                        v-model="actYwTimeForm.year"
                        type="year"
                        value-format="yyyy"
                        placeholder="选择年份">
                </el-date-picker>
            </el-form-item>
            <el-form-item prop="daterange" ref="proDate" label="项目时间：">
                <el-date-picker
                        v-model="actYwTimeForm.daterange"
                        @change="handleChangeProDate"
                        @focus="focusProDate('proDate')"
                        type="datetimerange"
                        unlink-panels
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        format="yyyy-MM-dd HH:mm:ss"
                        :default-time="proDateDefaultTime"
                        value-format="yyyy-MM-dd HH:mm:ss">
                </el-date-picker>
            </el-form-item>
            <template v-if="hasShowTime">
                <el-form-item prop="nodeState" label="申报时间：">
                    <el-switch v-model="actYwTimeForm.nodeState"></el-switch>
                </el-form-item>
                <el-form-item prop="showTime" label="显示时间：">
                    <el-switch v-model="actYwTimeForm.showTime" active-value="1" inactive-value="0"></el-switch>
                </el-form-item>
            </template>
        </div>
        <el-form-item label="流程节点时间：">
            <el-table :data="actYwTimeForm.actYwGtimeList" size="small" class="table table-act-yw-time"
                      style="width: 960px;" border>
                <el-table-column label="流程节点" width="160">
                    <template slot-scope="scope">
                        {{scope.row.gnodeName}}
                    </template>
                </el-table-column>
                <el-table-column label="有效期">
                    <template slot-scope="scope">
                        <el-form-item :inline-message="true" :prop="'actYwGtimeList.'+ scope.$index + '.daterange'" :ref="'actYwGtimeList.'+ scope.$index"
                                      :rules="{ required: true, message: '请选择有效期', trigger: 'change'}" label-width="0"
                                      style="margin-bottom: 0">
                            <el-tooltip class="item" effect="dark" popper-class="white"
                                        :content="scope.row.isApplyDate ? applyDateTip : nodeDateTip" placement="right">
                                <el-date-picker
                                        style="width: 350px;"
                                        @focus="focusProDate('actYwGtimeList.'+ scope.$index)"
                                        v-model="scope.row.daterange"
                                        type="datetimerange"
                                        :default-value="scope.row.defaultMaxDate"
                                        unlink-panels
                                        range-separator="至"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期"
                                        :picker-options="pickerOptionsNode(scope.$index)"
                                        :default-time="scope.row.isApplyDate ? defaultApplyDatetimerange : defaultDatetimerange"
                                        format="yyyy-MM-dd HH:mm:ss"
                                        value-format="yyyy-MM-dd HH:mm:ss">
                                </el-date-picker>
                            </el-tooltip>
                        </el-form-item>
                    </template>
                </el-table-column>
                <%--<el-table-column label="导入导出模板">--%>
                    <%--<template slot-scope="scope">--%>
                        <%--<el-form-item v-if="!scope.row.isApplyDate" :inline-message="true"--%>
                                      <%--:prop="'actYwGtimeList.'+ scope.$index + '.excelTplPath'"--%>
                                      <%--label-width="0" style="margin-bottom: 0">--%>
                            <%--<el-select placeholder="请选择" v-model="scope.row.excelTplPath"--%>
                                       <%--clearable filterable--%>
                                       <%--:disabled="!scope.row.hasTpl">--%>
                                <%--<el-option v-for="expType in expTypes" :key="expType.tplpext + expType.tplname"--%>
                                           <%--:value="expType.tplpext + expType.tplname" :label="expType.name"></el-option>--%>
                            <%--</el-select>--%>
                            <%--<el-checkbox v-model="scope.row.hasTpl">是</el-checkbox>--%>
                        <%--</el-form-item>--%>
                        <%--<div v-else>-</div>--%>
                    <%--</template>--%>
                <%--</el-table-column>--%>
            </el-table>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click.stop.prevent="validateForm">保存</el-button>
            <el-button @click.stop.prevent="goToBack">返回</el-button>
        </el-form-item>
    </el-form>
</div>

<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var actYw = JSON.parse(JSON.stringify(${fns: toJson(actYw)})) || {};
            var actYwGtimeList = JSON.parse(JSON.stringify(${fns:toJson(actYwGtimeList)})) || [];
            var actYwYear = JSON.parse(JSON.stringify(${fns: toJson(actYwYear)})) || {};
            var proProject = actYw.proProject || {};
            var startDate = actYwYear.startDate || proProject.startDate;
            var endDate = actYwYear.endDate  || proProject.endDate;
            var actYwTimeRang = [];
            var flowType = actYw.group ? actYw.group.flowType : '';
            var nodeState = actYw.proProject ? actYw.proProject.nodeState : '';
            var applyDate = {
                beginDate: moment(actYwYear.nodeStartDate).format('YYYY-MM-DD HH:mm:ss'),
                endDate: moment(actYwYear.nodeEndDate).format('YYYY-MM-DD HH:mm:ss'),
                gnode: {
                    name: '申报'
                },
                gnodeName:'申报',
                defaultMaxDate: '',
                isApplyDate: true
            };
            actYwGtimeList.unshift(applyDate);
            actYwGtimeList = actYwGtimeList.map(function (item) {
                var daterang = [];
                if (item.beginDate) {
                    daterang = [item.beginDate, item.endDate];
                }
                item['daterange'] = daterang;
                item.defaultMaxDate = '';
                console.log(item, item.gnode)
                return item;
            });


            if (startDate) {
                actYwTimeRang = [moment(startDate).format('YYYY-MM-DD HH:mm:ss'), moment(endDate).format('YYYY-MM-DD HH:mm:ss')];
            }

            return {
                actYwTimeForm: {
                    id: actYw.id,
                    year: actYwYear.year,
                    daterange: actYwTimeRang,
                    startDate: moment(startDate).format('YYYY-MM-DD HH:mm:ss'),
                    endDate: moment(endDate).format('YYYY-MM-DD HH:mm:ss'),
                    nodeState: nodeState,
                    showTime: actYw.showTime,
                    actYwGtimeList: actYwGtimeList
                },
                actYwTimeRules: {
                    year: [
                        {required: true, message: '请选择年份', trigger: 'change'}
                    ],
                    daterange: [
                        {required: true, message: '请选择项目时间', trigger: 'change'}
                    ]
                },
                disabled: false,
                flowType: flowType,
                expTypes: [],
                defaultDatetimerange: ['00:00:00', '23:59:59'],
                defaultApplyDatetimerange: ['00:00:00', '17:00:00'],
                proDateDefaultTime: ['00:00:00', '23:59:59'],
                applyDateTip: '建议结束时间选择工作日的工作时间9:00-17:00',
                nodeDateTip: '请选择的时间在上一个节点日期之后',
                styleMin: {minHeight: ''},
                isTL: actYw.group.theme === 70
            }
        },
        computed: {
            secondName: function () {
                return this.actYwTimeForm.id ? '修改时间' : '添加时间'
            },
            hasShowTime: function () {
                return ["1", "13"].indexOf(this.flowType) === -1;
            }
        },
        methods: {
            focusProDate: function (refName) {
                var needHeight = $(this.$refs[refName].$el).offset().top + 428 + 28;
                var wHeight = $(window).height() ;
                var wScorllTop = $(window).scrollTop();
                if (needHeight > wHeight + wScorllTop) {
                    this.styleMin.minHeight = needHeight + 'px';
//                    console.log(needHeight - wHeight + wScorllTop)
                    this.$nextTick(function () {
//                        document.body.scrollTop = needHeight - wHeight + wScorllTop
                        $(document).scrollTop(needHeight - wHeight + wScorllTop);
                    })

//                    $(window).scrollTop(230)
                }
            },
            pickerOptionsNode: function ($index) {
                var maxDate = this.getTlMaxDate($index);
                var applyDateMaxDate = this.getApplyDateMaxDate();
                this.actYwTimeForm.actYwGtimeList[$index].defaultMaxDate = maxDate ? moment(maxDate).format('YYYY-MM-DD HH:mm:ss') : '';
                return {
                    disabledDate: function (time) {
                        if (maxDate === -1 && applyDateMaxDate === -1) {
                            return false;
                        }else if(applyDateMaxDate === -1 && maxDate !== -1){
                            return time.getTime() < maxDate;
                        }else {
                            return time.getTime() < maxDate || time.getTime() > applyDateMaxDate;
                        }

                    }
                }
            },

            getTlMaxDate: function ($index) {
                if (this.isTL){
                    var daterange = this.actYwTimeForm.actYwGtimeList[0].daterange;
                    if(daterange && daterange.length > 0 && $index > 1){
                        return new Date(daterange[1]).getTime();
                    }
                }
                return this.getBeforeListMaxDate($index);
            },

            getApplyDateMaxDate: function () {
                if(this.actYwTimeForm.daterange && this.actYwTimeForm.daterange.length > 0){
                    return new Date(this.actYwTimeForm.daterange[1]).getTime();
                };
                return -1;
            },

            getBeforeListMaxDate: function ($index) {
                var actYwGtimeList = this.actYwTimeForm.actYwGtimeList;
                actYwGtimeList = actYwGtimeList.slice(0, $index);
                actYwGtimeList = actYwGtimeList.map(function (item) {
                    return item.daterange || [];
                });
                actYwGtimeList = actYwGtimeList.reduce(function (arr, item) {
                    return arr.concat(item);
                }, []);
                if(this.actYwTimeForm.daterange && this.actYwTimeForm.daterange.length > 0){
                    actYwGtimeList.unshift(this.actYwTimeForm.daterange[0])
                };
                actYwGtimeList = actYwGtimeList.map(function (item) {
                    return new Date(item).getTime();
                });
                if (actYwGtimeList.length === 0) {
                    return -1;
                }
                return Math.max.apply(Math, actYwGtimeList);
            },

            handleChangeProDate: function (value) {
                if (value && value.length > 0) {
                    this.actYwTimeForm.startDate = value[0];
                    this.actYwTimeForm.endDate = value[1];
                } else {
                    this.actYwTimeForm.startDate = '';
                    this.actYwTimeForm.endDate = '';
                }
            },
            getExpTypes: function () {
                var self = this;
                this.$axios.get('/impdata/ajaxExpTypes?isAll=true').then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        data = JSON.parse(data.datas);
                        self.expTypes = data;
                    }
                }).catch(function (error) {

                })
            },

            goToBack: function () {
                history.go(-1);
            },


            validateForm: function () {
                var self = this;
                this.$refs.actYwTimeForm.validate(function (valid) {
                    if (valid) {
                        var timesIsValid = self.timesIsValid();
                        if (timesIsValid.status === 'error') {
                            self.$alert(timesIsValid.message, '提示', {
                                type: timesIsValid.status
                            });
                            return false;
                        }
                        self.submitActYwForm();
                    }
                })
            },

            //提交表单
            submitActYwForm: function () {
                var self = this;
                var params = this.getActYwParams();
                this.disabled = true;
                this.$axios({
                    method: "GET",
                    url: '/actyw/actYw/ajaxGtime?'+params,
                }).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.$alert("保存成功", "提示", {
                            type: 'success'
                        }).then(function () {
                            location.href = '${ctx}/actyw/actYw/list?group.flowType=${actYw.group.flowType}'
                        }).catch(function () {

                        })
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.disabled = false;
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg);
                    self.disabled = false;
                })
            },

            //获取提交参数
            getActYwParams: function () {
                var params =  {};
                var paramsStr = [];
                var urlStrParams;
                var actYwTimeForm = this.actYwTimeForm;
                var actYwGtimeList = actYwTimeForm.actYwGtimeList;
                params.id = actYwTimeForm.id;
                params.showTime = actYwTimeForm.showTime ? '1' : '0';
                params.startYearDate = actYwTimeForm.startDate;
                params.endYearDate = actYwTimeForm.endDate;
                params['proProject.year'] = actYwTimeForm.year;
                params['proProject.nodeState'] = actYwTimeForm.nodeState ? '1' : '0';
                actYwGtimeList.forEach(function (item, index) {
                    if (item.isApplyDate) {
                        params.nodeStartDate = item.daterange[0];
                        params.nodeEndDate = item.daterange[1];
                    } else {
                        params['beginDate' + (index - 1)] = item.daterange[0];
                        params['endDate' + (index - 1)] = item.daterange[1];
                        params['hasTpl' + (index - 1)] = item.hasTpl ? '1' : '0';
                        params['excelTplClazz' + (index - 1)] = item.excelTplClazz;
                        params['excelTplPath' + (index - 1)] = item.excelTplPath;
                        paramsStr.push(("nodeId="+item.gnodeId));
//                        params['nodeId'+(index - 1)] = item.gnodeId;
//                        params["nodeIds"].push(item.gnodeId)
                    }
                });
                urlStrParams = Object.toURLSearchParams(params);
                urlStrParams += '&'+ paramsStr.join('&');
                return urlStrParams;
            },

            //获取所有时间数据
            getAllDateTimes: function () {
                var startDate = this.actYwTimeForm.startDate;
                var endDate = this.actYwTimeForm.endDate;
                var actYwGtimeList = this.actYwTimeForm.actYwGtimeList;
                var dateTimes = [];
                actYwGtimeList.forEach(function (item) {
                    var daterange = item.daterange;
                    var startTime = daterange[0];
                    var endTime = daterange[1];
                    dateTimes.push({
                        name: item.gnode ? item.gnode.name : '',
                        time: moment(startTime).valueOf()
                    });
                    dateTimes.push({
                        name: item.gnode ? item.gnode.name : '',
                        time: moment(endTime).valueOf()
                    });
                });
                dateTimes.unshift({
                    name: '项目开始',
                    time: moment(startDate).valueOf()
                });
                dateTimes.push({
                    name: '项目结束',
                    time: moment(endDate).valueOf()
                });
                return dateTimes;
            },
            //判断时间是否合格
            timesIsValid: function () {
                if(this.isTL){
                    return {
                        status: true
                    };
                }
                var dateTimes = this.getAllDateTimes();
                for (var i = 0; i < dateTimes.length - 1; i++) {
                    var item = dateTimes[i];
                    var item2 = dateTimes[i + 1];
                    if (item.time > item2.time) {
                        return {
                            status: 'error',
                            message: item.name + '时间大于' + item2.name + '时间'
                        };
                    }
                }
                return {
                    status: true
                };
            }
        },
        created: function () {
            this.getExpTypes();
        }
    })

</script>
</body>
</html>