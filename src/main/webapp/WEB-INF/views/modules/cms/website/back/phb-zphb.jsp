<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative-province.jsp" %>
</head>
<body>


<div id="app" v-show="pageLoad" style="display: none">
    <edit-bar-province></edit-bar-province>


    <div class="content-container">
        <el-row class="mgb-20" :gutter="10">
            <el-col :span="8">
                <cs-panel label="申报项目总数排名Top5">
                    <a href="javascript:void (0);" slot="header">查看更多排名</a>
                    <div class="school-apply-rank">
                        <div class="school-apply-rank-item">
                            <div class="snr-scn">
                                <span class="snr-scn_index">1</span>
                                武汉大学
                            </div>
                            <div class="sar-num">
                                8966
                            </div>
                        </div>
                        <div class="school-apply-rank-item">
                            <div class="snr-scn">
                                <span class="snr-scn_index">2</span>
                                武汉理工大学
                            </div>
                            <div class="sar-num">
                                8745
                            </div>
                        </div>
                        <div class="school-apply-rank-item">
                            <div class="snr-scn">
                                <span class="snr-scn_index">3</span>
                                华中科技大学
                            </div>
                            <div class="sar-num">
                                7963
                            </div>
                        </div>
                        <div class="school-apply-rank-item">
                            <div class="snr-scn">
                                <span class="snr-scn_index">4</span>
                                华中农业大学
                            </div>
                            <div class="sar-num">
                                7896
                            </div>
                        </div>
                        <div class="school-apply-rank-item">
                            <div class="snr-scn">
                                <span class="snr-scn_index">5</span>
                                华中科技大学
                            </div>
                            <div class="sar-num">
                                7456
                            </div>
                        </div>
                    </div>
                </cs-panel>
            </el-col>
            <el-col :span="8">
                <cs-panel label="参与创新创业的活跃度排名Top5">
                    <a href="javascript:void (0);" slot="header">查看更多排名</a>
                    <div class="example-block">
                        <img src="${ctxImages}/province/province/hydtop5.jpg"/>
                    </div>
                </cs-panel>
            </el-col>
            <el-col :span="8">
                <cs-panel label="参与创新创业活动的学校导师数量Top5">
                    <a href="javascript:void (0);" slot="header">查看更多排名</a>
                    <div class="example-block">
                        <img src="${ctxImages}/province/province/dssltop5.jpg"/>
                    </div>
                </cs-panel>
            </el-col>
        </el-row>
        <cs-panel class="mgb-20" label="创新创业课程排名">
            <a href="javascript:void (0);" slot="header">查看更多排名</a>
            <el-row :gutter="10">
                <el-col :span="8">
                    <div class="course-rank-item">
                        <div class="example-block" style="border-left: 0">
                            <img src="${ctxImages}/province/province/kksltop5.jpg"/>
                        </div>
                    </div>
                </el-col>
                <el-col :span="8">
                    <div class="course-rank-item">
                        <div class="example-block">
                            <img src="${ctxImages}/province/province/rementop5.jpg"/>
                        </div>
                    </div>
                </el-col>
                <el-col :span="8">
                    <div class="course-rank-item">
                        <div class="example-block">
                            <img src="${ctxImages}/province/province/shtop5.jpg"/>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </cs-panel>
        <el-row class="mgb-20" :gutter="10">
            <el-col :span="12">
                <cs-panel label="荣获项目资金排名Top5">
                    <a href="javascript:void (0);" slot="header">查看更多排名</a>
                    <div class="example-block">
                        <img src="${ctxImages}/province/province/zjtop5.jpg"/>
                    </div>
                </cs-panel>
            </el-col>
            <el-col :span="12">
                <cs-panel label="热门技术排名Top10">
                    <a href="javascript:void (0);" slot="header">查看更多排名</a>
                    <div class="example-block">
                        <img src="${ctxImages}/province/province/rementop5.jpg"/>
                    </div>
                </cs-panel>
            </el-col>
        </el-row>
    </div>

</div>

<script>
    'use strict';

    new Vue({
        el: '#app'
    });

</script>

</body>
</html>
