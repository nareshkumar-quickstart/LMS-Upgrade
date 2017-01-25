package com.softech.vu360.lms.model;

/**
 * User: joong
 * Date: Nov 6, 2009
 */
public class ProductCategory implements SearchableKey {
    private ValueHolderInterface product;
    private long commissionId;
    private String name;
    private long id;

    public ProductCategory() {
        //this.product = new ValueHolder();
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

    public Product getProduct() {
        return (Product) this.product.getValue();
    }

    protected ValueHolderInterface getProductHolder() {
        return this.product;
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

    public void setProduct(Product product) {
        this.product.setValue(product);
    }

    protected void setProductHolder(ValueHolderInterface product) {
        this.product = product;
    }

    public String getKey() {
        return String.valueOf(id);
    }
}
