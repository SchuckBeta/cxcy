var ratio = "";
var snumber = 0;
//学分配比校验
function checkRatio() {
    var result = true;
    if (ratio != "") {
        var creditArr = [];
        $('.credit-ratio input.form-control').each(function (i, item) {
            creditArr.push($(item).val());
        });

        creditArr.sort(function (a, b) {
            return b - a;
        });
        var ratioArr = ratio.split(':').sort(function (a, b) {
            return b - a;
        });

        if (creditArr.join(':') !== ratioArr.join(':')) {
            result = false;
        }
    }
    return result;
}

//根据项团队学生人数查找配比规则
function changeRatio() {
    $.ajax({
        type: 'post',
        url: '/f/gcontest/gContest/findRatio',
        data: {snumber: snumber},
        success: function (data) {
            if (data != "") {
                $("#ratio").text("学分配比规则：" + data);
            } else {
                $('th.credit-ratio,td.credit-ratio').remove();
                $("#ratio").text("");
            }
            ratio = data;
        }
    });
}


$(function () {

    // if ($("#id").val() != "") {
    //     snumber = $(".studenttb tbody tr").size();
    //     if(snumber == null || snumber == undefined ){
    //         snumber=0;
    //     }
    //     changeRatio();
    // }

    tlxy.initTlxy();

    $('input[name="guochuang"]').change(function () {
        var val = $(this).val();
        $(this).parent().parent().find('select').css('visibility', function () {
            return val == "2" ? 'visible' : 'hidden'
        })
    })
});

function getStartDatepicker(row) {
	if(typeof  row === "number") {
		return {ychanged: onStartDateChange,Mchanged: onStartDateChange,dchanged: onStartDateChange};
	} else {
		return {ychanged: onStartDateChange,Mchanged: onStartDateChange,dchanged: onStartDateChange};
	}
}
function getEndDatepicker(row){
	var str;
	if(typeof row === "number") {
		str = "-" + row;
	} else {
		str = "";
	}
	return {minDate:"#F{$dp.$D('plan-start-date" + str + "')}"};
}

function onStartDateChange(dp){
	var v = dp.cal.getNewDateStr();
	var enddate = $(this).siblings('input')[0];
	if(v > enddate.value) {
		enddate.value = v;
	}
	var tr=$(this).parent().parent();
	var nextStart=tr.next().find(".Wdate")[0];
	if(nextStart&& v>nextStart.value){
		tr.nextAll().find(".Wdate").val(v);
	}
	enddate.focus();
}


