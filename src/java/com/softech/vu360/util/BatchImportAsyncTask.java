package com.softech.vu360.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.service.BatchImportData;
import com.softech.vu360.lms.service.BatchImportLearnersService;
import com.softech.vu360.lms.vo.BatchImportParam;

/**
 * User: Faisal A. Siddiqui
 * Date: May 27, 2010
 * @since LMS-5781
 */
public class BatchImportAsyncTask implements Runnable {

    private BatchImportParam batchImportParam;
    private BatchImportLearnersService batchImportService;
    private static final Logger log = Logger.getLogger(BatchImportAsyncTask.class);
   
    @Override
    public void run() {
        try {
            batchImportService.importUsersFromBatchFile(new BatchImportData(batchImportParam,
                    getCustomerFields(batchImportParam.getCustomer())));// [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
        
        } catch (Exception e) {
            log.error(e.getMessage()); //do something smart here
        } finally {
        	//send email logic to report Asynchronous Job Result
        }
        
    }

    private List<CustomField> getCustomerFields(Customer customer) {
    	List<CustomField> customFields = new ArrayList<CustomField>();
        if (CollectionUtils.isNotEmpty(customer.getCustomFields())) {
    		customFields.addAll(customer.getCustomFields());
        }
        if (CollectionUtils.isNotEmpty(customer.getDistributor().getCustomFields())) {
    		customFields.addAll(customer.getDistributor().getCustomFields());
        }
    	return customFields;
    }

	public BatchImportLearnersService getBatchImportService() {
		return batchImportService;
	}

	public void setBatchImportService(BatchImportLearnersService batchImportService) {
		this.batchImportService = batchImportService;
	}

	public void setBatchImportParam(BatchImportParam batchImportParam) {
		this.batchImportParam = batchImportParam;
	}
}
