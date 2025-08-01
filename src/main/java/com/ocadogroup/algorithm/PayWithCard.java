package com.ocadogroup.algorithm;

import com.ocadogroup.pojo.Order;
import com.ocadogroup.pojo.PaymentMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PayWithCard {
    private List<Order> orders;
    private List<PaymentMethod> paymentMethods;

    public PayWithCard(List<Order> orders, List<PaymentMethod> paymentMethods) {
        this.orders = orders;
        this.paymentMethods = paymentMethods;
    }

    public HashMap<String, List<Order>> generateBestGrossDiscountListsById() {

        HashMap<String, List<Order>> bestGrossDiscountListsById = new HashMap<>();
        try {
            // for each payment method via card
            for (PaymentMethod paymentMethod : paymentMethods) {
                if (paymentMethod.getId().equals(PaymentMethod.getBonusId())) {
                    continue; // skip bonuses
                }
                // find supported cards
                List<Order> ordersListWithCurrentId = ordersWithCurrentId(paymentMethod.getId());
                if (ordersListWithCurrentId == null) {
                    continue; // skip unsupported card ids
                }
                // remove order from candidate list if its discount is less than bonus' discount or previous discount (if those exist)
                ordersListWithCurrentId.removeIf(order -> order.getCurrentDiscount() != null && order.getCurrentDiscount() < paymentMethod.getDiscount());
                // for supported card id
                List<Order> bestGrossDiscount = BackpackProblemSolver.selectOrdersToPay(ordersListWithCurrentId, paymentMethod.getLimit());
                double limitMinus = 0;
                int discountForThisMethod = paymentMethod.getDiscount();
                for (Order order : orders) {
                    if (bestGrossDiscount.contains(order)) {
                        revertPreviousLimit(order);

                        order.setCurrentDiscount(paymentMethod.getDiscount());
                        order.setPaymentMethod(paymentMethod.getId());
                        limitMinus += order.getValue() * (100 - discountForThisMethod) * 0.01;
                    }
                }
                bestGrossDiscountListsById.put(paymentMethod.getId(), bestGrossDiscount);
                paymentMethod.setLimit(paymentMethod.getLimit() - limitMinus);
            }
        }
        catch (Exception e){
            System.err.println("Error in generateBestGrossDiscountListsById: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return bestGrossDiscountListsById;
    }

    private void revertPreviousLimit(Order order) {
        try {
            if (order.getPaymentMethod() != null && !order.getIsPayedMixed()) {
                for (PaymentMethod paymentMethod : paymentMethods) {
                    if (paymentMethod.getId().equals(order.getPaymentMethod())) {
                        // newLimit = oldLimit - (value - value * discount)
                        // oldLimit = newLimit + value(1 - discount)
                        paymentMethod.setLimit(paymentMethod.getLimit() + (100 - order.getCurrentDiscount()) * order.getValue() * 0.01);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in revertPreviousLimit: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Order> ordersWithCurrentId(String id) {
        List<Order> output = new ArrayList<>();
        for (Order order : orders) {
            if (order.getPromotions() != null && order.getPromotions().contains(id)) {
                output.add(order);
            }
        }
        if (output.isEmpty()) {
            return null;
        }
        return output;
    }
}