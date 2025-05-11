package com.ocadogroup;

import com.ocadogroup.algorithm.PayWithBonusPoints;
import com.ocadogroup.algorithm.PayWithCard;
import com.ocadogroup.algorithm.PayWithMixedMethods;
import com.ocadogroup.pojo.Order;
import com.ocadogroup.pojo.PaymentMethod;
import com.ocadogroup.utils.JsonUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class Application {
    public static void main(String[] args) throws IOException {
        Path ordersPath = Path.of(args[0]);
        Path paymentMethodsPath = Path.of(args[1]);

        List<Order> orders = JsonUtils.parseOrders(ordersPath);
        List<PaymentMethod> paymentMethods = JsonUtils.parsePaymentMethods(paymentMethodsPath);

        PayWithBonusPoints payWithBonusPoints = new PayWithBonusPoints(orders, paymentMethods);
        List<Order> bonuses = payWithBonusPoints.generateBestGrossDiscountList();
        for (Order order : bonuses) {
            System.out.println("Discount with bonus:" + order.getCurrentDiscount());
        }
        PayWithCard payWithCard = new PayWithCard(orders, paymentMethods);
        HashMap<String, List<Order>> cards = payWithCard.generateBestGrossDiscountListsById();
//        for (List<Order> value : cards.values()) {
//            for (Order order : value) {
//                for (PaymentMethod paymentMethod : paymentMethods) {
//                    if (paymentMethod.getId().equals(order.getPaymentMethod())) {
//                        System.out.println(order.getPaymentMethod() + " " + paymentMethod.getLimit());
//                    }
//                }
//            }
//        }
        for (PaymentMethod paymentMethod : paymentMethods){
            System.out.println(paymentMethod.getId() + " " + paymentMethod.getLimit());
        }
        PayWithMixedMethods payWithMixedMethods = new PayWithMixedMethods(orders, paymentMethods);
        payWithMixedMethods.GenerateBestDiscountsForMixedPayment();
        for (PaymentMethod paymentMethod : paymentMethods){
            System.out.println(paymentMethod.getId() + " " + paymentMethod.getLimit());
        }
    }
}
