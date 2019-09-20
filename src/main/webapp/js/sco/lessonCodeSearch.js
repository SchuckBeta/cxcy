/**
 * Created by qingtengwang on 2017/7/13.
 */

!function($){

    function LessonCodeSearch($element,option){
        this.$element = $($element);
        this.options = $.extend({},LessonCodeSearch.DEFAULTS,option);

    }

    LessonCodeSearch.DEFAULTS = {
        noResultTemplate: '<div class="search-result-container">没有查询结果，请点击<a href="#modalResult" data-toggle="modal">查询</a></div>',
        url: '/f/scoapply/findListByNameOrCode',
        loading: '<div class="loading text-center" style="width: 50%"><img src="' + ctxImg + '/loading.gif"></div>',
        targetForm: '#searchCode',
        targetFormContainer: null,
        resultContainerId: '#result',
        resultModalContainerId: null,
        submitFormId: '#formLesson'
    };

    LessonCodeSearch.prototype.choose = function(){
        var $resultContainerId = $(this.options.resultContainerId);
        var $resultModalContainerId = $(this.options.resultModalContainerId);
        var self = this;
        $resultContainerId.off('.xz').on('click.xz','.btn-primary',function(e){
            var item = $(this).data('row');
            $.each(item,function(k,v){
                if(k === 'code'){
                    $('input[name="keyword"]').val(v)
                }
                $('input[name="'+k+'"]').val(v)
            });
            $('#courseId').val(item.id);
            $resultContainerId.empty();
        });
        $resultModalContainerId.off('.xz').on('click.xz','.btn-primary',function(e){
            var item = $(this).data('row');
            var $modalResult = $('#modalResult');
            $.each(item,function(k,v){
                if(k === 'code'){
                    $('input[name="keyword"]').val(v)
                }
                $('.search-result-container').remove();
                $modalResult.modal('hide');
                $modalResult.find('input[type="text"]').val('');
                $modalResult.find('select').val('');
                $modalResult.find('input[type="checkbox"]').prop('checked',false);
                $('input[name="'+k+'"]').val(v);
                $('#courseId').val(item.id);
            })
        });

    };



    LessonCodeSearch.prototype.search = function(){
        var options = this.options;
        var $targetForm = $(options.targetForm);
        var url = options.url;
        var postData = {};
        var targetFormContainer = options.targetFormContainer;
        var $formControls;
        var $formControlsCheck;
        var resultTemplate;
        var noResultTemplate = options.noResultTemplate;
        var loading = options.loading;
        var $result;
        var professionalValArr = [];
        var professionalName;

        this.$element.parents('.form-horizontal').find('input').not('[name="keyword"]').val('');
        $('#courseId').val('');
        if(targetFormContainer){
            $formControls = $(targetFormContainer).find('input[type="text"],select');
            $formControlsCheck = $(targetFormContainer).find('input[type="checkbox"]:checked');
            $formControls.each(function () {
                postData[$(this).attr('name')] = $(this).val() || null;
            });
            $formControlsCheck.each(function(i,item){
                professionalValArr.push($(item).val());
            });
            $result = $(options.resultModalContainerId);
        }else{
            $formControls = $targetForm;
            $result = $(options.resultContainerId);
        }
        $result.html(loading);
        postData[$formControls.attr('name')] = $formControls.val();
        professionalName = $(targetFormContainer).find('input[type="checkbox"]').eq(0).attr('name');
        if(professionalName){
            postData[professionalName] = professionalValArr.join(',');
        }
        var xhr = $.post(url,postData);
        var self = this;
        xhr.success(function(data){
            if(data.length > 0){
                resultTemplate =  self.resultTemplate(data);
                $result.html(resultTemplate);
                self.choose();
            }else{
                $result.html(noResultTemplate)
            }
        });

        xhr.error(function(error){
            $result.html(noResultTemplate)
        })


    };

    LessonCodeSearch.prototype.resultTemplate = function(data){
        var list = data;
        var htmlStr = '';
        var trs = '';
        $.each(list,function(i,item){
            trs += ' <tr><td>'+item.code +'</td><td>'+item.name +'</td><td>'+item.planScore +'</td><td>'+item.planTime +'</td><td>'+item.overScore +'</td><td> <button type="button" data-row='+(JSON.stringify(item))+'  class="btn btn-primary btn-xs">选择</button> </td></tr>'
        });

        htmlStr += '<div class="search-result-container" style="max-width: 499px;"> ' +
            '<table class="table table-bordered table-condensed table-orange"> ' +
            '<thead> ' +
            '<tr> <th>课程代码</th> <th>课程名</th> <th>计划学分</th> <th>计划课时</th> <th>合格成绩</th> <th>操作</th> </tr> </thead>' +
            ' <tbody> '+trs+'  </tbody> </table> </div>';

        return htmlStr
    };


    $.fn.lessonCodeSearch = function(option){
        return this.each(function(){
            var $this = $(this);
            var data = $this.data('code-search');
            var options = $.extend({}, $this.data(), typeof option === 'object' && option);
            if(!data) {
                $this.data('code-search',(data = new LessonCodeSearch(this,options)))
            }
            if(typeof option === 'string'){
                data[option]();
            }
        })
    };

    $.fn.lessonCodeSearch.Constructor = LessonCodeSearch;

   $(function(){
       $(document).on('click','[data-search="lessonCode"]',function(){
            $(this).lessonCodeSearch('search');
       });
        $('.form-block').find('>span').addClass('checkbox-inline');


       var $modalResult = $('#modalResult');
       var $searchCode = $('#searchCode');
       var $modalBtnSearch = $('#modalBtnSearch');

       $modalBtnSearch.lessonCodeSearch({
           targetFormContainer: '#modalFormInline',
           resultModalContainerId: '#modalResultBox',
           url: '/f/scoapply/findCourseList',
           noResultTemplate: '<div class="search-result-container text-center" style="border-top: 1px solid #ddd;padding: 10px 0">没有查询结果，请重新填入搜索条件</div>',
           loading: '<div class="loading text-center"><img src="' + ctxImg + '/loading.gif"></div>'
       });

       $modalResult.on('shown.bs.modal',function(){
           var val =  $searchCode.val();
           var $keyword = $modalResult.find('input[name="keyword"]');
           $keyword.val(val);
       });

       $('#content').css('min-height',($(window).height() - $('.header').height() - $('.footerBox').height()));

       $('#searchCode').on('input propertychange',function(){
           $('#formLesson').find('.form-group').not(':first').find('input').val('')
           $('#courseId').val('')
       })
   })

}(jQuery);