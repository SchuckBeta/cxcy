package com.oseasy.com.pcore.modules.syt.manager;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public interface ISytModule {
    public boolean before();
    public boolean run();
    public boolean resetTpl();
    public boolean pushTpl();
    public boolean after();
}
