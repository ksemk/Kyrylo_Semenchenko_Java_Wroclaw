//package com.ocadogroup.algorithm;
//
//import com.ocadogroup.pojo.Order;
//import com.ocadogroup.pojo.PaymentMethod;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class PayWithBonusPointsTest {
//
//    private List<Order> orders;
//    private List<PaymentMethod> paymentMethods;
//    private PayWithBonusPoints payWithBonusPoints;
//
//    @BeforeEach
//    void setup() {
//        orders = new ArrayList<>(List.of(
//                new Order("ORDER1", 100.0, Set.of("mZysk")),
//                new Order("ORDER2", 200.0, Set.of("BosBankrut")),
//                new Order("ORDER3", 150.0, Set.of("mZysk", "BosBankrut")),
//                new Order("ORDER4", 50.0, Collections.emptySet())
//        ));
//
//        paymentMethods = new ArrayList<>(List.of(
//                new PaymentMethod("PUNKTY", 15, 100.0),
//                new PaymentMethod("mZysk", 10, 180.0),
//                new PaymentMethod("BosBankrut", 5, 200.0)
//        ));
//
//        payWithBonusPoints = new PayWithBonusPoints(orders, paymentMethods);
//    }
//
//    @Test
//    void testGenerateBestGrossDiscountList_appliesDiscountsCorrectly() {
//        // We will mock the BackpackProblemSolver to return a specific subset of orders
//        List<Order> selectedOrders = List.of(orders.get(0), orders.get(3)); // ORDER1 and ORDER4
//
//        try (MockedStatic<BackpackProblemSolver> mocked = Mockito.mockStatic(BackpackProblemSolver.class)) {
//            mocked.when(() -> BackpackProblemSolver.selectOrdersToPay(Mockito.anyList(), Mockito.anyDouble()))
//                    .thenReturn(selectedOrders);
//
//            List<Order> result = payWithBonusPoints.generateBestGrossDiscountList();
//
//            assertNotNull(result);
//            assertEquals(2, result.size());
//
//            // Check discount and payment method applied
//            for (Order o : result) {
//                assertEquals(15, o.getCurrentDiscount());
//                assertEquals("PUNKTY", o.getPaymentMethod());
//            }
//
//            // Check untouched orders
//            for (Order o : orders) {
//                if (!selectedOrders.contains(o)) {
//                    assertNull(o.getPaymentMethod());
//                    assertEquals(0, o.getCurrentDiscount());
//                }
//            }
//
//            // Validate limit was decreased appropriately (100*0.15 + 50*0.15 = 22.5)
//            PaymentMethod bonusMethod = paymentMethods.stream()
//                    .filter(pm -> pm.getId().equals("PUNKTY"))
//                    .findFirst()
//                    .orElseThrow();
//            assertEquals(77.5, bonusMethod.getLimit(), 0.01);
//        }
//    }
//}
