package com.ocadogroup.algorithm;

import com.ocadogroup.pojo.Order;
import com.ocadogroup.pojo.PaymentMethod;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PayWithMixedMethodsTest {

    @Test
    void testMixedPaymentsAppliedCorrectly() {
        // Set up orders
        Order order1 = new Order("ORDER1", 100.0, Set.of("mZysk"));        // bonus
        Order order2 = new Order("ORDER2", 200.0, Set.of("BosBankrut"));   // card
        Order order3 = new Order("ORDER3", 150.0, Set.of("mZysk"));        // eligible for mixed
        Order order4 = new Order("ORDER4", 50.0, Collections.emptySet());  // bonus

        List<Order> orders = new ArrayList<>(List.of(order1, order2, order3, order4));
        List<PaymentMethod> methods = new ArrayList<>(List.of(
                new PaymentMethod("PUNKTY", 15, 100.0),      // bonus
                new PaymentMethod("mZysk", 10, 200.0),        // card
                new PaymentMethod("BosBankrut", 5, 200.0)     // card
        ));

        try (MockedStatic<BackpackProblemSolver> mock = Mockito.mockStatic(BackpackProblemSolver.class)) {
            // Bonus points applied to ORDER1, ORDER4
            mock.when(() -> BackpackProblemSolver.selectOrdersToPay(Mockito.anyList(), Mockito.eq(100.0)))
                    .thenReturn(List.of(order1, order4));
            new PayWithBonusPoints(orders, methods).generateBestGrossDiscountList();

            // Card payment applied to ORDER2
            mock.when(() -> BackpackProblemSolver.selectOrdersToPay(Mockito.anyList(), Mockito.eq(200.0)))
                    .thenReturn(List.of(order2));
            new PayWithCard(orders, methods).generateBestGrossDiscountListsById();

            // Mixed payment applies only to ORDER3 (has discount ≤ 10)
            mock.when(() -> BackpackProblemSolver.selectOrdersToPay(Mockito.anyList(), Mockito.anyDouble()))
                    .thenReturn(List.of(order3));
            HashMap<String, Order> mixedResults =
                    new PayWithMixedMethods(orders, methods).GenerateBestDiscountsForMixedPayment();

            assertNotNull(mixedResults);
            assertTrue(mixedResults.containsValue(order3));
            assertEquals(10, order3.getCurrentDiscount());
            assertTrue(order3.getIsPayedMixed());
        }
    }
}
