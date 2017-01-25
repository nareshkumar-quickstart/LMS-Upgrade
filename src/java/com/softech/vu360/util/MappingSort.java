package com.softech.vu360.util;

import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.softech.vu360.lms.model.Mapping;

/**
 * Provides a collection containing BrandProperty to sort based on 
 * property name.
 * 
 * @author abdul.aziz
 */
public class MappingSort implements Comparator<Mapping> {
    
    private static final Log logger = LogFactory.getLog(MappingSort.class);
    
    /** Sort direction, e.g. 0 means ascending order 
     * and 1 mean descending order. */
    protected int sortDirection = -1;

    /**
     * Sort column index
     *  0 - key name
     *  1 - key value
     */
    private int sortColumnIndex = 0;

    /**
     * Set sort column index
     * @param sortColumnIndex sort column index
     */
    public void setSortColumnIndex(int sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}
    
    /**
     * 
     * @param mapping0
     * @param mapping1
     * @return 
     */
    public int compare(Mapping mapping0, Mapping mapping1) {
        if( sortDirection == -1) {
            throw new IllegalArgumentException(
                "Please provide valid sortDirection.");
        }
        
        logger.debug("Sort Column Index: " + sortColumnIndex);
        logger.debug("Sort direction: " + sortDirection);
        
        if( sortDirection == 0 ){
        	if(sortColumnIndex == 1)
                return mapping0.getValue().trim().toUpperCase().compareTo(
                        mapping1.getValue().trim().toUpperCase());
            return mapping0.getKey().trim().toUpperCase().compareTo(
                mapping1.getKey().trim().toUpperCase());
        }else{
        	if(sortColumnIndex == 1)
                return mapping1.getValue().trim().toUpperCase().compareTo(
                        mapping0.getValue().trim().toUpperCase());        		
            return mapping1.getKey().trim().toUpperCase().compareTo(
                mapping0.getKey().trim().toUpperCase());
        }
    }

    /**
     * @param sortDirection the sortDirection to set, can either be 0 or 1.
     */
    public void setSortDirection(int sortDirection) {
        if( sortDirection < 0){
            throw new IllegalArgumentException("Please provide 0 or 1.");
        }
        this.sortDirection = sortDirection;
    }
}
