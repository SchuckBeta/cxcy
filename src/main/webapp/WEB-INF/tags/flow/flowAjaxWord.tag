<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="下载说明" %>
<%@ attribute name="proId" type="java.lang.String" required="true" description="项目ID" %>
<%@ attribute name="proCategory" type="java.lang.String" required="true" description="项目类别" %>
<%@ attribute name="wordType" type="java.lang.String" required="true" description="Word类型" %>
<%@ attribute name="types" type="java.lang.String" required="true" description="类型列表" %>
<%@ attribute name="prefix" type="java.lang.String" required="true" description="前缀" %>
<%@ attribute name="idx" type="java.lang.String" required="false" description="当页面有多个下载时标记索引" %>
<div class="col-md-12">
    <div class="form-group">
        <label class="control-label">${title }：</label>
        <div class="input-box">
            <ul id="downFileUl${idx}" class="file-list">
                <li class="file-item">
                    <div class="file-info">
                        <img src="${ctxImg}/filetype/word.png">
                        <a href="" data-url=""
                           data-original="" data-size=""
                           data-title=""
                           data-type=""
                           data-ftp-url=""></a>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <script type="text/javascript">
	    $(function(){
	        var $downFileUl = $('#downFileUl${idx}');
	        var name, proCategory;
	        $.each(JSON.parse('${types}'), function (i, item) {
	            var tpl = item.tpl;
	            if(item.key == '${proCategory}${wordType}'){
	                name = item.name;
	                proCategory = item.key;
	                return false;
	            }
	        });
	        $downFileUl.find('a').attr('href', '/f/proprojectmd/proModelMd/ajaxWord?proId=${proId}&type='+proCategory).text('${prefix}'+name);
	    });
    </script>
</div>