<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" v-show="pageLoad" class="container-fluid mgb-60" style="display: none">
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>
    <div class="search-block_bar clearfix">
        <div class="search-btns">
            <el-button type="primary" size="mini" @click.stop.prevent="openDialogCreditType"><i
                    class="el-icon-circle-plus el-icon--left"></i>创建学分类别
            </el-button>
            <el-button type="primary" size="mini" @click.stop.prevent="openDialogCreditRule"><i
                    class="el-icon-circle-plus el-icon--left"></i>设置学分规则
            </el-button>
            <el-button size="mini" @click.stop="saveSorts">保存排序
            </el-button>
        </div>
        <div class="search-input">
            <input type="text" style="display: none">
            <el-input placeholder="学分类别/认定内容及标准"
                      v-model="searchListForm.keys"
                      size="mini"
                      @keyup.enter.native="searchText"
                      @change="searchText"
                      class="w300">
                <el-button slot="append" icon="el-icon-search"
                           @click.stop="searchText"></el-button>
            </el-input>
        </div>
    </div>
    <%--<div class="mgb-20">--%>
    <%--<el-button size="mini" @click.stop="toggleCollasped">{{isAllCollasped ? '展开' : '收起'}}</el-button>--%>
    <%--</div>--%>
    <div v-loading="loading" class="table-container">
        <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
               style="table-layout: fixed;"
               cellspacing="0" cellpadding="0">
            <thead>
            <tr>
                <th width="200"
                <%--:class="sortClassName('a.name')" @click.stop="handleChangeSort('a.name')" style="cursor: pointer"--%>
                >
                    <i v-show="creditRuleList.length > 0"
                       :class="{'el-icon-remove-outline':isAllCollasped,'el-icon-circle-plus-outline':!isAllCollasped}"
                       style="margin-right: 6px;color:#e9432d;cursor:pointer;" @click.stop="toggleCollasped"></i>
                    学分类别
                    <%--<span--%>
                    <%--class="caret-wrapper"><i class="sort-caret ascending"></i><i class="sort-caret descending"></i></span>--%>
                </th>
                <th width="260"
                <%--:class="sortClassName('a.sname')" @click.stop="handleChangeSort('a.sname')" style="cursor: pointer"--%>
                >认定内容
                    <%--<span class="caret-wrapper"><i--%>
                    <%--class="sort-caret ascending"></i><i class="sort-caret descending"></i></span>--%>
                </th>
                <th width="200">认定标准</th>
                <th width="90">分值</th>
                <%--<th width="80" class="text-center" :class="sortClassName('a.sort')"--%>
                <%--@click.stop="handleChangeSort('a.sort')" style="cursor: pointer">--%>
                <%--排序--%>
                <%--<span class="caret-wrapper"><i class="sort-caret ascending"></i><i--%>
                <%--class="sort-caret descending"></i></span>--%>
                <%--</th>--%>
                <th width="80" class="text-center">排序</th>
                <th>备注</th>
                <th width="180">操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-if="index > 0" v-for="(item, index) in creditRuleList" v-show="!item.isCollapsed">
                <td>
                    <div style="white-space: nowrap" v-if="item.type === '2'">
                        <span class="e-table-tree-dot" v-if="item.dots"
                              v-for="(dot, index) in item.dots.split('-')"></span>
                        <i class="el-icon-caret-right" :class="elIconCaret(item)"
                           @click.stop.prevent="handleExpandCell(item)"></i>
                        <span class="e-checkbox__label_dr_card">
                        <template v-if="!item.rule"> <el-tooltip class="item" popper-class="white" effect="dark"
                                                                 :content="item.name"
                                                                 placement="right"><span class="break-ellipsis"
                                                                                         style="max-width: 140px;"><keyword-font
                                :word="queryStrStatic"
                                :text="item.name"></keyword-font></span></el-tooltip></template>
                    </span>
                    </div>
                </td>
                <td style="white-space: nowrap">
                    <standard-name-column :type="item.type">
                        <span class="e-table-tree-dot" v-if="index > 1"
                              v-for="(dot, index) in item.dots.split('-')"></span>
                        <i class="el-icon-caret-right" :class="elIconCaret(item)"
                           @click.stop.prevent="handleExpandCell(item)"></i>
                        <span class="e-checkbox__label_dr_card">
                        <template v-if="!item.rule"><el-tooltip class="item" popper-class="white" effect="dark"
                                                                :content="item.name"
                                                                placement="right"><span class="break-ellipsis"
                                                                                        style="max-width: 180px;"><keyword-font
                                :word="queryStrStatic"
                                :text="item.name"></keyword-font></span></el-tooltip></template>
                    </span>
                    </standard-name-column>
                </td>
                <td style="white-space: nowrap">
                    <template v-if="item.rule">
                        <el-tooltip class="item" popper-class="white" effect="dark" :content="item.name"
                                    placement="right"><span class="break-ellipsis" style="max-width: 140px;"><keyword-font
                                :word="queryStrStatic" :text="item.name"></keyword-font></span></el-tooltip>
                    </template>
                </td>
                <td>{{item.score}}</td>
                <td>
                    <el-input v-model="item.sort" size="mini" maxlength="6"></el-input>
                </td>
                <td>
                    <el-tooltip class="item" popper-class="white" effect="dark" :content="item.remarks"
                                placement="right"><span class="break-ellipsis"
                                                        style="max-width: 90%;">{{item.remarks}}</span></el-tooltip>
                </td>
                <!--<td>{{item.addRuleAble}}</td>-->
                <td>
                    <div class="table-btns-action">
                        <el-button v-if="!item.rule" type="text" size="mini"
                                   @click.stop="addCreditType(item)">添加内容
                        </el-button>
                        <el-button v-if="!item.rule" type="text" size="mini"
                                   @click.stop.prevent="addCreditDetail(item)">添加标准
                        </el-button>
                        <el-button v-if="item.ptype === '2'" type="text" size="mini"
                                   @click.stop.prevent="openDialogCreditRatio(item)">学分配比
                        </el-button>
                        <template v-if="!item.rule">
                            <el-button type="text" size="mini" @click.stop="editCreditType(item)">编辑</el-button>
                            <el-button v-if="SKILLCREDITID !== item.id" type="text" size="mini"
                                       @click.stop="confirmCreditType(item, index)">删除
                            </el-button>
                        </template>
                        <template v-else-if="item.rule">
                            <el-button type="text" size="mini" @click.stop="editCreditDetail(item)">编辑</el-button>
                            <el-button type="text" size="mini" @click.stop="confirmDelCreditDetail(item, index)">删除
                            </el-button>
                        </template>

                    </div>
                </td>
            </tr>
            <tr v-if="creditRuleList.length === 0">
                <td colspan="7" class="text-center"><span class="empty">暂无数据</span></td>
            </tr>
            </tbody>
        </table>
    </div>

    <el-dialog
            :title="creditTypeTitle"
            :visible.sync="dialogVisibleCreditType"
            width="660px"
            :close-on-click-modal="false"
            :before-close="handleCloseCreditType">
        <el-form :model="creditTypeForm" ref="creditTypeForm" :validate-on-rule-change="false" :rules="creditTypeRules"
                 label-width="110px" size="mini"
                 :disabled="disabledCreditType">
            <el-row>
                <el-col :span="24">
                    <el-form-item prop="name" :label="creditTypeTitle+'：'">
                        <el-input name="name" v-model="creditTypeForm.name" maxlength="100"></el-input>
                    </el-form-item>
                </el-col>
                <%--<el-col :span="2">--%>
                <%--<el-form-item label-width="0" prop="scoRuleDetailMould.isCondcheck">--%>
                <%--<el-checkbox v-model="creditTypeForm.scoRuleDetailMould.isCondcheck" true-label="1"--%>
                <%--false-label="0"--%>
                <%--@change="handleChangeIsTypeCC"--%>
                <%--style="margin:0 17px 0;"></el-checkbox>--%>
                <%--</el-form-item>--%>
                <%--</el-col>--%>
                <%--<el-col :span="12">--%>
                <%--<el-form-item label-width="0" prop="scoRuleDetailMould.condition">--%>
                <%--<el-input v-model="creditTypeForm.scoRuleDetailMould.condition" maxlength="15"--%>
                <%--:disabled="creditTypeForm.scoRuleDetailMould.isCondcheck === '0'"--%>
                <%--class="input-with-select" class="w300">--%>
                <%--<el-select v-model="creditTypeForm.scoRuleDetailMould.condType" slot="prepend"--%>
                <%--placeholder="请选择"--%>
                <%--style="width: 100px;">--%>
                <%--<el-option v-for="item in regTypes" :label="item.rulename" :value="item.key"--%>
                <%--:key="item.key"></el-option>--%>
                <%--</el-select>--%>
                <%--<span slot="append">分</span>--%>
                <%--</el-input>--%>
                <%--</el-form-item>--%>
                <%--</el-col>--%>
            </el-row>
            <el-form-item prop="ptype" label="认定形式：">
                <el-radio-group v-model="creditTypeForm.ptype" :disabled="isEditSkill">
                    <el-radio v-for="item in modalities" :label="item.key" :key="item.key">{{item.name}}</el-radio>
                </el-radio-group>
                <span v-show="creditTypeForm.ptype === '2'" class="el-form-item-expository">支持一人及以上人数申请学分</span>
            </el-form-item>
            <%--<el-form-item label="认定学分：" prop="scoRuleDetailMould.score" class="w300">--%>
            <%--<el-input v-model="creditTypeForm.scoRuleDetailMould.score" maxlength="15"><span slot="append">分</span>--%>
            <%--</el-input>--%>
            <%--</el-form-item>--%>
            <el-form-item label="计算分值规则：" required>
                <div class="credit-detail-maxOS">
                    <el-form-item prop="scoRuleDetailMould.maxOrSum" label-width="0">
                        <el-select v-model="creditTypeForm.scoRuleDetailMould.maxOrSum" placeholder="请选择"
                                   @change="handleChangeTypeMaxOrSum">
                            <el-option v-for="item in scoRuleTypes" :key="item.key" :label="item.name"
                                       :value="item.key"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item style="margin-bottom: 0" label-width="0">
                        <el-col :span="14">
                            <el-form-item prop="scoRuleDetailMould.isLowSco" class="form-item-score-rule"
                                          style="margin-bottom: 0">
                                <el-checkbox v-model="creditTypeForm.scoRuleDetailMould.isLowSco"
                                             @change="handleChangeIsTypeLowSco"
                                             true-label="1"
                                             false-label="0">低于
                                </el-checkbox>
                            </el-form-item>
                            <el-form-item prop="scoRuleDetailMould.lowSco" label-width="0"
                                          class="form-item-score-rule"
                                          style="margin-bottom: 0">
                                <el-input v-model="creditTypeForm.scoRuleDetailMould.lowSco" maxlength="15"
                                          :disabled=" creditTypeForm.scoRuleDetailMould.isLowSco === '0'"
                                          class="form-item-score-rule-width"></el-input>
                                分按
                            </el-form-item>
                            <el-form-item prop="scoRuleDetailMould.lowScoMax" label-width="0"
                                          class="form-item-score-rule"
                                          style="margin-bottom: 0">
                                <el-input v-model="creditTypeForm.scoRuleDetailMould.lowScoMax" maxlength="15"
                                          :disabled=" creditTypeForm.scoRuleDetailMould.isLowSco === '0'"
                                          class=" form-item-score-rule-width"></el-input>
                                分计算
                            </el-form-item>
                        </el-col>
                        <el-col v-show="isShowJoinMaxType" :span="10">
                            <el-form-item prop="scoRuleDetailMould.isJoin" class="form-item-score-rule"
                                          style="margin-bottom: 0;margin-left: 5px;">
                                <el-checkbox v-model="creditTypeForm.scoRuleDetailMould.isJoin" true-label="1"
                                             false-label="0" @change="handleChangeIsTypeJoin">累计不超过
                                </el-checkbox>
                            </el-form-item>
                            <el-form-item prop="scoRuleDetailMould.joinMax" label-width="0"
                                          class="form-item-score-rule form-item-score-rule-width"
                                          style="margin-bottom: 0">
                                <el-input v-model="creditTypeForm.scoRuleDetailMould.joinMax" maxlength="15"
                                          :disabled="creditTypeForm.scoRuleDetailMould.isJoin === '0'"></el-input>
                            </el-form-item>
                            分
                        </el-col>
                    </el-form-item>
                </div>
            </el-form-item>
            <el-form-item prop="remarks" label="备注：">
                <el-input type="textarea" v-model="creditTypeForm.remarks" :rows="3" maxlength="1000"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button size="mini" @click.stop.prevent="handleCloseCreditType">取 消</el-button>
    <el-button size="mini" type="primary" @click.stop.prevent="validateCreditForm">确 定</el-button>
  </span>
    </el-dialog>


    <el-dialog
            title="标准"
            :visible.sync="dialogVisibleCreditDetail"
            width="660px"
            :close-on-click-modal="false"
            :before-close="handleCloseCreditDetail">
        <el-form :model="creditDetailForm" :rules="creditDetailRules" :validate-on-rule-change="false"
                 ref="creditDetailForm" size="mini"
                 label-width="110px" :disabled="disabledCreditDetail">
            <el-form-item :label="creditDetailForm.rule.type == '2' ? '学分类别：': '认定内容：'">
                {{creditDetailForm.rule.name}}
            </el-form-item>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="标准：" prop="name">
                        <el-input v-model="creditDetailForm.name" maxlength="100"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="2">
                    <el-form-item label-width="0" prop="isCondcheck">
                        <el-checkbox v-model="creditDetailForm.isCondcheck" true-label="1" false-label="0"
                                     @change="handleChangeIsCC"
                                     style="margin:0 17px 0;"></el-checkbox>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label-width="0" prop="condition">
                        <el-input v-model="creditDetailForm.condition" maxlength="15"
                                  :disabled="creditDetailForm.isCondcheck === '0'"
                                  class="input-with-select" class="w300">
                            <el-select v-model="creditDetailForm.condType" slot="prepend" placeholder="请选择"
                                       style="width: 100px;">
                                <el-option v-for="item in regTypes" :label="item.rulename" :value="item.key"
                                           :key="item.key"></el-option>
                            </el-select>
                            <span slot="append">分</span>
                        </el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item label="认定学分：" prop="score" class="w300">
                <el-input v-model.number="creditDetailForm.score" maxlength="15"><span slot="append">分</span></el-input>
            </el-form-item>
            <el-form-item label="计算分值规则：" required>
                <div class="credit-detail-maxOS">
                    <el-form-item prop="maxOrSum" label-width="0">

                        <el-select v-model="creditDetailForm.maxOrSum" placeholder="请选择" @change="handleChangeMaxOrSum">
                            <el-option v-for="item in scoRuleTypes" :key="item.key" :label="item.name"
                                       :value="item.key"></el-option>
                        </el-select>
                        <el-checkbox v-show="creditDetailForm.maxOrSum == '1' && isEditSkill" style="margin-left: 15px;" v-model="creditDetailForm.isSpecial" true-label="1"
                                     false-label="0">特例
                            <el-tooltip content="该标准若可以累计获得学分则勾选" popper-class="white" placement="bottom"><span
                                    style="padding: 0 6px;"><i class="el-icon-question"></i></span></el-tooltip>
                        </el-checkbox>

                    </el-form-item>
                    <el-form-item style="margin-bottom: 0" label-width="0">
                        <el-col :span="14">
                            <el-form-item prop="isLowSco" class="form-item-score-rule" style="margin-bottom: 0">
                                <el-checkbox v-model="creditDetailForm.isLowSco" @change="handleChangeIsLowSco"
                                             true-label="1"
                                             false-label="0">低于
                                </el-checkbox>
                            </el-form-item>
                            <el-form-item prop="lowSco" label-width="0"
                                          class="form-item-score-rule"
                                          style="margin-bottom: 0">
                                <el-input v-model="creditDetailForm.lowSco" maxlength="15"
                                          :disabled=" creditDetailForm.isLowSco === '0'"
                                          class="form-item-score-rule-width"></el-input>
                                分按
                            </el-form-item>
                            <el-form-item prop="lowScoMax" label-width="0"
                                          class="form-item-score-rule"
                                          style="margin-bottom: 0">
                                <el-input v-model="creditDetailForm.lowScoMax" maxlength="15"
                                          :disabled=" creditDetailForm.isLowSco === '0'"
                                          class=" form-item-score-rule-width"></el-input>
                                分计算
                            </el-form-item>
                        </el-col>
                        <el-col v-show="isShowJoinMax" :span="10">
                            <el-form-item prop="isJoin" class="form-item-score-rule"
                                          style="margin-bottom: 0;margin-left: 5px;">
                                <el-checkbox v-model="creditDetailForm.isJoin" true-label="1"
                                             false-label="0" @change="handleChangeIsJoin">累计不超过
                                </el-checkbox>
                            </el-form-item>
                            <el-form-item prop="joinMax" label-width="0"
                                          class="form-item-score-rule form-item-score-rule-width"
                                          style="margin-bottom: 0">
                                <el-input v-model="creditDetailForm.joinMax" maxlength="15"
                                          :disabled="creditDetailForm.isJoin === '0'"></el-input>
                            </el-form-item>
                            分
                        </el-col>
                    </el-form-item>
                </div>
            </el-form-item>
            <el-form-item prop="remarks" label="备注：">
                <el-input type="textarea" v-model="creditDetailForm.remarks" :rows="3" maxlength="1000"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button size="mini" @click.stop="handleCloseCreditDetail">取 消</el-button>
    <el-button size="mini" type="primary" @click.stop="validateCreditDetailForm">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
            title="认定学分规则"
            :visible.sync="dialogVisibleCreditRule"
            width="520px"
            :close-on-click-modal="false"
            :before-close="handleCloseCreditRule">
        <el-form :model="creditRuleForm" ref="creditRuleForm" :validate-on-rule-change="false" :rules="creditRuleRules"
                 size="mini"
                 :disabled="disabledCreditRule">
            <control-rule-block title="学分位数" class="auto control-rule-credit" required>
                <el-row>
                    <el-col :span="14">
                        <el-form-item prop="isKeepNpoint">
                            <el-radio-group v-model="creditRuleForm.isKeepNpoint" @change="handleChangeIsKeepNponit">
                                <el-radio label="1">系统自动保留小数
                                    <template v-if="creditRuleForm.keepNpoint">{{creditRuleForm.keepNpoint}}位</template>
                                </el-radio>
                                <el-radio label="0">取整数</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col v-show="creditRuleForm.isKeepNpoint === '1'" :span="10">
                        <el-form-item prop="keepNpoint">
                            <el-select v-model="creditRuleForm.keepNpoint" placeholder="请选择"
                                       style="width: 100px;">
                                <el-option value="1" label="1"></el-option>
                                <el-option value="2" label="2"></el-option>
                                <el-option value="3" label="3"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
            </control-rule-block>
            <control-rule-block title="分值处理" class="auto control-rule-credit" required>
                <el-form-item prop="isRprd">
                    <el-radio-group v-model="creditRuleForm.isRprd">
                        <el-radio label="1">最后一位四舍五入</el-radio>
                        <el-radio label="0">不处理</el-radio>
                    </el-radio-group>
                </el-form-item>
                <div class="credit-snum-block">
                    <el-form-item prop="isSnum" class="form-item-score-rule">
                        <el-checkbox v-model="creditRuleForm.isSnum" true-label="1" @change="handleChangeIsSm"
                                     false-label="0">低于
                        </el-checkbox>
                    </el-form-item>
                    <el-form-item prop="snumMin" label-width="0"
                                  class="form-item-score-rule">
                        <el-input :disabled="creditRuleForm.isSnum === '0'" v-model.number="creditRuleForm.snumMin"
                                  class="form-item-score-rule-width"
                                  maxlength="15"></el-input>
                        分按
                    </el-form-item>
                    <el-form-item prop="snumVal" label-width="0"
                                  class="form-item-score-rule">
                        <el-input :disabled="creditRuleForm.isSnum === '0'" v-model.number="creditRuleForm.snumVal"
                                  class="form-item-score-rule-width"
                                  maxlength="15"></el-input>
                        分计算
                    </el-form-item>
                </div>
            </control-rule-block>
            <control-rule-block title="学分限制" class="auto control-rule-credit" required>
                <el-form-item style="margin-bottom: 0">
                    <el-form-item prop="isSnumlimit" class="form-item-score-rule">
                        <el-checkbox v-model="creditRuleForm.isSnumlimit" true-label="1"
                                     false-label="0" @change="handleChangeIsSnumlimit">认定总分不得超过
                        </el-checkbox>
                    </el-form-item>
                    <el-form-item prop="snumlimit" label-width="0"
                                  class="form-item-score-rule">
                        <el-input v-model.number="creditRuleForm.snumlimit" class="form-item-score-rule-width"
                                  :disabled=" creditRuleForm.isSnumlimit === '0'"
                                  maxlength="15"></el-input>
                        分
                    </el-form-item>

                </el-form-item>
            </control-rule-block>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button size="mini" @click="handleCloseCreditRule">取 消</el-button>
    <el-button size="mini" type="primary" @click="validateCreditRuleForm">确 定</el-button>
  </span>
    </el-dialog>


    <el-dialog title="学分配比"
               :visible.sync="dialogVisibleCreditRatio"
               width="660px"
               :close-on-click-modal="false"
               :before-close="handleCloseCreditRatio">
        <el-form :model="creditRatioForm" ref="creditRatioForm" :validate-on-rule-change="false" :show-message="false"
                 :disabled="disabledCreditRatio" size="mini">
            <el-form-item class="text-right">
                <el-button type="primary" icon="el-icon-circle-plus"
                           @click.stop="addScoRulePb">添加
                </el-button>
            </el-form-item>
            <el-table class="table" :data="creditRatioForm.scoRulePbs" size="mini">
                <el-table-column prop="num" label="组人数" width="90" align="center"></el-table-column>
                <el-table-column prop="valArr" :render-header="creditRatioTableLabel">
                    <template slot-scope="scope">
                        <el-row>
                            <el-col :span="3" :key="value" v-for="(value, index) in scope.row.valArr">
                                <el-form-item :prop="'scoRulePbs.'+scope.$index+'.valArr.'+ index"
                                              :rules="creditRatioValRules"
                                              style="margin-bottom: 0; width: 40px; display: inline-block"
                                              :inline-message="true">
                                    <el-input type="number" v-model.number="scope.row.valArr[index]"
                                              maxlength="4"></el-input>
                                </el-form-item>
                                <template v-if="index < scope.row.valArr.length - 1"><span>:</span></template>
                            </el-col>
                        </el-row>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" width="80">
                    <template slot-scope="scope">
                        <el-button
                                :disabled="scope.$index != creditRatioForm.scoRulePbs.length - 1 || creditRatioForm.scoRulePbs.length == 1"
                                size="mini" icon="el-icon-remove" @click.stop="removeScoRule(scope.$index)"></el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button size="mini" @click="handleCloseCreditRatio">取 消</el-button>
    <el-button size="mini" type="primary" @click="validateCreditRatioForm">确 定</el-button>
  </span>
    </el-dialog>
