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
        <edit-bar second-name="楼栋查看" href="/pw/pwSpace/list"></edit-bar>
    </div>

    <el-row :gutter="20" label-width="120px">
        <el-col :span="20" v-if="school">
            <e-col-item label="所属学院：">{{school}}</e-col-item>
        </el-col>
        <el-col :span="20" v-if="campus">
            <e-col-item label="所属校区：">{{campus}}</e-col-item>
        </el-col>
        <el-col :span="20" v-if="base">
            <e-col-item label="所属基地：">{{base}}</e-col-item>
        </el-col>
        <el-col :span="20">
            <e-col-item label="楼栋名称：">{{pwSpace.name}}</e-col-item>
        </el-col>
        <el-col :span="20">
            <e-col-item label="负责人：">{{pwSpace.person}}</e-col-item>
        </el-col>
        <el-col :span="20">
            <e-col-item label="联系方式：">{{pwSpace.phone}}</e-col-item>
        </el-col>
        <el-col :span="20">
            <e-col-item label="开放时间：">
                <div class="open-time-cell">
                    <div>
                        <div class="open-time-cell-label">开放日：</div>
                        {{openWeeksStr(pwSpace.openWeeks)}}
                    </div>
                    <div>
                        <div class="open-time-cell-label">时间：</div>
                        <div class="open-time-cell-date">
                            <div>上午：{{pwSpace.amOpenStartTime}}-{{pwSpace.amOpenEndTime}}</div>
                            <div>下午：{{pwSpace.pmOpenStartTime}}-{{pwSpace.pmOpenEndTime}}</div>
                        </div>
                    </div>
                </div>
            </e-col-item>
        </el-col>
        <el-col :span="20">
            <e-col-item label="楼层：">{{pwSpace.floorNum}}</e-col-item>
        </el-col>
        <el-col :span="20">
            <e-col-item label="占地面积：">{{pwSpace.area}}平方米</e-col-item>
        </el-col>
        <el-col :span="20">
            <e-col-item label="建筑图片：">
                <img :src="src" alt="" style="width:270px;">
            </e-col-item>
        </el-col>
        <el-col :span="20">
            <e-col-item label="备注：" class="white-space-pre-static word-break"><span v-html="pwSpace.remarks"></span></e-col-item>
        </el-col>
    </el-row>

</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var pwSpace = JSON.parse(JSON.stringify(${fns:toJson(pwSpace)})) || [];
            return {
                pwSpace: pwSpace,
                school:'${school}',
                campus:'${campus}',
                base:'${base}',
                message: '${message}',
                src:''
            }
        },
        methods: {
            openWeeksStr: function (arr) {
                var openWeeksLabel = ['每周一','每周二','每周三','每周四','每周五','每周六','每周日'];
                var strArr = [],str = '';
                if(!arr || arr.length == 0){
                    return '';
                }
                if(arr.length == 7){
                    str = '不限';
                    return str;
                }
                arr.forEach(function (item) {
                    strArr.push(openWeeksLabel[item-1]);
                });
                str = strArr.join('、');
                return str;
            },
            goToBack: function () {
                window.location.href = this.frontOrAdmin + '/pw/pwSpace/list';
            }
        },
        created: function () {
            this.src = this.addFtpHttp(this.pwSpace.imageUrl);
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