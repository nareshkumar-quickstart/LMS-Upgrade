package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.util.Brander;

/**
 * Batch Import of Learners Service.
 * @author S M Humayun
 */

public interface BatchImportLearnersService {

    public Map<String, String[]> validateCSVData(
            String csvFileData,
            boolean firstRowHeader,
            String delimiter,
            Brander brander,
            List<CustomField> allCustomFields
    ) throws Exception;

    /**
     * Process the user data contained in the csv file loads them,
     * in the system.
     * @param batchImportData
     * @return
     * @throws Exception 
     */
    public Map<Object, Object> importUsersFromBatchFile(
            BatchImportData batchImportData
    ) throws Exception;

}
