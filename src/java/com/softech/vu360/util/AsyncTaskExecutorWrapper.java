package com.softech.vu360.util;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * User: joong
 * Date: Nov 13, 2009
 */
public class AsyncTaskExecutorWrapper {
    private SimpleAsyncTaskExecutor taskExecutor;

    public AsyncTaskExecutorWrapper(SimpleAsyncTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void execute(Runnable task) {
        taskExecutor.execute(task, AsyncTaskExecutor.TIMEOUT_IMMEDIATE);
    }
}
