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
            if (paymentMethod.getId().equals("PUNKTY")) {
                return paymentMethod.getDiscount();
            }
        }
        return null;
    }

    public Double findBonusLimit(List<PaymentMethod> paymentMethods) {
        for (PaymentMethod paymentMethod : paymentMethods) {
            if (paymentMethod.getId().equals("PUNKTY")) {
                return paymentMethod.getLimit();
            }
        }
        return null;
    }

    public String FindMostExpensiveOrder(List<Order> orders, Double limit) {
        Order mostExpensive = null;
        double highestPrice = 0.0;
        double currentOrderValue;
        for (Order order : orders) {
            currentOrderValue = order.getValue();
            if (order.getPromotions() == null) {
                continue;
            }
            if (currentOrderValue > highestPrice && currentOrderValue <= limit) {
                highestPrice = currentOrderValue;
                mostExpensive = order;
            }
        }
        if (mostExpensive == null) {
            throw new NullPointerException("FindMostExpensiveOrder failed");
        } else {
            return mostExpensive.getId();
        }
    }

    public List<Order> generateBestGrossDiscountList() {
        List<Order> bestGrossDiscount = BackpackProblemSolver.selectOrdersToPay(orders, limit);
        for (Order order : orders) {
            if (bestGrossDiscount.contains(order)) {
                order.setCurrentDiscount(discountFullRate);
                order.setPaymentMethod("PUNKTY");
            }
        }
        changeLimit();
        return bestGrossDiscount;
    }

    private void changeLimit() {
        double limitMinus = 0;
        for (Order order : orders) {

            if (order.getPaymentMethod() != null && order.getPaymentMethod().equals("PUNKTY")) {
                limitMinus -= order.getValue() * discountFullRate * 0.01;
            }
            System.out.println(limit);
            limit += limitMinus;
        }
        for (PaymentMethod paymentMethod : paymentMethods) {
            if (paymentMethod.getId().equals("PUNKTY")) {
                paymentMethod.setLimit(paymentMethod.getLimit() + limitMinus);
            }
        }
    }
}

