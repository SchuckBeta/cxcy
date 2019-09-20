

var tlxyValidate = $('#form1').validate({
    rules: {
        'proModel.pName': {
            remote: {
                url: "/f/promodel/proModel/checkProName",     //后台处理程序
                type: "post",               //数据发送方式
                dataType: "json",           //接受数据格式
                data: {                     //要传递的数据
                    actYwId: function() {
                        return $("[id='actYwId']").val();
                    },
                    id: function () {
                        return $("[id='id']").val();
                    },
                    pName: function () {
                        return $('input[name="proModel.pName"]').val()
                    }
                }
            }
        },
    },
    messages: {
        'proModel.pName': {
            remote: '项目名称已经存在'
        }
    },
    errorPlacement : function(error,element) {
        if (element.is(":checkbox")|| element.is(":radio")) {
           error.appendTo(element.parent().parent());
        }else if (element.is(".declareDate")) {
            error.appendTo(element.parent());
        }else if(element.is('.Wdate')){
            error.appendTo(element.parent());
        }else if(element.is(".budget-dollar")){
            error.appendTo(element.parent());
        }else{
            error.insertAfter(element);
        }
    }

})
