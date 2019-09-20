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
        <cs-panel class="mgb-20" label="参与创新创业基本概况">
            <div class="analyze-sketch-container">
                <div class="analyze-sketch-item">
                    <div class="analyze-sketch-item_top">
                        <div class="ask-top_icon">
                            <img src="${ctxImages}/province/province/xuexiao.png"/>
                        </div>
                        <div class="ask-top_content">
                            <div class="ask-top_label">
                                高校数量
                            </div>
                            <div class="ask-top_value">
                                82
                            </div>
                        </div>
                    </div>
                    <div class="ask-main_content">
                        <div class="ask-main-content_item">
                            <div class="ask-mc_label">
                                部属高校
                            </div>
                            <div class="ask-mc_value">
                                8
                            </div>
                        </div>
                        <div class="ask-main-content_item">
                            <div class="ask-mc_label">
                                省属本科
                            </div>
                            <div class="ask-mc_value">
                                34
                            </div>
                        </div>
                        <div class="ask-main-content_item span8">
                            <div class="ask-mc_label">
                                民办本科
                            </div>
                            <div class="ask-mc_value">
                                8
                            </div>
                        </div>
                        <div class="ask-main-content_item span8">
                            <div class="ask-mc_label">
                                高职高专
                            </div>
                            <div class="ask-mc_value">
                                30
                            </div>
                        </div>
                        <div class="ask-main-content_item span8">
                            <div class="ask-mc_label">
                                独立院校
                            </div>
                            <div class="ask-mc_value">
                                10
                            </div>
                        </div>
                    </div>
                </div>
                <div class="analyze-sketch-item">
                    <div class="analyze-sketch-item_top">
                        <div class="ask-top_icon">
                            <img src="${ctxImages}/province/province/yonghu.png"/>
                        </div>
                        <div class="ask-top_content">
                            <div class="ask-top_label">
                                学生人数
                            </div>
                            <div class="ask-top_value">
                                89632
                            </div>
                        </div>
                    </div>
                    <div class="ask-main_content">
                        <div class="ask-main-content_item">
                            <div class="ask-mc_label">
                                专科
                            </div>
                            <div class="ask-mc_value">
                                29635
                            </div>
                        </div>
                        <div class="ask-main-content_item">
                            <div class="ask-mc_label">
                                学士
                            </div>
                            <div class="ask-mc_value">
                                46321
                            </div>
                        </div>
                        <div class="ask-main-content_item">
                            <div class="ask-mc_label">
                                硕士
                            </div>
                            <div class="ask-mc_value">
                                9856
                            </div>
                        </div>
                        <div class="ask-main-content_item">
                            <div class="ask-mc_label">
                                博士
                            </div>
                            <div class="ask-mc_value">
                                3820
                            </div>
                        </div>
                    </div>
                </div>
                <div class="analyze-sketch-item">
                    <div class="analyze-sketch-item_top">
                        <div class="ask-top_icon">
                            <img src="${ctxImages}/province/province/toupian.png"/>
                        </div>
                        <div class="ask-top_content">
                            <div class="ask-top_label">
                                项目指导教师
                            </div>
                            <div class="ask-top_value">
                                9427
                            </div>
                        </div>
                    </div>
                    <div class="ask-main_content">
                        <div class="ask-main-content_item">
                            <div class="ask-mc_label">
                                校内导师
                            </div>
                            <div class="ask-mc_value">
                                8863
                            </div>
                        </div>
                        <div class="ask-main-content_item">
                            <div class="ask-mc_label">
                                校外导师
                            </div>
                            <div class="ask-mc_value">
                                564
                            </div>
                        </div>
                    </div>
                </div>
                <div class="analyze-sketch-item">
                    <div class="analyze-sketch-item_top">
                        <div class="ask-top_icon">
                            <img src="${ctxImages}/province/province/toupian.png"/>
                        </div>
                        <div class="ask-top_content">
                            <div class="ask-top_label">
                                专家评委
                            </div>
                            <div class="ask-top_value">
                                5237
                            </div>
                        </div>
                    </div>
                    <div class="ask-main_content">
                        <div class="ask-main-content_item">
                            <div class="ask-mc_label">
                                省级专家
                            </div>
                            <div class="ask-mc_value">
                                3965
                            </div>
                        </div>
                        <div class="ask-main-content_item">
                            <div class="ask-mc_label">
                                校级专家
                            </div>
                            <div class="ask-mc_value">
                                1272
                            </div>
                        </div>
                    </div>
                </div>
                <div class="analyze-sketch-item">
                    <div class="analyze-sketch-item_top">
                        <div class="ask-top_icon">
                            <img src="${ctxImages}/province/province/toupian.png"/>
                        </div>
                        <div class="ask-top_content">
                            <div class="ask-top_label">
                                项目总数
                            </div>
                            <div class="ask-top_value">
                                35632
                            </div>
                        </div>
                    </div>
                    <div class="ask-main_content">
                        <div class="ask-main-content_item">
                            <div class="ask-mc_label">
                                互联网+大赛
                            </div>
                            <div class="ask-mc_value">
                                10092
                            </div>
                        </div>
                        <div class="ask-main-content_item">
                            <div class="ask-mc_label">
                                国创项目
                            </div>
                            <div class="ask-mc_value">
                                25634
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </cs-panel>


        <el-row class="ask-el-row" :gutter="10">
            <el-col :span="8">
                <cs-panel label="创新创业项目发展趋势">
                    <el-radio-group v-model="qsValue" slot="header">
                        <el-radio label="1">项目数</el-radio>
                        <el-radio label="2">参与学生人数</el-radio>
                    </el-radio-group>
                    <div class="example-block">
                        <img v-show="qsValue === '1'" src="${ctxImages}/province/province/fzqs1.png"/>
                        <img v-show="qsValue === '2'" src="${ctxImages}/province/province/fzqs2.png"/>
                    </div>
                </cs-panel>
            </el-col>
            <el-col :span="8">
                <cs-panel label="各高校参与创新创业项目活跃度">
                    <div class="example-block">
                        <img src="${ctxImages}/province/province/hy.png" style="height: 100%;width: auto"/>
                    </div>
                </cs-panel>
            </el-col>
            <el-col :span="8">
                <cs-panel label="各类项目下发资金统计">
                    <el-date-picker
                            slot="header"
                            style="width: 120px;"
                            v-model="zjValue"
                            type="year"
                            placeholder="选择年"
                            size="mini"
                    >
                    </el-date-picker>
                    <div class="example-block">
                        <img src="${ctxImages}/province/province/zj1.png"/>
                    </div>
                </cs-panel>
            </el-col>
            <el-col :span="8">
                <cs-panel label="创新创业项目分布比例">
                    <div class="example-block">
                        <img src="${ctxImages}/province/province/fb.png"/>
                    </div>
                </cs-panel>
            </el-col>
            <el-col :span="8">
                <cs-panel label="参与项目指导教师分布">
                    <div class="example-block">
                        <img src="${ctxImages}/province/province/jsfb.png" style="height: 100%;width: auto"/>
                    </div>
                </cs-panel>
            </el-col>
            <el-col :span="8">
                <cs-panel label="参与各类项目人员分布">
                    <el-date-picker
                            slot="header"
                            style="width: 120px;"
                            v-model="ryfbValue"
                            type="year"
                            placeholder="选择年"
                            size="mini"
                    >
                    </el-date-picker>
                    <div class="example-block">
                        <img src="${ctxImages}/province/province/ryfb.png"/>
                    </div>
                </cs-panel>
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
                qsValue: "1",
                zjValue: "",
                ryfbValue: ""
            }
        }
    });

</script>

</body>
</html>