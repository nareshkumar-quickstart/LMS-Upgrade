package com.softech.vu360.lms.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author S M Humayun
 */
public class OptimizedBatchImportLearnersProcessorThread extends Thread
{
    private static final Logger log = Logger.getLogger(OptimizedBatchImportLearnersProcessorThread.class.getName());
    private OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext;
    private OptimizedBatchImportLearnersProcessorThreadLocalContext localContext;

    public OptimizedBatchImportLearnersProcessorThread (Runnable runnable, String threadName, String csvDataChunk, int recordNumberOffset, OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext)
    {
        super(runnable, threadName);
        localContext = new OptimizedBatchImportLearnersProcessorThreadLocalContext(csvDataChunk, recordNumberOffset);
        this.sharedContext = sharedContext;
    }

    public OptimizedBatchImportLearnersProcessorThreadLocalContext getLocalContext() {
        return localContext;
    }

    public void setLocalContext(OptimizedBatchImportLearnersProcessorThreadLocalContext localContext) {
        this.localContext = localContext;
    }

    public OptimizedBatchImportLearnersProcessorThreadSharedContext getSharedContext() {
        return sharedContext;
    }

    public void setSharedContext(OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext) {
        this.sharedContext = sharedContext;
    }

    private void logDebug (String str)
    {
        log.info(str);
    }
}
