package com.oseasy.pw.modules.pw.manager.sub;

import com.oseasy.com.pcore.modules.syt.manager.sub.SupSytm;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmPw extends SupSytm<SytmvPw> {
    public SytmPw(SytmvPw sytmvo) {
        super(sytmvo);
    }

    @Override
    public String name() {
        return "入驻";
    }

    @Override
    public boolean before() {
        if(!super.before()){
            this.status.setMsg(name() + "不能为Null！");
        }

        if(StringUtil.isEmpty(this.sytmvo.getId())){
            this.status.setMsg(name() + "ID不能为空！");
        }
        return super.before() && StringUtil.isNotEmpty(this.sytmvo.getId());
    }

    @Override
    public boolean run() {
        if(!before()){
            return false;
        }

        System.out.println(name() + "处理中...");

        after();
        return true;
    }

    @Override
    public boolean after() {
        return true;
    }
}
