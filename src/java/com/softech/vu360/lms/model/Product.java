package com.softech.vu360.lms.model;

/**
 * User: joong
 * Date: Nov 6, 2009
 */
public class Product implements SearchableKey {
    private ValueHolderInterface productCategory;
    private String name;
    private long commissionId;
    private long id;

    public Product() {
        //this.productCategory = new ValueHolder();
    }

    public long getCommissionId() {
        return this.commissionId;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public ProductCategory getProductCategory() {
        return (ProductCategory) this.productCategory.getValue();
    }

    protected ValueHolderInterface getProductCategoryHolder() {
        return this.productCategory;
    }

    public void setCommissionId(long commissionId) {
        this.commissionId = commissionId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory.setValue(productCategory);
    }

    protected void setProductCategoryHolder(ValueHolderInterface productCategory) {
        this.productCategory = productCategory;
    }

    public String getKey() {
        return String.valueOf(id);
    }
}
