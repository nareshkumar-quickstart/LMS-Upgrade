package com.softech.vu360.lms.util;

/**
 * User: joong
 * Date: Nov 7, 2009
 * @since LMS-3407 Commission
 */
public enum CommissionType {
    COMMISSION_MODEL("Commission Model"),
    WHOLESALE_MODEL("Wholesale Model");

    private final String label;

    private CommissionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
