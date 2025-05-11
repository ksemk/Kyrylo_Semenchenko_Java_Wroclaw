package com.ocadogroup.algorithm;

import com.ocadogroup.pojo.Order;
import com.ocadogroup.pojo.PaymentMethod;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PayWithCardTest {

    @Test
    void testCardDiscountsAppliedAfterBonus() {
        // Setup test data
        Order order1 = new Order("ORDER1", 100.0, Set.of("mZysk"));         // will get bonus
        Order order2 = new Order("ORDER2", 200.0, Set.of("BosBankrut"));   // will get BosBankrut
        Order order3 = new Order("ORDER3", 150.0, Set.of("mZysk"));        // will get mZysk
        Order order4 = new Order("ORDER4", 50.0, Collections.emptySet());  // will get bonus

        List<Order> orders = new ArrayList<>(List.of(order1, order2, order3, order4));
        List<PaymentMethod> methods = new ArrayList<>(List.of(
                new PaymentMethod("PUNKTY", 15, 100.0),
                new PaymentMethod("mZysk", 10, 200.0),
                new PaymentMethod("BosBankrut", 5, 300.0)
        ));

        try (MockedStatic<BackpackProblemSolver> mock = Mockito.mockStatic(BackpackProblemSolver.class)) {
            // Simulate bonus payment logic
            mock.when(() -> BackpackProblemSolver.selectOrdersToPay(Mockito.anyList(), Mockito.eq(100.0)))
                    .thenReturn(List.of(order1, order4));

            PayWithBonusPoints bonus = new PayWithBonusPoints(orders, methods);
            bonus.generateBestGrossDiscountList();

            // Simulate card payment logic
            mock.when(() -> BackpackProblemSolver.selectOrdersToPay(Mockito.anyList(), Mockito.eq(200.0)))
                    .thenReturn(List.of(order3)); // mZysk applies to order3

            mock.when(() -> BackpackProblemSolver.selectOrdersToPay(Mockito.anyList(), Mockito.eq(300.0)))
                    .thenReturn(List.of(order2)); // BosBankrut applies to order2

            PayWithCard card = new PayWithCard(orders, methods);
            Map<String, List<Order>> result = card.generateBestGrossDiscountListsById();

            // Simple checks
            assertEquals("PUNKTY", order1.getPaymentMethod());   // bonus stays
            assertEquals("PUNKTY", order4.getPaymentMethod());   // bonus stays
            assertEquals("BosBankrut", order2.getPaymentMethod());
            assertEquals("mZysk", order3.getPaymentMethod());
        }
    }
}
