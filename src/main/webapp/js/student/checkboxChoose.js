/**
 * Created by zhangzheng on 2017/5/31.
 */
var arr=[];
$(function(){
    btnAction(arr);
    $('#check_all').click(function(){
        if($(this).is(':checked')){
            checkAll()
        }else{
            clearAll()
        }
        arr=getValue();
        btnAction(arr);
    })
    $("input[name='boxTd']").click(function(){
        if(isChooseAll()){
            $('#check_all').attr("checked", true);
        }else{
            $('#check_all').attr("checked", false);
        }
        arr=getValue();
        btnAction(arr);
    })


});

function btnAction(array){
    if(array.length==0){
        $('#btnSubmit1').attr('disabled',true);
    }else{
        $('#btnSubmit1').attr('disabled',false);
    }
}

function getValue(){
    var value=[];
    $("input[name='boxTd']").each(function(){
        if($(this).is(':checked')){
            value.push($(this).val());
        }
    })
    return value;
}

function isChooseAll(){
    var flag=true;
    $("input[name='boxTd']").each(function(){
        if(!$(this).is(':checked')){
            flag= false;
        }
    })
    return flag;
}

function checkAll(){
    $('.checkone').children('input').attr('checked', true);
}
function clearAll(){
    $('.checkone').children('input').attr('checked', false);
}

var timer=null;
$('#selectArea').show();
$('#bufferImg').hide();

function doBatch(url){
    //先校验有没有选择
    $('#selectArea').hide();
    $('#bufferImg').show();  //显示等待
    $('#confirmBtn').attr("disabled",true);

    var ids=arr.join(",")
    //ajax 后台处理
    $.ajax({
        type: "post",
        url: url,
        data: {"ids":ids},
        async : true,
        success: function (data) {
            // arr=[];
            // $('#confirmBtn').attr("disabled",false);
            $('#bufferImg').hide();  //取消
            $('#myModal').modal('hide');
            // clearAll();
            alertx(data);
           location.reload();
        }
    });
}




function batchPass(url){
    var ids=arr.join(",");
    //ajax 后台处理
    $.ajax({
        type: "post",
        url: url,
        data: {"ids":ids},
        success: function (data) {
            if (data.ret == '1') {
                location.reload();
            }else{
                alertx('审核失败');
            }
        }
    });
}
