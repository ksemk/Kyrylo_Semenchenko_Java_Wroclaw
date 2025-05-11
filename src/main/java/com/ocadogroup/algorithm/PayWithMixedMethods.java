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
        try {
            List<Order> unpaidOrders = new ArrayList<>();
            for (Order order : orders) {
                // Choose the ones that aren't paid yet, or the ones that discount could be increased
                if (order.getPaymentMethod() == null || order.getCurrentDiscount() <= 10) {
                    unpaidOrders.add(order);
                }
            }
            if (unpaidOrders.isEmpty()) {
                return null;
            }
            List<Order> bestGrossDiscountTenPercWithBon = BackpackProblemSolver.selectOrdersToPay(
                    unpaidOrders,
                    Objects.requireNonNull(getBonus()).getLimit() * 10
            );
            for (Order order : bestGrossDiscountTenPercWithBon) {
                for (PaymentMethod paymentMethod : paymentMethods) {
                    // Skip for Bonus
                    if (paymentMethod.getId().equals(PaymentMethod.getBonusId())) {
                        continue;
                    }
                    // 90% is paid with card, other 10% with bonus to get 10% discount
                    if ((0.9 * order.getValue() <= paymentMethod.getLimit()) && !order.getIsPayedMixed()) {
                        revertPreviousLimit(order);
                        order.setPaymentMethod(paymentMethod.getId());
                        order.setCardPayedPart(90);
                        order.setCurrentDiscount(10);


                        double bonusPayment = 0.1 * order.getValue();
                        double cardPayment = 0.9 * order.getValue() - bonusPayment;

                        paymentMethod.setLimit(paymentMethod.getLimit() - cardPayment);
                        getBonus().setLimit(getBonus().getLimit() - bonusPayment);
                        order.setIsPayedMixed(true);
                        output.put(paymentMethod.getId(), order);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in GenerateBestDiscountsForMixedPayment: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return output;
    }

    public HashMap<String, Order> remainPaymentsWithNoDiscounts() {
        HashMap<String, Order> remainPayments = new HashMap<>();
        try {
            List<PaymentMethod> biggestLimits = new ArrayList<>(paymentMethods);

            biggestLimits.sort((o1, o2) -> Double.compare(o2.getLimit(), o1.getLimit()));
            biggestLimits.removeIf(paymentMethod -> paymentMethod.getId().equals(PaymentMethod.getBonusId()));

            for (Order order : orders) {
                PaymentMethod currentBiggestLimit = biggestLimits.getFirst();
                // if it's enough money on the highest limit card to pay for the order
                if (order.getPaymentMethod() == null && currentBiggestLimit.getLimit() >= order.getValue()) {
                    order.setPaymentMethod(currentBiggestLimit.getId());
                    currentBiggestLimit.setLimit(currentBiggestLimit.getLimit() - order.getValue());
                    // if it's not enough money on the card and customer pays partly with bonuses
                } else if (order.getPaymentMethod() == null && (currentBiggestLimit.getLimit() + Objects.requireNonNull(getBonus()).getLimit() >= order.getValue())) {
                    order.setPaymentMethod(currentBiggestLimit.getId());
                    getBonus().setLimit(order.getValue() - currentBiggestLimit.getLimit());
                    currentBiggestLimit.setLimit(0.0);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            System.err.println("Error in remainPaymentsWithNoDiscounts: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return remainPayments;
    }

    private PaymentMethod getBonus() {
        for (PaymentMethod paymentMethod : paymentMethods) {
            if (paymentMethod.getId().equals(PaymentMethod.getBonusId())) {
                return paymentMethod;
            }
        }
        return null;
    }

    private void revertPreviousLimit(Order order) {
        try {
            if (order.getPaymentMethod() != null && !order.getIsPayedMixed()) {
                for (PaymentMethod paymentMethod : paymentMethods) {
                    if (paymentMethod.getId().equals(order.getPaymentMethod())) {
                        paymentMethod.setLimit(paymentMethod.getLimit() + (100 - order.getCurrentDiscount()) * order.getValue() * 0.01);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in revertPreviousLimit: " + e.getMessage());
            e.printStackTrace();
        }
    }
}