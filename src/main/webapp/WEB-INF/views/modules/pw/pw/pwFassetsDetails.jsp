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
        <edit-bar second-name="查看" href="/pw/pwFassets/list"></edit-bar>
    </div>

    <el-row :gutter="20" label-width="120px">
        <el-col>
            <e-col-item label="资产类别：">{{saveForm.pwCategory.parentId | selectedFilter(assetsTypesEntries)}}</e-col-item>
        </el-col>
        <el-col>
            <e-col-item label="资产名称：">{{saveForm.pwCategory.name}}</e-col-item>
        </el-col>
        <el-col>
            <e-col-item label="品牌：">{{saveForm.brand}}</e-col-item>
        </el-col>
        <el-col>
            <e-col-item label="规格：">{{saveForm.specification}}</e-col-item>
        </el-col>
        <el-col>
            <e-col-item label="购买人：">{{saveForm.prname}}</e-col-item>
        </el-col>
        <el-col>
            <e-col-item label="手机号码：">{{saveForm.phone}}</e-col-item>
        </el-col>
        <el-col>
            <e-col-item label="购买时间：">{{saveForm.time | formatDateFilter('YYYY-MM-DD')}}</e-col-item>
        </el-col>
        <el-col>
            <e-col-item label="价格：">{{saveForm.price}}</e-col-item>
        </el-col>
        <el-col>
            <e-col-item label="备注：" class="white-space-pre-static word-break"><span v-html="saveForm.remarks"></span></e-col-item>
        </el-col>
    </el-row>

</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var pwFassets = JSON.parse(JSON.stringify(${fns:toJson(pwFassets)})) || [];
            var pwCategory = pwFassets.pwCategory || {};
            var assetsTypes = JSON.parse(JSON.stringify(${fns:toJson(fns:findChildrenCategorys(null))}));

            return {
                assetsTypes:assetsTypes,
				saveForm:{
                    id: pwFassets.id || '',
                    pwCategory:{
                        parentId:pwCategory.parentId,
                        name:pwCategory.name
                    },
                    brand:pwFassets.brand || '',
                    specification:pwFassets.specification || '',
                    prname:pwFassets.prname || '',
                    phone:pwFassets.phone || '',
                    time:pwFassets.time || '',
                    price:pwFassets.price || '',
                    remarks:pwFassets.remarks || ''
                },
                message:'${message}'

            }
        },
        computed:{
            assetsTypesEntries:{
                get:function () {
                    return this.getEntries(this.assetsTypes,{label:'name',value:'id'});
                }
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