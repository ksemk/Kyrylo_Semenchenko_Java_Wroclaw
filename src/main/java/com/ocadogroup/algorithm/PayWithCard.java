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
        // for each payment method via card
        for (PaymentMethod paymentMethod : paymentMethods) {
            if (paymentMethod.getId().equals("PUNKTY")) {
                continue; // skip bonuses
            }
            // find supported cards
            List<Order> ordersListWithCurrentId = ordersWithCurrentId(paymentMethod.getId());
            if (ordersListWithCurrentId == null) {
                continue; // skip unsupported card ids
            }
            for (Order order : ordersListWithCurrentId) {
                // remove order from candidate list if its discount is less than bonus' discount or previous discount (if those exist)
                if (order.getCurrentDiscount() != null && order.getCurrentDiscount() < paymentMethod.getDiscount()) {
                    ordersListWithCurrentId.remove(order);
                }
            }
            // for supported card id
            List<Order> bestGrossDiscount = BackpackProblemSolver.selectOrdersToPay(ordersListWithCurrentId, paymentMethod.getLimit());
            double limitMinus = 0;
            int discountForThisMethod = paymentMethod.getDiscount();
            for (Order order : orders) {
                if (bestGrossDiscount.contains(order)) {
                    revertPreviousLimit(order);

                    order.setCurrentDiscount(paymentMethod.getDiscount());
                    order.setPaymentMethod(paymentMethod.getId());
                    limitMinus -= order.getValue() * discountForThisMethod * 0.01;
                }
            }
            bestGrossDiscountListsById.put(paymentMethod.getId(), bestGrossDiscount);
            paymentMethod.setLimit(paymentMethod.getLimit() + limitMinus);
        }
        return bestGrossDiscountListsById;
    }

    private void revertPreviousLimit(Order order) {
        if (order.getPaymentMethod() != null) {
            for (PaymentMethod paymentMethod1 : paymentMethods) {
                if (paymentMethod1.getId().equals(order.getPaymentMethod())) {
                    paymentMethod1.setLimit(paymentMethod1.getLimit() / order.getValue() / (1 - order.getCurrentDiscount() * 0.01));
                }
            }
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


