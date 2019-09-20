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
		<edit-bar second-name="查看" href="/pw/pwCategory/list"></edit-bar>
    </div>

    <el-row :gutter="20" label-width="120px">
        <el-col>
            <e-col-item label="父类别：">{{saveForm.parentId | selectedFilter(selectOptionsEntries)}}</e-col-item>
        </el-col>
        <el-col>
            <e-col-item label="名称：">{{saveForm.name}}</e-col-item>
        </el-col>
        <el-col>
            <e-col-item label="备注：" class="white-space-pre-static word-break"><span v-html="saveForm.remarks"></span></e-col-item>
        </el-col>
        <el-col>
            <e-col-item label="编号规则：" class="item-bottom-border"></e-col-item>
        </el-col>
        <el-col>
            <e-col-item label="前缀：">{{saveForm.pwFassetsnoRule.prefix}}</e-col-item>
        </el-col>
        <el-col v-if="saveForm.parentId != '1'">
            <e-col-item label="开始编号：">{{saveForm.pwFassetsnoRule.startNumber}}</e-col-item>
        </el-col>
        <el-col v-if="saveForm.parentId != '1'">
            <e-col-item label="编号位数：">
                {{saveForm.pwFassetsnoRule.numberLen}}
                <span style="color:#999;margin-left: 10px;">表示数字最小的位数，不足位数的前面补0</span>
            </e-col-item>
        </el-col>
        <el-col v-if="saveForm.parentId != '1'" style="color:#999;">
            <e-col-item label="示例：">{{rule}}</e-col-item>
        </el-col>
    </el-row>


</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var pwCategory = JSON.parse(JSON.stringify(${fns:toJson(pwCategory)})) || [];
            var pwFassetsnoRule = pwCategory.pwFassetsnoRule || {};
            var pCategoryTypes = JSON.parse(JSON.stringify(${fns:toJson(fns:findChildrenCategorys('1'))}));

            return {
				saveForm:{
                    id: pwCategory.id || '',
                    parentId: pwCategory.parentId || '',
                    name: pwCategory.name || '',
                    remarks: pwCategory.remarks || '',
                    pwFassetsnoRule:{
                        id:pwFassetsnoRule.id || '',
                        prefix:pwFassetsnoRule.prefix || '',
                        startNumber:pwFassetsnoRule.startNumber || '',
                        numberLen:pwFassetsnoRule.numberLen || ''
                    }
                },
                selectOptions:pCategoryTypes,
                parentPrefix:pwCategory.parentPrefix,
                message:'${message}'
            }
        },
        computed:{
			selectOptionsEntries:{
				get:function () {
					return this.getEntries(this.selectOptions,{label:'name',value:'id'});
				}
			},
            rule:{
                get:function () {
                    var prefix = this.saveForm.pwFassetsnoRule.prefix;
                    var startNumber = this.saveForm.pwFassetsnoRule.startNumber;
                    var numberLen = this.saveForm.pwFassetsnoRule.numberLen;
                    if (prefix && startNumber && numberLen) {
                        return this.parentPrefix + prefix + this.prefixZero(parseInt(startNumber), parseInt(numberLen));
                    }
                    return ''
                }
            }
        },
        methods: {
            prefixZero:function (num, length) {
                return (Array(length).join('0') + num).slice(-length);
            }
        },
        created: function () {
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