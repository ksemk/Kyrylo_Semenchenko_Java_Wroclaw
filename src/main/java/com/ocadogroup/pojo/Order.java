package com.ocadogroup.pojo;

import java.util.Set;

public class Order {
    String id;
    Double value;
    Set<String> promotions;
    Integer currentDiscount;
    String paymentMethod;
    boolean isPayedMixed;

    public Order(String id, Double value, Set<String> promotions) {
        this.id = id;
        this.value = value;
        this.promotions = promotions;
        this.currentDiscount = 0;
        this.paymentMethod = null;
        this.isPayedMixed = false;
    }


    // Getters and Setters
    public void setIsPayedMixed(boolean isPayedMixed) {
        this.isPayedMixed = isPayedMixed;
    }

    public boolean getIsPayedMixed() {
        return isPayedMixed;
    }

    public Integer getCurrentDiscount() {
        return currentDiscount;
    }

    public void setCurrentDiscount(Integer currentDiscount) {
        this.currentDiscount = currentDiscount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Set<String> getPromotions() {
        return promotions;
    }

    public void setPromotions(Set<String> promotions) {
        this.promotions = promotions;
    }
}
