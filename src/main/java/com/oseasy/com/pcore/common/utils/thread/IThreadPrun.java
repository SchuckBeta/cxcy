package com.oseasy.com.pcore.common.utils.thread;

import java.util.List;

public interface IThreadPrun {
    public List<? extends IThreadPvo> datas();
    public void run(List<? extends IThreadPvo> eles);
}
