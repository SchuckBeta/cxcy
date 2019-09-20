var editCertId;
+function ($) {

    function CertManage() {
        this.contentImg = $('.cert-content-img');
        this.contentImgLen = this.contentImg.length;
        this.tabLi = $('.cert-ul li');
        this.wWidth = $(window).width();
        this.lookWidth = this.wWidth * 0.65;
        this.lookHeight = this.lookWidth * 0.63;
        this.bannerWidth = this.lookWidth * 0.91;
        this.bannerHeight = this.lookHeight * 0.86;
        this.togetherUl = $('.together-ul').height();
        this.changeUl = $('.change-ul').height();

        this.dialogLook = $("#dialog-look");
        this.lookBanner = $('.dialog-look-banner');
        this.dialogTogether = $("#dialog-together");
        this.certTogether = $(".cert-together");
        this.dialogChange = $("#dialog-change");
        this.certChangeModel = $(".cert-changeModel");
        this.certLook = $(".cert-look");
        this.certShadeLook = $('.cert-shade');

        this.init();

    }


    CertManage.prototype.init = function () {
        this.longPhotoCenter();
        this.dialogShow();
        this.dialogToge();
        this.dialogChan();
        this.other();
        this.edtit();
    }

    //tab页中让竖长图片居中 并 切换
    CertManage.prototype.longPhotoCenter = function () {
        for (var n = 0; n < this.contentImgLen; n++) {
            var cn = this.contentImg.eq(n);
            var cw = parseInt(cn.css('width').replace("px", ""));
            var ch = parseInt(cn.css('height').replace("px", ""));
            if (cw < ch) {
                cn.css({
                    width: 'auto',
                    height: '225px',
                    position: 'relative',
                    left: '50%',
                    transform: 'translateX(-50%)'
                });
            }
        }
        this.tabLi.click(function () {
            var index = $(this).index();
            $(this).parent().next().find('.cert-content>div').hide().eq(index).show();
            $(this).addClass('active').siblings().removeClass('active');
        });
    }

    //关联弹框
    CertManage.prototype.dialogToge = function () {
        var _this = this;
        this.dialogTogether.dialog({
            autoOpen: false,
            height: this.togetherUl + 255,
            width: 470,
            modal: true,
            dialogClass: 'dialog-together',
            closeText: '',
            containment: 'document'
        });
        this.certTogether.on("click", function () {
            editCertId = $(this).parent().parent().parent().parent().attr("certid");
            _this.dialogTogether.dialog("open");
        });
    }

    //修改弹框
    CertManage.prototype.dialogChan = function () {
        var _this = this;
        this.dialogChange.dialog({
            autoOpen: false,
            height: this.changeUl + 255,
            width: 470,
            modal: true,
            containment: 'document'
        });
        this.certChangeModel.on("click", function () {
            _this.dialogChange.dialog("open");
        });
    }

    //预览弹框 手动轮播
    CertManage.prototype.dialogShow = function () {
        var _this = this;

        this.dialogLook.dialog({
            autoOpen: false,
            width: this.lookWidth,
            modal: true,
            dialogClass: 'dialog-look',
            closeText: '',
            containment: 'document'
        });

        this.dialogLook.on('dialogclose', function () {
            lookImg.css('left', '');
            $(".dialog-look-banner .cert-content").remove();
        })

        var startIndex;
        var $index;
        var $exdex;
        var len;
        var lookImg;

        this.certShadeLook.on("click", function () {


            //得到正在显示的那个图片的索引
            var content = $(this).prev();                //yaogai
            var imgs = content.find('.cert-content-img').not('.error');
            var imgsLen = imgs.length;


            var indexF;
            var lis = $(this).parent().prev().find('li');
            var lisActive = $(this).parent().prev().find('.active');
            startIndex = lis.index(lisActive);
            indexF = content.find('.cert-content-img').index(imgs.eq(0));
            if(imgsLen < 1 || content.find('.cert-content-img').eq(startIndex).hasClass('error')){
                return false
            }

            startIndex -= indexF;

            _this.dialogLook.dialog("open");
            var imgHtml = '';

            for (var j = 0; j < imgsLen; j++) {
                var img = imgs.eq(j);
                var imgSrc = img.attr('src');
                imgHtml += '<div><img class="cert-content-img" src="' + imgSrc + '" alt=""></div>';
            }

            _this.lookBanner.append('<div class="cert-content">' + imgHtml + '</div>');

            lookImg = $('.dialog-look-banner .cert-content-img');
            len = lookImg.length;


            //得到当前点击所得到的图片，当其为长图片时  使其在轮播中居中显示
            for (var m = 0; m < len; m++) {
                var lm = lookImg.eq(m);
                var lw = parseInt(lm.css('width').replace("px", ""));
                var lh = parseInt(lm.css('height').replace("px", ""));
                var scaleW = (lw / lh) * _this.bannerHeight;
                var scaleH = (lh / lw) * _this.bannerWidth;
                if (lh < _this.bannerHeight) {
                    lm.css({
                        width: _this.bannerWidth,
                        height: 'auto',
                        marginTop: (_this.bannerHeight - scaleH) / 2
                    });
                } else {
                    lm.css({
                        width: 'auto',
                        height: _this.bannerHeight,
                        marginLeft: (_this.bannerWidth - scaleW) / 2
                    });
                }

            }

            _this.lookBanner.find('.cert-content-img').removeClass('first');
            _this.lookBanner.find('.cert-content-img').eq(startIndex).addClass('first');
        });

        //让预览图片弹框宽高自适应
        this.lookBanner.css({
            width: this.bannerWidth,
            height: this.bannerHeight
        });


        $(".next").click(function () {
            //手动轮播
            var nextLen = lookImg.length;
            $index = startIndex;
            $exdex = startIndex;

            $index++;
            if (nextLen > 1) {
                if ($index > len - 1) {
                    $index = 0;
                }
                startIndex = $index;
                next();
            }
        });

        //上一页的点击事件
        $(".pre").click(function () {
            var preLen = lookImg.length;
            $index = startIndex;
            $exdex = startIndex;

            $index--;

            if (preLen > 1) {
                if ($index < 0) {
                    $index = len - 1;
                }
                ;
                startIndex = $index;
                pre();
            }


        });


        //这里为右移和左移的事件函数。
        //右移基本原理就是先让exdex定位的left左移百分百，而选中的当前页从屏幕右边移入,left变为0
        function next() {
            lookImg.eq($index).stop(true, true).css("left", "100%").animate({"left": "0"});
            lookImg.eq($exdex).stop(true, true).css("left", "0").animate({"left": "-100%"});
        }

        function pre() {
            lookImg.eq($index).stop(true, true).css("left", "-100%").animate({"left": "0"});
            lookImg.eq($exdex).stop(true, true).css("left", "0").animate({"left": "100%"});
        }

        //左右箭头居中
        $('.dialog-look-banner .pre, .next').css('top', this.bannerHeight / 2 - 30);


    }

    CertManage.prototype.other = function () {

        var liChecked;
        $('.drop-together li').click(function () {
            liChecked = $.trim($(this).find('a').text());
            $(this).parent().prev().attr("scfid", $(this).attr("scfid"));
            $(this).parent().prev().html(liChecked + ' <span class="caret"></span>');
            // $(this).parent().children().css('background', 'white').find('a').css('color', 'black');
            // $(this).css('background', '#E9432D').find('a').css('color', 'white');
        });

        //点击操作按钮变色，点击别处恢复
        var _this;
        var $buttonChange = $('.button-change');
        var $spanCaret = $('.button-change .caret');
        var $certCheckInput = $('.cert-check input');



        //模板被选择时，删除模板按钮亮起
        $certCheckInput.change(function () {
            if ($(this).prop("checked") && $(this).attr("releases") == "1") {
                $(this).prop("checked", false)
                alertx("不能删除已发布的证书");
                return false;
            }
            if ($('.cert-check input[type="checkbox"]:checked').length > 0) {
                $('.cert-del').removeClass('cert-dele').addClass('btn-primary');
            } else {
                $('.cert-del').removeClass('btn-primary').addClass('cert-dele');
            }
        });


    }


    CertManage.prototype.edtit = function () {
        var $dblEdit = $('.dbl-edit');
        var _this;
        //双击可以编辑
        $dblEdit.dblclick(function () {
            _this = this;
            $(_this).attr('readonly', false);
            $('[data-toggle="tooltip"]').tooltip('hide');
        });
        //编辑时监听内容、长度
        $dblEdit.on('input propertychange', function () {
            $(this).next().text($(this).val());
            $('[data-toggle="tooltip"]').tooltip('hide');
        });
        $dblEdit.focus(function () {
            $(this).css({
                border: 'none',
                outline: 'none',
                boxShadow: 'none'
            });
            $('[data-toggle="tooltip"]').tooltip('hide');
        });
        $('[data-toggle="tooltip"]').tooltip();

        $dblEdit.change(function () {
            var pagename = $(_this).val();
            var pageid = $(_this).parent().parent().attr('pageid');
            $.ajax({
                type: "POST",
                url: "/a/cert/savePageName",
                data: {
                    pageid: pageid,
                    pagename: pagename
                },
                success: function (data) {
                    if (data.ret == "1") {
                        alertx('修改成功', function () {
                            location.reload();
                        });
                    } else {
                        alertx('修改失败');
                    }
                }
            });
            $(_this).attr('readonly', true);
        });


    }

    var certManage = new CertManage();


    // //获取Tab页中的每一张图片 当其为长图片时 让其居中显示
    // var contentImg = $('.cert-content-img');
    // var contentImgLen = $('.cert-content-img').length;
    //
    //
    // for (var n = 0; n < contentImgLen; n++) {
    //     var cn = contentImg.eq(n);
    //     var cw = parseInt(cn.css('width').replace("px", ""));
    //     var ch = parseInt(cn.css('height').replace("px", ""));
    //     if (cw < ch) {
    //         cn.css({
    //             width: 'auto',
    //             height: '225px',
    //             position: 'relative',
    //             left: ' 50%',
    //             transform: 'translateX(-50%)'
    //         });
    //     }
    // }
    //
    // //tab切换
    // $('.cert-ul li').click(function () {
    //     var index = $(this).index();
    //     $(this).parent().next().find('.cert-content-img').hide().eq(index).show();
    //     $(this).addClass('active').siblings().removeClass('active');
    // });
    //
    // var wWidth = $(window).width();
    // var lookWidth = wWidth * 0.65;
    // var lookHeight = lookWidth * 0.63;
    // var bannerWidth = lookWidth * 0.91;
    // var bannerHeight = lookHeight * 0.86;
    // var togetherUl = $('.together-ul').height();
    // var changeUl = $('.change-ul').height();
    //
    // $('.dialog-look-banner').css({
    //     width: bannerWidth,
    //     height: bannerHeight
    // });
    //
    // $('.dialog-look-banner .pre, .next').css('top', bannerHeight / 2 - 30);
    //
    // $("#dialog-together").dialog({
    //     autoOpen: false,
    //     height: togetherUl + 255,
    //     width: 470,
    //     modal: true,
    //     containment: 'document'
    // });
    //
    // $(".cert-together").on("click", function () {
    //     editCertId = $(this).parent().attr("certid");
    //     $('#dialog-together').dialog("open");
    // });
    //
    // $("#dialog-change").dialog({
    //     autoOpen: false,
    //     height: changeUl + 255,
    //     width: 470,
    //     modal: true,
    //     containment: 'document'
    // });
    //
    // $(".cert-changeModel").on("click", function () {
    //     $('#dialog-change').dialog("open");
    // });
    //
    //
    // $("#dialog-look").dialog({
    //     autoOpen: false,
    //     width: lookWidth,
    //     modal: true,
    //     containment: 'document'
    // });
    //
    //
    // var startIndex;
    // var $index;
    // var $exdex;
    // var len;
    // var lookImg;
    //
    // $('#dialog-look').on('dialogclose', function () {
    //     $(".dialog-look-banner .cert-content .cert-content-img").css('left', '');
    //     $(".dialog-look-banner .cert-content").remove();
    //
    // })
    //
    //
    //
    // $(".cert-look").on("click", function () {
    //     $('#dialog-look').dialog("open");
    //
    //     //得到正在显示的那个图片的索引
    //     var content = $(this).parent().next().next();
    //     var imgs = content.find('.cert-content-img');
    //     var imgsLen = imgs.length;
    //
    //     var lis = $(this).parent().next().find('li');
    //     var lisLen = lis.length;
    //
    //     for (var i = 0; i < lisLen; i++) {
    //         if (lis.eq(i).hasClass('active')) {
    //             startIndex = i;
    //         }
    //     }
    //
    //
    //     var imgHtml = '';
    //
    //     for (var j = 0; j < imgsLen; j++) {
    //         var img = imgs.eq(j);
    //         var imgSrc = img.attr('src');
    //         imgHtml += '<div><img class="cert-content-img" src="' + imgSrc + '" alt=""></div>';
    //     }
    //
    //     $('#dialog-look .dialog-look-banner').append('<div class="cert-content">' + imgHtml + '</div>');
    //
    //     // len = $('.dialog-look-banner .cert-content-img').length;
    //
    //     lookImg = $('.dialog-look-banner .cert-content-img');
    //     len = lookImg.length;
    //
    //
    //     //得到当前点击所得到的图片，当其为长图片时  使其在轮播中居中显示
    //     for (var m = 0; m < len; m++) {
    //         var lm = lookImg.eq(m);
    //         var lw = parseInt(lm.css('width').replace("px", ""));
    //         var lh = parseInt(lm.css('height').replace("px", ""));
    //         var scalew = (lw / lh) * bannerHeight;
    //         lm.css({
    //             width: 'auto',
    //             height: bannerHeight,
    //             marginLeft: (bannerWidth - scalew) / 2
    //         });
    //     }
    //
    //     $('.dialog-look-banner').find('.cert-content-img').removeClass('first');
    //     $('.dialog-look-banner').find('.cert-content-img').eq(startIndex).addClass('first');
    // });
    //
    //
    // $(".next").click(function () {
    //     //手动轮播
    //     var nextLen = $('.dialog-look-banner .cert-content-img').length;
    //     $index = startIndex;
    //     $exdex = startIndex;
    //
    //     $index++;
    //     if (nextLen > 1) {
    //         if ($index > len - 1) {
    //             $index = 0;
    //         }
    //         startIndex = $index;
    //         next();
    //     }
    // });
    //
    // //上一页的点击事件
    // $(".pre").click(function () {
    //     var preLen = $('.dialog-look-banner .cert-content-img').length;
    //     $index = startIndex;
    //     $exdex = startIndex;
    //
    //     $index--;
    //
    //     if (preLen > 1) {
    //         if ($index < 0) {
    //             $index = len - 1;
    //         }
    //         ;
    //         startIndex = $index;
    //         pre();
    //     }
    //
    //
    // });
    //
    //
    // //这里为右移和左移的事件函数。
    // //右移基本原理就是先让exdex定位的left左移百分百，而选中的当前页从屏幕右边移入,left变为0
    // function next() {
    //     $(".dialog-look-banner .cert-content-img").eq($index).stop(true, true).css("left", "100%").animate({"left": "0"});
    //     $(".dialog-look-banner .cert-content-img").eq($exdex).stop(true, true).css("left", "0").animate({"left": "-100%"});
    // }
    //
    // function pre() {
    //     $(".dialog-look-banner .cert-content-img").eq($index).stop(true, true).css("left", "-100%").animate({"left": "0"});
    //     $(".dialog-look-banner .cert-content-img").eq($exdex).stop(true, true).css("left", "0").animate({"left": "100%"});
    // }
    //
    //
    // $('.ui-dialog .ui-dialog-titlebar').css({
    //     margin: '-2.5px -3px 0 -2.5px',
    //     border: 'none',
    //     borderRadius: '0'
    // });
    //
    // $('.cert-imgs').hover(function () {
    //     $(this).find('.cert-delete').show();
    // }, function () {
    //     $(this).find('.cert-delete').hide();
    // });
    //
    // var liChecked;
    // $('.dropdown-menu li').click(function () {
    //     liChecked = $.trim($(this).find('a').text());
    //     $(this).parent().prev().attr("scfid", $(this).attr("scfid"));
    //     $(this).parent().prev().html(liChecked + ' <span class="caret"></span>');
    //     $(this).parent().children().css('background', 'white').find('a').css('color', 'black');
    //     $(this).css('background', '#E9432D').find('a').css('color', 'white');
    // });


}(jQuery);


