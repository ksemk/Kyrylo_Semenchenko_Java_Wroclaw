package com.ocadogroup.algorithm;

import com.ocadogroup.pojo.Order;
import com.ocadogroup.pojo.PaymentMethod;

import java.util.List;

public class PayWithBonusPoints {

    private List<Order> orders;
    private List<PaymentMethod> paymentMethods;
    private Double limit;
    private Integer discountFullRate; // discount rate in case of full payment made with bonuses

    public PayWithBonusPoints(List<Order> orders, List<PaymentMethod> paymentMethods) {
        this.orders = orders;
        this.paymentMethods = paymentMethods;
        this.limit = findBonusLimit(paymentMethods);
        this.discountFullRate = findDiscountFullRate(paymentMethods);
    }

    public Integer findDiscountFullRate(List<PaymentMethod> paymentMethods) {
        for (PaymentMethod paymentMethod : paymentMethods) {
            if (paymentMethod.getId().equals(PaymentMethod.getBonusId())) {
                return paymentMethod.getDiscount();
            }
        }
        return null;
    }

    public Double findBonusLimit(List<PaymentMethod> paymentMethods) {
        for (PaymentMethod paymentMethod : paymentMethods) {
            if (paymentMethod.getId().equals(PaymentMethod.getBonusId())) {
                return paymentMethod.getLimit();
            }
        }
        return null;
    }

    public List<Order> generateBestGrossDiscountList() {
            List<Order> bestGrossDiscount = BackpackProblemSolver.selectOrdersToPay(orders, limit);
        try {
            for (Order order : orders) {
                if (bestGrossDiscount.contains(order)) {
                    order.setCurrentDiscount(discountFullRate);
                    order.setPaymentMethod(PaymentMethod.getBonusId());
                }
            }
            changeLimit();
        }
        catch (Exception e){
            System.err.println("Error in generateBestGrossDiscountList: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return bestGrossDiscount;
    }

    private void changeLimit() {
        try {
            double limitMinus = 0;
            for (Order order : orders) {

                if (order.getPaymentMethod() != null && order.getPaymentMethod().equals(PaymentMethod.getBonusId())) {
                    limitMinus -= order.getValue() * (100 - discountFullRate) * 0.01;
                }
                limit += limitMinus;
            }
            for (PaymentMethod paymentMethod : paymentMethods) {
                if (paymentMethod.getId().equals(PaymentMethod.getBonusId())) {
                    paymentMethod.setLimit(paymentMethod.getLimit() + limitMinus);
                }
            }
        }
        catch(Exception e){
            System.err.println("Error in changeLimit: " + e.getMessage());
            e.printStackTrace();
        }
    }
}