</div>


<script>
    'use strict';

    Vue.component('keyword-font', {
        props: {
            word: String,
            text: String
        },
        render: function (createElement) {
            var text = this.text;
            if (this.word) {
                text = this.text.replace(new RegExp(this.word, 'g'), '<span style="font-weight: bold;color: #e9432d">' + this.word + '</span>');
            }
            return createElement('span', {
                domProps: {
                    innerHTML: text
                }
            });
        }
    });

    Vue.component('standard-name-column', {
        template: '<div><slot v-if="typeNum >= 3"></slot></div>',
        props: {
            type: String
        },
        computed: {
            typeNum: function () {
                return parseInt(this.type)
            }
        }
    })

    new Vue({
        el: '#app',
        mixins: [Vue.creditRatioMixin, Vue.creditDetailMixin],
        data: function () {
            return {
                SKILLCREDITID: '${SKILLCREDITID}',
                isAllCollasped: false, //存在问题
                sortEntry: {},
                creditRuleList: [],
                creditRuleTree: [],

                searchListForm: {
                    keys: ''
                },

                //创建学分类别
                dialogVisibleCreditType: false,
                disabledCreditType: false,
                creditTypeForm: {
                    id: '',
                    parent: {
                        id: ''
                    },
                    // parentId: '',
                    name: '',

                    scoRuleDetailMould: {
                        id: '',
//                        isCondcheck: '0', //是否选中内容标准分数
//                        condType: '10',//计算分值规则 默认等于
                        condName: '',//区间名称
//                        score: '',//认定学分
//                    joinType: '',//计算分值规则
                        condition: '',//内容标准分数
                        maxOrSum: '1', //分值规则类型
                        isLowSco: '0', //低于
                        lowSco: '',//低于分
                        lowScoMax: '',//最小分,
                        isJoin: '0',//是否累计,
                        joinMax: '',//累计最大值
                    },

                    type: '2', //级别：1：类型 ;2：类别（级别）;3、子类别（子级别）
                    ptype: '1', //'认定形式：1：个人 2：团队'
                    remarks: '',
                },


                //dialogVisibleCreditDetail
                dialogVisibleCreditDetail: false,
                disabledCreditDetail: false,
                creditDetailForm: {
                    id: '',
                    rule: {
                        id: '',
                        name: '',
                        type: ''
                    },
                    name: '',
                    isCondcheck: '0', //是否选中内容标准分数
                    condType: '',//计算分值规则 默认等于
                    condName: '',//区间名称
                    score: '',//认定学分
//                    joinType: '',//计算分值规则
                    condition: '',//内容标准分数
                    maxOrSum: '1', //分值规则类型
                    isSpecial: '0', //特例
                    isLowSco: '0', //低于
                    lowSco: '',//低于分
                    lowScoMax: '',//最小分,
                    isJoin: '0',//是否累计,
                    joinMax: '',//累计最大值
                    remarks: '' //备注
                },


                //dialogVisibleCreditRule
                dialogVisibleCreditRule: false,
                disabledCreditRule: false,
                creditRuleForm: {
                    id: '',
                    isKeepNpoint: '1', //'系统自动保留小数点 1：是自动，0不是自动',
                    keepNpoint: '2', //小数点位数
                    isRprd: '1',//是否最后一位四舍五入 1：是自动，0不是自动
                    isSnum: '', //是否低于X分（0否1是）
                    snumMin: '', //低于x分
                    snumVal: '',//按照x分,
                    isSnumlimit: '',//是否认定总分不超过X分（0否1是）
                    snumlimit: '',//认定总分
                    remarks: '',//备注
                },


                //dialogVisibleCreditRatio
                dialogVisibleCreditRatio: false,
                disabledCreditRatio: false,
                creditRatioForm: {
                    id: '',
                    scoRulePbs: [
                        {id: '', rule: {id: ''}, num: '2', val: '', valArr: ['', '']}
                    ]
                },


                modalities: [],
                regTypes: [], //正则范围类型接口
                scoRuleTypes: [], //计算学分规则接口


                queryStrStatic: '', //关键字,
                loading: false,

                isStandard: false


            }
        },
        computed: {
            isAllCollapsed: function () {
                return this.creditRuleList.every(function (item) {
                    if (item.children) {
                        return item.controlAllIsCollapsed;
                    }
                    return true;
                })
            },
            creditTypeTitle: function () {
                return this.isStandard ? '认定内容' : '学分类别'
            },
            creditTypeRules: function () {
                var validateTypeName = this.validateTypeName;
                var validateScore = this.validateScore;
                var creditTypeForm = this.creditTypeForm;
                var isLowSco = creditTypeForm.scoRuleDetailMould.isLowSco;
                var hasLowSco = isLowSco === '1';
                var isJoin = creditTypeForm.scoRuleDetailMould.isJoin;
                var hasJoin = isJoin === '1';
                var isCondcheck = creditTypeForm.scoRuleDetailMould.isCondcheck;
                var hasCondcheck = isCondcheck === '1';
                var validateLowScoMaxType = this.validateLowScoMaxType;
                return {
                    name: [
                        {required: true, message: '请填写名称', trigger: 'blur'},
                        {validator: validateTypeName, trigger: 'blur'}
                    ],
                    ptype: [
                        {required: true, message: '请选择认定形式', trigger: 'change'}
                    ],
                    scoRuleDetailMould: {
                        maxOrSum: [
                            {required: true, message: '请选择计算分值规则', trigger: 'change'}
                        ],
                        lowSco: [
                            {required: hasLowSco, message: '请输入分', trigger: 'blur'},
                            {validator: validateScore, trigger: 'blur'}
                        ],
                        lowScoMax: [
                            {required: hasLowSco, message: '请输入分', trigger: 'blur'},
                            {validator: validateScore, trigger: 'blur'},
//                            {validator: validateLowScoMaxType, trigger: 'blur'}
                        ],
                        joinMax: [
                            {required: hasJoin, message: '请输入分', trigger: 'blur'},
                            {validator: validateScore, trigger: 'blur'}
                        ],
//                        condType: [
//                            {required: hasCondcheck, message: '请选择区间', trigger: 'change'}
//                        ],
                        condition: [
                            {required: hasCondcheck, message: '请输入分值', trigger: 'blur'},
                            {validator: validateScore, trigger: 'blur'}
                        ],
//                        score: [
//                            {required: true, message: '请输入分数', trigger: 'blur'},
//                            {validator: validateScore, trigger: 'blur'}
//                        ]
                    }
                }
            },

            creditDetailRules: function () {
                var creditDetailForm = this.creditDetailForm;
                var isLowSco = creditDetailForm.isLowSco;
                var hasLowSco = isLowSco === '1';
                var isJoin = creditDetailForm.isJoin;
                var hasJoin = isJoin === '1';
                var isCondcheck = creditDetailForm.isCondcheck;
                var hasCondcheck = isCondcheck === '1';
                var validateScore = this.validateScore;
                var validateLowScoMax = this.validateLowScoMax;
                var validateRuleDetailName = this.validateRuleDetailName;
                return {
                    name: [
                        {required: true, message: '请输入内容及标准的名称', trigger: 'blur'},
                        {validator: validateRuleDetailName, trigger: 'blur'}
                    ],
                    maxOrSum: [
                        {required: true, message: '请选择计算分值规则', trigger: 'change'}
                    ],
                    lowSco: [
                        {required: hasLowSco, message: '请输入分', trigger: 'blur'},
                        {validator: validateScore, trigger: 'blur'}
                    ],
                    lowScoMax: [
                        {required: hasLowSco, message: '请输入分', trigger: 'blur'},
                        {validator: validateScore, trigger: 'blur'},
//                        {validator: validateLowScoMax, trigger: 'blur'}
                    ],
                    joinMax: [
                        {required: hasJoin, message: '请输入分', trigger: 'blur'},
                        {validator: validateScore, trigger: 'blur'}
                    ],
                    condType: [
                        {required: hasCondcheck, message: '请选择区间', trigger: 'change'}
                    ],
                    condition: [
                        {required: hasCondcheck, message: '请输入分值', trigger: 'blur'},
                        {validator: validateScore, trigger: 'blur'}
                    ],
                    score: [
                        {required: true, message: '请输入分数', trigger: 'blur'},
                        {validator: validateScore, trigger: 'blur'}
                    ]
                }
            },

            isShowJoinMax: function () {
                return this.creditDetailForm.maxOrSum === '2' || this.creditDetailForm.isSpecial === '1'
            },

            isShowJoinMaxType: function () {
                return this.creditTypeForm.scoRuleDetailMould.maxOrSum === '2'
            },

            creditRuleRules: function () {
                var creditRuleForm = this.creditRuleForm;
                var isKeepNpoint = creditRuleForm.isKeepNpoint;
                var isSnum = creditRuleForm.isSnum;
                var isSnumlimit = creditRuleForm.isSnumlimit;
                var validateScore = this.validateScore;
                var validateSnumVal = this.validateSnumVal;
                var hasIsSnum = isSnum === '1';
                return {
                    isKeepNpoint: [
                        {required: true, message: '请选择学位分数', trigger: 'change'}
                    ],
                    keepNpoint: [
                        {required: isKeepNpoint === '1', message: '请选择小数后几位', trigger: 'change'}
                    ],
                    isRprd: [
                        {required: true, message: '请选择分值处理', trigger: 'change'}
                    ],
                    isSnum: [
                        {required: false}
                    ],
                    snumMin: [
                        {required: hasIsSnum, message: '请输入分值', trigger: 'blur'},
                        {validator: validateScore, trigger: 'blur'}
                    ],
                    snumVal: [
                        {required: hasIsSnum, message: '请输入分值', trigger: 'blur'},
                        {validator: validateScore, trigger: 'blur'},
//                        {validator: validateSnumVal, trigger: 'blur'}
                    ],
                    snumlimit: [
                        {required: isSnumlimit === '1', message: '请输入总分', trigger: 'blur'},
                        {validator: validateScore, trigger: 'blur'}
                    ]
                }
            },

            creditRatioValRules: function () {
//                var validateRatioValReg = this.validateRatioValReg;
                return [
                    {required: true, message: '请输入配比', trigger: 'blur'},
//                    {validator: validateRatioValReg, trigger: 'blur'}
                ]
            },

            creditRuleEntries: function () {
                var creditRuleList = this.creditRuleList;
                var entries = {};
                var i = 0;
                while (i < creditRuleList.length) {
                    var item = creditRuleList[i];
                    entries[item.id] = item;
                    i++;
                }
                return entries;
            },

            isEditSkill: function () {
                var parentId;
                var creditRuleEntries = this.creditRuleEntries;
                var isSkill = false;
                if (this.creditTypeForm.parent) {
                    parentId = this.creditTypeForm.parent.id;
                }
                parentId = parentId || this.creditDetailForm.rule.id;
                if (!parentId) {
                    return false;
                }
                if (this.creditTypeForm.id === this.SKILLCREDITID) {
                    return true;
                }
                while (!!parentId) {
                    if (parentId === this.SKILLCREDITID) {
                        isSkill = true;
                        break;
                    }
                    var parent = creditRuleEntries[parentId];
                    if (!parent) {
                        break;
                    }
                    if (parent.parentId === '0') {
                        break;
                    }
                    parentId = parent.parentId;
                }
                return isSkill;
            }

        },
        methods: {

            saveSorts: function () {
                var creditRuleList = this.creditRuleList;
                var i = 0;
                var nCreditRuleList = [];
                while (i < creditRuleList.length) {
                    var creditRule = creditRuleList[i];
                    if (typeof creditRule.sort !== 'undefined' && creditRule.sort != '') {
                        var sortString = creditRule.sort.toString();
                        if (!(/^0{1}$|^[1-9]{1}\d*$/.test(sortString))) {
                            this.$message({
                                message: '请输入数字',
                                type: 'error'
                            });
                            break;
                        }
                    }
                    nCreditRuleList.push({
                        id: creditRule.id,
                        sort: parseInt(creditRule.sort),
                        type: creditRule.type
                    });
                    i++;
                }
                if (nCreditRuleList.length === creditRuleList.length) {
                    this.saveSortsXhr(nCreditRuleList)
                }
            },

            saveSortsXhr: function (nCreditRuleList) {
                var self = this;
                this.loading = true;
                this.$axios.post('/scr/scoRule/saveSort', nCreditRuleList).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$message({
                            message: '保存成功',
                            type: 'success'
                        })
                        self.getCreditRuleTree();
                    } else {
                        self.$message({
                            message: data.msg,
                            type: 'error'
                        })
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                })
            },


            toggleCollasped: function () {
                this.isAllCollasped = !this.isAllCollasped;
                this.creditRuleList = this.treeToList(this.creditRuleTree, this.isAllCollasped ? 0 : 10);

            },

            searchText: function () {
                if (!this.searchListForm.keys) {
                    this.queryStrStatic = '';
                    return false;
                }
                this.setCollegeExpand(this.searchListForm.keys);
                this.queryStrStatic = this.searchListForm.keys;
            },

            setCollegeExpand: function (keyword) {
                var creditRuleEntries = this.creditRuleEntries;
                var self = this;
                this.creditRuleList.forEach(function (item) {
                    if (item.name.indexOf(keyword) > -1) {
                        var parentId = item.parentId ? item.parentId : item.rule.id;
                        var parentCollege = creditRuleEntries[parentId];
                        if (parentCollege && !parentCollege.isExpand) {
                            self.setParentCollegeExpand(parentCollege)
                        }
                    }
                })
            },

            setParentCollegeExpand: function (parentCollege) {
                var parent = parentCollege;
                var creditRuleEntries = this.creditRuleEntries;
                while (parent) {
                    if (parent.isExpand) {
                        break;
                    }
                    parent.isExpand = true;
                    this.expandCellTrue(parent.children, parent.isExpand);
                    parent = creditRuleEntries[parent.parentId];
                }
            },


            childrenHasStan: function (item) {
                if (item.children && item.children.length > 0) {
                    return item.children.some(function (child) {
                        return !!child.rule
                    })
                }
                return false;
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
                this.searchListForm.orderBy = 'a.name';
                this.searchListForm.orderByType = sortValue ? ( sortValue.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getCreditRuleTree();
            },

            getCreditRuleTree: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/scr/scoRule/listpage?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.creditRuleTree = data.data || [];
                        self.getCreditRuleList(self.creditRuleTree)
                    }
                    self.loading = false;
                    self.isAllCollasped = false;
                }).catch(function (error) {
                    self.loading = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            getCreditRuleList: function (tree) {
                this.creditRuleList = this.treeToList(tree, 10);
            },

            treeToList: function (data, expandLevel) {
                data = JSON.parse(JSON.stringify(data));
                expandLevel = expandLevel || 0;

                function flatten(data) {
                    return data.reduce(function (p1, p2) {
                        var children = p2.children || [];
                        var parentIds = p2.parentIds;
                        //判断children是否存在rule,存在即可以添加标准
                        if (children.length > 0) {
                            var child = children[0];
                            if (child.rule) {
                                p2.addRuleAble = true;
                            }
                        }
                        if (!parentIds && p2.rule) {
                            p2.addRuleAble = false;
                            p2.parentIds = p2.rule.parentIds + p2.rule.id + ',';
                            parentIds = p2.parentIds;
                        } else {
                            p2.parent = {id: p2.parentId}
                        }
                        parentIds = parentIds.replace(/\,$/, '');
                        parentIds = parentIds.split(',');
                        parentIds = parentIds.slice(1);
                        Vue.set(p2, 'dots', parentIds.join('-'))
                        Vue.set(p2, 'isCollapsed', parentIds.length > expandLevel + 1);
                        Vue.set(p2, 'isExpand', parentIds.length <= expandLevel);
//                        Vue.set(p2, 'controlAllIsCollapsed', parentIds.length <= expandLevel); // 控制所有的关闭
                        return p1.concat(p2, flatten(children))
                    }, [])
                }

                return flatten(data);
            },

            elIconCaret: function (row) {
                return {
                    'is-leaf': !row.children || !row.children.length,
                    'expand-icon': row.isExpand
                }
            },

            //控制行的展开收起
            handleExpandCell: function (row) {
                var children = row.children;
                if (!children) {
                    return;
                }
                row.isExpand = !row.isExpand;
                if (row.isExpand) {
                    this.expandCellTrue(children, row.isExpand);
                    return
                }
                this.expandCellFalse(children, row.isExpand);
            },

            expandCellTrue: function (list, b) {
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    item.isCollapsed = !b;
                    item.isExpand = false;
                    item.controlAllIsCollapsed = b;
                }
            },

            expandCellFalse: function (list, b) {
                var childrenIds = this.getCreditRuleChildrenIds(list);
                for (var i = 0; i < this.creditRuleList.length; i++) {
                    var item = this.creditRuleList[i];
                    if (childrenIds.indexOf(item.id) > -1) {
                        item.isCollapsed = !b;
                        item.isExpand = false;
                    }
                }
            },

            //获取所有子的ID
            getCreditRuleChildrenIds: function (list) {
                var ids = [];

                function getIds(list) {
                    if (!list) return ids;
                    for (var i = 0; i < list.length; i++) {
                        ids.push(list[i].id);
                        getIds(list[i].children);
                    }
                }

                getIds(list);
                return ids;
            },

            //创建学分类别
            handleCloseCreditType: function () {
//                this.$refs.creditTypeForm.clearValidate();
                this.$refs.creditTypeForm.resetFields();
                this.creditTypeForm.parent.id = '';
                this.creditTypeForm.id = '';
                this.creditTypeForm.scoRuleDetailMould.id = '';
                this.$nextTick(function () {
                    this.dialogVisibleCreditType = false;
                })
            },

            handleChangeIsTypeCC: function (value) {
                if (value === '0') {
                    this.creditTypeForm.scoRuleDetailMould.condition = ''
                }
            },

            //改变计算分值规则
            handleChangeTypeMaxOrSum: function (value) {
                if (value === '1') {
                    this.creditTypeForm.scoRuleDetailMould.isJoin = '0';
                    this.creditTypeForm.scoRuleDetailMould.joinMax = '';
                }
            },

            //改变低于
            handleChangeIsTypeLowSco: function (value) {
                if (value === '0') {
                    this.creditTypeForm.scoRuleDetailMould.lowSco = '';
                    this.creditTypeForm.scoRuleDetailMould.lowScoMax = '';
                }
            },

            handleChangeIsTypeJoin: function (value) {
                if (value === '0') {
                    this.creditTypeForm.scoRuleDetailMould.joinMax = '';
                }
            },

            openDialogCreditType: function () {
                this.dialogVisibleCreditType = true;
                this.isStandard = false;
                this.creditTypeForm.type = '2';
                this.creditTypeForm.parent.id = '1';
                // this.creditTypeForm.parentId = '1';
            },

            addCreditType: function (item) {
                this.dialogVisibleCreditType = true;
                this.isStandard = true;
                this.$nextTick(function () {
                    if (item.id) {
                        this.creditTypeForm.parent.id = item.id;
                        // this.creditTypeForm.parentId = item.id;
                        this.creditTypeForm.type = (parseInt(item.type) + 1).toString();
                        var parent = this.creditRuleEntries[item.id];
                        var module = parent.scoRuleDetailMould;
                        this.creditTypeForm.ptype = parent.ptype;

                        this.assignFormData(this.creditTypeForm.scoRuleDetailMould, module);
                    }
                })
            },
            //添加子项
            editCreditType: function (item) {
                this.dialogVisibleCreditType = true;
                this.$nextTick(function () {
                    this.assignFormData(this.creditTypeForm, item);
                })
            },

            validateCreditForm: function () {
                var self = this;
                this.$refs.creditTypeForm.validate(function (valid) {
                    if (valid) {
                        self.submitCreditForm();
                    }
                })
            },

            submitCreditForm: function () {
                var self = this;
                this.disabledCreditType = true;
                var ruleId = this.creditTypeForm.id;
                var ptype = this.creditTypeForm.ptype;
                this.$axios.post('/scr/scoRule/ajaxAuditScoRule', this.creditTypeForm).then(function (reponse) {
                    var data = reponse.data;
                    if (data.status === 1) {
//                        self.$message.success('保存成功');
                        var msgBoxOption = {
                            type: 'success',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            confirmButtonText: '设置学分配比',
                            cancelButtonText: '取消',
                            showCancelButton: true,
                            showClose: false,
                            message: '保存成功，请设置学分配比'
                        };
                        if (ptype === '2') {
//                            if (ruleId) {
//                                msgBoxOption['showCancelButton'] = true;
//                            }
                            self.$msgbox(msgBoxOption).then(function () {
                                var nRule = data.data || {};
                                self.openDialogCreditRatio(nRule)
                            }).catch(function () {

                            });
                        } else {
                            self.$message.success('保存成功');
                        }

                        self.getCreditRuleTree();
                        self.handleCloseCreditType();
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.disabledCreditType = false;
                }).catch(function (error) {
                    self.disabledCreditType = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            },


            confirmCreditType: function (item, index) {
                var self = this;
                var text = item.children ? '其下包含子项，删除后将一并删除，是否继续？' : '确认删除学分类别吗？'
                this.$confirm(text, "提示", {
                    type: 'warning',
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(function () {
                    self.delCreditType(item, index)
                }).catch(function () {

                })
            },

            delCreditType: function (item, index) {
                var self = this;
                this.loading = true;
                this.$axios.get('/scr/scoRule/ajaxDeleteScrRule?id=' + item.id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$message.success("删除成功");
                        self.getCreditRuleTree();
//                        self.creditRuleList.splice(index, 1);
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.loading = false;
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg);
                    self.loading = false;
                })
            },

            //添加内容及标准
            handleCloseCreditDetail: function () {
                this.$refs.creditDetailForm.clearValidate();
                this.$refs.creditDetailForm.resetFields();
                this.$nextTick(function () {
                    this.dialogVisibleCreditDetail = false;
                })
            },

            validateCreditDetailForm: function () {
                var self = this;
                this.$refs.creditDetailForm.validate(function (valid) {
                    if (valid) {
                        self.submitCreditDetailForm()
                    }
                })
            },

            submitCreditDetailForm: function () {
                var self = this;
                this.disabledCreditDetail = true;
                this.$axios.post('/scr/scoRuleDetail/ajaxAuditScoRuleDetail', this.creditDetailForm).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getCreditRuleTree();
                        self.handleCloseCreditDetail();
                        self.$message.success('保存成功');
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.disabledCreditDetail = false;
                }).catch(function () {
                    self.disabledCreditDetail = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            //添加标准
            addCreditDetail: function (item) {
                this.dialogVisibleCreditDetail = true;
                this.$nextTick(function () {
                    this.assignFormData(this.creditDetailForm.rule, item);
                    this.creditDetailForm.id = '';
                    var parent = this.creditRuleEntries[item.id];
                    var module = parent.scoRuleDetailMould;
                    module.condType = module.condType == '0' ? '10' : module.condType;
                    module.score = module.score == '0' ? '' : module.score;
                    module.id = '';
                    this.assignFormData(this.creditDetailForm, module)
                })
            },
            //编辑标准
            editCreditDetail: function (item) {
                this.dialogVisibleCreditDetail = true;
                this.$nextTick(function () {
                    this.assignFormData(this.creditDetailForm, item);
                })
            },

            //确认删除标准
            confirmDelCreditDetail: function (item, index) {
                var self = this;
                this.$confirm("确认删除内容及标准吗？", "提示", {
                    type: 'warning',
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(function () {
                    self.delCreditDetail(item, index)
                }).catch(function () {

                });
            },

            delCreditDetail: function (item, index) {
                var self = this;
                this.$axios.get('/scr/scoRuleDetail/ajaxDeleteScoRuleDetail?id=' + item.id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$message.success("删除成功");
                        self.creditRuleList.splice(index, 1);
                    } else {
                        self.$message.error(data.msg);
                    }
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            //改变计算分值规则
            handleChangeMaxOrSum: function (value) {
                if (value === '1') {
                    this.creditDetailForm.isJoin = '0';
                    this.creditDetailForm.joinMax = '';
                }else {
                    this.creditDetailForm.isSpecial = '0'
                }
            },

            //改变低于
            handleChangeIsLowSco: function (value) {
                if (value === '0') {
                    this.creditDetailForm.lowSco = '';
                    this.creditDetailForm.lowScoMax = '';
                }
            },

            handleChangeIsJoin: function (value) {
                if (value === '0') {
                    this.creditDetailForm.joinMax = '';
                }
            },

            handleChangeIsCC: function (value) {
                if (value === '0') {
                    this.creditDetailForm.condition = '';
                }
            },

            handleChangeIsSm: function (value) {
                if (value === '0') {
                    this.creditRuleForm.snumMin = '';
                    this.creditRuleForm.snumVal = '';
                }

            },

            handleChangeIsSnumlimit: function (value) {
                if (value === '0') {
                    this.creditRuleForm.snumlimit = '';
                }
            },

            //添加/修改设置学分规则接口
            handleCloseCreditRule: function () {
                this.dialogVisibleCreditRule = false;
            },

            openDialogCreditRule: function () {
                this.dialogVisibleCreditRule = true;
                this.getCreditRule();
            },

            getCreditRule: function () {
                var self = this;
                this.$axios.get('/scr/scoRset/list').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || [];
                        if (data.length > 0) {
                            self.assignFormData(self.creditRuleForm, data[0]);
                        }
                    }
                }).catch(function () {

                })
            },


            validateCreditRuleForm: function () {
                var self = this;
                this.$refs.creditRuleForm.validate(function (valid) {
                    if (valid) {
                        self.submitCreditRuleForm()
                    }
                })
            },

            submitCreditRuleForm: function () {
                var self = this;
                this.disabledCreditRule = true;
                this.$axios.post('/scr/scoRset/ajaxAuditScoRset', this.creditRuleForm).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.handleCloseCreditRule();
                        self.$message.success("保存成功");
                    } else {
                        self.$message.error(data.msg)
                    }
                    self.disabledCreditRule = false;
                }).catch(function () {
                    self.disabledCreditRule = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            handleChangeIsKeepNponit: function (value) {
                if (value === '0') {
                    this.creditRuleForm.keepNpoint = '';
                }
            },

            //学分配比列表接口
            handleCloseCreditRatio: function () {
                this.creditRatioForm.scoRulePbs = [
                    {id: '', rule: {id: ''}, num: '2', val: '', valArr: ['', '']}
                ];
                this.$nextTick(function () {
                    this.$refs.creditRatioForm.resetFields();
                    this.dialogVisibleCreditRatio = false;
                })

            },

            //恢复默认表单
            openDialogCreditRatio: function (item) {
                this.dialogVisibleCreditRatio = true;
                this.getScoPbList(item);
            },

            validateCreditRatioForm: function () {
                var self = this;
                this.$refs.creditRatioForm.validate(function (valid) {
                    if (valid) {
                        self.submitCreditRatio()
                    }
                })
            },

            getScoRulePbsParams: function () {
                var scoRulePbs = JSON.parse(JSON.stringify(this.creditRatioForm.scoRulePbs));
                scoRulePbs = scoRulePbs.map(function (item) {
                    item.val = item.valArr.join(':')
                    delete item.valArr;
                    return item;
                });
                return scoRulePbs;
            },

            submitCreditRatio: function () {
                var self = this;
                this.disabledCreditRatio = true;
                this.$axios.post('/scr/scoRulePb/ajaxSetScoPb', this.getScoRulePbsParams()).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$message.success('保存成功');
                        self.handleCloseCreditRatio();
                    } else {
                        self.$message.error(data.msg)
                    }
                    self.disabledCreditRatio = false;
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg);
                    self.disabledCreditRatio = false;
                })
            },

            pbListXhrs: function (pbItem) {
                var ids = pbItem.parentIds + pbItem.id;
                var axiosArr = [];
                ids = ids.split(',');
                ids = ids.slice(2);
                for (var i = ids.length - 1; i > -1; i--) {
                    axiosArr.push(this.$axios.get('/scr/scoRulePb/ajaxScoPbList?rule.id=' + ids[i]));
                }
                return {
                    axiosArr: axiosArr,
                    id: pbItem.id
                }
            },

            //获取学分配比
            getScoPbList: function (pbItem) {
                var self = this;
//                var pbListXhrs = this.pbListXhrs(pbItem);
//                this.$axios.all(pbListXhrs.axiosArr).then(this.$axios.spread(function () {
//                    var isPb = false;
//
//                    for(var i = 0; i < arguments.length; i++){
//                        var data = arguments[i].data;
//                        console.log(data)
//                        if(data.status === 1){
//                            data = data.data;
//                            if(data && data.length > 0){
//                                isPb = true;
//                                data = data.map(function (item) {
//                                    var rule = item.rule || {};
//                                    var pbItemId = pbListXhrs.id === rule.id ? item.id : '';
//                                    return {id: pbItemId, rule: {id: pbListXhrs.id}, num: item.num, val: item.val}
//                                });
//                                self.creditRatioForm.scoRulePbs = data;
//                                break;
//                            }
//                        }
//                    }
//                    if(!isPb){
//                        self.creditRatioForm.scoRulePbs[0].rule.id = pbListXhrs.id;
//                    }
//                }));
//
                this.$axios.get('/scr/scoRulePb/ajaxScoPbList?rule.id=' + pbItem.id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || [];
                        data = data.map(function (item) {
                            var rule = item.rule || {};
                            return {
                                id: item.id,
                                rule: {id: rule.id},
                                num: item.num,
                                val: item.val,
                                valArr: item.val.split(':')
                            }
                        });
                        if (data.length > 0) {
                            self.creditRatioForm.scoRulePbs = data;
                        } else {
                            self.creditRatioForm.scoRulePbs[0].rule.id = pbItem.id;
                        }
                    }
                }).catch(function () {

                })
            },


            //渲染表头
            creditRatioTableLabel: function (h) {
                return h('span', {
                    'class': 'credit-ratio-label'
                }, '设置配比')
            },

            //ratioplaceholder
            ratioPlaceholder: function (num) {
                var i = 0, str = '';
                num = parseInt(num);
                while (i < num) {
                    str += 'n:';
                    i++;
                }
                return str.replace(/\:$/, '');
            },

            addScoRulePb: function () {
                var creditRatioForm = this.creditRatioForm;
                var scoRulePbs = creditRatioForm.scoRulePbs;
                var lastRulePb = scoRulePbs[scoRulePbs.length - 1];
                // console.log(/^([1-9]{1}\:){1,10}[1-9]$/.toString(), new RegExp("/^([1-9]{1}\\:){1,10}[1-9]$/").toString())
                var num = parseInt(lastRulePb.num) + 1;
                var valArr = [];
                var valI = 0;
                while (valI < num) {
                    valArr.push('');
                    valI++;
                }

                scoRulePbs.push({
                    id: '',
                    rule: {
                        id: lastRulePb.rule.id
                    },
                    num: num.toString(),
                    val: '',
                    valArr: valArr
                })
            },

            removeScoRule: function (index) {
                var creditRatioForm = this.creditRatioForm;
                var scoRulePbs = creditRatioForm.scoRulePbs;
                scoRulePbs.splice(index, 1);
            },


            //认定形式接口
            getScoRptypes: function () {
                var self = this;
                this.$axios.get('/scr/scoRule/ajaxScoRptypes?isAll=true').then(function (response) {
                    var data = response.data;
                    data = JSON.parse(data.data) || [];
                    self.modalities = data;
                })
            },

            //正则范围类型接口
            getRegTypes: function () {
                var self = this;
                this.$axios.get('/scr/scoRule/ajaxRegTypes?isAll=false').then(function (response) {
                    var data = response.data;
                    data = JSON.parse(data.data) || [];
                    self.regTypes = data;
                })
            },

            //计算学分规则接口
            getScoRuleTypes: function () {
                var self = this;
                this.$axios.get('/scr/scoRule/ajaxScoRuleTypes?isAll=true').then(function (response) {
                    var data = response.data;
                    data = JSON.parse(data.data) || [];
                    self.scoRuleTypes = data;
                })
            }

        },
        created: function () {
            this.getCreditRuleTree();
            this.getScoRptypes();
            this.getRegTypes();
            this.getScoRuleTypes();
            this.getCreditRule();
        }
    })
</script>
</body>
</html>