$(document).on('click', '.cert-delete', function (e) {
    if ($(this).attr("releases") == "1") {
        alertx("不能删除已发布的证书");
        return false;
    }
    var cc = $(this).parent().prev().find("li").length;
    if (cc == 1) {
        var certid = $(this).parent().prev().attr("certid");
        confirmx("只有一个模板页，将删除整个模板，确定删除？", function () {
            $.ajax({
                type: "POST",
                url: "/a/cert/deleteAll",
                data: {ids: certid},
                success: function (data) {
                    if (data.ret == "1") {
                        alertx(data.msg, function () {
                            location.reload();
                        });
                    } else {
                        alertx(data.msg);
                    }
                }
            });
        });
    } else {
        var pageid = $(this).parent().prev().find(".active").attr("pageid");
        deleteCertPage(pageid);
    }
});
function deleteCertPage(id) {
    confirmx("确定删除模板页？", function () {
        $.ajax({
            type: "POST",
            url: "/a/cert/deleteCertPage",
            data: {id: id},
            success: function (data) {
                if (data.ret == "1") {
                    alertx(data.msg, function () {
                        location.reload();
                    });
                } else {
                    alertx(data.msg);
                }
            }
        });
    });

}
function flowChange() {
    $("#node").find("option:gt(0)").remove();
    if ($("#flow").val()) {
        $.ajax({
            type: 'get',
            url: '/a/actyw/actYwGnode/treeDataByYwId?level=1&ywId=' + $("#flow").val(),
            success: function (data) {
                if (data) {
                    $.each(data, function (i, v) {
                        $("#node").append('<option value="' + v.id + '" >' + v.name + '</option>');
                    });
                }
            }
        });
    }
}
function saveFlowNode() {
    if (!$("#flow").val()) {
        alertx("请选择下发证书项目");
        return;
    }
    if (!$("#node").val()) {
        alertx("请选择下发证书节点");
        return;
    }
    $('#dialog-together').dialog("close");
    $.ajax({
        type: "POST",
        url: "/a/cert/saveCertFlow",
        data: {certId: editCertId, flow: $("#flow").val(), node: $("#node").val()},
        success: function (data) {
            if (data.ret == "1") {
                alertx(data.msg, function () {
                    location.reload();
                });
            } else {
                alertx(data.msg);
            }
        }
    });
}
function delCertFlow(ob) {
    var scfid = $(ob).parents('.cert-handlebar').find(".dropdown>button").attr("scfid");
    if (scfid == "") {
        alertx("请选择要删除的关联");
        return;
    }
    confirmx("确定删除？", function () {
        $.ajax({
            type: "POST",
            url: "/a/cert/delCertFlow",
            data: {id: scfid},
            success: function (data) {
                if (data.ret == "1") {
                    alertx(data.msg, function () {
                        location.reload();
                    });
                } else {
                    alertx(data.msg);
                }
            }
        });
    });
}
function unrelease(ob) {
    $.ajax({
        type: "POST",
        url: "/a/cert/unrelease",
        data: {id: $(ob).parent().parent().parent().parent().attr("certid")},
        success: function (data) {
            if (data.ret == "1") {
                alertx(data.msg, function () {
                    location.reload();
                });
            } else {
                alertx(data.msg);
            }
        }
    });
}
function release(ob) {
    $.ajax({
        type: "POST",
        url: "/a/cert/release",
        data: {id: $(ob).parent().parent().parent().parent().attr("certid")},
        success: function (data) {
            if (data.ret == "1") {
                alertx(data.msg, function () {
                    location.reload();
                });
            } else {
                alertx(data.msg);
            }
        }
    });
}
function deleteAll() {
    var str = "";
    var arr = new Array();
    $.each($(".cert-checkbox:checked"), function (i, v) {
        arr.push($(v).val());
    });
    str = arr.join(",");
    if (str != "") {
        confirmx("确定删除所选证书模板？", function () {
            $.ajax({
                type: "POST",
                url: "/a/cert/deleteAll",
                data: {ids: str},
                success: function (data) {
                    if (data.ret == "1") {
                        alertx(data.msg, function () {
                            location.reload();
                        });
                    } else {
                        alertx(data.msg);
                    }
                }
            });
        });
    }

}
function editPage(ob) {
    var certid = $(ob).parent().parent().parent().parent().attr("certid");
    var pageid = $(ob).parent().parent().parent().parent().next().find(".active").attr("pageid");
    location.href = "/a/cert/form?certpageid=" + pageid + "&certid=" + certid;
}