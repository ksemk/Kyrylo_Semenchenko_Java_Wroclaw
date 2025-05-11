package com.ocadogroup.algorithm;

import com.ocadogroup.pojo.Order;
import com.ocadogroup.pojo.PaymentMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PayWithMixedMethods {
    private List<Order> orders;
    private List<PaymentMethod> paymentMethods;


    public PayWithMixedMethods(List<Order> orders, List<PaymentMethod> paymentMethods) {
        this.orders = orders;
        this.paymentMethods = paymentMethods;
    }

    public HashMap<String, Order> GenerateBestDiscountsForMixedPayment() {
        HashMap<String, Order> output = new HashMap<>();
        List<Order> unpaidOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getPaymentMethod() == null || order.getCurrentDiscount() <= 10) {
                unpaidOrders.add(order);
            }
        }
        if (unpaidOrders.isEmpty()) {
            return null;
        }
//        unpaidOrders.sort((o1, o2) -> Double.compare(o2.getValue(), o1.getValue()));

        List<Order> bestGrossDiscountTenPercWithBon = BackpackProblemSolver.selectOrdersToPay(unpaidOrders, Objects.requireNonNull(getBonus()).getLimit() * 10);
        double payedWithCard = 0.0;
        double payedWithBonuses = 0.0;
        for (Order order : bestGrossDiscountTenPercWithBon) {
            for (PaymentMethod paymentMethod : paymentMethods) {
                if (paymentMethod.getId().equals("PUNKTY")) {
                    continue;
                }
                // 90% is payed with card, other 10% with bonus to get 10% discount
                if ((0.9 * order.getValue() <= paymentMethod.getLimit()) && !order.getIsPayedMixed()) {
                    revertPreviousLimit(order);
                    order.setPaymentMethod(paymentMethod.getId());
                    order.setCardPayedPart(90);
                    order.setCurrentDiscount(10);
                    paymentMethod.setLimit(paymentMethod.getLimit() - (0.9 * order.getValue() - 0.1 * order.getValue()));
                    getBonus().setLimit(paymentMethod.getLimit() - 0.1 * order.getValue());
                    order.setIsPayedMixed(true);
                    output.put(paymentMethod.getId(), order);
                    break;
                }
            }
        }

        return output;
    }

    public HashMap<String, Order> remainPaymentsWithNoDiscounts() {
        HashMap<String, Order> remainPayments = new HashMap<>();
        List<PaymentMethod> biggestLimits = paymentMethods;

        biggestLimits.sort((o1, o2) -> Double.compare(o2.getLimit(), o1.getLimit()));
        biggestLimits.removeIf(paymentMethod -> paymentMethod.getId().equals("PUNKTY"));

        for (Order order : orders) {
            PaymentMethod currentBiggestLimit = biggestLimits.getFirst();
            // if it's enough money on the highest limit card to pay for the order
            if (order.getPaymentMethod() == null && currentBiggestLimit.getLimit() >= order.getValue()) {
                order.setPaymentMethod(currentBiggestLimit.getId());
                currentBiggestLimit.setLimit(order.getValue());
                // if its not enough money on the card and customer pays partly with bonuses
            } else if (order.getPaymentMethod() == null && (currentBiggestLimit.getLimit() + Objects.requireNonNull(getBonus()).getLimit() >= order.getValue())) {
                order.setPaymentMethod(currentBiggestLimit.getId());
                getBonus().setLimit(order.getValue() - currentBiggestLimit.getLimit());
                currentBiggestLimit.setLimit(0.0);
            } else {
                return null;
            }
        }
        return remainPayments;
    }

    private PaymentMethod getBonus() {
        for (PaymentMethod paymentMethod : paymentMethods) {
            if (paymentMethod.getId().equals("PUNKTY")) {
                return paymentMethod;
            }
        }
        return null;
    }

    private void revertPreviousLimit(Order order) {
        if (order.getPaymentMethod() != null) {
            for (PaymentMethod paymentMethod : paymentMethods) {
                if (paymentMethod.getId().equals(order.getPaymentMethod())) {
                    paymentMethod.setLimit(paymentMethod.getLimit() / order.getValue() / (1 - order.getCurrentDiscount() * 0.01));
                }
            }
        }
    }
}


