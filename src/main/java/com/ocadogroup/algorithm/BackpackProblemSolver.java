package com.ocadogroup.algorithm;

import com.ocadogroup.pojo.Order;

import java.util.ArrayList;
import java.util.List;

public class BackpackProblemSolver {

    private static final int SCALE = 100; // Convert 0.01 units to integers

    protected static List<Order> selectOrdersToPay(List<Order> orders, double balance) {
        int n = orders.size();
        int scaledBalance = (int) Math.round(balance * SCALE);

        int[][] dp = new int[n + 1][scaledBalance + 1];

        // Fill DP table
        for (int i = 1; i <= n; i++) {
            int scaledValue = (int) Math.round(orders.get(i - 1).getValue() * SCALE);
            for (int w = 0; w <= scaledBalance; w++) {
                if (scaledValue <= w) {
                    dp[i][w] = Math.max(scaledValue + dp[i - 1][w - scaledValue], dp[i - 1][w]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        // Backtrack to find selected orders
        List<Order> selected = new ArrayList<>();
        int w = scaledBalance;
        for (int i = n; i > 0 && w > 0; i--) {
            int scaledValue = (int) Math.round(orders.get(i - 1).getValue() * SCALE);
            if (dp[i][w] != dp[i - 1][w]) {
                selected.add(orders.get(i - 1));
                w -= scaledValue;
            }
        }
        return selected;
    }
}