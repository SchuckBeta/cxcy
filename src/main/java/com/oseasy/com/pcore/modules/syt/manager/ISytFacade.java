package com.oseasy.com.pcore.modules.syt.manager;

import com.oseasy.com.pcore.modules.syt.manager.sub.SupSytm;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2019/5/7 0007.
 */
public abstract class ISytFacade {
    public final static Logger logger = Logger.getLogger(ISytFacade.class);
    private String id;
    private ISytMvo isyt;

    public ISytFacade() {
    }

    public ISytFacade(String id) {
        this.id = id;
    }

    public boolean initNce(){
        return true;
    }
    public boolean initNpr(){
        return true;
    }
    public abstract boolean initNsc();

    public boolean resetTNce(){
        return true;
    }
    public boolean resetTNpr(){
        return true;
    }
    public boolean resetTNsc(){
        return true;
    }

    public boolean pushNce(){
        return true;
    }
    public boolean pushNpr(){
        return true;
    }
    public boolean pushNsc(String targetId, String groupId, String scid){
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static SupSytm println(SupSytm sytm) {
        sytm.run();
        if(sytm.status.getStatus()){
            sytm.status.setMsg(sytm.name() + "初始化成功，" + sytm.status.getMsg());
        }else{
            sytm.status.setMsg(sytm.name() + "初始化失败，" + sytm.status.getMsg());
        }
        logger.info(sytm.status.getMsg());
        return sytm;
    }

    public static SupSytm printlnPush(SupSytm sytm) {
        sytm.pushTpl();
        if(sytm.status.getStatus()){
            sytm.status.setMsg(sytm.name() + "初始化成功，" + sytm.status.getMsg());
        }else{
            sytm.status.setMsg(sytm.name() + "初始化失败，" + sytm.status.getMsg());
        }
        logger.info(sytm.status.getMsg());
        return sytm;
    }

    public static void main(String[] args) {
    }
}
