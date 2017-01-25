package com.softech.vu360.lms.service.impl;

/**
 * @author S M Humayun
 */
public class OptimizedBatchImportLearnersProcessorThreadLocalContext extends ThreadLocal {

    private String csvFileDataChunk;
    private int recordNumberOffset;

    public OptimizedBatchImportLearnersProcessorThreadLocalContext(String csvFileDataChunk, int recordNumberOffset) {
        this.csvFileDataChunk = csvFileDataChunk;
        this.recordNumberOffset = recordNumberOffset;
    }

    public String getCsvFileDataChunk() {
        return csvFileDataChunk;
    }

    public void setCsvFileDataChunk(String csvFileDataChunk) {
        this.csvFileDataChunk = csvFileDataChunk;
    }

    public int getRecordNumberOffset() {
        return recordNumberOffset;
    }

    public void setRecordNumberOffset(int recordNumberOffset) {
        this.recordNumberOffset = recordNumberOffset;
    }
}
