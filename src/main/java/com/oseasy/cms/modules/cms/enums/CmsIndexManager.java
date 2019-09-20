package com.oseasy.cms.modules.cms.enums;

/**
 * 内容管理--错误信息枚举类
 *
 * Created by liangjie on 2018/8/30.
 */
public enum CmsIndexManager {
    HOMEBANNER("homeBanner","banner"),
    HOMEANNOUNCEMENT("homeAnnouncement","通知公告"),
    HOMEPROJECT("homeProject","优秀项目展示"),
    HOMECOURSE("homeCourse","名师讲堂"),
    HOMETEACHER("homeTeacher","导师风采"),
    HOMEGCONTEST("homeGcontest","大赛热点"),
    HOMENOTICE("homeNotice","通知/动态"),
    HOMESCDT("homeSCDT","双创动态"),
    HOMESCTZ("homeSCTZ","双创通知"),
    HOMESSDT("homeSSDT","省市动态");

    private String code;
    private String msg;

    CmsIndexManager(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
