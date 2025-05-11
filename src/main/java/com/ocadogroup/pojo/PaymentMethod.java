package com.ocadogroup.pojo;

public class PaymentMethod {
    private String id;
    private Integer discount;
    private Double limit;

    public PaymentMethod(String id, Integer discount, Double limit) {
        this.id = id;
        this.discount = discount;
        this.limit = limit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }
}
