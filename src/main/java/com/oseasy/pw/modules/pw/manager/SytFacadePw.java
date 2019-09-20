package com.oseasy.pw.modules.pw.manager;

import com.oseasy.com.pcore.modules.syt.manager.ISytFacade;
import com.oseasy.pw.modules.pw.manager.sub.SytmPw;
import com.oseasy.pw.modules.pw.manager.sub.SytmvPw;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytFacadePw extends ISytFacade {
    public SytFacadePw(String id) {
        super(id);
    }

    @Override
    public boolean initNsc(){
        return true;
    }

    public static SytmPw initSytmPw() {
        return (SytmPw) ISytFacade.println(new SytmPw(new SytmvPw("1")));
    }
}