function checkOnInit() {
    var user_name = $.trim($("input[name='shenbaoren']").val());
    var user_officename = $.trim($("#company").val());
    var user_no = $.trim($("#zhuanye").val());
    var user_professional = $.trim($("input[name='zynj']").val());
    var user_mobile = $.trim($("#mobile").val());
    var user_email = $.trim($("#email").val());
    var isCompletedInfo = false;
    var hasTeam = false;
    isCompletedInfo = !user_name || !user_officename || !user_no || !user_mobile || !user_email;
    if (isCompletedInfo) {
        //不完整
        dialogCyjd.createDialog(0, '个人信息未完善，立即完善个人信息？', {
            buttons: [{
                text: '确定',
                'class': 'btn btn-primary',
                'click': function () {
                    top.location = "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1";
                }
            }]
        })
        return;
    }

    hasTeam = $("select[id='teamId']").find("option").length <= 1;

    if (hasTeam) {
        dialogCyjd.createDialog(0, '无可用团队信息，立即建设团队？', {
            buttons: [{
                text: '确定',
                'class': 'btn btn-primary',
                'click': function () {
                    top.location = "/f/team/indexMyTeamList";
                }
            }]
        })
    }
}
var tlxy = {
    plansIndex: 0,
    initTlxy: function () {
        if ($("#pageType").val() == "edit") {
            onGcontestApplyInit($("[id='actywId']").val(), $("[id='id']").val(), checkOnInit);
        }
        tlxy.addEventToBtn();
        tlxy.printOut();
        tlxy.plansIndex = $(".task tbody tr").size() - 1;
        $("#pId").on('change', function () {
            var options = $("#pId option:selected");
            var opt = options.text();
            $("#projectName").val(opt);
        });
    },
    tabletrminus: function () {
        $(".minus").unbind();
        $(".minus").click(function () {
            if ($(".task tbody tr").size() > 1) {
                var rex = "\\[(.+?)\\]";
                $(this).parent().parent().remove();//删除当前行
                tlxy.plansIndex--;//索引减一
                //重置序号
                $(".task tbody tr").find("td:first").each(function (i, v) {
                    $(this).html(i + 1);
                });
                var index = 0;
                $(".task tbody tr").each(function (i, v) {
                    $(this).find("[name]").each(function (i2, v2) {
                        var name = $(this).attr("name");
                        var indx = name.match(rex)[1];
                        ;
                        $(this).attr("name", name.replace(indx, index));
                    });
                    index++;
                });
            }
            return false;
        });
    },
    tabletrplus: function () {
        $(".plus").unbind();
        $(".plus").click(function () {
            tlxy.plansIndex++;
            var html = '<tr>'
                + '<td>'
                + (tlxy.plansIndex + 1)
                + '</td>'
                + '<td>'
                + '	<textarea name="planList[' + tlxy.plansIndex + '].content" required maxlength="2000" class="form-control" rows="3"></textarea>'
                + '</td>'
                + '<td>'
                + '	<textarea name="planList[' + tlxy.plansIndex + '].description" required maxlength="2000" class="form-control" rows="3"></textarea>'
                + '</td>'
                + '<td class="timeerrorbox">'
                + '	<input id="plan-start-date-'+tlxy.plansIndex + '" class="Wdate date-input required" readonly="readonly" style="width: 120px;" type="text" name="planList[' + tlxy.plansIndex + '].startDate" onClick="WdatePicker()"/>'
                + '	<span>至</span>'
                + '	<input id="plan-end-date-'+tlxy.plansIndex + '" class="Wdate date-input required" readonly="readonly" style="width: 120px;" type="text" name="planList[' + tlxy.plansIndex + '].endDate"  onClick="WdatePicker({dateFmt:\'yyyy-MM-dd\',isShowClear:true,minDate:$(\'#plan-start-date-'+tlxy.plansIndex + '\').val()})"/>'
                + '</td>'
                + '<td>'
                + '	<input type="number" name="planList[' + tlxy.plansIndex + '].cost" class="number form-control required" maxlength="20" />'
                + '</td>'
                + '<td>'
                + '	<textarea name="planList[' + tlxy.plansIndex + '].quality" class="form-control required" rows="3" maxlength="2000"></textarea>'
                + '</td>'
                + '<td>'
                + '	<a class="minus"></a>'
                + '	<a class="plus"></a>'
                + '</td>'
                + '</tr>';
            $(".task tbody").append(html);
            tlxy.addEventToBtn();
            return false;
        })
    },
    addEventToBtn: function () {
        tlxy.tabletrminus();
        tlxy.tabletrplus();
    },
    printOut: function () {
        $("#print").click(function () {
            document.body.innerHTML = document.getElementById('wholebox').innerHTML;
            window.print();
        })
    },

    checkIsToLogin: function (data) {
        try {
            if (data.indexOf("id=\"imFrontLoginPage\"") != -1) {
                return true;
            }
        } catch (e) {
            return false;
        }
    },
    save: function () {
        var ischeck = true;
        ischeck = checkMenuByNum(ischeck);
        if (!ischeck) {
            return false;
        }
        /*//学分配比的校验
         if(!checkRatio()){
         showModalMessage(0, "学分配比不符合规则！",{
         "确定":function() {
         $( this ).dialog( "close" );
         $("input[name='teamUserHistoryList[0].weightVal']" ).focus();
         }
         });
         return false;
         }*/

        if (tlxyValidate.form()) {
            $("#competitionfm").attr("action", "/f/gcontest/gContest/save");

            $("#competitionfm").ajaxSubmit(function (data) {
                if (data.ret == 1) {

                    dialogCyjd.createDialog(1, data.msg, {
                        buttons: [{
                            text: '确定',
                            'class': 'btn btn-primary',
                            'click': function () {
                                location.href = 'list'
                            }
                        }]
                    })
                } else {
                    dialogCyjd.createDialog(0, data.msg)
                }
            });
        }
    },
    submit: function () {
        //学分配比的校验
        var ischeck = true;
        ischeck = checkMenuByNum(ischeck);
        if (!ischeck) {
            return false;
        }
        $("#competitionfm").attr("action", "/f/gcontest/gContest/submit");
        if (tlxyValidate.form()) {
            $('#btnSubmit').hide().prop('disabled', true).prev().hide().prop('disabled', true).end().next().hide().find('input[name="file"]').prop('disabled', true);
            $("#competitionfm").ajaxSubmit(function (data) {
                if (tlxy.checkIsToLogin(data)) {
                    $('#btnSubmit').show().prop('disabled', false).prev().show().prop('disabled', false).end().next().show().find('input[name="file"]').prop('disabled', false);
                    // showModalMessage(0, "未登录或登录超时。请重新登录，谢谢！", {
                    //     确定: function () {
                    //         location.href = 'list'
                    //     }
                    // });

                    dialogCyjd.createDialog(0, '未登录或登录超时。请重新登录，谢谢！', {
                        buttons: [{
                            text: '确定',
                            'class': 'btn btn-primary',
                            'click': function () {
                                location.href = 'list'
                            }
                        }]
                    })
                } else {
                    if (data.ret == 1) {

                        dialogCyjd.createDialog(1, data.msg, {
                            buttons: [{
                                text: '跟踪当前项目',
                                'class': 'btn btn-sm btn-primary',
                                'click': function () {
                                    top.location = "viewList";
                                }
                            }, {
                                text: '返回',
                                'class': 'btn btn-sm btn-default',
                                'click': function () {
                                    top.location = "list";
                                }
                            }]
                        })


                    } else {
                        dialogCyjd.createDialog(0, data.msg)
                        $('#btnSubmit').show().prop('disabled', false).prev().show().prop('disabled', false).end().next().show().find('input[name="file"]').prop('disabled', false);
                    }
                }
            });
        }else {
            tlxyValidate.focusInvalid();
        }
    },
    findTeamPerson: function () {

        var actywId = $.trim($("#actywId").val());

        $.ajax({
            type: "GET",
            url: "findTeamPerson",
            data: "id=" + $("[id='teamId']").val() + "&actywId=" + actywId,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                $(".studenttb tbody").html("");
                $(".teachertb tbody").html("");
                if (data) {
                    var shtml = "";
                    var scount = 1;
                    var thtml = "";
                    var tcount = 1;
                    $.each(data, function (i, v) {
                        if (v.user_type == '1') {
                            shtml = shtml + "<tr>"
                                + "<td>" + scount + "<input type='hidden' name='teamUserHistoryList[" + (scount - 1) + "].userId' value='" + v.userId + "' /></td>"
                                + "<td>" + (v.name || "") + "</td>"
                                + "<td>" + (v.no || "") + "</td>"
                                + "<td>" + (v.org_name || "") + "</td>"
                                + "<td>" + (v.professional || "") + "</td>"
                                + "<td>" + (v.domain || "") + "</td>"
                                + "<td>" + (v.mobile || "") + "</td>"
                                + "<td>" + (v.instudy || "") + "</td>"

                            $.ajax({
                                type: "GET",
                                url: "/f/authorize/checkMenuByNum",
                                data: "num=5",
                                async: false,
                                contentType: "application/json; charset=utf-8",
                                dataType: "json",
                                success: function (data) {
                                    if (data) {
                                        shtml = shtml + "<td class='credit-ratio'><input class='form-control input-sm' name='teamUserHistoryList[" + (scount - 1) + "].weightVal' value=''/> </td>"
                                        //添加表头
                                        if ($("#studentTr th.credit-ratio").size() < 1) {
                                            $("#studentTr").append("<th class='credit-ratio'>学分配比</th>")
                                        }
                                        ;

                                    }
                                },
                                error: function (msg) {
                                    showModalMessage(0, msg);
                                }
                            });


                            +"</tr>";
                            scount++;
                        }
                        if (v.user_type == '2') {
                            thtml = thtml + "<tr>"
                                + "<td>" + tcount + "</td>"
                                + "<td>" + (v.name || "") + "</td>"
                                + "<td>" + (v.no || "") + "</td>"
                                + "<td>" + (v.org_name || "") + "</td>"
                                + "<td>" + (v.technical_title || "") + "</td>"
                                + "<td>" + (v.domain || "") + "</td>"
                                + "<td>" + (v.mobile || "") + "</td>"
                                + "<td>" + (v.email || "") + "</td>"
                                + "</tr>";
                            tcount++;
                        }
                    });


                    snumber = scount - 1;
                    changeRatio();
                    checkGcontestTeam();
                    $(".studenttb tbody").html(shtml);
                    $(".teachertb tbody").html(thtml);
                }
            },
            error: function (msg) {
                showModalMessage(0, msg);
            }
        });
    }
}

