package com.oseasy.com.pcore.common.utils.thread;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.oseasy.com.pcore.common.config.CoreSval;

public class ThreadPoolUtils {
	public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Integer.parseInt(CoreSval.getConfig("threadMaxNum")));
	public static int maxThread = 18;
	public static int minRow = 300;

	public static void run(int maxThread, int minRow, IThreadPrun itPool) {
        int curMaxRow = itPool.datas().size() / maxThread;
        System.out.println("run.curMaxRow="+curMaxRow);
        ThreadPoolUtils.runThread(itPool, itPool.datas());
        if((itPool.datas().size() < minRow)){
            ThreadPoolUtils.runThread(itPool, itPool.datas());
        }else {
            //需要实现逻辑
            ThreadPoolUtils.runThread(itPool, itPool.datas());
////            if((curMaxRow <= minRow)){
//                for (int i = 0; i < maxThread; i++) {
//                    List<? extends IThreadPvo> cureles = null;
//                    if (i == (maxThread - 1)) {//最后一个链接处理剩余全部文件
//                        cureles = itPool.datas().subList(i * curMaxRow, itPool.datas().size());
//                    }else{
//                        cureles = itPool.datas().subList(i * curMaxRow, ((i + 1) * curMaxRow));
//                    }
//                    if (StringUtil.checkNotEmpty(cureles)) {
//                        ThreadPoolUtils.runThread(itPool, cureles);
//                    }
//                }
////            }else{
////                ThreadPoolUtils.runThread(itPool, cureles);
////                curMaxRow = minRow;
////                for (int i = 0; i < itPool.datas().size(); ) {
////                    List<? extends IThreadPvo> cureles = null;
////                    if (i == (maxThread - 1)) {//最后一个链接处理剩余全部文件
////                        cureles = itPool.datas().subList(i, itPool.datas().size());
////                    }else{
////                        cureles = itPool.datas().subList(i, (i + curMaxRow));
////                    }
////
////
////                    if (StringUtil.checkNotEmpty(cureles)) {
////                    }
////                    i = i + curMaxRow;
////                }
//            }
        }
    }

	public static void runThread(final IThreadPrun itPool, final List<? extends IThreadPvo> eles) {
        ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
            @Override
            public void run() {
                try {
                    itPool.run(eles);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

	public static void main(String[] args) {
//      ThreadPoolUtils.run(ThreadPoolUtils.maxThread, minRow, new IThreadPrun() {
//          @Override
//          public List<DrCard> datas() {
//              return adrCards;
//          }
//
//          @Override
//          @SuppressWarnings("unchecked")
//          public void run(List<? extends IThreadPvo> eles) {
//              drCardService.updateUserByPl((List<DrCard>) eles);
//              System.out.println("DrCard.runThread 处理完成");
//          }
//      });
    }
}
