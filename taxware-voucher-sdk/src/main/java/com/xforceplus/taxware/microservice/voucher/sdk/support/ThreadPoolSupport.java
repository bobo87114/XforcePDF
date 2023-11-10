package com.xforceplus.taxware.microservice.voucher.sdk.support;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 业务线程池对象
 *
 * @author Bobo
 * @date 2017/6/9
 */
public class ThreadPoolSupport {
    private static int corePoolSize = 50;
    private ThreadPoolSupport(){}
    private static ExecutorService threadPool = null;

    public static ExecutorService getThreadPool(){
        if (threadPool == null){
            synchronized (ThreadPoolSupport.class){
                if(threadPool == null){
                    threadPool = Executors.newFixedThreadPool(corePoolSize);
                }
            }
        }
        return threadPool;
    }

}
