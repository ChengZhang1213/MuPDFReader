package com.chengzhang.mupdfreader.app.thread;

import java.util.concurrent.Executor;

/**
 * Created by zhangcheng on 15/7/8.
 */
public class ThreadPerTaskExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