function checkMenuByNum(ischeck) {
    $.ajax({
        type: "GET",
        url: "/f/authorize/checkMenuByNum",
        data: "num=5",
        async: false,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data) {
                if (!checkRatio()) {
                    ischeck = false;
                    dialogCyjd.createDialog(0, '学分配比不符合规则', {
                        buttons: [{
                            text: '确定',
                            'class': 'btn btn-primary',
                            'click': function () {
                                $(this).dialog("close");
                                $("input[name='teamUserRelationList[0].weightVal']").focus();
                            }
                        }]
                    })

                }
            }
        },
        error: function (msg) {
            // showModalMessage(0, msg);
            dialogCyjd.createDialog(1, msg.status)
        }
    });
    return ischeck;
}
function checkGcontestTeam() {
    if (!$("[id='teamId']").val()) {
        return;
    }
    $.ajax({
        type: 'post',
        url: '/f/gcontest/gContest/checkGcontestTeam',
        data: {
            proid: $("[id='id']").val(),
            actywId: $("[id='actywId']").val(),
            teamid: $("[id='teamId']").val()
        },
        success: function (data) {
            if (tlxy.checkIsToLogin(data)) {
                dialogCyjd.createDialog(0, '未登录或登录超时。请重新登录，谢谢！', {
                    buttons: [{
                        text: '确定',
                        'class': 'btn btn-primary',
                        'click': function () {
                            top.location = top.location;
                        }
                    }]
                })

            } else {
                if (data.ret == '0') {
                    dialogCyjd.createDialog(0, data.msg)

                }
            }
        }
    });
}
