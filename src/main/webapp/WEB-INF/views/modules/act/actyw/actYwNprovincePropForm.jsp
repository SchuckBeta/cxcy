<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
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
    <el-form :model="actYwGroupForm" ref="actYwGroupForm" :rules="actYwGroupRules" size="mini" label-width="120px"
             autocomplete="off" :disabled="disabled" style="max-width: 960px">
        <el-form-item label="功能类型：">
            ${fpType.name }
        </el-form-item>
        <el-form-item label="关联流程：">
            <%--<el-select v-model="actYwGroupForm.groupId" filterable clearable placeholder="请选择">--%>
                <%--<el-option v-for="item in actYwGroups" :key="item.id" :value="item.id" :label="item.name"></el-option>--%>
            <%--</el-select>--%>
            <%--<span class="el-form-item-expository">更换流程需要重新发布项目才能生效</span>--%>
            {{actYwGroupForm.groupId | selectedFilter(actYwGroupEntries)}}
        </el-form-item>
        <c:if test="${(not empty fpType.type.key) && (aconfig.hasPtype)}">
            <el-form-item :label="ftpTypeName+'：'">
                {{proProject.type | selectedFilter(fpTypeKeyEntries)}}
            </el-form-item>
        </c:if>
        <c:if test="${(not empty fpType.category.key) && (aconfig.hasPctype)}">
            <el-form-item prop="proCategorys" :label="categoryName+'：'">
                <el-select
                        v-model="actYwGroupForm.proCategorys"
                        multiple
                        filterable
                        default-first-option
                        style="width: 500px;"
                        placeholder="请选择">
                    <el-option
                            v-for="item in fpCategoryKeys" :key="item.value" :value="item.value" :label="item.label">
                    </el-option>
                </el-select>
            </el-form-item>
        </c:if>
        <el-form-item label="开始时间：">
            {{proProject.startDate | formatDateFilter('YYYY-MM-DD')}}
        </el-form-item>
        <el-form-item label="结束时间：">
            {{proProject.endDate | formatDateFilter('YYYY-MM-DD')}}
        </el-form-item>
        <el-form-item>
        	<c:if test="${aconfig.hasUpProp}">
            	<el-button type="primary" @click.stop.prevent="validActYwGroupForm">保存</el-button>
            </c:if>
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
            var fpType = JSON.parse(JSON.stringify(${fns: toJson(fpType)})) || {};
            var fpTypeKeys = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList(fpType.type.key))}));
            var fpCategoryKeys = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList(fpType.category.key))}));
            var actYwGroups = JSON.parse(JSON.stringify(${fns: toJson(actYwGroups)}));
            var proProject = actYw.proProject || {};
            var menu = proProject.menu || {};
            var category = proProject.category || {};
            var showFlow = '${showFlow}';
            showFlow = showFlow === 'true';
            return {
                actYwGroupForm: {
                    id: actYw.id,
                    isPreRelease: actYw.isPreRelease,
                    keyType: actYw.keyType,
                    'proProject.id': proProject.id,
                    'proProject.menu.id': menu.id,
                    'proProject.category.id': category.id,
                    'proProject.projectName': proProject.name,
                    fpkey: '${fpType.key}',
                    'proProject.imgUrl': '/images/upload.png',
                    'actYw.groupId': actYw.groupId,
                    'proProject.type': proProject.type,
                    proCategorys: proProject.proCategorys || [],
                    'group.flowType': '${aconfig.flowType}',
                    groupId: actYw.groupId,
                    isUpdateYw: false
                },
                actYwGroupRules: {
                    proCategorys: [
                        {required: true, message: '请选择${fpType.category.name}', trigger: 'change'}
                    ]
                },
                proProject: proProject,
                disabled: false,
                showFlow: showFlow,
                actYwGroupId: '${flowYwId.id }',
                ftpTypeName: '${aconfig.dictPtypeKey}',
                categoryName: '${aconfig.dictPctypeKey}',
                fpTypeKeys: fpTypeKeys,
                fpCategoryKeys: fpCategoryKeys,
                actYwGroups: actYwGroups
            }
        },
        computed: {
            secondName: function () {
                return this.actYwGroupForm.id ? '修改' : '添加'
            },
            fpTypeKeyEntries: function () {
                return this.getEntries(this.fpTypeKeys)
            },
            actYwGroupEntries: function () {
                return this.getEntries(this.actYwGroups, {
                    value: 'id',
                    label: 'name'
                })
            }
        },
        methods: {
            validActYwGroupForm: function () {
                var self = this;
                this.$refs.actYwGroupForm.validate(function (valid) {
                    if(valid){
                        self.submitActYwGroup();
                    }
                })
            },
            goToBack: function () {
                history.go(-1);
            },

            getActYwGroupParams: function () {
                var actYwGroupForm = JSON.parse(JSON.stringify(this.actYwGroupForm));
                var proCategorys = actYwGroupForm.proCategorys;
                var params;
                proCategorys = proCategorys.map(function (item) {
                    return ['proProject.proCategorys='+item]
                });
                delete actYwGroupForm.proCategorys;
                params = Object.toURLSearchParams(actYwGroupForm);
                params += '&'+ proCategorys.join('&');
                return params;
            },

            submitActYwGroup: function () {
                var self = this;
                this.disabled = true;
                this.$axios({
                    method: 'POST',
                    url: '/actyw/actYw/ajaxProp?'+ this.getActYwGroupParams()
                }).then(function (response) {
                    var data = response.data;
                    if(data.status){
                        self.$alert(data.msg, "提示", {
                            type: 'success'
                        }).then(function () {
                            location.href = '${ctx}/actyw/actYw/list?group.flowType=${aconfig.flowType}'
                        }).catch(function () {

                        })
                    }else {
                        self.$message.error(data.msg);
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.disabled = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            }
        },
        created: function () {
            if (this.showFlow) {
                this.actYwGroupForm['actYw.groupId'] = this.actYwGroupId;
            }
        }
    })

</script>
</body>
</html>