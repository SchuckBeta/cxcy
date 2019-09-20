var id_pix = "p55c4142a72584313a96a8e38d2d16368_";
var pageNo = 1;
var pageSize;
var queryType = 1;

!dialogCyjd && console.log('need update');

$(document).ready(function () {
    if (!localStorage.p55c4142a72584313a96a8e38d2d16368) {
        $.ajax({
            type: "GET",
            url: "/f/interactive/sysLikes/uuid",
            dataType: "text",
            success: function (data) {
                localStorage.p55c4142a72584313a96a8e38d2d16368 = data;
            },
        });
    }
    initData();
});
function initData() {
    $("#comment-load-btn").removeAttr("onclick");
    $("#comment-load-btn").html("加载中...");
    $("span[name='" + id_pix + "comments']").html($("input[name='" + id_pix + "comments']").val());
    $("span[name='" + id_pix + "likes']").html($("input[name='" + id_pix + "likes']").val());
    $.ajax({
        type: "GET",
        url: "/f/interactive/sysComment/getCommentData",
        data: "pageNo=" + pageNo + "&foreignId=" + $("input[name='" + id_pix + "foreignId']").val() + "&token=" + localStorage.p55c4142a72584313a96a8e38d2d16368,
        dataType: "json",
        success: function (data) {
            if (data) {
                pageSize = data.pageSize;
                $("span[name='" + id_pix + "mycomments']").html(data.myComments);
                if (data.isExistsLike == "1") {
                    $("#like_a").removeAttr("onclick");
                    $("#like_a").addClass("article-havelike-a");
                    $("#like_a").find("i").addClass("article-havelike-i");
                }
                var tpl = $("#tpl").html();
                if (data.list) {
                    $.each(data.list, function (i, v) {
                        var comment_a = "";
                        var comment_i = ""
                        var userUrl = "javascript:void(0);";
                        var a_target = "_self";
                        if (v.is_open == '1') {
                            a_target = "_blank";
                            if (v.is_open == '1') {
                                if (v.user_type == "1") {
                                    userUrl = "/f/sys/frontStudentExpansion/form?id=" + v.user_id;
                                }
                                if (v.user_type == "2") {
                                    userUrl = "/f/sys/frontTeacherExpansion/view?id=" + v.user_id;
                                }
                            }
                        }
                        if (v.existsLikes > 0) {
                            comment_a = "comment-havelike-a";
                            comment_i = "comment-havelike-i";
                        }
                        $("#comments").append(Mustache.render(tpl, {
                            photo: v.photo,
                            name: v.name,
                            create_date: v.create_date,
                            likes: v.likes,
                            content: v.content,
                            id: v.id,
                            userUrl: userUrl,
                            a_target: a_target,
                            comment_a: comment_a,
                            comment_i: comment_i
                        }));
                    });
                }
                if (data.list && data.list.length == pageSize) {
                    pageNo++;
                    $("#comment-load-btn").attr("onclick", "getNextPage()");
                    $("#comment-load-btn").html("加载更多评论");
                } else {
                    $("#comment-load-btn").removeAttr("onclick");
                    $("#comment-load-btn").html("没有更多评论");
                }
            }
        },
        error: function (msg) {
            try {
                dialogCyjd.createDialog(0, msg)
            }catch(e) {
                showModalMessage(0, msg)
            }
        }
    });

}
function changeTab(ob) {
    $("#comments").html("");
    pageNo = 1;
    queryType = $(ob).attr("queryType");
    $(".tab-comment-nav li").removeClass("active");
    $(ob).addClass("active");
    getNextPage();
}
function getNextPage() {
    $("#comment-load-btn").removeAttr("onclick");
    $("#comment-load-btn").html("加载中...");
    $.ajax({
        type: "GET",
        url: "/f/interactive/sysComment/getNextPage",
        data: "queryType=" + queryType + "&pageNo=" + pageNo + "&foreignId=" + $("input[name='" + id_pix + "foreignId']").val()
        + "&token=" + localStorage.p55c4142a72584313a96a8e38d2d16368,
        dataType: "json",
        success: function (data) {
            if (data) {
                var tpl = $("#tpl").html();
                $.each(data, function (i, v) {
                    var comment_a = "";
                    var comment_i = ""
                    var userUrl = "javascript:void(0);";
                    var a_target = "_self";
                    if (v.is_open == '1') {
                        a_target = "_blank";
                        if (v.is_open == '1') {
                            if (v.user_type == "1") {
                                userUrl = "/f/sys/frontStudentExpansion/form?id=" + v.user_id;
                            }
                            if (v.user_type == "2") {
                                userUrl = "/f/sys/frontTeacherExpansion/view?id=" + v.user_id;
                            }
                        }
                    }
                    if (v.existsLikes > 0) {
                        comment_a = "comment-havelike-a";
                        comment_i = "comment-havelike-i";
                    }
                    $("#comments").append(Mustache.render(tpl, {
                        photo: v.photo,
                        name: v.name,
                        create_date: v.create_date,
                        likes: v.likes,
                        content: v.content,
                        id: v.id,
                        userUrl: userUrl,
                        a_target: a_target,
                        comment_a: comment_a,
                        comment_i: comment_i
                    }));
                });
            }
            if (data && data.length == pageSize) {
                pageNo++;
                $("#comment-load-btn").attr("onclick", "getNextPage()");
                $("#comment-load-btn").html("加载更多评论");
            } else {
                $("#comment-load-btn").removeAttr("onclick");
                $("#comment-load-btn").html("没有更多评论");
            }
        },
        error: function (msg) {


            try {
                dialogCyjd.createDialog(0, msg)
            }catch(e) {
                showModalMessage(0, msg)
            }
        }
    });
}
function publish() {
    if ($("#" + id_pix + "content").val().length > 500) {
        // showModalMessage(0, "最多500个字");
        try {
            dialogCyjd.createDialog(0, '最多500个字')
        }catch(e) {
            showModalMessage(0, '最多500个字')
        }
        return;
    }
    var data = {
        foreignId: $("input[name='" + id_pix + "foreignId']").val(),
        foreignType: $("input[name='" + id_pix + "foreignType']").val(),
        content: encodeURIComponent($("#" + id_pix + "content").val())
    };
    $.ajax({
        type: "POST",
        url: "/f/interactive/sysComment/save",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.ret == 1) {
                $("#" + id_pix + "content").val("");
                // showModalMessage(1, data.msg);
            }
            try {
                dialogCyjd.createDialog(data.ret, data.msg)
            }catch(e) {
                showModalMessage(data.ret, data.msg)
            }
        },
        error: function (msg) {
            try {
                dialogCyjd.createDialog(data.ret, msg)
            }catch(e) {
                showModalMessage(data.ret, msg)
            }
        }
    });
}
function like(ob) {
    $("#like_a").removeAttr("onclick");
    $("#like_a").addClass("article-havelike-a");
    $("#like_a").find("i").addClass("article-havelike-i");
    $("span[name='" + id_pix + "likes']").html(parseInt($("span[name='" + id_pix + "likes']").html()) + 1);
    var data = {
        foreignId: $("input[name='" + id_pix + "foreignId']").val(),
        foreignType: $("input[name='" + id_pix + "foreignType']").val(),
        token: localStorage.p55c4142a72584313a96a8e38d2d16368
    };
    saveLike(data);
}
function commentlike(ob, foreignId) {
    $(ob).addClass("comment-havelike-a");
    $(ob).find("i").addClass("comment-havelike-i");
    $(ob).removeAttr("onclick");
    var likes = $(ob).find("span");
    likes.html(parseInt(likes.html()) + 1);
    var data = {
        foreignId: foreignId,
        foreignType: '0',
        token: localStorage.p55c4142a72584313a96a8e38d2d16368
    };
    saveLike(data);
}
function saveLike(data) {
    $.ajax({
        type: "POST",
        url: "/f/interactive/sysLikes/save",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.ret == 1) {
            } else {

                try {
                    dialogCyjd.createDialog(data.ret, data.msg)
                }catch(e) {
                    showModalMessage(data.ret, data.msg)
                }
            }
        },
        error: function (msg) {
            try {
                dialogCyjd.createDialog(data.ret, msg)
            }catch(e) {
                showModalMessage(data.ret, msg)
            }
        }
    });
}