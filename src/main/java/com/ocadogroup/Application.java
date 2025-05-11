package com.ocadogroup;

import com.ocadogroup.algorithm.PayWithBonusPoints;
import com.ocadogroup.algorithm.PayWithCard;
import com.ocadogroup.algorithm.PayWithMixedMethods;
import com.ocadogroup.pojo.Order;
import com.ocadogroup.pojo.PaymentMethod;
import com.ocadogroup.utils.JsonUtils;
import com.ocadogroup.utils.OutputUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Application {
    public static void main(String[] args) throws IOException {
        Path ordersPath = Path.of(args[0]);
        Path paymentMethodsPath = Path.of(args[1]);

        List<Order> orders = JsonUtils.parseOrders(ordersPath);
        List<PaymentMethod> paymentMethods = JsonUtils.parsePaymentMethods(paymentMethodsPath);

        // First try to pay full price with bonuses, as it possibly has better discounts
        PayWithBonusPoints payWithBonusPoints = new PayWithBonusPoints(orders, paymentMethods);
        payWithBonusPoints.generateBestGrossDiscountList();

        // Secondly try to pay full price with traditional payment method, as possibly not all orders were payed with bonuses,
        // or partner company offers better discount than bonus discount
        PayWithCard payWithCard = new PayWithCard(orders, paymentMethods);
        payWithCard.generateBestGrossDiscountListsById();

        // Thirdly pay the remain orders using mixed methods, but try to spilt bonuses, so they would cover at least 10% of order value,
        // gaining better discount
        PayWithMixedMethods payWithMixedMethods = new PayWithMixedMethods(orders, paymentMethods);
        payWithMixedMethods.GenerateBestDiscountsForMixedPayment();

        // And finally, if there is no bank account, pay partly with bonuses
        payWithMixedMethods.remainPaymentsWithNoDiscounts();
        OutputUtils.printResultToStdStream(paymentMethods);

    }
}
